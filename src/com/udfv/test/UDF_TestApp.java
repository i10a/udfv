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
import com.udfv.ecma167.*;
import org.w3c.dom.*;
import com.udfv.access.*;
import com.udfv.exception.*;
import java.lang.reflect.Field;

/**
   UDF_TestAppは テストアプリケーションの基底となるクラスである。

   envの初期化を行う。
 */
public abstract class UDF_TestApp{
    public UDF_Env env;

    public static final int F_UID = 1;
    public static final int F_GID = 2;
    public static final int F_FLAGS = 4;
    public static final int F_ALL = 0x7;
    public static final int F_FULLPATH = 0x100;

    protected UDF_TestApp(){
	env = new UDF_Env();
    }

    public void printVersion(PrintStream out) {

        String nl = UDF_Util.getSystemNewLine();
        String version = env.system_version;

        version = version.substring(6, version.length() - 2); /*　"$Rev: ", " $"を取り除く　*/

        out.print("UDFV Verifier Version " + UDF_Version.getVerifierVersion() + nl
               + " (UDFV Library : build " + version + "-" + UDF_Version.getBuildingDate() + ")" + nl
               +  "Copyright (C) 2005 Heart Solutions, Inc. All Rights Reserved." + nl
        );
    }

    private static String moreInfo(UDF_FEDesc fe, UDF_desc257 fid, int mode){
	long unique_id = 0;
	if(fid != null)
	    unique_id = fid.getUniqueId();
	else
	    unique_id = fe.getUniqueId().getLongValue();

	String more = UDF_Util.i2d(fe.getICBFileType(), 3, ' ') + " " +
	    UDF_Util.i2d(unique_id, 5, ' ') + " " +
	    UDF_Util.i2d(fe.getADSize(), 10, ' ');
	
	if((mode & F_UID) != 0)
	    more += UDF_Util.i2d(fe.getUid().getLongValue(), 11, ' ');
	if((mode & F_GID) != 0)
	    more += UDF_Util.i2d(fe.getGid().getLongValue(), 11, ' ');
	if((mode & F_FLAGS) != 0)
	    more += UDF_Util.i2d(fe.getICBFlags(), 10, ' ');

	return more;
    }

    public static void displayTree(PrintStream out, UDF_FEDesc fe, int mode){
	String more = "TYP " + " UNIQ " + "      SIZE ";

	if((mode & F_UID) != 0)
	    more += "       UID ";
	if((mode & F_GID) != 0)
	    more += "       GID ";
	if((mode & F_FLAGS) != 0)
	    more += "     FLAG ";

	out.println(more);

	out.println(moreInfo(fe, null, mode) + " /");
	displayTree(out, "  ", fe.getDirectoryList(), mode);
    }

    public static void displayTree(PrintStream out, com.udfv.core.UDF_ElementList directory){
        displayTree(out, "", directory, 0);
    }

    public static void displayTree(PrintStream out, com.udfv.core.UDF_ElementList directory, int mode) {
        displayTree(out, "", directory, mode);
    }
    public static void displayTree(PrintStream out, String padding, com.udfv.core.UDF_ElementList directory) {
        displayTree(out, "", directory, 0);
    }

    public static void displayTree(PrintStream out, String padding, com.udfv.core.UDF_ElementList directory, int mode) {

        if (directory == null) {
            return;
        }

        for (int i = 1, max = directory.size(); i < max; i++) {

            UDF_ElementBase elem = directory.elementAt(i);
            String name = elem.toString();
            if (!name.equals("UDF_desc257")) {
                continue;
            }

            //　このFile Identifier Descriptorから参照されるFile Entry(Extended File Entry) を取得します　//
            com.udfv.ecma167.UDF_desc257 fid = (com.udfv.ecma167.UDF_desc257) elem;
            com.udfv.ecma167.UDF_FEDesc fe = fid.getReferenceTo();
            if (fe == null) {
                continue;
            }

            //　File Identifier からファイル名を取り出して表示します　//
            name = fid.getFileId().getStringData();

	    String more = moreInfo(fe, fid, mode);

	    if((mode & F_FULLPATH) == 0){
		out.println(more + " " + padding + name);
		displayTree(out, padding + "    ", fe.getDirectoryList(), mode);
	    }
	    else{
		out.println(more +  padding + name);
		displayTree(out, padding + name + "/", fe.getDirectoryList(), mode);
	    }
        }
    }

    public static void printEnv(PrintStream out, UDF_Image image, int revision) {
	String [] mode = { "Psudo overwrite"
	                 , "Read only"
	                 , "Write once"
	                 , "Rewritable"
	                 , "Overwritable"
	};
        String nl = UDF_Util.getSystemNewLine();

        UDF_Env env = image.env;
        String image_file = env.image_file;

	out.print(       "Input:" + nl
		       + "  FILE         " + image_file + nl
		       + "  MEDIA TYPE   " + (image.env.isMediaTypeBR() ? "BD": "DVD") + nl
		       + "  MEDIA MODE   " + mode[image.env.media_mode] + nl
	);
	
	
	// MVDS、RVDSを認識できなければ、'missing'と表示する(2005/09/15:追加 by seta)
	String mvdsloc = "missing";
	String rvdsloc = "missing";
	try{
	    int ml = image.env.getMVDSLoc2();
	    mvdsloc = String.valueOf(ml);
	}
	catch(UDF_Exception e){
	    ;
	}
	try{
	    int rl = image.env.getRVDSLoc2();
	    rvdsloc = String.valueOf(rl);
	}
	catch(UDF_Exception e){
	    ;
	}
	
	
	try{
	    UDF_desc1 desc1 = env.getPrimaryVolDesc(UDF_Env.VDS_AUTO, 1);
	    String volinfo = ""
		+ "  VOLUME ID           " + desc1.getVolId().getStringData() + nl 
		+ "  VOLUME SET ID       " + desc1.getVolSetId().getStringData() + nl;

	    // Prevailing Partition Descriptor の1つ目を用いる。2つあった場合は考慮してない
	    UDF_ElementList desc5l = env.getPartDescList(env.VDS_AUTO);
	    com.udfv.ecma167.UDF_desc5 desc5 = (com.udfv.ecma167.UDF_desc5)desc5l.elementAt(0);
	    int partno = desc5.getPartNumber().getIntValue();
	    String out1 = 
		nl
		+ "UDF:" + nl
		/*+ ((revision > 0) ? ("  REVISION(verify)    " + convRevision(revision) + nl): "")*/
		+ "  REVISION(recorded)  " + convRevision(image.env.recorded_udf_revision) + nl
		+ "  AVDP" + nl
		+ "    LSN (256)         " + (image.env.anchorVolDescPointer[0] != null ? "exist": "missing") + nl
		+ "    LSN (n - 1)       " + (image.env.anchorVolDescPointer[2] != null ? "exist": "missing") + nl
		+ "    LSN (n - 257)     " + (image.env.anchorVolDescPointer[1] != null ? "exist": "missing") + nl
		+ "  MVDS LSN            " + mvdsloc + nl
		+ "  RVDS LSN            " + rvdsloc + nl
		+ volinfo
		+ "  LVIS LSN            " + image.env.getISLoc() + nl
		+ "  Part. Starting loc. " + image.env.getPartStartingLoc(partno) + nl
		+ "  Part. length        " + image.env.getPartLen(partno);
	    
	    UDF_AD fsds_loc = image.env.getLogicalVolDesc().getFSDSLoc();
	    String fsds_info = 
  		  "  FSDS Part.Refno     " + fsds_loc.getPartRefNo() + nl
		+ "  FSDS LBN            " + fsds_loc.getLbn();

	    int root_lbn = image.env.getFileSetDesc(0).getRootDirectoryICB().getLbn();
	    int root_partno = image.env.getFileSetDesc(0).getRootDirectoryICB().getPartRefNo();
	    int stream_lbn = image.env.getFileSetDesc(0).getSystemStreamDirectoryICB().getLbn();
	    int stream_partno = image.env.getFileSetDesc(0).getSystemStreamDirectoryICB().getPartRefNo();
	    String root_info = 
		  "  Root Part.Refno     " + root_partno + nl
		+ "  Root LBN            " + root_lbn;
	    
	    out.println(out1);
	    out.println(fsds_info);
	    out.println(root_info);
	}
	catch(UDF_VolException e){
	    e.printStackTrace();
	}
    }

    public static void printInformation(PrintStream out, UDF_Image image) {

	try {
	    out.println("Primary Vol. Desc.");
	    UDF_desc1 desc1 = image.env.getPrimaryVolDesc(UDF_Env.VDS_AUTO, 1);
	    String [] str = desc1.toInformation("  ", true);
	    for (int i = 0, max = str.length; i < max; i++) {
		out.println(str[i]);
	    }
	}
	catch(UDF_VolException e) {
	    e.printStackTrace();
	}

	try {
	    UDF_desc4 [] desc4 = image.env.getImplUseVolDesc(UDF_Env.VDS_AUTO);

	    for (int n = 0, nmax = desc4.length; n < nmax; n++) {

		out.println("Impl. Use Vol. Desc.");
		String [] str = desc4[n].toInformation("  ", true);
		for (int i = 0, max = str.length; i < max; i++) {
		    out.println(str[i]);
		}
	    }
	}
	catch(UDF_VolException e) {
	    e.printStackTrace();
	}

	try {
	    for (int n = 0; ; n++) {

		UDF_desc5 desc5 = image.env.getPartDesc(UDF_Env.VDS_AUTO, n);
		out.println("Partition Desc.");
		String [] str = desc5.toInformation("  ", true);
		for (int i = 0, max = str.length; i < max; i++) {
		    out.println(str[i]);
		}
		UDF_PartHeaderDesc phd = (UDF_PartHeaderDesc)desc5.getPartContentsUse().getFirstChild();
		out.println("Partition Header Desc.");
		str = phd.toInformation("  ", true);
		for (int i = 0, max = str.length; i < max; i++) {
		    out.println(str[i]);
		}
	    }

	}
	catch(UDF_VolException e) {
//	    e.printStackTrace();
	}

	try {
	    out.println("Logical Vol. Desc.");
	    UDF_desc6 desc6 = image.env.getLogicalVolDesc(UDF_Env.VDS_AUTO);
	    String [] str = desc6.toInformation("  ", true);
	    for (int i = 0, max = str.length; i < max; i++) {
		out.println(str[i]);
	    }

	    out.println("Part. Maps");
	    UDF_Element [] el = desc6.getPartMaps().getChildren();
	    for (int n = 0, nmax = el.length; n < nmax; n++) {
		str = el[n].toInformation("  ", true);
		for (int i = 0, max = str.length; i < max; i++) {
		    out.println(str[i]);
		}
	    }
	}
	catch(UDF_VolException e) {
	    e.printStackTrace();
	}

	try {
	    out.println("Logical Vol. Integrity Desc.");
	    UDF_desc9 desc9 = image.env.getLogicalVolIntegrityDesc();
	    String [] str = desc9.toInformation("  ", true);
	    for (int i = 0, max = str.length; i < max; i++) {
		out.println(str[i]);
	    }
	}
	catch(UDF_VolException e) {
	    e.printStackTrace();
	}

	try {
	    out.println("File Set Desc.");
	    UDF_desc256 desc256 = image.env.getFileSetDesc(0);
	    String [] str = desc256.toInformation("  ", true);
	    for (int i = 0, max = str.length; i < max; i++) {
		out.println(str[i]);
	    }
	}
	catch(UDF_VolException e) {
	    e.printStackTrace();
	}

    }

    public static String convRevision(int revision) {

        int major, minor;

        major = revision / 0x0100;
        revision -= major * 0x0100;
        minor = revision / 0x0010;
        revision -= minor * 0x0010;
        minor *= 10;
        minor += revision;

        return (String.valueOf(major) + "." + ((minor > 9) ? String.valueOf(minor): "0" + String.valueOf(minor)));
    }
}
