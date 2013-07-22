package com.udfv.udfapi;

import java.lang.*;
import java.io.*;

import com.udfv.core.*;
import com.udfv.ecma167.*;
import com.udfv.exception.*;

/**
   指定の Partition のSpace Bitmapの指定のlbn 以前セクタから
   指定のサイズの連続している空き領域を探して割り当て、開放するクラス。

*/
public class UDF_AllocPolicyContinuousBackward extends UDF_AllocPolicyImpl {

    public UDF_AllocPolicyContinuousBackward(){
        this(-1, 0, 1);
    }
    public UDF_AllocPolicyContinuousBackward(int lbn, int partno){
        this(lbn, partno, 1);
    }
    /**
      コンストラクタ。

      @param lbn    領域の確保の際にlbn指定が省略された場合に使用される値
      @param partno  領域の確保の際にpartno指定が省略された場合に使用される値
      @param align  領域の確保の際にalign指定が省略された場合に使用される値
    */
    public UDF_AllocPolicyContinuousBackward(int lbn, int partno, int align){
        super(lbn, partno, align);
    }

    /**
       指定の Partition のSpace Bitmapの指定のlbn 以前のセクタから指定のサイズの連続の空き領域をだけ探して割り当てる。

       割り当てた範囲のビットはOFF（使用済み）になる。

       @param image	UDF_Image。
       @param lbn	このlbn以降の領域から確保する。
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

        int sec = (int)((size + image.env.LBS - 1) / image.env.LBS);
/*
        //　空き領域が不足していたらエラー　//
        if (sec > bm.countBitOn()) {
            throw new UDF_NotAllocException(image, "Bad alloc.");
        }
*/
        if (lbn < 0) {
            lbn = bm.getBitSize() - 1;
        }

        //　ビットマップのサイズ上限まで達してしまったらエラー　//
        if (lbn < 0) {
            throw new UDF_InternalException(image, "Bad alloc.");
        }

        //　空き領域を検索　//
        try {
            lbn = bm.searchBackBitOn(lbn, default_align, sec);
        }
        catch(UDF_InternalException e) {
            throw new UDF_InternalException(image, "Bad alloc.");
        }

        //　空き領域をADに変換して UDF_ADList に追加　//
        al.addADFromLbn(base_ad, lbn, size);

        //　対応するビットマップを使用済みに　//
        bm.unsetFromADs(al);

        return al;
    }

}
