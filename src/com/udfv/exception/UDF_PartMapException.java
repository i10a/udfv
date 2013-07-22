/*
 */
package com.udfv.exception;
import com.udfv.core.*;
import com.udfv.util.*;

/**
   PartitionMapに関する例外
 */
public class UDF_PartMapException extends UDF_Exception
{
    /**
       PartitionMapが期待したものでない。
     */
    public static final short C_BADPARTMAP = 1;

    public UDF_PartMapException(UDF_Element elem, String msg, short typ, short category, int info){
	super(elem, msg, typ, category, info);
    }
    public UDF_PartMapException(UDF_Element elem, String msg){
	super(elem, msg);
    }
    public UDF_PartMapException(UDF_Exception e){
	super(e);
    }
}
