/*
*/
package com.udfv.udf150;

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

public class UDF_timestamp extends com.udfv.udf102.UDF_timestamp
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
	    
	    ret.setCategory(UDF_Error.C_UDF150);
	    el.addError(ret);
	}
	
	el.addError(super.verify());
	return el;
    }
}
