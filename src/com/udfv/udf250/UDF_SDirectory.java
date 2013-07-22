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
import com.udfv.access.*;
import com.udfv.exception.*;
import com.udfv.core.*;
import com.udfv.util.*;

/**
   UDF_SDirectory は、Stream Directory を表現するクラスである。
*/
public class UDF_SDirectory extends com.udfv.udf201.UDF_SDirectory
{
    public UDF_SDirectory(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
    }
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	if(env.udf_revision != 0x250)
	    return super.verify();
	
	el.addError(verifyBase(UDF_Error.C_UDF250));
	
	// Non-Allocatable Space Stream は、Sparable Part Map があるボリュームにのみ記録できる
	UDF_ElementBase[] child = getChildren();
	for(int i = 0; i < child.length; i++){
	    
	    UDF_desc257 fid = (UDF_desc257)child[i];
	    int filechar = fid.getFileChar().getIntValue();
	    
	    
	    if((filechar & 0x02) == 0 &&
	       UDF_Util.cmpBytesString(fid.getFileId().getData(), 1, "*UDF Non-Allocatable Space")){
		
		int j = 0;
		for( ; j < env.getPartMapList().size() ; j++){
		    
		    if(com.udfv.udf250.UDF_SparablePartMap.class.isAssignableFrom(env.part[j].getClass()))
			break;
		}
		if(env.getPartMapList().size() <= j){
		    
		    el.addError(new UDF_Error
				(UDF_Error.C_UDF250, UDF_Error.L_ERROR, "Non-Allocatable Space Stream",
				 "The Non-Allocatable Space Stream shall be recorded only on volumes " +
				 "with a sparable partition map recorded.", "3.3.7.2"));
		}
	    }
	}
	
	el.setRName("Stream Directory");
	el.setGlobalPoint(getGlobalPoint());
	el.addError(super.verify());
	
	return el;
    }
}


