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
NSR&nbsp;Descriptor&nbsp;を表現するクラス。<br/>
<br/>
<em><br/>
ECMA-167&nbsp;2nd&nbsp;Edition&nbsp;の定義。<br/>
</em><br/>


<dl>

<dt>フィールド一覧</dt>
<dd>
<table border="1">
<tr><td>メンバ</td><td>型</td><td>サイズ</td></tr>
<tr><td><b>StructureType</b></td><td><b>UDF_uint8</b></td><td><i>1</i></td></tr>
<tr><td><b>StandardId</b></td><td><b>UDF_bytes</b></td><td><i>5</i></td></tr>
<tr><td><b>StructureVersion</b></td><td><b>UDF_uint8</b></td><td><i>1</i></td></tr>
<tr><td><b>Reserved</b></td><td><b>UDF_bytes</b></td><td><i>1</i></td></tr>
<tr><td><b>StructureData</b></td><td><b>UDF_bytes</b></td><td><i>2040</i></td></tr>
</table>
</dd>
</dl>
*/
public class UDF_ECMA167_NSR02 extends UDF_Element 
{

    /**
	このオブジェクトのパッケージ名を除いたクラス名を取得する。

	@return クラス名
    */
    public static String getUDFClassName( ) {
        return "UDF_ECMA167_NSR02";
    }

    /**
	コンストラクタ。

	@param elem   親エレメント。
	@param prefix ネームスペース。
	@param name   エレメント名。
    */
    public UDF_ECMA167_NSR02(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }
    /**
	このUDFエレメントのサイズを返す。

	@return UDFエレメントのサイズ。
    */
    public int getSize(){
	return 0+1+standardId.getSize()+1+reserved.getSize()+structureData.getSize();
    }

    /**
	このUDFエレメントのサイズをlong 型で返す。

	@return UDFエレメントのサイズ。
    */
    public long getLongSize(){
	return (long)0+1+standardId.getSize()+1+reserved.getSize()+structureData.getSize();
    }
    private UDF_uint8 structureType;
    private UDF_bytes standardId;
    private UDF_uint8 structureVersion;
    private UDF_bytes reserved;
    private UDF_bytes structureData;

    /**
	structureTypeを取得する。

	@return 取得したstructureType を返す。
    */
    public UDF_uint8 getStructureType(){return structureType;}
    /**
	standardIdを取得する。

	@return 取得したstandardId を返す。
    */
    public UDF_bytes getStandardId(){return standardId;}
    /**
	structureVersionを取得する。

	@return 取得したstructureVersion を返す。
    */
    public UDF_uint8 getStructureVersion(){return structureVersion;}
    /**
	reservedを取得する。

	@return 取得したreserved を返す。
    */
    public UDF_bytes getReserved(){return reserved;}
    /**
	structureDataを取得する。

	@return 取得したstructureData を返す。
    */
    public UDF_bytes getStructureData(){return structureData;}

    /**
	structureTypeを設定する。

	@param	v 設定する値。
    */
    public void setStructureType(UDF_uint8 v){replaceChild(v, structureType); structureType = v;}
    /**
	standardIdを設定する。

	@param	v 設定する値。
    */
    public void setStandardId(UDF_bytes v){replaceChild(v, standardId); standardId = v;}
    /**
	structureVersionを設定する。

	@param	v 設定する値。
    */
    public void setStructureVersion(UDF_uint8 v){replaceChild(v, structureVersion); structureVersion = v;}
    /**
	reservedを設定する。

	@param	v 設定する値。
    */
    public void setReserved(UDF_bytes v){replaceChild(v, reserved); reserved = v;}
    /**
	structureDataを設定する。

	@param	v 設定する値。
    */
    public void setStructureData(UDF_bytes v){replaceChild(v, structureData); structureData = v;}

    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long rsize = 0;
	preReadHook(f);
	structureType = (UDF_uint8)createElement("UDF_uint8", "", "structure-type");
	rsize += structureType.readFrom(f);
	standardId = (UDF_bytes)createElement("UDF_bytes", "", "standard-id");
	standardId.setSize(5);
	rsize += standardId.readFrom(f);
	structureVersion = (UDF_uint8)createElement("UDF_uint8", "", "structure-version");
	rsize += structureVersion.readFrom(f);
	reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
	reserved.setSize(1);
	rsize += reserved.readFrom(f);
	structureData = (UDF_bytes)createElement("UDF_bytes", "", "structure-data");
	structureData.setSize(2040);
	rsize += structureData.readFrom(f);
	apply( );
	postReadHook(f);
	return rsize;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long wsize = 0;
	wsize += structureType.writeTo(f);
	wsize += standardId.writeTo(f);
	wsize += structureVersion.writeTo(f);
	wsize += reserved.writeTo(f);
	wsize += structureData.writeTo(f);
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
	    else if(name.equals("structure-type")){
		structureType = (UDF_uint8)createElement("UDF_uint8", "", "structure-type");
		structureType.readFromXML(child);
	    }
	    else if(name.equals("standard-id")){
		standardId = (UDF_bytes)createElement("UDF_bytes", "", "standard-id");
		standardId.setSize(5);
		standardId.readFromXML(child);
	    }
	    else if(name.equals("structure-version")){
		structureVersion = (UDF_uint8)createElement("UDF_uint8", "", "structure-version");
		structureVersion.readFromXML(child);
	    }
	    else if(name.equals("reserved")){
		reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
		reserved.setSize(1);
		reserved.readFromXML(child);
	    }
	    else if(name.equals("structure-data")){
		structureData = (UDF_bytes)createElement("UDF_bytes", "", "structure-data");
		structureData.setSize(2040);
		structureData.readFromXML(child);
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
	structureType = (UDF_uint8)createElement("UDF_uint8", "", "structure-type");
	structureType.setDefaultValue();
	standardId = (UDF_bytes)createElement("UDF_bytes", "", "standard-id");
	standardId.setSize(5);
	standardId.setDefaultValue();
	standardId.setValue("NSR02");
	structureVersion = (UDF_uint8)createElement("UDF_uint8", "", "structure-version");
	structureVersion.setDefaultValue();
	structureVersion.setValue(1);
	reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
	reserved.setSize(1);
	reserved.setDefaultValue();
	structureData = (UDF_bytes)createElement("UDF_bytes", "", "structure-data");
	structureData.setSize(2040);
	structureData.setDefaultValue();

	apply( );
	postSetDefaultValueHook();
    }


    /**
	複製する
    */
    public UDF_Element duplicateElement(){
	UDF_ECMA167_NSR02 dup = new UDF_ECMA167_NSR02(this, null, getName());
	dup.setDefaultValue();
	dup.duplicateHook(this);
	dup.setStructureType((UDF_uint8)structureType.duplicateElement());
	dup.setStandardId((UDF_bytes)standardId.duplicateElement());
	dup.setStructureVersion((UDF_uint8)structureVersion.duplicateElement());
	dup.setReserved((UDF_bytes)reserved.duplicateElement());
	dup.setStructureData((UDF_bytes)structureData.duplicateElement());

	apply();

	return dup;
    }

    /**
        子の UDF Element の Node を append しなおす。

    */
    private void apply( ) {

	removeAllChildren();
	appendChild(structureType);
	appendChild(standardId);
	appendChild(structureVersion);
	appendChild(reserved);
	appendChild(structureData);
    }

    public String getInfo(int indent){
	String a = "";
	a += UDF_Util.repeat(' ', indent);
	a += structureType.getInfo(indent + 1);
	a += standardId.getInfo(indent + 1);
	a += structureVersion.getInfo(indent + 1);
	a += reserved.getInfo(indent + 1);
	a += structureData.getInfo(indent + 1);
      return a;
    }
    public void debug(int indent){
	System.err.print(UDF_Util.repeat(' ', indent));

	String info = " gp=" + getGlobalPoint();
	info += " pn=" + getElemPartRefNo();
	info += " sn=" + getPartSubno();
	System.err.println(getName() + ": (" + hashCode() + ")" + info);	structureType.debug(indent + 1);
	standardId.debug(indent + 1);
	structureVersion.debug(indent + 1);
	reserved.debug(indent + 1);
	structureData.debug(indent + 1);
    }
//begin:add your code here
    protected void init( ) {
        super.init();

        //　この要素の記録されている論理位置をＸＭＬに表記するように設定します　//
	//setViewGlobalPoint(true);
    }
    protected boolean hasGlobalPoint(){
	return true;
    }
//end:
};
