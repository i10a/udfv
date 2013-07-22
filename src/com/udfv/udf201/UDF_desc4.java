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
import com.udfv.exception.*;
import com.udfv.udf201.*;

public class UDF_desc4 extends com.udfv.udf200.UDF_desc4
{

  /**
	@param elem 親
	@param name 名前
  */
  public UDF_desc4(UDF_Element elem, String prefix, String name){
     super(elem, prefix, name == null ? "UDF_desc4" : name);	
     init();
  }

//begin:add your code here
    public void postReadHook(UDF_RandomAccess f) throws UDF_Exception{
	//UDF_LVInformation d = new UDF_LVInformation(this, "UDF_desc4", "UDF_LVInformation");
	com.udfv.udf102.UDF_LVInformation d = (com.udfv.udf102.UDF_LVInformation)createElement("UDF_LVInformation", "UDF_desc4", "UDF_LVInformation");
	getImplUse().readFromAndReplaceChild(d);
    }
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	if(env.udf_revision != 0x201)
	    return super.verify();
	
	el.addError(verifyBase(UDF_Error.C_UDF201, "2.2.7.1"));
	
	el.setRName("Implementation Use Volume Descriptor");
	el.addError(super.verify());
	el.setGlobalPoint(getGlobalPoint());
	
	return el;
    }
    
//end:
};
