/*
   Copyright 2013 Heart Solutions Inc.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package com.udfv.udfapi;

import java.lang.*;
import java.io.*;

//import java.util.ArrayList;

import com.udfv.core.*;
import com.udfv.ecma167.*;
import com.udfv.exception.*;

/**
   指定の Partition のSpace Bitmapの指定のlbn 以降の空き領域を
   指定のサイズだけ探して割り当て、開放するクラス。

*/
public class UDF_AllocPolicyImpl implements UDF_AllocPolicy {
    protected int default_lbn;
    protected int default_partno;
    protected int default_align;

    public UDF_AllocPolicyImpl(){
        this(0, 0, 1);
    }
    public UDF_AllocPolicyImpl(int lbn, int partno){
        this(lbn, partno, 1);
    }
    public UDF_AllocPolicyImpl(int lbn, int partno, int align){
        default_lbn = lbn;
        default_partno = partno;
        default_align = align;
    }

    public void setDefaultAlign(int align){
        default_align = align;
    }
    public void setDefaultPartno(int partno){
        default_partno = partno;
    }
    public void setDefaultLbn(int lbn){
        default_lbn = lbn;
    }

    /**
       空き領域を指定のサイズだけ探して割り当てる。
       割り当てた範囲のビットはOFF（使用済み）になる。

       @param image	UDF_Image。
       @param size	確保したいサイズ。
       @param rettype	戻値ADタイプ(0:short_ad, 1:long_ad, 2:ext_ad)

       @return 確保した空き領域のADのList。割り当てられないときは NotAllocExceptionを投げる
    */
    public UDF_ADList alloc(UDF_Image image, long size, int rettype) throws UDF_Exception, IOException {
	return alloc(image, default_lbn, default_partno, size, rettype);
    }

    /**
       AD配列で記述されている領域を解放する。

       @param	image	UDF_Image。
       @param	ad	解放する領域。
     */
    public void free(UDF_Image image, UDF_ADList ad) throws UDF_Exception,IOException{
	free(image, default_partno, ad);
    }

    /**
       指定の Partition のSpace Bitmapの指定のlbn 以降の空き領域を指定のサイズだけ探して割り当てる。

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

        String ad_type;
        if (rettype == 2) {
            ad_type = "UDF_ext_ad";
        }
        else
        if (rettype == 1) {
            ad_type = "UDF_long_ad";
        }
        else
        if (rettype == 0) {
            ad_type = "UDF_short_ad";
        }
        else {
            throw new UDF_InternalException(image, "UDF_AllocPolicyImpl#alloc() : can not allocate : type = " + rettype);
        }

	UDF_ADList al = new UDF_ADList();
	// size 0でも作れないといけない(重要)
	if(size == 0){
            UDF_AD ad = (UDF_AD) image.createElement(ad_type, null, null);
            ad.setDefaultValue();
            ad.setPartRefNo(partno);

	    al.add(ad);
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
            //return null;
	    throw new UDF_NotAllocException(image, "Bad alloc.");
        }
*/
        //ArrayList al = new ArrayList();
        int bm_max = bm.getBitSize();


        while(size > 0) {

            int len;

            //　ビットマップのサイズ上限まで達してしまったらエラー　//
            if (lbn >= bm_max) {
                throw new UDF_InternalException(image, "Bad alloc.");
            }

            //　空き領域を検索　//
            lbn = bm.searchBitOn(lbn, default_align);
            try {
                len = bm.searchBitOff(lbn + 1, default_align) - lbn;
            }
            catch(UDF_InternalException e) {
                len = bm_max - lbn;
            }

            //　Extent Length に書き込める最大サイズに調整　//
            len = (len > sec) ? sec: len;
            if (len > 0x0007ffff) {
                len = 0x0007ffff;
            }

            UDF_AD ad;

            ad = (UDF_AD) image.createElement(ad_type, "", null);
            ad.setDefaultValue();

            ad.setPartRefNo(partno);
            ad.setLbn(lbn);

            lbn += len;
            sec -= len;

            len *= image.env.LBS;
            size -= len;

            if (size < 0) {
                len += size;
                size = 0;
            }
            ad.setLen(len);
            al.add(ad);
        }
	/*
        UDF_AD [] adlist = new UDF_AD[al.size()];
        for (int i = 0, max = adlist.length; i < max; i++) {
            adlist[i] = (UDF_AD) al.get(i);
        }
	*/

        //　対応するビットマップを使用済みに　//
        bm.unsetFromADs(al);

        return al;
    }

    /**
       AD配列で記述されている領域を解放する。

       ADに含まれる partno は無視され、引数の partno に対して処理が行われる。

       @param	image	UDF_Image。
       @param	partno	パーティション番号(short_adには partnoがないため)
       @param	ad	解放する領域。
     */
    protected void free(UDF_Image image, int partno, UDF_ADList ad) throws UDF_Exception,IOException{

        UDF_bitmap bm = image.env.getPartMapBitmap(partno);
	bm.setFromADs(ad);
    }
}
