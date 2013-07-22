package com.udfv.udfapi;

import java.lang.*;
import java.io.*;
import com.udfv.ecma167.*;
import com.udfv.exception.*;
import com.udfv.core.*;


/**
   Partition Map作成

*/
public interface UDF_PartMapPolicy {
    /**
       Partition Mapを作る

       @param image	イメージ
       @param policy	ポリシー
       @param type	Partition Type Identifier。nullまたは空文字列だと type1を作る

       @throws UDF_DescTagException typeが不正
    */
    public UDF_PartMap createPartMap(UDF_Image image, UDF_Policy policy, String type) throws UDF_Exception, IOException;

    public void createPartMapSub(UDF_Image image, UDF_Policy policy, UDF_PartMap pm) throws UDF_Exception, IOException;
}

