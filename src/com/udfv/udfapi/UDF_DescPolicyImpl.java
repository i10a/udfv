package com.udfv.udfapi;

import java.lang.*;
import java.io.*;
import com.udfv.core.*;
import com.udfv.ecma167.*;
import com.udfv.exception.*;
import com.udfv.access.*;
import com.udfv.encoding.*;
import com.udfv.udf102.UDF_LVInformation;
import com.udfv.udf102.UDF_desc9_ImplUse;

public class UDF_DescPolicyImpl implements UDF_DescPolicy {
    //for desc1
    //String vol_id = "8:HEART UDF VOLUME";
    //String vol_set_id = "8:2C6428C300000000TEST_IMAGE";

    //for desc2
    //int mvds_loc = 32;
    //int mvds_len = 32768;
    //int rvds_loc = 64;
    //int rvds_len = 32768;

    //for desc4
    // LVInformation
    //String lVInfo1 = "8:OWNER NAME";
    //String lVInfo2 = "8:Heart Solutions, Inc.";
    //String lVInfo3 = "8:CONTACT INFORMATION";

    //for desc5
    //int access_type = 4;
    //int part_number = 0;
    //int part_starting_loc = 272;
    //int part_len = 2297344;
    //String nsr = "+NSR03";

    //for desc6
    //String logical_vol_id = "8:PSX Volume";
    //int logical_block_size = 2048;
    //int lvis_loc = 96;
    //int lvis_len = 8192;

    //int fsds_loc = 0;
    //int fsds_len = 4096;
    //int fsds_partno = 1;

    public UDF_CrcDesc createVolDesc(UDF_Image image, UDF_Policy policy, int tagid) throws UDF_Exception, IOException{
	if(tagid < 0 || tagid > 10)
	    throw new UDF_DescTagException(image, "Bad tagid:" + tagid);
	UDF_CrcDesc desc = UDF_CrcDesc.genCrcDesc(tagid, image, null, null);
	desc.setDefaultValue();
	UDF_VolPolicy vp = policy.getVolPolicy();

	//descTag置きかえ
        UDF_tag descTag = policy.getDescTagPolicy().create(image, policy);
	descTag.getTagId().setValue(desc.getDescTag().getTagId().getIntValue());
	desc.setDescTag(descTag);
	final String ocu = "OSTA Compressed Unicode";

	switch(tagid){
	default:
	    break;
	case 1:
	    {
		UDF_Encoding enc = UDF_Encoding.genEncoding(ocu);
		
		UDF_desc1 desc1 = (UDF_desc1)desc;
		UDF_charspec charspec = UDF_charspec.createInstance(image, null, "desc-char-set", 0, ocu.getBytes());
		desc1.setDescCharSet(charspec);
		charspec = UDF_charspec.createInstance(image, null, "explanatory-char-set", 0, ocu.getBytes());
		desc1.setExplanatoryCharSet(charspec);
		
		desc1.getVolId().setEncoding(enc);
		desc1.getVolSetId().setEncoding(enc);

		desc1.getVolId().setValue(vp.getVolId());
		desc1.getVolSetId().setValue(vp.getVolSetId());
		desc1.setImplId(policy.getRegidPolicy().createDeveloperId(image, "impl-id"));
	    }
	    break;
	case 2:
	    UDF_desc2 desc2 = (UDF_desc2)desc;
	    desc2.getMainVolDescSeqExtent().getExtentLen().setValue(vp.getMVDSLen());
	    desc2.getMainVolDescSeqExtent().getExtentLen().setAttribute("ref", "MVDS.len");

	    desc2.getMainVolDescSeqExtent().getExtentLoc().setValue(vp.getMVDSLoc());
	    desc2.getMainVolDescSeqExtent().getExtentLoc().setAttribute("ref", "MVDS.loc");

	    desc2.getReserveVolDescSeqExtent().getExtentLen().setValue(vp.getRVDSLen());
	    desc2.getReserveVolDescSeqExtent().getExtentLen().setAttribute("ref", "RVDS.len");

	    desc2.getReserveVolDescSeqExtent().getExtentLoc().setValue(vp.getRVDSLoc());
	    desc2.getReserveVolDescSeqExtent().getExtentLoc().setAttribute("ref", "RVDS.loc");
	    break;
	case 3:
	    throw new UDF_NotImplException(null, "volume descriptor pointer not impelemnted");
	case 4:
	    {
		UDF_desc4 desc4 = (UDF_desc4)desc;
		desc4.setImplId(policy.getRegidPolicy().createUDFId(image, "*UDF LV Info", "impl-id"));
		UDF_LVInformation lvi = (UDF_LVInformation)image.createElement("UDF_LVInformation", null, null);
		lvi.setDefaultValue();
		
		UDF_Encoding enc = UDF_Encoding.genEncoding(ocu);//"OSTA Compressed Unicode");
		UDF_charspec cs = (UDF_charspec)image.createElement("UDF_charspec", null, "lvi-charset");
		cs.setDefaultValue();
		cs.getCharSetInfo().setValue(ocu);//"OSTA Compressed Unicode");
		
		lvi.setLVICharset(cs);
		lvi.getLogicalVolId().setEncoding(enc);
		lvi.getLVInfo1().setEncoding(enc);
		lvi.getLVInfo2().setEncoding(enc);
		lvi.getLVInfo3().setEncoding(enc);
		lvi.getLVInfo1().setValue(vp.getLVInfo1());
		lvi.getLVInfo2().setValue(vp.getLVInfo2());
		lvi.getLVInfo3().setValue(vp.getLVInfo3());
		lvi.setImplId(policy.getRegidPolicy().createDeveloperId(image, "impl-id"));
		
		desc4.getImplUse().replaceChild(lvi);
	    }
	    break;
	case 5:
	    {
		UDF_desc5 desc5 = (UDF_desc5)desc;
		desc5.getAccessType().setValue(vp.getAccessType());
		desc5.getPartNumber().setValue(vp.getPartNumber());

		desc5.setPartContents(policy.getRegidPolicy().createApplicationId
				      (image, vp.getNSR(), "part-contents"));

		desc5.getPartStartingLoc().setValue(vp.getPartStartingLoc());
		desc5.getPartLen().setValue(vp.getPartLen());

		desc5.setImplId(policy.getRegidPolicy().createDeveloperId
				(image, "impl-id"));
	    }
	    break;
	case 6:
	    {
		UDF_desc6 desc6 = (UDF_desc6)desc;
		
		UDF_Encoding enc = UDF_Encoding.genEncoding(ocu);//"OSTA Compressed Unicode");
		UDF_charspec cs = (UDF_charspec)image.createElement("UDF_charspec", null, "desc-char-set");
		cs.setDefaultValue();
		cs.getCharSetInfo().setValue(ocu);//"OSTA Compressed Unicode");
		desc6.setDescCharSet(cs);
		
		desc6.getLogicalVolId().setEncoding(enc);
		desc6.getLogicalVolId().setValue(vp.getLogicalVolId());
		desc6.getLogicalBlockSize().setValue(vp.getLogicalBlockSize());

		desc6.setDomainId(policy.getRegidPolicy().createDomainId
				  (image, "domain-id"));
		desc6.setImplId(policy.getRegidPolicy().createDeveloperId
				(image, "impl-id"));

		desc6.getIntegritySeqExtent().getExtentLen().setValue(vp.getLVISLen());
		desc6.getIntegritySeqExtent().getExtentLoc().setValue(vp.getLVISLoc());

		desc6.getIntegritySeqExtent().getExtentLen().setAttribute("ref", "LVIS.len");
		desc6.getIntegritySeqExtent().getExtentLoc().setAttribute("ref", "LVIS.loc");

		UDF_long_ad ad = (UDF_long_ad)image.createElement("UDF_long_ad", null, null);
		ad.setDefaultValue();
		ad.setLen(vp.getFSDSLen());
		ad.setLbn(vp.getFSDSLoc());
		ad.setPartRefNo(vp.getFSDSPartno());
		ad.setRefAttribute("FSDS");

		com.udfv.udf102.UDF_long_ad_ADImpUse imp = (com.udfv.udf102.UDF_long_ad_ADImpUse)image.createElement("UDF_long_ad_ADImpUse", null, null);
		imp.setDefaultValue();
		ad.getImplUse().replaceChild(imp);
		
		desc6.getLogicalVolContentsUse().replaceChild(ad);

	    }		

	    break;
	case 7:
	    UDF_desc7 desc7 = (UDF_desc7)desc;
	    break;
	case 8:
	    UDF_desc8 desc8 = (UDF_desc8)desc;
	    break;
	case 9:
	    {
		UDF_desc9 desc9 = (UDF_desc9)desc;
		UDF_LogicalVolHeaderDesc lvhd = (UDF_LogicalVolHeaderDesc)image.createElement("UDF_LogicalVolHeaderDesc", null, null);
		lvhd.setDefaultValue();
		desc9.getLogicalVolContentsUse().replaceChild(lvhd);
		desc9.getLenOfImplUse().setValue(60);
		desc9.getImplUse().setSize(60);
		
		UDF_desc9_ImplUse impu = (UDF_desc9_ImplUse)image.createElement("UDF_desc9_ImplUse", null, null);
		impu.setHintSize(60);
		impu.setDefaultValue();
		impu.setImplId(policy.getRegidPolicy().createDeveloperId(image, "impl-id"));
		impu.getMinUDFReadRevision().setValue(0x250);
		impu.getMinUDFWriteRevision().setValue(0x250);
		impu.getMaxUDFWriteRevision().setValue(0x250);
		impu.getNumberOfDirectories().setValue(1);

		desc9.getImplUse().replaceChild(impu);

	    }
	}

	descTag.getDescCRCLen().setValue(desc.getSize() - 16);

	return desc;
    }

    public UDF_CrcDesc createFSDesc(UDF_Image image, UDF_Policy policy, int tagid) throws UDF_Exception, IOException{
	if(tagid != 8 && tagid < 256 && tagid > 266)
	    throw new UDF_DescTagException(image, "Bad tagid:" + tagid);

	UDF_CrcDesc desc = UDF_CrcDesc.genCrcDesc(tagid, image, null, null);
	desc.setDefaultValue();

	//descTag置きかえ
        UDF_tag descTag = policy.getDescTagPolicy().create(image, policy);
	descTag.getTagId().setValue(desc.getDescTag().getTagId().getIntValue());
	desc.setDescTag(descTag);
	final String ocu = "OSTA Compressed Unicode";

	UDF_VolPolicy vp = policy.getVolPolicy();
	switch(tagid){
	default:
	    break;
	case 8:
	    break;
	case 256:
	    {
		UDF_desc256 desc256 = (UDF_desc256)desc;
		UDF_charspec charspec = UDF_charspec.createInstance(image, null, "logical-vol-id-char-set", 0, ocu.getBytes());
		desc256.setLogicalVolIdCharSet(charspec);
		charspec = UDF_charspec.createInstance(image, null, "file-set-char-set", 0, ocu.getBytes());
		desc256.setFileSetCharSet(charspec);
		desc256.setDomainId(policy.getRegidPolicy().createDomainId
				    (image, "domain-id"));
		desc256.getFileSetId().setValue(vp.getFileSetId());
		desc256.getLogicalVolId().setValue(vp.getLogicalVolId());
	    }
	    break;
	case 257:
	    break;
	case 258:
	    break;
	case 259:
	    break;
	case 260:
	    break;
	case 261:
	    break;
	case 262:
	    break;
	case 263:
	    break;
	case 264:
	    break;
	case 265:
	    break;
	case 266:
	    break;
	}

	if(tagid != 264)
	    descTag.getDescCRCLen().setValue(desc.getSize() - 16);
	else
	    descTag.getDescCRCLen().setValue(0);
	
	return desc;
    }
}
