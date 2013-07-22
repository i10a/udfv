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

public class UDF_desc9 extends com.udfv.udf150.UDF_desc9
{

  /**
	@param elem 親
	@param name 名前
  */
  public UDF_desc9(UDF_Element elem, String prefix, String name){
     super(elem, prefix, name == null ? "UDF_desc9" : name);	
     init();
  }

//begin:add your code here
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	if(env.udf_revision != 0x200)
	    return super.verify();
	
	el.addError(verifyBase(UDF_Error.C_UDF200));
	
	el.setRName("Logical Volume Integrity Descriptor");
	el.addError(super.verify());
	el.setGlobalPoint(getGlobalPoint());
	
	return el;
    }
    
    protected UDF_ErrorList verifyBase(short category) throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	if(env.getLogicalVolDesc(UDF_Env.VDS_AUTO) == null)
	    throw new UDF_InternalException(this, "env.logicalVolDesc == null");
	
	// Virtual Partition Map のパーティション番号を控える //
//	UDF_bytes pmbytes = env.logicalVolDesc.getPartMaps();
	UDF_bytes pmbytes = env.getLogicalVolDesc(UDF_Env.VDS_AUTO).getPartMaps();
	byte[] partmaps = pmbytes.getData();
	int  maxlen  = partmaps.length;
	int  offset  = 0;
	int  virtualpartnum = 0;
	Vector virtualpart = new Vector();  // ココに入れる
	
	
	while(offset < maxlen){
	    
	    int  pmsize = 0;
	    
	    
	    if(partmaps[offset] == 1){
		
		pmsize = 6;
	    }
	    else if(partmaps[offset] == 2){  // Type2 PartMap
		
		UDF_PartMap pm = (UDF_part_map2)createElement("UDF_part_map2", null, null);
		pmsize = 64;
	    
		UDF_RandomAccessBytes rab =
		    new UDF_RandomAccessBytes(env, partmaps, pmbytes.getPartMapOffset(), pmbytes.getElemPartRefNo(), pmbytes.getPartSubno());
		long readsz = 0;
		
		try{
		    rab.seek(offset);
		    readsz = pm.readFrom(rab);
		    
		    // Virtual PartMap かどうか？
		    if(UDF_VirtualPartMap.class.isAssignableFrom(pm.getClass())){
			
			virtualpart.add(new Integer(virtualpartnum));
		    }
		}
		catch(Exception e){
		    
		    if(readsz < pmsize)
			throw new UDF_DataException(pm, "PartitionMap readFrom error.");
		}
	    }
	    else
		break;  // 想定外
	    
	    offset += pmsize;
	    virtualpartnum++;
	}
	
	
	byte[] fstable = getFreeSpaceTable().getData();
	for(int i = 0; i < fstable.length; i += 4){
	    
	    long value = UDF_Util.b2uint32(fstable, i);
	    if(virtualpart.contains(new Integer(i / 4))){
		
		// 仮想パーティションでは、0xffffffff でなければならない
		if(value != 0xffffffff){
		    
		    el.addError(new UDF_Error
				(category, UDF_Error.L_ERROR, "FreeSpaceTable[" + (i / 4) + "]",
				 "For virtual partitions the FreeSpaceTable value shall be set to #FFFFFFFF.",
				 "2.2.6.2", String.valueOf(value), "#FFFFFFFF"));
		}
	    }
	    else{
		
		// 非仮想パーティションでは、0xffffffff であってはならない
		if(value == 0xffffffff){
		
		    el.addError(new UDF_Error
				(category, UDF_Error.L_ERROR, "FreeSpaceTable[" + (i / 4) + "]",
				 "The optional value of #FFFFFFFF, which indicates that the amount of available free space is not known, " +
				 "shall not be used for non-virtual partitions.",
				 "2.2.6.2"));
		}
	    }
	}
	
	byte[] sizetable = getSizeTable().getData();
	for(int i = 0; i < sizetable.length; i += 4){
	    
	    long value = UDF_Util.b2uint32(sizetable, i);
	    if(virtualpart.contains(new Integer(i / 4))){
	    
		// 仮想パーティションでは、0xffffffff でなければならない
		if(value != 0xffffffff){
		
		    el.addError(new UDF_Error
				(UDF_Error.C_UDF200, UDF_Error.L_ERROR, "SizeTable[" + (i / 4) + "]",
				 "For virtual partitions the SizeTable value shall be set to #FFFFFFFF.",
				 "2.2.6.3", String.valueOf(value), "#FFFFFFFF"));
		}
	    }
	    else{
		
		// 非仮想パーティションでは、0xffffffff であってはならない
		if(value == 0xffffffff){
		    
		    el.addError(new UDF_Error
				(UDF_Error.C_UDF200, UDF_Error.L_ERROR, "SizeTable[" + (i / 4) + "]",
				 "The optional value of #FFFFFFFF, which indicates that the partition size is not known, " +
				 "shall not be used for non-virtual partitions.", "2.2.6.3"));
		}
	    }
	}
	
	return el;
    }
//end:
};
