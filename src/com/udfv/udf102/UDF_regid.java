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
import com.udfv.ecma167.*;
import com.udfv.exception.*;
import com.udfv.access.*;
import com.udfv.udf102.*;

public class UDF_regid extends com.udfv.ecma167.UDF_regid
{
    /**
       @param elem 親
       @param name 名前
    */
    public UDF_regid(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? "UDF_regid" : name);	
    }

//begin:add your code here

    public void postReadHook(UDF_RandomAccess f) throws UDF_Exception{
	try{
	    long saved_pos = f.getPointer();

	    if(getId().cmpString("*UDF LV Info")){
		UDF_regid_UDFIdSuffix d = (UDF_regid_UDFIdSuffix)createElement("UDF_regid_UDFIdSuffix", null, "UDF_regid_UDFIdSuffix");
		getIdSuffix().readFromAndReplaceChild(d);
	    }
	    else if(getId().cmpString("*OSTA UDF Compliant")){
		UDF_regid_UDFDomainIdSuffix d = (UDF_regid_UDFDomainIdSuffix)createElement("UDF_regid_UDFDomainIdSuffix", null, "UDF_regid_UDFDomainIdSuffix");
		getIdSuffix().readFromAndReplaceChild(d);
	    }

	    f.seek(saved_pos);
	}
	catch(IOException e){
	    ;
	}
    }

    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = super.verify();
	UDF_Error ret;
	
	
	el.addError(getIdSuffix().verify("Identifier Suffix"));
	
	if(env.udf_revision != 0x102)
	    return el;
	
	// Flags は0 でなければならない
	ret = verifyFlags(0);
	if(ret.isError()){
	    
	    ret.setCategory(UDF_Error.C_UDF102);
	    ret.setMessage("Shall be set to ZERO.");
	    ret.setRefer("2.1.5.1");
	    el.addError(ret);
	}
	
	el.setGlobalPoint(getGlobalPoint());
	return el;
    }
    public void extractSuffixAsUDFDomainId() throws UDF_Exception{
	UDF_regid_UDFDomainIdSuffix uiis = (UDF_regid_UDFDomainIdSuffix)createElement("UDF_regid_UDFDomainIdSuffix", null, null);
	getIdSuffix().readFromAndReplaceChild(uiis);
    }
    public void extractSuffixAsUDFIdSuffix() throws UDF_Exception{
	UDF_regid_UDFIdSuffix uiis = (UDF_regid_UDFIdSuffix)createElement("UDF_regid_UDFIdSuffix", null, null);
	getIdSuffix().readFromAndReplaceChild(uiis);
    }
    public void extractSuffixAsUDFImplIdSuffix() throws UDF_Exception{
	UDF_regid_UDFImplIdSuffix uiis = (UDF_regid_UDFImplIdSuffix)createElement("UDF_regid_UDFImplIdSuffix", null, null);
	getIdSuffix().readFromAndReplaceChild(uiis);
    }
    
    
//end:
};
