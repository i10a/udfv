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
Partition&nbsp;Header&nbsp;Descriptor&nbsp;を表現するクラス。<br/>


<dl>

<dt>フィールド一覧</dt>
<dd>
<table border="1">
<tr><td>メンバ</td><td>型</td><td>サイズ</td></tr>
<tr><td><b>UnallocatedSpaceTable</b></td><td><b>UDF_short_ad</b></td><td><i>unallocatedSpaceTable.getSize()</i></td></tr>
<tr><td><b>UnallocatedSpaceBitmap</b></td><td><b>UDF_short_ad</b></td><td><i>unallocatedSpaceBitmap.getSize()</i></td></tr>
<tr><td><b>PartIntegrityTable</b></td><td><b>UDF_short_ad</b></td><td><i>partIntegrityTable.getSize()</i></td></tr>
<tr><td><b>FreedSpaceTable</b></td><td><b>UDF_short_ad</b></td><td><i>freedSpaceTable.getSize()</i></td></tr>
<tr><td><b>FreedSpaceBitmap</b></td><td><b>UDF_short_ad</b></td><td><i>freedSpaceBitmap.getSize()</i></td></tr>
<tr><td><b>Reserved</b></td><td><b>UDF_bytes</b></td><td><i>88</i></td></tr>
</table>
</dd>
</dl>
*/
public class UDF_PartHeaderDesc extends UDF_Element 
{

    /**
	このオブジェクトのパッケージ名を除いたクラス名を取得する。

	@return クラス名
    */
    public static String getUDFClassName( ) {
        return "UDF_PartHeaderDesc";
    }

    /**
	コンストラクタ。

	@param elem   親エレメント。
	@param prefix ネームスペース。
	@param name   エレメント名。
    */
    public UDF_PartHeaderDesc(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }
    /**
	このUDFエレメントのサイズを返す。

	@return UDFエレメントのサイズ。
    */
    public int getSize(){
	return 0+unallocatedSpaceTable.getSize()+unallocatedSpaceBitmap.getSize()+partIntegrityTable.getSize()+freedSpaceTable.getSize()+freedSpaceBitmap.getSize()+reserved.getSize();
    }

    /**
	このUDFエレメントのサイズをlong 型で返す。

	@return UDFエレメントのサイズ。
    */
    public long getLongSize(){
	return (long)0+unallocatedSpaceTable.getSize()+unallocatedSpaceBitmap.getSize()+partIntegrityTable.getSize()+freedSpaceTable.getSize()+freedSpaceBitmap.getSize()+reserved.getSize();
    }
    private UDF_short_ad unallocatedSpaceTable;
    private UDF_short_ad unallocatedSpaceBitmap;
    private UDF_short_ad partIntegrityTable;
    private UDF_short_ad freedSpaceTable;
    private UDF_short_ad freedSpaceBitmap;
    private UDF_bytes reserved;

    /**
	unallocatedSpaceTableを取得する。

	@return 取得したunallocatedSpaceTable を返す。
    */
    public UDF_short_ad getUnallocatedSpaceTable(){return unallocatedSpaceTable;}
    /**
	unallocatedSpaceBitmapを取得する。

	@return 取得したunallocatedSpaceBitmap を返す。
    */
    public UDF_short_ad getUnallocatedSpaceBitmap(){return unallocatedSpaceBitmap;}
    /**
	partIntegrityTableを取得する。

	@return 取得したpartIntegrityTable を返す。
    */
    public UDF_short_ad getPartIntegrityTable(){return partIntegrityTable;}
    /**
	freedSpaceTableを取得する。

	@return 取得したfreedSpaceTable を返す。
    */
    public UDF_short_ad getFreedSpaceTable(){return freedSpaceTable;}
    /**
	freedSpaceBitmapを取得する。

	@return 取得したfreedSpaceBitmap を返す。
    */
    public UDF_short_ad getFreedSpaceBitmap(){return freedSpaceBitmap;}
    /**
	reservedを取得する。

	@return 取得したreserved を返す。
    */
    public UDF_bytes getReserved(){return reserved;}

    /**
	unallocatedSpaceTableを設定する。

	@param	v 設定する値。
    */
    public void setUnallocatedSpaceTable(UDF_short_ad v){replaceChild(v, unallocatedSpaceTable); unallocatedSpaceTable = v;}
    /**
	unallocatedSpaceBitmapを設定する。

	@param	v 設定する値。
    */
    public void setUnallocatedSpaceBitmap(UDF_short_ad v){replaceChild(v, unallocatedSpaceBitmap); unallocatedSpaceBitmap = v;}
    /**
	partIntegrityTableを設定する。

	@param	v 設定する値。
    */
    public void setPartIntegrityTable(UDF_short_ad v){replaceChild(v, partIntegrityTable); partIntegrityTable = v;}
    /**
	freedSpaceTableを設定する。

	@param	v 設定する値。
    */
    public void setFreedSpaceTable(UDF_short_ad v){replaceChild(v, freedSpaceTable); freedSpaceTable = v;}
    /**
	freedSpaceBitmapを設定する。

	@param	v 設定する値。
    */
    public void setFreedSpaceBitmap(UDF_short_ad v){replaceChild(v, freedSpaceBitmap); freedSpaceBitmap = v;}
    /**
	reservedを設定する。

	@param	v 設定する値。
    */
    public void setReserved(UDF_bytes v){replaceChild(v, reserved); reserved = v;}

    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long rsize = 0;
	preReadHook(f);
	unallocatedSpaceTable = (UDF_short_ad)createElement("UDF_short_ad", "", "unallocated-space-table");
	rsize += unallocatedSpaceTable.readFrom(f);
	unallocatedSpaceBitmap = (UDF_short_ad)createElement("UDF_short_ad", "", "unallocated-space-bitmap");
	rsize += unallocatedSpaceBitmap.readFrom(f);
	partIntegrityTable = (UDF_short_ad)createElement("UDF_short_ad", "", "part-integrity-table");
	rsize += partIntegrityTable.readFrom(f);
	freedSpaceTable = (UDF_short_ad)createElement("UDF_short_ad", "", "freed-space-table");
	rsize += freedSpaceTable.readFrom(f);
	freedSpaceBitmap = (UDF_short_ad)createElement("UDF_short_ad", "", "freed-space-bitmap");
	rsize += freedSpaceBitmap.readFrom(f);
	reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
	reserved.setSize(88);
	rsize += reserved.readFrom(f);
	apply( );
	postReadHook(f);
	return rsize;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long wsize = 0;
	wsize += unallocatedSpaceTable.writeTo(f);
	wsize += unallocatedSpaceBitmap.writeTo(f);
	wsize += partIntegrityTable.writeTo(f);
	wsize += freedSpaceTable.writeTo(f);
	wsize += freedSpaceBitmap.writeTo(f);
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
	    else if(name.equals("unallocated-space-table")){
		unallocatedSpaceTable = (UDF_short_ad)createElement("UDF_short_ad", "", "unallocated-space-table");
		unallocatedSpaceTable.readFromXML(child);
	    }
	    else if(name.equals("unallocated-space-bitmap")){
		unallocatedSpaceBitmap = (UDF_short_ad)createElement("UDF_short_ad", "", "unallocated-space-bitmap");
		unallocatedSpaceBitmap.readFromXML(child);
	    }
	    else if(name.equals("part-integrity-table")){
		partIntegrityTable = (UDF_short_ad)createElement("UDF_short_ad", "", "part-integrity-table");
		partIntegrityTable.readFromXML(child);
	    }
	    else if(name.equals("freed-space-table")){
		freedSpaceTable = (UDF_short_ad)createElement("UDF_short_ad", "", "freed-space-table");
		freedSpaceTable.readFromXML(child);
	    }
	    else if(name.equals("freed-space-bitmap")){
		freedSpaceBitmap = (UDF_short_ad)createElement("UDF_short_ad", "", "freed-space-bitmap");
		freedSpaceBitmap.readFromXML(child);
	    }
	    else if(name.equals("reserved")){
		reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
		reserved.setSize(88);
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
	unallocatedSpaceTable = (UDF_short_ad)createElement("UDF_short_ad", "", "unallocated-space-table");
	unallocatedSpaceTable.setDefaultValue();
	unallocatedSpaceBitmap = (UDF_short_ad)createElement("UDF_short_ad", "", "unallocated-space-bitmap");
	unallocatedSpaceBitmap.setDefaultValue();
	partIntegrityTable = (UDF_short_ad)createElement("UDF_short_ad", "", "part-integrity-table");
	partIntegrityTable.setDefaultValue();
	freedSpaceTable = (UDF_short_ad)createElement("UDF_short_ad", "", "freed-space-table");
	freedSpaceTable.setDefaultValue();
	freedSpaceBitmap = (UDF_short_ad)createElement("UDF_short_ad", "", "freed-space-bitmap");
	freedSpaceBitmap.setDefaultValue();
	reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
	reserved.setSize(88);
	reserved.setDefaultValue();

	apply( );
	postSetDefaultValueHook();
    }


    /**
	複製する
    */
    public UDF_Element duplicateElement(){
	UDF_PartHeaderDesc dup = new UDF_PartHeaderDesc(this, null, getName());
	dup.setDefaultValue();
	dup.duplicateHook(this);
	dup.setUnallocatedSpaceTable((UDF_short_ad)unallocatedSpaceTable.duplicateElement());
	dup.setUnallocatedSpaceBitmap((UDF_short_ad)unallocatedSpaceBitmap.duplicateElement());
	dup.setPartIntegrityTable((UDF_short_ad)partIntegrityTable.duplicateElement());
	dup.setFreedSpaceTable((UDF_short_ad)freedSpaceTable.duplicateElement());
	dup.setFreedSpaceBitmap((UDF_short_ad)freedSpaceBitmap.duplicateElement());
	dup.setReserved((UDF_bytes)reserved.duplicateElement());

	apply();

	return dup;
    }

    /**
        子の UDF Element の Node を append しなおす。

    */
    private void apply( ) {

	removeAllChildren();
	appendChild(unallocatedSpaceTable);
	appendChild(unallocatedSpaceBitmap);
	appendChild(partIntegrityTable);
	appendChild(freedSpaceTable);
	appendChild(freedSpaceBitmap);
	appendChild(reserved);
    }

    public String getInfo(int indent){
	String a = "";
	a += UDF_Util.repeat(' ', indent);
	a += unallocatedSpaceTable.getInfo(indent + 1);
	a += unallocatedSpaceBitmap.getInfo(indent + 1);
	a += partIntegrityTable.getInfo(indent + 1);
	a += freedSpaceTable.getInfo(indent + 1);
	a += freedSpaceBitmap.getInfo(indent + 1);
	a += reserved.getInfo(indent + 1);
      return a;
    }
    public void debug(int indent){
	System.err.print(UDF_Util.repeat(' ', indent));

	String info = " gp=" + getGlobalPoint();
	info += " pn=" + getElemPartRefNo();
	info += " sn=" + getPartSubno();
	System.err.println(getName() + ": (" + hashCode() + ")" + info);	unallocatedSpaceTable.debug(indent + 1);
	unallocatedSpaceBitmap.debug(indent + 1);
	partIntegrityTable.debug(indent + 1);
	freedSpaceTable.debug(indent + 1);
	freedSpaceBitmap.debug(indent + 1);
	reserved.debug(indent + 1);
    }
//begin:add your code here
    UDF_desc264 my_usb;

    public UDF_desc264 getSpaceBitmap(){
	return my_usb;
    }

    public void recalc(short type, UDF_RandomAccess f){
	super.recalc(type, f);

	if(type == RECALC_TREE || type == RECALC_VDSLIST){
	    if(getUnallocatedSpaceBitmap().getExtentLen().getIntValue() != 0){
		String label = UDF_Util.car(getUnallocatedSpaceBitmap().getExtentLen().getAttribute("ref"), '.');
		my_usb = (UDF_desc264)findById(label).getFirstChild();
	    }
	}
    }

    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	el.addError(unallocatedSpaceTable.verify("Unallocated Space Table"));
	el.addError(unallocatedSpaceBitmap.verify("Unallocated Space Bitmap"));
	el.addError(partIntegrityTable.verify("Partition Integrity Table"));
	el.addError(freedSpaceTable.verify("Freed Space Table"));
	el.addError(freedSpaceBitmap.verify("Freed Space Bitmap"));
	
	
	if(!UDF_Util.isAllZero(reserved.getData())){
	    
	    el.addError(new UDF_Error
			(UDF_Error.C_ECMA167, UDF_Error.L_ERROR, "Reserved",
			 "This field shall be reserved for future standardisation and all bytes shall be set to #00.",
			 "4/14.3.6"));
	}
	
	el.setRName("Partition Header Descriptor");
	return el;
    }
    
//end:
};
