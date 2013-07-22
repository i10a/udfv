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
package com.udfv.udf150;

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
import com.udfv.ecma119.*;
import com.udfv.ecma167.*;

/**
   UDF_Imageは UDF1.50に対応したイメージを操作するクラスである。
 */
public class UDF_Image extends com.udfv.udf102.UDF_Image
{
    public UDF_Image(){
	super();
    }
    
    public UDF_Image( Document document){
	super(document);
    }
    
    /**
       readVAT() で読み込んだUDF_Extent を格納するためのVector。
       readVAT() 後、この値をUDF_Env のvat_part_e に格納する。
    */
    private Vector vat_extent;
    
    protected Vector getVatExtent(){
	
	return vat_extent;
    }

    /**
       ボリューム情報を読み込みます。
       
       @param f       イメージアクセサ。
       @return 0を返します。
    */
    public long readVolume(UDF_RandomAccess f){
	
	try{
	    f.seek(16 * UDF_Env.LBS);
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
	final short category = UDF_Error.C_UDF150;
	
	
	if(env.udf_revision != 0x150)
	    return super.verify();
	
	el.addError(verifyBase(category));
	el.addError(super.verify());
	
	return el;
    }
    
    
    //_/_/ protected /_/_//
    
    
    protected UDF_Extent parseTree(UDF_Element mother, int lbn, int partno, int subno, boolean sstream) throws ClassNotFoundException, UDF_Exception, IOException{
	debugMsg(3, "lbn=" + lbn);
	//debugMsg(3, "partno:" + partno + (mirror ? "(mirror)": "(main)"));
	debugMsg(3, "udf150:partno:" + partno + "(" + subno + ")");

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
	    //env.numberOfFiles++;
	}
	else
	    throw new UDF_ICBException(fe, "unknown icb file type:" + file_type);

	return fe_ext;
    }
    
    /**
       シンボリックリンクを辿る。
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
       パーティションマップを読む。

       このメソッドは VDSを読み終わった直後に呼ばれ、パーティションマップに付随する各種情報を読みとるメソッドである。
     */
    public void readPartMap() throws IOException, UDF_Exception, ClassNotFoundException{
	for(int i=0 ; i<env.getPartMapList().size() ; ++i){
	    readSparablePartMap(i);
	    readVirtualPartMap(i);
	}
    }
    
    /**
       VATを読みこむ

       UDF1.50で規定されている VATと
       UDF2.00以上で規定されている VATは根本的に構造が違うので注意が必要である。
	

       @param f	アクセサ
       @param lbn	位置
       @param size	長さ
       @param previous	previous VATか？
     */
    protected void readVAT(UDF_RandomAccess f, int lbn, int partrefno, long size, boolean previous) throws UDF_Exception, IOException, ClassNotFoundException{
	try{
	    int partno = env.getPartDescByPartRefNo(UDF_Env.VDS_AUTO, partrefno).getPartNumber().getIntValue();
	    int part_loc = env.getPartStartingLoc(partno);
	    long part_len = env.getPartLen(partno);
	    int loc = lbn + part_loc;

	    if(loc * UDF_Env.LBS + size > f.length())
		return;

	    debugMsg(1, "VAT lbn=" + lbn + " size=" + size);
	    UDF_Extent vat_ext = createExtent(null, null);
	    vat_ext.addExtent(lbn, partrefno, size);
	    UDF_RandomAccessExtent vat_rae =  vat_ext.genRandomAccessExtent();
	    UDF_FEDesc fe = UDF_FEDesc.genFEDesc(vat_rae, vat_ext, null, null);
	    fe.readFrom(vat_rae);

	    UDF_RandomAccessBuffer rab = fe.getAllocDesc().genRandomAccessBytes();
	    UDF_VirtualAllocTable150 vat = (UDF_VirtualAllocTable150)createElement("UDF_VirtualAllocTable150", null, null);

	    vat.setHintSize((int) rab.length());
	    //vat.readFrom(rab);
	    fe.getAllocDesc().readFromAndReplaceChild(vat);
	    
	    env.root.appendChild(vat_ext);
	    //appendChild(vat_ext);
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
		ext.addExtent(part_loc, -1, env.getPartMapExtent(vatpartnum, 0).getLongSize());
	    }
	    else
		ext = env.getPartMapExtent(vatpartnum, 0);
	    
	    int sec = 0;
            com.udfv.udf150.UDF_VirtualPartMap vpm = (com.udfv.udf150.UDF_VirtualPartMap)env.part[vatpartnum];

	    rab.seek(0);
	    while(!rab.eof() && rab.getPointer() < rab.length() - 36){
		//UDF_uint32 u = new UDF_uint32(this, null, null);
		long u = rab.readUint32();
		if(u != 0xffffffffL)
		    ext.spareExtent(sec, -1, (int)(u + part_loc), -1, UDF_Env.LBS, vpm.getPartNumber().getIntValue());
		
		++sec;
	    }
	    
	    ext.normalize();
	    vat_extent.add(ext);
	    
	    
	    UDF_uint32 previous_vat = vat.getPreviousVATICBLocation();
	    if(previous_vat != null && previous_vat.getIntValue() != 0xffffffff){
		debugMsg(3, "previous VAT:" + previous_vat.getIntValue());
		
		//int preloc = previous_vat.getIntValue() + env.partDesc.getPartStartingLoc().getIntValue();
		//とりあえずのコード
		int preloc = previous_vat.getIntValue();// + env.getPartDescByPartRefNo(UDF_Env.VDS_AUTO, 0).getPartStartingLoc().getIntValue();
		readVAT(f, preloc, env.getVATPartMap(), UDF_Env.LBS, true);
	    }

	}
	catch(UDF_EOFException e){
	    ;
	}
    }
    
    /**
       partnumのパーティション番号を調べ、VirtualPartMapなら VATを追加する

       @param partnum	パーティション番号

       @return VirtualPartMapがあったかどうか。あったなら true。
     */
    protected boolean readVirtualPartMap(int partnum) throws IOException, UDF_Exception, ClassNotFoundException{
	if(com.udfv.udf150.UDF_VirtualPartMap.class.isAssignableFrom(env.part[partnum].getClass())){
	    UDF_RandomAccess f = env.f;

	    //もう設定されているから不要
	    //env.part_e[partnum].addExtent(env.part_loc, -1, env.part_e[0].getLongSize());
	    int part_loc = 0;
	    int partno = env.getPartDescByPartRefNo(UDF_Env.VDS_AUTO, partnum).getPartNumber().getIntValue();
	    part_loc = env.getPartStartingLoc(partno);

	    int last_lbn = (int)(env.getImageSize()/env.LBS) - 1 - part_loc;
	    vat_extent = new Vector();
            //VATはpartnumではなく、参照先(type1のほう)にあると考えるべきである
            //UDF1.50 p.6
	    readVAT(f, last_lbn, env.getVATPartMap(), env.LBS, false);
	    //env.part_ra[partnum] = env.part_e[partnum].genRandomAccessExtent();

	    
	    // env のvat_... に値を設定
	    if(vat_extent.size() != 0){
		for(int i = 0; i < vat_extent.size(); i++){
		    
		    env.setPartMapExtent(partnum, i, (UDF_Extent)vat_extent.elementAt(i));
		    env.setPartMapRandomAccess(partnum, i, ((UDF_Extent)vat_extent.elementAt(i)).genRandomAccessExtent());
		}
	    }
	    
	    return true;
	}
	return false;
    }
    
    /**
       partnumのパーティション番号を調べ、SparablePartMapなら SparingTableを追加する。

       @param partnum	パーティション番号

       @return SparablePartMapがあったかどうか。あったなら true。
     */
    protected boolean readSparablePartMap(int partnum) throws IOException, UDF_Exception, ClassNotFoundException{

	//SparablePartmapがあるなら SparingTableを追加する
	if(com.udfv.udf150.UDF_SparablePartMap.class.isAssignableFrom(env.part[partnum].getClass())){
	    
	    UDF_SparablePartMap sp = (UDF_SparablePartMap)env.part[partnum];
	    UDF_ElementBase[] eb = sp.getLocsOfSparingTables().getChildren();
	    for(int j=0 ; j<eb.length ; ++j){
		UDF_Extent st_ext = createExtent(null, null);
		UDF_uint32 ee = (UDF_uint32)eb[j];

		st_ext.addExtent(ee.getIntValue(), -1, sp.getSizeOfEachSparingTable().getIntValue());
		String label = UDF_Label.genExtentLabel(st_ext);

		st_ext.setAttribute("id", label);
		ee.setAttribute("ref", label + ".loc");

		UDF_SparingTable st = (UDF_SparingTable) createElement("UDF_SparingTable", null, null);
		st.readFrom(st_ext.genRandomAccessExtent());
		st_ext.appendChild(st);
		env.root.appendChild(st_ext);

		UDF_Extent main_ext = env.getPartMapExtent(partnum, 0);
		//appendChild(st_ext);
		//spareがある場合の対処
		if(j == 0){//最初の1つにのみ対処
		    UDF_RandomAccessBuffer rab = st.getMapEntry().genRandomAccessBytes();
		    boolean has_spare = false;
		    while(!rab.eof()){
			long src = rab.readUint32();
			long dst = rab.readUint32();
			if(src != 0xffffffffL){//spareがあった
			    //もしspareできたなら part_raも作りなおす。
			    if(main_ext.spareExtent((int)src, -1, (int)dst, -1, (long)sp.getPacketLen().getIntValue() * UDF_Env.LBS, sp.getPartNumber().getIntValue())){//partnum)){
				has_spare = true;
			    }
			}
		    }
		    if(has_spare){
			main_ext.normalize();
			//env.part_e[partnum].normalize();
			//env.part_ra[partnum] = env.part_e[partnum].genRandomAccessExtent();
		    }
		}
	    }
	    
	    return true;
	}
	
	return false;
    }
    
    /**
       Anchor が規定の位置に記録されているかを検証する。
       256/N-256/N セクタのうち、AVDP が記録されているセクタが2箇所未満の場合はエラーとなる。
       
       @param category エラーカテゴリ。
       @param refer  エラーのリファレンス項目番号を指す文字列。
       @return エラーインスタンス。
    */
    protected UDF_Error verifyAVDPNum(short category, String refer){

	// XML に対して検証を行う際、UDF_Image は常に最上位のクラスインスタンスが生成される（05/05/25 現在の仕様）
	// ため、UDF 1.02 のXML に対して検証を行うと、常にこのクラスのverifyAVDPNum() が
	// コールされてしまう。それを避けるために、とりあえず以下のコードを挿入する。
	if(env.udf_revision < 0x150)
	    return super.verifyAVDPNum(category, refer);
	
	int avdpnum = 0;
	for(int i = 0; i < 3; i++){
	    /*
	    if(env.exist_avdp[i])
		avdpnum++;
	    */
	    if(env.anchorVolDescPointer[i] != null)
		avdpnum++;
	}
	
	
	// Anchor は少なくとも2箇所必要
	if(avdpnum < 2){
	    
	    String recorded = "";
	    
	    /*
	    if(env.exist_avdp[0])
		recorded += " 256 ";
	    if(env.exist_avdp[1])
		recorded += " N - 256 ";
	    if(env.exist_avdp[2])
		recorded += " N ";
	    */
	    if(env.anchorVolDescPointer[0] != null)
		recorded += " 256 ";
	    if(env.anchorVolDescPointer[1] != null)
		recorded += " N - 256 ";
	    if(env.anchorVolDescPointer[2] != null)
		recorded += " N ";
		    
	    return new UDF_Error
		(category, UDF_Error.L_ERROR, "",
		 "An AnchorVolumeDescriptorPointer structure shall be recorded in at least 2 of the following 3 locations on the media:\n" +
		 " Logical Sector 256.\n Logical Sector (N - 256). \n N",
		 refer, recorded, "");
	}
	else
	    return new UDF_Error();
    }
    
    /*
       使用するパッケージの優先順位を設定します。
    private void setPkgPriority(){
	setPkgPriority(0x150);
    }
    */
}
