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

public class UDF_tag extends com.udfv.udf150.UDF_tag
{
    /**
	コンストラクタ

	@param elem 親
	@param prefix ネームスペース
	@param name 名前
    */
    public UDF_tag(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }

    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = super.verify();

	
	if(env.udf_revision != 0x200)
	    return el;
	
	int tagid = getTagId().getIntValue();
	
	// Volume Descriptor のとき
	if(0 < tagid && tagid < 10){
	    
	    if(env.getPrimaryVolDesc(UDF_Env.VDS_AUTO, 1) != null){
		
		//int desc2_tagsernum = env.anchorDesc.getDescTag().getTagSerialNumber().getIntValue();
		int desc2_tagsernum = env.getAnchorVolDescPointer().getDescTag().getTagSerialNumber().getIntValue();
		if(getTagSerialNumber().getIntValue() != desc2_tagsernum){
		    
		    el.addError(new UDF_Error
				(UDF_Error.C_UDF200, UDF_Error.L_ERROR, "TagSerialNumber",
				 "Shall be set to the TagSerialNumber value of the Anchor Volume Descriptor Pointers on this volume.",
				 "2.2.1.1", getTagSerialNumber().getIntValue(), desc2_tagsernum));
		}
	    }
	}
	
	return el;
    }
    
//end:
};
