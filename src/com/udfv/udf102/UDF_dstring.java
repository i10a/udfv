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
//import com.udfv.udf200.*;

public class UDF_dstring extends com.udfv.ecma167.UDF_dstring
{
    /**
  	@param elem 親
	@param prefix ネームスペース
	@param name 名前
    */
    public UDF_dstring(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name);
    }
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	if(env.udf_revision != 0x102)
	    return super.verify();
	
	el.addError(verifyBase(UDF_Error.C_UDF102));
	el.setGlobalPoint(getGlobalPoint());
	el.addError(super.verify());
	
	return el;
    }
    
    /**
       UDF 全リビジョン共通の検証を行う。
       
       @param category エラーカテゴリ。
       @return エラーリストインスタンス。
    */
    protected UDF_ErrorList verifyBase(short category) throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	final String cause   = "Compression ID";
	final String basemsg = "The CompressionID shall identify the compression algorithm used to compress the CompressedBitStream field.";
	
	
	if(getEncoding() == null) {
	    el.addError(
	        new UDF_Error(
	            category, UDF_Error.L_ERROR, cause,
	            "The character set used by UDF for the structures defined in this document is the CS0 character set.",
	            "2.1.1"
	        )
	    );
	    return el;
	}
	else{
	    
	    int cmpid = getData()[0];
	    final String strcmpid = String.valueOf(cmpid);
	    
	    
	    // 0-7 は予約 但し0の場合、長さが0であればＯＫ
	    if((getLength() != 0 && cmpid == 0) || (0 < cmpid && cmpid < 8)){
		
		el.addError(new UDF_Error
			    (category, UDF_Error.L_ERROR, cause, basemsg + "\n0-7: Reserved", "2.1.1", strcmpid, ""));
	    }
	    
	    // 9-15 も予約
	    if(8 < cmpid && cmpid < 16){
		
		el.addError(new UDF_Error
			    (category, UDF_Error.L_ERROR, cause, basemsg + "\n9-15: Reserved", "2.1.1", strcmpid, ""));
	    }
	    
	    // 17-253 も予約
	    if(16 < cmpid && cmpid < 254){
		
		el.addError(new UDF_Error
			    (category, UDF_Error.L_ERROR, cause, basemsg + "\n17-253: Reserved", "2.1.1", strcmpid, ""));
	    }
	    
	    // 長さが0 のとき、その内容は全て0でなければならない
	    if(getLength() == 0){
		
		if(!UDF_Util.isAllZero(getData())){
		    
		    el.addError(new UDF_Error
				(category, UDF_Error.L_ERROR, cause,
				 "A zero length string shall be recorded by setting the entire dstring field to all zeros.",
				 "2.1.3"));
		}
	    }
	    //compression codeが1バイトだけというのは許されない
	    if(getLength() == 1){
		el.addError(new UDF_Error
			    (category, UDF_Error.L_ERROR, cause,
			     "A zero length string shall be recorded by setting the entire dstring field to all zeros.",
			     "2.1.3"));
	    }
	}
	
	
	// #FFFE または#FEFF が含まれていてはならない
	byte[] data = getData();
	for(int i = 1; (i + 1) < data.length; i++){
	    
	    int val1 = UDF_Util.b2i(data[i]);
	    int val2 = UDF_Util.b2i(data[i + 1]);
	    
	    if((val1 == 0xff && val2 == 0xfe) || (val1 == 0xfe && val2 == 0xff)){
		
		el.addError(new UDF_Error
			    (category, UDF_Error.L_ERROR, "Compressed Bit Stream[" + i + "]",
			     "The Unicode byte-order marks, #FEFF and #FFFE, shall not be used.", "2.1.1"));
		break;//i++;
	    }
	    else if(val2 != 0xfe && val2 != 0xff)
		i++;
	}
	
	return el;
    }
    
//end:
};
