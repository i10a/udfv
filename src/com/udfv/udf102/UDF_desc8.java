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
import com.udfv.exception.*;
import com.udfv.access.*;
import com.udfv.ecma167.*;

public class UDF_desc8 extends com.udfv.ecma167.UDF_desc8
{

  /**
	@param elem 親
	@param name 名前
  */
  public UDF_desc8(UDF_Element elem, String prefix, String name){
     super(elem, prefix, name == null ? "UDF_desc8" : name);	
     init();
  }
//begin:add your code here
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	if(env.udf_revision != 0x102)
	    return super.verify();
	
	if(512 < getSize()){
	    
	    el.addError(new UDF_Error
			(UDF_Error.C_UDF102, UDF_Error.L_ERROR, "",
			 "The following table summarizes the UDF limitations on the lengths of the Descriptors described in ECMA 167.\n" +
			 "Terminating Descriptor: 512", "5.1"));
	}
	
	el.setRName("Terminating Descriptor");
	el.setGlobalPoint(getGlobalPoint());
	
	el.addError(super.verify());
	return el;
    }
    
//end:
};
