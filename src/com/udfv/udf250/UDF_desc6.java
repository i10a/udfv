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
package com.udfv.udf250;

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

public class UDF_desc6 extends com.udfv.udf201.UDF_desc6
{
    /**
      コンストラクタ

      @param elem 親
      @param prefix ネームスコープ
      @param name 名前
    */
    public UDF_desc6(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
    }

    protected Hashtable availPartMap(){
	Hashtable tbl = new Hashtable();
	//         12345678901234567890123
	tbl.put("\0*UDF Sparable Partition", "UDF_SparablePartMap");
	tbl.put("\0*UDF Virtual Partition\0", "UDF_VirtualPartMap");
	tbl.put("\0*UDF Metadata Partition", "UDF_MetadataPartMap");
	return tbl;
    }

    /**
       Volume を読んだ後に、UDF_desc6がいくつあろうと preivailig に対し
       一回だけ呼ばれるフック関数

     */
    public void postVolReadHook(UDF_RandomAccess f) throws UDF_Exception{
	env.part = new UDF_PartMap[getNumberOfPartMaps().getIntValue()];
	//env.part_e = new UDF_Extent[getNumberOfPartMaps().getIntValue()];
	//env.part_ra = new UDF_RandomAccessExtent[getNumberOfPartMaps().getIntValue()];

	UDF_Element[] child = getPartMaps().getChildren();

	for(int i=0 ; i<child.length ; ++i){
	    env.part[i] = (UDF_PartMap)child[i];
	    int partno = env.part[i].getPartNumber().getIntValue();
	    int part_loc = env.getPartStartingLoc(partno);
	    long part_len = env.getPartLen(partno);

	    UDF_Extent parte = new UDF_Extent(this, null, "part");
	    parte.addExtent(part_loc, -1, part_len);
	    //env.part_e[i] = parte;
	    //env.part_ra[i] = env.part_e[i].genRandomAccessExtent();

	    //System.err.println("setPartExtent: " + i);
	    env.setPartMapExtent(i, 0, parte);
	    env.setPartMapRandomAccess(i, 0, parte.genRandomAccessExtent());

	    //if(env.part[i].hasMirror()){
	    if(com.udfv.udf250.UDF_MetadataPartMap.class.isAssignableFrom(env.part[i].getClass())){
		parte = new UDF_Extent(this, null, "part");
		parte.addExtent(part_loc, -1, part_len);

		//System.err.println("setPartExtent: " + i);
		env.setPartMapExtent(i, 1, parte);
		env.setPartMapRandomAccess(i, 1, parte.genRandomAccessExtent());
	    }

	}

	setupLVISandFSDS();
    }

    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	if(env.udf_revision != 0x250)
	    return super.verify();
	
	el.addError(verifyBase(UDF_Error.C_UDF250));
	
	el.addError(getPartMaps().verify("PartitionMaps"));
	
	el.setRName("Logical Volume Descriptor");
        el.addError(super.verify());
        el.setGlobalPoint(getGlobalPoint());
	return el;
    }
}
