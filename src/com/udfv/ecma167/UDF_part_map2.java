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
Type&nbsp;2&nbsp;Partition&nbsp;Map&nbsp;を表現するクラス。<br/>


<dl>

<dt>フィールド一覧</dt>
<dd>
<table border="1">
<tr><td>メンバ</td><td>型</td><td>サイズ</td></tr>
<tr><td><b>PartMapType</b></td><td><b>UDF_uint8</b></td><td><i>1</i></td></tr>
<tr><td><b>PartMapLen</b></td><td><b>UDF_uint8</b></td><td><i>1</i></td></tr>
<tr><td><b>PartId</b></td><td><b>UDF_bytes</b></td><td><i>62</i></td></tr>
</table>
</dd>
</dl>
*/
public class UDF_part_map2 extends UDF_PartMap 
{

    /**
	このオブジェクトのパッケージ名を除いたクラス名を取得する。

	@return クラス名
    */
    public static String getUDFClassName( ) {
        return "UDF_part_map2";
    }

    /**
	コンストラクタ。

	@param elem   親エレメント。
	@param prefix ネームスペース。
	@param name   エレメント名。
    */
    public UDF_part_map2(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }
    /**
	このUDFエレメントのサイズを返す。

	@return UDFエレメントのサイズ。
    */
    public int getSize(){
	return 0+1+1+partId.getSize();
    }

    /**
	このUDFエレメントのサイズをlong 型で返す。

	@return UDFエレメントのサイズ。
    */
    public long getLongSize(){
	return (long)0+1+1+partId.getSize();
    }
    private UDF_uint8 partMapType;
    private UDF_uint8 partMapLen;
    private UDF_bytes partId;

    /**
	partMapTypeを取得する。

	@return 取得したpartMapType を返す。
    */
    public UDF_uint8 getPartMapType(){return partMapType;}
    /**
	partMapLenを取得する。

	@return 取得したpartMapLen を返す。
    */
    public UDF_uint8 getPartMapLen(){return partMapLen;}
    /**
	partIdを取得する。

	@return 取得したpartId を返す。
    */
    public UDF_bytes getPartId(){return partId;}

    /**
	partMapTypeを設定する。

	@param	v 設定する値。
    */
    public void setPartMapType(UDF_uint8 v){replaceChild(v, partMapType); partMapType = v;}
    /**
	partMapLenを設定する。

	@param	v 設定する値。
    */
    public void setPartMapLen(UDF_uint8 v){replaceChild(v, partMapLen); partMapLen = v;}
    /**
	partIdを設定する。

	@param	v 設定する値。
    */
    public void setPartId(UDF_bytes v){replaceChild(v, partId); partId = v;}

    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long rsize = 0;
	preReadHook(f);
	partMapType = (UDF_uint8)createElement("UDF_uint8", "", "part-map-type");
	rsize += partMapType.readFrom(f);
	partMapLen = (UDF_uint8)createElement("UDF_uint8", "", "part-map-len");
	rsize += partMapLen.readFrom(f);
	partId = (UDF_bytes)createElement("UDF_bytes", "", "part-id");
	partId.setSize(62);
	rsize += partId.readFrom(f);
	apply( );
	postReadHook(f);
	return rsize;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long wsize = 0;
	wsize += partMapType.writeTo(f);
	wsize += partMapLen.writeTo(f);
	wsize += partId.writeTo(f);
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
	    else if(name.equals("part-map-type")){
		partMapType = (UDF_uint8)createElement("UDF_uint8", "", "part-map-type");
		partMapType.readFromXML(child);
	    }
	    else if(name.equals("part-map-len")){
		partMapLen = (UDF_uint8)createElement("UDF_uint8", "", "part-map-len");
		partMapLen.readFromXML(child);
	    }
	    else if(name.equals("part-id")){
		partId = (UDF_bytes)createElement("UDF_bytes", "", "part-id");
		partId.setSize(62);
		partId.readFromXML(child);
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
	partMapType = (UDF_uint8)createElement("UDF_uint8", "", "part-map-type");
	partMapType.setDefaultValue();
	partMapLen = (UDF_uint8)createElement("UDF_uint8", "", "part-map-len");
	partMapLen.setDefaultValue();
	partId = (UDF_bytes)createElement("UDF_bytes", "", "part-id");
	partId.setSize(62);
	partId.setDefaultValue();

	apply( );
	postSetDefaultValueHook();
    }


    /**
	複製する
    */
    public UDF_Element duplicateElement(){
	UDF_part_map2 dup = new UDF_part_map2(this, null, getName());
	dup.setDefaultValue();
	dup.duplicateHook(this);
	dup.setPartMapType((UDF_uint8)partMapType.duplicateElement());
	dup.setPartMapLen((UDF_uint8)partMapLen.duplicateElement());
	dup.setPartId((UDF_bytes)partId.duplicateElement());

	apply();

	return dup;
    }

    /**
        子の UDF Element の Node を append しなおす。

    */
    private void apply( ) {

	removeAllChildren();
	appendChild(partMapType);
	appendChild(partMapLen);
	appendChild(partId);
    }

    public void debug(int indent){
	System.err.print(UDF_Util.repeat(' ', indent));
	System.err.println(getName() + ": (" + hashCode() + ")");	partMapType.debug(indent + 1);
	partMapLen.debug(indent + 1);
	partId.debug(indent + 1);
    }
//begin:add your code here

    public UDF_uint16 getPartNumber(){return null;}

    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	final short category = UDF_Error.C_ECMA167;
	
	
	if(partMapType.getIntValue() != 2){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Partition Map Type", "This field shall specify 2.","3/10.7.3.1",
			 String.valueOf(partMapType.getIntValue()), "2"));
	}
	
	if(partMapLen.getIntValue() != 64){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Partition Map Length", "This field shall specify 64.",
			 "3/10.7.3.2", String.valueOf(partMapLen.getIntValue()), "64"));
	}
	
	el.setRName("Type 2 Partition Map");
	return el;
    }
//end:
};
