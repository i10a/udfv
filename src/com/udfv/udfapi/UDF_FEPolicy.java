
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
