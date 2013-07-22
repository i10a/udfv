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
Boot&nbsp;Descriptor&nbsp;を表現するクラス。<br/>


<dl>

<dt>フィールド一覧</dt>
<dd>
<table border="1">
<tr><td>メンバ</td><td>型</td><td>サイズ</td></tr>
<tr><td><b>StructureType</b></td><td><b>UDF_uint8</b></td><td><i>1</i></td></tr>
<tr><td><b>StandardId</b></td><td><b>UDF_bytes</b></td><td><i>5</i></td></tr>
<tr><td><b>StructureVersion</b></td><td><b>UDF_uint8</b></td><td><i>1</i></td></tr>
<tr><td><b>Reserved</b></td><td><b>UDF_bytes</b></td><td><i>1</i></td></tr>
<tr><td><b>ArchitectureType</b></td><td><b>UDF_regid</b></td><td><i>architectureType.getSize()</i></td></tr>
<tr><td><b>BootId</b></td><td><b>UDF_regid</b></td><td><i>bootId.getSize()</i></td></tr>
<tr><td><b>BootExtentLoc</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>BootExtentLen</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>LoadAddress</b></td><td><b>UDF_uint64</b></td><td><i>8</i></td></tr>
<tr><td><b>StartAddress</b></td><td><b>UDF_uint64</b></td><td><i>8</i></td></tr>
<tr><td><b>DescCreationDateAndTime</b></td><td><b>UDF_timestamp</b></td><td><i>descCreationDateAndTime.getSize()</i></td></tr>
<tr><td><b>Flags</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td></tr>
<tr><td><b>Reserved2</b></td><td><b>UDF_bytes</b></td><td><i>32</i></td></tr>
<tr><td><b>BootUse</b></td><td><b>UDF_bytes</b></td><td><i>1906</i></td></tr>
</table>
</dd>
</dl>
*/
public class UDF_ECMA167_BOOT2 extends UDF_Element 
{

    /**
	このオブジェクトのパッケージ名を除いたクラス名を取得する。

	@return クラス名
    */
    public static String getUDFClassName( ) {
        return "UDF_ECMA167_BOOT2";
    }

    /**
	コンストラクタ。

	@param elem   親エレメント。
	@param prefix ネームスペース。
	@param name   エレメント名。
    */
    public UDF_ECMA167_BOOT2(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }
    /**
	このUDFエレメントのサイズを返す。

	@return UDFエレメントのサイズ。
    */
    public int getSize(){
	return 0+1+standardId.getSize()+1+reserved.getSize()+architectureType.getSize()+bootId.getSize()+4+4+8+8+descCreationDateAndTime.getSize()+2+reserved2.getSize()+bootUse.getSize();
    }

    /**
	このUDFエレメントのサイズをlong 型で返す。

	@return UDFエレメントのサイズ。
    */
    public long getLongSize(){
	return (long)0+1+standardId.getSize()+1+reserved.getSize()+architectureType.getSize()+bootId.getSize()+4+4+8+8+descCreationDateAndTime.getSize()+2+reserved2.getSize()+bootUse.getSize();
    }
    private UDF_uint8 structureType;
    private UDF_bytes standardId;
    private UDF_uint8 structureVersion;
    private UDF_bytes reserved;
    private UDF_regid architectureType;
    private UDF_regid bootId;
    private UDF_uint32 bootExtentLoc;
    private UDF_uint32 bootExtentLen;
    private UDF_uint64 loadAddress;
    private UDF_uint64 startAddress;
    private UDF_timestamp descCreationDateAndTime;
    private UDF_uint16 flags;
    private UDF_bytes reserved2;
    private UDF_bytes bootUse;

    /**
	structureTypeを取得する。

	@return 取得したstructureType を返す。
    */
    public UDF_uint8 getStructureType(){return structureType;}
    /**
	standardIdを取得する。

	@return 取得したstandardId を返す。
    */
    public UDF_bytes getStandardId(){return standardId;}
    /**
	structureVersionを取得する。

	@return 取得したstructureVersion を返す。
    */
    public UDF_uint8 getStructureVersion(){return structureVersion;}
    /**
	reservedを取得する。

	@return 取得したreserved を返す。
    */
    public UDF_bytes getReserved(){return reserved;}
    /**
	architectureTypeを取得する。

	@return 取得したarchitectureType を返す。
    */
    public UDF_regid getArchitectureType(){return architectureType;}
    /**
	bootIdを取得する。

	@return 取得したbootId を返す。
    */
    public UDF_regid getBootId(){return bootId;}
    /**
	bootExtentLocを取得する。

	@return 取得したbootExtentLoc を返す。
    */
    public UDF_uint32 getBootExtentLoc(){return bootExtentLoc;}
    /**
	bootExtentLenを取得する。

	@return 取得したbootExtentLen を返す。
    */
    public UDF_uint32 getBootExtentLen(){return bootExtentLen;}
    /**
	loadAddressを取得する。

	@return 取得したloadAddress を返す。
    */
    public UDF_uint64 getLoadAddress(){return loadAddress;}
    /**
	startAddressを取得する。

	@return 取得したstartAddress を返す。
    */
    public UDF_uint64 getStartAddress(){return startAddress;}
    /**
	descCreationDateAndTimeを取得する。

	@return 取得したdescCreationDateAndTime を返す。
    */
    public UDF_timestamp getDescCreationDateAndTime(){return descCreationDateAndTime;}
    /**
	flagsを取得する。

	@return 取得したflags を返す。
    */
    public UDF_uint16 getFlags(){return flags;}
    /**
	reserved2を取得する。

	@return 取得したreserved2 を返す。
    */
    public UDF_bytes getReserved2(){return reserved2;}
    /**
	bootUseを取得する。

	@return 取得したbootUse を返す。
    */
    public UDF_bytes getBootUse(){return bootUse;}

    /**
	structureTypeを設定する。

	@param	v 設定する値。
    */
    public void setStructureType(UDF_uint8 v){replaceChild(v, structureType); structureType = v;}
    /**
	standardIdを設定する。

	@param	v 設定する値。
    */
    public void setStandardId(UDF_bytes v){replaceChild(v, standardId); standardId = v;}
    /**
	structureVersionを設定する。

	@param	v 設定する値。
    */
    public void setStructureVersion(UDF_uint8 v){replaceChild(v, structureVersion); structureVersion = v;}
    /**
	reservedを設定する。

	@param	v 設定する値。
    */
    public void setReserved(UDF_bytes v){replaceChild(v, reserved); reserved = v;}
    /**
	architectureTypeを設定する。

	@param	v 設定する値。
    */
    public void setArchitectureType(UDF_regid v){replaceChild(v, architectureType); architectureType = v;}
    /**
	bootIdを設定する。

	@param	v 設定する値。
    */
    public void setBootId(UDF_regid v){replaceChild(v, bootId); bootId = v;}
    /**
	bootExtentLocを設定する。

	@param	v 設定する値。
    */
    public void setBootExtentLoc(UDF_uint32 v){replaceChild(v, bootExtentLoc); bootExtentLoc = v;}
    /**
	bootExtentLenを設定する。

	@param	v 設定する値。
    */
    public void setBootExtentLen(UDF_uint32 v){replaceChild(v, bootExtentLen); bootExtentLen = v;}
    /**
	loadAddressを設定する。

	@param	v 設定する値。
    */
    public void setLoadAddress(UDF_uint64 v){replaceChild(v, loadAddress); loadAddress = v;}
    /**
	startAddressを設定する。

	@param	v 設定する値。
    */
    public void setStartAddress(UDF_uint64 v){replaceChild(v, startAddress); startAddress = v;}
    /**
	descCreationDateAndTimeを設定する。

	@param	v 設定する値。
    */
    public void setDescCreationDateAndTime(UDF_timestamp v){replaceChild(v, descCreationDateAndTime); descCreationDateAndTime = v;}
    /**
	flagsを設定する。

	@param	v 設定する値。
    */
    public void setFlags(UDF_uint16 v){replaceChild(v, flags); flags = v;}
    /**
	reserved2を設定する。

	@param	v 設定する値。
    */
    public void setReserved2(UDF_bytes v){replaceChild(v, reserved2); reserved2 = v;}
    /**
	bootUseを設定する。

	@param	v 設定する値。
    */
    public void setBootUse(UDF_bytes v){replaceChild(v, bootUse); bootUse = v;}

    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long rsize = 0;
	preReadHook(f);
	structureType = (UDF_uint8)createElement("UDF_uint8", "", "structure-type");
	rsize += structureType.readFrom(f);
	standardId = (UDF_bytes)createElement("UDF_bytes", "", "standard-id");
	standardId.setSize(5);
	rsize += standardId.readFrom(f);
	structureVersion = (UDF_uint8)createElement("UDF_uint8", "", "structure-version");
	rsize += structureVersion.readFrom(f);
	reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
	reserved.setSize(1);
	rsize += reserved.readFrom(f);
	architectureType = (UDF_regid)createElement("UDF_regid", "", "architecture-type");
	rsize += architectureType.readFrom(f);
	bootId = (UDF_regid)createElement("UDF_regid", "", "boot-id");
	rsize += bootId.readFrom(f);
	bootExtentLoc = (UDF_uint32)createElement("UDF_uint32", "", "boot-extent-loc");
	rsize += bootExtentLoc.readFrom(f);
	bootExtentLen = (UDF_uint32)createElement("UDF_uint32", "", "boot-extent-len");
	rsize += bootExtentLen.readFrom(f);
	loadAddress = (UDF_uint64)createElement("UDF_uint64", "", "load-address");
	rsize += loadAddress.readFrom(f);
	startAddress = (UDF_uint64)createElement("UDF_uint64", "", "start-address");
	rsize += startAddress.readFrom(f);
	descCreationDateAndTime = (UDF_timestamp)createElement("UDF_timestamp", "", "desc-creation-date-and-time");
	rsize += descCreationDateAndTime.readFrom(f);
	flags = (UDF_uint16)createElement("UDF_uint16", "", "flags");
	rsize += flags.readFrom(f);
	reserved2 = (UDF_bytes)createElement("UDF_bytes", "", "reserved2");
	reserved2.setSize(32);
	rsize += reserved2.readFrom(f);
	bootUse = (UDF_bytes)createElement("UDF_bytes", "", "boot-use");
	bootUse.setSize(1906);
	rsize += bootUse.readFrom(f);
	apply( );
	postReadHook(f);
	return rsize;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long wsize = 0;
	wsize += structureType.writeTo(f);
	wsize += standardId.writeTo(f);
	wsize += structureVersion.writeTo(f);
	wsize += reserved.writeTo(f);
	wsize += architectureType.writeTo(f);
	wsize += bootId.writeTo(f);
	wsize += bootExtentLoc.writeTo(f);
	wsize += bootExtentLen.writeTo(f);
	wsize += loadAddress.writeTo(f);
	wsize += startAddress.writeTo(f);
	wsize += descCreationDateAndTime.writeTo(f);
	wsize += flags.writeTo(f);
	wsize += reserved2.writeTo(f);
	wsize += bootUse.writeTo(f);
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
	    else if(name.equals("structure-type")){
		structureType = (UDF_uint8)createElement("UDF_uint8", "", "structure-type");
		structureType.readFromXML(child);
	    }
	    else if(name.equals("standard-id")){
		standardId = (UDF_bytes)createElement("UDF_bytes", "", "standard-id");
		standardId.setSize(5);
		standardId.readFromXML(child);
	    }
	    else if(name.equals("structure-version")){
		structureVersion = (UDF_uint8)createElement("UDF_uint8", "", "structure-version");
		structureVersion.readFromXML(child);
	    }
	    else if(name.equals("reserved")){
		reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
		reserved.setSize(1);
		reserved.readFromXML(child);
	    }
	    else if(name.equals("architecture-type")){
		architectureType = (UDF_regid)createElement("UDF_regid", "", "architecture-type");
		architectureType.readFromXML(child);
	    }
	    else if(name.equals("boot-id")){
		bootId = (UDF_regid)createElement("UDF_regid", "", "boot-id");
		bootId.readFromXML(child);
	    }
	    else if(name.equals("boot-extent-loc")){
		bootExtentLoc = (UDF_uint32)createElement("UDF_uint32", "", "boot-extent-loc");
		bootExtentLoc.readFromXML(child);
	    }
	    else if(name.equals("boot-extent-len")){
		bootExtentLen = (UDF_uint32)createElement("UDF_uint32", "", "boot-extent-len");
		bootExtentLen.readFromXML(child);
	    }
	    else if(name.equals("load-address")){
		loadAddress = (UDF_uint64)createElement("UDF_uint64", "", "load-address");
		loadAddress.readFromXML(child);
	    }
	    else if(name.equals("start-address")){
		startAddress = (UDF_uint64)createElement("UDF_uint64", "", "start-address");
		startAddress.readFromXML(child);
	    }
	    else if(name.equals("desc-creation-date-and-time")){
		descCreationDateAndTime = (UDF_timestamp)createElement("UDF_timestamp", "", "desc-creation-date-and-time");
		descCreationDateAndTime.readFromXML(child);
	    }
	    else if(name.equals("flags")){
		flags = (UDF_uint16)createElement("UDF_uint16", "", "flags");
		flags.readFromXML(child);
	    }
	    else if(name.equals("reserved2")){
		reserved2 = (UDF_bytes)createElement("UDF_bytes", "", "reserved2");
		reserved2.setSize(32);
		reserved2.readFromXML(child);
	    }
	    else if(name.equals("boot-use")){
		bootUse = (UDF_bytes)createElement("UDF_bytes", "", "boot-use");
		bootUse.setSize(1906);
		bootUse.readFromXML(child);
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
	structureType = (UDF_uint8)createElement("UDF_uint8", "", "structure-type");
	structureType.setDefaultValue();
	standardId = (UDF_bytes)createElement("UDF_bytes", "", "standard-id");
	standardId.setSize(5);
	standardId.setDefaultValue();
	standardId.setValue("BOOT2");
	structureVersion = (UDF_uint8)createElement("UDF_uint8", "", "structure-version");
	structureVersion.setDefaultValue();
	structureVersion.setValue(1);
	reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
	reserved.setSize(1);
	reserved.setDefaultValue();
	architectureType = (UDF_regid)createElement("UDF_regid", "", "architecture-type");
	architectureType.setDefaultValue();
	bootId = (UDF_regid)createElement("UDF_regid", "", "boot-id");
	bootId.setDefaultValue();
	bootExtentLoc = (UDF_uint32)createElement("UDF_uint32", "", "boot-extent-loc");
	bootExtentLoc.setDefaultValue();
	bootExtentLen = (UDF_uint32)createElement("UDF_uint32", "", "boot-extent-len");
	bootExtentLen.setDefaultValue();
	loadAddress = (UDF_uint64)createElement("UDF_uint64", "", "load-address");
	loadAddress.setDefaultValue();
	startAddress = (UDF_uint64)createElement("UDF_uint64", "", "start-address");
	startAddress.setDefaultValue();
	descCreationDateAndTime = (UDF_timestamp)createElement("UDF_timestamp", "", "desc-creation-date-and-time");
	descCreationDateAndTime.setDefaultValue();
	flags = (UDF_uint16)createElement("UDF_uint16", "", "flags");
	flags.setDefaultValue();
	reserved2 = (UDF_bytes)createElement("UDF_bytes", "", "reserved2");
	reserved2.setSize(32);
	reserved2.setDefaultValue();
	bootUse = (UDF_bytes)createElement("UDF_bytes", "", "boot-use");
	bootUse.setSize(1906);
	bootUse.setDefaultValue();

	apply( );
	postSetDefaultValueHook();
    }


    /**
	複製する
    */
    public UDF_Element duplicateElement(){
	UDF_ECMA167_BOOT2 dup = new UDF_ECMA167_BOOT2(this, null, getName());
	dup.setDefaultValue();
	dup.duplicateHook(this);
	dup.setStructureType((UDF_uint8)structureType.duplicateElement());
	dup.setStandardId((UDF_bytes)standardId.duplicateElement());
	dup.setStructureVersion((UDF_uint8)structureVersion.duplicateElement());
	dup.setReserved((UDF_bytes)reserved.duplicateElement());
	dup.setArchitectureType((UDF_regid)architectureType.duplicateElement());
	dup.setBootId((UDF_regid)bootId.duplicateElement());
	dup.setBootExtentLoc((UDF_uint32)bootExtentLoc.duplicateElement());
	dup.setBootExtentLen((UDF_uint32)bootExtentLen.duplicateElement());
	dup.setLoadAddress((UDF_uint64)loadAddress.duplicateElement());
	dup.setStartAddress((UDF_uint64)startAddress.duplicateElement());
	dup.setDescCreationDateAndTime((UDF_timestamp)descCreationDateAndTime.duplicateElement());
	dup.setFlags((UDF_uint16)flags.duplicateElement());
	dup.setReserved2((UDF_bytes)reserved2.duplicateElement());
	dup.setBootUse((UDF_bytes)bootUse.duplicateElement());

	apply();

	return dup;
    }

    /**
        子の UDF Element の Node を append しなおす。

    */
    private void apply( ) {

	removeAllChildren();
	appendChild(structureType);
	appendChild(standardId);
	appendChild(structureVersion);
	appendChild(reserved);
	appendChild(architectureType);
	appendChild(bootId);
	appendChild(bootExtentLoc);
	appendChild(bootExtentLen);
	appendChild(loadAddress);
	appendChild(startAddress);
	appendChild(descCreationDateAndTime);
	appendChild(flags);
	appendChild(reserved2);
	appendChild(bootUse);
    }

    public String getInfo(int indent){
	String a = "";
	a += UDF_Util.repeat(' ', indent);
	a += structureType.getInfo(indent + 1);
	a += standardId.getInfo(indent + 1);
	a += structureVersion.getInfo(indent + 1);
	a += reserved.getInfo(indent + 1);
	a += architectureType.getInfo(indent + 1);
	a += bootId.getInfo(indent + 1);
	a += bootExtentLoc.getInfo(indent + 1);
	a += bootExtentLen.getInfo(indent + 1);
	a += loadAddress.getInfo(indent + 1);
	a += startAddress.getInfo(indent + 1);
	a += descCreationDateAndTime.getInfo(indent + 1);
	a += flags.getInfo(indent + 1);
	a += reserved2.getInfo(indent + 1);
	a += bootUse.getInfo(indent + 1);
      return a;
    }
    public void debug(int indent){
	System.err.print(UDF_Util.repeat(' ', indent));

	String info = " gp=" + getGlobalPoint();
	info += " pn=" + getElemPartRefNo();
	info += " sn=" + getPartSubno();
	System.err.println(getName() + ": (" + hashCode() + ")" + info);	structureType.debug(indent + 1);
	standardId.debug(indent + 1);
	structureVersion.debug(indent + 1);
	reserved.debug(indent + 1);
	architectureType.debug(indent + 1);
	bootId.debug(indent + 1);
	bootExtentLoc.debug(indent + 1);
	bootExtentLen.debug(indent + 1);
	loadAddress.debug(indent + 1);
	startAddress.debug(indent + 1);
	descCreationDateAndTime.debug(indent + 1);
	flags.debug(indent + 1);
	reserved2.debug(indent + 1);
	bootUse.debug(indent + 1);
    }
//begin:add your code here
    protected void init( ) {
	super.init();

        //　この要素の記録されている論理位置をＸＭＬに表記するように設定します　//
	//setViewGlobalPoint(true);
    }
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	final short category = UDF_Error.C_ECMA167;
	
	
	// Structure Type は0
	if(structureType.getIntValue() != 0){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Structure Type",
			 "This field shall specify 0.", "2/9.4.1", String.valueOf(structureType.getIntValue()), "0"));
	}
	
	// Standard Id は"BOOT2"
	byte[] idbuf = standardId.getData();
	if(!UDF_Util.cmpBytesString(idbuf, "BOOT2")){
	    
	    UDF_Error err = new UDF_Error(category, UDF_Error.L_ERROR, "Standard Identifier",
					  "This field shall specify \"BOOT2\".", "2/9.4.2", "", "BOOT2");
	    
	    try{
		err.setRecordedValue(new String(idbuf, "ISO-8859-1"));
	    }
	    catch(Exception e){
		e.printStackTrace();
	    }
	    
	    el.addError(err);
	}
	
	// Structure Version は1
	if(structureVersion.getIntValue() != 1){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Structure Version",
			 "This field shall specify the version of this descriptor. " +
			 "The value 1 shall indicate the structure of Part2.", "2/9.4.3",
			 String.valueOf(structureVersion.getIntValue()), "1"));
	}
	
	// 0
	if(reserved.getData()[0] != 0){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Reserved",
			 "This field shall be reserved for future standardisation and shall be set to #00.",
			 "2/9.4.4", String.valueOf(reserved.getData()[0]), "0"));
	}
	
	// Architecture Type
	el.addError(architectureType.verify("Architecture Type"));
	// Boot Identifier
	el.addError(bootId.verify("Boot Identifier"));
	
	// Boot Extent Len が0 のとき、Boot Extent Loc も0 でなければならない
	if(bootExtentLen.getLongValue() == 0 && bootExtentLoc.getLongValue() != 0){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Boot Extent Location",
			 "If the Boot Extent Length field contains 0, then no boot extent is specified and this field shall contain 0.",
			 "4/9.4.7", String.valueOf(bootExtentLoc.getLongValue()), "0"));
	}
	
	// Descriptor Creation Date and Time
	el.addError(descCreationDateAndTime.verify("Descriptor Creation Date and Time"));
	
	// Flags の1-15 ビットは0でなければならない
	if((flags.getIntValue() & 0xfffe) != 0){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Flags",
			 "This field shall specify certain characteristics of the Boot Descriptor as shown in figure 2/7.\n" +
			 "1-15: Shall be reserved for future standardisation and all bits shall be set to ZERO.",
			 "2/9.4.13", String.valueOf(flags.getIntValue()), ""));
	}
	
	// Reserved は0
	if(!UDF_Util.isAllZero(reserved2.getData())){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Reserved",
			 "This field shall be reserved for future standardisation and all bytes shall be set to #00.",
			 "2/9.4.13"));
	}
	
	
	el.setGlobalPoint(getGlobalPoint());
	el.setRName("Boot Descriptor");
	return el;
    }
    protected boolean hasGlobalPoint(){
	return true;
    }
    
//end:
};
