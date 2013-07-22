/*
 */
package com.udfv.udf200;

import java.io.*;
import java.util.*;
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
import com.udfv.exception.*;
import com.udfv.access.*;
import com.udfv.core.*;
import com.udfv.util.*;

public class UDF_VRS extends com.udfv.udf150.UDF_VRS
{
    public UDF_VRS(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? "UDF_VRS" : name);
    }
    	
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	if(env.udf_revision != 0x200)
	    return super.verify();
	
	UDF_ElementBase[] child = getChildren();
	
	
	for(int i = 0; i < child.length; i++){

	    String name = child[i].getName();
	    
	    
	    if(name.equals("UDF_ECMA167_NSR02")){
		
		el.addError(new UDF_Error
			    (UDF_Error.C_UDF200, UDF_Error.L_ERROR, "Volume Structure Descriptor(NSR02)",
			     "NSR02 descriptor is not defined by ECMA167 3rd Edition.", "1"));
	    }
	}
	
	el.setRName("Volume recognition sequence");
	el.addError(super.verify());
	
	return el;
    }
}


