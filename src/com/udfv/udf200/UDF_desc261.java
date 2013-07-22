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

public class UDF_desc261 extends com.udfv.udf150.UDF_desc261
{
    /**
       @param elem 親
       @param name 名前
    */
    public UDF_desc261(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? "UDF_desc261" : name);	
    }
    
//begin:add your code here
    public UDF_ErrorList verify() throws UDF_Exception{
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	if(env.udf_revision != 0x200)
	    return super.verify();

	el.addError(verifyBase(this, UDF_Error.C_UDF200));
	// Stream Data を指すのにノーマルなFile Entry を使うべきではない。
	if(getICBFileType() == UDF_icbtag.T_SDIRECTORY){
	    
	    el.addError(new UDF_Error
			(UDF_Error.C_UDF200, UDF_Error.L_CAUTION, "",
			 "ECMA 167 3rd edition defines a new File Entry that contains a field for identifying a stream directory. " +
			 "This new File Entry should be used in place of the old File Entry, " +
			 "and should be used for describing the streams themselves.",
			 "3.3.5.1"));
	}

	el.setRName("File Entry");
	
	el.setGlobalPoint(getGlobalPoint());
	el.addError(super.verify());
	
	return el;
    }
    
    /**
       UDF 2.00 以降の規格に準じたFile Entryの共通部分の検証を行う。
       
       @param fe       検証対象となるFile Entry インスタンス。
       @param category エラーカテゴリ。
       
       @return エラーリストインスタンス。
    */
    static public UDF_ErrorList verifyBase(com.udfv.ecma167.UDF_desc261 fe, short category) throws UDF_Exception {

	UDF_ErrorList el = new UDF_ErrorList();
	
	// UDF 2.00 以前の共通のエラー検証
	el.addError(com.udfv.udf150.UDF_desc261.verifyBase(fe, category));
	
	// 埋め込みデータのとき、Logical Blocks Recorded は0 でなければならない
	if((fe.getICBFlags() & 0x03) == 0x03){

	    long logicalBlocksRecorded = fe.getLogicalBlocksRecorded().getLongValue();
	    if(logicalBlocksRecorded != 0){

		el.addError(new UDF_Error
			    (category, UDF_Error.L_ERROR, "LogicalBlocksRecorded",
			     "For files and directories with embedded data the value of this field shall be ZERO.",
			     "2.3.6.5", logicalBlocksRecorded, 0));
	    }
	}

	int root_lbn = fe.env.getFileSetDesc(fe.getPartSubno()).getRootDirectoryICB().getLbn();
	
	// root ディレクトリのFE であるとき、UniqueID は0 でなければならない
	if(root_lbn == fe.getDescTag().getTagLoc().getIntValue()){

	    long uniqueId = fe.getUniqueId().getLongValue();
	    if(uniqueId != 0){

		el.addError(new UDF_Error
			    (category, UDF_Error.L_ERROR, "UniqueID",
			     "For the root directory of a file set this value shall be set to ZERO.",
			     "2.3.6.7", uniqueId, 0));
	    }
	}

	int flags = fe.getICBFlags();
	int ftype = fe.getICBFileType();
	
	
	// Stream Data のとき
/*
	if((flags & 0x2000) != 0){
	    
	    // ハードリンクがあってはならない（ファイルリンクカウントは1でなければならない）
	    // （ただし、通常のファイルへのハードリンクをStream Data として作成するのは許されているらしい）
	    if(fe.getFileLinkCount().getIntValue() != 1){
		
		el.addError(new UDF_Error
			    (category, UDF_Error.L_ERROR, "File Link Count",
			     "Each named stream shall be identified by exactly one FID in exactly one Stream Directory. " +
			     "[no hard links among named streamms or files and named streams]",
			     "3.3.5.1", String.valueOf(fe.getFileLinkCount().getIntValue()), "1"));
	    }
	    
	    // ファイルタイプは5でなければならない
	    UDF_Error err = fe.getICBTag().verifyFileType(5);
	    if(err.isError()){
		
		err.setRName("ICB Tag");
		err.setCategory(category);
		err.setMessage("All named streams shall have a file type of 5.");
		err.setRefer("3.3.5.1");
		el.addError(err);
	    }
	}
*/	
	// Stream Data またはStream Directory のとき
	if((flags & 0x2000) != 0 || ftype == UDF_icbtag.T_SDIRECTORY){
	    
	    // Extended Attribute を持っていてはならない（条件が長いので2つに分ける）。
	    // Extended Attribute ICB を持っていてはならない、または・・・
	    com.udfv.ecma167.UDF_long_ad lad = (com.udfv.ecma167.UDF_long_ad)fe.getExtendedAttrICB();
	    if(lad.getExtentLen().getIntValue() != 0 || lad.getExtentLoc().getLogicalBlockNumber().getIntValue() != 0
	       || lad.getExtentLoc().getPartReferenceNumber().getIntValue() != 0 || !UDF_Util.isAllZero(lad.getImplUse().getData())){
		
		el.addError(new UDF_Error
			    (category, UDF_Error.L_ERROR, "ExtendedAttributeICB",
			     "Named Streams and Stream Directories shall not have Extended Attributes",
			     "3.3.5.1"));
	    }
	    // ・・・L_EA が0ではない、もしくはExtended Attribute フィールドに何かが書かれている
	    else if(fe.getLenOfExtendedAttr().getIntValue() != 0 || !UDF_Util.isAllZero(fe.getExtendedAttr().getData())){
		
		el.addError(new UDF_Error
			    (category, UDF_Error.L_ERROR, "ExtendedAttribute",
			     "Named Streams and Stream Directories shall not have Extended Attributes",
			     "3.3.5.1"));
	    }
	}
	
	UDF_ElementList elemlist = fe.getReferencedFID();
	int linkct = fe.getFileLinkCount().getIntValue();
	for(int i = 0; i < elemlist.size(); i++){
	    
	    com.udfv.ecma167.UDF_desc257 fid = (com.udfv.ecma167.UDF_desc257)elemlist.elementAt(i);
	    if((fid.getFileChar().getIntValue() & 0x08) != 0)
		linkct--;
	}
	
	for(int i = 0; i < elemlist.size(); i++){
	    
	    com.udfv.ecma167.UDF_desc257 fid = (com.udfv.ecma167.UDF_desc257)elemlist.elementAt(i);
	    if((fid.getFileChar().getIntValue() & 0x08) != 0)
		continue;
	    
	    long  fiduid = UDF_Util.b2uint32(fid.getICB().getImplUse().getData(), 2);
	    long  feuid = (fe.getUniqueId().getLongValue() & 0x00000000ffffffff);
	    short level = UDF_Error.L_NOERR;
	    
	    // このFE を指しているFID が（親ディレクトリを指すものを除いて）1つの場合、
	    // そのFID のUniqueID と、FE のUniqueID の下位32ビットが一致しなければならない
	    if(linkct == 1){
		
		if(fiduid != feuid)
		    level = UDF_Error.L_ERROR;
	    }
	    // このFE を指しているFID が（親ディレクトリを指すものを除いて）2つ以上存在する場合、
	    // そのFID のUniqueID は、少なくともFE のUniqueID の下位32ビット以上の値である
	    else if(fiduid < feuid){
		level = UDF_Error.L_CAUTION;
	    }
	    
	    if(level != UDF_Error.L_NOERR){
		
		el.addError(new UDF_Error
			    (category, level, "UniqueID",
			     "The lower 32-bits of UniqueID shall be the same in the File Entry/Extended File Entry and its first " +
			     "File Identifier Descriptor, but the shall differ in subsequent FIDs.",
			     "3.2.1.1", String.valueOf(feuid), String.valueOf(fiduid)));
	    }
	}
	
	return el;
    }
    
    /**
       File Entry をNon-Allocatable Space Stream のFE として検証する。
       
       @param fe       検証対象となるFile Entry インスタンス。
       @param category エラーカテゴリ。
       @param system   System Stream Directory かどうかを示すフラグ。
       
       @return エラーリストインスタンス。
    */
    static public UDF_ErrorList verifyNonAllocSpace(com.udfv.ecma167.UDF_desc261 fe, short category, boolean system) throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	// System Stream Direcotry のStream Data でなければならない
	if(!system){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "",
			 "The Non-Allocatable Space Stream shall be recorded as a named stream in " +
			 "the system stream directory of the File Set Descriptor.",
			 "3.3.7.2"));
	}
	
	// System bit が立っていなければならない
	int flags = fe.getICBFlags();
	if((flags & 0x400) == 0){  // System bit
	    
	    UDF_Error err = new UDF_Error
		(category, UDF_Error.L_ERROR, "Flags",
		 "The stream shall be marked with the attributes System (bit 10 of ICB flags field set to ONE).",
		 "3.3.7.2", String.valueOf(flags), String.valueOf(flags | 0x0400));
	    err.setRName("ICBTag");
	    el.addError(err);
	}
	
	// AD のタイプは全て1 でなければならない
	UDF_ElementBase[] ad = fe.getAllocDesc().getChildren();
	for (int i = 0, len = ad.length; i < len; i++) {

	    String name = ad[i].getName();
	    if(!name.equals("UDF_short_ad") && !name.equals("UDF_long_ad") && !name.equals("UDF_ext_ad"))
		continue;
		
	    UDF_AD ad2 = (UDF_AD)ad[i];
	    UDF_Error err = ad2.verifyFlag(UDF_AD.FLAG1);
	    if(err.isError()){
		
		err.setCategory(category);
		err.setRName("AllocationDescriptors[" + i + "]");
		err.setMessage("The allocation descriptors shall have allocation type 1(allocation but not recorded).");
		err.setRefer("3.3.7.2");
		el.addError(err);
	    }
	}

	if(com.udfv.ecma167.UDF_desc266.class.isAssignableFrom(fe.getClass()))
	    el.setRName("Extended File Entry(Non-Allocatable Space Stream)");
	else
	    el.setRName("File Entry(Non-Allocatable Space Stream)");
	
	el.setGlobalPoint(fe.getGlobalPoint());
	return el;
    }
    
//end:
};
