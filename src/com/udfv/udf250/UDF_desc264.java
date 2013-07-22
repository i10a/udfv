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

/**
  Space Bitmap Descriptor 用クラス
*/
public class UDF_desc264 extends com.udfv.ecma167.UDF_desc264
{
    /**
      コンストラクタ

      @param elem 親
      @param prefix ネームスコープ
      @param name 名前
    */
    public UDF_desc264(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
    }
    
    /**
       Metadata Partition 固有のエラー項目を検証する。
       
       @param category エラーカテゴリ。
       @param refer    エラー参照番号文字列。
       @return エラーリストインスタンス。
    */
    public UDF_ErrorList verifyForMetadata(short category, String refer) throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	// Descriptor CRC は0でなければならない
	if(getDescTag().getDescCRC().getIntValue() != 0){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "DescriptorCRC",
			 "The descriptor tag fields Descriptor CRC and DescriptorCRCLength for this SBD shall be set to zero.",
			 refer, String.valueOf(getDescTag().getDescCRC().getIntValue()), "0"));
	}
	
	// Descriptor CRC Length も0でなければならない
	if(getDescTag().getDescCRCLen().getIntValue() != 0){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "DescriptorCRC",
			 "The descriptor tag fields Descriptor CRC and DescriptorCRCLength for this SBD shall be set to zero.",
			 refer, String.valueOf(getDescTag().getDescCRCLen().getIntValue()), "0"));
	}
	
	el.setGlobalPoint(getGlobalPoint());
	el.setRName("Space Bitmap Descriptor");
	return el;
    }
}
