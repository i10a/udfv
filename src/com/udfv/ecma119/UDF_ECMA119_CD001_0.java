/*
*/
package com.udfv.ecma119;

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
ECMA119/8.2で規定されるBootRecordを表現するクラス。

<dl>

<dt>フィールド一覧</dt>
<dd>
<table border="1">
<tr><td>メンバ</td><td>型</td><td>サイズ</td></tr>
<tr><td><b>VolDescType</b></td><td><b>UDF_uint8</b></td><td><i>1</i></td></tr>
<tr><td><b>StandardId</b></td><td><b>UDF_bytes</b></td><td><i>5</i></td></tr>
<tr><td><b>VolDescVersion</b></td><td><b>UDF_uint8</b></td><td><i>1</i></td></tr>
<tr><td><b>BootSystemId</b></td><td><b>UDF_bytes</b></td><td><i>32</i></td></tr>
<tr><td><b>BootId</b></td><td><b>UDF_bytes</b></td><td><i>32</i></td></tr>
<tr><td><b>BootSystemUse</b></td><td><b>UDF_bytes</b></td><td><i>1977</i></td></tr>
</table>
</dd>
</dl>
*/
public class UDF_ECMA119_CD001_0 extends UDF_Element 
{

    /**
	このオブジェクトのパッケージ名を除いたクラス名を取得する。

	@return クラス名
    */
    public static String getUDFClassName( ) {
        return "UDF_ECMA119_CD001_0";
    }

    /**
	コンストラクタ。

	@param elem   親エレメント。
	@param prefix ネームスペース。
	@param name   エレメント名。
    */
    public UDF_ECMA119_CD001_0(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }
    /**
	このUDFエレメントのサイズを返す。

	@return UDFエレメントのサイズ。
    */
    public int getSize(){
	return 0+1+standardId.getSize()+1+bootSystemId.getSize()+bootId.getSize()+bootSystemUse.getSize();
    }

    /**
	このUDFエレメントのサイズをlong 型で返す。

	@return UDFエレメントのサイズ。
    */
    public long getLongSize(){
	return (long)0+1+standardId.getSize()+1+bootSystemId.getSize()+bootId.getSize()+bootSystemUse.getSize();
    }
    private UDF_uint8 volDescType;
    private UDF_bytes standardId;
    private UDF_uint8 volDescVersion;
    private UDF_bytes bootSystemId;
    private UDF_bytes bootId;
    private UDF_bytes bootSystemUse;

    /**
	volDescTypeを取得する。

	@return 取得したvolDescType を返す。
    */
    public UDF_uint8 getVolDescType(){return volDescType;}
    /**
	standardIdを取得する。

	@return 取得したstandardId を返す。
    */
    public UDF_bytes getStandardId(){return standardId;}
    /**
	volDescVersionを取得する。

	@return 取得したvolDescVersion を返す。
    */
    public UDF_uint8 getVolDescVersion(){return volDescVersion;}
    /**
	bootSystemIdを取得する。

	@return 取得したbootSystemId を返す。
    */
    public UDF_bytes getBootSystemId(){return bootSystemId;}
    /**
	bootIdを取得する。

	@return 取得したbootId を返す。
    */
    public UDF_bytes getBootId(){return bootId;}
    /**
	bootSystemUseを取得する。

	@return 取得したbootSystemUse を返す。
    */
    public UDF_bytes getBootSystemUse(){return bootSystemUse;}

    /**
	volDescTypeを設定する。

	@param	v 設定する値。
    */
    public void setVolDescType(UDF_uint8 v){replaceChild(v, volDescType); volDescType = v;}
    /**
	standardIdを設定する。

	@param	v 設定する値。
    */
    public void setStandardId(UDF_bytes v){replaceChild(v, standardId); standardId = v;}
    /**
	volDescVersionを設定する。

	@param	v 設定する値。
    */
    public void setVolDescVersion(UDF_uint8 v){replaceChild(v, volDescVersion); volDescVersion = v;}
    /**
	bootSystemIdを設定する。

	@param	v 設定する値。
    */
    public void setBootSystemId(UDF_bytes v){replaceChild(v, bootSystemId); bootSystemId = v;}
    /**
	bootIdを設定する。

	@param	v 設定する値。
    */
    public void setBootId(UDF_bytes v){replaceChild(v, bootId); bootId = v;}
    /**
	bootSystemUseを設定する。

	@param	v 設定する値。
    */
    public void setBootSystemUse(UDF_bytes v){replaceChild(v, bootSystemUse); bootSystemUse = v;}

    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long rsize = 0;
	preReadHook(f);
	volDescType = (UDF_uint8)createElement("UDF_uint8", "ecma119", "vol-desc-type");
	rsize += volDescType.readFrom(f);
	standardId = (UDF_bytes)createElement("UDF_bytes", "ecma119", "standard-id");
	standardId.setSize(5);
	rsize += standardId.readFrom(f);
	volDescVersion = (UDF_uint8)createElement("UDF_uint8", "ecma119", "vol-desc-version");
	rsize += volDescVersion.readFrom(f);
	bootSystemId = (UDF_bytes)createElement("UDF_bytes", "ecma119", "boot-system-id");
	bootSystemId.setSize(32);
	rsize += bootSystemId.readFrom(f);
	bootId = (UDF_bytes)createElement("UDF_bytes", "ecma119", "boot-id");
	bootId.setSize(32);
	rsize += bootId.readFrom(f);
	bootSystemUse = (UDF_bytes)createElement("UDF_bytes", "ecma119", "boot-system-use");
	bootSystemUse.setSize(1977);
	rsize += bootSystemUse.readFrom(f);
	apply( );
	postReadHook(f);
	return rsize;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long wsize = 0;
	wsize += volDescType.writeTo(f);
	wsize += standardId.writeTo(f);
	wsize += volDescVersion.writeTo(f);
	wsize += bootSystemId.writeTo(f);
	wsize += bootId.writeTo(f);
	wsize += bootSystemUse.writeTo(f);
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
	    else if(name.equals("vol-desc-type")){
		volDescType = (UDF_uint8)createElement("UDF_uint8", "ecma119", "vol-desc-type");
		volDescType.readFromXML(child);
	    }
	    else if(name.equals("standard-id")){
		standardId = (UDF_bytes)createElement("UDF_bytes", "ecma119", "standard-id");
		standardId.setSize(5);
		standardId.readFromXML(child);
	    }
	    else if(name.equals("vol-desc-version")){
		volDescVersion = (UDF_uint8)createElement("UDF_uint8", "ecma119", "vol-desc-version");
		volDescVersion.readFromXML(child);
	    }
	    else if(name.equals("boot-system-id")){
		bootSystemId = (UDF_bytes)createElement("UDF_bytes", "ecma119", "boot-system-id");
		bootSystemId.setSize(32);
		bootSystemId.readFromXML(child);
	    }
	    else if(name.equals("boot-id")){
		bootId = (UDF_bytes)createElement("UDF_bytes", "ecma119", "boot-id");
		bootId.setSize(32);
		bootId.readFromXML(child);
	    }
	    else if(name.equals("boot-system-use")){
		bootSystemUse = (UDF_bytes)createElement("UDF_bytes", "ecma119", "boot-system-use");
		bootSystemUse.setSize(1977);
		bootSystemUse.readFromXML(child);
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
	volDescType = (UDF_uint8)createElement("UDF_uint8", "ecma119", "vol-desc-type");
	volDescType.setDefaultValue();
	standardId = (UDF_bytes)createElement("UDF_bytes", "ecma119", "standard-id");
	standardId.setSize(5);
	standardId.setDefaultValue();
	volDescVersion = (UDF_uint8)createElement("UDF_uint8", "ecma119", "vol-desc-version");
	volDescVersion.setDefaultValue();
	bootSystemId = (UDF_bytes)createElement("UDF_bytes", "ecma119", "boot-system-id");
	bootSystemId.setSize(32);
	bootSystemId.setDefaultValue();
	bootId = (UDF_bytes)createElement("UDF_bytes", "ecma119", "boot-id");
	bootId.setSize(32);
	bootId.setDefaultValue();
	bootSystemUse = (UDF_bytes)createElement("UDF_bytes", "ecma119", "boot-system-use");
	bootSystemUse.setSize(1977);
	bootSystemUse.setDefaultValue();

	apply( );
	postSetDefaultValueHook();
    }


    /**
	複製する
    */
    public UDF_Element duplicateElement(){
	UDF_ECMA119_CD001_0 dup = new UDF_ECMA119_CD001_0(this, null, getName());
	dup.setDefaultValue();
	dup.duplicateHook(this);
	dup.setVolDescType((UDF_uint8)volDescType.duplicateElement());
	dup.setStandardId((UDF_bytes)standardId.duplicateElement());
	dup.setVolDescVersion((UDF_uint8)volDescVersion.duplicateElement());
	dup.setBootSystemId((UDF_bytes)bootSystemId.duplicateElement());
	dup.setBootId((UDF_bytes)bootId.duplicateElement());
	dup.setBootSystemUse((UDF_bytes)bootSystemUse.duplicateElement());

	apply();

	return dup;
    }

    /**
        子の UDF Element の Node を append しなおす。

    */
    private void apply( ) {

	removeAllChildren();
	appendChild(volDescType);
	appendChild(standardId);
	appendChild(volDescVersion);
	appendChild(bootSystemId);
	appendChild(bootId);
	appendChild(bootSystemUse);
    }

    public String getInfo(int indent){
	String a = "";
	a += UDF_Util.repeat(' ', indent);
	a += volDescType.getInfo(indent + 1);
	a += standardId.getInfo(indent + 1);
	a += volDescVersion.getInfo(indent + 1);
	a += bootSystemId.getInfo(indent + 1);
	a += bootId.getInfo(indent + 1);
	a += bootSystemUse.getInfo(indent + 1);
      return a;
    }
    public void debug(int indent){
	System.err.print(UDF_Util.repeat(' ', indent));

	String info = " gp=" + getGlobalPoint();
	info += " pn=" + getElemPartRefNo();
	info += " sn=" + getPartSubno();
	System.err.println(getName() + ": (" + hashCode() + ")" + info);	volDescType.debug(indent + 1);
	standardId.debug(indent + 1);
	volDescVersion.debug(indent + 1);
	bootSystemId.debug(indent + 1);
	bootId.debug(indent + 1);
	bootSystemUse.debug(indent + 1);
    }
//begin:add your code here
//end:
};
