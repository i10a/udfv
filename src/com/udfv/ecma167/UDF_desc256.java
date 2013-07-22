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
File&nbsp;Set&nbsp;Descriptor&nbsp;を表現するクラス。<br/>


<dl>

<dt>フィールド一覧</dt>
<dd>
<table border="1">
<tr><td>メンバ</td><td>型</td><td>サイズ</td></tr>
<tr><td><b>DescTag</b></td><td><b>UDF_tag</b></td><td><i>descTag.getSize()</i></td></tr>
<tr><td><b>RecordingDateAndTime</b></td><td><b>UDF_timestamp</b></td><td><i>recordingDateAndTime.getSize()</i></td></tr>
<tr><td><b>InterchangeLevel</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td></tr>
<tr><td><b>MaxInterchangeLevel</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td></tr>
<tr><td><b>CharSetList</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>MaxCharSetList</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>FileSetNumber</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>FileSetDescNumber</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>LogicalVolIdCharSet</b></td><td><b>UDF_charspec</b></td><td><i>logicalVolIdCharSet.getSize()</i></td></tr>
<tr><td><b>LogicalVolId</b></td><td><b>UDF_dstring</b></td><td><i>128</i></td></tr>
<tr><td><b>FileSetCharSet</b></td><td><b>UDF_charspec</b></td><td><i>fileSetCharSet.getSize()</i></td></tr>
<tr><td><b>FileSetId</b></td><td><b>UDF_dstring</b></td><td><i>32</i></td></tr>
<tr><td><b>CopyrightFileId</b></td><td><b>UDF_dstring</b></td><td><i>32</i></td></tr>
<tr><td><b>AbstractFileId</b></td><td><b>UDF_dstring</b></td><td><i>32</i></td></tr>
<tr><td><b>RootDirectoryICB</b></td><td><b>UDF_long_ad</b></td><td><i>rootDirectoryICB.getSize()</i></td></tr>
<tr><td><b>DomainId</b></td><td><b>UDF_regid</b></td><td><i>domainId.getSize()</i></td></tr>
<tr><td><b>NextExtent</b></td><td><b>UDF_long_ad</b></td><td><i>nextExtent.getSize()</i></td></tr>
<tr><td><b>SystemStreamDirectoryICB</b></td><td><b>UDF_long_ad</b></td><td><i>systemStreamDirectoryICB.getSize()</i></td></tr>
<tr><td><b>Reserved</b></td><td><b>UDF_bytes</b></td><td><i>32</i></td></tr>
</table>
</dd>
</dl>
*/
public class UDF_desc256 extends UDF_CrcDesc 
{

    /**
	このオブジェクトのパッケージ名を除いたクラス名を取得する。

	@return クラス名
    */
    public static String getUDFClassName( ) {
        return "UDF_desc256";
    }

    /**
	コンストラクタ。

	@param elem   親エレメント。
	@param prefix ネームスペース。
	@param name   エレメント名。
    */
    public UDF_desc256(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }
    /**
	このUDFエレメントのサイズを返す。

	@return UDFエレメントのサイズ。
    */
    public int getSize(){
	return 0+descTag.getSize()+recordingDateAndTime.getSize()+2+2+4+4+4+4+logicalVolIdCharSet.getSize()+logicalVolId.getSize()+fileSetCharSet.getSize()+fileSetId.getSize()+copyrightFileId.getSize()+abstractFileId.getSize()+rootDirectoryICB.getSize()+domainId.getSize()+nextExtent.getSize()+systemStreamDirectoryICB.getSize()+reserved.getSize();
    }

    /**
	このUDFエレメントのサイズをlong 型で返す。

	@return UDFエレメントのサイズ。
    */
    public long getLongSize(){
	return (long)0+descTag.getSize()+recordingDateAndTime.getSize()+2+2+4+4+4+4+logicalVolIdCharSet.getSize()+logicalVolId.getSize()+fileSetCharSet.getSize()+fileSetId.getSize()+copyrightFileId.getSize()+abstractFileId.getSize()+rootDirectoryICB.getSize()+domainId.getSize()+nextExtent.getSize()+systemStreamDirectoryICB.getSize()+reserved.getSize();
    }
    private UDF_tag descTag;
    private UDF_timestamp recordingDateAndTime;
    private UDF_uint16 interchangeLevel;
    private UDF_uint16 maxInterchangeLevel;
    private UDF_uint32 charSetList;
    private UDF_uint32 maxCharSetList;
    private UDF_uint32 fileSetNumber;
    private UDF_uint32 fileSetDescNumber;
    private UDF_charspec logicalVolIdCharSet;
    private UDF_dstring logicalVolId;
    private UDF_charspec fileSetCharSet;
    private UDF_dstring fileSetId;
    private UDF_dstring copyrightFileId;
    private UDF_dstring abstractFileId;
    private UDF_long_ad rootDirectoryICB;
    private UDF_regid domainId;
    private UDF_long_ad nextExtent;
    private UDF_long_ad systemStreamDirectoryICB;
    private UDF_bytes reserved;

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
	interchangeLevelを取得する。

	@return 取得したinterchangeLevel を返す。
    */
    public UDF_uint16 getInterchangeLevel(){return interchangeLevel;}
    /**
	maxInterchangeLevelを取得する。

	@return 取得したmaxInterchangeLevel を返す。
    */
    public UDF_uint16 getMaxInterchangeLevel(){return maxInterchangeLevel;}
    /**
	charSetListを取得する。

	@return 取得したcharSetList を返す。
    */
    public UDF_uint32 getCharSetList(){return charSetList;}
    /**
	maxCharSetListを取得する。

	@return 取得したmaxCharSetList を返す。
    */
    public UDF_uint32 getMaxCharSetList(){return maxCharSetList;}
    /**
	fileSetNumberを取得する。

	@return 取得したfileSetNumber を返す。
    */
    public UDF_uint32 getFileSetNumber(){return fileSetNumber;}
    /**
	fileSetDescNumberを取得する。

	@return 取得したfileSetDescNumber を返す。
    */
    public UDF_uint32 getFileSetDescNumber(){return fileSetDescNumber;}
    /**
	logicalVolIdCharSetを取得する。

	@return 取得したlogicalVolIdCharSet を返す。
    */
    public UDF_charspec getLogicalVolIdCharSet(){return logicalVolIdCharSet;}
    /**
	logicalVolIdを取得する。

	@return 取得したlogicalVolId を返す。
    */
    public UDF_dstring getLogicalVolId(){return logicalVolId;}
    /**
	fileSetCharSetを取得する。

	@return 取得したfileSetCharSet を返す。
    */
    public UDF_charspec getFileSetCharSet(){return fileSetCharSet;}
    /**
	fileSetIdを取得する。

	@return 取得したfileSetId を返す。
    */
    public UDF_dstring getFileSetId(){return fileSetId;}
    /**
	copyrightFileIdを取得する。

	@return 取得したcopyrightFileId を返す。
    */
    public UDF_dstring getCopyrightFileId(){return copyrightFileId;}
    /**
	abstractFileIdを取得する。

	@return 取得したabstractFileId を返す。
    */
    public UDF_dstring getAbstractFileId(){return abstractFileId;}
    /**
	rootDirectoryICBを取得する。

	@return 取得したrootDirectoryICB を返す。
    */
    public UDF_long_ad getRootDirectoryICB(){return rootDirectoryICB;}
    /**
	domainIdを取得する。

	@return 取得したdomainId を返す。
    */
    public UDF_regid getDomainId(){return domainId;}
    /**
	nextExtentを取得する。

	@return 取得したnextExtent を返す。
    */
    public UDF_long_ad getNextExtent(){return nextExtent;}
    /**
	systemStreamDirectoryICBを取得する。

	@return 取得したsystemStreamDirectoryICB を返す。
    */
    public UDF_long_ad getSystemStreamDirectoryICB(){return systemStreamDirectoryICB;}
    /**
	reservedを取得する。

	@return 取得したreserved を返す。
    */
    public UDF_bytes getReserved(){return reserved;}

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
	interchangeLevelを設定する。

	@param	v 設定する値。
    */
    public void setInterchangeLevel(UDF_uint16 v){replaceChild(v, interchangeLevel); interchangeLevel = v;}
    /**
	maxInterchangeLevelを設定する。

	@param	v 設定する値。
    */
    public void setMaxInterchangeLevel(UDF_uint16 v){replaceChild(v, maxInterchangeLevel); maxInterchangeLevel = v;}
    /**
	charSetListを設定する。

	@param	v 設定する値。
    */
    public void setCharSetList(UDF_uint32 v){replaceChild(v, charSetList); charSetList = v;}
    /**
	maxCharSetListを設定する。

	@param	v 設定する値。
    */
    public void setMaxCharSetList(UDF_uint32 v){replaceChild(v, maxCharSetList); maxCharSetList = v;}
    /**
	fileSetNumberを設定する。

	@param	v 設定する値。
    */
    public void setFileSetNumber(UDF_uint32 v){replaceChild(v, fileSetNumber); fileSetNumber = v;}
    /**
	fileSetDescNumberを設定する。

	@param	v 設定する値。
    */
    public void setFileSetDescNumber(UDF_uint32 v){replaceChild(v, fileSetDescNumber); fileSetDescNumber = v;}
    /**
	logicalVolIdCharSetを設定する。

	@param	v 設定する値。
    */
    public void setLogicalVolIdCharSet(UDF_charspec v){replaceChild(v, logicalVolIdCharSet); logicalVolIdCharSet = v;}
    /**
	logicalVolIdを設定する。

	@param	v 設定する値。
    */
    public void setLogicalVolId(UDF_dstring v){replaceChild(v, logicalVolId); logicalVolId = v;}
    /**
	fileSetCharSetを設定する。

	@param	v 設定する値。
    */
    public void setFileSetCharSet(UDF_charspec v){replaceChild(v, fileSetCharSet); fileSetCharSet = v;}
    /**
	fileSetIdを設定する。

	@param	v 設定する値。
    */
    public void setFileSetId(UDF_dstring v){replaceChild(v, fileSetId); fileSetId = v;}
    /**
	copyrightFileIdを設定する。

	@param	v 設定する値。
    */
    public void setCopyrightFileId(UDF_dstring v){replaceChild(v, copyrightFileId); copyrightFileId = v;}
    /**
	abstractFileIdを設定する。

	@param	v 設定する値。
    */
    public void setAbstractFileId(UDF_dstring v){replaceChild(v, abstractFileId); abstractFileId = v;}
    /**
	rootDirectoryICBを設定する。

	@param	v 設定する値。
    */
    public void setRootDirectoryICB(UDF_long_ad v){replaceChild(v, rootDirectoryICB); rootDirectoryICB = v;}
    /**
	domainIdを設定する。

	@param	v 設定する値。
    */
    public void setDomainId(UDF_regid v){replaceChild(v, domainId); domainId = v;}
    /**
	nextExtentを設定する。

	@param	v 設定する値。
    */
    public void setNextExtent(UDF_long_ad v){replaceChild(v, nextExtent); nextExtent = v;}
    /**
	systemStreamDirectoryICBを設定する。

	@param	v 設定する値。
    */
    public void setSystemStreamDirectoryICB(UDF_long_ad v){replaceChild(v, systemStreamDirectoryICB); systemStreamDirectoryICB = v;}
    /**
	reservedを設定する。

	@param	v 設定する値。
    */
    public void setReserved(UDF_bytes v){replaceChild(v, reserved); reserved = v;}

    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long rsize = 0;
	preReadHook(f);
	descTag = (UDF_tag)createElement("UDF_tag", "", "desc-tag");
	rsize += descTag.readFrom(f);
	recordingDateAndTime = (UDF_timestamp)createElement("UDF_timestamp", "", "recording-date-and-time");
	rsize += recordingDateAndTime.readFrom(f);
	interchangeLevel = (UDF_uint16)createElement("UDF_uint16", "", "interchange-level");
	rsize += interchangeLevel.readFrom(f);
	maxInterchangeLevel = (UDF_uint16)createElement("UDF_uint16", "", "max-interchange-level");
	rsize += maxInterchangeLevel.readFrom(f);
	charSetList = (UDF_uint32)createElement("UDF_uint32", "", "char-set-list");
	rsize += charSetList.readFrom(f);
	maxCharSetList = (UDF_uint32)createElement("UDF_uint32", "", "max-char-set-list");
	rsize += maxCharSetList.readFrom(f);
	fileSetNumber = (UDF_uint32)createElement("UDF_uint32", "", "file-set-number");
	rsize += fileSetNumber.readFrom(f);
	fileSetDescNumber = (UDF_uint32)createElement("UDF_uint32", "", "file-set-desc-number");
	rsize += fileSetDescNumber.readFrom(f);
	logicalVolIdCharSet = (UDF_charspec)createElement("UDF_charspec", "", "logical-vol-id-char-set");
	rsize += logicalVolIdCharSet.readFrom(f);
	logicalVolId = (UDF_dstring)createElement("UDF_dstring", "", "logical-vol-id");
	logicalVolId.setSize(128);
	rsize += logicalVolId.readFrom(f);
	fileSetCharSet = (UDF_charspec)createElement("UDF_charspec", "", "file-set-char-set");
	rsize += fileSetCharSet.readFrom(f);
	fileSetId = (UDF_dstring)createElement("UDF_dstring", "", "file-set-id");
	fileSetId.setSize(32);
	rsize += fileSetId.readFrom(f);
	copyrightFileId = (UDF_dstring)createElement("UDF_dstring", "", "copyright-file-id");
	copyrightFileId.setSize(32);
	rsize += copyrightFileId.readFrom(f);
	abstractFileId = (UDF_dstring)createElement("UDF_dstring", "", "abstract-file-id");
	abstractFileId.setSize(32);
	rsize += abstractFileId.readFrom(f);
	rootDirectoryICB = (UDF_long_ad)createElement("UDF_long_ad", "", "root-directory-icb");
	rsize += rootDirectoryICB.readFrom(f);
	domainId = (UDF_regid)createElement("UDF_regid", "", "domain-id");
	rsize += domainId.readFrom(f);
	nextExtent = (UDF_long_ad)createElement("UDF_long_ad", "", "next-extent");
	rsize += nextExtent.readFrom(f);
	systemStreamDirectoryICB = (UDF_long_ad)createElement("UDF_long_ad", "", "system-stream-directory-icb");
	rsize += systemStreamDirectoryICB.readFrom(f);
	reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
	reserved.setSize(32);
	rsize += reserved.readFrom(f);
	apply( );
	postReadHook(f);
	return rsize;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long wsize = 0;
	wsize += descTag.writeTo(f);
	wsize += recordingDateAndTime.writeTo(f);
	wsize += interchangeLevel.writeTo(f);
	wsize += maxInterchangeLevel.writeTo(f);
	wsize += charSetList.writeTo(f);
	wsize += maxCharSetList.writeTo(f);
	wsize += fileSetNumber.writeTo(f);
	wsize += fileSetDescNumber.writeTo(f);
	wsize += logicalVolIdCharSet.writeTo(f);
	wsize += logicalVolId.writeTo(f);
	wsize += fileSetCharSet.writeTo(f);
	wsize += fileSetId.writeTo(f);
	wsize += copyrightFileId.writeTo(f);
	wsize += abstractFileId.writeTo(f);
	wsize += rootDirectoryICB.writeTo(f);
	wsize += domainId.writeTo(f);
	wsize += nextExtent.writeTo(f);
	wsize += systemStreamDirectoryICB.writeTo(f);
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
	    else if(name.equals("desc-tag")){
		descTag = (UDF_tag)createElement("UDF_tag", "", "desc-tag");
		descTag.readFromXML(child);
	    }
	    else if(name.equals("recording-date-and-time")){
		recordingDateAndTime = (UDF_timestamp)createElement("UDF_timestamp", "", "recording-date-and-time");
		recordingDateAndTime.readFromXML(child);
	    }
	    else if(name.equals("interchange-level")){
		interchangeLevel = (UDF_uint16)createElement("UDF_uint16", "", "interchange-level");
		interchangeLevel.readFromXML(child);
	    }
	    else if(name.equals("max-interchange-level")){
		maxInterchangeLevel = (UDF_uint16)createElement("UDF_uint16", "", "max-interchange-level");
		maxInterchangeLevel.readFromXML(child);
	    }
	    else if(name.equals("char-set-list")){
		charSetList = (UDF_uint32)createElement("UDF_uint32", "", "char-set-list");
		charSetList.readFromXML(child);
	    }
	    else if(name.equals("max-char-set-list")){
		maxCharSetList = (UDF_uint32)createElement("UDF_uint32", "", "max-char-set-list");
		maxCharSetList.readFromXML(child);
	    }
	    else if(name.equals("file-set-number")){
		fileSetNumber = (UDF_uint32)createElement("UDF_uint32", "", "file-set-number");
		fileSetNumber.readFromXML(child);
	    }
	    else if(name.equals("file-set-desc-number")){
		fileSetDescNumber = (UDF_uint32)createElement("UDF_uint32", "", "file-set-desc-number");
		fileSetDescNumber.readFromXML(child);
	    }
	    else if(name.equals("logical-vol-id-char-set")){
		logicalVolIdCharSet = (UDF_charspec)createElement("UDF_charspec", "", "logical-vol-id-char-set");
		logicalVolIdCharSet.readFromXML(child);
	    }
	    else if(name.equals("logical-vol-id")){
		logicalVolId = (UDF_dstring)createElement("UDF_dstring", "", "logical-vol-id");
		logicalVolId.setSize(128);
		logicalVolId.readFromXML(child);
	    }
	    else if(name.equals("file-set-char-set")){
		fileSetCharSet = (UDF_charspec)createElement("UDF_charspec", "", "file-set-char-set");
		fileSetCharSet.readFromXML(child);
	    }
	    else if(name.equals("file-set-id")){
		fileSetId = (UDF_dstring)createElement("UDF_dstring", "", "file-set-id");
		fileSetId.setSize(32);
		fileSetId.readFromXML(child);
	    }
	    else if(name.equals("copyright-file-id")){
		copyrightFileId = (UDF_dstring)createElement("UDF_dstring", "", "copyright-file-id");
		copyrightFileId.setSize(32);
		copyrightFileId.readFromXML(child);
	    }
	    else if(name.equals("abstract-file-id")){
		abstractFileId = (UDF_dstring)createElement("UDF_dstring", "", "abstract-file-id");
		abstractFileId.setSize(32);
		abstractFileId.readFromXML(child);
	    }
	    else if(name.equals("root-directory-icb")){
		rootDirectoryICB = (UDF_long_ad)createElement("UDF_long_ad", "", "root-directory-icb");
		rootDirectoryICB.readFromXML(child);
	    }
	    else if(name.equals("domain-id")){
		domainId = (UDF_regid)createElement("UDF_regid", "", "domain-id");
		domainId.readFromXML(child);
	    }
	    else if(name.equals("next-extent")){
		nextExtent = (UDF_long_ad)createElement("UDF_long_ad", "", "next-extent");
		nextExtent.readFromXML(child);
	    }
	    else if(name.equals("system-stream-directory-icb")){
		systemStreamDirectoryICB = (UDF_long_ad)createElement("UDF_long_ad", "", "system-stream-directory-icb");
		systemStreamDirectoryICB.readFromXML(child);
	    }
	    else if(name.equals("reserved")){
		reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
		reserved.setSize(32);
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
	descTag = (UDF_tag)createElement("UDF_tag", "", "desc-tag");
	descTag.setDefaultValue();
	recordingDateAndTime = (UDF_timestamp)createElement("UDF_timestamp", "", "recording-date-and-time");
	recordingDateAndTime.setDefaultValue();
	interchangeLevel = (UDF_uint16)createElement("UDF_uint16", "", "interchange-level");
	interchangeLevel.setDefaultValue();
	interchangeLevel.setValue(3);
	maxInterchangeLevel = (UDF_uint16)createElement("UDF_uint16", "", "max-interchange-level");
	maxInterchangeLevel.setDefaultValue();
	maxInterchangeLevel.setValue(3);
	charSetList = (UDF_uint32)createElement("UDF_uint32", "", "char-set-list");
	charSetList.setDefaultValue();
	charSetList.setValue(1);
	maxCharSetList = (UDF_uint32)createElement("UDF_uint32", "", "max-char-set-list");
	maxCharSetList.setDefaultValue();
	maxCharSetList.setValue(1);
	fileSetNumber = (UDF_uint32)createElement("UDF_uint32", "", "file-set-number");
	fileSetNumber.setDefaultValue();
	fileSetDescNumber = (UDF_uint32)createElement("UDF_uint32", "", "file-set-desc-number");
	fileSetDescNumber.setDefaultValue();
	logicalVolIdCharSet = (UDF_charspec)createElement("UDF_charspec", "", "logical-vol-id-char-set");
	logicalVolIdCharSet.setDefaultValue();
	logicalVolId = (UDF_dstring)createElement("UDF_dstring", "", "logical-vol-id");
	logicalVolId.setSize(128);
	logicalVolId.setDefaultValue();
	fileSetCharSet = (UDF_charspec)createElement("UDF_charspec", "", "file-set-char-set");
	fileSetCharSet.setDefaultValue();
	fileSetId = (UDF_dstring)createElement("UDF_dstring", "", "file-set-id");
	fileSetId.setSize(32);
	fileSetId.setDefaultValue();
	copyrightFileId = (UDF_dstring)createElement("UDF_dstring", "", "copyright-file-id");
	copyrightFileId.setSize(32);
	copyrightFileId.setDefaultValue();
	abstractFileId = (UDF_dstring)createElement("UDF_dstring", "", "abstract-file-id");
	abstractFileId.setSize(32);
	abstractFileId.setDefaultValue();
	rootDirectoryICB = (UDF_long_ad)createElement("UDF_long_ad", "", "root-directory-icb");
	rootDirectoryICB.setDefaultValue();
	domainId = (UDF_regid)createElement("UDF_regid", "", "domain-id");
	domainId.setDefaultValue();
	nextExtent = (UDF_long_ad)createElement("UDF_long_ad", "", "next-extent");
	nextExtent.setDefaultValue();
	systemStreamDirectoryICB = (UDF_long_ad)createElement("UDF_long_ad", "", "system-stream-directory-icb");
	systemStreamDirectoryICB.setDefaultValue();
	reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
	reserved.setSize(32);
	reserved.setDefaultValue();

	apply( );
	postSetDefaultValueHook();
    }


    /**
	複製する
    */
    public UDF_Element duplicateElement(){
	UDF_desc256 dup = new UDF_desc256(this, null, getName());
	dup.setDefaultValue();
	dup.duplicateHook(this);
	dup.setDescTag((UDF_tag)descTag.duplicateElement());
	dup.setRecordingDateAndTime((UDF_timestamp)recordingDateAndTime.duplicateElement());
	dup.setInterchangeLevel((UDF_uint16)interchangeLevel.duplicateElement());
	dup.setMaxInterchangeLevel((UDF_uint16)maxInterchangeLevel.duplicateElement());
	dup.setCharSetList((UDF_uint32)charSetList.duplicateElement());
	dup.setMaxCharSetList((UDF_uint32)maxCharSetList.duplicateElement());
	dup.setFileSetNumber((UDF_uint32)fileSetNumber.duplicateElement());
	dup.setFileSetDescNumber((UDF_uint32)fileSetDescNumber.duplicateElement());
	dup.setLogicalVolIdCharSet((UDF_charspec)logicalVolIdCharSet.duplicateElement());
	dup.setLogicalVolId((UDF_dstring)logicalVolId.duplicateElement());
	dup.setFileSetCharSet((UDF_charspec)fileSetCharSet.duplicateElement());
	dup.setFileSetId((UDF_dstring)fileSetId.duplicateElement());
	dup.setCopyrightFileId((UDF_dstring)copyrightFileId.duplicateElement());
	dup.setAbstractFileId((UDF_dstring)abstractFileId.duplicateElement());
	dup.setRootDirectoryICB((UDF_long_ad)rootDirectoryICB.duplicateElement());
	dup.setDomainId((UDF_regid)domainId.duplicateElement());
	dup.setNextExtent((UDF_long_ad)nextExtent.duplicateElement());
	dup.setSystemStreamDirectoryICB((UDF_long_ad)systemStreamDirectoryICB.duplicateElement());
	dup.setReserved((UDF_bytes)reserved.duplicateElement());

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
	appendChild(interchangeLevel);
	appendChild(maxInterchangeLevel);
	appendChild(charSetList);
	appendChild(maxCharSetList);
	appendChild(fileSetNumber);
	appendChild(fileSetDescNumber);
	appendChild(logicalVolIdCharSet);
	appendChild(logicalVolId);
	appendChild(fileSetCharSet);
	appendChild(fileSetId);
	appendChild(copyrightFileId);
	appendChild(abstractFileId);
	appendChild(rootDirectoryICB);
	appendChild(domainId);
	appendChild(nextExtent);
	appendChild(systemStreamDirectoryICB);
	appendChild(reserved);
    }

    public String getInfo(int indent){
	String a = "";
	a += UDF_Util.repeat(' ', indent);
	a += descTag.getInfo(indent + 1);
	a += recordingDateAndTime.getInfo(indent + 1);
	a += interchangeLevel.getInfo(indent + 1);
	a += maxInterchangeLevel.getInfo(indent + 1);
	a += charSetList.getInfo(indent + 1);
	a += maxCharSetList.getInfo(indent + 1);
	a += fileSetNumber.getInfo(indent + 1);
	a += fileSetDescNumber.getInfo(indent + 1);
	a += logicalVolIdCharSet.getInfo(indent + 1);
	a += logicalVolId.getInfo(indent + 1);
	a += fileSetCharSet.getInfo(indent + 1);
	a += fileSetId.getInfo(indent + 1);
	a += copyrightFileId.getInfo(indent + 1);
	a += abstractFileId.getInfo(indent + 1);
	a += rootDirectoryICB.getInfo(indent + 1);
	a += domainId.getInfo(indent + 1);
	a += nextExtent.getInfo(indent + 1);
	a += systemStreamDirectoryICB.getInfo(indent + 1);
	a += reserved.getInfo(indent + 1);
      return a;
    }
    public void debug(int indent){
	System.err.print(UDF_Util.repeat(' ', indent));

	String info = " gp=" + getGlobalPoint();
	info += " pn=" + getElemPartRefNo();
	info += " sn=" + getPartSubno();
	System.err.println(getName() + ": (" + hashCode() + ")" + info);	descTag.debug(indent + 1);
	recordingDateAndTime.debug(indent + 1);
	interchangeLevel.debug(indent + 1);
	maxInterchangeLevel.debug(indent + 1);
	charSetList.debug(indent + 1);
	maxCharSetList.debug(indent + 1);
	fileSetNumber.debug(indent + 1);
	fileSetDescNumber.debug(indent + 1);
	logicalVolIdCharSet.debug(indent + 1);
	logicalVolId.debug(indent + 1);
	fileSetCharSet.debug(indent + 1);
	fileSetId.debug(indent + 1);
	copyrightFileId.debug(indent + 1);
	abstractFileId.debug(indent + 1);
	rootDirectoryICB.debug(indent + 1);
	domainId.debug(indent + 1);
	nextExtent.debug(indent + 1);
	systemStreamDirectoryICB.debug(indent + 1);
	reserved.debug(indent + 1);
    }
//begin:add your code here
    public int getFixedTagId() { return 256; }
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	final short category = UDF_Error.C_ECMA167;
	UDF_Error ret;
	
	
	el.addError(super.verify());  // Descriptor Tag
	
	ret = verifyId();
	if(ret.isError()){
	    
	    ret.setRefer("4/14.1.1");
	    el.addError(ret);
	}
	
	el.addError(recordingDateAndTime.verify("Recording Date and Time"));
	
	el.addError(rootDirectoryICB.verify("Root Directory ICB"));
	el.addError(domainId.verify("Domain Identifier"));
	el.addError(nextExtent.verify("Next Extent"));
	el.addError(systemStreamDirectoryICB.verify("System Stream Directory ICB"));
	el.addError(copyrightFileId.verify("Copyright File Id"));
	el.addError(abstractFileId.verify("Abstract File Id"));
	
	
	if(!UDF_Util.isAllZero(reserved.getData())){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Reserved",
			 "This field shall be reserved for future standardisation and all bytes shall be set to #00.",
			 "4/14.1.18"));
	}
	
	el.setGlobalPoint(getGlobalPoint());
	el.setRName("File Set Descriptor");
	return el;
    }
    
    public void postReadHook(UDF_RandomAccess f) throws UDF_Exception{
	UDF_Encoding _logicalVolIdCharSet = getLogicalVolIdCharSet().genEncoding();
	UDF_Encoding _fileSetCharSet = getFileSetCharSet().genEncoding();
	getLogicalVolId().setEncoding(_logicalVolIdCharSet);
	getFileSetId().setEncoding(_fileSetCharSet);
	getCopyrightFileId().setEncoding(_fileSetCharSet);
	getAbstractFileId().setEncoding(_fileSetCharSet);

	//env.root_lbn = getRootDirectoryICB().getLbn();
	//env.root_partno = getRootDirectoryICB().getPartRefNo();
	//env.root_len = getRootDirectoryICB().getLen();

	getRootDirectoryICB().getExtentLen().setAttribute("ref", "ROOT.len");
	getRootDirectoryICB().getExtentLoc().getLogicalBlockNumber().setAttribute("ref", "ROOT.lbn");
	getRootDirectoryICB().getExtentLoc().getPartReferenceNumber().setAttribute("ref", "ROOT.partno");
	//env.stream_lbn = getSystemStreamDirectoryICB().getLbn();
	//env.stream_partno = getSystemStreamDirectoryICB().getPartRefNo();
	//env.stream_len = getSystemStreamDirectoryICB().getLen();

	getSystemStreamDirectoryICB().getExtentLen().setAttribute("ref", "SROOT.len");
	getSystemStreamDirectoryICB().getExtentLoc().getLogicalBlockNumber().setAttribute("ref", "SROOT.lbn");
	getSystemStreamDirectoryICB().getExtentLoc().getPartReferenceNumber().setAttribute("ref", "SROOT.partno");

    }

    public void recalc(short type, UDF_RandomAccess f){
	super.recalc(type, f);

	if(type == RECALC_ENV){
	    try{
		//env.fileSetCharSet = getFileSetCharSet().genEncoding();
		/*
		env.logicalVolIdCharSet = getLogicalVolIdCharSet().genEncoding();
		env.root_lbn = getRootDirectoryICB().getLbn();
		env.root_partno = getRootDirectoryICB().getPartRefNo();
		env.root_len = getRootDirectoryICB().getLen();
		env.stream_lbn = getSystemStreamDirectoryICB().getLbn();
		env.stream_partno = getSystemStreamDirectoryICB().getPartRefNo();
		env.stream_len = getSystemStreamDirectoryICB().getLen();
		*/
	    }
	    catch(Exception e){
		ignoreMsg("recalc", e);
	    }
	}

    }

//end:
};
