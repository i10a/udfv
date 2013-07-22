package com.udfv.udfapi;

import java.lang.*;
import java.io.*;
import com.udfv.ecma167.*;
import com.udfv.exception.*;
import com.udfv.core.*;


/**
   CRCをもつDescriptorに関するポリシー

*/
public interface UDF_DescPolicy {
    /**
       Volume Descriptorを作る

       @param image	イメージ
       @param policy	ポリシー
       @param tagid	tagid(1 - 8)

       @throws UDF_DescTagException tagidが不正
    */
    public UDF_CrcDesc createVolDesc(UDF_Image image, UDF_Policy policy, int tagid) throws UDF_Exception, IOException;

    /**
       FileSystem Descriptor作る

       desc257は UDF_FIDPolicyを呼び出して作る。

       @param image	イメージ
       @param policy	ポリシー
       @param tagid	tagid(256 - 266, 8)

       @throws UDF_DescTagException tagidが不正
    */
    public UDF_CrcDesc createFSDesc(UDF_Image image, UDF_Policy policy, int tagid) throws UDF_Exception, IOException;
}
