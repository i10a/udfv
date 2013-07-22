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
regidを作成する際のポリシーを記述するインターフェース。

<p>       
このメソッドはイメージ中にファイルエントリを生成しようとした際、どのように生成するかを記述するインターフェースである。このインタフェースを備えたクラスを実装すれば、任意のタイプのregidの作成が可能となる。
</p>
*/
public interface UDF_RegidPolicy{
    /**
       "*Application ID"を生成する。Suffix type は ImplIdSuffixとなる。

       @param image	イメージ
       @param name	タグ名。nullのときは UDF_regidで生成される。
       @return regid
     */
    public UDF_regid createApplicationId(UDF_Image image, String name) throws UDF_Exception,IOException;
    /**
       "*Developer ID"を生成する。Suffix type は ApplicationIdSuffixとなる。

       @param image	イメージ
       @param name	タグ名。nullのときは UDF_regidで生成される。
       @return regid
     */
    public UDF_regid createDeveloperId(UDF_Image image, String name) throws UDF_Exception,IOException;

    /**
       IDValueを指定して regidを生成する。Suffix Typeは ImplIdSuffixとなる。

       @param image	イメージ
       @param idval	識別子
       @param name	タグ名。nullのときは UDF_regidで生成される。
       @return regid
     */
    public UDF_regid createImplId(UDF_Image image, String idval, String name) throws UDF_Exception,IOException;
    /**
       IDValueを指定して regidを生成する。Suffix Typeは UDFIdSuffixとなる。

       @param image	イメージ
       @param idval	識別子
       @param name	タグ名。nullのときは UDF_regidで生成される。
       @return regid
     */
    public UDF_regid createUDFId(UDF_Image image, String idval, String name) throws UDF_Exception,IOException;
    /**
       "*OSTA UDF Compliant"を生成する。Suffix Typeは DomainIdSuffixとなる。

       @param image	イメージ
       @param name	タグ名。nullのときは UDF_regidで生成される。
       @return regid
     */
    public UDF_regid createDomainId(UDF_Image image, String name) throws UDF_Exception,IOException;
    /**
       IDValueを指定して regidを生成する。suffix Typeは ApplicationIdSuffixとなる。

       @param image	イメージ
       @param idval	識別子
       @param name	タグ名。nullのときは UDF_regidで生成される。
       @return regid
     */
    public UDF_regid createApplicationId(UDF_Image image, String idval, String name) throws UDF_Exception,IOException;
}
