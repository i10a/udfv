/*
*/
package com.udfv.udf250;

import java.io.*;
import java.util.*;
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.access.*;
import com.udfv.exception.*;
import com.udfv.ecma167.*;

public class UDF_desc266 extends com.udfv.udf201.UDF_desc266
{
    /**
	（ローカル）クラス名を取得
    */
    public static String getUDFClassName( ) {
        return "UDF_desc266";
    }


    /**
      コンストラクタ

      @param elem 親
      @param prefix ネームスコープ
      @param name 名前
    */
    public UDF_desc266(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
    }

    /**
      Extended File Entry の検証。
    */
    public UDF_ErrorList verify() throws UDF_Exception{

	UDF_ErrorList el = new UDF_ErrorList();
	final short category = UDF_Error.C_UDF250;

	if(env.udf_revision != 0x250)
	    return super.verify();

	//　File Entry / Extended File Entryの共通部分の検証　//
	el.addError(com.udfv.udf250.UDF_desc261.verifyBase(this, UDF_Error.C_UDF250));

	// Metadata * Extended File Entry 固有の検証
	int filetype = getICBFileType();
	if(filetype == 250){

	    el.addError(UDF_desc261.verifyMetadataFile(this, true));
	    el.setRName("Metadata Main Extended File Entry");
	}
	else if(filetype == 251){

	    el.addError(UDF_desc261.verifyMetadataFile(this, false));
	    el.setRName("Metadata Mirror Extended File Entry");
	}
	else if(filetype == 252){

	    el.addError(UDF_desc261.verifyMetadataBitmapFile(this));
	    el.setRName("Metadata Bitmap Extended File Entry");
	}else
	    el.setRName("Extended File Entry");

	el.setGlobalPoint(getGlobalPoint());

	el.addError(super.verify());

	return el;
    }
    
    /*
      desc261にも同じものがある
     */
    public void recalc(short type, UDF_RandomAccess f){
	super.recalc(type, f);
	try{
	    if(type == RECALC_ENV){
		int filetype = getICBFileType();
		int fileflag = getICBFlags();
		
		if(filetype == 252){
		    if(fileflag == 3){
			//env.metaPartSpaceBitmapDesc = (UDF_desc264)getAllocDesc().getFirstChild();
		    }
		    else if(env.getMetadataPartMap() != null){
			int ref_partno = env.getMetadataPartMap().getPartNumber().getIntValue();
			//env.metaPartSpaceBitmapDesc = (UDF_desc264)getReferenceExtent().getFirstChild();
		    }
		    else
			debugMsg(3, "desc266.recalc(): Metadata Partition Map is not loaded.");
		}
	    }
	}    
	catch(Exception e){
	    ignoreMsg("udf250.UDF_desc261:recalc", e);
	}
    }

}
