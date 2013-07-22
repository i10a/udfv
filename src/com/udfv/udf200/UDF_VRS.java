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
import com.udfv.exception.*;
import com.udfv.access.*;
import com.udfv.core.*;
import com.udfv.util.*;

public class UDF_VRS extends com.udfv.udf150.UDF_VRS
{
    public UDF_VRS(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? "UDF_VRS" : name);
    }
    	
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	if(env.udf_revision != 0x200)
	    return super.verify();
	
	UDF_ElementBase[] child = getChildren();
	
	
	for(int i = 0; i < child.length; i++){

	    String name = child[i].getName();
	    
	    
	    if(name.equals("UDF_ECMA167_NSR02")){
		
		el.addError(new UDF_Error
			    (UDF_Error.C_UDF200, UDF_Error.L_ERROR, "Volume Structure Descriptor(NSR02)",
			     "NSR02 descriptor is not defined by ECMA167 3rd Edition.", "1"));
	    }
	}
	
	el.setRName("Volume recognition sequence");
	el.addError(super.verify());
	
	return el;
    }
}


