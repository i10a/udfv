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
Information&nbsp;Times&nbsp;Extended&nbsp;Attribute&nbsp;を表現するクラス。<br/>


<dl>

<dt>フィールド一覧</dt>
<dd>
<table border="1">
<tr><td>メンバ</td><td>型</td><td>サイズ</td></tr>
<tr><td><b>AttrType</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>AttrSubtype</b></td><td><b>UDF_uint8</b></td><td><i>1</i></td></tr>
<tr><td><b>Reserved</b></td><td><b>UDF_bytes</b></td><td><i>3</i></td></tr>
<tr><td><b>AttrLen</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>DataLen</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>InfoTimeExistence</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>InfoTimes</b></td><td><b>UDF_bytes</b></td><td><i>getDataLen().getIntValue()</i></td></tr>
</table>
</dd>
</dl>
*/
public class UDF_attr6 extends UDF_attr 
{

    /**
	このオブジェクトのパッケージ名を除いたクラス名を取得する。

	@return クラス名
    */
    public static String getUDFClassName( ) {
        return "UDF_attr6";
    }

    /**
	コンストラクタ。

	@param elem   親エレメント。
	@param prefix ネームスペース。
	@param name   エレメント名。
    */
    public UDF_attr6(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }
    /**
	このUDFエレメントのサイズを返す。

	@return UDFエレメントのサイズ。
    */
    public int getSize(){
	return 0+4+1+reserved.getSize()+4+4+4+infoTimes.getSize();
    }

    /**
	このUDFエレメントのサイズをlong 型で返す。

	@return UDFエレメントのサイズ。
    */
    public long getLongSize(){
	return (long)0+4+1+reserved.getSize()+4+4+4+infoTimes.getSize();
    }
    private UDF_uint32 attrType;
    private UDF_uint8 attrSubtype;
    private UDF_bytes reserved;
    private UDF_uint32 attrLen;
    private UDF_uint32 dataLen;
    private UDF_uint32 infoTimeExistence;
    private UDF_bytes infoTimes;

    /**
	attrTypeを取得する。

	@return 取得したattrType を返す。
    */
    public UDF_uint32 getAttrType(){return attrType;}
    /**
	attrSubtypeを取得する。

	@return 取得したattrSubtype を返す。
    */
    public UDF_uint8 getAttrSubtype(){return attrSubtype;}
    /**
	reservedを取得する。

	@return 取得したreserved を返す。
    */
    public UDF_bytes getReserved(){return reserved;}
    /**
	attrLenを取得する。

	@return 取得したattrLen を返す。
    */
    public UDF_uint32 getAttrLen(){return attrLen;}
    /**
	dataLenを取得する。

	@return 取得したdataLen を返す。
    */
    public UDF_uint32 getDataLen(){return dataLen;}
    /**
	infoTimeExistenceを取得する。

	@return 取得したinfoTimeExistence を返す。
    */
    public UDF_uint32 getInfoTimeExistence(){return infoTimeExistence;}
    /**
	infoTimesを取得する。

	@return 取得したinfoTimes を返す。
    */
    public UDF_bytes getInfoTimes(){return infoTimes;}

    /**
	attrTypeを設定する。

	@param	v 設定する値。
    */
    public void setAttrType(UDF_uint32 v){replaceChild(v, attrType); attrType = v;}
    /**
	attrSubtypeを設定する。

	@param	v 設定する値。
    */
    public void setAttrSubtype(UDF_uint8 v){replaceChild(v, attrSubtype); attrSubtype = v;}
    /**
	reservedを設定する。

	@param	v 設定する値。
    */
    public void setReserved(UDF_bytes v){replaceChild(v, reserved); reserved = v;}
    /**
	attrLenを設定する。

	@param	v 設定する値。
    */
    public void setAttrLen(UDF_uint32 v){replaceChild(v, attrLen); attrLen = v;}
    /**
	dataLenを設定する。

	@param	v 設定する値。
    */
    public void setDataLen(UDF_uint32 v){replaceChild(v, dataLen); dataLen = v;}
    /**
	infoTimeExistenceを設定する。

	@param	v 設定する値。
    */
    public void setInfoTimeExistence(UDF_uint32 v){replaceChild(v, infoTimeExistence); infoTimeExistence = v;}
    /**
	infoTimesを設定する。

	@param	v 設定する値。
    */
    public void setInfoTimes(UDF_bytes v){replaceChild(v, infoTimes); infoTimes = v;}

    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long rsize = 0;
	preReadHook(f);
	attrType = (UDF_uint32)createElement("UDF_uint32", "", "attr-type");
	rsize += attrType.readFrom(f);
	attrSubtype = (UDF_uint8)createElement("UDF_uint8", "", "attr-subtype");
	rsize += attrSubtype.readFrom(f);
	reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
	reserved.setSize(3);
	rsize += reserved.readFrom(f);
	attrLen = (UDF_uint32)createElement("UDF_uint32", "", "attr-len");
	rsize += attrLen.readFrom(f);
	dataLen = (UDF_uint32)createElement("UDF_uint32", "", "data-len");
	rsize += dataLen.readFrom(f);
	infoTimeExistence = (UDF_uint32)createElement("UDF_uint32", "", "info-time-existence");
	rsize += infoTimeExistence.readFrom(f);
	infoTimes = (UDF_bytes)createElement("UDF_bytes", "", "info-times");
	infoTimes.setSize(getDataLen().getIntValue());
	rsize += infoTimes.readFrom(f);
	apply( );
	postReadHook(f);
	return rsize;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long wsize = 0;
	wsize += attrType.writeTo(f);
	wsize += attrSubtype.writeTo(f);
	wsize += reserved.writeTo(f);
	wsize += attrLen.writeTo(f);
	wsize += dataLen.writeTo(f);
	wsize += infoTimeExistence.writeTo(f);
	wsize += infoTimes.writeTo(f);
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
	    else if(name.equals("attr-type")){
		attrType = (UDF_uint32)createElement("UDF_uint32", "", "attr-type");
		attrType.readFromXML(child);
	    }
	    else if(name.equals("attr-subtype")){
		attrSubtype = (UDF_uint8)createElement("UDF_uint8", "", "attr-subtype");
		attrSubtype.readFromXML(child);
	    }
	    else if(name.equals("reserved")){
		reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
		reserved.setSize(3);
		reserved.readFromXML(child);
	    }
	    else if(name.equals("attr-len")){
		attrLen = (UDF_uint32)createElement("UDF_uint32", "", "attr-len");
		attrLen.readFromXML(child);
	    }
	    else if(name.equals("data-len")){
		dataLen = (UDF_uint32)createElement("UDF_uint32", "", "data-len");
		dataLen.readFromXML(child);
	    }
	    else if(name.equals("info-time-existence")){
		infoTimeExistence = (UDF_uint32)createElement("UDF_uint32", "", "info-time-existence");
		infoTimeExistence.readFromXML(child);
	    }
	    else if(name.equals("info-times")){
		infoTimes = (UDF_bytes)createElement("UDF_bytes", "", "info-times");
		infoTimes.setSize(getDataLen().getIntValue());
		infoTimes.readFromXML(child);
	    }
	    else{
		System.err.println("Unknown XML tag:" + name);
	    }
	}
	apply( );
	postReadFromXMLHook(n);
    }

    /**
	初期値を設定する
    */
    public void setDefaultValue(){
	super.setDefaultValue();
	attrType = (UDF_uint32)createElement("UDF_uint32", "", "attr-type");
	attrType.setDefaultValue();
	attrType.setValue(6);
	attrSubtype = (UDF_uint8)createElement("UDF_uint8", "", "attr-subtype");
	attrSubtype.setDefaultValue();
	attrSubtype.setValue(1);
	reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
	reserved.setSize(3);
	reserved.setDefaultValue();
	attrLen = (UDF_uint32)createElement("UDF_uint32", "", "attr-len");
	attrLen.setDefaultValue();
	dataLen = (UDF_uint32)createElement("UDF_uint32", "", "data-len");
	dataLen.setDefaultValue();
	infoTimeExistence = (UDF_uint32)createElement("UDF_uint32", "", "info-time-existence");
	infoTimeExistence.setDefaultValue();
	infoTimes = (UDF_bytes)createElement("UDF_bytes", "", "info-times");
	infoTimes.setSize(getDataLen().getIntValue());
	infoTimes.setDefaultValue();

	apply( );
	postSetDefaultValueHook();
    }


    /**
	複製する
    */
    public UDF_Element duplicateElement(){
	UDF_attr6 dup = new UDF_attr6(this, null, getName());
	dup.setDefaultValue();
	dup.duplicateHook(this);
	dup.setAttrType((UDF_uint32)attrType.duplicateElement());
	dup.setAttrSubtype((UDF_uint8)attrSubtype.duplicateElement());
	dup.setReserved((UDF_bytes)reserved.duplicateElement());
	dup.setAttrLen((UDF_uint32)attrLen.duplicateElement());
	dup.setDataLen((UDF_uint32)dataLen.duplicateElement());
	dup.setInfoTimeExistence((UDF_uint32)infoTimeExistence.duplicateElement());
	dup.setInfoTimes((UDF_bytes)infoTimes.duplicateElement());

	apply();

	return dup;
    }

    /**
        子の UDF Element の Node を append しなおす。

    */
    private void apply( ) {

	removeAllChildren();
	appendChild(attrType);
	appendChild(attrSubtype);
	appendChild(reserved);
	appendChild(attrLen);
	appendChild(dataLen);
	appendChild(infoTimeExistence);
	appendChild(infoTimes);
    }

    public void debug(int indent){
	System.err.print(UDF_Util.repeat(' ', indent));
	System.err.println(getName() + ": (" + hashCode() + ")");	attrType.debug(indent + 1);
	attrSubtype.debug(indent + 1);
	reserved.debug(indent + 1);
	attrLen.debug(indent + 1);
	dataLen.debug(indent + 1);
	infoTimeExistence.debug(indent + 1);
	infoTimes.debug(indent + 1);
    }
//begin:add your code here
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	final short category = UDF_Error.C_ECMA167;
	
	
	el.addError(super.verify(6, 6));
	
	// Information Time Existence の0、1、2、3 以外のビットは0
	long itexist = infoTimeExistence.getLongValue();
	if((itexist & 0xfffffff0) != 0){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Information Time Existence",
			 "Bits not specified in figure 4/33 are reserved for future standardisation and shall be set to ZERO.",
			 "4/14.10.5.6", String.valueOf(itexist), ""));
	}
	
	
	// Information Times の中身はtimestamp
	byte[] ftbuf = infoTimes.getData();
	final int TSSIZE = 12;
	int datalen = dataLen.getIntValue();
	int offset = 0;
	
	while(offset < datalen){
	    
	    UDF_timestamp ts = (UDF_timestamp)createElement("UDF_timestamp", null, null);
	    UDF_RandomAccessBuffer rab = new UDF_RandomAccessBuffer(datalen);
	    long readsz = 0;
	    
	    
	    readsz = rab.write(ftbuf, offset, TSSIZE);
	    if(readsz < TSSIZE){
		
		el.addError(new UDF_Error
			    (category, UDF_Error.L_ERROR, "Information Times",
			     "The dates and time specified in the Information Times Existence field shall be recorded contiguously " +
			     "in this field, each as a timestamp(1/7.3), in ascending order of their bit positions.",
			     "4/14.10.6.7"));
	    }
	    
	    try{
		rab.seek(0);
		readsz = ts.readFrom(rab);
		el.addError(ts.verify());
		el.setRName("Information Times");
	    }
	    catch(Exception e){
		
		if(readsz < TSSIZE)
		    throw new UDF_DataException(ts, "readFrom error.");
	    }
	    
	    offset += TSSIZE;
	}
	
	el.setRName("Information Times Extended Attribute");
	return el;
    }
    
//end:
};
