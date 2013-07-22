/*
 */
package com.udfv.exception;
import com.udfv.core.*;
import com.udfv.util.*;

/**
   FileSystem に関する例外。

   FileSystemとは EMCA167の4章に書いてある FileSystemの規格で、
   FileSet、SpaceBitmapを除いた部分である。
 */
public class UDF_FSException extends UDF_Exception
{
    public UDF_FSException(UDF_Element elem, String msg, short typ, short category, int info){
	super(elem, msg, typ, category, info);
    }
    public UDF_FSException(UDF_Element elem, String msg){
	super(elem, msg);
    }
}
