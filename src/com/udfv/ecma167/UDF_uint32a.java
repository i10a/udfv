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
   AD(short_ad, long_ad, ext_ad)で使用されている長さを表わすクラス。

   ADでは下位30ビットが長さを表わし、上位2ビットはADの領域の状態を
   表わすフラグと定められている。
 */
public class UDF_uint32a extends UDF_uint32
{
    public UDF_uint32a(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? "UDF_uint32a" : name);
    }
    
    protected void setNodeVal(){
	if(marimite){
	    long flag = getFlag();
	    long size = val & 0x3fffffff;
	    
	    //Text t = getDocument().createTextNode(Long.toString(flag) + ":" + Long.toString(size));
	    //getNode().appendChild(t);
	    setNodeVal(Long.toString(flag) + ":" + Long.toString(size));
	}
    }

    public int getFlag( ) {
        return (int)((val >> 30)  & 3);
    }
    public void setFlag(int flag){
	long f = (long)flag;
	val = (val & 0x3fffffff) | (f<<30);
	setNodeVal();
    }

    public void readFromXML(Node n){
	preReadFromXMLHook(n);
	NodeList nl = n.getChildNodes();
	if(nl != null){
	    String v = ((Text)(nl.item(0))).getData();
	    int pos = v.indexOf(":");
	    if(pos >= 0){
		long flag = UDF_Util.str2l(v.substring(0, pos));
		long size = UDF_Util.str2l(v.substring(pos+1, v.length()));
		val = (flag << 30) | size;
		setNodeVal();
	    }
	}
    }
    
    public void recalc(short type, UDF_RandomAccess f){
	if(type == RECALC_REF){
	    String r = getAttribute("ref");
	    int posp = r.indexOf('.');
	    if(posp < 0)
		return;

	    String ref = UDF_Util.car(r, '.');
	    String value = UDF_Util.cdr(r, '.');

	    if(ref != null && ref.length() > 0){
		UDF_ElementBase refref = env.root.findById(ref);
		if(refref != null && UDF_Extent.class.isAssignableFrom(refref.getClass())){
		    UDF_Extent ext = (UDF_Extent)refref;
		    //UDF_ExtentElem[] ee = ext.getExtent();
		    UDF_ExtentElemList eel = ext.getExtentElem();
		    UDF_ExtentElem ee = eel.firstElement();
		    if(value.equals("len")){
			debugMsg(3, "SET" + toString() + ":" + ee.getExtentFlag() + ":" + ee.getLen());
			setValue(ee.getLen());
			setFlag(ee.getExtentFlag());
		    }
		}
	    }
	}
	else{
	    super.recalc(type, f);
	}
    }
}

