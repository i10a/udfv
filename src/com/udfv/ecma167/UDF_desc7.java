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
Unallocated&nbsp;Space&nbsp;Descriptor&nbsp;を表現するクラス。<br/>


<dl>

<dt>フィールド一覧</dt>
<dd>
<table border="1">
<tr><td>メンバ</td><td>型</td><td>サイズ</td></tr>
<tr><td><b>DescTag</b></td><td><b>UDF_tag</b></td><td><i>descTag.getSize()</i></td></tr>
<tr><td><b>VolDescSeqNumber</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>NumberOfAllocDesc</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>AllocDesc</b></td><td><b>UDF_bytes</b></td><td><i>getNumberOfAllocDesc().getIntValue()*8</i></td></tr>
</table>
</dd>
</dl>
*/
public class UDF_desc7 extends UDF_CrcDesc implements UDF_VolDesc, UDF_VolDescSeqNum
{

    /**
	このオブジェクトのパッケージ名を除いたクラス名を取得する。

	@return クラス名
    */
    public static String getUDFClassName( ) {
        return "UDF_desc7";
    }

    /**
	コンストラクタ。

	@param elem   親エレメント。
	@param prefix ネームスペース。
	@param name   エレメント名。
    */
    public UDF_desc7(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }
    /**
	このUDFエレメントのサイズを返す。

	@return UDFエレメントのサイズ。
    */
    public int getSize(){
	return 0+descTag.getSize()+4+4+allocDesc.getSize();
    }

    /**
	このUDFエレメントのサイズをlong 型で返す。

	@return UDFエレメントのサイズ。
    */
    public long getLongSize(){
	return (long)0+descTag.getSize()+4+4+allocDesc.getSize();
    }
    private UDF_tag descTag;
    private UDF_uint32 volDescSeqNumber;
    private UDF_uint32 numberOfAllocDesc;
    private UDF_bytes allocDesc;

    /**
	descTagを取得する。

	@return 取得したdescTag を返す。
    */
    public UDF_tag getDescTag(){return descTag;}
    /**
	volDescSeqNumberを取得する。

	@return 取得したvolDescSeqNumber を返す。
    */
    public UDF_uint32 getVolDescSeqNumber(){return volDescSeqNumber;}
    /**
	numberOfAllocDescを取得する。

	@return 取得したnumberOfAllocDesc を返す。
    */
    public UDF_uint32 getNumberOfAllocDesc(){return numberOfAllocDesc;}
    /**
	allocDescを取得する。

	@return 取得したallocDesc を返す。
    */
    public UDF_bytes getAllocDesc(){return allocDesc;}

    /**
	descTagを設定する。

	@param	v 設定する値。
    */
    public void setDescTag(UDF_tag v){replaceChild(v, descTag); descTag = v;}
    /**
	volDescSeqNumberを設定する。

	@param	v 設定する値。
    */
    public void setVolDescSeqNumber(UDF_uint32 v){replaceChild(v, volDescSeqNumber); volDescSeqNumber = v;}
    /**
	numberOfAllocDescを設定する。

	@param	v 設定する値。
    */
    public void setNumberOfAllocDesc(UDF_uint32 v){replaceChild(v, numberOfAllocDesc); numberOfAllocDesc = v;}
    /**
	allocDescを設定する。

	@param	v 設定する値。
    */
    public void setAllocDesc(UDF_bytes v){replaceChild(v, allocDesc); allocDesc = v;}

    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long rsize = 0;
	preReadHook(f);
	descTag = (UDF_tag)createElement("UDF_tag", "", "desc-tag");
	rsize += descTag.readFrom(f);
	volDescSeqNumber = (UDF_uint32)createElement("UDF_uint32", "", "vol-desc-seq-number");
	rsize += volDescSeqNumber.readFrom(f);
	numberOfAllocDesc = (UDF_uint32)createElement("UDF_uint32", "", "number-of-alloc-desc");
	rsize += numberOfAllocDesc.readFrom(f);
	allocDesc = (UDF_bytes)createElement("UDF_bytes", "", "alloc-desc");
	allocDesc.setSize(getNumberOfAllocDesc().getIntValue()*8);
	rsize += allocDesc.readFrom(f);
	apply( );
	postReadHook(f);
	return rsize;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long wsize = 0;
	wsize += descTag.writeTo(f);
	wsize += volDescSeqNumber.writeTo(f);
	wsize += numberOfAllocDesc.writeTo(f);
	wsize += allocDesc.writeTo(f);
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
	    else if(name.equals("vol-desc-seq-number")){
		volDescSeqNumber = (UDF_uint32)createElement("UDF_uint32", "", "vol-desc-seq-number");
		volDescSeqNumber.readFromXML(child);
	    }
	    else if(name.equals("number-of-alloc-desc")){
		numberOfAllocDesc = (UDF_uint32)createElement("UDF_uint32", "", "number-of-alloc-desc");
		numberOfAllocDesc.readFromXML(child);
	    }
	    else if(name.equals("alloc-desc")){
		allocDesc = (UDF_bytes)createElement("UDF_bytes", "", "alloc-desc");
		allocDesc.setSize(getNumberOfAllocDesc().getIntValue()*8);
		allocDesc.readFromXML(child);
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
	volDescSeqNumber = (UDF_uint32)createElement("UDF_uint32", "", "vol-desc-seq-number");
	volDescSeqNumber.setDefaultValue();
	numberOfAllocDesc = (UDF_uint32)createElement("UDF_uint32", "", "number-of-alloc-desc");
	numberOfAllocDesc.setDefaultValue();
	allocDesc = (UDF_bytes)createElement("UDF_bytes", "", "alloc-desc");
	allocDesc.setSize(getNumberOfAllocDesc().getIntValue()*8);
	allocDesc.setDefaultValue();

	apply( );
	postSetDefaultValueHook();
    }


    /**
	複製する
    */
    public UDF_Element duplicateElement(){
	UDF_desc7 dup = new UDF_desc7(this, null, getName());
	dup.setDefaultValue();
	dup.duplicateHook(this);
	dup.setDescTag((UDF_tag)descTag.duplicateElement());
	dup.setVolDescSeqNumber((UDF_uint32)volDescSeqNumber.duplicateElement());
	dup.setNumberOfAllocDesc((UDF_uint32)numberOfAllocDesc.duplicateElement());
	dup.setAllocDesc((UDF_bytes)allocDesc.duplicateElement());

	apply();

	return dup;
    }

    /**
        子の UDF Element の Node を append しなおす。

    */
    private void apply( ) {

	removeAllChildren();
	appendChild(descTag);
	appendChild(volDescSeqNumber);
	appendChild(numberOfAllocDesc);
	appendChild(allocDesc);
    }

    public String getInfo(int indent){
	String a = "";
	a += UDF_Util.repeat(' ', indent);
	a += descTag.getInfo(indent + 1);
	a += volDescSeqNumber.getInfo(indent + 1);
	a += numberOfAllocDesc.getInfo(indent + 1);
	a += allocDesc.getInfo(indent + 1);
      return a;
    }
    public void debug(int indent){
	System.err.print(UDF_Util.repeat(' ', indent));

	String info = " gp=" + getGlobalPoint();
	info += " pn=" + getElemPartRefNo();
	info += " sn=" + getPartSubno();
	System.err.println(getName() + ": (" + hashCode() + ")" + info);	descTag.debug(indent + 1);
	volDescSeqNumber.debug(indent + 1);
	numberOfAllocDesc.debug(indent + 1);
	allocDesc.debug(indent + 1);
    }
//begin:add your code here
    
    public int getFixedTagId() { return 7; }

    public void postVolReadHook(UDF_RandomAccess f){
	;
    }

    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	UDF_Error ret;
	
	
	el.addError(super.verify());  // Descriptor Tag
	
	ret = verifyId();
	if(ret.isError()){
	    
	    ret.setRefer("3/10.8.1");
	    el.addError(ret);
	}
	
	el.addError(verifyAllocDesc());
	el.setRName("Unallocated Space Descriptor");
	return el;
    }
    
    private UDF_ErrorList verifyAllocDesc() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	byte[] adbuf  = allocDesc.getData();
	int    adsize = adbuf.length;
	int    offset = 0;
	
	
	if(adsize != numberOfAllocDesc.getIntValue() * 8){
	    
	    el.addError(new UDF_Error
			(UDF_Error.C_ECMA167, UDF_Error.L_ERROR, "Number of Allocation Descriptors(=N_AD)",
			 "This field shall specify the number of allocation descriptors recorded in this descriptor.",
			 "3/10.8.3", String.valueOf(numberOfAllocDesc.getIntValue()), String.valueOf(adsize)));
	}
	
	while(offset < adsize){
	    
	    UDF_RandomAccessBytes rab = new UDF_RandomAccessBytes
		(env, adbuf, allocDesc.getPartMapOffset(), allocDesc.getElemPartRefNo(), allocDesc.getPartSubno());
	    UDF_extent_ad extent = (UDF_extent_ad)createElement("UDF_extent_ad", null, null);
	    long  readsz = 0;
	    
	    
	    try{
		rab.seek(0);
		readsz = extent.readFrom(rab);
		el.addError(extent.verify());
	    }
	    catch(Exception e){
		
		if(readsz < 6)
		    throw new UDF_DataException(extent, "readFrom error.");
	    }
	    
	    offset += readsz;
	}
	
	el.setGlobalPoint(getGlobalPoint());
	el.setRName("Allocation Descriptors");
	return el;
    }
    
//end:
};
