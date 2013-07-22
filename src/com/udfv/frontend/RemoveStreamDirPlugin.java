/*
 */
package com.udfv.frontend;

import com.udfv.udfapi.*;
import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.ecma167.*;

public class RemoveStreamDirPlugin implements FrontendPlugin{
    
    Frontend front;
    UDF_API api;
    UDF_Policy policy;
    com.udfv.core.UDF_Image image;

    public RemoveStreamDirPlugin(){
    }

    public void setFrontend(Frontend frontend){
	front = frontend;
	api = front.getAPI();
	policy = front.getPolicy();
	image = front.getImage();
    }
    
    public int runCmd(String argv[], int mode) throws Exception{
	/*
	  frag (<file or directory>)
	  何も指定しない場合、System Stream Direcotryを削除する。
	 */

	if(argv.length != 0){
	    
	    UDF_FEDesc fe = image.findFEByPathname(argv[1], 0);
	    if(!com.udfv.ecma167.UDF_desc266.class.isAssignableFrom(fe.getClass())){
		
		System.err.println(argv[1] + " is not used Extended File Entry.");
		return CMD_OK;
	    }
	    
	    
	    UDF_desc266 efe = (UDF_desc266)fe;
	    String ref = efe.getStreamDirectoryICB().getExtentLen().getAttribute("ref");
	    if(ref.equals("")){
		
		System.err.println(argv[1] + " doesn't have Stream Directory.");
		return CMD_OK;
	    }
	    
	    ref = UDF_Util.car(ref, '.');
	    if(0 < ref.length()){
		
		UDF_Extent fe_ext = (UDF_Extent)image.findById(ref);
		if(fe_ext != null){
		    UDF_FEDesc fe2 = (UDF_FEDesc)fe_ext.getFirstChild();
			removeChildStreamDir(fe2);
		}
	    }
	    
	    setStreamICB2Zero(efe.getStreamDirectoryICB());
	}
	else{
	    
	    removeChildStreamDir(image.env.getSRootFE(0));
	    
	    // System Stream Directory ICBを全て0にする
	    com.udfv.ecma167.UDF_desc256 desc256 = image.env.getFileSetDesc(0);
	    setStreamICB2Zero(desc256.getSystemStreamDirectoryICB());
	    
	    if(image.env.hasMirrorPartmap(1)){
		
		removeChildStreamDir(image.env.getSRootFE(1));
		desc256 = image.env.getFileSetDesc(1);
		setStreamICB2Zero(desc256.getSystemStreamDirectoryICB());
	    }
	}
	
	System.out.println("removeStreamDir");
	return CMD_OK;
    }
    
    /**
       指定されたStream Directory以下のデータを削除する。
       
       @param parentfe 削除するStream DirectoryのFE。通常のFEを削除することもできるかもしれない。
    */
    private void removeChildStreamDir(com.udfv.ecma167.UDF_FEDesc parentfe){
	
	if(parentfe == null)
	    return;
	
	com.udfv.core.UDF_ElementList directory = parentfe.getDirectoryList();
	if(directory == null)
	    return;
	
        for (int i = 1, max = directory.size(); i < max; i++) {

            //　File Identifier Descriptorを読み出します　//
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
	    
	    removeChildStreamDir(fe);
	}
	
	UDF_Extent ext = parentfe.getReferenceExtent();
	image.removeChild(ext);
	
	UDF_Extent ext2 = (UDF_Extent)parentfe.getParent();
	image.removeChild(ext2);
    }
    
    /**
       Stream Directory ICB(long AD)を全て0にセットする。
       
       @param icb stream directory ICB
    */
    private void setStreamICB2Zero(com.udfv.ecma167.UDF_long_ad icb){
	
	icb.getExtentLen().setValue(0);
	icb.getExtentLoc().getLogicalBlockNumber().setValue(0);
	icb.getExtentLoc().getPartReferenceNumber().setValue(0);
	byte pad[] = new byte[icb.getImplUse().getSize()];
	icb.getImplUse().setData(pad);
    }
}

