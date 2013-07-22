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
import com.udfv.access.*;
import com.udfv.exception.*;
import com.udfv.ecma167.*;

public class UDF_desc266 extends com.udfv.ecma167.UDF_desc266
{
    /**
      コンストラクタ

      @param elem 親
      @param prefix ネームスコープ
      @param name 名前
    */
    public UDF_desc266(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
    }
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	// 1.02 ではこの構造は定義されていない
	if(env.udf_revision == 0x102){
	    
	    UDF_ErrorList el = new UDF_ErrorList();
	    el.addError(new UDF_Error
			(UDF_Error.C_UDF102, UDF_Error.L_ERROR, "",
			 "This structure shall not be recorded(undefined structure in ECMA167 - 2nd)."));
	    
	    el.setRName("Extended File Entry");
	    el.addError(super.verify());
	    el.setGlobalPoint(getGlobalPoint());
	    
	    return el;
	}
	else
	    return super.verify();
    }
    public void postReadHook(UDF_RandomAccess f) throws IOException, UDF_Exception{
	super.postReadHook(f);
	//UDF 2.1.5.2
	getImplId().extractSuffixAsUDFImplIdSuffix();
    }
}
