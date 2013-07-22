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
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.exception.*;
import com.udfv.access.*;
import com.udfv.ecma167.*;

/**
File&nbsp;Descriptor&nbsp;を表現するクラス。

*/
public class UDF_desc266 extends UDF_desc261
{
    /**
	（ローカル）クラス名を取得します
    */
    public static String getUDFClassName( ) {
        return "UDF_desc266";
    }

    /**
	@param elem 親
	@param prefix ネームスペース
	@param name 名前
    */
    public UDF_desc266(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }

    /**
	エレメントのサイズを返します。

	@return サイズ
    */
    public int getSize(){
	return 0+getDescTag().getSize()+getICBTag().getSize()+4+4+4+2+1+1+4+8+8+8+getAccessDateAndTime().getSize()+getModificationDateAndTime().getSize()+getCreationDateAndTime().getSize()+getAttrDateAndTime().getSize()+4+getReserved().getSize()+getExtendedAttrICB().getSize()+getStreamDirectoryICB().getSize()+getImplId().getSize()+8+4+4+getExtendedAttr().getSize()+getAllocDesc().getSize();
    }

    /**
	エレメントのサイズを返します。

	@return サイズ
    */
    public long getLongSize(){
	return (long)0+getDescTag().getSize()+getICBTag().getSize()+4+4+4+2+1+1+4+8+8+8+getAccessDateAndTime().getSize()+getModificationDateAndTime().getSize()+getCreationDateAndTime().getSize()+getAttrDateAndTime().getSize()+4+getReserved().getSize()+getExtendedAttrICB().getSize()+getStreamDirectoryICB().getSize()+getImplId().getSize()+8+4+4+getExtendedAttr().getSize()+getAllocDesc().getSize();
    }
    
    private UDF_uint64 objectSize;
    private UDF_timestamp creationDateAndTime;
    private UDF_bytes reserved;
    private UDF_long_ad streamDirectoryICB;

    public UDF_uint64 getObjectSize(){return objectSize;}
    public UDF_timestamp getCreationDateAndTime(){return creationDateAndTime;}
    public UDF_bytes getReserved(){return reserved;}
    public UDF_long_ad getStreamDirectoryICB(){return streamDirectoryICB;}

    public void setObjectSize(UDF_uint64 v){objectSize = v;}
    public void setCreationDateAndTime(UDF_timestamp v){creationDateAndTime = v;}
    public void setReserved(UDF_bytes v){reserved = v;}
    public void setStreamDirectoryICB(UDF_long_ad v){streamDirectoryICB = v;}

    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long rsize = 0;
	preReadHook(f);
	setDescTag((UDF_tag)createElement("UDF_tag", "UDF_desc266", "desc-tag"));
	rsize += getDescTag().readFrom(f);
	setICBTag((UDF_icbtag)createElement("UDF_icbtag", "UDF_desc266", "icb-tag"));
	rsize += getICBTag().readFrom(f);
	setUid((UDF_uint32)createElement("UDF_uint32", "UDF_desc266", "uid"));
	rsize += getUid().readFrom(f);
	setGid((UDF_uint32)createElement("UDF_uint32", "UDF_desc266", "gid"));
	rsize += getGid().readFrom(f);
	setPermissions((UDF_uint32)createElement("UDF_uint32", "UDF_desc266", "permissions"));
	rsize += getPermissions().readFrom(f);
	setFileLinkCount((UDF_uint16)createElement("UDF_uint16", "UDF_desc266", "file-link-count"));
	rsize += getFileLinkCount().readFrom(f);
	setRecordFormat((UDF_uint8)createElement("UDF_uint8", "UDF_desc266", "record-format"));
	rsize += getRecordFormat().readFrom(f);
	setRecordDisplayAttr((UDF_uint8)createElement("UDF_uint8", "UDF_desc266", "record-display-attr"));
	rsize += getRecordDisplayAttr().readFrom(f);
	setRecordLen((UDF_uint32)createElement("UDF_uint32", "UDF_desc266", "record-len"));
	rsize += getRecordLen().readFrom(f);
	setInfoLen((UDF_uint64)createElement("UDF_uint64", "UDF_desc266", "info-len"));
	rsize += getInfoLen().readFrom(f);
	setObjectSize((UDF_uint64)createElement("UDF_uint64", "UDF_desc266", "object-size"));
	rsize += getObjectSize().readFrom(f);
	setLogicalBlocksRecorded((UDF_uint64)createElement("UDF_uint64", "UDF_desc266", "logical-blocks-recorded"));
	rsize += getLogicalBlocksRecorded().readFrom(f);
	setAccessDateAndTime((UDF_timestamp)createElement("UDF_timestamp", "UDF_desc266", "access-date-and-time"));
	rsize += getAccessDateAndTime().readFrom(f);
	setModificationDateAndTime((UDF_timestamp)createElement("UDF_timestamp", "UDF_desc266", "modification-date-and-time"));
	rsize += getModificationDateAndTime().readFrom(f);
	setCreationDateAndTime((UDF_timestamp)createElement("UDF_timestamp", "UDF_desc266", "creation-date-and-time"));
	rsize += getCreationDateAndTime().readFrom(f);
	setAttrDateAndTime((UDF_timestamp)createElement("UDF_timestamp", "UDF_desc266", "attr-date-and-time"));
	rsize += getAttrDateAndTime().readFrom(f);
	setCheckpoint((UDF_uint32)createElement("UDF_uint32", "UDF_desc266", "checkpoint"));
	rsize += getCheckpoint().readFrom(f);
	setReserved((UDF_bytes)createElement("UDF_bytes", "UDF_desc266", "reserved"));
	getReserved().setSize(4);
	rsize += getReserved().readFrom(f);
	setExtendedAttrICB((UDF_long_ad)createElement("UDF_long_ad", "UDF_desc266", "extended-attr-icb"));
	rsize += getExtendedAttrICB().readFrom(f);
	setStreamDirectoryICB((UDF_long_ad)createElement("UDF_long_ad", "UDF_desc266", "stream-directory-icb"));
	rsize += getStreamDirectoryICB().readFrom(f);
	setImplId((UDF_regid)createElement("UDF_regid", "UDF_desc266", "impl-id"));
	rsize += getImplId().readFrom(f);
	setUniqueId((UDF_uint64)createElement("UDF_uint64", "UDF_desc266", "unique-id"));
	rsize += getUniqueId().readFrom(f);
	setLenOfExtendedAttr((UDF_uint32)createElement("UDF_uint32", "UDF_desc266", "len-of-extended-attr"));
	rsize += getLenOfExtendedAttr().readFrom(f);
	setLenOfAllocDesc((UDF_uint32)createElement("UDF_uint32", "UDF_desc266", "len-of-alloc-desc"));
	rsize += getLenOfAllocDesc().readFrom(f);
	setExtendedAttr((UDF_bytes)createElement("UDF_bytes", "UDF_desc266", "extended-attr"));
	getExtendedAttr().setSize(getLenOfExtendedAttr().getIntValue());
	rsize += getExtendedAttr().readFrom(f);
	setAllocDesc((UDF_bytes)createElement("UDF_bytes", "UDF_desc266", "alloc-desc"));
	getAllocDesc().setSize(getLenOfAllocDesc().getIntValue());
	rsize += getAllocDesc().readFrom(f);
	apply( );
	postReadHook(f);
	return rsize;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long wsize = 0;
	wsize += getDescTag().writeTo(f);
	wsize += getICBTag().writeTo(f);
	wsize += getUid().writeTo(f);
	wsize += getGid().writeTo(f);
	wsize += getPermissions().writeTo(f);
	wsize += getFileLinkCount().writeTo(f);
	wsize += getRecordFormat().writeTo(f);
	wsize += getRecordDisplayAttr().writeTo(f);
	wsize += getRecordLen().writeTo(f);
	wsize += getInfoLen().writeTo(f);
	wsize += getObjectSize().writeTo(f);
	wsize += getLogicalBlocksRecorded().writeTo(f);
	wsize += getAccessDateAndTime().writeTo(f);
	wsize += getModificationDateAndTime().writeTo(f);
	wsize += getCreationDateAndTime().writeTo(f);
	wsize += getAttrDateAndTime().writeTo(f);
	wsize += getCheckpoint().writeTo(f);
	wsize += getReserved().writeTo(f);
	wsize += getExtendedAttrICB().writeTo(f);
	wsize += getStreamDirectoryICB().writeTo(f);
	wsize += getImplId().writeTo(f);
	wsize += getUniqueId().writeTo(f);
	wsize += getLenOfExtendedAttr().writeTo(f);
	wsize += getLenOfAllocDesc().writeTo(f);
	wsize += getExtendedAttr().writeTo(f);
	wsize += getAllocDesc().writeTo(f);
	return wsize;
    }

  /**
	XMLのノードを指定して読み込む

	@param n	読み込む先ノード
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
		setDescTag((UDF_tag)createElement("UDF_tag", "UDF_desc266", "desc-tag"));
		getDescTag().readFromXML(child);
	    }
	    else if(name.equals("icb-tag")){
		setICBTag((UDF_icbtag)createElement("UDF_icbtag", "UDF_desc266", "icb-tag"));
		getICBTag().readFromXML(child);
	    }
	    else if(name.equals("uid")){
		setUid((UDF_uint32)createElement("UDF_uint32", "UDF_desc266", "uid"));
		getUid().readFromXML(child);
	    }
	    else if(name.equals("gid")){
		setGid((UDF_uint32)createElement("UDF_uint32", "UDF_desc266", "gid"));
		getGid().readFromXML(child);
	    }
	    else if(name.equals("permissions")){
		setPermissions((UDF_uint32)createElement("UDF_uint32", "UDF_desc266", "permissions"));
		getPermissions().readFromXML(child);
	    }
	    else if(name.equals("file-link-count")){
		setFileLinkCount((UDF_uint16)createElement("UDF_uint16", "UDF_desc266", "file-link-count"));
		getFileLinkCount().readFromXML(child);
	    }
	    else if(name.equals("record-format")){
		setRecordFormat((UDF_uint8)createElement("UDF_uint8", "UDF_desc266", "record-format"));
		getRecordFormat().readFromXML(child);
	    }
	    else if(name.equals("record-display-attr")){
		setRecordDisplayAttr((UDF_uint8)createElement("UDF_uint8", "UDF_desc266", "record-display-attr"));
		getRecordDisplayAttr().readFromXML(child);
	    }
	    else if(name.equals("record-len")){
		setRecordLen((UDF_uint32)createElement("UDF_uint32", "UDF_desc266", "record-len"));
		getRecordLen().readFromXML(child);
	    }
	    else if(name.equals("info-len")){
		setInfoLen((UDF_uint64)createElement("UDF_uint64", "UDF_desc266", "info-len"));
		getInfoLen().readFromXML(child);
	    }
	    else if(name.equals("object-size")){
		setObjectSize((UDF_uint64)createElement("UDF_uint64", "UDF_desc266", "object-size"));
		getObjectSize().readFromXML(child);
	    }
	    else if(name.equals("logical-blocks-recorded")){
		setLogicalBlocksRecorded((UDF_uint64)createElement("UDF_uint64", "UDF_desc266", "logical-blocks-recorded"));
		getLogicalBlocksRecorded().readFromXML(child);
	    }
	    else if(name.equals("access-date-and-time")){
		setAccessDateAndTime((UDF_timestamp)createElement("UDF_timestamp", "UDF_desc266", "access-date-and-time"));
		getAccessDateAndTime().readFromXML(child);
	    }
	    else if(name.equals("modification-date-and-time")){
		setModificationDateAndTime((UDF_timestamp)createElement("UDF_timestamp", "UDF_desc266", "modification-date-and-time"));
		getModificationDateAndTime().readFromXML(child);
	    }
	    else if(name.equals("creation-date-and-time")){
		setCreationDateAndTime((UDF_timestamp)createElement("UDF_timestamp", "UDF_desc266", "creation-date-and-time"));
		getCreationDateAndTime().readFromXML(child);
	    }
	    else if(name.equals("attr-date-and-time")){
		setAttrDateAndTime((UDF_timestamp)createElement("UDF_timestamp", "UDF_desc266", "attr-date-and-time"));
		getAttrDateAndTime().readFromXML(child);
	    }
	    else if(name.equals("checkpoint")){
		setCheckpoint((UDF_uint32)createElement("UDF_uint32", "UDF_desc266", "checkpoint"));
		getCheckpoint().readFromXML(child);
	    }
	    else if(name.equals("reserved")){
		setReserved((UDF_bytes)createElement("UDF_bytes", "UDF_desc266", "reserved"));
		getReserved().setSize(4);
		getReserved().readFromXML(child);
	    }
	    else if(name.equals("extended-attr-icb")){
		setExtendedAttrICB((UDF_long_ad)createElement("UDF_long_ad", "UDF_desc266", "extended-attr-icb"));
		getExtendedAttrICB().readFromXML(child);
	    }
	    else if(name.equals("stream-directory-icb")){
		setStreamDirectoryICB((UDF_long_ad)createElement("UDF_long_ad", "UDF_desc266", "stream-directory-icb"));
		getStreamDirectoryICB().readFromXML(child);
	    }
	    else if(name.equals("impl-id")){
		setImplId((UDF_regid)createElement("UDF_regid", "UDF_desc266", "impl-id"));
		getImplId().readFromXML(child);
	    }
	    else if(name.equals("unique-id")){
		setUniqueId((UDF_uint64)createElement("UDF_uint64", "UDF_desc266", "unique-id"));
		getUniqueId().readFromXML(child);
	    }
	    else if(name.equals("len-of-extended-attr")){
		setLenOfExtendedAttr((UDF_uint32)createElement("UDF_uint32", "UDF_desc266", "len-of-extended-attr"));
		getLenOfExtendedAttr().readFromXML(child);
	    }
	    else if(name.equals("len-of-alloc-desc")){
		setLenOfAllocDesc((UDF_uint32)createElement("UDF_uint32", "UDF_desc266", "len-of-alloc-desc"));
		getLenOfAllocDesc().readFromXML(child);
	    }
	    else if(name.equals("extended-attr")){
		setExtendedAttr((UDF_bytes)createElement("UDF_bytes", "UDF_desc266", "extended-attr"));
		getExtendedAttr().setSize(getLenOfExtendedAttr().getIntValue());
		getExtendedAttr().readFromXML(child);
	    }
	    else if(name.equals("alloc-desc")){
		setAllocDesc((UDF_bytes)createElement("UDF_bytes", "UDF_desc266", "alloc-desc"));
		getAllocDesc().setSize(getLenOfAllocDesc().getIntValue());
		getAllocDesc().readFromXML(child);
	    }
	}
	apply( );
	postReadFromXMLHook(n);
    }

    public void debug(int indent){
	getDescTag().debug(indent + 1);
	getICBTag().debug(indent + 1);
	getUid().debug(indent + 1);
	getGid().debug(indent + 1);
	getPermissions().debug(indent + 1);
	getFileLinkCount().debug(indent + 1);
	getRecordFormat().debug(indent + 1);
	getRecordDisplayAttr().debug(indent + 1);
	getRecordLen().debug(indent + 1);
	getInfoLen().debug(indent + 1);
	getObjectSize().debug(indent + 1);
	getLogicalBlocksRecorded().debug(indent + 1);
	getAccessDateAndTime().debug(indent + 1);
	getModificationDateAndTime().debug(indent + 1);
	getCreationDateAndTime().debug(indent + 1);
	getAttrDateAndTime().debug(indent + 1);
	getCheckpoint().debug(indent + 1);
	getReserved().debug(indent + 1);
	getExtendedAttrICB().debug(indent + 1);
	getStreamDirectoryICB().debug(indent + 1);
	getImplId().debug(indent + 1);
	getUniqueId().debug(indent + 1);
	getLenOfExtendedAttr().debug(indent + 1);
	getLenOfAllocDesc().debug(indent + 1);
	getExtendedAttr().debug(indent + 1);
	getAllocDesc().debug(indent + 1);
    }

    public int getFixedTagId() { return 266; }

    /**
	初期値を設定する
    */
    public void setDefaultValue(){
	super.setDefaultValue();

	objectSize = (UDF_uint64)createElement("UDF_uint64", "", "object-size");
	objectSize.setDefaultValue();

	creationDateAndTime = (UDF_timestamp)createElement("UDF_timestamp", "", "creation-date-and-time");
	creationDateAndTime.setDefaultValue();

	reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
	reserved.setDefaultValue();
	reserved.setSize(4);

	streamDirectoryICB = (UDF_long_ad)createElement("UDF_long_ad", "", "stream-directory-icb");
	streamDirectoryICB.setDefaultValue();

	apply( );
	postSetDefaultValueHook();
    }

    /**
	複製する
    */
    public UDF_Element duplicateElement(){
	UDF_desc266 dup = new UDF_desc266(this, null, getName());
	dup.setDefaultValue();
	dup.duplicateHook(this);
	dup.setDescTag((UDF_tag)getDescTag().duplicateElement());
	dup.setICBTag((UDF_icbtag)getICBTag().duplicateElement());
	dup.setUid((UDF_uint32)getUid().duplicateElement());
	dup.setGid((UDF_uint32)getGid().duplicateElement());
	dup.setPermissions((UDF_uint32)getPermissions().duplicateElement());
	dup.setFileLinkCount((UDF_uint16)getFileLinkCount().duplicateElement());
	dup.setRecordFormat((UDF_uint8)getRecordFormat().duplicateElement());
	dup.setRecordDisplayAttr((UDF_uint8)getRecordDisplayAttr().duplicateElement());
	dup.setRecordLen((UDF_uint32)getRecordLen().duplicateElement());
	dup.setInfoLen((UDF_uint64)getInfoLen().duplicateElement());
	dup.setLogicalBlocksRecorded((UDF_uint64)getLogicalBlocksRecorded().duplicateElement());
	dup.setAccessDateAndTime((UDF_timestamp)getAccessDateAndTime().duplicateElement());
	dup.setModificationDateAndTime((UDF_timestamp)getModificationDateAndTime().duplicateElement());
	dup.setAttrDateAndTime((UDF_timestamp)getAttrDateAndTime().duplicateElement());
	dup.setCheckpoint((UDF_uint32)getCheckpoint().duplicateElement());
	dup.setExtendedAttrICB((UDF_long_ad)getExtendedAttrICB().duplicateElement());
	dup.setImplId((UDF_regid)getImplId().duplicateElement());
	dup.setUniqueId((UDF_uint64)getUniqueId().duplicateElement());
	dup.setLenOfExtendedAttr((UDF_uint32)getLenOfExtendedAttr().duplicateElement());
	dup.setLenOfAllocDesc((UDF_uint32)getLenOfAllocDesc().duplicateElement());
	dup.setExtendedAttr((UDF_bytes)getExtendedAttr().duplicateElement());
	dup.setAllocDesc((UDF_bytes)getAllocDesc().duplicateElement());

	dup.setObjectSize((UDF_uint64)objectSize.duplicateElement());
	dup.setCreationDateAndTime((UDF_timestamp)creationDateAndTime.duplicateElement());
	dup.setReserved((UDF_bytes)reserved.duplicateElement());
	dup.setStreamDirectoryICB((UDF_long_ad)streamDirectoryICB.duplicateElement());
	apply();

	return dup;
    }

    /**
        子の UDF Element の Node を append しなおす。

    */
    private void apply( ) {

	removeAllChildren();
	appendChild(getDescTag());
	appendChild(getICBTag());
	appendChild(getUid());
	appendChild(getGid());
	appendChild(getPermissions());
	appendChild(getFileLinkCount());
	appendChild(getRecordFormat());
	appendChild(getRecordDisplayAttr());
	appendChild(getRecordLen());
	appendChild(getInfoLen());
	appendChild(getObjectSize());		//
	appendChild(getLogicalBlocksRecorded());
	appendChild(getAccessDateAndTime());
	appendChild(getModificationDateAndTime());
	appendChild(getCreationDateAndTime());	//
	appendChild(getAttrDateAndTime());
	appendChild(getCheckpoint());
	appendChild(getReserved());		//
	appendChild(getExtendedAttrICB());
	appendChild(getStreamDirectoryICB());	//
	appendChild(getImplId());
	appendChild(getUniqueId());
	appendChild(getLenOfExtendedAttr());
	appendChild(getLenOfAllocDesc());
	appendChild(getExtendedAttr());
	appendChild(getAllocDesc());
    }

    /**
      検証。
    */
    public UDF_ErrorList verify() throws UDF_Exception {

	UDF_RandomAccess ra = env.f;
	UDF_ErrorList elist = new UDF_ErrorList();
	UDF_Error err;

	short category = UDF_Error.C_ECMA167;
	
	
	//　IDの判定　//
	err = verifyId();
	if (err.isError()) {
	    err.setRefer("4/4.17.1");
	    elist.addError(err);
	}
	
	// File Entry 部の検証
	elist.addError(super.verifyFE());
	
	
	//Object Size
	long information_length = getInfoLen().getLongValue();
	long object_size = getObjectSize().getLongValue();
	if (getStreamDirectoryICB().getLen() == 0) {
	    if (object_size != information_length) {
		err = new UDF_Error(UDF_Error.C_ECMA167, UDF_Error.L_ERROR, "Object Size");
		err.setRefer("4/14.17.11");
		err.setMessage("If this file has no streams, the Object Size shall be equal to the Information Length.");
		elist.addError(err);
	    }
	}
	else {
	    //　今後の課題　全てのストリームの長さを足し合わせたものと同じでないといけません　//
	    if (false) {
		err = new UDF_Error(UDF_Error.C_ECMA167, UDF_Error.L_ERROR, "Object Size");
		err.setRefer("4/14.17.11");
		err.setMessage("The sum of all Information Length fields for all streams of a file (including the default stream).");
		elist.addError(err);
	    }
	}


	//Creation Date and Time
	elist.addError(getCreationDateAndTime().verify("Creation Date and Time"));


	//Stream Directory ICB
	//4/14.17.19
	//no check


	elist.setGlobalPoint(getGlobalPoint());
	elist.setRName("Extended File Entry");

	return elist;
    }
};
