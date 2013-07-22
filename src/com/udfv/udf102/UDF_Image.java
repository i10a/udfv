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
package com.udfv.udf102;

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
import com.udfv.ecma119.*;

/**
   UDF_Imageは UDF1.02に対応したイメージを操作するクラスである。
 */
public class UDF_Image extends com.udfv.core.UDF_Image
{
    public UDF_Image(){
	super();
    }
    
    public UDF_Image(Document document){
	super(document);
    }

    /**
       ファイルシステムを読み込む。
       
       @param f         イメージアクセサ。
       @param subno	副番号。

       @return 常に0を返す。
     */
    public long readFileSystem(UDF_RandomAccess f, int subno) throws IOException, ClassNotFoundException, UDF_VolException, UDF_Exception{
	readFSDS(f, subno);

	int root_lbn = env.getFileSetDesc(subno).getRootDirectoryICB().getLbn();
	int root_partno = env.getFileSetDesc(subno).getRootDirectoryICB().getPartRefNo();
	long root_len = env.getFileSetDesc(subno).getRootDirectoryICB().getLen();
	int stream_lbn = env.getFileSetDesc(subno).getSystemStreamDirectoryICB().getLbn();
	int stream_partno = env.getFileSetDesc(subno).getSystemStreamDirectoryICB().getPartRefNo();
	long stream_len = env.getFileSetDesc(subno).getSystemStreamDirectoryICB().getLen();
	
	UDF_Extent ext = parseTree(this, root_lbn, root_partno, subno, false);
	env.setRootFE(subno, (UDF_FEDesc)(ext.getFirstChild()));
	
	// parse tree
	if(stream_lbn == 0 && stream_partno == 0 && stream_len == 0)
	    ;
	else{
	    ext = parseTree(this, stream_lbn, stream_partno, subno, true);
	    env.setSRootFE(subno, (UDF_FEDesc)(ext.getFirstChild()));
	}

	return 0;
    }
    /**
       ファイルシステムを読み込む。
       
       @param f         イメージアクセサ。
       @param mirror	ミラーかどうか。

       @return 常に0を返す。

       @deprecated replaced by {@link #readFileSystem(UDF_RandomAccess, int)}
     */
    public long readFileSystem(UDF_RandomAccess f, boolean mirror) throws IOException, ClassNotFoundException, UDF_VolException, UDF_Exception{
	return readFileSystem(f, mirror ? 1 : 0);
    }

    /**
       @param f
     */
    protected void readECMA119FileSystem(UDF_RandomAccess f) throws IOException, UDF_DataException, UDF_IOException, UDF_Exception{
	if(env.ecma119_PrimaryVolumeDesc == null)
	    return;

	UDF_ECMA119_CD001_1 cd001_1 = env.ecma119_PrimaryVolumeDesc;
	readCD9660PathTable(cd001_1.getLocOfOccurenceOfTypeLPathTable().getLongValue(),
			    cd001_1.getPathTableSize().getLongValue(),
			    true); 
	readCD9660PathTable(cd001_1.getLocOfOccurenceOfTypeMPathTable().getLongValue(),
			    cd001_1.getPathTableSize().getLongValue(),
			    false);
	readCD9660DirRec(
			 cd001_1.getDirectoryRecordForRootDirectory().getLocOfExtent().getLongValue(),
			 cd001_1.getDirectoryRecordForRootDirectory().getDataLen().getLongValue(),
			 -1);

	//副ボリュームがあるか？
	if(env.ecma119_SupplementaryVolumeDesc.size() > 0){
	    for(int i=0 ; i<env.ecma119_SupplementaryVolumeDesc.size() ; ++i){
		UDF_ECMA119_CD001_2 cd001_2 = (UDF_ECMA119_CD001_2)env.ecma119_SupplementaryVolumeDesc.elementAt(i);
		readCD9660PathTable(cd001_2.getLocOfOccurenceOfTypeLPathTable().getLongValue(),
				    cd001_2.getPathTableSize().getLongValue(),
				    true); 
		readCD9660PathTable(cd001_2.getLocOfOccurenceOfTypeMPathTable().getLongValue(),
				    cd001_2.getPathTableSize().getLongValue(),
				    false);
		readCD9660DirRec(
				 cd001_2.getDirectoryRecordForRootDirectory().getLocOfExtent().getLongValue(),
				 cd001_2.getDirectoryRecordForRootDirectory().getDataLen().getLongValue(),
				 -1);
	    }
	}
    }

    /**
       ボリューム情報を読み込みます。
       
       @param f       イメージアクセサ。
       @return 0を返します。
    */
    public long readVolume(UDF_RandomAccess f){
	
	try{
	    f.seek(16 * env.LBS);
	    readVRS(f);
	    
	    // 16 => N-256 => N-1 の順にAVDP を読む。1つでも読めたらおしまい
	    if(!readAVDPinVR(f))
		return 0;
	    
	    boolean mvds = readMVDS(f);
	    boolean rvds = readRVDS(f);
	    if(!mvds && !rvds)
		throw new UDF_DataException(this, "Neither MVDS nor RVDS is not recorded.");
	    readLVIS(f);

	    readECMA119FileSystem(f);

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
	final short category = UDF_Error.C_UDF102;
	
	
	if(env.udf_revision != 0x102){
	    return super.verify();
	}
	
	
	el.addError(verifyBase(category));
	el.addError(super.verify());
	
	return el;
    }
    
    /**
       UDF の全リビジョンで共通して使用できる検証用関数。
       
       @param category エラーカテゴリ。
       @return エラーリストインスタンス。
    */
    public UDF_ErrorList verifyBase(short category) throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	boolean exist_vrs = false;
	
	
	UDF_ElementBase[] ebl = getChildren();
	for(int i = 0; i < ebl.length; i++){
	    
	    String name = ebl[i].getName();
	    if(name.equals("vrs"))
		exist_vrs = true;
//	    System.err.println("name = " + name);
	    UDF_Extent ext = (UDF_Extent)ebl[i];
	    UDF_ExtentElem[] eelem = ext.getExtent();
	    for(int j = 0; j < eelem.length; j++){
		
		if(eelem[j].getPartRefNo() == -1 && eelem[j].getLoc() < (32768 / env.LBS)){
		
		    UDF_Error err = new UDF_Error
			(category, UDF_Error.L_ERROR, "",
			 "The first 32768 bytes of the Volume space shall not be used for the recording of ECMA 167 structures. " +
			 "This area shall not be referenced by the Unallocated Space Descriptor or any other ECMA 167 descriptor.",
			 "2");
		
		    err.setGlobalPoint(eelem[j].getLoc());
		    el.addError(err);
//		    System.err.println("name = " + ebl[i].getName());
		}
	    }
	}
	
	// VRSが全く記録されていない(2005/09/15:追加 by seta)
	if(!exist_vrs){
	    
	    UDF_Error err = new UDF_Error
		(category, UDF_Error.L_ERROR, "Volume Recognition Sequence",
		 "The Volume Recognition Sequence as described in part 2 of ECMA167 shall be recorded.", "2");
	    el.addError(err);
	}
	
	// AVDP の数の検証
	el.addError(verifyAVDPNum(category, "2.2.3"));


	//Bitmapの検証を抜本的に書きかえてみた。
/*
	// Metadata の範囲をBitmap に含める
	Vector rc = new Vector();
	UDF_ElementBase[] child = env.root.getChildren();
	
	for(int i = 0; i < child.length; i++)
	    rc.add(child[i]);

	com.udfv.ecma167.UDF_desc261  fe = null;
	com.udfv.ecma167.UDF_desc261 mfe = null;
	try {
	    fe = findMetadataFile(261, com.udfv.udf250.UDF_icbtag.T_METADATA_FILE);
	}
	catch(UDF_InternalException e) {
	    try {
		fe = findMetadataFile(266, com.udfv.udf250.UDF_icbtag.T_METADATA_FILE);
	    }
	    catch(UDF_InternalException ee) {
	    }
	}

	try {
	    mfe = findMetadataFile(261, com.udfv.udf250.UDF_icbtag.T_METADATA_MIRROR);
	}
	catch(UDF_InternalException e) {
	    try {
		mfe = findMetadataFile(266, com.udfv.udf250.UDF_icbtag.T_METADATA_MIRROR);
	    }
	    catch(UDF_InternalException ee) {
	    }
	}

	if (fe != null) {
	    UDF_AD[] ad = fe.getAD();
	    UDF_Extent main_ext = (UDF_Extent)createElement("UDF_Extent", null, null);
	    main_ext.addExtent(ad);
	    rc.add(main_ext);
	}

	com.udfv.udf250.UDF_MetadataPartMap mpm = env.metaDataPartMap;

	if (mfe != null && mpm.getMetadataFileLoc().getIntValue() != mpm.getMetadataMirrorFileLoc().getIntValue()) {
	    UDF_AD[] ad = mfe.getAD();
	    UDF_Extent mirror_ext = (UDF_Extent)createElement("UDF_Extent", null, null);
	    mirror_ext.addExtent(ad);
	    rc.add(mirror_ext);
	}
		
	UDF_ElementBase[] part0 = new UDF_ElementBase[rc.size()];
	for(int i = 0; i < rc.size(); i++){
	    
	    part0[i] = (UDF_ElementBase)rc.elementAt(i);
	}
	
	// Space Bitmap の検証
	if(env.spaceBitmapDesc != null)
	    el.addError(env.spaceBitmapDesc.verifyBitmap(part0, 0, category));
*/
	// Space Bitmap の検証改
	//PartHeaderDesc1つ1つに対してチェックをする。
	//(通常は1つ)
	UDF_ElementList pdl = env.getPartDescList(UDF_Env.VDS_AUTO);
	for(int i=0 ; i<pdl.size() ; ++i){
	    UDF_desc5 d5 = (UDF_desc5)pdl.elementAt(i);
	    com.udfv.ecma167.UDF_PartHeaderDesc phd = d5.getPartHeaderDesc();
	    int partnum = d5.getPartNumber().getIntValue();
	    if(phd.getSpaceBitmap() != null){
		//el.addError(phd.getSpaceBitmap().verifyBitmap(part0, 0, category));
		el.addError(phd.getSpaceBitmap().verifyBitmap(buildPartBitmap(partnum), "Partition Number 0", category));
	    }
	}
	
	return el;
    }
       
    
    //_/_/ protected /_/_//

    
    protected UDF_Extent parseTree(UDF_Element mother, int lbn, int partno, int subno, boolean sstream) throws ClassNotFoundException, UDF_Exception, IOException{
	boolean mirror = (subno != 0 ? true : false);
	debugMsg(3, "udf102:lbn=" + lbn);
	debugMsg(3, "udf102:partno:" + partno + "(" + subno + ")");

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

	int file_type = fe.getICBFileType();
	int file_flags = fe.getICBFlags() & 0x7;

	if(file_type == UDF_icbtag.T_DIRECTORY){
	    parseTreeDirectory(mother, fe, partno, subno, sstream);
	}
	else if(file_type == UDF_icbtag.T_FILE){
	    parseTreeFile(mother, fe, partno, subno);
	}
	else if(file_type == UDF_icbtag.T_SYMLINK){
	    // シンボリックリンクを追う（動作未確認）
	    readSymLink(mother, fe, file_flags, partno);
	}
	else
	    throw new UDF_ICBException(fe, "unknown icb file type:" + file_type);

	return fe_ext;
    }    
    /**
       @deprecated replaced by {@link #parseTree(UDF_Element, int, int, int, boolean)}
     */
    protected UDF_Extent parseTree(UDF_Element mother, int lbn, int partno, boolean mirror, boolean sstream) throws ClassNotFoundException, UDF_Exception, IOException{
	return parseTree(mother, lbn, partno, mirror ? 1 : 0, sstream);
    }

    /**
       シンボリックリンクをたどる。
    */
    protected void readSymLink(UDF_Element mother, UDF_FEDesc fe, int flags, int partno){
	
	try{
	    int offset = 0;
	    
	    if(flags != 3){
		
		UDF_Extent file_ext = (UDF_Extent)createElement("UDF_Extent", null, null);
		fe.setADToExtent(file_ext, partno);
		UDF_ExtentElem[] ee = file_ext.getExtent();
		UDF_RandomAccessExtent rae = file_ext.genRandomAccessExtent();
		
		
		for(int i = 0; i < ee.length; i++){
		    
		    UDF_Extent file_ext2 = (UDF_Extent)createElement("UDF_Extent", null, null);
		    file_ext2.addExtent(ee[i].getLoc(), ee[i].getPartRefNo(), ee[i].getLen());
		    
		    
		    rae.seek(ee[i].getOffset());
		    while(offset < ee[i].getLen()){
			
			UDF_PathComponent pathcomp = (UDF_PathComponent)createElement("UDF_PathComponent", null, null);
			
			
			// Component Id がバッファサイズを超えなければ、Path Component として処理する
			if(offset + 1 < ee[i].getLen()){
			    
			    byte[] buf = { 0, };
			    rae.write(buf, offset + 1, 1);
			    
			    if(ee[i].getLen() <= offset + 1 + 1 + 2 + buf[0])
				break;
			    rae.seek(offset);
			}
			else
			    break;  // あまりが存在するが、これでおしまい
			
			offset += pathcomp.readFrom(rae);
			file_ext2.appendChild(pathcomp);
		    }
		    
		    file_ext2.addExtent(ee[i].getLoc(), ee[i].getPartRefNo(), ee[i].getLen());
		    mother.appendChild(file_ext2);
		}
	    }
	    else{  // immediate ・・・こっちのパターンの方が多い？
		
		byte[] adbuf = fe.getAllocDesc().getData();
		UDF_RandomAccessBuffer rab = new UDF_RandomAccessBuffer(adbuf.length);
		
		
		rab.write(adbuf, offset, adbuf.length);
		rab.seek(0);
		
		while(offset < adbuf.length){
		    
		    UDF_PathComponent pathcomp = (UDF_PathComponent)createElement("UDF_PathComponent", null, null);
		    
		    
		    // Component Id がバッファサイズを超えなければ、Path Component として処理する
		    if(offset + 1 < adbuf.length){
			
			if(adbuf.length <= offset + 1 + 1 + 2 + adbuf[offset + 1])
			    break;
		    }
		    else
			break;  // あまりが存在するが、これでおしまい
		    
		    offset += pathcomp.readFrom(rab);
		    fe.getAllocDesc().appendChild(pathcomp);
		}
	    }
	}
	catch(Exception e){
	    
	    e.printStackTrace();
	}
    }
    
    /**
       Anchor が規定の位置に記録されているかを検証する。
       256/N-256/N セクタのうち、AVDP が記録されているセクタが2箇所以外の場合はエラーとなる。
       
       @param category エラーカテゴリ。
       @param refer  エラーのリファレンス項目番号を指す文字列。
       @return エラーインスタンス。
    */
    protected UDF_Error verifyAVDPNum(short category, String refer){
	
	int avdpnum = 0;
	for(int i = 0; i < 3; i++){
	    /*
	    if(env.exist_avdp[i])
		avdpnum++;
	    */
	    if(env.anchorVolDescPointer[i] != null)
		avdpnum++;
	}
	
	
	// Anchor は2箇所必要
	if(avdpnum != 2){
	    
	    String recorded = "";
	    
	    /*
	    if(env.exist_avdp[0])
		recorded += "256";
	    if(env.exist_avdp[1])
		recorded += ", N - 256";
	    if(env.exist_avdp[2])
		recorded += ", N";
	    */
	    if(env.anchorVolDescPointer[0] != null)
		recorded += " 256 ";
	    if(env.anchorVolDescPointer[1] != null)
		recorded += " N - 256 ";
	    if(env.anchorVolDescPointer[2] != null)
		recorded += " N ";
		    
	    return new UDF_Error
		(category, UDF_Error.L_ERROR, "",
		 "An AnchorVolumeDescriptorPointer structure shall only be recorded at 2 of the following 3 locations on the media:\n" +
		 " Logical Sector 256.\n Logical Sector (N - 256). \n N",
		 refer, recorded, "");
	}
	else
	    return new UDF_Error();
    }
    
    /*
       使用するパッケージの優先順位を設定します。
    private void setPkgPriority(){
	setPkgPriority(0x102);
    }
    */
}
