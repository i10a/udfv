/*
 */
package com.udfv.udf102;

import java.io.*;
import java.util.*;
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
import com.udfv.exception.*;
import com.udfv.access.*;
import com.udfv.core.*;
import com.udfv.util.*;

public class UDF_Directory extends com.udfv.ecma167.UDF_Directory
{    
    public UDF_Directory(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? "UDF_Directory" : name);
    }
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	if(env.udf_revision != 0x102)
	    return super.verify();
	
	el.addError(verifyBase(this, UDF_Error.C_UDF102));
	
	el.setGlobalPoint(getGlobalPoint());
	el.setRName("Directory");
	el.addError(super.verify());
	
	return el;
    }
    
    static public UDF_ErrorList verifyBase(com.udfv.ecma167.UDF_Directory dir, short category) throws UDF_Exception {
	
	UDF_ErrorList el = new UDF_ErrorList();
	UDF_ElementBase[] child = dir.getChildren();
	boolean parent = false;
	
	
	for(int i = 0; i < child.length; i++){
	    
	    UDF_desc257 fid = (UDF_desc257)child[i];
	    
	    
	    // Parent bit が立っているか？
	    if((fid.getFileChar().getIntValue() & 0x08) != 0){
		
		// Parent bit は1つ目のFID にセットされていなければならない
		if(i != 0){
		    
		    UDF_Error err = new UDF_Error
			(category, UDF_Error.L_ERROR, "File Identifier Descriptor[" + i + "]",
			 "The File Identifier Descriptor describing the parent directory shall be the first " +
			 "File Identifier Descriptor recorded in the directory.");
		    err.setRefer((dir.env.udf_revision <= 0x150) ? "3.3.1" : "2.3.4");
		    el.addError(err);
		}
		
		parent = true;
	    }
	}
	
	// Parent を指すFID は必ず存在しなければならない
	if(!parent){
	    
	    UDF_Error err = new UDF_Error
		(category, UDF_Error.L_ERROR, "",
		 "All UDF directories shall include a File Identifier Descriptor that indicates the location of the parent directory.");
	    err.setRefer((dir.env.udf_revision <= 0x150) ? "3.3.1" : "2.3.4");
	    el.addError(err);
	}
	
	return el;
    }
}


