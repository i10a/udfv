/*
*/
package com.udfv.udf201;

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

public class UDF_desc266 extends com.udfv.udf200.UDF_desc266
{
    /**
       @param elem 親
       @param name 名前
    */
    public UDF_desc266(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? "UDF_desc266" : name);	
    }
    
//begin:add your code here
    
    /**
      Extended File Entry の検証。
    */
    public UDF_ErrorList verify() throws UDF_Exception{

	UDF_ErrorList el = new UDF_ErrorList();
	
	
	if(env.udf_revision != 0x201)
	    return super.verify();

	//　File Entry / Extended File Entryの共通部分の検証　//
	el.addError(com.udfv.udf201.UDF_desc261.verifyBase(this, UDF_Error.C_UDF201));

	el.setRName("Extended File Entry");

	el.setGlobalPoint(getGlobalPoint());

	el.addError(super.verify());

	return el;
    }

//end:
};
