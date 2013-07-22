/*
 */
package com.udfv.udf201;

import java.io.*;
import java.util.*;
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
import com.udfv.exception.*;
import com.udfv.access.*;
import com.udfv.core.*;
import com.udfv.util.*;

public class UDF_Directory extends com.udfv.udf200.UDF_Directory
{    
    public UDF_Directory(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? "UDF_Directory" : name);
    }
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	if(env.udf_revision != 0x201)
	    return super.verify();
	
	el.addError(verifyBase(UDF_Error.C_UDF201, "2.3.4.4"));
	
	el.setRName("Directory");
	el.addError(super.verify());
	el.setGlobalPoint(getGlobalPoint());
	
	return el;
    }

}


