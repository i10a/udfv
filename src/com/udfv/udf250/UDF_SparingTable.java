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
import com.udfv.udf201.*;

public class UDF_SparingTable extends com.udfv.udf201.UDF_SparingTable
{
    /**
        @param elem 親
        @param prefix ネームスコープ
        @param name 名前
    */
    public UDF_SparingTable(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? "UDF_SparingTable" : name);
    }
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	UDF_Error ret;
	
	
	if(env.udf_revision != 0x250)
	    return super.verify();

	el.addError(verifyCRC());
	// タグ番号のチェック
	ret = verifyId();
	if(ret.isError()){
	    
	    ret.setCategory(UDF_Error.C_UDF250);
	    ret.setRefer("2.2.12");
	    el.addError(ret);
	}
	
	el.addError(verifyBase(UDF_Error.C_UDF250, "2.2.12"));
	
	el.setRName("Sparing Table");
	el.addError(super.verify());
	el.setGlobalPoint(getGlobalPoint());
	return el;
    }
}
