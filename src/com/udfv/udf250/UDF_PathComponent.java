/*
*/
package com.udfv.udf250;

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

public class UDF_PathComponent extends com.udfv.udf201.UDF_PathComponent
{

    /**
  	@param elem 親
	@param prefix ネームスペース
	@param name 名前
    */
    public UDF_PathComponent(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? "UDF_PathComponent" : name);	
	init();
    }
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = super.verify();
	
	
	if(env.udf_revision != 0x102)
	    return el;
	
	// ComponentFileVersionNumber は0 でなければならない
	if(getComponentFileVersionNumber().getIntValue() != 0){
	    
	    el.addError(new UDF_Error
			(UDF_Error.C_UDF250, UDF_Error.L_ERROR, "ComponentFileVersionNumber",
			 "Shall be set to ZERO.",
			 "2.3.12.1.1", getComponentFileVersionNumber().getIntValue(), 0));
	}
	
	el.setGlobalPoint(getGlobalPoint());
	el.setRName("Path Component");
	return el;
    }
};
