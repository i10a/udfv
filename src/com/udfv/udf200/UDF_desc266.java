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

public class UDF_desc266 extends com.udfv.udf150.UDF_desc266
{
    /**
       @param elem 親
       @param name 名前
    */
    public UDF_desc266(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? "UDF_desc266" : name);	
    }
    
//begin:add your code here
    
    /**
      Extended File Entry の検証。
    */
    public UDF_ErrorList verify() throws UDF_Exception{

	UDF_ErrorList el = new UDF_ErrorList();
	
	if(env.udf_revision != 0x200)
	    return super.verify();


	//　File Entry / Extended File Entryの共通部分の検証　//
	el.addError(verifyBase(this, UDF_Error.C_UDF200));

	el.setRName("Extended File Entry");

	el.setGlobalPoint(getGlobalPoint());

	el.addError(super.verify());

	return el;
    }
    
    /**
       UDF 2.00 以降の規格に準じたExtended File Entryの共通部分の検証を行う。
       
       @param fe       検証対象となるExtended File Entry インスタンス。
       @param category エラーカテゴリ。
       @return  エラーリストインスタンス。
    */
    static public UDF_ErrorList verifyBase(com.udfv.ecma167.UDF_desc261 fe, short category) throws UDF_Exception {

	UDF_ErrorList el = new UDF_ErrorList();
	
	// UDF 2.00 以前の共通のエラー検証
	el.addError(com.udfv.udf200.UDF_desc261.verifyBase(fe, category));
	
	
	UDF_desc266 efe = (UDF_desc266)fe;
	int flags = efe.getICBFlags();
	
	// Stream Data のとき
	if((flags & 0x2000) != 0){
	    
	    // Stream Directory ICB は0でなければならない
	    com.udfv.ecma167.UDF_long_ad lad = (com.udfv.ecma167.UDF_long_ad)efe.getStreamDirectoryICB();
	    if(lad.getExtentLen().getIntValue() != 0 || lad.getExtentLoc().getLogicalBlockNumber().getIntValue() != 0
	       || lad.getExtentLoc().getPartReferenceNumber().getIntValue() != 0 || !UDF_Util.isAllZero(lad.getImplUse().getData())){
		
		el.addError(new UDF_Error
			    (category, UDF_Error.L_ERROR, "Stream Directory ICB",
			     "The stream directory ICB field of ICBs describing stream directories or named streams " + 
			     "shall be set to zero. [no hierarchical streams]",
			     "3.3.5.1"));
	    }
	}
	
	//　Extended Attributes の調査　//
	int len_of_ea = fe.getLenOfExtendedAttr().getIntValue();
	if (len_of_ea != 0) {

	    UDF_ElementBase [] elem = fe.getExtendedAttr().getChildren();
	    for (int idx = 0, max = elem.length; idx < max; idx++) {

		//　Extended File Entry のときFile Times Extended Attribute に File Creation Date and Timeは存在してはいけない　//
		if (!(elem[idx].getName().equals(com.udfv.ecma167.UDF_attr5.getUDFClassName()))) {
		    continue;
		}

		com.udfv.ecma167.UDF_attr5 attr5 = (com.udfv.ecma167.UDF_attr5)elem[idx];
		if (!attr5.isExistance(attr5.FILE_CREATION_DATE_AND_TIME)) {
		    continue;
		}

		UDF_Error err = new UDF_Error(
					category, UDF_Error.L_ERROR, "File Times",
					"If the main File Entry is an Extended File Entry, this structure shall not be recorded with a file creation time.",
					"3.3.4.3.1"
				);
		
		err.setRName("File Times Extended Attribute");
		err.setRName("Extended Attributes");
		el.addError(err);
	    }
	}
	
	return el;
    }
//end:
};
