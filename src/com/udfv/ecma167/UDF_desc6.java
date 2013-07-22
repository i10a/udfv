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
Logical&nbsp;Volume&nbsp;Descriptor&nbsp;を表現するクラス。<br/>


<dl>

<dt>フィールド一覧</dt>
<dd>
<table border="1">
<tr><td>メンバ</td><td>型</td><td>サイズ</td></tr>
<tr><td><b>DescTag</b></td><td><b>UDF_tag</b></td><td><i>descTag.getSize()</i></td></tr>
<tr><td><b>VolDescSeqNumber</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>DescCharSet</b></td><td><b>UDF_charspec</b></td><td><i>descCharSet.getSize()</i></td></tr>
<tr><td><b>LogicalVolId</b></td><td><b>UDF_dstring</b></td><td><i>128</i></td></tr>
<tr><td><b>LogicalBlockSize</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>DomainId</b></td><td><b>UDF_regid</b></td><td><i>domainId.getSize()</i></td></tr>
<tr><td><b>LogicalVolContentsUse</b></td><td><b>UDF_bytes</b></td><td><i>16</i></td></tr>
<tr><td><b>MapTableLen</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>NumberOfPartMaps</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>ImplId</b></td><td><b>UDF_regid</b></td><td><i>implId.getSize()</i></td></tr>
<tr><td><b>ImplUse</b></td><td><b>UDF_bytes</b></td><td><i>128</i></td></tr>
<tr><td><b>IntegritySeqExtent</b></td><td><b>UDF_extent_ad</b></td><td><i>integritySeqExtent.getSize()</i></td></tr>
<tr><td><b>PartMaps</b></td><td><b>UDF_bytes</b></td><td><i>getMapTableLen().getIntValue()</i></td></tr>
</table>
</dd>
</dl>
*/
public class UDF_desc6 extends UDF_CrcDesc implements UDF_VolDesc, UDF_VolDescSeqNum
{

    /**
	このオブジェクトのパッケージ名を除いたクラス名を取得する。

	@return クラス名
    */
    public static String getUDFClassName( ) {
        return "UDF_desc6";
    }

    /**
	コンストラクタ。

	@param elem   親エレメント。
	@param prefix ネームスペース。
	@param name   エレメント名。
    */
    public UDF_desc6(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }
    /**
	このUDFエレメントのサイズを返す。

	@return UDFエレメントのサイズ。
    */
    public int getSize(){
	return 0+descTag.getSize()+4+descCharSet.getSize()+logicalVolId.getSize()+4+domainId.getSize()+logicalVolContentsUse.getSize()+4+4+implId.getSize()+implUse.getSize()+integritySeqExtent.getSize()+partMaps.getSize();
    }

    /**
	このUDFエレメントのサイズをlong 型で返す。

	@return UDFエレメントのサイズ。
    */
    public long getLongSize(){
	return (long)0+descTag.getSize()+4+descCharSet.getSize()+logicalVolId.getSize()+4+domainId.getSize()+logicalVolContentsUse.getSize()+4+4+implId.getSize()+implUse.getSize()+integritySeqExtent.getSize()+partMaps.getSize();
    }
    private UDF_tag descTag;
    private UDF_uint32 volDescSeqNumber;
    private UDF_charspec descCharSet;
    private UDF_dstring logicalVolId;
    private UDF_uint32 logicalBlockSize;
    private UDF_regid domainId;
    private UDF_bytes logicalVolContentsUse;
    private UDF_uint32 mapTableLen;
    private UDF_uint32 numberOfPartMaps;
    private UDF_regid implId;
    private UDF_bytes implUse;
    private UDF_extent_ad integritySeqExtent;
    private UDF_bytes partMaps;

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
	descCharSetを取得する。

	@return 取得したdescCharSet を返す。
    */
    public UDF_charspec getDescCharSet(){return descCharSet;}
    /**
	logicalVolIdを取得する。

	@return 取得したlogicalVolId を返す。
    */
    public UDF_dstring getLogicalVolId(){return logicalVolId;}
    /**
	logicalBlockSizeを取得する。

	@return 取得したlogicalBlockSize を返す。
    */
    public UDF_uint32 getLogicalBlockSize(){return logicalBlockSize;}
    /**
	domainIdを取得する。

	@return 取得したdomainId を返す。
    */
    public UDF_regid getDomainId(){return domainId;}
    /**
	logicalVolContentsUseを取得する。

	@return 取得したlogicalVolContentsUse を返す。
    */
    public UDF_bytes getLogicalVolContentsUse(){return logicalVolContentsUse;}
    /**
	mapTableLenを取得する。

	@return 取得したmapTableLen を返す。
    */
    public UDF_uint32 getMapTableLen(){return mapTableLen;}
    /**
	numberOfPartMapsを取得する。

	@return 取得したnumberOfPartMaps を返す。
    */
    public UDF_uint32 getNumberOfPartMaps(){return numberOfPartMaps;}
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
	integritySeqExtentを取得する。

	@return 取得したintegritySeqExtent を返す。
    */
    public UDF_extent_ad getIntegritySeqExtent(){return integritySeqExtent;}
    /**
	partMapsを取得する。

	@return 取得したpartMaps を返す。
    */
    public UDF_bytes getPartMaps(){return partMaps;}

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
	descCharSetを設定する。

	@param	v 設定する値。
    */
    public void setDescCharSet(UDF_charspec v){replaceChild(v, descCharSet); descCharSet = v;}
    /**
	logicalVolIdを設定する。

	@param	v 設定する値。
    */
    public void setLogicalVolId(UDF_dstring v){replaceChild(v, logicalVolId); logicalVolId = v;}
    /**
	logicalBlockSizeを設定する。

	@param	v 設定する値。
    */
    public void setLogicalBlockSize(UDF_uint32 v){replaceChild(v, logicalBlockSize); logicalBlockSize = v;}
    /**
	domainIdを設定する。

	@param	v 設定する値。
    */
    public void setDomainId(UDF_regid v){replaceChild(v, domainId); domainId = v;}
    /**
	logicalVolContentsUseを設定する。

	@param	v 設定する値。
    */
    public void setLogicalVolContentsUse(UDF_bytes v){replaceChild(v, logicalVolContentsUse); logicalVolContentsUse = v;}
    /**
	mapTableLenを設定する。

	@param	v 設定する値。
    */
    public void setMapTableLen(UDF_uint32 v){replaceChild(v, mapTableLen); mapTableLen = v;}
    /**
	numberOfPartMapsを設定する。

	@param	v 設定する値。
    */
    public void setNumberOfPartMaps(UDF_uint32 v){replaceChild(v, numberOfPartMaps); numberOfPartMaps = v;}
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
    /**
	integritySeqExtentを設定する。

	@param	v 設定する値。
    */
    public void setIntegritySeqExtent(UDF_extent_ad v){replaceChild(v, integritySeqExtent); integritySeqExtent = v;}
    /**
	partMapsを設定する。

	@param	v 設定する値。
    */
    public void setPartMaps(UDF_bytes v){replaceChild(v, partMaps); partMaps = v;}

    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long rsize = 0;
	preReadHook(f);
	descTag = (UDF_tag)createElement("UDF_tag", "", "desc-tag");
	rsize += descTag.readFrom(f);
	volDescSeqNumber = (UDF_uint32)createElement("UDF_uint32", "", "vol-desc-seq-number");
	rsize += volDescSeqNumber.readFrom(f);
	descCharSet = (UDF_charspec)createElement("UDF_charspec", "", "desc-char-set");
	rsize += descCharSet.readFrom(f);
	logicalVolId = (UDF_dstring)createElement("UDF_dstring", "", "logical-vol-id");
	logicalVolId.setSize(128);
	rsize += logicalVolId.readFrom(f);
	logicalBlockSize = (UDF_uint32)createElement("UDF_uint32", "", "logical-block-size");
	rsize += logicalBlockSize.readFrom(f);
	domainId = (UDF_regid)createElement("UDF_regid", "", "domain-id");
	rsize += domainId.readFrom(f);
	logicalVolContentsUse = (UDF_bytes)createElement("UDF_bytes", "", "logical-vol-contents-use");
	logicalVolContentsUse.setSize(16);
	rsize += logicalVolContentsUse.readFrom(f);
	mapTableLen = (UDF_uint32)createElement("UDF_uint32", "", "map-table-len");
	rsize += mapTableLen.readFrom(f);
	numberOfPartMaps = (UDF_uint32)createElement("UDF_uint32", "", "number-of-part-maps");
	rsize += numberOfPartMaps.readFrom(f);
	implId = (UDF_regid)createElement("UDF_regid", "", "impl-id");
	rsize += implId.readFrom(f);
	implUse = (UDF_bytes)createElement("UDF_bytes", "", "impl-use");
	implUse.setSize(128);
	rsize += implUse.readFrom(f);
	integritySeqExtent = (UDF_extent_ad)createElement("UDF_extent_ad", "", "integrity-seq-extent");
	rsize += integritySeqExtent.readFrom(f);
	partMaps = (UDF_bytes)createElement("UDF_bytes", "", "part-maps");
	partMaps.setSize(getMapTableLen().getIntValue());
	rsize += partMaps.readFrom(f);
	apply( );
	postReadHook(f);
	return rsize;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long wsize = 0;
	wsize += descTag.writeTo(f);
	wsize += volDescSeqNumber.writeTo(f);
	wsize += descCharSet.writeTo(f);
	wsize += logicalVolId.writeTo(f);
	wsize += logicalBlockSize.writeTo(f);
	wsize += domainId.writeTo(f);
	wsize += logicalVolContentsUse.writeTo(f);
	wsize += mapTableLen.writeTo(f);
	wsize += numberOfPartMaps.writeTo(f);
	wsize += implId.writeTo(f);
	wsize += implUse.writeTo(f);
	wsize += integritySeqExtent.writeTo(f);
	wsize += partMaps.writeTo(f);
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
	    else if(name.equals("desc-char-set")){
		descCharSet = (UDF_charspec)createElement("UDF_charspec", "", "desc-char-set");
		descCharSet.readFromXML(child);
	    }
	    else if(name.equals("logical-vol-id")){
		logicalVolId = (UDF_dstring)createElement("UDF_dstring", "", "logical-vol-id");
		logicalVolId.setSize(128);
		logicalVolId.readFromXML(child);
	    }
	    else if(name.equals("logical-block-size")){
		logicalBlockSize = (UDF_uint32)createElement("UDF_uint32", "", "logical-block-size");
		logicalBlockSize.readFromXML(child);
	    }
	    else if(name.equals("domain-id")){
		domainId = (UDF_regid)createElement("UDF_regid", "", "domain-id");
		domainId.readFromXML(child);
	    }
	    else if(name.equals("logical-vol-contents-use")){
		logicalVolContentsUse = (UDF_bytes)createElement("UDF_bytes", "", "logical-vol-contents-use");
		logicalVolContentsUse.setSize(16);
		logicalVolContentsUse.readFromXML(child);
	    }
	    else if(name.equals("map-table-len")){
		mapTableLen = (UDF_uint32)createElement("UDF_uint32", "", "map-table-len");
		mapTableLen.readFromXML(child);
	    }
	    else if(name.equals("number-of-part-maps")){
		numberOfPartMaps = (UDF_uint32)createElement("UDF_uint32", "", "number-of-part-maps");
		numberOfPartMaps.readFromXML(child);
	    }
	    else if(name.equals("impl-id")){
		implId = (UDF_regid)createElement("UDF_regid", "", "impl-id");
		implId.readFromXML(child);
	    }
	    else if(name.equals("impl-use")){
		implUse = (UDF_bytes)createElement("UDF_bytes", "", "impl-use");
		implUse.setSize(128);
		implUse.readFromXML(child);
	    }
	    else if(name.equals("integrity-seq-extent")){
		integritySeqExtent = (UDF_extent_ad)createElement("UDF_extent_ad", "", "integrity-seq-extent");
		integritySeqExtent.readFromXML(child);
	    }
	    else if(name.equals("part-maps")){
		partMaps = (UDF_bytes)createElement("UDF_bytes", "", "part-maps");
		partMaps.setSize(getMapTableLen().getIntValue());
		partMaps.readFromXML(child);
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
	descCharSet = (UDF_charspec)createElement("UDF_charspec", "", "desc-char-set");
	descCharSet.setDefaultValue();
	logicalVolId = (UDF_dstring)createElement("UDF_dstring", "", "logical-vol-id");
	logicalVolId.setSize(128);
	logicalVolId.setDefaultValue();
	logicalBlockSize = (UDF_uint32)createElement("UDF_uint32", "", "logical-block-size");
	logicalBlockSize.setDefaultValue();
	domainId = (UDF_regid)createElement("UDF_regid", "", "domain-id");
	domainId.setDefaultValue();
	logicalVolContentsUse = (UDF_bytes)createElement("UDF_bytes", "", "logical-vol-contents-use");
	logicalVolContentsUse.setSize(16);
	logicalVolContentsUse.setDefaultValue();
	mapTableLen = (UDF_uint32)createElement("UDF_uint32", "", "map-table-len");
	mapTableLen.setDefaultValue();
	numberOfPartMaps = (UDF_uint32)createElement("UDF_uint32", "", "number-of-part-maps");
	numberOfPartMaps.setDefaultValue();
	implId = (UDF_regid)createElement("UDF_regid", "", "impl-id");
	implId.setDefaultValue();
	implUse = (UDF_bytes)createElement("UDF_bytes", "", "impl-use");
	implUse.setSize(128);
	implUse.setDefaultValue();
	integritySeqExtent = (UDF_extent_ad)createElement("UDF_extent_ad", "", "integrity-seq-extent");
	integritySeqExtent.setDefaultValue();
	partMaps = (UDF_bytes)createElement("UDF_bytes", "", "part-maps");
	partMaps.setSize(getMapTableLen().getIntValue());
	partMaps.setDefaultValue();

	apply( );
	postSetDefaultValueHook();
    }


    /**
	複製する
    */
    public UDF_Element duplicateElement(){
	UDF_desc6 dup = new UDF_desc6(this, null, getName());
	dup.setDefaultValue();
	dup.duplicateHook(this);
	dup.setDescTag((UDF_tag)descTag.duplicateElement());
	dup.setVolDescSeqNumber((UDF_uint32)volDescSeqNumber.duplicateElement());
	dup.setDescCharSet((UDF_charspec)descCharSet.duplicateElement());
	dup.setLogicalVolId((UDF_dstring)logicalVolId.duplicateElement());
	dup.setLogicalBlockSize((UDF_uint32)logicalBlockSize.duplicateElement());
	dup.setDomainId((UDF_regid)domainId.duplicateElement());
	dup.setLogicalVolContentsUse((UDF_bytes)logicalVolContentsUse.duplicateElement());
	dup.setMapTableLen((UDF_uint32)mapTableLen.duplicateElement());
	dup.setNumberOfPartMaps((UDF_uint32)numberOfPartMaps.duplicateElement());
	dup.setImplId((UDF_regid)implId.duplicateElement());
	dup.setImplUse((UDF_bytes)implUse.duplicateElement());
	dup.setIntegritySeqExtent((UDF_extent_ad)integritySeqExtent.duplicateElement());
	dup.setPartMaps((UDF_bytes)partMaps.duplicateElement());

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
	appendChild(descCharSet);
	appendChild(logicalVolId);
	appendChild(logicalBlockSize);
	appendChild(domainId);
	appendChild(logicalVolContentsUse);
	appendChild(mapTableLen);
	appendChild(numberOfPartMaps);
	appendChild(implId);
	appendChild(implUse);
	appendChild(integritySeqExtent);
	appendChild(partMaps);
    }

    public String getInfo(int indent){
	String a = "";
	a += UDF_Util.repeat(' ', indent);
	a += descTag.getInfo(indent + 1);
	a += volDescSeqNumber.getInfo(indent + 1);
	a += descCharSet.getInfo(indent + 1);
	a += logicalVolId.getInfo(indent + 1);
	a += logicalBlockSize.getInfo(indent + 1);
	a += domainId.getInfo(indent + 1);
	a += logicalVolContentsUse.getInfo(indent + 1);
	a += mapTableLen.getInfo(indent + 1);
	a += numberOfPartMaps.getInfo(indent + 1);
	a += implId.getInfo(indent + 1);
	a += implUse.getInfo(indent + 1);
	a += integritySeqExtent.getInfo(indent + 1);
	a += partMaps.getInfo(indent + 1);
      return a;
    }
    public void debug(int indent){
	System.err.print(UDF_Util.repeat(' ', indent));

	String info = " gp=" + getGlobalPoint();
	info += " pn=" + getElemPartRefNo();
	info += " sn=" + getPartSubno();
	System.err.println(getName() + ": (" + hashCode() + ")" + info);	descTag.debug(indent + 1);
	volDescSeqNumber.debug(indent + 1);
	descCharSet.debug(indent + 1);
	logicalVolId.debug(indent + 1);
	logicalBlockSize.debug(indent + 1);
	domainId.debug(indent + 1);
	logicalVolContentsUse.debug(indent + 1);
	mapTableLen.debug(indent + 1);
	numberOfPartMaps.debug(indent + 1);
	implId.debug(indent + 1);
	implUse.debug(indent + 1);
	integritySeqExtent.debug(indent + 1);
	partMaps.debug(indent + 1);
    }
//begin:add your code here
    public int getFixedTagId() { return 6; }

    protected Hashtable availPartMap(){
	return new Hashtable();
    }

    /**
       LVISと PartMapsをセットアップする。

       このメソッドは内部的に使用するものである。
    */
    protected void setupLVISandFSDS() throws UDF_Exception{
	//env.is_len = getIntegritySeqExtent().getExtentLen().getLongValue();
	//env.is_loc = getIntegritySeqExtent().getExtentLoc().getIntValue();

	//Labelを打つ
	getIntegritySeqExtent().getExtentLoc().setAttribute("ref", "LVIS.loc");
	getIntegritySeqExtent().getExtentLen().setAttribute("ref", "LVIS.len");

	//UDF_long_ad ad = new UDF_long_ad(this, null, null);
	UDF_long_ad ad = (UDF_long_ad)createElement("UDF_long_ad", null, null);
	getLogicalVolContentsUse().readFromAndReplaceChild(ad);
      
	//env.fsds_len = ad.getLen();
	//env.fsds_lbn = ad.getLbn();
	//env.fsds_partno = ad.getPartRefNo();

	//Labelを打つ

	ad.getExtentLen().setAttribute("ref", "FSDS.len");
	ad.getExtentLoc().getLogicalBlockNumber().setAttribute("ref", "FSDS.lbn");
	ad.getExtentLoc().getPartReferenceNumber().setAttribute("ref", "FSDS.partno");
    }
    public UDF_AD getFSDSLoc() throws UDF_VolException{
	try{
	    UDF_AD ad = (UDF_AD)getLogicalVolContentsUse().getFirstChild();
	    return ad;
	}
	catch(ClassCastException e){
	    e.printStackTrace();
	    throw new UDF_VolException(this, "No valid logical vol contents use");
	}
    }
    
    /**
       PartMapsを読み込む。

       このメソッドは内部的に使用するものである。
       knownPMIDに存在してよい PartMapsの種類を入れて呼ぶこと。
    */
    final protected void readPartMaps(UDF_RandomAccess rab) throws UDF_Exception{
      try{
	  /*
	    リビジョンによって存在してよいパーティションマップが違う。
	    knownPMIDには何が存在してよいのかが入っている。
	  */
	  Hashtable knownPMID = availPartMap();
	  int partno = 0;
	  UDF_ElementList v = new UDF_ElementList();
	  
	  while(!rab.eof()){
	      int pm_typ = rab.readUint8();
	      rab.seek(-1, UDF_RandomAccess.SEEK_CUR);
	      switch(pm_typ){
	      case 1:
		  UDF_part_map1 pm = new UDF_part_map1(this, null, "UDF_part_map1");
		  pm.readFrom(rab);
		  v.add(pm);
		  
		  pm.setPartPartno(partno);

		  ++partno;
		  break;
	      case 2:
		  long pos0 = rab.getPointer();
		  UDF_part_map2 tmppm2 = new UDF_part_map2(this, null, null);
		  tmppm2.readFrom(rab);
		  String id = new String(UDF_Util.subByte(tmppm2.getPartId().getData(), 2, 24));
		  
		  UDF_PartMap pm2 = null;
		  if(knownPMID.get(id) != null){
		      String className = (String)knownPMID.get(id);
		      pm2 = (UDF_PartMap)createElement(className, null, className);
		      rab.seek(pos0);
		      pm2.readFrom(rab);
		      v.add(pm2);
		  }
		  else{
		      pm2 = tmppm2;  // ECMA167 のPart2
		  }
		  pm2.setPartPartno(partno);

		  ++partno;
		  break;
	      default:
		  throw new UDF_PartMapException(this, "BAD Partition Type", 
						 UDF_Exception.T_UDF200,
						 UDF_PartMapException.C_BADPARTMAP,
						 0);
	      }
	      getPartMaps().replaceChildren(v);
	  }
      }
      catch(IOException e){
	  // ignore
      }
    }
    /**
       Desc6を読む度に呼ばれるフック関数
     */
    public void postReadHook(UDF_RandomAccess f) throws UDF_Exception{
	UDF_RandomAccessBuffer rab = getPartMaps().genRandomAccessBytes();
	
	try{
	    readPartMaps(rab);
	}
	catch(UDF_PartMapException e){
	    throw new UDF_PartMapException(this, "BAD Partition Type", 
					   UDF_Exception.T_ECMA167_3,
					   UDF_PartMapException.C_BADPARTMAP,
					   0);
	}
    }

    /**
       Volume を読んだ後に、UDF_desc6がいくつあろうと preivailig に対し
       一回だけ呼ばれるフック関数

       MVDSおよびRVDSがあるので、実際には2回呼ばれる。
     */
    public void postVolReadHook(UDF_RandomAccess f) throws UDF_Exception{
	env.clearPartMap();
	env.part = new UDF_PartMap[getNumberOfPartMaps().getIntValue()];

	UDF_Element[] child = getPartMaps().getChildren();

	for(int i=0 ; i<child.length ; ++i){
	    try{
		env.part[i] = (UDF_PartMap)child[i];

		int partno = env.part[i].getPartNumber().getIntValue();
		int part_loc = env.getPartStartingLoc(partno);
		long part_len = env.getPartLen(partno);
		
		UDF_Extent parte = new UDF_Extent(this, null, "part");
		parte.addExtent(part_loc, -1, part_len);
		//env.part_e[i] = parte;
		//env.part_ra[i] = env.part_e[i].genRandomAccessExtent();
		
		env.setPartMapExtent(i, 0, parte);
		env.setPartMapRandomAccess(i, 0, parte.genRandomAccessExtent());
	    }
	    catch(UDF_VolException e){
		//PANIC
		//ここにくるときは恐しいとき
		e.printStackTrace();
		System.exit(0);
	    }
	}

	setupLVISandFSDS();
    }

    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	final short category = UDF_Error.C_ECMA167;
	UDF_Error  ret;
	
	
	el.addError(super.verify());  // Descriptor Tag
	
	ret = verifyId();
	if(ret.isError()){
	    
	    ret.setRefer("3/10.6.1");
	    el.addError(ret);
	}
	
	el.addError(logicalVolContentsUse.verify("Logical Volume Contents use"));  // long_ad
	el.addError(descCharSet.verify("Descriptor Character Set"));
	el.addError(domainId.verify("Domain Identifier"));
	
	if(mapTableLen.getLongValue() != partMaps.getLongSize()){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Map Table Length(=MT_L)",
			 "This field shall specify the length of the Partiton Maps field in bytes.",
			 "3/10.6.8", String.valueOf(mapTableLen.getLongValue()), String.valueOf(partMaps.getLongSize())));
	}
	
	// Number of Partition Maps が0のとき、integrity... のextent len が０でない
	if(numberOfPartMaps.getLongValue() == 0){
	    
	    ret = integritySeqExtent.verifyExtLen(0);
	    if(ret.isError()){
		
		ret.setLevel(UDF_Error.L_WARNING);
		ret.setRefer("3/10.6.2");
		ret.setMessage("If N_PM is 0, then the extent's length may be 0.");
		ret.setRName("Integrity Sequence Extent");
		el.addError(ret);
	    }
	}
	
	// Map Table Length が実際のサイズと異なる
	if(mapTableLen.getLongValue() < partMaps.getLongSize()){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Partition Maps",
			 "The length of the Partition Maps shall not exceed MT_L bytes.",
			 "3/10.6.13"));
	}
	
	el.addError(implId.verify("Implementation Identifier"));
	el.addError(integritySeqExtent.verify("Integrity Sequence Extent"));
	el.addError(verifyPartMaps());  // ECMA167 レベルの検証
	
	el.setRName("Logical Volume Descriptor");
	return el;
    }
    
    /**
       Partition Maps の検証を行う。
       このメソッドは、verify() からのみコールされる。
       
       @return エラーリストインスタンス。
    */
    private UDF_ErrorList verifyPartMaps() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	final short category = UDF_Error.C_ECMA167;
	byte[] partmaps = partMaps.getData();
	int  maxlen  = partmaps.length;
	int  offset  = 0;
	int  partnum = 0;
	
	
_LOOP1:
	while(offset < maxlen){
	    
	    UDF_PartMap pm = null;
	    int  pmsize = 0;
	    
	    switch(partmaps[offset]){
	    case 0:
		// このフィールドでは定義されないため、残り領域は全て0でなければならない
		for(int i = offset; i < maxlen; i++){
		    
		    if(partmaps[i] != 0){
			
			UDF_Error err = new UDF_Error(category, UDF_Error.L_ERROR);
			err.setMessage("Any unused bytes of Partition Maps field shall be set to #00 bytes.");
			err.setRefer("3/10.6.13");
			
			el.addError(err);
			break _LOOP1;
		    }
		}
		break _LOOP1;
	    case 1:
		pm  = (UDF_part_map1)createElement("UDF_part_map1", null, null);
		pmsize = 6;
		break;
	    case 2:
		pm  = (UDF_part_map2)createElement("UDF_part_map2", null, null);
		pmsize = 64;
		break;
	    default:
		el.addError(new UDF_Error
			    (category, UDF_Error.L_ERROR, "Partition Map Type",
			     "3-255: Reserved for future standardisation.",
			     "3/10.7.1.1", String.valueOf(partmaps[offset]), "0, 1 or 2"));
		break _LOOP1;
	    }
	    
	    UDF_RandomAccessBytes rab = new UDF_RandomAccessBytes
		(env, partmaps, partMaps.getPartMapOffset(), partMaps.getElemPartRefNo(), partMaps.getPartSubno());
	    long readsz = 0;
	    
	    try{
		rab.seek(offset);
		readsz = pm.readFrom(rab);
		el.addError(pm.verify());
	    }
	    catch(Exception e){
		
		if(readsz < pmsize)
		    throw new UDF_DataException(pm, "readFrom error.");
	    }
	    
	    offset += pmsize;
	    partnum++;
	}
	
	// Number of Partition Maps が実際のパーティションマップ数と違う
	if(numberOfPartMaps.getIntValue() != partnum){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Number of Partition Maps(=N_PM)",
			 "This field shall specify the number of Partition Maps recorded in the Partition Maps field.",
			 "3/10.6.9", String.valueOf(numberOfPartMaps.getIntValue()), String.valueOf(partnum)));
	}
	
	el.setGlobalPoint(getGlobalPoint());
	el.setRName("Partition Maps");
	return el;
    }

    final public void recalcLVD(){
	try{
	    UDF_ElementBase[]child = getPartMaps().getChildren();
	    env.part = new UDF_PartMap[child.length];

	    for(int i=0 ; i<child.length ; ++i){
		env.part[i] = (UDF_PartMap)child[i];
		
		// Type1またはSparable PartMapがあれば、それだけ先にrecalcしておく(2005/07/27 by seta)
		if(env.part[i].getPartMapType().getIntValue() == 1)
		    env.part[i].recalcPM(i);
		else if(com.udfv.udf150.UDF_SparablePartMap.class.isAssignableFrom(env.part[i].getClass()))
		    env.part[i].recalcPM(i);
	    }
	    
	    // その後、その他のPartMapをrecalcする
	    for(int i = 0 ; i<child.length ; ++i){
		if(env.part[i].getPartMapType().getIntValue() != 1 &&
		   !com.udfv.udf150.UDF_SparablePartMap.class.isAssignableFrom(env.part[i].getClass()))
		    env.part[i].recalcPM(i);
	    }
	}
	catch(Exception e){
	    ignoreMsg("UDF_desc6#recalc", e);
	}
    }
    public void recalc(short type, UDF_RandomAccess f){
	super.recalc(type, f);

	/*
	  desc6は複数ありうる。そのうちの prevailingだけが信用できるの
	  だが、desc6内だけでは自身が prevailingかどうかわからない。
	  よって、ここでは recalcしない
	*/
    }
//end:
};
