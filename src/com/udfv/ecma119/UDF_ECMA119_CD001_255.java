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
ECMA119/8.3で規定されるVolumeDescriptorSetTerminatorを表現するクラス。

<dl>

<dt>フィールド一覧</dt>
<dd>
<table border="1">
<tr><td>メンバ</td><td>型</td><td>サイズ</td></tr>
<tr><td><b>VolDescType</b></td><td><b>UDF_uint8</b></td><td><i>1</i></td></tr>
<tr><td><b>StandardId</b></td><td><b>UDF_bytes</b></td><td><i>5</i></td></tr>
<tr><td><b>VolDescVersion</b></td><td><b>UDF_uint8</b></td><td><i>1</i></td></tr>
<tr><td><b>Reserved</b></td><td><b>UDF_bytes</b></td><td><i>2041</i></td></tr>
</table>
</dd>
</dl>
*/
public class UDF_ECMA119_CD001_255 extends UDF_Element 
{

    /**
	このオブジェクトのパッケージ名を除いたクラス名を取得する。

	@return クラス名
    */
    public static String getUDFClassName( ) {
        return "UDF_ECMA119_CD001_255";
    }

    /**
	コンストラクタ。

	@param elem   親エレメント。
	@param prefix ネームスペース。
	@param name   エレメント名。
    */
    public UDF_ECMA119_CD001_255(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }
    /**
	このUDFエレメントのサイズを返す。

	@return UDFエレメントのサイズ。
    */
    public int getSize(){
	return 0+1+standardId.getSize()+1+reserved.getSize();
    }

    /**
	このUDFエレメントのサイズをlong 型で返す。

	@return UDFエレメントのサイズ。
    */
    public long getLongSize(){
	return (long)0+1+standardId.getSize()+1+reserved.getSize();
    }
    private UDF_uint8 volDescType;
    private UDF_bytes standardId;
    private UDF_uint8 volDescVersion;
    private UDF_bytes reserved;

    /**
	volDescTypeを取得する。

	@return 取得したvolDescType を返す。
    */
    public UDF_uint8 getVolDescType(){return volDescType;}
    /**
	standardIdを取得する。

	@return 取得したstandardId を返す。
    */
    public UDF_bytes getStandardId(){return standardId;}
    /**
	volDescVersionを取得する。

	@return 取得したvolDescVersion を返す。
    */
    public UDF_uint8 getVolDescVersion(){return volDescVersion;}
    /**
	reservedを取得する。

	@return 取得したreserved を返す。
    */
    public UDF_bytes getReserved(){return reserved;}

    /**
	volDescTypeを設定する。

	@param	v 設定する値。
    */
    public void setVolDescType(UDF_uint8 v){replaceChild(v, volDescType); volDescType = v;}
    /**
	standardIdを設定する。

	@param	v 設定する値。
    */
    public void setStandardId(UDF_bytes v){replaceChild(v, standardId); standardId = v;}
    /**
	volDescVersionを設定する。

	@param	v 設定する値。
    */
    public void setVolDescVersion(UDF_uint8 v){replaceChild(v, volDescVersion); volDescVersion = v;}
    /**
	reservedを設定する。

	@param	v 設定する値。
    */
    public void setReserved(UDF_bytes v){replaceChild(v, reserved); reserved = v;}

    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long rsize = 0;
	preReadHook(f);
	volDescType = (UDF_uint8)createElement("UDF_uint8", "ecma119", "vol-desc-type");
	rsize += volDescType.readFrom(f);
	standardId = (UDF_bytes)createElement("UDF_bytes", "ecma119", "standard-id");
	standardId.setSize(5);
	rsize += standardId.readFrom(f);
	volDescVersion = (UDF_uint8)createElement("UDF_uint8", "ecma119", "vol-desc-version");
	rsize += volDescVersion.readFrom(f);
	reserved = (UDF_bytes)createElement("UDF_bytes", "ecma119", "reserved");
	reserved.setSize(2041);
	rsize += reserved.readFrom(f);
	apply( );
	postReadHook(f);
	return rsize;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long wsize = 0;
	wsize += volDescType.writeTo(f);
	wsize += standardId.writeTo(f);
	wsize += volDescVersion.writeTo(f);
	wsize += reserved.writeTo(f);
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
	    else if(name.equals("vol-desc-type")){
		volDescType = (UDF_uint8)createElement("UDF_uint8", "ecma119", "vol-desc-type");
		volDescType.readFromXML(child);
	    }
	    else if(name.equals("standard-id")){
		standardId = (UDF_bytes)createElement("UDF_bytes", "ecma119", "standard-id");
		standardId.setSize(5);
		standardId.readFromXML(child);
	    }
	    else if(name.equals("vol-desc-version")){
		volDescVersion = (UDF_uint8)createElement("UDF_uint8", "ecma119", "vol-desc-version");
		volDescVersion.readFromXML(child);
	    }
	    else if(name.equals("reserved")){
		reserved = (UDF_bytes)createElement("UDF_bytes", "ecma119", "reserved");
		reserved.setSize(2041);
		reserved.readFromXML(child);
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
	volDescType = (UDF_uint8)createElement("UDF_uint8", "ecma119", "vol-desc-type");
	volDescType.setDefaultValue();
	standardId = (UDF_bytes)createElement("UDF_bytes", "ecma119", "standard-id");
	standardId.setSize(5);
	standardId.setDefaultValue();
	volDescVersion = (UDF_uint8)createElement("UDF_uint8", "ecma119", "vol-desc-version");
	volDescVersion.setDefaultValue();
	reserved = (UDF_bytes)createElement("UDF_bytes", "ecma119", "reserved");
	reserved.setSize(2041);
	reserved.setDefaultValue();

	apply( );
	postSetDefaultValueHook();
    }


    /**
	複製する
    */
    public UDF_Element duplicateElement(){
	UDF_ECMA119_CD001_255 dup = new UDF_ECMA119_CD001_255(this, null, getName());
	dup.setDefaultValue();
	dup.duplicateHook(this);
	dup.setVolDescType((UDF_uint8)volDescType.duplicateElement());
	dup.setStandardId((UDF_bytes)standardId.duplicateElement());
	dup.setVolDescVersion((UDF_uint8)volDescVersion.duplicateElement());
	dup.setReserved((UDF_bytes)reserved.duplicateElement());

	apply();

	return dup;
    }

    /**
        子の UDF Element の Node を append しなおす。

    */
    private void apply( ) {

	removeAllChildren();
	appendChild(volDescType);
	appendChild(standardId);
	appendChild(volDescVersion);
	appendChild(reserved);
    }

    public String getInfo(int indent){
	String a = "";
	a += UDF_Util.repeat(' ', indent);
	a += volDescType.getInfo(indent + 1);
	a += standardId.getInfo(indent + 1);
	a += volDescVersion.getInfo(indent + 1);
	a += reserved.getInfo(indent + 1);
      return a;
    }
    public void debug(int indent){
	System.err.print(UDF_Util.repeat(' ', indent));

	String info = " gp=" + getGlobalPoint();
	info += " pn=" + getElemPartRefNo();
	info += " sn=" + getPartSubno();
	System.err.println(getName() + ": (" + hashCode() + ")" + info);	volDescType.debug(indent + 1);
	standardId.debug(indent + 1);
	volDescVersion.debug(indent + 1);
	reserved.debug(indent + 1);
    }
//begin:add your code here
//end:
};
