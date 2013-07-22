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

public class ManyFilePlugin implements FrontendPlugin{
    Frontend front;
    UDF_API api;
    UDF_Policy policy;
    com.udfv.core.UDF_Image image;

    public ManyFilePlugin(){
    }

    public void setFrontend(Frontend frontend){
	front = frontend;
	api = front.getAPI();
	policy = front.getPolicy();
	image = front.getImage();
    }

    public int runCmd(String argv[], int mode) throws Exception{
	/*
	  flag <directory> <file> <dir size> <file size> <n of file>

	  <directory>のサイズを <dir size>に広げ、
	  <file size>の <file>を n個作る。
	  filenameは <file> + n + ".xxx"

	  となる。
	 */
	if(argv[0].equalsIgnoreCase("manyfile")){
	    String dir = argv[1];
	    String file = argv[2];
	    long dirsize = front.evalLong(argv[3]);
	    long size = front.evalLong(argv[4]);
	    int n = front.evalInt(argv[5]);
	    //String datafile = argv[6];
	    int desc_adtype = front.evalInt("${data_adtype}");
	    int dir_adtype = front.evalInt("${desc_adtype}");

	    //directory領域を確保
	    UDF_AllocPolicy ap = new UDF_AllocPolicyContinuous(0, 1, 1);
	    UDF_ADList adl = ap.alloc(image, dirsize, desc_adtype);
	    api.replaceAllocDesc(policy, dir, adl);
	    for(int i=0 ; i<n ; ++i){
		String sfx = "." + "xxx";//UDF_Util.repeat('x', i % 7 + 1);
		api.mkfile(policy, file + i + sfx, desc_adtype);
		//api.attachDataToFile(policy, file + i + sfx, datafile);
		if(i % 10 == 0)
		    System.err.println("create " + file + i + sfx + " :" + (i*100.0) /n +"%");
		if(i % 1000 == 0){//小まめにガベコレする。
		    System.err.println("execute GC ");
		    System.gc();
		}
	    }
	}
	/*
	  flag <directory> <file> <file size> <n of file>

	  <file size>の <file>を n個作る。
	  filenameは <file> + n + ".xxx"

	  となる。

	  directoryのサイズはあらかじめ広げておくこと
	 */
	else if(argv[0].equalsIgnoreCase("manyfile2")){
	    String dir = argv[1];
	    String file = argv[2];
	    //long dirsize = front.evalLong(argv[3]);
	    long size = front.evalLong(argv[3]);
	    int n = front.evalInt(argv[4]);
	    //String datafile = argv[6];
	    int desc_adtype = front.evalInt("${data_adtype}");
	    int dir_adtype = front.evalInt("${desc_adtype}");

	    //directory領域を確保
	    UDF_AllocPolicy ap = new UDF_AllocPolicyContinuous(0, 1, 1);
	    //UDF_ADList adl = ap.alloc(image, dirsize, desc_adtype);
	    //api.replaceAllocDesc(policy, dir, adl);
	    for(int i=0 ; i<n ; ++i){
		String sfx = "." + "xxx";//UDF_Util.repeat('x', i % 7 + 1);
		api.mkfile(policy, file + i + sfx, desc_adtype);
		//api.attachDataToFile(policy, file + i + sfx, datafile);
		if(i % 10 == 0)
		    System.err.println("create " + file + i + sfx + " :" + (i*100.0) /n +"%");
		if(i % 1000 == 0){//小まめにガベコレする。
		    System.err.println("execute GC ");
		    System.gc();
		}
	    }
	}

	/*
	  flag <directory> <file> <file size> <n of file>

	  <file size>の <file>を n個作る。
	  filenameは <file> + n + ".xxx"

	  となる。

	  directoryのサイズはあらかじめ広げておくこと
	 */
	else if(argv[0].equalsIgnoreCase("manylink2")){
	    String dir = argv[1];
	    String file = argv[2];
	    //long dirsize = front.evalLong(argv[3]);
	    long size = front.evalLong(argv[3]);
	    int n = front.evalInt(argv[4]);
	    //String datafile = argv[6];
	    int desc_adtype = front.evalInt("${data_adtype}");
	    int dir_adtype = front.evalInt("${desc_adtype}");

	    //directory領域を確保
	    UDF_AllocPolicy ap = new UDF_AllocPolicyContinuous(0, 1, 1);
	    //UDF_ADList adl = ap.alloc(image, dirsize, desc_adtype);
	    //api.replaceAllocDesc(policy, dir, adl);
	    for(int i=0 ; i<n ; ++i){
		String sfx = "." + "xxx";//UDF_Util.repeat('x', i % 7 + 1);
		if(i == 0)
		    api.mkfile(policy, file + i + sfx, desc_adtype);
		else
		    api.link(policy, file + "0" + sfx, file + i + sfx);
		//api.attachDataToFile(policy, file + i + sfx, datafile);
		if(i % 10 == 0)
		    System.err.println("create " + file + i + sfx + " :" + (i*100.0) /n +"%");
		if(i % 1000 == 0){//小まめにガベコレする。
		    System.err.println("execute GC ");
		    System.gc();
		}
	    }
	}
	
	return CMD_OK;
    }
}