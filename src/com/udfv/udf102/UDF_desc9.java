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

public class UDF_desc9 extends com.udfv.ecma167.UDF_desc9
{
    /**
       @param elem 親
       @param prefix ネームスペース
       @param name 名前
    */
    public UDF_desc9(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? "UDF_desc9" : name);	
	init();
    }

//begin:add your code here
    public void postVolReadHook(UDF_RandomAccess f) throws IOException,UDF_Exception{
	
	super.postVolReadHook(f);
	
	UDF_ElementList v = new UDF_ElementList();

	UDF_RandomAccessBuffer rab = getFreeSpaceTable().genRandomAccessBytes();
	while(!rab.eof()){
	    UDF_uint32 u = new UDF_uint32(this, null, null);
	    u.readFrom(rab);
	    v.add(u);
	}
	getFreeSpaceTable().replaceChildren(v);
	v = new UDF_ElementList();

	rab = getSizeTable().genRandomAccessBytes();
	while(!rab.eof()){
	    UDF_uint32 u = new UDF_uint32(this, null, null);
	    u.readFrom(rab);
	    v.add(u);
	}
	
	getSizeTable().replaceChildren(v);
	
	
	UDF_RandomAccessBuffer rae = getImplUse().genRandomAccessBytes();
	UDF_desc9_ImplUse d9iu = (UDF_desc9_ImplUse)createElement("UDF_desc9_ImplUse", null, "UDF_desc9_ImplUse");
	d9iu.setHintSize(getLenOfImplUse().getIntValue());
	getImplUse().readFromAndReplaceChild(d9iu);
    }
    
    /**
        Logical Vol. Integrity の情報を表示。

    */
    public String [] toInformation(String indent, boolean detail) {

	String nl = UDF_Util.getSystemNewLine();
	Vector vec = new Vector();
	String str;

	com.udfv.ecma167.UDF_LogicalVolHeaderDesc lvhd = (UDF_LogicalVolHeaderDesc) getLogicalVolContentsUse().getFirstChild();
	str = indent + "Unique Id.                # " + lvhd.getUniqueId().getLongValue();
	vec.add(str);

	UDF_Element [] el = getFreeSpaceTable().getChildren();
	str = indent + "Free Space Table          # ";
	for (int i = 0, max = el.length; i < max; i++) {
	    UDF_uint32 i32 = (UDF_uint32) el[i];
	    str +=  nl + indent + "                          # " + i32.getIntValue();
	}
	vec.add(str);

	el = getSizeTable().getChildren();
	str = indent + "Size Table                # ";
	for (int i = 0, max = el.length; i < max; i++) {
	    UDF_uint32 i32 = (UDF_uint32) el[i];
	    str +=  nl + indent + "                          # " + i32.getIntValue();
	}
	vec.add(str);

	com.udfv.udf102.UDF_desc9_ImplUse d9iu = (UDF_desc9_ImplUse) getImplUse().getFirstChild();
	str = indent + "Number Of Files           # " + d9iu.getNumberOfFiles().getIntValue();
	vec.add(str);
	str = indent + "Number Of Directories     # " + d9iu.getNumberOfDirectories().getIntValue();
	vec.add(str);
	str = indent + "Min. UDF Read Revision    # " + d9iu.getMinUDFReadRevision().getIntValue();
	vec.add(str);
	str = indent + "Min. UDF Write Revision   # " + d9iu.getMinUDFWriteRevision().getIntValue();
	vec.add(str);
	str = indent + "Max. UDF Write Revision   # " + d9iu.getMinUDFWriteRevision().getIntValue();
	vec.add(str);

	String [] sl = new String[vec.size()];
	for (int i = 0, max = vec.size(); i < max; i++) {
	    sl[i] = (String)vec.elementAt(i);
	}

	return sl;
    }

    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	el.addError(getImplUse().verify("ImplementationUse"));
	
	if(env.udf_revision != 0x102){
	    
	    el.addError(super.verify());
	    return el;
	}
	
	el.addError(verifyBaseTo150(UDF_Error.C_UDF102));
	
	el.setRName("Logical Volume Integrity Descriptor");
	el.addError(super.verify());
	
	el.setGlobalPoint(getGlobalPoint());
	return el;
    }
    
    /**
       UDF 1.02 とUDF 1.50 のみで共通に使用される検証用メソッド。
       
       @param category エラーカテゴリ。
       @return エラーリストインスタンス。
    */
    protected UDF_ErrorList verifyBaseTo150(short category) throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	byte[] fstable = getFreeSpaceTable().getData();
	for(int i = 0; i < fstable.length; i += 4){
	    
	    // 0xffffffff の値が存在してはならない
	    long value = UDF_Util.b2uint32(fstable, i);
	    if(value == 0xffffffff){
		
		el.addError(new UDF_Error
			    (category, UDF_Error.L_ERROR, "FreeSpaceTable[" + (i / 4) + "]",
			     "The optional value of #FFFFFFFF, which indicates that the amount of " +
			     "available free space is not known, shall not be used.",
			     "2.2.6.2"));
	    }
	}
	
	byte[] sizetable = getSizeTable().getData();
	for(int i = 0; i < sizetable.length; i += 4){
	    
	    // 0xffffffff の値が存在してはならない
	    long value = UDF_Util.b2uint32(sizetable, i);
	    if(value == 0xffffffff){
		
		el.addError(new UDF_Error
			    (category, UDF_Error.L_ERROR, "SizeTable[" + (i / 4) + "]",
			     "The optional value of #FFFFFFFF, which indicates that the partition size is not known, " +
			     "shall not be used.", "2.2.6.3"));
	    }
	}
	
	
	return el;
    }
//end:
};
