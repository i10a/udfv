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
//
//
//
package com.udfv.ecma167;

import java.io.*;
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
import com.udfv.exception.*;
import com.udfv.access.*;
import com.udfv.core.*;
import com.udfv.util.*;

public class UDF_dstring extends UDF_bytes
{
    private int length;

    public UDF_dstring(UDF_Element elem, String prefix, String name, int sz){
	super(elem, prefix, name, sz);
	setLength(0);
    }
    public UDF_dstring(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name);
	setLength(0);
    }

    public void setLength(int len){
	length = len;
	setAttribute("length", len);
	if(getSize() > 0){
	    my_data[getSize() - 1] = (byte)len;
	}
    }

    public int getLength(){
	if(getSize() == 0)
	    return 0;

	return UDF_Util.b2i(my_data[getSize() - 1]);
    }

    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception{
	return super.readFrom(f);
    }

    public void readFromXML(Node n) throws UDF_Exception{
	super.readFromXML(n);
	int len = UDF_Util.str2i(((Element)n).getAttribute("length"));
	setLength(len);
	setNodeVal();
    }

    protected void setNodeVal(){
	if(!marimite)
	    return;

	if(my_data.length == 0)
	    return;
	
	removeAllChildren();

	int len, len2;
	len = len2 = UDF_Util.b2i(my_data[my_data.length - 1]);
	if(my_data.length < len)
	    len = my_data.length;  // はみ出た場合は最大限読み込む
	byte [] b = new byte[len];
	
	if(len == 0)
	    return;
	for(int i=0 ; i<len ; ++i)
	    b[i] = my_data[i];

	String v;
	if(getEncoding() == null)
	    v = UDF_Util.b2qstr(b);
	else
	    v = getEncoding().toString(b);
	setNodeVal(v);

	// len とlen2 が異なる場合があるので、実際に記録されている値を採用する
	setLength(len2);
    }

    /**
       エレメントを複製する。
       
       @return 複製されたエレメント。
    */
    public UDF_Element duplicateElement(){
	UDF_dstring v = (UDF_dstring)createElement("UDF_dstring", null, getName());
	v.setSize(getSize());
	v.setData(getData());
	
	if(getEncoding() != null)
	    v.setEncoding(getEncoding());
	
	v.setLength(getLength());
	v.setNodeVal();
	
	v.duplicateHook((UDF_Element)this);
	return v;
    }
    
    public void debug(String tag, int indent){
	System.out.println(tag + "[" + getSize() + "]:");
	for(int i=0 ; i<getSize() ; ++i){
	    if(i % 16 != 15)
		System.out.print(UDF_Util.b2qstr(my_data[i]) + " ");
	    else
		System.out.print(UDF_Util.b2qstr(my_data[i]) + "\n");
	}
	System.out.println("");
    }
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
//	if(getSize() < getLength())
//	    throw new UDF_DataException(this, "The number of bytes of dstrings is too big.");
	
	// 文字長以降のバイトは全て0でなければならない。
	if(!UDF_Util.isAllZero(getData(), getLength(), getSize() - 1 - getLength())){
	    
	    el.addError(new UDF_Error
			(UDF_Error.C_ECMA167, UDF_Error.L_ERROR, "The number of bytes",
			 "The characters shall be recorded starting with the first byte of the field, and any remaining byte " +
			 "positions after the characters up until byte n-1 inclusive shall be set to #00.",
			 "3/7.2.12"));
	}
	
	// 特に指定されない限り、全て0であってはならない。
	if(getLength() != 0 && UDF_Util.isAllZero(getData())){
	    
	    el.addError(new UDF_Error
			(UDF_Error.C_ECMA167, UDF_Error.L_ERROR, "",
			 "Unless otherwise specified, a dstring shall not be all #00 bytes.",
			 "3/7.2.12"));
	}
	
	// 最終バイトで示される長さが不正である。
	if(getSize() < getLength()){
	    
	    el.addError(new UDF_Error
			(UDF_Error.C_ECMA167, UDF_Error.L_ERROR, "length",
			 "The number of bytes used to record the characters shall be recorded as a Uint8(1/7.1.1) in byte n, " +
			 "where n is the length of the field.",
			 "3/7.2.12", String.valueOf(getLength()), String.valueOf(getSize())));
	}
	
	return el;
    }

    /**
       文字列をバイトデータとして設定する。

       このメソッドにより、UDF_bytesのサイズは変更されない。
       サイズに足りない部分は0x00で埋められる。

       @param s	文字列
     */
    public void setValue(String s){

	try{
	    int size = getSize();

	    byte [] b = getData();
	    if(my_enc == null) {
		b = UDF_Util.qstr2b(s);
	    }
	    else {
		b = my_enc.toBytes(s);
	    }
	    setData(b);
	    setSize(size);
	    setLength(b.length);
        }
	catch(UDF_Exception e){
	    ;
	}

    }
    /**
       バイトデータを文字列として取得する。
       内部エンコーディングが設定されている場合、"Quoted Encoded" 文字列を返す。
       そうでない場合、単にtoString() 文字列を返す。
       
       @return データ文字列。
    */
    public String getStringData(){
	String s;
	byte[]b = UDF_Util.subByte(getData(), 0, getLength());
	if(my_enc == null)
	    s = UDF_Util.b2qstr(b);
	else
	    s = my_enc.toString(b);

	return s;
    }

};
