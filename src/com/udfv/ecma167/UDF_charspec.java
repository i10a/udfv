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
ECMA-167&nbsp;1/7.2.1&nbsp;Character&nbsp;set&nbsp;specification&nbsp;を表現するクラス。<br/>


<dl>

<dt>フィールド一覧</dt>
<dd>
<table border="1">
<tr><td>メンバ</td><td>型</td><td>サイズ</td></tr>
<tr><td><b>CharSetType</b></td><td><b>UDF_uint8</b></td><td><i>1</i></td></tr>
<tr><td><b>CharSetInfo</b></td><td><b>UDF_bytes</b></td><td><i>63</i></td></tr>
</table>
</dd>
</dl>
*/
public class UDF_charspec extends UDF_Element 
{

    /**
	このオブジェクトのパッケージ名を除いたクラス名を取得する。

	@return クラス名
    */
    public static String getUDFClassName( ) {
        return "UDF_charspec";
    }

    /**
	コンストラクタ。

	@param elem   親エレメント。
	@param prefix ネームスペース。
	@param name   エレメント名。
    */
    public UDF_charspec(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }
    /**
	このUDFエレメントのサイズを返す。

	@return UDFエレメントのサイズ。
    */
    public int getSize(){
	return 0+1+charSetInfo.getSize();
    }

    /**
	このUDFエレメントのサイズをlong 型で返す。

	@return UDFエレメントのサイズ。
    */
    public long getLongSize(){
	return (long)0+1+charSetInfo.getSize();
    }
    private UDF_uint8 charSetType;
    private UDF_bytes charSetInfo;

    /**
	charSetTypeを取得する。

	@return 取得したcharSetType を返す。
    */
    public UDF_uint8 getCharSetType(){return charSetType;}
    /**
	charSetInfoを取得する。

	@return 取得したcharSetInfo を返す。
    */
    public UDF_bytes getCharSetInfo(){return charSetInfo;}

    /**
	charSetTypeを設定する。

	@param	v 設定する値。
    */
    public void setCharSetType(UDF_uint8 v){replaceChild(v, charSetType); charSetType = v;}
    /**
	charSetInfoを設定する。

	@param	v 設定する値。
    */
    public void setCharSetInfo(UDF_bytes v){replaceChild(v, charSetInfo); charSetInfo = v;}

    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long rsize = 0;
	preReadHook(f);
	charSetType = (UDF_uint8)createElement("UDF_uint8", "", "char-set-type");
	rsize += charSetType.readFrom(f);
	charSetInfo = (UDF_bytes)createElement("UDF_bytes", "", "char-set-info");
	charSetInfo.setSize(63);
	charSetInfo.setEncoding(new UDF_EncodingCS0_UDF());
	rsize += charSetInfo.readFrom(f);
	apply( );
	postReadHook(f);
	return rsize;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long wsize = 0;
	wsize += charSetType.writeTo(f);
	wsize += charSetInfo.writeTo(f);
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
	    else if(name.equals("char-set-type")){
		charSetType = (UDF_uint8)createElement("UDF_uint8", "", "char-set-type");
		charSetType.readFromXML(child);
	    }
	    else if(name.equals("char-set-info")){
		charSetInfo = (UDF_bytes)createElement("UDF_bytes", "", "char-set-info");
		charSetInfo.setSize(63);
		charSetInfo.setEncoding(new UDF_EncodingCS0_UDF());
		charSetInfo.readFromXML(child);
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
	charSetType = (UDF_uint8)createElement("UDF_uint8", "", "char-set-type");
	charSetType.setDefaultValue();
	charSetInfo = (UDF_bytes)createElement("UDF_bytes", "", "char-set-info");
	charSetInfo.setSize(63);
	charSetInfo.setEncoding(new UDF_EncodingCS0_UDF());
	charSetInfo.setDefaultValue();

	apply( );
	postSetDefaultValueHook();
    }


    /**
	複製する
    */
    public UDF_Element duplicateElement(){
	UDF_charspec dup = new UDF_charspec(this, null, getName());
	dup.setDefaultValue();
	dup.duplicateHook(this);
	dup.setCharSetType((UDF_uint8)charSetType.duplicateElement());
	dup.setCharSetInfo((UDF_bytes)charSetInfo.duplicateElement());

	apply();

	return dup;
    }

    /**
        子の UDF Element の Node を append しなおす。

    */
    private void apply( ) {

	removeAllChildren();
	appendChild(charSetType);
	appendChild(charSetInfo);
    }

    public String getInfo(int indent){
	String a = "";
	a += UDF_Util.repeat(' ', indent);
	a += charSetType.getInfo(indent + 1);
	a += charSetInfo.getInfo(indent + 1);
      return a;
    }
    public void debug(int indent){
	System.err.print(UDF_Util.repeat(' ', indent));

	String info = " gp=" + getGlobalPoint();
	info += " pn=" + getElemPartRefNo();
	info += " sn=" + getPartSubno();
	System.err.println(getName() + ": (" + hashCode() + ")" + info);	charSetType.debug(indent + 1);
	charSetInfo.debug(indent + 1);
    }
//begin:add your code here

    static public UDF_charspec createInstance(UDF_Element mother, String prefix, String name, int type, byte[] info){
	try{
	    UDF_charspec charspec = (UDF_charspec)UDF_ElementBase.genElement("UDF_charspec", mother, prefix, name);
	    charspec.setDefaultValue();
	    charspec.getCharSetType().setValue(type);
	    charspec.getCharSetInfo().setData(info);
	    charspec.getCharSetInfo().setSize(63);

	    return charspec;
	}
	catch(ClassNotFoundException e){
	    return null;
	}
    }

    public boolean isOSTACompressedUnicode(){
	if(charSetType.getIntValue() != 0)
	    return false;
	if(!charSetInfo.cmpString("OSTA Compressed Unicode"))
	    return false;
	return true;
    }
    public UDF_Encoding genEncoding() throws UDF_Exception{
	return UDF_Encoding.genEncoding(UDF_Util.bz2str(charSetInfo.getData()));
    }
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	int cstype = charSetType.getIntValue();
	if(8 < cstype){
	    
	    el.addError(new UDF_Error
			(UDF_Error.C_ECMA167, UDF_Error.L_ERROR, "Character Set Type",
			 "This field shall specify the allowd characters by identifying a set of characters shown in figure 1/6.\n" +
			 "9-255: Rserved for future standardisation.", "1/7.2.1.1", String.valueOf(cstype), ""));
	}
	
	return el;
    }
    
    /**
       Character Set Type の値を検証します。
       エラーにはC_ECMA167 カテゴリ、L_ERROR レベル、原因、記録値、期待値が含まれます。
       
       @param type 検証する値。エラーの期待値にはこの値が設定されます。
       @return エラーインスタンス。
    */
    public UDF_Error verifyCharSetType(int type){
	
	if(type != charSetType.getIntValue()){
	    
	    return new UDF_Error(UDF_Error.C_ECMA167, UDF_Error.L_ERROR, "Character Set Type",
				"", "", String.valueOf(charSetType.getIntValue()), String.valueOf(type));
	}
	else
	    return new UDF_Error();
    }
    
//end:
};
