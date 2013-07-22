/*
 */
package com.udfv.exception;
import com.udfv.core.*;
import com.udfv.util.*;

/**
   内部的な例外
 */
public class UDF_InternalException extends UDF_Exception
{
    public UDF_InternalException(UDF_Element elem, String msg, short typ, short category, int info){
	super(elem, msg, typ, category, info);
    }
    public UDF_InternalException(UDF_Element elem, String msg ){
	super(elem, msg);
    }
    public UDF_InternalException(UDF_Exception e){
	super(e);
    }
}
