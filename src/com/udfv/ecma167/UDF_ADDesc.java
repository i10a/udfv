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
   UDF_ADDescは Allocation Descriptorを持つクラスの基底となるクラスである。

   
 */
abstract public class UDF_ADDesc extends UDF_CrcDesc
{
    //private UDF_ElementList adlist;
    /* adlistを廃止した理由

    readFromするときに adlistを生成するのは非常に便利であるが、OperateImg等で
    allocDesc()に何かを追加したり削除したときに adlistが更新されないという
    問題がある。

    そこで、adlistを廃止し、常に現在の FEの状態から取得するように変更する。
    */

    /*
       ADは (desc261|desc266)->(desc258)->(desc258) ... ->(desc258)
       と連なる。
     */

    /**
       一つ前の desc。
     */
    UDF_ADDesc prev_addesc;
    /**
       次の desc。
     */
    UDF_ADDesc next_addesc;

    /**
      コンストラクタ

       @param elem	親
       @param prefix	namespace
       @param name	名前
    */
    protected UDF_ADDesc(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name);

	next_addesc = null;
	prev_addesc = null;
    }

    public UDF_FEDesc getFirstADDesc(){
	UDF_ADDesc add = this;
	while(add.prev_addesc != null)
	    add = add.prev_addesc;
	return (UDF_FEDesc)add;
    }
    public UDF_ADDesc getLastADDesc(){
	UDF_ADDesc add = this;
	while(add.next_addesc != null)
	    add = add.next_addesc;
	return add;
    }
    public UDF_ADDesc getNextADDesc(){
	return next_addesc;
    }
    public UDF_ADDesc getPrevADDesc(){
	return prev_addesc;
    }

    public void setNextADDesc(UDF_ADDesc desc){
	next_addesc = desc;
    }
    public void setPrevADDesc(UDF_ADDesc desc){
	prev_addesc = desc;
    }

    /**
	iCBTagを取得する。

	@return 取得したiCBTag を返す。
    */
    abstract public UDF_icbtag getICBTag();
    /**
	allocDescを取得する。

	@return 取得したallocDesc を返す。
    */
    abstract public UDF_bytes getAllocDesc();
    /**
	allocDescを設定する。

	@param	v 設定する値。
    */
    abstract public void setAllocDesc(UDF_bytes v);
    
    /**
	lenOfAllocDescを取得する。

	@return 取得したlenOfAllocDesc を返す。
    */
    abstract public UDF_uint32 getLenOfAllocDesc();

    /**
       ICB FileTypeの値を取得する

       @return 値
     */
    public int getICBFileType(){
	return getICBTag().getFileType().getIntValue();
    }
    /**
       ICB Flagsの値を取得する

       @return 値
     */
    public int getICBFlags(){
	return getICBTag().getFlags().getIntValue();
    }

    /**
       Allocation Descriptorのタイプを取得する。


       @return	0	short_ad
       @return	1	long_ad
       @return	2	ext_ad
       @return	3	immediate
     */
    public int getAllocType() {
	return getICBFlags() & 0x07;
    }

    public void recalc(short type, UDF_RandomAccess f){
	super.recalc(type, f);
    }
    /**
       Allocation Descriptorを取得する。

       Alloc Typeが 3のときはnullを返す

       getAD()は、ADが フラグ3で desc258に飛ばされ、続きが記述されている場合、その先を取りこまない。
       そうではなくて、このデスクリプタの差す全ての ADを取得したい時は、getADList()を使用すること。

     */
    public UDF_AD[] getAD(){
	if(getAllocType() == 3)
	    return null;

	UDF_ElementBase[] ee = getAllocDesc().getChildren();
	UDF_AD[] ad = new UDF_AD[ee.length];
	for(int i=0 ; i<ee.length ; ++i){
	    ad[i] = (UDF_AD)ee[i];
	}
	return ad;
    }
    /**
       Allocation Descriptorのリストを取得する。

       Alloc Typeが 3のときはnullを返す。

       getADList()は、ADが フラグ3で desc258に飛ばされ、続きが記述されている場合、その先も取りこむ。
       そうではなくて、このデスクリプタのADを取得したい時は、getAllocDesc().getChildren()を使用すること。

     */
    public UDF_ADList getADList(){
	if(getAllocType() == 3)
	    return null;

	UDF_ADList adlist = new UDF_ADList();
	UDF_ElementList eb = getAllocDesc().getChildList();
	for(Iterator it = eb.iterator() ; it.hasNext() ; ){
	    try{
		UDF_AD ad = (UDF_AD)it.next();
		if(ad.getFlag() != 3)
		    adlist.add(ad);
		else if(getNextADDesc() != null)
		    adlist.add(getNextADDesc().getADList());
	    }
	    catch(ClassCastException e){
		debug(0);
		e.printStackTrace();
		return null;
	    }
	}

	return adlist;
    }

    /**
      File Entry / Extended File EntryのAllocation Descriptors領域に対する処理。

      @param f  アクセサ
    */
    public void postReadHook(UDF_RandomAccess f) throws UDF_Exception, IOException {
	UDF_AD next_ad = null;
	//adlist = new UDF_ElementList();

	int flags = getAllocType();

	UDF_ElementList v = new UDF_ElementList();

	//　ADの種類別に読み込みます　//
	switch(flags & 0x07) {

	case UDF_icbtag.SHORT_AD:
	    next_ad = read_short_ad(v);
	    break;

	case UDF_icbtag.LONG_AD:
	    next_ad = read_long_ad(v);
	    break;

	case UDF_icbtag.EXT_AD:
	    next_ad = read_ext_ad(v);
	    break;


	case UDF_icbtag.DIRECT:
	    if (read_direct(f, v)) {
		break;
	    }
	    // through default

	default:
	    throw new UDF_ICBException(this,
				   	"SYS: BAD ICB FLAGS",
				   	UDF_Exception.T_ECMA167_3, 
				   	UDF_ICBException.C_BADFLAGS, 
				   	flags);
	}

	getAllocDesc().replaceChildren(v);

	// flag3がある場合はその先も
	if(next_ad != null)
	    parse_desc258(this, next_ad);
    }

    /**
      ADを全て削除する。

      alloc typeが3の場合は何もしない

      flag3のADは削除しないが、flag3のADが差す先の desc258の中身は全て削除される。

      ファイルサイズの調整は行われない。

    */
    public void removeAllAD( ) throws UDF_Exception {
        int type = getAllocType();

        if (type == UDF_icbtag.SHORT_AD) {
        }
        else
        if (type == UDF_icbtag.LONG_AD) {
        }
        else
        if (type == UDF_icbtag.EXT_AD) {
        }
        else {
            throw new UDF_InternalException(this, "Bad Alloc Type : " + getAllocType());
        }

        UDF_bytes b = getAllocDesc();

	int size = 0;
	UDF_ElementBase[] el = getAllocDesc().getChildren();
	for(int i=0 ; i<el.length ; ++i){
	    UDF_AD ad = (UDF_AD)el[i];
	    if(ad.getFlag() != 3)
		b.removeChild(ad);
	    else
		size += ad.getSize();
	}
        b.setSize(size);

	if(getNextADDesc() != null)
	    getNextADDesc().removeAllAD();
    }


    /// short_ad形式のADを読み取る。
    /**
      ADの領域がshort_ad形式で保存されているものとして読み取る。

      @param rab バッファアクセサ
      @return AD flag 3のものがあった場合そのADを返す。ない場合はnullを返す。
    */
    private UDF_AD read_short_ad(UDF_ElementList el) throws UDF_Exception, IOException {
	UDF_AD next_ad = null;
	UDF_bytes b = getAllocDesc();
	UDF_RandomAccessBuffer rab = b.genRandomAccessBytes();

	while(!rab.eof()){

	    UDF_AD ad = new UDF_short_ad(b, null, null);

	    ad.readFrom(rab);
	    el.add(ad);
	    if(ad.getFlag() == 3)
		next_ad = ad;
	    /*
	    else if(ad.getFlag() == 0)
		adlist.add(ad);
	    else if(ad.getFlag() == 1)
		adlist.add(ad);
	    */
	}

	return next_ad;
    }

    ///  long_ad形式のADを読み取る。
    /**
      ADの領域が long_ad形式で保存されているものとして読み取る。

      @param rab バッファアクセサ
      @return AD flag 3のものがあった場合そのADを返す。ない場合はnullを返す。
    */
    private UDF_AD read_long_ad(UDF_ElementList el) throws UDF_Exception, IOException {
	UDF_AD next_ad = null;

	UDF_bytes b = getAllocDesc();
	UDF_RandomAccessBuffer rab = b.genRandomAccessBytes();//new UDF_RandomAccessBuffer(b.getData());

	while(!rab.eof()){

	    UDF_AD ad = new UDF_long_ad(b, null, null);

	    ad.readFrom(rab);
	    el.add(ad);
	    if(ad.getFlag() == 3)
		next_ad = ad;
	    /*
	    else if(ad.getFlag() == 0)
		adlist.add(ad);
	    else if(ad.getFlag() == 1)
		adlist.add(ad);
	    */
	}
	return next_ad;
    }

    /// ext_ad形式のADを読み取る。
    /**
      ADの領域がext_ad形式で保存されているものとして読み取る。

      @param rab バッファアクセサ
      @return AD flag 3のものがあった場合そのADを返す。ない場合はnullを返す。
    */
    private UDF_AD read_ext_ad(UDF_ElementList el) throws UDF_Exception, IOException {
	UDF_AD next_ad = null;

	UDF_bytes b = getAllocDesc();
	UDF_RandomAccessBuffer rab = b.genRandomAccessBytes();//new UDF_RandomAccessBuffer(b.getData());

	while(!rab.eof()){

	    UDF_AD ad = new UDF_ext_ad(b, null, null);

	    ad.readFrom(rab);
	    el.add(ad);
	    if(ad.getFlag() == 3)
		next_ad = ad;
	    /*
	    else if(ad.getFlag() == 0)
		adlist.add(ad);
	    else if(ad.getFlag() == 1)
		adlist.add(ad);
	    */
	}
	return next_ad;
    }

    /**
      File Entry / Extended File EntryのADの領域にデータ本体が存在しているとして読み込む。
      File Entry / Extended File EntryのADの領域に
      データが存在する（データが embedなファイル）として読み取る。

      @param f  バッファアクセサ
      @param el 呼び出し先に設定するエレメントリスト。
      @return 読み込みに成功したか否か
    */
    private boolean read_direct(UDF_RandomAccess f, UDF_ElementList el) throws UDF_Exception, IOException {

	long fp = f.getAbsPointer();


	UDF_bytes b = getAllocDesc();
	UDF_RandomAccessBuffer rab = b.genRandomAccessBytes();//new UDF_RandomAccessBuffer(b.getData());

	//　Directory だった場合　//
	if (getICBTag().isTypeDirectory()) {
	    
	    com.udfv.ecma167.UDF_Directory dir = (com.udfv.ecma167.UDF_Directory)createElement("UDF_Directory", null, UDF_XML.DIRECTORY);
	    dir.setSize((int)getAllocDesc().getSize());  //ここで設定したサイズは readFrom()後に変る
	    long readsz = dir.readFrom(rab);

	    rab.seek(dir.getSize());
	    UDF_pad pad = new UDF_pad(this, null, null, getAllocDesc().getSize());
	    pad.readFrom(rab);

	    el.add(dir);
	    el.add(pad);
	    return true;
	}
	else if (getICBFileType() == UDF_icbtag.T_SYMLINK){
	    UDF_PathComponent pc = (UDF_PathComponent)createElement("UDF_PathComponent", null, null);
	    while(!rab.eof()){
		long readsz = pc.readFrom(rab);
		el.add(pc);
	    }
	    return true;
	}
	else{
	    //　ファイルとして読み込みます　//
	    long len = rab.length();
	    
	    if (len <= (env.LBS - ((fp % env.LBS) - len))) {
		
		UDF_bytes embed_data = (UDF_bytes) createElement("UDF_bytes", "UDF_ADDesc", "UDF_bytes");
		embed_data.setSize((int)len);
		embed_data.readFrom(rab);
		el.add(embed_data);
		return true;
	    }
	}

	return false;
    }


    /**
      ADがflag 3により別論理ブロックに続いていたときに、
      flag 3で指定されるAllocation Extent Descriptorを読み取る。

      @param prev 一つ前のAD	
      @param next flag 3を持つAD
    */
    private void parse_desc258(UDF_ADDesc prev, UDF_AD next) throws UDF_Exception, IOException {

	if(next == null){
	    return;
	}

	UDF_Extent desc258_ext = (UDF_Extent)createElement("UDF_Extent", null, null);
	//desc258_ext.setMirror(getFirstADDesc().getMirror());
	desc258_ext.setPartSubno(getFirstADDesc().getPartSubno());

	desc258_ext.addExtent(next.getLbn(), next.getPartRefNo(), next.getLen(), 3);

	String desc258_ext_label = UDF_Label.genExtentLabel(desc258_ext);
	desc258_ext.setAttribute("id", desc258_ext_label);
	next.getExtentLbn().setAttribute("ref", desc258_ext_label + ".lbn");
	if(next.getExtentPartRefNo() != null)
	    next.getExtentPartRefNo().setAttribute("ref", desc258_ext_label + ".partno");
	next.getExtentLen().setAttribute("ref", desc258_ext_label + ".len");

	UDF_desc258 desc258 = (UDF_desc258)createElement("UDF_desc258", null, null);
	UDF_pad pad = (UDF_pad)createElement("UDF_pad", null, null);
	pad.setAlign(next.getLen());

	//リスト構築
	prev.setNextADDesc(desc258);
	desc258.setPrevADDesc(prev);

	UDF_RandomAccessExtent rae = desc258_ext.genRandomAccessExtent();

	//desc258.setICBTag(getICBTag());
	desc258.readFrom(rae);
	pad.readFrom(rae);

	env.root.appendChild(desc258_ext);
	desc258_ext.appendChild(desc258);
	desc258_ext.appendChild(pad);
    }

    // UDF_AD[]タイプのものが微妙につかいにくい場面もあるので追加
    /**
      ADを追加する。

      @param ee		AD
      
      もし、デスクリプタが FLAG3のADでチェーンされている場合はチェーンの最後のADに追加する
    */
    public void appendAD(UDF_ExtentElem[] ee) throws UDF_Exception{
	if(ee == null)
	    return;

	if(ee.length == 0)
	    return;

        int type = getAllocType();
	int size = 0;
	UDF_AD[] ad = new UDF_AD[ee.length];

	for(int i=0 ; i<ee.length ; ++i){
	    switch(type){
	    case 0:
		ad[i] = (UDF_AD)createElement("UDF_short_ad", null, null);
		break;
	    case 1:
		ad[i] = (UDF_AD)createElement("UDF_long_ad", null, null);
		break;
	    case 2:
		ad[i] = (UDF_AD)createElement("UDF_ext_ad", null, null);
		break;
	    default:
		throw new UDF_InternalException(this, "Bad Alloc Type : " + type);
	    }
	    ad[i].setDefaultValue();
	    ad[i].setLbn(ee[i].getLoc());
	    ad[i].setPartRefNo(ee[i].getPartRefNo());
	    ad[i].setLen(ee[i].getLen());

	    size += ad[i].getSize();
	}

	//チェーンの最後の feに追加する
	UDF_ADDesc fe = getLastADDesc();

	fe.getAllocDesc().setSize(fe.getAllocDesc().getSize() + size);
	for(int i=0 ; i<ad.length ; ++i)
	    fe.getAllocDesc().appendChild(ad[i]);

	fe.getLenOfAllocDesc().setValue(fe.getAllocDesc().getSize());
	fe.getDescTag().getDescCRCLen().setValue(fe.getSize() - 16);

	UDF_FEDesc fe0 = getFirstADDesc();
	fe0.getInfoLen().setValue(fe0.calcInfoLenFromAD());
	fe0.getLogicalBlocksRecorded().setValue(fe0.calcLogicalBlocksRecordedFromAD());
	if(fe0.getObjectSize() != null)
	    fe0.getObjectSize().setValue(fe0.calcObjectSizeFromAD());
    }

    /**
      ADを追加する。

      @param ee		AD
      
      もし、デスクリプタが FLAG3のADでチェーンされている場合はチェーンの最後のADに追加する
    */
    public void appendAD(UDF_ElementList ads) throws UDF_Exception{
        if (ads == null) {
            return;
        }
        if (ads.size() < 1) {
            return;
        }

        String name = ads.firstElement().getName();
        int type = getAllocType();

        if (type == UDF_icbtag.SHORT_AD && name.equals("UDF_short_ad")) {
        }
        else
        if (type == UDF_icbtag.LONG_AD  && name.equals("UDF_long_ad")) {
        }
        else
        if (type == UDF_icbtag.EXT_AD   && name.equals("UDF_ext_ad")) {
        }
        else {
            throw new UDF_InternalException(this, "Bad Alloc Type : " + name + " (record : " + type + ")");
        }

	int size = 0;
        UDF_ElementList el = new UDF_ElementList();

        for (Iterator it = ads.iterator() ; it.hasNext() ; ) {
	    UDF_AD ad = (UDF_AD)it.next();
            el.add(ad);
	    size += ad.getSize();
        }
	//チェーンの最後の feに追加する
	UDF_ADDesc fe = getLastADDesc();

	fe.getAllocDesc().setSize(fe.getAllocDesc().getSize() + size);
	fe.getAllocDesc().appendChildren(el);

	fe.getLenOfAllocDesc().setValue(fe.getAllocDesc().getSize());
	fe.getDescTag().getDescCRCLen().setValue(fe.getSize() - 16);

	//チェーンの最初の feの info-len, logical-blocks-recordedを変更する
	UDF_FEDesc fe0 = getFirstADDesc();
	fe0.getInfoLen().setValue(fe0.calcInfoLenFromAD());
	fe0.getLogicalBlocksRecorded().setValue(fe0.calcLogicalBlocksRecordedFromAD());
	if(fe0.getObjectSize() != null)
	    fe0.getObjectSize().setValue(fe0.calcObjectSizeFromAD());
    }

    /**
      ADを追加する。

      @param ee		AD
      
      もし、デスクリプタが FLAG3のADでチェーンされている場合はチェーンの最後のADに追加する
    */
    public void appendAD(UDF_ADList ads) throws UDF_Exception{
        if (ads == null) {
            return;
        }
        if (ads.size() < 1) {
            return;
        }

        String name = ads.firstElement().getName();
        int type = getAllocType();

        if (type == UDF_icbtag.SHORT_AD && name.equals("UDF_short_ad")) {
        }
        else
        if (type == UDF_icbtag.LONG_AD  && name.equals("UDF_long_ad")) {
        }
        else
        if (type == UDF_icbtag.EXT_AD   && name.equals("UDF_ext_ad")) {
        }
        else {
            throw new UDF_InternalException(this, "Bad Alloc Type : " + name + " (record : " + type + ")");
        }

	int size = 0;
        UDF_ElementList el = new UDF_ElementList();

        for (Iterator it = ads.iterator() ; it.hasNext() ; ) {
	    UDF_AD ad = (UDF_AD)it.next();
            el.add(ad);
	    size += ad.getSize();
        }
	//チェーンの最後の feに追加する
	UDF_ADDesc fe = getLastADDesc();

	fe.getAllocDesc().setSize(fe.getAllocDesc().getSize() + size);
	fe.getAllocDesc().appendChildren(el);

	fe.getLenOfAllocDesc().setValue(fe.getAllocDesc().getSize());
	fe.getDescTag().getDescCRCLen().setValue(fe.getSize() - 16);

	//チェーンの最初の feの info-len, logical-blocks-recordedを変更する
	UDF_FEDesc fe0 = getFirstADDesc();
	fe0.getInfoLen().setValue(fe0.calcInfoLenFromAD());
	fe0.getLogicalBlocksRecorded().setValue(fe0.calcLogicalBlocksRecordedFromAD());
	if(fe0.getObjectSize() != null)
	    fe0.getObjectSize().setValue(fe0.calcObjectSizeFromAD());
    }

    /**
      ADを追加する。

      @param ads 追加したいAD配列。

      もし、デスクリプタが FLAG3のADでチェーンされている場合はチェーンの最後のADに追加する。
    */
    public void appendAD(UDF_AD [] ads) throws UDF_Exception {

        if (ads == null) {
            return;
        }
        if (ads.length < 1) {
            return;
        }

        String name = ads[0].getName();
        int type = getAllocType();

        if (type == UDF_icbtag.SHORT_AD && name.equals("UDF_short_ad")) {
        }
        else
        if (type == UDF_icbtag.LONG_AD  && name.equals("UDF_long_ad")) {
        }
        else
        if (type == UDF_icbtag.EXT_AD   && name.equals("UDF_ext_ad")) {
        }
        else {
            throw new UDF_InternalException(this, "Bad Alloc Type : " + name + " (record : " + type + ")");
        }

	int size = 0;
        UDF_ElementList el = new UDF_ElementList();
        for (int i = 0 ; i < ads.length ; i++) {
            el.add(ads[i]);
	    size += ads[i].getSize();
        }
	//チェーンの最後の feに追加する
	UDF_ADDesc fe = getLastADDesc();

	fe.getAllocDesc().setSize(fe.getAllocDesc().getSize() + size);
	fe.getAllocDesc().appendChildren(el);

	fe.getLenOfAllocDesc().setValue(fe.getAllocDesc().getSize());
	fe.getDescTag().getDescCRCLen().setValue(fe.getSize() - 16);

	//チェーンの最初の feの info-len, logical-blocks-recordedを変更する
	UDF_FEDesc fe0 = getFirstADDesc();
	fe0.getInfoLen().setValue(fe0.calcInfoLenFromAD());
	fe0.getLogicalBlocksRecorded().setValue(fe0.calcLogicalBlocksRecordedFromAD());
	if(fe0.getObjectSize() != null)
	    fe0.getObjectSize().setValue(fe0.calcObjectSizeFromAD());
    }

    /**
       サイズ0のADを削除する

       ただし、最低一個は残す
     */
    public void normalize(){
	    int size = getAllocDesc().getSize();
	    try{
		UDF_AD[] ad = getAD();
		int n_ad = ad.length;
		for(int i=0 ; i<ad.length && n_ad > 1 ; ++i){
		    if(ad[i].getLen() == 0){
			size -= ad[i].getSize();
			getAllocDesc().removeChild(ad[i]);
			--n_ad;
		    }
		}
	    }
	    catch(ClassCastException e){
		ignoreMsg("UDF_ADDesc#normalize()", e);
	    }

	    getAllocDesc().setSize(size);
	    getLenOfAllocDesc().setValue(size);
    }
};

