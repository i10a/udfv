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
import com.udfv.access.*;
import com.udfv.exception.*;
import com.udfv.ecma167.*;

/**
VirtualAllocationTable<br/>
UDF2.00/2.2.10で規定。<br/>
<br/>
UDF1.50とは仕様が大きく違うので注意すること。
<table border="1">
<tr><td>メンバ</td><td>型</td><td>サイズ</td><td>デフォルト値</td></tr>
<tr><td><b>LenOfHeader</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td><td>&nbsp;</td></tr>
<tr><td><b>LenOfImplUse</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td><td>&nbsp;</td></tr>
<tr><td><b>LogicalVolId</b></td><td><b>UDF_dstring</b></td><td><i>logicalVolId.getSize()</i></td><td>&nbsp;</td></tr>
<tr><td><b>PreviousVATICBLocation</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td><td>&nbsp;</td></tr>
<tr><td><b>NumberOfFiles</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td><td>&nbsp;</td></tr>
<tr><td><b>NumberOfDirectories</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td><td>&nbsp;</td></tr>
<tr><td><b>MinUDFReadVersion</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td><td>&nbsp;</td></tr>
<tr><td><b>MinUDFWriteVersion</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td><td>&nbsp;</td></tr>
<tr><td><b>MaxUDFWriteVersion</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td><td>&nbsp;</td></tr>
<tr><td><b>Reserved</b></td><td><b>UDF_bytes</b></td><td><i>2</i></td><td>&nbsp;</td></tr>
<tr><td><b>ImplUse</b></td><td><b>UDF_bytes</b></td><td><i>getLenOfImplUse().getIntValue()</i></td><td>&nbsp;</td></tr>
<tr><td><b>VATEntry</b></td><td><b>UDF_bytes</b></td><td><i>getHintSize() - 152</i></td><td>&nbsp;</td></tr>
</table>
*/
public class UDF_VirtualAllocTable200 extends com.udfv.udf200.UDF_VirtualAllocTable200
{
    /**
	コンストラクタ

	@param elem 親
	@param prefix ネームスペース
	@param name 名前
    */
    public UDF_VirtualAllocTable200(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }

//begin:add your code here
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	if(env.udf_revision != 0x201)
	    return el;
	
	el.addError(verifyBase(UDF_Error.C_UDF201, "2.2.10"));
	
	el.setRName("Virtual Allocation Table");
	el.setGlobalPoint(getGlobalPoint());
	
	return el;
    }

//end:
};
