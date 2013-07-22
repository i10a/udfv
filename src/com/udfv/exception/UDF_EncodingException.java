/*
 */
package com.udfv.exception;
import com.udfv.core.*;
import com.udfv.util.*;


/**
   Encodingに関する例外
 */
public class UDF_EncodingException extends UDF_Exception
{
    /**
       不明のEncoding
     */
    public static final short C_BADENCODING = 1;

    public UDF_EncodingException(UDF_Element elem, String msg, short typ, short category, int info){
	super(elem, msg, typ, category, info);
    }
    public UDF_EncodingException(UDF_Element elem, String msg){
	super(elem, msg);
    }
}
