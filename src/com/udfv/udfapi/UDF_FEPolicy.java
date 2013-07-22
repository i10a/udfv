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
ファイルエントリを作成する際のポリシーを記述するインターフェース。

<p>       
このメソッドはイメージ中にファイルエントリを生成しようとした際、どのように生成するかを記述するインターフェースである。このインタフェースを備えたクラスを実装すれば、任意のタイプのFEの作成が可能となる。
</p>
*/
public interface UDF_FEPolicy{
    /**
       領域を割り当てるためのポリシーを記述するインタフェース。

       @param image	UDF_Image
       @param policy	ポリシー
       @param icbtype	ICBタイプ

イメージのパーティション番号中にFEを生成しようとする際、FEを生成するメソッド。
    */
    public UDF_FEDesc create(UDF_Image image, UDF_Policy policy, int icbtype) throws UDF_Exception,IOException;
}
