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
import com.udfv.encoding.*;

public class UDF_desc6 extends com.udfv.udf200.UDF_desc6
{

  /**
	@param elem 親
	@param name 名前
  */
  public UDF_desc6(UDF_Element elem, String prefix, String name){
     super(elem, prefix, name == null ? "UDF_desc6" : name);	
     init();
  }

//begin:add your code here

    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	if(env.udf_revision != 0x201)
	    return super.verify();
	
	el.addError(verifyBase(UDF_Error.C_UDF201));
	
	el.addError(getPartMaps().verify("PartitionMaps"));
	
	el.setRName("Logical Volume Descriptor");
	el.addError(super.verify());
	el.setGlobalPoint(getGlobalPoint());
	return el;	
    }

//end:
};
