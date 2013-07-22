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
import com.udfv.ecma167.*;

public class UDF_desc257 extends com.udfv.udf200.UDF_desc257
{

  /**
	@param elem 親
	@param name 名前
  */
  public UDF_desc257(UDF_Element elem, String prefix, String name){
     super(elem, prefix, name == null ? "UDF_desc257" : name);	
  }

//begin:add your code here
    public UDF_ErrorList verify() throws UDF_Exception{
        
        UDF_ErrorList el = new UDF_ErrorList();
        
        
	if(env.udf_revision != 0x201)
            return super.verify();
        
        el.addError(verifyBase(UDF_Error.C_UDF201));
        
        el.setRName("File Identifier Descriptor");
        el.addError(super.verify());
        el.setGlobalPoint(getGlobalPoint());
        
        return el;
    }
    
    /**
       File Identifier のCompression ID 周りの検証を行う。
       このメソッドは、verifyBase() からコールされることが期待されている。
       
       @param category エラーカテゴリ。
       @return エラーリストインスタンス。
    */
    protected UDF_ErrorList verifyCompId(short category) throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
        int file_characteristics = getFileChar().getIntValue();
	int l_fi = getLenOfFileId().getIntValue();
	
	
	if (0 != (file_characteristics & PARENT) || l_fi == 0)
	    return el;
	
	int compressionID = UDF_Util.b2i(getFileId().getData()[0]);

	final String msg_compressionId = "Leave the remaining bytes of the File Identifier unchanged";
	final String msg_compressionId8  = "If the compression ID of the File Identifier is 8, rewrite the compression ID to 254. ";
	final String msg_compressionId16 = "If the compression ID of the File Identifier is 16, rewrite the compression ID to 255. ";
	final String ERRMSG = 
	    "When deleting a File Identifier Descriptor an implementation may change the Compression ID to 0xFE " +
	    "and set the next four bytes, or to 0xFF and set the next eight bytes of the identifier to the byte " +
	    "offset of the FID within the directory as a Uint32 or Uint64 value. ";
	    
	if (0 != (file_characteristics & DELETED)) {
	    
	    //　deleteビットが存在するときの CompressionIDは 254もしくは 255になります　//
	    if (compressionID != 254 && compressionID != 255) {
		
		if(compressionID == 8){
		    
		    el.addError(new UDF_Error
				(category, UDF_Error.L_CAUTION, "FileIdentifier", msg_compressionId8 + msg_compressionId,
				 "2.3.4.2", Integer.toString(compressionID), "254"));
		}
		else if(compressionID == 16){
		    
		    el.addError(new UDF_Error
				(category, UDF_Error.L_CAUTION, "FileIdentifier", msg_compressionId16 + msg_compressionId,
				 "2.3.4.2", Integer.toString(compressionID), "255"));
		}
		else{
		    ; // 規定されていない
		}
	    }
	}
	else {
	    // FID でのみcompressionID 254 または255 の使用が許可されているが、
	    // Delete bit が立っていなければならない
	    if (compressionID == 254 || compressionID == 255) {
		
		el.addError(new UDF_Error
			    (category, UDF_Error.L_ERROR, "FileIdentifier",
			     "Compression IDs 254 and 255 shall only be used in FIDs where the deleted bit is set to ONE.",
			     "2.1.1"));
	    }
	}
	
	return el;
    }
    
//end:
};
