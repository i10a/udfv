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
package com.udfv.core;

import java.io.*;
import java.util.*;
import java.lang.reflect.*;
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
import com.udfv.access.*;
import com.udfv.exception.*;
import com.udfv.core.UDF_Data;
import com.udfv.util.*;
import com.udfv.ecma167.UDF_uint8;
import com.udfv.ecma167.UDF_uint16;
import com.udfv.ecma167.UDF_uint32;
import com.udfv.ecma167.UDF_uint32a;
import com.udfv.ecma167.UDF_uint64;
import com.udfv.ecma167.UDF_short_ad;
import com.udfv.ecma167.UDF_lb_addr;
import com.udfv.ecma167.UDF_bytes;
import com.udfv.ecma167.UDF_pad;
import com.udfv.ecma167.UDF_CrcDesc;
import javax.swing.JTextArea;
import javax.swing.JComponent;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;


/**
   全ての UDF Elementの基底になるクラスである。

   UDF Element は ECMA167、UDF1.02〜UDF2.60の中で規定されているデスクリプタである。
   UDFVでは内部的なデータ構造として各 UDF Elementは

   - XML Nodeと1対1に対応している。
   - 親および子を複数持ち、それぞれXML Nodeに対応している。

   これらの構造を実現するために、UDF_ElementBaseでは 

   - XML Nodeを操作するメソッド
   - 親子関係を操作するメソッド

   を提供している。

   UDF Elementは通常 newで生成しない。createElementを使用すること。
   理由は README参照。
 */
abstract public class UDF_ElementBase extends UDF_Base implements Comparable{
    //サイズのヒント
    int my_hint_size;
    //全イメージ中での自身の位置
    long my_gp;
    //PartitionMap中での自身の位置
    long my_partmapoff;
    //親Elementに対する自身の相対位置
    long my_reloff;
    //Extentの中での自身の先頭が属するパーティションリファレンス番号
    int my_partno;


    /**
       パーティション副番号。
    
       Virtual Partition Mapの場合	何代前の VATか。0が最新。-1が新規に作ったもの。
       Metadata Partition の場合	ミラーかどうか。0はメイン。1がミラー
    */
    short my_subno;

    /**
       コンストラクタ
    */
    public UDF_ElementBase(){
	my_children = new UDF_ElementList();
	my_subno = 0;
	my_gp = -1;
    }

    /**
       コンストラクタ。

       内部的な初期化および、<prefix:name>のXML Nodeを生成する。
       
       @param mother	母。
       @param prefix	ネームスペース。
       @param name	XML Node名。

       prefixを nullにするとネームスペースを用いない <name>形式の XML Nodeを生成する。
       nameを nullにするとクラス名のXML Nodeを生成する。

       XML Nodeを生成するには XML Documentが必要なので、
       第一引数にUDF_Elementを 指定し、そのUDF_Elementから XML Documentを取得する。
       
       この段階では XML Nodeは XML Documentに追加されない。また、内部的な親子関係も構成されていない。
       第一引数は正しいUDF_Elementなら何を指定しても構わない。
       
     */
    protected UDF_ElementBase(UDF_Element mother, String prefix, String name){
	my_children = new UDF_ElementList();
	//my_mirror = false;
	my_subno = 0;
	if(name == null)
	    name = getClass().getName();

	if(prefix != null && prefix.equals("ecma119"))
	    ;
	else
	    prefix = null;

	if(marimite && mother.getNode() != null){
	    if(prefix != null)
		my_node = (Element)(mother.getNode().getOwnerDocument().createElement(prefix + ":" + name));
	    else
		my_node = (Element)(mother.getNode().getOwnerDocument().createElement(name));
	}
	
	my_prefix = prefix;
	//XMLを使用しないときは名前を設定する
	if(!marimite){
	    if(prefix != null)
		my_name = prefix + ":"+ name;
	    else
		my_name = name;
	}

	setEnv(mother.env);
    }

    /**
       初期化関数

       コンストラクタの後に実行される。
     */
    protected void init(){}

    /**
      UDF 要素が記録されていた論理位置をＸＭＬ中に表記するか否か。
     */
    protected boolean hasGlobalPoint(){
	return false;
    }

    //デバッグ用
    protected boolean hasElemPartRefNo(){
	return false;
    }
    //デバッグ用
    protected boolean hasPartMapOffset(){
	return false;
    }
    //デバッグ用
    protected boolean hasOffset(){
	return false;
    }
    /**
      mirror Attributeを XMLに表記するか否か
     */
    protected boolean hasAttrPartSubno(){
	return false;
    }
    

    /**
       readFromする前にサイズがわからないといけないエレメントにサイズのヒントを設定する

       @param sz	サイズ(byte)
     */
    public void setHintSize(int sz){
	my_hint_size = sz;
    }
    /**
       readFromする前にサイズがわからないといけないエレメントのサイズのヒントを取得する

       @return サイズ(byte)
     */
    public int getHintSize(){
	return my_hint_size;
    }

    /**
       UDF Elementのパーティション番号を設定する。
       パーティション外にあるときは -1を指定すること

       @param partno	パーティション番号
     */
    public void setElemPartRefNo(int partno){
	my_partno = partno;
	if(marimite && hasElemPartRefNo()){
	    setAttribute("partrefno", Integer.toString(my_partno));
	}
    }

    /**
       UDF Elementのパーティション番号を取得する。
       パーティション外にあるときは -1が帰る。
      
     */
    public int getElemPartRefNo(){
	return my_partno;
    }

    /**
       Partition Map内でのオフセットを取得する。
     */
    public long getPartMapOffset(){
	return my_partmapoff;
    }
    /**
       Partition Map内でのオフセットを指定する。
     */
    public void setPartMapOffset(long offset){
	my_partmapoff = offset;
	if(marimite && hasPartMapOffset()){
	    setAttribute("partmap-offset", Long.toString(my_partmapoff));
	}
    }

    /**
       Extent 内でのオフセットを取得する。
     */
    public long getOffset(){
	return my_reloff;
    }
    /**
       Extent 内でのオフセットを指定する。
     */
    public void setOffset(long offset){
	my_reloff = offset;
	if(marimite && hasOffset()){
	    setAttribute("offset", Long.toString(my_reloff));
	}
    }

    /**
       @deprecated replaced by {@link getPartSubno()}
     */
    public boolean getMirror() {
	return my_subno != 0 ? true : false;
    }

    /**
       @deprecated replaced by {@link setPartSubno(int)}
     */
    public void setMirror(boolean flag) {
	/*
	my_subno = (short)(flag ? 1 : 0);
	if(marimite && hasAttrPartSubno()){
	    if(flag)
		setAttribute("mirror", "yes");
	    else
		removeAttribute("mirror");
	}
	*/
	setPartSubno(flag ? 1 : 0);
    }
    /**
       @deprecated replaced by {@link getPartSubno()}
     */
    public int getVATno(){
	return my_subno;
    }
    /**
       @deprecated replaced by {@link setPartSubno(int)}
     */
    public void setVATno(int vatno){
	my_subno = (short)vatno;
    }

    public int getPartSubno(){
	return my_subno;
    }
    public void setPartSubno(int subno){
	my_subno = (short)subno;
	if(marimite && hasAttrPartSubno() && my_partno >= 0){
	    if(subno != 0)
		setAttribute("subno", "" + subno);
	    else
		removeAttribute("subno");
	}
    }


    /*
       UDF ElementのUDF_Extent内でのLBN位置また SEC位置を取得する。
       Partition内ならば LBN位置、Partition外ならば SEC位置を返す。
     */
    public int getElemLoc(){
	return (int)(my_partmapoff / env.LBS);
    }

    /**
      このUDF 要素が記録されている位置を返します。

      @return 論理位置（単位バイト）。-1のときは記録されていない。
    */
    public long getGlobalPoint( ) {
	return my_gp;
    }
    
    /**
      このUDF 要素が記録されている位置を設定します。

      @param f イメージアクセサ。
    */
    public void setGlobalPoint(UDF_RandomAccess f) {
	try {
	    my_gp = f.getAbsPointer();
	    if(marimite){
		if(hasGlobalPoint()) {
		    setAttribute("gp", Long.toString(my_gp));
		}
	    }
	}
	catch (IOException e) {

	}
    }

    /**
      このUDF 要素が記録されている位置を設定します。

      @param f イメージアクセサ。
    */
    public void setGlobalPoint(long gp){
	my_gp = gp;
	if(marimite){
	    if(hasGlobalPoint()) {
		setAttribute("gp", Long.toString(my_gp));
	    }
	}
    }

    /**
       Elementを比較する。
     */
    public int compareTo(Object o){
	if(!getClass().isAssignableFrom(o.getClass())){
	    throw new ClassCastException(getClass().getName() + "!=" + o.getClass());
	}

	UDF_Element elem = (UDF_Element)o;
	
	byte[] byte1 = getBytes();
	byte[] byte2 = elem.getBytes();
	
	for(int i=0 ; i<byte1.length && i<byte2.length ; ++i)
	    if(byte1[i] != byte2[i])
		return UDF_Util.b2i(byte1[i]) - UDF_Util.b2i(byte2[i]);
	
	return byte1.length - byte2.length;
    }
    /**
       要素の内容をバイト配列に変換して返す。
    */
    public byte [] getBytes( )/* throws UDF_Exception*/ {

        int size = getSize();
        byte [] bin = new byte[size];
        UDF_RandomAccessBuffer rab = new UDF_RandomAccessBuffer(size);
        try {
            writeTo(rab);
            rab.seek(0);
            rab.read(bin);
            rab.close();
            rab = null;

            return bin;
        }
	catch(IOException e){
	    //ここにはこないはず;
	}
	catch(UDF_Exception e){
	    //ここにはこないはず;
	}
	/*
        catch (IOException e) {
            throw new UDF_InternalException((UDF_Element)this, e.toString());
        }
	*/
	return null;//ここにはこないはず
    }

    /**
       この UDF Element のバイナリデータを持つアクセサを作成する。

    */
    public UDF_RandomAccessBytes genRandomAccessBytes( ) {
	byte [] b = getBytes();
	return new UDF_RandomAccessBytes(env, b, getPartMapOffset(), getElemPartRefNo(), getMirror());
    }


    protected void setPkgPriority(int revision){
	generator.setPkgPriority(revision);
    }
    static private UDF_ElementGenerator generator = new UDF_ElementGeneratorImpl(0);

    private Element my_node;
    private UDF_ElementList my_children;
    private String my_prefix;
    private UDF_Element my_parent;

    protected final boolean marimite = UDF_XMLUtil.MARIMITE;
    protected final byte sa_ya = UDF_XMLUtil.SAYA;
    
    private String my_name;

    /**
       Reference属性を保存する。(marimiteが falseのときのみ) 
     */
    private String my_ref;

    /**
       UDFにおけるバイトサイズを取得する。ただしサイズが 2^31を越える場合は桁落ちがある可能性がある。
     */
    abstract public int getSize();
    /**
       UDFにおけるバイトサイズを取得する
     */
    abstract public long getLongSize();

    /**
       アクセサからバイトデータを読み、内部的な値を設定する。

       @param f	アクセサ

       @return 実際に読んだバイト数
     */
    abstract public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception;
    /**
       内部的な値をバイデータに変換し、アクセサに書き出す。

       @param f	アクセサ

       @return 実際に読んだバイト数
     */
    abstract public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception;
    /**
       XML Nodeからバイトデータを読み、内部的な値を設定する。

       @param n	ノード
     */
    abstract public void readFromXML(Node n) throws UDF_Exception;

    /**
       この UDF Elementの名前を取得する。
     */
    public String getName(){
	if(!marimite)
	    return my_name;
	else if(getNode() != null)
	    return getNode().getNodeName();
	return null;
    }
    /**
       この UDF Elementの prefix名を取得する。
     */
    public String getPrefix(){
	String name = null;
	if(!marimite)
	    name = my_name;
	else
	    name = getNode().getNodeName();

	if(name.indexOf(':') >=0)
	    return name.substring(0, name.indexOf(':'));
	return name;
    }
    /**
       この UDF Elementが保持する XML Nodeを取得する

       @return XML Node
     */
    public Element getNode(){
	return my_node;
    }
    /*
       この UDF Elementが保持する XML Nodeを設定する

       @param n XML Node

       このメソッドは一番上位の root XML Nodeを設定するときにのみ必用で、現在 UDF_Imageからのみ使用されている。

     */
    protected void setNode(Element n){
	my_node = n;
    }

    /**
       この UDF Elementの子 UDF Elementの最初のものを取得する。
     */
    public UDF_Element getFirstChild(){
	if(my_children.size() > 0)
	    return my_children.firstElement();
	else
	    return null;
    }

    /**
       この UDF Elementの子 UDF Elementを取得する

       @return 子 UDF Element

     */
    //@deprecated replaced by {@link getChildList()}
    public UDF_Element[] getChildren(){
	return my_children.toElemArray();
    }

    public UDF_ElementList getChildList(){
	return my_children;
    }

    public boolean hasChild(){
	return (my_children.size() > 0);
    }

    /**
       この UDF Elementの保持する XML Nodeが属する XML Documentを取得する。

       @return XML Document
     */
    public Document getDocument(){
	if(my_node == null)
	    return null;
	else
	    return my_node.getOwnerDocument();
    }

    /**
       この UDF_Elementの 親を設定する
       
       @param e 親
     */
    protected final void setParent(UDF_Element e){
	my_parent = e;
    }
    /**
       この UDF_Elementの 親を取得する
       
       @return 親 
     */
    public final UDF_Element getParent(){
	return my_parent;
    }

    /**
       この UDF_Elementが属する CrcDescを求める。
       
       @return 親 
    */
    public final UDF_CrcDesc getParentCrcDesc(){
	UDF_Element e = (UDF_Element)this;
	while(!UDF_CrcDesc.class.isAssignableFrom(e.getClass())){
	    e = e.getParent();
	    if(e == null)
		return null;
	}
	return (UDF_CrcDesc)e;
    }

    /**
       子供が追加されたり削除されたりした時に呼ばれるフック
     */
    public void changeChildHook(){
    }
    /**
       この UDF_Elementの子供を全て削除する
       
       このメソッドは同時にXMLの子Node も削除する
     */
    public void removeAllChildren(){
	my_children.removeAllElements();
	if(marimite && getNode() != null){
	    while(my_node.hasChildNodes())
		my_node.removeChild(my_node.getFirstChild());
	}
	changeChildHook();
    }

    /**
       UDF Elementを子供として追加する

       このメソッドは同時にXMLの子Node としても追加する
       また、親と同じパーティションを子に設定する
     */
    public void appendChild(UDF_Element e){
	my_children.add(e);
	if(marimite && getNode() != null)
	    my_node.appendChild(e.getNode());
	e.setParent((UDF_Element)this);
	setElemPartRefNo(getElemPartRefNo());
	changeChildHook();
    }
    /**
       UDF Elementを子供として追加する

       このメソッドは同時にXMLの子Node としても追加する
       また、親と同じパーティションを子に設定する
     */
    public void appendChildren(UDF_ElementList el){
	for(Iterator it=el.iterator() ; it.hasNext() ; ){
	    appendChild((UDF_Element)it.next());
	}
	changeChildHook();
    }

    /**
       UDF Elementを子供として追加する

       このメソッドは同時にXMLの子Node としても追加する
       また、親と同じパーティションを子に設定する
     */
    public void appendChildren(UDF_ADList el){
	for(Iterator it=el.iterator() ; it.hasNext() ; ){
	    appendChild((UDF_AD)it.next());
	}
	changeChildHook();
    }

    /**
       UDF Elementを子供として追加する

       このメソッドは同時にXMLの子Node としても追加する
       また、親と同じパーティションを子に設定する

     */
    public void appendChildren(UDF_ElementBase el[]){
	for(int i=0 ; i<el.length ; ++i){
	    appendChild((UDF_Element)el[i]);
	}
	changeChildHook();
    }

    /**
       指定したUDF Elementを削除する

       このメソッドは同時にXMLの子Node も削除する
     */
    public void removeChild(UDF_Element e){
	my_children.remove(e);
	//もうXML nodeが削除されている場合は無視して継続する
	if(marimite && getNode() != null && e.getNode().getParentNode() == getNode())
	    my_node.removeChild(e.getNode());
	changeChildHook();
    }

    /**
       指定した UDF Element を指定の UDF Element に置き換える。

       このメソッドは同時に XML の子 Node を置き換える。
    */
    public void replaceChild(UDF_Element neo, UDF_Element old) {

	if (old == null) {
	    appendChild(neo);
	    return;
	}

	int idx = my_children.indexOf(old);
	if (idx < 0) {
	    return;
	}
	my_children.set(idx, neo);

	if(marimite && getNode() != null){
	    my_node.replaceChild(neo.getNode(), old.getNode());
	}
	neo.setParent((UDF_Element)this);
	changeChildHook();
    }


    /**
       自分自身を複製する。

       ただし、id属性だけはそのまま複製しない。
       
       @return 複製したエレメント。
    */
    public UDF_Element duplicateElement(){
	UDF_Element[] children = getChildren();
	UDF_Element retelem = (UDF_Element)createElement(getClass().getName(), null, null);
	

	try{
	    UDF_RandomAccessBytes rab = genRandomAccessBytes();
	    
	    for(int i = 0; i < children.length; i++){
		
		UDF_Element child = (UDF_Element)children[i];
		child.writeTo(rab);
	    }
	    
	    rab.seek(0);
	    retelem.readFrom(rab);
	}
	catch(Exception e){
	    
	    e.printStackTrace();
	}

	retelem.duplicateHook((UDF_Element)this);
	return retelem;
    }

    /**
       <a>123</a>のように子供としてTextノードを追加する。
     */
    public void setNodeVal(String str){
	if(marimite && getNode() != null){
	    removeAllChildren();
	    Text t = getDocument().createTextNode(str);
	    getNode().appendChild(t);
	}
    }

    /**
       Name Space prefixを取得する。

       @param mode	動作モード
       @return prefix

       modeが trueの場合 prefixが nullの場合は空文字列「""」を返し、非nullの場合 ":"を付加して返す。
       modeが falseの場合はそのまま prefixを返す。 
     */
    final protected  String getPrefix(boolean mode){
	if(!mode)
	    return my_prefix;

	if(my_prefix == null)
	    return "";
	return my_prefix + ":";
    }

    /**
       この UDF Elementの XML Nodeに XML Attributeを設定する

       @param attr	XML Attribute名
       @param val	値
     */
    public void setAttribute(String attr, String val){
	if(attr.equals("id")){
	    env.id_hash.put(val, this);
	}
	else if(!marimite && attr.equals("ref")){
	    my_ref = val;
	}
	if(marimite && getNode() != null){
	    String prefix = my_prefix;
	    if(prefix != null)
		my_node.setAttribute(getPrefix(true) + attr, val);
	    else
		my_node.setAttribute(attr, val);
	}
    }

    /**
       この UDF Elementの XML Node に XML Attributeを設定する

       @param attr	XML Attribute名
       @param val	値
     */
    public void setAttribute(String attr, int val){
	setAttribute(attr, String.valueOf(val));
    }

    /**
       この UDF Elementの XML Node に XML Attributeを設定する

       @param attr	XML Attribute名
       @param val	値
     */
    public void setAttribute(String attr, long val){
	setAttribute(attr, String.valueOf(val));
    }

    public void removeAttribute(String attr){

	if(marimite && getNode() != null){
	    String prefix = my_prefix;
	    if(prefix != null)
		my_node.removeAttribute(getPrefix(true) + attr);
	    else
		my_node.removeAttribute(attr);
	}
    }

    /**
       この UDF Elementの XML Node の XML Attributeを取得する

       @param attr	XML Attribute名
       @return 値
     */
    public String getAttribute(String attr){
	if(!marimite && attr.equals("ref"))
	    return my_ref;
	if(!marimite && getNode() != null)
	    return null;

	if(my_prefix == null)
	    return my_node.getAttribute(attr);
	else
	    return my_node.getAttribute(getPrefix(true) +  attr);
    }

    /**
       この UDF Elementの XML Nodeに id Attributeを取得する
       
       @return id Attribute
     */
    public String getAttributeId(){
	return getAttribute("id");
    }
    /**
       この UDF Elementの XML Nodeに id Attributeを設定する

       @param attr	id Attribute
     */
    public void setAttributeId(String attr){
	setAttribute("id", attr);
    }


    /**
       XML Nodeの id Attributeをキーに UDF Elementを探す。

       @param id	id Attribute
       @return		UDF_Element。見つからない場合は nullを返す。
     */
    public UDF_Element findById(String id){
	UDF_Element e = (UDF_Element)env.id_hash.get(id);
	/*
	if(e == null){
	    System.err.println("Warning: no such id: '" + id + "'");
	}
	*/
	return e;
    }

    /**
       loc, partno, mirrorから UDF_Extentを探す。

       @param loc	場所
       @param partrefno	パーティション参照番号
       @param subno	副番号
       @return		UDF_Extent。見つからない場合は nullを返す。

       Extentの先頭要素の loc, partno, subnoが等しい Extentを返す。
       同じ Extentが複数あった場合、どれか一つを返す。
     */
    public UDF_Element findExtentByLoc(int loc, int partrefno, int subno){
	//UDF_Element[] el = env.root.getChildren();
	UDF_ElementList el = env.root.getChildList();
	for(Iterator i=el.iterator() ; i.hasNext() ; ){
	    UDF_Extent ex = (UDF_Extent)i.next();
	    if(UDF_Extent.class.isAssignableFrom(ex.getClass())){
		UDF_Extent ext = (UDF_Extent)ex;
		if(ext.getPartSubno() == subno){
		    UDF_ExtentElemList eel = ext.getExtentElem();
		    UDF_ExtentElem eee = eel.firstElement();
		    if(eee.loc == loc && eee.partno == partrefno)
			return ext;
		}
	    }
	}
	return null;
    }

    /**
       Pathnameから UDF Elementを探す

       @param pathname	Pathname
       @return UDF Element

       Pathnameとは  XMLのノードを /を区切文字として並べていったものである。
       例)

       /udf/vrs/standard-id

       ※2005/04/07 現在、/udfを省略して/vrs/standard-idと指定することも可能。過去との互換性のため。

       同じ名前の識別子がある場合、一番最初のものが取得される。n番目の以降のものが欲しい場合は 

	UDF_desc257[n]

       のように指定する。インデックスは0から始まる。

       XML Elementに id属性がついていれば、そちらの名前が優先される。

	/で始まる場合は rootから、そうでなければ自分自身の位置から検索する。

	..は親を意味する
     */
    public UDF_Element findByXPATH(String pathname){
	//debugMsg("pathname=" + pathname);
	if(pathname.length() == 0)
	    return (UDF_Element)this;

	// "/udf"が指定されているときは、それはすなわちrootである。※ディレクトリの概念の'/' にあたるものがない。あえて言うならenv そのものがそう。
	if(pathname.equals("/udf")) {
	    return (UDF_Element)env.root;
	}

	// "/udf"で始まる場合それを取りのぞき rootから検索する。
	if(pathname.length() > 4) {
	    if(pathname.substring(0, 4).equals("/udf")) {
	        return env.root.findByXPATH(pathname.substring(4));
	    }
	}

	//　パスを含むかどうか　//
	int pos = pathname.indexOf('/');

	// /で始まる場合それを取りのぞき rootから検索する。※過去との互換性を維持。
	if(pos == 0){
	    pathname = pathname.substring(1);
	    return env.root.findByXPATH(pathname);
	}

	String car = null;
	String cdr = null;
	if(pos < 0){
	    car = pathname;
	    cdr = "";
	}
	else{
	    car = pathname.substring(0, pos);
	    cdr = pathname.substring(pos + 1);
	}

	//debugMsg("car=" + car);
	//debugMsg("cdr=" + cdr);

	// carが ..かどうか。マッチしたら親へ移動
	if(car.equals("..")){
	    return getParent().findByXPATH(cdr);
	}
	// carが .かどうか。マッチしたら再帰
	if (car.equals(".")) {
	    return findByXPATH(cdr);
	}

	//　何番目のUDF Element を要求されているか　//
	int index = 0;
	int pos1 = car.indexOf('[');
	if (pos1 > 0) {
	    //　インデックス情報とUDF Element 名を分離　//
	    int pos2 = car.indexOf(']');
	    index = Integer.parseInt(car.substring(pos1 + 1, pos2));
	    car = car.substring(0, pos1);
	}

	//　対象となるUDF Element を検索　//
	UDF_Element[] children = getChildren();

	for(int i = 0; i < children.length; i++){

	    if (!children[i].getName().equals(car)) {
		continue;
	    }
	    if (index > 0) {
		index--;
		continue;
	    }
	    //　途中のpathのサーチでなかったら結果として返す　//
	    if (cdr.equals("")) {
		return children[i];
	    }
	    //　検索を継続する　//
	    return children[i].findByXPATH(cdr);
	}

	return null;
    }

    /**
       CRCおよびチェックサムを計算しなおす
     */
    final public static short RECALC_CRC = 1;
    /**
       リファレンスを解決し値を設定する
     */
    final public static short RECALC_REF = 2;
    /**
       Tag Locationを計算しなおす
     */
    final public static short RECALC_TAGLOC = 3;
    /**
       gp, off, partno, mirrorを再計算し、設定する。
     */
    final public static short RECALC_GP = 4;
    /**
       ベリファイのため必要な情報を Envへ代入しなおす。
     */
    final public static short RECALC_ENV = 5;

    /**
       ツリー情報(ディレクトリ構造)を構築する。
     */
    final public static short RECALC_TREE = 6;

    /**
       (内部的な)スペースビットマップを再計算する。

       もし、そのパーティションにSpaceBitmapDesc(desc_264)が存在すれば、それを使う。
       なければファイルシステム構造を走査し構築する。
     */
    final public static short RECALC_SB = 7;

    /**
       UDF_Extent の最終 UDF Element として UDF_pad を再計算する。

        UDF_pad が最終 UDF Element として存在するときは、その UDF_pad を再計算する。<br>
        存在しないときは新たな UDF_pad を作成して再計算する。<br>
        元の UDF_pad より小さくなるときは byte データの先頭から減らすような動作をする。
    */
    final public static short RECALC_PAD = 8;

    /**
       (desc261|desc266)->desc258のリンクの再構成をする。
     */
    final public static short RECALC_ADLIST = 9;

    /**
       VDSおよび LVISのリンクの再構成、
       FDSのリンクの再構成、
       PHD->SB のリンクの再構成、
       等、主要デスクリプタの参照や内部変数の再構成を行う。
     */
    final public static short RECALC_VDSLIST = 10;

    /**
       VDSのリンクの再構成だけを行う
     */
    final public static short RECALC_VDSLIST2 = 13;

    /**
       partition map の関連を再構成する
       (例 Metadata Partmap -> Metadata File Entry/Metadata Mirror File Entry/Metadata Bitmap File Entry)
     */
    final public static short RECALC_PARTMAP = 11;

    final public static short RECALC_LVIS = 12;

    /**
       この UDF Elementおよびその子孫 UDF Elementの正当性を検査する。

       @return 結果リスト
     */
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	UDF_Element[] children = getChildren();
	

	if(sa_ya == UDF_XMLUtil.SAYA_UME)
	    throw new UDF_NotImplException(null, "\nSorry, but this version of udfv is not supported to verify xml and image file.\n");

	for(int i = 0; i < children.length; i++)
	    el.addError(children[i].verify());
	
	return el;
    }
    
    /**
       UDF Elementのverify() メソッドを呼び、その親となる原因を設定する。
       
       @param parent  親エレメントを示す文字列
       @return エラーリスト
    */
    public UDF_ErrorList verify(String parent) throws UDF_Exception{
	
	UDF_ErrorList ret = verify();
	ret.setRName(parent);
	return ret;
    }
    
    /**
       現在の UDFのリビジョンレベルに合った適切なパッケージを持つ UDF_Elementを生成する。

       @param className	クラス名
       @param prefix	ネームスペース
       @param tagName	XML Node名
     */
    public UDF_Element createElement(String className, String prefix, String tagName) {
	try{
	    UDF_Element elem = genElement(className, (UDF_Element)this, prefix, tagName);
	    return elem;
	}
	catch(ClassNotFoundException e){
	    System.err.println("no such element:" + className +" " + prefix + ":" + tagName);
	}
	return null;
    }

    /**
       現在の UDFのリビジョンレベルに合った適切なパッケージを持つ UDF_Elementを生成する。

       @param className	クラス名
       @param mother	親
       @param prefix	ネームスペース
       @param tagName	XML Node名
     */
    static public UDF_Element genElement(String className, UDF_Element mother, String prefix, String tagName) throws ClassNotFoundException{
	return generator.genElement(className, mother, prefix, tagName);
    }
    
    /**
       デフォルト値を設定する。

       UDF_Elementは createElement()しただけでは、子供要素が割りつけられない。
       したがって、createElement()した直後はその要素は全て nullであり、
       値の代入等を行うことができない。

       setDefaultValue()は子供要素を全て割りつけし、値を代入できる状態にすると
       ともに、初期値が設定されていれば、それで値を初期化する
     */
    public void setDefaultValue(){
    }


    /**
       デバッグメソッド

       @param indent	インデント
     */
    abstract public void debug(int indent);

    public String toString(){
	if(getName() != null)
	    return getName();
	return getClass().getName();
    }

    public void ignoreMsg(String method, Exception e){
	e.printStackTrace();
	System.err.println(getClass().getName() + "." + method + "() caught " + e.getClass().getName() + ", but ignore it and continue.");
	//debug(0);
    }

    /**
       情報表示メソッド。

       @param indent インデント用文字列。
       @param detail 詳細表示を行うか。
    */
    public String [] toInformation(String indent, boolean detail) {
        return new String[] { "" };
    }

    // =================================================
    // recalc
    // =================================================
    protected void recalcGlobalPoint(UDF_RandomAccess f){
	try{
	    //System.err.println(getName()+":"+f.getAbsPointer());
	    setGlobalPoint(f);
	    setElemPartRefNo(f.getPartRefNo());
	    setPartMapOffset(f.getPartPointer());
	    //setMirror(f.isMirror());
	    setPartSubno(f.getPartSubno());
	}
	catch(Exception e){
	    ignoreMsg("recalc", e);
	}
    }

    /**
       この UDF Elementおよびその子孫の UDF Elementの値を計算しなおす。

       @param type	再計算するタイプ
       @param f		再計算に用いるアクセサ。GP、ENVの再計算の時に使用する。
     */
    public void recalc(short type, UDF_RandomAccess f){
	
	try{
	    long saved_pos = 0;
	    long ofst = 0;
	    if(type == RECALC_GP){
		saved_pos = f.getPointer();
		recalcGlobalPoint(f);
	    }
	    //UDF_Element[] children = getChildren();
	    UDF_ElementList cl = getChildList();
	    for(Iterator it = cl.iterator() ; it.hasNext(); ) {
		UDF_Element child = (UDF_Element)it.next();
		child.setOffset(ofst);
		child.recalc(type, f);
		try{
		    ofst += child.getLongSize();
		}
		catch(NullPointerException e){
		    child.debug(0);
		}
	    }
	    if(type == RECALC_GP){
		f.seek(saved_pos + getLongSize());
	    }
	}
	catch(Exception e){
	    ignoreMsg("recalc", e);
	}
    }


    //========================
    // hooks
    //========================

    /**
       イメージからデータを読んだ後に実行するフック関数。

       @param f アクセサ
     */
    public void postReadHook(UDF_RandomAccess f) throws IOException, UDF_Exception{
    }


    /**
       イメージからデータを読む前に実行するフック関数。

       @param f アクセサ
     */
    public void preReadHook(UDF_RandomAccess f) throws IOException{
	my_gp = -1;
	setElemPartRefNo(f.getPartRefNo());
	setPartMapOffset(f.getPartPointer());
	//setMirror(f.isMirror());
	setPartSubno(f.getPartSubno());
	setGlobalPoint(f);
    }

    /**
       XMLから情報を読み取る前に行う前処理。

       @param n	このUDFエレメントに対応した XMLノード
    */
    protected void preReadFromXMLHook(Node n) {
	debugMsg(3, "reading.." + n);
	/*
	  属性をコピル
	*/
	NamedNodeMap nnm = n.getAttributes();
	for(int i=0 ; i<nnm.getLength() ; ++i){
	    Node n2 = nnm.item(i);
	    if(n2 != null){
		String attr = n2.getNodeName();
		if(attr.indexOf(":") > 0)
		    attr = UDF_Util.cdr(attr, ':');
		setAttribute(attr, n2.getNodeValue());
	    }
	}

    }

    /**
       XMLから情報を読み取った後に行う後処理。

       @param n	このUDFエレメントに対応した XMLノード
    */
    protected void postReadFromXMLHook(Node n){
	;
    }

    public void postSetDefaultValueHook(){
	;
    }

    /**
       duplicateのときに実行されるフック関数
    */
    protected void duplicateHook(UDF_Element src) {

	if (marimite) {
	    Node n = src.getNode();
	    NamedNodeMap nnm = n.getAttributes();
	    for(int i=0 ; i<nnm.getLength() ; ++i){
		Node n2 = nnm.item(i);
		if(n2 != null){
		    /*
		      attributeをコピーする
		      1)id属性は "_DUP"を付加して複製する。
		      2)ref属性は "_DUP"を付加して複製する。

		      ※"_DUP"は変更予定
		    */
		    String name = n2.getNodeName();
		    if(name.equals("id"))
			setAttribute(name, n2.getNodeValue() + "_DUP");
		    else if(name.equals("ref")){
			String ref = n2.getNodeValue();
			String car = UDF_Util.car(ref, '.');
			String cdr = UDF_Util.car(ref, '.');
			setAttribute(name,  car + "_DUP." + cdr);
		    }
		    else
			setAttribute(name, n2.getNodeValue());
		}
	    }
	}
	setParent(src.getParent());
    }

    /**
       Elementの情報を文字列で取得する。

       (debugの文字列版)
     */
    public String getInfo(int indent){
	StringBuffer sb = new StringBuffer();

	sb.append(getName() + ":" + "\n");
	UDF_Element[] child = getChildren();
	
	for(int i=0 ; i<child.length ; ++i){
	    sb.append(child[i].getInfo(indent + 1));
	}

	return sb.toString();
    }

    protected JComponent createJInfoPanel(String name){
	JPanel panel = new JPanel();
	LayoutManager lm = new BoxLayout(panel, BoxLayout.Y_AXIS);
	
	
	Border border = new CompoundBorder(
		new MatteBorder(5, 5, 0, 0, Color.WHITE),
		new TitledBorder(null, name,
		                 TitledBorder.LEFT,
		                 TitledBorder.TOP)
		);
	    
	panel.setLayout(lm);
	panel.setBorder(border);
	panel.setBackground(Color.WHITE);

	return panel;
    }
    public JComponent getJInfo(){

	if(hasChild()){
	    JComponent panel = createJInfoPanel(getName());

	    for(Iterator it=getChildList().iterator() ; it.hasNext() ; ){
		UDF_Element elem = (UDF_Element)it.next();
		panel.add(elem.getJInfo());
	    }
	    return panel;
	}
	else{
	    JTextArea jt =  new JTextArea(getInfo(0));
	    jt.setEditable(false);
	    return jt;
	}
    }


}
