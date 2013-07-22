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
Extended&nbsp;Attribute&nbsp;Header&nbsp;Descriptor&nbsp;を表現するクラス。<br/>


<dl>

<dt>フィールド一覧</dt>
<dd>
<table border="1">
<tr><td>メンバ</td><td>型</td><td>サイズ</td></tr>
<tr><td><b>DescTag</b></td><td><b>UDF_tag</b></td><td><i>descTag.getSize()</i></td></tr>
<tr><td><b>ImplAttrLoc</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>ApplicationAttrLoc</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
</table>
</dd>
</dl>
*/
public class UDF_desc262 extends UDF_CrcDesc 
{

    /**
	このオブジェクトのパッケージ名を除いたクラス名を取得する。

	@return クラス名
    */
    public static String getUDFClassName( ) {
        return "UDF_desc262";
    }

    /**
	コンストラクタ。

	@param elem   親エレメント。
	@param prefix ネームスペース。
	@param name   エレメント名。
    */
    public UDF_desc262(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }
    /**
	このUDFエレメントのサイズを返す。

	@return UDFエレメントのサイズ。
    */
    public int getSize(){
	return 0+descTag.getSize()+4+4;
    }

    /**
	このUDFエレメントのサイズをlong 型で返す。

	@return UDFエレメントのサイズ。
    */
    public long getLongSize(){
	return (long)0+descTag.getSize()+4+4;
    }
    private UDF_tag descTag;
    private UDF_uint32 implAttrLoc;
    private UDF_uint32 applicationAttrLoc;

    /**
	descTagを取得する。

	@return 取得したdescTag を返す。
    */
    public UDF_tag getDescTag(){return descTag;}
    /**
	implAttrLocを取得する。

	@return 取得したimplAttrLoc を返す。
    */
    public UDF_uint32 getImplAttrLoc(){return implAttrLoc;}
    /**
	applicationAttrLocを取得する。

	@return 取得したapplicationAttrLoc を返す。
    */
    public UDF_uint32 getApplicationAttrLoc(){return applicationAttrLoc;}

    /**
	descTagを設定する。

	@param	v 設定する値。
    */
    public void setDescTag(UDF_tag v){replaceChild(v, descTag); descTag = v;}
    /**
	implAttrLocを設定する。

	@param	v 設定する値。
    */
    public void setImplAttrLoc(UDF_uint32 v){replaceChild(v, implAttrLoc); implAttrLoc = v;}
    /**
	applicationAttrLocを設定する。

	@param	v 設定する値。
    */
    public void setApplicationAttrLoc(UDF_uint32 v){replaceChild(v, applicationAttrLoc); applicationAttrLoc = v;}

    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long rsize = 0;
	preReadHook(f);
	descTag = (UDF_tag)createElement("UDF_tag", "", "desc-tag");
	rsize += descTag.readFrom(f);
	implAttrLoc = (UDF_uint32)createElement("UDF_uint32", "", "impl-attr-loc");
	rsize += implAttrLoc.readFrom(f);
	applicationAttrLoc = (UDF_uint32)createElement("UDF_uint32", "", "application-attr-loc");
	rsize += applicationAttrLoc.readFrom(f);
	apply( );
	postReadHook(f);
	return rsize;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long wsize = 0;
	wsize += descTag.writeTo(f);
	wsize += implAttrLoc.writeTo(f);
	wsize += applicationAttrLoc.writeTo(f);
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
	    else if(name.equals("impl-attr-loc")){
		implAttrLoc = (UDF_uint32)createElement("UDF_uint32", "", "impl-attr-loc");
		implAttrLoc.readFromXML(child);
	    }
	    else if(name.equals("application-attr-loc")){
		applicationAttrLoc = (UDF_uint32)createElement("UDF_uint32", "", "application-attr-loc");
		applicationAttrLoc.readFromXML(child);
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
	implAttrLoc = (UDF_uint32)createElement("UDF_uint32", "", "impl-attr-loc");
	implAttrLoc.setDefaultValue();
	applicationAttrLoc = (UDF_uint32)createElement("UDF_uint32", "", "application-attr-loc");
	applicationAttrLoc.setDefaultValue();

	apply( );
	postSetDefaultValueHook();
    }


    /**
	複製する
    */
    public UDF_Element duplicateElement(){
	UDF_desc262 dup = new UDF_desc262(this, null, getName());
	dup.setDefaultValue();
	dup.duplicateHook(this);
	dup.setDescTag((UDF_tag)descTag.duplicateElement());
	dup.setImplAttrLoc((UDF_uint32)implAttrLoc.duplicateElement());
	dup.setApplicationAttrLoc((UDF_uint32)applicationAttrLoc.duplicateElement());

	apply();

	return dup;
    }

    /**
        子の UDF Element の Node を append しなおす。

    */
    private void apply( ) {

	removeAllChildren();
	appendChild(descTag);
	appendChild(implAttrLoc);
	appendChild(applicationAttrLoc);
    }

    public String getInfo(int indent){
	String a = "";
	a += UDF_Util.repeat(' ', indent);
	a += descTag.getInfo(indent + 1);
	a += implAttrLoc.getInfo(indent + 1);
	a += applicationAttrLoc.getInfo(indent + 1);
      return a;
    }
    public void debug(int indent){
	System.err.print(UDF_Util.repeat(' ', indent));

	String info = " gp=" + getGlobalPoint();
	info += " pn=" + getElemPartRefNo();
	info += " sn=" + getPartSubno();
	System.err.println(getName() + ": (" + hashCode() + ")" + info);	descTag.debug(indent + 1);
	implAttrLoc.debug(indent + 1);
	applicationAttrLoc.debug(indent + 1);
    }
//begin:add your code here
    public int getFixedTagId() { return 262; }
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	final short category = UDF_Error.C_ECMA167;
	UDF_Error ret;
	
	
	el.addError(super.verify());  // Descriptor Tag
	
	ret = verifyId();
	if(ret.isError()){
	    
	    ret.setRefer("4/14.10.1");
	    el.addError(ret);
	}
	
	
	el.setGlobalPoint(getGlobalPoint());
	el.setRName("Extended Attribute Header Descriptor");
	return el;
    }

//end:
};
