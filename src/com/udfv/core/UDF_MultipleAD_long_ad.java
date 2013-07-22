/*
*/
package com.udfv.core;

import java.io.*;
import java.util.*;
import org.w3c.dom.*;
import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.ecma167.*;
import com.udfv.access.*;
import com.udfv.exception.*;
import com.udfv.udf201.*;


/**
   
*/
public class UDF_MultipleAD_long_ad extends UDF_AD implements UDF_MultipleAD
{
    int my_times;
    int my_lbn;
    long my_length;
    int my_step;
    int my_partrefno;

    /**
       @param elem 親
       @param name 名前
    */
    public UDF_MultipleAD_long_ad(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name);
    }

    public void setTimes(int times){
	my_times = times;
	if(marimite)
	    setAttribute("times", times);
    }

    public void setLbn(int lbn){
	my_lbn = lbn;
	if(marimite)
	    setAttribute("lbn", lbn);
    }

    public void setStep(int step){
	my_step = step;
	if(marimite)
	    setAttribute("step", step);
    }

    public void setPartRefNo(int partrefno){
	my_partrefno = partrefno;
	if(marimite)
	    setAttribute("partrefno", partrefno);
    }

    public void setLen(long len){
	my_length = len;
	if(marimite)
	    setAttribute("len", len);
    }
    public int getStep(){
	return my_step;
    }
    
    //
    // 
    //

    /**
     */
    public long readFrom(UDF_RandomAccess f) throws UDF_Exception{
	debugMsg(3, "UDF_MultipleAD_long_ad#readFrom is not implemented... ignore");
	/*
	if(env.debug_level >= 4){
	    try{
		int a = 0/0;
	    }
	    catch(Exception e){
		e.printStackTrace();
	    }
	}
	*/  
	return my_times * my_length;
    }
    
    /**
     */
    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{
	UDF_AD ad = (UDF_AD)createElement("UDF_long_ad", null, null);
	ad.setDefaultValue();
	int start = my_lbn;
	long wsize = 0;

	for(int i=0 ; i<my_times ; ++i){
	    ad.setLbn(start);
	    ad.setLen(my_length);
	    ad.setPartRefNo(my_partrefno);
	    start += my_step;
	    wsize += ad.writeTo(f);
	}
	return wsize; 
    }


    private int str2i(String s){
	try{
	    return Integer.parseInt(s);
	}
	catch(NumberFormatException e){
	}
	return 0;
    }
    /**

    */
    public void readFromXML(Node n){
	preReadFromXMLHook(n);
	Element elm = (Element)n;

	my_times = str2i(elm.getAttribute(getPrefix(true) + "times"));
	my_step = str2i(elm.getAttribute(getPrefix(true) + "step"));
	my_lbn = str2i(elm.getAttribute(getPrefix(true) + "lbn"));
	my_length = str2i(elm.getAttribute(getPrefix(true) + "len"));
	my_partrefno = str2i(elm.getAttribute(getPrefix(true) + "partrefno"));

	postReadFromXMLHook(n);
    }

    /**
       繰り返し数を求める。
    */
    public long getLongSize(){
	return 16L * my_times;	//long_adのサイズは16
    }
    /**
       繰り返し数を求める。
    */
    public int getSize(){
	return 16 * my_times;	//long_adのサイズは16
    }

    public void debug(int indent){
	System.err.print(UDF_Util.repeat(' ', indent));
	System.err.print(getName() + ": (" + hashCode() + ") ");
	System.err.println("lbn="+my_lbn
			   + " len="+my_length
			   + " partrefno="+my_partrefno
			   + " times="+my_times
			   + " step="+my_step);
			   
    }


    //
    // 
    //

    /**
       繰り返し数を求める。
     */
    public int getTimes(){
	return my_times;
    }
    /**
       N番目のADのLBN値を求める。
     */
    public int getLoc(int index){
	return my_step * index + my_lbn;
    }

    public UDF_Element duplicateElement(){
	UDF_MultipleAD_long_ad elem = (UDF_MultipleAD_long_ad)super.duplicateElement();
	elem.setTimes(my_times);
	elem.setLbn(my_lbn);
	elem.setLen(my_length);
	elem.setStep(my_step);
	elem.setPartRefNo(my_partrefno);

	return elem;
    }

    public UDF_uint16 getExtentPartRefNo(){
	System.err.println("UDF_MultipleAD_long_ad#getExtentPartRefNo is not implemented... ignore");
	return null;
    }
    public UDF_uint32 getExtentLbn(){
	System.err.println("UDF_MultipleAD_long_ad#getExtentLbn is not implemented... ignore");
	return null;
    }
    public UDF_uint32a getExtentLen(){
	System.err.println("UDF_MultipleAD_long_ad#getExtentLen is not implemented... ignore");
	return null;
    }
    public void setFlag(int flag){
	System.err.println("UDF_MultipleAD_long_ad#setFlag is not implemented... ignore");
    }

    /**
       常に0
     */
    public int getFlag(){
	return 0;
    }
    public int getLbn(){
	return my_lbn;
    }
    public long getLen(){
	return my_length * my_times;
    }
    public int getPartRefNo(){
	return my_partrefno;
    }
}
