/*
*/
package com.udfv.udf201;

import java.io.*;
import java.util.*;
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.exception.*;
import com.udfv.access.*;
import com.udfv.udf201.*;

public class UDF_icbtag extends com.udfv.udf200.UDF_icbtag
{

  /**
	@param elem 親
	@param name 名前
  */
  public UDF_icbtag(UDF_Element elem, String prefix, String name){
     super(elem, prefix, name == null ? "UDF_icbtag" : name);	
  }

//begin:add your code here
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	if(env.udf_revision != 0x201)
	    return super.verify();
	
	el.addError(verifyBase(UDF_Error.C_UDF201));
	
        int flags_value = getFlags().getIntValue();
        //　Non-relocatable は０であることが推奨　//
        if (0 != (flags_value & (1 << 4))) {

            el.addError(
                new UDF_Error(
                    UDF_Error.C_UDF201, UDF_Error.L_CAUTION, "Flags(Non-relocatable)",
                    "Should be set to ZERO unless required.",
                    "2.3.5.4"
               )
            );
        }
	
        //　UDF 2.01でも使用されていないファイルタイプがあります　//
        int filetype = getFileType().getIntValue();
        if (249 < filetype && filetype < 256) {

            el.addError(
                new UDF_Error(
                    UDF_Error.C_UDF201, UDF_Error.L_CAUTION, "FileType",
                    "File types 250 to 255 shall not be used.",
                    "2.3.5.2"
                )
            );
        }
	
	el.setRName("ICB Tag");
	el.setGlobalPoint(getGlobalPoint());
	el.addError(super.verify());
	
	return el;
    }
//end:
};
