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
Short&nbsp;Allocation&nbsp;Descriptor&nbsp;を表現するクラス。<br/>


<dl>

<dt>フィールド一覧</dt>
<dd>
<table border="1">
<tr><td>メンバ</td><td>型</td><td>サイズ</td></tr>
<tr><td><b>ExtentLen</b></td><td><b>UDF_uint32a</b></td><td><i>extentLen.getSize()</i></td></tr>
<tr><td><b>ExtentPos</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
</table>
</dd>
</dl>
*/
public class UDF_short_ad extends UDF_AD 
{

    /**
	このオブジェクトのパッケージ名を除いたクラス名を取得する。

	@return クラス名
    */
    public static String getUDFClassName( ) {
        return "UDF_short_ad";
    }

    /**
	コンストラクタ。

	@param elem   親エレメント。
	@param prefix ネームスペース。
	@param name   エレメント名。
    */
    public UDF_short_ad(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }
    /**
	このUDFエレメントのサイズを返す。

	@return UDFエレメントのサイズ。
    */
    public int getSize(){
	return 0+extentLen.getSize()+4;
    }

    /**
	このUDFエレメントのサイズをlong 型で返す。

	@return UDFエレメントのサイズ。
    */
    public long getLongSize(){
	return (long)0+extentLen.getSize()+4;
    }
    private UDF_uint32a extentLen;
    private UDF_uint32 extentPos;

    /**
	extentLenを取得する。

	@return 取得したextentLen を返す。
    */
    public UDF_uint32a getExtentLen(){return extentLen;}
    /**
	extentPosを取得する。

	@return 取得したextentPos を返す。
    */
    public UDF_uint32 getExtentPos(){return extentPos;}

    /**
	extentLenを設定する。

	@param	v 設定する値。
    */
    public void setExtentLen(UDF_uint32a v){replaceChild(v, extentLen); extentLen = v;}
    /**
	extentPosを設定する。

	@param	v 設定する値。
    */
    public void setExtentPos(UDF_uint32 v){replaceChild(v, extentPos); extentPos = v;}

    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long rsize = 0;
	preReadHook(f);
	extentLen = (UDF_uint32a)createElement("UDF_uint32a", "", "extent-len");
	rsize += extentLen.readFrom(f);
	extentPos = (UDF_uint32)createElement("UDF_uint32", "", "extent-pos");
	rsize += extentPos.readFrom(f);
	apply( );
	postReadHook(f);
	return rsize;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long wsize = 0;
	wsize += extentLen.writeTo(f);
	wsize += extentPos.writeTo(f);
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
	    else if(name.equals("extent-len")){
		extentLen = (UDF_uint32a)createElement("UDF_uint32a", "", "extent-len");
		extentLen.readFromXML(child);
	    }
	    else if(name.equals("extent-pos")){
		extentPos = (UDF_uint32)createElement("UDF_uint32", "", "extent-pos");
		extentPos.readFromXML(child);
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
	extentLen = (UDF_uint32a)createElement("UDF_uint32a", "", "extent-len");
	extentLen.setDefaultValue();
	extentPos = (UDF_uint32)createElement("UDF_uint32", "", "extent-pos");
	extentPos.setDefaultValue();

	apply( );
	postSetDefaultValueHook();
    }


    /**
	複製する
    */
    public UDF_Element duplicateElement(){
	UDF_short_ad dup = new UDF_short_ad(this, null, getName());
	dup.setDefaultValue();
	dup.duplicateHook(this);
	dup.setExtentLen((UDF_uint32a)extentLen.duplicateElement());
	dup.setExtentPos((UDF_uint32)extentPos.duplicateElement());

	apply();

	return dup;
    }

    /**
        子の UDF Element の Node を append しなおす。

    */
    private void apply( ) {

	removeAllChildren();
	appendChild(extentLen);
	appendChild(extentPos);
    }

    public String getInfo(int indent){
	String a = "";
	a += UDF_Util.repeat(' ', indent);
	a += extentLen.getInfo(indent + 1);
	a += extentPos.getInfo(indent + 1);
      return a;
    }
    public void debug(int indent){
	System.err.print(UDF_Util.repeat(' ', indent));

	String info = " gp=" + getGlobalPoint();
	info += " pn=" + getElemPartRefNo();
	info += " sn=" + getPartSubno();
	System.err.println(getName() + ": (" + hashCode() + ")" + info);	extentLen.debug(indent + 1);
	extentPos.debug(indent + 1);
    }
//begin:add your code here

    public int getPartRefNo(){
	return getElemPartRefNo();//my_partno;
    }
    public int getLbn(){
	return extentPos.getIntValue();
    }
    public long getLen(){
	return extentLen.getLongValue() & 0x3fffffffL;
    }
    public int getFlag(){
	return extentLen.getFlag();
    }
    
    public void setPartRefNo(int partno){
	//my_partno = partno;
	setElemPartRefNo(partno);
    }
    public void setLbn(int lbn){
	extentPos.setValue(lbn);
    }
    public void setLen(long len){
	extentLen.setValue(len);
    }
    public void setFlag(int flag){
	//	extentLen.setValue(((int)getLen()) | (flag << 30));
	extentLen.setFlag(flag);
    }

    public UDF_uint32 getExtentLbn(){
	return extentPos;
    }
    public UDF_uint16 getExtentPartRefNo(){
	return null;
    }
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = super.verify();
	
	
	if(getLen() == 0 && getLbn() != 0){
	    
	    el.addError(new UDF_Error
			(UDF_Error.C_ECMA167, UDF_Error.L_ERROR, "Extent Position",
			 "This field shall specify the logical block number, within the partition the descriptor is recorded on, " +
			 "of the extent. If the extent's length is 0, no extent is specified and this field shall contain 0.",
			 "4/14.14.1.2", String.valueOf(getLbn()), "0"));
	}
	
	el.setRName("Short Allocation Descriptor");
	return el;
    }
    protected void duplicateHook(UDF_Element src) {
	super.duplicateHook(src);
	//short_adには partnoがないので、複製するだけでなく、
	//内部的な partnoを設定しておかないといけない
	setPartRefNo(((UDF_short_ad) src).getPartRefNo());
    }
    
//end:
};
