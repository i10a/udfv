
package com.udfv.udfapi;

import java.lang.*;
import java.io.*;
import com.udfv.core.*;
import com.udfv.ecma167.*;
import com.udfv.exception.*;

/**
  ファイルエントリを作成する際のポリシーを記述するインターフェース。
  <p>
  このメソッドはイメージ中にファイルエントリを生成しようとした際、どのように生成するかを記述するインターフェースである。このインタフェースを備えたクラスを実装すれば、任意のタイプのFIDの作成が可能となる。
  </p>
*/
public interface UDF_FIDPolicy{
    /**
       領域を割り当てるためのポリシーを記述するインタフェース。

       @param image	UDF_Image
       @param policy	ポリシー
       @param filename	ファイル名

       イメージのパーティション番号中にFIDを生成しようとする際、FIDを生成するメソッド。
    */
    public UDF_desc257 create(UDF_Image image, UDF_Policy policy, String filename) throws UDF_Exception,IOException;
}
