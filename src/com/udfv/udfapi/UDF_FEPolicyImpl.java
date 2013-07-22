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

package com.udfv.udfapi;

import java.lang.*;
import java.io.*;
import com.udfv.core.*;
import com.udfv.ecma167.*;
import com.udfv.exception.*;
import com.udfv.access.*;

/**
   ファイルエントリを生成する。
*/
public class UDF_FEPolicyImpl implements UDF_FEPolicy {
    
    private void setExtendedAttr(UDF_FEDesc fe, UDF_Image image, UDF_Policy policy) throws UDF_Exception,IOException{
	//UDF_RandomAccessBytes rab = fe.getExtendedAttr().genRandomAccessBytes();

	UDF_desc262 d1 = (UDF_desc262)fe.createElement("UDF_desc262", null, null);
	UDF_attr2048 a1 = (UDF_attr2048)fe.createElement("UDF_attr2048", null, null);
	UDF_attr2048 a2 = (UDF_attr2048)fe.createElement("UDF_attr2048", null, null);

	d1.setDefaultValue();
	a1.setDefaultValue();
	a2.setDefaultValue();

	/*
	System.err.println("fe pn"+fe.getElemPartRefNo());
	System.err.println("fe sn"+fe.getPartSubno());

	System.err.println("a1 pn"+a1.getImplUse().getElemPartRefNo());
	System.err.println("a1 sn"+a1.getImplUse().getPartSubno());
	*/
        UDF_tag descTag = policy.getDescTagPolicy().create(image, policy);
	descTag.getTagId().setValue(262);
	descTag.getDescCRCLen().setValue(d1.getSize() - 16);

	d1.setDescTag(descTag);
	d1.getImplAttrLoc().setValue(24);
	d1.getApplicationAttrLoc().setValue(4294967295L);

	UDF_regid reg1 = policy.getRegidPolicy().createUDFId(image, "*UDF FreeEASpace", "impl-id");
	UDF_regid reg2 = policy.getRegidPolicy().createUDFId(image, "*UDF DVD CGMS Info", "impl-id");

	a1.setImplId(reg1);
	a2.setImplId(reg2);

	a1.getImplUseLen().setValue(4);
	a1.getImplUse().setSize(4);
	a1.getAttrLen().setValue(52);

	a2.getImplUseLen().setValue(8);
	a2.getImplUse().setSize(8);
	a2.getAttrLen().setValue(56);

	/*
	d1.writeTo(rab);
	a1.writeTo(rab);
	a2.writeTo(rab);

	fe.getExtendedAttr().setSize((int)rab.getPointer());

	fe.getExtendedAttr().setData(rab.getBuffer());
	*/
	fe.getLenOfExtendedAttr().setValue(d1.getSize() + a1.getSize() + a2.getSize());
	fe.getExtendedAttr().setSize(d1.getSize() + a1.getSize() + a2.getSize());
	fe.getExtendedAttr().appendChild(d1);
	fe.getExtendedAttr().appendChild(a1);
	fe.getExtendedAttr().appendChild(a2);
    }

    /**
       領域を割り当てるためのポリシーを記述するインタフェース

       @param image	UDF_Image
       @param icbtype	ICBタイプ

       イメージのパーティション番号中にFEを生成しようとする際、FEを生成するメソッド。
    */
    public UDF_FEDesc create(UDF_Image image, UDF_Policy policy, int icbtype) throws UDF_Exception,IOException{
	//CRCの計算等はここではしない。呼び出し側で recalc()で一括でやる。
	//ADはタッチしない。このメソッドはあくまでFEを作るだけ。
	UDF_FEDesc fe = null;

	if(icbtype == UDF_icbtag.T_SDIRECTORY)
	    fe =(UDF_FEDesc)image.createElement("UDF_desc266", null, null);
	else
	    fe =(UDF_FEDesc)image.createElement("UDF_desc261", null, null);

	fe.setDefaultValue();
	
	// DescTag
        UDF_tag descTag = policy.getDescTagPolicy().create(image, policy);
	if(icbtype == UDF_icbtag.T_SDIRECTORY)
	    descTag.getTagId().setValue(266);
	else
	    descTag.getTagId().setValue(261);

	descTag.getDescCRCLen().setValue(fe.getSize() - 16);
	fe.setDescTag(descTag);

	// ICBTag
	fe.getICBTag().getFlags().setValue(32);
	fe.getICBTag().getFileType().setValue(icbtype);
	
	// Implementation ID
	UDF_RegidPolicy repo = policy.getRegidPolicy();
	fe.setImplId(repo.createDeveloperId(image, "impl-id"));

	// Unique ID
	if(icbtype != 248 && icbtype != 250 && icbtype != 251 && icbtype != 252){
	    setExtendedAttr(fe, image, policy);
	    fe.postReadHook(null);
	}

	//fe.debug(0);

	return fe;
    }
}
