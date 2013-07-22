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

public class UDF_desc257 extends com.udfv.ecma167.UDF_desc257
{

  /**
	@param elem 親
	@param name 名前
  */
  public UDF_desc257(UDF_Element elem, String prefix, String name){
     super(elem, prefix, name == null ? "UDF_desc257" : name);	
  }

//begin:add your code here
    public void setUniqueId(long unique_id){
	if(getICB().getImplUse().hasChild()){
	    UDF_long_ad_ADImpUse impuse = (UDF_long_ad_ADImpUse)getICB().getImplUse().getFirstChild();
	    try{
		UDF_RandomAccessBuffer rab = new UDF_RandomAccessBuffer(4);
		rab.writeUint32((0xffffffffL & unique_id));
		impuse.getImpUse().setData(rab.getBuffer());
	    }
	    catch(Exception e){
		e.printStackTrace();
		//発生しないはず
	    }
	}
	else{
	    UDF_bytes b = getICB().getImplUse();
	    try{
		UDF_RandomAccessBuffer rab = new UDF_RandomAccessBuffer(6);
		rab.writeUint16(0);
		rab.writeUint32((0xffffffffL & unique_id));
		b.setData(rab.getBuffer());
	    }
	    catch(Exception e){
		e.printStackTrace();
		//発生しないはず
	    }

	}
    }

    public long getUniqueId(){
	if(getICB().getImplUse().hasChild()){
	    UDF_long_ad_ADImpUse impuse = (UDF_long_ad_ADImpUse)getICB().getImplUse().getFirstChild();
	    try{
		UDF_RandomAccessBuffer rab = new UDF_RandomAccessBytes(impuse.getImpUse());
		long v = rab.readUint32();
		return v;
	    }
	    catch(Exception e){
		e.printStackTrace();
	    }
	    return 0;
	}
	else{
	    UDF_bytes b = getICB().getImplUse();
	    try{
		UDF_RandomAccessBuffer rab = new UDF_RandomAccessBytes(b);
		rab.readUint16();
		long v = rab.readUint32();
		return v;
	    }
	    catch(Exception e){
		e.printStackTrace();
	    }
	    return 0;
	}
    }

    public UDF_ErrorList verify() throws UDF_Exception{
        
        UDF_ErrorList el = new UDF_ErrorList();
        
        
        if(env.udf_revision != 0x102)
            return super.verify();
        
        el.addError(verifyBase(UDF_Error.C_UDF102));
        el.setRName("File Identifier Descriptor (=257)");
        
        el.addError(super.verify());
        el.setGlobalPoint(getGlobalPoint());
        
        return el;
    }
    
    protected UDF_ErrorList verifyBase(short category) throws UDF_Exception{
	
        UDF_ErrorList el = new UDF_ErrorList();
	
	
        //　Descriptorのサイズが１論理ブロックを超えてはいけない　//
        if (getSize() > env.LBS) {

            el.addError(
                new UDF_Error(
                    category, UDF_Error.L_ERROR, "",
                    "The File Identifier Descriptor shall be restricted to the length of at most one Logical Block.",
                    "2.3.4"
                )
            );
        }

        //　ファイルバージョンは１にすべきである　//
        int file_version_number = getFileVersionNumber().getIntValue();
        if (1 != file_version_number) {

            el.addError(
                new UDF_Error(
                    category, UDF_Error.L_ERROR, "FileVersionNumber",
                    "Shall be set to 1.",
                    "2.3.4.1"
                )
            );
        }
        
	// #FFFE または#FEFF が含まれていてはならない
	byte[] data = getFileId().getData();
	for(int i = 1; (i + 1) < data.length; i++){
	    
	    int val1 = UDF_Util.b2i(data[i]);
	    int val2 = UDF_Util.b2i(data[i + 1]);
	    
	    if((val1 == 0xff && val2 == 0xfe) || (val1 == 0xfe && val2 == 0xff)){
		
		el.addError(new UDF_Error
			    (category, UDF_Error.L_ERROR, "Compressed Bit Stream[" + i + "]",
			     "The Unicode byte-order marks, #FEFF and #FFFE, shall not be used.", "2.1.1"));
		break;//i++;
	    }
	    else if(val2 != 0xfe && val2 != 0xff)
		i++;
	}
	
        return el;
    }
    
//end:
};
