
package com.udfv.udfapi;

import java.lang.*;
import java.io.*;
import com.udfv.core.*;
import com.udfv.ecma167.*;
import com.udfv.exception.*;

public interface UDF_DescTagPolicy{
    /**
       @param image	UDF_Image
       @param policy	ポリシー
    */
    public UDF_tag create(UDF_Image image, UDF_Policy policy) throws UDF_Exception,IOException;
}
