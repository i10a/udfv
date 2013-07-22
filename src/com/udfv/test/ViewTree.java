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

/**
   UDF Imageのディレクトリ構造を表示するサンプル
 */
public class ViewTree extends UDF_TestApp {
    int revision;
    int target_sec = -1;
    String image_file;
    //String output_xml_file;

    void parseOpt(String argv[]){
	int i;
	for(i=0 ; i<argv.length ; ++i){
	    if(argv[i].equals("-revision")){
		revision = Integer.parseInt(argv[++i], 16);
	    }
	    /*
	    else if(argv[i].equals("-o")){
		output_xml_file = argv[++i];
	    }
	    */
	    else if(argv[i].equals("-sec")){
		target_sec = Integer.parseInt(argv[++i], 10);
	    }
	    else
		image_file = argv[i];
	}
    }

    ViewTree(String argv[]){
	argv = env.parseOpt(argv);
	parseOpt(argv);

	UDF_Image image = null;
	UDF_RandomAccess f = null;
	try{
	    env.image_file = image_file;
	    env.f = f = new UDF_RandomAccessFile(env.image_file, "r");

	    switch(revision){
	    default:
		image = UDF_ImageFactory.genImage(env, f);
		image.setUDFDocument(UDF_Util.genDocument());
		break;
	    case 0x102:
	    case 0x150:
	    case 0x200:
	    case 0x201:
	    case 0x250:
	    case 0x260:
		image = UDF_ImageFactory.genImage(env, revision);
		image.setUDFDocument(UDF_Util.genDocument());
		env.f = f;
		break;
	    }
	    image.setEnv(env);
	}
	catch(Exception e){
	    e.printStackTrace();
	}

	try{
	    if(target_sec >= 0){
		env.f = f;
		f.seekSec(target_sec);
		image.readFromDesc(f, 1);
		env.sortxml = false;
	    }
	    else{
		image.readFrom(f);
		System.err.print("\n"
				 + "Directory Tree:\n"
				 );
		displayTree(System.err, " /", image.env.getRootFE(0).getDirectoryList(), -1);
		if(image.env.getSRootFE(0) != null){
		    System.err.print("\n"
				     + "System Stream Tree:\n"
				     );

		    displayTree(System.err, " @", image.env.getSRootFE(0).getDirectoryList(), -1);
		}
	    }
	}
	catch(Exception e){
	    e.printStackTrace();
	}
    }

    public static void main(String argv[]){
	new ViewTree(argv);
    }
}
