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
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
import com.udfv.access.*;
import com.udfv.exception.*;
import com.udfv.core.*;
import com.udfv.util.*;


/**
  ECMA-167 1/7.1.7 64-bit unsigned numerical values を表現するクラス。

*/
public class UDF_uint64 extends UDF_Element implements UDF_Numeric
{
    private long val;

    public UDF_uint64(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? "UDF_uint64" : name);
	val = 0;
    }

    public long getLongValue(){return val;}

    public void setValue(long v){
	val = v;
	setNodeVal(Long.toString(v));
    }

    public long addValue(long v){
	setValue(val + v);
	return val;
    }

    public long addValue(int v){
	setValue(val + v);
	return val;
    }

    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception{
	preReadHook(f);
	byte[] b = new byte[8];

	int ret = f.read(b, 0, 8);
	long v = (((long)b[0]) & 0xff) |
	    ((((long)b[1]) & 0xff) << 8) |
	    ((((long)b[2]) & 0xff) << 16) |
	    ((((long)b[3]) & 0xff) << 24) |
	    ((((long)b[4]) & 0xff) << 32) |
	    ((((long)b[5]) & 0xff) << 40) |
	    ((((long)b[6]) & 0xff) << 48) |
	    ((((long)b[7]) & 0xff) << 56);

	setValue(v);
	return ret;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{
	byte[] b = new byte[8];

	b[0] = (byte)val;
	b[1] = (byte)(val >> 8);
	b[2] = (byte)(val >> 16);
	b[3] = (byte)(val >> 24);

	b[4] = (byte)(val >> 32);
	b[5] = (byte)(val >> 40);
	b[6] = (byte)(val >> 48);
	b[7] = (byte)(val >> 56);

	int ret = f.write(b, 0, 8);

	return ret;
    }

    public void readFromXML(Node n){
	preReadFromXMLHook(n);
	NodeList nl = n.getChildNodes();
	if(nl != null){
	    String v = ((Text)(nl.item(0))).getData();
	    setValue(Long.valueOf(v).longValue());
	}
    }

    /**
       エレメントを複製する。
       
       @return 複製されたエレメント。
    */
    public UDF_Element duplicateElement(){
	UDF_uint64 v = (UDF_uint64)createElement("UDF_uint64", null, getName());

	v.duplicateHook(this);
	v.setValue(getLongValue());
	return v;
    }

    public String getInfo(int indent){
	String a = "";
	a += UDF_Util.repeat(' ', indent);
	a += getName() + ":";
	a += val;
	a += "\n";
	return a;
    }    

    public void debug(int indent){
	System.err.println(getInfo(indent));
    }

    public int getSize(){return 8;}
    public long getLongSize(){return 8;}
    public void setDefaultValue(){
	setValue(0);
    }

    public int compareTo(Object obj){
	//return val - ((UDF_uint32)obj).getLongValue();
	long val2 = ((UDF_uint64)obj).getLongValue();

	if(val == val2)
	    return 0;
	else if(val < val2)
	    return -1;
	else
	    return 1;
    }
};
