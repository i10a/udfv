/*
 */
package com.udfv.core;

import java.io.*;
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.access.*;
import com.udfv.exception.*;
import com.udfv.ecma167.*;

/**
   ICBを持つクラスのインターフェース
 */
public interface UDF_VolDesc {
    /**
       VDSを読んだあとに実行するフック関数
    */
    public void postVolReadHook(UDF_RandomAccess f) throws UDF_Exception, IOException;
    public UDF_tag getDescTag();
    public void setDescTag(UDF_tag v);
}
