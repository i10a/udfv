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
import com.udfv.encoding.*;

public class UDF_desc6 extends com.udfv.ecma167.UDF_desc6
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

    public void postReadHook(UDF_RandomAccess f) throws UDF_Exception{
	if(!getDescCharSet().isOSTACompressedUnicode()){
	    throw new UDF_EncodingException(this, "Bad Charspec(Only OSTA Compressed Unicode Supported)",
					    UDF_Exception.T_UDF200,
					    UDF_EncodingException.C_BADENCODING,
					    0);
	}
	UDF_Encoding enc = UDF_Encoding.genEncoding("OSTA Compressed Unicode");

	getLogicalVolId().setEncoding(enc);
	
	//UDF 2.1.5.2
	getImplId().extractSuffixAsUDFImplIdSuffix();

	super.postReadHook(f);
    }

    /**
        Logical Vol. Desc. の情報を表示。

    */
    public String [] toInformation(String indent, boolean detail) {

	String nl = UDF_Util.getSystemNewLine();

	Vector vec = new Vector();
	String str;

	str = indent + "Logical Vol. ID           # " + getLogicalVolId().getStringData();
	vec.add(str);
	str = indent + "Logical Block size        # " + getLogicalBlockSize().getIntValue();
	vec.add(str);
	str = indent + "Logical Vol. Contents Use # " + getLogicalVolContentsUse().getStringData();
	vec.add(str);
	str = indent + "Number Of Part. Maps      # " + getNumberOfPartMaps().getIntValue();
	vec.add(str);

	UDF_extent_ad ad = getIntegritySeqExtent();
	str = indent + "Integrity Seq. Extent     # extent loc      # " + ad.getExtentLoc().getIntValue() + nl
	    + indent + "                          # extent len      # " + ad.getExtentLen().getIntValue();
	vec.add(str);

	com.udfv.ecma167.UDF_regid autoDomainId = getDomainId();
	com.udfv.udf102.UDF_regid_UDFDomainIdSuffix suffix = (UDF_regid_UDFDomainIdSuffix) autoDomainId.getIdSuffix().getFirstChild();
	str = indent + "Domain Id                 # Id              # " + autoDomainId.getId().getStringData() + nl
	    + indent + "                          # UDF Revision    # " + suffix.getUDFRevision().getIntValue() + nl
	    + indent + "                          # Domain Flags    # " + suffix.getDomainFlags().getIntValue();
	vec.add(str);

	String [] sl = new String[vec.size()];
	for (int i = 0, max = vec.size(); i < max; i++) {
	    sl[i] = (String)vec.elementAt(i);
	}

	return sl;
    }

    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	if(env.udf_revision != 0x102)
	    return super.verify();
	
	el.addError(verifyBase(UDF_Error.C_UDF102));
	
	el.addError(getPartMaps().verify("PartitionMaps"));
	
	el.setRName("Logical Volume Descriptor");
	el.addError(super.verify());
	
	el.setGlobalPoint(getGlobalPoint());
	return el;
    }
    
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

	//partition map[0]の参照している partitionの accesstypeで比較する
	int accesstype = env.getPartDescByPartRefNo(UDF_Env.VDS_AUTO, 0).getPartNumber().getIntValue();
	long isextlen = getIntegritySeqExtent().getExtentLen().getLongValue();
	if((accesstype == 3 || accesstype == 4)){
	    
	    if(isextlen < (8 * 1024)){
	    
		el.addError(new UDF_Error
			    (category, UDF_Error.L_ERROR, "IntegritySequenceExtent",
			     "For Rewriteable or Overwriteable media this shall be set to a minimu of 8K bytes.",
			     "2.2.4.5", String.valueOf(isextlen), ""));
	    }
	}
	else if(isextlen == 0){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "IntegritySequenceExtent",
			 "If the extent's length is 0, then nosuch extent is specified(ECMA167). " + 
			 "But in UDF, Logical Volume Integrity Descriptor shall be recorded.",
			 "2", "0", ""));
	}
	
	return el;
    }


//end:
};
