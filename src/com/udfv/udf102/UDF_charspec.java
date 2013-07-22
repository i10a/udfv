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

public class UDF_charspec extends com.udfv.ecma167.UDF_charspec
{

  /**
	@param elem 親
	@param name 名前
  */
  public UDF_charspec(UDF_Element elem, String prefix, String name){
     super(elem, prefix, name == null ? "UDF_charspec" : name);	
     init();
  }

//begin:add your code here
    public boolean isOSTACompressedUnicode(){
	if(getCharSetType().getIntValue() != 0)
	    return false;
	if(!getCharSetInfo().cmpString("OSTA Compressed Unicode"))
	    return false;
	return true;
    }
    
    public UDF_ErrorList veirfy() throws UDF_Exception{
	
	UDF_ErrorList el = super.verify();
	
	
	if(env.udf_revision != 0x102)
	    return el;
	
	el.addError(verifyBase(UDF_Error.C_UDF102, "2.1.2"));
	return el;
    }
    
    /**
       UDF 全リビジョン共通の検証を行う。
       
       @param category エラーカテゴリ。
       @param refer    リファレンス参照番号。
       @return エラーリストインスタンス。
    */
    public UDF_ErrorList verifyBase(short category, String refer) throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	if(getCharSetType().getIntValue() == 0){
	    
	    if(!UDF_Util.cmpBytesString(getCharSetInfo().getData(), "OSTA Compressed Unicode")){
		
		el.addError(new UDF_Error
			    (category, UDF_Error.L_ERROR, "CharacterSetInfo",
			     "The CharacterSetInfo field shall contain the following byte values with the remainder of the field set to a value of 0.\n" +
			     "The byte values represent the following ASCII string:\n\"OSTA Compressed Unicode\"",
			     refer));
	    }
	}
	
	return el;
    }
    
//end:
};
