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

public class UDF_VirtualPartMap extends com.udfv.udf200.UDF_VirtualPartMap
{

    /**
	（ローカル）クラス名を取得します
    */
    public static String getUDFClassName( ) {
        return "UDF_VirtualPartMap";
    }

    /**
	コンストラクタ

	@param elem 親
	@param prefix ネームスペース
	@param name 名前
    */
    public UDF_VirtualPartMap(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }

//begin:add your code here
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	if(env.udf_revision != 0x201)
	    return super.verify();
	
	
	el.addError(verifyBase(UDF_Error.C_UDF201, "2.2.8"));
	
	el.setRName("UDF Virtual Partition Map");
	el.addError(super.verify());
	el.setGlobalPoint(getGlobalPoint());
	return el;
    }
    

    
//end:
};