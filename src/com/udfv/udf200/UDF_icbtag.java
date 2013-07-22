/*
*/
package com.udfv.udf200;

import java.io.*;
import java.util.*;
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.exception.*;
import com.udfv.access.*;
import com.udfv.udf200.*;

public class UDF_icbtag extends com.udfv.udf150.UDF_icbtag
{

  /**
	@param elem 親
	@param name 名前
  */
  public UDF_icbtag(UDF_Element elem, String prefix, String name){
     super(elem, prefix, name == null ? "UDF_icbtag" : name);	
  }

//begin:add your code here
    public static final int T_RTFILE = 249;
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	if(env.udf_revision != 0x200)
	    return super.verify();
	
	el.addError(verifyBase(UDF_Error.C_UDF200));
	
        int flags_value = getFlags().getIntValue();
        //　Non-relocatable は０であることが推奨　//
        if (0 != (flags_value & (1 << 4))) {

            el.addError(
                new UDF_Error(
                    UDF_Error.C_UDF200, UDF_Error.L_WARNING, "Flags(Non-relocatable)",
                    "Should be set to ZERO.",
                    "2.3.5.4"
               )
            );
        }
	
        // UDF2.00 では249 は特に規定されていない
	int filetype = getFileType().getIntValue();
	if (filetype == 249) {
	    
	    el.addError(new UDF_Error
			(UDF_Error.C_UDF200, UDF_Error.L_CAUTION, "FileType",
			 "File type 249 is not defined in UDF2.00(see UDF2.01 2.3.5.2.1).",
			 "2.3.5.2"));
	}
	
	el.setRName("ICB Tag");
	el.setGlobalPoint(getGlobalPoint());
	el.addError(super.verify());
	
	return el;
    }
    
//end:
};
