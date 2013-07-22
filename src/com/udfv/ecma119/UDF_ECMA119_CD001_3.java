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
ECMA119/8.6で規定されるVolumePartitionDescriptorを表現するクラス。

<dl>
<dt>詳細</dt><dd>UDFVライブラリでは現在VolumePartitionDescriptorに対応していないため、ボリューム中に存在しても正しく処理される保証はない。</dd>
<dt>フィールド一覧</dt>
<dd>
<table border="1">
<tr><td>メンバ</td><td>型</td><td>サイズ</td></tr>
<tr><td><b>VolDescType</b></td><td><b>UDF_uint8</b></td><td><i>1</i></td></tr>
<tr><td><b>StandardId</b></td><td><b>UDF_bytes</b></td><td><i>5</i></td></tr>
<tr><td><b>VolDescVersion</b></td><td><b>UDF_uint8</b></td><td><i>1</i></td></tr>
<tr><td><b>UnusedField</b></td><td><b>UDF_bytes</b></td><td><i>1</i></td></tr>
<tr><td><b>SystemId</b></td><td><b>UDF_bytes</b></td><td><i>32</i></td></tr>
<tr><td><b>VolPartId</b></td><td><b>UDF_bytes</b></td><td><i>32</i></td></tr>
<tr><td><b>VolPartLoc</b></td><td><b>UDF_uint64</b></td><td><i>8</i></td></tr>
<tr><td><b>VolPartSize</b></td><td><b>UDF_uint64</b></td><td><i>8</i></td></tr>
<tr><td><b>SystemUse</b></td><td><b>UDF_bytes</b></td><td><i>1960</i></td></tr>
</table>
</dd>
</dl>
*/
public class UDF_ECMA119_CD001_3 extends UDF_Element 
{

    /**
	このオブジェクトのパッケージ名を除いたクラス名を取得する。

	@return クラス名
    */
    public static String getUDFClassName( ) {
        return "UDF_ECMA119_CD001_3";
    }

    /**
	コンストラクタ。

	@param elem   親エレメント。
	@param prefix ネームスペース。
	@param name   エレメント名。
    */
    public UDF_ECMA119_CD001_3(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }
    /**
	このUDFエレメントのサイズを返す。

	@return UDFエレメントのサイズ。
    */
    public int getSize(){
	return 0+1+standardId.getSize()+1+unusedField.getSize()+systemId.getSize()+volPartId.getSize()+8+8+systemUse.getSize();
    }

    /**
	このUDFエレメントのサイズをlong 型で返す。

	@return UDFエレメントのサイズ。
    */
    public long getLongSize(){
	return (long)0+1+standardId.getSize()+1+unusedField.getSize()+systemId.getSize()+volPartId.getSize()+8+8+systemUse.getSize();
    }
    private UDF_uint8 volDescType;
    private UDF_bytes standardId;
    private UDF_uint8 volDescVersion;
    private UDF_bytes unusedField;
    private UDF_bytes systemId;
    private UDF_bytes volPartId;
    private UDF_uint64 volPartLoc;
    private UDF_uint64 volPartSize;
    private UDF_bytes systemUse;

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
	unusedFieldを取得する。

	@return 取得したunusedField を返す。
    */
    public UDF_bytes getUnusedField(){return unusedField;}
    /**
	systemIdを取得する。

	@return 取得したsystemId を返す。
    */
    public UDF_bytes getSystemId(){return systemId;}
    /**
	volPartIdを取得する。

	@return 取得したvolPartId を返す。
    */
    public UDF_bytes getVolPartId(){return volPartId;}
    /**
	volPartLocを取得する。

	@return 取得したvolPartLoc を返す。
    */
    public UDF_uint64 getVolPartLoc(){return volPartLoc;}
    /**
	volPartSizeを取得する。

	@return 取得したvolPartSize を返す。
    */
    public UDF_uint64 getVolPartSize(){return volPartSize;}
    /**
	systemUseを取得する。

	@return 取得したsystemUse を返す。
    */
    public UDF_bytes getSystemUse(){return systemUse;}

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
	unusedFieldを設定する。

	@param	v 設定する値。
    */
    public void setUnusedField(UDF_bytes v){replaceChild(v, unusedField); unusedField = v;}
    /**
	systemIdを設定する。

	@param	v 設定する値。
    */
    public void setSystemId(UDF_bytes v){replaceChild(v, systemId); systemId = v;}
    /**
	volPartIdを設定する。

	@param	v 設定する値。
    */
    public void setVolPartId(UDF_bytes v){replaceChild(v, volPartId); volPartId = v;}
    /**
	volPartLocを設定する。

	@param	v 設定する値。
    */
    public void setVolPartLoc(UDF_uint64 v){replaceChild(v, volPartLoc); volPartLoc = v;}
    /**
	volPartSizeを設定する。

	@param	v 設定する値。
    */
    public void setVolPartSize(UDF_uint64 v){replaceChild(v, volPartSize); volPartSize = v;}
    /**
	systemUseを設定する。

	@param	v 設定する値。
    */
    public void setSystemUse(UDF_bytes v){replaceChild(v, systemUse); systemUse = v;}

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
	unusedField = (UDF_bytes)createElement("UDF_bytes", "ecma119", "unused-field");
	unusedField.setSize(1);
	rsize += unusedField.readFrom(f);
	systemId = (UDF_bytes)createElement("UDF_bytes", "ecma119", "system-id");
	systemId.setSize(32);
	rsize += systemId.readFrom(f);
	volPartId = (UDF_bytes)createElement("UDF_bytes", "ecma119", "vol-part-id");
	volPartId.setSize(32);
	rsize += volPartId.readFrom(f);
	volPartLoc = (UDF_uint64)createElement("UDF_uint64", "ecma119", "vol-part-loc");
	rsize += volPartLoc.readFrom(f);
	volPartSize = (UDF_uint64)createElement("UDF_uint64", "ecma119", "vol-part-size");
	rsize += volPartSize.readFrom(f);
	systemUse = (UDF_bytes)createElement("UDF_bytes", "ecma119", "system-use");
	systemUse.setSize(1960);
	rsize += systemUse.readFrom(f);
	apply( );
	postReadHook(f);
	return rsize;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long wsize = 0;
	wsize += volDescType.writeTo(f);
	wsize += standardId.writeTo(f);
	wsize += volDescVersion.writeTo(f);
	wsize += unusedField.writeTo(f);
	wsize += systemId.writeTo(f);
	wsize += volPartId.writeTo(f);
	wsize += volPartLoc.writeTo(f);
	wsize += volPartSize.writeTo(f);
	wsize += systemUse.writeTo(f);
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
	    else if(name.equals("unused-field")){
		unusedField = (UDF_bytes)createElement("UDF_bytes", "ecma119", "unused-field");
		unusedField.setSize(1);
		unusedField.readFromXML(child);
	    }
	    else if(name.equals("system-id")){
		systemId = (UDF_bytes)createElement("UDF_bytes", "ecma119", "system-id");
		systemId.setSize(32);
		systemId.readFromXML(child);
	    }
	    else if(name.equals("vol-part-id")){
		volPartId = (UDF_bytes)createElement("UDF_bytes", "ecma119", "vol-part-id");
		volPartId.setSize(32);
		volPartId.readFromXML(child);
	    }
	    else if(name.equals("vol-part-loc")){
		volPartLoc = (UDF_uint64)createElement("UDF_uint64", "ecma119", "vol-part-loc");
		volPartLoc.readFromXML(child);
	    }
	    else if(name.equals("vol-part-size")){
		volPartSize = (UDF_uint64)createElement("UDF_uint64", "ecma119", "vol-part-size");
		volPartSize.readFromXML(child);
	    }
	    else if(name.equals("system-use")){
		systemUse = (UDF_bytes)createElement("UDF_bytes", "ecma119", "system-use");
		systemUse.setSize(1960);
		systemUse.readFromXML(child);
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
	unusedField = (UDF_bytes)createElement("UDF_bytes", "ecma119", "unused-field");
	unusedField.setSize(1);
	unusedField.setDefaultValue();
	systemId = (UDF_bytes)createElement("UDF_bytes", "ecma119", "system-id");
	systemId.setSize(32);
	systemId.setDefaultValue();
	volPartId = (UDF_bytes)createElement("UDF_bytes", "ecma119", "vol-part-id");
	volPartId.setSize(32);
	volPartId.setDefaultValue();
	volPartLoc = (UDF_uint64)createElement("UDF_uint64", "ecma119", "vol-part-loc");
	volPartLoc.setDefaultValue();
	volPartSize = (UDF_uint64)createElement("UDF_uint64", "ecma119", "vol-part-size");
	volPartSize.setDefaultValue();
	systemUse = (UDF_bytes)createElement("UDF_bytes", "ecma119", "system-use");
	systemUse.setSize(1960);
	systemUse.setDefaultValue();

	apply( );
	postSetDefaultValueHook();
    }


    /**
	複製する
    */
    public UDF_Element duplicateElement(){
	UDF_ECMA119_CD001_3 dup = new UDF_ECMA119_CD001_3(this, null, getName());
	dup.setDefaultValue();
	dup.duplicateHook(this);
	dup.setVolDescType((UDF_uint8)volDescType.duplicateElement());
	dup.setStandardId((UDF_bytes)standardId.duplicateElement());
	dup.setVolDescVersion((UDF_uint8)volDescVersion.duplicateElement());
	dup.setUnusedField((UDF_bytes)unusedField.duplicateElement());
	dup.setSystemId((UDF_bytes)systemId.duplicateElement());
	dup.setVolPartId((UDF_bytes)volPartId.duplicateElement());
	dup.setVolPartLoc((UDF_uint64)volPartLoc.duplicateElement());
	dup.setVolPartSize((UDF_uint64)volPartSize.duplicateElement());
	dup.setSystemUse((UDF_bytes)systemUse.duplicateElement());

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
	appendChild(unusedField);
	appendChild(systemId);
	appendChild(volPartId);
	appendChild(volPartLoc);
	appendChild(volPartSize);
	appendChild(systemUse);
    }

    public String getInfo(int indent){
	String a = "";
	a += UDF_Util.repeat(' ', indent);
	a += volDescType.getInfo(indent + 1);
	a += standardId.getInfo(indent + 1);
	a += volDescVersion.getInfo(indent + 1);
	a += unusedField.getInfo(indent + 1);
	a += systemId.getInfo(indent + 1);
	a += volPartId.getInfo(indent + 1);
	a += volPartLoc.getInfo(indent + 1);
	a += volPartSize.getInfo(indent + 1);
	a += systemUse.getInfo(indent + 1);
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
	unusedField.debug(indent + 1);
	systemId.debug(indent + 1);
	volPartId.debug(indent + 1);
	volPartLoc.debug(indent + 1);
	volPartSize.debug(indent + 1);
	systemUse.debug(indent + 1);
    }
//begin:add your code here
//end:
};
