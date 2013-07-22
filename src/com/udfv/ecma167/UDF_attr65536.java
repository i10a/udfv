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
Application&nbsp;Use&nbsp;Extended&nbsp;Attribute&nbsp;を表現するクラス。<br/>


<dl>

<dt>フィールド一覧</dt>
<dd>
<table border="1">
<tr><td>メンバ</td><td>型</td><td>サイズ</td></tr>
<tr><td><b>AttrType</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>AttrSubtype</b></td><td><b>UDF_uint8</b></td><td><i>1</i></td></tr>
<tr><td><b>Reserved</b></td><td><b>UDF_bytes</b></td><td><i>3</i></td></tr>
<tr><td><b>AttrLen</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>ApplicationUseLen</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>ApplicationId</b></td><td><b>UDF_regid</b></td><td><i>applicationId.getSize()</i></td></tr>
<tr><td><b>ApplicationUse</b></td><td><b>UDF_bytes</b></td><td><i>getApplicationUseLen().getIntValue()</i></td></tr>
</table>
</dd>
</dl>
*/
public class UDF_attr65536 extends UDF_attr 
{

    /**
	このオブジェクトのパッケージ名を除いたクラス名を取得する。

	@return クラス名
    */
    public static String getUDFClassName( ) {
        return "UDF_attr65536";
    }

    /**
	コンストラクタ。

	@param elem   親エレメント。
	@param prefix ネームスペース。
	@param name   エレメント名。
    */
    public UDF_attr65536(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }
    /**
	このUDFエレメントのサイズを返す。

	@return UDFエレメントのサイズ。
    */
    public int getSize(){
	return 0+4+1+reserved.getSize()+4+4+applicationId.getSize()+applicationUse.getSize();
    }

    /**
	このUDFエレメントのサイズをlong 型で返す。

	@return UDFエレメントのサイズ。
    */
    public long getLongSize(){
	return (long)0+4+1+reserved.getSize()+4+4+applicationId.getSize()+applicationUse.getSize();
    }
    private UDF_uint32 attrType;
    private UDF_uint8 attrSubtype;
    private UDF_bytes reserved;
    private UDF_uint32 attrLen;
    private UDF_uint32 applicationUseLen;
    private UDF_regid applicationId;
    private UDF_bytes applicationUse;

    /**
	attrTypeを取得する。

	@return 取得したattrType を返す。
    */
    public UDF_uint32 getAttrType(){return attrType;}
    /**
	attrSubtypeを取得する。

	@return 取得したattrSubtype を返す。
    */
    public UDF_uint8 getAttrSubtype(){return attrSubtype;}
    /**
	reservedを取得する。

	@return 取得したreserved を返す。
    */
    public UDF_bytes getReserved(){return reserved;}
    /**
	attrLenを取得する。

	@return 取得したattrLen を返す。
    */
    public UDF_uint32 getAttrLen(){return attrLen;}
    /**
	applicationUseLenを取得する。

	@return 取得したapplicationUseLen を返す。
    */
    public UDF_uint32 getApplicationUseLen(){return applicationUseLen;}
    /**
	applicationIdを取得する。

	@return 取得したapplicationId を返す。
    */
    public UDF_regid getApplicationId(){return applicationId;}
    /**
	applicationUseを取得する。

	@return 取得したapplicationUse を返す。
    */
    public UDF_bytes getApplicationUse(){return applicationUse;}

    /**
	attrTypeを設定する。

	@param	v 設定する値。
    */
    public void setAttrType(UDF_uint32 v){replaceChild(v, attrType); attrType = v;}
    /**
	attrSubtypeを設定する。

	@param	v 設定する値。
    */
    public void setAttrSubtype(UDF_uint8 v){replaceChild(v, attrSubtype); attrSubtype = v;}
    /**
	reservedを設定する。

	@param	v 設定する値。
    */
    public void setReserved(UDF_bytes v){replaceChild(v, reserved); reserved = v;}
    /**
	attrLenを設定する。

	@param	v 設定する値。
    */
    public void setAttrLen(UDF_uint32 v){replaceChild(v, attrLen); attrLen = v;}
    /**
	applicationUseLenを設定する。

	@param	v 設定する値。
    */
    public void setApplicationUseLen(UDF_uint32 v){replaceChild(v, applicationUseLen); applicationUseLen = v;}
    /**
	applicationIdを設定する。

	@param	v 設定する値。
    */
    public void setApplicationId(UDF_regid v){replaceChild(v, applicationId); applicationId = v;}
    /**
	applicationUseを設定する。

	@param	v 設定する値。
    */
    public void setApplicationUse(UDF_bytes v){replaceChild(v, applicationUse); applicationUse = v;}

    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long rsize = 0;
	preReadHook(f);
	attrType = (UDF_uint32)createElement("UDF_uint32", "", "attr-type");
	rsize += attrType.readFrom(f);
	attrSubtype = (UDF_uint8)createElement("UDF_uint8", "", "attr-subtype");
	rsize += attrSubtype.readFrom(f);
	reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
	reserved.setSize(3);
	rsize += reserved.readFrom(f);
	attrLen = (UDF_uint32)createElement("UDF_uint32", "", "attr-len");
	rsize += attrLen.readFrom(f);
	applicationUseLen = (UDF_uint32)createElement("UDF_uint32", "", "application-use-len");
	rsize += applicationUseLen.readFrom(f);
	applicationId = (UDF_regid)createElement("UDF_regid", "", "application-id");
	rsize += applicationId.readFrom(f);
	applicationUse = (UDF_bytes)createElement("UDF_bytes", "", "application-use");
	applicationUse.setSize(getApplicationUseLen().getIntValue());
	rsize += applicationUse.readFrom(f);
	apply( );
	postReadHook(f);
	return rsize;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long wsize = 0;
	wsize += attrType.writeTo(f);
	wsize += attrSubtype.writeTo(f);
	wsize += reserved.writeTo(f);
	wsize += attrLen.writeTo(f);
	wsize += applicationUseLen.writeTo(f);
	wsize += applicationId.writeTo(f);
	wsize += applicationUse.writeTo(f);
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
	    else if(name.equals("attr-type")){
		attrType = (UDF_uint32)createElement("UDF_uint32", "", "attr-type");
		attrType.readFromXML(child);
	    }
	    else if(name.equals("attr-subtype")){
		attrSubtype = (UDF_uint8)createElement("UDF_uint8", "", "attr-subtype");
		attrSubtype.readFromXML(child);
	    }
	    else if(name.equals("reserved")){
		reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
		reserved.setSize(3);
		reserved.readFromXML(child);
	    }
	    else if(name.equals("attr-len")){
		attrLen = (UDF_uint32)createElement("UDF_uint32", "", "attr-len");
		attrLen.readFromXML(child);
	    }
	    else if(name.equals("application-use-len")){
		applicationUseLen = (UDF_uint32)createElement("UDF_uint32", "", "application-use-len");
		applicationUseLen.readFromXML(child);
	    }
	    else if(name.equals("application-id")){
		applicationId = (UDF_regid)createElement("UDF_regid", "", "application-id");
		applicationId.readFromXML(child);
	    }
	    else if(name.equals("application-use")){
		applicationUse = (UDF_bytes)createElement("UDF_bytes", "", "application-use");
		applicationUse.setSize(getApplicationUseLen().getIntValue());
		applicationUse.readFromXML(child);
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
	attrType = (UDF_uint32)createElement("UDF_uint32", "", "attr-type");
	attrType.setDefaultValue();
	attrType.setValue(65536);
	attrSubtype = (UDF_uint8)createElement("UDF_uint8", "", "attr-subtype");
	attrSubtype.setDefaultValue();
	attrSubtype.setValue(1);
	reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
	reserved.setSize(3);
	reserved.setDefaultValue();
	attrLen = (UDF_uint32)createElement("UDF_uint32", "", "attr-len");
	attrLen.setDefaultValue();
	applicationUseLen = (UDF_uint32)createElement("UDF_uint32", "", "application-use-len");
	applicationUseLen.setDefaultValue();
	applicationId = (UDF_regid)createElement("UDF_regid", "", "application-id");
	applicationId.setDefaultValue();
	applicationUse = (UDF_bytes)createElement("UDF_bytes", "", "application-use");
	applicationUse.setSize(getApplicationUseLen().getIntValue());
	applicationUse.setDefaultValue();

	apply( );
	postSetDefaultValueHook();
    }


    /**
	複製する
    */
    public UDF_Element duplicateElement(){
	UDF_attr65536 dup = new UDF_attr65536(this, null, getName());
	dup.setDefaultValue();
	dup.duplicateHook(this);
	dup.setAttrType((UDF_uint32)attrType.duplicateElement());
	dup.setAttrSubtype((UDF_uint8)attrSubtype.duplicateElement());
	dup.setReserved((UDF_bytes)reserved.duplicateElement());
	dup.setAttrLen((UDF_uint32)attrLen.duplicateElement());
	dup.setApplicationUseLen((UDF_uint32)applicationUseLen.duplicateElement());
	dup.setApplicationId((UDF_regid)applicationId.duplicateElement());
	dup.setApplicationUse((UDF_bytes)applicationUse.duplicateElement());

	apply();

	return dup;
    }

    /**
        子の UDF Element の Node を append しなおす。

    */
    private void apply( ) {

	removeAllChildren();
	appendChild(attrType);
	appendChild(attrSubtype);
	appendChild(reserved);
	appendChild(attrLen);
	appendChild(applicationUseLen);
	appendChild(applicationId);
	appendChild(applicationUse);
    }

    public void debug(int indent){
	System.err.print(UDF_Util.repeat(' ', indent));
	System.err.println(getName() + ": (" + hashCode() + ")");	attrType.debug(indent + 1);
	attrSubtype.debug(indent + 1);
	reserved.debug(indent + 1);
	attrLen.debug(indent + 1);
	applicationUseLen.debug(indent + 1);
	applicationId.debug(indent + 1);
	applicationUse.debug(indent + 1);
    }
//begin:add your code here
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	final short category = UDF_Error.C_ECMA167;
	
	
	el.addError(super.verify(65536, 9));
	el.addError(applicationId.verify("Application Identifier"));
		    
	el.setRName("Application Use Extended Attribute");
	return el;
    }
    
//end:
};
