package com.udfv.udfapi;

import java.lang.*;
import java.io.*;
import com.udfv.core.*;
import com.udfv.ecma167.*;
import com.udfv.exception.*;

/**
   最後に割り付けをした領域の後方に割り付ける。

   VATの追記用。
*/
public class UDF_AllocPolicyVAT implements UDF_AllocPolicy {
    /**
       空き領域を指定のサイズだけ探して割り当てる。
       割り当てた範囲のビットはOFF（使用済み）になる。

       @param image	UDF_Image。
       @param size	確保したいサイズ。
       @param rettype	戻値ADタイプ(0:short_ad, 1:long_ad, 2:ext_ad)

       @return 確保した空き領域のADのList。割り当てられないときは NotAllocExceptionを投げる
    */
    public UDF_ADList alloc(UDF_Image image, long size, int rettype) throws UDF_Exception, IOException {
	return null;
    }

    /**
       AD配列で記述されている領域を解放する。

       @param	image	UDF_Image。
       @param	ad	解放する領域。
     */
    public void free(UDF_Image image, UDF_ADList ad) throws UDF_Exception,IOException{
	;
    }
}
