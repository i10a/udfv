/*
 */
package com.udfv.udf200;

import java.io.*;
import java.util.*;
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
import com.udfv.exception.*;
import com.udfv.access.*;
import com.udfv.core.*;
import com.udfv.util.*;

public class UDF_Directory extends com.udfv.udf150.UDF_Directory
{    
    public UDF_Directory(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? "UDF_Directory" : name);
    }
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	if(env.udf_revision != 0x200)
	    return super.verify();
	
	el.addError(verifyBase(UDF_Error.C_UDF200, "2.3.4.4"));
	
	el.setRName("Directory");
	el.setGlobalPoint(getGlobalPoint());
	el.addError(super.verify());
	
	return el;
    }
    
    protected UDF_ErrorList verifyBase(short category, String refer) throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	UDF_ElementBase[] child = getChildren();
	
//	UDF_RandomAccessExtent rae = genRandomAccessExtent();
	UDF_RandomAccessBytes rab = genRandomAccessBytes();
	
	
	el.addError(com.udfv.udf150.UDF_Directory.verifyBase(this, category));
	
	
	long feuid = 0; // このDirectory を指しているFE のUnique Idの下位32ビット
	for(int i = 0; i < child.length; i++){
	    
	    UDF_Element fid = (UDF_Element)child[i];
	    
	    
	    try{
		// FID のtag 領域が境界をまたがってはならない
		long remain = rab.getAbsPointer() % UDF_Env.LBS;
		if(remain != 0 && (UDF_Env.LBS - 16) < remain){
		    
		    el.addError(new UDF_Error
				(category, UDF_Error.L_WARNING, "File Identifier Descriptor[" + i + "]",
				 "When writing a File Identifier Descriptor to write-once media, to ensure that the Descriptor Tag" +
				 "field of the next FID will never span a block boundary, if there are less than 16 bytes " +
				 "remaining in the current block after the FID, the length of the FID shall be increased " +
				 "(using the Implementation Use field) enough to prevent this.",
				 refer));
		}
		
		rab.seek(fid.getSize(), UDF_RandomAccess.SEEK_CUR);
	    }
	    catch(Exception e){
		
		e.printStackTrace();
	    }
	}
	
	return el;
    }
}


