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
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
import com.udfv.exception.*;
import com.udfv.access.*;
import com.udfv.core.*;
import com.udfv.util.*;

/**
   DescTagを持つデスクリプタの基底となるクラス。
 */
abstract public class UDF_CrcDesc extends UDF_Element
{
    public UDF_CrcDesc(UDF_Element elem, String preifx, String name){
	super(elem, preifx, name);
	//	setViewGlobalPoint(true);
    }
    protected boolean hasGlobalPoint(){
	return true;
    }
    protected boolean hasAttrPartSubno(){
	return true;
    }

    public static UDF_CrcDesc genCrcDesc(UDF_RandomAccess f, UDF_Element parent, String prefix, String tagName)
	throws UDF_Exception, IOException{
	try{
	    int tagid = f.readUint16();
	    f.seek(-2, UDF_RandomAccess.SEEK_CUR);
	    return genCrcDesc(tagid, parent, prefix, tagName);
	}
	catch(UDF_DataException ue){
	    System.err.println("gp=" + f.getAbsPointer() + "(lsn=" + f.getAbsPointer() / UDF_Env.LBS + ")");
	    throw ue;
	}
	catch(Exception e){
	    e.printStackTrace();
	}
	return null;
    }
    public static UDF_CrcDesc genCrcDesc(int tagid, UDF_Element parent, String prefix, String tagName) throws UDF_Exception{
	try{
	    return (UDF_CrcDesc) genElement("UDF_desc"+tagid, parent, prefix, tagName);
	}
	catch(ClassNotFoundException e){
	    throw new UDF_DataException(parent, "bad tag id:" + tagid);
	}
    }
    
    abstract public UDF_tag getDescTag();
    abstract public void setDescTag(UDF_tag tag);
    abstract public int getFixedTagId();
    
    /**
       CRC-16 を計算します。
       
       @return 計算したCRC-16 の値。
               内部的に例外が発生すると、-1 を返します。
    */
    public int calcCRC(){
	
	try{
	    int  size   = getSize();
	    int  crclen = getDescTag().getDescCRCLen().getIntValue();
	    long readsz = 0;
	    UDF_RandomAccessBuffer rab = new UDF_RandomAccessBuffer(size);
	    byte[] descbuf = new byte[size];
	    
	    
	    writeTo(rab);
	    rab.seek(0);
	    readsz = rab.read(descbuf);
	    
	    CRC16 crc16 = new CRC16();
	    crc16.update(descbuf, 16, crclen);
	    return crc16.getValue();
	}
	catch(IOException e){
	    return -1;
	}
	catch(UDF_Exception e){
	    return -1;
	}
    }
    /**
       CRC-16 を計算し設定します。またチェックサムも計算しなおします。
     */
    public void recalcCRC(){
	if(getDescTag().getDescCRC().getIntValue() != calcCRC()){
	    debugMsg(3, "CRC " + getDescTag().getDescCRC().getIntValue() + "=>" + calcCRC());
	    getDescTag().getDescCRC().setValue(calcCRC());
	}
	getDescTag().recalcChecksum();
    }

    /**
       CRC が正しく設定されているか否か。

       @return true 正しく設定されている。
       @return false 間違った値が設定されている。
    */
    public boolean isCRC( ) {
        return (calcCRC() == getDescTag().getDescCRC().getIntValue());
    }

    public void recalc(short type, UDF_RandomAccess f){
	super.recalc(type, f);

	if(type == RECALC_CRC)
	    recalcCRC();
    }

    public UDF_ErrorList verify() throws UDF_Exception{
	UDF_ErrorList el = new UDF_ErrorList();
	UDF_tag  tag     = getDescTag();
	// CRC 以外の検証
	el.addError(tag.verify());

	el.addError(verifyCRC());
	return el;
    }

    public UDF_ErrorList verifyCRC() throws UDF_Exception{

	UDF_ErrorList el = new UDF_ErrorList();
	UDF_tag  tag     = getDescTag();
	short category   = UDF_Error.C_ECMA167;
	
	
	
	
	try{
	    int size   = getSize();
	    int crclen = tag.getDescCRCLen().getIntValue();
	    
	    // CRCLength がディスクリプタのサイズより長い
	    if(size - 16 < crclen){
		
		el.addError(new UDF_Error
			    (category, UDF_Error.L_CAUTION,
			     "Descriptor CRC Length",
			     "This filed specifies how many bytes were used in calculating the Descriptor CRC. " +
			     "It should be set to actual descriptor size or fewer.",
			     "3/7.2.7", String.valueOf(crclen), String.valueOf(size - 16)));
	    }
	    
	    // この検証のみUDF 以上の検証
	    if(tag.getTagId().getIntValue() != 264)
		el.addError(verifyCRCLenForUDF());
	    
	    // CRCLength が0 のとき、CRC が設定されていてはならない
	    if(crclen == 0){
		
		if(tag.getDescCRC().getIntValue() != 0){
		    
		    el.addError(new UDF_Error
				(category, UDF_Error.L_ERROR, "Descriptor CRC",
				 "Implementations can avoid calculating CRC by setting the Descriptor CRC Length to 0," +
				 "as then the Descriptor CRC shall be 0.",
				 "3/7.2.6", String.valueOf(tag.getDescCRC().getIntValue()), ""));
		}
	    }
	    else if(!isCRC()) {  // ズバリCRC の値が正しくない
		
		el.addError(new UDF_Error
			    (category, UDF_Error.L_ERROR, "Descriptor CRC", 
			     "This field shall specify the CRC of the bytes of the descriptor starting at the first byte after the descriptor tag. " +
			     "The number of bytes shall be specified by the Descriptor CRC Length field. " +
			     "The CRC shall be 16 bits long and be generated by the CRC_ITU_T polynomial(see ITU-T V.41):",
			     "3/7.2.6",
			     String.valueOf(tag.getDescCRC().getIntValue()),
			     String.valueOf(calcCRC())));
	    }

	    el.setRName("Descriptor Tag");
	}
	catch(Exception e){
	    
	    e.printStackTrace();
	}
	
	el.setGlobalPoint(getGlobalPoint());
	return el;
    }
    
    /**
       タグのTag Identifier フィールドを検証します。
       検証をパスしない場合、エラーにはカテゴリ・レベル・原因・メッセージの４つが設定されます。
       
       @return 検証をパスしない場合、エラーインスタンスが返ります。
               そうでない場合、NOERR インスタンスが返ります。
    */
    public UDF_Error verifyId(){
	
	if(getDescTag().getTagId().getIntValue() != getFixedTagId()){
	    
	    UDF_Error err = new UDF_Error
		(UDF_Error.C_ECMA167, UDF_Error.L_ERROR, "Tag Identifier",
		 "This field shall be set to " + getFixedTagId() + ".");
	    err.setGlobalPoint(getGlobalPoint());
	    err.setRName("Descriptor Tag");
	    return err;
	}
	
	return new UDF_Error();
    }
    
    /**
       タグのCRC Length をUDF レベルの検証を行う。
       
       @return エラーインスタンス。
    */
    private UDF_Error verifyCRCLenForUDF(){
	
	int size   = getSize();
	int crclen = getDescTag().getDescCRCLen().getIntValue();
	short category = 0;
	
	switch(env.udf_revision){
	case 0x102: category = UDF_Error.C_UDF102; break;
	case 0x150: category = UDF_Error.C_UDF150; break;
	case 0x200: category = UDF_Error.C_UDF200; break;
	case 0x201: category = UDF_Error.C_UDF201; break;
	case 0x250: category = UDF_Error.C_UDF250; break;
	case 0x260: category = UDF_Error.C_UDF260; break;
	}
	    
	// CRCLength がディスクリプタのサイズより長い
	if(size - 16 != crclen){
	    
	    return new UDF_Error
		(category, UDF_Error.L_ERROR,
		 "Descriptor CRC Length",
		 "CRCs shall be supported and calculated for each descriptor. " +
		 "The value of this field shall be set to (Size of the Descriptor) - (Length of Descriptor Tag).",
		 "2.2.1.2, 2.3.1.2", String.valueOf(crclen), String.valueOf(size - 16));
	}
	
	return new UDF_Error();
    }
    
    /* --- verifyId() に置き換えました。--- */
    /*
       TAGIDが期待したものと同じかどうかを調べる。
       違う場合は UDF_DescTagExceptionをスローする。
     */
    /*    public void checkTagId(int id) throws UDF_DescTagException{
	if(getDescTag().getTagId().getIntValue() != id)
	    throw new UDF_DescTagException(this, "Bad TAG Id: " + getDescTag().getTagId().getIntValue(),
					   UDF_Exception.T_BASIC, UDF_DescTagException.C_BADTAGID, 
					   getDescTag().getTagId().getIntValue());
    }*/
    
    public void postSetDefaultValueHook(){
	getDescTag().getTagId().setValue(getFixedTagId());
    }
};
