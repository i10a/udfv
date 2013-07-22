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
package com.udfv.frontend;

import com.udfv.udfapi.*;
import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.ecma167.*;

public class OutSBFromMBFEPlugin implements FrontendPlugin{
    
    Frontend front;
    UDF_API api;
    UDF_Policy policy;
    com.udfv.core.UDF_Image image;

    public OutSBFromMBFEPlugin(){
    }

    public void setFrontend(Frontend frontend){
	front = frontend;
	api = front.getAPI();
	policy = front.getPolicy();
	image = front.getImage();
    }
    
    public int runCmd(String argv[], int mode) throws Exception{
	/*
	   frag 引数無し
	*/
	UDF_Env env = image.env;
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
	
	System.out.println("OutSBFromMBFE");
	return CMD_OK;
    }
}

