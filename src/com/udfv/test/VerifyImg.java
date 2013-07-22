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

public class VerifyImg extends UDF_TestApp{
    int revision;
    String image_file;

    public static void main(String argv[]){
	new VerifyImg(argv);
    }

    void parseOpt(String argv[]){

        String nl = UDF_Util.getSystemNewLine();

	boolean flag_file = false;
	int i;
	for(i=0 ; i<argv.length ; ++i){
	    if(argv[i].equals("-revision")){
		if ((i + 1) >= argv.length) {
		    System.err.print("-revision <n>: this option needs revision" + nl);
		    System.exit(1);
		}
		revision = Integer.parseInt(argv[++i], 16);
	    }
	    else
	    if(argv[i].equals("-version")) {
                printVersion(System.out);
                System.exit(0);
	    }
	    else {
		if (flag_file) {
		    System.err.println("too many input file.");
		    System.exit(1);
		}
		image_file = argv[i];
		flag_file = true;
	    }
	}
    }

    void usage( ) {
        String nl = UDF_Util.getSystemNewLine();
        System.out.print("usage : " + getClass().getName() + " <options> <udf disc image file>" + nl
                        +  "where possible options incldue:" + nl
                        +  "  -revision <n>    verify as image that has revision n.(default auto)" + nl
                        +  "  -debug_level <n> change debug level n." + nl
                        +  "  -media_mode <n>  change media type n. [0:POW 1:RO 2:WO 3:RW 4:OW](default=4)" + nl
                        +  "  -media_type <n>  change media type n. [0:DVD 16:BR] (default=0)" + nl
                        +  "  -version         print version" + nl
        );
    }

    VerifyImg(String argv[]){
	UDF_Image image = null;
	image_file = null;
	revision = 0;


	try{
	    argv = env.parseOpt(argv);
	    parseOpt(argv);

	    if (image_file == null) {
	        usage();
	        System.exit(0);
	    }

	    File file = new File(image_file);
	    if (!file.exists()) {
	        System.err.println("File Not Found : " + image_file);
	        return;
	    }

	    UDF_RandomAccess f = new UDF_RandomAccessFile(image_file, "r");
	    env.image_file = image_file;
	    env.f = f;

	    Document doc = UDF_XMLUtil.genDocument();

	    switch(revision){
	    default:
		//UDF_ImageFactory factory = new com.udfv.UDF_ImageFactory();
		//factory.setEnv(env);
		env.udf_revision = -1;
		image = UDF_ImageFactory.genImage(env, f);
		image.setUDFDocument(doc);
		break;

	    case 0x102:
	    case 0x150:
	    case 0x200:
	    case 0x201:
	    case 0x250:
		env.udf_revision = revision;
		image = UDF_ImageFactory.genImage(env, revision);
		image.setUDFDocument(doc);
		break;
	    }
	    image.readFrom(f);
	}
	catch(UDF_NotImplException e){
	    e.printStackTrace();
	    return;
	}
	catch(Exception e){
	    e.printStackTrace();
	}
	
	System.gc();
	UDF_ErrorList el = null;
	try{
	    el = image.verify();

	    if(1 < argv.length){
		
		if(argv[1].equals("ECMA167"))
		    el = el.getErrorInCategory(UDF_Error.C_ECMA167);
		else if(argv[1].equals("UDF102"))
		    el = el.getErrorInCategory(UDF_Error.C_UDF102);
		else if(argv[1].equals("UDF150"))
		    el = el.getErrorInCategory(UDF_Error.C_UDF150);
		else if(argv[1].equals("UDF200"))
		    el = el.getErrorInCategory(UDF_Error.C_UDF200);
		else if(argv[1].equals("UDF201"))
		    el = el.getErrorInCategory(UDF_Error.C_UDF201);
		else if(argv[1].equals("UDF250"))
		    el = el.getErrorInCategory(UDF_Error.C_UDF250);
		else if(argv[1].equals("UDF"))
		    el = el.getErrorInCategory(UDF_Error.C_UDFALL);
		else if(argv[1].equals("UDF20X"))
		    el = el.getErrorInCategory(UDF_Error.C_UDF20X);
	    }
	}
	catch(UDF_NotImplException e){
	    e.printStackTrace();
	    return;
	}
	catch(Exception e){
	    e.printStackTrace();
	}
	System.gc();

	printEnv(System.out, image, revision);

        String nl = UDF_Util.getSystemNewLine();

	System.out.print( nl
	               + "Directory Tree:" + nl
	);

	displayTree(System.out, "  ", image.env.getRootFE(0).getDirectoryList());

	if(image.env.getSRootFE(0) != null){
	    System.out.print(    nl
		       + "System Stream Tree:" + nl
	    );
	    displayTree(System.out, "  ", image.env.getSRootFE(0).getDirectoryList());
	}

	System.out.print(       nl
	               + "Result:" + nl
	);

	try{
	    el.output();
	}
	catch(Exception e){
	    e.printStackTrace();
	}
    }
}
