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
  ECMA-167 1/7.1.5 32-bit unsigned numerical values を表現するクラス。

*/
public class UDF_uint32 extends UDF_Element implements UDF_Numeric
{
    protected long val;

    public UDF_uint32(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? "UDF_uint32" : name);
	val = 0;
    }

    public int getIntValue(){return (int)val;}
    public long getLongValue(){return val;}

    protected void setNodeVal(){
	setNodeVal(Long.toString(val));
    }

    public void setValue(int v){
	val = v;
	setNodeVal();
    }

    public void setValue(long v){
	val = v;
	setNodeVal();
    }
    /**
       vだけ値を増やす。
     */
    public long addValue(int v){
	val += v;
	setNodeVal();
	return getLongValue();
    }

    /**
       vだけ値を増やす。
     */
    public long addValue(long v){
	val += v;
	setNodeVal();
	return getLongValue();
    }

    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_EOFException, UDF_DataException{
	preReadHook(f);
	byte[] b = new byte[4];

	int ret = f.read(b, 0, 4);
	long v = (((long)b[0]) & 0xff) |
	    ((((long)b[1]) & 0xff) << 8) |
	    ((((long)b[2]) & 0xff) << 16) |
	    ((((long)b[3]) & 0xff) << 24);

	setValue(v);
	return ret;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_EOFException{
	byte[] b = new byte[4];

	b[0] = (byte)val;
	b[1] = (byte)(val >> 8);
	b[2] = (byte)(val >> 16);
	b[3] = (byte)(val >> 24);
	int ret = f.write(b, 0, 4);

	return ret;
    }

    public void recalc(short type, UDF_RandomAccess f){
	if(type == RECALC_REF){
	    String r = getAttribute("ref");
	    int posp = r.indexOf('.');
	    if(posp < 0)
		return;

	    String ref = r.substring(0, posp);
	    String value = r.substring(posp + 1);

	    if(ref != null && ref.length() > 0){
		UDF_ElementBase refref = env.root.findById(ref);
		if(refref != null && UDF_Extent.class.isAssignableFrom(refref.getClass())){
		    UDF_Extent ext = (UDF_Extent)refref;
		    UDF_ExtentElemList eel = ext.getExtentElem();
		    UDF_ExtentElem ee = eel.firstElement();
		    if(value.equals("lbn") || value.equals("loc")){
			debugMsg(3, "SET" + toString() + ":" +ee.getLoc());
			setValue(ee.getLoc());
		    }
		    else if(value.equals("len")){
			debugMsg(3, "SET" + toString() + ":" +ee.getLen());
			setValue(ee.getLen());
		    }
		}
	    }
	}
	else{
	    super.recalc(type, f);
	}
    }
    public void readFromXML(Node n){
	preReadFromXMLHook(n);
	NodeList nl = n.getChildNodes();
	if(nl != null){
	    //　UDF_uint32は 32 bitの数値であるが、符号なしの値が格納されているためLongでないと受け取れない
	    String v = ((Text)(nl.item(0))).getData();
	    setValue(Long.valueOf(v).longValue() & 0xffffffffL);
	}
    }

    /**
       エレメントを複製する。
       
       @return 複製されたエレメント。
    */
    public UDF_Element duplicateElement(){
	UDF_uint32 v = (UDF_uint32)createElement(getClass().getName(), null, getName());
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

    public int getSize(){return 4;}
    public long getLongSize(){return 4;}
    public void setDefaultValue(){
	setValue(0);
    }

    public int compareTo(Object obj){
	//return (int)(val - ((UDF_uint32)obj).getLongValue());
	long val2 = ((UDF_uint32)obj).getIntValue();

	if(val == val2)
	    return 0;
	else if(val < val2)
	    return -1;
	else 
	    return 1;
    }

};
