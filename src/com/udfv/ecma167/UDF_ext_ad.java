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
Extended&nbsp;Allocation&nbsp;Descriptor&nbsp;を表現するクラス。<br/>


<dl>

<dt>フィールド一覧</dt>
<dd>
<table border="1">
<tr><td>メンバ</td><td>型</td><td>サイズ</td></tr>
<tr><td><b>ExtentLen</b></td><td><b>UDF_uint32a</b></td><td><i>extentLen.getSize()</i></td></tr>
<tr><td><b>RecordedLen</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>InfoLen</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>ExtentLoc</b></td><td><b>UDF_lb_addr</b></td><td><i>extentLoc.getSize()</i></td></tr>
<tr><td><b>ImplUse</b></td><td><b>UDF_bytes</b></td><td><i>2</i></td></tr>
</table>
</dd>
</dl>
*/
public class UDF_ext_ad extends UDF_AD 
{

    /**
	このオブジェクトのパッケージ名を除いたクラス名を取得する。

	@return クラス名
    */
    public static String getUDFClassName( ) {
        return "UDF_ext_ad";
    }

    /**
	コンストラクタ。

	@param elem   親エレメント。
	@param prefix ネームスペース。
	@param name   エレメント名。
    */
    public UDF_ext_ad(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }
    /**
	このUDFエレメントのサイズを返す。

	@return UDFエレメントのサイズ。
    */
    public int getSize(){
	return 0+extentLen.getSize()+4+4+extentLoc.getSize()+implUse.getSize();
    }

    /**
	このUDFエレメントのサイズをlong 型で返す。

	@return UDFエレメントのサイズ。
    */
    public long getLongSize(){
	return (long)0+extentLen.getSize()+4+4+extentLoc.getSize()+implUse.getSize();
    }
    private UDF_uint32a extentLen;
    private UDF_uint32 recordedLen;
    private UDF_uint32 infoLen;
    private UDF_lb_addr extentLoc;
    private UDF_bytes implUse;

    /**
	extentLenを取得する。

	@return 取得したextentLen を返す。
    */
    public UDF_uint32a getExtentLen(){return extentLen;}
    /**
	recordedLenを取得する。

	@return 取得したrecordedLen を返す。
    */
    public UDF_uint32 getRecordedLen(){return recordedLen;}
    /**
	infoLenを取得する。

	@return 取得したinfoLen を返す。
    */
    public UDF_uint32 getInfoLen(){return infoLen;}
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
	recordedLenを設定する。

	@param	v 設定する値。
    */
    public void setRecordedLen(UDF_uint32 v){replaceChild(v, recordedLen); recordedLen = v;}
    /**
	infoLenを設定する。

	@param	v 設定する値。
    */
    public void setInfoLen(UDF_uint32 v){replaceChild(v, infoLen); infoLen = v;}
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
	recordedLen = (UDF_uint32)createElement("UDF_uint32", "", "recorded-len");
	rsize += recordedLen.readFrom(f);
	infoLen = (UDF_uint32)createElement("UDF_uint32", "", "info-len");
	rsize += infoLen.readFrom(f);
	extentLoc = (UDF_lb_addr)createElement("UDF_lb_addr", "", "extent-loc");
	rsize += extentLoc.readFrom(f);
	implUse = (UDF_bytes)createElement("UDF_bytes", "", "impl-use");
	implUse.setSize(2);
	rsize += implUse.readFrom(f);
	apply( );
	postReadHook(f);
	return rsize;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long wsize = 0;
	wsize += extentLen.writeTo(f);
	wsize += recordedLen.writeTo(f);
	wsize += infoLen.writeTo(f);
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
	    else if(name.equals("recorded-len")){
		recordedLen = (UDF_uint32)createElement("UDF_uint32", "", "recorded-len");
		recordedLen.readFromXML(child);
	    }
	    else if(name.equals("info-len")){
		infoLen = (UDF_uint32)createElement("UDF_uint32", "", "info-len");
		infoLen.readFromXML(child);
	    }
	    else if(name.equals("extent-loc")){
		extentLoc = (UDF_lb_addr)createElement("UDF_lb_addr", "", "extent-loc");
		extentLoc.readFromXML(child);
	    }
	    else if(name.equals("impl-use")){
		implUse = (UDF_bytes)createElement("UDF_bytes", "", "impl-use");
		implUse.setSize(2);
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
	recordedLen = (UDF_uint32)createElement("UDF_uint32", "", "recorded-len");
	recordedLen.setDefaultValue();
	infoLen = (UDF_uint32)createElement("UDF_uint32", "", "info-len");
	infoLen.setDefaultValue();
	extentLoc = (UDF_lb_addr)createElement("UDF_lb_addr", "", "extent-loc");
	extentLoc.setDefaultValue();
	implUse = (UDF_bytes)createElement("UDF_bytes", "", "impl-use");
	implUse.setSize(2);
	implUse.setDefaultValue();

	apply( );
	postSetDefaultValueHook();
    }


    /**
	複製する
    */
    public UDF_Element duplicateElement(){
	UDF_ext_ad dup = new UDF_ext_ad(this, null, getName());
	dup.setDefaultValue();
	dup.duplicateHook(this);
	dup.setExtentLen((UDF_uint32a)extentLen.duplicateElement());
	dup.setRecordedLen((UDF_uint32)recordedLen.duplicateElement());
	dup.setInfoLen((UDF_uint32)infoLen.duplicateElement());
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
	appendChild(recordedLen);
	appendChild(infoLen);
	appendChild(extentLoc);
	appendChild(implUse);
    }

    public String getInfo(int indent){
	String a = "";
	a += UDF_Util.repeat(' ', indent);
	a += extentLen.getInfo(indent + 1);
	a += recordedLen.getInfo(indent + 1);
	a += infoLen.getInfo(indent + 1);
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
	recordedLen.debug(indent + 1);
	infoLen.debug(indent + 1);
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
	//	return (int)((extentLen.getLongValue() >> 6) & 0x3);
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
	final short category = UDF_Error.C_ECMA167;
	
	
	// Recorded Length の上位2ビットは予約されている
	if((recordedLen.getLongValue() & 0x3fffffff) != 0){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Recorded Length",
			 "The two most significant bits of this field are reserved for future standardisation and shall be set to ZERO.",
			 "4/14.14.3.2", String.valueOf(recordedLen.getLongValue()), ""));
	}
	
	// Recorded Length はExtent Length とおそらく異なる
	if(extentLen.getLongValue() == recordedLen.getLongValue()){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_WARNING, "Recorded Length",
			 "This may be different from the number of bytes specified in the Extent Length field.",
			 "4/14.14.3.2"));
	}
	
	// Information Length はおそらくExtent Len ともRecorded Len とも異なる
	if(infoLen.getLongValue() == extentLen.getLongValue() ||
	   infoLen.getLongValue() == recordedLen.getLongValue()){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_WARNING, "Information Length",
			 "This may be different from the value in either the Extent Length field or the Recorded Length field.",
			 "4/14.14.3.3"));
	}
	
	el.addError(extentLoc.verify("Extent Location"));
	
	el.setRName("Extended Allocation Descriptor");
	return el;
    }
//end:
};
