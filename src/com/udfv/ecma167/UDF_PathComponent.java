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
no documents.(AUTOMATICALY GENERATED)

<dl>

<dt>フィールド一覧</dt>
<dd>
<table border="1">
<tr><td>メンバ</td><td>型</td><td>サイズ</td></tr>
<tr><td><b>ComponentType</b></td><td><b>UDF_uint8</b></td><td><i>1</i></td></tr>
<tr><td><b>LenOfComponentId</b></td><td><b>UDF_uint8</b></td><td><i>1</i></td></tr>
<tr><td><b>ComponentFileVersionNumber</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td></tr>
<tr><td><b>ComponentId</b></td><td><b>UDF_bytes</b></td><td><i>getLenOfComponentId().getIntValue()</i></td></tr>
</table>
</dd>
</dl>
*/
public class UDF_PathComponent extends UDF_Element 
{

    /**
	このオブジェクトのパッケージ名を除いたクラス名を取得する。

	@return クラス名
    */
    public static String getUDFClassName( ) {
        return "UDF_PathComponent";
    }

    /**
	コンストラクタ。

	@param elem   親エレメント。
	@param prefix ネームスペース。
	@param name   エレメント名。
    */
    public UDF_PathComponent(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }
    /**
	このUDFエレメントのサイズを返す。

	@return UDFエレメントのサイズ。
    */
    public int getSize(){
	return 0+1+1+2+componentId.getSize();
    }

    /**
	このUDFエレメントのサイズをlong 型で返す。

	@return UDFエレメントのサイズ。
    */
    public long getLongSize(){
	return (long)0+1+1+2+componentId.getSize();
    }
    private UDF_uint8 componentType;
    private UDF_uint8 lenOfComponentId;
    private UDF_uint16 componentFileVersionNumber;
    private UDF_bytes componentId;

    /**
	componentTypeを取得する。

	@return 取得したcomponentType を返す。
    */
    public UDF_uint8 getComponentType(){return componentType;}
    /**
	lenOfComponentIdを取得する。

	@return 取得したlenOfComponentId を返す。
    */
    public UDF_uint8 getLenOfComponentId(){return lenOfComponentId;}
    /**
	componentFileVersionNumberを取得する。

	@return 取得したcomponentFileVersionNumber を返す。
    */
    public UDF_uint16 getComponentFileVersionNumber(){return componentFileVersionNumber;}
    /**
	componentIdを取得する。

	@return 取得したcomponentId を返す。
    */
    public UDF_bytes getComponentId(){return componentId;}

    /**
	componentTypeを設定する。

	@param	v 設定する値。
    */
    public void setComponentType(UDF_uint8 v){replaceChild(v, componentType); componentType = v;}
    /**
	lenOfComponentIdを設定する。

	@param	v 設定する値。
    */
    public void setLenOfComponentId(UDF_uint8 v){replaceChild(v, lenOfComponentId); lenOfComponentId = v;}
    /**
	componentFileVersionNumberを設定する。

	@param	v 設定する値。
    */
    public void setComponentFileVersionNumber(UDF_uint16 v){replaceChild(v, componentFileVersionNumber); componentFileVersionNumber = v;}
    /**
	componentIdを設定する。

	@param	v 設定する値。
    */
    public void setComponentId(UDF_bytes v){replaceChild(v, componentId); componentId = v;}

    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long rsize = 0;
	preReadHook(f);
	componentType = (UDF_uint8)createElement("UDF_uint8", "", "component-type");
	rsize += componentType.readFrom(f);
	lenOfComponentId = (UDF_uint8)createElement("UDF_uint8", "", "len-of-component-id");
	rsize += lenOfComponentId.readFrom(f);
	componentFileVersionNumber = (UDF_uint16)createElement("UDF_uint16", "", "component-file-version-number");
	rsize += componentFileVersionNumber.readFrom(f);
	componentId = (UDF_bytes)createElement("UDF_bytes", "", "component-id");
	componentId.setSize(getLenOfComponentId().getIntValue());
	rsize += componentId.readFrom(f);
	apply( );
	postReadHook(f);
	return rsize;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long wsize = 0;
	wsize += componentType.writeTo(f);
	wsize += lenOfComponentId.writeTo(f);
	wsize += componentFileVersionNumber.writeTo(f);
	wsize += componentId.writeTo(f);
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
	    else if(name.equals("component-type")){
		componentType = (UDF_uint8)createElement("UDF_uint8", "", "component-type");
		componentType.readFromXML(child);
	    }
	    else if(name.equals("len-of-component-id")){
		lenOfComponentId = (UDF_uint8)createElement("UDF_uint8", "", "len-of-component-id");
		lenOfComponentId.readFromXML(child);
	    }
	    else if(name.equals("component-file-version-number")){
		componentFileVersionNumber = (UDF_uint16)createElement("UDF_uint16", "", "component-file-version-number");
		componentFileVersionNumber.readFromXML(child);
	    }
	    else if(name.equals("component-id")){
		componentId = (UDF_bytes)createElement("UDF_bytes", "", "component-id");
		componentId.setSize(getLenOfComponentId().getIntValue());
		componentId.readFromXML(child);
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
	componentType = (UDF_uint8)createElement("UDF_uint8", "", "component-type");
	componentType.setDefaultValue();
	lenOfComponentId = (UDF_uint8)createElement("UDF_uint8", "", "len-of-component-id");
	lenOfComponentId.setDefaultValue();
	componentFileVersionNumber = (UDF_uint16)createElement("UDF_uint16", "", "component-file-version-number");
	componentFileVersionNumber.setDefaultValue();
	componentId = (UDF_bytes)createElement("UDF_bytes", "", "component-id");
	componentId.setSize(getLenOfComponentId().getIntValue());
	componentId.setDefaultValue();

	apply( );
	postSetDefaultValueHook();
    }


    /**
	複製する
    */
    public UDF_Element duplicateElement(){
	UDF_PathComponent dup = new UDF_PathComponent(this, null, getName());
	dup.setDefaultValue();
	dup.duplicateHook(this);
	dup.setComponentType((UDF_uint8)componentType.duplicateElement());
	dup.setLenOfComponentId((UDF_uint8)lenOfComponentId.duplicateElement());
	dup.setComponentFileVersionNumber((UDF_uint16)componentFileVersionNumber.duplicateElement());
	dup.setComponentId((UDF_bytes)componentId.duplicateElement());

	apply();

	return dup;
    }

    /**
        子の UDF Element の Node を append しなおす。

    */
    private void apply( ) {

	removeAllChildren();
	appendChild(componentType);
	appendChild(lenOfComponentId);
	appendChild(componentFileVersionNumber);
	appendChild(componentId);
    }

    public String getInfo(int indent){
	String a = "";
	a += UDF_Util.repeat(' ', indent);
	a += componentType.getInfo(indent + 1);
	a += lenOfComponentId.getInfo(indent + 1);
	a += componentFileVersionNumber.getInfo(indent + 1);
	a += componentId.getInfo(indent + 1);
      return a;
    }
    public void debug(int indent){
	System.err.print(UDF_Util.repeat(' ', indent));

	String info = " gp=" + getGlobalPoint();
	info += " pn=" + getElemPartRefNo();
	info += " sn=" + getPartSubno();
	System.err.println(getName() + ": (" + hashCode() + ")" + info);	componentType.debug(indent + 1);
	lenOfComponentId.debug(indent + 1);
	componentFileVersionNumber.debug(indent + 1);
	componentId.debug(indent + 1);
    }
//begin:add your code here
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	final short category = UDF_Error.C_ECMA167;
	
	
	// Component Type の0または6-255は予約
	int comtype = componentType.getIntValue();
	if(comtype == 0 || 5 < comtype){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Component Type",
			 "This field shall specify the component type as shown in figure 4/47.\n" + 
			 "0, 6-255: Reserved for future standardisation.",
			 "4/14.16.1.1", String.valueOf(comtype), ""));
	}
	
	// Component Type が5のとき、L_CI は0より大きい
	int lenofcomid = lenOfComponentId.getIntValue();
	if(lenofcomid == 0){
	    
	    if(comtype == 5){
		
		el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Length of Component Identifier(=L_CI)",
			 "If the Component Type field contains 5, L_CI shall be greater than 0.",
			 "4/14.16.1.2", "0", ""));
	    }
	}
	else if(comtype == 1){
	    
	    // Component Type が1のとき、Length of CompId が0でなければ、
	    // 要使用者間の取り決め
	    el.addError(new UDF_Error
			(category, UDF_Error.L_CAUTION, "Component Type",
			 "If L_CI is not 0, the component specifies the root of a directory hierarchy subject to " +
			 "agreement between the originator and recipient of the medium.",
			 "4/14.16.1.1", "1", ""));
	}
	else if(comtype != 5){
	    
	    // Component Type が1 でも5 でもないとき、
	    // Length of CompId は0 でなければならない
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Length of Component Identifier(=L_CI)",
			 "If the Component Type field does not contain 1 or 5, this field shall contain 0.",
			 "4/14.16.1.2", String.valueOf(lenofcomid), "0"));
	}
	
	// Component File Version Number の32768-65535 は予約
	int filevernum = componentFileVersionNumber.getIntValue();
	if(32768 <= filevernum){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Component File Version Number",
			 "The numbers 32768 to 65535 inclusive are reserved for future standardisation.",
			 "4/14.16.1.3", String.valueOf(filevernum), ""));
	}
	
	// コンポーネントがディレクトリのとき、Component File Version Number は0
	if(comtype == 3 || comtype == 4){
	    
	    if(filevernum != 0){
		
		el.addError(new UDF_Error
			    (category, UDF_Error.L_ERROR, "Component File Version Number",
			     "If the entity identified by the Component Identifier field(see 4/8.7) is a directory, " +
			     "then the value of this field shall be 0.",
			     "4/14.16.1.3", String.valueOf(filevernum), "0"));
	    }
	}
	
	el.setGlobalPoint(getGlobalPoint());
	el.setRName("Path Component");
	return el;
    }
//end:
};
