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
package com.udfv.udf150;

import java.io.*;
import java.util.*;
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.ecma167.*;
import com.udfv.exception.*;
import com.udfv.access.*;
import com.udfv.udf102.*;

public class UDF_LVInformation extends com.udfv.udf102.UDF_LVInformation
{
/**
@param elem	親
@param name	名前
*/
  public UDF_LVInformation(UDF_Element elem, String prefix, String name){
     super(elem, prefix, name == null ? "UDF_LVInformation" : name);
     init();
  }
//begin:add your code here
    
    public UDF_ErrorList verify() throws UDF_Exception{

	UDF_ErrorList el = super.verify();
	UDF_Error ret;
	
	
	if(env.udf_revision != 0x150)
	    return el;
	
	// CS0 でなければならない
	ret = getLVICharset().verifyCharSetType(0);
	if(ret.isError()){
	    
	    ret.setCategory(UDF_Error.C_UDF150);
	    ret.setMessage("Shall be set to indicate support for CS0 only as defined in 2.1.2.");
	    ret.setRefer("2.2.7.2.1");
	    ret.setRName("LVICharset");
	    el.addError(ret);
	}
	
	el.setRName("LVInformation");
	return el;
    }
//end:

}
