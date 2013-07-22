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
import com.udfv.exception.*;
import com.udfv.access.*;
import com.udfv.ecma167.*;

public class UDF_VDS extends com.udfv.ecma167.UDF_VDS
{
    public UDF_VDS(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? "UDF_VDS" : name);
    }
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	if(env.udf_revision != 0x102)
	    return super.verify();
	
	el.addError(verifyBase(UDF_Error.C_UDF102));
	
	el.setRName("Volume Descriptor Sequence");
	el.addError(super.verify());
	
	return el;
    }
    
    
    protected UDF_ErrorList verifyBase(short category) throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	boolean isexist[] = { false, false, false, false, false, false, false, false, false, };  // 0は使用しない
	
	// Prevailing かどうかを見分けるフラグ
	int desc5_partnum = -1;
	com.udfv.ecma167.UDF_dstring  desc6_logicalvolid = null;
	com.udfv.ecma167.UDF_charspec desc6_desccharset = null;
	
	
	// desc3 で飛ばされた領域単体では検証できないのでやらない
	if(getPrevVDS() != null)
	    return el;
	
	UDF_Element[] child = getVDSList().toElemArray();
//	UDF_ElementBase[] child = getChildren();
	
	// VDS は最低でも16セクタのサイズがなければならない
	long vdssize = getLongSize();
	vdssize += (getNextVDS() != null) ? getNextVDS().getLongSize() : 0;
	if(vdssize < UDF_Env.LBS * 16){
	    
	    UDF_Error err = new UDF_Error
			(category, UDF_Error.L_ERROR, "",
			 "Both the main and reserve volume descriptor sequence extents shall each have " +
			 "a minimum length of 16 logical sectors.", "2");
	    err.setGlobalPoint(getGlobalPoint());
	    el.addError(err);
	}
	
	for(int i = 0; i < child.length; i++){

	    String name = child[i].getName();
	    
	    
	    if(name.equals("UDF_desc1")){  // Primary Vol Desc

		
	    }
	    else if(name.equals("UDF_desc4")){  // Imple Use Vol Desc
		
		isexist[4] = true;
	    }
	    else if(name.equals("UDF_desc5")){  // Partition Desc
		
		com.udfv.ecma167.UDF_desc5 desc5 = (com.udfv.ecma167.UDF_desc5)child[i];
		int partnum = desc5.getPartNumber().getIntValue();
		
		// Prevailing なdesc5 は複数存在してはならない
		if(isexist[5]){
		    
		    if(partnum != desc5_partnum){
			
			UDF_Error err = new UDF_Error
				    (category, UDF_Error.L_ERROR, "Partition Descriptor",
				     "There shall be exactly one prevailing Partition Descriptor recorded per volume.", "2");
			err.setGlobalPoint(desc5.getGlobalPoint());
			el.addError(err);
		    }
		}
		else{
		    
		    desc5_partnum = partnum;
		    isexist[5] = true;
		}
	    }
	    else if(name.equals("UDF_desc6")){  // Logical Vol Desc
		
		com.udfv.ecma167.UDF_desc6 desc6 = (com.udfv.ecma167.UDF_desc6)child[i];
		com.udfv.ecma167.UDF_charspec desccharset = desc6.getDescCharSet();
		com.udfv.ecma167.UDF_dstring logicalvolid = desc6.getLogicalVolId();
		
		// Prevailing なdesc6 は複数存在してはならない
		if(isexist[6]){
		    
		    if(!UDF_Util.cmpBytesBytes(desc6_logicalvolid.getBytes(), logicalvolid.getBytes()) ||
		       !UDF_Util.cmpBytesBytes(desc6_desccharset.getBytes(), desccharset.getBytes())){
			
			UDF_Error err = new UDF_Error
				    (category, UDF_Error.L_ERROR, "Logical Volume Descriptor",
				     "There shall be exactly one prevailing Logical Volume Descriptor recorded per Volume Set.", "2");
			err.setGlobalPoint(desc6.getGlobalPoint());
			el.addError(err);
		    }
		}
		else{
		    
		    desc6_logicalvolid = logicalvolid;
		    desc6_desccharset = desccharset;
		    isexist[6] = true;
		}
	    }
	    else if(name.equals("UDF_desc7")){  // Unallocated Space Desc
		
		isexist[7] = true;
	    }
	}
	
	// Implementation Use Volume Descriptor が１つも存在しない
	if(!isexist[4]){
	    
	    UDF_Error err = new UDF_Error
			(category, UDF_Error.L_ERROR, "",
			 "Implementation Use Volume Descriptor shall be recorded on every Volume of a Volume Set.", "2.2.7");
	    err.setGlobalPoint(getGlobalPoint());
	    el.addError(err);
	}
	
	// Part Desc が1 つも存在しない
	if(!isexist[5]){
	    
	    UDF_Error err = new UDF_Error
			(category, UDF_Error.L_ERROR, "",
			 "There shall be exactly one prevailing Partition Descriptor recorded per volume.", "2");
	    err.setGlobalPoint(getGlobalPoint());
	    el.addError(err);
	}
	
	// Logical Vol Desc が1 つも存在しない
	if(!isexist[6]){
	    
	    UDF_Error err = new UDF_Error
			(category, UDF_Error.L_ERROR, "",
			 "There shall be exactly one prevailing Logical Volume Descriptor recorded per Volume Set.", "2");
	    err.setGlobalPoint(getGlobalPoint());
	    el.addError(err);
	}
	
	// Unallocated Space Desc が1 つも存在しない
	if(!isexist[7]){
	    
	    UDF_Error err = new UDF_Error
			(category, UDF_Error.L_ERROR, "",
			 "A single prevailing Unallocated Space Descriptor shall be recorded per volume", "2");
	    err.setGlobalPoint(getGlobalPoint());
	    el.addError(err);
	}
	
	return el;
    }
}
