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
Long&nbsp;Allocation&nbsp;Descriptor&nbsp;を表現するクラス。<br/>


<dl>

<dt>フィールド一覧</dt>
<dd>
<table border="1">
<tr><td>メンバ</td><td>型</td><td>サイズ</td></tr>
<tr><td><b>ExtentLen</b></td><td><b>UDF_uint32a</b></td><td><i>extentLen.getSize()</i></td></tr>
<tr><td><b>ExtentLoc</b></td><td><b>UDF_lb_addr</b></td><td><i>extentLoc.getSize()</i></td></tr>
<tr><td><b>ImplUse</b></td><td><b>UDF_bytes</b></td><td><i>6</i></td></tr>
</table>
</dd>
</dl>
*/
public class UDF_long_ad extends UDF_AD 
{

    /**
	このオブジェクトのパッケージ名を除いたクラス名を取得する。

	@return クラス名
    */
    public static String getUDFClassName( ) {
        return "UDF_long_ad";
    }

    /**
	コンストラクタ。

	@param elem   親エレメント。
	@param prefix ネームスペース。
	@param name   エレメント名。
    */
    public UDF_long_ad(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }
    /**
	このUDFエレメントのサイズを返す。

	@return UDFエレメントのサイズ。
    */
    public int getSize(){
	return 0+extentLen.getSize()+extentLoc.getSize()+implUse.getSize();
    }

    /**
	このUDFエレメントのサイズをlong 型で返す。

	@return UDFエレメントのサイズ。
    */
    public long getLongSize(){
	return (long)0+extentLen.getSize()+extentLoc.getSize()+implUse.getSize();
    }
    private UDF_uint32a extentLen;
    private UDF_lb_addr extentLoc;
    private UDF_bytes implUse;

    /**
	extentLenを取得する。

	@return 取得したextentLen を返す。
    */
    public UDF_uint32a getExtentLen(){return extentLen;}
    /**
	extentLocを取得する。

	@return 取得したextentLoc を返す。
    */
    public UDF_lb_addr getExtentLoc(){return extentLoc;}
    /**
	implUseを取得する。

	@return 取得したimplUse を返す。
    */
    public UDF_bytes getImplUse(){return implUse;}

    /**
	extentLenを設定する。

	@param	v 設定する値。
    */
    public void setExtentLen(UDF_uint32a v){replaceChild(v, extentLen); extentLen = v;}
    /**
	extentLocを設定する。

	@param	v 設定する値。
    */
    public void setExtentLoc(UDF_lb_addr v){replaceChild(v, extentLoc); extentLoc = v;}
    /**
	implUseを設定する。

	@param	v 設定する値。
    */
    public void setImplUse(UDF_bytes v){replaceChild(v, implUse); implUse = v;}

    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long rsize = 0;
	preReadHook(f);
	extentLen = (UDF_uint32a)createElement("UDF_uint32a", "", "extent-len");
	rsize += extentLen.readFrom(f);
	extentLoc = (UDF_lb_addr)createElement("UDF_lb_addr", "", "extent-loc");
	rsize += extentLoc.readFrom(f);
	implUse = (UDF_bytes)createElement("UDF_bytes", "", "impl-use");
	implUse.setSize(6);
	rsize += implUse.readFrom(f);
	apply( );
	postReadHook(f);
	return rsize;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long wsize = 0;
	wsize += extentLen.writeTo(f);
	wsize += extentLoc.writeTo(f);
	wsize += implUse.writeTo(f);
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
	    else if(name.equals("extent-len")){
		extentLen = (UDF_uint32a)createElement("UDF_uint32a", "", "extent-len");
		extentLen.readFromXML(child);
	    }
	    else if(name.equals("extent-loc")){
		extentLoc = (UDF_lb_addr)createElement("UDF_lb_addr", "", "extent-loc");
		extentLoc.readFromXML(child);
	    }
	    else if(name.equals("impl-use")){
		implUse = (UDF_bytes)createElement("UDF_bytes", "", "impl-use");
		implUse.setSize(6);
		implUse.readFromXML(child);
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
	extentLen = (UDF_uint32a)createElement("UDF_uint32a", "", "extent-len");
	extentLen.setDefaultValue();
	extentLoc = (UDF_lb_addr)createElement("UDF_lb_addr", "", "extent-loc");
	extentLoc.setDefaultValue();
	implUse = (UDF_bytes)createElement("UDF_bytes", "", "impl-use");
	implUse.setSize(6);
	implUse.setDefaultValue();

	apply( );
	postSetDefaultValueHook();
    }


    /**
	複製する
    */
    public UDF_Element duplicateElement(){
	UDF_long_ad dup = new UDF_long_ad(this, null, getName());
	dup.setDefaultValue();
	dup.duplicateHook(this);
	dup.setExtentLen((UDF_uint32a)extentLen.duplicateElement());
	dup.setExtentLoc((UDF_lb_addr)extentLoc.duplicateElement());
	dup.setImplUse((UDF_bytes)implUse.duplicateElement());

	apply();

	return dup;
    }

    /**
        子の UDF Element の Node を append しなおす。

    */
    private void apply( ) {

	removeAllChildren();
	appendChild(extentLen);
	appendChild(extentLoc);
	appendChild(implUse);
    }

    public String getInfo(int indent){
	String a = "";
	a += UDF_Util.repeat(' ', indent);
	a += extentLen.getInfo(indent + 1);
	a += extentLoc.getInfo(indent + 1);
	a += implUse.getInfo(indent + 1);
      return a;
    }
    public void debug(int indent){
	System.err.print(UDF_Util.repeat(' ', indent));

	String info = " gp=" + getGlobalPoint();
	info += " pn=" + getElemPartRefNo();
	info += " sn=" + getPartSubno();
	System.err.println(getName() + ": (" + hashCode() + ")" + info);	extentLen.debug(indent + 1);
	extentLoc.debug(indent + 1);
	implUse.debug(indent + 1);
    }
//begin:add your code here

    public int getPartRefNo(){
	return extentLoc.getPartReferenceNumber().getIntValue();
    }
    public int getLbn(){
	return extentLoc.getLogicalBlockNumber().getIntValue();
    }
    public long getLen(){
	return extentLen.getLongValue() & 0x3fffffffL;
    }
    public int getFlag(){
	return extentLen.getFlag();
    }

    public void setPartRefNo(int partno){
	setElemPartRefNo(partno);
	extentLoc.getPartReferenceNumber().setValue(partno);
    }
    public void setLbn(int lbn){
	extentLoc.getLogicalBlockNumber().setValue(lbn);
    }
    public void setLen(long len){
	extentLen.setValue(len);
    }
    public void setFlag(int flag){
	extentLen.setFlag(flag);
    }

    public UDF_uint32 getExtentLbn(){
	return extentLoc.getLogicalBlockNumber();
    }
    public UDF_uint16 getExtentPartRefNo(){
	return extentLoc.getPartReferenceNumber();
    }
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = super.verify();
	
	
	el.addError(extentLoc.verify("Extent Location"));
	if(getLen() == 0 && getLbn() != 0){
	    
	    el.addError(new UDF_Error
			(UDF_Error.C_ECMA167, UDF_Error.L_ERROR, "Extent Location",
			 "This field shall specify the logical block number of the extent. " +
			 "If the extent's length is 0, no extent is specified and this field shall contain 0.",
			 "4/14.14.2.2", String.valueOf(getLbn()), "0"));
	}
	
	el.setRName("Long Allocation Descriptor");
	return el;
    }    
//end:
};
