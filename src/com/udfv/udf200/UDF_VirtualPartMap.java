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

public class UDF_VirtualPartMap extends com.udfv.udf150.UDF_VirtualPartMap
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
	
	
	if(env.udf_revision != 0x200)
	    return super.verify();
	
	
	el.addError(verifyBase(UDF_Error.C_UDF200, "2.2.8"));
	
	el.setRName("UDF Virtual Partition Map");
	el.addError(super.verify());
	el.setGlobalPoint(getGlobalPoint());
	return el;
    }

    /**
       @param i partrefno
     */
    protected void readVAT(int i, int lbn, int partno, int vatno)  throws UDF_PartMapException,UDF_InternalException,IOException{
	try{
	    //XMLから読むときは setPkgPriorityされないので苦肉の策
	    if(env.getUDFRevision() < 0x200){
		super.readVAT(i, lbn, partno, vatno);
		return;
	    }
	}
	catch(UDF_VolException e){
	    e.printStackTrace();
	    throw new UDF_InternalException(this, "no volume");
	}

	    
	int part_loc = 0;
	try{
	    part_loc = env.getPartStartingLoc(getPartNumber().getIntValue());
	}
	catch(UDF_VolException e){
	    e.printStackTrace();
	    throw new UDF_InternalException(this, "no volume");
	}

	UDF_FEDesc fe = null;
	UDF_Element x = findExtentByLoc(lbn, partno, 0);
	if(x == null)//古いXMLは secで記録されている
	    x = findExtentByLoc(lbn + part_loc, -1, 0);

	if(x == null)
	    return;//ないときは何もしない(VATを利用しない)

	fe = (UDF_FEDesc)x.getFirstChild();

	com.udfv.udf200.UDF_VirtualAllocTable200 vat = null;

	//DDDDDDDDDDDDDDDDD とりあえず alloctype 3のみ対処
	// 2005/10/18 追加: とりあえずshort/long ADを読めるように by seta
	if(fe.getAllocType() == 3)
	    vat = (com.udfv.udf200.UDF_VirtualAllocTable200)fe.getAllocDesc().getFirstChild();
	else if(fe.getAllocType() == 0 || fe.getAllocType() == 1){
	    vat = (com.udfv.udf200.UDF_VirtualAllocTable200)fe.getReferenceData().getFirstChild().getFirstChild();
	}

	//Spareする前の基底のExtentを作る
	//それは、参照しているパーティションに等しい
	UDF_Extent ext = (UDF_Extent)createElement("UDF_Extent", null, null);
	try{
	    com.udfv.ecma167.UDF_desc5 d5 = env.getPartDescByPartRefNo(i);
	    ext.addExtent(d5.getPartStartingLoc().getIntValue(),
			  -1,
			  d5.getPartLen().getLongValue() * env.LBS);
	}
	catch(UDF_VolException e){
	    e.printStackTrace();
	    throw new UDF_InternalException(e);
	}

	UDF_Element[] ee = vat.getVATEntry().getChildren();
	boolean has_spare = false;

	for(int k=0 ; k<ee.length ; ++k){
	    UDF_uint32 dst = (UDF_uint32)ee[k];
	    if(dst.getLongValue() != 0xffffffffL){
		if(ext.spareExtent(k, -1, dst.getIntValue() + part_loc, -1, env.LBS, getPartNumber().getIntValue())){
		    has_spare = true;
		}
	    }
	}

	if(has_spare){
	    //ext.debug(0);
	    ext.blessExtent();
	    //ext.debug(0);
	}
	env.setPartMapExtent(i, vatno, ext);
	env.setPartMapRandomAccess(i, vatno, ext.genRandomAccessExtent());

	int prev_loc = vat.getPreviousVATICBLocation().getIntValue();

	if(prev_loc != 0xffffffff){
	    readVAT(i, prev_loc, partno, vatno+1);
	}
    }
    

    //113800    
//end:
};
