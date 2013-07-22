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
//import com.udfv.udf201.*;

public class UDF_dstring extends com.udfv.udf201.UDF_dstring
{
    /**
  	@param elem 親
	@param prefix ネームスペース
	@param name 名前
    */
    public UDF_dstring(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name);
    }
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = super.verify();
	
	
	if(env.udf_revision != 0x250)
	    return el;
	
	el.addError(verifyBase(UDF_Error.C_UDF250));
	el.setGlobalPoint(getGlobalPoint());
	el.addError(super.verify());
	
	return el;
    }

//end:
};
