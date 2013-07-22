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

public class UDF_charspec extends com.udfv.udf200.UDF_charspec
{

  /**
	@param elem 親
	@param name 名前
  */
  public UDF_charspec(UDF_Element elem, String prefix, String name){
     super(elem, prefix, name == null ? "UDF_charspec" : name);	
     init();
  }

//begin:add your code here
    
    public UDF_ErrorList veirfy() throws UDF_Exception{
	
	UDF_ErrorList el = super.verify();
	
	
	if(env.udf_revision != 0x201)
	    return el;
	
	el.addError(verifyBase(UDF_Error.C_UDF201, "2.1.2"));
	return el;
    }
    
//end:
};
