/*
*/
package com.udfv.udf150;

import java.io.*;
import java.util.*;
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
import com.udfv.exception.*;
import com.udfv.access.*;
import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.udf150.*;

public class UDF_desc1 extends com.udfv.udf102.UDF_desc1
{

  /**
	@param elem 親
	@param name 名前
  */
  public UDF_desc1(UDF_Element elem, String prefix, String name){
     super(elem, prefix, name == null ? "UDF_desc1" : name);	
     init();
  }

//begin:add your code here
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	if(env.udf_revision != 0x150)
	    return super.verify();
	
	el.addError(getApplicationId().verify("Application Identifier"));
	el.addError(verifyBase(UDF_Error.C_UDF150));
	
 	el.setRName("Primary Volume Descriptor");
	el.addError(super.verify());
	el.setGlobalPoint(getGlobalPoint());
	
	return el;
    }
    
//end:
};
