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
  ECMA-167 1/7.1.1 8-bit unsigned numerical values を表現するクラス。

*/
public class UDF_uint8 extends UDF_Element implements UDF_Numeric
{
    private int val;

    public UDF_uint8(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name);
	val = 0;
    }

    public int getIntValue(){return val;}
    public long getLongValue(){return (long)val;}

    public void setValue(int v){
	val = v;
	setNodeVal(Integer.toString(v));
    }

    public long addValue(int v){
	setValue(val + v);
	return getLongValue();
    }
    public long addValue(long v){
	setValue(val + (int)v);
	return getLongValue();
    }

    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_EOFException{
	preReadHook(f);
	byte[] b = new byte[1];

	int ret = f.read(b, 0, 1);
	int v = ((int)b[0]) & 0xff;

	setValue(v);
	return ret;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_EOFException{
	byte[] b = new byte[1];

	b[0] = (byte)val;
	int ret = f.write(b, 0, 1);

	return ret;
    }

    public void readFromXML(Node n){
	preReadFromXMLHook(n);
	NodeList nl = n.getChildNodes();
	if(nl != null){
	    String v = ((Text)(nl.item(0))).getData();
	    setValue(Integer.valueOf(v).intValue());
	}
    }

    public String getInfo(int indent){
	String a = "";
	a += UDF_Util.repeat(' ', indent);
	a += getName() + ":";
	a += val;
	return a + "\n";
    }    

    public void debug(int indent){
	System.err.println(getInfo(indent));
    }

    public UDF_Element duplicateElement(){

	UDF_uint8 v = (UDF_uint8)createElement("UDF_uint8", null, getName());
	v.duplicateHook(this);
	v.setValue(getIntValue());
	return v;
    }

    public int getSize(){return 1;}
    public long getLongSize(){return 1;}

    public void setDefaultValue(){
	setValue(0);
    }

    public int compareTo(Object obj){
	int val2 = ((UDF_uint8)obj).getIntValue();

	if(val == val2)
	    return 0;
	else if(val < val2)
	    return -1;
	else 
	    return 1;
    }
};
