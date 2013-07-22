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
import com.udfv.exception.*;
import com.udfv.access.*;
import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.udf201.*;

public class UDF_desc1 extends com.udfv.udf200.UDF_desc1
{

  /**
	@param elem 親
	@param name 名前
  */
  public UDF_desc1(UDF_Element elem, String prefix, String name){
     super(elem, prefix, name == null ? "UDF_desc1" : name);	
     init();
  }

//begin:add your code here
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	if(env.udf_revision != 0x201)
	    return super.verify();
	
	el.addError(verifyBase(UDF_Error.C_UDF201));
	
 	el.setRName("Primary Volume Descriptor");
	el.addError(super.verify());
	el.setGlobalPoint(getGlobalPoint());
	
	return el;
    }
    
    /**
       UDF 2.01 以上のリビジョンにおける Primary Volume Descriptor の共通の検証を行う。
       
       @param category  エラーカテゴリ。
       @return エラーリストインスタンス。
    */
    protected UDF_ErrorList verifyBase(short category) throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	el.addError(super.verifyBase(category));
	
	if(512 < getSize()){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "",
			 "The following table summarizes the UDF limitations on the lengths of the Descriptors described in ECMA 167.\n" +
			 "Primary Volume Descriptor: 512", "5.1"));
	}
	
	// 全て0でない場合、
	UDF_regid appid = (UDF_regid)getApplicationId();
	if(appid.getFlags().getIntValue() != 0 ||
	   !UDF_Util.isAllZero(appid.getId().getData()) ||
	   !UDF_Util.isAllZero(appid.getIdSuffix().getData())){
	    
	    el.addError(getApplicationId().verify("ApplicationIdentifier"));
	}
	
	return el;
    }
	
//end:
};
