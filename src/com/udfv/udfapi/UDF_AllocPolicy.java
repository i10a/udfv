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
import com.udfv.core.*;
import com.udfv.ecma167.*;
import com.udfv.exception.*;

/**
   領域を割り当てるためのポリシーを記述するインタフェース。

<p>       
このメソッドはイメージ中に sizeバイトの領域を割り当てるメソッドの割り当てルールを容易に変更するため用意されたインターフェースメソッドである。このインタフェースを備えたクラスを実装すれば、任意のルールに乗っ取った領域確保が可能となる。
</p>
*/
public interface UDF_AllocPolicy{

    /**
       領域を割り当てるためのポリシーを記述するインタフェース。

       @param image	UDF_Image。
       @param size	確保したいサイズ。
       @param rettype	戻値ADタイプ(0:short_ad, 1:long_ad, 2:ext_ad)

       @return 確保した空き領域のADの配列。割り当てられないときは UDF_NotAllocExceptionを投げる。

イメージのパーティション番号中に sizeの領域を確保しようとした際、確保すべき領域リストを返すメソッド。

    */
    public UDF_ADList alloc(UDF_Image image, long size, int rettype) throws UDF_Exception,IOException;

    /**
       領域を解放するためのポリシーを記述するインターフェース

       @param	image	UDF_Image。
       @param	ad	解放する領域。
     */
    public void free(UDF_Image image, UDF_ADList ad) throws UDF_Exception,IOException;
}


