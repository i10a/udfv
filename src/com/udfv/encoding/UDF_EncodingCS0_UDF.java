package com.udfv.encoding;

import java.io.*;
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
import com.udfv.exception.*;
import com.udfv.core.*;
import com.udfv.util.*;

public class UDF_EncodingCS0_UDF extends UDF_Encoding
{
    public String encName(){
	return "CS0_UDF";
    }
    public String toString(byte b[]){
	int i;
	char[] chars = new char[b.length];

	for(i=0 ; i<b.length ; ++i){
	    /*
	      7.2.2

	      any unused bytes shall be set to #00
	     */
	    if(b[i] == 0)
		break;
	    chars[i] = (char)(UDF_Util.b2i(b[i]));
	}
	
	return new String(chars, 0, i);
    }

    public byte[] toBytes(String s, int size) throws UDF_Exception{
	byte[] b = new byte[size];
	for(int i=0 ; i<s.length() ; ++i){
	    b[i] = (byte)s.charAt(i);
	}
	return b;
    }

    public byte[] toBytes(String s) throws UDF_Exception{
	return toBytes(s, s.length());
    }
}
