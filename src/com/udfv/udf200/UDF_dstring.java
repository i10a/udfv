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
import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.exception.*;
import com.udfv.access.*;
import com.udfv.ecma167.*;
//import com.udfv.udf200.*;

public class UDF_dstring extends com.udfv.udf150.UDF_dstring
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
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	if(env.udf_revision != 0x200)
	    return super.verify();
	
	el.addError(verifyBase(UDF_Error.C_UDF200));
	
	int compressionID = UDF_Util.b2i(getData()[0]);
	if(compressionID == 254){
	    
	    if(getData().length < 4){
		
		el.addError(new UDF_Error
			    (UDF_Error.C_UDF200, UDF_Error.L_ERROR, "Compressed Bit Stream",
			     "The CompressionID shall identify the compression algorithm used to compress the CompressedBitStream field. " +
			     "254: Value indicates there is a unique 4-byte binary number following.",
			     "2.1.1"));
	    }
	}
	else if(compressionID == 255){
	    
	    if(getData().length < 8){
		
		el.addError(new UDF_Error
			    (UDF_Error.C_UDF200, UDF_Error.L_ERROR, "Compressed Bit Stream",
			     "The CompressionID shall identify the compression algorithm used to compress the CompressedBitStream field. " +
			     "255: Value indicates there is a unique 8-byte binary number following.",
			     "2.1.1"));
	    }
	}
	
	el.setGlobalPoint(getGlobalPoint());
	el.addError(super.verify());
	return el;
    }
    
//end:
};
