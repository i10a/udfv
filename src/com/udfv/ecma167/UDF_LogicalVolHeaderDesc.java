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
Logical&nbsp;Volume&nbsp;Header&nbsp;Descriptor&nbsp;を表現するクラス。<br/>


<dl>

<dt>フィールド一覧</dt>
<dd>
<table border="1">
<tr><td>メンバ</td><td>型</td><td>サイズ</td></tr>
<tr><td><b>UniqueId</b></td><td><b>UDF_uint64</b></td><td><i>8</i></td></tr>
<tr><td><b>Reserved</b></td><td><b>UDF_bytes</b></td><td><i>24</i></td></tr>
</table>
</dd>
</dl>
*/
public class UDF_LogicalVolHeaderDesc extends UDF_Element 
{

    /**
	このオブジェクトのパッケージ名を除いたクラス名を取得する。

	@return クラス名
    */
    public static String getUDFClassName( ) {
        return "UDF_LogicalVolHeaderDesc";
    }

    /**
	コンストラクタ。

	@param elem   親エレメント。
	@param prefix ネームスペース。
	@param name   エレメント名。
    */
    public UDF_LogicalVolHeaderDesc(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }
    /**
	このUDFエレメントのサイズを返す。

	@return UDFエレメントのサイズ。
    */
    public int getSize(){
	return 0+8+reserved.getSize();
    }

    /**
	このUDFエレメントのサイズをlong 型で返す。

	@return UDFエレメントのサイズ。
    */
    public long getLongSize(){
	return (long)0+8+reserved.getSize();
    }
    private UDF_uint64 uniqueId;
    private UDF_bytes reserved;

    /**
	uniqueIdを取得する。

	@return 取得したuniqueId を返す。
    */
    public UDF_uint64 getUniqueId(){return uniqueId;}
    /**
	reservedを取得する。

	@return 取得したreserved を返す。
    */
    public UDF_bytes getReserved(){return reserved;}

    /**
	uniqueIdを設定する。

	@param	v 設定する値。
    */
    public void setUniqueId(UDF_uint64 v){replaceChild(v, uniqueId); uniqueId = v;}
    /**
	reservedを設定する。

	@param	v 設定する値。
    */
    public void setReserved(UDF_bytes v){replaceChild(v, reserved); reserved = v;}

    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long rsize = 0;
	preReadHook(f);
	uniqueId = (UDF_uint64)createElement("UDF_uint64", "", "unique-id");
	rsize += uniqueId.readFrom(f);
	reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
	reserved.setSize(24);
	rsize += reserved.readFrom(f);
	apply( );
	postReadHook(f);
	return rsize;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long wsize = 0;
	wsize += uniqueId.writeTo(f);
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
	    else if(name.equals("unique-id")){
		uniqueId = (UDF_uint64)createElement("UDF_uint64", "", "unique-id");
		uniqueId.readFromXML(child);
	    }
	    else if(name.equals("reserved")){
		reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
		reserved.setSize(24);
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
	uniqueId = (UDF_uint64)createElement("UDF_uint64", "", "unique-id");
	uniqueId.setDefaultValue();
	reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
	reserved.setSize(24);
	reserved.setDefaultValue();

	apply( );
	postSetDefaultValueHook();
    }


    /**
	複製する
    */
    public UDF_Element duplicateElement(){
	UDF_LogicalVolHeaderDesc dup = new UDF_LogicalVolHeaderDesc(this, null, getName());
	dup.setDefaultValue();
	dup.duplicateHook(this);
	dup.setUniqueId((UDF_uint64)uniqueId.duplicateElement());
	dup.setReserved((UDF_bytes)reserved.duplicateElement());

	apply();

	return dup;
    }

    /**
        子の UDF Element の Node を append しなおす。

    */
    private void apply( ) {

	removeAllChildren();
	appendChild(uniqueId);
	appendChild(reserved);
    }

    public String getInfo(int indent){
	String a = "";
	a += UDF_Util.repeat(' ', indent);
	a += uniqueId.getInfo(indent + 1);
	a += reserved.getInfo(indent + 1);
      return a;
    }
    public void debug(int indent){
	System.err.print(UDF_Util.repeat(' ', indent));

	String info = " gp=" + getGlobalPoint();
	info += " pn=" + getElemPartRefNo();
	info += " sn=" + getPartSubno();
	System.err.println(getName() + ": (" + hashCode() + ")" + info);	uniqueId.debug(indent + 1);
	reserved.debug(indent + 1);
    }
//begin:add your code here
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	if(!UDF_Util.isAllZero(reserved.getData())){
	    
	    el.addError(new UDF_Error
			(UDF_Error.C_ECMA167, UDF_Error.L_ERROR, "Reserved",
			 "This field shall be reserved for future standardisation and all bytes shall contain #00.",
			 "4/14.15.2"));
	}
	
	el.setRName("Logical Volume Header Descriptor");
	return el;
    }
    /**
       Unique Idを取得し、自身のUnique Idを1プラスする。

       @return Unique Id
     */
    public long getNextUniqueId(){
	long value = getUniqueId().getLongValue();
	getUniqueId().setValue(value + 1);
	return value;
    }
    
//end:
};
