/*
 */
package com.udfv.exception;
import com.udfv.core.*;
import com.udfv.util.*;

/**
   IOに関する例外
 */
public class UDF_IOException extends UDF_Exception
{
    public UDF_IOException(UDF_Element elem, String msg, short typ, short category, int info){
	super(elem, msg, typ, category, info);
    }
    public UDF_IOException(UDF_Element elem, String msg){
	super(elem, msg);
    }
    public UDF_IOException(Exception e){
	super(e);
    }
}
