/*
*/
package com.udfv.udf102;

import java.io.*;
import java.util.*;
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.ecma167.*;
import com.udfv.exception.*;
import com.udfv.access.*;

public class UDF_PartHeaderDesc extends com.udfv.ecma167.UDF_PartHeaderDesc
{
  /**
    @param elem 親
    @param name 名前
  */
  public UDF_PartHeaderDesc(UDF_Element elem, String prefix, String name){
     super(elem, prefix, name == null ? "UDF_PartHeaderDesc" : name);	
  }

    
    public UDF_ErrorList verify() throws UDF_Exception{
	
        UDF_ErrorList el = new UDF_ErrorList();
	
	
	if(env.udf_revision != 0x102)
	    return super.verify();
	
	el.addError(verifyBase(UDF_Error.C_UDF102));
	el.setGlobalPoint(getGlobalPoint());
	el.setRName("Partition Header Descriptor");
	
	return el;
    }
    
    /**
      全てのUDF規格共通のPartition Header Descriptor の検証を行います。
    */
    public UDF_ErrorList verifyBase(short category) throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	// Unalloc Space Table とUnalloc Space Bitmap は同時に使用されてはならない
	com.udfv.ecma167.UDF_short_ad ustad = getUnallocatedSpaceTable();
	com.udfv.ecma167.UDF_short_ad usbad = getUnallocatedSpaceBitmap();
	
	if((ustad.getLen() != 0 || ustad.getLbn() != 0) &&
	   (usbad.getLen() != 0 || usbad.getLbn() != 0)){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "",
			 "Space Tables and Space Bitmaps shall not both be used at the same time within a Logical Volume.",
			 "2.3.3"));
	}
	
	//　PartitionIntegrityEntrysは使用されないので全て０に設定されるべきです　//
        UDF_short_ad ad = getPartIntegrityTable();
        if (ad.getLen() != 0 || ad.getLbn() != 0) {

            el.addError(
                new UDF_Error(
                    category, UDF_Error.L_ERROR, "PartitionIntegrityTable",
                    "Shall be set to all zeros since PartitionIntegrityEntrys are not used.",
                    "2.3.3.1"
                )
            );
        }
	
        return el;
    }

    /**
        Partition Header Desc. の情報を表示。

    */
    public String [] toInformation(String indent, boolean detail) {

	String nl = UDF_Util.getSystemNewLine();

	Vector vec = new Vector();
	String str;

	UDF_short_ad ad;
	if (detail) {
	    ad = getUnallocatedSpaceTable();
	    str = indent + "Unallcated Space Table    # short_ad lbn    # " + ad.getLbn() + nl
	        + indent + "                          # short_ad len    # " + ad.getLen();
	    vec.add(str);
	}
	{
	    ad = getUnallocatedSpaceBitmap();
	    str = indent + "Unallcated Space Bitmap   # short_ad lbn    # " + ad.getLbn() + nl
	        + indent + "                          # short_ad len    # " + ad.getLen();
	    vec.add(str);
	}
	if (detail) {
	    ad = getPartIntegrityTable();
	    str = indent + "Part. Integrity Table     # short_ad lbn    # " + ad.getLbn() + nl
	        + indent + "                          # short_ad len    # " + ad.getLen();
	    vec.add(str);

	    ad = getFreedSpaceTable();
	    str = indent + "Free Space Table          # short_ad lbn    # " + ad.getLbn() + nl
	        + indent + "                          # short_ad len    # " + ad.getLen();
	    vec.add(str);

	    ad = getFreedSpaceBitmap();
	    str = indent + "Free Space Bitmap         # short_ad lbn    # " + ad.getLbn() + nl
	        + indent + "                          # short_ad len    # " + ad.getLen();
	    vec.add(str);
	}

	String [] sl = new String[vec.size()];
	for (int i = 0, max = vec.size(); i < max; i++) {
	    sl[i] = (String)vec.elementAt(i);
	}

	return sl;
    }
};
