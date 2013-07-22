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

/**
  Partition Integrity Entry
*/
public class UDF_desc265 extends com.udfv.ecma167.UDF_desc265
{
    /**
      コンストラクタ

      @param elem 親
      @param prefix ネームスコープ
      @param name 名前
    */
    public UDF_desc265(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
    }

    /**
      UDF 規格ではこのdescriptorは使用しないので必ずエラーになります。
    */
    public UDF_ErrorList verify() throws UDF_Exception {

        UDF_ErrorList el = new UDF_ErrorList();

	if(env.udf_revision != 0x102)
	    return super.verify();

	el.addError(
            new UDF_Error(
                UDF_Error.C_UDF102, UDF_Error.L_ERROR, "Partition Integrity Entry ",
                "(See ECMA 167 4/14.13). With the functionality of the Logical Volume Integrity Descriptor (see section 2.2.6) this descriptor is not needed, and therefore this descriptor shall not be recorded.",
                "2.3.9"
            )
        );
        el.addError(super.verify());
        el.setGlobalPoint(getGlobalPoint());

        return el;
    }
}
