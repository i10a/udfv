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
Logical&nbsp;Volume&nbsp;Integrity&nbsp;Descriptor&nbsp;を表現するクラス。<br/>


<dl>

<dt>フィールド一覧</dt>
<dd>
<table border="1">
<tr><td>メンバ</td><td>型</td><td>サイズ</td></tr>
<tr><td><b>DescTag</b></td><td><b>UDF_tag</b></td><td><i>descTag.getSize()</i></td></tr>
<tr><td><b>RecordingDateAndTime</b></td><td><b>UDF_timestamp</b></td><td><i>recordingDateAndTime.getSize()</i></td></tr>
<tr><td><b>IntegrityType</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>NextIntegrityExtent</b></td><td><b>UDF_extent_ad</b></td><td><i>nextIntegrityExtent.getSize()</i></td></tr>
<tr><td><b>LogicalVolContentsUse</b></td><td><b>UDF_bytes</b></td><td><i>32</i></td></tr>
<tr><td><b>NumberOfPart</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>LenOfImplUse</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>FreeSpaceTable</b></td><td><b>UDF_bytes</b></td><td><i>getNumberOfPart().getIntValue()*4</i></td></tr>
<tr><td><b>SizeTable</b></td><td><b>UDF_bytes</b></td><td><i>getNumberOfPart().getIntValue()*4</i></td></tr>
<tr><td><b>ImplUse</b></td><td><b>UDF_bytes</b></td><td><i>getLenOfImplUse().getIntValue()</i></td></tr>
</table>
</dd>
</dl>
*/
public class UDF_desc9 extends UDF_CrcDesc implements UDF_VolDesc
{

    /**
	このオブジェクトのパッケージ名を除いたクラス名を取得する。

	@return クラス名
    */
    public static String getUDFClassName( ) {
        return "UDF_desc9";
    }

    /**
	コンストラクタ。

	@param elem   親エレメント。
	@param prefix ネームスペース。
	@param name   エレメント名。
    */
    public UDF_desc9(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }
    /**
	このUDFエレメントのサイズを返す。

	@return UDFエレメントのサイズ。
    */
    public int getSize(){
	return 0+descTag.getSize()+recordingDateAndTime.getSize()+4+nextIntegrityExtent.getSize()+logicalVolContentsUse.getSize()+4+4+freeSpaceTable.getSize()+sizeTable.getSize()+implUse.getSize();
    }

    /**
	このUDFエレメントのサイズをlong 型で返す。

	@return UDFエレメントのサイズ。
    */
    public long getLongSize(){
	return (long)0+descTag.getSize()+recordingDateAndTime.getSize()+4+nextIntegrityExtent.getSize()+logicalVolContentsUse.getSize()+4+4+freeSpaceTable.getSize()+sizeTable.getSize()+implUse.getSize();
    }
    private UDF_tag descTag;
    private UDF_timestamp recordingDateAndTime;
    private UDF_uint32 integrityType;
    private UDF_extent_ad nextIntegrityExtent;
    private UDF_bytes logicalVolContentsUse;
    private UDF_uint32 numberOfPart;
    private UDF_uint32 lenOfImplUse;
    private UDF_bytes freeSpaceTable;
    private UDF_bytes sizeTable;
    private UDF_bytes implUse;

    /**
	descTagを取得する。

	@return 取得したdescTag を返す。
    */
    public UDF_tag getDescTag(){return descTag;}
    /**
	recordingDateAndTimeを取得する。

	@return 取得したrecordingDateAndTime を返す。
    */
    public UDF_timestamp getRecordingDateAndTime(){return recordingDateAndTime;}
    /**
	integrityTypeを取得する。

	@return 取得したintegrityType を返す。
    */
    public UDF_uint32 getIntegrityType(){return integrityType;}
    /**
	nextIntegrityExtentを取得する。

	@return 取得したnextIntegrityExtent を返す。
    */
    public UDF_extent_ad getNextIntegrityExtent(){return nextIntegrityExtent;}
    /**
	logicalVolContentsUseを取得する。

	@return 取得したlogicalVolContentsUse を返す。
    */
    public UDF_bytes getLogicalVolContentsUse(){return logicalVolContentsUse;}
    /**
	numberOfPartを取得する。

	@return 取得したnumberOfPart を返す。
    */
    public UDF_uint32 getNumberOfPart(){return numberOfPart;}
    /**
	lenOfImplUseを取得する。

	@return 取得したlenOfImplUse を返す。
    */
    public UDF_uint32 getLenOfImplUse(){return lenOfImplUse;}
    /**
	freeSpaceTableを取得する。

	@return 取得したfreeSpaceTable を返す。
    */
    public UDF_bytes getFreeSpaceTable(){return freeSpaceTable;}
    /**
	sizeTableを取得する。

	@return 取得したsizeTable を返す。
    */
    public UDF_bytes getSizeTable(){return sizeTable;}
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
	recordingDateAndTimeを設定する。

	@param	v 設定する値。
    */
    public void setRecordingDateAndTime(UDF_timestamp v){replaceChild(v, recordingDateAndTime); recordingDateAndTime = v;}
    /**
	integrityTypeを設定する。

	@param	v 設定する値。
    */
    public void setIntegrityType(UDF_uint32 v){replaceChild(v, integrityType); integrityType = v;}
    /**
	nextIntegrityExtentを設定する。

	@param	v 設定する値。
    */
    public void setNextIntegrityExtent(UDF_extent_ad v){replaceChild(v, nextIntegrityExtent); nextIntegrityExtent = v;}
    /**
	logicalVolContentsUseを設定する。

	@param	v 設定する値。
    */
    public void setLogicalVolContentsUse(UDF_bytes v){replaceChild(v, logicalVolContentsUse); logicalVolContentsUse = v;}
    /**
	numberOfPartを設定する。

	@param	v 設定する値。
    */
    public void setNumberOfPart(UDF_uint32 v){replaceChild(v, numberOfPart); numberOfPart = v;}
    /**
	lenOfImplUseを設定する。

	@param	v 設定する値。
    */
    public void setLenOfImplUse(UDF_uint32 v){replaceChild(v, lenOfImplUse); lenOfImplUse = v;}
    /**
	freeSpaceTableを設定する。

	@param	v 設定する値。
    */
    public void setFreeSpaceTable(UDF_bytes v){replaceChild(v, freeSpaceTable); freeSpaceTable = v;}
    /**
	sizeTableを設定する。

	@param	v 設定する値。
    */
    public void setSizeTable(UDF_bytes v){replaceChild(v, sizeTable); sizeTable = v;}
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
	recordingDateAndTime = (UDF_timestamp)createElement("UDF_timestamp", "", "recording-date-and-time");
	rsize += recordingDateAndTime.readFrom(f);
	integrityType = (UDF_uint32)createElement("UDF_uint32", "", "integrity-type");
	rsize += integrityType.readFrom(f);
	nextIntegrityExtent = (UDF_extent_ad)createElement("UDF_extent_ad", "", "next-integrity-extent");
	rsize += nextIntegrityExtent.readFrom(f);
	logicalVolContentsUse = (UDF_bytes)createElement("UDF_bytes", "", "logical-vol-contents-use");
	logicalVolContentsUse.setSize(32);
	rsize += logicalVolContentsUse.readFrom(f);
	numberOfPart = (UDF_uint32)createElement("UDF_uint32", "", "number-of-part");
	rsize += numberOfPart.readFrom(f);
	lenOfImplUse = (UDF_uint32)createElement("UDF_uint32", "", "len-of-impl-use");
	rsize += lenOfImplUse.readFrom(f);
	freeSpaceTable = (UDF_bytes)createElement("UDF_bytes", "", "free-space-table");
	freeSpaceTable.setSize(getNumberOfPart().getIntValue()*4);
	rsize += freeSpaceTable.readFrom(f);
	sizeTable = (UDF_bytes)createElement("UDF_bytes", "", "size-table");
	sizeTable.setSize(getNumberOfPart().getIntValue()*4);
	rsize += sizeTable.readFrom(f);
	implUse = (UDF_bytes)createElement("UDF_bytes", "", "impl-use");
	implUse.setSize(getLenOfImplUse().getIntValue());
	rsize += implUse.readFrom(f);
	apply( );
	postReadHook(f);
	return rsize;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long wsize = 0;
	wsize += descTag.writeTo(f);
	wsize += recordingDateAndTime.writeTo(f);
	wsize += integrityType.writeTo(f);
	wsize += nextIntegrityExtent.writeTo(f);
	wsize += logicalVolContentsUse.writeTo(f);
	wsize += numberOfPart.writeTo(f);
	wsize += lenOfImplUse.writeTo(f);
	wsize += freeSpaceTable.writeTo(f);
	wsize += sizeTable.writeTo(f);
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
	    else if(name.equals("recording-date-and-time")){
		recordingDateAndTime = (UDF_timestamp)createElement("UDF_timestamp", "", "recording-date-and-time");
		recordingDateAndTime.readFromXML(child);
	    }
	    else if(name.equals("integrity-type")){
		integrityType = (UDF_uint32)createElement("UDF_uint32", "", "integrity-type");
		integrityType.readFromXML(child);
	    }
	    else if(name.equals("next-integrity-extent")){
		nextIntegrityExtent = (UDF_extent_ad)createElement("UDF_extent_ad", "", "next-integrity-extent");
		nextIntegrityExtent.readFromXML(child);
	    }
	    else if(name.equals("logical-vol-contents-use")){
		logicalVolContentsUse = (UDF_bytes)createElement("UDF_bytes", "", "logical-vol-contents-use");
		logicalVolContentsUse.setSize(32);
		logicalVolContentsUse.readFromXML(child);
	    }
	    else if(name.equals("number-of-part")){
		numberOfPart = (UDF_uint32)createElement("UDF_uint32", "", "number-of-part");
		numberOfPart.readFromXML(child);
	    }
	    else if(name.equals("len-of-impl-use")){
		lenOfImplUse = (UDF_uint32)createElement("UDF_uint32", "", "len-of-impl-use");
		lenOfImplUse.readFromXML(child);
	    }
	    else if(name.equals("free-space-table")){
		freeSpaceTable = (UDF_bytes)createElement("UDF_bytes", "", "free-space-table");
		freeSpaceTable.setSize(getNumberOfPart().getIntValue()*4);
		freeSpaceTable.readFromXML(child);
	    }
	    else if(name.equals("size-table")){
		sizeTable = (UDF_bytes)createElement("UDF_bytes", "", "size-table");
		sizeTable.setSize(getNumberOfPart().getIntValue()*4);
		sizeTable.readFromXML(child);
	    }
	    else if(name.equals("impl-use")){
		implUse = (UDF_bytes)createElement("UDF_bytes", "", "impl-use");
		implUse.setSize(getLenOfImplUse().getIntValue());
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
	recordingDateAndTime = (UDF_timestamp)createElement("UDF_timestamp", "", "recording-date-and-time");
	recordingDateAndTime.setDefaultValue();
	integrityType = (UDF_uint32)createElement("UDF_uint32", "", "integrity-type");
	integrityType.setDefaultValue();
	nextIntegrityExtent = (UDF_extent_ad)createElement("UDF_extent_ad", "", "next-integrity-extent");
	nextIntegrityExtent.setDefaultValue();
	logicalVolContentsUse = (UDF_bytes)createElement("UDF_bytes", "", "logical-vol-contents-use");
	logicalVolContentsUse.setSize(32);
	logicalVolContentsUse.setDefaultValue();
	numberOfPart = (UDF_uint32)createElement("UDF_uint32", "", "number-of-part");
	numberOfPart.setDefaultValue();
	lenOfImplUse = (UDF_uint32)createElement("UDF_uint32", "", "len-of-impl-use");
	lenOfImplUse.setDefaultValue();
	freeSpaceTable = (UDF_bytes)createElement("UDF_bytes", "", "free-space-table");
	freeSpaceTable.setSize(getNumberOfPart().getIntValue()*4);
	freeSpaceTable.setDefaultValue();
	sizeTable = (UDF_bytes)createElement("UDF_bytes", "", "size-table");
	sizeTable.setSize(getNumberOfPart().getIntValue()*4);
	sizeTable.setDefaultValue();
	implUse = (UDF_bytes)createElement("UDF_bytes", "", "impl-use");
	implUse.setSize(getLenOfImplUse().getIntValue());
	implUse.setDefaultValue();

	apply( );
	postSetDefaultValueHook();
    }


    /**
	複製する
    */
    public UDF_Element duplicateElement(){
	UDF_desc9 dup = new UDF_desc9(this, null, getName());
	dup.setDefaultValue();
	dup.duplicateHook(this);
	dup.setDescTag((UDF_tag)descTag.duplicateElement());
	dup.setRecordingDateAndTime((UDF_timestamp)recordingDateAndTime.duplicateElement());
	dup.setIntegrityType((UDF_uint32)integrityType.duplicateElement());
	dup.setNextIntegrityExtent((UDF_extent_ad)nextIntegrityExtent.duplicateElement());
	dup.setLogicalVolContentsUse((UDF_bytes)logicalVolContentsUse.duplicateElement());
	dup.setNumberOfPart((UDF_uint32)numberOfPart.duplicateElement());
	dup.setLenOfImplUse((UDF_uint32)lenOfImplUse.duplicateElement());
	dup.setFreeSpaceTable((UDF_bytes)freeSpaceTable.duplicateElement());
	dup.setSizeTable((UDF_bytes)sizeTable.duplicateElement());
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
	appendChild(recordingDateAndTime);
	appendChild(integrityType);
	appendChild(nextIntegrityExtent);
	appendChild(logicalVolContentsUse);
	appendChild(numberOfPart);
	appendChild(lenOfImplUse);
	appendChild(freeSpaceTable);
	appendChild(sizeTable);
	appendChild(implUse);
    }

    public String getInfo(int indent){
	String a = "";
	a += UDF_Util.repeat(' ', indent);
	a += descTag.getInfo(indent + 1);
	a += recordingDateAndTime.getInfo(indent + 1);
	a += integrityType.getInfo(indent + 1);
	a += nextIntegrityExtent.getInfo(indent + 1);
	a += logicalVolContentsUse.getInfo(indent + 1);
	a += numberOfPart.getInfo(indent + 1);
	a += lenOfImplUse.getInfo(indent + 1);
	a += freeSpaceTable.getInfo(indent + 1);
	a += sizeTable.getInfo(indent + 1);
	a += implUse.getInfo(indent + 1);
      return a;
    }
    public void debug(int indent){
	System.err.print(UDF_Util.repeat(' ', indent));

	String info = " gp=" + getGlobalPoint();
	info += " pn=" + getElemPartRefNo();
	info += " sn=" + getPartSubno();
	System.err.println(getName() + ": (" + hashCode() + ")" + info);	descTag.debug(indent + 1);
	recordingDateAndTime.debug(indent + 1);
	integrityType.debug(indent + 1);
	nextIntegrityExtent.debug(indent + 1);
	logicalVolContentsUse.debug(indent + 1);
	numberOfPart.debug(indent + 1);
	lenOfImplUse.debug(indent + 1);
	freeSpaceTable.debug(indent + 1);
	sizeTable.debug(indent + 1);
	implUse.debug(indent + 1);
    }
//begin:add your code here
    public int getFixedTagId() { return 9; }
    
    public void postReadHook(UDF_RandomAccess f) throws IOException,UDF_Exception{
	/*
	  ECMA167 3.1
	  if the volume is recorded according to Part3. the Logical
	  Volume Header descriptor shall be recorded in the Logical
	  Volume Contents Use field of the prevailng Logical Volume
	  Integrity Descriptor for the logical volume */

	UDF_RandomAccessBuffer rae = logicalVolContentsUse.genRandomAccessBytes();
	UDF_LogicalVolHeaderDesc d = (UDF_LogicalVolHeaderDesc)createElement("UDF_LogicalVolHeaderDesc", null, null);
	logicalVolContentsUse.readFromAndReplaceChild(d);
    }
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	final short category = UDF_Error.C_ECMA167;
	UDF_Error ret;


	el.addError(super.verify());  // Descriptor Tag

	ret = verifyId();
	if(ret.isError()){
    
	    ret.setRefer("3/10.10.1");
	    el.addError(ret);
	}
	
	el.addError(recordingDateAndTime.verify("Recording Date and Time"));
	
	// Integrity Type は０または１
	int integrity = integrityType.getIntValue();
	if(integrity != 0 && integrity != 1){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Integrity Type",
			 "This field shall specify the type of Integrity Descriptor. The types are shown in figure 3/23.",
			 "3/10.10.3", String.valueOf(integrity), "0 or 1"));
	}
	
	el.addError(nextIntegrityExtent.verify("Next Integrity Extent"));
	
	// Free Space Table のサイズがN_Px4 でない
	int num_4 = numberOfPart.getIntValue() * 4;
	if(freeSpaceTable.getSize() != num_4){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Free Space Table",
			 "This field shall contain N_P values, each recorded as a Uint32, " + 
			 "recorded contiguously starting at the first byte of the field.",
			 "3/10.10.8",
			 String.valueOf("length=" + freeSpaceTable.getSize()), String.valueOf("N_PM*4=" + num_4 * 4)
			));
	}
	
	// Size Table のサイズがN_Px4 でない
	if(sizeTable.getSize() != num_4){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Size Table",
			 "This field shall contain N_P values, each recorded as a Uint32, " + 
			 "recorded contiguously starting at the first byte of the field.",
			 "3/10.10.9",
			 String.valueOf("length=" + sizeTable.getSize()), String.valueOf("N_PM*4=" + num_4 * 4)));
	}
	
	// Implementation Use のサイズがL_IU でない
	if(implUse.getSize() != lenOfImplUse.getIntValue()){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Length of Implementation Use(=L_IU)",
			 "This field shall specify the length, in bytes, of the Implementation Use field.",
			 "3/10.10.7, 3/10.10.10",
			 String.valueOf("length=" + implUse.getSize()),
			 String.valueOf(" L_IU =" + lenOfImplUse.getIntValue())));
	}
	
	
	if(0 < lenOfImplUse.getIntValue()){
	    
	    final int REGID_SIZE = 32;

	    byte[] implusebuf = implUse.getData();
	    UDF_RandomAccessBytes rab = new UDF_RandomAccessBytes
		(env, implusebuf, implUse.getPartMapOffset(), implUse.getElemPartRefNo(), implUse.getPartSubno());
	    UDF_regid regid = (UDF_regid)createElement("UDF_regid", null, null);
	    long readsz = 0;
	    
	    
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
	
	el.setGlobalPoint(getGlobalPoint());
	el.setRName("Logical Volume Integrity Descriptor");
	return el;
    }
    
    /**
       Number of Partitions の値を検証します。
       エラーはL_ERRORレベル、原因・メッセージ・参照・記録値が設定されます。
       
       @param num  検証する値。
       @return 検証をパスしなかった場合、エラーインスタンスが返ります。
               そうでない場合、NOERR が返ります。
    */
    public UDF_Error verifyN_P(int num){
	
	if(numberOfPart.getIntValue() != num){
	    
	    return new UDF_Error(UDF_Error.C_ECMA167, UDF_Error.L_ERROR, "Number of Partitions(=N_P)",
				 "This field shall specify the number of partitions in the associated logical volume.",
				 "3/10.10.6", String.valueOf(numberOfPart.getIntValue()), "");
	}
	else
	    return new UDF_Error();
    }
    public void recalc(short type, UDF_RandomAccess f){
	super.recalc(type, f);
	if(type == RECALC_ENV){
	}
    }


    public void postVolReadHook(UDF_RandomAccess f) throws IOException,UDF_Exception{
    }

//end:
};
