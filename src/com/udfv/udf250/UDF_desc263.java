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

public class UDF_desc263 extends com.udfv.udf201.UDF_desc263
{
    /**
      コンストラクタ

      @param elem 親
      @param prefix ネームスコープ
      @param name 名前
    */
    public UDF_desc263(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
    }

    /**
      UDF 2.50規格のUnallocated Space Entry の検証を行います。
    */
    public UDF_ErrorList verify() throws UDF_Exception {
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	if(env.udf_revision != 0x250)
	    return super.verify();
	
	el.addError(verifyBase(UDF_Error.C_UDF250));
        el.setRName("Unallocated Space Entry");
        el.setGlobalPoint(getGlobalPoint());
        el.addError(super.verify());
	
	return el;
    }
}
