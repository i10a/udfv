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
import com.udfv.access.UDF_RandomAccess;
import com.udfv.exception.*;
import com.udfv.ecma167.*;

/**
no documents.

<table border="1">
<tr><td>メンバ</td><td>型</td><td>サイズ</td><td>デフォルト値</td></tr>
<tr><td><b>ExtentLen</b></td><td><b>UDF_uint32a</b></td><td><i>extentLen.getSize()</i></td><td>&nbsp;</td></tr>
<tr><td><b>ExtentLoc</b></td><td><b>UDF_lb_addr</b></td><td><i>extentLoc.getSize()</i></td><td>&nbsp;</td></tr>
<tr><td><b>ImplUse</b></td><td><b>UDF_bytes</b></td><td><i>implUse.getSize()</i></td><td>&nbsp;</td></tr>
</table>
*/
public class UDF_long_ad extends com.udfv.ecma167.UDF_long_ad
{
    /**
	コンストラクタ

	@param elem 親
	@param prefix ネームスペース
	@param name 名前
    */
    public UDF_long_ad(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }

    public void postReadHook(UDF_RandomAccess f) throws IOException,UDF_Exception{
	UDF_long_ad_ADImpUse d = new UDF_long_ad_ADImpUse(this, null, null);
	getImplUse().readFromAndReplaceChild(d);
    }

    public UDF_ErrorList verify() throws UDF_Exception{
	return verifyBase(UDF_Error.C_UDF102);
    }

    public UDF_ErrorList verifyBase(short category) throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	UDF_long_ad_ADImpUse impuse = (UDF_long_ad_ADImpUse)(getImplUse().getChildren()[0]);
	el.addError(impuse.verify("ImplementationUse"));

	
        //　フラグ EXTENTErased が存在するときはADのフラグが１(not record but allocated)であるべきです　//
        int flags_value = impuse.getFlags().getIntValue();
        if (0 != (0x00000001 & flags_value)) {

            if (1 != getExtentLen().getFlag()) {
                el.addError(
                    new UDF_Error(
			  category, UDF_Error.L_ERROR, "ExtentLength",
                        "In the interests of efficiency on Rewritable media that benefits from preprocessing, the EXTENTErased flag shall be set to ONE to indicate an erased extent. This applies only to extents of type not recorded but allocated.",
                        "2.3.10.1", getExtentLen().getFlag(), 1
                    )
                );
            }
        }

	el.setRName("Long Allocation Descriptor");
	el.setGlobalPoint(getGlobalPoint());
	
	return el;
    }    
//end:
};
