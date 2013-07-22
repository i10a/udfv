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
File&nbsp;Times&nbsp;Extended&nbsp;Attribute&nbsp;を表現するクラス。<br/>


<dl>

<dt>フィールド一覧</dt>
<dd>
<table border="1">
<tr><td>メンバ</td><td>型</td><td>サイズ</td></tr>
<tr><td><b>AttrType</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>AttrSubtype</b></td><td><b>UDF_uint8</b></td><td><i>1</i></td></tr>
<tr><td><b>Reserved</b></td><td><b>UDF_bytes</b></td><td><i>3</i></td></tr>
<tr><td><b>AttrLen</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>DataLen</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>FileTimeExistence</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>FileTimes</b></td><td><b>UDF_bytes</b></td><td><i>getDataLen().getIntValue()</i></td></tr>
</table>
</dd>
</dl>
*/
public class UDF_attr5 extends UDF_attr 
{

    /**
	このオブジェクトのパッケージ名を除いたクラス名を取得する。

	@return クラス名
    */
    public static String getUDFClassName( ) {
        return "UDF_attr5";
    }

    /**
	コンストラクタ。

	@param elem   親エレメント。
	@param prefix ネームスペース。
	@param name   エレメント名。
    */
    public UDF_attr5(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }
    /**
	このUDFエレメントのサイズを返す。

	@return UDFエレメントのサイズ。
    */
    public int getSize(){
	return 0+4+1+reserved.getSize()+4+4+4+fileTimes.getSize();
    }

    /**
	このUDFエレメントのサイズをlong 型で返す。

	@return UDFエレメントのサイズ。
    */
    public long getLongSize(){
	return (long)0+4+1+reserved.getSize()+4+4+4+fileTimes.getSize();
    }
    private UDF_uint32 attrType;
    private UDF_uint8 attrSubtype;
    private UDF_bytes reserved;
    private UDF_uint32 attrLen;
    private UDF_uint32 dataLen;
    private UDF_uint32 fileTimeExistence;
    private UDF_bytes fileTimes;

    /**
	attrTypeを取得する。

	@return 取得したattrType を返す。
    */
    public UDF_uint32 getAttrType(){return attrType;}
    /**
	attrSubtypeを取得する。

	@return 取得したattrSubtype を返す。
    */
    public UDF_uint8 getAttrSubtype(){return attrSubtype;}
    /**
	reservedを取得する。

	@return 取得したreserved を返す。
    */
    public UDF_bytes getReserved(){return reserved;}
    /**
	attrLenを取得する。

	@return 取得したattrLen を返す。
    */
    public UDF_uint32 getAttrLen(){return attrLen;}
    /**
	dataLenを取得する。

	@return 取得したdataLen を返す。
    */
    public UDF_uint32 getDataLen(){return dataLen;}
    /**
	fileTimeExistenceを取得する。

	@return 取得したfileTimeExistence を返す。
    */
    public UDF_uint32 getFileTimeExistence(){return fileTimeExistence;}
    /**
	fileTimesを取得する。

	@return 取得したfileTimes を返す。
    */
    public UDF_bytes getFileTimes(){return fileTimes;}

    /**
	attrTypeを設定する。

	@param	v 設定する値。
    */
    public void setAttrType(UDF_uint32 v){replaceChild(v, attrType); attrType = v;}
    /**
	attrSubtypeを設定する。

	@param	v 設定する値。
    */
    public void setAttrSubtype(UDF_uint8 v){replaceChild(v, attrSubtype); attrSubtype = v;}
    /**
	reservedを設定する。

	@param	v 設定する値。
    */
    public void setReserved(UDF_bytes v){replaceChild(v, reserved); reserved = v;}
    /**
	attrLenを設定する。

	@param	v 設定する値。
    */
    public void setAttrLen(UDF_uint32 v){replaceChild(v, attrLen); attrLen = v;}
    /**
	dataLenを設定する。

	@param	v 設定する値。
    */
    public void setDataLen(UDF_uint32 v){replaceChild(v, dataLen); dataLen = v;}
    /**
	fileTimeExistenceを設定する。

	@param	v 設定する値。
    */
    public void setFileTimeExistence(UDF_uint32 v){replaceChild(v, fileTimeExistence); fileTimeExistence = v;}
    /**
	fileTimesを設定する。

	@param	v 設定する値。
    */
    public void setFileTimes(UDF_bytes v){replaceChild(v, fileTimes); fileTimes = v;}

    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long rsize = 0;
	preReadHook(f);
	attrType = (UDF_uint32)createElement("UDF_uint32", "", "attr-type");
	rsize += attrType.readFrom(f);
	attrSubtype = (UDF_uint8)createElement("UDF_uint8", "", "attr-subtype");
	rsize += attrSubtype.readFrom(f);
	reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
	reserved.setSize(3);
	rsize += reserved.readFrom(f);
	attrLen = (UDF_uint32)createElement("UDF_uint32", "", "attr-len");
	rsize += attrLen.readFrom(f);
	dataLen = (UDF_uint32)createElement("UDF_uint32", "", "data-len");
	rsize += dataLen.readFrom(f);
	fileTimeExistence = (UDF_uint32)createElement("UDF_uint32", "", "file-time-existence");
	rsize += fileTimeExistence.readFrom(f);
	fileTimes = (UDF_bytes)createElement("UDF_bytes", "", "file-times");
	fileTimes.setSize(getDataLen().getIntValue());
	rsize += fileTimes.readFrom(f);
	apply( );
	postReadHook(f);
	return rsize;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long wsize = 0;
	wsize += attrType.writeTo(f);
	wsize += attrSubtype.writeTo(f);
	wsize += reserved.writeTo(f);
	wsize += attrLen.writeTo(f);
	wsize += dataLen.writeTo(f);
	wsize += fileTimeExistence.writeTo(f);
	wsize += fileTimes.writeTo(f);
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
	    else if(name.equals("attr-type")){
		attrType = (UDF_uint32)createElement("UDF_uint32", "", "attr-type");
		attrType.readFromXML(child);
	    }
	    else if(name.equals("attr-subtype")){
		attrSubtype = (UDF_uint8)createElement("UDF_uint8", "", "attr-subtype");
		attrSubtype.readFromXML(child);
	    }
	    else if(name.equals("reserved")){
		reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
		reserved.setSize(3);
		reserved.readFromXML(child);
	    }
	    else if(name.equals("attr-len")){
		attrLen = (UDF_uint32)createElement("UDF_uint32", "", "attr-len");
		attrLen.readFromXML(child);
	    }
	    else if(name.equals("data-len")){
		dataLen = (UDF_uint32)createElement("UDF_uint32", "", "data-len");
		dataLen.readFromXML(child);
	    }
	    else if(name.equals("file-time-existence")){
		fileTimeExistence = (UDF_uint32)createElement("UDF_uint32", "", "file-time-existence");
		fileTimeExistence.readFromXML(child);
	    }
	    else if(name.equals("file-times")){
		fileTimes = (UDF_bytes)createElement("UDF_bytes", "", "file-times");
		fileTimes.setSize(getDataLen().getIntValue());
		fileTimes.readFromXML(child);
	    }
	    else{
		System.err.println("Unknown XML tag:" + name);
	    }
	}
	apply( );
	postReadFromXMLHook(n);
    }

    /**
	初期値を設定する
    */
    public void setDefaultValue(){
	super.setDefaultValue();
	attrType = (UDF_uint32)createElement("UDF_uint32", "", "attr-type");
	attrType.setDefaultValue();
	attrType.setValue(5);
	attrSubtype = (UDF_uint8)createElement("UDF_uint8", "", "attr-subtype");
	attrSubtype.setDefaultValue();
	attrSubtype.setValue(1);
	reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
	reserved.setSize(3);
	reserved.setDefaultValue();
	attrLen = (UDF_uint32)createElement("UDF_uint32", "", "attr-len");
	attrLen.setDefaultValue();
	dataLen = (UDF_uint32)createElement("UDF_uint32", "", "data-len");
	dataLen.setDefaultValue();
	fileTimeExistence = (UDF_uint32)createElement("UDF_uint32", "", "file-time-existence");
	fileTimeExistence.setDefaultValue();
	fileTimes = (UDF_bytes)createElement("UDF_bytes", "", "file-times");
	fileTimes.setSize(getDataLen().getIntValue());
	fileTimes.setDefaultValue();

	apply( );
	postSetDefaultValueHook();
    }


    /**
	複製する
    */
    public UDF_Element duplicateElement(){
	UDF_attr5 dup = new UDF_attr5(this, null, getName());
	dup.setDefaultValue();
	dup.duplicateHook(this);
	dup.setAttrType((UDF_uint32)attrType.duplicateElement());
	dup.setAttrSubtype((UDF_uint8)attrSubtype.duplicateElement());
	dup.setReserved((UDF_bytes)reserved.duplicateElement());
	dup.setAttrLen((UDF_uint32)attrLen.duplicateElement());
	dup.setDataLen((UDF_uint32)dataLen.duplicateElement());
	dup.setFileTimeExistence((UDF_uint32)fileTimeExistence.duplicateElement());
	dup.setFileTimes((UDF_bytes)fileTimes.duplicateElement());

	apply();

	return dup;
    }

    /**
        子の UDF Element の Node を append しなおす。

    */
    private void apply( ) {

	removeAllChildren();
	appendChild(attrType);
	appendChild(attrSubtype);
	appendChild(reserved);
	appendChild(attrLen);
	appendChild(dataLen);
	appendChild(fileTimeExistence);
	appendChild(fileTimes);
    }

    public void debug(int indent){
	System.err.print(UDF_Util.repeat(' ', indent));
	System.err.println(getName() + ": (" + hashCode() + ")");	attrType.debug(indent + 1);
	attrSubtype.debug(indent + 1);
	reserved.debug(indent + 1);
	attrLen.debug(indent + 1);
	dataLen.debug(indent + 1);
	fileTimeExistence.debug(indent + 1);
	fileTimes.debug(indent + 1);
    }
//begin:add your code here
    public static final int FILE_CREATION_DATE_AND_TIME    = 0;
    public static final int FILE_DELETION_DATE_AND_TIME    = 2;
    public static final int FILE_EFFECTIVE_DATE_AND_TIME   = 3;
    public static final int FILE_LAST_BACKUP_DATE_AND_TIME = 5;

    private static final int [] ft_bit = { 0, 2, 3, 5 };

    /*
      指定のビットを実際のデータのインデックスに変換します。<br>
    */
    private int bit2index(int bit) throws UDF_Exception {

	int count = 0;
	for (int i = 0, max = ft_bit.length; i < max; i++) {

	     if (bit <= ft_bit[i]) {
	        return count;
	     }

	    if (isExistance(ft_bit[i])) {
		count++;
	    }
	}

	throw new UDF_InternalException(this, "OUT OF ARRAY : File Times Extended Attribute.Existance");
    }

    /**
       readFrom直後に呼ばれるフック関数。
       File Timesに含まれる UDF_timestamp を解析して、UDF_bytes と置き換えます。

       @param f	アクセサ
    */
    public void postReadHook(UDF_RandomAccess f) throws UDF_Exception, IOException{

	//　File Timesが存在しないときは処理しない　//
	int datalen = dataLen.getIntValue();
	if(datalen < 1) {
	    super.postReadHook(f);
	    return;
	}

	UDF_ElementList el = new UDF_ElementList();

	//　File Timesの中身はUDF_timestamp 　//
	byte[] ftbuf = getFileTimes().getData();
	final int TSSIZE = 12;

	int offset = 0;

	//　UDF_timestamp の読み込み　//
	while(offset < datalen){

	    UDF_timestamp ts = (UDF_timestamp)createElement(UDF_timestamp.getUDFClassName(), null, null);
	    UDF_RandomAccessBuffer rab = fileTimes.genRandomAccessBytes(); //new UDF_RandomAccessBuffer(datalen);
	    long readsz = 0;

	    readsz = rab.write(ftbuf, offset, TSSIZE);
	    if(readsz < TSSIZE){
		throw new UDF_InternalException(this, "write error.");
	    }

	    try{
		rab.seek(0);
		readsz = ts.readFrom(rab);
	    }
	    catch(Exception e){
		throw new UDF_DataException(ts, "readFrom error.");
	    }
	    el.add(ts);
	    offset += TSSIZE;
	}

	getFileTimes().replaceChildren(el);

	super.postReadHook(f);
    }

    /**
      指定のビットに対応するタイムスタンプが存在するか
    */
    public boolean isExistance(int bit_number) {

	long ftexist = getFileTimeExistence().getLongValue();
	return (0 != (ftexist & (1 << bit_number)));
    }

    /**
      指定のビットに対応したタイムスタンプを読み込む
    */
    public UDF_timestamp getTimestamp(int bit) throws UDF_Exception {

	if (!isExistance(bit)) {
	    throw new UDF_InternalException(this, "NOT FOUND : File Times Extended Attribute");
	}

	try {
	    UDF_timestamp ts = (UDF_timestamp) createElement(UDF_timestamp.getUDFClassName(), null, null);
	    int off = bit2index(bit) * ts.getSize();
	    UDF_RandomAccessBuffer rab = getFileTimes().genRandomAccessBytes();//new UDF_RandomAccessBuffer(getFileTimes().getData());

	    rab.seek(off);
	    ts.readFrom(rab);
	    return ts;
	}
	catch(IOException e) {
	    throw new UDF_InternalException(this, "READ ERROR : File Times Extended Attribute");
	}

    }

    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	final short category = UDF_Error.C_ECMA167;
	
	
	el.addError(super.verify(5, 5));
	
	// File Time Existence の0、2、3、5 以外のビットは0
	long ftexist = fileTimeExistence.getLongValue();
	if((ftexist & 0xffffffd2) != 0){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "File Time Existence",
			 "Bits not specified in figure 4/31 are reserved for future standardisation and shall be set to ZERO.",
			 "4/14.10.5.6", String.valueOf(ftexist), ""));
	}

	// File Times の中身はtimestamp
	byte[] ftbuf = fileTimes.getData();
	final int TSSIZE = 12;
	int datalen = dataLen.getIntValue();
	int offset = 0;


	//　File Tims の領域が File Time Existenceで指定されている個数分より狭いことはありえない　//

	//　timestamp 情報が保存されている個数をカウント　//
	int count = 0;
	for (int i = 0, max = ft_bit.length; i < max; i++) {
	    if (0 != (ftexist & (1 << ft_bit[i]))) {
	        count++;
	    }
	}
	//　サイズ比較　//
	count *= TSSIZE;
	if (count < datalen || count < ftbuf.length) {
	    el.addError(
		new UDF_Error(category, UDF_Error.L_ERROR, "Data Length",
		    "This field shall contain the number of bytes used to record the dates and times specified by the File Time Existence field.",
		    "4/14.10.5.5, 4/14.10.5.6, 4/14.10.5.7"
		)
	    );
	}

	// timestampの読み込み
	while(offset < datalen){
	    
	    UDF_timestamp ts = (UDF_timestamp)createElement(UDF_timestamp.getUDFClassName(), null, null);
	    UDF_RandomAccessBuffer rab = fileTimes.genRandomAccessBytes();// new UDF_RandomAccessBytes();
	    long readsz = 0;
	    
	    
	    readsz = rab.write(ftbuf, offset, TSSIZE);
	    if(readsz < TSSIZE){
		
		el.addError(new UDF_Error
			    (category, UDF_Error.L_ERROR, "File Times",
			     "The dates and time specified in the Information Times Existence field shall be recorded contiguously " +
			     "in this field, each as a timestamp(1/7.3), in ascending order of their bit positions.",
			     "4/14.10.5.7"));
	    }
	    
	    try{
		rab.seek(0);
		readsz = ts.readFrom(rab);
		el.addError(ts.verify());
		el.setRName("File Times");
	    }
	    catch(Exception e){
		
		if(readsz < TSSIZE)
		    throw new UDF_DataException(ts, "readFrom error.");
	    }
	    
	    offset += TSSIZE;
	}
	
	el.setRName("File Times Extended Attribute");
	return el;
    }

//end:
};
