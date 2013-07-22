/*
*/
package com.udfv.udf250;

import com.udfv.exception.*;
import com.udfv.access.*;
import com.udfv.exception.*;
import java.io.*;
import com.udfv.exception.*;
import com.udfv.access.*;
import com.udfv.exception.*;
import java.util.*;
import com.udfv.exception.*;
import com.udfv.access.*;
import com.udfv.exception.*;
import org.w3c.dom.*;
import com.udfv.exception.*;
import com.udfv.access.*;
import com.udfv.exception.*;
import org.apache.xerces.parsers.*;
import com.udfv.exception.*;
import com.udfv.access.*;
import com.udfv.exception.*;
import org.apache.xerces.dom.*;
import com.udfv.exception.*;
import com.udfv.access.*;
import com.udfv.exception.*;
import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.exception.*;
import com.udfv.access.*;
import com.udfv.exception.*;
import com.udfv.ecma167.*;

public class UDF_timestamp extends com.udfv.udf201.UDF_timestamp
{
    /**
        コンストラクタ

        @param elem 親
        @param prefix ネームスコープ
        @param name 名前
    */
    public UDF_timestamp(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
    }

    public UDF_ErrorList verify() throws UDF_Exception {

        UDF_ErrorList el = new UDF_ErrorList();
        boolean error_flag = false;
	
        int centiseconds = getCentiseconds();
        int microseconds = getMicroseconds();
        int hundreds_of_microseconds = getHundredsofMicroseconds();
	
	if(env.udf_revision != 0x250)
	    return super.verify();
	
	
	// Type は1でなければならない
	UDF_Error ret = verifyTypeOfTimeZone(1);
	if(ret.isError()){
	    
	    ret.setCategory(UDF_Error.C_UDF250);
	    el.addError(ret);
	}

        //　秒以下の単位は機種依存を避けるため０を設定すべし　//
        if (0 != centiseconds) {

            el.addError(
                new UDF_Error(
                    UDF_Error.C_UDF250, UDF_Error.L_ERROR, "Centiseconds",
                    "For operating systems that do not support the concept of centiseconds the implementation shall set this field to ZERO.",
                    "3.1.1.1"
                )
            );

            error_flag = true;
        }
        if (0 != hundreds_of_microseconds) {

            el.addError(
                new UDF_Error(
                    UDF_Error.C_UDF250, UDF_Error.L_ERROR, "HundredsofMicroseconds",
                    "For operating systems that do not support the concept of a hundreds of Microseconds the implementation shall set this field to ZERO.",
                    "3.1.1.2"
                )
            );

            error_flag = true;
        }
        if (0 != microseconds) {

            el.addError(
                new UDF_Error(
                    UDF_Error.C_UDF250, UDF_Error.L_ERROR, "Microseconds",
                    "For operating systems that do not support the concept of microseconds the implementation shall set this field to ZERO.",
                    "3.1.1.3"
                )
            );

            error_flag = true;
        }

        if (error_flag) {
            el.setRName("Timestamp");
        }
        el.addError(super.verify());
	el.setGlobalPoint(getGlobalPoint());

        return el;
    }
}
