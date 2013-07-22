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
Allocation&nbsp;Extent&nbsp;Descriptor&nbsp;を表現するクラス。<br/>


<dl>

<dt>フィールド一覧</dt>
<dd>
<table border="1">
<tr><td>メンバ</td><td>型</td><td>サイズ</td></tr>
<tr><td><b>DescTag</b></td><td><b>UDF_tag</b></td><td><i>descTag.getSize()</i></td></tr>
<tr><td><b>PreviousAllocExtentLoc</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>LenOfAllocDesc</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>AllocDesc</b></td><td><b>UDF_bytes</b></td><td><i>getLenOfAllocDesc().getIntValue()</i></td></tr>
</table>
</dd>
</dl>
*/
public class UDF_desc258 extends UDF_ADDesc 
{

    /**
	このオブジェクトのパッケージ名を除いたクラス名を取得する。

	@return クラス名
    */
    public static String getUDFClassName( ) {
        return "UDF_desc258";
    }

    /**
	コンストラクタ。

	@param elem   親エレメント。
	@param prefix ネームスペース。
	@param name   エレメント名。
    */
    public UDF_desc258(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }
    /**
	このUDFエレメントのサイズを返す。

	@return UDFエレメントのサイズ。
    */
    public int getSize(){
	return 0+descTag.getSize()+4+4+allocDesc.getSize();
    }

    /**
	このUDFエレメントのサイズをlong 型で返す。

	@return UDFエレメントのサイズ。
    */
    public long getLongSize(){
	return (long)0+descTag.getSize()+4+4+allocDesc.getSize();
    }
    private UDF_tag descTag;
    private UDF_uint32 previousAllocExtentLoc;
    private UDF_uint32 lenOfAllocDesc;
    private UDF_bytes allocDesc;

    /**
	descTagを取得する。

	@return 取得したdescTag を返す。
    */
    public UDF_tag getDescTag(){return descTag;}
    /**
	previousAllocExtentLocを取得する。

	@return 取得したpreviousAllocExtentLoc を返す。
    */
    public UDF_uint32 getPreviousAllocExtentLoc(){return previousAllocExtentLoc;}
    /**
	lenOfAllocDescを取得する。

	@return 取得したlenOfAllocDesc を返す。
    */
    public UDF_uint32 getLenOfAllocDesc(){return lenOfAllocDesc;}
    /**
	allocDescを取得する。

	@return 取得したallocDesc を返す。
    */
    public UDF_bytes getAllocDesc(){return allocDesc;}

    /**
	descTagを設定する。

	@param	v 設定する値。
    */
    public void setDescTag(UDF_tag v){replaceChild(v, descTag); descTag = v;}
    /**
	previousAllocExtentLocを設定する。

	@param	v 設定する値。
    */
    public void setPreviousAllocExtentLoc(UDF_uint32 v){replaceChild(v, previousAllocExtentLoc); previousAllocExtentLoc = v;}
    /**
	lenOfAllocDescを設定する。

	@param	v 設定する値。
    */
    public void setLenOfAllocDesc(UDF_uint32 v){replaceChild(v, lenOfAllocDesc); lenOfAllocDesc = v;}
    /**
	allocDescを設定する。

	@param	v 設定する値。
    */
    public void setAllocDesc(UDF_bytes v){replaceChild(v, allocDesc); allocDesc = v;}

    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long rsize = 0;
	preReadHook(f);
	descTag = (UDF_tag)createElement("UDF_tag", "", "desc-tag");
	rsize += descTag.readFrom(f);
	previousAllocExtentLoc = (UDF_uint32)createElement("UDF_uint32", "", "previous-alloc-extent-loc");
	rsize += previousAllocExtentLoc.readFrom(f);
	lenOfAllocDesc = (UDF_uint32)createElement("UDF_uint32", "", "len-of-alloc-desc");
	rsize += lenOfAllocDesc.readFrom(f);
	allocDesc = (UDF_bytes)createElement("UDF_bytes", "", "alloc-desc");
	allocDesc.setSize(getLenOfAllocDesc().getIntValue());
	rsize += allocDesc.readFrom(f);
	apply( );
	postReadHook(f);
	return rsize;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long wsize = 0;
	wsize += descTag.writeTo(f);
	wsize += previousAllocExtentLoc.writeTo(f);
	wsize += lenOfAllocDesc.writeTo(f);
	wsize += allocDesc.writeTo(f);
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
	    else if(name.equals("previous-alloc-extent-loc")){
		previousAllocExtentLoc = (UDF_uint32)createElement("UDF_uint32", "", "previous-alloc-extent-loc");
		previousAllocExtentLoc.readFromXML(child);
	    }
	    else if(name.equals("len-of-alloc-desc")){
		lenOfAllocDesc = (UDF_uint32)createElement("UDF_uint32", "", "len-of-alloc-desc");
		lenOfAllocDesc.readFromXML(child);
	    }
	    else if(name.equals("alloc-desc")){
		allocDesc = (UDF_bytes)createElement("UDF_bytes", "", "alloc-desc");
		allocDesc.setSize(getLenOfAllocDesc().getIntValue());
		allocDesc.readFromXML(child);
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
	previousAllocExtentLoc = (UDF_uint32)createElement("UDF_uint32", "", "previous-alloc-extent-loc");
	previousAllocExtentLoc.setDefaultValue();
	lenOfAllocDesc = (UDF_uint32)createElement("UDF_uint32", "", "len-of-alloc-desc");
	lenOfAllocDesc.setDefaultValue();
	allocDesc = (UDF_bytes)createElement("UDF_bytes", "", "alloc-desc");
	allocDesc.setSize(getLenOfAllocDesc().getIntValue());
	allocDesc.setDefaultValue();

	apply( );
	postSetDefaultValueHook();
    }


    /**
	複製する
    */
    public UDF_Element duplicateElement(){
	UDF_desc258 dup = new UDF_desc258(this, null, getName());
	dup.setDefaultValue();
	dup.duplicateHook(this);
	dup.setDescTag((UDF_tag)descTag.duplicateElement());
	dup.setPreviousAllocExtentLoc((UDF_uint32)previousAllocExtentLoc.duplicateElement());
	dup.setLenOfAllocDesc((UDF_uint32)lenOfAllocDesc.duplicateElement());
	dup.setAllocDesc((UDF_bytes)allocDesc.duplicateElement());

	apply();

	return dup;
    }

    /**
        子の UDF Element の Node を append しなおす。

    */
    private void apply( ) {

	removeAllChildren();
	appendChild(descTag);
	appendChild(previousAllocExtentLoc);
	appendChild(lenOfAllocDesc);
	appendChild(allocDesc);
    }

    public String getInfo(int indent){
	String a = "";
	a += UDF_Util.repeat(' ', indent);
	a += descTag.getInfo(indent + 1);
	a += previousAllocExtentLoc.getInfo(indent + 1);
	a += lenOfAllocDesc.getInfo(indent + 1);
	a += allocDesc.getInfo(indent + 1);
      return a;
    }
    public void debug(int indent){
	System.err.print(UDF_Util.repeat(' ', indent));

	String info = " gp=" + getGlobalPoint();
	info += " pn=" + getElemPartRefNo();
	info += " sn=" + getPartSubno();
	System.err.println(getName() + ": (" + hashCode() + ")" + info);	descTag.debug(indent + 1);
	previousAllocExtentLoc.debug(indent + 1);
	lenOfAllocDesc.debug(indent + 1);
	allocDesc.debug(indent + 1);
    }
//begin:add your code here
    public int getFixedTagId() { return 258; }
    
    public UDF_icbtag getICBTag(){
	return getFirstADDesc().getICBTag();
    }
    //public void setICBTag(UDF_icbtag v){/*DUMMY*/}
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	UDF_Error ret;
	
	
	el.addError(super.verify());  // Descriptor Tag
	
	ret = verifyId();
	if(ret.isError()){
    
	    ret.setRefer("4/14.5.1");
	    el.addError(ret);
	}
	
	el.setGlobalPoint(getGlobalPoint());
	el.setRName("Allocation Extent Descriptor");
	return el;
    }
    
//end:
};
