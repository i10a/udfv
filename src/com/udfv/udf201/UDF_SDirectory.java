/*
 */
package com.udfv.udf201;

import java.io.*;
import java.util.*;
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
import com.udfv.access.*;
import com.udfv.exception.*;
import com.udfv.core.*;
import com.udfv.util.*;

/**
   UDF_SDirectory は、Stream Directory を表現するクラスである。
*/
public class UDF_SDirectory extends com.udfv.udf200.UDF_SDirectory
{
    public UDF_SDirectory(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
    }
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	if(env.udf_revision != 0x201)
	    return super.verify();
	
	el.addError(verifyBase(UDF_Error.C_UDF201));
	
	el.setRName("Stream Directory");
	el.setGlobalPoint(getGlobalPoint());
	el.addError(super.verify());
	
	return el;
    }
}


