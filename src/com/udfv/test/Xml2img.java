/*

*/
package com.udfv.test;

import java.io.*;
import java.util.*;
import java.util.zip.*;
import com.udfv.core.*;
import com.udfv.util.*;
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
import com.udfv.access.*;
import org.apache.xml.serialize.*;

public class Xml2img extends UDF_TestApp{

    public static void main(String argv[]){
	new Xml2img(argv);
    }
    Xml2img(String argv[]){
	DOMParser parser;
	Document input_doc;

	//UDF_Env env = new UDF_Env();
	argv = env.parseOpt(argv);
	
	String input_xml = argv[0];
	String output = argv[1];
	UDF_Image u = null;
	try{

	    //　出力するイメージファイルを一度削除しておきます　//
	    File check = new File(output);
	    if (check.exists()) {
	        check.delete();
	    }
	    check = null;

	    parser = new DOMParser();
	    parser.parse(input_xml);

	    input_doc = parser.getDocument();

	    u = new com.udfv.udf250.UDF_Image();
	    u.setEnv(env);

	    u.readFromXML(input_doc);
	}
	catch(Exception e){
	    e.printStackTrace();
	}

	System.gc();
	try{

	    System.err.println("= output =");

	    UDF_RandomAccessFile f = new UDF_RandomAccessFile(output);
	    u.writeTo(f);
	}
	catch(Exception e){
	    e.printStackTrace();
	}
    }
}
