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
<tr><td><b>DomainFlags</b></td><td><b>UDF_uint8</b></td><td><i>1</i></td></tr>
<tr><td><b>Reserved</b></td><td><b>UDF_bytes</b></td><td><i>5</i></td></tr>
</table>
</dd>
</dl>
*/
public class UDF_regid_UDFDomainIdSuffix extends UDF_Element 
{

    /**
	このオブジェクトのパッケージ名を除いたクラス名を取得する。

	@return クラス名
    */
    public static String getUDFClassName( ) {
        return "UDF_regid_UDFDomainIdSuffix";
    }

    /**
	コンストラクタ。

	@param elem   親エレメント。
	@param prefix ネームスペース。
	@param name   エレメント名。
    */
    public UDF_regid_UDFDomainIdSuffix(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }
    /**
	このUDFエレメントのサイズを返す。

	@return UDFエレメントのサイズ。
    */
    public int getSize(){
	return 0+2+1+reserved.getSize();
    }

    /**
	このUDFエレメントのサイズをlong 型で返す。

	@return UDFエレメントのサイズ。
    */
    public long getLongSize(){
	return (long)0+2+1+reserved.getSize();
    }
    private UDF_uint16 uDFRevision;
    private UDF_uint8 domainFlags;
    private UDF_bytes reserved;

    /**
	uDFRevisionを取得する。

	@return 取得したuDFRevision を返す。
    */
    public UDF_uint16 getUDFRevision(){return uDFRevision;}
    /**
	domainFlagsを取得する。

	@return 取得したdomainFlags を返す。
    */
    public UDF_uint8 getDomainFlags(){return domainFlags;}
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
	domainFlagsを設定する。

	@param	v 設定する値。
    */
    public void setDomainFlags(UDF_uint8 v){replaceChild(v, domainFlags); domainFlags = v;}
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
	domainFlags = (UDF_uint8)createElement("UDF_uint8", "", "domain-flags");
	rsize += domainFlags.readFrom(f);
	reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
	reserved.setSize(5);
	rsize += reserved.readFrom(f);
	apply( );
	postReadHook(f);
	return rsize;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long wsize = 0;
	wsize += uDFRevision.writeTo(f);
	wsize += domainFlags.writeTo(f);
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
	    else if(name.equals("domain-flags")){
		domainFlags = (UDF_uint8)createElement("UDF_uint8", "", "domain-flags");
		domainFlags.readFromXML(child);
	    }
	    else if(name.equals("reserved")){
		reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
		reserved.setSize(5);
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
	domainFlags = (UDF_uint8)createElement("UDF_uint8", "", "domain-flags");
	domainFlags.setDefaultValue();
	reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
	reserved.setSize(5);
	reserved.setDefaultValue();

	apply( );
	postSetDefaultValueHook();
    }


    /**
	複製する
    */
    public UDF_Element duplicateElement(){
	UDF_regid_UDFDomainIdSuffix dup = new UDF_regid_UDFDomainIdSuffix(this, null, getName());
	dup.setDefaultValue();
	dup.duplicateHook(this);
	dup.setUDFRevision((UDF_uint16)uDFRevision.duplicateElement());
	dup.setDomainFlags((UDF_uint8)domainFlags.duplicateElement());
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
	appendChild(domainFlags);
	appendChild(reserved);
    }

    public void debug(int indent){
	System.err.print(UDF_Util.repeat(' ', indent));
	System.err.println(getName() + ": (" + hashCode() + ")");	uDFRevision.debug(indent + 1);
	domainFlags.debug(indent + 1);
	reserved.debug(indent + 1);
    }
//begin:add your code here
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	if(env.udf_revision != 0x102)
	    return el;
	
	// リビジョンチェック
	if(uDFRevision.getIntValue() != 0x102){
	    
	    el.addError(new UDF_Error
			(UDF_Error.C_UDF102, UDF_Error.L_ERROR, "UDF Revision",
			 "The UDFRevision field shall contain #0102 to indicate revision 1.02.",
			 "2.1.5.3", String.valueOf(uDFRevision.getIntValue()), "258"));
	}
	
	el.addError(verifyBase(UDF_Error.C_UDF102, "2.1.5.3"));
	el.setGlobalPoint(getGlobalPoint());
	return el;
    }
    
    /**
       基礎的な検証を行います。
       この検証は他のリビジョンでも用いられます。
       
       @param  category  エラーカテゴリを指定できます。
       @return エラーインスタンス。
    */
    protected UDF_ErrorList verifyBase(short category, String refer){
	
	UDF_ErrorList el = new UDF_ErrorList();
	final String basemsg = "In regard to OSTA Domain Entity Identifiers specified in this document(appendix 6.1) " +
	    "the IdentifierSuffix field shall be constructed asl follows:\nReserved  bytes(=#00)";
	
	
	// Domain Flags の2-7 ビットは0
	if((domainFlags.getIntValue() & 0xfc) != 0){
	    
	    el.addError(new UDF_Error(category, UDF_Error.L_ERROR, "Domain Flags", basemsg, refer));
	}
	
	// Reserved は0
	if(!UDF_Util.isAllZero(reserved.getData())){
	    
	    el.addError(new UDF_Error(category, UDF_Error.L_ERROR, "Reserved", basemsg, refer));
	}
	
	el.setGlobalPoint(getGlobalPoint());
	return el;
    }
    
//end:
};
