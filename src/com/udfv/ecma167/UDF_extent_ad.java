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
Extent&nbsp;Descriptor&nbsp;を表現するクラス。<br/>


<dl>

<dt>フィールド一覧</dt>
<dd>
<table border="1">
<tr><td>メンバ</td><td>型</td><td>サイズ</td></tr>
<tr><td><b>ExtentLen</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>ExtentLoc</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
</table>
</dd>
</dl>
*/
public class UDF_extent_ad extends UDF_Element 
{

    /**
	このオブジェクトのパッケージ名を除いたクラス名を取得する。

	@return クラス名
    */
    public static String getUDFClassName( ) {
        return "UDF_extent_ad";
    }

    /**
	コンストラクタ。

	@param elem   親エレメント。
	@param prefix ネームスペース。
	@param name   エレメント名。
    */
    public UDF_extent_ad(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }
    /**
	このUDFエレメントのサイズを返す。

	@return UDFエレメントのサイズ。
    */
    public int getSize(){
	return 0+4+4;
    }

    /**
	このUDFエレメントのサイズをlong 型で返す。

	@return UDFエレメントのサイズ。
    */
    public long getLongSize(){
	return (long)0+4+4;
    }
    private UDF_uint32 extentLen;
    private UDF_uint32 extentLoc;

    /**
	extentLenを取得する。

	@return 取得したextentLen を返す。
    */
    public UDF_uint32 getExtentLen(){return extentLen;}
    /**
	extentLocを取得する。

	@return 取得したextentLoc を返す。
    */
    public UDF_uint32 getExtentLoc(){return extentLoc;}

    /**
	extentLenを設定する。

	@param	v 設定する値。
    */
    public void setExtentLen(UDF_uint32 v){replaceChild(v, extentLen); extentLen = v;}
    /**
	extentLocを設定する。

	@param	v 設定する値。
    */
    public void setExtentLoc(UDF_uint32 v){replaceChild(v, extentLoc); extentLoc = v;}

    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long rsize = 0;
	preReadHook(f);
	extentLen = (UDF_uint32)createElement("UDF_uint32", "", "extent-len");
	rsize += extentLen.readFrom(f);
	extentLoc = (UDF_uint32)createElement("UDF_uint32", "", "extent-loc");
	rsize += extentLoc.readFrom(f);
	apply( );
	postReadHook(f);
	return rsize;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long wsize = 0;
	wsize += extentLen.writeTo(f);
	wsize += extentLoc.writeTo(f);
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
		extentLen = (UDF_uint32)createElement("UDF_uint32", "", "extent-len");
		extentLen.readFromXML(child);
	    }
	    else if(name.equals("extent-loc")){
		extentLoc = (UDF_uint32)createElement("UDF_uint32", "", "extent-loc");
		extentLoc.readFromXML(child);
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
	extentLen = (UDF_uint32)createElement("UDF_uint32", "", "extent-len");
	extentLen.setDefaultValue();
	extentLoc = (UDF_uint32)createElement("UDF_uint32", "", "extent-loc");
	extentLoc.setDefaultValue();

	apply( );
	postSetDefaultValueHook();
    }


    /**
	複製する
    */
    public UDF_Element duplicateElement(){
	UDF_extent_ad dup = new UDF_extent_ad(this, null, getName());
	dup.setDefaultValue();
	dup.duplicateHook(this);
	dup.setExtentLen((UDF_uint32)extentLen.duplicateElement());
	dup.setExtentLoc((UDF_uint32)extentLoc.duplicateElement());

	apply();

	return dup;
    }

    /**
        子の UDF Element の Node を append しなおす。

    */
    private void apply( ) {

	removeAllChildren();
	appendChild(extentLen);
	appendChild(extentLoc);
    }

    public String getInfo(int indent){
	String a = "";
	a += UDF_Util.repeat(' ', indent);
	a += extentLen.getInfo(indent + 1);
	a += extentLoc.getInfo(indent + 1);
      return a;
    }
    public void debug(int indent){
	System.err.print(UDF_Util.repeat(' ', indent));

	String info = " gp=" + getGlobalPoint();
	info += " pn=" + getElemPartRefNo();
	info += " sn=" + getPartSubno();
	System.err.println(getName() + ": (" + hashCode() + ")" + info);	extentLen.debug(indent + 1);
	extentLoc.debug(indent + 1);
    }
//begin:add your code here

    public UDF_Extent genExtent(){
	//UDF_Extentをつくる。
	UDF_Extent ext = (UDF_Extent)createElement("UDF_Extent", null, null);
	ext.addExtent(getExtentLoc().getIntValue(), -1, 
		      getExtentLen().getIntValue(), 0);
	return ext;
    }
    public UDF_ErrorList verify(){
	
	UDF_ErrorList el = new UDF_ErrorList();
	final short category = UDF_Error.C_ECMA167;
	long extlen = extentLen.getLongValue();
	
	
	// extentLen が2の30乗以上である
	if((1 << 30) <= extlen){
	    
	    el.addError(new UDF_Error
		   (category, UDF_Error.L_ERROR, "Extent Length",
		    "The length shall be less than 2^30.",
		    "3/7.1.1"));
	}
	
	// 特に何かない限り、extentLen は論理ブロック倍でなければならない
	if((extlen % UDF_Env.LBS) != 0){
	    
	    el.addError(new UDF_Error
		   (category, UDF_Error.L_WARNING, "Extent Length",
		    "Unless otherwise specified, the length shall be an integral multiple of the logical sector size.",
		    "3/7.1.1"));
	}
	
	// extentLen が0 のとき、extentLoc も0 でなければならない
	if(extlen == 0 && extentLoc.getLongValue() != 0){
	    
	    el.addError(new UDF_Error
		   (category, UDF_Error.L_ERROR, "Extent Location",
		    "If the extent's length is 0, no extent is specified and this field shall contain 0.",
		    "3/7.1.2"));
	}
	
	el.setGlobalPoint(getGlobalPoint());
	return el;
    }
    
    /**
       Extent Length の値を検証する。
       エラーには、L_ERROR レベル、原因、記録値が設定される。
       
       @param len この値とExtent Lengthが等しいかを検証する。
       @return エラーインスタンス。
    */
    public UDF_Error verifyExtLen(long len){
	
	if(extentLen.getLongValue() != len)
	    return new UDF_Error(UDF_Error.C_ECMA167, UDF_Error.L_ERROR, "Extent Length",
				 "", "", String.valueOf(extentLen.getLongValue()), String.valueOf(len));
	else
	    return new UDF_Error();
    }
    
    /**
       Extent Location の値を検証する。
       エラーには、L_ERROR レベル、原因、記録値が設定される。
       
       @param loc この値とExtent Locationが等しいかを検証する。
       @return エラーインスタンス。
    */
    public UDF_Error verifyExtLoc(long loc){
	
	if(extentLoc.getLongValue() != loc)
	    return new UDF_Error(UDF_Error.C_ECMA167, UDF_Error.L_ERROR, "Extent Location",
				 "", "", String.valueOf(extentLoc.getLongValue()), String.valueOf(loc));
	else
	    return new UDF_Error();
    }

    public void setRefAttribute(String ref){
	getExtentLoc().setAttribute("ref", ref + ".sec");
	getExtentLen().setAttribute("ref", ref + ".len");
    }
    
//end:
};
