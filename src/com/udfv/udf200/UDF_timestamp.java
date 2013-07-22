/*
*/
package com.udfv.udf200;

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

public class UDF_timestamp extends com.udfv.udf150.UDF_timestamp
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
	
	
	if(env.udf_revision != 0x200)
	    return super.verify();
	
	// Type は1でなければならない
	ret = verifyTypeOfTimeZone(1);
	if(ret.isError()){
	    
	    ret.setCategory(UDF_Error.C_UDF200);
	    el.addError(ret);
	}
	
	el.setGlobalPoint(getGlobalPoint());
	el.addError(super.verify());
	
	return el;
    }
}
