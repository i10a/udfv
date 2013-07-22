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
//import com.udfv.udf200.*;

public class UDF_SparablePartMap extends com.udfv.udf150.UDF_SparablePartMap
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
	
	if(env.udf_revision != 0x200)
	    return super.verify();
	
	el.addError(verifyBase(UDF_Error.C_UDF200, "2.2.9"));
	
	// Packet Length は32 でなければならない
	if(getPacketLen().getIntValue() != 32){
	    
	    el.addError(new UDF_Error
			(UDF_Error.C_UDF200, UDF_Error.L_ERROR, "Packet Length",
			 "Shall be recorded in the format shown in 'Layout of Type 2 partition map for sparable partition'.\n" +
			 "the number of user data blocks per fixed packet. Shall be set to 32.",
			 "2.2.9", String.valueOf(getPacketLen().getIntValue()), "32"));
	}
	
	el.setRName("UDF Sparable Partition Map");
	el.setGlobalPoint(getGlobalPoint());
	el.addError(super.verify());
	return el;
    }
    
    /**
       UDF 2.00 以降全リビジョン共通のエラー検証を行う。
       
       @param category  エラーカテゴリ。
       @param refer     エラー参照番号。
       
       @return エラーリストインスタンス。
    */
    protected UDF_ErrorList verifyBase(short category, String refer) throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	UDF_Error ret;
	
	
	el.addError(super.verifyBase(category, refer));
	
	// Type 1 のパーティションマップが存在するべきではない
	int i = 0;
	for(; i < env.getPartMapList().size() ; i++){
	    
	    if(com.udfv.ecma167.UDF_part_map1.class.isAssignableFrom(env.part[i].getClass())){
		
		el.addError(new UDF_Error
			    (category, UDF_Error.L_WARNING, "",
			     "There should not be a type 1 map recorded if a Sparable Partition Map is recorded.",
			     refer));
		break;
	    }
	}
	
	return el;
    }
    
//end:
};

