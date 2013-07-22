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
<tr><td><b>Flags</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td></tr>
<tr><td><b>ImpUse</b></td><td><b>UDF_bytes</b></td><td><i>4</i></td></tr>
</table>
</dd>
</dl>
*/
public class UDF_long_ad_ADImpUse extends UDF_Element 
{

    /**
	このオブジェクトのパッケージ名を除いたクラス名を取得する。

	@return クラス名
    */
    public static String getUDFClassName( ) {
        return "UDF_long_ad_ADImpUse";
    }

    /**
	コンストラクタ。

	@param elem   親エレメント。
	@param prefix ネームスペース。
	@param name   エレメント名。
    */
    public UDF_long_ad_ADImpUse(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }
    /**
	このUDFエレメントのサイズを返す。

	@return UDFエレメントのサイズ。
    */
    public int getSize(){
	return 0+2+impUse.getSize();
    }

    /**
	このUDFエレメントのサイズをlong 型で返す。

	@return UDFエレメントのサイズ。
    */
    public long getLongSize(){
	return (long)0+2+impUse.getSize();
    }
    private UDF_uint16 flags;
    private UDF_bytes impUse;

    /**
	flagsを取得する。

	@return 取得したflags を返す。
    */
    public UDF_uint16 getFlags(){return flags;}
    /**
	impUseを取得する。

	@return 取得したimpUse を返す。
    */
    public UDF_bytes getImpUse(){return impUse;}

    /**
	flagsを設定する。

	@param	v 設定する値。
    */
    public void setFlags(UDF_uint16 v){replaceChild(v, flags); flags = v;}
    /**
	impUseを設定する。

	@param	v 設定する値。
    */
    public void setImpUse(UDF_bytes v){replaceChild(v, impUse); impUse = v;}

    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long rsize = 0;
	preReadHook(f);
	flags = (UDF_uint16)createElement("UDF_uint16", "", "flags");
	rsize += flags.readFrom(f);
	impUse = (UDF_bytes)createElement("UDF_bytes", "", "imp-use");
	impUse.setSize(4);
	rsize += impUse.readFrom(f);
	apply( );
	postReadHook(f);
	return rsize;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long wsize = 0;
	wsize += flags.writeTo(f);
	wsize += impUse.writeTo(f);
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
	    else if(name.equals("flags")){
		flags = (UDF_uint16)createElement("UDF_uint16", "", "flags");
		flags.readFromXML(child);
	    }
	    else if(name.equals("imp-use")){
		impUse = (UDF_bytes)createElement("UDF_bytes", "", "imp-use");
		impUse.setSize(4);
		impUse.readFromXML(child);
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
	flags = (UDF_uint16)createElement("UDF_uint16", "", "flags");
	flags.setDefaultValue();
	impUse = (UDF_bytes)createElement("UDF_bytes", "", "imp-use");
	impUse.setSize(4);
	impUse.setDefaultValue();

	apply( );
	postSetDefaultValueHook();
    }


    /**
	複製する
    */
    public UDF_Element duplicateElement(){
	UDF_long_ad_ADImpUse dup = new UDF_long_ad_ADImpUse(this, null, getName());
	dup.setDefaultValue();
	dup.duplicateHook(this);
	dup.setFlags((UDF_uint16)flags.duplicateElement());
	dup.setImpUse((UDF_bytes)impUse.duplicateElement());

	apply();

	return dup;
    }

    /**
        子の UDF Element の Node を append しなおす。

    */
    private void apply( ) {

	removeAllChildren();
	appendChild(flags);
	appendChild(impUse);
    }

    public void debug(int indent){
	System.err.print(UDF_Util.repeat(' ', indent));
	System.err.println(getName() + ": (" + hashCode() + ")");	flags.debug(indent + 1);
	impUse.debug(indent + 1);
    }
//begin:add your code here
    public UDF_ErrorList verify() throws UDF_Exception{
	return verifyBase(UDF_Error.C_UDF102);
    }
    public UDF_ErrorList verifyBase(short category) throws UDF_Exception{
        
        UDF_ErrorList el = new UDF_ErrorList();
        
        //　フラグの予約ビットに数値が入っていたら警告しましょう　//
        if (0 != (0xfffffffe & flags.getIntValue())) {

            el.addError(
                new UDF_Error(
			      category, UDF_Error.L_WARNING, "Flags",
			      "ADImpUse Flags (NOTE: bits 1-15 reserved for future use by UDF)",
			      "2.3.10.1"
                )
            );
        }
       
        el.setRName("ADImpUse");
        el.setGlobalPoint(getGlobalPoint());
        
        return el;
    }
//end:
};
