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
import com.udfv.access.UDF_RandomAccess;
import com.udfv.exception.*;
import com.udfv.ecma167.*;

/**
no documents.

<table border="1">
<tr><td>メンバ</td><td>型</td><td>サイズ</td><td>デフォルト値</td></tr>
<tr><td><b>ExtentLen</b></td><td><b>UDF_uint32a</b></td><td><i>extentLen.getSize()</i></td><td>&nbsp;</td></tr>
<tr><td><b>ExtentLoc</b></td><td><b>UDF_lb_addr</b></td><td><i>extentLoc.getSize()</i></td><td>&nbsp;</td></tr>
<tr><td><b>ImplUse</b></td><td><b>UDF_bytes</b></td><td><i>implUse.getSize()</i></td><td>&nbsp;</td></tr>
</table>
*/
public class UDF_long_ad extends com.udfv.ecma167.UDF_long_ad
{
    /**
	コンストラクタ

	@param elem 親
	@param prefix ネームスペース
	@param name 名前
    */
    public UDF_long_ad(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }

    public void postReadHook(UDF_RandomAccess f) throws IOException,UDF_Exception{

	UDF_long_ad_ADImpUse d = new UDF_long_ad_ADImpUse(this, null, null);
	getImplUse().readFromAndReplaceChild(d);
    }
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	

	//impuseは常にあるとは限らない。
	UDF_long_ad_ADImpUse impuse = null;
	try{
	    impuse = (UDF_long_ad_ADImpUse)(getImplUse().getChildren()[0]);
	    el.addError(impuse.verify("ImplementationUse"));
	}
	catch(ArrayIndexOutOfBoundsException e){
	    //debug(0);
	    //e.printStackTrace();
	}
	
	if(env.udf_revision != 0x201)
	    return super.verify();
	

	if(impuse != null){
	    //　フラグ EXTENTErased が存在するときはADのフラグが１(not record but allocated)であるべきです　//
	    int flags_value = impuse.getFlags().getIntValue();
	    if (0 != (0x00000001 & flags_value)) {
		
		if (1 != getExtentLen().getFlag()) {
		    el.addError(
                    new UDF_Error(
                        UDF_Error.C_UDF201, UDF_Error.L_ERROR, "ExtentLength",
                        "In the interests of efficiency on Rewritable media that benefits from preprocessing, the EXTENTErased flag shall be set to ONE to indicate an erased extent. This applies only to extents of type not recorded but allocated.",
                        "2.3.10.1", getExtentLen().getFlag(), 1
                    )
                );
		}
	    }
	}

	el.setRName("Long Allocation Descriptor");
	el.addError(super.verify());
	el.setGlobalPoint(getGlobalPoint());
	
	return el;
    }    
//end:
};
