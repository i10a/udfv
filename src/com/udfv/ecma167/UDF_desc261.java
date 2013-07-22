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
File&nbsp;Descriptor&nbsp;を表現するクラス。<br/>


<dl>

<dt>フィールド一覧</dt>
<dd>
<table border="1">
<tr><td>メンバ</td><td>型</td><td>サイズ</td></tr>
<tr><td><b>DescTag</b></td><td><b>UDF_tag</b></td><td><i>descTag.getSize()</i></td></tr>
<tr><td><b>ICBTag</b></td><td><b>UDF_icbtag</b></td><td><i>iCBTag.getSize()</i></td></tr>
<tr><td><b>Uid</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>Gid</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>Permissions</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>FileLinkCount</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td></tr>
<tr><td><b>RecordFormat</b></td><td><b>UDF_uint8</b></td><td><i>1</i></td></tr>
<tr><td><b>RecordDisplayAttr</b></td><td><b>UDF_uint8</b></td><td><i>1</i></td></tr>
<tr><td><b>RecordLen</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>InfoLen</b></td><td><b>UDF_uint64</b></td><td><i>8</i></td></tr>
<tr><td><b>LogicalBlocksRecorded</b></td><td><b>UDF_uint64</b></td><td><i>8</i></td></tr>
<tr><td><b>AccessDateAndTime</b></td><td><b>UDF_timestamp</b></td><td><i>accessDateAndTime.getSize()</i></td></tr>
<tr><td><b>ModificationDateAndTime</b></td><td><b>UDF_timestamp</b></td><td><i>modificationDateAndTime.getSize()</i></td></tr>
<tr><td><b>AttrDateAndTime</b></td><td><b>UDF_timestamp</b></td><td><i>attrDateAndTime.getSize()</i></td></tr>
<tr><td><b>Checkpoint</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>ExtendedAttrICB</b></td><td><b>UDF_long_ad</b></td><td><i>extendedAttrICB.getSize()</i></td></tr>
<tr><td><b>ImplId</b></td><td><b>UDF_regid</b></td><td><i>implId.getSize()</i></td></tr>
<tr><td><b>UniqueId</b></td><td><b>UDF_uint64</b></td><td><i>8</i></td></tr>
<tr><td><b>LenOfExtendedAttr</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>LenOfAllocDesc</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>ExtendedAttr</b></td><td><b>UDF_bytes</b></td><td><i>getLenOfExtendedAttr().getIntValue()</i></td></tr>
<tr><td><b>AllocDesc</b></td><td><b>UDF_bytes</b></td><td><i>getLenOfAllocDesc().getIntValue()</i></td></tr>
</table>
</dd>
</dl>
*/
public class UDF_desc261 extends UDF_FEDesc 
{

    /**
	このオブジェクトのパッケージ名を除いたクラス名を取得する。

	@return クラス名
    */
    public static String getUDFClassName( ) {
        return "UDF_desc261";
    }

    /**
	コンストラクタ。

	@param elem   親エレメント。
	@param prefix ネームスペース。
	@param name   エレメント名。
    */
    public UDF_desc261(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }
    /**
	このUDFエレメントのサイズを返す。

	@return UDFエレメントのサイズ。
    */
    public int getSize(){
	return 0+descTag.getSize()+iCBTag.getSize()+4+4+4+2+1+1+4+8+8+accessDateAndTime.getSize()+modificationDateAndTime.getSize()+attrDateAndTime.getSize()+4+extendedAttrICB.getSize()+implId.getSize()+8+4+4+extendedAttr.getSize()+allocDesc.getSize();
    }

    /**
	このUDFエレメントのサイズをlong 型で返す。

	@return UDFエレメントのサイズ。
    */
    public long getLongSize(){
	return (long)0+descTag.getSize()+iCBTag.getSize()+4+4+4+2+1+1+4+8+8+accessDateAndTime.getSize()+modificationDateAndTime.getSize()+attrDateAndTime.getSize()+4+extendedAttrICB.getSize()+implId.getSize()+8+4+4+extendedAttr.getSize()+allocDesc.getSize();
    }
    private UDF_tag descTag;
    private UDF_icbtag iCBTag;
    private UDF_uint32 uid;
    private UDF_uint32 gid;
    private UDF_uint32 permissions;
    private UDF_uint16 fileLinkCount;
    private UDF_uint8 recordFormat;
    private UDF_uint8 recordDisplayAttr;
    private UDF_uint32 recordLen;
    private UDF_uint64 infoLen;
    private UDF_uint64 logicalBlocksRecorded;
    private UDF_timestamp accessDateAndTime;
    private UDF_timestamp modificationDateAndTime;
    private UDF_timestamp attrDateAndTime;
    private UDF_uint32 checkpoint;
    private UDF_long_ad extendedAttrICB;
    private UDF_regid implId;
    private UDF_uint64 uniqueId;
    private UDF_uint32 lenOfExtendedAttr;
    private UDF_uint32 lenOfAllocDesc;
    private UDF_bytes extendedAttr;
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
	uidを取得する。

	@return 取得したuid を返す。
    */
    public UDF_uint32 getUid(){return uid;}
    /**
	gidを取得する。

	@return 取得したgid を返す。
    */
    public UDF_uint32 getGid(){return gid;}
    /**
	permissionsを取得する。

	@return 取得したpermissions を返す。
    */
    public UDF_uint32 getPermissions(){return permissions;}
    /**
	fileLinkCountを取得する。

	@return 取得したfileLinkCount を返す。
    */
    public UDF_uint16 getFileLinkCount(){return fileLinkCount;}
    /**
	recordFormatを取得する。

	@return 取得したrecordFormat を返す。
    */
    public UDF_uint8 getRecordFormat(){return recordFormat;}
    /**
	recordDisplayAttrを取得する。

	@return 取得したrecordDisplayAttr を返す。
    */
    public UDF_uint8 getRecordDisplayAttr(){return recordDisplayAttr;}
    /**
	recordLenを取得する。

	@return 取得したrecordLen を返す。
    */
    public UDF_uint32 getRecordLen(){return recordLen;}
    /**
	infoLenを取得する。

	@return 取得したinfoLen を返す。
    */
    public UDF_uint64 getInfoLen(){return infoLen;}
    /**
	logicalBlocksRecordedを取得する。

	@return 取得したlogicalBlocksRecorded を返す。
    */
    public UDF_uint64 getLogicalBlocksRecorded(){return logicalBlocksRecorded;}
    /**
	accessDateAndTimeを取得する。

	@return 取得したaccessDateAndTime を返す。
    */
    public UDF_timestamp getAccessDateAndTime(){return accessDateAndTime;}
    /**
	modificationDateAndTimeを取得する。

	@return 取得したmodificationDateAndTime を返す。
    */
    public UDF_timestamp getModificationDateAndTime(){return modificationDateAndTime;}
    /**
	attrDateAndTimeを取得する。

	@return 取得したattrDateAndTime を返す。
    */
    public UDF_timestamp getAttrDateAndTime(){return attrDateAndTime;}
    /**
	checkpointを取得する。

	@return 取得したcheckpoint を返す。
    */
    public UDF_uint32 getCheckpoint(){return checkpoint;}
    /**
	extendedAttrICBを取得する。

	@return 取得したextendedAttrICB を返す。
    */
    public UDF_long_ad getExtendedAttrICB(){return extendedAttrICB;}
    /**
	implIdを取得する。

	@return 取得したimplId を返す。
    */
    public UDF_regid getImplId(){return implId;}
    /**
	uniqueIdを取得する。

	@return 取得したuniqueId を返す。
    */
    public UDF_uint64 getUniqueId(){return uniqueId;}
    /**
	lenOfExtendedAttrを取得する。

	@return 取得したlenOfExtendedAttr を返す。
    */
    public UDF_uint32 getLenOfExtendedAttr(){return lenOfExtendedAttr;}
    /**
	lenOfAllocDescを取得する。

	@return 取得したlenOfAllocDesc を返す。
    */
    public UDF_uint32 getLenOfAllocDesc(){return lenOfAllocDesc;}
    /**
	extendedAttrを取得する。

	@return 取得したextendedAttr を返す。
    */
    public UDF_bytes getExtendedAttr(){return extendedAttr;}
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
	uidを設定する。

	@param	v 設定する値。
    */
    public void setUid(UDF_uint32 v){replaceChild(v, uid); uid = v;}
    /**
	gidを設定する。

	@param	v 設定する値。
    */
    public void setGid(UDF_uint32 v){replaceChild(v, gid); gid = v;}
    /**
	permissionsを設定する。

	@param	v 設定する値。
    */
    public void setPermissions(UDF_uint32 v){replaceChild(v, permissions); permissions = v;}
    /**
	fileLinkCountを設定する。

	@param	v 設定する値。
    */
    public void setFileLinkCount(UDF_uint16 v){replaceChild(v, fileLinkCount); fileLinkCount = v;}
    /**
	recordFormatを設定する。

	@param	v 設定する値。
    */
    public void setRecordFormat(UDF_uint8 v){replaceChild(v, recordFormat); recordFormat = v;}
    /**
	recordDisplayAttrを設定する。

	@param	v 設定する値。
    */
    public void setRecordDisplayAttr(UDF_uint8 v){replaceChild(v, recordDisplayAttr); recordDisplayAttr = v;}
    /**
	recordLenを設定する。

	@param	v 設定する値。
    */
    public void setRecordLen(UDF_uint32 v){replaceChild(v, recordLen); recordLen = v;}
    /**
	infoLenを設定する。

	@param	v 設定する値。
    */
    public void setInfoLen(UDF_uint64 v){replaceChild(v, infoLen); infoLen = v;}
    /**
	logicalBlocksRecordedを設定する。

	@param	v 設定する値。
    */
    public void setLogicalBlocksRecorded(UDF_uint64 v){replaceChild(v, logicalBlocksRecorded); logicalBlocksRecorded = v;}
    /**
	accessDateAndTimeを設定する。

	@param	v 設定する値。
    */
    public void setAccessDateAndTime(UDF_timestamp v){replaceChild(v, accessDateAndTime); accessDateAndTime = v;}
    /**
	modificationDateAndTimeを設定する。

	@param	v 設定する値。
    */
    public void setModificationDateAndTime(UDF_timestamp v){replaceChild(v, modificationDateAndTime); modificationDateAndTime = v;}
    /**
	attrDateAndTimeを設定する。

	@param	v 設定する値。
    */
    public void setAttrDateAndTime(UDF_timestamp v){replaceChild(v, attrDateAndTime); attrDateAndTime = v;}
    /**
	checkpointを設定する。

	@param	v 設定する値。
    */
    public void setCheckpoint(UDF_uint32 v){replaceChild(v, checkpoint); checkpoint = v;}
    /**
	extendedAttrICBを設定する。

	@param	v 設定する値。
    */
    public void setExtendedAttrICB(UDF_long_ad v){replaceChild(v, extendedAttrICB); extendedAttrICB = v;}
    /**
	implIdを設定する。

	@param	v 設定する値。
    */
    public void setImplId(UDF_regid v){replaceChild(v, implId); implId = v;}
    /**
	uniqueIdを設定する。

	@param	v 設定する値。
    */
    public void setUniqueId(UDF_uint64 v){replaceChild(v, uniqueId); uniqueId = v;}
    /**
	lenOfExtendedAttrを設定する。

	@param	v 設定する値。
    */
    public void setLenOfExtendedAttr(UDF_uint32 v){replaceChild(v, lenOfExtendedAttr); lenOfExtendedAttr = v;}
    /**
	lenOfAllocDescを設定する。

	@param	v 設定する値。
    */
    public void setLenOfAllocDesc(UDF_uint32 v){replaceChild(v, lenOfAllocDesc); lenOfAllocDesc = v;}
    /**
	extendedAttrを設定する。

	@param	v 設定する値。
    */
    public void setExtendedAttr(UDF_bytes v){replaceChild(v, extendedAttr); extendedAttr = v;}
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
	uid = (UDF_uint32)createElement("UDF_uint32", "", "uid");
	rsize += uid.readFrom(f);
	gid = (UDF_uint32)createElement("UDF_uint32", "", "gid");
	rsize += gid.readFrom(f);
	permissions = (UDF_uint32)createElement("UDF_uint32", "", "permissions");
	rsize += permissions.readFrom(f);
	fileLinkCount = (UDF_uint16)createElement("UDF_uint16", "", "file-link-count");
	rsize += fileLinkCount.readFrom(f);
	recordFormat = (UDF_uint8)createElement("UDF_uint8", "", "record-format");
	rsize += recordFormat.readFrom(f);
	recordDisplayAttr = (UDF_uint8)createElement("UDF_uint8", "", "record-display-attr");
	rsize += recordDisplayAttr.readFrom(f);
	recordLen = (UDF_uint32)createElement("UDF_uint32", "", "record-len");
	rsize += recordLen.readFrom(f);
	infoLen = (UDF_uint64)createElement("UDF_uint64", "", "info-len");
	rsize += infoLen.readFrom(f);
	logicalBlocksRecorded = (UDF_uint64)createElement("UDF_uint64", "", "logical-blocks-recorded");
	rsize += logicalBlocksRecorded.readFrom(f);
	accessDateAndTime = (UDF_timestamp)createElement("UDF_timestamp", "", "access-date-and-time");
	rsize += accessDateAndTime.readFrom(f);
	modificationDateAndTime = (UDF_timestamp)createElement("UDF_timestamp", "", "modification-date-and-time");
	rsize += modificationDateAndTime.readFrom(f);
	attrDateAndTime = (UDF_timestamp)createElement("UDF_timestamp", "", "attr-date-and-time");
	rsize += attrDateAndTime.readFrom(f);
	checkpoint = (UDF_uint32)createElement("UDF_uint32", "", "checkpoint");
	rsize += checkpoint.readFrom(f);
	extendedAttrICB = (UDF_long_ad)createElement("UDF_long_ad", "", "extended-attr-icb");
	rsize += extendedAttrICB.readFrom(f);
	implId = (UDF_regid)createElement("UDF_regid", "", "impl-id");
	rsize += implId.readFrom(f);
	uniqueId = (UDF_uint64)createElement("UDF_uint64", "", "unique-id");
	rsize += uniqueId.readFrom(f);
	lenOfExtendedAttr = (UDF_uint32)createElement("UDF_uint32", "", "len-of-extended-attr");
	rsize += lenOfExtendedAttr.readFrom(f);
	lenOfAllocDesc = (UDF_uint32)createElement("UDF_uint32", "", "len-of-alloc-desc");
	rsize += lenOfAllocDesc.readFrom(f);
	extendedAttr = (UDF_bytes)createElement("UDF_bytes", "", "extended-attr");
	extendedAttr.setSize(getLenOfExtendedAttr().getIntValue());
	rsize += extendedAttr.readFrom(f);
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
	wsize += uid.writeTo(f);
	wsize += gid.writeTo(f);
	wsize += permissions.writeTo(f);
	wsize += fileLinkCount.writeTo(f);
	wsize += recordFormat.writeTo(f);
	wsize += recordDisplayAttr.writeTo(f);
	wsize += recordLen.writeTo(f);
	wsize += infoLen.writeTo(f);
	wsize += logicalBlocksRecorded.writeTo(f);
	wsize += accessDateAndTime.writeTo(f);
	wsize += modificationDateAndTime.writeTo(f);
	wsize += attrDateAndTime.writeTo(f);
	wsize += checkpoint.writeTo(f);
	wsize += extendedAttrICB.writeTo(f);
	wsize += implId.writeTo(f);
	wsize += uniqueId.writeTo(f);
	wsize += lenOfExtendedAttr.writeTo(f);
	wsize += lenOfAllocDesc.writeTo(f);
	wsize += extendedAttr.writeTo(f);
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
	    else if(name.equals("uid")){
		uid = (UDF_uint32)createElement("UDF_uint32", "", "uid");
		uid.readFromXML(child);
	    }
	    else if(name.equals("gid")){
		gid = (UDF_uint32)createElement("UDF_uint32", "", "gid");
		gid.readFromXML(child);
	    }
	    else if(name.equals("permissions")){
		permissions = (UDF_uint32)createElement("UDF_uint32", "", "permissions");
		permissions.readFromXML(child);
	    }
	    else if(name.equals("file-link-count")){
		fileLinkCount = (UDF_uint16)createElement("UDF_uint16", "", "file-link-count");
		fileLinkCount.readFromXML(child);
	    }
	    else if(name.equals("record-format")){
		recordFormat = (UDF_uint8)createElement("UDF_uint8", "", "record-format");
		recordFormat.readFromXML(child);
	    }
	    else if(name.equals("record-display-attr")){
		recordDisplayAttr = (UDF_uint8)createElement("UDF_uint8", "", "record-display-attr");
		recordDisplayAttr.readFromXML(child);
	    }
	    else if(name.equals("record-len")){
		recordLen = (UDF_uint32)createElement("UDF_uint32", "", "record-len");
		recordLen.readFromXML(child);
	    }
	    else if(name.equals("info-len")){
		infoLen = (UDF_uint64)createElement("UDF_uint64", "", "info-len");
		infoLen.readFromXML(child);
	    }
	    else if(name.equals("logical-blocks-recorded")){
		logicalBlocksRecorded = (UDF_uint64)createElement("UDF_uint64", "", "logical-blocks-recorded");
		logicalBlocksRecorded.readFromXML(child);
	    }
	    else if(name.equals("access-date-and-time")){
		accessDateAndTime = (UDF_timestamp)createElement("UDF_timestamp", "", "access-date-and-time");
		accessDateAndTime.readFromXML(child);
	    }
	    else if(name.equals("modification-date-and-time")){
		modificationDateAndTime = (UDF_timestamp)createElement("UDF_timestamp", "", "modification-date-and-time");
		modificationDateAndTime.readFromXML(child);
	    }
	    else if(name.equals("attr-date-and-time")){
		attrDateAndTime = (UDF_timestamp)createElement("UDF_timestamp", "", "attr-date-and-time");
		attrDateAndTime.readFromXML(child);
	    }
	    else if(name.equals("checkpoint")){
		checkpoint = (UDF_uint32)createElement("UDF_uint32", "", "checkpoint");
		checkpoint.readFromXML(child);
	    }
	    else if(name.equals("extended-attr-icb")){
		extendedAttrICB = (UDF_long_ad)createElement("UDF_long_ad", "", "extended-attr-icb");
		extendedAttrICB.readFromXML(child);
	    }
	    else if(name.equals("impl-id")){
		implId = (UDF_regid)createElement("UDF_regid", "", "impl-id");
		implId.readFromXML(child);
	    }
	    else if(name.equals("unique-id")){
		uniqueId = (UDF_uint64)createElement("UDF_uint64", "", "unique-id");
		uniqueId.readFromXML(child);
	    }
	    else if(name.equals("len-of-extended-attr")){
		lenOfExtendedAttr = (UDF_uint32)createElement("UDF_uint32", "", "len-of-extended-attr");
		lenOfExtendedAttr.readFromXML(child);
	    }
	    else if(name.equals("len-of-alloc-desc")){
		lenOfAllocDesc = (UDF_uint32)createElement("UDF_uint32", "", "len-of-alloc-desc");
		lenOfAllocDesc.readFromXML(child);
	    }
	    else if(name.equals("extended-attr")){
		extendedAttr = (UDF_bytes)createElement("UDF_bytes", "", "extended-attr");
		extendedAttr.setSize(getLenOfExtendedAttr().getIntValue());
		extendedAttr.readFromXML(child);
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
	uid = (UDF_uint32)createElement("UDF_uint32", "", "uid");
	uid.setDefaultValue();
	uid.setValue(0xffffffffL);
	gid = (UDF_uint32)createElement("UDF_uint32", "", "gid");
	gid.setDefaultValue();
	gid.setValue(0xffffffffL);
	permissions = (UDF_uint32)createElement("UDF_uint32", "", "permissions");
	permissions.setDefaultValue();
	permissions.setValue(32767);
	fileLinkCount = (UDF_uint16)createElement("UDF_uint16", "", "file-link-count");
	fileLinkCount.setDefaultValue();
	fileLinkCount.setValue(1);
	recordFormat = (UDF_uint8)createElement("UDF_uint8", "", "record-format");
	recordFormat.setDefaultValue();
	recordDisplayAttr = (UDF_uint8)createElement("UDF_uint8", "", "record-display-attr");
	recordDisplayAttr.setDefaultValue();
	recordLen = (UDF_uint32)createElement("UDF_uint32", "", "record-len");
	recordLen.setDefaultValue();
	infoLen = (UDF_uint64)createElement("UDF_uint64", "", "info-len");
	infoLen.setDefaultValue();
	logicalBlocksRecorded = (UDF_uint64)createElement("UDF_uint64", "", "logical-blocks-recorded");
	logicalBlocksRecorded.setDefaultValue();
	accessDateAndTime = (UDF_timestamp)createElement("UDF_timestamp", "", "access-date-and-time");
	accessDateAndTime.setDefaultValue();
	modificationDateAndTime = (UDF_timestamp)createElement("UDF_timestamp", "", "modification-date-and-time");
	modificationDateAndTime.setDefaultValue();
	attrDateAndTime = (UDF_timestamp)createElement("UDF_timestamp", "", "attr-date-and-time");
	attrDateAndTime.setDefaultValue();
	checkpoint = (UDF_uint32)createElement("UDF_uint32", "", "checkpoint");
	checkpoint.setDefaultValue();
	checkpoint.setValue(1);
	extendedAttrICB = (UDF_long_ad)createElement("UDF_long_ad", "", "extended-attr-icb");
	extendedAttrICB.setDefaultValue();
	implId = (UDF_regid)createElement("UDF_regid", "", "impl-id");
	implId.setDefaultValue();
	uniqueId = (UDF_uint64)createElement("UDF_uint64", "", "unique-id");
	uniqueId.setDefaultValue();
	lenOfExtendedAttr = (UDF_uint32)createElement("UDF_uint32", "", "len-of-extended-attr");
	lenOfExtendedAttr.setDefaultValue();
	lenOfAllocDesc = (UDF_uint32)createElement("UDF_uint32", "", "len-of-alloc-desc");
	lenOfAllocDesc.setDefaultValue();
	extendedAttr = (UDF_bytes)createElement("UDF_bytes", "", "extended-attr");
	extendedAttr.setSize(getLenOfExtendedAttr().getIntValue());
	extendedAttr.setDefaultValue();
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
	UDF_desc261 dup = new UDF_desc261(this, null, getName());
	dup.setDefaultValue();
	dup.duplicateHook(this);
	dup.setDescTag((UDF_tag)descTag.duplicateElement());
	dup.setICBTag((UDF_icbtag)iCBTag.duplicateElement());
	dup.setUid((UDF_uint32)uid.duplicateElement());
	dup.setGid((UDF_uint32)gid.duplicateElement());
	dup.setPermissions((UDF_uint32)permissions.duplicateElement());
	dup.setFileLinkCount((UDF_uint16)fileLinkCount.duplicateElement());
	dup.setRecordFormat((UDF_uint8)recordFormat.duplicateElement());
	dup.setRecordDisplayAttr((UDF_uint8)recordDisplayAttr.duplicateElement());
	dup.setRecordLen((UDF_uint32)recordLen.duplicateElement());
	dup.setInfoLen((UDF_uint64)infoLen.duplicateElement());
	dup.setLogicalBlocksRecorded((UDF_uint64)logicalBlocksRecorded.duplicateElement());
	dup.setAccessDateAndTime((UDF_timestamp)accessDateAndTime.duplicateElement());
	dup.setModificationDateAndTime((UDF_timestamp)modificationDateAndTime.duplicateElement());
	dup.setAttrDateAndTime((UDF_timestamp)attrDateAndTime.duplicateElement());
	dup.setCheckpoint((UDF_uint32)checkpoint.duplicateElement());
	dup.setExtendedAttrICB((UDF_long_ad)extendedAttrICB.duplicateElement());
	dup.setImplId((UDF_regid)implId.duplicateElement());
	dup.setUniqueId((UDF_uint64)uniqueId.duplicateElement());
	dup.setLenOfExtendedAttr((UDF_uint32)lenOfExtendedAttr.duplicateElement());
	dup.setLenOfAllocDesc((UDF_uint32)lenOfAllocDesc.duplicateElement());
	dup.setExtendedAttr((UDF_bytes)extendedAttr.duplicateElement());
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
	appendChild(uid);
	appendChild(gid);
	appendChild(permissions);
	appendChild(fileLinkCount);
	appendChild(recordFormat);
	appendChild(recordDisplayAttr);
	appendChild(recordLen);
	appendChild(infoLen);
	appendChild(logicalBlocksRecorded);
	appendChild(accessDateAndTime);
	appendChild(modificationDateAndTime);
	appendChild(attrDateAndTime);
	appendChild(checkpoint);
	appendChild(extendedAttrICB);
	appendChild(implId);
	appendChild(uniqueId);
	appendChild(lenOfExtendedAttr);
	appendChild(lenOfAllocDesc);
	appendChild(extendedAttr);
	appendChild(allocDesc);
    }

    public String getInfo(int indent){
	String a = "";
	a += UDF_Util.repeat(' ', indent);
	a += descTag.getInfo(indent + 1);
	a += iCBTag.getInfo(indent + 1);
	a += uid.getInfo(indent + 1);
	a += gid.getInfo(indent + 1);
	a += permissions.getInfo(indent + 1);
	a += fileLinkCount.getInfo(indent + 1);
	a += recordFormat.getInfo(indent + 1);
	a += recordDisplayAttr.getInfo(indent + 1);
	a += recordLen.getInfo(indent + 1);
	a += infoLen.getInfo(indent + 1);
	a += logicalBlocksRecorded.getInfo(indent + 1);
	a += accessDateAndTime.getInfo(indent + 1);
	a += modificationDateAndTime.getInfo(indent + 1);
	a += attrDateAndTime.getInfo(indent + 1);
	a += checkpoint.getInfo(indent + 1);
	a += extendedAttrICB.getInfo(indent + 1);
	a += implId.getInfo(indent + 1);
	a += uniqueId.getInfo(indent + 1);
	a += lenOfExtendedAttr.getInfo(indent + 1);
	a += lenOfAllocDesc.getInfo(indent + 1);
	a += extendedAttr.getInfo(indent + 1);
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
	uid.debug(indent + 1);
	gid.debug(indent + 1);
	permissions.debug(indent + 1);
	fileLinkCount.debug(indent + 1);
	recordFormat.debug(indent + 1);
	recordDisplayAttr.debug(indent + 1);
	recordLen.debug(indent + 1);
	infoLen.debug(indent + 1);
	logicalBlocksRecorded.debug(indent + 1);
	accessDateAndTime.debug(indent + 1);
	modificationDateAndTime.debug(indent + 1);
	attrDateAndTime.debug(indent + 1);
	checkpoint.debug(indent + 1);
	extendedAttrICB.debug(indent + 1);
	implId.debug(indent + 1);
	uniqueId.debug(indent + 1);
	lenOfExtendedAttr.debug(indent + 1);
	lenOfAllocDesc.debug(indent + 1);
	extendedAttr.debug(indent + 1);
	allocDesc.debug(indent + 1);
    }
//begin:add your code here
    public int getFixedTagId() { return 261; }
    
    public UDF_ErrorList verifyFE() throws UDF_Exception{
	
	UDF_RandomAccess ra = env.f;
	UDF_ErrorList elist = new UDF_ErrorList();
	UDF_Error err;

	short category = UDF_Error.C_ECMA167;


	//　タグの基本チェック　//
	elist.addError(super.verify());
	
	//　ICB Tag の基本チェック　//
	elist.addError(getICBTag().verify());

	//Uid
	//no check

	//Gid
	//no check

	//Permissions
	long perm = permissions.getLongValue();
	if((perm & 0xffff8000) != 0){
	    
	    elist.addError(new UDF_Error
			   (category, UDF_Error.L_ERROR, "Permissions",
			    "The allowd access is shown in figure 4/22.\n15-31: Reserved: Shall be set to ZERO.",
			    "4/14.9.5", String.valueOf(perm), ""));
	}
	
	//File Link Count
	//　Record Format を読み込みます　//
	int record_format = getRecordFormat().getIntValue();
	if (11 < record_format) {

	    err = new UDF_Error(category, UDF_Error.L_ERROR, "Record Format");
	    err.setRefer("4/14.9.7");
	    err.setMessage("12-255: Reserved for future standardisation.");
	    elist.addError(err);
	}

	//　Record Display Attributes を読み込みます　//
	int record_display_attributes = getRecordDisplayAttr().getIntValue();
	if (3 < record_display_attributes) {

	    err = new UDF_Error(category, UDF_Error.L_ERROR, "Record Display Attributes");
	    err.setRefer("4/14.9.8");
	    err.setMessage("4-255: Reserved for future standardisation.");
	    elist.addError(err);
	}

	//Record Length
	int record_length = getRecordLen().getIntValue();

	if (record_format == 1 || record_format == 2) {
	    //　今後の課題　//
	    if (false) {
		err = new UDF_Error(category, UDF_Error.L_ERROR, "Record Length");
		err.setRefer("4/14.9.9");
		err.setMessage("If the Record Format field contains either 1 or 2, the Record Length field shall specify the length, in bytes, of each record in the file.");
		elist.addError(err);
	    }
	}

	//　Information Length　//
	long information_length = getInfoLen().getLongValue();
	long sumlen_ad = sumLengthsOfAllocDesc();
	//　ADが示す長さと一致しないと駄目です　//
	if (sumlen_ad != information_length) {
	    err = new UDF_Error(category, UDF_Error.L_ERROR, "Infomation Length");
	    err.setRefer("4/14.9.10");
	    err.setMessage("This shall be equal to the sum of the Information Lengths of the allocation descriptors for the body of the file (see 4/8.8.2 and 4/12).");
	    err.setRecordedValue(String.valueOf(information_length));
	    err.setExpectedValue(String.valueOf(sumlen_ad));
	    elist.addError(err);
	}


	//Logical Blocks Recorded
	//　このExtended File Entry のextent情報が存在するセクタ番号を示す　//
	if (true) {
	    if(getAllocType() != 3)
		elist.addError(verifyLogicalBlocksRecorded(calcLogicalBlocksRecordedFromAD()));
	    else
		elist.addError(verifyLogicalBlocksRecorded(0));
	}

	//Access Date and Time
	elist.addError(getAccessDateAndTime().verify("Access Date and Time"));
	if (false) {
	    err = new UDF_Error(category, UDF_Error.L_WARNING, "Access Date and Time");
	    err.setRefer("4/14.17.13");
	    err.setMessage("The access date and time should reflect the last access to any stream of the file.");
	    elist.addError(err);
	}

	//Modification Date and Time
	UDF_timestamp modification_timestamp = getModificationDateAndTime();
	elist.addError(modification_timestamp.verify("Modification Date and Time"));
	if (false) {
	    err = new UDF_Error(category, UDF_Error.L_WARNING, "Modification Date and Time");
	    err.setRefer("4/14.17.14");
	    err.setRefer("The modification date and time should reflect the last modification to any stream of the file.");
	    elist.addError(err);
	}

	//Attribute Date and Time
	UDF_timestamp attribute_timestamp = getAttrDateAndTime();
	elist.addError(attribute_timestamp.verify("Attribute Date and Time"));

	//Checkpoint
	if (getCheckpoint().getIntValue() < 1) {
	    err = new UDF_Error(category, UDF_Error.L_CAUTION, "Checkpoint");
	    err.setRefer("4/14.9.15");
	    err.setMessage("This field shall contain 1 for the first instance of a file and shall be incremented by 1 when directed to do so by the user.");
	    err.setRecordedValue(String.valueOf(getCheckpoint().getIntValue()));
	    elist.addError(err);
	}

	//　File Times Extended Attributesがあった場合、各タイムスタンプと比較しなければならない　//
	UDF_timestamp ext_ts = null;

	//Extended Attribute ICB
	//4/14.9.16
	UDF_long_ad ea_icb = getExtendedAttrICB();
	if (0 != ea_icb.getLen() && 0 != ea_icb.getLbn()) {

/*  この手法では失敗します。2005/03/11
	    //　File Times Extended Attributesの検索　//
	    UDF_attr5 file_times_ext_attr = (UDF_attr5) searchElementBase(ea_icb, "UDF_attr5");

	    //　タイムスタンプの読み取り※UDF_attr5 をタイムスタンプに対応させればよい？　//
	    UDF_RandomAccessBuffer rab = file_times_ext_attr.getFileTimes().genRandomAccessBytes();//new UDF_RandomAccessBuffer(file_times_ext_attr.getFileTimes().getData());
	    ext_ts = (UDF_timestamp) createElement("UDF_timestamp", null, null);
	    try {
		ext_ts.readFrom(rab);
	    }
	    catch(IOException e) {
		throw new UDF_InternalException(this, e.toString());
	    }
*/
	}

	//Implementation Identifier
	//4/14.9.17

	//Unique Id
/*====
	long unique_id = getUniqueId().getLongValue();
	if (false) {

	    err = new UDF_Error(category, UDF_Error.L_ERROR, "Unique Id");
	    err.setRefer("4/14.9.18");
	    err.setMessage("All File Entries with the same contents of this field shall describe the same file or directory.");
	    elist.addError(err);
	}
====*/

	//Length of Extended Attributes
	//　※Extended Attribute ICBとは独立した内容なので注意　//
	int len_of_ea = getLenOfExtendedAttr().getIntValue();
	if ((len_of_ea % 4) != 0) {
	    err = new UDF_Error(category, UDF_Error.L_ERROR, "Length of Extended Attributes");
	    err.setRefer("4/14.9.19");
	    err.setMessage("L_EA shall be an integral multiple of 4.");
	    elist.addError(err);
	}

	//Extended Attributes
	//4/14.9.21
	//UDF_attr.verifyUnuseSpace();
	if (len_of_ea != 0) {

	    //　File Times Extended Attributesを検索　//
	    UDF_bytes extended_attributes = getExtendedAttr();
	    elist.addError(extended_attributes.verify());

	    //UDF_attr5 attr5 = (UDF_attr5) searchElementBase(extended_attributes, "UDF_attr5");
	    UDF_ElementBase tmpattr5 = extended_attributes.findByXPATH("UDF_attr5");
	    if(tmpattr5 != null){
		
		UDF_attr5 attr5 = (UDF_attr5) tmpattr5;//extended_attributes.findByXPATH("UDF_attr5");
		
		//　File Creation Data and Time を読み取り、ファイル作成時の時刻を更新します　//
		try{
		    UDF_timestamp ts = attr5.getTimestamp(attr5.FILE_CREATION_DATE_AND_TIME);
		    
		    if (ext_ts == null) {
			ext_ts = ts;
		    }
		    else
		    if (ts.before(ext_ts)) {
			ext_ts = ts;
		    }
		}
		catch(UDF_InternalException uie){
		    ;
		}
		
	    }
	}

	//　File Times Extented Attribute と各タイムスタンプを比較する　//
	if (ext_ts != null) {

	    //Modification Date and Time
	    String err_msg_file_times_extended_attrbute = "This date and time shall not be earlier than the File Creation Date and Time specified in the File Times Extended Attribute, if any.";
	    if (ext_ts.after(modification_timestamp)) {
		err = new UDF_Error(category, UDF_Error.L_ERROR, "Modification Date and Time");
		err.setRefer("4/14.9.13");
		err.setMessage(err_msg_file_times_extended_attrbute);
		elist.addError(err);
	    }

	    //Attribute Date and Time
	    if (ext_ts.after(attribute_timestamp)) {
		err = new UDF_Error(category, UDF_Error.L_ERROR, "Attribute Date and Time");
		err.setRefer("4/14.9.14");
		err.setMessage(err_msg_file_times_extended_attrbute);
		elist.addError(err);
	    }
	}


	//Length of Allocation Descriptor
	//4/14.9.20
	//no check

	//Allocation Descriptor
	//　論理ブロック番号０を指すICB は存在しないとされている　//
	UDF_ElementBase [] member = getChildren();
	for (int idx = 0, max = member.length; idx < max; idx++) {

	    if (!(member[idx].getName().equals("alloc-desc"))) {
		continue;
	    }

	    UDF_ElementBase [] ad = member[idx].getChildren();
	    for (int i = 0, len = ad.length; i < len; i++) {
		
		String name = ad[i].getName();
		if (name.equals("UDF_short_ad")) {
		}
		else
		if (name.equals("UDF_long_ad")) {
		}
		else
		if (name.equals("UDF_ext_ad")) {
		}
		else {
		    elist.addError(ad[i].verify("Allocation Descriptor"));
		    continue;
		}
		
		UDF_AD ad2 = (UDF_AD)ad[i];
		if(ad2.getFlag() != UDF_AD.FLAG2)
		    continue;
		if (0 == ad2.getLbn()) {
		    continue;
		}
		
		elist.addError(ad[i].verify("Allocation Descriptor[" + i + "]"));
		
		err = new UDF_Error(UDF_Error.C_ECMA167, UDF_Error.L_CAUTION, "Allocation Descriptor[" + i + "]");
		err.setRefer("4/14.9.22");
		err.setMessage("Any such allocation descriptor which is specified as unrecorded and unallocated (see 4/14.14.1.1) shall have its Extent Location field set to 0.");
		elist.addError(err);
	    }
	}
	
	return elist;
    }


    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList elist = new UDF_ErrorList();
	UDF_Error err;
	
	
	//　IDの判定　//
	err = verifyId();
	if (err.isError()) {
	    err.setRefer("4/4.9.1");
	    elist.addError(err);
	}
	
	elist.addError(verifyFE());
	elist.setGlobalPoint(getGlobalPoint());
	elist.setRName("File Entry");
	
	return elist;
    }
    
    /**
      Allocation Descriptor が保持するLengthを足したものを返します。
    */
    protected long sumLengthsOfAllocDesc() {

	long size;

	if ((getICBFlags() & 0x07) == UDF_icbtag.DIRECT) {
	    return getLenOfAllocDesc().getLongValue();
	}
	/*
	UDF_ElementList elem = getADList();

	size = 0;
	for (int i = 0, max = elem.size(); i < max; i++) {
	    
	    UDF_AD ad = (UDF_AD) elem.elementAt(i);
	    if (ad.getFlag() != 0) {
		continue;
	    }
	    size += ad.getLen();
	}
	
	return size;
	*/
	return getADSize();
    }

    /**
      LogicalBlocksRecordedの値を引数のものと比較します。
    */
    protected UDF_Error verifyLogicalBlocksRecorded(long x) throws UDF_Exception {
	
	long lbr = getLogicalBlocksRecorded().getLongValue();
	if (lbr != x) {
	    
	    return new UDF_Error(UDF_Error.C_ECMA167, UDF_Error.L_ERROR, "Logical Blocks Recorded",
				 "This field specify the number of recorded logical blocks specified by the allocation descriptor for the body of the file", "4/14.9.11", String.valueOf(lbr), String.valueOf(x));
	}

	return new UDF_Error();
    }
//end:
};
