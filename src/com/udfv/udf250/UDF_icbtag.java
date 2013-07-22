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

public class UDF_icbtag extends com.udfv.udf201.UDF_icbtag
{

    /**
      @param elem 親
      @param prefix ネームスコープ
      @param name 名前
    */
    public UDF_icbtag(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? "UDF_icbtag" : name);
    }

    static public final int T_METADATA_FILE = 250;
    static public final int T_METADATA_MIRROR = 251;
    static public final int T_METADATA_BITMAP = 252;

    /**
      UDF 2.50規格でICB tag の検証を行います。
    */
    public UDF_ErrorList verify() throws UDF_Exception{
        UDF_ErrorList el = new UDF_ErrorList();
	
	
	if(env.udf_revision != 0x250)
	    return super.verify();
	

        int flags_value = getFlags().getIntValue();
        //　Non-relocatable は０であることが推奨　//
        if (0 != (flags_value & (1 << 4))) {

            el.addError(
                new UDF_Error(
                    UDF_Error.C_UDF250, UDF_Error.L_CAUTION, "Flags(Non-relocatable)",
                    "Should be set to ZERO unless required.",
                    "2.3.5.4"
               )
            );
        }
	
        int strategy = getStrategyType().getIntValue();
        //　UDF 2.50でも使用されていないファイルタイプがあります　//
        int filetype = getFileType().getIntValue();
        if (252 < filetype && filetype < 256) {

            el.addError(
                new UDF_Error(
                    UDF_Error.C_UDF250, UDF_Error.L_ERROR, "FileType",
                    "File types 253 to 255 shall not be used.",
                    "2.3.5.2"
                )
            );
        }

        //　StrategyTypeが 4のときには ParentICBLocationは使用されません　//
        UDF_lb_addr icb = getParentICBLoc();
        if (4 == strategy) {
            if (0 != icb.getLogicalBlockNumber().getIntValue() || 0 != icb.getPartReferenceNumber().getIntValue()) {

                el.addError(
                    new UDF_Error(
                        UDF_Error.C_UDF250, UDF_Error.L_ERROR, "ParentICBLocation",
                        "For strategy 4 this field shall not be used and contain all zero bytes.",
                        "2.3.5.3"
                   )
                );
            }
        }

	el.setRName("ICB Tag");
	el.setGlobalPoint(getGlobalPoint());
        el.addError(super.verify());

        return el;
    }
}
