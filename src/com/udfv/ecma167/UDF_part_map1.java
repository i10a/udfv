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
Type&nbsp;1&nbsp;Partition&nbsp;Map&nbsp;を表現するクラス。<br/>


<dl>

<dt>フィールド一覧</dt>
<dd>
<table border="1">
<tr><td>メンバ</td><td>型</td><td>サイズ</td></tr>
<tr><td><b>PartMapType</b></td><td><b>UDF_uint8</b></td><td><i>1</i></td></tr>
<tr><td><b>PartMapLen</b></td><td><b>UDF_uint8</b></td><td><i>1</i></td></tr>
<tr><td><b>VolSeqNumber</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td></tr>
<tr><td><b>PartNumber</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td></tr>
</table>
</dd>
</dl>
*/
public class UDF_part_map1 extends UDF_PartMap 
{

    /**
	このオブジェクトのパッケージ名を除いたクラス名を取得する。

	@return クラス名
    */
    public static String getUDFClassName( ) {
        return "UDF_part_map1";
    }

    /**
	コンストラクタ。

	@param elem   親エレメント。
	@param prefix ネームスペース。
	@param name   エレメント名。
    */
    public UDF_part_map1(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }
    /**
	このUDFエレメントのサイズを返す。

	@return UDFエレメントのサイズ。
    */
    public int getSize(){
	return 0+1+1+2+2;
    }

    /**
	このUDFエレメントのサイズをlong 型で返す。

	@return UDFエレメントのサイズ。
    */
    public long getLongSize(){
	return (long)0+1+1+2+2;
    }
    private UDF_uint8 partMapType;
    private UDF_uint8 partMapLen;
    private UDF_uint16 volSeqNumber;
    private UDF_uint16 partNumber;

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
	volSeqNumberを取得する。

	@return 取得したvolSeqNumber を返す。
    */
    public UDF_uint16 getVolSeqNumber(){return volSeqNumber;}
    /**
	partNumberを取得する。

	@return 取得したpartNumber を返す。
    */
    public UDF_uint16 getPartNumber(){return partNumber;}

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
	volSeqNumberを設定する。

	@param	v 設定する値。
    */
    public void setVolSeqNumber(UDF_uint16 v){replaceChild(v, volSeqNumber); volSeqNumber = v;}
    /**
	partNumberを設定する。

	@param	v 設定する値。
    */
    public void setPartNumber(UDF_uint16 v){replaceChild(v, partNumber); partNumber = v;}

    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long rsize = 0;
	preReadHook(f);
	partMapType = (UDF_uint8)createElement("UDF_uint8", "", "part-map-type");
	rsize += partMapType.readFrom(f);
	partMapLen = (UDF_uint8)createElement("UDF_uint8", "", "part-map-len");
	rsize += partMapLen.readFrom(f);
	volSeqNumber = (UDF_uint16)createElement("UDF_uint16", "", "vol-seq-number");
	rsize += volSeqNumber.readFrom(f);
	partNumber = (UDF_uint16)createElement("UDF_uint16", "", "part-number");
	rsize += partNumber.readFrom(f);
	apply( );
	postReadHook(f);
	return rsize;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long wsize = 0;
	wsize += partMapType.writeTo(f);
	wsize += partMapLen.writeTo(f);
	wsize += volSeqNumber.writeTo(f);
	wsize += partNumber.writeTo(f);
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
	    else if(name.equals("vol-seq-number")){
		volSeqNumber = (UDF_uint16)createElement("UDF_uint16", "", "vol-seq-number");
		volSeqNumber.readFromXML(child);
	    }
	    else if(name.equals("part-number")){
		partNumber = (UDF_uint16)createElement("UDF_uint16", "", "part-number");
		partNumber.readFromXML(child);
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
	partMapLen.setValue(6);
	volSeqNumber = (UDF_uint16)createElement("UDF_uint16", "", "vol-seq-number");
	volSeqNumber.setDefaultValue();
	partNumber = (UDF_uint16)createElement("UDF_uint16", "", "part-number");
	partNumber.setDefaultValue();

	apply( );
	postSetDefaultValueHook();
    }


    /**
	複製する
    */
    public UDF_Element duplicateElement(){
	UDF_part_map1 dup = new UDF_part_map1(this, null, getName());
	dup.setDefaultValue();
	dup.duplicateHook(this);
	dup.setPartMapType((UDF_uint8)partMapType.duplicateElement());
	dup.setPartMapLen((UDF_uint8)partMapLen.duplicateElement());
	dup.setVolSeqNumber((UDF_uint16)volSeqNumber.duplicateElement());
	dup.setPartNumber((UDF_uint16)partNumber.duplicateElement());

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
	appendChild(volSeqNumber);
	appendChild(partNumber);
    }

    public void debug(int indent){
	System.err.print(UDF_Util.repeat(' ', indent));
	System.err.println(getName() + ": (" + hashCode() + ")");	partMapType.debug(indent + 1);
	partMapLen.debug(indent + 1);
	volSeqNumber.debug(indent + 1);
	partNumber.debug(indent + 1);
    }
//begin:add your code here
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	final short category = UDF_Error.C_ECMA167;
	
	
	if(partMapType.getIntValue() != 1){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Partition Map Type<" + partMapType.getIntValue() + ">",
			 "This field shall specify 1.", "3/10.7.2.1"));
	}
	
	if(partMapLen.getIntValue() != 6){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Partition Map Length<" + partMapLen.getIntValue() + ">",
			 "This field shall specify 6.",
			 "3/10.7.2.2"));
	}
	
	el.setGlobalPoint(getGlobalPoint());
	el.setRName("Type 1 Partition Map");
	return el;
    }
    /*
       パーティションの開始位置と長さを指定する。

       @param	loc	開始位置(LBS単位)
       @param	len	サイズ

       このメソッドは type1 partition map, sparable partition map
       で有効である。

       それ以外で意味を持たない
    public void setLocAndLen(int loc, long len) throws UDF_PartMapException{
	env.getPartMapExtent(getPartPartno(), 0).addExtent(loc, -1, len);
    }
     */

//end:
};
