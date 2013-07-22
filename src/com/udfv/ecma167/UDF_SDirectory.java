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
package com.udfv.ecma167;

import java.io.*;
import java.util.*;
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
import com.udfv.access.*;
import com.udfv.exception.*;
import com.udfv.core.*;
import com.udfv.util.*;

/**
   UDF_SDirectory は、Stream Directory を表現するクラスである。。
*/
public class UDF_SDirectory extends com.udfv.ecma167.UDF_Directory
{
    /**
	（ローカル）クラス名を取得します
    */
    public static String getUDFClassName( ) {
        return "UDF_SDirectory";
    }
    
    public UDF_SDirectory(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
    }
    

    public UDF_ErrorList verify() throws UDF_Exception{

	UDF_ErrorList el = new UDF_ErrorList();
	UDF_Error err;
	UDF_ElementBase[] child = getChildren();
	
	
	Vector filevernumvec = new Vector();  // File Version Number を貯める
	Vector fileidvec = new Vector();  // File Identifier を貯める
	final short category = UDF_Error.C_ECMA167;
	int parentnum = 0;
	
	
	for(int i = 0; i < child.length; i++){
	    
	    UDF_desc257 fid = (UDF_desc257)child[i];
	    UDF_uint16  fvernum = fid.getFileVersionNumber();
	    UDF_bytes   fileid  = fid.getFileId();
	    
	    
	    // 基本チェック
	    el.addError(fid.verify("File Identifier Descriptor[" + i + "]"));
	    
	    // File Version Number とFile Identifier が等しい
	    // FID は２つとあってはならない
	    for(int j = 0; j < fileidvec.size(); j++){
		
		UDF_uint16 fvernum2 = (UDF_uint16)filevernumvec.elementAt(j);
		UDF_bytes  fileid2  = (UDF_bytes)fileidvec.elementAt(j);
		
		
		if(fvernum2.getIntValue() == fvernum.getIntValue() &&
		   UDF_Util.cmpBytesBytes(fileid2.getData(), fileid.getData())){
		    
		    el.addError(new UDF_Error
				(category, UDF_Error.L_ERROR, "File Identifier Descriptor[" + i + "]",
				 "For the descriptors in a directory\n" +
				 "- There shall not be more than one descriptor with the same File Identifier and File Version Number.",
				 "4/9.2"));
		}
	    }
	    // 今回のデータを追加
	    fileidvec.add(fid.getFileId());
	    filevernumvec.add(fid.getFileVersionNumber());
	    
	    if((fid.getFileChar().getIntValue() & 0x08) != 0)  // Parent bit
		parentnum++;
	}
	
	// Parent を指すFID は１つだけ存在しなければならない
	if(parentnum != 1){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "",
			 "For the descriptors in a directory\n" +
			 "- there shall be exactly one File Identifier Descriptor identifying the parent (main data stream)(see 4/14.4.3)",
			 "4/8.6"));
	}
	
	el.setRName("Stream Directory");
	el.setGlobalPoint(getGlobalPoint());
	return el;
    }
}


