package com.udfv.encoding;

import java.io.*;
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
import com.udfv.exception.*;
import com.udfv.core.*;
import com.udfv.util.*;

public class UDF_EncodingOSTACompressedUnicode extends UDF_Encoding
{
    public String encName(){return "OSTA Compressed Unicode";}

    public String toString(byte []b){
	if(b.length == 0)
	    return "";

	int compid = UDF_Util.b2i(b[0]);
	
	if(compid == 8 || compid == 254){
	    char[] chars = new char[b.length];
	    String body = UDF_Util.b2qstr(b, 1, false);

	    return "" + compid + ":" + body;
	}
	else if(compid == 16 || compid == 255){
	    char[] chars = new char[b.length / 2];
	    int count = 0;
	    for(int i=1 ; i < b.length - 1 ; i+=2, ++count){
		chars[count] = (char)((UDF_Util.b2i(b[i])<<8) | UDF_Util.b2i(b[i+1]));
	    }
	    String body = new String(chars, 0, count);
	    return "" + compid + ":" + body;
	}
	return null;
    }

    //サイズを指定しないときは適切な値で newされる
    public byte[] toBytes(String s) throws UDF_Exception{
	if(s.length() == 0)
	    return new byte[0];
	int colon_pos = s.indexOf(':');
	if(colon_pos < 1)
	    throw new UDF_XMLException(null, "Bad string");

	int compid = Integer.parseInt(s.substring(0, colon_pos));
	String rest = s.substring(colon_pos + 1);

	if(compid == 8 || compid == 254){
	    int charAt = 0;

	    byte [] bb = UDF_Util.qstr2b(rest);
	    byte [] b = new byte[bb.length + 1];
	    b[0] = (byte)compid;
	    for(int i = 0 ; i < bb.length ; ++i){
		b[i+1] = bb[i];
	    }
	    return b;
	}
	else
	if(compid == 16 || compid == 255){

	    byte [] b = new byte[rest.length() * 2 + 1];
	    b[0] = (byte)compid;
	    for(int i=0 ; i<rest.length() ; i++){
		char c = rest.charAt(i);
		b[i*2 + 1] = (byte)(c >> 8);
		b[i*2 + 2] = (byte)(c & 0xff);
	    }
	    return b;
	}

	throw new UDF_XMLException(null, "Bad string");
    }

   //サイズを指定すると足りない部分は 0x00で埋められる。
    public byte[] toBytes(String s, int size) throws UDF_Exception{
	if(s.length() == 0)
	    return new byte[size];

	int colon_pos = s.indexOf(':');
	if(colon_pos < 1)
	    throw new UDF_XMLException(null, "Bad string");

	int compid = Integer.parseInt(s.substring(0, colon_pos));
	String rest = s.substring(colon_pos + 1);

	if(compid == 8 || compid == 254){
	    int charAt = 0;

	    byte [] bb = UDF_Util.qstr2b(rest);
	    byte [] b = new byte[size];//bb.length + 1];
	    b[0] = (byte)compid;
	    for(int i = 0 ; i < bb.length ; ++i){
		b[i+1] = bb[i];
	    }
	    return b;
	}
	else
	if(compid == 16 || compid == 255){

	    byte [] b = new byte[size];//rest.length() * 2 + 1];
	    b[0] = (byte)compid;
	    for(int i=0 ; i<rest.length() ; i++){
		char c = rest.charAt(i);
		b[i*2 + 1] = (byte)(c >> 8);
		b[i*2 + 2] = (byte)(c & 0xff);
	    }
	    return b;
	}

	throw new UDF_XMLException(null, "Bad string");
    }
}
