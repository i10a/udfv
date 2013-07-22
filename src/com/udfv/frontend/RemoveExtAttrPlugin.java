/*
 */
package com.udfv.frontend;

import com.udfv.udfapi.*;
import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.ecma167.*;

public class RemoveExtAttrPlugin implements FrontendPlugin{
    
    Frontend front;
    UDF_API api;
    UDF_Policy policy;
    com.udfv.core.UDF_Image image;

    public RemoveExtAttrPlugin(){
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
	  何も指定しない場合、ルートディレクトリ以下の全てのFEのExtAttrを削除する。
	 */

	if(argv.length != 0){
	    
	    UDF_FEDesc fe = image.findFEByPathname(argv[1], 0);
	    removeExtAttr(fe);
	    
	    if(image.env.hasMirrorPartmap(1)){
		
		fe = image.findFEByPathname(argv[1], 0);
		removeExtAttr(fe);
	    }
	}
	else{
	    
	    removeChildExtAttr(image.env.getRootFE(0));
	    if(image.env.hasMirrorPartmap(1))
		removeChildExtAttr(image.env.getRootFE(1));
	}
	
	System.out.println("removeExtAttr");
	return CMD_OK;
    }
    
    /**
       File EntryからExtended Attributeを削除する。
       
       @param fe Extended Attributeを削除するFile Entry。
    */
    private void removeExtAttr(com.udfv.ecma167.UDF_FEDesc fe){
	
	UDF_bytes attr = fe.getExtendedAttr();
	attr.removeAllChildren();
	attr.setSize(0);
	
	int attrlen = fe.getLenOfExtendedAttr().getIntValue();
	if(attrlen != 0){
	    
	    fe.getLenOfExtendedAttr().setValue(0);
	    
	    UDF_tag tag = fe.getDescTag();
	    int curcrclen = tag.getDescCRCLen().getIntValue();
	    tag.getDescCRCLen().setValue(curcrclen - attrlen);
	}
    }
    
    /**
       指定されたFile Entry以下のFile EntryからExtended Attributeを削除する。
       
       @param parentfe Extended Attributeを削除するFile Entry。ディレクトリである必要はない。
    */
    private void removeChildExtAttr(com.udfv.ecma167.UDF_FEDesc parentfe){

	removeExtAttr(parentfe);
	
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
	    
            removeChildExtAttr(fe);
	}
    }
}

