/*
   Copyright 2013 Heart Solutions Inc.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
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

public class UDF_desc5 extends com.udfv.udf150.UDF_desc5
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
	
	
	if(env.udf_revision != 0x200)
	    return super.verify();
	
	final short category = UDF_Error.C_UDF200;
	
	// 以下の制限は実はUDF2.00には存在しない(UDF2.01から) (2005/10/07 by seta)
/*	int i = 0;
	for(; i < env.getPartMapList().size() ; i++){
	    
	    if(com.udfv.udf200.UDF_SparablePartMap.class.isAssignableFrom(env.part[i].getClass())){
		
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
*/	
	if(512 < getSize()){
	    
	    el.addError(new UDF_Error
			(UDF_Error.C_UDF200, UDF_Error.L_ERROR, "",
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
