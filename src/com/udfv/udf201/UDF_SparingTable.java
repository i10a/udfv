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

public class UDF_SparingTable extends com.udfv.udf200.UDF_SparingTable
{

    /**
	（ローカル）クラス名を取得します
    */
    public static String getUDFClassName( ) {
        return "UDF_SparingTable";
    }

    /**
	コンストラクタ

	@param elem 親
	@param prefix ネームスペース
	@param name 名前
    */
    public UDF_SparingTable(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }

//begin:add your code here
    public int getFixedTagId() { return 0; }
    
    /*150に用意
	//map-entry の要素を uint32で置きかえる
    public void postReadHook(UDF_RandomAccess f) throws IOException, UDF_Exception{
	UDF_ElementList v = new UDF_ElementList();
	UDF_RandomAccessBuffer rab = mapEntry.genRandomAccessBuffer();
	while(!rab.eof()){
	    UDF_uint32 u = new UDF_uint32(this, null, null);
	    u.readFrom(rab);
	    v.add(u);
	}
	getMapEntry.replaceChildren(v);
    }
    */
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	UDF_Error ret;
	
	
	if(env.udf_revision != 0x201)
	    return super.verify();
	el.addError(verifyCRC());
	
	// タグ番号のチェック
	ret = verifyId();
	if(ret.isError()){
	    
	    ret.setCategory(UDF_Error.C_UDF201);
	    ret.setRefer("2.2.11");
	    el.addError(ret);
	}
	
	el.addError(verifyBase(UDF_Error.C_UDF201, "2.2.11"));
	
	el.setRName("Sparing Table");
	el.addError(super.verify());
	el.setGlobalPoint(getGlobalPoint());
	return el;
    }
    
    
//end:
};
