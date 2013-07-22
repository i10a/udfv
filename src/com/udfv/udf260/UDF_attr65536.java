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
package com.udfv.udf260;

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

/**
Application&nbsp;Use&nbsp;Extended&nbsp;Attribute&nbsp;を表現するクラス。

*/
public class UDF_attr65536 extends com.udfv.udf250.UDF_attr65536
{
    /**
      コンストラクタ

      @param elem 親
      @param prefix ネームスコープ
      @param name 名前
    */
    public UDF_attr65536(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
    }


    /**
      Application Use Exteneded Attribute の検証。
    */
    public UDF_ErrorList verify() throws UDF_Exception{

        if(env.udf_revision != 0x260) {
            return super.verify();
        }

        UDF_ErrorList el = new UDF_ErrorList();

        int size = getSize();
        if (size > 49 && getApplicationUseLen().getIntValue() > 1) {

            //　チェックサム計算を行います　//
            byte [] bin = getBytes();

            int checksum = headerChecksum(bin);
            int recorded = UDF_Util.b2uint16(bin, 48);
            if (checksum != recorded) {

                el.addError(
                    new UDF_Error(
                        UDF_Error.C_UDF260, UDF_Error.L_ERROR, "Header Checksum",
                        "The structures defined in the following section contain a Header Checksum field."
                      + "This field represents a 16-bit checksum of the Application Use Extended Attribute header."
                      + "The fields AttributeType through ApplicationIdentifier inclusively represent the data covered by the checksum.",
                        "3.3.4.6", 
                        recorded,
                        checksum
                    )
                );
            }

            el.setRName("Application Use Extended Attribute");
        }

        el.addError(super.verify());

        return el;
    }
}
