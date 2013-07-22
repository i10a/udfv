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
Virtual&nbsp;Partition&nbsp;Map&nbsp;を表現するクラス。<br/>


<dl>

<dt>フィールド一覧</dt>
<dd>
<table border="1">
<tr><td>メンバ</td><td>型</td><td>サイズ</td></tr>
<tr><td><b>PartMapType</b></td><td><b>UDF_uint8</b></td><td><i>1</i></td></tr>
<tr><td><b>PartMapLen</b></td><td><b>UDF_uint8</b></td><td><i>1</i></td></tr>
<tr><td><b>Reserved</b></td><td><b>UDF_bytes</b></td><td><i>2</i></td></tr>
<tr><td><b>PartTypeId</b></td><td><b>UDF_regid</b></td><td><i>partTypeId.getSize()</i></td></tr>
<tr><td><b>VolSeqNumber</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td></tr>
<tr><td><b>PartNumber</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td></tr>
<tr><td><b>Reserved2</b></td><td><b>UDF_bytes</b></td><td><i>24</i></td></tr>
</table>
</dd>
</dl>
*/
public class UDF_VirtualPartMap extends UDF_PartMap 
{

    /**
	このオブジェクトのパッケージ名を除いたクラス名を取得する。

	@return クラス名
    */
    public static String getUDFClassName( ) {
        return "UDF_VirtualPartMap";
    }

    /**
	コンストラクタ。

	@param elem   親エレメント。
	@param prefix ネームスペース。
	@param name   エレメント名。
    */
    public UDF_VirtualPartMap(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }
    /**
	このUDFエレメントのサイズを返す。

	@return UDFエレメントのサイズ。
    */
    public int getSize(){
	return 0+1+1+reserved.getSize()+partTypeId.getSize()+2+2+reserved2.getSize();
    }

    /**
	このUDFエレメントのサイズをlong 型で返す。

	@return UDFエレメントのサイズ。
    */
    public long getLongSize(){
	return (long)0+1+1+reserved.getSize()+partTypeId.getSize()+2+2+reserved2.getSize();
    }
    private UDF_uint8 partMapType;
    private UDF_uint8 partMapLen;
    private UDF_bytes reserved;
    private com.udfv.ecma167.UDF_regid partTypeId;
    private UDF_uint16 volSeqNumber;
    private UDF_uint16 partNumber;
    private UDF_bytes reserved2;

    /**
	partMapTypeを取得する。

	@return 取得したpartMapType を返す。
    */
    public UDF_uint8 getPartMapType(){return partMapType;}
    /**
	partMapLenを取得する。

	@return 取得したpartMapLen を返す。
    */
    public UDF_uint8 getPartMapLen(){return partMapLen;}
    /**
	reservedを取得する。

	@return 取得したreserved を返す。
    */
    public UDF_bytes getReserved(){return reserved;}
    /**
	partTypeIdを取得する。

	@return 取得したpartTypeId を返す。
    */
    public com.udfv.ecma167.UDF_regid getPartTypeId(){return partTypeId;}
    /**
	volSeqNumberを取得する。

	@return 取得したvolSeqNumber を返す。
    */
    public UDF_uint16 getVolSeqNumber(){return volSeqNumber;}
    /**
	partNumberを取得する。

	@return 取得したpartNumber を返す。
    */
    public UDF_uint16 getPartNumber(){return partNumber;}
    /**
	reserved2を取得する。

	@return 取得したreserved2 を返す。
    */
    public UDF_bytes getReserved2(){return reserved2;}

    /**
	partMapTypeを設定する。

	@param	v 設定する値。
    */
    public void setPartMapType(UDF_uint8 v){replaceChild(v, partMapType); partMapType = v;}
    /**
	partMapLenを設定する。

	@param	v 設定する値。
    */
    public void setPartMapLen(UDF_uint8 v){replaceChild(v, partMapLen); partMapLen = v;}
    /**
	reservedを設定する。

	@param	v 設定する値。
    */
    public void setReserved(UDF_bytes v){replaceChild(v, reserved); reserved = v;}
    /**
	partTypeIdを設定する。

	@param	v 設定する値。
    */
    public void setPartTypeId(com.udfv.ecma167.UDF_regid v){replaceChild(v, partTypeId); partTypeId = v;}
    /**
	volSeqNumberを設定する。

	@param	v 設定する値。
    */
    public void setVolSeqNumber(UDF_uint16 v){replaceChild(v, volSeqNumber); volSeqNumber = v;}
    /**
	partNumberを設定する。

	@param	v 設定する値。
    */
    public void setPartNumber(UDF_uint16 v){replaceChild(v, partNumber); partNumber = v;}
    /**
	reserved2を設定する。

	@param	v 設定する値。
    */
    public void setReserved2(UDF_bytes v){replaceChild(v, reserved2); reserved2 = v;}

    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long rsize = 0;
	preReadHook(f);
	partMapType = (UDF_uint8)createElement("UDF_uint8", "", "part-map-type");
	rsize += partMapType.readFrom(f);
	partMapLen = (UDF_uint8)createElement("UDF_uint8", "", "part-map-len");
	rsize += partMapLen.readFrom(f);
	reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
	reserved.setSize(2);
	rsize += reserved.readFrom(f);
	partTypeId = (UDF_regid)createElement("UDF_regid", "", "part-type-id");
	rsize += partTypeId.readFrom(f);
	volSeqNumber = (UDF_uint16)createElement("UDF_uint16", "", "vol-seq-number");
	rsize += volSeqNumber.readFrom(f);
	partNumber = (UDF_uint16)createElement("UDF_uint16", "", "part-number");
	rsize += partNumber.readFrom(f);
	reserved2 = (UDF_bytes)createElement("UDF_bytes", "", "reserved2");
	reserved2.setSize(24);
	rsize += reserved2.readFrom(f);
	apply( );
	postReadHook(f);
	return rsize;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long wsize = 0;
	wsize += partMapType.writeTo(f);
	wsize += partMapLen.writeTo(f);
	wsize += reserved.writeTo(f);
	wsize += partTypeId.writeTo(f);
	wsize += volSeqNumber.writeTo(f);
	wsize += partNumber.writeTo(f);
	wsize += reserved2.writeTo(f);
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
	    else if(name.equals("part-map-type")){
		partMapType = (UDF_uint8)createElement("UDF_uint8", "", "part-map-type");
		partMapType.readFromXML(child);
	    }
	    else if(name.equals("part-map-len")){
		partMapLen = (UDF_uint8)createElement("UDF_uint8", "", "part-map-len");
		partMapLen.readFromXML(child);
	    }
	    else if(name.equals("reserved")){
		reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
		reserved.setSize(2);
		reserved.readFromXML(child);
	    }
	    else if(name.equals("part-type-id")){
		partTypeId = (UDF_regid)createElement("UDF_regid", "", "part-type-id");
		partTypeId.readFromXML(child);
	    }
	    else if(name.equals("vol-seq-number")){
		volSeqNumber = (UDF_uint16)createElement("UDF_uint16", "", "vol-seq-number");
		volSeqNumber.readFromXML(child);
	    }
	    else if(name.equals("part-number")){
		partNumber = (UDF_uint16)createElement("UDF_uint16", "", "part-number");
		partNumber.readFromXML(child);
	    }
	    else if(name.equals("reserved2")){
		reserved2 = (UDF_bytes)createElement("UDF_bytes", "", "reserved2");
		reserved2.setSize(24);
		reserved2.readFromXML(child);
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
	partMapType = (UDF_uint8)createElement("UDF_uint8", "", "part-map-type");
	partMapType.setDefaultValue();
	partMapLen = (UDF_uint8)createElement("UDF_uint8", "", "part-map-len");
	partMapLen.setDefaultValue();
	reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
	reserved.setSize(2);
	reserved.setDefaultValue();
	partTypeId = (UDF_regid)createElement("UDF_regid", "", "part-type-id");
	partTypeId.setDefaultValue();
	volSeqNumber = (UDF_uint16)createElement("UDF_uint16", "", "vol-seq-number");
	volSeqNumber.setDefaultValue();
	partNumber = (UDF_uint16)createElement("UDF_uint16", "", "part-number");
	partNumber.setDefaultValue();
	reserved2 = (UDF_bytes)createElement("UDF_bytes", "", "reserved2");
	reserved2.setSize(24);
	reserved2.setDefaultValue();

	apply( );
	postSetDefaultValueHook();
    }


    /**
	複製する
    */
    public UDF_Element duplicateElement(){
	UDF_VirtualPartMap dup = new UDF_VirtualPartMap(this, null, getName());
	dup.setDefaultValue();
	dup.duplicateHook(this);
	dup.setPartMapType((UDF_uint8)partMapType.duplicateElement());
	dup.setPartMapLen((UDF_uint8)partMapLen.duplicateElement());
	dup.setReserved((UDF_bytes)reserved.duplicateElement());
	dup.setPartTypeId((com.udfv.ecma167.UDF_regid)partTypeId.duplicateElement());
	dup.setVolSeqNumber((UDF_uint16)volSeqNumber.duplicateElement());
	dup.setPartNumber((UDF_uint16)partNumber.duplicateElement());
	dup.setReserved2((UDF_bytes)reserved2.duplicateElement());

	apply();

	return dup;
    }

    /**
        子の UDF Element の Node を append しなおす。

    */
    private void apply( ) {

	removeAllChildren();
	appendChild(partMapType);
	appendChild(partMapLen);
	appendChild(reserved);
	appendChild(partTypeId);
	appendChild(volSeqNumber);
	appendChild(partNumber);
	appendChild(reserved2);
    }

    public String getInfo(int indent){
	String a = "";
	a += UDF_Util.repeat(' ', indent);
	a += partMapType.getInfo(indent + 1);
	a += partMapLen.getInfo(indent + 1);
	a += reserved.getInfo(indent + 1);
	a += partTypeId.getInfo(indent + 1);
	a += volSeqNumber.getInfo(indent + 1);
	a += partNumber.getInfo(indent + 1);
	a += reserved2.getInfo(indent + 1);
      return a;
    }
    public void debug(int indent){
	System.err.print(UDF_Util.repeat(' ', indent));

	String info = " gp=" + getGlobalPoint();
	info += " pn=" + getElemPartRefNo();
	info += " sn=" + getPartSubno();
	System.err.println(getName() + ": (" + hashCode() + ")" + info);	partMapType.debug(indent + 1);
	partMapLen.debug(indent + 1);
	reserved.debug(indent + 1);
	partTypeId.debug(indent + 1);
	volSeqNumber.debug(indent + 1);
	partNumber.debug(indent + 1);
	reserved2.debug(indent + 1);
    }
//begin:add your code here

    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	if(env.udf_revision != 0x150)
	    return el;
	
	el.addError(verifyBase(UDF_Error.C_UDF150, "2.2.8"));
	
	el.setRName("UDF Virtual Partition Map");
	el.setGlobalPoint(getGlobalPoint());
	return el;
    }
    
    public UDF_ErrorList verifyBase(short category, String refer) throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	UDF_Error ret;
	final String basemsg = "Shall be recorded in the format shown in 'Layout of Type 2 partition map for virtual partition'.";
	
	
	// Virtual Partition Map が存在するときは、パーティションマップの数は少なくとも2つ以上必要
	if(env.getPartMapList().size() < 2){
	    
	    el.addError(new UDF_Error(category, UDF_Error.L_ERROR, "",
				      "If a Virtual Partition Map is recorded, then the Logical Volume Descriptor shall contaion " +
				      "at least two partition maps.", refer));
	}
	else{
	    
	    // Type 1 のパーティションマップが存在しない
	    int i = 0;
	    for(; i < env.getPartMapList().size() ; i++){
		
		if(com.udfv.ecma167.UDF_part_map1.class.isAssignableFrom(env.part[i].getClass()))
		    break;
	    }
	    
	    if(env.getPartMapList().size() <= i){
		
		el.addError(new UDF_Error
			    (category, UDF_Error.L_ERROR, "",
			     "If a Virtual Partition Map is recorded, then the Logical Volume Descriptor shall contaion " +
			     "at least two partition maps. One partition map, shall be recorded as a Type1 partition map.", refer));
	    }
	}
	
	// Partition Map Type は2
	if(getPartMapType().getIntValue() != 2){
	    
	    el.addError(new UDF_Error(category, UDF_Error.L_ERROR, "Partition Map Type",
				      basemsg, refer, String.valueOf(getPartMapType().getIntValue()), "2"));
	}
	
	// Partition Map Length は64
	if(getPartMapLen().getIntValue() != 64){
	    
	    el.addError(new UDF_Error(category, UDF_Error.L_ERROR, "Partition Map Length",
				      basemsg, refer, String.valueOf(getPartMapLen().getIntValue()), "64"));
	}
	
	// reserved は0
	if(!UDF_Util.isAllZero(getReserved().getData())){
	    
	    el.addError(new UDF_Error(category, UDF_Error.L_ERROR, "Reserved", basemsg + "(#00 bytes)", refer));
	}
	
	// Partition Type Identifier の検証
	final String ptid = "Partition Type Identifier";
	el.addError(getPartTypeId().verify(ptid));
	ret = getPartTypeId().verifyFlags(0);
	if(ret.isError()){
	    
	    ret.setCategory(category);
	    ret.setMessage(basemsg);
	    ret.setRefer(refer);
	    ret.setRName(ptid);
	    el.addError(ret);
	}
	
	ret = getPartTypeId().verifyId("*UDF Virtual Partition");
	if(ret.isError()){
	    
	    ret.setCategory(category);
	    ret.setMessage(basemsg);
	    ret.setRefer(refer);
	    ret.setRName(ptid);
	    el.addError(ret);
	}
	
	// reserved2 も0
	if(!UDF_Util.isAllZero(getReserved2().getData())){
	    
	    el.addError(new UDF_Error(category, UDF_Error.L_ERROR, "Reserved(2nd)", basemsg + "(#00 bytes)", refer));
	}
	
	return el;
    }

    public void recalcPM(int partrefno) throws UDF_PartMapException,UDF_InternalException,IOException {
	super.recalcPM(partrefno);

	int part_loc = 0;
	try{
	    int partno = env.getPartDescByPartRefNo(UDF_Env.VDS_AUTO, partrefno).getPartNumber().getIntValue();
	    part_loc = env.getPartStartingLoc(partno);
	}
	catch(UDF_VolException e){
	    e.printStackTrace();
	    throw new UDF_InternalException(this, "no volume");
	}

	//VATは LBNで検索するのがおそらく正しい
	int last_lbn = (int)(env.getImageSize()/env.LBS) - 1 - part_loc;

	readVAT(partrefno, last_lbn, 0, 0);

	//VATがいくつあったか数える
	int nvat = 0;
	try{
	    while(env.getPartMapExtent(partrefno, nvat) != null)
		++nvat;
	    --nvat;
	}
	catch(UDF_PartMapException e){
	    ;//Exceptionが起きるまでカウントアップ
	}
    }

    protected void readVAT(int partrefno, int lbn, int partno, int vatno)  throws UDF_PartMapException,UDF_InternalException,IOException{
	int part_loc = 0;
	try{
	    part_loc = env.getPartStartingLoc(0);
	}
	catch(UDF_VolException e){
	    e.printStackTrace();
	    throw new UDF_InternalException(this, "no volume");
	}

	UDF_FEDesc fe = null;
	UDF_Element x = findExtentByLoc(lbn, partno, 0);
	if(x == null)//古いXMLは secで記録されている
	    x = findExtentByLoc(lbn + part_loc, -1, 0);

	if(x == null)
	    return;//ないときは何もしない(VATを利用しない)

	fe = (UDF_FEDesc)x.getFirstChild();

	UDF_VirtualAllocTable150 vat = null;
	//DDDDDDDDDDDDDDDDD とりあえず alloctype 3のみ対処
	if(fe.getAllocType() == 3)
	    vat = (UDF_VirtualAllocTable150)fe.getAllocDesc().getFirstChild();

	//Spareする前の基底のExtentを作る
	//それは、参照しているパーティションに等しい
	UDF_Extent ext = (UDF_Extent)createElement("UDF_Extent", null, null);
	try{
	    com.udfv.ecma167.UDF_desc5 d5 = env.getPartDescByPartRefNo(partrefno);
	    ext.addExtent(d5.getPartStartingLoc().getIntValue(),
			  -1,
			  d5.getPartLen().getLongValue() * env.LBS);
	}
	catch(UDF_VolException e){
	    e.printStackTrace();
	    throw new UDF_InternalException(e);
	}


	UDF_Element[] ee = vat.getVATEntry().getChildren();
	boolean has_spare = false;

	for(int k=0 ; k<ee.length ; ++k){
	    UDF_uint32 dst = (UDF_uint32)ee[k];
	    if(dst.getLongValue() != 0xffffffffL){
		if(ext.spareExtent(k, -1, dst.getIntValue() + part_loc, -1, env.LBS, getPartNumber().getIntValue())){
		    has_spare = true;
		}
	    }
	}
	
	if(has_spare){
	    ext.blessExtent();
	}
	env.setPartMapExtent(partrefno, vatno, ext);
	env.setPartMapRandomAccess(partrefno, vatno, ext.genRandomAccessExtent());

	int prev_loc = vat.getPreviousVATICBLocation().getIntValue();

	if(prev_loc != 0xffffffff){
	    readVAT(partrefno, prev_loc, partno, vatno+1);
	}
    }


//end:
};
