/*
 */
package com.udfv.exception;
import com.udfv.core.*;
import com.udfv.util.*;

public class UDF_EOFException extends UDF_Exception
{
    public UDF_EOFException(UDF_Element elem, String msg, short typ, short category, int info){
	super(elem, msg, typ, category, info);
    }
    public UDF_EOFException(UDF_Element elem, String msg){
	super(elem, msg);
    }
}
