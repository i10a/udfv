/*
*/
package com.udfv.ecma167;

import java.io.*;
import java.util.*;
import org.w3c.dom.*;
import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.encoding.*;
import com.udfv.access.*;
import com.udfv.exception.*;
import com.udfv.ecma167.*;

/**
Anchor&nbsp;Volume&nbsp;Descriptor&nbsp;Pointer&nbsp;を表現するクラス。<br/>


<dl>

<dt>フィールド一覧</dt>
<dd>
<table border="1">
<tr><td>メンバ</td><td>型</td><td>サイズ</td></tr>
<tr><td><b>DescTag</b></td><td><b>UDF_tag</b></td><td><i>descTag.getSize()</i></td></tr>
<tr><td><b>MainVolDescSeqExtent</b></td><td><b>UDF_extent_ad</b></td><td><i>mainVolDescSeqExtent.getSize()</i></td></tr>
<tr><td><b>ReserveVolDescSeqExtent</b></td><td><b>UDF_extent_ad</b></td><td><i>reserveVolDescSeqExtent.getSize()</i></td></tr>
<tr><td><b>Reserved</b></td><td><b>UDF_bytes</b></td><td><i>480</i></td></tr>
</table>
</dd>
</dl>
*/
public class UDF_desc2 extends UDF_CrcDesc 
{

    /**
	このオブジェクトのパッケージ名を除いたクラス名を取得する。

	@return クラス名
    */
    public static String getUDFClassName( ) {
        return "UDF_desc2";
    }

    /**
	コンストラクタ。

	@param elem   親エレメント。
	@param prefix ネームスペース。
	@param name   エレメント名。
    */
    public UDF_desc2(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }
    /**
	このUDFエレメントのサイズを返す。

	@return UDFエレメントのサイズ。
    */
    public int getSize(){
	return 0+descTag.getSize()+mainVolDescSeqExtent.getSize()+reserveVolDescSeqExtent.getSize()+reserved.getSize();
    }

    /**
	このUDFエレメントのサイズをlong 型で返す。

	@return UDFエレメントのサイズ。
    */
    public long getLongSize(){
	return (long)0+descTag.getSize()+mainVolDescSeqExtent.getSize()+reserveVolDescSeqExtent.getSize()+reserved.getSize();
    }
    private UDF_tag descTag;
    private UDF_extent_ad mainVolDescSeqExtent;
    private UDF_extent_ad reserveVolDescSeqExtent;
    private UDF_bytes reserved;

    /**
	descTagを取得する。

	@return 取得したdescTag を返す。
    */
    public UDF_tag getDescTag(){return descTag;}
    /**
	mainVolDescSeqExtentを取得する。

	@return 取得したmainVolDescSeqExtent を返す。
    */
    public UDF_extent_ad getMainVolDescSeqExtent(){return mainVolDescSeqExtent;}
    /**
	reserveVolDescSeqExtentを取得する。

	@return 取得したreserveVolDescSeqExtent を返す。
    */
    public UDF_extent_ad getReserveVolDescSeqExtent(){return reserveVolDescSeqExtent;}
    /**
	reservedを取得する。

	@return 取得したreserved を返す。
    */
    public UDF_bytes getReserved(){return reserved;}

    /**
	descTagを設定する。

	@param	v 設定する値。
    */
    public void setDescTag(UDF_tag v){replaceChild(v, descTag); descTag = v;}
    /**
	mainVolDescSeqExtentを設定する。

	@param	v 設定する値。
    */
    public void setMainVolDescSeqExtent(UDF_extent_ad v){replaceChild(v, mainVolDescSeqExtent); mainVolDescSeqExtent = v;}
    /**
	reserveVolDescSeqExtentを設定する。

	@param	v 設定する値。
    */
    public void setReserveVolDescSeqExtent(UDF_extent_ad v){replaceChild(v, reserveVolDescSeqExtent); reserveVolDescSeqExtent = v;}
    /**
	reservedを設定する。

	@param	v 設定する値。
    */
    public void setReserved(UDF_bytes v){replaceChild(v, reserved); reserved = v;}

    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long rsize = 0;
	preReadHook(f);
	descTag = (UDF_tag)createElement("UDF_tag", "", "desc-tag");
	rsize += descTag.readFrom(f);
	mainVolDescSeqExtent = (UDF_extent_ad)createElement("UDF_extent_ad", "", "main-vol-desc-seq-extent");
	rsize += mainVolDescSeqExtent.readFrom(f);
	reserveVolDescSeqExtent = (UDF_extent_ad)createElement("UDF_extent_ad", "", "reserve-vol-desc-seq-extent");
	rsize += reserveVolDescSeqExtent.readFrom(f);
	reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
	reserved.setSize(480);
	rsize += reserved.readFrom(f);
	apply( );
	postReadHook(f);
	return rsize;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long wsize = 0;
	wsize += descTag.writeTo(f);
	wsize += mainVolDescSeqExtent.writeTo(f);
	wsize += reserveVolDescSeqExtent.writeTo(f);
	wsize += reserved.writeTo(f);
	return wsize;
    }

    /**
	XMLのノードを指定して読み込む。

	@param n 読み込み先ノード。
    */
    public void readFromXML(Node n) throws UDF_Exception{
	preReadFromXMLHook(n);
	NodeList nl = n.getChildNodes();

	for(int i=0 ; i<nl.getLength() ; ++i){
	    Node child = nl.item(i);
	    if(child.getNodeType() != Node.ELEMENT_NODE)
		continue;
	    String name = child.getLocalName();
	    if(false)
		;
	    else if(name.equals("desc-tag")){
		descTag = (UDF_tag)createElement("UDF_tag", "", "desc-tag");
		descTag.readFromXML(child);
	    }
	    else if(name.equals("main-vol-desc-seq-extent")){
		mainVolDescSeqExtent = (UDF_extent_ad)createElement("UDF_extent_ad", "", "main-vol-desc-seq-extent");
		mainVolDescSeqExtent.readFromXML(child);
	    }
	    else if(name.equals("reserve-vol-desc-seq-extent")){
		reserveVolDescSeqExtent = (UDF_extent_ad)createElement("UDF_extent_ad", "", "reserve-vol-desc-seq-extent");
		reserveVolDescSeqExtent.readFromXML(child);
	    }
	    else if(name.equals("reserved")){
		reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
		reserved.setSize(480);
		reserved.readFromXML(child);
	    }
	    else{
		System.err.println("Unknown XML tag:" + name);
	    }
	}
	apply( );
	postReadFromXMLHook(n);

	//読んだら消す(もういらないので)
	n.getParentNode().removeChild(n);
    }

    /**
	初期値を設定する
    */
    public void setDefaultValue(){
	super.setDefaultValue();
	descTag = (UDF_tag)createElement("UDF_tag", "", "desc-tag");
	descTag.setDefaultValue();
	mainVolDescSeqExtent = (UDF_extent_ad)createElement("UDF_extent_ad", "", "main-vol-desc-seq-extent");
	mainVolDescSeqExtent.setDefaultValue();
	reserveVolDescSeqExtent = (UDF_extent_ad)createElement("UDF_extent_ad", "", "reserve-vol-desc-seq-extent");
	reserveVolDescSeqExtent.setDefaultValue();
	reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
	reserved.setSize(480);
	reserved.setDefaultValue();

	apply( );
	postSetDefaultValueHook();
    }


    /**
	複製する
    */
    public UDF_Element duplicateElement(){
	UDF_desc2 dup = new UDF_desc2(this, null, getName());
	dup.setDefaultValue();
	dup.duplicateHook(this);
	dup.setDescTag((UDF_tag)descTag.duplicateElement());
	dup.setMainVolDescSeqExtent((UDF_extent_ad)mainVolDescSeqExtent.duplicateElement());
	dup.setReserveVolDescSeqExtent((UDF_extent_ad)reserveVolDescSeqExtent.duplicateElement());
	dup.setReserved((UDF_bytes)reserved.duplicateElement());

	apply();

	return dup;
    }

    /**
        子の UDF Element の Node を append しなおす。

    */
    private void apply( ) {

	removeAllChildren();
	appendChild(descTag);
	appendChild(mainVolDescSeqExtent);
	appendChild(reserveVolDescSeqExtent);
	appendChild(reserved);
    }

    public String getInfo(int indent){
	String a = "";
	a += UDF_Util.repeat(' ', indent);
	a += descTag.getInfo(indent + 1);
	a += mainVolDescSeqExtent.getInfo(indent + 1);
	a += reserveVolDescSeqExtent.getInfo(indent + 1);
	a += reserved.getInfo(indent + 1);
      return a;
    }
    public void debug(int indent){
	System.err.print(UDF_Util.repeat(' ', indent));

	String info = " gp=" + getGlobalPoint();
	info += " pn=" + getElemPartRefNo();
	info += " sn=" + getPartSubno();
	System.err.println(getName() + ": (" + hashCode() + ")" + info);	descTag.debug(indent + 1);
	mainVolDescSeqExtent.debug(indent + 1);
	reserveVolDescSeqExtent.debug(indent + 1);
	reserved.debug(indent + 1);
    }
//begin:add your code here
    public int getFixedTagId() { return 2; }
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	UDF_Error err;
	final short category = UDF_Error.C_ECMA167;
	
	
	el.addError(super.verify());  // Descriptor Tag
	
	err = verifyId();
	if(err.isError()){
    
	    err.setRefer("3/10.2.1");
	    err.setLevel(UDF_Error.L_ALERT);  // Anchor だけ特別
	    el.addError(err);
	}
	
	int  mvdsloc = 0, rvdsloc = 0;
	long mvdslen = 0, rvdslen = 0;
	
	mvdsloc = mainVolDescSeqExtent.getExtentLoc().getIntValue();
	mvdslen = mainVolDescSeqExtent.getExtentLen().getLongValue();
	rvdsloc = reserveVolDescSeqExtent.getExtentLoc().getIntValue();
	rvdslen = reserveVolDescSeqExtent.getExtentLen().getLongValue();
	if(mvdslen == 0 && rvdslen == 0){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ALERT, "",
			 "Main Volume Descriptor Sequence Extent and Reserve Volume Descriptor Sequence Extent are 0."));
	}
	
	String VDS_ERRMSG = "This field shall specify an extent of the Main Volume Descriptor Sequence.";

	// MVDS のExtent が正しいか？
	try{
	    int  env_mvdsloc = env.getMVDSLoc2();
	    long env_mvdslen = env.getMVDSLen2();
	    
	    err = getMainVolDescSeqExtent().verifyExtLoc(env_mvdsloc);
	    if(err.isError()){
		
		err.setRName("Main Volume Descriptor Sequence Extent");
		err.setMessage(VDS_ERRMSG);
		err.setRefer("3/10.2.2");
		el.addError(err);
	    }
	    err = getMainVolDescSeqExtent().verifyExtLen(env_mvdslen);
	    if(err.isError()){
		
		err.setRName("Main Volume Descriptor Sequence Extent");
		err.setMessage(VDS_ERRMSG);
		err.setRefer("3/10.2.2");
		el.addError(err);
	    }
	}
	// MVDSが存在しない(AVDPで示される位置に記録されていない)場合、キャッチされる
	catch(UDF_VolException e){
	    // ここはボリューム認識時に必ずコールされるUDF_Image.readAVDP()から呼ばれる。
	    // ボリューム認識時にはまだMVDSは読み込まれていないため、必ずこの例外が発生する。
	    // つまり毎回printStackTrace()が表示されてウザイのでコメントアウト
//	    e.printStackTrace();
	    
	    err = getMainVolDescSeqExtent().verifyExtLoc(mvdsloc + 1); // 単にエラーインスタンスが欲しいだけなので、不正な値を入れる
	    err.setExpectedValue("");
	    err.setRName("Main Volume Descriptor Sequence Extent");
	    err.setMessage(VDS_ERRMSG);
	    err.setRefer("3/10.2.2");
	    el.addError(err);
	}
	
	
	// RVDS のExtent が正しいか？
	try{
	    int  env_rvdsloc = env.getRVDSLoc2();
	    long env_rvdslen = env.getRVDSLen2();
	    
	    err = getReserveVolDescSeqExtent().verifyExtLoc(env_rvdsloc);
	    if(err.isError()){
		
		err.setRName("Reserve Volume Descriptor Sequence Extent");
		err.setMessage(VDS_ERRMSG);
		err.setRefer("3/10.2.2");
		el.addError(err);
	    }
	    
	    err = getReserveVolDescSeqExtent().verifyExtLen(env_rvdslen);
	    if(err.isError()){
		
		err.setRName("Reserve Volume Descriptor Sequence Extent");
		err.setMessage(VDS_ERRMSG);
		err.setRefer("3/10.2.2");
		el.addError(err);
	    }
	}
	// RVDSが存在しない(AVDPで示される位置に記録されていない)場合、キャッチされる
	catch(UDF_VolException e){
	    // ここはボリューム認識時に必ずコールされるUDF_Image.readAVDP()から呼ばれているため、
	    // ボリューム認識時にはまだRVDSは読み込まれていないため、必ずこの例外が発生する。
	    // つまり毎回printStackTrace()が表示されてウザイのでコメントアウト
//	    e.printStackTrace();
	    
	    // RVDSに限り、Extent Lengthが0であれば問題ナシ
	    if(rvdslen != 0){
		err = getMainVolDescSeqExtent().verifyExtLoc(rvdsloc + 1); // 単にエラーインスタンスが欲しいだけなので、不正な値を入れる
		err.setExpectedValue("");
		err.setRName("Reserve Volume Descriptor Sequence Extent");
		err.setMessage(VDS_ERRMSG);
		err.setRefer("3/10.2.2");
		el.addError(err);
	    }
	}
	
	
	if(!UDF_Util.isAllZero(reserved.getData())){
	    
	    el.addError(new UDF_Error(UDF_Error.C_ECMA167, UDF_Error.L_ERROR, "Reserved",
			"This field shall be reserved for future standardisation and all bytes shall be set to #00.",
			"3/10.2.4"));
	}
	
	el.setGlobalPoint(getGlobalPoint());
	el.setRName("Anchor Volume Descriptor Pointer");
	return el;
    }

    public int getMVDSLoc(){
	return getMainVolDescSeqExtent().getExtentLoc().getIntValue();
    }
    public long getMVDSLen(){
	return getMainVolDescSeqExtent().getExtentLen().getIntValue();
    }
    public int getRVDSLoc(){
	return getReserveVolDescSeqExtent().getExtentLoc().getIntValue();
    }
    public long getRVDSLen(){
	return getReserveVolDescSeqExtent().getExtentLen().getIntValue();
    }
    public void recalc(short type, UDF_RandomAccess f){
	super.recalc(type, f);
	try{
	    if(type == RECALC_ENV){
		long gp = getGlobalPoint();
		if(gp == 256 * env.LBS){
		    env.anchorVolDescPointer[0] = this;
		}
		else if(gp == env.image_size - 257 * env.LBS){
		    env.anchorVolDescPointer[1] = this;
		}
		else if(gp == env.image_size - 1 * env.LBS){
		    env.anchorVolDescPointer[2] = this;
		}
	    }
	}
	catch(Exception e){
	    ignoreMsg("recalc", e);
	}
    }
    
//end:
};
