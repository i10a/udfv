/*
*/
package com.udfv.ecma119;

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
ECMA119/9.1で規定されるDirectoryRecordを表現するクラス。

<dl>

<dt>フィールド一覧</dt>
<dd>
<table border="1">
<tr><td>メンバ</td><td>型</td><td>サイズ</td></tr>
<tr><td><b>LenOfDirectoryRecord</b></td><td><b>UDF_uint8</b></td><td><i>1</i></td></tr>
<tr><td><b>ExtendedAttrRecordLen</b></td><td><b>UDF_uint8</b></td><td><i>1</i></td></tr>
<tr><td><b>LocOfExtent</b></td><td><b>UDF_uint32_lebe</b></td><td><i>locOfExtent.getSize()</i></td></tr>
<tr><td><b>DataLen</b></td><td><b>UDF_uint32_lebe</b></td><td><i>dataLen.getSize()</i></td></tr>
<tr><td><b>RecordingDateAndTime</b></td><td><b>UDF_bytes</b></td><td><i>7</i></td></tr>
<tr><td><b>FileFlags</b></td><td><b>UDF_uint8</b></td><td><i>1</i></td></tr>
<tr><td><b>FileUnitSize</b></td><td><b>UDF_uint8</b></td><td><i>1</i></td></tr>
<tr><td><b>InterleaveGapSize</b></td><td><b>UDF_uint8</b></td><td><i>1</i></td></tr>
<tr><td><b>VolSeqNumber</b></td><td><b>UDF_uint16_lebe</b></td><td><i>volSeqNumber.getSize()</i></td></tr>
<tr><td><b>LenOfFileId</b></td><td><b>UDF_uint8</b></td><td><i>1</i></td></tr>
<tr><td><b>FileId</b></td><td><b>UDF_bytes</b></td><td><i>getLenOfFileId().getIntValue()</i></td></tr>
<tr><td><b>PaddingField</b></td><td><b>UDF_pad</b></td><td><i>2</i></td></tr>
<tr><td><b>SystemUse</b></td><td><b>UDF_bytes</b></td><td><i>(getLenOfDirectoryRecord().getIntValue() - 33 - getFileId().getSize() - getPaddingField().getSize())</i></td></tr>
</table>
</dd>
</dl>
*/
public class UDF_ECMA119DirectoryRecord extends UDF_Element 
{

    /**
	このオブジェクトのパッケージ名を除いたクラス名を取得する。

	@return クラス名
    */
    public static String getUDFClassName( ) {
        return "UDF_ECMA119DirectoryRecord";
    }

    /**
	コンストラクタ。

	@param elem   親エレメント。
	@param prefix ネームスペース。
	@param name   エレメント名。
    */
    public UDF_ECMA119DirectoryRecord(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }
    /**
	このUDFエレメントのサイズを返す。

	@return UDFエレメントのサイズ。
    */
    public int getSize(){
	return 0+1+1+locOfExtent.getSize()+dataLen.getSize()+recordingDateAndTime.getSize()+1+1+1+volSeqNumber.getSize()+1+fileId.getSize()+paddingField.getSize()+systemUse.getSize();
    }

    /**
	このUDFエレメントのサイズをlong 型で返す。

	@return UDFエレメントのサイズ。
    */
    public long getLongSize(){
	return (long)0+1+1+locOfExtent.getSize()+dataLen.getSize()+recordingDateAndTime.getSize()+1+1+1+volSeqNumber.getSize()+1+fileId.getSize()+paddingField.getSize()+systemUse.getSize();
    }
    private UDF_uint8 lenOfDirectoryRecord;
    private UDF_uint8 extendedAttrRecordLen;
    private UDF_uint32_lebe locOfExtent;
    private UDF_uint32_lebe dataLen;
    private UDF_bytes recordingDateAndTime;
    private UDF_uint8 fileFlags;
    private UDF_uint8 fileUnitSize;
    private UDF_uint8 interleaveGapSize;
    private UDF_uint16_lebe volSeqNumber;
    private UDF_uint8 lenOfFileId;
    private UDF_bytes fileId;
    private UDF_pad paddingField;
    private UDF_bytes systemUse;

    /**
	lenOfDirectoryRecordを取得する。

	@return 取得したlenOfDirectoryRecord を返す。
    */
    public UDF_uint8 getLenOfDirectoryRecord(){return lenOfDirectoryRecord;}
    /**
	extendedAttrRecordLenを取得する。

	@return 取得したextendedAttrRecordLen を返す。
    */
    public UDF_uint8 getExtendedAttrRecordLen(){return extendedAttrRecordLen;}
    /**
	locOfExtentを取得する。

	@return 取得したlocOfExtent を返す。
    */
    public UDF_uint32_lebe getLocOfExtent(){return locOfExtent;}
    /**
	dataLenを取得する。

	@return 取得したdataLen を返す。
    */
    public UDF_uint32_lebe getDataLen(){return dataLen;}
    /**
	recordingDateAndTimeを取得する。

	@return 取得したrecordingDateAndTime を返す。
    */
    public UDF_bytes getRecordingDateAndTime(){return recordingDateAndTime;}
    /**
	fileFlagsを取得する。

	@return 取得したfileFlags を返す。
    */
    public UDF_uint8 getFileFlags(){return fileFlags;}
    /**
	fileUnitSizeを取得する。

	@return 取得したfileUnitSize を返す。
    */
    public UDF_uint8 getFileUnitSize(){return fileUnitSize;}
    /**
	interleaveGapSizeを取得する。

	@return 取得したinterleaveGapSize を返す。
    */
    public UDF_uint8 getInterleaveGapSize(){return interleaveGapSize;}
    /**
	volSeqNumberを取得する。

	@return 取得したvolSeqNumber を返す。
    */
    public UDF_uint16_lebe getVolSeqNumber(){return volSeqNumber;}
    /**
	lenOfFileIdを取得する。

	@return 取得したlenOfFileId を返す。
    */
    public UDF_uint8 getLenOfFileId(){return lenOfFileId;}
    /**
	fileIdを取得する。

	@return 取得したfileId を返す。
    */
    public UDF_bytes getFileId(){return fileId;}
    /**
	paddingFieldを取得する。

	@return 取得したpaddingField を返す。
    */
    public UDF_pad getPaddingField(){return paddingField;}
    /**
	systemUseを取得する。

	@return 取得したsystemUse を返す。
    */
    public UDF_bytes getSystemUse(){return systemUse;}

    /**
	lenOfDirectoryRecordを設定する。

	@param	v 設定する値。
    */
    public void setLenOfDirectoryRecord(UDF_uint8 v){replaceChild(v, lenOfDirectoryRecord); lenOfDirectoryRecord = v;}
    /**
	extendedAttrRecordLenを設定する。

	@param	v 設定する値。
    */
    public void setExtendedAttrRecordLen(UDF_uint8 v){replaceChild(v, extendedAttrRecordLen); extendedAttrRecordLen = v;}
    /**
	locOfExtentを設定する。

	@param	v 設定する値。
    */
    public void setLocOfExtent(UDF_uint32_lebe v){replaceChild(v, locOfExtent); locOfExtent = v;}
    /**
	dataLenを設定する。

	@param	v 設定する値。
    */
    public void setDataLen(UDF_uint32_lebe v){replaceChild(v, dataLen); dataLen = v;}
    /**
	recordingDateAndTimeを設定する。

	@param	v 設定する値。
    */
    public void setRecordingDateAndTime(UDF_bytes v){replaceChild(v, recordingDateAndTime); recordingDateAndTime = v;}
    /**
	fileFlagsを設定する。

	@param	v 設定する値。
    */
    public void setFileFlags(UDF_uint8 v){replaceChild(v, fileFlags); fileFlags = v;}
    /**
	fileUnitSizeを設定する。

	@param	v 設定する値。
    */
    public void setFileUnitSize(UDF_uint8 v){replaceChild(v, fileUnitSize); fileUnitSize = v;}
    /**
	interleaveGapSizeを設定する。

	@param	v 設定する値。
    */
    public void setInterleaveGapSize(UDF_uint8 v){replaceChild(v, interleaveGapSize); interleaveGapSize = v;}
    /**
	volSeqNumberを設定する。

	@param	v 設定する値。
    */
    public void setVolSeqNumber(UDF_uint16_lebe v){replaceChild(v, volSeqNumber); volSeqNumber = v;}
    /**
	lenOfFileIdを設定する。

	@param	v 設定する値。
    */
    public void setLenOfFileId(UDF_uint8 v){replaceChild(v, lenOfFileId); lenOfFileId = v;}
    /**
	fileIdを設定する。

	@param	v 設定する値。
    */
    public void setFileId(UDF_bytes v){replaceChild(v, fileId); fileId = v;}
    /**
	paddingFieldを設定する。

	@param	v 設定する値。
    */
    public void setPaddingField(UDF_pad v){replaceChild(v, paddingField); paddingField = v;}
    /**
	systemUseを設定する。

	@param	v 設定する値。
    */
    public void setSystemUse(UDF_bytes v){replaceChild(v, systemUse); systemUse = v;}

    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long rsize = 0;
	preReadHook(f);
	lenOfDirectoryRecord = (UDF_uint8)createElement("UDF_uint8", "ecma119", "len-of-directory-record");
	rsize += lenOfDirectoryRecord.readFrom(f);
	extendedAttrRecordLen = (UDF_uint8)createElement("UDF_uint8", "ecma119", "extended-attr-record-len");
	rsize += extendedAttrRecordLen.readFrom(f);
	locOfExtent = (UDF_uint32_lebe)createElement("UDF_uint32_lebe", "ecma119", "loc-of-extent");
	rsize += locOfExtent.readFrom(f);
	dataLen = (UDF_uint32_lebe)createElement("UDF_uint32_lebe", "ecma119", "data-len");
	rsize += dataLen.readFrom(f);
	recordingDateAndTime = (UDF_bytes)createElement("UDF_bytes", "ecma119", "recording-date-and-time");
	recordingDateAndTime.setSize(7);
	rsize += recordingDateAndTime.readFrom(f);
	fileFlags = (UDF_uint8)createElement("UDF_uint8", "ecma119", "file-flags");
	rsize += fileFlags.readFrom(f);
	fileUnitSize = (UDF_uint8)createElement("UDF_uint8", "ecma119", "file-unit-size");
	rsize += fileUnitSize.readFrom(f);
	interleaveGapSize = (UDF_uint8)createElement("UDF_uint8", "ecma119", "interleave-gap-size");
	rsize += interleaveGapSize.readFrom(f);
	volSeqNumber = (UDF_uint16_lebe)createElement("UDF_uint16_lebe", "ecma119", "vol-seq-number");
	rsize += volSeqNumber.readFrom(f);
	lenOfFileId = (UDF_uint8)createElement("UDF_uint8", "ecma119", "len-of-file-id");
	rsize += lenOfFileId.readFrom(f);
	fileId = (UDF_bytes)createElement("UDF_bytes", "ecma119", "file-id");
	fileId.setSize(getLenOfFileId().getIntValue());
	rsize += fileId.readFrom(f);
	paddingField = (UDF_pad)createElement("UDF_pad", "ecma119", "padding-field");
	paddingField.setAlign(2);
	rsize += paddingField.readFrom(f);
	systemUse = (UDF_bytes)createElement("UDF_bytes", "ecma119", "system-use");
	systemUse.setSize((getLenOfDirectoryRecord().getIntValue() - 33 - getFileId().getSize() - getPaddingField().getSize()));
	rsize += systemUse.readFrom(f);
	apply( );
	postReadHook(f);
	return rsize;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long wsize = 0;
	wsize += lenOfDirectoryRecord.writeTo(f);
	wsize += extendedAttrRecordLen.writeTo(f);
	wsize += locOfExtent.writeTo(f);
	wsize += dataLen.writeTo(f);
	wsize += recordingDateAndTime.writeTo(f);
	wsize += fileFlags.writeTo(f);
	wsize += fileUnitSize.writeTo(f);
	wsize += interleaveGapSize.writeTo(f);
	wsize += volSeqNumber.writeTo(f);
	wsize += lenOfFileId.writeTo(f);
	wsize += fileId.writeTo(f);
	wsize += paddingField.writeTo(f);
	wsize += systemUse.writeTo(f);
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
	    else if(name.equals("len-of-directory-record")){
		lenOfDirectoryRecord = (UDF_uint8)createElement("UDF_uint8", "ecma119", "len-of-directory-record");
		lenOfDirectoryRecord.readFromXML(child);
	    }
	    else if(name.equals("extended-attr-record-len")){
		extendedAttrRecordLen = (UDF_uint8)createElement("UDF_uint8", "ecma119", "extended-attr-record-len");
		extendedAttrRecordLen.readFromXML(child);
	    }
	    else if(name.equals("loc-of-extent")){
		locOfExtent = (UDF_uint32_lebe)createElement("UDF_uint32_lebe", "ecma119", "loc-of-extent");
		locOfExtent.readFromXML(child);
	    }
	    else if(name.equals("data-len")){
		dataLen = (UDF_uint32_lebe)createElement("UDF_uint32_lebe", "ecma119", "data-len");
		dataLen.readFromXML(child);
	    }
	    else if(name.equals("recording-date-and-time")){
		recordingDateAndTime = (UDF_bytes)createElement("UDF_bytes", "ecma119", "recording-date-and-time");
		recordingDateAndTime.setSize(7);
		recordingDateAndTime.readFromXML(child);
	    }
	    else if(name.equals("file-flags")){
		fileFlags = (UDF_uint8)createElement("UDF_uint8", "ecma119", "file-flags");
		fileFlags.readFromXML(child);
	    }
	    else if(name.equals("file-unit-size")){
		fileUnitSize = (UDF_uint8)createElement("UDF_uint8", "ecma119", "file-unit-size");
		fileUnitSize.readFromXML(child);
	    }
	    else if(name.equals("interleave-gap-size")){
		interleaveGapSize = (UDF_uint8)createElement("UDF_uint8", "ecma119", "interleave-gap-size");
		interleaveGapSize.readFromXML(child);
	    }
	    else if(name.equals("vol-seq-number")){
		volSeqNumber = (UDF_uint16_lebe)createElement("UDF_uint16_lebe", "ecma119", "vol-seq-number");
		volSeqNumber.readFromXML(child);
	    }
	    else if(name.equals("len-of-file-id")){
		lenOfFileId = (UDF_uint8)createElement("UDF_uint8", "ecma119", "len-of-file-id");
		lenOfFileId.readFromXML(child);
	    }
	    else if(name.equals("file-id")){
		fileId = (UDF_bytes)createElement("UDF_bytes", "ecma119", "file-id");
		fileId.setSize(getLenOfFileId().getIntValue());
		fileId.readFromXML(child);
	    }
	    else if(name.equals("padding-field")){
		paddingField = (UDF_pad)createElement("UDF_pad", "ecma119", "padding-field");
		paddingField.setAlign(2);
		paddingField.readFromXML(child);
	    }
	    else if(name.equals("system-use")){
		systemUse = (UDF_bytes)createElement("UDF_bytes", "ecma119", "system-use");
		systemUse.setSize((getLenOfDirectoryRecord().getIntValue() - 33 - getFileId().getSize() - getPaddingField().getSize()));
		systemUse.readFromXML(child);
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
	lenOfDirectoryRecord = (UDF_uint8)createElement("UDF_uint8", "ecma119", "len-of-directory-record");
	lenOfDirectoryRecord.setDefaultValue();
	extendedAttrRecordLen = (UDF_uint8)createElement("UDF_uint8", "ecma119", "extended-attr-record-len");
	extendedAttrRecordLen.setDefaultValue();
	locOfExtent = (UDF_uint32_lebe)createElement("UDF_uint32_lebe", "ecma119", "loc-of-extent");
	locOfExtent.setDefaultValue();
	dataLen = (UDF_uint32_lebe)createElement("UDF_uint32_lebe", "ecma119", "data-len");
	dataLen.setDefaultValue();
	recordingDateAndTime = (UDF_bytes)createElement("UDF_bytes", "ecma119", "recording-date-and-time");
	recordingDateAndTime.setSize(7);
	recordingDateAndTime.setDefaultValue();
	fileFlags = (UDF_uint8)createElement("UDF_uint8", "ecma119", "file-flags");
	fileFlags.setDefaultValue();
	fileUnitSize = (UDF_uint8)createElement("UDF_uint8", "ecma119", "file-unit-size");
	fileUnitSize.setDefaultValue();
	interleaveGapSize = (UDF_uint8)createElement("UDF_uint8", "ecma119", "interleave-gap-size");
	interleaveGapSize.setDefaultValue();
	volSeqNumber = (UDF_uint16_lebe)createElement("UDF_uint16_lebe", "ecma119", "vol-seq-number");
	volSeqNumber.setDefaultValue();
	lenOfFileId = (UDF_uint8)createElement("UDF_uint8", "ecma119", "len-of-file-id");
	lenOfFileId.setDefaultValue();
	fileId = (UDF_bytes)createElement("UDF_bytes", "ecma119", "file-id");
	fileId.setSize(getLenOfFileId().getIntValue());
	fileId.setDefaultValue();
	paddingField = (UDF_pad)createElement("UDF_pad", "ecma119", "padding-field");
	paddingField.setAlign(2);
	paddingField.setDefaultValue();
	systemUse = (UDF_bytes)createElement("UDF_bytes", "ecma119", "system-use");
	systemUse.setSize((getLenOfDirectoryRecord().getIntValue() - 33 - getFileId().getSize() - getPaddingField().getSize()));
	systemUse.setDefaultValue();

	apply( );
	postSetDefaultValueHook();
    }


    /**
	複製する
    */
    public UDF_Element duplicateElement(){
	UDF_ECMA119DirectoryRecord dup = new UDF_ECMA119DirectoryRecord(this, null, getName());
	dup.setDefaultValue();
	dup.duplicateHook(this);
	dup.setLenOfDirectoryRecord((UDF_uint8)lenOfDirectoryRecord.duplicateElement());
	dup.setExtendedAttrRecordLen((UDF_uint8)extendedAttrRecordLen.duplicateElement());
	dup.setLocOfExtent((UDF_uint32_lebe)locOfExtent.duplicateElement());
	dup.setDataLen((UDF_uint32_lebe)dataLen.duplicateElement());
	dup.setRecordingDateAndTime((UDF_bytes)recordingDateAndTime.duplicateElement());
	dup.setFileFlags((UDF_uint8)fileFlags.duplicateElement());
	dup.setFileUnitSize((UDF_uint8)fileUnitSize.duplicateElement());
	dup.setInterleaveGapSize((UDF_uint8)interleaveGapSize.duplicateElement());
	dup.setVolSeqNumber((UDF_uint16_lebe)volSeqNumber.duplicateElement());
	dup.setLenOfFileId((UDF_uint8)lenOfFileId.duplicateElement());
	dup.setFileId((UDF_bytes)fileId.duplicateElement());
	dup.setPaddingField((UDF_pad)paddingField.duplicateElement());
	dup.setSystemUse((UDF_bytes)systemUse.duplicateElement());

	apply();

	return dup;
    }

    /**
        子の UDF Element の Node を append しなおす。

    */
    private void apply( ) {

	removeAllChildren();
	appendChild(lenOfDirectoryRecord);
	appendChild(extendedAttrRecordLen);
	appendChild(locOfExtent);
	appendChild(dataLen);
	appendChild(recordingDateAndTime);
	appendChild(fileFlags);
	appendChild(fileUnitSize);
	appendChild(interleaveGapSize);
	appendChild(volSeqNumber);
	appendChild(lenOfFileId);
	appendChild(fileId);
	appendChild(paddingField);
	appendChild(systemUse);
    }

    public String getInfo(int indent){
	String a = "";
	a += UDF_Util.repeat(' ', indent);
	a += lenOfDirectoryRecord.getInfo(indent + 1);
	a += extendedAttrRecordLen.getInfo(indent + 1);
	a += locOfExtent.getInfo(indent + 1);
	a += dataLen.getInfo(indent + 1);
	a += recordingDateAndTime.getInfo(indent + 1);
	a += fileFlags.getInfo(indent + 1);
	a += fileUnitSize.getInfo(indent + 1);
	a += interleaveGapSize.getInfo(indent + 1);
	a += volSeqNumber.getInfo(indent + 1);
	a += lenOfFileId.getInfo(indent + 1);
	a += fileId.getInfo(indent + 1);
	a += paddingField.getInfo(indent + 1);
	a += systemUse.getInfo(indent + 1);
      return a;
    }
    public void debug(int indent){
	System.err.print(UDF_Util.repeat(' ', indent));

	String info = " gp=" + getGlobalPoint();
	info += " pn=" + getElemPartRefNo();
	info += " sn=" + getPartSubno();
	System.err.println(getName() + ": (" + hashCode() + ")" + info);	lenOfDirectoryRecord.debug(indent + 1);
	extendedAttrRecordLen.debug(indent + 1);
	locOfExtent.debug(indent + 1);
	dataLen.debug(indent + 1);
	recordingDateAndTime.debug(indent + 1);
	fileFlags.debug(indent + 1);
	fileUnitSize.debug(indent + 1);
	interleaveGapSize.debug(indent + 1);
	volSeqNumber.debug(indent + 1);
	lenOfFileId.debug(indent + 1);
	fileId.debug(indent + 1);
	paddingField.debug(indent + 1);
	systemUse.debug(indent + 1);
    }
//begin:add your code here
//end:
};
