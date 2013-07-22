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

/**
  Allocation Extent Descriptor
*/
public class UDF_desc258 extends com.udfv.ecma167.UDF_desc258
{
    /**
      コンストラクタ

      @param elem 親
      @param prefix ネームスコープ
      @param name 名前
    */
    public UDF_desc258(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
    }

    /**
      UDF 2.50規格に準じた検証を行います。
    */
    public UDF_ErrorList verify() throws UDF_Exception {

        UDF_ErrorList el = new UDF_ErrorList();
        boolean error_flag = false;

	
	if(env.udf_revision != 0x250)
	    return super.verify();

        //　CRC 計算を行う範囲はLengthOfAllocationDescriptorの数を含んだ分になります　//
        UDF_tag tag = (UDF_tag)getDescTag();
        int crc_length = tag.getDescCRCLen().getIntValue();
        int length_of_ad = getLenOfAllocDesc().getIntValue();

        if (crc_length != 8 && crc_length != (8 + length_of_ad)) {

            el.addError(
                new UDF_Error(
                    UDF_Error.C_UDF250, UDF_Error.L_ERROR, "Descriptor Tag",
                    "The DescriptorCRCLength of the DescriptorTag should include the Allocation Descriptors following the Allocation Extent Descriptor. The DescriptorCRCLength shall be either 8 or 8 + LengthOfAllocationDescriptors.",
                    "2.3.11.1"
                )
            );
            error_flag = true;
        }

        //　PreviousAlloctionExtentLocation は使うべきではない　//
        int pre_loc = getPreviousAllocExtentLoc().getIntValue();
        if (0 != pre_loc) {

            el.addError(
                new UDF_Error(
                    UDF_Error.C_UDF250, UDF_Error.L_ERROR, "PreviousAlloctionExtentLocation",
                    "Shall be set to 0.",
                    "2.3.11.2",
                    pre_loc,
                    0
                )
            );
            error_flag = true;
        }

        if (error_flag) {
            el.setRName("Allocation Extent Descriptor");
        }

        //　Allocation Extent Descriptorのサイズは１論理ブロックを超えてはならない　//
        if (getSize() > env.LBS) {

            el.addError(
                new UDF_Error(
                    UDF_Error.C_UDF250, UDF_Error.L_ERROR, "Allocation Extent Descriptor",
                    "The length of an extent of allocation descriptors shall not exceed the logical block size.",
                    "2.3.11"
                )
            );
        }

        el.addError(super.verify());
        el.setGlobalPoint(getGlobalPoint());

        return el;
    }
}
