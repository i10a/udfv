package com.udfv.udfapi;

import java.lang.*;
import java.io.*;

import com.udfv.core.*;
import com.udfv.ecma167.*;
import com.udfv.exception.*;

/**
   指定の Partition のSpace Bitmapの指定のlbn 以前からの空き領域を
   指定のサイズだけ探して割り当て、開放するクラス。

*/
public class UDF_AllocPolicyBackward extends UDF_AllocPolicyImpl {

    public UDF_AllocPolicyBackward(){
        this(-1, 0, 1);
    }
    public UDF_AllocPolicyBackward(int lbn, int partno){
        this(lbn, partno, 1);
    }
    /**
      コンストラクタ。

      @param lbn    領域の確保の際にlbn指定が省略された場合に使用される値
      @param partno  領域の確保の際にpartno指定が省略された場合に使用される値
      @param align  領域の確保の際にalign指定が省略された場合に使用される値
    */
    public UDF_AllocPolicyBackward(int lbn, int partno, int align){
        super(lbn, partno, align);
    }

    /**
       指定の Partition のSpace Bitmapの指定のlbn 以前からの空き領域を指定のサイズだけ探して割り当てる。

       割り当てた範囲のビットはOFF（使用済み）になる。

       @param image	UDF_Image。
       @param lbn	このlbn以前の領域から確保する。
       @param partno	パーティション番号。
       @param size	確保したいサイズ。
       @param rettype	戻値ADタイプ(0:short_ad, 1:long_ad, 2:ext_ad)

       @return 確保した空き領域のADの配列。割り当てられないときは NotAllocExceptionを投げる

       サイズに0を指定した場合、length=0、LBN=0のADが1つ作成される。但し、partno だけは正しく入る。
    */
    protected UDF_ADList alloc(UDF_Image image, int lbn, int partno, long size, int rettype) throws UDF_Exception, IOException {

        UDF_AD base_ad = image.createAllocDesc(rettype);
        base_ad.setPartRefNo(partno);

        UDF_ADList al = new UDF_ADList();

        // size 0でも作れないといけない(重要)
        if(size == 0){
            al.add(base_ad);
            return al;
        }

        UDF_bitmap bm = image.env.getPartMapBitmap(partno);

        if (bm == null) {
            throw new UDF_InternalException(image, "No Space Bitmap");
        }

        long bsize = image.env.LBS;
        int sec = (int)((size + bsize - 1) / bsize);
/*
        //　空き領域が不足していたらエラー　//
        if (sec > bm.countBitOn()) {
            throw new UDF_NotAllocException(image, "Bad alloc.");
        }
*/
        int bm_max = bm.getBitSize();

        if (lbn < 0) {
            lbn = bm_max - 1;
        }

        while(size > 0) {

            int start, end;
            int len;

            //　ビットマップのサイズ上限まで達してしまったらエラー　//
            if (lbn < 0) {
                bm.setFromADs(al);  //　埋めてしまった領域を開放する　//
                throw new UDF_InternalException(image, "Bad alloc.");
            }

            //　空き領域を検索　//
            try {
                end = bm.searchBackBitOn(lbn); //　現在地点から空き領域を前方に向けて検索　//
            }
            catch(UDF_InternalException e) {
                //　開いているセクタが無かったら領域を確保するのは無理　//
                bm.setFromADs(al);  //　埋めてしまった領域を開放する　//
                throw new UDF_InternalException(image, "Bad alloc.");
            }
            //　使用可能な領域から使用済みの領域を前方に向けて検索　//
            try {
                start = 1 + bm.searchBackBitOff(end - 1);
            }
            catch(UDF_InternalException e ) {
                //　使用済みの領域がなければ先頭が空き領域の始まり//
                start = 0;
            }
            end++;

            //　開始セクタの境界を合わせる　//
            int n2 = com.udfv.util.UDF_Util.align(start, default_align);

            //　空き領域のサイズを計算。境界を合わせたことによって　//
            //　開始セクタが終端セクタを超えてしまうこともあるが、ここではじく　//
            len = end - n2;
            if (len < 1) {
                if (n2 == start) {
                    lbn = start - default_align;
                }
                else {
                    start -= start % default_align;
                    lbn = start;
                }
                continue;
            }

            start = n2;

            long ssize = len * bsize;
            if (ssize > size) {
                ssize = size;
                len = (int)((ssize + bsize - 1) / bsize);
                start = end - len;
            }

            //　空き領域をADに変換して UDF_ADList に追加　//
            al.addADFromLbn(base_ad, start, ssize);

            //　対応するビットマップを使用済みに※ここでしないと続く検索に影響を与えてしまう　//
            bm.unset(start, len);

            sec -= len;
            size -= ssize;

            //　次に探す先頭位置を　//
            lbn = start - 1;
            lbn -= lbn % default_align;
        }

        return al;
    }

}
