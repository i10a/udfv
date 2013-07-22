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
import com.udfv.access.*;
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
import org.apache.xml.serialize.*;

public class Xml2xml extends UDF_TestApp{

    public static void main(String argv[]){
	new Xml2xml(argv);
    }
    Xml2xml(String argv[]){

	DOMParser parser = null;
	Document document = null;

	//UDF_Env env = new UDF_Env();
	argv = env.parseOpt(argv);
	    
	try{
	    String input_xml = argv[0];
	    parser = new DOMParser();
	    parser.parse(input_xml);
	}
	catch(Exception e){
	    e.printStackTrace();
	}
	try {
	    document = parser.getDocument();
	}
	catch(Exception e){
	    e.printStackTrace();
	}
	try {
	    UDF_Image u = new com.udfv.udf250.UDF_Image();
	    env.f = new UDF_RandomAccessZero(env.image_size);
	    u.setEnv(env);
	    u.readFromXML(document);
	    u.recalc(UDF_ElementBase.RECALC_GP, env.f);
	    if (argv.length < 2) {
		u.outputXML(System.out);
	    }
	    else {
		try{
		    OutputStream os = new FileOutputStream(argv[1]);
		    u.outputXML(os);
		}
		catch(IOException e){
		    System.err.println("cannot open file:" + argv[1]);
		}
	    }
	}
	catch(Exception e){
	    e.printStackTrace();
	}
    }
}
