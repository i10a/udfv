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

public class UDF_desc5 extends com.udfv.udf200.UDF_desc5
{

  /**
	@param elem 親
	@param name 名前
  */
  public UDF_desc5(UDF_Element elem, String prefix, String name){
     super(elem, prefix, name == null ? "UDF_desc5" : name);	
     init();
  }

//begin:add your code here
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	if(env.udf_revision != 0x201)
	    return super.verify();
	
	final short category = UDF_Error.C_UDF201;
	
	int i = 0;
	for(; i < env.getPartMapList().size() ; i++){
	    
	    if(com.udfv.udf201.UDF_SparablePartMap.class.isAssignableFrom(env.part[i].getClass())){
		
		UDF_SparablePartMap spm = (UDF_SparablePartMap)env.part[i];
		long packetlen = spm.getPacketLen().getLongValue();
		
		// Part Starting Loc はPacket Len の倍数である
		if(getPartStartingLoc().getLongValue() % packetlen != 0){
		    
		    el.addError(new UDF_Error
				(category, UDF_Error.L_ERROR, "PartitionStartingLocation",
				 "For a Sparable Partition, the value of this field shall be an integral multiple of the Packet Length.",
				 "2.2.12.2"));
		}
		
		// Partition Len はPacket Len の倍数である
		if(getPartLen().getLongValue() % packetlen != 0){
		    
		    el.addError(new UDF_Error
				(category, UDF_Error.L_ERROR, "PartitionLength",
				 "For a Sparable Partition, the value of this field shall be an integral multiple of the Packet Length.",
				 "2.2.12.3"));
		}
		
		break;
	    }
	}
	
	if(512 < getSize()){
	    
	    el.addError(new UDF_Error
			(UDF_Error.C_UDF201, UDF_Error.L_ERROR, "",
			 "The following table summarizes the UDF limitations on the lengths of the Descriptors described in ECMA 167.\n" +
			 "Implementation Use Volume Descriptor: 512", "5.1"));
	}
	
	el.setRName("Partition Descriptor");
	el.addError(super.verify());
	el.setGlobalPoint(getGlobalPoint());
	
	return el;
    }
    
//end:
};
