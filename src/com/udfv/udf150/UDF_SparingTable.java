/*
*/
package com.udfv.udf150;

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
no documents.(AUTOMATICALY GENERATED)

<dl>

<dt>フィールド一覧</dt>
<dd>
<table border="1">
<tr><td>メンバ</td><td>型</td><td>サイズ</td></tr>
<tr><td><b>DescTag</b></td><td><b>UDF_tag</b></td><td><i>descTag.getSize()</i></td></tr>
<tr><td><b>SparingId</b></td><td><b>UDF_regid</b></td><td><i>sparingId.getSize()</i></td></tr>
<tr><td><b>ReallocationTableLen</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td></tr>
<tr><td><b>Reserved</b></td><td><b>UDF_bytes</b></td><td><i>2</i></td></tr>
<tr><td><b>SeqNumber</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>MapEntry</b></td><td><b>UDF_bytes</b></td><td><i>getReallocationTableLen().getIntValue()*8</i></td></tr>
</table>
</dd>
</dl>
*/
public class UDF_SparingTable extends UDF_CrcDesc 
{

    /**
	このオブジェクトのパッケージ名を除いたクラス名を取得する。

	@return クラス名
    */
    public static String getUDFClassName( ) {
        return "UDF_SparingTable";
    }

    /**
	コンストラクタ。

	@param elem   親エレメント。
	@param prefix ネームスペース。
	@param name   エレメント名。
    */
    public UDF_SparingTable(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }
    /**
	このUDFエレメントのサイズを返す。

	@return UDFエレメントのサイズ。
    */
    public int getSize(){
	return 0+descTag.getSize()+sparingId.getSize()+2+reserved.getSize()+4+mapEntry.getSize();
    }

    /**
	このUDFエレメントのサイズをlong 型で返す。

	@return UDFエレメントのサイズ。
    */
    public long getLongSize(){
	return (long)0+descTag.getSize()+sparingId.getSize()+2+reserved.getSize()+4+mapEntry.getSize();
    }
    private com.udfv.ecma167.UDF_tag descTag;
    private com.udfv.ecma167.UDF_regid sparingId;
    private UDF_uint16 reallocationTableLen;
    private UDF_bytes reserved;
    private UDF_uint32 seqNumber;
    private UDF_bytes mapEntry;

    /**
	descTagを取得する。

	@return 取得したdescTag を返す。
    */
    public com.udfv.ecma167.UDF_tag getDescTag(){return descTag;}
    /**
	sparingIdを取得する。

	@return 取得したsparingId を返す。
    */
    public com.udfv.ecma167.UDF_regid getSparingId(){return sparingId;}
    /**
	reallocationTableLenを取得する。

	@return 取得したreallocationTableLen を返す。
    */
    public UDF_uint16 getReallocationTableLen(){return reallocationTableLen;}
    /**
	reservedを取得する。

	@return 取得したreserved を返す。
    */
    public UDF_bytes getReserved(){return reserved;}
    /**
	seqNumberを取得する。

	@return 取得したseqNumber を返す。
    */
    public UDF_uint32 getSeqNumber(){return seqNumber;}
    /**
	mapEntryを取得する。

	@return 取得したmapEntry を返す。
    */
    public UDF_bytes getMapEntry(){return mapEntry;}

    /**
	descTagを設定する。

	@param	v 設定する値。
    */
    public void setDescTag(com.udfv.ecma167.UDF_tag v){replaceChild(v, descTag); descTag = v;}
    /**
	sparingIdを設定する。

	@param	v 設定する値。
    */
    public void setSparingId(com.udfv.ecma167.UDF_regid v){replaceChild(v, sparingId); sparingId = v;}
    /**
	reallocationTableLenを設定する。

	@param	v 設定する値。
    */
    public void setReallocationTableLen(UDF_uint16 v){replaceChild(v, reallocationTableLen); reallocationTableLen = v;}
    /**
	reservedを設定する。

	@param	v 設定する値。
    */
    public void setReserved(UDF_bytes v){replaceChild(v, reserved); reserved = v;}
    /**
	seqNumberを設定する。

	@param	v 設定する値。
    */
    public void setSeqNumber(UDF_uint32 v){replaceChild(v, seqNumber); seqNumber = v;}
    /**
	mapEntryを設定する。

	@param	v 設定する値。
    */
    public void setMapEntry(UDF_bytes v){replaceChild(v, mapEntry); mapEntry = v;}

    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long rsize = 0;
	preReadHook(f);
	descTag = (UDF_tag)createElement("UDF_tag", "", "desc-tag");
	rsize += descTag.readFrom(f);
	sparingId = (UDF_regid)createElement("UDF_regid", "", "sparing-id");
	rsize += sparingId.readFrom(f);
	reallocationTableLen = (UDF_uint16)createElement("UDF_uint16", "", "reallocation-table-len");
	rsize += reallocationTableLen.readFrom(f);
	reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
	reserved.setSize(2);
	rsize += reserved.readFrom(f);
	seqNumber = (UDF_uint32)createElement("UDF_uint32", "", "seq-number");
	rsize += seqNumber.readFrom(f);
	mapEntry = (UDF_bytes)createElement("UDF_bytes", "", "map-entry");
	mapEntry.setSize(getReallocationTableLen().getIntValue()*8);
	rsize += mapEntry.readFrom(f);
	apply( );
	postReadHook(f);
	return rsize;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long wsize = 0;
	wsize += descTag.writeTo(f);
	wsize += sparingId.writeTo(f);
	wsize += reallocationTableLen.writeTo(f);
	wsize += reserved.writeTo(f);
	wsize += seqNumber.writeTo(f);
	wsize += mapEntry.writeTo(f);
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
	    else if(name.equals("sparing-id")){
		sparingId = (UDF_regid)createElement("UDF_regid", "", "sparing-id");
		sparingId.readFromXML(child);
	    }
	    else if(name.equals("reallocation-table-len")){
		reallocationTableLen = (UDF_uint16)createElement("UDF_uint16", "", "reallocation-table-len");
		reallocationTableLen.readFromXML(child);
	    }
	    else if(name.equals("reserved")){
		reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
		reserved.setSize(2);
		reserved.readFromXML(child);
	    }
	    else if(name.equals("seq-number")){
		seqNumber = (UDF_uint32)createElement("UDF_uint32", "", "seq-number");
		seqNumber.readFromXML(child);
	    }
	    else if(name.equals("map-entry")){
		mapEntry = (UDF_bytes)createElement("UDF_bytes", "", "map-entry");
		mapEntry.setSize(getReallocationTableLen().getIntValue()*8);
		mapEntry.readFromXML(child);
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
	sparingId = (UDF_regid)createElement("UDF_regid", "", "sparing-id");
	sparingId.setDefaultValue();
	reallocationTableLen = (UDF_uint16)createElement("UDF_uint16", "", "reallocation-table-len");
	reallocationTableLen.setDefaultValue();
	reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
	reserved.setSize(2);
	reserved.setDefaultValue();
	seqNumber = (UDF_uint32)createElement("UDF_uint32", "", "seq-number");
	seqNumber.setDefaultValue();
	mapEntry = (UDF_bytes)createElement("UDF_bytes", "", "map-entry");
	mapEntry.setSize(getReallocationTableLen().getIntValue()*8);
	mapEntry.setDefaultValue();

	apply( );
	postSetDefaultValueHook();
    }


    /**
	複製する
    */
    public UDF_Element duplicateElement(){
	UDF_SparingTable dup = new UDF_SparingTable(this, null, getName());
	dup.setDefaultValue();
	dup.duplicateHook(this);
	dup.setDescTag((com.udfv.ecma167.UDF_tag)descTag.duplicateElement());
	dup.setSparingId((com.udfv.ecma167.UDF_regid)sparingId.duplicateElement());
	dup.setReallocationTableLen((UDF_uint16)reallocationTableLen.duplicateElement());
	dup.setReserved((UDF_bytes)reserved.duplicateElement());
	dup.setSeqNumber((UDF_uint32)seqNumber.duplicateElement());
	dup.setMapEntry((UDF_bytes)mapEntry.duplicateElement());

	apply();

	return dup;
    }

    /**
        子の UDF Element の Node を append しなおす。

    */
    private void apply( ) {

	removeAllChildren();
	appendChild(descTag);
	appendChild(sparingId);
	appendChild(reallocationTableLen);
	appendChild(reserved);
	appendChild(seqNumber);
	appendChild(mapEntry);
    }

    public String getInfo(int indent){
	String a = "";
	a += UDF_Util.repeat(' ', indent);
	a += descTag.getInfo(indent + 1);
	a += sparingId.getInfo(indent + 1);
	a += reallocationTableLen.getInfo(indent + 1);
	a += reserved.getInfo(indent + 1);
	a += seqNumber.getInfo(indent + 1);
	a += mapEntry.getInfo(indent + 1);
      return a;
    }
    public void debug(int indent){
	System.err.print(UDF_Util.repeat(' ', indent));

	String info = " gp=" + getGlobalPoint();
	info += " pn=" + getElemPartRefNo();
	info += " sn=" + getPartSubno();
	System.err.println(getName() + ": (" + hashCode() + ")" + info);	descTag.debug(indent + 1);
	sparingId.debug(indent + 1);
	reallocationTableLen.debug(indent + 1);
	reserved.debug(indent + 1);
	seqNumber.debug(indent + 1);
	mapEntry.debug(indent + 1);
    }
//begin:add your code here
    public int getFixedTagId() { return 0; }
	//map-entry の要素を uint32で置きかえる
    public void postReadHook(UDF_RandomAccess f) throws IOException, UDF_Exception{
	UDF_ElementList v = new UDF_ElementList();
	UDF_RandomAccessBuffer rab = mapEntry.genRandomAccessBytes();
	while(!rab.eof()){
	    UDF_uint32 u = new UDF_uint32(this, null, null);
	    u.readFrom(rab);
	    v.add(u);
	}
	mapEntry.replaceChildren(v);
    }
    
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	UDF_Error ret;
	
	
	if(env.udf_revision != 0x150)
	    return el;
	el.addError(verifyCRC());
	
	// タグ番号のチェック
	ret = verifyId();
	if(ret.isError()){
	    
	    ret.setCategory(UDF_Error.C_UDF150);
	    ret.setRefer("2.2.11");
	    el.addError(ret);
	}
	
	el.addError(verifyBase(UDF_Error.C_UDF150, "2.2.11"));
	
	el.setRName("Sparing Table");
	el.setGlobalPoint(getGlobalPoint());
	return el;
    }
    
    public UDF_ErrorList verifyBase(short category, String refer) throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	UDF_Error ret;
	final String basemsg = "Shall be recorded in the format shown in 'Sparing Table layout'.";
	
	// Sparing Identifier の検証
	final String sid = "Sparing Identifier";
	el.addError(getSparingId().verify(sid));
	ret = getSparingId().verifyFlags(0);
	if(ret.isError()){
	    
	    ret.setCategory(category);
	    ret.setMessage(basemsg);
	    ret.setRefer(refer);
	    ret.setRName(sid);
	    el.addError(ret);
	}
	
	ret = getSparingId().verifyId("*UDF Sparing Table");
	if(ret.isError()){
	    
	    ret.setCategory(category);
	    ret.setMessage(basemsg);
	    ret.setRefer(refer);
	    ret.setRName(sid);
	    el.addError(ret);
	}
	
	
	UDF_ErrorList mapentryel = new UDF_ErrorList();
	UDF_ElementBase eb[] = getMapEntry().getChildren();
	
	
	// Original Location の0xfffffff1 から0xffffffff は予約
	for(int i = 0; i < eb.length; i += 2){
	    
	    UDF_uint32 originalLoc = (UDF_uint32)eb[i];
	    int ol = originalLoc.getIntValue();
	    
	    
	    if(0xfffffff1 <= ol && ol <= 0xfffffffe){
		
		mapentryel.addError(new UDF_Error
				    (category, UDF_Error.L_ERROR, "Original Location[" + (i / 2) + "]",
				     "Original Locations of #FFFFFFF1 through #FFFFFFFE are reserved.",
				     refer, "#" + Integer.toHexString(ol), ""));
	    }
	}
	
	mapentryel.setRName("Map Entry");
	el.addError(mapentryel);
	
	return el;
    }
    
//end:
};
