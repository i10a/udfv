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
Primary&nbsp;Volume&nbsp;Descriptor&nbsp;を表現するクラス。<br/>


<dl>

<dt>フィールド一覧</dt>
<dd>
<table border="1">
<tr><td>メンバ</td><td>型</td><td>サイズ</td></tr>
<tr><td><b>DescTag</b></td><td><b>UDF_tag</b></td><td><i>descTag.getSize()</i></td></tr>
<tr><td><b>VolDescSeqNumber</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>PrimaryVolDescNumber</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>VolId</b></td><td><b>UDF_dstring</b></td><td><i>32</i></td></tr>
<tr><td><b>VolSeqNumber</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td></tr>
<tr><td><b>MaxVolSeqNumber</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td></tr>
<tr><td><b>InterchangeLevel</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td></tr>
<tr><td><b>MaxInterchangeLevel</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td></tr>
<tr><td><b>CharSetList</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>MaxCharSetList</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>VolSetId</b></td><td><b>UDF_dstring</b></td><td><i>128</i></td></tr>
<tr><td><b>DescCharSet</b></td><td><b>UDF_charspec</b></td><td><i>descCharSet.getSize()</i></td></tr>
<tr><td><b>ExplanatoryCharSet</b></td><td><b>UDF_charspec</b></td><td><i>explanatoryCharSet.getSize()</i></td></tr>
<tr><td><b>VolAbstract</b></td><td><b>UDF_extent_ad</b></td><td><i>volAbstract.getSize()</i></td></tr>
<tr><td><b>VolCopyrightNotice</b></td><td><b>UDF_extent_ad</b></td><td><i>volCopyrightNotice.getSize()</i></td></tr>
<tr><td><b>ApplicationId</b></td><td><b>UDF_regid</b></td><td><i>applicationId.getSize()</i></td></tr>
<tr><td><b>RecordingDateAndTime</b></td><td><b>UDF_timestamp</b></td><td><i>recordingDateAndTime.getSize()</i></td></tr>
<tr><td><b>ImplId</b></td><td><b>UDF_regid</b></td><td><i>implId.getSize()</i></td></tr>
<tr><td><b>ImplUse</b></td><td><b>UDF_bytes</b></td><td><i>64</i></td></tr>
<tr><td><b>PredecessorVolDescSeqLoc</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>Flags</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td></tr>
<tr><td><b>Reserved</b></td><td><b>UDF_bytes</b></td><td><i>22</i></td></tr>
</table>
</dd>
</dl>
*/
public class UDF_desc1 extends UDF_CrcDesc implements UDF_VolDesc, UDF_VolDescSeqNum
{

    /**
	このオブジェクトのパッケージ名を除いたクラス名を取得する。

	@return クラス名
    */
    public static String getUDFClassName( ) {
        return "UDF_desc1";
    }

    /**
	コンストラクタ。

	@param elem   親エレメント。
	@param prefix ネームスペース。
	@param name   エレメント名。
    */
    public UDF_desc1(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }
    /**
	このUDFエレメントのサイズを返す。

	@return UDFエレメントのサイズ。
    */
    public int getSize(){
	return 0+descTag.getSize()+4+4+volId.getSize()+2+2+2+2+4+4+volSetId.getSize()+descCharSet.getSize()+explanatoryCharSet.getSize()+volAbstract.getSize()+volCopyrightNotice.getSize()+applicationId.getSize()+recordingDateAndTime.getSize()+implId.getSize()+implUse.getSize()+4+2+reserved.getSize();
    }

    /**
	このUDFエレメントのサイズをlong 型で返す。

	@return UDFエレメントのサイズ。
    */
    public long getLongSize(){
	return (long)0+descTag.getSize()+4+4+volId.getSize()+2+2+2+2+4+4+volSetId.getSize()+descCharSet.getSize()+explanatoryCharSet.getSize()+volAbstract.getSize()+volCopyrightNotice.getSize()+applicationId.getSize()+recordingDateAndTime.getSize()+implId.getSize()+implUse.getSize()+4+2+reserved.getSize();
    }
    private UDF_tag descTag;
    private UDF_uint32 volDescSeqNumber;
    private UDF_uint32 primaryVolDescNumber;
    private UDF_dstring volId;
    private UDF_uint16 volSeqNumber;
    private UDF_uint16 maxVolSeqNumber;
    private UDF_uint16 interchangeLevel;
    private UDF_uint16 maxInterchangeLevel;
    private UDF_uint32 charSetList;
    private UDF_uint32 maxCharSetList;
    private UDF_dstring volSetId;
    private UDF_charspec descCharSet;
    private UDF_charspec explanatoryCharSet;
    private UDF_extent_ad volAbstract;
    private UDF_extent_ad volCopyrightNotice;
    private UDF_regid applicationId;
    private UDF_timestamp recordingDateAndTime;
    private UDF_regid implId;
    private UDF_bytes implUse;
    private UDF_uint32 predecessorVolDescSeqLoc;
    private UDF_uint16 flags;
    private UDF_bytes reserved;

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
	primaryVolDescNumberを取得する。

	@return 取得したprimaryVolDescNumber を返す。
    */
    public UDF_uint32 getPrimaryVolDescNumber(){return primaryVolDescNumber;}
    /**
	volIdを取得する。

	@return 取得したvolId を返す。
    */
    public UDF_dstring getVolId(){return volId;}
    /**
	volSeqNumberを取得する。

	@return 取得したvolSeqNumber を返す。
    */
    public UDF_uint16 getVolSeqNumber(){return volSeqNumber;}
    /**
	maxVolSeqNumberを取得する。

	@return 取得したmaxVolSeqNumber を返す。
    */
    public UDF_uint16 getMaxVolSeqNumber(){return maxVolSeqNumber;}
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
	volSetIdを取得する。

	@return 取得したvolSetId を返す。
    */
    public UDF_dstring getVolSetId(){return volSetId;}
    /**
	descCharSetを取得する。

	@return 取得したdescCharSet を返す。
    */
    public UDF_charspec getDescCharSet(){return descCharSet;}
    /**
	explanatoryCharSetを取得する。

	@return 取得したexplanatoryCharSet を返す。
    */
    public UDF_charspec getExplanatoryCharSet(){return explanatoryCharSet;}
    /**
	volAbstractを取得する。

	@return 取得したvolAbstract を返す。
    */
    public UDF_extent_ad getVolAbstract(){return volAbstract;}
    /**
	volCopyrightNoticeを取得する。

	@return 取得したvolCopyrightNotice を返す。
    */
    public UDF_extent_ad getVolCopyrightNotice(){return volCopyrightNotice;}
    /**
	applicationIdを取得する。

	@return 取得したapplicationId を返す。
    */
    public UDF_regid getApplicationId(){return applicationId;}
    /**
	recordingDateAndTimeを取得する。

	@return 取得したrecordingDateAndTime を返す。
    */
    public UDF_timestamp getRecordingDateAndTime(){return recordingDateAndTime;}
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
	predecessorVolDescSeqLocを取得する。

	@return 取得したpredecessorVolDescSeqLoc を返す。
    */
    public UDF_uint32 getPredecessorVolDescSeqLoc(){return predecessorVolDescSeqLoc;}
    /**
	flagsを取得する。

	@return 取得したflags を返す。
    */
    public UDF_uint16 getFlags(){return flags;}
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
	volDescSeqNumberを設定する。

	@param	v 設定する値。
    */
    public void setVolDescSeqNumber(UDF_uint32 v){replaceChild(v, volDescSeqNumber); volDescSeqNumber = v;}
    /**
	primaryVolDescNumberを設定する。

	@param	v 設定する値。
    */
    public void setPrimaryVolDescNumber(UDF_uint32 v){replaceChild(v, primaryVolDescNumber); primaryVolDescNumber = v;}
    /**
	volIdを設定する。

	@param	v 設定する値。
    */
    public void setVolId(UDF_dstring v){replaceChild(v, volId); volId = v;}
    /**
	volSeqNumberを設定する。

	@param	v 設定する値。
    */
    public void setVolSeqNumber(UDF_uint16 v){replaceChild(v, volSeqNumber); volSeqNumber = v;}
    /**
	maxVolSeqNumberを設定する。

	@param	v 設定する値。
    */
    public void setMaxVolSeqNumber(UDF_uint16 v){replaceChild(v, maxVolSeqNumber); maxVolSeqNumber = v;}
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
	volSetIdを設定する。

	@param	v 設定する値。
    */
    public void setVolSetId(UDF_dstring v){replaceChild(v, volSetId); volSetId = v;}
    /**
	descCharSetを設定する。

	@param	v 設定する値。
    */
    public void setDescCharSet(UDF_charspec v){replaceChild(v, descCharSet); descCharSet = v;}
    /**
	explanatoryCharSetを設定する。

	@param	v 設定する値。
    */
    public void setExplanatoryCharSet(UDF_charspec v){replaceChild(v, explanatoryCharSet); explanatoryCharSet = v;}
    /**
	volAbstractを設定する。

	@param	v 設定する値。
    */
    public void setVolAbstract(UDF_extent_ad v){replaceChild(v, volAbstract); volAbstract = v;}
    /**
	volCopyrightNoticeを設定する。

	@param	v 設定する値。
    */
    public void setVolCopyrightNotice(UDF_extent_ad v){replaceChild(v, volCopyrightNotice); volCopyrightNotice = v;}
    /**
	applicationIdを設定する。

	@param	v 設定する値。
    */
    public void setApplicationId(UDF_regid v){replaceChild(v, applicationId); applicationId = v;}
    /**
	recordingDateAndTimeを設定する。

	@param	v 設定する値。
    */
    public void setRecordingDateAndTime(UDF_timestamp v){replaceChild(v, recordingDateAndTime); recordingDateAndTime = v;}
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
	predecessorVolDescSeqLocを設定する。

	@param	v 設定する値。
    */
    public void setPredecessorVolDescSeqLoc(UDF_uint32 v){replaceChild(v, predecessorVolDescSeqLoc); predecessorVolDescSeqLoc = v;}
    /**
	flagsを設定する。

	@param	v 設定する値。
    */
    public void setFlags(UDF_uint16 v){replaceChild(v, flags); flags = v;}
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
	volDescSeqNumber = (UDF_uint32)createElement("UDF_uint32", "", "vol-desc-seq-number");
	rsize += volDescSeqNumber.readFrom(f);
	primaryVolDescNumber = (UDF_uint32)createElement("UDF_uint32", "", "primary-vol-desc-number");
	rsize += primaryVolDescNumber.readFrom(f);
	volId = (UDF_dstring)createElement("UDF_dstring", "", "vol-id");
	volId.setSize(32);
	rsize += volId.readFrom(f);
	volSeqNumber = (UDF_uint16)createElement("UDF_uint16", "", "vol-seq-number");
	rsize += volSeqNumber.readFrom(f);
	maxVolSeqNumber = (UDF_uint16)createElement("UDF_uint16", "", "max-vol-seq-number");
	rsize += maxVolSeqNumber.readFrom(f);
	interchangeLevel = (UDF_uint16)createElement("UDF_uint16", "", "interchange-level");
	rsize += interchangeLevel.readFrom(f);
	maxInterchangeLevel = (UDF_uint16)createElement("UDF_uint16", "", "max-interchange-level");
	rsize += maxInterchangeLevel.readFrom(f);
	charSetList = (UDF_uint32)createElement("UDF_uint32", "", "char-set-list");
	rsize += charSetList.readFrom(f);
	maxCharSetList = (UDF_uint32)createElement("UDF_uint32", "", "max-char-set-list");
	rsize += maxCharSetList.readFrom(f);
	volSetId = (UDF_dstring)createElement("UDF_dstring", "", "vol-set-id");
	volSetId.setSize(128);
	rsize += volSetId.readFrom(f);
	descCharSet = (UDF_charspec)createElement("UDF_charspec", "", "desc-char-set");
	rsize += descCharSet.readFrom(f);
	explanatoryCharSet = (UDF_charspec)createElement("UDF_charspec", "", "explanatory-char-set");
	rsize += explanatoryCharSet.readFrom(f);
	volAbstract = (UDF_extent_ad)createElement("UDF_extent_ad", "", "vol-abstract");
	rsize += volAbstract.readFrom(f);
	volCopyrightNotice = (UDF_extent_ad)createElement("UDF_extent_ad", "", "vol-copyright-notice");
	rsize += volCopyrightNotice.readFrom(f);
	applicationId = (UDF_regid)createElement("UDF_regid", "", "application-id");
	rsize += applicationId.readFrom(f);
	recordingDateAndTime = (UDF_timestamp)createElement("UDF_timestamp", "", "recording-date-and-time");
	rsize += recordingDateAndTime.readFrom(f);
	implId = (UDF_regid)createElement("UDF_regid", "", "impl-id");
	rsize += implId.readFrom(f);
	implUse = (UDF_bytes)createElement("UDF_bytes", "", "impl-use");
	implUse.setSize(64);
	rsize += implUse.readFrom(f);
	predecessorVolDescSeqLoc = (UDF_uint32)createElement("UDF_uint32", "", "predecessor-vol-desc-seq-loc");
	rsize += predecessorVolDescSeqLoc.readFrom(f);
	flags = (UDF_uint16)createElement("UDF_uint16", "", "flags");
	rsize += flags.readFrom(f);
	reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
	reserved.setSize(22);
	rsize += reserved.readFrom(f);
	apply( );
	postReadHook(f);
	return rsize;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long wsize = 0;
	wsize += descTag.writeTo(f);
	wsize += volDescSeqNumber.writeTo(f);
	wsize += primaryVolDescNumber.writeTo(f);
	wsize += volId.writeTo(f);
	wsize += volSeqNumber.writeTo(f);
	wsize += maxVolSeqNumber.writeTo(f);
	wsize += interchangeLevel.writeTo(f);
	wsize += maxInterchangeLevel.writeTo(f);
	wsize += charSetList.writeTo(f);
	wsize += maxCharSetList.writeTo(f);
	wsize += volSetId.writeTo(f);
	wsize += descCharSet.writeTo(f);
	wsize += explanatoryCharSet.writeTo(f);
	wsize += volAbstract.writeTo(f);
	wsize += volCopyrightNotice.writeTo(f);
	wsize += applicationId.writeTo(f);
	wsize += recordingDateAndTime.writeTo(f);
	wsize += implId.writeTo(f);
	wsize += implUse.writeTo(f);
	wsize += predecessorVolDescSeqLoc.writeTo(f);
	wsize += flags.writeTo(f);
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
	    else if(name.equals("vol-desc-seq-number")){
		volDescSeqNumber = (UDF_uint32)createElement("UDF_uint32", "", "vol-desc-seq-number");
		volDescSeqNumber.readFromXML(child);
	    }
	    else if(name.equals("primary-vol-desc-number")){
		primaryVolDescNumber = (UDF_uint32)createElement("UDF_uint32", "", "primary-vol-desc-number");
		primaryVolDescNumber.readFromXML(child);
	    }
	    else if(name.equals("vol-id")){
		volId = (UDF_dstring)createElement("UDF_dstring", "", "vol-id");
		volId.setSize(32);
		volId.readFromXML(child);
	    }
	    else if(name.equals("vol-seq-number")){
		volSeqNumber = (UDF_uint16)createElement("UDF_uint16", "", "vol-seq-number");
		volSeqNumber.readFromXML(child);
	    }
	    else if(name.equals("max-vol-seq-number")){
		maxVolSeqNumber = (UDF_uint16)createElement("UDF_uint16", "", "max-vol-seq-number");
		maxVolSeqNumber.readFromXML(child);
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
	    else if(name.equals("vol-set-id")){
		volSetId = (UDF_dstring)createElement("UDF_dstring", "", "vol-set-id");
		volSetId.setSize(128);
		volSetId.readFromXML(child);
	    }
	    else if(name.equals("desc-char-set")){
		descCharSet = (UDF_charspec)createElement("UDF_charspec", "", "desc-char-set");
		descCharSet.readFromXML(child);
	    }
	    else if(name.equals("explanatory-char-set")){
		explanatoryCharSet = (UDF_charspec)createElement("UDF_charspec", "", "explanatory-char-set");
		explanatoryCharSet.readFromXML(child);
	    }
	    else if(name.equals("vol-abstract")){
		volAbstract = (UDF_extent_ad)createElement("UDF_extent_ad", "", "vol-abstract");
		volAbstract.readFromXML(child);
	    }
	    else if(name.equals("vol-copyright-notice")){
		volCopyrightNotice = (UDF_extent_ad)createElement("UDF_extent_ad", "", "vol-copyright-notice");
		volCopyrightNotice.readFromXML(child);
	    }
	    else if(name.equals("application-id")){
		applicationId = (UDF_regid)createElement("UDF_regid", "", "application-id");
		applicationId.readFromXML(child);
	    }
	    else if(name.equals("recording-date-and-time")){
		recordingDateAndTime = (UDF_timestamp)createElement("UDF_timestamp", "", "recording-date-and-time");
		recordingDateAndTime.readFromXML(child);
	    }
	    else if(name.equals("impl-id")){
		implId = (UDF_regid)createElement("UDF_regid", "", "impl-id");
		implId.readFromXML(child);
	    }
	    else if(name.equals("impl-use")){
		implUse = (UDF_bytes)createElement("UDF_bytes", "", "impl-use");
		implUse.setSize(64);
		implUse.readFromXML(child);
	    }
	    else if(name.equals("predecessor-vol-desc-seq-loc")){
		predecessorVolDescSeqLoc = (UDF_uint32)createElement("UDF_uint32", "", "predecessor-vol-desc-seq-loc");
		predecessorVolDescSeqLoc.readFromXML(child);
	    }
	    else if(name.equals("flags")){
		flags = (UDF_uint16)createElement("UDF_uint16", "", "flags");
		flags.readFromXML(child);
	    }
	    else if(name.equals("reserved")){
		reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
		reserved.setSize(22);
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
	volDescSeqNumber = (UDF_uint32)createElement("UDF_uint32", "", "vol-desc-seq-number");
	volDescSeqNumber.setDefaultValue();
	primaryVolDescNumber = (UDF_uint32)createElement("UDF_uint32", "", "primary-vol-desc-number");
	primaryVolDescNumber.setDefaultValue();
	volId = (UDF_dstring)createElement("UDF_dstring", "", "vol-id");
	volId.setSize(32);
	volId.setDefaultValue();
	volSeqNumber = (UDF_uint16)createElement("UDF_uint16", "", "vol-seq-number");
	volSeqNumber.setDefaultValue();
	volSeqNumber.setValue(1);
	maxVolSeqNumber = (UDF_uint16)createElement("UDF_uint16", "", "max-vol-seq-number");
	maxVolSeqNumber.setDefaultValue();
	maxVolSeqNumber.setValue(1);
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
	volSetId = (UDF_dstring)createElement("UDF_dstring", "", "vol-set-id");
	volSetId.setSize(128);
	volSetId.setDefaultValue();
	descCharSet = (UDF_charspec)createElement("UDF_charspec", "", "desc-char-set");
	descCharSet.setDefaultValue();
	explanatoryCharSet = (UDF_charspec)createElement("UDF_charspec", "", "explanatory-char-set");
	explanatoryCharSet.setDefaultValue();
	volAbstract = (UDF_extent_ad)createElement("UDF_extent_ad", "", "vol-abstract");
	volAbstract.setDefaultValue();
	volCopyrightNotice = (UDF_extent_ad)createElement("UDF_extent_ad", "", "vol-copyright-notice");
	volCopyrightNotice.setDefaultValue();
	applicationId = (UDF_regid)createElement("UDF_regid", "", "application-id");
	applicationId.setDefaultValue();
	recordingDateAndTime = (UDF_timestamp)createElement("UDF_timestamp", "", "recording-date-and-time");
	recordingDateAndTime.setDefaultValue();
	implId = (UDF_regid)createElement("UDF_regid", "", "impl-id");
	implId.setDefaultValue();
	implUse = (UDF_bytes)createElement("UDF_bytes", "", "impl-use");
	implUse.setSize(64);
	implUse.setDefaultValue();
	predecessorVolDescSeqLoc = (UDF_uint32)createElement("UDF_uint32", "", "predecessor-vol-desc-seq-loc");
	predecessorVolDescSeqLoc.setDefaultValue();
	flags = (UDF_uint16)createElement("UDF_uint16", "", "flags");
	flags.setDefaultValue();
	reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
	reserved.setSize(22);
	reserved.setDefaultValue();

	apply( );
	postSetDefaultValueHook();
    }


    /**
	複製する
    */
    public UDF_Element duplicateElement(){
	UDF_desc1 dup = new UDF_desc1(this, null, getName());
	dup.setDefaultValue();
	dup.duplicateHook(this);
	dup.setDescTag((UDF_tag)descTag.duplicateElement());
	dup.setVolDescSeqNumber((UDF_uint32)volDescSeqNumber.duplicateElement());
	dup.setPrimaryVolDescNumber((UDF_uint32)primaryVolDescNumber.duplicateElement());
	dup.setVolId((UDF_dstring)volId.duplicateElement());
	dup.setVolSeqNumber((UDF_uint16)volSeqNumber.duplicateElement());
	dup.setMaxVolSeqNumber((UDF_uint16)maxVolSeqNumber.duplicateElement());
	dup.setInterchangeLevel((UDF_uint16)interchangeLevel.duplicateElement());
	dup.setMaxInterchangeLevel((UDF_uint16)maxInterchangeLevel.duplicateElement());
	dup.setCharSetList((UDF_uint32)charSetList.duplicateElement());
	dup.setMaxCharSetList((UDF_uint32)maxCharSetList.duplicateElement());
	dup.setVolSetId((UDF_dstring)volSetId.duplicateElement());
	dup.setDescCharSet((UDF_charspec)descCharSet.duplicateElement());
	dup.setExplanatoryCharSet((UDF_charspec)explanatoryCharSet.duplicateElement());
	dup.setVolAbstract((UDF_extent_ad)volAbstract.duplicateElement());
	dup.setVolCopyrightNotice((UDF_extent_ad)volCopyrightNotice.duplicateElement());
	dup.setApplicationId((UDF_regid)applicationId.duplicateElement());
	dup.setRecordingDateAndTime((UDF_timestamp)recordingDateAndTime.duplicateElement());
	dup.setImplId((UDF_regid)implId.duplicateElement());
	dup.setImplUse((UDF_bytes)implUse.duplicateElement());
	dup.setPredecessorVolDescSeqLoc((UDF_uint32)predecessorVolDescSeqLoc.duplicateElement());
	dup.setFlags((UDF_uint16)flags.duplicateElement());
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
	appendChild(volDescSeqNumber);
	appendChild(primaryVolDescNumber);
	appendChild(volId);
	appendChild(volSeqNumber);
	appendChild(maxVolSeqNumber);
	appendChild(interchangeLevel);
	appendChild(maxInterchangeLevel);
	appendChild(charSetList);
	appendChild(maxCharSetList);
	appendChild(volSetId);
	appendChild(descCharSet);
	appendChild(explanatoryCharSet);
	appendChild(volAbstract);
	appendChild(volCopyrightNotice);
	appendChild(applicationId);
	appendChild(recordingDateAndTime);
	appendChild(implId);
	appendChild(implUse);
	appendChild(predecessorVolDescSeqLoc);
	appendChild(flags);
	appendChild(reserved);
    }

    public String getInfo(int indent){
	String a = "";
	a += UDF_Util.repeat(' ', indent);
	a += descTag.getInfo(indent + 1);
	a += volDescSeqNumber.getInfo(indent + 1);
	a += primaryVolDescNumber.getInfo(indent + 1);
	a += volId.getInfo(indent + 1);
	a += volSeqNumber.getInfo(indent + 1);
	a += maxVolSeqNumber.getInfo(indent + 1);
	a += interchangeLevel.getInfo(indent + 1);
	a += maxInterchangeLevel.getInfo(indent + 1);
	a += charSetList.getInfo(indent + 1);
	a += maxCharSetList.getInfo(indent + 1);
	a += volSetId.getInfo(indent + 1);
	a += descCharSet.getInfo(indent + 1);
	a += explanatoryCharSet.getInfo(indent + 1);
	a += volAbstract.getInfo(indent + 1);
	a += volCopyrightNotice.getInfo(indent + 1);
	a += applicationId.getInfo(indent + 1);
	a += recordingDateAndTime.getInfo(indent + 1);
	a += implId.getInfo(indent + 1);
	a += implUse.getInfo(indent + 1);
	a += predecessorVolDescSeqLoc.getInfo(indent + 1);
	a += flags.getInfo(indent + 1);
	a += reserved.getInfo(indent + 1);
      return a;
    }
    public void debug(int indent){
	System.err.print(UDF_Util.repeat(' ', indent));

	String info = " gp=" + getGlobalPoint();
	info += " pn=" + getElemPartRefNo();
	info += " sn=" + getPartSubno();
	System.err.println(getName() + ": (" + hashCode() + ")" + info);	descTag.debug(indent + 1);
	volDescSeqNumber.debug(indent + 1);
	primaryVolDescNumber.debug(indent + 1);
	volId.debug(indent + 1);
	volSeqNumber.debug(indent + 1);
	maxVolSeqNumber.debug(indent + 1);
	interchangeLevel.debug(indent + 1);
	maxInterchangeLevel.debug(indent + 1);
	charSetList.debug(indent + 1);
	maxCharSetList.debug(indent + 1);
	volSetId.debug(indent + 1);
	descCharSet.debug(indent + 1);
	explanatoryCharSet.debug(indent + 1);
	volAbstract.debug(indent + 1);
	volCopyrightNotice.debug(indent + 1);
	applicationId.debug(indent + 1);
	recordingDateAndTime.debug(indent + 1);
	implId.debug(indent + 1);
	implUse.debug(indent + 1);
	predecessorVolDescSeqLoc.debug(indent + 1);
	flags.debug(indent + 1);
	reserved.debug(indent + 1);
    }
//begin:add your code here

    public int getFixedTagId() { return 1; }

    /**
      ECMA-167規格におけるPrimary Volume Descriptor の検証を行う。
    */
    public UDF_ErrorList verify() throws UDF_Exception{

	UDF_ErrorList el = new UDF_ErrorList();
	final short category = UDF_Error.C_ECMA167;
	UDF_Error ret;


	/*　Descriptor Tagの整合性を問う　*/
	el.addError(super.verify());  // Descriptor Tag

	/*　Descriptor Tag Id の検証を行う　*/
	ret = verifyId();
	if(ret.isError()){

	    ret.setRefer("3/10.1.1");
	    el.addError(ret);
	}

	/*　各メンバの検証を行う　*/
	el.addError(volId.verify("Volume Identifier"));
	el.addError(descCharSet.verify("Descriptor Character Set"));
	el.addError(explanatoryCharSet.verify("Explanatory Character Set"));
	el.addError(volCopyrightNotice.verify("Volume Copyright Notice"));
//	el.addError(applicationId.verify("Application Identifier"));  // UDF2.01以降、解釈が微妙に異なる
	el.addError(recordingDateAndTime.verify("Recording Date and Time"));
	el.addError(implId.verify("Implementation Identifier"));

	/*　フラグの予約ビットがONになっていてはいけない　*/
	if((flags.getIntValue() & 0xfffe) != 0){

	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Flags",
			 "Shall be reserved for future standardisation and all bits shall be set to ZERO.",
			 "3/10.1.21", String.valueOf(flags.getIntValue()), ""));
	}

	/*　リザーブ領域は０クリアされて　*/
	if(!UDF_Util.isAllZero(reserved.getData())){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Reserved",
			 "This field shall be reserved for future standardisation and all bytes shall be se to #00.",
			 "3/10.1.22"));
	}
	
	el.setGlobalPoint(getGlobalPoint());
	el.setRName("Primary Volume Descriptor");
	return el;
    }
    /*
    public void postReadFromXMLHook(Node n){
	super.postReadFromXMLHook(n);
	if(env.primaryDesc == null)
	    env.primaryDesc = this;
    }
    */
    public void recalc(short type, UDF_RandomAccess f){
	super.recalc(type, f);
	/*
	if(type == RECALC_ENV){
	    if(env.primaryDesc == null)
		env.primaryDesc = this;
	}
	*/
    }
    public void postVolReadHook(UDF_RandomAccess f){
	;
    }
//end:
};
