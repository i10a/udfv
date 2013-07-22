/*
 */
package com.udfv.exception;
import com.udfv.core.*;
import com.udfv.util.*;

/**
   割付けに関する例外
 */
public class UDF_NotAllocException extends UDF_Exception
{
    public UDF_NotAllocException(UDF_Element elem, String msg, short typ, short category, int info){
	super(elem, msg, typ, category, info);
    }
    public UDF_NotAllocException(UDF_Element elem, String msg){
	super(elem, msg);
    }
    public UDF_NotAllocException(UDF_Exception e){
	super(e);
    }
}
