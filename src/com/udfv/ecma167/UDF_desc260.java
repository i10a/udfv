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
Terminal&nbsp;Entry&nbsp;を表現するクラス。<br/>


<dl>

<dt>フィールド一覧</dt>
<dd>
<table border="1">
<tr><td>メンバ</td><td>型</td><td>サイズ</td></tr>
<tr><td><b>DescTag</b></td><td><b>UDF_tag</b></td><td><i>descTag.getSize()</i></td></tr>
<tr><td><b>ICBTag</b></td><td><b>UDF_icbtag</b></td><td><i>iCBTag.getSize()</i></td></tr>
</table>
</dd>
</dl>
*/
public class UDF_desc260 extends UDF_CrcDesc implements UDF_ICBDesc
{

    /**
	このオブジェクトのパッケージ名を除いたクラス名を取得する。

	@return クラス名
    */
    public static String getUDFClassName( ) {
        return "UDF_desc260";
    }

    /**
	コンストラクタ。

	@param elem   親エレメント。
	@param prefix ネームスペース。
	@param name   エレメント名。
    */
    public UDF_desc260(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }
    /**
	このUDFエレメントのサイズを返す。

	@return UDFエレメントのサイズ。
    */
    public int getSize(){
	return 0+descTag.getSize()+iCBTag.getSize();
    }

    /**
	このUDFエレメントのサイズをlong 型で返す。

	@return UDFエレメントのサイズ。
    */
    public long getLongSize(){
	return (long)0+descTag.getSize()+iCBTag.getSize();
    }
    private UDF_tag descTag;
    private UDF_icbtag iCBTag;

    /**
	descTagを取得する。

	@return 取得したdescTag を返す。
    */
    public UDF_tag getDescTag(){return descTag;}
    /**
	iCBTagを取得する。

	@return 取得したiCBTag を返す。
    */
    public UDF_icbtag getICBTag(){return iCBTag;}

    /**
	descTagを設定する。

	@param	v 設定する値。
    */
    public void setDescTag(UDF_tag v){replaceChild(v, descTag); descTag = v;}
    /**
	iCBTagを設定する。

	@param	v 設定する値。
    */
    public void setICBTag(UDF_icbtag v){replaceChild(v, iCBTag); iCBTag = v;}

    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long rsize = 0;
	preReadHook(f);
	descTag = (UDF_tag)createElement("UDF_tag", "", "desc-tag");
	rsize += descTag.readFrom(f);
	iCBTag = (UDF_icbtag)createElement("UDF_icbtag", "", "icb-tag");
	rsize += iCBTag.readFrom(f);
	apply( );
	postReadHook(f);
	return rsize;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long wsize = 0;
	wsize += descTag.writeTo(f);
	wsize += iCBTag.writeTo(f);
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
	    else if(name.equals("desc-tag")){
		descTag = (UDF_tag)createElement("UDF_tag", "", "desc-tag");
		descTag.readFromXML(child);
	    }
	    else if(name.equals("icb-tag")){
		iCBTag = (UDF_icbtag)createElement("UDF_icbtag", "", "icb-tag");
		iCBTag.readFromXML(child);
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
	descTag = (UDF_tag)createElement("UDF_tag", "", "desc-tag");
	descTag.setDefaultValue();
	iCBTag = (UDF_icbtag)createElement("UDF_icbtag", "", "icb-tag");
	iCBTag.setDefaultValue();

	apply( );
	postSetDefaultValueHook();
    }


    /**
	複製する
    */
    public UDF_Element duplicateElement(){
	UDF_desc260 dup = new UDF_desc260(this, null, getName());
	dup.setDefaultValue();
	dup.duplicateHook(this);
	dup.setDescTag((UDF_tag)descTag.duplicateElement());
	dup.setICBTag((UDF_icbtag)iCBTag.duplicateElement());

	apply();

	return dup;
    }

    /**
        子の UDF Element の Node を append しなおす。

    */
    private void apply( ) {

	removeAllChildren();
	appendChild(descTag);
	appendChild(iCBTag);
    }

    public String getInfo(int indent){
	String a = "";
	a += UDF_Util.repeat(' ', indent);
	a += descTag.getInfo(indent + 1);
	a += iCBTag.getInfo(indent + 1);
      return a;
    }
    public void debug(int indent){
	System.err.print(UDF_Util.repeat(' ', indent));

	String info = " gp=" + getGlobalPoint();
	info += " pn=" + getElemPartRefNo();
	info += " sn=" + getPartSubno();
	System.err.println(getName() + ": (" + hashCode() + ")" + info);	descTag.debug(indent + 1);
	iCBTag.debug(indent + 1);
    }
//begin:add your code here
    public int getFixedTagId() { return 260; }
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	final short category = UDF_Error.C_ECMA167;
	UDF_Error ret;
	
	
	el.addError(super.verify());  // Tag Descriptor
	
	ret = verifyId();
	if(ret.isError()){
	    
	    ret.setRefer("4/14.8.1");
	    el.addError(ret);
	}
	
	el.addError(iCBTag.verify("ICB Tag"));  // ICB Tag
	
	// ICB Tag のファイルタイプは11でなければならない
	ret = iCBTag.verifyFileType(11);
	if(ret.isError()){
	    
	    ret.setMessage("The File Type field of the icbtag(4/14.6) for this descriptor shall contain 11.");
	    ret.setRefer("4/14.8.2");
	    el.addError(ret);
	}
	
	el.setGlobalPoint(getGlobalPoint());
	el.setRName("Terminal Entry");
	return el;
    }
    public int getICBFileType(){
	return getICBTag().getFileType().getIntValue();
    }
    public int getICBFlags(){
	return getICBTag().getFlags().getIntValue();
    }
//end:
};
