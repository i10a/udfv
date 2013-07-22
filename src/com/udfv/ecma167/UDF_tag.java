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
import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.encoding.*;
import com.udfv.access.*;
import com.udfv.exception.*;
import com.udfv.ecma167.*;

/**
ECMA-167&nbsp;3/7.2&nbsp;Descriptor&nbsp;tag&nbsp;を表現するクラス。<br/>


<dl>

<dt>フィールド一覧</dt>
<dd>
<table border="1">
<tr><td>メンバ</td><td>型</td><td>サイズ</td></tr>
<tr><td><b>TagId</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td></tr>
<tr><td><b>DescVersion</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td></tr>
<tr><td><b>TagChecksum</b></td><td><b>UDF_uint8</b></td><td><i>1</i></td></tr>
<tr><td><b>Reserved</b></td><td><b>UDF_bytes</b></td><td><i>1</i></td></tr>
<tr><td><b>TagSerialNumber</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td></tr>
<tr><td><b>DescCRC</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td></tr>
<tr><td><b>DescCRCLen</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td></tr>
<tr><td><b>TagLoc</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
</table>
</dd>
</dl>
*/
public class UDF_tag extends UDF_Element 
{

    /**
	このオブジェクトのパッケージ名を除いたクラス名を取得する。

	@return クラス名
    */
    public static String getUDFClassName( ) {
        return "UDF_tag";
    }

    /**
	コンストラクタ。

	@param elem   親エレメント。
	@param prefix ネームスペース。
	@param name   エレメント名。
    */
    public UDF_tag(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }
    /**
	このUDFエレメントのサイズを返す。

	@return UDFエレメントのサイズ。
    */
    public int getSize(){
	return 0+2+2+1+reserved.getSize()+2+2+2+4;
    }

    /**
	このUDFエレメントのサイズをlong 型で返す。

	@return UDFエレメントのサイズ。
    */
    public long getLongSize(){
	return (long)0+2+2+1+reserved.getSize()+2+2+2+4;
    }
    private UDF_uint16 tagId;
    private UDF_uint16 descVersion;
    private UDF_uint8 tagChecksum;
    private UDF_bytes reserved;
    private UDF_uint16 tagSerialNumber;
    private UDF_uint16 descCRC;
    private UDF_uint16 descCRCLen;
    private UDF_uint32 tagLoc;

    /**
	tagIdを取得する。

	@return 取得したtagId を返す。
    */
    public UDF_uint16 getTagId(){return tagId;}
    /**
	descVersionを取得する。

	@return 取得したdescVersion を返す。
    */
    public UDF_uint16 getDescVersion(){return descVersion;}
    /**
	tagChecksumを取得する。

	@return 取得したtagChecksum を返す。
    */
    public UDF_uint8 getTagChecksum(){return tagChecksum;}
    /**
	reservedを取得する。

	@return 取得したreserved を返す。
    */
    public UDF_bytes getReserved(){return reserved;}
    /**
	tagSerialNumberを取得する。

	@return 取得したtagSerialNumber を返す。
    */
    public UDF_uint16 getTagSerialNumber(){return tagSerialNumber;}
    /**
	descCRCを取得する。

	@return 取得したdescCRC を返す。
    */
    public UDF_uint16 getDescCRC(){return descCRC;}
    /**
	descCRCLenを取得する。

	@return 取得したdescCRCLen を返す。
    */
    public UDF_uint16 getDescCRCLen(){return descCRCLen;}
    /**
	tagLocを取得する。

	@return 取得したtagLoc を返す。
    */
    public UDF_uint32 getTagLoc(){return tagLoc;}

    /**
	tagIdを設定する。

	@param	v 設定する値。
    */
    public void setTagId(UDF_uint16 v){replaceChild(v, tagId); tagId = v;}
    /**
	descVersionを設定する。

	@param	v 設定する値。
    */
    public void setDescVersion(UDF_uint16 v){replaceChild(v, descVersion); descVersion = v;}
    /**
	tagChecksumを設定する。

	@param	v 設定する値。
    */
    public void setTagChecksum(UDF_uint8 v){replaceChild(v, tagChecksum); tagChecksum = v;}
    /**
	reservedを設定する。

	@param	v 設定する値。
    */
    public void setReserved(UDF_bytes v){replaceChild(v, reserved); reserved = v;}
    /**
	tagSerialNumberを設定する。

	@param	v 設定する値。
    */
    public void setTagSerialNumber(UDF_uint16 v){replaceChild(v, tagSerialNumber); tagSerialNumber = v;}
    /**
	descCRCを設定する。

	@param	v 設定する値。
    */
    public void setDescCRC(UDF_uint16 v){replaceChild(v, descCRC); descCRC = v;}
    /**
	descCRCLenを設定する。

	@param	v 設定する値。
    */
    public void setDescCRCLen(UDF_uint16 v){replaceChild(v, descCRCLen); descCRCLen = v;}
    /**
	tagLocを設定する。

	@param	v 設定する値。
    */
    public void setTagLoc(UDF_uint32 v){replaceChild(v, tagLoc); tagLoc = v;}

    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long rsize = 0;
	preReadHook(f);
	tagId = (UDF_uint16)createElement("UDF_uint16", "", "tag-id");
	rsize += tagId.readFrom(f);
	descVersion = (UDF_uint16)createElement("UDF_uint16", "", "desc-version");
	rsize += descVersion.readFrom(f);
	tagChecksum = (UDF_uint8)createElement("UDF_uint8", "", "tag-checksum");
	rsize += tagChecksum.readFrom(f);
	reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
	reserved.setSize(1);
	rsize += reserved.readFrom(f);
	tagSerialNumber = (UDF_uint16)createElement("UDF_uint16", "", "tag-serial-number");
	rsize += tagSerialNumber.readFrom(f);
	descCRC = (UDF_uint16)createElement("UDF_uint16", "", "desc-crc");
	rsize += descCRC.readFrom(f);
	descCRCLen = (UDF_uint16)createElement("UDF_uint16", "", "desc-crc-len");
	rsize += descCRCLen.readFrom(f);
	tagLoc = (UDF_uint32)createElement("UDF_uint32", "", "tag-loc");
	rsize += tagLoc.readFrom(f);
	apply( );
	postReadHook(f);
	return rsize;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long wsize = 0;
	wsize += tagId.writeTo(f);
	wsize += descVersion.writeTo(f);
	wsize += tagChecksum.writeTo(f);
	wsize += reserved.writeTo(f);
	wsize += tagSerialNumber.writeTo(f);
	wsize += descCRC.writeTo(f);
	wsize += descCRCLen.writeTo(f);
	wsize += tagLoc.writeTo(f);
	return wsize;
    }

    /**
	XMLのノードを指定して読み込む。

	@param n 読み込み先ノード。
    */
    public void readFromXML(Node n) throws UDF_Exception{
	preReadFromXMLHook(n);
	NodeList nl = n.getChildNodes();

	for(int i=0 ; i<nl.getLength() ; ++i){
	    Node child = nl.item(i);
	    if(child.getNodeType() != Node.ELEMENT_NODE)
		continue;
	    String name = child.getLocalName();
	    if(false)
		;
	    else if(name.equals("tag-id")){
		tagId = (UDF_uint16)createElement("UDF_uint16", "", "tag-id");
		tagId.readFromXML(child);
	    }
	    else if(name.equals("desc-version")){
		descVersion = (UDF_uint16)createElement("UDF_uint16", "", "desc-version");
		descVersion.readFromXML(child);
	    }
	    else if(name.equals("tag-checksum")){
		tagChecksum = (UDF_uint8)createElement("UDF_uint8", "", "tag-checksum");
		tagChecksum.readFromXML(child);
	    }
	    else if(name.equals("reserved")){
		reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
		reserved.setSize(1);
		reserved.readFromXML(child);
	    }
	    else if(name.equals("tag-serial-number")){
		tagSerialNumber = (UDF_uint16)createElement("UDF_uint16", "", "tag-serial-number");
		tagSerialNumber.readFromXML(child);
	    }
	    else if(name.equals("desc-crc")){
		descCRC = (UDF_uint16)createElement("UDF_uint16", "", "desc-crc");
		descCRC.readFromXML(child);
	    }
	    else if(name.equals("desc-crc-len")){
		descCRCLen = (UDF_uint16)createElement("UDF_uint16", "", "desc-crc-len");
		descCRCLen.readFromXML(child);
	    }
	    else if(name.equals("tag-loc")){
		tagLoc = (UDF_uint32)createElement("UDF_uint32", "", "tag-loc");
		tagLoc.readFromXML(child);
	    }
	    else{
		System.err.println("Unknown XML tag:" + name);
	    }
	}
	apply( );
	postReadFromXMLHook(n);

	//読んだら消す(もういらないので)
	n.getParentNode().removeChild(n);
    }

    /**
	初期値を設定する
    */
    public void setDefaultValue(){
	super.setDefaultValue();
	tagId = (UDF_uint16)createElement("UDF_uint16", "", "tag-id");
	tagId.setDefaultValue();
	descVersion = (UDF_uint16)createElement("UDF_uint16", "", "desc-version");
	descVersion.setDefaultValue();
	tagChecksum = (UDF_uint8)createElement("UDF_uint8", "", "tag-checksum");
	tagChecksum.setDefaultValue();
	reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
	reserved.setSize(1);
	reserved.setDefaultValue();
	tagSerialNumber = (UDF_uint16)createElement("UDF_uint16", "", "tag-serial-number");
	tagSerialNumber.setDefaultValue();
	descCRC = (UDF_uint16)createElement("UDF_uint16", "", "desc-crc");
	descCRC.setDefaultValue();
	descCRCLen = (UDF_uint16)createElement("UDF_uint16", "", "desc-crc-len");
	descCRCLen.setDefaultValue();
	tagLoc = (UDF_uint32)createElement("UDF_uint32", "", "tag-loc");
	tagLoc.setDefaultValue();

	apply( );
	postSetDefaultValueHook();
    }


    /**
	複製する
    */
    public UDF_Element duplicateElement(){
	UDF_tag dup = new UDF_tag(this, null, getName());
	dup.setDefaultValue();
	dup.duplicateHook(this);
	dup.setTagId((UDF_uint16)tagId.duplicateElement());
	dup.setDescVersion((UDF_uint16)descVersion.duplicateElement());
	dup.setTagChecksum((UDF_uint8)tagChecksum.duplicateElement());
	dup.setReserved((UDF_bytes)reserved.duplicateElement());
	dup.setTagSerialNumber((UDF_uint16)tagSerialNumber.duplicateElement());
	dup.setDescCRC((UDF_uint16)descCRC.duplicateElement());
	dup.setDescCRCLen((UDF_uint16)descCRCLen.duplicateElement());
	dup.setTagLoc((UDF_uint32)tagLoc.duplicateElement());

	apply();

	return dup;
    }

    /**
        子の UDF Element の Node を append しなおす。

    */
    private void apply( ) {

	removeAllChildren();
	appendChild(tagId);
	appendChild(descVersion);
	appendChild(tagChecksum);
	appendChild(reserved);
	appendChild(tagSerialNumber);
	appendChild(descCRC);
	appendChild(descCRCLen);
	appendChild(tagLoc);
    }

    public String getInfo(int indent){
	String a = "";
	a += UDF_Util.repeat(' ', indent);
	a += tagId.getInfo(indent + 1);
	a += descVersion.getInfo(indent + 1);
	a += tagChecksum.getInfo(indent + 1);
	a += reserved.getInfo(indent + 1);
	a += tagSerialNumber.getInfo(indent + 1);
	a += descCRC.getInfo(indent + 1);
	a += descCRCLen.getInfo(indent + 1);
	a += tagLoc.getInfo(indent + 1);
      return a;
    }
    public void debug(int indent){
	System.err.print(UDF_Util.repeat(' ', indent));

	String info = " gp=" + getGlobalPoint();
	info += " pn=" + getElemPartRefNo();
	info += " sn=" + getPartSubno();
	System.err.println(getName() + ": (" + hashCode() + ")" + info);	tagId.debug(indent + 1);
	descVersion.debug(indent + 1);
	tagChecksum.debug(indent + 1);
	reserved.debug(indent + 1);
	tagSerialNumber.debug(indent + 1);
	descCRC.debug(indent + 1);
	descCRCLen.debug(indent + 1);
	tagLoc.debug(indent + 1);
    }
//begin:add your code here
    /**
       Chksum を計算します。
       
       @return 計算したChksum の値。
    */
    public int calcChecksum(){
	try{
	    UDF_RandomAccessBuffer rab = new UDF_RandomAccessBuffer(16);
	    byte[] descbuf = new byte[16];
	    int sum = 0;

	    writeTo(rab);
	    rab.seek(0);
	    rab.read(descbuf);
	    
	    for(int i=0 ; i<16 ; ++i){
		if(i != 4)
		    sum += UDF_Util.b2i(descbuf[i]);
	    }
	    return sum % 256;
	}
	catch(IOException e){
	    return -1;
	}
	catch(UDF_Exception e){
	    return -1;
	}
    }

    /**
       Chksum を計算し設定します。
    */
    public void recalcChecksum(){
	if(getTagChecksum().getIntValue() != calcChecksum())
	    debugMsg(3, "Checksum " + getTagChecksum().getIntValue() + "=>" + calcChecksum());
	getTagChecksum().setValue(calcChecksum());
    }


    /**
       Chksum が正しいか否か。

       @return true 正しく設定されている。
       @return false 間違った値が設定されている。
    */
    public boolean isChecksum( ) {
        return (calcChecksum() == getTagChecksum().getIntValue());
    }


    public void recalc(short type, UDF_RandomAccess f){
	if(type == RECALC_CRC)
	    recalcChecksum();
	else if(type == RECALC_TAGLOC)
	    getTagLoc().setValue(getElemLoc());
	else
	    super.recalc(type, f);
    }

    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	short category   = UDF_Error.C_ECMA167;

	
	int tagid = getTagId().getIntValue();
	
	// この範囲のID は予約されている
	if((9 < tagid && tagid < 256) || (277 < tagid && tagid < 65280)){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Tag Identifier",
			 "Type 0 shall specify that the format of descriptor is not specified by Part3. " +
			 "Types 1-7 and 9 are specified as shown in figure 3/3. " +
			 "Type 8 is specified identically in Part3 and Part4. " +
			 "Types 256-266 are specified in Part4. " +
			 "Types 65280-65535 are subject to agreement between the originator and recipient of the medium. " +
			 "All other types are reserved for future standadisation.",
			 "3/7.2.1", String.valueOf(tagid), ""));
	}
	// この範囲のID は作成者と使用者の同意がないと使用できない
	else if(65280 <= tagid && tagid <= 65535){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_CAUTION, "Tag Identifier",
			 "Types 65280-65535 are subject to agreement between the originator and recipient of the medium.",
			 "3/7.2.1", String.valueOf(tagid), ""));
	}
	
	// Descriptor Version は2 か3 で無ければならない
	int descver = getDescVersion().getIntValue();
	if(descver != 2 && descver != 3){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Descriptor Version",
			 "This field shall specify the version of this descriptor. This value shall be 2 or 3. " +
			 "The value 3 shall indicate the structure of Part 3. The value 2 shall indicate the structure of Part 3 of ECMA-167/2. " +
			 "See 3/13.1 and 3/14.1 for requirements.", "3/7.2.2",
			 String.valueOf(descver), "2 or 3"));
	}
	
	// ズバリChksum の値が正しくない
	if(!isChecksum()){
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Tag Checksum", 
			 "This field shall specify the sum modulo 256 of bytes 0-3 and 5-15 of the tag.",
			 "3/7.2.3",
			 String.valueOf(getTagChecksum().getIntValue()),
			 String.valueOf(calcChecksum())));
	}
	
	// reserved は0 でなければならない
	if(getReserved().getData()[0] != 0){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Reserved",
			 "This field shall be reserved for future standardisation and shall be set to 0.",
			 "3/7.2.4"));
	}
	
	// Tag Locationが正しくない
	if(getTagLoc().getIntValue() != getElemLoc()){
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Tag Location",
			 "This field shall specify the number of the logical sector containing the first byte of the descriptor.",
			 "3/7.2.8",
			 String.valueOf(getTagLoc().getIntValue()),
			 String.valueOf(getElemLoc())));
					
	}
	
	return el;
    }
    
//end:
};
