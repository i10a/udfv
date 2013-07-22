/*
*/
package com.udfv.udf102;

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
no documents.(AUTOMATICALY GENERATED)

<dl>

<dt>フィールド一覧</dt>
<dd>
<table border="1">
<tr><td>メンバ</td><td>型</td><td>サイズ</td></tr>
<tr><td><b>UDFRevision</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td></tr>
<tr><td><b>OSClass</b></td><td><b>UDF_uint8</b></td><td><i>1</i></td></tr>
<tr><td><b>OSId</b></td><td><b>UDF_uint8</b></td><td><i>1</i></td></tr>
<tr><td><b>Reserved</b></td><td><b>UDF_bytes</b></td><td><i>4</i></td></tr>
</table>
</dd>
</dl>
*/
public class UDF_regid_UDFIdSuffix extends UDF_Element 
{

    /**
	このオブジェクトのパッケージ名を除いたクラス名を取得する。

	@return クラス名
    */
    public static String getUDFClassName( ) {
        return "UDF_regid_UDFIdSuffix";
    }

    /**
	コンストラクタ。

	@param elem   親エレメント。
	@param prefix ネームスペース。
	@param name   エレメント名。
    */
    public UDF_regid_UDFIdSuffix(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }
    /**
	このUDFエレメントのサイズを返す。

	@return UDFエレメントのサイズ。
    */
    public int getSize(){
	return 0+2+1+1+reserved.getSize();
    }

    /**
	このUDFエレメントのサイズをlong 型で返す。

	@return UDFエレメントのサイズ。
    */
    public long getLongSize(){
	return (long)0+2+1+1+reserved.getSize();
    }
    private UDF_uint16 uDFRevision;
    private UDF_uint8 oSClass;
    private UDF_uint8 oSId;
    private UDF_bytes reserved;

    /**
	uDFRevisionを取得する。

	@return 取得したuDFRevision を返す。
    */
    public UDF_uint16 getUDFRevision(){return uDFRevision;}
    /**
	oSClassを取得する。

	@return 取得したoSClass を返す。
    */
    public UDF_uint8 getOSClass(){return oSClass;}
    /**
	oSIdを取得する。

	@return 取得したoSId を返す。
    */
    public UDF_uint8 getOSId(){return oSId;}
    /**
	reservedを取得する。

	@return 取得したreserved を返す。
    */
    public UDF_bytes getReserved(){return reserved;}

    /**
	uDFRevisionを設定する。

	@param	v 設定する値。
    */
    public void setUDFRevision(UDF_uint16 v){replaceChild(v, uDFRevision); uDFRevision = v;}
    /**
	oSClassを設定する。

	@param	v 設定する値。
    */
    public void setOSClass(UDF_uint8 v){replaceChild(v, oSClass); oSClass = v;}
    /**
	oSIdを設定する。

	@param	v 設定する値。
    */
    public void setOSId(UDF_uint8 v){replaceChild(v, oSId); oSId = v;}
    /**
	reservedを設定する。

	@param	v 設定する値。
    */
    public void setReserved(UDF_bytes v){replaceChild(v, reserved); reserved = v;}

    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long rsize = 0;
	preReadHook(f);
	uDFRevision = (UDF_uint16)createElement("UDF_uint16", "", "udf-revision");
	rsize += uDFRevision.readFrom(f);
	oSClass = (UDF_uint8)createElement("UDF_uint8", "", "os-class");
	rsize += oSClass.readFrom(f);
	oSId = (UDF_uint8)createElement("UDF_uint8", "", "os-id");
	rsize += oSId.readFrom(f);
	reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
	reserved.setSize(4);
	rsize += reserved.readFrom(f);
	apply( );
	postReadHook(f);
	return rsize;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long wsize = 0;
	wsize += uDFRevision.writeTo(f);
	wsize += oSClass.writeTo(f);
	wsize += oSId.writeTo(f);
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
	    else if(name.equals("udf-revision")){
		uDFRevision = (UDF_uint16)createElement("UDF_uint16", "", "udf-revision");
		uDFRevision.readFromXML(child);
	    }
	    else if(name.equals("os-class")){
		oSClass = (UDF_uint8)createElement("UDF_uint8", "", "os-class");
		oSClass.readFromXML(child);
	    }
	    else if(name.equals("os-id")){
		oSId = (UDF_uint8)createElement("UDF_uint8", "", "os-id");
		oSId.readFromXML(child);
	    }
	    else if(name.equals("reserved")){
		reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
		reserved.setSize(4);
		reserved.readFromXML(child);
	    }
	    else{
		System.err.println("Unknown XML tag:" + name);
	    }
	}
	apply( );
	postReadFromXMLHook(n);
    }

    /**
	初期値を設定する
    */
    public void setDefaultValue(){
	super.setDefaultValue();
	uDFRevision = (UDF_uint16)createElement("UDF_uint16", "", "udf-revision");
	uDFRevision.setDefaultValue();
	oSClass = (UDF_uint8)createElement("UDF_uint8", "", "os-class");
	oSClass.setDefaultValue();
	oSId = (UDF_uint8)createElement("UDF_uint8", "", "os-id");
	oSId.setDefaultValue();
	reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
	reserved.setSize(4);
	reserved.setDefaultValue();

	apply( );
	postSetDefaultValueHook();
    }


    /**
	複製する
    */
    public UDF_Element duplicateElement(){
	UDF_regid_UDFIdSuffix dup = new UDF_regid_UDFIdSuffix(this, null, getName());
	dup.setDefaultValue();
	dup.duplicateHook(this);
	dup.setUDFRevision((UDF_uint16)uDFRevision.duplicateElement());
	dup.setOSClass((UDF_uint8)oSClass.duplicateElement());
	dup.setOSId((UDF_uint8)oSId.duplicateElement());
	dup.setReserved((UDF_bytes)reserved.duplicateElement());

	apply();

	return dup;
    }

    /**
        子の UDF Element の Node を append しなおす。

    */
    private void apply( ) {

	removeAllChildren();
	appendChild(uDFRevision);
	appendChild(oSClass);
	appendChild(oSId);
	appendChild(reserved);
    }

    public void debug(int indent){
	System.err.print(UDF_Util.repeat(' ', indent));
	System.err.println(getName() + ": (" + hashCode() + ")");	uDFRevision.debug(indent + 1);
	oSClass.debug(indent + 1);
	oSId.debug(indent + 1);
	reserved.debug(indent + 1);
    }
//begin:add your code here
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	if(env.udf_revision != 0x102)
	    return el;
	
	
	final short category = UDF_Error.C_UDF102;
	
	// リビジョンチェック
	if(getUDFRevision().getIntValue() != 0x102){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "UDF Revision",
			 "The UDFRevision field shall contain #0102 to indicate revision 1.02.",
			 "2.1.5.3", String.valueOf(uDFRevision.getIntValue()), "258"));
	}
	
	// Reserved は0
	if(!UDF_Util.isAllZero(getReserved().getData())){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Reserved",
			 "Implementation use Entity Identifiers defined by UDF(appendix 6.1) " +
			 "the IdentifierSuffix field shall be constructed as follows.", "2.1.5.3"));
	}
	
	el.setGlobalPoint(getGlobalPoint());
	return el;
    }
    
//end:
};
