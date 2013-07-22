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
Unallocated&nbsp;Space&nbsp;Entry&nbsp;を表現するクラス。<br/>


<dl>

<dt>フィールド一覧</dt>
<dd>
<table border="1">
<tr><td>メンバ</td><td>型</td><td>サイズ</td></tr>
<tr><td><b>DescTag</b></td><td><b>UDF_tag</b></td><td><i>descTag.getSize()</i></td></tr>
<tr><td><b>ICBTag</b></td><td><b>UDF_icbtag</b></td><td><i>iCBTag.getSize()</i></td></tr>
<tr><td><b>LenOfAllocDesc</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>AllocDesc</b></td><td><b>UDF_bytes</b></td><td><i>getLenOfAllocDesc().getIntValue()</i></td></tr>
</table>
</dd>
</dl>
*/
public class UDF_desc263 extends UDF_CrcDesc implements UDF_ICBDesc
{

    /**
	このオブジェクトのパッケージ名を除いたクラス名を取得する。

	@return クラス名
    */
    public static String getUDFClassName( ) {
        return "UDF_desc263";
    }

    /**
	コンストラクタ。

	@param elem   親エレメント。
	@param prefix ネームスペース。
	@param name   エレメント名。
    */
    public UDF_desc263(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }
    /**
	このUDFエレメントのサイズを返す。

	@return UDFエレメントのサイズ。
    */
    public int getSize(){
	return 0+descTag.getSize()+iCBTag.getSize()+4+allocDesc.getSize();
    }

    /**
	このUDFエレメントのサイズをlong 型で返す。

	@return UDFエレメントのサイズ。
    */
    public long getLongSize(){
	return (long)0+descTag.getSize()+iCBTag.getSize()+4+allocDesc.getSize();
    }
    private UDF_tag descTag;
    private UDF_icbtag iCBTag;
    private UDF_uint32 lenOfAllocDesc;
    private UDF_bytes allocDesc;

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
	lenOfAllocDescを取得する。

	@return 取得したlenOfAllocDesc を返す。
    */
    public UDF_uint32 getLenOfAllocDesc(){return lenOfAllocDesc;}
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
	iCBTagを設定する。

	@param	v 設定する値。
    */
    public void setICBTag(UDF_icbtag v){replaceChild(v, iCBTag); iCBTag = v;}
    /**
	lenOfAllocDescを設定する。

	@param	v 設定する値。
    */
    public void setLenOfAllocDesc(UDF_uint32 v){replaceChild(v, lenOfAllocDesc); lenOfAllocDesc = v;}
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
	iCBTag = (UDF_icbtag)createElement("UDF_icbtag", "", "icb-tag");
	rsize += iCBTag.readFrom(f);
	lenOfAllocDesc = (UDF_uint32)createElement("UDF_uint32", "", "len-of-alloc-desc");
	rsize += lenOfAllocDesc.readFrom(f);
	allocDesc = (UDF_bytes)createElement("UDF_bytes", "", "alloc-desc");
	allocDesc.setSize(getLenOfAllocDesc().getIntValue());
	rsize += allocDesc.readFrom(f);
	apply( );
	postReadHook(f);
	return rsize;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long wsize = 0;
	wsize += descTag.writeTo(f);
	wsize += iCBTag.writeTo(f);
	wsize += lenOfAllocDesc.writeTo(f);
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
	    else if(name.equals("icb-tag")){
		iCBTag = (UDF_icbtag)createElement("UDF_icbtag", "", "icb-tag");
		iCBTag.readFromXML(child);
	    }
	    else if(name.equals("len-of-alloc-desc")){
		lenOfAllocDesc = (UDF_uint32)createElement("UDF_uint32", "", "len-of-alloc-desc");
		lenOfAllocDesc.readFromXML(child);
	    }
	    else if(name.equals("alloc-desc")){
		allocDesc = (UDF_bytes)createElement("UDF_bytes", "", "alloc-desc");
		allocDesc.setSize(getLenOfAllocDesc().getIntValue());
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
	iCBTag = (UDF_icbtag)createElement("UDF_icbtag", "", "icb-tag");
	iCBTag.setDefaultValue();
	lenOfAllocDesc = (UDF_uint32)createElement("UDF_uint32", "", "len-of-alloc-desc");
	lenOfAllocDesc.setDefaultValue();
	allocDesc = (UDF_bytes)createElement("UDF_bytes", "", "alloc-desc");
	allocDesc.setSize(getLenOfAllocDesc().getIntValue());
	allocDesc.setDefaultValue();

	apply( );
	postSetDefaultValueHook();
    }


    /**
	複製する
    */
    public UDF_Element duplicateElement(){
	UDF_desc263 dup = new UDF_desc263(this, null, getName());
	dup.setDefaultValue();
	dup.duplicateHook(this);
	dup.setDescTag((UDF_tag)descTag.duplicateElement());
	dup.setICBTag((UDF_icbtag)iCBTag.duplicateElement());
	dup.setLenOfAllocDesc((UDF_uint32)lenOfAllocDesc.duplicateElement());
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
	appendChild(iCBTag);
	appendChild(lenOfAllocDesc);
	appendChild(allocDesc);
    }

    public String getInfo(int indent){
	String a = "";
	a += UDF_Util.repeat(' ', indent);
	a += descTag.getInfo(indent + 1);
	a += iCBTag.getInfo(indent + 1);
	a += lenOfAllocDesc.getInfo(indent + 1);
	a += allocDesc.getInfo(indent + 1);
      return a;
    }
    public void debug(int indent){
	System.err.print(UDF_Util.repeat(' ', indent));

	String info = " gp=" + getGlobalPoint();
	info += " pn=" + getElemPartRefNo();
	info += " sn=" + getPartSubno();
	System.err.println(getName() + ": (" + hashCode() + ")" + info);	descTag.debug(indent + 1);
	iCBTag.debug(indent + 1);
	lenOfAllocDesc.debug(indent + 1);
	allocDesc.debug(indent + 1);
    }
//begin:add your code here
    public int getFixedTagId() { return 263; }
    
    /**
       Desc263を読む度に呼ばれるフック関数
    */
    public void postReadHook(UDF_RandomAccess f) throws UDF_Exception{
	
	UDF_RandomAccessBuffer rab = getAllocDesc().genRandomAccessBytes();
	UDF_ElementList v = new UDF_ElementList();
	int adflag = getICBTag().getFlags().getIntValue() & 0x03;

	
	while(!rab.eof()){
	    
	    UDF_AD ad = null;
	    
	    if(adflag == 0)
		ad = (UDF_short_ad)createElement("UDF_short_ad", null, "UDF_short_ad");
	    else if(adflag == 1)
		ad = (UDF_short_ad)createElement("UDF_long_ad", null, "UDF_long_ad");
	    else if(adflag == 2)
		ad = (UDF_short_ad)createElement("UDF_ext_ad", null, "UDF_ext_ad");
	    else{
		;
	    }
	    
	    try{
		ad.readFrom(rab);
	    }
	    catch(Exception e){
		
		e.printStackTrace();
	    }
	    v.add(ad);
	}
	
	getAllocDesc().replaceChildren(v);
    }

    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	final short category = UDF_Error.C_ECMA167;
	UDF_Error ret;
	
	
	el.addError(super.verify());  // Descriptor Tag
	
	ret = verifyId();
	if(ret.isError()){
	    
	    ret.setRefer("4/14.11.1");
	    el.addError(ret);
	}
	
	
	// ICBTag のFileType は1でなければならない
	ret = iCBTag.verifyFileType(1);
	if(ret.isError()){
	    
	    ret.setMessage("The File Type field of the icbtag(4/14.6) for this descriptor shall contain 1.");
	    ret.setRName("ICB Tag");
	    el.addError(ret);
	}
	
	// Length Of AD + 40 は論理ブロック以下でなければならない
	long lenofad = lenOfAllocDesc.getLongValue();
	if(UDF_Env.LBS < lenofad + 40){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Length of Allocation Descriptors(=L_AD)",
			 "L_AD+40 shall not be greater than the size of a logical block.",
			 "4/14.11.3", String.valueOf(lenofad), ""));
	}
	
	// 実際のＡＤのサイズと合わない
	if(lenofad != allocDesc.getSize()){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_WARNING, "Length of Allocation Descriptors(=L_AD)",
			 "This field specifies the length, in bytes, of the Allocation Descriptors field.",
			 "4/14.11.3", String.valueOf(lenofad), String.valueOf(allocDesc.getSize())));
	    
	    if(allocDesc.getSize() < lenofad)
		lenofad = allocDesc.getSize();
	}
	
	
	// AD のExtentLen の合計は論理ブロック倍でなければならない
	byte[] adbuf = allocDesc.getData();
	int    flags = (iCBTag.getFlags().getIntValue() & 0x03);
	long   offset = 0;
	long   sumextlen = 0;
	UDF_ElementBase[] child = getAllocDesc().getChildren();
	
	
	for(int i = 0; i < child.length; i++){
	    
	    UDF_AD ad = (UDF_AD)child[i];
	      
	    el.addError(ad.verify("Allocation descriptors[" + i + "]"));
	    sumextlen += ad.getLen();
	}
	
	if((sumextlen % UDF_Env.LBS) != 0){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Allocation descriptors",
			 "The extent length fields of these allocation descriptors shall be an integral multiple of the logical block size.",
			 "4/14.11.4", String.valueOf(sumextlen), ""));
	}
	
	el.setGlobalPoint(getGlobalPoint());
	el.setRName("Unallocated Space Entry");
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
