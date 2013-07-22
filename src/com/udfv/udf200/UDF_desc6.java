/*
*/
package com.udfv.udf200;

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
import com.udfv.encoding.*;

public class UDF_desc6 extends com.udfv.udf150.UDF_desc6
{

  /**
	@param elem 親
	@param name 名前
  */
  public UDF_desc6(UDF_Element elem, String prefix, String name){
     super(elem, prefix, name == null ? "UDF_desc6" : name);	
     init();
  }

//begin:add your code here
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	if(env.udf_revision != 0x200)
	    return super.verify();
	
	el.addError(verifyBase(UDF_Error.C_UDF200));
	
	el.addError(getPartMaps().verify("PartitionMaps"));
	
	el.setRName("Logical Volume Descriptor");
	el.addError(super.verify());
	el.setGlobalPoint(getGlobalPoint());
	return el;	
    }
    
    /**
    */
    protected UDF_ErrorList verifyBase(short category) throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	UDF_Error ret;
	
	
	// DescriptorCharacterSet がCS0 でない
	ret = getDescCharSet().verifyCharSetType(0);
	if(ret.isError()){
	    
	    ret.setCategory(category);
	    ret.setMessage("Shall be set to indicate support for CS0 only as defined in 2.1.2");
	    ret.setRefer("2.2.4.1");
	    ret.setRName("DescriptorCharacterSet");
	    el.addError(ret);
	}
	
	// LogicalVolumeIdentifier
	el.addError(getLogicalVolId().verify("LogicalVolumeIdentifier"));
	
	// DomainIdentifier のId は*OSTA UDF Compliant
	ret = getDomainId().verifyId("*OSTA UDF Compliant");
	if(ret.isError()){
	    
	    ret.setCategory(category);
	    ret.setMessage("This field shall indicate that the contents of this logical volume conforms to the domain " +
			   "defined in this document, therefore the DomainIdentifier shall be set to:\n" +
			   "\"*OSTA UDF Compliant\"");
	    ret.setRefer("2.2.4.3");
	    ret.setRName("DomainIdentifier");
	    el.addError(ret);
	}
	
	
	// Integrity Sequence Extent （の長さは）
	// Rewriteable またはOverwriteable のときは最低でも8K バイトでなければならない
	//int  accesstype = env.partDesc.getAccessType().getIntValue();
	int accesstype = env.getPartDescByPartRefNo(UDF_Env.VDS_AUTO, 0).getPartNumber().getIntValue();
	long isextlen = getIntegritySeqExtent().getExtentLen().getLongValue();
	if((accesstype == 3 || accesstype == 4) && isextlen < (8 * 1024)){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "IntegritySequenceExtent",
			 "For Rewriteable or Overwriteable media this shall be set to a minimu of 8K bytes.",
			 "2.2.4.6", String.valueOf(isextlen), ""));  // この参照番号が以前のリビジョンと異なる
	}
	
	return el;
    }

//end:
};
