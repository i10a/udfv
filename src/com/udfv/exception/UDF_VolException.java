/*
 */
package com.udfv.exception;
import com.udfv.core.*;
import com.udfv.util.*;

/**
   Volume に関する例外。

   Volumeとは EMCA167の3章に書いてある Volumeの規格だけでなく
   4章に書いてある FileSet、SpaceBitmapなども含む。
 */
public class UDF_VolException extends UDF_Exception
{
    public UDF_VolException(UDF_Element elem, String msg, short typ, short category, int info){
	super(elem, msg, typ, category, info);
    }
    public UDF_VolException(UDF_Element elem, String msg){
	super(elem, msg);
    }
}
