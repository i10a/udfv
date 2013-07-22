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
ECMA-167&nbsp;1/7.4&nbsp;Entity&nbsp;identifier&nbsp;を表現するクラス。<br/>


<dl>

<dt>フィールド一覧</dt>
<dd>
<table border="1">
<tr><td>メンバ</td><td>型</td><td>サイズ</td></tr>
<tr><td><b>Flags</b></td><td><b>UDF_uint8</b></td><td><i>1</i></td></tr>
<tr><td><b>Id</b></td><td><b>UDF_bytes</b></td><td><i>23</i></td></tr>
<tr><td><b>IdSuffix</b></td><td><b>UDF_bytes</b></td><td><i>8</i></td></tr>
</table>
</dd>
</dl>
*/
public class UDF_regid extends UDF_Element 
{

    /**
	このオブジェクトのパッケージ名を除いたクラス名を取得する。

	@return クラス名
    */
    public static String getUDFClassName( ) {
        return "UDF_regid";
    }

    /**
	コンストラクタ。

	@param elem   親エレメント。
	@param prefix ネームスペース。
	@param name   エレメント名。
    */
    public UDF_regid(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }
    /**
	このUDFエレメントのサイズを返す。

	@return UDFエレメントのサイズ。
    */
    public int getSize(){
	return 0+1+id.getSize()+idSuffix.getSize();
    }

    /**
	このUDFエレメントのサイズをlong 型で返す。

	@return UDFエレメントのサイズ。
    */
    public long getLongSize(){
	return (long)0+1+id.getSize()+idSuffix.getSize();
    }
    private UDF_uint8 flags;
    private UDF_bytes id;
    private UDF_bytes idSuffix;

    /**
	flagsを取得する。

	@return 取得したflags を返す。
    */
    public UDF_uint8 getFlags(){return flags;}
    /**
	idを取得する。

	@return 取得したid を返す。
    */
    public UDF_bytes getId(){return id;}
    /**
	idSuffixを取得する。

	@return 取得したidSuffix を返す。
    */
    public UDF_bytes getIdSuffix(){return idSuffix;}

    /**
	flagsを設定する。

	@param	v 設定する値。
    */
    public void setFlags(UDF_uint8 v){replaceChild(v, flags); flags = v;}
    /**
	idを設定する。

	@param	v 設定する値。
    */
    public void setId(UDF_bytes v){replaceChild(v, id); id = v;}
    /**
	idSuffixを設定する。

	@param	v 設定する値。
    */
    public void setIdSuffix(UDF_bytes v){replaceChild(v, idSuffix); idSuffix = v;}

    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long rsize = 0;
	preReadHook(f);
	flags = (UDF_uint8)createElement("UDF_uint8", "", "flags");
	rsize += flags.readFrom(f);
	id = (UDF_bytes)createElement("UDF_bytes", "", "id");
	id.setSize(23);
	rsize += id.readFrom(f);
	idSuffix = (UDF_bytes)createElement("UDF_bytes", "", "id-suffix");
	idSuffix.setSize(8);
	rsize += idSuffix.readFrom(f);
	apply( );
	postReadHook(f);
	return rsize;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long wsize = 0;
	wsize += flags.writeTo(f);
	wsize += id.writeTo(f);
	wsize += idSuffix.writeTo(f);
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
	    else if(name.equals("flags")){
		flags = (UDF_uint8)createElement("UDF_uint8", "", "flags");
		flags.readFromXML(child);
	    }
	    else if(name.equals("id")){
		id = (UDF_bytes)createElement("UDF_bytes", "", "id");
		id.setSize(23);
		id.readFromXML(child);
	    }
	    else if(name.equals("id-suffix")){
		idSuffix = (UDF_bytes)createElement("UDF_bytes", "", "id-suffix");
		idSuffix.setSize(8);
		idSuffix.readFromXML(child);
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
	flags = (UDF_uint8)createElement("UDF_uint8", "", "flags");
	flags.setDefaultValue();
	id = (UDF_bytes)createElement("UDF_bytes", "", "id");
	id.setSize(23);
	id.setDefaultValue();
	idSuffix = (UDF_bytes)createElement("UDF_bytes", "", "id-suffix");
	idSuffix.setSize(8);
	idSuffix.setDefaultValue();

	apply( );
	postSetDefaultValueHook();
    }


    /**
	複製する
    */
    public UDF_Element duplicateElement(){
	UDF_regid dup = new UDF_regid(this, null, getName());
	dup.setDefaultValue();
	dup.duplicateHook(this);
	dup.setFlags((UDF_uint8)flags.duplicateElement());
	dup.setId((UDF_bytes)id.duplicateElement());
	dup.setIdSuffix((UDF_bytes)idSuffix.duplicateElement());

	apply();

	return dup;
    }

    /**
        子の UDF Element の Node を append しなおす。

    */
    private void apply( ) {

	removeAllChildren();
	appendChild(flags);
	appendChild(id);
	appendChild(idSuffix);
    }

    public String getInfo(int indent){
	String a = "";
	a += UDF_Util.repeat(' ', indent);
	a += flags.getInfo(indent + 1);
	a += id.getInfo(indent + 1);
	a += idSuffix.getInfo(indent + 1);
      return a;
    }
    public void debug(int indent){
	System.err.print(UDF_Util.repeat(' ', indent));

	String info = " gp=" + getGlobalPoint();
	info += " pn=" + getElemPartRefNo();
	info += " sn=" + getPartSubno();
	System.err.println(getName() + ": (" + hashCode() + ")" + info);	flags.debug(indent + 1);
	id.debug(indent + 1);
	idSuffix.debug(indent + 1);
    }
//begin:add your code here

    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	final short category = UDF_Error.C_ECMA167;
	
	
	if((flags.getIntValue() & 0xfc) != 0){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Flags",
			 "Shall be reserved for future standardisation and all bits shall be set to ZERO.",
			 "1/7.4.1", String.valueOf(flags.getIntValue()), ""));
	}
	
	el.setGlobalPoint(getGlobalPoint());
	return el;
    }
    
    /**
       Flags の値を検証します。
       なお、UDF 1.02 以降では、Flags の値は0でなければなりません。
       
       エラーにはC_ECMA167 カテゴリ、L_ERROR レベル、原因、記録値、期待値が含まれます。
       
       @param flag 比較検証するFlags の値。
       @return エラーインスタンス。
    */
    public UDF_Error verifyFlags(int flag) throws UDF_Exception{
	
	if(getFlags().getIntValue() != flag){
	    
	    return new UDF_Error(UDF_Error.C_ECMA167, UDF_Error.L_ERROR, "Flags", "",
				 "", String.valueOf(getFlags().getIntValue()), "0");
	}
	else
	    return new UDF_Error();
    }
    
    /**
       Identifier フィールドの値を検証します。
       エラーはL_ERROR、原因、記録値、期待値が設定されます。
       
       @param  identifier  比較対照となるidentifier の文字列。
       @return 検証をパスしないと、エラーインスタンスを返します。
               そうでない場合、NOERR を返します。
    */
    public UDF_Error verifyId(String identifier) throws UDF_Exception{
	
	byte[] idbuf = id.getData();
	
	
	if(!UDF_Util.cmpBytesString(idbuf, identifier)){
	    
	    UDF_Error err =
		new UDF_Error(UDF_Error.C_ECMA167, UDF_Error.L_ERROR, "Identifier");
	   
	    err.setRecordedValue(UDF_Util.b2qstr(idbuf));
	    err.setExpectedValue(identifier);
	    err.setGlobalPoint(getGlobalPoint());
	    return err;
	}
	else
	    return new UDF_Error();
    }
    public void extractSuffixAsUDFDomainId() throws UDF_Exception{
	;
    }
    public void extractSuffixAsUDFIdSuffix() throws UDF_Exception{
	;
    }
    public void extractSuffixAsUDFImplIdSuffix() throws UDF_Exception{
	;
    }

//end:
};
