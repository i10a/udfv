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
import com.udfv.exception.*;
import com.udfv.access.*;
import com.udfv.ecma167.*;
//import com.udfv.udf201.*;

public class UDF_SparablePartMap extends com.udfv.udf200.UDF_SparablePartMap
{

    /**
	コンストラクタ

	@param elem 親
	@param prefix ネームスペース
	@param name 名前
    */
    public UDF_SparablePartMap(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }

//begin:add your code here
    /*
    public void postReadHook(UDF_RandomAccess f) throws IOException,UDF_Exception{
	//locsOfSpartingTablesの要素を uint32で置きかえる
	UDF_ElementList v = new UDF_ElementList();
	UDF_RandomAccessBuffer rab = getLocsOfSparingTables().genRandomAccessBytes();
	while(!rab.eof()){
	    UDF_uint32 u = new UDF_uint32(this, null, null);
	    u.readFrom(rab);
	    v.add(u);
	}
	getLocsOfSparingTables().replaceChildren(v);
    }
    */
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	if(env.udf_revision != 0x201)
	    return super.verify();
	
	// CD-RW の場合、Packet Length は32でなければならない
	// （DVD-RW 等については、2.01 では具体的に触れられていない）
	if(env.media_type == env.MT_CD_RW){
	    
	    if(getPacketLen().getIntValue() != 32){
		
		el.addError(new UDF_Error
			    (UDF_Error.C_UDF201, UDF_Error.L_ERROR, "Packet Length",
			     "the number of user data blocks per fixed packet. This value is specified in the medium specific section of Appendix 6." +
			     "In case of use UDF on CD-RW media, the Packet Length shall be set when the disc is formatted. " +
			     "The packet length shall be 32 sectors(64 KB).",
			     "2.2.9, 6.10.2.1", String.valueOf(getPacketLen().getIntValue()), "32"));
	    }
	}
	
	el.addError(verifyBase(UDF_Error.C_UDF201, "2.2.9"));
	
	el.setRName("UDF Sparable Partition Map");
	el.setGlobalPoint(getGlobalPoint());
	el.addError(super.verify());
	
	return el;
    }
    
//end:
};

