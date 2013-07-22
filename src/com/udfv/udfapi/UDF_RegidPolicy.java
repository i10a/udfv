
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
