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
import com.udfv.ecma167.*;
import com.udfv.encoding.*;

public class UDF_desc257 extends com.udfv.udf150.UDF_desc257
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
        final short category = UDF_Error.C_UDF200;
	
        
	if(env.udf_revision != 0x200)
            return super.verify();
        
        el.addError(verifyBase(category));
	
	el.setRName("File Identifier Descriptor");
	el.setGlobalPoint(getGlobalPoint());
        el.addError(super.verify());
        
        return el;
    }
    
    protected UDF_ErrorList verifyBase(short category) throws UDF_Exception {

        UDF_ErrorList el = new UDF_ErrorList();
	
	
        el.addError(super.verifyBase(category));

        
        UDF_bytes file_identifier = getFileId();
        int file_length = getLenOfFileId().getIntValue();

        //　File Identifier のエンコーディングを読み取ります　//
//        UDF_Encoding enc = file_identifier.getEncoding();


	el.addError(verifyCompId(category));


        //　フラグ EXTENTErased が存在するときはADのフラグが１(not record but allocated)であるべきです　//
        el.addError(getICB().verify("ICB"));
        
        //　ユニークIDは０はルートディレクトリ、１〜１５はMacintosh で予約されているため使用しません　//
        com.udfv.udf102.UDF_long_ad_ADImpUse adimpuse = (com.udfv.udf102.UDF_long_ad_ADImpUse)(getICB().getImplUse().getChildren()[0]);
      
        //　※０は　//
        long unique = UDF_Util.b2uint32(adimpuse.getImpUse().getData(), 0);
        if (0 < unique && unique < 16) {

            UDF_Error err = new UDF_Error(category, UDF_Error.L_ERROR, "ICB.ADImplUse.UDF Unique ID",
                                         "The Next UniqueID value is initialized to 16 because the value 0 is reserved for the root directory and system stream directory objects and the values 1-15 are reserved for use in Macintosh implementations.",
                                         "2.3.4.3 -> 3.2.1.1"
            );
            err.setRecordedValue(Long.toString(unique));
            el.addError(err);
        }

        /*
          ImplementationUse が使用されているとき、最初の32バイトは
          Implementation IdentifierSuffix を持つEntity Identifier が
          保存されている必要があります。
          ※Implementation IdentifierSuffix が持つIdentifierは、
            実装ごとに異なるので検証しないでおきます。

          基本的なチェックはsuper.verify()で実行されます。
	   
	   05/05/10 seta: ImplementationUse が使用されているとき、
	   lenOfImplUseは32以上でなければならない、という検証を追加
        */
	int L_IU = getLenOfImplUse().getIntValue();
	if(L_IU != 0 && L_IU < 32){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "LengthofImplementationUse",
			 "If this field doesn't contain 0, it shall contain at least 32 as required by 2.3.4.5.",
			 "2.3.4.4", String.valueOf(L_IU), "<=32"));
	}
	/*
        if (false) {
            try {
                //byte [] data = getImplUse().getData();
                UDF_RandomAccessBuffer rab = getImplUse().genRandomAccessBytes();//new UDF_RandomAccessBuffer(data);
                rab.seek(0);
                UDF_regid entity_identifier = (UDF_regid) createElement("UDF_regid", null, "UDF_regid");
                entity_identifier.readFrom(rab);

                el.addError(entity_identifier.verifyId(env.Implementation_ID));

                rab.close();
                rab = null;
            }
            catch(IOException e) {
                throw new UDF_InternalException(this, e.toString());
            }
        }
	*/
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

	final String ERRMSG = 
	    "When deleting a File Identifier Descriptor an implementation may change the Compression ID to 0xFE " +
	    "and set the next four bytes, or to 0xFF and set the next eight bytes of the identifier to the byte " +
	    "offset of the FID within the directory as a Uint32 or Uint64 value. ";
	    
	if (0 != (file_characteristics & DELETED)) {
	    
	    //　deleteビットが存在するときの CompressionIDは 254もしくは 255になります　//
	    if (compressionID != 254 && compressionID != 255) {
		
		el.addError(
			    new UDF_Error(
					  category, UDF_Error.L_CAUTION, "FileIdentifier",
					  ERRMSG,
					  "2.3.4.2",
					  Integer.toString(compressionID),
					  "254 or 255"
					 )
			   );
	    }
	    //　254または 255 のとき、 L_FIはそれぞれ 5または 9になります　//
	    else if(compressionID == 254){
		
		if(l_fi != 5){
		    
		    el.addError(new UDF_Error
				(category, UDF_Error.L_ERROR, "LengthOfFileIdentifier",
				 ERRMSG + "L_FI shall be set to 5.", "2.3.4.2", String.valueOf(l_fi), "5"));
		}
	    }
	    else{
		
		if(l_fi != 9){
		    
		    el.addError(new UDF_Error
				(category, UDF_Error.L_ERROR, "LengthOfFileIdentifier",
				 ERRMSG + "L_FI shall be set to 9.", "2.3.4.2", String.valueOf(l_fi), "9"));
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
	    
	    // このチェックは2.1.1 で実装済み。且つ、2.3.4.2 ではこのことについて言及していない
	    //　存在しないときは 8か16になります　//	    
/*	    if (compressionID != 8 && compressionID != 16) {
		
		el.addError(
			    new UDF_Error(
					  category, UDF_Error.L_CAUTION, "FileIdentifier",
					  ERRMSG,
					  "2.3.4.2",
					  Integer.toString(compressionID),
					  "8 or 16"
					 )
			   );
	    }
*/
	}
	
	return el;
    }
    
//end:
};
