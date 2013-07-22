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
