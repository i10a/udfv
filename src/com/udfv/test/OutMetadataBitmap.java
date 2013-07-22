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
   XMLがMetadata Bitmap FEを持っているとき、desc264がimmediateとして
   FE内部に記録されていれば、それを外部へ出す
*/
package com.udfv.test;

import java.io.*;
import java.util.*;
import java.util.zip.*;
import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.access.*;
import com.udfv.udfapi.*;
import com.udfv.ecma167.*;
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
import org.apache.xml.serialize.*;

public class OutMetadataBitmap extends UDF_TestApp{

    public static void main(String argv[]){
	new OutMetadataBitmap(argv);
    }
    
    OutMetadataBitmap(String argv[]){

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
	    UDF_Image image = new com.udfv.udf250.UDF_Image();
	    env.f = new UDF_RandomAccessZero(env.image_size);
	    image.setEnv(env);
	    image.readFromXML(document);
	    
	    UDF_ElementList el = env.getPartDescList(UDF_Env.VDS_AUTO);
	    el = env.getPartMapList();

	    UDF_API api = new UDF_API(image);
	    UDF_Policy policy = new UDF_PolicyImpl();
	    
	    
	    for(int i=0 ; i<el.size() ; ++i){
		UDF_PartMap pm = (UDF_PartMap)el.elementAt(i);
		if(env.isMetadataPartMap(i)){
		    
		    com.udfv.udf250.UDF_MetadataPartMap mpm = (com.udfv.udf250.UDF_MetadataPartMap) pm;
		    if(mpm.getMetadataBitmapFileLoc().getIntValue() == -1)
			continue;
		    
		    UDF_FEDesc mbfe = mpm.getMetadataBitmapFile();
		    UDF_desc264 desc264 = mpm.getMetadataBitmap();
		    
		    UDF_ADList ad_sbd =
			policy.getAllocDescPolicy().alloc(image, UDF_Util.align(desc264.getSize(), image.env.LBS), 0);
		    
		    api.changeADType(policy, mbfe, UDF_icbtag.SHORT_AD, ad_sbd, false);
		    break;
		}
	    }
	    
	    if (argv.length < 2) {
		image.outputXML(System.out);
	    }
	    else {
		try{
		    OutputStream os = new FileOutputStream(argv[1]);
		    image.outputXML(os);
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
