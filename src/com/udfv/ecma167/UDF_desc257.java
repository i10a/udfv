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
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/**
File&nbsp;Identifier&nbsp;Descriptor&nbsp;を表現するクラス。

<dl>

<dt>フィールド一覧</dt>
<dd>
<table border="1">
<tr><td>メンバ</td><td>型</td><td>サイズ</td></tr>
<tr><td><b>DescTag</b></td><td><b>UDF_tag</b></td><td><i>descTag.getSize()</i></td></tr>
<tr><td><b>FileVersionNumber</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td></tr>
<tr><td><b>FileChar</b></td><td><b>UDF_uint8</b></td><td><i>1</i></td></tr>
<tr><td><b>LenOfFileId</b></td><td><b>UDF_uint8</b></td><td><i>1</i></td></tr>
<tr><td><b>ICB</b></td><td><b>UDF_long_ad</b></td><td><i>iCB.getSize()</i></td></tr>
<tr><td><b>LenOfImplUse</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td></tr>
<tr><td><b>ImplUse</b></td><td><b>UDF_bytes</b></td><td><i>getLenOfImplUse().getIntValue()</i></td></tr>
<tr><td><b>FileId</b></td><td><b>UDF_bytes</b></td><td><i>getLenOfFileId().getIntValue()</i></td></tr>
<tr><td><b>Padding</b></td><td><b>UDF_pad</b></td><td><i>4</i></td></tr>
</table>
</dd>
</dl>
*/
public class UDF_desc257 extends UDF_CrcDesc 
{

    /**
	このオブジェクトのパッケージ名を除いたクラス名を取得する。

	@return クラス名
    */
    public static String getUDFClassName( ) {
        return "UDF_desc257";
    }

    /**
	コンストラクタ。

	@param elem   親エレメント。
	@param prefix ネームスペース。
	@param name   エレメント名。
    */
    public UDF_desc257(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }
    /**
	このUDFエレメントのサイズを返す。

	@return UDFエレメントのサイズ。
    */
    public int getSize(){
	return 0+descTag.getSize()+2+1+1+iCB.getSize()+2+implUse.getSize()+fileId.getSize()+padding.getSize();
    }

    /**
	このUDFエレメントのサイズをlong 型で返す。

	@return UDFエレメントのサイズ。
    */
    public long getLongSize(){
	return (long)0+descTag.getSize()+2+1+1+iCB.getSize()+2+implUse.getSize()+fileId.getSize()+padding.getSize();
    }
    private UDF_tag descTag;
    private UDF_uint16 fileVersionNumber;
    private UDF_uint8 fileChar;
    private UDF_uint8 lenOfFileId;
    private UDF_long_ad iCB;
    private UDF_uint16 lenOfImplUse;
    private UDF_bytes implUse;
    private UDF_bytes fileId;
    private UDF_pad padding;

    /**
	descTagを取得する。

	@return 取得したdescTag を返す。
    */
    public UDF_tag getDescTag(){return descTag;}
    /**
	fileVersionNumberを取得する。

	@return 取得したfileVersionNumber を返す。
    */
    public UDF_uint16 getFileVersionNumber(){return fileVersionNumber;}
    /**
	fileCharを取得する。

	@return 取得したfileChar を返す。
    */
    public UDF_uint8 getFileChar(){return fileChar;}
    /**
	lenOfFileIdを取得する。

	@return 取得したlenOfFileId を返す。
    */
    public UDF_uint8 getLenOfFileId(){return lenOfFileId;}
    /**
	iCBを取得する。

	@return 取得したiCB を返す。
    */
    public UDF_long_ad getICB(){return iCB;}
    /**
	lenOfImplUseを取得する。

	@return 取得したlenOfImplUse を返す。
    */
    public UDF_uint16 getLenOfImplUse(){return lenOfImplUse;}
    /**
	implUseを取得する。

	@return 取得したimplUse を返す。
    */
    public UDF_bytes getImplUse(){return implUse;}
    /**
	fileIdを取得する。

	@return 取得したfileId を返す。
    */
    public UDF_bytes getFileId(){return fileId;}
    /**
	paddingを取得する。

	@return 取得したpadding を返す。
    */
    public UDF_pad getPadding(){return padding;}

    /**
	descTagを設定する。

	@param	v 設定する値。
    */
    public void setDescTag(UDF_tag v){replaceChild(v, descTag); descTag = v;}
    /**
	fileVersionNumberを設定する。

	@param	v 設定する値。
    */
    public void setFileVersionNumber(UDF_uint16 v){replaceChild(v, fileVersionNumber); fileVersionNumber = v;}
    /**
	fileCharを設定する。

	@param	v 設定する値。
    */
    public void setFileChar(UDF_uint8 v){replaceChild(v, fileChar); fileChar = v;}
    /**
	lenOfFileIdを設定する。

	@param	v 設定する値。
    */
    public void setLenOfFileId(UDF_uint8 v){replaceChild(v, lenOfFileId); lenOfFileId = v;}
    /**
	iCBを設定する。

	@param	v 設定する値。
    */
    public void setICB(UDF_long_ad v){replaceChild(v, iCB); iCB = v;}
    /**
	lenOfImplUseを設定する。

	@param	v 設定する値。
    */
    public void setLenOfImplUse(UDF_uint16 v){replaceChild(v, lenOfImplUse); lenOfImplUse = v;}
    /**
	implUseを設定する。

	@param	v 設定する値。
    */
    public void setImplUse(UDF_bytes v){replaceChild(v, implUse); implUse = v;}
    /**
	fileIdを設定する。

	@param	v 設定する値。
    */
    public void setFileId(UDF_bytes v){replaceChild(v, fileId); fileId = v;}
    /**
	paddingを設定する。

	@param	v 設定する値。
    */
    public void setPadding(UDF_pad v){replaceChild(v, padding); padding = v;}

    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long rsize = 0;
	preReadHook(f);
	descTag = (UDF_tag)createElement("UDF_tag", "", "desc-tag");
	rsize += descTag.readFrom(f);
	fileVersionNumber = (UDF_uint16)createElement("UDF_uint16", "", "file-version-number");
	rsize += fileVersionNumber.readFrom(f);
	fileChar = (UDF_uint8)createElement("UDF_uint8", "", "file-char");
	rsize += fileChar.readFrom(f);
	lenOfFileId = (UDF_uint8)createElement("UDF_uint8", "", "len-of-file-id");
	rsize += lenOfFileId.readFrom(f);
	iCB = (UDF_long_ad)createElement("UDF_long_ad", "", "icb");
	rsize += iCB.readFrom(f);
	lenOfImplUse = (UDF_uint16)createElement("UDF_uint16", "", "len-of-impl-use");
	rsize += lenOfImplUse.readFrom(f);
	implUse = (UDF_bytes)createElement("UDF_bytes", "", "impl-use");
	implUse.setSize(getLenOfImplUse().getIntValue());
	rsize += implUse.readFrom(f);
	fileId = (UDF_bytes)createElement("UDF_bytes", "", "file-id");
	fileId.setSize(getLenOfFileId().getIntValue());
	rsize += fileId.readFrom(f);
	padding = (UDF_pad)createElement("UDF_pad", "", "padding");
	padding.setAlign(4);
	rsize += padding.readFrom(f);
	apply( );
	postReadHook(f);
	return rsize;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long wsize = 0;
	wsize += descTag.writeTo(f);
	wsize += fileVersionNumber.writeTo(f);
	wsize += fileChar.writeTo(f);
	wsize += lenOfFileId.writeTo(f);
	wsize += iCB.writeTo(f);
	wsize += lenOfImplUse.writeTo(f);
	wsize += implUse.writeTo(f);
	wsize += fileId.writeTo(f);
	wsize += padding.writeTo(f);
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
	    else if(name.equals("file-version-number")){
		fileVersionNumber = (UDF_uint16)createElement("UDF_uint16", "", "file-version-number");
		fileVersionNumber.readFromXML(child);
	    }
	    else if(name.equals("file-char")){
		fileChar = (UDF_uint8)createElement("UDF_uint8", "", "file-char");
		fileChar.readFromXML(child);
	    }
	    else if(name.equals("len-of-file-id")){
		lenOfFileId = (UDF_uint8)createElement("UDF_uint8", "", "len-of-file-id");
		lenOfFileId.readFromXML(child);
	    }
	    else if(name.equals("icb")){
		iCB = (UDF_long_ad)createElement("UDF_long_ad", "", "icb");
		iCB.readFromXML(child);
	    }
	    else if(name.equals("len-of-impl-use")){
		lenOfImplUse = (UDF_uint16)createElement("UDF_uint16", "", "len-of-impl-use");
		lenOfImplUse.readFromXML(child);
	    }
	    else if(name.equals("impl-use")){
		implUse = (UDF_bytes)createElement("UDF_bytes", "", "impl-use");
		implUse.setSize(getLenOfImplUse().getIntValue());
		implUse.readFromXML(child);
	    }
	    else if(name.equals("file-id")){
		fileId = (UDF_bytes)createElement("UDF_bytes", "", "file-id");
		fileId.setSize(getLenOfFileId().getIntValue());
		fileId.readFromXML(child);
		if(env.encodefid == false){
		    UDF_bytes tmp_fileId = (UDF_bytes)createElement("UDF_bytes", "", "file-id");
		    tmp_fileId.setSize(fileId.getSize());
		    tmp_fileId.setData(fileId.getData());
		    setFileId(tmp_fileId);
		}
	    }
	    else if(name.equals("padding")){
		padding = (UDF_pad)createElement("UDF_pad", "", "padding");
		padding.setAlign(4);
		padding.readFromXML(child);
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
	fileVersionNumber = (UDF_uint16)createElement("UDF_uint16", "", "file-version-number");
	fileVersionNumber.setDefaultValue();
	fileChar = (UDF_uint8)createElement("UDF_uint8", "", "file-char");
	fileChar.setDefaultValue();
	lenOfFileId = (UDF_uint8)createElement("UDF_uint8", "", "len-of-file-id");
	lenOfFileId.setDefaultValue();
	iCB = (UDF_long_ad)createElement("UDF_long_ad", "", "icb");
	iCB.setDefaultValue();
	lenOfImplUse = (UDF_uint16)createElement("UDF_uint16", "", "len-of-impl-use");
	lenOfImplUse.setDefaultValue();
	implUse = (UDF_bytes)createElement("UDF_bytes", "", "impl-use");
	implUse.setSize(getLenOfImplUse().getIntValue());
	implUse.setDefaultValue();
	fileId = (UDF_bytes)createElement("UDF_bytes", "", "file-id");
	fileId.setSize(getLenOfFileId().getIntValue());
	fileId.setDefaultValue();
	padding = (UDF_pad)createElement("UDF_pad", "", "padding");
	padding.setAlign(4);
	padding.setDefaultValue();

	apply( );
	postSetDefaultValueHook();
    }


    /**
	複製する
    */
    public UDF_Element duplicateElement(){
	UDF_desc257 dup = new UDF_desc257(this, null, getName());
	dup.setDefaultValue();
	dup.duplicateHook(this);
	dup.setDescTag((UDF_tag)descTag.duplicateElement());
	dup.setFileVersionNumber((UDF_uint16)fileVersionNumber.duplicateElement());
	dup.setFileChar((UDF_uint8)fileChar.duplicateElement());
	dup.setLenOfFileId((UDF_uint8)lenOfFileId.duplicateElement());
	dup.setICB((UDF_long_ad)iCB.duplicateElement());
	dup.setLenOfImplUse((UDF_uint16)lenOfImplUse.duplicateElement());
	dup.setImplUse((UDF_bytes)implUse.duplicateElement());
	dup.setFileId((UDF_bytes)fileId.duplicateElement());
	dup.setPadding((UDF_pad)padding.duplicateElement());

	apply();

	return dup;
    }

    /**
        子の UDF Element の Node を append しなおす。

    */
    private void apply( ) {

	removeAllChildren();
	appendChild(descTag);
	appendChild(fileVersionNumber);
	appendChild(fileChar);
	appendChild(lenOfFileId);
	appendChild(iCB);
	appendChild(lenOfImplUse);
	appendChild(implUse);
	appendChild(fileId);
	appendChild(padding);
    }

    public String getInfo(int indent){
	String a = "";
	a += UDF_Util.repeat(' ', indent);
	a += descTag.getInfo(indent + 1);
	a += fileVersionNumber.getInfo(indent + 1);
	a += fileChar.getInfo(indent + 1);
	a += lenOfFileId.getInfo(indent + 1);
	a += iCB.getInfo(indent + 1);
	a += lenOfImplUse.getInfo(indent + 1);
	a += implUse.getInfo(indent + 1);
	a += fileId.getInfo(indent + 1);
	a += padding.getInfo(indent + 1);
      return a;
    }
    public void debug(int indent){
	System.err.print(UDF_Util.repeat(' ', indent));

	String info = " gp=" + getGlobalPoint();
	info += " pn=" + getElemPartRefNo();
	info += " sn=" + getPartSubno();
	System.err.println(getName() + ": (" + hashCode() + ")" + info);	descTag.debug(indent + 1);
	fileVersionNumber.debug(indent + 1);
	fileChar.debug(indent + 1);
	lenOfFileId.debug(indent + 1);
	iCB.debug(indent + 1);
	lenOfImplUse.debug(indent + 1);
	implUse.debug(indent + 1);
	fileId.debug(indent + 1);
	padding.debug(indent + 1);
    }
//begin:add your code here
    public void setUniqueId(long unique_id){
	//ECMA167レベルでは何もしない(see. udf102.UDF_desc257)
	return;
    }
    public long getUniqueId(){
	//ECMA167レベルでは何もしない(see. udf102.UDF_desc257)
	return 0;
    }
    public int getFixedTagId() { return 257; }

    public static final int EXISTENCE = 1;
    public static final int DIRECTORY = 2;
    public static final int DELETED = 4;
    public static final int PARENT = 8;
    public static final int METADATA = 16;

    //このFIDが差しているFE
    private UDF_FEDesc my_reference_to_fe;
    //このFIDが差されているFE
    private UDF_FEDesc my_referenced_by_fe;

    /**
       このFIDが差している FEを取得する。
     */
    public UDF_FEDesc getReferenceTo(){
	return my_reference_to_fe;
    }
    /**
       このFIDが差している FEを設定する。
     */
    public void setReferenceTo(UDF_FEDesc fe){
	my_reference_to_fe = fe;
    }

    /**
       このFIDが差されている FEを取得する。
     */
    public UDF_FEDesc getReferencedBy(){
	return my_referenced_by_fe;
    }
    /**
       このFIDが差されている FEを設定する。
     */
    public void setReferencedBy(UDF_FEDesc fe){
	my_referenced_by_fe = fe;
    }

    public void postReadHook(UDF_RandomAccess f) throws UDF_Exception,IOException{
	if(verifyId().isError())
	    throw new UDF_DescTagException(this, "Bad TAG Id: " + getDescTag().getTagId().getIntValue(),
					   UDF_Exception.T_BASIC, UDF_DescTagException.C_BADTAGID,
					   getDescTag().getTagId().getIntValue());

	if(env.encodefid){
	    getFileId().setEncoding(env.getFileSetDesc(getPartSubno()).getFileSetCharSet().genEncoding());
	}
    }

    public UDF_ErrorList verify() throws UDF_Exception{

	UDF_ErrorList el = new UDF_ErrorList();
	UDF_Error ret;
	final short category = UDF_Error.C_ECMA167;


	el.addError(super.verify());  // Descriptor Tag

	ret = verifyId();
	if(ret.isError()){

	    ret.setRefer("4/14.4.1");
	    el.addError(ret);
	}

	// File Version Number は32767 以下でなければならない
	int fvernum = fileVersionNumber.getIntValue();
	if(32767 < fvernum){

	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "File Version Number",
			 "The numbers 32768 to 65535 inclusive are reserved for future standardisation.",
			 "4/14.4.2", String.valueOf(fvernum), ""));
	}
	else if(fvernum == 0){

	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "File Version Number",
			 "This field shall specify the file version number of the file specified by " +
			 "the File Identifier field as a number in the range 1 to 32767 inclusive.",
			 "4/14.4.2", "0", ""));
	}

	int filechar = fileChar.getIntValue();

	// File Characteristics の5-7 ビットは0でなければならない
	if((filechar & 0xe0) != 0x00){

	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "File Characteristics",
			 "This field shall specify certain characteristics of the file as shown in figure 4/13.\n" +
			 "5-7: Shall be reserved for future standardisation and all bits shall be set to ZERO.",
			 "4/14.4.3", String.valueOf(fileChar.getIntValue()), ""));
	}

	// Parent bit が立っている場合・・・
	if((filechar & 0x08) != 0){

	    // len of fileid は0でなければならない
	    if(lenOfFileId.getIntValue() != 0){

		el.addError(new UDF_Error
			    (category, UDF_Error.L_ERROR, "Length Of File Identifier(=L_FI)",
			     "If the Parent bit of the File Characteristics field is set to ONE, " +
			     "the length of the File Identifier field shall be 0.",
			     "4/14.4.4", String.valueOf(lenOfFileId.getIntValue()), "0"));
	    }

	    // Directory bit は立っていなければならない
	    if((filechar & 0x02) == 0){

		el.addError(new UDF_Error
			    (category, UDF_Error.L_ERROR, "File Characteristics",
			     "If the Parent bit is set to ONE, then the Directory bit shall be set to ONE.",
			     "4/14.4.3"));
	    }
	}

	// Delete bit が立っている場合、ICB はすべて0でなければならない
	if((filechar & 0x04) == 1){

	    if(iCB.getPartRefNo() != 0 || iCB.getLbn() != 0 || iCB.getLen() != 0 || iCB.getFlag() != 0){

		el.addError(new UDF_Error
			    (category, UDF_Error.L_ERROR, "ICB",
			     "If the Delete bit of the File Characteristics field of the File Identifier Descriptor is set to ONE, " +
			     "the ICB field may contain all #00 bytes, in which case no ICB is specified.",
			     "4/14.4.5"));
	    }
	}


	// Directory bit が立っている場合、指している場所はDirectoryでなくてはならない。
	// 2005/5/9追加 by issei
	if((filechar & 0x02) == 1){

	    int ft = getReferenceTo() != null ? getReferenceTo().getICBFileType() : 0;
	    if(ft == UDF_icbtag.T_DIRECTORY || ft == UDF_icbtag.T_SDIRECTORY){
	    }
	    else{
		el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "File Characteristics",
			 "If the Directory bit of the File Characteristics field of the File Identifier Descriptor is set to ONE, " +
			 "shall mean that the file is directory",
			 "4/14.4.3"));
	    }
	}
	// 指している場所がディレクトリなら Directory Bitが立ってなくてはならない。
	// 2005/5/9追加 by issei
	{
	    int ft = getReferenceTo() != null ? getReferenceTo().getICBFileType() : 0;
	    if(ft == UDF_icbtag.T_DIRECTORY || ft == UDF_icbtag.T_SDIRECTORY){
		if((filechar & 0x02) == 0) {
		    el.addError(new UDF_Error
				(category, UDF_Error.L_ERROR, "File Characteristics",
				 "If the Directory bit of the File Characteristics field of the File Identifier Descriptor is set to ONE, " +
				 "shall mean that the file is directory",
				 "4/14.4.3"));
		}
	    }
	}


	// L_IU が4の倍数でない
	if(lenOfImplUse.getIntValue() % 4 != 0){

	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Length of Implementation Use(=L_IU)",
			 "This field shall specify the length, in bytes, of the Implementation Use field. " +
			 "L_IU shall be an integral multiple of 4.",
			 "4/14.4.6", String.valueOf(lenOfImplUse.getIntValue()), ""));
	}

	final int L_FI = lenOfFileId.getIntValue();
	final int L_IU = lenOfImplUse.getIntValue();

	// lenOfImplUse が0より大きいとき、最初の32バイトはregid でなければならない
	if(0 < lenOfImplUse.getIntValue()){

	    final int REGID_SIZE = 32;

	    UDF_RandomAccessBytes rab = new UDF_RandomAccessBytes
		(env, implUse.getData(), implUse.getPartMapOffset(), implUse.getElemPartRefNo(), implUse.getPartSubno());
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

	// ICB
	el.addError(iCB.verify("ICB"));

	// File Identifier
	el.addError(fileId.verify("File Identifier"));

	int padlen = (4 * ((L_FI + L_IU + 38 + 3) / 4)) - (L_FI + L_IU + 38);
	if(padding.getSize() != padlen){

	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Padding",
			 "This field shall be 4 x ip((L_FI+L_IU+38+3)/4) - (L_FI+L_IU+38) bytes long.",
			 "4/14.4.9", String.valueOf(padlen), String.valueOf(padding.getSize())));
	}

	if(!UDF_Util.isAllZero(padding.getData())){

	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Padding",
			 "This field shall be 4 x ip((L_FI+L_IU+38+3)/4) - " +
			 "(L_FI+L_IU+38) bytes long and shall contain all #00 bytes.",
			 "4/14.4.9"));
	}

	el.setGlobalPoint(getGlobalPoint());
	el.setRName("File Identifier Descriptor");
	return el;
    }

    /**
       File Version Number の値を検証します。
       エラーにはC_ECMA167 カテゴリ、L_ERROR レベル、原因、記録値、期待値が含まれます。

       @param num  比較検証する値。期待値にはこの値が使用されます。
       @return エラーインスタンス。
    */
    public UDF_Error verifyFileVerNum(int num){

	int fvernum = fileVersionNumber.getIntValue();
	if(num != fvernum){

	    return new UDF_Error(UDF_Error.C_ECMA167, UDF_Error.L_ERROR, "File Version Number",
				 "", "", String.valueOf(fvernum), String.valueOf(num));
	}
	else
	    return new UDF_Error();
    }
    /**

    */
    public void recalc(short type, UDF_RandomAccess f){
	super.recalc(type, f);
	try{
	    if(type == RECALC_ENV){
		if(getPartSubno() == 0){
		    int filechar = getFileChar().getIntValue();
		    if((filechar & UDF_desc257.PARENT) != 0)
			;
		    else if((filechar & UDF_desc257.DELETED) != 0)
			;
		    else if((filechar & UDF_desc257.METADATA) != 0)
			;
		    else if((filechar & UDF_desc257.DIRECTORY) != 0)
			env.recorded_num_directories++;
		    else
			env.recorded_num_files++;
		}
	    }
	    else if(type == RECALC_TREE){
		int filechar = getFileChar().getIntValue();
		if ((filechar & UDF_desc257.DELETED) == 0) {

		    String ref = getICB().getExtentLen().getAttribute("ref");
		    ref = UDF_Util.car(ref, '.');

		    if(ref.length() > 0){
			UDF_Extent fe_ext = (UDF_Extent)findById(ref);
			if(fe_ext != null){
			    UDF_FEDesc fe = (UDF_FEDesc)fe_ext.getFirstChild();
			    if(fe != null){
				//System.err.println(ref);
				fe.getReferencedFID().add(this);
				setReferenceTo(fe);
			    }
			}
		    }
		}
	    }
	}
	catch(Exception e){
	    ignoreMsg("recalc", e);
	    debug(0);
	}
    }

    /**
       引数の文字列を nカラムで整列
     */
    JPanel createJPanel(String title, Vector str1, int n){
	JPanel panel = new JPanel();
	Border border = new CompoundBorder(
		new MatteBorder(10, 20, 10, 20, Color.WHITE),
		new TitledBorder(null, title,
		                 TitledBorder.LEFT,
		                 TitledBorder.TOP)
	);
	LayoutManager lm = new GridLayout(str1.size() / n, 2);
	panel.setLayout(lm);
	panel.setBorder(border);
	panel.setBackground(Color.WHITE);

	Iterator it = str1.iterator();
	while(it.hasNext()){
	    String s = (String)it.next();
	    JLabel label = new JLabel(s);
	    panel.add(label);
	}

	return panel;
    }

    /**
       引数の文字列を 2カラムで整列(Gridbag版)

     */
    JPanel createJPanel2(String title, Vector str1){
	JPanel panel = new JPanel();
	Border border = new CompoundBorder(
		new MatteBorder(10, 20, 10, 20, Color.WHITE),
		new TitledBorder(null, title,
		                 TitledBorder.LEFT,
		                 TitledBorder.TOP)
	);
	panel.setBorder(border);
	panel.setBackground(Color.WHITE);

	GridBagLayout gridbag = new GridBagLayout();
	GridBagConstraints c = new GridBagConstraints();

	panel.setLayout(gridbag);

	c.fill = GridBagConstraints.BOTH;

	Iterator it = str1.iterator();
	while(it.hasNext()){
	    {
		String s = (String)it.next();
		c.weightx = 0.7;
		JLabel label = new JLabel(s);
		c.gridwidth = GridBagConstraints.RELATIVE;
		gridbag.setConstraints(label, c);
		panel.add(label);
	    }
	    {
		JLabel label = (JLabel)it.next();
		c.weightx = 1.0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		gridbag.setConstraints(label, c);
		panel.add(label);
	    }
	}

	return panel;
    }

    public JComponent getJInfo2() {
	Box panel = Box.createVerticalBox();

	Vector v = new Vector();
	v.add("File Name:");
	v.add(new JLabel(getFileId().getStringData()));
	v.add("File Characteristics:");
	v.add(new JLabel(""+getFileChar().getIntValue()));
	panel.add(createJPanel2("File Identifier", v));

	UDF_FEDesc fe = getReferenceTo();
	v = new Vector();
	v.add("ICB Type:");
	v.add(new JLabel(fe.getICBFileType() + "(" + UDF_icbtag.typeToString(fe.getICBFileType()) + ")"));
	v.add("ICB Flag:");
	v.add(new JLabel(fe.getICBFlags() + "(" + UDF_icbtag.charToString(fe.getICBFlags()) + ")"));
	v.add("Permissions:");
	v.add(new JLabel(fe.getPermissions().getIntValue() + "(" + UDF_FEDesc.permToString(fe.getPermissions().getIntValue()) + ")"));
	v.add("File Link Count:");
	v.add(new JLabel(""+fe.getFileLinkCount().getIntValue()));
	v.add("Unique Id:");
	v.add(new JLabel(""+fe.getUniqueId().getLongValue()));
	v.add("UID:");
	v.add(new JLabel(""+fe.getUid().getLongValue()));
	v.add("GID:");
	v.add(new JLabel(""+fe.getGid().getLongValue()));
	v.add("Size:");
	v.add(new JLabel(""+fe.getADSize()));
	v.add("Access Date And Time:");
	v.add(new JLabel(fe.getAccessDateAndTime().getStringData()));
	v.add("Modification And Time:");
	v.add(new JLabel(fe.getModificationDateAndTime().getStringData()));
	v.add("Attribute Date And Time:");
	v.add(new JLabel(fe.getAccessDateAndTime().getStringData()));

	if(UDF_desc261.class.isAssignableFrom(fe.getClass())){
	    UDF_desc261 d = (UDF_desc261)fe;
	}
	else{
	    UDF_desc266 d = (UDF_desc266)fe;
	    v.add("Creation Date And Time:");
	    v.add(new JLabel(d.getCreationDateAndTime().getStringData()));
	}

	panel.add(createJPanel2("File Entry", v));

	v = new Vector();
	String[] attrs = fe.getExtendedAttr().getInfo(0).split("\n");
	for(int i=0 ; i<attrs.length ; ++i){
	    System.err.println(attrs[i]);
	    v.add(attrs[i]);
	}
	panel.add(createJPanel("Extended Attribute", v, 1));

	v = new Vector();
	UDF_ADList adlist = fe.getADList();

	v.add("#");
	v.add("PartRefNo");
	v.add("LBN");
	v.add("length");

	if(adlist != null){
	    for(int i=0 ; i<adlist.size() ; ++i){
		UDF_AD ad = (UDF_AD)adlist.elementAt(i);
		v.add("" + i);
		v.add("" + ad.getPartRefNo());
		v.add("" + ad.getLbn());
		v.add("" + ad.getFlag() + ":" + ad.getLen());
	    }
	}

	panel.add(createJPanel("Allocation Descriptor", v, 4));

	return panel;
    }

//end:
};
