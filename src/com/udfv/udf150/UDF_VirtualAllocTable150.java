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
<tr><td><b>VATEntry</b></td><td><b>UDF_bytes</b></td><td><i>getHintSize() - 36</i></td></tr>
<tr><td><b>EntityId</b></td><td><b>UDF_regid</b></td><td><i>entityId.getSize()</i></td></tr>
<tr><td><b>PreviousVATICBLocation</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
</table>
</dd>
</dl>
*/
public class UDF_VirtualAllocTable150 extends UDF_Element implements UDF_VirtualAllocTable
{

    /**
	このオブジェクトのパッケージ名を除いたクラス名を取得する。

	@return クラス名
    */
    public static String getUDFClassName( ) {
        return "UDF_VirtualAllocTable150";
    }

    /**
	コンストラクタ。

	@param elem   親エレメント。
	@param prefix ネームスペース。
	@param name   エレメント名。
    */
    public UDF_VirtualAllocTable150(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }
    /**
	このUDFエレメントのサイズを返す。

	@return UDFエレメントのサイズ。
    */
    public int getSize(){
	return 0+vATEntry.getSize()+entityId.getSize()+4;
    }

    /**
	このUDFエレメントのサイズをlong 型で返す。

	@return UDFエレメントのサイズ。
    */
    public long getLongSize(){
	return (long)0+vATEntry.getSize()+entityId.getSize()+4;
    }
    private UDF_bytes vATEntry;
    private com.udfv.ecma167.UDF_regid entityId;
    private UDF_uint32 previousVATICBLocation;

    /**
	vATEntryを取得する。

	@return 取得したvATEntry を返す。
    */
    public UDF_bytes getVATEntry(){return vATEntry;}
    /**
	entityIdを取得する。

	@return 取得したentityId を返す。
    */
    public com.udfv.ecma167.UDF_regid getEntityId(){return entityId;}
    /**
	previousVATICBLocationを取得する。

	@return 取得したpreviousVATICBLocation を返す。
    */
    public UDF_uint32 getPreviousVATICBLocation(){return previousVATICBLocation;}

    /**
	vATEntryを設定する。

	@param	v 設定する値。
    */
    public void setVATEntry(UDF_bytes v){replaceChild(v, vATEntry); vATEntry = v;}
    /**
	entityIdを設定する。

	@param	v 設定する値。
    */
    public void setEntityId(com.udfv.ecma167.UDF_regid v){replaceChild(v, entityId); entityId = v;}
    /**
	previousVATICBLocationを設定する。

	@param	v 設定する値。
    */
    public void setPreviousVATICBLocation(UDF_uint32 v){replaceChild(v, previousVATICBLocation); previousVATICBLocation = v;}

    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long rsize = 0;
	preReadHook(f);
	vATEntry = (UDF_bytes)createElement("UDF_bytes", "", "vat-entry");
	vATEntry.setSize(getHintSize() - 36);
	rsize += vATEntry.readFrom(f);
	entityId = (UDF_regid)createElement("UDF_regid", "", "entity-id");
	rsize += entityId.readFrom(f);
	previousVATICBLocation = (UDF_uint32)createElement("UDF_uint32", "", "previous-vat-icb-location");
	rsize += previousVATICBLocation.readFrom(f);
	apply( );
	postReadHook(f);
	return rsize;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long wsize = 0;
	wsize += vATEntry.writeTo(f);
	wsize += entityId.writeTo(f);
	wsize += previousVATICBLocation.writeTo(f);
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
	    else if(name.equals("vat-entry")){
		vATEntry = (UDF_bytes)createElement("UDF_bytes", "", "vat-entry");
		vATEntry.setSize(getHintSize() - 36);
		vATEntry.readFromXML(child);
	    }
	    else if(name.equals("entity-id")){
		entityId = (UDF_regid)createElement("UDF_regid", "", "entity-id");
		entityId.readFromXML(child);
	    }
	    else if(name.equals("previous-vat-icb-location")){
		previousVATICBLocation = (UDF_uint32)createElement("UDF_uint32", "", "previous-vat-icb-location");
		previousVATICBLocation.readFromXML(child);
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
	vATEntry = (UDF_bytes)createElement("UDF_bytes", "", "vat-entry");
	vATEntry.setSize(getHintSize() - 36);
	vATEntry.setDefaultValue();
	entityId = (UDF_regid)createElement("UDF_regid", "", "entity-id");
	entityId.setDefaultValue();
	previousVATICBLocation = (UDF_uint32)createElement("UDF_uint32", "", "previous-vat-icb-location");
	previousVATICBLocation.setDefaultValue();

	apply( );
	postSetDefaultValueHook();
    }


    /**
	複製する
    */
    public UDF_Element duplicateElement(){
	UDF_VirtualAllocTable150 dup = new UDF_VirtualAllocTable150(this, null, getName());
	dup.setDefaultValue();
	dup.duplicateHook(this);
	dup.setVATEntry((UDF_bytes)vATEntry.duplicateElement());
	dup.setEntityId((com.udfv.ecma167.UDF_regid)entityId.duplicateElement());
	dup.setPreviousVATICBLocation((UDF_uint32)previousVATICBLocation.duplicateElement());

	apply();

	return dup;
    }

    /**
        子の UDF Element の Node を append しなおす。

    */
    private void apply( ) {

	removeAllChildren();
	appendChild(vATEntry);
	appendChild(entityId);
	appendChild(previousVATICBLocation);
    }

    public String getInfo(int indent){
	String a = "";
	a += UDF_Util.repeat(' ', indent);
	a += vATEntry.getInfo(indent + 1);
	a += entityId.getInfo(indent + 1);
	a += previousVATICBLocation.getInfo(indent + 1);
      return a;
    }
    public void debug(int indent){
	System.err.print(UDF_Util.repeat(' ', indent));

	String info = " gp=" + getGlobalPoint();
	info += " pn=" + getElemPartRefNo();
	info += " sn=" + getPartSubno();
	System.err.println(getName() + ": (" + hashCode() + ")" + info);	vATEntry.debug(indent + 1);
	entityId.debug(indent + 1);
	previousVATICBLocation.debug(indent + 1);
    }
//begin:add your code here
    public void postReadHook(UDF_RandomAccess f) throws IOException, UDF_Exception{
	UDF_ElementList v = new UDF_ElementList();
	UDF_RandomAccessBuffer rab = getVATEntry().genRandomAccessBytes();
	
	while(!rab.eof()){
	    UDF_uint32 u = new UDF_uint32(this, null, null);
	    u.readFrom(rab);
	    v.add(u);
	}
	getVATEntry().replaceChildren(v);
    }

//end:
};
