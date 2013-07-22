/*
 */
package com.udfv.udf201;

import java.io.*;
import java.util.*;
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
import com.udfv.exception.*;
import com.udfv.access.*;
import com.udfv.core.*;
import com.udfv.util.*;

public class UDF_VRS extends com.udfv.udf200.UDF_VRS
{
    public UDF_VRS(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? "UDF_VRS" : name);
    }
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	if(env.udf_revision != 0x201)
	    return super.verify();
	
	el.addError(verifyBase(UDF_Error.C_UDF201, "2.1.7"));
	el.setRName("Volume Recognition Sequence");
	el.addError(super.verify());
	
	return el;
    }
    
    public UDF_ErrorList verifyBase(short category, String refer) throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	UDF_ElementBase[] child = getChildren();
	int nsrnum = 0;    // NSR Desc の登場する数
	int bea01num = 0;  // BEA01 の登場する数
	int tea01num = 0;  // TEA01 の登場する数
	boolean boot2desc = false;  // BOOT2 Desc が登場するか？
	
	
	for(int i = 0; i < child.length; i++){

	    String name = child[i].getName();
	    
	    
	    if(name.equals("UDF_ECMA167_BEA01")){
		
		bea01num++;
	    }
	    else if(name.equals("UDF_ECMA167_TEA01")){
		
		tea01num++;
	    }
//	    else if(name.equals("UDF_ECMA167_NSR02") || name.equals("UDF_ECMA167_NSR03")){
	    else if(name.equals("UDF_ECMA167_NSR03")){
		
		nsrnum++;
		if(bea01num == 0){
		    
		    el.addError(new UDF_Error
				(category, UDF_Error.L_ERROR, "NSR descriptor",
				 "The NSR and BOOT2 descriptors shall be in the Extended Area.", refer));
		}
	    }
	    else if(name.equals("UDF_ECMA167_BOOT2")){
		
		boot2desc = true;
		if(bea01num == 0){
		    
		    el.addError(new UDF_Error
				(category, UDF_Error.L_ERROR, "BOOT2 descriptor",
				 "The NSR and BOOT2 descriptors shall be in the Extended Area.", refer));
		}
	    }
	    else if(name.equals("UDF_pad")){  // pad のときは残りの領域とみなす
		
		com.udfv.ecma167.UDF_bytes pad = (com.udfv.ecma167.UDF_bytes)child[i];
		if(!UDF_Util.isAllZero(pad.getData())){
		    
		    el.addError(new UDF_Error
				(category, UDF_Error.L_ERROR, "",
				 "The block after the VRS shall be unrecorded or contain all #00.", refer));
		}
	    }
	    else if(name.equals("UDF_ECMA167_NSR02")){
		
		el.addError(new UDF_Error
			    (category, UDF_Error.L_ERROR, "NSR02 descriptor",
			     "NSR02 descriptor is not defined by ECMA167 3rd Edition.", "1"));
	    }
	    else{
		
		if(bea01num != 0){
		    
		    el.addError(new UDF_Error
				(category, UDF_Error.L_ERROR, "Volume Structure Descriptor",
				 "All other VSDs are only allowd before the Extended Area.", refer));
		}
	    }
	}
	
	// Extended Area が複数存在してはならない(BEA01)
	if(bea01num != 1){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Beginning Extended Area Descriptor",
			 "There shall be only one Extended Area with one BEA01 and one TEA01.", refer));
	}
	
	// Extended Area が複数存在してはならない(TEA01)
	if(1 < tea01num){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Terminating Extended Area Descriptor",
			 "There shall be only one Extended Area with one BEA01 and one TEA01.", refer));
	}
	
	// NSR descriptor が複数存在してはならない
	if(nsrnum != 1){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "NSR descriptor",
			 "There shall be exactly one NSR descriptor in VRS. " +
			 "The NSR and BOOT2 descriptors shall be in the Extended Area.", refer));
	}
	
	return el;
    }
}


