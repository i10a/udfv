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
<tr><td><b>OSClass</b></td><td><b>UDF_uint8</b></td><td><i>1</i></td></tr>
<tr><td><b>OSId</b></td><td><b>UDF_uint8</b></td><td><i>1</i></td></tr>
<tr><td><b>ImplUseArea</b></td><td><b>UDF_bytes</b></td><td><i>6</i></td></tr>
</table>
</dd>
</dl>
*/
public class UDF_regid_UDFImplIdSuffix extends UDF_Element 
{

    /**
	このオブジェクトのパッケージ名を除いたクラス名を取得する。

	@return クラス名
    */
    public static String getUDFClassName( ) {
        return "UDF_regid_UDFImplIdSuffix";
    }

    /**
	コンストラクタ。

	@param elem   親エレメント。
	@param prefix ネームスペース。
	@param name   エレメント名。
    */
    public UDF_regid_UDFImplIdSuffix(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }
    /**
	このUDFエレメントのサイズを返す。

	@return UDFエレメントのサイズ。
    */
    public int getSize(){
	return 0+1+1+implUseArea.getSize();
    }

    /**
	このUDFエレメントのサイズをlong 型で返す。

	@return UDFエレメントのサイズ。
    */
    public long getLongSize(){
	return (long)0+1+1+implUseArea.getSize();
    }
    private UDF_uint8 oSClass;
    private UDF_uint8 oSId;
    private UDF_bytes implUseArea;

    /**
	oSClassを取得する。

	@return 取得したoSClass を返す。
    */
    public UDF_uint8 getOSClass(){return oSClass;}
    /**
	oSIdを取得する。

	@return 取得したoSId を返す。
    */
    public UDF_uint8 getOSId(){return oSId;}
    /**
	implUseAreaを取得する。

	@return 取得したimplUseArea を返す。
    */
    public UDF_bytes getImplUseArea(){return implUseArea;}

    /**
	oSClassを設定する。

	@param	v 設定する値。
    */
    public void setOSClass(UDF_uint8 v){replaceChild(v, oSClass); oSClass = v;}
    /**
	oSIdを設定する。

	@param	v 設定する値。
    */
    public void setOSId(UDF_uint8 v){replaceChild(v, oSId); oSId = v;}
    /**
	implUseAreaを設定する。

	@param	v 設定する値。
    */
    public void setImplUseArea(UDF_bytes v){replaceChild(v, implUseArea); implUseArea = v;}

    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long rsize = 0;
	preReadHook(f);
	oSClass = (UDF_uint8)createElement("UDF_uint8", "", "os-class");
	rsize += oSClass.readFrom(f);
	oSId = (UDF_uint8)createElement("UDF_uint8", "", "os-id");
	rsize += oSId.readFrom(f);
	implUseArea = (UDF_bytes)createElement("UDF_bytes", "", "impl-use-area");
	implUseArea.setSize(6);
	rsize += implUseArea.readFrom(f);
	apply( );
	postReadHook(f);
	return rsize;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long wsize = 0;
	wsize += oSClass.writeTo(f);
	wsize += oSId.writeTo(f);
	wsize += implUseArea.writeTo(f);
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
	    else if(name.equals("os-class")){
		oSClass = (UDF_uint8)createElement("UDF_uint8", "", "os-class");
		oSClass.readFromXML(child);
	    }
	    else if(name.equals("os-id")){
		oSId = (UDF_uint8)createElement("UDF_uint8", "", "os-id");
		oSId.readFromXML(child);
	    }
	    else if(name.equals("impl-use-area")){
		implUseArea = (UDF_bytes)createElement("UDF_bytes", "", "impl-use-area");
		implUseArea.setSize(6);
		implUseArea.readFromXML(child);
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
	oSClass = (UDF_uint8)createElement("UDF_uint8", "", "os-class");
	oSClass.setDefaultValue();
	oSId = (UDF_uint8)createElement("UDF_uint8", "", "os-id");
	oSId.setDefaultValue();
	implUseArea = (UDF_bytes)createElement("UDF_bytes", "", "impl-use-area");
	implUseArea.setSize(6);
	implUseArea.setDefaultValue();

	apply( );
	postSetDefaultValueHook();
    }


    /**
	複製する
    */
    public UDF_Element duplicateElement(){
	UDF_regid_UDFImplIdSuffix dup = new UDF_regid_UDFImplIdSuffix(this, null, getName());
	dup.setDefaultValue();
	dup.duplicateHook(this);
	dup.setOSClass((UDF_uint8)oSClass.duplicateElement());
	dup.setOSId((UDF_uint8)oSId.duplicateElement());
	dup.setImplUseArea((UDF_bytes)implUseArea.duplicateElement());

	apply();

	return dup;
    }

    /**
        子の UDF Element の Node を append しなおす。

    */
    private void apply( ) {

	removeAllChildren();
	appendChild(oSClass);
	appendChild(oSId);
	appendChild(implUseArea);
    }

    public void debug(int indent){
	System.err.print(UDF_Util.repeat(' ', indent));
	System.err.println(getName() + ": (" + hashCode() + ")");	oSClass.debug(indent + 1);
	oSId.debug(indent + 1);
	implUseArea.debug(indent + 1);
    }
//begin:add your code here
//end:
};
