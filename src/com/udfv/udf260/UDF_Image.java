/*
*/
package com.udfv.udf260;

import java.io.*;
import java.util.*;
import java.util.zip.*;
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
import org.apache.xml.serialize.*;
import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.exception.*;
import com.udfv.access.*;
import com.udfv.exception.*;
import com.udfv.ecma167.*;

/**
   UDF_Imageは UDF2.60に対応したイメージを操作するクラスである。
 */
public class UDF_Image extends com.udfv.udf250.UDF_Image
{
    public UDF_Image(){
	super();
    }

    public UDF_Image(Document document){
	super(document);
    }
    
    public UDF_ErrorList verify() throws UDF_Exception{
	UDF_ErrorList el = new UDF_ErrorList();
	final short category = UDF_Error.C_UDF260;
	
	if(env.udf_revision != 0x260)
	    return super.verify();
	
	// MVDSとRVDSは同じECCブロック内に記録されていてはならない。UDF260のみのチェック
	final int ECC = (int)(env.ecc_blocksize / env.LBS);
	if(env.getMVDSLoc() / ECC == env.getRVDSLoc() / ECC){
	    el.addError(new UDF_Error
		       (category, UDF_Error.L_ERROR, "",
			"MVDS is recorded at LBN" + env.getMVDSLoc() + ", and RVDS is at LBN" + env.getRVDSLoc() + ".\n" + 
			"The Main VDS extent and the Reserve VDS extent shall be recorded in different ECC blocks.",
			"2.2.3.2"));
	}
	
	el.addError(verifyBase(category));
	el.addError(super.verify());
	
	return el;
    }

    /**
       指定されたエレメントを、Metadata File として検証する。
       
       @param elems 検証対象となるエレメントの配列。
       @return エラーリストインスタンス。
    */
    protected UDF_ErrorList verifyMetadataExtent(UDF_ElementBase[] elems, short category) throws UDF_Exception{
	if(env.udf_revision != 0x260)
	    return super.verifyMetadataExtent(elems, category);
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	for(int i = 0; i < elems.length; i++){
	    
	    UDF_Element elem = (UDF_Element)elems[i];
	    
	    
	    // UDF_Extent かUDF_Directory の場合のみ、さらにその中身も見る
	    if(com.udfv.core.UDF_Extent.class.isAssignableFrom(elem.getClass()) ||
	       com.udfv.ecma167.UDF_Directory.class.isAssignableFrom(elem.getClass())){
		
		el.addError(verifyMetadataExtent(elem.getChildren(), category));
		continue;
	    }
	    
	    // FSD
	    if(com.udfv.ecma167.UDF_desc256.class.isAssignableFrom(elem.getClass()))
		continue;
	    // Terminating Descriptor
	    if(com.udfv.ecma167.UDF_desc8.class.isAssignableFrom(elem.getClass()))
		continue;
	    // FID
	    else if(com.udfv.ecma167.UDF_desc257.class.isAssignableFrom(elem.getClass()))
		continue;
	    // FE(ICB)
	    else if(com.udfv.ecma167.UDF_desc261.class.isAssignableFrom(elem.getClass())){
		
		el.addError(com.udfv.udf250.UDF_desc261.verifyFEInMetadata((com.udfv.ecma167.UDF_desc261)elem, category));
		continue;
	    }
	    // EFE(ICB)
//	    else if(com.udfv.ecma167.UDF_desc266.class.isAssignableFrom(elem.getClass()))
//		continue;
	    // An unused block marked free in the Metadata Bitmap File
	    else if(com.udfv.ecma167.UDF_pad.class.isAssignableFrom(elem.getClass()))
		continue;
	    // Extent of Allocation Descriptors
	    else if(com.udfv.core.UDF_Data.class.isAssignableFrom(elem.getClass()))
		continue;
	    // Allocation Extent Descriptor
	    else if(com.udfv.ecma167.UDF_desc258.class.isAssignableFrom(elem.getClass()))
		continue;
	    // Indirect Entry(ICB)
	    else if(com.udfv.ecma167.UDF_desc259.class.isAssignableFrom(elem.getClass()))
		continue;
	    // Termina Entry(ICB)
	    else if(com.udfv.ecma167.UDF_desc260.class.isAssignableFrom(elem.getClass()))
		continue;
	    else{
		
		// 上記以外の構造は含まれていてはならない
		UDF_Error err = new UDF_Error
		    (category, UDF_Error.L_ERROR, "Metadata File(" + elem.getName() + ")",
		     "The Allocation Descriptors for this file shall describe only logical blocks which contain " +
		     " one of the below data types. No user data or other metadata may be referenced.\n\n" +
		     "* FSD\n* ICB\n* Extent of Allocation Descriptors(see 2.3.11)\n* Directory or stream directory data(i.e. FIDs)\n" +
		     "* An unused block marked free in the Metadata Bitmap File.",
		     "2.2.13.1");
		
		err.setGlobalPoint(elem.getGlobalPoint());
		el.addError(err);
	    }
	}
	return el;
    }
    
    /*
       使用するパッケージの優先順位を設定します。
    private void setPkgPriority(){
	setPkgPriority(0x260);
    }
    */

}
