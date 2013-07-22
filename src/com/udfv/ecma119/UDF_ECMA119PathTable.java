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
package com.udfv.ecma119;

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
ECMA119/9.4で規定されるDirectoryRecordを表現するクラス。

<dl>

<dt>フィールド一覧</dt>
<dd>
<table border="1">
<tr><td>メンバ</td><td>型</td><td>サイズ</td></tr>
<tr><td><b>LenOfDirectoryId</b></td><td><b>UDF_uint8</b></td><td><i>1</i></td></tr>
<tr><td><b>ExtendedAttrRecordLen</b></td><td><b>UDF_uint8</b></td><td><i>1</i></td></tr>
<tr><td><b>LocOfExtent</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>ParentDirectoryNumber</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td></tr>
<tr><td><b>DirectoryId</b></td><td><b>UDF_bytes</b></td><td><i>getLenOfDirectoryId().getIntValue()</i></td></tr>
<tr><td><b>PaddingField</b></td><td><b>UDF_pad</b></td><td><i>2</i></td></tr>
</table>
</dd>
</dl>
*/
public class UDF_ECMA119PathTable extends UDF_Element 
{

    /**
	このオブジェクトのパッケージ名を除いたクラス名を取得する。

	@return クラス名
    */
    public static String getUDFClassName( ) {
        return "UDF_ECMA119PathTable";
    }

    /**
	コンストラクタ。

	@param elem   親エレメント。
	@param prefix ネームスペース。
	@param name   エレメント名。
    */
    public UDF_ECMA119PathTable(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }
    /**
	このUDFエレメントのサイズを返す。

	@return UDFエレメントのサイズ。
    */
    public int getSize(){
	return 0+1+1+4+2+directoryId.getSize()+paddingField.getSize();
    }

    /**
	このUDFエレメントのサイズをlong 型で返す。

	@return UDFエレメントのサイズ。
    */
    public long getLongSize(){
	return (long)0+1+1+4+2+directoryId.getSize()+paddingField.getSize();
    }
    private UDF_uint8 lenOfDirectoryId;
    private UDF_uint8 extendedAttrRecordLen;
    private UDF_uint32 locOfExtent;
    private UDF_uint16 parentDirectoryNumber;
    private UDF_bytes directoryId;
    private UDF_pad paddingField;

    /**
	lenOfDirectoryIdを取得する。

	@return 取得したlenOfDirectoryId を返す。
    */
    public UDF_uint8 getLenOfDirectoryId(){return lenOfDirectoryId;}
    /**
	extendedAttrRecordLenを取得する。

	@return 取得したextendedAttrRecordLen を返す。
    */
    public UDF_uint8 getExtendedAttrRecordLen(){return extendedAttrRecordLen;}
    /**
	locOfExtentを取得する。

	@return 取得したlocOfExtent を返す。
    */
    public UDF_uint32 getLocOfExtent(){return locOfExtent;}
    /**
	parentDirectoryNumberを取得する。

	@return 取得したparentDirectoryNumber を返す。
    */
    public UDF_uint16 getParentDirectoryNumber(){return parentDirectoryNumber;}
    /**
	directoryIdを取得する。

	@return 取得したdirectoryId を返す。
    */
    public UDF_bytes getDirectoryId(){return directoryId;}
    /**
	paddingFieldを取得する。

	@return 取得したpaddingField を返す。
    */
    public UDF_pad getPaddingField(){return paddingField;}

    /**
	lenOfDirectoryIdを設定する。

	@param	v 設定する値。
    */
    public void setLenOfDirectoryId(UDF_uint8 v){replaceChild(v, lenOfDirectoryId); lenOfDirectoryId = v;}
    /**
	extendedAttrRecordLenを設定する。

	@param	v 設定する値。
    */
    public void setExtendedAttrRecordLen(UDF_uint8 v){replaceChild(v, extendedAttrRecordLen); extendedAttrRecordLen = v;}
    /**
	locOfExtentを設定する。

	@param	v 設定する値。
    */
    public void setLocOfExtent(UDF_uint32 v){replaceChild(v, locOfExtent); locOfExtent = v;}
    /**
	parentDirectoryNumberを設定する。

	@param	v 設定する値。
    */
    public void setParentDirectoryNumber(UDF_uint16 v){replaceChild(v, parentDirectoryNumber); parentDirectoryNumber = v;}
    /**
	directoryIdを設定する。

	@param	v 設定する値。
    */
    public void setDirectoryId(UDF_bytes v){replaceChild(v, directoryId); directoryId = v;}
    /**
	paddingFieldを設定する。

	@param	v 設定する値。
    */
    public void setPaddingField(UDF_pad v){replaceChild(v, paddingField); paddingField = v;}

    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long rsize = 0;
	preReadHook(f);
	lenOfDirectoryId = (UDF_uint8)createElement("UDF_uint8", "ecma119", "len-of-directory-id");
	rsize += lenOfDirectoryId.readFrom(f);
	extendedAttrRecordLen = (UDF_uint8)createElement("UDF_uint8", "ecma119", "extended-attr-record-len");
	rsize += extendedAttrRecordLen.readFrom(f);
	locOfExtent = (UDF_uint32)createElement("UDF_uint32", "ecma119", "loc-of-extent");
	rsize += locOfExtent.readFrom(f);
	parentDirectoryNumber = (UDF_uint16)createElement("UDF_uint16", "ecma119", "parent-directory-number");
	rsize += parentDirectoryNumber.readFrom(f);
	directoryId = (UDF_bytes)createElement("UDF_bytes", "ecma119", "directory-id");
	directoryId.setSize(getLenOfDirectoryId().getIntValue());
	rsize += directoryId.readFrom(f);
	paddingField = (UDF_pad)createElement("UDF_pad", "ecma119", "padding-field");
	paddingField.setAlign(2);
	rsize += paddingField.readFrom(f);
	apply( );
	postReadHook(f);
	return rsize;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long wsize = 0;
	wsize += lenOfDirectoryId.writeTo(f);
	wsize += extendedAttrRecordLen.writeTo(f);
	wsize += locOfExtent.writeTo(f);
	wsize += parentDirectoryNumber.writeTo(f);
	wsize += directoryId.writeTo(f);
	wsize += paddingField.writeTo(f);
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
	    else if(name.equals("len-of-directory-id")){
		lenOfDirectoryId = (UDF_uint8)createElement("UDF_uint8", "ecma119", "len-of-directory-id");
		lenOfDirectoryId.readFromXML(child);
	    }
	    else if(name.equals("extended-attr-record-len")){
		extendedAttrRecordLen = (UDF_uint8)createElement("UDF_uint8", "ecma119", "extended-attr-record-len");
		extendedAttrRecordLen.readFromXML(child);
	    }
	    else if(name.equals("loc-of-extent")){
		locOfExtent = (UDF_uint32)createElement("UDF_uint32", "ecma119", "loc-of-extent");
		locOfExtent.readFromXML(child);
	    }
	    else if(name.equals("parent-directory-number")){
		parentDirectoryNumber = (UDF_uint16)createElement("UDF_uint16", "ecma119", "parent-directory-number");
		parentDirectoryNumber.readFromXML(child);
	    }
	    else if(name.equals("directory-id")){
		directoryId = (UDF_bytes)createElement("UDF_bytes", "ecma119", "directory-id");
		directoryId.setSize(getLenOfDirectoryId().getIntValue());
		directoryId.readFromXML(child);
	    }
	    else if(name.equals("padding-field")){
		paddingField = (UDF_pad)createElement("UDF_pad", "ecma119", "padding-field");
		paddingField.setAlign(2);
		paddingField.readFromXML(child);
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
	lenOfDirectoryId = (UDF_uint8)createElement("UDF_uint8", "ecma119", "len-of-directory-id");
	lenOfDirectoryId.setDefaultValue();
	extendedAttrRecordLen = (UDF_uint8)createElement("UDF_uint8", "ecma119", "extended-attr-record-len");
	extendedAttrRecordLen.setDefaultValue();
	locOfExtent = (UDF_uint32)createElement("UDF_uint32", "ecma119", "loc-of-extent");
	locOfExtent.setDefaultValue();
	parentDirectoryNumber = (UDF_uint16)createElement("UDF_uint16", "ecma119", "parent-directory-number");
	parentDirectoryNumber.setDefaultValue();
	directoryId = (UDF_bytes)createElement("UDF_bytes", "ecma119", "directory-id");
	directoryId.setSize(getLenOfDirectoryId().getIntValue());
	directoryId.setDefaultValue();
	paddingField = (UDF_pad)createElement("UDF_pad", "ecma119", "padding-field");
	paddingField.setAlign(2);
	paddingField.setDefaultValue();

	apply( );
	postSetDefaultValueHook();
    }


    /**
	複製する
    */
    public UDF_Element duplicateElement(){
	UDF_ECMA119PathTable dup = new UDF_ECMA119PathTable(this, null, getName());
	dup.setDefaultValue();
	dup.duplicateHook(this);
	dup.setLenOfDirectoryId((UDF_uint8)lenOfDirectoryId.duplicateElement());
	dup.setExtendedAttrRecordLen((UDF_uint8)extendedAttrRecordLen.duplicateElement());
	dup.setLocOfExtent((UDF_uint32)locOfExtent.duplicateElement());
	dup.setParentDirectoryNumber((UDF_uint16)parentDirectoryNumber.duplicateElement());
	dup.setDirectoryId((UDF_bytes)directoryId.duplicateElement());
	dup.setPaddingField((UDF_pad)paddingField.duplicateElement());

	apply();

	return dup;
    }

    /**
        子の UDF Element の Node を append しなおす。

    */
    private void apply( ) {

	removeAllChildren();
	appendChild(lenOfDirectoryId);
	appendChild(extendedAttrRecordLen);
	appendChild(locOfExtent);
	appendChild(parentDirectoryNumber);
	appendChild(directoryId);
	appendChild(paddingField);
    }

    public String getInfo(int indent){
	String a = "";
	a += UDF_Util.repeat(' ', indent);
	a += lenOfDirectoryId.getInfo(indent + 1);
	a += extendedAttrRecordLen.getInfo(indent + 1);
	a += locOfExtent.getInfo(indent + 1);
	a += parentDirectoryNumber.getInfo(indent + 1);
	a += directoryId.getInfo(indent + 1);
	a += paddingField.getInfo(indent + 1);
      return a;
    }
    public void debug(int indent){
	System.err.print(UDF_Util.repeat(' ', indent));

	String info = " gp=" + getGlobalPoint();
	info += " pn=" + getElemPartRefNo();
	info += " sn=" + getPartSubno();
	System.err.println(getName() + ": (" + hashCode() + ")" + info);	lenOfDirectoryId.debug(indent + 1);
	extendedAttrRecordLen.debug(indent + 1);
	locOfExtent.debug(indent + 1);
	parentDirectoryNumber.debug(indent + 1);
	directoryId.debug(indent + 1);
	paddingField.debug(indent + 1);
    }
//begin:add your code here
//end:
};
