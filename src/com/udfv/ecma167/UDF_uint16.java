/*
*/
package com.udfv.ecma167;

import java.io.*;
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
import com.udfv.exception.*;
import com.udfv.access.*;
import com.udfv.core.*;
import com.udfv.util.*;

/**
  ECMA-167 1/7.1.3 16-bit unsigned numerical values を表現するクラス。

*/
public class UDF_uint16 extends UDF_Element  implements UDF_Numeric
{
    protected int val;

    public UDF_uint16(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? "UDF_uint16" : name);
	val = 0;
    }

    public void setValue(int v){
	val = v;
	setNodeVal(Integer.toString(v));
    }

    public void setValue(long v){
	setValue((int)v);
    }
    
    public long addValue(int v){
	setValue((int)(val + v));
	return getLongValue();
    }

    public long addValue(long v){
	setValue((int)(val + (int)v));
	return getLongValue();
    }

    public int getIntValue(){return (int)val;}
    public long getLongValue(){return val;}

    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_EOFException, UDF_DataException{
	preReadHook(f);
	byte[] b = new byte[2];
	
	int ret = f.read(b, 0, 2);
	int v = (((int)b[0]) & 0xff) |((((int)b[1]) & 0xff) << 8);

	setValue(v);

	return ret;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_EOFException, UDF_DataException{
	byte[] b = new byte[2];

	b[0] = (byte)val;
	b[1] = (byte)(val >> 8);
	int ret = f.write(b, 0, 2);

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
		    else if(value.equals("partno")){
			debugMsg(3, "SET" + toString() + ":" +ee.getPartRefNo());
			setValue(ee.getPartRefNo());
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
	    String v = ((Text)(nl.item(0))).getData();
	    setValue(Integer.valueOf(v).intValue());
	}
    }
    //    public void writeToXML(Node n){}


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

    public int getSize(){return 2;}
    public long getLongSize(){return 2;}
    public void setDefaultValue(){
	setValue(0);
    }

    /**
       エレメントを複製する。
       
       @return 複製されたエレメント。
    */
    public UDF_Element duplicateElement(){
	UDF_uint16 v = (UDF_uint16)createElement("UDF_uint16", null, getName());
	v.duplicateHook(this);
	v.setValue(getLongValue());
	return v;
    }

    public int compareTo(Object obj){
	int val2 = ((UDF_uint16)obj).getIntValue();

	if(val == val2)
	    return 0;
	else if(val < val2)
	    return -1;
	else
	    return 1;
    }

};
