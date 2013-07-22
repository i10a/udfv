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
Recorded&nbsp;address&nbsp;を表現するクラス。<br/>


<dl>

<dt>フィールド一覧</dt>
<dd>
<table border="1">
<tr><td>メンバ</td><td>型</td><td>サイズ</td></tr>
<tr><td><b>LogicalBlockNumber</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>PartReferenceNumber</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td></tr>
</table>
</dd>
</dl>
*/
public class UDF_lb_addr extends UDF_Element 
{

    /**
	このオブジェクトのパッケージ名を除いたクラス名を取得する。

	@return クラス名
    */
    public static String getUDFClassName( ) {
        return "UDF_lb_addr";
    }

    /**
	コンストラクタ。

	@param elem   親エレメント。
	@param prefix ネームスペース。
	@param name   エレメント名。
    */
    public UDF_lb_addr(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }
    /**
	このUDFエレメントのサイズを返す。

	@return UDFエレメントのサイズ。
    */
    public int getSize(){
	return 0+4+2;
    }

    /**
	このUDFエレメントのサイズをlong 型で返す。

	@return UDFエレメントのサイズ。
    */
    public long getLongSize(){
	return (long)0+4+2;
    }
    private UDF_uint32 logicalBlockNumber;
    private UDF_uint16 partReferenceNumber;

    /**
	logicalBlockNumberを取得する。

	@return 取得したlogicalBlockNumber を返す。
    */
    public UDF_uint32 getLogicalBlockNumber(){return logicalBlockNumber;}
    /**
	partReferenceNumberを取得する。

	@return 取得したpartReferenceNumber を返す。
    */
    public UDF_uint16 getPartReferenceNumber(){return partReferenceNumber;}

    /**
	logicalBlockNumberを設定する。

	@param	v 設定する値。
    */
    public void setLogicalBlockNumber(UDF_uint32 v){replaceChild(v, logicalBlockNumber); logicalBlockNumber = v;}
    /**
	partReferenceNumberを設定する。

	@param	v 設定する値。
    */
    public void setPartReferenceNumber(UDF_uint16 v){replaceChild(v, partReferenceNumber); partReferenceNumber = v;}

    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long rsize = 0;
	preReadHook(f);
	logicalBlockNumber = (UDF_uint32)createElement("UDF_uint32", "", "logical-block-number");
	rsize += logicalBlockNumber.readFrom(f);
	partReferenceNumber = (UDF_uint16)createElement("UDF_uint16", "", "part-reference-number");
	rsize += partReferenceNumber.readFrom(f);
	apply( );
	postReadHook(f);
	return rsize;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long wsize = 0;
	wsize += logicalBlockNumber.writeTo(f);
	wsize += partReferenceNumber.writeTo(f);
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
	    else if(name.equals("logical-block-number")){
		logicalBlockNumber = (UDF_uint32)createElement("UDF_uint32", "", "logical-block-number");
		logicalBlockNumber.readFromXML(child);
	    }
	    else if(name.equals("part-reference-number")){
		partReferenceNumber = (UDF_uint16)createElement("UDF_uint16", "", "part-reference-number");
		partReferenceNumber.readFromXML(child);
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
	logicalBlockNumber = (UDF_uint32)createElement("UDF_uint32", "", "logical-block-number");
	logicalBlockNumber.setDefaultValue();
	partReferenceNumber = (UDF_uint16)createElement("UDF_uint16", "", "part-reference-number");
	partReferenceNumber.setDefaultValue();

	apply( );
	postSetDefaultValueHook();
    }


    /**
	複製する
    */
    public UDF_Element duplicateElement(){
	UDF_lb_addr dup = new UDF_lb_addr(this, null, getName());
	dup.setDefaultValue();
	dup.duplicateHook(this);
	dup.setLogicalBlockNumber((UDF_uint32)logicalBlockNumber.duplicateElement());
	dup.setPartReferenceNumber((UDF_uint16)partReferenceNumber.duplicateElement());

	apply();

	return dup;
    }

    /**
        子の UDF Element の Node を append しなおす。

    */
    private void apply( ) {

	removeAllChildren();
	appendChild(logicalBlockNumber);
	appendChild(partReferenceNumber);
    }

    public String getInfo(int indent){
	String a = "";
	a += UDF_Util.repeat(' ', indent);
	a += logicalBlockNumber.getInfo(indent + 1);
	a += partReferenceNumber.getInfo(indent + 1);
      return a;
    }
    public void debug(int indent){
	System.err.print(UDF_Util.repeat(' ', indent));

	String info = " gp=" + getGlobalPoint();
	info += " pn=" + getElemPartRefNo();
	info += " sn=" + getPartSubno();
	System.err.println(getName() + ": (" + hashCode() + ")" + info);	logicalBlockNumber.debug(indent + 1);
	partReferenceNumber.debug(indent + 1);
    }
//begin:add your code here
//end:
};
