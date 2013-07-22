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

/**
   UDF_ISは Extentからデータをリードするクラス。
   Logical Vol Integrity Desc を先頭にデスクリプタが複数並び、
   desc8で終端されることが期待されている。
 */
public class UDF_IS extends com.udfv.ecma167.UDF_IS
{
    public UDF_IS(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? "UDF_IS" : name);
    }
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	if(env.udf_revision != 0x102)
	    return super.verify();
	
	el.addError(verifyBase(UDF_Error.C_UDF102));
	el.setRName("Logical Volume Integrity Sequence");
	el.addError(super.verify());
	
	return el;
    }
    
    public UDF_ErrorList verifyBase(short category) throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	UDF_ElementBase[] child = getChildren();
	boolean isdesc9 = false;
	
	
	for(int i = 0; i < child.length; i++){

	    String name = child[i].getName();
	    
	    
	    if(name.equals("UDF_desc9")){  // Logical Volume Integrity Desc
		
		isdesc9 = true;
		break;  // 現状では他にやることが無いので抜ける
	    }
	}
	
	// LVID は必ず存在しなければならない
	if(!isdesc9){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "",
			 "Logical Volume Integrity Descriptor: Shall be recorded.", "2."));
	}
	
	return el;
    }
}
