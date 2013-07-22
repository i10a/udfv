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
package com.udfv.udf102;

import java.io.*;
import java.util.*;
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.access.*;
import com.udfv.exception.*;
import com.udfv.ecma167.*;

/**
Partition&nbsp;Descriptor&nbsp;を表現するクラス。<br/>

<table border="1">
<tr><td>メンバ</td><td>型</td><td>サイズ</td><td>デフォルト値</td></tr>
<tr><td><b>DescTag</b></td><td><b>UDF_tag</b></td><td><i>descTag.getSize()</i></td><td>&nbsp;</td></tr>
<tr><td><b>VolDescSeqNumber</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td><td>&nbsp;</td></tr>
<tr><td><b>PartFlags</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td><td>&nbsp;</td></tr>
<tr><td><b>PartNumber</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td><td>&nbsp;</td></tr>
<tr><td><b>PartContents</b></td><td><b>UDF_regid</b></td><td><i>partContents.getSize()</i></td><td>&nbsp;</td></tr>
<tr><td><b>PartContentsUse</b></td><td><b>UDF_bytes</b></td><td><i>128</i></td><td>&nbsp;</td></tr>
<tr><td><b>AccessType</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td><td>&nbsp;</td></tr>
<tr><td><b>PartStartingLoc</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td><td>&nbsp;</td></tr>
<tr><td><b>PartLen</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td><td>&nbsp;</td></tr>
<tr><td><b>ImplId</b></td><td><b>UDF_regid</b></td><td><i>implId.getSize()</i></td><td>&nbsp;</td></tr>
<tr><td><b>ImplUse</b></td><td><b>UDF_bytes</b></td><td><i>128</i></td><td>&nbsp;</td></tr>
<tr><td><b>Reserved</b></td><td><b>UDF_bytes</b></td><td><i>156</i></td><td>&nbsp;</td></tr>
</table>
*/
public class UDF_desc5 extends com.udfv.ecma167.UDF_desc5
{
    /**
	コンストラクタ

	@param elem 親
	@param prefix ネームスペース
	@param name 名前
    */
    public UDF_desc5(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }
//begin:add your code here
    /**
        Partition Desc. の情報を表示。

    */
    public String [] toInformation(String indent, boolean detail) {

	String nl = UDF_Util.getSystemNewLine();

	Vector vec = new Vector();
	String str;

	str = indent + "Access Type               # " + getAccessType().getIntValue();
	vec.add(str);
	str = indent + "Part. Starting Loc.       # " + getPartStartingLoc().getIntValue();
	vec.add(str);
	str = indent + "Part. Len.                # " + getPartLen().getIntValue();
	vec.add(str);

	String [] sl = new String[vec.size()];
	for (int i = 0, max = vec.size(); i < max; i++) {
	    sl[i] = (String)vec.elementAt(i);
	}

	return sl;
    }

    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	if(env.udf_revision != 0x102)
	    return super.verify();
	
	if(512 < getSize()){
	    
	    el.addError(new UDF_Error
			(UDF_Error.C_UDF102, UDF_Error.L_ERROR, "",
			 "The following table summarizes the UDF limitations on the lengths of the Descriptors described in ECMA 167.\n" +
			 "Implementation Use Volume Descriptor: 512", "5.1"));
	}
	
	el.setRName("Partition Descriptor");
	el.setGlobalPoint(getGlobalPoint());
	
	el.addError(super.verify());
	
	return el;
    }

    public void postReadHook(UDF_RandomAccess f) throws IOException, UDF_Exception{
	super.postReadHook(f);
	
	//UDF 2.1.5.2
	getImplId().extractSuffixAsUDFImplIdSuffix();
    }
    
//end:
};
