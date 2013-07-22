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
ECMA119/8.4で規定されるPrimaryVolumeDescriptorを表現するクラス。

<dl>

<dt>フィールド一覧</dt>
<dd>
<table border="1">
<tr><td>メンバ</td><td>型</td><td>サイズ</td></tr>
<tr><td><b>VolDescType</b></td><td><b>UDF_uint8</b></td><td><i>1</i></td></tr>
<tr><td><b>StandardId</b></td><td><b>UDF_bytes</b></td><td><i>5</i></td></tr>
<tr><td><b>VolDescVersion</b></td><td><b>UDF_uint8</b></td><td><i>1</i></td></tr>
<tr><td><b>UnusedField0</b></td><td><b>UDF_bytes</b></td><td><i>1</i></td></tr>
<tr><td><b>SystemId</b></td><td><b>UDF_bytes</b></td><td><i>32</i></td></tr>
<tr><td><b>VolId</b></td><td><b>UDF_bytes</b></td><td><i>32</i></td></tr>
<tr><td><b>UnusedField1</b></td><td><b>UDF_bytes</b></td><td><i>8</i></td></tr>
<tr><td><b>VolSpaceSize</b></td><td><b>UDF_uint32_lebe</b></td><td><i>volSpaceSize.getSize()</i></td></tr>
<tr><td><b>UnusedField2</b></td><td><b>UDF_bytes</b></td><td><i>32</i></td></tr>
<tr><td><b>VolSetSize</b></td><td><b>UDF_uint16_lebe</b></td><td><i>volSetSize.getSize()</i></td></tr>
<tr><td><b>VolSeqNumber</b></td><td><b>UDF_uint16_lebe</b></td><td><i>volSeqNumber.getSize()</i></td></tr>
<tr><td><b>LogicalBlockSize</b></td><td><b>UDF_uint16_lebe</b></td><td><i>logicalBlockSize.getSize()</i></td></tr>
<tr><td><b>PathTableSize</b></td><td><b>UDF_uint32_lebe</b></td><td><i>pathTableSize.getSize()</i></td></tr>
<tr><td><b>LocOfOccurenceOfTypeLPathTable</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>LocOfOptionalOccurenceOfTypeLPathTable</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>LocOfOccurenceOfTypeMPathTable</b></td><td><b>UDF_uint32_be</b></td><td><i>locOfOccurenceOfTypeMPathTable.getSize()</i></td></tr>
<tr><td><b>LocOfOptionalOccurenceOfTypeMPathTable</b></td><td><b>UDF_uint32_be</b></td><td><i>locOfOptionalOccurenceOfTypeMPathTable.getSize()</i></td></tr>
<tr><td><b>DirectoryRecordForRootDirectory</b></td><td><b>UDF_ECMA119DirectoryRecord</b></td><td><i>directoryRecordForRootDirectory.getSize()</i></td></tr>
<tr><td><b>VolSetId</b></td><td><b>UDF_bytes</b></td><td><i>128</i></td></tr>
<tr><td><b>PublishedId</b></td><td><b>UDF_bytes</b></td><td><i>128</i></td></tr>
<tr><td><b>DataPreparerId</b></td><td><b>UDF_bytes</b></td><td><i>128</i></td></tr>
<tr><td><b>ApplicationId</b></td><td><b>UDF_bytes</b></td><td><i>128</i></td></tr>
<tr><td><b>CopyrightFileId</b></td><td><b>UDF_bytes</b></td><td><i>37</i></td></tr>
<tr><td><b>AbstractFileId</b></td><td><b>UDF_bytes</b></td><td><i>37</i></td></tr>
<tr><td><b>BibliographicFileId</b></td><td><b>UDF_bytes</b></td><td><i>37</i></td></tr>
<tr><td><b>VolCreationDateAndTime</b></td><td><b>UDF_bytes</b></td><td><i>17</i></td></tr>
<tr><td><b>VolModificationDateAndTime</b></td><td><b>UDF_bytes</b></td><td><i>17</i></td></tr>
<tr><td><b>VolExpirationDateAndTime</b></td><td><b>UDF_bytes</b></td><td><i>17</i></td></tr>
<tr><td><b>VolEffectiveDateAndTime</b></td><td><b>UDF_bytes</b></td><td><i>17</i></td></tr>
<tr><td><b>FileStructureVersion</b></td><td><b>UDF_uint8</b></td><td><i>1</i></td></tr>
<tr><td><b>Reserved</b></td><td><b>UDF_bytes</b></td><td><i>1</i></td></tr>
<tr><td><b>ApplicationUse</b></td><td><b>UDF_bytes</b></td><td><i>512</i></td></tr>
<tr><td><b>Reserved2</b></td><td><b>UDF_bytes</b></td><td><i>653</i></td></tr>
</table>
</dd>
</dl>
*/
public class UDF_ECMA119_CD001_1 extends UDF_Element 
{

    /**
	このオブジェクトのパッケージ名を除いたクラス名を取得する。

	@return クラス名
    */
    public static String getUDFClassName( ) {
        return "UDF_ECMA119_CD001_1";
    }

    /**
	コンストラクタ。

	@param elem   親エレメント。
	@param prefix ネームスペース。
	@param name   エレメント名。
    */
    public UDF_ECMA119_CD001_1(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }
    /**
	このUDFエレメントのサイズを返す。

	@return UDFエレメントのサイズ。
    */
    public int getSize(){
	return 0+1+standardId.getSize()+1+unusedField0.getSize()+systemId.getSize()+volId.getSize()+unusedField1.getSize()+volSpaceSize.getSize()+unusedField2.getSize()+volSetSize.getSize()+volSeqNumber.getSize()+logicalBlockSize.getSize()+pathTableSize.getSize()+4+4+locOfOccurenceOfTypeMPathTable.getSize()+locOfOptionalOccurenceOfTypeMPathTable.getSize()+directoryRecordForRootDirectory.getSize()+volSetId.getSize()+publishedId.getSize()+dataPreparerId.getSize()+applicationId.getSize()+copyrightFileId.getSize()+abstractFileId.getSize()+bibliographicFileId.getSize()+volCreationDateAndTime.getSize()+volModificationDateAndTime.getSize()+volExpirationDateAndTime.getSize()+volEffectiveDateAndTime.getSize()+1+reserved.getSize()+applicationUse.getSize()+reserved2.getSize();
    }

    /**
	このUDFエレメントのサイズをlong 型で返す。

	@return UDFエレメントのサイズ。
    */
    public long getLongSize(){
	return (long)0+1+standardId.getSize()+1+unusedField0.getSize()+systemId.getSize()+volId.getSize()+unusedField1.getSize()+volSpaceSize.getSize()+unusedField2.getSize()+volSetSize.getSize()+volSeqNumber.getSize()+logicalBlockSize.getSize()+pathTableSize.getSize()+4+4+locOfOccurenceOfTypeMPathTable.getSize()+locOfOptionalOccurenceOfTypeMPathTable.getSize()+directoryRecordForRootDirectory.getSize()+volSetId.getSize()+publishedId.getSize()+dataPreparerId.getSize()+applicationId.getSize()+copyrightFileId.getSize()+abstractFileId.getSize()+bibliographicFileId.getSize()+volCreationDateAndTime.getSize()+volModificationDateAndTime.getSize()+volExpirationDateAndTime.getSize()+volEffectiveDateAndTime.getSize()+1+reserved.getSize()+applicationUse.getSize()+reserved2.getSize();
    }
    private UDF_uint8 volDescType;
    private UDF_bytes standardId;
    private UDF_uint8 volDescVersion;
    private UDF_bytes unusedField0;
    private UDF_bytes systemId;
    private UDF_bytes volId;
    private UDF_bytes unusedField1;
    private UDF_uint32_lebe volSpaceSize;
    private UDF_bytes unusedField2;
    private UDF_uint16_lebe volSetSize;
    private UDF_uint16_lebe volSeqNumber;
    private UDF_uint16_lebe logicalBlockSize;
    private UDF_uint32_lebe pathTableSize;
    private UDF_uint32 locOfOccurenceOfTypeLPathTable;
    private UDF_uint32 locOfOptionalOccurenceOfTypeLPathTable;
    private UDF_uint32_be locOfOccurenceOfTypeMPathTable;
    private UDF_uint32_be locOfOptionalOccurenceOfTypeMPathTable;
    private UDF_ECMA119DirectoryRecord directoryRecordForRootDirectory;
    private UDF_bytes volSetId;
    private UDF_bytes publishedId;
    private UDF_bytes dataPreparerId;
    private UDF_bytes applicationId;
    private UDF_bytes copyrightFileId;
    private UDF_bytes abstractFileId;
    private UDF_bytes bibliographicFileId;
    private UDF_bytes volCreationDateAndTime;
    private UDF_bytes volModificationDateAndTime;
    private UDF_bytes volExpirationDateAndTime;
    private UDF_bytes volEffectiveDateAndTime;
    private UDF_uint8 fileStructureVersion;
    private UDF_bytes reserved;
    private UDF_bytes applicationUse;
    private UDF_bytes reserved2;

    /**
	volDescTypeを取得する。

	@return 取得したvolDescType を返す。
    */
    public UDF_uint8 getVolDescType(){return volDescType;}
    /**
	standardIdを取得する。

	@return 取得したstandardId を返す。
    */
    public UDF_bytes getStandardId(){return standardId;}
    /**
	volDescVersionを取得する。

	@return 取得したvolDescVersion を返す。
    */
    public UDF_uint8 getVolDescVersion(){return volDescVersion;}
    /**
	unusedField0を取得する。

	@return 取得したunusedField0 を返す。
    */
    public UDF_bytes getUnusedField0(){return unusedField0;}
    /**
	systemIdを取得する。

	@return 取得したsystemId を返す。
    */
    public UDF_bytes getSystemId(){return systemId;}
    /**
	volIdを取得する。

	@return 取得したvolId を返す。
    */
    public UDF_bytes getVolId(){return volId;}
    /**
	unusedField1を取得する。

	@return 取得したunusedField1 を返す。
    */
    public UDF_bytes getUnusedField1(){return unusedField1;}
    /**
	volSpaceSizeを取得する。

	@return 取得したvolSpaceSize を返す。
    */
    public UDF_uint32_lebe getVolSpaceSize(){return volSpaceSize;}
    /**
	unusedField2を取得する。

	@return 取得したunusedField2 を返す。
    */
    public UDF_bytes getUnusedField2(){return unusedField2;}
    /**
	volSetSizeを取得する。

	@return 取得したvolSetSize を返す。
    */
    public UDF_uint16_lebe getVolSetSize(){return volSetSize;}
    /**
	volSeqNumberを取得する。

	@return 取得したvolSeqNumber を返す。
    */
    public UDF_uint16_lebe getVolSeqNumber(){return volSeqNumber;}
    /**
	logicalBlockSizeを取得する。

	@return 取得したlogicalBlockSize を返す。
    */
    public UDF_uint16_lebe getLogicalBlockSize(){return logicalBlockSize;}
    /**
	pathTableSizeを取得する。

	@return 取得したpathTableSize を返す。
    */
    public UDF_uint32_lebe getPathTableSize(){return pathTableSize;}
    /**
	locOfOccurenceOfTypeLPathTableを取得する。

	@return 取得したlocOfOccurenceOfTypeLPathTable を返す。
    */
    public UDF_uint32 getLocOfOccurenceOfTypeLPathTable(){return locOfOccurenceOfTypeLPathTable;}
    /**
	locOfOptionalOccurenceOfTypeLPathTableを取得する。

	@return 取得したlocOfOptionalOccurenceOfTypeLPathTable を返す。
    */
    public UDF_uint32 getLocOfOptionalOccurenceOfTypeLPathTable(){return locOfOptionalOccurenceOfTypeLPathTable;}
    /**
	locOfOccurenceOfTypeMPathTableを取得する。

	@return 取得したlocOfOccurenceOfTypeMPathTable を返す。
    */
    public UDF_uint32_be getLocOfOccurenceOfTypeMPathTable(){return locOfOccurenceOfTypeMPathTable;}
    /**
	locOfOptionalOccurenceOfTypeMPathTableを取得する。

	@return 取得したlocOfOptionalOccurenceOfTypeMPathTable を返す。
    */
    public UDF_uint32_be getLocOfOptionalOccurenceOfTypeMPathTable(){return locOfOptionalOccurenceOfTypeMPathTable;}
    /**
	directoryRecordForRootDirectoryを取得する。

	@return 取得したdirectoryRecordForRootDirectory を返す。
    */
    public UDF_ECMA119DirectoryRecord getDirectoryRecordForRootDirectory(){return directoryRecordForRootDirectory;}
    /**
	volSetIdを取得する。

	@return 取得したvolSetId を返す。
    */
    public UDF_bytes getVolSetId(){return volSetId;}
    /**
	publishedIdを取得する。

	@return 取得したpublishedId を返す。
    */
    public UDF_bytes getPublishedId(){return publishedId;}
    /**
	dataPreparerIdを取得する。

	@return 取得したdataPreparerId を返す。
    */
    public UDF_bytes getDataPreparerId(){return dataPreparerId;}
    /**
	applicationIdを取得する。

	@return 取得したapplicationId を返す。
    */
    public UDF_bytes getApplicationId(){return applicationId;}
    /**
	copyrightFileIdを取得する。

	@return 取得したcopyrightFileId を返す。
    */
    public UDF_bytes getCopyrightFileId(){return copyrightFileId;}
    /**
	abstractFileIdを取得する。

	@return 取得したabstractFileId を返す。
    */
    public UDF_bytes getAbstractFileId(){return abstractFileId;}
    /**
	bibliographicFileIdを取得する。

	@return 取得したbibliographicFileId を返す。
    */
    public UDF_bytes getBibliographicFileId(){return bibliographicFileId;}
    /**
	volCreationDateAndTimeを取得する。

	@return 取得したvolCreationDateAndTime を返す。
    */
    public UDF_bytes getVolCreationDateAndTime(){return volCreationDateAndTime;}
    /**
	volModificationDateAndTimeを取得する。

	@return 取得したvolModificationDateAndTime を返す。
    */
    public UDF_bytes getVolModificationDateAndTime(){return volModificationDateAndTime;}
    /**
	volExpirationDateAndTimeを取得する。

	@return 取得したvolExpirationDateAndTime を返す。
    */
    public UDF_bytes getVolExpirationDateAndTime(){return volExpirationDateAndTime;}
    /**
	volEffectiveDateAndTimeを取得する。

	@return 取得したvolEffectiveDateAndTime を返す。
    */
    public UDF_bytes getVolEffectiveDateAndTime(){return volEffectiveDateAndTime;}
    /**
	fileStructureVersionを取得する。

	@return 取得したfileStructureVersion を返す。
    */
    public UDF_uint8 getFileStructureVersion(){return fileStructureVersion;}
    /**
	reservedを取得する。

	@return 取得したreserved を返す。
    */
    public UDF_bytes getReserved(){return reserved;}
    /**
	applicationUseを取得する。

	@return 取得したapplicationUse を返す。
    */
    public UDF_bytes getApplicationUse(){return applicationUse;}
    /**
	reserved2を取得する。

	@return 取得したreserved2 を返す。
    */
    public UDF_bytes getReserved2(){return reserved2;}

    /**
	volDescTypeを設定する。

	@param	v 設定する値。
    */
    public void setVolDescType(UDF_uint8 v){replaceChild(v, volDescType); volDescType = v;}
    /**
	standardIdを設定する。

	@param	v 設定する値。
    */
    public void setStandardId(UDF_bytes v){replaceChild(v, standardId); standardId = v;}
    /**
	volDescVersionを設定する。

	@param	v 設定する値。
    */
    public void setVolDescVersion(UDF_uint8 v){replaceChild(v, volDescVersion); volDescVersion = v;}
    /**
	unusedField0を設定する。

	@param	v 設定する値。
    */
    public void setUnusedField0(UDF_bytes v){replaceChild(v, unusedField0); unusedField0 = v;}
    /**
	systemIdを設定する。

	@param	v 設定する値。
    */
    public void setSystemId(UDF_bytes v){replaceChild(v, systemId); systemId = v;}
    /**
	volIdを設定する。

	@param	v 設定する値。
    */
    public void setVolId(UDF_bytes v){replaceChild(v, volId); volId = v;}
    /**
	unusedField1を設定する。

	@param	v 設定する値。
    */
    public void setUnusedField1(UDF_bytes v){replaceChild(v, unusedField1); unusedField1 = v;}
    /**
	volSpaceSizeを設定する。

	@param	v 設定する値。
    */
    public void setVolSpaceSize(UDF_uint32_lebe v){replaceChild(v, volSpaceSize); volSpaceSize = v;}
    /**
	unusedField2を設定する。

	@param	v 設定する値。
    */
    public void setUnusedField2(UDF_bytes v){replaceChild(v, unusedField2); unusedField2 = v;}
    /**
	volSetSizeを設定する。

	@param	v 設定する値。
    */
    public void setVolSetSize(UDF_uint16_lebe v){replaceChild(v, volSetSize); volSetSize = v;}
    /**
	volSeqNumberを設定する。

	@param	v 設定する値。
    */
    public void setVolSeqNumber(UDF_uint16_lebe v){replaceChild(v, volSeqNumber); volSeqNumber = v;}
    /**
	logicalBlockSizeを設定する。

	@param	v 設定する値。
    */
    public void setLogicalBlockSize(UDF_uint16_lebe v){replaceChild(v, logicalBlockSize); logicalBlockSize = v;}
    /**
	pathTableSizeを設定する。

	@param	v 設定する値。
    */
    public void setPathTableSize(UDF_uint32_lebe v){replaceChild(v, pathTableSize); pathTableSize = v;}
    /**
	locOfOccurenceOfTypeLPathTableを設定する。

	@param	v 設定する値。
    */
    public void setLocOfOccurenceOfTypeLPathTable(UDF_uint32 v){replaceChild(v, locOfOccurenceOfTypeLPathTable); locOfOccurenceOfTypeLPathTable = v;}
    /**
	locOfOptionalOccurenceOfTypeLPathTableを設定する。

	@param	v 設定する値。
    */
    public void setLocOfOptionalOccurenceOfTypeLPathTable(UDF_uint32 v){replaceChild(v, locOfOptionalOccurenceOfTypeLPathTable); locOfOptionalOccurenceOfTypeLPathTable = v;}
    /**
	locOfOccurenceOfTypeMPathTableを設定する。

	@param	v 設定する値。
    */
    public void setLocOfOccurenceOfTypeMPathTable(UDF_uint32_be v){replaceChild(v, locOfOccurenceOfTypeMPathTable); locOfOccurenceOfTypeMPathTable = v;}
    /**
	locOfOptionalOccurenceOfTypeMPathTableを設定する。

	@param	v 設定する値。
    */
    public void setLocOfOptionalOccurenceOfTypeMPathTable(UDF_uint32_be v){replaceChild(v, locOfOptionalOccurenceOfTypeMPathTable); locOfOptionalOccurenceOfTypeMPathTable = v;}
    /**
	directoryRecordForRootDirectoryを設定する。

	@param	v 設定する値。
    */
    public void setDirectoryRecordForRootDirectory(UDF_ECMA119DirectoryRecord v){replaceChild(v, directoryRecordForRootDirectory); directoryRecordForRootDirectory = v;}
    /**
	volSetIdを設定する。

	@param	v 設定する値。
    */
    public void setVolSetId(UDF_bytes v){replaceChild(v, volSetId); volSetId = v;}
    /**
	publishedIdを設定する。

	@param	v 設定する値。
    */
    public void setPublishedId(UDF_bytes v){replaceChild(v, publishedId); publishedId = v;}
    /**
	dataPreparerIdを設定する。

	@param	v 設定する値。
    */
    public void setDataPreparerId(UDF_bytes v){replaceChild(v, dataPreparerId); dataPreparerId = v;}
    /**
	applicationIdを設定する。

	@param	v 設定する値。
    */
    public void setApplicationId(UDF_bytes v){replaceChild(v, applicationId); applicationId = v;}
    /**
	copyrightFileIdを設定する。

	@param	v 設定する値。
    */
    public void setCopyrightFileId(UDF_bytes v){replaceChild(v, copyrightFileId); copyrightFileId = v;}
    /**
	abstractFileIdを設定する。

	@param	v 設定する値。
    */
    public void setAbstractFileId(UDF_bytes v){replaceChild(v, abstractFileId); abstractFileId = v;}
    /**
	bibliographicFileIdを設定する。

	@param	v 設定する値。
    */
    public void setBibliographicFileId(UDF_bytes v){replaceChild(v, bibliographicFileId); bibliographicFileId = v;}
    /**
	volCreationDateAndTimeを設定する。

	@param	v 設定する値。
    */
    public void setVolCreationDateAndTime(UDF_bytes v){replaceChild(v, volCreationDateAndTime); volCreationDateAndTime = v;}
    /**
	volModificationDateAndTimeを設定する。

	@param	v 設定する値。
    */
    public void setVolModificationDateAndTime(UDF_bytes v){replaceChild(v, volModificationDateAndTime); volModificationDateAndTime = v;}
    /**
	volExpirationDateAndTimeを設定する。

	@param	v 設定する値。
    */
    public void setVolExpirationDateAndTime(UDF_bytes v){replaceChild(v, volExpirationDateAndTime); volExpirationDateAndTime = v;}
    /**
	volEffectiveDateAndTimeを設定する。

	@param	v 設定する値。
    */
    public void setVolEffectiveDateAndTime(UDF_bytes v){replaceChild(v, volEffectiveDateAndTime); volEffectiveDateAndTime = v;}
    /**
	fileStructureVersionを設定する。

	@param	v 設定する値。
    */
    public void setFileStructureVersion(UDF_uint8 v){replaceChild(v, fileStructureVersion); fileStructureVersion = v;}
    /**
	reservedを設定する。

	@param	v 設定する値。
    */
    public void setReserved(UDF_bytes v){replaceChild(v, reserved); reserved = v;}
    /**
	applicationUseを設定する。

	@param	v 設定する値。
    */
    public void setApplicationUse(UDF_bytes v){replaceChild(v, applicationUse); applicationUse = v;}
    /**
	reserved2を設定する。

	@param	v 設定する値。
    */
    public void setReserved2(UDF_bytes v){replaceChild(v, reserved2); reserved2 = v;}

    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long rsize = 0;
	preReadHook(f);
	volDescType = (UDF_uint8)createElement("UDF_uint8", "ecma119", "vol-desc-type");
	rsize += volDescType.readFrom(f);
	standardId = (UDF_bytes)createElement("UDF_bytes", "ecma119", "standard-id");
	standardId.setSize(5);
	rsize += standardId.readFrom(f);
	volDescVersion = (UDF_uint8)createElement("UDF_uint8", "ecma119", "vol-desc-version");
	rsize += volDescVersion.readFrom(f);
	unusedField0 = (UDF_bytes)createElement("UDF_bytes", "ecma119", "unused-field0");
	unusedField0.setSize(1);
	rsize += unusedField0.readFrom(f);
	systemId = (UDF_bytes)createElement("UDF_bytes", "ecma119", "system-id");
	systemId.setSize(32);
	rsize += systemId.readFrom(f);
	volId = (UDF_bytes)createElement("UDF_bytes", "ecma119", "vol-id");
	volId.setSize(32);
	rsize += volId.readFrom(f);
	unusedField1 = (UDF_bytes)createElement("UDF_bytes", "ecma119", "unused-field1");
	unusedField1.setSize(8);
	rsize += unusedField1.readFrom(f);
	volSpaceSize = (UDF_uint32_lebe)createElement("UDF_uint32_lebe", "ecma119", "vol-space-size");
	rsize += volSpaceSize.readFrom(f);
	unusedField2 = (UDF_bytes)createElement("UDF_bytes", "ecma119", "unused-field2");
	unusedField2.setSize(32);
	rsize += unusedField2.readFrom(f);
	volSetSize = (UDF_uint16_lebe)createElement("UDF_uint16_lebe", "ecma119", "vol-set-size");
	rsize += volSetSize.readFrom(f);
	volSeqNumber = (UDF_uint16_lebe)createElement("UDF_uint16_lebe", "ecma119", "vol-seq-number");
	rsize += volSeqNumber.readFrom(f);
	logicalBlockSize = (UDF_uint16_lebe)createElement("UDF_uint16_lebe", "ecma119", "logical-block-size");
	rsize += logicalBlockSize.readFrom(f);
	pathTableSize = (UDF_uint32_lebe)createElement("UDF_uint32_lebe", "ecma119", "path-table-size");
	rsize += pathTableSize.readFrom(f);
	locOfOccurenceOfTypeLPathTable = (UDF_uint32)createElement("UDF_uint32", "ecma119", "loc-of-occurence-of-type-l-path-table");
	rsize += locOfOccurenceOfTypeLPathTable.readFrom(f);
	locOfOptionalOccurenceOfTypeLPathTable = (UDF_uint32)createElement("UDF_uint32", "ecma119", "loc-of-optional-occurence-of-type-l-path-table");
	rsize += locOfOptionalOccurenceOfTypeLPathTable.readFrom(f);
	locOfOccurenceOfTypeMPathTable = (UDF_uint32_be)createElement("UDF_uint32_be", "ecma119", "loc-of-occurence-of-type-m-path-table");
	rsize += locOfOccurenceOfTypeMPathTable.readFrom(f);
	locOfOptionalOccurenceOfTypeMPathTable = (UDF_uint32_be)createElement("UDF_uint32_be", "ecma119", "loc-of-optional-occurence-of-type-m-path-table");
	rsize += locOfOptionalOccurenceOfTypeMPathTable.readFrom(f);
	directoryRecordForRootDirectory = (UDF_ECMA119DirectoryRecord)createElement("UDF_ECMA119DirectoryRecord", "ecma119", "directory-record-for-root-directory");
	rsize += directoryRecordForRootDirectory.readFrom(f);
	volSetId = (UDF_bytes)createElement("UDF_bytes", "ecma119", "vol-set-id");
	volSetId.setSize(128);
	rsize += volSetId.readFrom(f);
	publishedId = (UDF_bytes)createElement("UDF_bytes", "ecma119", "published-id");
	publishedId.setSize(128);
	rsize += publishedId.readFrom(f);
	dataPreparerId = (UDF_bytes)createElement("UDF_bytes", "ecma119", "data-preparer-id");
	dataPreparerId.setSize(128);
	rsize += dataPreparerId.readFrom(f);
	applicationId = (UDF_bytes)createElement("UDF_bytes", "ecma119", "application-id");
	applicationId.setSize(128);
	rsize += applicationId.readFrom(f);
	copyrightFileId = (UDF_bytes)createElement("UDF_bytes", "ecma119", "copyright-file-id");
	copyrightFileId.setSize(37);
	rsize += copyrightFileId.readFrom(f);
	abstractFileId = (UDF_bytes)createElement("UDF_bytes", "ecma119", "abstract-file-id");
	abstractFileId.setSize(37);
	rsize += abstractFileId.readFrom(f);
	bibliographicFileId = (UDF_bytes)createElement("UDF_bytes", "ecma119", "bibliographic-file-id");
	bibliographicFileId.setSize(37);
	rsize += bibliographicFileId.readFrom(f);
	volCreationDateAndTime = (UDF_bytes)createElement("UDF_bytes", "ecma119", "vol-creation-date-and-time");
	volCreationDateAndTime.setSize(17);
	rsize += volCreationDateAndTime.readFrom(f);
	volModificationDateAndTime = (UDF_bytes)createElement("UDF_bytes", "ecma119", "vol-modification-date-and-time");
	volModificationDateAndTime.setSize(17);
	rsize += volModificationDateAndTime.readFrom(f);
	volExpirationDateAndTime = (UDF_bytes)createElement("UDF_bytes", "ecma119", "vol-expiration-date-and-time");
	volExpirationDateAndTime.setSize(17);
	rsize += volExpirationDateAndTime.readFrom(f);
	volEffectiveDateAndTime = (UDF_bytes)createElement("UDF_bytes", "ecma119", "vol-effective-date-and-time");
	volEffectiveDateAndTime.setSize(17);
	rsize += volEffectiveDateAndTime.readFrom(f);
	fileStructureVersion = (UDF_uint8)createElement("UDF_uint8", "ecma119", "file-structure-version");
	rsize += fileStructureVersion.readFrom(f);
	reserved = (UDF_bytes)createElement("UDF_bytes", "ecma119", "reserved");
	reserved.setSize(1);
	rsize += reserved.readFrom(f);
	applicationUse = (UDF_bytes)createElement("UDF_bytes", "ecma119", "application-use");
	applicationUse.setSize(512);
	rsize += applicationUse.readFrom(f);
	reserved2 = (UDF_bytes)createElement("UDF_bytes", "ecma119", "reserved2");
	reserved2.setSize(653);
	rsize += reserved2.readFrom(f);
	apply( );
	postReadHook(f);
	return rsize;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long wsize = 0;
	wsize += volDescType.writeTo(f);
	wsize += standardId.writeTo(f);
	wsize += volDescVersion.writeTo(f);
	wsize += unusedField0.writeTo(f);
	wsize += systemId.writeTo(f);
	wsize += volId.writeTo(f);
	wsize += unusedField1.writeTo(f);
	wsize += volSpaceSize.writeTo(f);
	wsize += unusedField2.writeTo(f);
	wsize += volSetSize.writeTo(f);
	wsize += volSeqNumber.writeTo(f);
	wsize += logicalBlockSize.writeTo(f);
	wsize += pathTableSize.writeTo(f);
	wsize += locOfOccurenceOfTypeLPathTable.writeTo(f);
	wsize += locOfOptionalOccurenceOfTypeLPathTable.writeTo(f);
	wsize += locOfOccurenceOfTypeMPathTable.writeTo(f);
	wsize += locOfOptionalOccurenceOfTypeMPathTable.writeTo(f);
	wsize += directoryRecordForRootDirectory.writeTo(f);
	wsize += volSetId.writeTo(f);
	wsize += publishedId.writeTo(f);
	wsize += dataPreparerId.writeTo(f);
	wsize += applicationId.writeTo(f);
	wsize += copyrightFileId.writeTo(f);
	wsize += abstractFileId.writeTo(f);
	wsize += bibliographicFileId.writeTo(f);
	wsize += volCreationDateAndTime.writeTo(f);
	wsize += volModificationDateAndTime.writeTo(f);
	wsize += volExpirationDateAndTime.writeTo(f);
	wsize += volEffectiveDateAndTime.writeTo(f);
	wsize += fileStructureVersion.writeTo(f);
	wsize += reserved.writeTo(f);
	wsize += applicationUse.writeTo(f);
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
	    else if(name.equals("vol-desc-type")){
		volDescType = (UDF_uint8)createElement("UDF_uint8", "ecma119", "vol-desc-type");
		volDescType.readFromXML(child);
	    }
	    else if(name.equals("standard-id")){
		standardId = (UDF_bytes)createElement("UDF_bytes", "ecma119", "standard-id");
		standardId.setSize(5);
		standardId.readFromXML(child);
	    }
	    else if(name.equals("vol-desc-version")){
		volDescVersion = (UDF_uint8)createElement("UDF_uint8", "ecma119", "vol-desc-version");
		volDescVersion.readFromXML(child);
	    }
	    else if(name.equals("unused-field0")){
		unusedField0 = (UDF_bytes)createElement("UDF_bytes", "ecma119", "unused-field0");
		unusedField0.setSize(1);
		unusedField0.readFromXML(child);
	    }
	    else if(name.equals("system-id")){
		systemId = (UDF_bytes)createElement("UDF_bytes", "ecma119", "system-id");
		systemId.setSize(32);
		systemId.readFromXML(child);
	    }
	    else if(name.equals("vol-id")){
		volId = (UDF_bytes)createElement("UDF_bytes", "ecma119", "vol-id");
		volId.setSize(32);
		volId.readFromXML(child);
	    }
	    else if(name.equals("unused-field1")){
		unusedField1 = (UDF_bytes)createElement("UDF_bytes", "ecma119", "unused-field1");
		unusedField1.setSize(8);
		unusedField1.readFromXML(child);
	    }
	    else if(name.equals("vol-space-size")){
		volSpaceSize = (UDF_uint32_lebe)createElement("UDF_uint32_lebe", "ecma119", "vol-space-size");
		volSpaceSize.readFromXML(child);
	    }
	    else if(name.equals("unused-field2")){
		unusedField2 = (UDF_bytes)createElement("UDF_bytes", "ecma119", "unused-field2");
		unusedField2.setSize(32);
		unusedField2.readFromXML(child);
	    }
	    else if(name.equals("vol-set-size")){
		volSetSize = (UDF_uint16_lebe)createElement("UDF_uint16_lebe", "ecma119", "vol-set-size");
		volSetSize.readFromXML(child);
	    }
	    else if(name.equals("vol-seq-number")){
		volSeqNumber = (UDF_uint16_lebe)createElement("UDF_uint16_lebe", "ecma119", "vol-seq-number");
		volSeqNumber.readFromXML(child);
	    }
	    else if(name.equals("logical-block-size")){
		logicalBlockSize = (UDF_uint16_lebe)createElement("UDF_uint16_lebe", "ecma119", "logical-block-size");
		logicalBlockSize.readFromXML(child);
	    }
	    else if(name.equals("path-table-size")){
		pathTableSize = (UDF_uint32_lebe)createElement("UDF_uint32_lebe", "ecma119", "path-table-size");
		pathTableSize.readFromXML(child);
	    }
	    else if(name.equals("loc-of-occurence-of-type-l-path-table")){
		locOfOccurenceOfTypeLPathTable = (UDF_uint32)createElement("UDF_uint32", "ecma119", "loc-of-occurence-of-type-l-path-table");
		locOfOccurenceOfTypeLPathTable.readFromXML(child);
	    }
	    else if(name.equals("loc-of-optional-occurence-of-type-l-path-table")){
		locOfOptionalOccurenceOfTypeLPathTable = (UDF_uint32)createElement("UDF_uint32", "ecma119", "loc-of-optional-occurence-of-type-l-path-table");
		locOfOptionalOccurenceOfTypeLPathTable.readFromXML(child);
	    }
	    else if(name.equals("loc-of-occurence-of-type-m-path-table")){
		locOfOccurenceOfTypeMPathTable = (UDF_uint32_be)createElement("UDF_uint32_be", "ecma119", "loc-of-occurence-of-type-m-path-table");
		locOfOccurenceOfTypeMPathTable.readFromXML(child);
	    }
	    else if(name.equals("loc-of-optional-occurence-of-type-m-path-table")){
		locOfOptionalOccurenceOfTypeMPathTable = (UDF_uint32_be)createElement("UDF_uint32_be", "ecma119", "loc-of-optional-occurence-of-type-m-path-table");
		locOfOptionalOccurenceOfTypeMPathTable.readFromXML(child);
	    }
	    else if(name.equals("directory-record-for-root-directory")){
		directoryRecordForRootDirectory = (UDF_ECMA119DirectoryRecord)createElement("UDF_ECMA119DirectoryRecord", "ecma119", "directory-record-for-root-directory");
		directoryRecordForRootDirectory.readFromXML(child);
	    }
	    else if(name.equals("vol-set-id")){
		volSetId = (UDF_bytes)createElement("UDF_bytes", "ecma119", "vol-set-id");
		volSetId.setSize(128);
		volSetId.readFromXML(child);
	    }
	    else if(name.equals("published-id")){
		publishedId = (UDF_bytes)createElement("UDF_bytes", "ecma119", "published-id");
		publishedId.setSize(128);
		publishedId.readFromXML(child);
	    }
	    else if(name.equals("data-preparer-id")){
		dataPreparerId = (UDF_bytes)createElement("UDF_bytes", "ecma119", "data-preparer-id");
		dataPreparerId.setSize(128);
		dataPreparerId.readFromXML(child);
	    }
	    else if(name.equals("application-id")){
		applicationId = (UDF_bytes)createElement("UDF_bytes", "ecma119", "application-id");
		applicationId.setSize(128);
		applicationId.readFromXML(child);
	    }
	    else if(name.equals("copyright-file-id")){
		copyrightFileId = (UDF_bytes)createElement("UDF_bytes", "ecma119", "copyright-file-id");
		copyrightFileId.setSize(37);
		copyrightFileId.readFromXML(child);
	    }
	    else if(name.equals("abstract-file-id")){
		abstractFileId = (UDF_bytes)createElement("UDF_bytes", "ecma119", "abstract-file-id");
		abstractFileId.setSize(37);
		abstractFileId.readFromXML(child);
	    }
	    else if(name.equals("bibliographic-file-id")){
		bibliographicFileId = (UDF_bytes)createElement("UDF_bytes", "ecma119", "bibliographic-file-id");
		bibliographicFileId.setSize(37);
		bibliographicFileId.readFromXML(child);
	    }
	    else if(name.equals("vol-creation-date-and-time")){
		volCreationDateAndTime = (UDF_bytes)createElement("UDF_bytes", "ecma119", "vol-creation-date-and-time");
		volCreationDateAndTime.setSize(17);
		volCreationDateAndTime.readFromXML(child);
	    }
	    else if(name.equals("vol-modification-date-and-time")){
		volModificationDateAndTime = (UDF_bytes)createElement("UDF_bytes", "ecma119", "vol-modification-date-and-time");
		volModificationDateAndTime.setSize(17);
		volModificationDateAndTime.readFromXML(child);
	    }
	    else if(name.equals("vol-expiration-date-and-time")){
		volExpirationDateAndTime = (UDF_bytes)createElement("UDF_bytes", "ecma119", "vol-expiration-date-and-time");
		volExpirationDateAndTime.setSize(17);
		volExpirationDateAndTime.readFromXML(child);
	    }
	    else if(name.equals("vol-effective-date-and-time")){
		volEffectiveDateAndTime = (UDF_bytes)createElement("UDF_bytes", "ecma119", "vol-effective-date-and-time");
		volEffectiveDateAndTime.setSize(17);
		volEffectiveDateAndTime.readFromXML(child);
	    }
	    else if(name.equals("file-structure-version")){
		fileStructureVersion = (UDF_uint8)createElement("UDF_uint8", "ecma119", "file-structure-version");
		fileStructureVersion.readFromXML(child);
	    }
	    else if(name.equals("reserved")){
		reserved = (UDF_bytes)createElement("UDF_bytes", "ecma119", "reserved");
		reserved.setSize(1);
		reserved.readFromXML(child);
	    }
	    else if(name.equals("application-use")){
		applicationUse = (UDF_bytes)createElement("UDF_bytes", "ecma119", "application-use");
		applicationUse.setSize(512);
		applicationUse.readFromXML(child);
	    }
	    else if(name.equals("reserved2")){
		reserved2 = (UDF_bytes)createElement("UDF_bytes", "ecma119", "reserved2");
		reserved2.setSize(653);
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
	volDescType = (UDF_uint8)createElement("UDF_uint8", "ecma119", "vol-desc-type");
	volDescType.setDefaultValue();
	standardId = (UDF_bytes)createElement("UDF_bytes", "ecma119", "standard-id");
	standardId.setSize(5);
	standardId.setDefaultValue();
	volDescVersion = (UDF_uint8)createElement("UDF_uint8", "ecma119", "vol-desc-version");
	volDescVersion.setDefaultValue();
	unusedField0 = (UDF_bytes)createElement("UDF_bytes", "ecma119", "unused-field0");
	unusedField0.setSize(1);
	unusedField0.setDefaultValue();
	systemId = (UDF_bytes)createElement("UDF_bytes", "ecma119", "system-id");
	systemId.setSize(32);
	systemId.setDefaultValue();
	volId = (UDF_bytes)createElement("UDF_bytes", "ecma119", "vol-id");
	volId.setSize(32);
	volId.setDefaultValue();
	unusedField1 = (UDF_bytes)createElement("UDF_bytes", "ecma119", "unused-field1");
	unusedField1.setSize(8);
	unusedField1.setDefaultValue();
	volSpaceSize = (UDF_uint32_lebe)createElement("UDF_uint32_lebe", "ecma119", "vol-space-size");
	volSpaceSize.setDefaultValue();
	unusedField2 = (UDF_bytes)createElement("UDF_bytes", "ecma119", "unused-field2");
	unusedField2.setSize(32);
	unusedField2.setDefaultValue();
	volSetSize = (UDF_uint16_lebe)createElement("UDF_uint16_lebe", "ecma119", "vol-set-size");
	volSetSize.setDefaultValue();
	volSeqNumber = (UDF_uint16_lebe)createElement("UDF_uint16_lebe", "ecma119", "vol-seq-number");
	volSeqNumber.setDefaultValue();
	logicalBlockSize = (UDF_uint16_lebe)createElement("UDF_uint16_lebe", "ecma119", "logical-block-size");
	logicalBlockSize.setDefaultValue();
	pathTableSize = (UDF_uint32_lebe)createElement("UDF_uint32_lebe", "ecma119", "path-table-size");
	pathTableSize.setDefaultValue();
	locOfOccurenceOfTypeLPathTable = (UDF_uint32)createElement("UDF_uint32", "ecma119", "loc-of-occurence-of-type-l-path-table");
	locOfOccurenceOfTypeLPathTable.setDefaultValue();
	locOfOptionalOccurenceOfTypeLPathTable = (UDF_uint32)createElement("UDF_uint32", "ecma119", "loc-of-optional-occurence-of-type-l-path-table");
	locOfOptionalOccurenceOfTypeLPathTable.setDefaultValue();
	locOfOccurenceOfTypeMPathTable = (UDF_uint32_be)createElement("UDF_uint32_be", "ecma119", "loc-of-occurence-of-type-m-path-table");
	locOfOccurenceOfTypeMPathTable.setDefaultValue();
	locOfOptionalOccurenceOfTypeMPathTable = (UDF_uint32_be)createElement("UDF_uint32_be", "ecma119", "loc-of-optional-occurence-of-type-m-path-table");
	locOfOptionalOccurenceOfTypeMPathTable.setDefaultValue();
	directoryRecordForRootDirectory = (UDF_ECMA119DirectoryRecord)createElement("UDF_ECMA119DirectoryRecord", "ecma119", "directory-record-for-root-directory");
	directoryRecordForRootDirectory.setDefaultValue();
	volSetId = (UDF_bytes)createElement("UDF_bytes", "ecma119", "vol-set-id");
	volSetId.setSize(128);
	volSetId.setDefaultValue();
	publishedId = (UDF_bytes)createElement("UDF_bytes", "ecma119", "published-id");
	publishedId.setSize(128);
	publishedId.setDefaultValue();
	dataPreparerId = (UDF_bytes)createElement("UDF_bytes", "ecma119", "data-preparer-id");
	dataPreparerId.setSize(128);
	dataPreparerId.setDefaultValue();
	applicationId = (UDF_bytes)createElement("UDF_bytes", "ecma119", "application-id");
	applicationId.setSize(128);
	applicationId.setDefaultValue();
	copyrightFileId = (UDF_bytes)createElement("UDF_bytes", "ecma119", "copyright-file-id");
	copyrightFileId.setSize(37);
	copyrightFileId.setDefaultValue();
	abstractFileId = (UDF_bytes)createElement("UDF_bytes", "ecma119", "abstract-file-id");
	abstractFileId.setSize(37);
	abstractFileId.setDefaultValue();
	bibliographicFileId = (UDF_bytes)createElement("UDF_bytes", "ecma119", "bibliographic-file-id");
	bibliographicFileId.setSize(37);
	bibliographicFileId.setDefaultValue();
	volCreationDateAndTime = (UDF_bytes)createElement("UDF_bytes", "ecma119", "vol-creation-date-and-time");
	volCreationDateAndTime.setSize(17);
	volCreationDateAndTime.setDefaultValue();
	volModificationDateAndTime = (UDF_bytes)createElement("UDF_bytes", "ecma119", "vol-modification-date-and-time");
	volModificationDateAndTime.setSize(17);
	volModificationDateAndTime.setDefaultValue();
	volExpirationDateAndTime = (UDF_bytes)createElement("UDF_bytes", "ecma119", "vol-expiration-date-and-time");
	volExpirationDateAndTime.setSize(17);
	volExpirationDateAndTime.setDefaultValue();
	volEffectiveDateAndTime = (UDF_bytes)createElement("UDF_bytes", "ecma119", "vol-effective-date-and-time");
	volEffectiveDateAndTime.setSize(17);
	volEffectiveDateAndTime.setDefaultValue();
	fileStructureVersion = (UDF_uint8)createElement("UDF_uint8", "ecma119", "file-structure-version");
	fileStructureVersion.setDefaultValue();
	reserved = (UDF_bytes)createElement("UDF_bytes", "ecma119", "reserved");
	reserved.setSize(1);
	reserved.setDefaultValue();
	applicationUse = (UDF_bytes)createElement("UDF_bytes", "ecma119", "application-use");
	applicationUse.setSize(512);
	applicationUse.setDefaultValue();
	reserved2 = (UDF_bytes)createElement("UDF_bytes", "ecma119", "reserved2");
	reserved2.setSize(653);
	reserved2.setDefaultValue();

	apply( );
	postSetDefaultValueHook();
    }


    /**
	複製する
    */
    public UDF_Element duplicateElement(){
	UDF_ECMA119_CD001_1 dup = new UDF_ECMA119_CD001_1(this, null, getName());
	dup.setDefaultValue();
	dup.duplicateHook(this);
	dup.setVolDescType((UDF_uint8)volDescType.duplicateElement());
	dup.setStandardId((UDF_bytes)standardId.duplicateElement());
	dup.setVolDescVersion((UDF_uint8)volDescVersion.duplicateElement());
	dup.setUnusedField0((UDF_bytes)unusedField0.duplicateElement());
	dup.setSystemId((UDF_bytes)systemId.duplicateElement());
	dup.setVolId((UDF_bytes)volId.duplicateElement());
	dup.setUnusedField1((UDF_bytes)unusedField1.duplicateElement());
	dup.setVolSpaceSize((UDF_uint32_lebe)volSpaceSize.duplicateElement());
	dup.setUnusedField2((UDF_bytes)unusedField2.duplicateElement());
	dup.setVolSetSize((UDF_uint16_lebe)volSetSize.duplicateElement());
	dup.setVolSeqNumber((UDF_uint16_lebe)volSeqNumber.duplicateElement());
	dup.setLogicalBlockSize((UDF_uint16_lebe)logicalBlockSize.duplicateElement());
	dup.setPathTableSize((UDF_uint32_lebe)pathTableSize.duplicateElement());
	dup.setLocOfOccurenceOfTypeLPathTable((UDF_uint32)locOfOccurenceOfTypeLPathTable.duplicateElement());
	dup.setLocOfOptionalOccurenceOfTypeLPathTable((UDF_uint32)locOfOptionalOccurenceOfTypeLPathTable.duplicateElement());
	dup.setLocOfOccurenceOfTypeMPathTable((UDF_uint32_be)locOfOccurenceOfTypeMPathTable.duplicateElement());
	dup.setLocOfOptionalOccurenceOfTypeMPathTable((UDF_uint32_be)locOfOptionalOccurenceOfTypeMPathTable.duplicateElement());
	dup.setDirectoryRecordForRootDirectory((UDF_ECMA119DirectoryRecord)directoryRecordForRootDirectory.duplicateElement());
	dup.setVolSetId((UDF_bytes)volSetId.duplicateElement());
	dup.setPublishedId((UDF_bytes)publishedId.duplicateElement());
	dup.setDataPreparerId((UDF_bytes)dataPreparerId.duplicateElement());
	dup.setApplicationId((UDF_bytes)applicationId.duplicateElement());
	dup.setCopyrightFileId((UDF_bytes)copyrightFileId.duplicateElement());
	dup.setAbstractFileId((UDF_bytes)abstractFileId.duplicateElement());
	dup.setBibliographicFileId((UDF_bytes)bibliographicFileId.duplicateElement());
	dup.setVolCreationDateAndTime((UDF_bytes)volCreationDateAndTime.duplicateElement());
	dup.setVolModificationDateAndTime((UDF_bytes)volModificationDateAndTime.duplicateElement());
	dup.setVolExpirationDateAndTime((UDF_bytes)volExpirationDateAndTime.duplicateElement());
	dup.setVolEffectiveDateAndTime((UDF_bytes)volEffectiveDateAndTime.duplicateElement());
	dup.setFileStructureVersion((UDF_uint8)fileStructureVersion.duplicateElement());
	dup.setReserved((UDF_bytes)reserved.duplicateElement());
	dup.setApplicationUse((UDF_bytes)applicationUse.duplicateElement());
	dup.setReserved2((UDF_bytes)reserved2.duplicateElement());

	apply();

	return dup;
    }

    /**
        子の UDF Element の Node を append しなおす。

    */
    private void apply( ) {

	removeAllChildren();
	appendChild(volDescType);
	appendChild(standardId);
	appendChild(volDescVersion);
	appendChild(unusedField0);
	appendChild(systemId);
	appendChild(volId);
	appendChild(unusedField1);
	appendChild(volSpaceSize);
	appendChild(unusedField2);
	appendChild(volSetSize);
	appendChild(volSeqNumber);
	appendChild(logicalBlockSize);
	appendChild(pathTableSize);
	appendChild(locOfOccurenceOfTypeLPathTable);
	appendChild(locOfOptionalOccurenceOfTypeLPathTable);
	appendChild(locOfOccurenceOfTypeMPathTable);
	appendChild(locOfOptionalOccurenceOfTypeMPathTable);
	appendChild(directoryRecordForRootDirectory);
	appendChild(volSetId);
	appendChild(publishedId);
	appendChild(dataPreparerId);
	appendChild(applicationId);
	appendChild(copyrightFileId);
	appendChild(abstractFileId);
	appendChild(bibliographicFileId);
	appendChild(volCreationDateAndTime);
	appendChild(volModificationDateAndTime);
	appendChild(volExpirationDateAndTime);
	appendChild(volEffectiveDateAndTime);
	appendChild(fileStructureVersion);
	appendChild(reserved);
	appendChild(applicationUse);
	appendChild(reserved2);
    }

    public String getInfo(int indent){
	String a = "";
	a += UDF_Util.repeat(' ', indent);
	a += volDescType.getInfo(indent + 1);
	a += standardId.getInfo(indent + 1);
	a += volDescVersion.getInfo(indent + 1);
	a += unusedField0.getInfo(indent + 1);
	a += systemId.getInfo(indent + 1);
	a += volId.getInfo(indent + 1);
	a += unusedField1.getInfo(indent + 1);
	a += volSpaceSize.getInfo(indent + 1);
	a += unusedField2.getInfo(indent + 1);
	a += volSetSize.getInfo(indent + 1);
	a += volSeqNumber.getInfo(indent + 1);
	a += logicalBlockSize.getInfo(indent + 1);
	a += pathTableSize.getInfo(indent + 1);
	a += locOfOccurenceOfTypeLPathTable.getInfo(indent + 1);
	a += locOfOptionalOccurenceOfTypeLPathTable.getInfo(indent + 1);
	a += locOfOccurenceOfTypeMPathTable.getInfo(indent + 1);
	a += locOfOptionalOccurenceOfTypeMPathTable.getInfo(indent + 1);
	a += directoryRecordForRootDirectory.getInfo(indent + 1);
	a += volSetId.getInfo(indent + 1);
	a += publishedId.getInfo(indent + 1);
	a += dataPreparerId.getInfo(indent + 1);
	a += applicationId.getInfo(indent + 1);
	a += copyrightFileId.getInfo(indent + 1);
	a += abstractFileId.getInfo(indent + 1);
	a += bibliographicFileId.getInfo(indent + 1);
	a += volCreationDateAndTime.getInfo(indent + 1);
	a += volModificationDateAndTime.getInfo(indent + 1);
	a += volExpirationDateAndTime.getInfo(indent + 1);
	a += volEffectiveDateAndTime.getInfo(indent + 1);
	a += fileStructureVersion.getInfo(indent + 1);
	a += reserved.getInfo(indent + 1);
	a += applicationUse.getInfo(indent + 1);
	a += reserved2.getInfo(indent + 1);
      return a;
    }
    public void debug(int indent){
	System.err.print(UDF_Util.repeat(' ', indent));

	String info = " gp=" + getGlobalPoint();
	info += " pn=" + getElemPartRefNo();
	info += " sn=" + getPartSubno();
	System.err.println(getName() + ": (" + hashCode() + ")" + info);	volDescType.debug(indent + 1);
	standardId.debug(indent + 1);
	volDescVersion.debug(indent + 1);
	unusedField0.debug(indent + 1);
	systemId.debug(indent + 1);
	volId.debug(indent + 1);
	unusedField1.debug(indent + 1);
	volSpaceSize.debug(indent + 1);
	unusedField2.debug(indent + 1);
	volSetSize.debug(indent + 1);
	volSeqNumber.debug(indent + 1);
	logicalBlockSize.debug(indent + 1);
	pathTableSize.debug(indent + 1);
	locOfOccurenceOfTypeLPathTable.debug(indent + 1);
	locOfOptionalOccurenceOfTypeLPathTable.debug(indent + 1);
	locOfOccurenceOfTypeMPathTable.debug(indent + 1);
	locOfOptionalOccurenceOfTypeMPathTable.debug(indent + 1);
	directoryRecordForRootDirectory.debug(indent + 1);
	volSetId.debug(indent + 1);
	publishedId.debug(indent + 1);
	dataPreparerId.debug(indent + 1);
	applicationId.debug(indent + 1);
	copyrightFileId.debug(indent + 1);
	abstractFileId.debug(indent + 1);
	bibliographicFileId.debug(indent + 1);
	volCreationDateAndTime.debug(indent + 1);
	volModificationDateAndTime.debug(indent + 1);
	volExpirationDateAndTime.debug(indent + 1);
	volEffectiveDateAndTime.debug(indent + 1);
	fileStructureVersion.debug(indent + 1);
	reserved.debug(indent + 1);
	applicationUse.debug(indent + 1);
	reserved2.debug(indent + 1);
    }
//begin:add your code here
//end:
};
