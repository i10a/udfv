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

public class UDF_desc261 extends com.udfv.udf200.UDF_desc261
{
    /**
       @param elem 親
       @param name 名前
    */
    public UDF_desc261(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? "UDF_desc261" : name);	
    }
    
//begin:add your code here
    public UDF_ErrorList verify() throws UDF_Exception{

	UDF_ErrorList el = new UDF_ErrorList();
	
	
	if(env.udf_revision != 0x201)
	    return super.verify();

	el.addError(verifyBase(this, UDF_Error.C_UDF201));
	// Stream Data を指すのにノーマルなFile Entry を使うべきではない。
	if(getICBFileType() == UDF_icbtag.T_SDIRECTORY){
	    
	    el.addError(new UDF_Error
			(UDF_Error.C_UDF201, UDF_Error.L_CAUTION, "",
			 "ECMA 167 3rd edition defines a new File Entry that contains a field for identifying a stream directory. " +
			 "This new File Entry should be used in place of the old File Entry, " +
			 "and should be used for describing the streams themselves.",
			 "3.3.5.1"));
	}
	el.setRName("File Entry");
	
	el.setGlobalPoint(getGlobalPoint());
	el.addError(super.verify());
	
	return el;
    }
    
    
//end:
};
