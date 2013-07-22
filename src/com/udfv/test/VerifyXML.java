/*

*/
package com.udfv.test;

import java.io.*;
import java.util.*;
import java.util.zip.*;
import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.ecma167.*;
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
import com.udfv.access.*;
import org.apache.xml.serialize.*;
import com.udfv.exception.*;

public class VerifyXML extends UDF_TestApp{
    int revision = 0;
    String image_file;

    public static void main(String argv[]){
	new VerifyXML(argv);
    }

    void parseOpt(String argv[]){
	int i;
	for(i=0 ; i<argv.length ; ++i){
	    if(argv[i].equals("-revision")){
		revision = Integer.parseInt(argv[++i], 16);
		env.udf_revision = revision;
	    }
	    else
		image_file = argv[i];
	}
    }
    void usage( ) {
        System.out.print("usage : com.udfv.test.VerifyImg <options> <udf disc image file>\n"
                        +  "where possible options incldue:\n"
                        +  "  -revision n     verify as image that has revision n.(default auto)\n"
                        +  "  -debug_level n  change debug level n.\n"
                        +  "  -media_mode n   change media type n. [0:POW 1:RO 2:WO 3:RW 4:OW] (default=4)\n"
                        +  "  -media_type n   change media type n. [0:DVD 16:BR] (default=0)\n"
        );
    }
    VerifyXML(String argv[]){
	DOMParser parser;
	Document input_doc;
	UDF_Image image = null;
	revision = 0;

	try{
	    argv = env.parseOpt(argv);
	    parseOpt(argv);

	    String input_xml = image_file;
	    parser = new DOMParser();
	    parser.parse(input_xml);

	    input_doc = parser.getDocument();

	    image = UDF_ImageFactory.genImage(env, revision == 0 ? 0x260 : revision);

	    //env.f(に相当するもの)は readFromXMLで作られるようになりました。
	    //env.f = new UDF_RandomAccessZero(env.image_size);

	    image.setEnv(env);
	    image.readFromXML(input_doc);
	}
	catch(OutOfMemoryError e){
	    e.printStackTrace();
	}
	catch(Exception e){
	    e.printStackTrace();
	}
	try{

	    //　検証リビジョンが設定されていなかったら、読み込んだ XML からリビジョンを取り出す　//
	    if (revision < 1) {
	        revision = env.udf_revision = image.readUDFRevision();
	    }

	    //XMLから読むだけでは gp, offset, partno, mirrorがないので、再計算する
	    image.recalc(UDF_Element.RECALC_GP, env.f);
	    //XMLから読むだけでは Envが正しく設定されないので、再計算する。
	    image.recalc(UDF_Element.RECALC_ENV, env.f);
	    //XMLから読むだけでは ADLIST構造が正しく設定されないので、再計算する。
	    image.recalc(UDF_Element.RECALC_ADLIST, env.f);
	    //XMLから読むだけでは Tree構造が正しく設定されないので、再計算する。
	    image.recalc(UDF_Element.RECALC_TREE, env.f);

	    
	}
	catch(OutOfMemoryError e){
	    e.printStackTrace();
	}
	catch(Exception e){
	    e.printStackTrace();
	}

	UDF_ErrorList el = null;
	try{
	    el = image.verify();
	}
	catch(OutOfMemoryError e){
	    e.printStackTrace();
	}
	catch(UDF_NotImplException e){
	    e.printStackTrace();
	    return;
	}
	catch(Exception e){
	    e.printStackTrace();
	}

	printEnv(System.out, image, revision);

        String nl = UDF_Util.getSystemNewLine();

	System.out.print(nl
	               + "Directory Tree:" + nl
	);

	displayTree(System.out, "  ", image.env.getRootFE(0).getDirectoryList());

	if(image.env.getSRootFE(0) != null){
	    System.out.print(   nl
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
	catch(OutOfMemoryError e){
	    e.printStackTrace();
	}
	catch(Exception e){
	    e.printStackTrace();
	}
    }
}


