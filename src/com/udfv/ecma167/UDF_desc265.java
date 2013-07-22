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
Partition&nbsp;Integrity&nbsp;Entry&nbsp;を表現するクラス。<br/>


<dl>

<dt>フィールド一覧</dt>
<dd>
<table border="1">
<tr><td>メンバ</td><td>型</td><td>サイズ</td></tr>
<tr><td><b>DescTag</b></td><td><b>UDF_tag</b></td><td><i>descTag.getSize()</i></td></tr>
<tr><td><b>ICBTag</b></td><td><b>UDF_icbtag</b></td><td><i>iCBTag.getSize()</i></td></tr>
<tr><td><b>RecordingDateAndTime</b></td><td><b>UDF_timestamp</b></td><td><i>recordingDateAndTime.getSize()</i></td></tr>
<tr><td><b>IntegrityType</b></td><td><b>UDF_uint8</b></td><td><i>1</i></td></tr>
<tr><td><b>Reserved</b></td><td><b>UDF_bytes</b></td><td><i>175</i></td></tr>
<tr><td><b>ImplId</b></td><td><b>UDF_regid</b></td><td><i>implId.getSize()</i></td></tr>
<tr><td><b>ImplUse</b></td><td><b>UDF_bytes</b></td><td><i>256</i></td></tr>
</table>
</dd>
</dl>
*/
public class UDF_desc265 extends UDF_CrcDesc implements UDF_ICBDesc
{

    /**
	このオブジェクトのパッケージ名を除いたクラス名を取得する。

	@return クラス名
    */
    public static String getUDFClassName( ) {
        return "UDF_desc265";
    }

    /**
	コンストラクタ。

	@param elem   親エレメント。
	@param prefix ネームスペース。
	@param name   エレメント名。
    */
    public UDF_desc265(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }
    /**
	このUDFエレメントのサイズを返す。

	@return UDFエレメントのサイズ。
    */
    public int getSize(){
	return 0+descTag.getSize()+iCBTag.getSize()+recordingDateAndTime.getSize()+1+reserved.getSize()+implId.getSize()+implUse.getSize();
    }

    /**
	このUDFエレメントのサイズをlong 型で返す。

	@return UDFエレメントのサイズ。
    */
    public long getLongSize(){
	return (long)0+descTag.getSize()+iCBTag.getSize()+recordingDateAndTime.getSize()+1+reserved.getSize()+implId.getSize()+implUse.getSize();
    }
    private UDF_tag descTag;
    private UDF_icbtag iCBTag;
    private UDF_timestamp recordingDateAndTime;
    private UDF_uint8 integrityType;
    private UDF_bytes reserved;
    private UDF_regid implId;
    private UDF_bytes implUse;

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
	recordingDateAndTimeを取得する。

	@return 取得したrecordingDateAndTime を返す。
    */
    public UDF_timestamp getRecordingDateAndTime(){return recordingDateAndTime;}
    /**
	integrityTypeを取得する。

	@return 取得したintegrityType を返す。
    */
    public UDF_uint8 getIntegrityType(){return integrityType;}
    /**
	reservedを取得する。

	@return 取得したreserved を返す。
    */
    public UDF_bytes getReserved(){return reserved;}
    /**
	implIdを取得する。

	@return 取得したimplId を返す。
    */
    public UDF_regid getImplId(){return implId;}
    /**
	implUseを取得する。

	@return 取得したimplUse を返す。
    */
    public UDF_bytes getImplUse(){return implUse;}

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
    /**
	recordingDateAndTimeを設定する。

	@param	v 設定する値。
    */
    public void setRecordingDateAndTime(UDF_timestamp v){replaceChild(v, recordingDateAndTime); recordingDateAndTime = v;}
    /**
	integrityTypeを設定する。

	@param	v 設定する値。
    */
    public void setIntegrityType(UDF_uint8 v){replaceChild(v, integrityType); integrityType = v;}
    /**
	reservedを設定する。

	@param	v 設定する値。
    */
    public void setReserved(UDF_bytes v){replaceChild(v, reserved); reserved = v;}
    /**
	implIdを設定する。

	@param	v 設定する値。
    */
    public void setImplId(UDF_regid v){replaceChild(v, implId); implId = v;}
    /**
	implUseを設定する。

	@param	v 設定する値。
    */
    public void setImplUse(UDF_bytes v){replaceChild(v, implUse); implUse = v;}

    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long rsize = 0;
	preReadHook(f);
	descTag = (UDF_tag)createElement("UDF_tag", "", "desc-tag");
	rsize += descTag.readFrom(f);
	iCBTag = (UDF_icbtag)createElement("UDF_icbtag", "", "icb-tag");
	rsize += iCBTag.readFrom(f);
	recordingDateAndTime = (UDF_timestamp)createElement("UDF_timestamp", "", "recording-date-and-time");
	rsize += recordingDateAndTime.readFrom(f);
	integrityType = (UDF_uint8)createElement("UDF_uint8", "", "integrity-type");
	rsize += integrityType.readFrom(f);
	reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
	reserved.setSize(175);
	rsize += reserved.readFrom(f);
	implId = (UDF_regid)createElement("UDF_regid", "", "impl-id");
	rsize += implId.readFrom(f);
	implUse = (UDF_bytes)createElement("UDF_bytes", "", "impl-use");
	implUse.setSize(256);
	rsize += implUse.readFrom(f);
	apply( );
	postReadHook(f);
	return rsize;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long wsize = 0;
	wsize += descTag.writeTo(f);
	wsize += iCBTag.writeTo(f);
	wsize += recordingDateAndTime.writeTo(f);
	wsize += integrityType.writeTo(f);
	wsize += reserved.writeTo(f);
	wsize += implId.writeTo(f);
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
	    else if(name.equals("desc-tag")){
		descTag = (UDF_tag)createElement("UDF_tag", "", "desc-tag");
		descTag.readFromXML(child);
	    }
	    else if(name.equals("icb-tag")){
		iCBTag = (UDF_icbtag)createElement("UDF_icbtag", "", "icb-tag");
		iCBTag.readFromXML(child);
	    }
	    else if(name.equals("recording-date-and-time")){
		recordingDateAndTime = (UDF_timestamp)createElement("UDF_timestamp", "", "recording-date-and-time");
		recordingDateAndTime.readFromXML(child);
	    }
	    else if(name.equals("integrity-type")){
		integrityType = (UDF_uint8)createElement("UDF_uint8", "", "integrity-type");
		integrityType.readFromXML(child);
	    }
	    else if(name.equals("reserved")){
		reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
		reserved.setSize(175);
		reserved.readFromXML(child);
	    }
	    else if(name.equals("impl-id")){
		implId = (UDF_regid)createElement("UDF_regid", "", "impl-id");
		implId.readFromXML(child);
	    }
	    else if(name.equals("impl-use")){
		implUse = (UDF_bytes)createElement("UDF_bytes", "", "impl-use");
		implUse.setSize(256);
		implUse.readFromXML(child);
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
	recordingDateAndTime = (UDF_timestamp)createElement("UDF_timestamp", "", "recording-date-and-time");
	recordingDateAndTime.setDefaultValue();
	integrityType = (UDF_uint8)createElement("UDF_uint8", "", "integrity-type");
	integrityType.setDefaultValue();
	reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
	reserved.setSize(175);
	reserved.setDefaultValue();
	implId = (UDF_regid)createElement("UDF_regid", "", "impl-id");
	implId.setDefaultValue();
	implUse = (UDF_bytes)createElement("UDF_bytes", "", "impl-use");
	implUse.setSize(256);
	implUse.setDefaultValue();

	apply( );
	postSetDefaultValueHook();
    }


    /**
	複製する
    */
    public UDF_Element duplicateElement(){
	UDF_desc265 dup = new UDF_desc265(this, null, getName());
	dup.setDefaultValue();
	dup.duplicateHook(this);
	dup.setDescTag((UDF_tag)descTag.duplicateElement());
	dup.setICBTag((UDF_icbtag)iCBTag.duplicateElement());
	dup.setRecordingDateAndTime((UDF_timestamp)recordingDateAndTime.duplicateElement());
	dup.setIntegrityType((UDF_uint8)integrityType.duplicateElement());
	dup.setReserved((UDF_bytes)reserved.duplicateElement());
	dup.setImplId((UDF_regid)implId.duplicateElement());
	dup.setImplUse((UDF_bytes)implUse.duplicateElement());

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
	appendChild(recordingDateAndTime);
	appendChild(integrityType);
	appendChild(reserved);
	appendChild(implId);
	appendChild(implUse);
    }

    public String getInfo(int indent){
	String a = "";
	a += UDF_Util.repeat(' ', indent);
	a += descTag.getInfo(indent + 1);
	a += iCBTag.getInfo(indent + 1);
	a += recordingDateAndTime.getInfo(indent + 1);
	a += integrityType.getInfo(indent + 1);
	a += reserved.getInfo(indent + 1);
	a += implId.getInfo(indent + 1);
	a += implUse.getInfo(indent + 1);
      return a;
    }
    public void debug(int indent){
	System.err.print(UDF_Util.repeat(' ', indent));

	String info = " gp=" + getGlobalPoint();
	info += " pn=" + getElemPartRefNo();
	info += " sn=" + getPartSubno();
	System.err.println(getName() + ": (" + hashCode() + ")" + info);	descTag.debug(indent + 1);
	iCBTag.debug(indent + 1);
	recordingDateAndTime.debug(indent + 1);
	integrityType.debug(indent + 1);
	reserved.debug(indent + 1);
	implId.debug(indent + 1);
	implUse.debug(indent + 1);
    }
//begin:add your code here
    public int getFixedTagId() { return 265; }
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	final short category = UDF_Error.C_ECMA167;
	UDF_Error ret;
	
	
	el.addError(super.verify());  // Descriptor Tag
	
	ret = verifyId();
	if(ret.isError()){
	    
	    ret.setRefer("4/14.13.1");
	    el.addError(ret);
	}
	
	// ICBTag のFileType は2でなければならない
	ret = iCBTag.verifyFileType(2);
	if(ret.isError()){
	    
	    ret.setMessage("The File Type field of the icbtag(4/14.6) for this descriptor shall contain 2.");
	    ret.setRName("ICB Tag");
	    el.addError(ret);
	}
	
	// Recording Date and Time
	el.addError(recordingDateAndTime.verify("Recording Date and Time"));
	
	// Integrity Type の3〜255の値は予約されている
	if(2 < integrityType.getIntValue()){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Integrity Type",
			 "This field shall specify the type of Integrity Entry. The types are shown in figure 4/40.\n" +
			 "3-255: Reserved for future standardisation", "4/14.13.4",
			 String.valueOf(integrityType.getIntValue()), ""));
	}
	
	// Reserved
	if(!UDF_Util.isAllZero(reserved.getData())){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Reserved",
			 "This field shall be reserved for future standardisation and all bytes shall be set to #00.",
			 "4/14.13.5"));
	}
	
	// implId
	el.addError(implId.verify("Implementation Identifier"));
	
	
	el.setGlobalPoint(getGlobalPoint());
	el.setRName("Partition Integrity Entry");
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
