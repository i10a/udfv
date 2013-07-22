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

public class UDF_LogicalVolHeaderDesc extends com.udfv.ecma167.UDF_LogicalVolHeaderDesc
{
    /**
      コンストラクタ

      @param elem 親
      @param prefix ネームスコープ
      @param name 名前
    */
    public UDF_LogicalVolHeaderDesc(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
    }

    private static final long LOWER_MASK = ((long)0x00000000ffffffff);
    private static final long LOWER_MAX  = ((long)0x00000000ffffffff);

    /**
    */
    public UDF_ErrorList verify() throws UDF_Exception {

        UDF_ErrorList el = new UDF_ErrorList();
	
	
	if(env.udf_revision != 0x250)
	    return super.verify();
	
        long unique = getUniqueId().getLongValue();
        long lower = unique & LOWER_MASK;

        //　０はルート、１〜１４はMacintosh で予約されている　//
        if (lower < 16 ) {

            el.addError(
                new UDF_Error(
                    UDF_Error.C_UDF250, UDF_Error.L_ERROR, "UniqueID",
                    "The Next UniqueID value is initialized to 16 because the value 0 is reserved for the root directory and system stream directory objects and the values 1-15 are reserved for use in Macintosh implementations."
                    + " ... "
                    + "Whenever the lower 32-bits of the Next UniqueID value reach #FFFFFFFF, the next increment is performed by incrementing the upper 32-bits by 1, as would be expected for a 64-bit value, but the lower 32-bits “wrap” to 16 (the initialization value).",
                    "3.2.1.1"
                )
            );
        }
        el.setRName("Logical Volume Header Descriptor");

        el.addError(super.verify());
	el.setGlobalPoint(getGlobalPoint());

        return el;
    }
}
