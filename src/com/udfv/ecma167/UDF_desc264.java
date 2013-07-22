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
Space&nbsp;Bitmap&nbsp;Descriptor&nbsp;を表現するクラス。<br/>


<dl>

<dt>フィールド一覧</dt>
<dd>
<table border="1">
<tr><td>メンバ</td><td>型</td><td>サイズ</td></tr>
<tr><td><b>DescTag</b></td><td><b>UDF_tag</b></td><td><i>descTag.getSize()</i></td></tr>
<tr><td><b>NumberOfBits</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>NumberOfBytes</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>Bitmap</b></td><td><b>UDF_bitmap</b></td><td><i>getNumberOfBytes().getIntValue()</i></td></tr>
</table>
</dd>
</dl>
*/
public class UDF_desc264 extends UDF_CrcDesc 
{

    /**
	このオブジェクトのパッケージ名を除いたクラス名を取得する。

	@return クラス名
    */
    public static String getUDFClassName( ) {
        return "UDF_desc264";
    }

    /**
	コンストラクタ。

	@param elem   親エレメント。
	@param prefix ネームスペース。
	@param name   エレメント名。
    */
    public UDF_desc264(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }
    /**
	このUDFエレメントのサイズを返す。

	@return UDFエレメントのサイズ。
    */
    public int getSize(){
	return 0+descTag.getSize()+4+4+bitmap.getSize();
    }

    /**
	このUDFエレメントのサイズをlong 型で返す。

	@return UDFエレメントのサイズ。
    */
    public long getLongSize(){
	return (long)0+descTag.getSize()+4+4+bitmap.getSize();
    }
    private UDF_tag descTag;
    private UDF_uint32 numberOfBits;
    private UDF_uint32 numberOfBytes;
    private UDF_bitmap bitmap;

    /**
	descTagを取得する。

	@return 取得したdescTag を返す。
    */
    public UDF_tag getDescTag(){return descTag;}
    /**
	numberOfBitsを取得する。

	@return 取得したnumberOfBits を返す。
    */
    public UDF_uint32 getNumberOfBits(){return numberOfBits;}
    /**
	numberOfBytesを取得する。

	@return 取得したnumberOfBytes を返す。
    */
    public UDF_uint32 getNumberOfBytes(){return numberOfBytes;}
    /**
	bitmapを取得する。

	@return 取得したbitmap を返す。
    */
    public UDF_bitmap getBitmap(){return bitmap;}

    /**
	descTagを設定する。

	@param	v 設定する値。
    */
    public void setDescTag(UDF_tag v){replaceChild(v, descTag); descTag = v;}
    /**
	numberOfBitsを設定する。

	@param	v 設定する値。
    */
    public void setNumberOfBits(UDF_uint32 v){replaceChild(v, numberOfBits); numberOfBits = v;}
    /**
	numberOfBytesを設定する。

	@param	v 設定する値。
    */
    public void setNumberOfBytes(UDF_uint32 v){replaceChild(v, numberOfBytes); numberOfBytes = v;}
    /**
	bitmapを設定する。

	@param	v 設定する値。
    */
    public void setBitmap(UDF_bitmap v){replaceChild(v, bitmap); bitmap = v;}

    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long rsize = 0;
	preReadHook(f);
	descTag = (UDF_tag)createElement("UDF_tag", "", "desc-tag");
	rsize += descTag.readFrom(f);
	numberOfBits = (UDF_uint32)createElement("UDF_uint32", "", "number-of-bits");
	rsize += numberOfBits.readFrom(f);
	numberOfBytes = (UDF_uint32)createElement("UDF_uint32", "", "number-of-bytes");
	rsize += numberOfBytes.readFrom(f);
	bitmap = (UDF_bitmap)createElement("UDF_bitmap", "", "bitmap");
	bitmap.setSize(getNumberOfBytes().getIntValue());
	rsize += bitmap.readFrom(f);
	apply( );
	postReadHook(f);
	return rsize;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long wsize = 0;
	wsize += descTag.writeTo(f);
	wsize += numberOfBits.writeTo(f);
	wsize += numberOfBytes.writeTo(f);
	wsize += bitmap.writeTo(f);
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
	    else if(name.equals("number-of-bits")){
		numberOfBits = (UDF_uint32)createElement("UDF_uint32", "", "number-of-bits");
		numberOfBits.readFromXML(child);
	    }
	    else if(name.equals("number-of-bytes")){
		numberOfBytes = (UDF_uint32)createElement("UDF_uint32", "", "number-of-bytes");
		numberOfBytes.readFromXML(child);
	    }
	    else if(name.equals("bitmap")){
		bitmap = (UDF_bitmap)createElement("UDF_bitmap", "", "bitmap");
		bitmap.setSize(getNumberOfBytes().getIntValue());
		bitmap.readFromXML(child);
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
	numberOfBits = (UDF_uint32)createElement("UDF_uint32", "", "number-of-bits");
	numberOfBits.setDefaultValue();
	numberOfBytes = (UDF_uint32)createElement("UDF_uint32", "", "number-of-bytes");
	numberOfBytes.setDefaultValue();
	bitmap = (UDF_bitmap)createElement("UDF_bitmap", "", "bitmap");
	bitmap.setSize(getNumberOfBytes().getIntValue());
	bitmap.setDefaultValue();

	apply( );
	postSetDefaultValueHook();
    }


    /**
	複製する
    */
    public UDF_Element duplicateElement(){
	UDF_desc264 dup = new UDF_desc264(this, null, getName());
	dup.setDefaultValue();
	dup.duplicateHook(this);
	dup.setDescTag((UDF_tag)descTag.duplicateElement());
	dup.setNumberOfBits((UDF_uint32)numberOfBits.duplicateElement());
	dup.setNumberOfBytes((UDF_uint32)numberOfBytes.duplicateElement());
	dup.setBitmap((UDF_bitmap)bitmap.duplicateElement());

	apply();

	return dup;
    }

    /**
        子の UDF Element の Node を append しなおす。

    */
    private void apply( ) {

	removeAllChildren();
	appendChild(descTag);
	appendChild(numberOfBits);
	appendChild(numberOfBytes);
	appendChild(bitmap);
    }

    public String getInfo(int indent){
	String a = "";
	a += UDF_Util.repeat(' ', indent);
	a += descTag.getInfo(indent + 1);
	a += numberOfBits.getInfo(indent + 1);
	a += numberOfBytes.getInfo(indent + 1);
	a += bitmap.getInfo(indent + 1);
      return a;
    }
    public void debug(int indent){
	System.err.print(UDF_Util.repeat(' ', indent));

	String info = " gp=" + getGlobalPoint();
	info += " pn=" + getElemPartRefNo();
	info += " sn=" + getPartSubno();
	System.err.println(getName() + ": (" + hashCode() + ")" + info);	descTag.debug(indent + 1);
	numberOfBits.debug(indent + 1);
	numberOfBytes.debug(indent + 1);
	bitmap.debug(indent + 1);
    }
//begin:add your code here
    public int getFixedTagId() { return 264; }
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	final short category = UDF_Error.C_ECMA167;
	UDF_Error ret;
	
	
	el.addError(super.verify());  // Descriptor Tag
	
	ret = verifyId();
	if(ret.isError()){
	    
	    ret.setRefer("4/14.12.1");
	    el.addError(ret);
	}
	
	
	long numbits  = numberOfBits.getLongValue();
	long numbytes = numberOfBytes.getLongValue();
	
	// Number of Bytes はip((N_BT+7)/8) 未満であってはならない
	if(numbytes < (numbits + 7) / 8){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Number of Bytes(=N_B)",
			 "This field shall specify the number of bytes in the Bitmap field. " +
			 "The length of this field shall not be less than ip((N_BT+7)/8) bytes.",
			 "4/14.12.3", String.valueOf(numbytes), String.valueOf((numbits + 7) / 8)));
	}
	
	el.setGlobalPoint(getGlobalPoint());
	el.setRName("Space Bitmap Descriptor");
	return el;
    }
    
    /**
       bitmapを検証する。
    */
    public UDF_ErrorList verifyBitmap(UDF_bitmap bitmap, String partno, short category) throws UDF_Exception{
	UDF_ErrorList el = new UDF_ErrorList();

	UDF_bitmap this_bitmap = getBitmap();

	if((getNumberOfBits().getLongValue() + 7)/8 > getNumberOfBytes().getLongValue()){
	    el.addError(new UDF_Error
			(UDF_Error.C_ECMA167, UDF_Error.L_ERROR, "Number of Bytes",
			 "This field shall specify the number of bytes in the Bitmap field. " +
			 "The length of this field shall not be less than ip((N_BT+7/8)) bytes.",
			 "4/14.12.3"));
	}


	int pos = 0;
	int pos2 = 0;
	
	String alloc_but_nomark = "";
	String noalloc_but_mark = "";

	//最初の bitが違うところを求める
	while(true){
	    pos = this_bitmap.diff(bitmap, pos);
	    if(pos < 0)
		break;
	    if(bitmap.isSet(pos)){//実際には使っていない
		pos2 = this_bitmap.same(bitmap, pos);
		if(pos2 == -1)
		    pos2 = this_bitmap.getBitSize();
		if(pos == pos2 - 1)
		    noalloc_but_mark += "\nlogical block  " + pos + ", 1 block";
		else
		    noalloc_but_mark += "\nlogical block  " + pos + "\tthru " + pos2 + ",  " + (pos2 - pos) + " blocks";
		pos = pos2;
	    }
	    else if(!bitmap.isSet(pos)){//実際には使っている。
		pos2 = this_bitmap.same(bitmap, pos);
		if(pos2 == -1)
		    pos2 = this_bitmap.getBitSize();
		if(pos == pos2 - 1)
		    alloc_but_nomark += "\nlogical block  " + pos + ", 1 block";
		else
		    alloc_but_nomark += "\nlogical block  " + pos + "\tthru " + pos2 + ",  " + (pos2 - pos) + " blocks";

		pos = pos2;
	    }
	}
	if(noalloc_but_mark.length() > 0){
	    el.addError(new UDF_Error
			(category, UDF_Error.L_WARNING, "Bitmap(" + partno + ")",
			 "Following blocks not marked as unallcated. Orphan space may exist within a logical volume, " +
			 "but it is not recommended since some type of logical volume repair facility may reallocate it." + 
			 noalloc_but_mark, "5.2.2"));
	    
	}
	if(alloc_but_nomark.length() > 0){
	    el.addError(new UDF_Error
			(UDF_Error.C_ECMA167, UDF_Error.L_ERROR, "Bitmap(" + partno + ")",
			 "Following blocks marked as unallocated or freed." +
			 alloc_but_nomark, "3/14.12.3"));
	}
	el.setGlobalPoint(getGlobalPoint());
	el.setRName("Space Bitmap Descriptor");

	return el;
    }

//end:
};
