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
import java.util.zip.*;
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
import org.apache.xml.serialize.*;
import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.exception.*;
import com.udfv.access.*;
import com.udfv.exception.*;
import com.udfv.ecma167.*;

/**
   UDF_Imageは UDF2.50に対応したイメージを操作するクラスである。
 */
public class UDF_Image extends com.udfv.udf201.UDF_Image
{
    public UDF_Image(){
	super();
    }

    public UDF_Image(Document document){
	super(document);
    }
    
    /**
       Metadata Partition Mapを読み込み、イメージに追加する。
       Metadata Bitmap Locを 0xffffffffにすることにより、
       Metadata Bitamp Fileが記録されていないという状態になる。
       
       Metadata Main File Entryを正常に読み込むと、env.has_metadata_partmap_mainをtrueに設定する。
       Metadata Mirror File Entryを正常に読み込むと、env.has_metadata_partmap_mirrorをtrueに設定する。
    */
    protected boolean readMetadataPartMap(int i) throws IOException, UDF_Exception, ClassNotFoundException{
	if(com.udfv.udf250.UDF_MetadataPartMap.class.isAssignableFrom(env.part[i].getClass())){
	    com.udfv.udf250.UDF_MetadataPartMap mpm = (com.udfv.udf250.UDF_MetadataPartMap)env.part[i];
	    //env.metaDataPartMap = mpm;
	    int metadata_file_loc = mpm.getMetadataFileLoc().getIntValue();
	    int metadata_mirr_loc = mpm.getMetadataMirrorFileLoc().getIntValue();
	    int metadata_bitmap_loc = mpm.getMetadataBitmapFileLoc().getIntValue();

            /*
              構成的にこうなっている。
              this ->partNumber --> desc5
                                       ^
                                       |
               type1 or Sparable Partmap

               このようになっている、type1 または Sparable Partmapを探す。
             */
	    int found = -1;
	    int ref_partno = mpm.getPartNumber().getIntValue();
	    //System.err.println("ref_partno" + ref_partno);
	    com.udfv.ecma167.UDF_desc5 desc5 = env.getPartDesc(UDF_Env.VDS_AUTO, ref_partno);
	    for(int ii=0 ; ii < env.getPartMapList().size() ; ++ii){
		if(i != ii){
		    UDF_PartMap part = env.part[ii];
		    int partno = part.getPartNumber().getIntValue();
		    //System.err.println("partno" + i + ":" + ref_partno);
		    if(partno == ref_partno){
			if(com.udfv.ecma167.UDF_part_map1.class.isAssignableFrom(part.getClass())||
			   com.udfv.udf150.UDF_SparablePartMap.class.isAssignableFrom(part.getClass()))
			    found = ii;
			break;
		    }
		}
	    }
	    if(found < 0)
		throw new UDF_PartMapException(this, "No reference partition map");
		
	    int meta_partno = found;

	    // main
	    UDF_Extent fe_ext = createFEExtent(metadata_file_loc, meta_partno, 0);
	    UDF_AD ad = null;
	    UDF_RandomAccessExtent rae = fe_ext.genRandomAccessExtent();
	    UDF_FEDesc fe = null;
	    try{
		fe = UDF_FEDesc.genFEDesc(rae, this, null, null);
		fe.readFrom(rae);
		int typ = fe.getICBFileType();
		if(typ != UDF_icbtag.T_METADATA_FILE){
		    
		    env.has_metadata_partmap_main = false;
		    debugMsg(3, "bad icb file type:" + UDF_icbtag.T_METADATA_FILE + "!=" + typ);
		}
		else{
		    UDF_Extent main_ext = env.getPartMapExtent(i, 0);
		    main_ext.truncExtent(0);
		    UDF_ADList adlist = fe.getADList();
		    for(Iterator it = adlist.iterator() ; it.hasNext() ; ){
			ad = (UDF_AD)it.next();
			if(ad.getFlag() == 0) {
			    main_ext.addExtent(ad.getLbn(), meta_partno, ad.getLen());
			}
		    }
		    env.has_metadata_partmap_main = true;
		    
		    env.setPartMapExtent(i, 0, main_ext);
		    env.setPartMapRandomAccess(i, 0, main_ext.genRandomAccessExtent());
		}
		appendChild(fe_ext);
		fe_ext.appendChild(fe);
	    }
	    catch(UDF_DataException e){
		;//ignore Metadata Fileぶっこわれているときを考慮
	    }
	    // mirror
	    fe_ext = createFEExtent(metadata_mirr_loc, meta_partno, 0);
	    
	    rae = fe_ext.genRandomAccessExtent();
	    fe = null;
	    try{
		fe = UDF_FEDesc.genFEDesc(rae, this, null, null);
		fe.readFrom(rae);
		int typ = fe.getICBFileType();
		if(typ != UDF_icbtag.T_METADATA_MIRROR && !env.has_metadata_partmap_main)
		    throw new UDF_ICBException(this, "bad icb file type:" + UDF_icbtag.T_METADATA_MIRROR + "!=" + typ);
		
		UDF_Extent mirr_ext = env.getPartMapExtent(i, 1);
		
		mirr_ext.truncExtent(0);
		UDF_ADList adlist = fe.getADList();
		for(Iterator it = adlist.iterator() ; it.hasNext() ; ){
		    ad = (UDF_AD)it.next();
		    if(ad.getFlag() == 0) {
			mirr_ext.addExtent(ad.getLbn(), meta_partno, ad.getLen());
		    }
		    
		}
		env.setPartMapExtent(i, 1, mirr_ext);
		env.setPartMapRandomAccess(i, 1, mirr_ext.genRandomAccessExtent());
		
		appendChild(fe_ext);
		fe_ext.appendChild(fe);
	    }
	    catch(UDF_DataException e){
		;//ignore MetadataMirror Fileぶっこわれているときを考慮
	    }
	    
	    //env.has_metadata_partmap_mirror = true;
	    if((mpm.getFlags().getIntValue() & 1) == 1) {
		env.has_metadata_partmap_mirror = true;
	    }
	    
	    // bitmap
	    if (metadata_bitmap_loc != -1) {
		fe_ext = createFEExtent(metadata_bitmap_loc, meta_partno, 0);

		rae = fe_ext.genRandomAccessExtent();
		fe = UDF_FEDesc.genFEDesc(rae, this, null, null);
		fe.readFrom(rae);

		fe_ext.appendChild(fe);
		appendChild(fe_ext);

		int typ = fe.getICBFileType();
		if(typ != UDF_icbtag.T_METADATA_BITMAP)
		    throw new UDF_ICBException(this, "bad icb file type:" + UDF_icbtag.T_METADATA_BITMAP + "!=" + typ);

		int file_flags = fe.getICBFlags() & 0x07;

		if(file_flags == 3) {

		    UDF_desc264 desc264 = (UDF_desc264) createElement("UDF_desc264", null, null);
		    fe.getAllocDesc().readFromAndReplaceChild(desc264);
		    //特に設定しなくてもいい
		    //desc264.setGlobalPoint(fe.getAllocDesc().getGlobalPoint());
		    //env.metaPartSpaceBitmapDesc = desc264;
		    env.setPartMapBitmap(i, desc264.getBitmap());
		}
		else {
		    UDF_Extent dir_ext = (UDF_Extent) createElement(UDF_XML.UDF_EXTENT, null, null);
		    fe.setADToExtent(dir_ext, meta_partno);
		    rae = dir_ext.genRandomAccessExtent();
		    UDF_desc264 desc264 = (UDF_desc264) createElement("UDF_desc264", null, null);
		    desc264.readFrom(rae);
		    dir_ext.appendChild(desc264);
		    appendChild(dir_ext);
		    //env.metaPartSpaceBitmapDesc = desc264;

		    String label = UDF_Label.genExtentLabel(dir_ext);
		    dir_ext.setAttribute("id", label);
		    fe.getAllocDesc().setAttribute("ref", label + ".extents");

		    env.setPartMapBitmap(i, desc264.getBitmap());
		}
	    }
	    //Metadata bitmapがないとき
	    else{//bitmapを構築する。
		env.setPartMapBitmap(i, buildPartMapBitmap(i));
	    }
	    env.part[i].recalc(RECALC_PARTMAP, env.f);
	    
	    return true;
	}
	return false;
    }

    public void recalc(short type, UDF_RandomAccess f){
	super.recalc(type, f);
	try{
	    if(type == RECALC_TREE || type == RECALC_ENV){
		try{
		    //env.mirr_root_fe = (UDF_FEDesc)findById("ROOT_1").getFirstChild();
		    env.setRootFE(1, (UDF_FEDesc)findById("ROOT_1").getFirstChild());
		}
		catch(NullPointerException e){
		    ;
		}
		try{
		    //env.mirr_sroot_fe = (UDF_FEDesc)findById("SROOT_1").getFirstChild();
		    env.setSRootFE(1, (UDF_FEDesc)findById("SROOT_1").getFirstChild());
		}
		catch(NullPointerException e){
		    ;
		}
	    }
	}
	catch(Exception e){
	    ignoreMsg("udf250.UDF_Image#recalc()", e);
	}
    }

    public void readPartMap() throws IOException, UDF_Exception, ClassNotFoundException{

	for(int i=0 ; i<env.getPartMapList().size() ; ++i){
	    //VirtualPartmapがあるなら VATを追加する
	    if(readVirtualPartMap(i))
		continue;

	    //SparablePartmapがあるなら SparingTableを追加する
	    if(readSparablePartMap(i))
		continue;

	    //MetadataPartMapがあるか？
	    if(readMetadataPartMap(i))
		continue;
	}
    }

    /**
       イメージファイルから Volumeを読み XMLを構築します。

       1) ECMA119のVRSを読む
       2) ECMA169のVRSを読む
       3) 256 セクタのAnchorを読む
       4) MVDSを読む
       5) RVDSを読む
       6) パーティションのパラメータを指定する
       7) N-257 セクタのAnchorを読む
       8) N-17 セクタのVRSを読む
       9) N-1 セクタのAnchorを読む
 
       @param f イメージファイル
       @return 常に0
    */
    public long readVolume(UDF_RandomAccess f){
	try{
	    long readsz;
	    
	    f.seek(16 * UDF_Env.LBS);
	    readVRS(f);
	    
	    // 256 => N-256 => N-1 の順にAVDP を読む。1つでも読めたらおしまい
	    if(!readAVDPinVR(f))
		return 0;
	    
	    boolean mvds = readMVDS(f);
	    boolean rvds = readRVDS(f);
	    if(!mvds && !rvds)
		throw new UDF_DataException(this, "Neither MVDS nor RVDS is not recorded.");


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

	    return 0;
	}
	catch(Exception e){
	    e.printStackTrace();
	}
	return 0;
    }

    
    /**
       イメージファイルから FileSystemを読み XMLを構築する。
       
       @param f イメージファイル
       @param mirror ミラーフラグ

       @deprecated replaced by {@link #readFileSystem(UDF_RandomAccess, int)}
    */
    public long readFileSystem(UDF_RandomAccess f, boolean mirror) throws IOException, ClassNotFoundException, UDF_VolException, UDF_Exception{
	return readFileSystem(f, mirror ? 1 : 0);
    }
    /**
       イメージファイルから FileSystemを読み XMLを構築する。
       
       @param f イメージファイル
       @param subno	副パーティション番号

       @see <a href="subno.html">副パーティション番号</a>
    */
    public long readFileSystem(UDF_RandomAccess f, int subno) throws IOException, ClassNotFoundException, UDF_VolException, UDF_Exception{
//	try{
	    boolean mirror = (subno == 0 ? false : true);
	    /*
	      Metadata Partition Mapを有する場合に、FileSystemを一つのExtentとして扱う。
	      現在のところ、このExtentのexntents要素はパーティションマップの最後の
	      情報だけを選出して出力している。
	      想定しがたいが、Metadata Partition Mapが複数存在するようなイメージなどで
	      正しい情報が出力されない可能性を否定できない。
	    */
	    UDF_Element mother;

	    mother = this;

	    debugMsg(3, "read FSDS");
	    readFSDS(f, subno);

	    //ツリーを辿る
	    if(true){
		env.debug();
		UDF_Extent ext;

		int root_lbn = env.getFileSetDesc(subno).getRootDirectoryICB().getLbn();
		int root_partno = env.getFileSetDesc(subno).getRootDirectoryICB().getPartRefNo();
		long root_len = env.getFileSetDesc(subno).getRootDirectoryICB().getLen();
		int stream_lbn = env.getFileSetDesc(subno).getSystemStreamDirectoryICB().getLbn();
		int stream_partno = env.getFileSetDesc(subno).getSystemStreamDirectoryICB().getPartRefNo();
		long stream_len = env.getFileSetDesc(subno).getSystemStreamDirectoryICB().getLen();

		// parse tree
		debugMsg(3, "Parse Tree");
		ext = parseTree(mother, root_lbn, root_partno, subno, false);
		debugMsg(3, "Parse Tree done");

		/*
		if(mirror)
		    env.mirr_root_fe = (UDF_FEDesc)(ext.getChildren()[0]);
		else
		    env.root_fe = (UDF_FEDesc)(ext.getChildren()[0]);
		*/

		env.setRootFE(mirror ? 1 : 0, (UDF_FEDesc)ext.getFirstChild());

		// parse tree
		if(stream_lbn == 0 && stream_partno == 0 && stream_len == 0)
		    ;
		else{
		    parseTree(mother, stream_lbn, stream_partno, subno, true);
		    /*
		    if(mirror)
			env.mirr_sroot_fe = (UDF_FEDesc)(ext.getChildren()[0]);
		    else
			env.sroot_fe = (UDF_FEDesc)(ext.getChildren()[0]);
		    */
		    env.setSRootFE(mirror ? 1 : 0, (UDF_FEDesc)ext.getFirstChild());
		}
	    }
/*	}
	catch(Exception e){
	    e.printStackTrace();
	}
*/	return 0;
    }
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	final short category = UDF_Error.C_UDF250;
	
	
	if(env.udf_revision != 0x250)
	    return super.verify();
	
	el.addError(verifyBase(category));
	el.addError(super.verify());
	
	return el;
    }

    /**
       UDF 2.50 以降のリビジョンで共通して使用できる検証用関数。
       
       @param category エラーカテゴリ。
       @return エラーリストインスタンス。
    */
    public UDF_ErrorList verifyBase(short category) throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	el.addError(super.verifyBase(category));
	
	
	// Metadata Partition のパーティション番号を調べる
	int metapartnum = -1;
	for(int i = 0; i < env.getPartMapList().size() ; i++){
	    
	    if(com.udfv.udf250.UDF_MetadataPartMap.class.isAssignableFrom(env.part[i].getClass())){
		
		metapartnum = i;
		break;
	    }
	}

	if(metapartnum != -1){
	    
	    // UDF のツリーを調べ、Metadata Main/Mirror のエレメントをそれぞれ取得する
	    UDF_ElementBase[] elems = env.root.getChildren();
	    UDF_ElementList metaelems = new UDF_ElementList();
	    UDF_ElementList mirrorelems = new UDF_ElementList();

	    if(metapartnum != -1){
		
		for(int i = 0; i < elems.length; i++){
		    
		    UDF_Element elem = (UDF_Element)elems[i];
		    
		    if(!com.udfv.core.UDF_Extent.class.isAssignableFrom(elem.getClass()))
			continue;
		    
		    UDF_ElementBase[] child = elem.getChildren();
		    for(int j = 0; j < child.length; j++){
			
			UDF_Element elem2 = (UDF_Element)child[j];
			if(elem2.getElemPartRefNo() == metapartnum){
			    if(elem2.getPartSubno() == 0){
				metaelems.add(elem2);
			    }
			    else{
				mirrorelems.add(elem2);
			    }
			}
		    }
		}
	    }
	    
	    
	    // Mirror がduplicate されていれば、Main とMirror の内容が等しいかチェックする
	    try{
		if((env.getMetadataPartMap().getFlags().getIntValue() & 0x01) != 0){
		    
		    int mainlen   = metaelems.size();
		    int mirrorlen = mirrorelems.size();
		    
		    if(mainlen != mirrorlen){
			
			el.addError(new UDF_Error
				    (category, UDF_Error.L_ERROR, "Metadata Partition",
				     "If the Duplicate Metadata Flag is set in the Metadata Partition Maps Flags field, " +
				     "the Metadata Mirror File shall be maintained dynamically so that it contains " +
				     "identical data to the Metadata File at all times" +
				     "(number of Main structures may differ from Mirrors' " + mainlen + "!="+ mirrorlen +").",
				     "2.2.13"));
		    }
		    else{
			
			// 全てのエレメントについて、バイトレベルで比較する
			el.addError(verifyMainMirror(category, metaelems.toElemArray(), mirrorelems.toElemArray()));
		    }
		}
	    }
	    catch(UDF_PartMapException e){
		e.printStackTrace();
	    }
	    
/*
	    // Metadata Part のSpace Bitmap の検証
	    if(env.metaPartSpaceBitmapDesc != null){
		
		el.addError(env.metaPartSpaceBitmapDesc.verifyBitmap(env.root.getChildren(), metapartnum, category));
		
		UDF_desc264 desc264 = (UDF_desc264)env.metaPartSpaceBitmapDesc;
		el.addError(desc264.verifyForMetadata(category, "2.2.13.2"));
	    }
*/
	    // Metadata Part のSpace Bitmap の検証改
	    UDF_ElementList pml = env.getPartMapList();
	    for(int i = 0; i < pml.size() ; ++i){
		if(env.isMetadataPartMap(i)){
		    UDF_MetadataPartMap mpm = (UDF_MetadataPartMap)pml.elementAt(i);
		    if(mpm.getMetadataBitmapFileLoc().getIntValue() == -1)
			break;
		    el.addError(mpm.getMetadataBitmap().verifyBitmap(buildPartMapBitmap(i), "Partition Reference Number " + i, category));
		    //el.addError(env.metaPartSpaceBitmapDesc.verifyBitmap(buildPartMapBitmap(i), "Partition Reference Number " + i, category));
		}
	    }

	    el.addError(verifyMetadataExtent(metaelems.toElemArray(), category));
	}
	
	return el;
    }
    
    private UDF_ErrorList verifyMainMirror(short category, UDF_ElementBase[] el1, UDF_ElementBase[] el2) throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();

	if(el1.length != el2.length){
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Metadata Partition",
			 "If the Duplicate Metadata Flag is set in the Metadata Partition Maps Flags field, " +
			 "the Metadata Mirror File shall be maintained dynamically so that it contains " +
			 "identical data to the Metadata File at all times" +
			 "(number of Main structures may differ from Mirrors' " + el1.length + "!="+ el2.length +").",
			 "2.2.13"));
	    return el;
	}

	// 全てのエレメントについて、バイトレベルで比較する
	for(int i = 0; i < el1.length; i++){
			
	    UDF_Element e1 = (UDF_Element)el1[i];
	    UDF_Element e2 = (UDF_Element)el2[i];
	    
	    
	    if(com.udfv.ecma167.UDF_Directory.class.isAssignableFrom(e1.getClass()) ||
	       com.udfv.core.UDF_Extent.class.isAssignableFrom(e1.getClass())){
		
		el.addError(verifyMainMirror(category, e1.getChildren(), e2.getChildren()));
		continue;
	    }

	    //System.err.println(e1.getLongSize());
	    //System.err.println(e2.getLongSize());

	    //byte[] b1 = e1.getBytes();
	    //byte[] b2 = e2.getBytes();
	    
	    //if(UDF_Util.cmpBytesBytes(b1, b2))
	    //continue;
	    if(e1.compareTo(e2) == 0)
		continue;
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Metadata Partition",
			 "If the Duplicate Metadata Flag is set in the Metadata Partition Maps Flags field, " +
			 "the Metadata Mirror File shall be maintained dynamically so that it contains " +
			 "identical data to the Metadata File at all times.\n" +
			 "(Type:" + e1.getName() + 
			 " LSN: MAIN=" + (e1.getGlobalPoint() / env.LBS) +
			 ", MIRROR=" + (e2.getGlobalPoint() / env.LBS) + ")",
			 "2.2.13"));
	}
	
	return el;
    }

    
    
    /**
       指定されたエレメントを、Metadata File として検証する。
       
       @param elems 検証対象となるエレメントの配列。
       @return エラーリストインスタンス。
    */
    protected UDF_ErrorList verifyMetadataExtent(UDF_ElementBase[] elems, short category) throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	

	for(int i = 0; i < elems.length; i++){
	    
	    UDF_Element elem = (UDF_Element)elems[i];
	    
	    // UDF_Extent かUDF_Directory の場合のみ、さらにその中身も見る
	    if(com.udfv.core.UDF_Extent.class.isAssignableFrom(elem.getClass()) ||
	       com.udfv.ecma167.UDF_Directory.class.isAssignableFrom(elem.getClass())){
		
		el.addError(verifyMetadataExtent(elem.getChildren(), category));
		continue;
	    }
	    
	    // FSD
	    if(com.udfv.ecma167.UDF_desc256.class.isAssignableFrom(elem.getClass()))
		continue;
	    // FID
	    else if(com.udfv.ecma167.UDF_desc257.class.isAssignableFrom(elem.getClass()))
		continue;
	    // FE(ICB)
	    else if(com.udfv.ecma167.UDF_desc261.class.isAssignableFrom(elem.getClass())){
		
		el.addError(com.udfv.udf250.UDF_desc261.verifyFEInMetadata((com.udfv.ecma167.UDF_desc261)elem, category));
		continue;
	    }
	    // EFE(ICB)  ここには来ない
//	    else if(com.udfv.ecma167.UDF_desc266.class.isAssignableFrom(elem.getClass()))
//		continue;
	    // An unused block marked free in the Metadata Bitmap File
	    else if(com.udfv.ecma167.UDF_pad.class.isAssignableFrom(elem.getClass()))
		continue;
	    // Extent of Allocation Descriptors
	    else if(com.udfv.core.UDF_Data.class.isAssignableFrom(elem.getClass()))
		continue;
	    // Allocation Extent Descriptor
	    else if(com.udfv.ecma167.UDF_desc258.class.isAssignableFrom(elem.getClass()))
		continue;
	    // Indirect Entry(ICB)
	    else if(com.udfv.ecma167.UDF_desc259.class.isAssignableFrom(elem.getClass()))
		continue;
	    // Termina Entry(ICB)
	    else if(com.udfv.ecma167.UDF_desc260.class.isAssignableFrom(elem.getClass()))
		continue;
	    // Terminating Descriptor // UDF2.50 Eratta DCN-5105
	    else if(com.udfv.ecma167.UDF_desc8.class.isAssignableFrom(elem.getClass()))
		continue;
	    else{
		// 上記以外の構造は含まれていてはならない
		UDF_Error err = new UDF_Error
		    (category, UDF_Error.L_ERROR, "Metadata File(" + elem.getName() + ")",
		     "The Allocation Descriptors for this file shall describe only logical blocks which contain " +
		     "one of the below data types. No user data or other metadata may be referenced.\n\n" +
		     "* FSD\n* ICB\n* Extent of Allocation Descriptors(see 2.3.11)\n* Directory or stream directory data(i.e. FIDs)\n" +
		     "* An unused block marked free in the Metadata Bitmap File.",
		     "2.2.13.1");
		
		err.setGlobalPoint(elem.getGlobalPoint());
		el.addError(err);
	    }
	}
	return el;
    }
    
    /**
       Direcotry treeを辿る。
     */
    protected UDF_Extent parseTree(UDF_Element mother, int lbn, int partno, int subno, boolean sstream) throws ClassNotFoundException, UDF_Exception, IOException{
	debugMsg(3, "udf250:lbn=" + lbn);
	//debugMsg(3, "udf250:partno:" + partno + (mirror ? "(mirror)": "(main)"));
	debugMsg(3, "udf250:partno:" + partno + "(" + subno + ")");

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

	//　Extended Attributes ICB を追跡する（未検証）　//
	com.udfv.ecma167.UDF_desc261 desc261 = (com.udfv.ecma167.UDF_desc261)fe;
	long extended_attr_icb_len = desc261.getExtendedAttrICB().getLen();
	if (extended_attr_icb_len > 0) {
	    int extended_attr_icb_lbn = desc261.getExtendedAttrICB().getLbn();
	    int extended_attr_icb_ref = desc261.getExtendedAttrICB().getPartRefNo();

	    UDF_Extent ea_ext = desc261.parseExtendedAttr(extended_attr_icb_lbn, extended_attr_icb_ref, extended_attr_icb_len, subno == 0 ? false : true);
	    mother.appendChild(ea_ext);
	}

	// 266ならば system stream icbをチェック
	if(com.udfv.ecma167.UDF_desc266.class.isAssignableFrom(fe.getClass())){
	    com.udfv.ecma167.UDF_desc266 fe266 = (com.udfv.ecma167.UDF_desc266)fe;
	    UDF_long_ad ad = fe266.getStreamDirectoryICB();
	    if(ad.getLen() > 0){
		UDF_Extent stream_ext = parseTree(mother, ad.getLbn(), ad.getPartRefNo(), ad.getPartSubno(), true);
		String slabel = UDF_Label.genExtentLabel(stream_ext);
		stream_ext.setAttribute("id", slabel);
		ad.setRefAttribute(slabel);
	    }
	}

	int file_type = fe.getICBFileType();
	int file_flags = fe.getICBFlags() & 0x7;

	if(UDF_icbtag.isTypeDirectory(file_type)) {
	    debugMsg(3, "parseTreeDirectory:");
	    parseTreeDirectory(mother, fe, partno, subno, sstream);
	    debugMsg(3, "parseTreeDirectory: done");
	}
	else if(file_type == UDF_icbtag.T_FILE || file_type == UDF_icbtag.T_RTFILE){
	    debugMsg(3, "parseTreeFile:");
	    parseTreeFile(mother, fe, partno, subno);
	    debugMsg(3, "parseTreeFile: done");
	}
	else if(file_type == UDF_icbtag.T_SYMLINK){
	    
	    // シンボリックリンクを追う（動作未確認）
	    readSymLink(mother, fe, file_flags, partno);
	    //if(!mirror)
		//env.numberOfFiles++;
	}
	else
	    throw new UDF_ICBException(fe, "unknown icb file type:" + file_type);

	return fe_ext;
    }
}
