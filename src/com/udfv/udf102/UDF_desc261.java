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

public class UDF_desc261 extends com.udfv.ecma167.UDF_desc261
{
    /**
      コンストラクタ

      @param elem 親
      @param prefix ネームスコープ
      @param name 名前
    */
    public UDF_desc261(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
    }

    public UDF_ErrorList verify() throws UDF_Exception{

	UDF_ErrorList el = new UDF_ErrorList();
	
	
	if(env.udf_revision != 0x102)
	    return super.verify();

	el.addError(verifyBase(this, UDF_Error.C_UDF102));
	el.setRName("File Entry");
	
	el.setGlobalPoint(getGlobalPoint());
	el.addError(super.verify());

	return el;
    }

    /**
       全てのUDF リビジョンにおけるFile Entryの共通部分の検証を行う。
       
       @param fe  検証対象となるFile Entry のインスタンス。
       @param category エラーカテゴリ。
       
       @return エラーリストインスタンス。
    */
    static public UDF_ErrorList verifyBase(com.udfv.ecma167.UDF_desc261 fe, short category) throws UDF_Exception {

	UDF_ErrorList el = new UDF_ErrorList();
	
	
	// 全体のサイズは論理セクタ以下でなければならない
	if(UDF_Env.LBS < fe.getSize()){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "",
			 "The total length of a File Entry shall not exceed the size of one Logical Block.",
			 "2", String.valueOf(fe.getSize()), String.valueOf(UDF_Env.LBS)));
	}
	
	// RecordFormat は0 でなければならない
	if(fe.getRecordFormat().getIntValue() != 0){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "RecordFormat", "Shall be set to ZERO.",
			 "2.3.6.1", fe.getRecordFormat().getIntValue(), 0));
	}
	
	// RecordDisplayAttribute も0 でなければならない
	if(fe.getRecordDisplayAttr().getIntValue() != 0){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "RecordDisplayFormat", "Shall be set to ZERO.",
			 "2.3.6.2", fe.getRecordDisplayAttr().getIntValue(), 0));
	}
	
	// RecordLength も0 でなければならない
	if(fe.getRecordLen().getIntValue() != 0){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "RecordLength", "Shall be set to ZERO.",
			 "2.3.6.3", fe.getRecordLen().getIntValue(), 0));
	}
	
	//　ユーザーIDはユーザーが明示しない限り2^32-1を設定しておくべき　//
	long uid = fe.getUid().getLongValue();
	if (uid != 4294967295L) {

	    el.addError(
	        new UDF_Error(
		    category, UDF_Error.L_CAUTION, "Uid",
		    "For operating systems that do not support the concept of a user identifier the implementation shall set this field to 2^32 - 1 to indicate an invalid UID, unless otherwise specified by the user.",
		    "3.3.3.1",
		    uid,
		    4294967295L
		)
	    );
	}

	//　グループIDはユーザーが明示しない限り2^32-1を設定しておくべき　//
	long gid = fe.getGid().getLongValue();
	if (gid != 4294967295L) {

	    el.addError(
	        new UDF_Error(
		    category, UDF_Error.L_CAUTION, "Gid",
		    "For operating systems that do not support the concept of a group identifier the implementation shall set this field to 2^32 - 1 to indicate an invalid GID, unless otherwise specified by the user.",
		    "3.3.3.2",
		    gid,
		    4294967295L
		)
	    );
	}

	
	el.addError(fe.getImplId().verify("ImplementationIdentifier"));
	
	//　処理速度を考えるなら、まずExtentdedAttributes フィールドを消費せよ　//
	int len_of_ea = fe.getLenOfExtendedAttr().getIntValue();
	if (0 < fe.getExtendedAttrICB().getExtentLen().getIntValue() && 0 == len_of_ea) {

	    el.addError(
	        new UDF_Error(
		    category, UDF_Error.L_CAUTION, "ExtendedAttributes",
		    "Certain extended attributes should be recorded in this field of the FileEntry for performance reasons. Other extended attributes should be recorded in an ICB pointed to by the field ExtendedAttributeICB.",
		    "3.3.3.5"
		)
	    );
	}
	
	//　Extended Attributes の調査　//
	int ftype = fe.getICBFileType();
	if (len_of_ea != 0) {

	    UDF_ElementBase [] elem = fe.getExtendedAttr().getChildren();
	    for (int idx = 0, max = elem.length; idx < max; idx++) {

		if (!(elem[idx].getName().equals(com.udfv.ecma167.UDF_attr12.getUDFClassName()))) {
		    continue;
		}
		
		if(ftype != 6 && ftype != 7){
		    
		    UDF_Error err = new UDF_Error
			(category, UDF_Error.L_ERROR, "FileType",
			 "If and only if a file has a DeviceSpecificationExtendedAttribute associated with it, " +
			 "the contents of the FileType field in the icbtag structure be set to 6" +
			 "(indicating a block special device file), OR 7(indicating a character special device file).",
			 "3.3.4.4", String.valueOf(ftype), "6 or 7");
		    err.setRName("ICBTag");
		    el.addError(err);
		}

		com.udfv.ecma167.UDF_attr12 attr12 = (com.udfv.ecma167.UDF_attr12)elem[idx];
//		el.addError(err);
	    }
	}
	return el;
    }
    public void postReadHook(UDF_RandomAccess f) throws IOException, UDF_Exception{
	super.postReadHook(f);
	//UDF 2.1.5.2
	getImplId().extractSuffixAsUDFImplIdSuffix();

    }
}

