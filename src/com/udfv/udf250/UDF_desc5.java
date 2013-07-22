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

public class UDF_desc5 extends com.udfv.udf201.UDF_desc5
{
    /**
      コンストラクタ

      @param elem 親
      @param prefix ネームスコープ
      @param name 名前
    */
    public UDF_desc5(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
    }
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	if(env.udf_revision != 0x250)
	    return super.verify();
	
	el.addError(verifyBase(UDF_Error.C_UDF250));
	
	el.setRName("Partition Descriptor");
	el.addError(super.verify());
	el.setGlobalPoint(getGlobalPoint());
	
	return el;
    }
    
    public UDF_ErrorList verifyBase(short category) throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	boolean hasSparablePart = false;
	int i = 0;
	
	
	for(; i < env.getPartMapList().size(); i++){
	    
	    if(com.udfv.udf201.UDF_SparablePartMap.class.isAssignableFrom(env.part[i].getClass())){
		
		UDF_SparablePartMap spm = (UDF_SparablePartMap)env.part[i];
		long packetlen = spm.getPacketLen().getLongValue();
		
		// Part Starting Loc はPacket Len の倍数である
		if(getPartStartingLoc().getLongValue() % packetlen != 0){
		    
		    el.addError(new UDF_Error
				(category, UDF_Error.L_ERROR, "PartitionStartingLocation",
				 "For a Sparable Partition, the value of this field shall be an integral multiple of the Packet Length.",
				 "2.2.12.3"));
		}
		
		// Partition Len はPacket Len の倍数である
		if(getPartLen().getLongValue() % packetlen != 0){
		    
		    el.addError(new UDF_Error
				(category, UDF_Error.L_ERROR, "PartitionLength",
				 "For a Sparable Partition, the value of this field shall be an integral multiple of the Packet Length.",
				 "2.2.12.4"));
		}
		
		hasSparablePart = true;
		break;
	    }
	}
	
	// Phisycal Partition時、Partition Starting LocationはECCブロック倍でなければならない
	if(!hasSparablePart){
	    
	    if((getPartStartingLoc().getLongValue() % (env.ecc_blocksize / env.LBS)) != 0){
		
		el.addError(new UDF_Error
			    (category, UDF_Error.L_ERROR, "PartitionStartingLocation",
			     "For a physical partition, the value of this field shall be an integral multiple of " +
			     "(\"ECC Block Size\" (divided by) sector size) for the media.",
			     "2.2.14.3", String.valueOf(getPartStartingLoc().getLongValue()), ""));
	    }
	}
	
	if(512 < getSize()){
	    
	    el.addError(new UDF_Error
			(UDF_Error.C_UDF250, UDF_Error.L_ERROR, "",
			 "The following table summarizes the UDF limitations on the lengths of the Descriptors described in ECMA 167.\n" +
			 "Implementation Use Volume Descriptor: 512", "5.1"));
	}

	/*
	if(getAccessType().getIntValue() == 0){
	    el.addError(new UDF_Error
			(UDF_Error.C_ECMA167, UDF_Error.L_CAUTION, "",
			 "The type of access is not specified by this field\n",
			 "10.5.7"));
	}
	else if(getAccessType().getIntValue() == 1){
	    if(env.isMediaReadonly())
		el.addError(new UDF_Error
			    (UDF_Error.C_ECMA167, UDF_Error.L_ERROR, "",
			     "This field shall specify information required for the interpretation of information recorded on the partition identified by the Partition Descriptor\n",
			     "10.5.7"));
	}
	else if(getAccessType().getIntValue() == 2){
	    if(env.isMediaWriteonce())
		el.addError(new UDF_Error
			    (UDF_Error.C_ECMA167, UDF_Error.L_ERROR, "",
			     "This field shall specify information required for the interpretation of information recorded on the partition identified by the Partition Descriptor\n",
			     "10.5.7"));
	}
	else if(getAccessType().getIntValue() == 3){
	    if(env.isMediaRewritable())
		el.addError(new UDF_Error
			    (UDF_Error.C_ECMA167, UDF_Error.L_ERROR, "",
			     "This field shall specify information required for the interpretation of information recorded on the partition identified by the Partition Descriptor\n",
			     "10.5.7"));
	}
	else if(getAccessType().getIntValue() == 4){
	    if(env.isMediaOverwritable())
		el.addError(new UDF_Error
			    (UDF_Error.C_ECMA167, UDF_Error.L_ERROR, "",
			     "This field shall specify information required for the interpretation of information recorded on the partition identified by the Partition Descriptor\n",
			     "10.5.7"));
	}
	else{
	    el.addError(new UDF_Error
			(UDF_Error.C_ECMA167, UDF_Error.L_ERROR, "",
			 "This field shall specify information required for the interpretation of information recorded on the partition identified by the Partition Descriptor\n",
			 "10.5.7"));
	}
	*/
	
	return el;
    }
}
