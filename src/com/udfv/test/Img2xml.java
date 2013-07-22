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
import com.udfv.exception.*;

/**
   UDF Imageを XMLに変換するサンプル。
 */
public class Img2xml extends UDF_TestApp {
    int revision;
    int target_sec = -1;
    String image_file;
    String output_xml_file;

    void parseOpt(String argv[]){
	int i;
	for(i=0 ; i<argv.length ; ++i){
	    if(argv[i].equals("-revision")){
		revision = Integer.parseInt(argv[++i], 16);
	    }
	    else if(argv[i].equals("-o")){
		output_xml_file = argv[++i];
	    }
	    else if(argv[i].equals("-sec")){
		target_sec = Integer.parseInt(argv[++i], 10);
	    }
	    else
		image_file = argv[i];
	}
    }

    Img2xml(String argv[]){
	argv = env.parseOpt(argv);
	parseOpt(argv);

	UDF_Image image = null;
	UDF_RandomAccess f = null;
	try{
	    env.image_file = image_file;
/*	    if (env.image_file == null) {
		env.image_file = "ATAPI";
		env.f = f = new UDF_RandomAccessATAPI("192.168.253.29", "E");
	    }
	    else {
*/
	    env.f = f = new UDF_RandomAccessFile(env.image_file, "r");
//	    }

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
		if(image.env.getRootFE(0) != null){
		    displayTree(System.err, "  ", image.env.getRootFE(0).getDirectoryList());
		}
		else if(image.env.getRootFE(1) != null){
		    displayTree(System.err, "  ", image.env.getRootFE(1).getDirectoryList());
		}
		if(image.env.getSRootFE(0) != null){
		    System.err.print("\n"
				     + "System Stream Tree:\n"
				     );

		    displayTree(System.err, "  ", image.env.getSRootFE(0).getDirectoryList());
		}
		else if(image.env.getSRootFE(1) != null){
		    System.err.print("\n"
				     + "System Stream Tree:\n"
				     );

		    displayTree(System.err, "  ", image.env.getSRootFE(1).getDirectoryList());
		}
	    }
	}
	catch(UDF_NotImplException e){
	    e.printStackTrace();
	    return;
	}
	catch(Exception e){
	    e.printStackTrace();
	}

	try{
	    if(output_xml_file == null)
		image.outputXML(System.out);
	    else{
		OutputStream os = new FileOutputStream(output_xml_file);
		image.outputXML(os);
	    }
	}
	catch(Exception e){
	    e.printStackTrace();
	}
	
    }

    public static void main(String argv[]){
	new Img2xml(argv);
    }
}
