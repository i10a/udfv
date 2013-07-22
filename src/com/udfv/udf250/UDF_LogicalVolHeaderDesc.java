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
