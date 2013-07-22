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

import java.util.*;
import com.udfv.udfapi.*;
import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.ecma167.*;

public class FragmentPlugin implements FrontendPlugin{
    Frontend front;
    UDF_API api;
    UDF_Policy policy;
    com.udfv.core.UDF_Image image;

    public FragmentPlugin(){
    }

    public void setFrontend(Frontend frontend){
	front = frontend;
	api = front.getAPI();
	policy = front.getPolicy();
	image = front.getImage();
    }

    public int runCmd(String argv[], int mode) throws Exception{
	/*
	  frag <file> <frag size> <n of frags> <n of chain>
	 */
	if(argv[0].equalsIgnoreCase("fragmentFile")){
	    String file = argv[1];
	    long size = front.evalLong(argv[2]);
	    int n = front.evalInt(argv[3]);
	    int nc = front.evalInt(argv[4]);
	    int adtype = front.evalInt("${data_adtype}");

	    int skip = (int)(UDF_Util.align(size, UDF_Env.LBS) / UDF_Env.LBS) * nc;

	    //UDF_AD[] ad0 = new UDF_AD[n];
	    UDF_ADList ad0 = new UDF_ADList();
	    for(int j=0 ; j<n ; ++j){
		UDF_ADList ad = policy.getAllocDataPolicy().alloc(image, size, adtype);
		//ad0[j] = ad[0];
		ad0.add(ad.firstElement());
	    }

	    for(int i=0 ; i<nc ; ++i){
		System.err.println("create fragment:" + (i*100.0 / nc) + "%");
		api.appendAllocDesc(policy, file, ad0);
		if(i != nc - 1){
		    api.chainAllocDesc(policy, file);
		}

		for(Iterator j=ad0.iterator() ; j.hasNext() ; ){
		    UDF_AD ad = (UDF_AD)j.next();
		    ad.setLbn(ad.getLbn() + skip);
		}
	    }
	}
	/*
	  frag <file> <frag size> <n of frags> <n of chain>
	*/
	else if(argv[0].equalsIgnoreCase("fragmentFile2")){
	    String file = argv[1];
	    long size = front.evalLong(argv[2]);
	    int n = front.evalInt(argv[3]);
	    int nc = front.evalInt(argv[4]);
	    int adtype = front.evalInt("${data_adtype}");
	    front.printArgs(argv);

	    long size0 = UDF_Util.align(size, UDF_Env.LBS) / UDF_Env.LBS; 
	    long unit = (int)(UDF_Util.align(size, UDF_Env.LBS*2));
	    int skip = (int)(unit / UDF_Env.LBS);
	    System.err.println("size="+ size);
	    System.err.println("number of chain="+ nc);
	    System.err.println("number of ad="+ n);
	    System.err.println("unit="+ unit);
	    System.err.println("skip="+ skip);

	    //でっかく取る
	    UDF_AllocPolicy ap = new UDF_AllocPolicyContinuous(0, 1, 1);
	    UDF_ADList adlist = ap.alloc(image, unit * nc * n, adtype);
	    UDF_AD ad0 = (UDF_AD)adlist.firstElement();

	    UDF_FEDesc fe1 = api.getFileEntry(file, 0);
	    UDF_FEDesc fe2 = api.getFileEntry(file, 1);

	    //参照Extent
	    UDF_Extent ref_ext = fe1.getReferenceExtent();
	    for(int i=0 ; i<nc ; ++i){
		System.err.println("create fragment:" + (i*100.0 / nc) + "%");
		UDF_MultipleAD_long_ad mad = (UDF_MultipleAD_long_ad)image.createElement("UDF_MultipleAD_long_ad", null, null);
		mad.setLen(size);
		mad.setPartRefNo(ad0.getPartRefNo());
		mad.setLbn(ad0.getLbn() + (int)(i*n*skip));
		mad.setTimes(n);
		mad.setStep(skip);

		UDF_ADDesc ad1 = fe1.getLastADDesc();
		ad1.getAllocDesc().replaceChild(mad);
		ad1.getAllocDesc().setSize(mad.getSize());
		ad1.getLenOfAllocDesc().setValue(mad.getSize());
		ad1.getDescTag().getDescCRCLen().setValue(ad1.getSize() - 16);

		UDF_ADDesc ad2 = fe2.getLastADDesc();
		ad2.getAllocDesc().replaceChild(mad.duplicateElement());
		ad2.getAllocDesc().setSize(mad.getSize());
		ad2.getLenOfAllocDesc().setValue(mad.getSize());
		ad2.getDescTag().getDescCRCLen().setValue(ad2.getSize() - 16);


		if(i != nc - 1){
		    api.chainAllocDesc(policy, file);
		}
		
		if(ref_ext != null){
		    ref_ext.addExtentElem(mad);
		}
	    }
	    ref_ext.normalize();
	}

	/*
	  frag <file> <frag size> <n of frags> <n of chain>
	*/
	else if(argv[0].equalsIgnoreCase("fragmentDirectory")){
	    String file = argv[1];
	    long size = front.evalLong(argv[2]);
	    int n = front.evalInt(argv[3]);
	    int nc = front.evalInt(argv[4]);
	    int adtype = front.evalInt("${desc_adtype}");
	    front.printArgs(argv);

	    long size0 = UDF_Util.align(size, UDF_Env.LBS) / UDF_Env.LBS; 
	    long unit = (int)(UDF_Util.align(size, UDF_Env.LBS*2));
	    int skip = (int)(unit / UDF_Env.LBS);
	    System.err.println("size="+ size);
	    System.err.println("number of chain="+ nc);
	    System.err.println("number of ad="+ n);
	    System.err.println("unit="+ unit);
	    System.err.println("skip="+ skip);

	    //でっかく取る
	    UDF_AllocPolicy ap = new UDF_AllocPolicyContinuous(0, 1, 1);
	    UDF_ADList adlist = ap.alloc(image, unit * nc * n, adtype);
	    UDF_AD ad0 = (UDF_AD)adlist.firstElement();

	    UDF_FEDesc fe1 = api.getFileEntry(file, 0);
	    UDF_FEDesc fe2 = api.getFileEntry(file, 1);

	    //参照Extent
	    //for(int j=0 ; j<2 ; ++j){
	    //Directoryは part1に置くので2回やらない。
	    for(int j=0 ; j<1 ; ++j){
		UDF_Extent ref_ext1 = fe1.getReferenceExtent();
		ref_ext1.truncExtent(0);
		UDF_Extent ref_ext2 = fe2.getReferenceExtent();
		ref_ext2.truncExtent(0);

		for(int i=0 ; i<nc ; ++i){
		    System.err.println("create fragment:" + (i*100.0 / nc) + "%");
		    UDF_MultipleAD_short_ad mad = (UDF_MultipleAD_short_ad)image.createElement("UDF_MultipleAD_short_ad", null, null);
		    mad.setLen(size);
		    mad.setPartRefNo(ad0.getPartRefNo());
		    mad.setLbn(ad0.getLbn() + (int)(i*n*skip));
		    mad.setTimes(n);
		    mad.setStep(skip);
		    
		    UDF_ADDesc ad1 = fe1.getLastADDesc();
		    ad1.getAllocDesc().replaceChild(mad);
		    ad1.getAllocDesc().setSize(mad.getSize());
		    ad1.getLenOfAllocDesc().setValue(mad.getSize());
		    ad1.getDescTag().getDescCRCLen().setValue(ad1.getSize() - 16);
		    
		    UDF_ADDesc ad2 = fe2.getLastADDesc();
		    ad2.getAllocDesc().replaceChild(mad.duplicateElement());
		    ad2.getAllocDesc().setSize(mad.getSize());
		    ad2.getLenOfAllocDesc().setValue(mad.getSize());
		    ad2.getDescTag().getDescCRCLen().setValue(ad2.getSize() - 16);
		    
		    
		    if(i != nc - 1){
			api.chainAllocDesc(policy, file);
		    }
		    
		    if(ref_ext1 != null){
			ref_ext1.addExtentElem(mad);
		    }
		    if(ref_ext2 != null){
			ref_ext2.addExtentElem(mad);
		    }
		}
		ref_ext1.normalize();
		ref_ext2.normalize();
	    }
	}
	
	return CMD_OK;
    }
}