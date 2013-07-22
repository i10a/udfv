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
import com.udfv.exception.*;
import com.udfv.access.*;
import com.udfv.ecma167.*;
import com.udfv.encoding.*;

public class UDF_desc6 extends com.udfv.udf102.UDF_desc6
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
    protected Hashtable availPartMap(){
	Hashtable tbl = new Hashtable();
	//         12345678901234567890123
	tbl.put("\0*UDF Sparable Partition", "UDF_SparablePartMap");
	tbl.put("\0*UDF Virtual Partition\0", "UDF_VirtualPartMap");
	return tbl;
    }
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
		
	if(env.udf_revision != 0x150)
	    return super.verify();
	
	el.addError(verifyBase(UDF_Error.C_UDF150));
	
	el.addError(getPartMaps().verify("PartitionMaps"));
	
	el.setRName("Logical Volume Descriptor");
	el.addError(super.verify());
	el.setGlobalPoint(getGlobalPoint());
	return el;
    }
    
//end:
};
