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
package com.udfv.udf102;

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
no documents.(AUTOMATICALY GENERATED)

<dl>

<dt>フィールド一覧</dt>
<dd>
<table border="1">
<tr><td>メンバ</td><td>型</td><td>サイズ</td></tr>
<tr><td><b>LVICharset</b></td><td><b>UDF_charspec</b></td><td><i>lVICharset.getSize()</i></td></tr>
<tr><td><b>LogicalVolId</b></td><td><b>UDF_dstring</b></td><td><i>128</i></td></tr>
<tr><td><b>LVInfo1</b></td><td><b>UDF_dstring</b></td><td><i>36</i></td></tr>
<tr><td><b>LVInfo2</b></td><td><b>UDF_dstring</b></td><td><i>36</i></td></tr>
<tr><td><b>LVInfo3</b></td><td><b>UDF_dstring</b></td><td><i>36</i></td></tr>
<tr><td><b>ImplId</b></td><td><b>UDF_regid</b></td><td><i>implId.getSize()</i></td></tr>
<tr><td><b>ImplUse</b></td><td><b>UDF_bytes</b></td><td><i>128</i></td></tr>
</table>
</dd>
</dl>
*/
public class UDF_LVInformation extends UDF_Element 
{

    /**
	このオブジェクトのパッケージ名を除いたクラス名を取得する。

	@return クラス名
    */
    public static String getUDFClassName( ) {
        return "UDF_LVInformation";
    }

    /**
	コンストラクタ。

	@param elem   親エレメント。
	@param prefix ネームスペース。
	@param name   エレメント名。
    */
    public UDF_LVInformation(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }
    /**
	このUDFエレメントのサイズを返す。

	@return UDFエレメントのサイズ。
    */
    public int getSize(){
	return 0+lVICharset.getSize()+logicalVolId.getSize()+lVInfo1.getSize()+lVInfo2.getSize()+lVInfo3.getSize()+implId.getSize()+implUse.getSize();
    }

    /**
	このUDFエレメントのサイズをlong 型で返す。

	@return UDFエレメントのサイズ。
    */
    public long getLongSize(){
	return (long)0+lVICharset.getSize()+logicalVolId.getSize()+lVInfo1.getSize()+lVInfo2.getSize()+lVInfo3.getSize()+implId.getSize()+implUse.getSize();
    }
    private com.udfv.ecma167.UDF_charspec lVICharset;
    private UDF_dstring logicalVolId;
    private UDF_dstring lVInfo1;
    private UDF_dstring lVInfo2;
    private UDF_dstring lVInfo3;
    private com.udfv.ecma167.UDF_regid implId;
    private UDF_bytes implUse;

    /**
	lVICharsetを取得する。

	@return 取得したlVICharset を返す。
    */
    public com.udfv.ecma167.UDF_charspec getLVICharset(){return lVICharset;}
    /**
	logicalVolIdを取得する。

	@return 取得したlogicalVolId を返す。
    */
    public UDF_dstring getLogicalVolId(){return logicalVolId;}
    /**
	lVInfo1を取得する。

	@return 取得したlVInfo1 を返す。
    */
    public UDF_dstring getLVInfo1(){return lVInfo1;}
    /**
	lVInfo2を取得する。

	@return 取得したlVInfo2 を返す。
    */
    public UDF_dstring getLVInfo2(){return lVInfo2;}
    /**
	lVInfo3を取得する。

	@return 取得したlVInfo3 を返す。
    */
    public UDF_dstring getLVInfo3(){return lVInfo3;}
    /**
	implIdを取得する。

	@return 取得したimplId を返す。
    */
    public com.udfv.ecma167.UDF_regid getImplId(){return implId;}
    /**
	implUseを取得する。

	@return 取得したimplUse を返す。
    */
    public UDF_bytes getImplUse(){return implUse;}

    /**
	lVICharsetを設定する。

	@param	v 設定する値。
    */
    public void setLVICharset(com.udfv.ecma167.UDF_charspec v){replaceChild(v, lVICharset); lVICharset = v;}
    /**
	logicalVolIdを設定する。

	@param	v 設定する値。
    */
    public void setLogicalVolId(UDF_dstring v){replaceChild(v, logicalVolId); logicalVolId = v;}
    /**
	lVInfo1を設定する。

	@param	v 設定する値。
    */
    public void setLVInfo1(UDF_dstring v){replaceChild(v, lVInfo1); lVInfo1 = v;}
    /**
	lVInfo2を設定する。

	@param	v 設定する値。
    */
    public void setLVInfo2(UDF_dstring v){replaceChild(v, lVInfo2); lVInfo2 = v;}
    /**
	lVInfo3を設定する。

	@param	v 設定する値。
    */
    public void setLVInfo3(UDF_dstring v){replaceChild(v, lVInfo3); lVInfo3 = v;}
    /**
	implIdを設定する。

	@param	v 設定する値。
    */
    public void setImplId(com.udfv.ecma167.UDF_regid v){replaceChild(v, implId); implId = v;}
    /**
	implUseを設定する。

	@param	v 設定する値。
    */
    public void setImplUse(UDF_bytes v){replaceChild(v, implUse); implUse = v;}

    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long rsize = 0;
	preReadHook(f);
	lVICharset = (UDF_charspec)createElement("UDF_charspec", "", "lvi-charset");
	rsize += lVICharset.readFrom(f);
	logicalVolId = (UDF_dstring)createElement("UDF_dstring", "", "logical-vol-id");
	logicalVolId.setSize(128);
	rsize += logicalVolId.readFrom(f);
	lVInfo1 = (UDF_dstring)createElement("UDF_dstring", "", "lv-info1");
	lVInfo1.setSize(36);
	rsize += lVInfo1.readFrom(f);
	lVInfo2 = (UDF_dstring)createElement("UDF_dstring", "", "lv-info2");
	lVInfo2.setSize(36);
	rsize += lVInfo2.readFrom(f);
	lVInfo3 = (UDF_dstring)createElement("UDF_dstring", "", "lv-info3");
	lVInfo3.setSize(36);
	rsize += lVInfo3.readFrom(f);
	implId = (UDF_regid)createElement("UDF_regid", "", "impl-id");
	rsize += implId.readFrom(f);
	implUse = (UDF_bytes)createElement("UDF_bytes", "", "impl-use");
	implUse.setSize(128);
	rsize += implUse.readFrom(f);
	apply( );
	postReadHook(f);
	return rsize;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long wsize = 0;
	wsize += lVICharset.writeTo(f);
	wsize += logicalVolId.writeTo(f);
	wsize += lVInfo1.writeTo(f);
	wsize += lVInfo2.writeTo(f);
	wsize += lVInfo3.writeTo(f);
	wsize += implId.writeTo(f);
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
	    else if(name.equals("lvi-charset")){
		lVICharset = (UDF_charspec)createElement("UDF_charspec", "", "lvi-charset");
		lVICharset.readFromXML(child);
	    }
	    else if(name.equals("logical-vol-id")){
		logicalVolId = (UDF_dstring)createElement("UDF_dstring", "", "logical-vol-id");
		logicalVolId.setSize(128);
		logicalVolId.readFromXML(child);
	    }
	    else if(name.equals("lv-info1")){
		lVInfo1 = (UDF_dstring)createElement("UDF_dstring", "", "lv-info1");
		lVInfo1.setSize(36);
		lVInfo1.readFromXML(child);
	    }
	    else if(name.equals("lv-info2")){
		lVInfo2 = (UDF_dstring)createElement("UDF_dstring", "", "lv-info2");
		lVInfo2.setSize(36);
		lVInfo2.readFromXML(child);
	    }
	    else if(name.equals("lv-info3")){
		lVInfo3 = (UDF_dstring)createElement("UDF_dstring", "", "lv-info3");
		lVInfo3.setSize(36);
		lVInfo3.readFromXML(child);
	    }
	    else if(name.equals("impl-id")){
		implId = (UDF_regid)createElement("UDF_regid", "", "impl-id");
		implId.readFromXML(child);
	    }
	    else if(name.equals("impl-use")){
		implUse = (UDF_bytes)createElement("UDF_bytes", "", "impl-use");
		implUse.setSize(128);
		implUse.readFromXML(child);
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
	lVICharset = (UDF_charspec)createElement("UDF_charspec", "", "lvi-charset");
	lVICharset.setDefaultValue();
	logicalVolId = (UDF_dstring)createElement("UDF_dstring", "", "logical-vol-id");
	logicalVolId.setSize(128);
	logicalVolId.setDefaultValue();
	lVInfo1 = (UDF_dstring)createElement("UDF_dstring", "", "lv-info1");
	lVInfo1.setSize(36);
	lVInfo1.setDefaultValue();
	lVInfo2 = (UDF_dstring)createElement("UDF_dstring", "", "lv-info2");
	lVInfo2.setSize(36);
	lVInfo2.setDefaultValue();
	lVInfo3 = (UDF_dstring)createElement("UDF_dstring", "", "lv-info3");
	lVInfo3.setSize(36);
	lVInfo3.setDefaultValue();
	implId = (UDF_regid)createElement("UDF_regid", "", "impl-id");
	implId.setDefaultValue();
	implUse = (UDF_bytes)createElement("UDF_bytes", "", "impl-use");
	implUse.setSize(128);
	implUse.setDefaultValue();

	apply( );
	postSetDefaultValueHook();
    }


    /**
	複製する
    */
    public UDF_Element duplicateElement(){
	UDF_LVInformation dup = new UDF_LVInformation(this, null, getName());
	dup.setDefaultValue();
	dup.duplicateHook(this);
	dup.setLVICharset((com.udfv.ecma167.UDF_charspec)lVICharset.duplicateElement());
	dup.setLogicalVolId((UDF_dstring)logicalVolId.duplicateElement());
	dup.setLVInfo1((UDF_dstring)lVInfo1.duplicateElement());
	dup.setLVInfo2((UDF_dstring)lVInfo2.duplicateElement());
	dup.setLVInfo3((UDF_dstring)lVInfo3.duplicateElement());
	dup.setImplId((com.udfv.ecma167.UDF_regid)implId.duplicateElement());
	dup.setImplUse((UDF_bytes)implUse.duplicateElement());

	apply();

	return dup;
    }

    /**
        子の UDF Element の Node を append しなおす。

    */
    private void apply( ) {

	removeAllChildren();
	appendChild(lVICharset);
	appendChild(logicalVolId);
	appendChild(lVInfo1);
	appendChild(lVInfo2);
	appendChild(lVInfo3);
	appendChild(implId);
	appendChild(implUse);
    }

    public void debug(int indent){
	System.err.print(UDF_Util.repeat(' ', indent));
	System.err.println(getName() + ": (" + hashCode() + ")");	lVICharset.debug(indent + 1);
	logicalVolId.debug(indent + 1);
	lVInfo1.debug(indent + 1);
	lVInfo2.debug(indent + 1);
	lVInfo3.debug(indent + 1);
	implId.debug(indent + 1);
	implUse.debug(indent + 1);
    }
//begin:add your code here
    public void postReadHook(UDF_RandomAccess f) throws UDF_Exception{
	UDF_Encoding enc = lVICharset.genEncoding();
	logicalVolId.setEncoding(enc);
	lVInfo1.setEncoding(enc);
	lVInfo2.setEncoding(enc);
	lVInfo3.setEncoding(enc);

	implId.extractSuffixAsUDFImplIdSuffix();
    }
    
    public UDF_ErrorList verify() throws UDF_Exception{

	UDF_ErrorList el = new UDF_ErrorList();
	UDF_Error ret;
	
	
	// LogicalVolumeIdentifier
	el.addError(getLogicalVolId().verify("LogicalVolumeIdentifier"));
	// LVInfo1
	el.addError(lVInfo1.verify("LVInfo1"));
	// LVInfo2
	el.addError(lVInfo2.verify("LVInfo2"));
	// LVInfo3
	el.addError(lVInfo3.verify("LVInfo3"));
	// ImplementationID
	el.addError(implId.verify("ImplementationID"));
	
	
	if(env.udf_revision != 0x102)
	    return el;
	
	// CS0 でなければならない
	ret = lVICharset.verifyCharSetType(0);
	if(ret.isError()){
	    
	    ret.setCategory(UDF_Error.C_UDF102);
	    ret.setMessage("Shall be set to indicate support for CS0 only as defined in 2.1.2.");
	    ret.setRefer("2.2.7.2.1");
	    ret.setRName("LVICharset");
	    el.addError(ret);
	}
	
	el.setRName("LVInformation");
	return el;
    }

//end:
};
