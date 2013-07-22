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
import java.util.zip.*;
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
import org.apache.xml.serialize.*;
import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.exception.*;
import com.udfv.access.*;
import com.udfv.ecma167.*;

/**
   UDF_Imageは UDF2.00に対応したイメージを操作するクラスである。
 */
public class UDF_Image extends com.udfv.udf150.UDF_Image
{
    public UDF_Image(){
	super();
    }

    public UDF_Image(Document document){
	super(document);
    }
    
    protected UDF_Extent parseTree(UDF_Element mother, int lbn, int partno, int subno, boolean sstream) throws ClassNotFoundException, UDF_Exception, IOException{
	debugMsg(3, "udf200:lbn=" + lbn);
	//debugMsg(3, "udf200:partno:" + partno + (mirror ? "(mirror)": "(main)"));
	debugMsg(3, "udf200:partno:" + partno + "(" + subno + ")");
	//現在のパーティションの UDF_Extentを取得

	//FEを入れるためのUDF_Extent作成
	UDF_Extent fe_ext = createFEExtent(lbn, partno, subno);
	UDF_RandomAccess ra = fe_ext.genRandomAccessExtent();

	UDF_FEDesc fe = UDF_FEDesc.genFEDesc(ra, fe_ext, null, null);
	fe.readFrom(ra);
	UDF_pad fe_pad = new UDF_pad(this, null, null, env.LBS);
	fe_pad.readFrom(ra);

	mother.appendChild(fe_ext);

	fe_ext.appendChild(fe);
	fe_ext.appendChild(fe_pad);

	// 266ならば system stream icbをチェック
	if(com.udfv.ecma167.UDF_desc266.class.isAssignableFrom(fe.getClass())){
	    com.udfv.ecma167.UDF_desc266 fe266 = (com.udfv.ecma167.UDF_desc266)fe;
	    UDF_AD ad = fe266.getStreamDirectoryICB();
	    if(ad.getLen() > 0){
		UDF_Extent stream_ext = parseTree(mother, ad.getLbn(), ad.getPartRefNo(), ad.getPartSubno(), true);
		String slabel = UDF_Label.genExtentLabel(stream_ext);
		stream_ext.setAttribute("id", slabel);
		ad.setRefAttribute(slabel);
	    }
	}

	int file_type = fe.getICBFileType();
	int file_flags = fe.getICBFlags() & 0x7;

	if(file_type == UDF_icbtag.T_DIRECTORY || file_type == UDF_icbtag.T_SDIRECTORY){
	    parseTreeDirectory(mother, fe, partno, subno, sstream);
	}
	else if(file_type == UDF_icbtag.T_FILE || file_type == UDF_icbtag.T_RTFILE){
	    parseTreeFile(mother, fe, partno, subno);
	}
	else if(file_type == UDF_icbtag.T_SYMLINK){
	    
	    // シンボリックリンクを追う（動作未確認）
	    readSymLink(mother, fe, file_flags, partno);
	    //env.numberOfFiles++;
	}
	else
	    throw new UDF_ICBException(fe, "unknown icb file type:" + file_type);

	return fe_ext;
    }

    public long readVolume(UDF_RandomAccess f){
	
	try{
	    f.seek(16 * UDF_Env.LBS);
	    readVRS(f);

	    // 256 => N-256 => N-1 の順にAVDP を読む。1つでも読めたらおしまい
	    if(!readAVDPinVR(f))
		return 0;
	    
	    boolean mvds = readMVDS(f);
	    boolean rvds = readRVDS(f);
	    if(!mvds && !rvds)
		throw new UDF_DataException(this, "Neither MVDS nor RVDS is not recorded.");
	}
	catch(Exception e){
	    e.printStackTrace();
	    return 0;
	}
	try{
	    readLVIS(f);
	    readECMA119FileSystem(f);

	    //　Partition Maps関連の追加　//
	    readPartMap();

	    //　"Partition Header Descriptor"."UnallocatedSpaceBitmap"."SpaceBitmapDescriptor"　//
	    readPartHeaderDesc();
	    
	    long loc = 0;

	    /*
	    // N - 257 AVDP
	    if(!env.exist_avdp[1]){
		
		loc = f.seek(-((256 + 1) * env.LBS), UDF_RandomAccess.SEEK_END);
		env.exist_avdp[1] = readAVDP(f, (int)(loc / env.LBS), 1);
	    }
	    */

	    // n - 17 VRS
	    loc = f.seek(-((16 + 1) * env.LBS), UDF_RandomAccess.SEEK_END);
	    readVRS(f);

	    /*
	    // N - 1 AVDP
	    if(!env.exist_avdp[2]){
		
		loc = f.seek(-env.LBS, UDF_RandomAccess.SEEK_END);
		env.exist_avdp[2] = readAVDP(f, (int)(loc / env.LBS), 2);
	    }
	    */
	}
	catch(Exception e){
	    e.printStackTrace();
	}
	
	return 0;
    }

    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	final short category = UDF_Error.C_UDF200;
	
	
	if(env.udf_revision != 0x200)
	    return super.verify();
	
	// VRS は必須
	if(env.vrs1 == null){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Volume Recognition Sequence",
			 "The Volume Recognition Sequence as described in part 2 of ECMA 167 shall be recorded.",
			 "2"));
	}
	
	el.addError(verifyBase(category));
	el.addError(super.verify());
	
	return el;
    }
    
    /**
       UDF 2.00 以降のリビジョンで共通して使用できる検証用関数。
       
       @param category エラーカテゴリ。
       @return エラーリストインスタンス。
    */
    public UDF_ErrorList verifyBase(short category) throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	el.addError(super.verifyBase(category));
	
	try{
	    int stream_lbn = env.getFileSetDesc(getPartSubno()).getSystemStreamDirectoryICB().getLbn();
	    long stream_len = env.getFileSetDesc(getPartSubno()).getSystemStreamDirectoryICB().getLen();
	    // ルートディレクトリからツリーの検証
	    el.addError(verifyDirTree(null, category, true));

	    // System Stream Directory の検証
	    if(stream_lbn != 0 && stream_len != 0)
		el.addError(verifyStreamDirTree(null, category, true));
	}
	catch(Exception e){
	    
	    e.printStackTrace();
	}
	
	return el;
    }
    
    //_/_/ Protected /_/_/
    
    /**

       Directory ツリーをパースしながら、その構成等を検証する。
       ROOTを検証したいときは FIDに nullを渡す
       
       @param	fid	FID

       @return エラーリストインスタンス。
    */
    public UDF_ErrorList verifyDirTree(com.udfv.ecma167.UDF_desc257 fid, short category, boolean system) throws ClassNotFoundException, UDF_Exception, IOException{
	UDF_FEDesc fe = null;
	if(fid == null)
	    fe = env.getRootFE(0);//root_fe;
	else
	    fe = fid.getReferenceTo();
	

	int file_type = fe.getICBFileType();
	int file_flags = fe.getICBFlags() & 0x7;
	UDF_ErrorList el = new UDF_ErrorList();
	byte[] fileid = null;
	if(fid != null)
	    fileid = fid.getFileId().getData();

	if(file_type == UDF_icbtag.T_SDIRECTORY){
	    UDF_ElementList fids = fe.getDirectoryList();
	    
	    /*ここはイメージから読みとるようにしなくては……
	    if(com.udfv.ecma167.UDF_desc266.class.isAssignableFrom(fe.getClass())){
		
		// Stream Directory の検証
		com.udfv.ecma167.UDF_desc266 efe = (com.udfv.ecma167.UDF_desc266)fe;
		long length  = efe.getStreamDirectoryICB().getExtentLen().getLongValue();
		int  slbn    = efe.getStreamDirectoryICB().getExtentLoc().getLogicalBlockNumber().getIntValue();
		int  spartno = efe.getStreamDirectoryICB().getExtentLoc().getPartReferenceNumber().getIntValue();
		
		if(length != 0)
		    el.addError(verifyStreamDirTree(slbn, spartno, mirror, category, new byte[0], false));
	    }
	    */

	    for(int i=0 ; i<fids.size() ; ++i){
		com.udfv.ecma167.UDF_desc257 child_fid = (UDF_desc257)fids.elementAt(i);
		int filechar = child_fid.getFileChar().getIntValue();
		if((filechar & UDF_desc257.PARENT) != 0)
		    continue;
		else if((filechar & UDF_desc257.DELETED) != 0)
		    continue;
		else
		    el.addError(verifyDirTree(child_fid, category, system));
	    }
	}
	else if(file_type == UDF_icbtag.T_FILE){
	}
	return el;
    }

    /**
       Stream Directory ツリーをパースしながら、その構成等を検証する。
       ROOTを検証したいときは FIDに nullを渡す。
       
       @param fid      FID。
       @param category エラーカテゴリ。
       @param system   System Stream Directory かどうかを示すフラグ。

       @return エラーリストインスタンス。
    */
    public UDF_ErrorList verifyStreamDirTree(com.udfv.ecma167.UDF_desc257 fid, short category, boolean system) throws ClassNotFoundException, UDF_Exception, IOException{
	UDF_FEDesc fe = null;
	if(fid == null)
	    fe = env.getSRootFE(0);//sroot_fe;
	else
	    fe = fid.getReferenceTo();

	int file_type = fe.getICBFileType();
	int file_flags = fe.getICBFlags() & 0x7;
	UDF_ErrorList el = new UDF_ErrorList();
	byte[] fileid = null;
	if(fid != null)
	    fileid = fid.getFileId().getData();

	String fename = (com.udfv.ecma167.UDF_desc266.class.isAssignableFrom(fe.getClass())) ?
	    "Extended File Entry" : "File Entry";

	if(file_type == UDF_icbtag.T_SDIRECTORY){
	    
	    // System Stream Root Directory 以外のStream Directory は、１つのFID から指されていなければならない
	    if(!system){
		
		if(fe.getReferencedFID().size() != 1){
		    UDF_Error err = new UDF_Error(category, UDF_Error.L_ERROR, fename + "(Stream Directory)",
						  "Each Stream Directory ICB shall be identified by exactly one Stream Directory ICB filed. " +
						  "[no hard links to stream directories].", "3.3.5.1");		    
		    el.addError(err);
		}
	    }
	    
	    // Stream bit が立っているときは、1つ以上のFID から指されているハズである
	    // （ROOT はFID から指されていない）
	    int flags = fe.getICBFlags();
	    if((flags & 0x2000) != 0){
		
		// ディレクトリのときは子供が親を指しているので、
		// 少なくともこのFEを指すFIDは1つ存在する。1つしかなければ、他のどのFIDからも指されていない。
		// ECMA では'identified by one or more FID' と書かれており、
		// 文面をそのまま受け止めれば子供が親を指すためのFID もFID に含まれそうだが、
		// Philips のverifer でこの個所はエラーとして出力されるため（但し理由は明示されていない）、
		// ここでは子供のFID は数に入れていない。
		if(fe.getReferencedFID().size() <= 1){ // == 0){
		    
		    UDF_Error err = new UDF_Error
			(UDF_Error.C_ECMA167, UDF_Error.L_WARNING, "Flags",
			 "If this bit set to ONE, the file is a stream identified by one or more " +
			 "File Identifier Descriptors in a stream directory(see 4/9.2)",
			 "4/14.6.8", flags, (flags ^ 0x2000));
		    
		    err.setGlobalPoint(fe.getGlobalPoint());
		    err.setRName("ICBTag");
		    err.setRName(fename + "(Stream Directory)");
		    el.addError(err);
		}
	    }
	    
	    UDF_ElementList fids = fe.getDirectoryList();
	    
	    for(int i=0 ; i<fids.size() ; ++i){
		com.udfv.ecma167.UDF_desc257 child_fid = (UDF_desc257)fids.elementAt(i);
		int filechar = child_fid.getFileChar().getIntValue();
		if((filechar & UDF_desc257.PARENT) != 0)
		    continue;
		else if((filechar & UDF_desc257.DELETED) != 0)
		    continue;
		else
		    el.addError(verifyStreamDirTree(child_fid, category, system));
	    }
	}
	else if(file_type == UDF_icbtag.T_FILE){
	    // Non-Allocatable Space Stream
	    if(UDF_Util.cmpBytesString(fileid, 1, "*UDF Non-Allocatable Space")){
		el.addError(UDF_desc261.verifyNonAllocSpace((com.udfv.ecma167.UDF_desc261)fe, category, system));
	    }
	    // Power Calibration Stream
	    else if(UDF_Util.cmpBytesString(fileid, 1, "*UDF Power Cal Table")){
		if(!system){
		    UDF_Error err = new UDF_Error
			(category, UDF_Error.L_ERROR, fename + "(Power Calibration Stream)",
			 "The Power Calibration Table may be recorded as a system stream of the File Set Descriptor " +
			 "according to the rules of 3.3.5.", "3.3.7.3");
		    err.setGlobalPoint(fe.getGlobalPoint());
		    el.addError(err);
		}
	    }
	}
	else{
	    
	    UDF_Error err = new UDF_Error
		(category, UDF_Error.L_ERROR, "File Type",
		 "(Extended) File Entry in Stream directory, but this File type of ICB is neither 5 nor 13. " +
		 "The ICB for a Named Stream directory shall have a file type of 13. " +
		 "All named streams shall have a file type of 5.",
		 "3.3.5.1", String.valueOf(file_type), "");
	    err.setRName("ICBTag");
	    err.setRName(fename);
	    err.setGlobalPoint(fe.getGlobalPoint());
	    el.addError(err);
	}
	
	return el;
    }

    /**
       VATを読み込む。

       UDF1.50で規定されている VATと
       UDF2.00以上で規定されている VATは根本的に構造が違うので注意が必要。

       @param f	アクセサ
       @param lbn	位置
       @param size	長さ
       @param previous	previous VATか？
    */
    protected void readVAT(UDF_RandomAccess f, int lbn, int partrefno, long size, boolean previous) throws UDF_Exception, IOException, ClassNotFoundException{
	try{
	    int partno = env.getPartDescByPartRefNo(UDF_Env.VDS_AUTO, partrefno).getPartNumber().getIntValue();
	    int part_loc = env.getPartStartingLoc(partno);
	    int loc = lbn + part_loc;
	    if(loc * UDF_Env.LBS + size > f.length())
		return;

	    debugMsg(1, "VAT lbn=" + lbn + " size=" + size);
	    UDF_Extent vat_ext = createExtent(null, null);
	    vat_ext.addExtent(lbn, partrefno, size);
	    UDF_RandomAccessExtent vat_rae =  vat_ext.genRandomAccessExtent();
	    UDF_FEDesc fe = null;
	    fe = UDF_FEDesc.genFEDesc(vat_rae, vat_ext, null, null);
	    fe.readFrom(vat_rae);

	    UDF_RandomAccess rab = fe.getAllocDesc().genRandomAccessBytes();
	    UDF_VirtualAllocTable200 vat = (UDF_VirtualAllocTable200)createElement("UDF_VirtualAllocTable200", null, null);

	    if(fe.getAllocType() == 3){
		vat.setHintSize((int) rab.length());
		fe.getAllocDesc().readFromAndReplaceChild(vat);
	    }
	    else{
		UDF_Extent vat_data_ext = createExtent(null, null);
		rab = vat_data_ext.genRandomAccessExtent();
		fe.setADToExtent(vat_data_ext, 0);
		vat.setHintSize((int)rab.length());
		
		vat.readFrom(rab);
		vat_data_ext.appendChild(vat);
		env.root.appendChild(vat_data_ext);
	    }
	    
	    env.root.appendChild(vat_ext);
	    vat_ext.appendChild(fe);

	    // Virtual Partition Map の番号を調べる
	    int vatpartnum = -1;
	    for(int i = 0; i < env.getPartMapList().size() ; i++){
	    
		if(com.udfv.udf150.UDF_VirtualPartMap.class.isAssignableFrom(env.part[i].getClass())){
		    
		    vatpartnum = i;
		    break;
		}
	    }
	    
	    //adを置き換える
	    UDF_Extent ext = null;
	    
	    if(previous){
		
		ext = new UDF_Extent(this, null, "part");
		long part_size = env.getPartMapExtent(vatpartnum, 0).getLongSize();
		ext.addExtent(part_loc, -1, part_size);
	    }
	    else
		ext = env.getPartMapExtent(vatpartnum, 0);
	    
	    
	    rab.seek(2);
	    int  sec = 0;
	    long l_ui = rab.readUint16();
            com.udfv.udf150.UDF_VirtualPartMap vpm = (com.udfv.udf150.UDF_VirtualPartMap)env.part[vatpartnum];
            
	    rab.seek(152 + l_ui);
	    while(!rab.eof() && rab.getPointer() < rab.length()){
		//UDF_uint32 u = new UDF_uint32(this, null, null);
		long u = rab.readUint32();
		if(u != 0xffffffffL)
		    ext.spareExtent(sec, -1, (int)(u + part_loc), -1, UDF_Env.LBS, vpm.getPartNumber().getIntValue());
		
		++sec;
	    }
	    
	    ext.normalize();
	    getVatExtent().add(ext);
	    
	    UDF_uint32 previous_vat = vat.getPreviousVATICBLocation();
	    if(previous_vat != null && previous_vat.getIntValue() != 0xffffffff){
		debugMsg(1, "previous VAT:" + previous_vat.getIntValue());
		
//		int preloc = previous_vat.getIntValue() + env.partDesc.getPartStartingLoc().getIntValue();
//		readVAT(f, previous_vat.getIntValue(), UDF_Env.LBS, true);
		//とりあえずのコード
		int preloc = previous_vat.getIntValue();// + env.getPartDescByPartRefNo(UDF_Env.VDS_AUTO, 0).getPartStartingLoc().getIntValue();
		readVAT(f, preloc, 0, UDF_Env.LBS, true);
	    }

	}
	catch(UDF_EOFException e){
	    ;
	}
    }
    
    /*
       使用するパッケージの優先順位を設定する。
    protected void setPkgPriority(){
	setPkgPriority(0x200);
    }
    */
    
}
