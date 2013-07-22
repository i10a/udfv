/*
*/
package com.udfv.udf200;

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

public class UDF_desc2 extends com.udfv.udf150.UDF_desc2
{

  /**
	@param elem 親
	@param name 名前
  */
  public UDF_desc2(UDF_Element elem, String prefix, String name){
     super(elem, prefix, name == null ? "UDF_desc2" : name);	
     init();
  }

//begin:add your code here
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	if(env.udf_revision != 0x200)
	    return super.verify();
	
	el.addError(verifyBase(UDF_Error.C_UDF200, "2.2.3"));
	
 	el.setRName("Anchor Volume Descriptor Pointer");
	el.addError(super.verify());
	el.setGlobalPoint(getGlobalPoint());
	
	return el;
    }
//end:
};
