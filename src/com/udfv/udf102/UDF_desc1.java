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
import com.udfv.exception.*;
import com.udfv.access.*;
import com.udfv.ecma167.*;
import com.udfv.encoding.*;

public class UDF_desc1 extends com.udfv.ecma167.UDF_desc1
{
    /**
      コンストラクタ

      @param elem 親
      @param prefix ネームスコープ
      @param name 名前
    */
    public UDF_desc1(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
    }
    
    public void postReadHook(UDF_RandomAccess f) throws UDF_Exception{
	UDF_Encoding descCharacterSet = UDF_Encoding.genEncoding("OSTA Compressed Unicode");
	UDF_Encoding explantoryCharacterSet = UDF_Encoding.genEncoding("OSTA Compressed Unicode");

	getVolId().setEncoding(descCharacterSet);
	getVolSetId().setEncoding(descCharacterSet);

	//UDF 2.1.5.2
	getImplId().extractSuffixAsUDFImplIdSuffix();
    }

    /**
        Primary Vol. Desc. の情報を表示。

    */
    public String [] toInformation(String indent, boolean detail) {

	String nl = UDF_Util.getSystemNewLine();
	Vector vec = new Vector();
	String str;

	str = indent + "Primary Vol. Desc. Number # " + getPrimaryVolDescNumber().getIntValue();
	vec.add(str);
	str = indent + "Interchange Level         # " + getInterchangeLevel().getIntValue();
	vec.add(str);
	str = indent + "Vol. Id.                  # " + getVolId().getStringData();
	vec.add(str);
	str = indent + "Vol. Set Id.              # " + getVolSetId().getStringData();
	vec.add(str);

	com.udfv.ecma167.UDF_charspec cs = getDescCharSet();
	str = indent + "Desc. Char. Set           # Char. Set Type  # " + cs.getCharSetType().getIntValue() + nl
	    + indent + "                          # Char. Set Info. # " + UDF_Util.b2qstr(cs.getCharSetInfo().getData());
	vec.add(str);

	if (detail) {

	    com.udfv.ecma167.UDF_extent_ad ad = getVolAbstract();
	    str = indent + "Vol. Abstract             # extent loc.     # " + ad.getExtentLoc().getIntValue() + nl
	        + indent + "                          # extent len.     # " + ad.getExtentLen().getIntValue();
	    vec.add(str);

	    ad = getVolCopyrightNotice();
	    str = indent + "Vol. Copyright Notice     # extent loc.     # " + ad.getExtentLoc().getIntValue() + nl
	        + indent + "                          # extent len.     # " + ad.getExtentLen().getIntValue();
	    vec.add(str);
	}

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

	int iclevel = getInterchangeLevel().getIntValue();
	if(iclevel != 2){
	    el.addError(new UDF_Error
			(UDF_Error.C_DVD_VIDEO, UDF_Error.L_ERROR, "InterchangeLevel",
			 "This field shall specify the current level of disc interchange of volume described by this descriptor." +
			 "If this volume set consists of a single volume, this field shall be set to 2.",
			 "DVD-R SPEC. 2.6.1", String.valueOf(getInterchangeLevel().getIntValue()), "2"));
	    
	}

	el.addError(getApplicationId().verify("Application Identifier"));
	el.addError(verifyBase(UDF_Error.C_UDF102));
	
 	el.setRName("Primary Volume Descriptor");
	el.addError(super.verify());
	
	el.setGlobalPoint(getGlobalPoint());
	return el;
    }
    
    /**
       UDF の全リビジョンにおける Primary Volume Descriptor の共通の検証を行う。
       
       @param category  エラーカテゴリ。
       @return エラーリストインスタンス。
    */
    protected UDF_ErrorList verifyBase(short category) throws UDF_Exception{

	UDF_ErrorList el = new UDF_ErrorList();
	UDF_Error ret;

	// Interchange Level は2または3
	int iclevel = getInterchangeLevel().getIntValue();
	if(iclevel != 2 && iclevel != 3){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "InterchangeLevel",
			 "If this volume is part of a multi-volume Volume Set then the level shall be set to 3, " +
			 "otherwise the level shall be set to 2.", "2.2.2.1", String.valueOf(iclevel), "2 or 3"));
	}
	

	// MaximumInterchangeLevel は3
	if(getMaxInterchangeLevel().getIntValue() != 3){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_WARNING, "MaximumInterchangeLevel",
			 "This field shall be set to level 3(No Restrictions Apply), " +
			 "unless specifically given a different value by the user.",
			 "2.2.2.2", String.valueOf(getMaxInterchangeLevel().getIntValue()), "3"));
	}
	
	final String CS0MSG = "Shall be set to indicate support for CS0 only as defined in 2.1.2";
	
	// CharacterSetList がCS0 以外の値を指している(see ECMA167 7.2.11)
	if(getCharSetList().getIntValue() != 1){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "CharacterSetList", CS0MSG,
			 "2.2.2.3", String.valueOf(getCharSetList().getIntValue()), "1"));
	}
	
	// MaximumCharacterSetList がCS0 以外の値を指している(see ECMA167 7.2.11)
	if(getMaxCharSetList().getIntValue() != 1){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "MaximumCharacterSetList", CS0MSG,
			 "2.2.2.4", String.valueOf(getCharSetList().getIntValue()), "1"));
	}
	
	// VolumeSetIdentifier
	el.addError(getVolSetId().verify("VolumeSetIdentifier"));
	
	// DescriptorCharacterSet がCS0 でない
	ret = getDescCharSet().verifyCharSetType(0);
	if(ret.isError()){
	    
	    ret.setCategory(category);
	    ret.setMessage(CS0MSG);
	    ret.setRefer("2.2.2.6");
	    ret.setRName("DescriptorCharacterSet");
	    el.addError(ret);
	}
	    
	
	// ExplanatoryCharacterSet がCS0 でない
	ret = getExplanatoryCharSet().verifyCharSetType(0);
	if(ret.isError()){
	    
	    ret.setCategory(category);
	    ret.setMessage(CS0MSG);
	    ret.setRefer("2.2.2.7");
	    ret.setRName("ExplanatoryCharacterSet");
	    el.addError(ret);
	}
	
	return el;
    }
}


