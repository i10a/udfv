/*
 */
package com.udfv.core;

import java.io.*;
import java.util.*;
import java.lang.reflect.*;
import com.udfv.ecma167.UDF_uint8;
import com.udfv.ecma167.UDF_uint16;
import com.udfv.ecma167.UDF_uint32;
import com.udfv.ecma167.UDF_uint32a;
import com.udfv.ecma167.UDF_uint64;
import com.udfv.ecma167.UDF_short_ad;
import com.udfv.ecma167.UDF_lb_addr;
import com.udfv.ecma167.UDF_bytes;
import com.udfv.ecma167.UDF_pad;

/*
 */
public interface UDF_ElementGenerator {
    public void setPkgPriority(int revision);

    /**
       現在の UDFのリビジョンレベルに合った適切なパッケージを持つ UDF_Elementを生成する。
       
       @param className	クラス名
       @param mother	親
       @param prefix	ネームスペース
       @param tagName	XML Node名
    */
    public UDF_Element genElement(String className, UDF_Element mother, String prefix, String tagName) throws ClassNotFoundException;
}
