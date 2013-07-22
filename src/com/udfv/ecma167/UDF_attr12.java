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
Device&nbsp;Specification&nbsp;を表現するクラス。<br/>


<dl>

<dt>フィールド一覧</dt>
<dd>
<table border="1">
<tr><td>メンバ</td><td>型</td><td>サイズ</td></tr>
<tr><td><b>AttrType</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>AttrSubtype</b></td><td><b>UDF_uint8</b></td><td><i>1</i></td></tr>
<tr><td><b>Reserved</b></td><td><b>UDF_bytes</b></td><td><i>3</i></td></tr>
<tr><td><b>AttrLen</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>ImplUseLen</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>MajorDeviceIdentification</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>MinorDeviceIdentification</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>ImplUse</b></td><td><b>UDF_bytes</b></td><td><i>getImplUseLen().getIntValue()</i></td></tr>
</table>
</dd>
</dl>
*/
public class UDF_attr12 extends UDF_attr 
{

    /**
	このオブジェクトのパッケージ名を除いたクラス名を取得する。

	@return クラス名
    */
    public static String getUDFClassName( ) {
        return "UDF_attr12";
    }

    /**
	コンストラクタ。

	@param elem   親エレメント。
	@param prefix ネームスペース。
	@param name   エレメント名。
    */
    public UDF_attr12(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }
    /**
	このUDFエレメントのサイズを返す。

	@return UDFエレメントのサイズ。
    */
    public int getSize(){
	return 0+4+1+reserved.getSize()+4+4+4+4+implUse.getSize();
    }

    /**
	このUDFエレメントのサイズをlong 型で返す。

	@return UDFエレメントのサイズ。
    */
    public long getLongSize(){
	return (long)0+4+1+reserved.getSize()+4+4+4+4+implUse.getSize();
    }
    private UDF_uint32 attrType;
    private UDF_uint8 attrSubtype;
    private UDF_bytes reserved;
    private UDF_uint32 attrLen;
    private UDF_uint32 implUseLen;
    private UDF_uint32 majorDeviceIdentification;
    private UDF_uint32 minorDeviceIdentification;
    private UDF_bytes implUse;

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
	implUseLenを取得する。

	@return 取得したimplUseLen を返す。
    */
    public UDF_uint32 getImplUseLen(){return implUseLen;}
    /**
	majorDeviceIdentificationを取得する。

	@return 取得したmajorDeviceIdentification を返す。
    */
    public UDF_uint32 getMajorDeviceIdentification(){return majorDeviceIdentification;}
    /**
	minorDeviceIdentificationを取得する。

	@return 取得したminorDeviceIdentification を返す。
    */
    public UDF_uint32 getMinorDeviceIdentification(){return minorDeviceIdentification;}
    /**
	implUseを取得する。

	@return 取得したimplUse を返す。
    */
    public UDF_bytes getImplUse(){return implUse;}

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
	implUseLenを設定する。

	@param	v 設定する値。
    */
    public void setImplUseLen(UDF_uint32 v){replaceChild(v, implUseLen); implUseLen = v;}
    /**
	majorDeviceIdentificationを設定する。

	@param	v 設定する値。
    */
    public void setMajorDeviceIdentification(UDF_uint32 v){replaceChild(v, majorDeviceIdentification); majorDeviceIdentification = v;}
    /**
	minorDeviceIdentificationを設定する。

	@param	v 設定する値。
    */
    public void setMinorDeviceIdentification(UDF_uint32 v){replaceChild(v, minorDeviceIdentification); minorDeviceIdentification = v;}
    /**
	implUseを設定する。

	@param	v 設定する値。
    */
    public void setImplUse(UDF_bytes v){replaceChild(v, implUse); implUse = v;}

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
	implUseLen = (UDF_uint32)createElement("UDF_uint32", "", "impl-use-len");
	rsize += implUseLen.readFrom(f);
	majorDeviceIdentification = (UDF_uint32)createElement("UDF_uint32", "", "major-device-identification");
	rsize += majorDeviceIdentification.readFrom(f);
	minorDeviceIdentification = (UDF_uint32)createElement("UDF_uint32", "", "minor-device-identification");
	rsize += minorDeviceIdentification.readFrom(f);
	implUse = (UDF_bytes)createElement("UDF_bytes", "", "impl-use");
	implUse.setSize(getImplUseLen().getIntValue());
	rsize += implUse.readFrom(f);
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
	wsize += implUseLen.writeTo(f);
	wsize += majorDeviceIdentification.writeTo(f);
	wsize += minorDeviceIdentification.writeTo(f);
	wsize += implUse.writeTo(f);
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
	    else if(name.equals("impl-use-len")){
		implUseLen = (UDF_uint32)createElement("UDF_uint32", "", "impl-use-len");
		implUseLen.readFromXML(child);
	    }
	    else if(name.equals("major-device-identification")){
		majorDeviceIdentification = (UDF_uint32)createElement("UDF_uint32", "", "major-device-identification");
		majorDeviceIdentification.readFromXML(child);
	    }
	    else if(name.equals("minor-device-identification")){
		minorDeviceIdentification = (UDF_uint32)createElement("UDF_uint32", "", "minor-device-identification");
		minorDeviceIdentification.readFromXML(child);
	    }
	    else if(name.equals("impl-use")){
		implUse = (UDF_bytes)createElement("UDF_bytes", "", "impl-use");
		implUse.setSize(getImplUseLen().getIntValue());
		implUse.readFromXML(child);
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
	attrType.setValue(12);
	attrSubtype = (UDF_uint8)createElement("UDF_uint8", "", "attr-subtype");
	attrSubtype.setDefaultValue();
	attrSubtype.setValue(1);
	reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
	reserved.setSize(3);
	reserved.setDefaultValue();
	attrLen = (UDF_uint32)createElement("UDF_uint32", "", "attr-len");
	attrLen.setDefaultValue();
	implUseLen = (UDF_uint32)createElement("UDF_uint32", "", "impl-use-len");
	implUseLen.setDefaultValue();
	majorDeviceIdentification = (UDF_uint32)createElement("UDF_uint32", "", "major-device-identification");
	majorDeviceIdentification.setDefaultValue();
	minorDeviceIdentification = (UDF_uint32)createElement("UDF_uint32", "", "minor-device-identification");
	minorDeviceIdentification.setDefaultValue();
	implUse = (UDF_bytes)createElement("UDF_bytes", "", "impl-use");
	implUse.setSize(getImplUseLen().getIntValue());
	implUse.setDefaultValue();

	apply( );
	postSetDefaultValueHook();
    }


    /**
	複製する
    */
    public UDF_Element duplicateElement(){
	UDF_attr12 dup = new UDF_attr12(this, null, getName());
	dup.setDefaultValue();
	dup.duplicateHook(this);
	dup.setAttrType((UDF_uint32)attrType.duplicateElement());
	dup.setAttrSubtype((UDF_uint8)attrSubtype.duplicateElement());
	dup.setReserved((UDF_bytes)reserved.duplicateElement());
	dup.setAttrLen((UDF_uint32)attrLen.duplicateElement());
	dup.setImplUseLen((UDF_uint32)implUseLen.duplicateElement());
	dup.setMajorDeviceIdentification((UDF_uint32)majorDeviceIdentification.duplicateElement());
	dup.setMinorDeviceIdentification((UDF_uint32)minorDeviceIdentification.duplicateElement());
	dup.setImplUse((UDF_bytes)implUse.duplicateElement());

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
	appendChild(implUseLen);
	appendChild(majorDeviceIdentification);
	appendChild(minorDeviceIdentification);
	appendChild(implUse);
    }

    public void debug(int indent){
	System.err.print(UDF_Util.repeat(' ', indent));
	System.err.println(getName() + ": (" + hashCode() + ")");	attrType.debug(indent + 1);
	attrSubtype.debug(indent + 1);
	reserved.debug(indent + 1);
	attrLen.debug(indent + 1);
	implUseLen.debug(indent + 1);
	majorDeviceIdentification.debug(indent + 1);
	minorDeviceIdentification.debug(indent + 1);
	implUse.debug(indent + 1);
    }
//begin:add your code here
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	final short category = UDF_Error.C_ECMA167;
	
	
	el.addError(super.verify(12, 7));
	
	if(0 < implUseLen.getIntValue()){
	    
	    final int REGID_SIZE = 32;
	
	    UDF_RandomAccessBuffer rab = new UDF_RandomAccessBuffer(REGID_SIZE);
	    UDF_regid regid = (UDF_regid)createElement("UDF_regid", null, null);
	    byte[] implusebuf = implUse.getData();
	    long readsz = 0;
	    
	    
	    readsz = rab.write(implusebuf, 0, REGID_SIZE);
	    if(readsz < REGID_SIZE){
		
		el.addError(new UDF_Error
			    (category, UDF_Error.L_ERROR, "Implementation Use",
			     "If IU_L is greater than 0, this field shall specify an identification of an implementation, " +
			     "recorded as a regid(1/7.4) in the first 32 bytes of this field.",
			     "4/14.10.7.8"));
	    }
	    else{
		try{
		    rab.seek(0);
		    readsz = regid.readFrom(rab);
		    el.addError(regid.verify("Implementation Use"));
		}
		catch(Exception e){
		    
		    if(readsz < REGID_SIZE)
			throw new UDF_DataException(regid, "readFrom error.");
		}
	    }
	}
	
	el.setRName("Device Specification Extended Attribute");
	return el;
    }
	
//end:
};
