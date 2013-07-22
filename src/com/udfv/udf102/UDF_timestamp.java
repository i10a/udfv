/*
*/
package com.udfv.udf102;

import java.io.*;
import java.util.*;
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.exception.*;
import com.udfv.access.*;
import com.udfv.ecma167.*;

public class UDF_timestamp extends com.udfv.ecma167.UDF_timestamp
{
    /**
        コンストラクタ

        @param elem 親
        @param prefix ネームスコープ
        @param name 名前
    */
    public UDF_timestamp(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
    }
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	UDF_Error ret;
	
	
	// Type は1でなければならない
	ret = verifyTypeOfTimeZone(1);
	if(ret.isError()){
	    
	    el.addError(ret);
	}
	
	el.addError(super.verify());
	return el;
    }
    
    protected UDF_Error verifyTypeOfTimeZone(int type) throws UDF_Exception{
	
	if(getTypeOfTimeZone() != type){
	    
	    return new UDF_Error
			(UDF_Error.C_UDF102, UDF_Error.L_ERROR, "TypeAndTimezone(Type)",
			 "Type shall be set to ONE to indicate Local Time.",
			 "2.1.4.1", String.valueOf(getTypeOfTimeZone()), String.valueOf(type));
	}
	else
	    return new UDF_Error();
    }
}
