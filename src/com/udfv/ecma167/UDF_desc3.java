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
Volume&nbsp;Descriptor&nbsp;Pointer&nbsp;を表現するクラス。<br/>


<dl>

<dt>フィールド一覧</dt>
<dd>
<table border="1">
<tr><td>メンバ</td><td>型</td><td>サイズ</td></tr>
<tr><td><b>DescTag</b></td><td><b>UDF_tag</b></td><td><i>descTag.getSize()</i></td></tr>
<tr><td><b>VolDescSeqNumber</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>NextVolDescSeqExtent</b></td><td><b>UDF_extent_ad</b></td><td><i>nextVolDescSeqExtent.getSize()</i></td></tr>
<tr><td><b>Reserved</b></td><td><b>UDF_bytes</b></td><td><i>484</i></td></tr>
</table>
</dd>
</dl>
*/
public class UDF_desc3 extends UDF_CrcDesc implements UDF_VolDesc, UDF_VolDescSeqNum
{

    /**
	このオブジェクトのパッケージ名を除いたクラス名を取得する。

	@return クラス名
    */
    public static String getUDFClassName( ) {
        return "UDF_desc3";
    }

    /**
	コンストラクタ。

	@param elem   親エレメント。
	@param prefix ネームスペース。
	@param name   エレメント名。
    */
    public UDF_desc3(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }
    /**
	このUDFエレメントのサイズを返す。

	@return UDFエレメントのサイズ。
    */
    public int getSize(){
	return 0+descTag.getSize()+4+nextVolDescSeqExtent.getSize()+reserved.getSize();
    }

    /**
	このUDFエレメントのサイズをlong 型で返す。

	@return UDFエレメントのサイズ。
    */
    public long getLongSize(){
	return (long)0+descTag.getSize()+4+nextVolDescSeqExtent.getSize()+reserved.getSize();
    }
    private UDF_tag descTag;
    private UDF_uint32 volDescSeqNumber;
    private UDF_extent_ad nextVolDescSeqExtent;
    private UDF_bytes reserved;

    /**
	descTagを取得する。

	@return 取得したdescTag を返す。
    */
    public UDF_tag getDescTag(){return descTag;}
    /**
	volDescSeqNumberを取得する。

	@return 取得したvolDescSeqNumber を返す。
    */
    public UDF_uint32 getVolDescSeqNumber(){return volDescSeqNumber;}
    /**
	nextVolDescSeqExtentを取得する。

	@return 取得したnextVolDescSeqExtent を返す。
    */
    public UDF_extent_ad getNextVolDescSeqExtent(){return nextVolDescSeqExtent;}
    /**
	reservedを取得する。

	@return 取得したreserved を返す。
    */
    public UDF_bytes getReserved(){return reserved;}

    /**
	descTagを設定する。

	@param	v 設定する値。
    */
    public void setDescTag(UDF_tag v){replaceChild(v, descTag); descTag = v;}
    /**
	volDescSeqNumberを設定する。

	@param	v 設定する値。
    */
    public void setVolDescSeqNumber(UDF_uint32 v){replaceChild(v, volDescSeqNumber); volDescSeqNumber = v;}
    /**
	nextVolDescSeqExtentを設定する。

	@param	v 設定する値。
    */
    public void setNextVolDescSeqExtent(UDF_extent_ad v){replaceChild(v, nextVolDescSeqExtent); nextVolDescSeqExtent = v;}
    /**
	reservedを設定する。

	@param	v 設定する値。
    */
    public void setReserved(UDF_bytes v){replaceChild(v, reserved); reserved = v;}

    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long rsize = 0;
	preReadHook(f);
	descTag = (UDF_tag)createElement("UDF_tag", "", "desc-tag");
	rsize += descTag.readFrom(f);
	volDescSeqNumber = (UDF_uint32)createElement("UDF_uint32", "", "vol-desc-seq-number");
	rsize += volDescSeqNumber.readFrom(f);
	nextVolDescSeqExtent = (UDF_extent_ad)createElement("UDF_extent_ad", "", "next-vol-desc-seq-extent");
	rsize += nextVolDescSeqExtent.readFrom(f);
	reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
	reserved.setSize(484);
	rsize += reserved.readFrom(f);
	apply( );
	postReadHook(f);
	return rsize;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long wsize = 0;
	wsize += descTag.writeTo(f);
	wsize += volDescSeqNumber.writeTo(f);
	wsize += nextVolDescSeqExtent.writeTo(f);
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
	    else if(name.equals("desc-tag")){
		descTag = (UDF_tag)createElement("UDF_tag", "", "desc-tag");
		descTag.readFromXML(child);
	    }
	    else if(name.equals("vol-desc-seq-number")){
		volDescSeqNumber = (UDF_uint32)createElement("UDF_uint32", "", "vol-desc-seq-number");
		volDescSeqNumber.readFromXML(child);
	    }
	    else if(name.equals("next-vol-desc-seq-extent")){
		nextVolDescSeqExtent = (UDF_extent_ad)createElement("UDF_extent_ad", "", "next-vol-desc-seq-extent");
		nextVolDescSeqExtent.readFromXML(child);
	    }
	    else if(name.equals("reserved")){
		reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
		reserved.setSize(484);
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
	descTag = (UDF_tag)createElement("UDF_tag", "", "desc-tag");
	descTag.setDefaultValue();
	volDescSeqNumber = (UDF_uint32)createElement("UDF_uint32", "", "vol-desc-seq-number");
	volDescSeqNumber.setDefaultValue();
	nextVolDescSeqExtent = (UDF_extent_ad)createElement("UDF_extent_ad", "", "next-vol-desc-seq-extent");
	nextVolDescSeqExtent.setDefaultValue();
	reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
	reserved.setSize(484);
	reserved.setDefaultValue();

	apply( );
	postSetDefaultValueHook();
    }


    /**
	複製する
    */
    public UDF_Element duplicateElement(){
	UDF_desc3 dup = new UDF_desc3(this, null, getName());
	dup.setDefaultValue();
	dup.duplicateHook(this);
	dup.setDescTag((UDF_tag)descTag.duplicateElement());
	dup.setVolDescSeqNumber((UDF_uint32)volDescSeqNumber.duplicateElement());
	dup.setNextVolDescSeqExtent((UDF_extent_ad)nextVolDescSeqExtent.duplicateElement());
	dup.setReserved((UDF_bytes)reserved.duplicateElement());

	apply();

	return dup;
    }

    /**
        子の UDF Element の Node を append しなおす。

    */
    private void apply( ) {

	removeAllChildren();
	appendChild(descTag);
	appendChild(volDescSeqNumber);
	appendChild(nextVolDescSeqExtent);
	appendChild(reserved);
    }

    public String getInfo(int indent){
	String a = "";
	a += UDF_Util.repeat(' ', indent);
	a += descTag.getInfo(indent + 1);
	a += volDescSeqNumber.getInfo(indent + 1);
	a += nextVolDescSeqExtent.getInfo(indent + 1);
	a += reserved.getInfo(indent + 1);
      return a;
    }
    public void debug(int indent){
	System.err.print(UDF_Util.repeat(' ', indent));

	String info = " gp=" + getGlobalPoint();
	info += " pn=" + getElemPartRefNo();
	info += " sn=" + getPartSubno();
	System.err.println(getName() + ": (" + hashCode() + ")" + info);	descTag.debug(indent + 1);
	volDescSeqNumber.debug(indent + 1);
	nextVolDescSeqExtent.debug(indent + 1);
	reserved.debug(indent + 1);
    }
//begin:add your code here
    public int getFixedTagId() { return 3; }
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	UDF_Error ret;
	
	
	el.addError(super.verify());  // Descriptor Tag
	
	ret = verifyId();
	if(ret.isError()){
	    
	    ret.setRefer("3/10.3.1");
	    el.addError(ret);
	}
	
	el.addError(nextVolDescSeqExtent.verify("Next Volume Descriptor Sequence Extent"));
	
	if(!UDF_Util.isAllZero(reserved.getData())){
	    
	    el.addError(new UDF_Error
			(UDF_Error.C_ECMA167, UDF_Error.L_ERROR, "Reserved",
			 "This field shall be reserved for future standardisation and all bytes shall be se to #00.",
			 "3/10.3.4"));
	}
	
	el.setGlobalPoint(getGlobalPoint());
	el.setRName("Volume Descriptor Pointer");
	return el;
    }
    public void postVolReadHook(UDF_RandomAccess f){
	;
    }
    
//end:
};
