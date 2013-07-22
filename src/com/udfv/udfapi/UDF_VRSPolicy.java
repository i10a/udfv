package com.udfv.udfapi;

import java.lang.*;
import java.io.*;
import com.udfv.ecma167.*;
import com.udfv.exception.*;
import com.udfv.core.*;

/**
   VRSに関するポリシー
 */
public interface UDF_VRSPolicy {
    /**
       VRS を作る

       @param image	イメージ
       @param policy	ポリシー
    */
    public UDF_VRS createVRS(UDF_Image image, UDF_Policy policy, int sec) throws UDF_Exception, IOException;
    public void createECMA119Bridge(UDF_Image image, UDF_Policy policy) throws UDF_Exception,IOException;
}
