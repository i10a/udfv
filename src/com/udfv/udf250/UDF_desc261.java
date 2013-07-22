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

public class UDF_desc261 extends com.udfv.udf201.UDF_desc261
{
    /**
      コンストラクタ

      @param elem 親
      @param prefix ネームスコープ
      @param name 名前
    */
    public UDF_desc261(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
    }

    public UDF_ErrorList verify() throws UDF_Exception{

	UDF_ErrorList el = new UDF_ErrorList();

	
	if(env.udf_revision != 0x250)
	    return super.verify();

	el.addError(super.verifyBase(this, UDF_Error.C_UDF250));
	// Stream Data を指すのにノーマルなFile Entry を使うべきではない。
	if(getICBFileType() == UDF_icbtag.T_SDIRECTORY){
	    
	    el.addError(new UDF_Error
			(UDF_Error.C_UDF250, UDF_Error.L_CAUTION, "",
			 "ECMA 167 3rd edition defines a new File Entry that contains a field for identifying a stream directory. " +
			 "This new File Entry should be used in place of the old File Entry, " +
			 "and should be used for describing the streams themselves.",
			 "3.3.5.1"));
	}
	
	// Metadata * File Entry 固有の検証
	int filetype = getICBFileType();
	if(filetype == 250){
	    
	    el.addError(verifyMetadataFile(this, true));
	    el.setRName("Metadata Main File Entry");
	}
	else if(filetype == 251){
	    
	    el.addError(verifyMetadataFile(this, false));
	    el.setRName("Metadata Mirror File Entry");
	}
	else if(filetype == 252){
	    
	    el.addError(verifyMetadataBitmapFile(this));
	    el.setRName("Metadata Bitmap File Entry");
	}else
	    el.setRName("File Entry");
	
	el.setGlobalPoint(getGlobalPoint());
	el.addError(super.verify());

	return el;
    }
    
    /**
       Metadata Partition 内に存在するFile Entry としての検証を行う。
       
       @param fe ファイルエントリ。
       @param category エラーカテゴリ。
       @return エラーリストインスタンス。
    */
    static public UDF_ErrorList verifyFEInMetadata(com.udfv.ecma167.UDF_desc261 fe, short category) throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	int filetype = fe.getICBTag().getFileType().getIntValue();
	int flags    = (fe.getICBTag().getFlags().getIntValue());// & 0x03);
	UDF_Error err = null;
	
	
	if(filetype == UDF_icbtag.T_DIRECTORY || filetype == UDF_icbtag.T_SDIRECTORY){
	    
	    // Directory のとき、SHORT_AD かimmediate でなければならない（ext_ad は別のところでチェック）
	    if((flags & 0x03) == UDF_icbtag.LONG_AD){
		
		err = new UDF_Error
		    (category, UDF_Error.L_ERROR, "FileType",
		     "File Entries describing directories or Stream Directories shall use either \"immediate\" " +
		     "allocation (i.e. the data is embedded in the File Entry - see ECMA 167 4/14.6.8 flag bits 0-2) " +
		     "or SHORT_ADs to describe the data space of the directory, since this data resides in " +
		     "the Metadata Partition along with the File Entry itself.",
		     "2.2.13", String.valueOf(flags), "");
	    }
	}
	else if((flags & 0x03) == UDF_icbtag.SHORT_AD){
	    
	    // そうでない場合、LONG_AD かimmediate でなければならない
	    err = new UDF_Error
		(category, UDF_Error.L_ERROR, "FileType",
		 "File Entries describing any other type of file data(including Named Streams) shall use either " +
		 "\"immediate\" allocation, or LONG_ADs that shall reference the physical or sparable partition " +
		 "referenced by the Metadata Partition Map, to describe the data space of the file.",
		 "2.2.13", String.valueOf(flags), "");
	}
	
	if(err != null){  // 上記２つのエラーのうち、エラーがあれば詳細を追加

	    err.setRName("ICBTag");
	    el.addError(err);
	}
	
	// ファイルがlongADである場合、そのPartition Reference Number はMetadata Partition を指していてはならない
	int metapartnum = -1; // まずMetadata Partition のパーティション番号を調べておく
	for(int i = 0; i < fe.env.getPartMapList().size() ; i++){
	    
	    if(com.udfv.udf250.UDF_MetadataPartMap.class.isAssignableFrom(fe.env.part[i].getClass())){
		
		metapartnum = i;
		break;
	    }
	}
	
	if(filetype != UDF_icbtag.T_DIRECTORY && filetype != UDF_icbtag.T_SDIRECTORY && metapartnum != -1 &&
	   (flags & 0x03) == UDF_icbtag.LONG_AD){
	    
	    UDF_ElementBase[] ad = fe.getAllocDesc().getChildren();
	    for(int i = 0, len = ad.length; i < len; i++){
		
		String name = ad[i].getName();
		if(!name.equals("UDF_long_ad"))
		    continue;
		
		com.udfv.ecma167.UDF_long_ad lad = (com.udfv.ecma167.UDF_long_ad)ad[i];
		int partrefernum = lad.getExtentLoc().getPartReferenceNumber().getIntValue();
		if(partrefernum == metapartnum && lad.getFlag() != UDF_AD.FLAG3){ // FLAG が3のときは例外
		    
		    err = new UDF_Error
			(category, UDF_Error.L_ERROR, "Partition Reference Number",
			 "File Entries describing file data (including streams) shall use either \"immediate\" allocation, " +
			 "or LONG_ADs which shall reference the physical or sparable partition referenced by the metadata partition, " +
			 "to describe the data space of the file.",
			 "2.2.13", String.valueOf(partrefernum), "");
		    err.setRName("ExtentLocation");
		    err.setRName("AllocationDescriptors[" + i + "]");
		    err.setGlobalPoint(lad.getGlobalPoint());
		    el.addError(err);
		}
	    }
	}
	
	if(com.udfv.ecma167.UDF_desc266.class.isAssignableFrom(fe.getClass()))
	    el.setRName("Extended File Entry");
	else
	    el.setRName("File Entry");
	
	el.setGlobalPoint(fe.getGlobalPoint());
	return el;
    }
    
    /**
       指定されたFile Entry を、Metadata (Mirror) File Entry として検証する。
       このクラスの派生クラスからのみ使用できる。
       
       @param fe    検証対象となるFile Entry エレメント。Extended File Entry も指定可能。
       @param flag  true  を指定すると、Metadata Main File Entry として検証を行う。
                    false を指定すると、Metadata Mirror File Entry として検証を行う。
       @return エラーインスタンス。
    */
    static protected UDF_ErrorList verifyMetadataFile(com.udfv.ecma167.UDF_desc261 fe, boolean flag) throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	UDF_Error ret;
	final short category = UDF_Error.C_UDF250;
	
	final String baseadstr = "The Allocation Descriptors(see 2.3.10) of these files shall at all times:\n";
	final String refer = "2.2.13.1";
	int filetype = (flag) ? 250 : 251;
	
	
	// ファイルタイプは250 または251
	ret = fe.getICBTag().verifyFileType(filetype);
	if(ret.isError()){
	    
	    ret.setCategory(UDF_Error.C_UDF250);
	    ret.setMessage("These files shall have the values of 250(main) and 251(Mirror) recorded in the IcbTag File Type fields of their File Entries.");
	    ret.setRefer(refer);
	    ret.setRName("ICBTag");
	    el.addError(ret);
	}
	
	// Unique Id は0でなければならない
	if(fe.getUniqueId().getLongValue() != 0){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Unique Id",
			 "The UniqueID field of these File Entries shall have a value of zero.",
			 refer, String.valueOf(fe.getUniqueId().getLongValue()), "0"));
	}
	
	// SHORT AD でなければならない
	ret = fe.getICBTag().verifyAdFlag(0);
	if(ret.isError()){
	    
	    ret.setCategory(category);
	    ret.setMessage(baseadstr + "Be SHORT_ADs(referencing space in the same physical/sparable partition in which the ICB resides).");
	    ret.setRefer(refer);
	    ret.setRName("ICBTag");
	    el.addError(ret);
	}
	
	    
	UDF_ElementBase[] member = fe.getAllocDesc().getChildren();
	long sumofadlen = 0;
	for (int i = 0, max = member.length; i < max; i++) {

	    String name = member[i].getName();
	    if(!name.equals("UDF_short_ad") && !name.equals("UDF_long_ad") && !name.equals("UDF_ext_ad"))
		continue;
	    
	    
	    // Flags は0または2でなければならない <= これは訂正
	    // 1(not recorded but allocated)であってはならない(DCN-5104)
	    UDF_AD ad2 = (UDF_AD)member[i];
	    int flags = ad2.getFlag();
	    if(flags == UDF_AD.FLAG1){
		
		ret = ad2.verifyFlag(0);
		ret.setCategory(UDF_Error.C_UDF250);
		ret.setMessage(baseadstr + "Not specify an extent of type \"allocated and recorded\".");
		ret.setRefer(refer);
		ret.setExpectedValue("except 1");
		ret.setRName("Allocation Descriptor[" + i + "]");
		el.addError(ret);
	    }
	    else if(flags != UDF_AD.FLAG2){
		
		sumofadlen += ad2.getLen();
	    }
	    
	    if(flags == 0 || flags == 2){
		
		// Extent Length は(Allocation Unit Size x LBS)倍でなければならない
		try{
		if(ad2.getLen() % (fe.env.getMetadataPartMap().getAllocUnitSize().getLongValue() * fe.env.LBS) != 0){
		    
		    ret = new UDF_Error
			(category, UDF_Error.L_ERROR, "Extent Length",
			 baseadstr + "Extents of type \'recorded and allocated\" or \"not allocated\" shall have " +
			 "an extent length that is an integer multiple of (Allocation Unit Size multiplied by logical block size).",
			 refer);
		    ret.setRName("Allocation Descriptor[" + i + "]");
		    el.addError(ret);
		}
		}
		catch(UDF_PartMapException e){
		    e.printStackTrace();
		}
	    }
	    
	    if(flags == 0){
		// Extent Position はAlignment Unit Size 倍の値でなければならない(DCN-5104)
		if(ad2.getLbn() % fe.env.getMetadataPartMap().getAlignmentUnitSize().getIntValue() != 0){
		
		    ret = new UDF_Error
			(category, UDF_Error.L_ERROR, "Extent Position",
			 baseadstr + "Extents of type \"recorded and allocated\" shall have a starting logical block number " +
			 "that is an integer multiple of the Alignment Unit Size specified in the Metadata Partition Map.",
			 refer);
		    ret.setRName("Allocation Descriptor[" + i + "]");
		    el.addError(ret);
		}
	    }
	}
	
	
	// Information Length はAD のExtent Length の合計と等しくなければならない(DCN-5104)
	if(fe.getInfoLen().getLongValue() != sumofadlen){
	    
	    el.addError(new UDF_Error
			(category ,UDF_Error.L_ERROR, "Information Length",
			 "The Information Length field of the File Entries for these files shall be equal to " +
			 "number of blocks described by the ADs for this stream * logical block size).",
			 refer, String.valueOf(fe.getInfoLen().getLongValue()), String.valueOf(sumofadlen)));
	}
	
	// Extended Attribute ICB はNULL でなければならない
	UDF_long_ad lad = fe.getExtendedAttrICB();
	if(lad.getLen() != 0 || lad.getExtentLoc().getLogicalBlockNumber().getLongValue() != 0 ||
	   lad.getExtentLoc().getPartReferenceNumber().getLongValue() != 0 || !UDF_Util.isAllZero(lad.getImplUse().getData())){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Extended Attribute ICB",
			 "The File Entries for the Metadata File and Metadata Mirror file shall have NULL " +
			 "Stream_Directory_ICB and Extended_Attribute_ICB fields.", refer));
	}
	
	// Stream Directory ICB はNULL でなければならない
	if(com.udfv.ecma167.UDF_desc266.class.isAssignableFrom(fe.getClass())){
	    
	    com.udfv.ecma167.UDF_desc266 efe = (com.udfv.ecma167.UDF_desc266)fe;
	    lad = efe.getStreamDirectoryICB();
	    if(lad.getLen() != 0 || lad.getExtentLoc().getLogicalBlockNumber().getLongValue() != 0 ||
	       lad.getExtentLoc().getPartReferenceNumber().getLongValue() != 0 || !UDF_Util.isAllZero(lad.getImplUse().getData())){
		
		el.addError(new UDF_Error
			    (category, UDF_Error.L_ERROR, "Stream Directory ICB",
			     "The File Entries for the Metadata File and Metadata Mirror file shall have NULL " +
			     "Stream_Directory_ICB and Extended_Attribute_ICB fields.", refer));
	    }
	}
	
	return el;
    }
    
    /**
       Metadata Bitmap File Entry の検証を行う。
       このクラスの派生クラスからのみ使用できる。
       
       @param fe  検証対象となるFile Entry エレメント。Extended File Entry も指定可能。
       @return エラーインスタンス。
    */
    static protected UDF_ErrorList verifyMetadataBitmapFile(com.udfv.ecma167.UDF_desc261 fe) throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	UDF_Error ret;
	final short category = UDF_Error.C_UDF250;
	
	final String refer = "2.2.13.2";
	
	
	// ファイルタイプは252
	ret = fe.getICBTag().verifyFileType(252);
	if(ret.isError()){
	    
	    ret.setCategory(UDF_Error.C_UDF250);
	    ret.setMessage("These files shall have the value of 252 recorded in the Icb Tag File Type fields of its File Entry.");
	    ret.setRefer(refer);
	    ret.setRName("ICBTag");
	    el.addError(ret);
	}
	
	// Unique Id は0でなければならない
	if(fe.getUniqueId().getLongValue() != 0){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Unique Id",
			 "The UniqueID field of this File Entry shall have a value of zero.",
			 refer, String.valueOf(fe.getUniqueId().getLongValue()), "0"));
	}
	
	// Descriptor CRC とその長さは0でなければならない
/*	if(env.metaSpaceBitmapDesc != null){
	    
	    el.addError(env.metaSpaceBitmapDesc.verifyForMetadata(category, refer));
	}
*/	
	// データ埋め込みでなければ・・・
	if((fe.getICBFlags() & 0x03) != 3){
	    
	    UDF_ElementBase[] member = fe.getAllocDesc().getChildren();
	    long sumofadlen = 0;
	    for (int i = 0, max = member.length; i < max; i++) {

		String name = member[i].getName();
		if(!name.equals("UDF_short_ad") && !name.equals("UDF_long_ad") && !name.equals("UDF_ext_ad"))
		    continue;
		
		
		// Flags に2 が含まれていてはならない
		UDF_AD ad2 = (UDF_AD)member[i];
		if(ad2.getFlag() == UDF_AD.FLAG2){
		    
		    ret = new UDF_Error
			(category, UDF_Error.L_ERROR, "Extent Length",
			 "The Allocation Descriptors for the Metadata Bitmap File shall not include any Allocation Descriptors of type \"not allocated\".",
			 refer);
		    ret.setRName("Allocation Descriptor[" + i + "]");
		    el.addError(ret);
		}
		else if(ad2.getFlag() == UDF_AD.FLAG0){  // Flags が0なので確実に記録されている領域
		
		    sumofadlen += ad2.getLen();
		}
	    }
	    
	    // Information Length はSBD のサイズと等しくなければならない（その１）
	    // AD が存在するときはFlag 0のAD のExtent Length の合計サイズがSBD のサイズとなる
	    if(fe.getInfoLen().getLongValue() != sumofadlen){
		
		el.addError(new UDF_Error
			    (category, UDF_Error.L_ERROR, "InformationLength",
			     "The Information Length field of the File Entry for this file shall equal the size of the SBD " +
			     "(NOTE: SBD size includes the bitmap portion).", refer,
			     String.valueOf(fe.getInfoLen().getLongValue()),
			     String.valueOf(sumofadlen)));
	    }
	}
	else{
	    
	    // Information Length はSBD のサイズと等しくなければならない（その２）
	    // 埋め込み時はAD のサイズ(Length of Allocation Descriptor)がSBD のサイズとなる
	    if(fe.getInfoLen().getLongValue() != fe.getLenOfAllocDesc().getLongValue()){
		
		el.addError(new UDF_Error
			    (category, UDF_Error.L_ERROR, "InformationLength",
			     "The Information Length field of the File Entry for this file shall equal the size of the SBD " +
			     "(NOTE: SBD size includes the bitmap portion).", refer,
			     String.valueOf(fe.getInfoLen().getLongValue()),
			     String.valueOf(fe.getLenOfAllocDesc().getLongValue())));
	    }
	}
	
	
	// Extended Attribute ICB はNULL でなければならない
	UDF_long_ad lad = fe.getExtendedAttrICB();
	if(lad.getLen() != 0 || lad.getExtentLoc().getLogicalBlockNumber().getLongValue() != 0 ||
	   lad.getExtentLoc().getPartReferenceNumber().getLongValue() != 0 || !UDF_Util.isAllZero(lad.getImplUse().getData())){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Extended Attribute ICB",
			 "The Metadata Bitmap File Entry shall have NULL " +
			 "StreamDirectoryICB(if extended FE) and ExtendedAttributeICB fields.", refer));
	}
	
	// Stream Directory ICB はNULL でなければならない
	if(com.udfv.ecma167.UDF_desc266.class.isAssignableFrom(fe.getClass())){
	    
	    com.udfv.ecma167.UDF_desc266 efe = (com.udfv.ecma167.UDF_desc266)fe;
	    lad = efe.getStreamDirectoryICB();
	    if(lad.getLen() != 0 || lad.getExtentLoc().getLogicalBlockNumber().getLongValue() != 0 ||
	       lad.getExtentLoc().getPartReferenceNumber().getLongValue() != 0 || !UDF_Util.isAllZero(lad.getImplUse().getData())){
		
		el.addError(new UDF_Error
			    (category, UDF_Error.L_ERROR, "Stream Directory ICB",
			     "The Metadata Bitmap File Entry shall have NULL " +
			     "StreamDirectoryICB(if extended FE) and ExtendedAttributeICB fields.", refer));
	    }
	}
	
	
	return el;
    }

    /*
      desc266にも同じものがある
     */
    public void recalc(short type, UDF_RandomAccess f){
	super.recalc(type, f);
	try{
	    if(type == RECALC_ENV){
		int filetype = getICBFileType();
		int fileflag = getAllocType();
		
		if(filetype == 252){
		    if(fileflag == 3){
			//env.metaPartSpaceBitmapDesc = (UDF_desc264)getAllocDesc().getFirstChild();
		    }
		    else if(env.getMetadataPartMap() != null){
			int ref_partno = env.getMetadataPartMap().getPartNumber().getIntValue();
		    }
		    else
			debugMsg(3, "desc261.recalc(): Metadata Partition Map is not loaded.");
		}
	    }
	}    
	catch(Exception e){
	    ignoreMsg("udf250.UDF_desc261:recalc", e);
	}
    }
}
    
