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
package com.udfv.ecma167;

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

/**
   UDF_FEDescは UDF_desc261と UDF_desc266の基底クラスとなるクラスである。

   (解説)
   ECMA167では FileEntryは File Entry Descriptor(UDF_desc261)および
   Extended File Entry Descriptor(UDF_desc266)のどちらで記述しても
   よいとされている。

   この二つのデスクリプタの動作は殆ど同じであるため、通常の操作に
   関しては UDF_FEDescを用いるのが望ましい。

 */
abstract public class UDF_FEDesc extends UDF_ADDesc implements UDF_ICBDesc 
{
    //(このFEが Directoryならば)その子供ディレクトリまたはファイル
    //0番目の要素は自分自身を指すFIDである。
    private UDF_ElementList my_directoryList;

    //このFEを指すFID。一つとは限らない。
    private UDF_ElementList my_referencedFID;

    UDF_FEDesc(UDF_Element elem, String preifx, String name){
	super(elem, preifx, name);
	my_directoryList = new UDF_ElementList();
	my_referencedFID = new UDF_ElementList();
    }

    abstract public UDF_icbtag getICBTag();
    abstract public void setICBTag(UDF_icbtag v);
    abstract public UDF_bytes getExtendedAttr();
    abstract public UDF_uint32 getLenOfExtendedAttr();
    abstract public void setExtendedAttr(UDF_bytes v);
    abstract public UDF_bytes getAllocDesc();
    abstract public void setAllocDesc(UDF_bytes v);
    abstract public UDF_uint16 getFileLinkCount();
    abstract public void setImplId(UDF_regid v);
    abstract public UDF_regid getImplId();
    abstract public UDF_uint64 getUniqueId();
    abstract public UDF_uint32 getLenOfAllocDesc();
    abstract public UDF_uint64 getInfoLen();
    abstract public UDF_uint64 getLogicalBlocksRecorded();
    abstract public UDF_timestamp getAccessDateAndTime();
    abstract public UDF_timestamp getModificationDateAndTime();
    abstract public UDF_timestamp getAttrDateAndTime();
    abstract public UDF_uint32 getPermissions();

    /**
	uidを取得する。

	@return 取得したuid を返す。
    */
    abstract public UDF_uint32 getUid();
    /**
	gidを取得する。

	@return 取得したgid を返す。
    */
    abstract public UDF_uint32 getGid();

    abstract public void setDescTag(UDF_tag v);
    public UDF_uint64 getObjectSize(){
	return null;
    }

    public UDF_ElementList getDirectoryList(){
	return my_directoryList;
    }
    public UDF_ElementList getReferencedFID(){
	return my_referencedFID;
    }

    /**
     */
    public boolean isDirectory(){
	if(getICBFileType() == UDF_icbtag.T_DIRECTORY ||
	   getICBFileType() == UDF_icbtag.T_SDIRECTORY)
	    return true;
	return false;
    }
    /**
       このFEが DirectoryならこのFEが抱える UDF_Directoryを返す。
       FEがDirectoryでなければ、UDF_FileAPIExceptionをスローする。
     */
    public UDF_Directory getUDFDirectory() throws UDF_FileAPIException{
	if(getICBFileType() != UDF_icbtag.T_DIRECTORY &&
	   getICBFileType() != UDF_icbtag.T_SDIRECTORY)
	    throw new UDF_FileAPIException(this, "Not directory");

	//子FIDの (XML的な)親は UDF_Directoryであろう
	return (UDF_Directory)my_directoryList.elementAt(0).getParent();
    }

    /**
       現在位置からデータを読み UDF_desc261または UDF_desc266を生成する。

       @param f	アクセサ
       @param parent 親
       @param prefix XMLネームスペース
       @param tagName	XMLエレメント名

       UDF_desc261か UDF_desc266かは最初の2バイトをチェックしなければならないが、
       genFEDesc()はこれを自動で行い適切なクラスを生成する。
    */
    public static UDF_FEDesc genFEDesc(UDF_RandomAccess f, UDF_Element parent, String prefix, String tagName) throws UDF_Exception, IOException/*, ClassNotFoundException*/{
	UDF_CrcDesc desc = null;
	try{
	    desc = UDF_CrcDesc.genCrcDesc(f, parent, prefix, tagName);
	    return (UDF_FEDesc)desc;
	}
	catch(ClassCastException e){
	    System.err.println("Class cast Exception: " + desc.getClass().getName() +
			       " gp:" + f.getAbsPointer());
	}
	/*
	catch(ClassNotFoundException e){
	    System.err.println("Class Not Found Exception: " + desc.getClass().getName());
	}
	*/
	return null;
    }
    /**
       tagidを指定して UDF_desc261または UDF_desc266を生成する。

       @param tagid	tagid
       @param parent	親 
       @param prefix	XMLネームスペース
       @param tagName	XMLエレメント名
     */
    public static UDF_FEDesc genFEDesc(int tagid, UDF_Element parent, String prefix, String tagName) throws UDF_Exception, IOException/*, ClassNotFoundException*/{
	UDF_CrcDesc desc = null;
	try{
	    desc = UDF_CrcDesc.genCrcDesc(tagid, parent, prefix, tagName);
	    return (UDF_FEDesc)desc;
	}
	catch(ClassCastException e){
	    System.err.println("Class cast Exception: " + desc.getClass().getName());
	}
	return null;
    }

    /**
       @deprecated replaced by {@link #getReferenceExtent()}
    */
    public UDF_Extent getUDFExtent(){
	return getReferenceExtent();
    }
    /**
       FEの指しているDataを取得する。

       DIRECTでない場合は getReferenceExtent()と同じ動作をする。
       DIRECTの場合はallocDesc()を返す
     */
    public UDF_Element getReferenceData(){
	if(getAllocType() == 3)
	    return getAllocDesc();
	return getReferenceExtent();
    }    
    /**
       FEの指している UDF_Extentを取得する

       DIRECTの場合は nullを返す

       @return UDF_Extent

       このメソッドは allocDesc()の refを参考に UDF_Extentを検索する。
     */
    public UDF_Extent getReferenceExtent(){
	if(getAllocType() == 3)
	    return null;

	String fe_label = getAllocDesc().getAttribute("ref");
	fe_label = UDF_Util.car(fe_label, '.');

	UDF_Extent ext;

	if(!isDirectory())
	    ext = (UDF_Extent)findById(fe_label);
	else
	    ext = (UDF_Extent)findById(fe_label).getParent();

	if(ext != null)
	    return ext;

	return ext;
    }

    /**
       FLAG=0のADの Lengthの合計を取得する。

       ICB Flagが 3である場合は alloc-descのサイズを返す
       フラグ3のADがある場合はその先も辿って計算する。
     */
    //@deprecated replaced by {@link calcInfoLenFromAD()}
    public long getADSize(){
	if(getAllocType() == 3)
	    return getAllocDesc().getLongSize();
	
	long size = 0;
	UDF_ADList el = getADList();
	for(Iterator it=el.iterator() ; it.hasNext() ; ){
	    UDF_AD ad = (UDF_AD)it.next();
	    if(ad.getFlag() == 0 || ad.getFlag() == 2)
		size += ad.getLen();
	}

	return size;
    }


    /**
       ADより Object Sizeを計算する。

       ICB Flagが 3の場合は Allocation Descriptorフィールドのサイズを返す。

       ECMA167 4/12.1にある通り、Tailにある unrecorded and allocated(フラグ1)の
       ADはLengthとして加えない。途中にあるものは加える。

       ECMA167 4/12.1では LBSの倍数でない ADを複数置くことができないと記述されているが、
       そのようなADもこのメソッドでは許可する。

       フラグ3のADがある場合はチェーンを辿って計算する。

       ※このメソッドは現在 {@link calcInfoLenFromAD()}と同じである。
    */
    public long calcObjectSizeFromAD(){
	return calcInfoLenFromAD();
    }
    /**
       ADより Information Lengthを計算する。

       ICB Flagが 3の場合は Allocation Descriptorフィールドのサイズを返す。

       ECMA167 4/12.1にある通り、Tailにある unrecorded and allocated(フラグ1)の
       ADはLengthとして加えない。途中にあるものは加える。

       ECMA167 4/12.1では LBSの倍数でない ADを複数置くことができないと記述されているが、
       そのようなADもこのメソッドでは許可する。

       フラグ3のADがある場合はチェーンを辿って計算する。
    */
    public long calcInfoLenFromAD(){
	if(getAllocType() == 3)
	    return getAllocDesc().getLongSize();
	
	long size = 0;
	long size1 = 0;	//フラグ1のADの合計

	UDF_ADList el = getADList();
	for(Iterator it = el.iterator() ; it.hasNext() ; ){
	    UDF_AD ad = (UDF_AD)it.next();
	    if(ad.getFlag() == 1)
		size1 += ad.getLen();
	    else{
		size1 = 0;
		size += ad.getLen();
	    }
	}
	//ADの最後に フラグ1の塊があれば size1にそのサイズの合計があるはず

	size -= size1;
	
	return size;
    }

    /**
       ADより Logical Block Recorded を計算する。

       ICB Flagが 3の場合は 0を返す。(UDF 2.3.6.4)

       ECMA167 4/12.1にある通り、Tailにある unrecorded and allocated(フラグ1)の
       ADはLengthとして加えない。途中にあるものは加える。

       ECMA167 4/12.1では LBSの倍数でない ADを複数置くことができないと記述されているが、
       そのようなADもこのメソッドでは許可する。

       フラグ3のADがある場合はチェーンを辿って計算する。
    */
    public int calcLogicalBlocksRecordedFromAD(){
	if(getAllocType() == 3)
	    return 0;
	
	long lbs = 0;

	UDF_ADList el = getADList();
	for(Iterator it = el.iterator() ; it.hasNext() ; ){
	    UDF_AD ad = (UDF_AD)it.next();
	    if(ad.getFlag() == 0)
		lbs += UDF_Util.align(ad.getLen(), env.LBS) / env.LBS;
	}
	
	return (int)lbs;
    }

    /**
       FEのADからUDF_Extentのextentの値を設定する。short_adのときは default_partnoが
       使用される。

       @param ext	extent
       @param default_partno	デフォルトパーティション番号
     */
    public void setADToExtent(UDF_Extent ext, int default_partno){
	int flags = getICBFlags();
	UDF_RandomAccessBytes rab = getAllocDesc().genRandomAccessBytes();
	UDF_AD ad;

	if((flags & 0x7) == UDF_icbtag.DIRECT){
	    return;
	}

	Iterator it = getADList().iterator();
	while(it.hasNext()){
	    ad = (UDF_AD) it.next();
	    ext.addExtent(ad);

	    /*
	    if(ad.getFlag() == 0 || ad.getFlag() == 1 || ad.getFlag() == 2){
		if((flags & 0x7) == 0)
		    ext.addExtent(ad.getLbn(), default_partno, ad.getLen(), ad.getFlag());
		else
		    ext.addExtent(ad.getLbn(), ad.getPartRefNo(), ad.getLen(), ad.getFlag());
		//ext.addExtent2(ad.getLbn(), ad.getPartRefNo(), ad.getLen(), ad.getFlag(), null, false);
	    }
	    */
	}
	return;
    }
    /**
       readFrom直後に呼ばれるフック関数。

       @param f	アクセサ。
     */
    public void postReadHook(UDF_RandomAccess f) throws UDF_Exception, IOException{

	//　Extended Attributeが存在しないときは置き換えない　//
	if(getLenOfExtendedAttr().getIntValue() == 0) {
	    super.postReadHook(f);
	    return;
	}

	//extended attrの処理
	UDF_RandomAccessBytes rab = getExtendedAttr().genRandomAccessBytes();
	getExtendedAttr().replaceChildren(readExtendedAttrFrom(rab));

	super.postReadHook(f);
    }

    /**
       Extended Attribute ICB が指している先を読み込む。
       
       @param f  アクセサ。
    */
    public UDF_ElementList readExtendedAttrFrom(UDF_RandomAccess f) throws UDF_Exception, IOException {

	UDF_ElementList v = new UDF_ElementList();

	UDF_desc262 desc262 = (UDF_desc262) createElement("UDF_desc262", null, null);
	desc262.readFrom(f);
	v.add(desc262);

	try{
	    while(!f.eof()){
		UDF_attr attr = UDF_attr.genAttr(f, this, null, null);
		attr.readFrom(f);
		v.add(attr);
	    }
	}
	catch(ClassNotFoundException e){
	    System.err.println("CLASS NOT FOUND: off=" + f.getPointer());
	    e.printStackTrace();
	}

	return v;
    }

    /**
      Extended Attributes のブロックを読み取る。

      @depracted replaced by {@link parseExtendedAttr(int, int, long, int)}
    */
    public UDF_Extent parseExtendedAttr(int lbn, int partno, long len, boolean mirror) throws UDF_Exception, IOException {
	return parseExtendedAttr(lbn, partno, len, mirror ? 1 : 0);
    }

    /**
      Extended Attributes のブロックを読み取る。
    */
    public UDF_Extent parseExtendedAttr(int lbn, int partno, long len, int subno) throws UDF_Exception, IOException {
	UDF_RandomAccess ra = env.getPartMapRandomAccess(partno, subno);

	UDF_Extent ext = (UDF_Extent) createElement("UDF_Extent", null, null);
	ext.setPartSubno(subno);
	ext.addExtent(lbn, partno, len);

	UDF_ElementBase [] elem = readExtendedAttrFrom(new UDF_RandomAccessExtent(ext)).toElemArray();
	for (int i = 0, max = elem.length; i < max; i++) {
	    ext.appendChild((UDF_Element)elem[i]);
	}

	return ext;
    }

    public void recalcADLIST(){
	UDF_ADDesc tgt = this;

	loop:
	while(tgt != null){
	    if(tgt.getAllocType() != 3){
		UDF_ElementBase[]eb = tgt.getAllocDesc().getChildren();
		for(int i=0 ; i<eb.length ; ++i){
		    UDF_AD ad = (UDF_AD)eb[i];
		    if(ad.getFlag() == 3){
			String ref = UDF_Util.car(ad.getExtentLen().getAttribute("ref"), '.');
			if(ref.length() > 0){
			    UDF_desc258 desc258;
			    try{
				desc258 = (UDF_desc258)findById(ref).getFirstChild();
			    }
			    catch(NullPointerException e){
				ad.debug(0);
				System.err.println("label '" + ref + "' is not found");
				throw e;
			    }
			    tgt.setNextADDesc(desc258);
			    desc258.setPrevADDesc(tgt);
			    tgt = desc258;
			    continue loop;
			}
		    }
		}
	    }
	    break;
	}
    }
    // AllocDescの参照を参考に内容を書きかえる
    /**
       desc258でチェーンされているとき、各ADDEscの ADの個数はできるだけ変えないよう努力する。
       
       1)ADの数が変化しない場合
       (desc261|desc266)->desc258->desc258 の流れの中で各 DESCのADの数は変化しない
       
       2)ADの数が増える場合
       (desc261|desc266)->desc258->desc258 の流れの中で最後の DESCのADの数以外は変化しない
       
       3)ADの数が減る場合
       (desc261|desc266)->desc258->desc258 の流れの中で最初のほうから詰めていき、
       最後のほうを削除するよう努力する。
       
    */
    private void recalcREF() throws UDF_InternalException {
	int file_type = getICBFileType();
	int file_flags = getICBFlags() & 0x7;
		
	if(file_flags != 3){
	    //このADが参照しているDATAを取得
	    UDF_Extent ref_ext = getReferenceExtent();
	    boolean multi_flag = false;
	    //
	    //<multiple-extent>がある場合は再計算しない。
	    //
	    if(ref_ext != null){
		UDF_ExtentElemList eel = ref_ext.getExtentElem();
		for(Iterator it = eel.iterator() ; it.hasNext() ; ){
		    Object obj = it.next();
		    if(com.udfv.core.UDF_MultipleExtentElem.class.isAssignableFrom(obj.getClass())){
			multi_flag = true;
		    }
		}
	    }
	    if(ref_ext != null && !multi_flag){
		UDF_ExtentElemList eel = ref_ext.getExtentElem();
		Iterator it = eel.iterator();
		UDF_ADList adlist = getADList();
		Iterator it2 = adlist.iterator();
		
		//数が等しい？(増減なし)
		if(eel.size() == adlist.size()){
		    for(int i=0 ; i <adlist.size() ; ++i){
			UDF_ExtentElem ee = (UDF_ExtentElem)it.next();
			UDF_AD tgt_ad = (UDF_AD)it2.next();
			//UDF_AD tgt_ad = (UDF_AD)adlist.elementAt(i);
			tgt_ad.setLbn(ee.getLoc());
			tgt_ad.setPartRefNo(ee.getPartRefNo());
			tgt_ad.setLen(ee.getLen());
			tgt_ad.setFlag(ee.getExtentFlag());
		    }
		}
		//増える
		else if(eel.size() > adlist.size()){
		    //入る分だけ入れる
		    for(int i=0 ; i <adlist.size() ; ++i){
			UDF_ExtentElem ee = (UDF_ExtentElem)it.next();
			//UDF_AD tgt_ad = (UDF_AD)adlist.elementAt(i);
			UDF_AD tgt_ad = (UDF_AD)it2.next();
			tgt_ad.setLbn(ee.getLoc());
			tgt_ad.setPartRefNo(ee.getPartRefNo());
			tgt_ad.setLen(ee.getLen());
			tgt_ad.setFlag(ee.getExtentFlag());
		    }
		    //足りないものは最後に追加する
		    int ad_size = 0;
		    UDF_ADDesc tgt = getLastADDesc();
		    while(it.hasNext()){
			UDF_ExtentElem ee = (UDF_ExtentElem)it.next();
			UDF_AD new_ad = UDF_AD.genAD(this, file_flags);
			new_ad.setDefaultValue();
			new_ad.setLbn(ee.getLoc());
			new_ad.setPartRefNo(ee.getPartRefNo());
			new_ad.setLen(ee.getLen());
			new_ad.setFlag(ee.getExtentFlag());
			ad_size += new_ad.getSize();
			tgt.getAllocDesc().appendChild(new_ad);
		    }
		}
		//減る
		else if(eel.size() < adlist.size()){
		    //とりあえず入れる
		    int i;
		    for(i=0 ; i < eel.size() ; ++i){
			UDF_ExtentElem ee = (UDF_ExtentElem)it.next();
			//UDF_AD tgt_ad = (UDF_AD)adlist.elementAt(i);
			UDF_AD tgt_ad = (UDF_AD)it2.next();
			tgt_ad.setLbn(ee.getLoc());
			tgt_ad.setPartRefNo(ee.getPartRefNo());
			tgt_ad.setLen(ee.getLen());
			tgt_ad.setFlag(ee.getExtentFlag());
		    }
		    //残りは全部0にする
		    for( ; i < adlist.size() ; ++i){
			//UDF_AD tgt_ad = (UDF_AD)adlist.elementAt(i);
			UDF_AD tgt_ad = (UDF_AD)it2.next();
			tgt_ad.setLbn(0);
			tgt_ad.setPartRefNo(0);
			tgt_ad.setLen(0);
			tgt_ad.setFlag(0);
		    }
		    //内容が 0のADは削除する
		    UDF_ADDesc addesc = this;
		    while(addesc != null){
			addesc.normalize();
			addesc = addesc.getNextADDesc();
		    }
		}
		//各DESCのsizeやCRClenなどのデータを書きかえ
		UDF_ADDesc addesc = this;
		while(addesc != null){
		    int ad_size = 0;
		    UDF_ElementBase[] eb = addesc.getAllocDesc().getChildren();
		    for(int i=0 ; i<eb.length ; ++i){
			ad_size += eb[i].getSize();
		    }
		    addesc.getAllocDesc().setSize(ad_size);
		    addesc.getLenOfAllocDesc().setValue(getAllocDesc().getSize());
		    addesc.getDescTag().getDescCRCLen().setValue(getSize() - 16);
		    addesc = addesc.getNextADDesc();
		}
	    }
	    //最後におおもと(this)の InfoLenなどを整える 
	    getInfoLen().setValue(getADSize());
	    getLogicalBlocksRecorded().setValue(UDF_Util.align(getADSize(), env.LBS) / env.LBS);
	}
	else{
	    getLenOfAllocDesc().setValue(getAllocDesc().getSize());
	    getInfoLen().setValue(getADSize());
	    getLogicalBlocksRecorded().setValue(0);
	    getDescTag().getDescCRCLen().setValue(getSize() - 16);
	}
    }
    public void recalc(short type, UDF_RandomAccess f){
	super.recalc(type, f);
	try{
	    if(type == RECALC_TREE){
		int file_type = getICBFileType();
		int file_flags = getICBFlags() & 0x7;

		if(file_type == UDF_icbtag.T_DIRECTORY || file_type == UDF_icbtag.T_SDIRECTORY){
		    String ref = getAllocDesc().getAttribute("ref");
		    if(ref != null && ref.indexOf(".") >= 0)
			ref = ref.substring(0, ref.indexOf("."));
			
		    //flag3でも辿れるようになった……はず
		    if(ref != null){
			UDF_Directory dir = (UDF_Directory)findById(ref);
			if(dir != null){
			    UDF_ElementBase[] fids = dir.getChildren();
			    for(int i=0 ; i<fids.length ; ++i){
				if(com.udfv.ecma167.UDF_desc257.class.isAssignableFrom(fids[i].getClass())){
				    UDF_desc257 fid = (UDF_desc257) fids[i];
				    getDirectoryList().add(fid);
				    fid.setReferencedBy(this);
				}
			    }
			}
		    }
		}
	    }
	    else if(type == RECALC_ADLIST){
		recalcADLIST();
	    }
	    else if(type == RECALC_REF){
		recalcREF();
	    }
	}
	catch(Exception e){
	    ignoreMsg("recalc", e);
	}
    }

    /**
      この File Entry が持つ UDF_Extent の extents 情報を線として描いた JPanel を返す。

      env.root に、このFEが存在する UDF_Image のインスタンスが格納されていないと動作しない。

      @param w 戻すJPanelの横幅
      @param h 戻すJPanelの縦幅
    */
    public javax.swing.JComponent createJBitmapPanel(int w, int h) {

        com.udfv.gui.JBitmapColorSet cs = new com.udfv.gui.JBitmapColorSet();

        cs.setColor(com.udfv.gui.JBitmapColorSet.FREE,   java.awt.Color.gray);
        cs.setColor(com.udfv.gui.JBitmapColorSet.VOL,    java.awt.Color.green);
        cs.setColor(com.udfv.gui.JBitmapColorSet.PART_0, java.awt.Color.green);
        cs.setColor(com.udfv.gui.JBitmapColorSet.PART_1, java.awt.Color.green);
        cs.setColor(com.udfv.gui.JBitmapColorSet.PART_2, java.awt.Color.green);
        cs.setColor(com.udfv.gui.JBitmapColorSet.PART_3, java.awt.Color.green);
        cs.setColor(com.udfv.gui.JBitmapColorSet.USER_0, java.awt.Color.red);
        cs.setColor(com.udfv.gui.JBitmapColorSet.USER_1, java.awt.Color.red);

        com.udfv.gui.JFEBitmapPanel panel = new com.udfv.gui.JFEBitmapPanel(this, w, h, cs);

        return panel;
    }

    /**
      この File Entry が持つ UDF_Extent の extents 情報を線として描いた JPanel を返す。

      env.root に、このFEが存在する UDF_Image のインスタンスが格納されていないと動作しない。

      @param w 戻すJPanelの横幅
      @param h 戻すJPanelの縦幅
      @param cs 配色情報
    */
    public javax.swing.JComponent createJBitmapPanel(int w, int h, com.udfv.gui.JBitmapColorSet cs) {

        com.udfv.gui.JFEBitmapPanel panel = new com.udfv.gui.JFEBitmapPanel(this, w, h, cs);

        return panel;
    }

    public static String permToString(int perm){
	StringBuffer sb = new StringBuffer();

	for(int i=0 ; i<15 ; ){
	    if((perm & (1<<i)) != 0)
		sb.append("x");
	    else
		sb.append("-");
	    ++i;

	    if((perm & (1<<i)) != 0)
		sb.append("w");
	    else
		sb.append("-");
	    ++i;

	    if((perm & (1<<i)) != 0)
		sb.append("r");
	    else
		sb.append("-");
	    ++i;

	    if((perm & (1<<i)) != 0)
		sb.append("a");
	    else
		sb.append("-");
	    ++i;

	    if((perm & (1<<i)) != 0)
		sb.append("d");
	    else
		sb.append("-");
	    ++i;
	}

	return sb.toString();
    }
};

