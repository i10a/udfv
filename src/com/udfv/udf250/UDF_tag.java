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

public class UDF_tag extends com.udfv.udf201.UDF_tag
{
    /**
	コンストラクタ

	@param elem 親
	@param prefix ネームスペース
	@param name 名前
    */
    public UDF_tag(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }

    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();

	
	if(env.udf_revision != 0x250)
	    return super.verify();
	
	int tagid = getTagId().getIntValue();
	
	// Volume Descriptor のとき
	if(0 < tagid && tagid < 10){
	    
	    if(env.getPrimaryVolDesc(UDF_Env.VDS_AUTO, 1) != null){
		
		//int desc2_tagsernum = env.anchorDesc.getDescTag().getTagSerialNumber().getIntValue();
		int desc2_tagsernum = env.getAnchorVolDescPointer().getDescTag().getTagSerialNumber().getIntValue();
		if(getTagSerialNumber().getIntValue() != desc2_tagsernum){
		    
		    el.addError(new UDF_Error
				(UDF_Error.C_UDF200, UDF_Error.L_ERROR, "TagSerialNumber",
				 "Shall be set to the TagSerialNumber value of the Anchor Volume Descriptor Pointers on this volume.",
				 "2.2.1.1", getTagSerialNumber().getIntValue(), desc2_tagsernum));
		}
	    }
	}
	
	el.addError(super.verify());
	return el;
    }
    
//end:
};
