/*
 */
package com.udfv.exception;
import com.udfv.core.*;
import com.udfv.util.*;

/**
   データの値が不正である、矛盾があるときに発生するException。

   データがICB内のときは UDF_ICBException
   データがDescTag内のときは UDF_DescTagException

   を使うこと

 */
public class UDF_DataException extends UDF_Exception
{
    public UDF_DataException(UDF_Element elem, String msg, short typ, short category, int info){
	super(elem, msg, typ, category, info);
    }
    public UDF_DataException(UDF_Element elem, String msg ){
	super(elem, msg);
    }
}
