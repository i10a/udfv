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
import com.udfv.exception.*;
import com.udfv.access.*;
import com.udfv.ecma167.*;

public class UDF_desc263 extends com.udfv.ecma167.UDF_desc263
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
      UDF 1.02規格のUnallocated Space Entry の検証を行います。
    */
    public UDF_ErrorList verify() throws UDF_Exception {
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	if(env.udf_revision != 0x250)
	    return super.verify();
	
	el.addError(verifyBase(UDF_Error.C_UDF102));
        el.setRName("Unallocated Space Entry");
        el.setGlobalPoint(getGlobalPoint());
        el.addError(super.verify());
	
	return el;
    }
    
    /**
       全UDF 規格共通のUnallocated Space Entry の検証を行います。
       
       @param category エラーカテゴリ。
       @return エラーリストインスタンス。
    */
    public UDF_ErrorList verifyBase(short category) throws UDF_Exception {

        UDF_ErrorList el = new UDF_ErrorList();
	
	
        int ad_type = getICBFlags() & 0x0003;
	UDF_Error err = getICBTag().verifyAdFlag(UDF_icbtag.SHORT_AD);
	if (err.isError()) {

	    err.setCategory(category);
	    err.setRName("ICBTag");
	    err.setMessage("Only Short Allocation Descriptors shall be used.");
	    err.setRefer("2.3.7.1");
            el.addError(err);
        }

        UDF_ElementBase [] elem = getAllocDesc().getChildren();
        int pre = -1;
        long prelen = -1;

        for (int i = 0, max = elem.length; i < max; i++) {
	    /*
            Node n = elem[i].getNode();
            if (n.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
	    */
            String tag_name = elem[i].getName();

            if (tag_name == null) {
                //　起こりえない？　//
                continue;
            }

            //　UDF_short_ad以外のADは認められていない　//
            if (!tag_name.equals("UDF_short_ad")) {
                //　こないはず　//
                el.addError(
                    new UDF_Error(
                        category, UDF_Error.L_ERROR, "AllocationDescriptors[" + i + "]",
                        "Only Short Allocation Descriptors shall be used.(" + tag_name + " is used)",
                        "2.3.7.1"
                    )
                );
            }
            

            UDF_AD ad = (UDF_AD) elem[i];
            int  flag = ad.getFlag();
            int  lbn  = ad.getLbn();
            long len  = ad.getLen();

            //　次のExtent集合が指定されている場合、そのdescriptorのサイズは１論理ブロックを超えてはならない　//
            if (3 == flag) {

                if (env.LBS < len) {

                     err = 
                        new UDF_Error(
                            category, UDF_Error.L_ERROR, "Extent Length",
                            "This next extent of allocation descriptors shall be limited to the length of one Logical Block.", 
                            "2.3.7.1",
                            0,
                            ad_type
                        );
                     err.setRName("AllocationDescriptors[" + i + "]");
                     el.addError(err);
                }
                //　終了へ　//
                break;
            }
            //　０と２は使用してはいけない　//
            if (1 != flag) {

                if (flag != 0 || (lbn != 0 && len != 0)){
                    err = ad.verifyFlag(1);
                    err.setRName("AllocationDescriptors[" + i + "]");
                    err.setMessage("For the allocation descriptors specified for the UnallocatedSpaceEntry the type shall be set to a value of 1 to indicate extent allocated but not recorded, or shall be set to a value of 3 to indicate the extent is the next extent of allocation descriptors.");
                    err.setRefer("2.3.7.1");
                    el.addError(err);
                }
            }

            //　領域がオーバーラップするのは論理的でない　//
            if (pre > lbn) {

                err = 
                    new UDF_Error(
                        category, UDF_Error.L_ERROR, "Extent Position",
                        "No overlapping AllocationDescriptors shall exist in the table.",
                        "2.3.7.1"
                    );
                el.setRName("AllocationDescriptors[" + i + "]");
                el.addError(err);
            }
            //　領域が連続しているときに分割されている必然性はない　//
            else
            if (pre == lbn && prelen < (0x40000000 - env.LBS)) {

                err =
                    new UDF_Error(
                        category, UDF_Error.L_ERROR, "Extent Position",
                        "Adjacent AllocationDescriptors shall not be contiguous.",
                        "2.3.7.1"
                    );
                el.setRName("AllocationDescriptors[" + i + "]");
                el.addError(err);
            }

            //　今回の領域の終わりを計算しておきます　//
            pre = lbn + (int)(len / env.LBS);
            prelen = len;
        }
	
        //　Unallocated Space Entryのサイズは１論理ブロックを超えてはならない　//
        if (getSize() > env.LBS) {

            el.addError(
                new UDF_Error(
                    category, UDF_Error.L_ERROR, "",
                    "The total length of an Unallocated Space Entry shall not exceed the size of one Logical Block.",
                    "2"
                )
            );
        }
	
        return el;
    }

}
