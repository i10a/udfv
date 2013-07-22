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

/**
  XMLを読みとり CRCを再計算する
*/
public class RecalcXML extends UDF_TestApp {
    boolean recalc_ref = false;
    boolean recalc_sb = true;
    boolean recalc_lvid = true;
    String xml_file;

    void parseOpt(String argv[]){
	int i;
	for(i=0 ; i<argv.length ; ++i){
	    if(argv[i].equals("-ref")){
		recalc_ref = true;
	    }
	    else if(argv[i].equals("-noref")){
		recalc_ref = false;
	    }
	    else if(argv[i].equals("-sb")){
		recalc_sb = true;
	    }
	    else if(argv[i].equals("-nosb")){
		recalc_sb = false;
	    }
	    else if(argv[i].equals("-lvid")){
		recalc_lvid = true;
	    }
	    else if(argv[i].equals("-nolvid")){
		recalc_lvid = false;
	    }
	    else
		xml_file = argv[i];
	}
    }
    public static void main(String argv[]){
	new RecalcXML(argv);
    }
    RecalcXML(String argv[]){
	DOMParser parser;
	Document document;

	try{
	    argv = env.parseOpt(argv);
	    parseOpt(argv);

	    String input_xml = xml_file;
	    parser = new DOMParser();
	    parser.parse(input_xml);

	    document = parser.getDocument();

	    //　最上位のUDF として読み取ります　//
	    UDF_Image image = new com.udfv.udf250.UDF_Image();
	    env.f = new UDF_RandomAccessZero(env.image_size);
	    
	    image.setEnv(env);
	    image.readFromXML(document);

	    image.recalc(UDF_ElementBase.RECALC_ENV, null);

//	    image.recalc(UDF_ElementBase.RECALC_CRC, null);

	    if(recalc_ref)
		image.recalc(UDF_ElementBase.RECALC_REF, null);
	    image.recalc(UDF_ElementBase.RECALC_GP, env.f);
	    image.recalc(UDF_ElementBase.RECALC_PAD, null);
	    image.recalc(UDF_ElementBase.RECALC_CRC, null);
	    image.recalc(UDF_ElementBase.RECALC_ADLIST, null);
	    
	    // Metadata Main/Mirror FEをrecalc
	    if(env.getMetadataPartMap() != null){
		
		int metapartnum = -1;
		for(int i = 0 ; i<env.part.length ; ++i){
		    if(com.udfv.udf250.UDF_MetadataPartMap.class.isAssignableFrom(env.part[i].getClass())){
			metapartnum = i;
			break;
		    }
		}
		env.getMetadataPartMap().recalcPM(metapartnum);
	    }
	    
	    if(recalc_sb){
		image.recalc(UDF_ElementBase.RECALC_SB, null);
		image.recalc(UDF_ElementBase.RECALC_PAD, null);
	    }
	    image.recalc(UDF_ElementBase.RECALC_GP, env.f);
	    image.recalc(UDF_ElementBase.RECALC_TAGLOC, null);
	    
	    // Prevailing Descriptor の1つ目を用いる。2つあった場合は考慮してない
	    UDF_ElementList desc5l = env.getPartDescList(env.VDS_AUTO);
	    com.udfv.ecma167.UDF_desc5 desc5 = (com.udfv.ecma167.UDF_desc5)desc5l.elementAt(0);
	    int partno = desc5.getPartNumber().getIntValue();
	    
	    if (recalc_lvid && image.env.getPartDesc(image.env.VDS_AUTO, partno).getAccessType().getIntValue() != 1) {
		image.recalc(UDF_ElementBase.RECALC_LVIS, null);
	    }
	    image.recalc(UDF_ElementBase.RECALC_CRC, null);

	    image.outputXML(System.out);
	}
	catch(Exception e){
	    e.printStackTrace();
	}
    }
}
