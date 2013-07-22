/*
 */
package com.udfv.udf200;

import java.io.*;
import java.util.*;
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
import com.udfv.access.*;
import com.udfv.exception.*;
import com.udfv.core.*;
import com.udfv.util.*;

/**
   UDF_SDirectory は、Stream Directory を表現するクラスである。
*/
public class UDF_SDirectory extends com.udfv.udf150.UDF_SDirectory
{
    public UDF_SDirectory(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
    }
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	if(env.udf_revision != 0x200)
	    return super.verify();
	
	el.addError(verifyBase(UDF_Error.C_UDF200));
	
	el.setRName("Stream Directory");
	el.setGlobalPoint(getGlobalPoint());
	el.addError(super.verify());
	
	return el;
    }
    
    protected UDF_ErrorList verifyBase(short category) throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	UDF_ElementBase[] child = getChildren();
	final String UNIQUEID_MAPPING_DATA = "*UDF Unique Id Mapping Data";
	final String NON_ALLOCATABLE_SPACE = "*UDF Non-Allocatable Space";
	final String POWER_CALIBRATION     = "*UDF Power Cal Table";
	final String UDF_BACKUP_TIME       = "*UDF Backup";
	final String MACINTOSH_RESOURCE_FORK = "*UDF Macintosh Resource Fork";
	
	for(int i = 0; i < child.length; i++){
	    
	    UDF_desc257 fid = (UDF_desc257)child[i];
	    int filechar = fid.getFileChar().getIntValue();
	    
	    
	    // Named Stream のMetadata bit は立っていなければならない
	    // ここでは、Named Stream は「Directory Bit が立っていないFID から指し示されたもの」であると解釈している
	    if((filechar & 0x02) == 0){  // Directory Bit
		
		// UDF で規定されている以外に、Named Stream で
		// "*UDF" で始まる名前を使用してはならない
		byte[] fileid = fid.getFileId().getData();
		if(5 < fileid.length){
		    
		    String strfileid = UDF_Util.b2qstr(fileid, 1, false);
		    if(fileid[1] == 0x2a && fileid[2] == 0x55 && fileid[3] == 0x44 && fileid[4] == 0x46){  // '*UDF'
			
			if(!strfileid.equals(UNIQUEID_MAPPING_DATA) &&
			   !strfileid.equals(NON_ALLOCATABLE_SPACE) &&
			   !strfileid.equals(POWER_CALIBRATION) &&
			   !strfileid.equals(UDF_BACKUP_TIME) &&
			   !strfileid.equals(MACINTOSH_RESOURCE_FORK)){
			    
			    UDF_Error err = new UDF_Error
				(category, UDF_Error.L_ERROR, "FileIdentifier",
				 "Implementations shall not use any identifier beginning with *UDF for named streams " +
				 "that are not defined in this document.",
				 "3.3.5.2");
			    try{
				err.setRecordedValue(new String(fileid, "ISO-8859-1"));
			    }
			    catch(Exception e){
				e.printStackTrace();
			    }
			    el.setRName("File Identifier Descriptor[" + i + "]");
			    el.addError(err);
			}
		    }
		}
		
		if(UDF_Util.cmpBytesString(fileid, 1, MACINTOSH_RESOURCE_FORK)){
		    
		    // Macintosh... は、Metadata bit が0でなければならない
		    if((filechar & 0x10) != 0){
			
			el.addError(new UDF_Error
				    (category, UDF_Error.L_ERROR, "FileCharacteristics(Macintosh Resource Fork Stream)",
				     "The Metadata bit in the File Characteristics field of the File Identifier Descriptor " +
				     " shall be set to 0 to indicate that the existence of this file should be made known " +
				     " to clients of a platform's file system interface.",
				     "3.3.8.1", String.valueOf(filechar), String.valueOf(filechar ^ 0x10)));
		    }
		}
		else{
		    // 特に規定されていないか、Metadata bit を立てると明示的に指定されているstream
		    if((filechar & 0x10) == 0){
			
			UDF_Error err = new UDF_Error
			    (category, UDF_Error.L_ERROR, "FileCharacteristics",
			     "All UDF named streams shall have the Metadata bit set in the File Identifier Descriptor in " +
			     "the Stream Directory, unless otherwise specified in this document.",
			     "3.3.5.2", String.valueOf(filechar), String.valueOf(filechar | 0x10));
			err.setRName("File Identifier Descriptor[" + i + "]");
			el.addError(err);
		    }
		}
	    }
	}
	
	return el;
    }
}


