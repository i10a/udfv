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

public class UDF_SparingTable extends com.udfv.udf150.UDF_SparingTable
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
	
	
	if(env.udf_revision != 0x200)
	    return super.verify();
	el.addError(verifyCRC());
	
	// タグ番号のチェック
	ret = verifyId();
	if(ret.isError()){
	    
	    ret.setCategory(UDF_Error.C_UDF200);
	    ret.setRefer("2.2.11");
	    el.addError(ret);
	}
	
	el.addError(verifyBase(UDF_Error.C_UDF200, "2.2.11"));
	
	el.setRName("Sparing Table");
	el.addError(super.verify());
	el.setGlobalPoint(getGlobalPoint());
	return el;
    }
    
    
//end:
};
