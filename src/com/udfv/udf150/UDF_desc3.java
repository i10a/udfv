/*
*/
package com.udfv.udf150;

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

/**
no documents.

<table border="1">
<tr><td>メンバ</td><td>型</td><td>サイズ</td><td>デフォルト値</td></tr>
<tr><td><b>DescTag</b></td><td><b>UDF_tag</b></td><td><i>descTag.getSize()</i></td><td>&nbsp;</td></tr>
<tr><td><b>VolDescSeqNumber</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td><td>&nbsp;</td></tr>
<tr><td><b>NextVolDescSeqExtent</b></td><td><b>UDF_extent_ad</b></td><td><i>nextVolDescSeqExtent.getSize()</i></td><td>&nbsp;</td></tr>
<tr><td><b>Reserved</b></td><td><b>UDF_bytes</b></td><td><i>484</i></td><td>&nbsp;</td></tr>
</table>
*/
public class UDF_desc3 extends com.udfv.udf102.UDF_desc3
{
    /**
	コンストラクタ

	@param elem 親
	@param prefix ネームスペース
	@param name 名前
    */
    public UDF_desc3(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }

//begin:add your code here
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	if(env.udf_revision != 0x150)
	    return super.verify();
	
	el.addError(verifyBase(UDF_Error.C_UDF150));
	el.setRName("Volume Descriptor Pointer");
	el.setGlobalPoint(getGlobalPoint());
	
	el.addError(super.verify());
	return el;
    }

    
//end:
};