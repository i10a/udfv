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
/**
 */

package com.udfv.udfapi;

import java.lang.*;
import java.io.*;
import java.util.*;
import com.udfv.core.*;
import com.udfv.ecma167.*;
import com.udfv.exception.*;
import com.udfv.access.*;
import com.udfv.util.*;
import com.udfv.encoding.UDF_Encoding;
import com.udfv.udf250.UDF_MetadataPartMap;

public class UDF_API implements UDF_FileAPI, UDF_PartitionAPI{
    UDF_Image my_image;
    UDF_Env env;
    static final int MAIN = 0;
    static final int MIRROR = 1;

    public UDF_API(UDF_Image image){
	my_image = image;
	env = my_image.env;
    }


    /*    
    private UDF_AD[] duplicateAD(UDF_AD[] ad){
	if(ad == null)
	    return null;

	UDF_AD[] dup_ad = new UDF_AD[ad.length];
	
	for(int i=0 ; i<dup_ad.length ; ++i)
	    dup_ad[i] = (UDF_AD)ad[i].duplicateElement();
	
	return dup_ad;
    }
    */
    /**
       ディレクトリを作成する。

       @param policy ポリシー。
       @param path 作成したいディレクトリ名（ルートからのパス含む）。
       @param adtype AD のタイプ。

       ディレクトリを生成するまでの流れ
       
       (説明)
       mkdir (8:AAA/8:BBB/8:CCC)とした仮定すると
       
       8:AAA/8:BBBの実体を現わすFEが fe
       8:AAA/8:BBB/8:CCC を現わすFIDが fid
       8:AAA/8:BBB/8:CCC の実体を現わすFEが fe2
       8:AAA/8:BBB/8:CCC/.. の実体を現わすFIDが fid2
       
       (重要事項1)
       UDF_Directoryならびに UDF_FEDescには idおよびrefアトリビュートを設定
       し、関連をつけておかなければならない。そうしないと recalc()が正しく
       動作しない。
       
       (重要事項2)
       UDF_FEDesc, UDF_desc257のツリーの親子関係は正しく構築しておかなければ
       ならない。そうしないと、いくつかのAPIが正しく動作しない。
       
       mkdir	---->	UDF_Image(FID追加のための親FEを取得)
       <----	fe
       
       ---->	fe (UDF_Directory取得)※1
       <----	directory
       
       ---->	UDF_FIDPolicy(※1に追加するFID生成)
       <----	fid
       
       ---->	directory(FID追加)	----> 2048バイトを越えたなら UDF_AllocFIDPolicy()で新しい領域を確保し移動する。ここのインプリはとりあえず pending)※2
       
       ---->	UDF_AllocFEPolicy(新規 FEの領域確保)※2
       <----	ad
       
       ---->	UDF_AllocFIDPolicy(新規 Direcoryの領域確保)※2
       <----	ad2
       
       ---->	UDF_FEPolicy(新規 FE生成)
       <----	fe2
       
       ---->	UDF_FIDPolicy(新規 FID生成)
       <----	fid2(親を指すもの)
       
       ---->	fe2(位置設定 fe2の lbn, partnoを adに設定)	
       ---->	fe2(領域設定 fe2のADに ad2を設定する
       
       ---->	新規UDF_Directory作成
       <----	directory2
       
       ---->	directory2(fid2を子供に追加)
       
       ---->	UDF_Image(fe2追加)
       ---->	UDF_Image(directory2追加)
       
       ---->	fe(recalcで CRC, CHKSUM, TAG-LOCを変更)
       ---->	directory2(recalcで CRC, CHKSUM, TAG-LOCを変更)
       
       ※2で領域が確保できなければ、確保した領域全てを戻して Exceptionをスロー
       する。
       
       fe-->directory-->fid-->fe2-->fid2
       
    */
    public void mkdir(UDF_Policy policy, String path, int adtype) throws UDF_Exception, IOException{
	String dirname = UDF_Util.dirname(path);
	String fidname = UDF_Util.fidname(path);
	//FIDを追加する FEを取得
	UDF_FEDesc fe = my_image.findFEByPathname(dirname, MAIN);
	UDF_FEDesc mirr_fe = my_image.findFEByPathname(dirname, MIRROR);
	if(fe == null && mirr_fe == null)
	    throw new UDF_FileAPIException(my_image, "No such directory" + dirname);

	//FEのタイプを調べ、ディレクトリでなければエラー
	if(fe.getICBFileType() != UDF_icbtag.T_DIRECTORY &&
	   fe.getICBFileType() != UDF_icbtag.T_SDIRECTORY)
	    throw new UDF_FileAPIException(my_image, "Not directory:" + dirname);

	//新規作成用FEの領域を確保
	UDF_ADList ad_fe = policy.getAllocDescPolicy().alloc(my_image, my_image.env.LBS, adtype);
	if(ad_fe == null){
	    throw new UDF_FileAPIException(my_image, "No space:" + path);
	}
	UDF_ADList ad_fid = policy.getAllocDescPolicy().alloc(my_image, 40/*my_image.env.LBS*/, adtype);
	if(ad_fid == null){
	    policy.getAllocDescPolicy().free(my_image, ad_fe);
	    throw new UDF_FileAPIException(my_image, "No space:" + path);
	}

	// UDF2.50 以降で、Mirror Partition があった場合
	// 先に mirrorを作る。(mirrorだと unique IDが1足されない)

	if(my_image.env.hasMirrorPartmap(ad_fe.firstElement().getPartRefNo())){
	    //mainと同じものを mirrorに追加することはできないので、
	    //複製しておく。
	    UDF_ADList mirr_ad_fe = ad_fe.duplicateList();
	    UDF_ADList mirr_ad_fid = ad_fid.duplicateList();

	    mkdir(policy, mirr_fe, path, adtype, mirr_ad_fe, mirr_ad_fid, true);
	}
	mkdir(policy, fe, path, adtype, ad_fe, ad_fid, false);

	my_image.env.getLVIDImplUse().addDirectories(1);
    }
    public void mkdir(UDF_Policy policy, UDF_FEDesc fe, String path, int adtype, UDF_ADList ad_fe, UDF_ADList ad_fid, boolean mirror) throws UDF_Exception, IOException{
	if(fe == null)
	    return;

	String dirname = UDF_Util.dirname(path);
	String fidname = UDF_Util.fidname(path);

	//FIDを追加する UDF_Directoryの取得
	UDF_Directory dir = fe.getUDFDirectory();
	int partno = dir.getElemPartRefNo();  // パーティション番号

	//FID生成
	UDF_desc257 fid = policy.getFIDPolicy().create(my_image, policy, fidname);
	fid.setElemPartRefNo(dir.getElemPartRefNo());
	fid.getFileChar().setValue(UDF_desc257.DIRECTORY);
	
	// 作成するFID の領域がimmediate である場合
	if((fe.getICBTag().getFlags().getIntValue() & UDF_icbtag.DIRECT) == UDF_icbtag.DIRECT){
	    
	    // 1論理セクタ内に埋め込めないサイズ
	    int fesize = fe.getSize() + fid.getSize();
	    if(my_image.env.LBS < fesize)
		throw new UDF_FileAPIException(fe, "cannot embed FID(over 1 logical sector)");
	}

	//サイズに関するコード
	//残りが2048 - 16になる場合は調整する
	//2.3.4.4
	long dir_size = dir.getLongSize();
	if((dir_size + fid.getSize()) % my_image.env.LBS > my_image.env.LBS - 16){
	    fid.getLenOfImplUse().setValue(32);
	    fid.getDescTag().getDescCRCLen().addValue(32);
	    UDF_regid regid = policy.getRegidPolicy().createDeveloperId(my_image, "UDF_regid");

	    fid.getImplUse().setSize(regid.getSize());
	    fid.getImplUse().appendChild(regid);
	}

	//ファイルリンクカウントを1足す (by issei 06/04/25)
	fe.getFileLinkCount().addValue(1);

	//サイズに関するコード
	//FIDを dirに追加する。FIDの分だけサイズを増やす
	dir.appendChild(fid);
	dir.setSize(dir.getLongSize() + fid.getSize());

	// Extentを伸ばす
	extendDirExtent(fe, dir, fid.getSize());
	
	int type = UDF_icbtag.SHORT_AD;
	if(adtype != UDF_icbtag.DIRECT)
	    type = adtype;

	//新規作成FEを入れる Extentの作成
	UDF_Extent fe2_ext = (UDF_Extent)my_image.createElement("UDF_Extent", null, null);
	fe2_ext.setPartSubno(mirror ? 1 : 0);
	//新規作成directory用 FEの作成
	UDF_FEDesc fe2 = policy.getFEPolicy().create(my_image, policy, UDF_icbtag.T_DIRECTORY);
	//新規作成 FIDを入れる Extentの作成
	UDF_Extent fid2_ext = (UDF_Extent)my_image.createElement("UDF_Extent", null, null);
	fid2_ext.setPartSubno(mirror ? 1 : 0);
	//新規作成directory用 UDF_Directoryの生成
	UDF_Directory dir2 = (UDF_Directory)my_image.createElement("UDF_Directory", null, null);
	//新規作成用FIDの作成
	UDF_desc257 fid2 = policy.getFIDPolicy().create(my_image, policy, "");
	//Extentに領域を割当てる
	fe2_ext.addExtent(ad_fe);
	fid2_ext.addExtent(ad_fid);

	fid2.getFileChar().setValue(UDF_desc257.PARENT | UDF_desc257.DIRECTORY);

	//XMLおよび内部構造の親子関係を構築
	my_image.appendChild(fe2_ext);
	fe2_ext.appendChild(fe2);

	fid2_ext.appendChild(dir2);
	dir2.setSize(fid2.getSize());
	dir2.appendChild(fid2);
	

	//Directoryツリーの関係を構築

	//feの子供が fidであり、fidはfeの子である。
	fe.getDirectoryList().add(fid);
	fid.setReferencedBy(fe);

	//fidは fe2を指していて、fe2は fidに指されている
	fid.setReferenceTo(fe2);
	fe2.getReferencedFID().add(fid);

	//fe2の子供がfid2であり、fid2はfe2の子である。
	fe2.getDirectoryList().add(fid2);
	fid2.setReferencedBy(fe2);

	//fid2は親ディレクトリ(fe)を指しており feはfid2から参照されている
	fid2.setReferenceTo(fe);
	fe.getReferencedFID().add(fid2);

	//fidのICBは fe2を参照する
	String fid_label = 
	    UDF_Label.genFELabel(
				 my_image.env, ad_fe.firstElement().getLbn(), 
				 ad_fe.firstElement().getPartRefNo(),
				 mirror ? 1 : 0);
	fid.getICB().getExtentLen().setAttribute("ref", fid_label + ".len");
	fid.getICB().getExtentLoc().getLogicalBlockNumber().setAttribute("ref", fid_label + ".lbn");
	fid.getICB().getExtentLoc().getPartReferenceNumber().setAttribute("ref", fid_label + ".partno");
	fe2_ext.setAttribute("id", fid_label);

	//fid2のICBは feを参照する
	String fid2_label = fid2.getReferenceTo().getParent().getAttribute("id");
	fid2.getICB().getExtentLen().setAttribute("ref", fid2_label + ".len");
	fid2.getICB().getExtentLoc().getLogicalBlockNumber().setAttribute("ref", fid2_label + ".lbn");
	fid2.getICB().getExtentLoc().getPartReferenceNumber().setAttribute("ref", fid2_label + ".partno");


	//fe2のalloc-descは dir2を指す
	//この時点で dir2に LBNや Partition情報がないので、
	//直接値を指定してラベルを生成する。
	String dir2_label = UDF_Label.genDirectoryLabel(
		my_image.env,
		ad_fid.firstElement().getLbn(),
		ad_fid.firstElement().getPartRefNo(),
		mirror ? 1 : 0,
		false);
	
	fe2.getAllocDesc().setAttribute("ref", dir2_label);
	dir2.setAttribute("id", dir2_label);

	//fe2のalloc-descは directory2の長さであり、その領域は ad_fid2である。
	//またそれを指すADが格納される。
	if(adtype == UDF_icbtag.DIRECT){
	    
	    fid2_ext.truncExtent(fid2.getSize());  // ExtentはFIDの長さピッタシにつめておく
	    fe2.getInfoLen().setValue(fid2.getSize());
	    fe2.getAllocDesc().replaceChild(dir2);
	    fe2.getAllocDesc().setSize((int)dir2.getLongSize());
	}
	else{
	    
	    int adsize = 0;
	    /*
	    for(int i = 0; i < ad_fid.length; i++)
		adsize += ad_fid[i].getSize();
	    */
	    for(Iterator i = ad_fid.iterator() ; i.hasNext(); ){
		adsize += ((UDF_AD)i.next()).getSize();
	    }
	    
	    fe2.getAllocDesc().setSize(adsize);
	    fe2.removeAllAD();
	    fe2.appendAD(ad_fid);  // adlist の追加
	    my_image.appendChild(fid2_ext);
	}
	
	int flags = fe2.getICBTag().getFlags().getIntValue();
	fe2.getICBTag().getFlags().setValue(flags | adtype);
	fe2.getLenOfAllocDesc().setValue(fe2.getAllocDesc().getSize());
	fe2.getDescTag().getDescCRCLen().setValue(fe2.getSize() - 16);

	//fid2のICBは feを参照する

	// 以下の descriptor 間において fid, fid2 の Unique Id を fe2 に合わせる。
	// fe-->directory-->fid-->fe2-->fid2
	long unique_id;

	if(mirror)
	    unique_id = my_image.env.getLogicalVolHeaderDesc().getUniqueId().getLongValue();
	else
	    unique_id = my_image.env.getLogicalVolHeaderDesc().getNextUniqueId();

	fe2.getUniqueId().setValue(unique_id);

	fid.setUniqueId(unique_id);
	fid2.setUniqueId(fid2.getReferenceTo().getUniqueId().getLongValue());
    }
    
    /**
       UDF_Directory の親であるUDF_Extent を広げる。
       このメソッドは、mkfile() / mkdir() からのみコールされる。
       
       @param fe  ファイルエントリ。
       @param dir 伸ばすUDF_Directory。AD のタイプが3のときはnull でも可。
       @param fid AD のタイプが3のときはnull でも可。
    */
    private void extendDirExtent(UDF_FEDesc fe, UDF_Directory dir, int size) throws UDF_Exception{
	
	//UDF_Directoryが大きくなってExtentにおさまらなくなる場合がある。
	//extentより大きくなった場合は Extentを広げる
	if((fe.getICBTag().getFlags().getIntValue() & UDF_icbtag.DIRECT) == UDF_icbtag.DIRECT){
	    
	    fe.getAllocDesc().setSize(fe.getAllocDesc().getSize() + size);
	    fe.getLenOfAllocDesc().setValue(fe.getAllocDesc().getSize());
	}
	else{
	    
	    UDF_Extent dir_ext = (UDF_Extent)dir.getParent();
	
	    if(dir_ext.getLongSize() < dir.getLongSize()){
		/*
		System.err.println(dir_ext.getLongSize());
		System.err.println(dir.getLongSize());
		System.err.println(size);
		*/
		dir_ext.truncExtent(dir.getLongSize());
	    }
	}
    }

    /**
       ファイルのAD の種類を入れ替える。
       現状はディレクトリのAD のみサポートしている。
       
       @param policy  ポリシー
       @param path    AD を入れ替えるファイルのパス。
       @param adtype  入れ替えるAD の種類。下位3ビットのみが使用される。
                      UDF_icbtag.SHORT_AD、UDF_icbtag.LONG_AD、UDF_icbtag.DIRECT の３つのみ指定可。
       
       @throws UDF_FileAPIException path で示されるファイルが存在しない。
                                    またはadtype にDIRECT を指定した場合に、ファイルの内容がFile Entryに収まりきらない。
       @throws IllegalArgumentException adtype がExtentded AD のタイプを指している。
    */
    public void changeADType(UDF_Policy policy, String path, int adtype) throws UDF_Exception, IOException, IllegalArgumentException{
	
	//FIDを追加する FEを取得
	UDF_FEDesc fe = my_image.findFEByPathname(path, MAIN);
	UDF_FEDesc mirr_fe = my_image.findFEByPathname(path, MIRROR);
	if(fe == null && mirr_fe == null)
	    throw new UDF_FileAPIException(my_image, "No such directory:" + path);
	
	//UDF_AD[] out_ad = null;
	UDF_ADList out_ad = null;
	UDF_ADList mirr_out_ad = null;
	if(fe.getAllocType() == UDF_icbtag.DIRECT){
	    if(fe.getICBFileType() == UDF_icbtag.T_DIRECTORY || fe.getICBFileType() == UDF_icbtag.T_SDIRECTORY){
		out_ad = policy.getAllocDescPolicy().alloc(my_image, fe.getAllocDesc().getLongSize(), adtype);
	    }
	    else{
		out_ad = policy.getAllocDataPolicy().alloc(my_image, fe.getAllocDesc().getLongSize(), adtype);
	    }
	}
	if(out_ad != null)
	    mirr_out_ad = out_ad.duplicateList();
	changeADType(policy, mirr_fe, adtype, mirr_out_ad, true);
	changeADType(policy, fe, adtype, out_ad, false);
	
    }
    
    /**
       ファイルのAD の種類を入れ替える。
       
       @param policy  ポリシー
       @param fe      AD を入れ替えるファイルのファイルエントリ。
       @param adtype  入れ替えるAD の種類。下位3ビットのみが使用される。
                      UDF_icbtag.SHORT_AD、UDF_icbtag.LONG_AD、UDF_icbtag.DIRECT の３つのみ指定可。
       @param out_ad  ADがimmediateである場合に、外へ出すための領域を示すADリスト。
       
       @throws UDF_FileAPIException path で示されるファイルが存在しない。
                                    またはadtype にDIRECT を指定した場合に、ファイルの内容がFile Entryに収まりきらない。
       @throws IllegalArgumentException adtype がExtentded AD のタイプを指している。
    */
    public void changeADType(UDF_Policy policy, UDF_FEDesc fe, int adtype, UDF_ADList out_ad, boolean mirror)
	throws UDF_Exception, IOException, IllegalArgumentException{

	if(fe == null)
	    return;

	int flags = fe.getAllocType();//getICBTag().getFlags().getIntValue() & 0x03;
	int adsize = 0;
	
	if(flags == adtype)
	    return;
	else if(flags == UDF_icbtag.DIRECT){
	    
	    // 外へ出すExtent の作成
	    UDF_Extent outside_ext = (UDF_Extent)my_image.createElement("UDF_Extent", null, null);
	    outside_ext.addExtent(out_ad);

	    // AllocDescの中身を……
	    UDF_ElementBase[] child = fe.getAllocDesc().getChildren();

	    // EXTENTに追加
	    outside_ext.appendChildren(child);

	    long sumof_extlen = 0; // AD のExtentLen の合計
	    for(Iterator it=out_ad.iterator() ; it.hasNext() ;){
		UDF_AD ad = (UDF_AD)it.next();
		sumof_extlen += ad.getLen();
		adsize += ad.getSize();
	    }

	    fe.getInfoLen().setValue(sumof_extlen);
	    if(fe.getObjectSize() != null){
//		long objsize = fe.getObjectSize().getLongValue();
//		fe.getObjectSize().setValue((objsize - fe.getAllocDesc().getSize()) + sumof_extlen);
		fe.getObjectSize().setValue(sumof_extlen);
	    }

	    // appendAD の前にタイプを設定しておく
	    fe.getICBTag().getFlags().setValue((flags & 0xfffffffc) | adtype);
	    // 空にした上でadlist を追加
	    fe.getAllocDesc().removeAllChildren();
	    fe.getAllocDesc().appendChildren(out_ad);
	    fe.getAllocDesc().setSize(adsize);

	    fe.getLenOfAllocDesc().setValue(adsize);

	    fe.getLogicalBlocksRecorded().setValue(UDF_Util.align(fe.getADSize(), my_image.env.LBS) / my_image.env.LBS);

	    if(fe.getICBFileType() == UDF_icbtag.T_DIRECTORY || fe.getICBFileType() == UDF_icbtag.T_SDIRECTORY){
		//ディレクトリの構成はかわらないから不要と思われ
	    }
	    else{
		String data_lbl = UDF_Label.genExtentLabel(outside_ext);
		outside_ext.setAttribute("id", data_lbl);
		fe.getAllocDesc().setAttribute("ref", data_lbl + ".extents");
		
		// 05/08/09 HENC用のXML変換のため追加。パディングを入れる。 by seta
		if(fe.getICBFileType() == 252){
		    
		    UDF_pad pad = new UDF_pad(outside_ext, null, null, outside_ext.getSize());
		    pad.setSize(0);
		    outside_ext.appendChild(pad);
		}
	    }

	    my_image.appendChild(outside_ext);
	}
	else if(flags == UDF_icbtag.SHORT_AD || flags == UDF_icbtag.LONG_AD || flags == UDF_icbtag.EXT_AD){
	    UDF_ADList adlist = fe.getADList();
	    
	    //AD -> Directにする
	    if(adtype == UDF_icbtag.DIRECT){
		UDF_Extent out_ext = null;
		
		String ref = fe.getAllocDesc().getAttribute("ref");
		if(ref != null){
		    ref = UDF_Util.car(ref, '.');

		    UDF_Element ref_ext = my_image.findById(ref);

		    if(ref_ext != null){
			fe.getAllocDesc().setSize((int)fe.getADSize());
			fe.getLenOfAllocDesc().setValue(fe.getADSize());


			//directoryのとき
			if(fe.getICBFileType() == UDF_icbtag.T_DIRECTORY || fe.getICBFileType() == UDF_icbtag.T_SDIRECTORY){
			    ref_ext = ref_ext.getParent();
			    ref_ext.getParent().removeChild(ref_ext);
			    fe.getAllocDesc().replaceChildren(ref_ext.getChildren());
			}
			//fileのとき
			else{
			    try{
				ref_ext.getParent().removeChild(ref_ext);
			    }
			    catch(org.w3c.dom.DOMException e){
				//MetadataPartitionMapでは既に削除されている場合がある
			    }
			    //複製して追加する
			    //何故なら、
			    //mainと mirrorの 2つのFEが同じ DATAを差している場合があるからで
			    //ある
			    fe.getAllocDesc().replaceChildren(ref_ext.duplicateElement().getChildren());
			    fe.getAllocDesc().removeAttribute("ref");
			}
		    }
		}
		fe.getICBTag().getFlags().setValue((flags & 0xfffffffc) | adtype);
		fe.getLogicalBlocksRecorded().setValue(0);  // immediate のときは0にする
	    }
	    else{

		// ext_ad はナイので、long_ad かshort_ad へ変える
		String ad_str = (flags == UDF_icbtag.SHORT_AD) ? "UDF_long_ad": "UDF_short_ad";

		//UDF_AD[] newad = new UDF_AD[adlist.size()];
		UDF_ElementList newlist = new UDF_ElementList();
		for(Iterator i = adlist.iterator() ; i.hasNext() ; ){
		    
		    UDF_AD newad = (UDF_AD) my_image.createElement(ad_str, null, null);
		    newad.setDefaultValue();
		    
		    UDF_AD ad = (UDF_AD)i.next();
		    newad.setLbn(ad.getLbn());
		    newad.setLen(ad.getLen());
		    newad.setFlag(ad.getFlag());
		    newad.setPartRefNo(ad.getPartRefNo());
		    
		    adsize += newad.getSize();

		    newlist.add(newad);
		}
		
		// appendAD の前にタイプを設定しておく
		fe.getICBTag().getFlags().setValue((flags & 0xfffffffc) | adtype);
		// adlist を入れ替える
		fe.removeAllAD();
		fe.appendAD(newlist);
		
		fe.getAllocDesc().replaceChildren(newlist);
	    }
	}
	else{
	    throw new UDF_DataException(my_image, "Extended AD shall not be recorded.");
	}
	
	// AD の変更による各種値の設定
	//fe.getAllocDesc().setSize(adsize); // AD の合計サイズ
	//fe.getLenOfAllocDesc().setValue(fe.getAllocDesc().getSize()); // LenOfAD の値
	fe.getDescTag().getDescCRCLen().setValue(fe.getSize() - 16); // DescCRCLen の値
    }
    
    /**
       ファイルに実際の fileを割りつける。

       @param policy
       @param path	ファイル名
       @param filename	埋めこむファイル

       ファイルの長さは埋めこむファイルの長さに自動で調整される。
     */
    public void attachDataToFile(UDF_Policy policy, String path, String filename) throws UDF_Exception, IOException{
	//FEを取得
	UDF_FEDesc fe = my_image.findFEByPathname(path, MAIN);
	UDF_FEDesc mirr_fe = my_image.findFEByPathname(path, MIRROR);
	if(fe == null && mirr_fe == null)
	    throw new UDF_FileAPIException(my_image, "No such file or directory:" + path);

	attachDataToFile(policy, mirr_fe, filename, true);
	attachDataToFile(policy, fe, filename, false);
    }
    /**
       ファイルに実際の fileを割りつける。

       @param policy
       @param path	ファイル名
       @param filename	埋めこむファイル

       ファイルのサイズがExtentより大きい場合、Extentのサイズは自動で調整されず、<UDF_Data>の length属性がExtentの長さになる。

    */
    private void attachDataToFile(UDF_Policy policy, UDF_FEDesc fe, String filename, boolean mirror) throws UDF_Exception, IOException{
	if(fe == null)
	    return;

	String fe_label = fe.getAllocDesc().getAttribute("ref");
	//int pos = fe_label.indexOf('.');
	fe_label = UDF_Util.car(fe_label, '.');//fe_label.substring(0, pos);

	UDF_Extent data_ext = (UDF_Extent)my_image.findById(fe_label);
	UDF_Data data = (UDF_Data)my_image.createElement("UDF_Data", null, null);

	long file_size = new File(filename).length();
	//resizeFile(policy, path, file_size);

	data.setSourceFile(filename);
	if(file_size >= data_ext.getLongSize())
	    file_size = data_ext.getLongSize();

	data.setLength(file_size);
	data.setSourceOffset(0L);
	data_ext.removeAllChildren();
	data_ext.appendChild(data);
    }

    /**
       ファイルやディレクトリのADに FLAG3のADがあればそれを削除する。 FLAG3のADがなければ何もしない。

       FEのAllocType が3のときは何もしない

       desc258が複数個チェーンされているときは、最後のdesc258が削除される。

       削除された desc258の ADの内容は 直前の ADDesc(desc261, desc266 or desc258)にコピーされる。
     */
    public void unchainAllocDesc(UDF_Policy policy, String path) throws UDF_Exception, IOException{
	//FEを取得
	UDF_FEDesc fe = my_image.findFEByPathname(path, MAIN);
	UDF_FEDesc mirr_fe = my_image.findFEByPathname(path, MIRROR);
	if(fe == null && mirr_fe == null)
	    throw new UDF_FileAPIException(my_image, "No such file or directory" + path);

	unchainAllocDesc(policy, mirr_fe, true);
	unchainAllocDesc(policy, fe, false);
    }
    /**
       ファイルやディレクトリのADに FLAG3のADがあればそれを削除する。 FLAG3のADがなければ何もしない。

       FEのAllocType が3のときは何もしない

       desc258が複数個チェーンされているときは、最後のdesc258が削除される。

       削除された desc258の ADの内容は 直前の ADDesc(desc261, desc266 or desc258)にコピーされる。
     */
    public void unchainAllocDesc(UDF_Policy policy, UDF_FEDesc fe0, boolean mirror) throws UDF_Exception, IOException{
	if(fe0 == null)
	    return;
	if(fe0.getAllocType() == 3)
	    return;

	UDF_ADDesc fe = fe0.getLastADDesc();
	UDF_ADDesc fe_prev = fe.getPrevADDesc();

	if(fe_prev == null)
	    return;

	UDF_AD[] ad_prev = fe_prev.getAD();

	UDF_AD ad3 = ad_prev[ad_prev.length - 1];

	//このチェックは不要だが、念のため
	if(ad3.getFlag() != 3)
	    return;

	//fe_prevの最後の ADを削除し、かわりに feのADを追加する
	fe_prev.getAllocDesc().removeChild(ad3);
	fe_prev.getAllocDesc().appendChildren(fe.getAllocDesc().getChildren());
	
	//サイズ調整
	fe.getAllocDesc().setSize(fe.getAllocDesc().getSize() - ad3.getSize() + fe.getAllocDesc().getSize());

	//リンクを切る
	fe.setNextADDesc(null);
	fe_prev.setPrevADDesc(null);

	//feおよび、それが入っている UDF_Extentを削除する。
	my_image.removeChild(fe.getParent());
    }

    /**
       ファイルやディレクトリのADに FLAG3のADを追加し、desc258を作成する

       FEのAllocType が3のときは何もしない

       desc258が複数個チェーンされているときは、最後のdescに追加される。

       追加された desc258の len-of-alloc-descは0である。

     */
    public void chainAllocDesc(UDF_Policy policy, String path) throws UDF_Exception, IOException{
	//FEを取得
	UDF_FEDesc fe = my_image.findFEByPathname(path, MAIN);
	UDF_FEDesc mirr_fe = my_image.findFEByPathname(path, MIRROR);
	if(fe == null && mirr_fe == null)
	    throw new UDF_FileAPIException(my_image, "No such file or directory" + path);

	UDF_ADList ad_desc258 = policy.getAllocDescPolicy().alloc(my_image, my_image.env.LBS, fe.getAllocType());
	ad_desc258.firstElement().setFlag(3);
	UDF_ADList mirr_ad_desc258 = ad_desc258.duplicateList();

	chainAllocDesc(policy, mirr_fe, mirr_ad_desc258, true);
	chainAllocDesc(policy, fe, ad_desc258, false);
    }
    /**
       ファイルやディレクトリのADに FLAG3のADを追加し、desc258を作成する

       FEのAllocType が3のときは何もしない

       desc258が複数個チェーンされているときは、最後のdescに追加される。

       追加された desc258の len-of-alloc-descは0である。

     */
    public void chainAllocDesc(UDF_Policy policy, UDF_FEDesc fe0, UDF_ADList ad_desc258, boolean mirror) throws UDF_Exception, IOException{
	if(fe0 == null)
	    return;

	if(fe0.getAllocType() == 3)
	    return;

	UDF_ADDesc fe = fe0.getLastADDesc();

	UDF_Extent desc258_ext = (UDF_Extent)my_image.createElement("UDF_Extent", null, null);
	desc258_ext.setPartSubno(mirror ? 1 : 0);
	UDF_desc258 desc258 = (UDF_desc258)my_image.createElement("UDF_desc258", null, null);
	desc258.setDefaultValue();

        UDF_tag descTag = policy.getDescTagPolicy().create(my_image, policy);
	descTag.getTagId().setValue(258);
	descTag.getDescCRCLen().setValue(desc258.getSize() - 16);
	desc258.setDescTag(descTag);

	desc258_ext.addExtent(ad_desc258);

	String label = UDF_Label.genExtentLabel(desc258_ext);
	desc258_ext.setAttribute("id", label);
	ad_desc258.firstElement().getExtentLen().setAttribute("ref", label + ".len");
	ad_desc258.firstElement().getExtentLbn().setAttribute("ref", label + ".lbn");
	if(ad_desc258.firstElement().getExtentPartRefNo() != null)
	    ad_desc258.firstElement().getExtentPartRefNo().setAttribute("ref", label + ".partno");

	fe.normalize();
	fe.appendAD(ad_desc258);

	fe.setNextADDesc(desc258);
	desc258.setPrevADDesc(fe);

	desc258_ext.appendChild(desc258);
	my_image.appendChild(desc258_ext);

    }

    /**
       サイズ 0のファイルを作成する。

       @param policy Policy
       @param path 作成したいファイル名（rootからのパス含む）。
       @param adtype ADのタイプ
       @throws UDF_EntryExistException 既に同名のファイル／ディレクトリが存在している。
       @throws UDF_FielAPIException ファイルまでのパスが存在しない。空きがない.l

      実際のデータとのひもつけは attachDataToFile で割り当てる。
    */
    public void mkfile(UDF_Policy policy, String path, int adtype) throws UDF_Exception, IOException{
	String dirname = UDF_Util.dirname(path);
	String fidname = UDF_Util.fidname(path);

	//新規作成用FEの領域を確保
	UDF_ADList ad_fe = policy.getAllocDescPolicy().alloc(my_image, my_image.env.LBS, adtype);
	if(ad_fe == null){
	    throw new UDF_FileAPIException(my_image, "No space:" + path);
	}

	UDF_ADList ad_data = policy.getAllocDataPolicy().alloc(my_image, 0, adtype);
	if(ad_data == null){
	    throw new UDF_FileAPIException(my_image, "No space:" + path);
	}

	UDF_FEDesc fe = my_image.findFEByPathname(dirname, MAIN);
	UDF_FEDesc mirr_fe = my_image.findFEByPathname(dirname, MIRROR);
	if(fe == null && mirr_fe == null)
	    throw new UDF_FileAPIException(my_image, "No such directory:" + dirname);

	//FEのタイプを調べ、ディレクトリでなければエラー
	if(fe.getICBFileType() != UDF_icbtag.T_DIRECTORY &&
	   fe.getICBFileType() != UDF_icbtag.T_SDIRECTORY)
	    throw new UDF_FileAPIException(my_image, "Not directory:" + dirname);

	// UDF2.50 以降で、Mirror Partition があった場合
	// 先に mirrorを作る。(mirrorだと unique IDが1足されない)
	if(my_image.env.getRootFE(1) != null){
	    //mainと同じものを mirrorに追加することはできないので、
	    //複製しておく。
	    UDF_ADList mirr_ad_fe = ad_fe.duplicateList();
	    UDF_ADList mirr_ad_data = ad_data.duplicateList();
	    mkfile(policy, mirr_fe, path, adtype, mirr_ad_fe, mirr_ad_data, true);
	}
	mkfile(policy, fe, path, adtype, ad_fe, ad_data, false);
	my_image.env.getLVIDImplUse().addFiles(1);
    }

    /**
       サイズ 0のファイルを作成する。

       @param policy Policy
       @param path 作成したいファイル名（rootからのパス含む）。
       @param adtype ADのタイプ
       @param ad_fe  新規に作成するFEを置く位置	
       @param mirror  Mirrorかどうか
       @throws UDF_EntryExistException 既に同名のファイル／ディレクトリが存在している。
       @throws UDF_FielAPIException ファイルまでのパスが存在しない。

       実際のデータとのひもつけは attachDataToFile で割り当てる。
    */
    private void mkfile(UDF_Policy policy, UDF_FEDesc fe, String path, int adtype, UDF_ADList ad_fe, UDF_ADList ad_data, boolean mirror) throws UDF_Exception, IOException{
	if(fe == null)
	    return;
	String dirname = UDF_Util.dirname(path);
	String fidname = UDF_Util.fidname(path);

	//FIDを追加する UDF_Directoryの取得
	UDF_Directory dir = fe.getUDFDirectory();

	//FID生成
	UDF_desc257 fid = policy.getFIDPolicy().create(my_image, policy, fidname);
	fid.setElemPartRefNo(dir.getElemPartRefNo());
	
	// 作成するFID の領域がimmediate である場合
	if((fe.getICBTag().getFlags().getIntValue() & UDF_icbtag.DIRECT) == UDF_icbtag.DIRECT){
	    
	    // 1論理セクタ内に埋め込めないサイズ
	    int fesize = fe.getSize() + fid.getSize();
	    if(my_image.env.LBS < fesize)
		throw new UDF_FileAPIException(fe, "cannot embed FID(over 1 logical sector)");
	}
	
	//サイズに関するコード
	//残りが2048 - 16になる場合は調整する
	//2.3.4.4
	long dir_size = dir.getLongSize();
	if((dir_size + fid.getSize()) % my_image.env.LBS > my_image.env.LBS - 16){
	    fid.getLenOfImplUse().setValue(32);
	    fid.getDescTag().getDescCRCLen().addValue(32);
	    UDF_regid regid = policy.getRegidPolicy().createDeveloperId(my_image, "UDF_regid");

	    fid.getImplUse().setSize(regid.getSize());
	    fid.getImplUse().replaceChild(regid);
	}

	//FIDを dirに追加する。FIDの分だけサイズを増やす
	dir.appendChild(fid);
	dir.setSize(dir.getLongSize() + fid.getSize());

	// Extentを伸ばす
	extendDirExtent(fe, dir, fid.getSize());
	
	int type = UDF_icbtag.SHORT_AD;
	if(adtype != UDF_icbtag.DIRECT)
	    type = adtype;
	
	//新規作成FEを入れる Extentの作成
	UDF_Extent fe2_ext = (UDF_Extent)my_image.createElement("UDF_Extent", null, null);
	fe2_ext.setPartSubno(mirror ? 1 : 0);
	//新規作成directory用 FEの作成
	UDF_FEDesc fe2 = policy.getFEPolicy().create(my_image, policy, UDF_icbtag.T_FILE);

	//Extentに領域を割当てる
	fe2_ext.addExtent(ad_fe);

	//XMLおよび内部構造の親子関係を構築
	my_image.appendChild(fe2_ext);
	fe2_ext.appendChild(fe2);

	//Directoryツリーの関係を構築

	//feの子供が fidであり、fidはfeの子である。
	fe.getDirectoryList().add(fid);
	fid.setReferencedBy(fe);

	//fidは fe2を指していて、fe2は fidに指されている
	fid.setReferenceTo(fe2);
	fe2.getReferencedFID().add(fid);

	//fidのICBは fe2を参照する
	String fid_label = 
	    UDF_Label.genFELabel(
				 my_image.env, ad_fe.firstElement().getLbn(), 
				 ad_fe.firstElement().getPartRefNo(),
				 mirror ? 1 : 0);
	fid.getICB().getExtentLen().setAttribute("ref", fid_label + ".len");
	fid.getICB().getExtentLoc().getLogicalBlockNumber().setAttribute("ref", fid_label + ".lbn");
	fid.getICB().getExtentLoc().getPartReferenceNumber().setAttribute("ref", fid_label + ".partno");
	fe2_ext.setAttribute("id", fid_label);

	int flags = fe2.getICBTag().getFlags().getIntValue();
	fe2.getICBTag().getFlags().setValue(flags | adtype);
	fe2.getLenOfAllocDesc().setValue(fe2.getAllocDesc().getSize());
	fe2.getDescTag().getDescCRCLen().setValue(fe2.getSize() - 16);

	// 以下の descriptor 間において fid, の Unique Id を fe2 に合わせる。
	// fe-->directory-->fid-->fe2
	long unique_id;

	if(mirror)
	    unique_id = my_image.env.getLogicalVolHeaderDesc().getUniqueId().getLongValue();
	else
	    unique_id = my_image.env.getLogicalVolHeaderDesc().getNextUniqueId();

	fe2.getUniqueId().setValue(unique_id);

	fid.setUniqueId(unique_id);

	//DATA部のあるパーティションもまた、metadata partitionに入っているなら
	//作る。そうでなければ2つ作る必要はない。
	UDF_Extent data_ext = (UDF_Extent)my_image.createElement("UDF_Extent", null, null);
	String data_label = "DATA" + path.hashCode() + (my_image.env.hasMirrorPartmap(ad_data.firstElement().getPartRefNo()) ? "_1" : "");
	if(my_image.findById(data_label) == null){
	    data_ext.addExtent(ad_data);
	    my_image.appendChild(data_ext);
	    data_ext.setAttribute("id", data_label);
	}

	fe2.getAllocDesc().setAttribute("ref", data_label + ".extents");
	fe2.appendAD(ad_data);

    }

    /**
      リンクファイルを作成する。

      @param policy ポリシー
      @param name1 リンク対象となるファイル名（rootからのパス含む）。
      @param name2 作成したいリンクファイル名（rootからのパス含む）。
      @throws UDF_EntryExistException 既に同名のファイル／ディレクトリが存在している。
      @throws UDF_FileAPIException ファイルまでのパスが存在しない。
    */
    public void link(UDF_Policy policy, String name1, String name2) throws UDF_Exception, IOException{
	UDF_FEDesc fe = my_image.findFEByPathname(name1, MAIN);
	UDF_FEDesc mirr_fe = my_image.findFEByPathname(name1, MIRROR);
	if(fe == null && mirr_fe == null)
	    throw new UDF_FileAPIException(my_image, "No such file or directory:" + name1);

	if(my_image.findFEByPathname(name2, MAIN) != null)
	    throw new UDF_FileAPIException(my_image, "File exists:" + name2);

	if(my_image.findFEByPathname(name2, MIRROR) != null)
	    throw new UDF_FileAPIException(my_image, "File exists:" + name2);

	String dirname2 = UDF_Util.dirname(name2);
	String fidname2 = UDF_Util.fidname(name2);

	UDF_FEDesc fe2 = my_image.findFEByPathname(dirname2, MAIN);
	UDF_FEDesc mirr_fe2 = my_image.findFEByPathname(dirname2, MIRROR);
	if(fe2 == null && mirr_fe2 == null)
	    throw new UDF_FileAPIException(my_image, "No such file or directory:" + name2);

	if(mirr_fe != null)
	    link(policy, mirr_fe, name2, true);
	link(policy, fe, name2, false);
	my_image.env.getLVIDImplUse().addFiles(1);
    }

    /**
      リンクファイルを作成する。

      @param policy ポリシー
      @param fe リンク対象となるファイルエントリ
      @param name2 作成したいリンクファイル名（rootからのパス含む）。
      @throws UDF_EntryExistException 既に同名のファイル／ディレクトリが存在している。
      @throws UDF_FileAPIException ファイルまでのパスが存在しない。
    */
    private void link(UDF_Policy policy, UDF_FEDesc fe, String name2, boolean mirror) throws IOException, UDF_Exception{
	String dirname2 = UDF_Util.dirname(name2);
	String fidname2 = UDF_Util.fidname(name2);

	UDF_FEDesc fe2 = my_image.findFEByPathname(dirname2, mirror ? 1 : 0);

	UDF_Directory dir = fe2.getUDFDirectory();
	UDF_desc257 fid = policy.getFIDPolicy().create(my_image, policy, fidname2);

	if(fe.getICBFileType() == UDF_icbtag.T_DIRECTORY || fe.getICBFileType() == UDF_icbtag.T_SDIRECTORY)
	    fid.getFileChar().setValue(UDF_desc257.DIRECTORY);

	long unique_id;

	if(mirror)
	    unique_id = my_image.env.getLogicalVolHeaderDesc().getUniqueId().getLongValue();
	else
	    unique_id = my_image.env.getLogicalVolHeaderDesc().getNextUniqueId();

	fid.setUniqueId(unique_id);

	//サイズに関するコード
	//残りが2048 - 16になる場合は調整する
	//2.3.4.4
	long dir_size = dir.getLongSize();
	if((dir_size + fid.getSize()) % my_image.env.LBS > my_image.env.LBS - 16){
	    fid.getLenOfImplUse().setValue(32);
	    fid.getDescTag().getDescCRCLen().addValue(32);
	    UDF_regid regid = policy.getRegidPolicy().createDeveloperId(my_image, "UDF_regid");

	    fid.getImplUse().setSize(regid.getSize());
	    fid.getImplUse().appendChild(regid);
	}

	dir.appendChild(fid);

	dir.incSize(fid.getSize());
	extendDirExtent(fe, dir, fid.getSize());

	fid.setReferenceTo(fe);
	fe.getReferencedFID().add(fid);

	fid.setReferencedBy(fe2);
	fe2.getDirectoryList().add(fid);

	fe.getFileLinkCount().addValue(1);

	String fid_label = fe.getParent().getAttribute("id");
	if(fid_label != null && fid_label.length() > 0){
	    fid.getICB().getExtentLen().setAttribute("ref", fid_label + ".len");
	    fid.getICB().getExtentLoc().getLogicalBlockNumber().setAttribute("ref", fid_label + ".lbn");
	    fid.getICB().getExtentLoc().getPartReferenceNumber().setAttribute("ref", fid_label + ".partno");
	}
    }

    /**
      ファイル／ディレクトリを削除する。
      FEのリンクカウントが0になれば、FEも削除する。

      @param path 削除したいファイル／ディレクトリ名（rootからのパス含む）。
      @throws UDF_FileAPIException ファイル／ディレクトリのパスが存在しない。
    */
    public void remove(UDF_Policy policy, String path) throws IOException, UDF_Exception{
	UDF_desc257 fid = my_image.findFIDByPathname(path, MAIN);
	UDF_desc257 mirr_fid = my_image.findFIDByPathname(path, MIRROR);

	if(fid == null && mirr_fid == null)
	    throw new UDF_FileAPIException(my_image, "No such file or directory:" + path);


	if(mirr_fid != null)
	    remove(policy, mirr_fid, true);
	remove(policy, fid, false);

	if((fid.getFileChar().getIntValue() & UDF_desc257.DIRECTORY) != 0)
	    my_image.env.getLVIDImplUse().addDirectories(-1);
	else
	    my_image.env.getLVIDImplUse().addFiles(-1);
    }

    /**
      ファイル／ディレクトリを削除する。
      FEのリンクカウントが0になれば、FEも削除する。

      @param policy ポリシー
      @param fid 削除したいFID
      @param mirror  Mirrorかどうか
      @throws UDF_FileAPIException ファイル／ディレクトリのパスが存在しない。
    */
    public void remove(UDF_Policy policy, UDF_desc257 fid, boolean mirror) throws IOException, UDF_Exception{
	UDF_Directory fid_dir = (UDF_Directory)fid.getParent();
	UDF_Extent fid_ext = (UDF_Extent)fid_dir.getParent();
	
	fid_dir.removeChild(fid);
	fid_dir.incSize(-fid.getSize());
	fid_ext.truncExtent(fid_dir.getLongSize());

	UDF_FEDesc fe = (UDF_FEDesc)fid.getReferenceTo();
	UDF_Extent fe_ext = (UDF_Extent)fe.getParent(); 
	
	if(fe.getFileLinkCount().addValue(-1) == 0){
	    my_image.removeChild(fe_ext);
	    UDF_Extent data_ext = fe.getReferenceExtent();
	    if(data_ext != null)
		my_image.removeChild(data_ext);
	}
	my_image.resetFEHash();
    }

    /**
      ファイル／ディレクトリ名を変更する。
      同一ディレクトリでないと変更できない。
      

      @param src 元のファイル／ディレクトリ名。
      @param dst 希望するファイル／ディレクトリ名。

      @throws UDF_EntryExistException 既に同名のファイル／ディレクトリが存在している。
      @throws UDF_FileAPIException ファイル／ディレクトリのパスが存在しない。
    */
    public void rename(UDF_Policy policy, String src, String dst) throws IOException, UDF_Exception{
	UDF_desc257 fid = my_image.findFIDByPathname(src, MAIN);
	UDF_desc257 mirr_fid = my_image.findFIDByPathname(src, MIRROR);
	if(fid == null && mirr_fid == null)
	    throw new UDF_FileAPIException(my_image, "No such file or directory:" + src);

	UDF_desc257 fid2 = my_image.findFIDByPathname(dst, MAIN);
	UDF_desc257 mirr_fid2 = my_image.findFIDByPathname(dst, MIRROR);
	if(fid2 != null || mirr_fid2 != null)
	    throw new UDF_FileAPIException(my_image, "File already exist:" + dst);

	UDF_FEDesc fe2 = my_image.findFEByPathname(UDF_Util.dirname(dst), MAIN);
	UDF_FEDesc mirr_fe2 = my_image.findFEByPathname(UDF_Util.dirname(dst), MIRROR);

	if(fid.getReferencedBy() != fe2)
	    throw new UDF_FileAPIException(my_image, "Cannot rename:" + src);
	
	if(mirr_fid != null){
	    if(mirr_fid.getReferencedBy() != mirr_fe2)
		throw new UDF_FileAPIException(my_image, "Cannot rename:" + src);
	}

	dst = UDF_Util.fidname(dst);

	if(mirr_fid != null)
	    rename(policy, mirr_fid, dst, true);
	rename(policy, fid, dst, false);
	my_image.resetFEHash();
    }

    /**
      ファイル／ディレクトリ名を変更する。

      @param fid 元のファイル／ディレクトリ名。
      @param fidname 希望するファイル／ディレクトリ名。

      @throws UDF_EntryExistException 既に同名のファイル／ディレクトリが存在している。
      @throws UDF_FileAPIException ファイル／ディレクトリのパスが存在しない。
    */
    public void rename(UDF_Policy policy, UDF_desc257 fid, String fidname, boolean mirror) throws IOException, UDF_Exception{
	long old_fid_size = fid.getSize();

	UDF_Directory fid_dir = (UDF_Directory)fid.getParent();
	UDF_Extent fid_ext = (UDF_Extent)fid_dir.getParent();
	
        UDF_Encoding enc = policy.getEncodingPolicy().create(my_image, policy, fidname);
        fid.getFileId().setEncoding(enc);
        fid.getFileId().setData(enc.toBytes(fidname));
        int len = fid.getFileId().getSize();
        fid.getLenOfFileId().setValue(len);

	fid.getPadding().setSize(0);
        fid.getPadding().setSize((int)(UDF_Util.align(fid.getSize(), 4)) - fid.getSize());

	long new_fid_size = fid.getSize();

	fid_dir.incSize((int)(new_fid_size - old_fid_size));
	fid_ext.truncExtent(fid_dir.getLongSize());
    }

    /**
       FEおよびFEのAD部を割り当て直す。
       
       1) FEを連続領域に置く
       2) FEのAD部を連続領域に置く。

      @param policy ポリシー
      @param path 希望するファイル／ディレクトリ名。
      @throws UDF_EntryExistException 既に同名のファイル／ディレクトリが存在している。
      @throws UDF_FileAPIException ファイル／ディレクトリのパスが存在しない。
    */
    public void realloc(UDF_Policy policy, String path) throws UDF_Exception, IOException{
	UDF_FEDesc fe = my_image.findFEByPathname(path, MAIN);
	UDF_FEDesc mirr_fe = my_image.findFEByPathname(path, MIRROR);
	if(fe == null && mirr_fe == null)
	    throw new UDF_FileAPIException(my_image, "No such file or directory" + path);

	UDF_ADList ad_fe = null;
	//FEが入っている extentが断片化しているかどうか
	//チェック
	UDF_Extent fe_ext = (UDF_Extent)fe.getParent();
	if(fe_ext.isFragment()){
	    ad_fe = policy.getAllocDescPolicy().alloc(my_image, fe_ext.getLongSize(), 1);
	    if(ad_fe == null)
		throw new UDF_FileAPIException(my_image, "No space:");
	    
	    if(ad_fe.size() != 1){
		policy.getAllocDescPolicy().free(my_image, ad_fe);
		throw new UDF_FileAPIException(my_image, "No space:");
	    }
	}

	UDF_ADList ad_data = null;
	UDF_Extent ext = fe.getReferenceExtent();
	if(ext != null && ext.isFragment()){
	    if(fe.isDirectory())
		ad_data = policy.getAllocDescPolicy().alloc(my_image, ext.getLongSize(), fe.getAllocType());
	    else
		ad_data = policy.getAllocDataPolicy().alloc(my_image, ext.getLongSize(), fe.getAllocType());

	    if(ad_data == null)
		throw new UDF_FileAPIException(my_image, "No space:");

	    if(ad_data.size() != 1){
		policy.getAllocDescPolicy().free(my_image, ad_data);
		throw new UDF_FileAPIException(my_image, "No space:");
	    }
	}

	if(mirr_fe != null)
	    realloc(policy, 
		    mirr_fe, ad_fe != null ? ad_fe.duplicateList() : null,
		    ad_data != null ? ad_data.duplicateList() : null, 
		    true);
	realloc(policy, fe, ad_fe, ad_data, false);
    }

    /**
      FEおよびFEのAD部を割り当てを適用する。

      @param policy ポリシー
      @param fe 希望するファイルエントリ
      @param ad_fe FEの割り当て
      @param ad_data FEのADに反映される割り当て
    */
    public void realloc(UDF_Policy policy, UDF_FEDesc fe, UDF_ADList ad_fe, UDF_ADList ad_data, boolean mirror) throws UDF_Exception, IOException{
	//FEが入っている extentが断片化しているかどうか
	//チェック
	UDF_Extent fe_ext = (UDF_Extent)fe.getParent();
	if(fe_ext.isFragment()){
	    fe_ext.truncExtent(0);
	    fe_ext.addExtent(ad_fe);
	}
	//DIRECTならここで終了
	if(fe.getAllocType() == 3)
	    return;

	UDF_Extent ext = fe.getReferenceExtent();

	if(ext.isFragment()){
	    ext.truncExtent(0);
	    ext.addExtent(ad_data);
	}

    }

    /**
      ファイル／ディレクトリのサイズを変更する。

      @param path サイズを変更したいファイル/ディレクトリの名称（rootからのパス含む）。
      @param size 希望するサイズ。
      @throws UDF_FileAPIException ファイルのパスが存在しない。
      @throws IllegalArgumentException サイズが異常。
    */
    public void resize(UDF_Policy policy, String path, long size) throws UDF_Exception, IOException{
	UDF_FEDesc fe = my_image.findFEByPathname(path, MAIN);
	UDF_FEDesc mirr_fe = my_image.findFEByPathname(path, MIRROR);


	UDF_Extent data_ext = fe.getReferenceExtent();
	UDF_Extent mirr_data_ext = fe.getReferenceExtent();

	UDF_ADList ad = null;
	UDF_ADList mirr_ad = null;
	if(fe.getAllocType() == 3)
	    ;
	else if(data_ext.getLongSize() < size){
	    //あらたな割付が必用か？
	    if(UDF_Util.align(data_ext.getLongSize(), my_image.env.LBS) < UDF_Util.align(size, my_image.env.LBS)){
		long alloc_size = size - data_ext.getLongSize();
		if(fe.isDirectory())
		    ad = policy.getAllocDescPolicy().alloc(my_image, alloc_size, fe.getICBFlags() & 0x7);
		else
		    ad = policy.getAllocDataPolicy().alloc(my_image, alloc_size, fe.getICBFlags() & 0x7);
		mirr_ad = ad.duplicateList();
	    }
	}
	
	if(my_image.env.getRootFE(1) != null)
	    resize(policy, mirr_fe, size, mirr_ad, true);
	resize(policy, fe, size, ad, false);
    }

    /**
      ファイルのサイズを変更する。

      @param policy ポリシー
      @param fe サイズを変更したいファイルエントリ
      @param size 希望するサイズ。
      @throws UDF_FileAPIException ファイルのパスが存在しない。
      @throws IllegalArgumentException サイズが異常。
    */
    public void resize(UDF_Policy policy, UDF_FEDesc fe, long size, UDF_ADList ad, boolean mirror) throws UDF_Exception, IOException{
	if(fe == null)
	    return;

	//DIRECTのときの処理
	if(fe.getAllocType() == 3){
	    long old_size = fe.getLongSize();
	    fe.getAllocDesc().setSize((int)size);
	    fe.getLenOfAllocDesc().setValue(size);
	    long new_size = fe.getLongSize();

	    // LBS内におさらまらない？
	    if(new_size > old_size &&
	       UDF_Util.align(new_size, my_image.env.LBS) > UDF_Util.align(old_size, my_image.env.LBS)){
		UDF_Extent fe_ext = (UDF_Extent)fe.getParent();
		//descから確保
		long alloc_size = new_size - old_size;
		alloc_size = UDF_Util.align(alloc_size, my_image.env.LBS);
		ad = policy.getAllocDescPolicy().alloc(my_image, alloc_size, 1);
		fe_ext.addExtent(ad);
	    }
	    recalcFieldValue(fe);

	    return;
	}

	UDF_ADDesc tgt = fe.getLastADDesc();

	String fe_label = fe.getAllocDesc().getAttribute("ref");
	fe_label = UDF_Util.car(fe_label, '.' );

	UDF_Extent data_ext = fe.isDirectory() ?
	    (UDF_Extent)my_image.findById(fe_label).getParent() :
	    (UDF_Extent)my_image.findById(fe_label);
	    

	/*
	  ここは main/mirrorで2度くる可能性がある。
	  DATAが type1にあるならば二回広げる必用はないので、
	  スルーする
	*/
	if(data_ext != null && data_ext.getLongSize() < size){//大きくなる
	    if(ad != null){
		if(!mirror || my_image.env.hasMirrorPartmap(ad.firstElement().getPartRefNo())){
		    data_ext.addExtent(ad);
		    data_ext.normalize();

		}
		tgt.normalize();// len=0のADを消したい……
		tgt.appendAD(ad);
		tgt.getDescTag().getDescCRCLen().setValue(tgt.getSize() - 16);
	    }
	    if(data_ext != null)
		if(!mirror || (ad != null && my_image.env.hasMirrorPartmap(ad.firstElement().getPartRefNo())))
		    data_ext.truncExtent(size);
	}
	else if(data_ext != null && data_ext.getLongSize() > size){//小さくなる
	    if(ad != null){
		if(!mirror || my_image.env.hasMirrorPartmap(ad.firstElement().getPartRefNo())){
		    data_ext.truncExtent(size);
		    data_ext.normalize();
		}
		tgt.normalize(); //len=0のADを消したい……
		tgt.removeAllAD();
		tgt.appendAD(data_ext.getExtent());
		tgt.getDescTag().getDescCRCLen().setValue(tgt.getSize() - 16);
	    }
	}
	else
	    return;
    }
    /**
      ファイルのサイズを取得する。

      @param path ファイル名（rootからのパス含む）。
      @throws UDF_FileAPIException ファイルのパスが存在しない。
    */
    public long getFileSize(String path) throws UDF_Exception {

        return ((UDF_desc261)getFileEntry(path, 0)).getInfoLen().getLongValue();
    }

    /*
      ファイル／ディレクトリのタイプを変更する。

      @param path ファイル／ディレクトリ名（rootからのパス含む）。
      @param type 希望するタイプの値。
      @throws UDF_FileAPIException ファイルのパスが存在しない。
    public void chtype(String path, int type) throws IOException, UDF_Exception{
    }
    */

    /**
      ファイル／ディレクトリのフラグを変更する。

      @param path ファイル／ディレクトリ名（rootからのパス含む）。
      @param flag 希望するフラグの値。
      @throws UDF_FileAPIException ファイルのパスが存在しない。
    */
    public void chflag(UDF_Policy policy, String path, int flag) throws IOException, UDF_Exception{
	UDF_FEDesc fe = my_image.findFEByPathname(path, MAIN);
	UDF_FEDesc mirr_fe = my_image.findFEByPathname(path, MIRROR);
	if(fe == null && mirr_fe == null)
	    throw new UDF_FileAPIException(my_image, "No such file or directory:" + path);
	
	if(fe != null)
	    fe.getICBTag().getFlags().setValue(flag);
	if(mirr_fe != null)
	    mirr_fe.getICBTag().getFlags().setValue(flag);
    }

    /**
      ファイル／ディレクトリの所有者を変更する。

      @param path ファイル／ディレクトリ名（rootからのパス含む）。
      @param owner 希望するオーナーの値。
      @throws UDF_FileAPIException ファイルのパスが存在しない。
    */
    public void chown(UDF_Policy policy, String path, int owner) throws IOException, UDF_Exception{
	UDF_FEDesc fe = my_image.findFEByPathname(path, MAIN);
	UDF_FEDesc mirr_fe = my_image.findFEByPathname(path, MIRROR);
	if(fe == null && mirr_fe == null)
	    throw new UDF_FileAPIException(my_image, "No such file or directory:" + path);
	
	if(fe != null)
	    fe.getUid().setValue(owner);
	if(mirr_fe != null)
	    mirr_fe.getUid().setValue(owner);
    }

    /**
      ファイル／ディレクトリのグループを変更する。

      @param path ファイル／ディレクトリ名（rootからのパス含む）。
      @param group 希望するグループの値。
      @throws UDF_FileAPIException ファイルのパスが存在しない。
    */
    public void chgrp(UDF_Policy policy, String path, int group) throws IOException, UDF_Exception{
	UDF_FEDesc fe = my_image.findFEByPathname(path, MAIN);
	UDF_FEDesc mirr_fe = my_image.findFEByPathname(path, MIRROR);
	if(fe == null && mirr_fe == null)
	    throw new UDF_FileAPIException(my_image, "No such file or directory:" + path);
	
	if(fe != null)
	    fe.getGid().setValue(group);
	if(mirr_fe != null)
	    mirr_fe.getGid().setValue(group);
    }

    /**
      ファイルに指定の外部ファイルのデータを書き込む。

      @param path ファイル名（rootからのパス含む）。
      @param offset 書き込み始めるファイル上の位置。
      @param size 書き込むバイト数。
      @param src 読み込む外部ファイルのパス
      @param src_offset 外部ファイルの読み込みを始める位置。
      @throws UDF_FileAPIException ファイルのパスが存在しない。
      @throws IllegalArgumentException 外部ファイルのパスが間違っている／オフセット値が異常／サイズ値が異常。
    */
    public void writeFileFile(String path, long offset, long size, String src, long src_offset) throws UDF_Exception{
	;
    }

    /**
      ファイルの内容を外部ファイルに書き出す。

      @param path ファイル名（rootからのパス含む）。
      @param offset 読み込み始めるファイル上の位置。
      @param size 書き込むバイト数。
      @param src 書き込む外部ファイルのパス
      @param src_offset 外部ファイルの書き込みを始める位置。
      @throws UDF_FileAPIException ファイルのパスが存在しない。
      @throws IllegalArgumentException 外部ファイルのパスが間違っている／オフセット値が異常／サイズ値が異常。
    */
    public void readFileFile(UDF_Policy policy, String path, long offset, long size, String src, long src_offset) throws UDF_Exception, IOException{
	try{
	    UDF_FEDesc fe = my_image.findFEByPathname(path, MAIN);
	    
	    UDF_Extent ref = fe.getReferenceExtent();
	    
	    UDF_RandomAccessFile f = new UDF_RandomAccessFile(src);
	    f.seek(src_offset);
	    
	    UDF_ElementList children = ref.getChildList();
	    for(int i=0 ; i<children.size() ; ++i){
		children.elementAt(i).writeTo(f);
	    }
	    
	    f.close();
	}
	catch(FileNotFoundException e){
	    e.printStackTrace();
	}
	catch(IOException e){
	    e.printStackTrace();
	}
    }

    /**
      ファイルの指定の位置に指定の数値を指定の数だけ書き込む。

      @param path ファイル名（rootからのパス含む）。
      @param b 書き込む数値。
      @param offset 書き込み始めるファイル上の位置。
      @param size 書き込む回数（バイト数）。
      @throws UDF_FileAPIException ファイルのパスが存在しない。
    */
    public void writeFileByte(String path, byte b, long offset, long size) throws UDF_Exception{
    }


    /**
      ファイル／ディレクトリの File Entry を取得する。

      @param path ファイル／ディレクトリ名（rootからのパス含む）。
      @param subno 副番号
      @return File Entry のUDF Element。
      @throws UDF_FileAPIException ファイルのパスが存在しない。
    */
    public UDF_FEDesc getFileEntry(String path, int subno) throws UDF_Exception {
        com.udfv.ecma167.UDF_FEDesc fe = my_image.findFEByPathname(path, subno);
        if (fe == null) {
            throw new UDF_FileAPIException(my_image, "No such file or direcotry : " + path);
        }
        return fe;
    }

    /**
      ファイル／ディレクトリの File Identifier を取得する。

      @param path ファイル／ディレクトリ名（rootからのパス含む）。
      @return File Identifier のUDF Element。
      @throws UDF_FileAPIException ファイルのパスが存在しない。
    */
    public UDF_desc257 getFileIdentifier(String path, int subno) throws UDF_Exception {
        com.udfv.ecma167.UDF_desc257 fid = my_image.findFIDByPathname(path, subno);
        if (fid == null) {
            throw new UDF_FileAPIException(my_image, "No such file or direcotry : " + path);
        }
        return fid;
    }

    /**
      ファイル／ディレクトリのADのタイプを取得する。

      @param path ファイル名（rootからのパス含む）。
      @return ADのタイプ (bit0-2/Flags/ICB Tag)。
      @throws UDF_FileAPIException ファイルのパスが存在しない。
    */
    public int getAllocType(String path) throws UDF_Exception {
	return getFileEntry(path, 0).getAllocType();
    }

    /*
      ファイル／ディレクトリのADのタイプを設定する。

      また、変更後に格納されるADの形式を自動的に変換する。

      @param path ファイル／ディレクトリ名（rootからのパス含む）。
      @param type ADのタイプ。
      @throws UDF_FileAPIException ファイルのパスが存在しない。
      @throws IllegalArgumentException emmbedな形式のとき１論理ブロックに収まらない。
    public void setAllocDescType(String path, int type) throws UDF_Exception {
//        changeADType(policy, path, type);
    }
    */

    /**
      ファイル／ディレクトリのADを取得する。

      @param path ファイル／ディレクトリ名（rootからのパス含む）。
      @return ADの配列。
      @throws UDF_FileAPIException AD配列を持たないファイルである。
      @throws UDF_FileAPIException ファイルのパスが存在しない。
    */
    public UDF_ADList getAllocDesc(String path) throws UDF_Exception {

	UDF_FEDesc fe = getFileEntry(path, 0);
	int type = fe.getAllocType();
	if (type < 0 && 2 < type) {
	    throw new UDF_FileAPIException(my_image, "Bad Alloc Type : " + type);
	}
	UDF_ADList el = fe.getADList();

	/*
	int max = el.size();
	UDF_AD [] ads = new UDF_AD[max];
	for (int i = 0; i < max; i++) {
	    ads[i] = (UDF_AD) el.elementAt(i);
	}
	*/
	return el;
    }

    /**
      ファイル／ディレクトリが持つADに指定のADを追加する。

      @param path ファイル／ディレクトリ名（rootからのパス含む）。
      @param ad 追加したいAD配列。
      @throws UDF_FileAPIException ファイルのパスが存在しない。
      @throws IllegalArgumentException 指定のADは使用できない。
    */
    public void appendAllocDesc(UDF_Policy policy, String path, UDF_ADList ad) throws UDF_Exception,IOException {
	UDF_FEDesc fe = my_image.findFEByPathname(path, MAIN);
	UDF_FEDesc mirr_fe = my_image.findFEByPathname(path, MIRROR);
	if(fe == null && mirr_fe == null)
	    throw new UDF_FileAPIException(my_image, "No such directory" + path);

	if(mirr_fe != null)
	    appendAllocDesc(policy, mirr_fe, ad, true);
	appendAllocDesc(policy, fe, ad, false);
    }
    
    public void appendAllocDesc(UDF_Policy policy, UDF_FEDesc fe, UDF_ADList ad, boolean mirror) throws UDF_Exception,IOException{
	fe.normalize();
	fe.appendAD(ad.duplicateList());
        fe.getLenOfAllocDesc().setValue(fe.getAllocDesc().getSize());

	if(!mirror || my_image.env.hasMirrorPartmap(ad.firstElement().getPartRefNo())){
	    UDF_Extent data_ext = fe.getReferenceExtent();
	    data_ext.addExtent(ad);
	    data_ext.normalize();
	}
    }

    /**
      ファイル／ディレクトリが持つADから指定のADと一致するものを削除する。

      @param path ファイル／ディレクトリ名（rootからのパス含む）。
      @param ad 削除したいAD配列。
      @throws UDF_FileAPIException ファイルのパスが存在しない。
      @throws UDF_InternalException AllocDescの種類が emmbed な形式であった。
      @throws IllegalArgumentException 指定のADは存在しない。
    */
    public void removeAllocDesc(UDF_Policy policy, String path, UDF_ADList ad) throws IOException, UDF_Exception {
	UDF_FEDesc fe = my_image.findFEByPathname(path, MAIN);
	UDF_FEDesc mirr_fe = my_image.findFEByPathname(path, MIRROR);
	if(fe == null && mirr_fe == null)
	    throw new UDF_FileAPIException(my_image, "No such directory" + path);

        int type = fe.getAllocType();
        if (type == 3)
            throw new UDF_InternalException(my_image, "BAD AllocDesc Type : " + type);

	if(mirr_fe != null)
	    removeAllocDesc(policy, mirr_fe, ad, true);
	removeAllocDesc(policy, fe, ad, false);
    }
    //1個削除する
    void removeAllocDesc(UDF_Policy policy, UDF_FEDesc fe, UDF_AD ad, boolean mirror) throws UDF_PartMapException{
	UDF_ADList cur_adlist = fe.getADList();
	for(Iterator i=cur_adlist.iterator() ; i.hasNext() ; ){//int i = 0 ; i < cur_adlist.size() ; i++) {
	    UDF_AD cur_ad = (UDF_AD)i.next();
	    
	    if(cur_ad.compareTo(ad) == 0){
		int removed_size = 0;
		cur_adlist.remove(cur_ad);

		fe.getAllocDesc().removeChild(cur_ad);
		removed_size = cur_ad.getSize();
		fe.getAllocDesc().incSize(-removed_size);
		fe.getLenOfAllocDesc().addValue(-removed_size);
		
		if(!mirror || my_image.env.hasMirrorPartmap(ad.getPartRefNo())){
		    UDF_Extent data_ext = fe.getReferenceExtent();
		    data_ext.removeExtent(ad);
		}

		break;
	    }
	}
    }
    //n個削除する
    void removeAllocDesc(UDF_Policy policy, UDF_FEDesc fe, UDF_ADList ad, boolean mirror) throws UDF_PartMapException{
	for(Iterator j=ad.iterator() ; j.hasNext() ; ){
	    removeAllocDesc(policy, fe, (UDF_AD)j.next(), mirror);
        }
    }

    /**
      ファイル／ディレクトリのADを置き換える。

      allocation typeが3の FEは追加できない。

      @param path ファイル／ディレクトリ名（rootからのパス含む）。
      @param ad 新しいAD配列。
      @throws UDF_FileAPIException ファイルのパスが存在しない。
      @throws IllegalArgumentException 指定のADは使用できない。
    */
    public void replaceAllocDesc(UDF_Policy policy, String path, UDF_ADList ad) throws IOException, UDF_Exception {

	UDF_FEDesc fe = my_image.findFEByPathname(path, MAIN);
	UDF_FEDesc mirr_fe = my_image.findFEByPathname(path, MIRROR);
	if(fe == null && mirr_fe == null)
	    throw new UDF_FileAPIException(my_image, "No such directory" + path);
	if(fe.getAllocType() == 3)
	    return;

	if(mirr_fe != null)
	    replaceAllocDesc(policy, mirr_fe, ad, true);
	replaceAllocDesc(policy, fe, ad, false);
    }
    
    public void replaceAllocDesc(UDF_Policy policy, UDF_FEDesc fe, UDF_ADList ad, boolean mirror) throws IOException, UDF_Exception{
	if(!mirror || my_image.env.hasMirrorPartmap(ad.firstElement().getPartRefNo())){
	    UDF_Extent data_ext = fe.getReferenceExtent();
	    data_ext.truncExtent(0);
	    data_ext.addExtent(ad.duplicateList());

	    //あとは recalcがよろしくやるはず
	}
    }

    /**
       UDF_FEDescの

       info-len
       logical block recroded
       crc length

       を計算して設定する
     */
    void recalcFieldValue(UDF_FEDesc fe){
	fe.getDescTag().getDescCRCLen().setValue(fe.getSize() - 16);
	fe.getInfoLen().setValue(fe.getADSize());
	if(fe.getAllocType() != 3)
	    fe.getLogicalBlocksRecorded().setValue(UDF_Util.align(fe.getADSize(), my_image.env.LBS) / my_image.env.LBS);
	else
	    fe.getLogicalBlocksRecorded().setValue(0);
    }

    //
    //
    // UDF_PartitionAPI
    //
    //

    /**
      パーティションマップのサイズを変更する。

      @param policy 領域割当てポリシー
      @param map_no パーティションマップ番号。
      @param size 希望するパーティションの長さ（単位：バイト）。
      @throws UDF_PartMapException 存在しないマップ番号を指定した／サイズが異常。
    */
    public void resizePartMap(UDF_Policy policy, int partmapno, long size) throws UDF_Exception, IOException{
	//現在metadata のみに対応。
	//小さくなることは考慮していない。

	UDF_MetadataPartMap pm = (UDF_MetadataPartMap)env.getPartMap(partmapno);
	if(pm == null)
	    return;

	UDF_FEDesc fe = pm.getMetadataFile();
	UDF_FEDesc fe2 = pm.getMetadataMirrorFile();

	UDF_ADList ad = null;
	UDF_ADList ad2 = null;
	if(fe.getADSize() < size){
	    //あらたな割付が必用か？
	    if(UDF_Util.align(fe.getADSize(), my_image.env.LBS) < UDF_Util.align(size, my_image.env.LBS)){
		long alloc_size = size - fe.getADSize();
		ad = policy.getAllocDataPolicy().alloc(my_image, alloc_size, fe.getICBFlags() & 0x7);
	    }
	}

	if(fe2.getADSize() < size){
	    //あらたな割付が必用か？
	    if(UDF_Util.align(fe2.getADSize(), my_image.env.LBS) < UDF_Util.align(size, my_image.env.LBS)){
		long alloc_size = size - fe2.getADSize();
		ad2 = policy.getAllocDataPolicy().alloc(my_image, alloc_size, fe2.getICBFlags() & 0x7);
	    }
	}

	int incsize = ad.firstElement().getSize() * ad.size();
	int incsize2 = ad2.firstElement().getSize() * ad2.size();
	fe.appendAD(ad);
	fe2.appendAD(ad2);

	fe.getInfoLen().setValue(size);
	fe2.getInfoLen().setValue(size);

	fe.getLogicalBlocksRecorded().setValue(size/env.LBS);
	fe2.getLogicalBlocksRecorded().setValue(size/env.LBS);

	UDF_Extent ext = pm.genExtent(0);
	UDF_Extent ext1 = pm.genExtent(1);
	env.setPartMapExtent(partmapno, 0, ext);
	env.setPartMapExtent(partmapno, 1, ext1);
	env.setPartMapRandomAccess(partmapno, 0, ext.genRandomAccessExtent());
	env.setPartMapRandomAccess(partmapno, 1, ext1.genRandomAccessExtent());

	//もう意味がないとは言え、<partition>をちゃんとつくっておく
	my_image.createPartElement();
	//
	my_image.recalcSB(true);
    }

    public void writeVAT(UDF_Policy policy, int partmapno) throws UDF_Exception, IOException{
	if(!env.isVirtualPartMap(partmapno))
	    throw new UDF_PartMapException(null, "Partition map is not virtual partition map.");

	//最大のExtentを求める(その後にVATを書くので)
	long off = 0;
	Iterator it = my_image.getChildList().iterator();
	while(it.hasNext()){
	    UDF_Extent ext = (UDF_Extent)it.next();
	    UDF_ExtentElemList eel = ext.getExtentElem();
	    for(Iterator it2 = eel.iterator() ; it2.hasNext() ; ){
		UDF_ExtentElem ee = (UDF_ExtentElem)it2.next();
		if(ee.getLoc() * env.LBS + ee.getLen() > off)
		    off = ee.getLoc() * env.LBS + ee.getLen();
	    }
	}
	int lastlbn = (int)(off / env.LBS);
	
	{	
	    UDF_Extent ext_vat = (UDF_Extent)my_image.createElement("UDF_Extent", null, null);
	    ext_vat.addExtent(lastlbn, 0, my_image.env.LBS);
	    UDF_FEDesc fe_vat = policy.getFEPolicy().create(my_image, policy, 248);
	    com.udfv.udf200.UDF_VirtualAllocTable200 vat200 =
		(com.udfv.udf200.UDF_VirtualAllocTable200)my_image.createElement("UDF_VirtualAllocTable200", null, null);
	    vat200.setHintSize(152);
	    vat200.setDefaultValue();
	    vat200.getLogicalVolId().setValue(policy.getVolPolicy().getLogicalVolId());
	    vat200.getNumberOfDirectories().setValue(1);
	    vat200.getMinUDFReadVersion().setValue(policy.getVolPolicy().getRevision());
	    vat200.getMinUDFWriteVersion().setValue(policy.getVolPolicy().getRevision());
	    vat200.getMaxUDFWriteVersion().setValue(policy.getVolPolicy().getRevision());
	    fe_vat.getICBTag().getFlags().setValue(3+32);
	    fe_vat.getAllocDesc().setSize(vat200.getSize());
	    fe_vat.getAllocDesc().replaceChild(vat200);
	    fe_vat.getLenOfAllocDesc().setValue(vat200.getSize());
	    ext_vat.appendChild(fe_vat);
	    my_image.appendChild(ext_vat);
	}
    }

    public void shiftPartMapSubno(UDF_Policy policy, int partmapno) throws UDF_Exception, IOException{
	if(!env.isVirtualPartMap(partmapno))
	    throw new UDF_PartMapException(null, "Partition map is not virtual partition map.");

	/*
	  最大のSubnoを求める
	*/
	int maxsubno = 0;
	try{
	    for( ; ; ++maxsubno){
		env.getPartMapExtent(partmapno, maxsubno);
	    }
	}
	catch(UDF_PartMapException e){
	    ;
	}
	/*
	  Subnoをスライドさせるため、PartMapをスライドさせる。

	  0->1
	  1->2
	  2->3
	  ....
	  ....
	 */
	while(maxsubno > 0){
	    UDF_Extent ext = (UDF_Extent)env.getPartMapExtent(partmapno, maxsubno - 1).duplicateElement();
	    env.setPartMapExtent(partmapno, maxsubno, ext);
	    env.setPartMapRandomAccess(partmapno, maxsubno, ext.genRandomAccessExtent());
	    --maxsubno;
	}

	UDF_Element[] el = my_image.getChildren();

	/*
	  1) Extentの最初の ExtentElemが該当するパーティション番号ならば、
	     Extentの Subnoを1増やす。
	     ただし、subnoが0のものは複製し、複製したものを1とする。
	*/
	//for(Iterator it = el.iterator() ; it.hasNext() ; ){
	//ここはiterator使えない。(追加とかあるんで)
	for(int i=0 ; i<el.length ; ++i){
	    UDF_Extent ext = (UDF_Extent)el[i];
	    UDF_ExtentElem ee = (UDF_ExtentElem)ext.getExtentElem().firstElement();
	    if(ee.getPartRefNo() == partmapno){
		int subno = ext.getPartSubno();
		if(subno != 0)
		    ext.setPartSubno(ext.getPartSubno() + 1);
		else{
		    UDF_Extent new_ext = (UDF_Extent)ext.duplicateElement();
		    new_ext.setPartSubno(1);
		    my_image.appendChild(new_ext);
		}
	    }
	}
    }
}



