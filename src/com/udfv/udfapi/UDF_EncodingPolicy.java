
package com.udfv.udfapi;

import java.lang.*;
import java.io.*;
import com.udfv.core.*;
import com.udfv.ecma167.*;
import com.udfv.exception.*;
import com.udfv.encoding.*;

public interface UDF_EncodingPolicy{
    /**
       文字列を encodeするのに最適なencodingを取得する。
       (実際のencodingはしない)

       @param image	UDF_Image
       @param policy	ポリシー
       @param str	エンコードしようとする文字列
    */
    public UDF_Encoding create(UDF_Image image, UDF_Policy policy, String str) throws UDF_Exception,IOException;
}
