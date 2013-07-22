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
import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.encoding.*;
import com.udfv.access.*;
import com.udfv.exception.*;
import com.udfv.ecma167.*;

/**
Alternate&nbsp;Permissions&nbsp;を表現するクラス。<br/>


<dl>

<dt>フィールド一覧</dt>
<dd>
<table border="1">
<tr><td>メンバ</td><td>型</td><td>サイズ</td></tr>
<tr><td><b>AttrType</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>AttrSubtype</b></td><td><b>UDF_uint8</b></td><td><i>1</i></td></tr>
<tr><td><b>Reserved</b></td><td><b>UDF_bytes</b></td><td><i>3</i></td></tr>
<tr><td><b>AttrLen</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>OwnerIdentification</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td></tr>
<tr><td><b>GroupIdentification</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td></tr>
<tr><td><b>Permission</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td></tr>
</table>
</dd>
</dl>
*/
public class UDF_attr3 extends UDF_attr 
{

    /**
	このオブジェクトのパッケージ名を除いたクラス名を取得する。

	@return クラス名
    */
    public static String getUDFClassName( ) {
        return "UDF_attr3";
    }

    /**
	コンストラクタ。

	@param elem   親エレメント。
	@param prefix ネームスペース。
	@param name   エレメント名。
    */
    public UDF_attr3(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }
    /**
	このUDFエレメントのサイズを返す。

	@return UDFエレメントのサイズ。
    */
    public int getSize(){
	return 0+4+1+reserved.getSize()+4+2+2+2;
    }

    /**
	このUDFエレメントのサイズをlong 型で返す。

	@return UDFエレメントのサイズ。
    */
    public long getLongSize(){
	return (long)0+4+1+reserved.getSize()+4+2+2+2;
    }
    private UDF_uint32 attrType;
    private UDF_uint8 attrSubtype;
    private UDF_bytes reserved;
    private UDF_uint32 attrLen;
    private UDF_uint16 ownerIdentification;
    private UDF_uint16 groupIdentification;
    private UDF_uint16 permission;

    /**
	attrTypeを取得する。

	@return 取得したattrType を返す。
    */
    public UDF_uint32 getAttrType(){return attrType;}
    /**
	attrSubtypeを取得する。

	@return 取得したattrSubtype を返す。
    */
    public UDF_uint8 getAttrSubtype(){return attrSubtype;}
    /**
	reservedを取得する。

	@return 取得したreserved を返す。
    */
    public UDF_bytes getReserved(){return reserved;}
    /**
	attrLenを取得する。

	@return 取得したattrLen を返す。
    */
    public UDF_uint32 getAttrLen(){return attrLen;}
    /**
	ownerIdentificationを取得する。

	@return 取得したownerIdentification を返す。
    */
    public UDF_uint16 getOwnerIdentification(){return ownerIdentification;}
    /**
	groupIdentificationを取得する。

	@return 取得したgroupIdentification を返す。
    */
    public UDF_uint16 getGroupIdentification(){return groupIdentification;}
    /**
	permissionを取得する。

	@return 取得したpermission を返す。
    */
    public UDF_uint16 getPermission(){return permission;}

    /**
	attrTypeを設定する。

	@param	v 設定する値。
    */
    public void setAttrType(UDF_uint32 v){replaceChild(v, attrType); attrType = v;}
    /**
	attrSubtypeを設定する。

	@param	v 設定する値。
    */
    public void setAttrSubtype(UDF_uint8 v){replaceChild(v, attrSubtype); attrSubtype = v;}
    /**
	reservedを設定する。

	@param	v 設定する値。
    */
    public void setReserved(UDF_bytes v){replaceChild(v, reserved); reserved = v;}
    /**
	attrLenを設定する。

	@param	v 設定する値。
    */
    public void setAttrLen(UDF_uint32 v){replaceChild(v, attrLen); attrLen = v;}
    /**
	ownerIdentificationを設定する。

	@param	v 設定する値。
    */
    public void setOwnerIdentification(UDF_uint16 v){replaceChild(v, ownerIdentification); ownerIdentification = v;}
    /**
	groupIdentificationを設定する。

	@param	v 設定する値。
    */
    public void setGroupIdentification(UDF_uint16 v){replaceChild(v, groupIdentification); groupIdentification = v;}
    /**
	permissionを設定する。

	@param	v 設定する値。
    */
    public void setPermission(UDF_uint16 v){replaceChild(v, permission); permission = v;}

    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long rsize = 0;
	preReadHook(f);
	attrType = (UDF_uint32)createElement("UDF_uint32", "", "attr-type");
	rsize += attrType.readFrom(f);
	attrSubtype = (UDF_uint8)createElement("UDF_uint8", "", "attr-subtype");
	rsize += attrSubtype.readFrom(f);
	reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
	reserved.setSize(3);
	rsize += reserved.readFrom(f);
	attrLen = (UDF_uint32)createElement("UDF_uint32", "", "attr-len");
	rsize += attrLen.readFrom(f);
	ownerIdentification = (UDF_uint16)createElement("UDF_uint16", "", "owner-identification");
	rsize += ownerIdentification.readFrom(f);
	groupIdentification = (UDF_uint16)createElement("UDF_uint16", "", "group-identification");
	rsize += groupIdentification.readFrom(f);
	permission = (UDF_uint16)createElement("UDF_uint16", "", "permission");
	rsize += permission.readFrom(f);
	apply( );
	postReadHook(f);
	return rsize;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long wsize = 0;
	wsize += attrType.writeTo(f);
	wsize += attrSubtype.writeTo(f);
	wsize += reserved.writeTo(f);
	wsize += attrLen.writeTo(f);
	wsize += ownerIdentification.writeTo(f);
	wsize += groupIdentification.writeTo(f);
	wsize += permission.writeTo(f);
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
	    else if(name.equals("attr-type")){
		attrType = (UDF_uint32)createElement("UDF_uint32", "", "attr-type");
		attrType.readFromXML(child);
	    }
	    else if(name.equals("attr-subtype")){
		attrSubtype = (UDF_uint8)createElement("UDF_uint8", "", "attr-subtype");
		attrSubtype.readFromXML(child);
	    }
	    else if(name.equals("reserved")){
		reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
		reserved.setSize(3);
		reserved.readFromXML(child);
	    }
	    else if(name.equals("attr-len")){
		attrLen = (UDF_uint32)createElement("UDF_uint32", "", "attr-len");
		attrLen.readFromXML(child);
	    }
	    else if(name.equals("owner-identification")){
		ownerIdentification = (UDF_uint16)createElement("UDF_uint16", "", "owner-identification");
		ownerIdentification.readFromXML(child);
	    }
	    else if(name.equals("group-identification")){
		groupIdentification = (UDF_uint16)createElement("UDF_uint16", "", "group-identification");
		groupIdentification.readFromXML(child);
	    }
	    else if(name.equals("permission")){
		permission = (UDF_uint16)createElement("UDF_uint16", "", "permission");
		permission.readFromXML(child);
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
	attrType = (UDF_uint32)createElement("UDF_uint32", "", "attr-type");
	attrType.setDefaultValue();
	attrType.setValue(3);
	attrSubtype = (UDF_uint8)createElement("UDF_uint8", "", "attr-subtype");
	attrSubtype.setDefaultValue();
	attrSubtype.setValue(1);
	reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
	reserved.setSize(3);
	reserved.setDefaultValue();
	attrLen = (UDF_uint32)createElement("UDF_uint32", "", "attr-len");
	attrLen.setDefaultValue();
	ownerIdentification = (UDF_uint16)createElement("UDF_uint16", "", "owner-identification");
	ownerIdentification.setDefaultValue();
	groupIdentification = (UDF_uint16)createElement("UDF_uint16", "", "group-identification");
	groupIdentification.setDefaultValue();
	permission = (UDF_uint16)createElement("UDF_uint16", "", "permission");
	permission.setDefaultValue();

	apply( );
	postSetDefaultValueHook();
    }


    /**
	複製する
    */
    public UDF_Element duplicateElement(){
	UDF_attr3 dup = new UDF_attr3(this, null, getName());
	dup.setDefaultValue();
	dup.duplicateHook(this);
	dup.setAttrType((UDF_uint32)attrType.duplicateElement());
	dup.setAttrSubtype((UDF_uint8)attrSubtype.duplicateElement());
	dup.setReserved((UDF_bytes)reserved.duplicateElement());
	dup.setAttrLen((UDF_uint32)attrLen.duplicateElement());
	dup.setOwnerIdentification((UDF_uint16)ownerIdentification.duplicateElement());
	dup.setGroupIdentification((UDF_uint16)groupIdentification.duplicateElement());
	dup.setPermission((UDF_uint16)permission.duplicateElement());

	apply();

	return dup;
    }

    /**
        子の UDF Element の Node を append しなおす。

    */
    private void apply( ) {

	removeAllChildren();
	appendChild(attrType);
	appendChild(attrSubtype);
	appendChild(reserved);
	appendChild(attrLen);
	appendChild(ownerIdentification);
	appendChild(groupIdentification);
	appendChild(permission);
    }

    public void debug(int indent){
	System.err.print(UDF_Util.repeat(' ', indent));
	System.err.println(getName() + ": (" + hashCode() + ")");	attrType.debug(indent + 1);
	attrSubtype.debug(indent + 1);
	reserved.debug(indent + 1);
	attrLen.debug(indent + 1);
	ownerIdentification.debug(indent + 1);
	groupIdentification.debug(indent + 1);
	permission.debug(indent + 1);
    }
//begin:add your code here
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	final short category = UDF_Error.C_ECMA167;
	
	
	el.addError(super.verify(3, 4));
	
	// Owner Id とGroup Id の一方が0 のとき、もう一方も0 でなければならない
	int ownerid = ownerIdentification.getIntValue();
	int groupid = groupIdentification.getIntValue();
	
	if(ownerid == 0){
	    
	    if(groupid != 0){
		
		el.addError(new UDF_Error
			    (category, UDF_Error.L_ERROR, "Group Identification",
			     "If the number in Owner Identification field is 0, this shall indicate that there is no owner identification specified for the file. " +
			     "In this case, the Group Identification field shall be set to 0.",
			     "4/14.10.4.6", String.valueOf(groupid), "0"));
	    }
	}
	else if(groupid == 0){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Owner Identification",
			 "If the number in Group Identification field is 0, this shall indicate that there is no owner identification specified for the file. " +
			 "In this case, the Owner Identification field shall be set to 0.",
			 "4/14.10.4.5", String.valueOf(ownerid), "0"));
	}
	
	el.setRName("Alternate Permissions Extended Attribute");
	return el;
    }
    
//end:
};
