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

public class UDF_desc2 extends com.udfv.ecma167.UDF_desc2
{
    /**
      コンストラクタ

      @param elem 親
      @param prefix ネームスコープ
      @param name 名前
    */
    public UDF_desc2(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
    }
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	if(env.udf_revision != 0x102)
	    return super.verify();
	
	
	el.addError(verifyBase(UDF_Error.C_UDF102, "2.2.3"));
	
 	el.setRName("Anchor Volume Descriptor Pointer");
	el.addError(super.verify());
	
	el.setGlobalPoint(getGlobalPoint());
	return el;
    }
    
    
    protected UDF_ErrorList verifyBase(short category, String refer) throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	long mainextlen = getMainVolDescSeqExtent().getExtentLen().getLongValue();
	long reserveextlen = getReserveVolDescSeqExtent().getExtentLen().getLongValue();

	
	// Main Vol Desc Seq のExtent は最低でも16セクタ
	if(mainextlen / UDF_Env.LBS < 16){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "MainVolumeDescriptorSequenceExtent",
			 "The main VolumeDescriptorSequenceExtent shall have a minimum length of 16 logical sectors.",
			 refer + ".1", String.valueOf(mainextlen), ""));
	}
	
	// Reserve Vol Desc Seq のExtent は最低でも16セクタ(2005/09/15:追記 0も含めて16セクタ未満ならダメ by seta)
	if(/*reserveextlen != 0 && */(reserveextlen / UDF_Env.LBS < 16)){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "ReserveVolumeDescriptorSequenceExtent",
			 "The reserve VolumeDescriptorSequenceExtent shall have a minimum length of 16 logical sectors.",
			 refer + ".2", String.valueOf(reserveextlen), ""));
	}
	
	// 512 バイトを超えてはならない
	if(512 < getSize()){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "",
			 "The following table summarizes the UDF limitations on the lengths of the Descriptors described in ECMA 167.\n" +
			 "Anchor Volume Descriptor Pointer: 512", "5.1"));
	}
	
	return el;
    }    
}
