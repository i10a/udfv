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
<tr><td><b>PartFlags</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td><td>&nbsp;</td></tr>
<tr><td><b>PartNumber</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td><td>&nbsp;</td></tr>
<tr><td><b>PartContents</b></td><td><b>UDF_regid</b></td><td><i>partContents.getSize()</i></td><td>&nbsp;</td></tr>
<tr><td><b>PartContentsUse</b></td><td><b>UDF_bytes</b></td><td><i>128</i></td><td>&nbsp;</td></tr>
<tr><td><b>AccessType</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td><td>&nbsp;</td></tr>
<tr><td><b>PartStartingLoc</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td><td>&nbsp;</td></tr>
<tr><td><b>PartLen</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td><td>&nbsp;</td></tr>
<tr><td><b>ImplId</b></td><td><b>UDF_regid</b></td><td><i>implId.getSize()</i></td><td>&nbsp;</td></tr>
<tr><td><b>ImplUse</b></td><td><b>UDF_bytes</b></td><td><i>128</i></td><td>&nbsp;</td></tr>
<tr><td><b>Reserved</b></td><td><b>UDF_bytes</b></td><td><i>156</i></td><td>&nbsp;</td></tr>
</table>
*/
public class UDF_desc5 extends com.udfv.udf102.UDF_desc5
{
    /**
	コンストラクタ

	@param elem 親
	@param prefix ネームスペース
	@param name 名前
    */
    public UDF_desc5(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }
//begin:add your code here
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	if(env.udf_revision != 0x150)
	    return super.verify();
	
	if(512 < getSize()){
	    
	    el.addError(new UDF_Error
			(UDF_Error.C_UDF150, UDF_Error.L_ERROR, "",
			 "The following table summarizes the UDF limitations on the lengths of the Descriptors described in ECMA 167.\n" +
			 "Implementation Use Volume Descriptor: 512", "5.1"));
	}
	
	el.setRName("Partition Descriptor");
	el.setGlobalPoint(getGlobalPoint());
	
	el.addError(super.verify());
	
	return el;
    }
//end:
};
