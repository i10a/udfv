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
package com.udfv.ecma167;

import java.io.*;
import java.util.*;
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.ecma167.*;
import com.udfv.exception.*;
import com.udfv.access.*;
import com.udfv.udf201.*;

/**
  Extended Attributes の抽象クラス。

*/
abstract public class UDF_attr extends UDF_Element
{
    /**
       @param elem 親
       @param name 名前
    */
    public UDF_attr(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name);
    }

    public static UDF_attr genAttr(UDF_RandomAccess f, UDF_Element parent, String prefix, String tagName) throws UDF_Exception, IOException, ClassNotFoundException{
	long attrid = f.readUint32();
	f.seek(-4, UDF_RandomAccess.SEEK_CUR);
	return genAttr((int)attrid, parent, prefix, tagName);
    }

    public static UDF_attr genAttr(int attrid, UDF_Element parent, String prefix, String tagName) throws UDF_Exception, IOException, ClassNotFoundException{
	UDF_attr attr = null;
	try{
	    String className = "UDF_attr" + attrid;
	    attr = (UDF_attr)UDF_Element.genElement(className, parent, prefix, tagName);
	    return attr;
	}
	catch(ClassCastException e){
	    System.err.println("CLASSCAST EXCEPTION: " + attr.getClass().getName());
	}
	return null;
    }

    abstract public UDF_uint32 getAttrType();
    abstract public UDF_uint8  getAttrSubtype();
    abstract public UDF_bytes  getReserved();
    abstract public UDF_uint32 getAttrLen();

    protected UDF_ErrorList verify(int attrtype, int subsec) throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	final short category = UDF_Error.C_ECMA167;
	
	
	// Attribute Type
	if(getAttrType().getLongValue() != attrtype){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Attribute Type",
			 "This field shall specify 1.", "4/14.10." + subsec + ".1",
			 String.valueOf(getAttrType().getLongValue()), String.valueOf(attrtype)));
	}
	
	// Sttribute Subtype はどのアトリビュートも全て１
	if(getAttrSubtype().getIntValue() != 1){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Attribute Subtype",
			 "This field shall specify 1. All other values are reserved for future standardisation.",
			 "4/14.10." + subsec + ".2", String.valueOf(getAttrSubtype().getIntValue()), "1"));
	}
	
	// Reserved
	if(!UDF_Util.isAllZero(getReserved().getData())){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Reserved",
			 "This field shall be reserved for future standardisation and all bytes shall be set to #00.",
			 "4/14.10." + subsec + ".3"));
	}
	
	// Attribute Length
	if(getAttrLen().getLongValue() % 4 != 0){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_WARNING, "Attribute Length",
			 "It is recommended that the extended attribute length be an integral multiple of 4.",
			 "4/14.10." + subsec + ".4", String.valueOf(getAttrLen().getLongValue()), ""));
	}
	
	return el;
    }

    /**
      UDF 2.50規格で定義されているImplementation Use Extended Attribute,
      Application Use Extended Attributeで使用されるチェックサム。<br>

      <em>
      本来ならudf250.UDF_attr を作成すべきだが、多重継承のできないJavaで
      その形式にすると不都合が多いため、ここに追加。
      </em>
    */
    public static int headerChecksum(byte [] bin) {

        //　ヘッダ部のチェックサム　//
        int checksum = 0;

        for(int count = 0; count < 48; count++) {
            checksum += UDF_Util.b2i(bin[count]);
        }
        return checksum;
    }

}
