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
import com.udfv.ecma167.*;
import com.udfv.exception.*;
import com.udfv.access.*;
import com.udfv.udf150.*;

public class UDF_regid_UDFDomainIdSuffix extends com.udfv.udf102.UDF_regid_UDFDomainIdSuffix
{
    /**
       @param elem 親
       @param name 名前
    */
    public UDF_regid_UDFDomainIdSuffix(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name);
    }

//begin:add your code here
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = super.verify();
	
	
	if(env.udf_revision != 0x150)
	    return el;
	
	// リビジョンチェック
	if(getUDFRevision().getIntValue() != 0x150){
	    
	    el.addError(new UDF_Error
			(UDF_Error.C_UDF250, UDF_Error.L_ERROR, "UDF Revision",
			 "The UDFRevision field shall contain #0150 to indicate revision 1.50.",
			 "2.1.5.3", String.valueOf(getUDFRevision().getIntValue()), "336"));
	}
	
	el.addError(verifyBase(UDF_Error.C_UDF250, "2.1.5.3"));
	el.setGlobalPoint(getGlobalPoint());
	return el;
    }
    
//end:
};
