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
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
import java.lang.reflect.*;
import com.udfv.exception.*;
import com.udfv.access.*;
import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.encoding.*;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextArea;

/**
   バイトデータを表現するクラス。

<pre>
UDF_bytesは任意の長さの、任意のバイトデータを現わすことができる
クラスである。

1) バイトデータのXMLにおける表現

例)
4バイトの「0x41 0x42 0x43 0x44」というバイト列は 

<UDF_bytes size="4">ABCD</UDF_bytes>

と表現される。

可視化できない特殊な文字コードが含まれている場合は
Quoted Printable Encodeで表現する。

例)
4バイトの「0x00 0x01 0x02 0x03」というバイト列は

<UDF_bytes size="4">=00=01=02=03</UDF_bytes>

と表現される。

バイトデータは同じバイトが繰り返し並ぶことが多いため
視覚的に見やすくするため{}をつかって繰り返しを表現
することができる。

例)
2048バイトの「0x00... 0x00」というバイト列は

<UDF_bytes size="2048">=00{2048}</UDF_bytes>

と表現される。

どのようなバイト列があるのか事前にわかっている場合は
encoding属性を指定することにより、より見やすい表記が可能である。

例)
OSTA Compressed Unicodeでエンコードされた DVD_RTAVというデータは

<UDF_bytes encoding="OSTA Compressed Unicode" size="9">8:DVD_RTAV</UDF_bytes>

上のように表現可能である。この場合どのように表記するかはEncodingスキー
マによる。

2) 子供を持つ UDF_bytes

UDF_bytesの大きな特徴は任意のUDF Elementを子供として持つことが
出来ることである。

例)
<UDF_bytes>
<UDF_desc257>
....
</UDF_desc257>
<UDF_desc257>
....
</UDF_desc257>
<UDF_bytes>

この場合<UDF_bytes>は それぞれの<UDF_desc257>をバイトに変換し
それを連結したバイトのように見える。

UDF_bytesの中に置くXMLタグは無名タグ(クラス名に等しい UDF_で始まるタグ)
でなくてはならない。

3) UDF_bytesの制限

データを持ち、かつ子供を持つような UDF_bytesは作れない。
どうしても必要な場合は以下のように UDF_bytesの中に UDF_bytesを入れれば
よい。

<UDF_bytes>
<UDF_desc257>
....
</UDF_desc257>
<UDF_desc257>
....
</UDF_desc257>
<UDF_bytes size="4">ABCD</UDF_bytes>
<UDF_bytes>

4)子供のデータが書きかわったときの処理

4-1) データが書きかわった場合
getData(), writeTo()はデータが反映される。

4-2) サイズが小さくなった場合
getData(), writeTo()はデータが反映される。
足りない分は0x00で埋められる。

4-3) サイズが大きくなった場合
getData(), writeTo()はデータが反映されるが、
sizeを越えた分は書き出されない。ただし、データは
保持されており、sizeを大きくすると書き出されるようになる。

</pre>
*/
public class UDF_bytes extends UDF_Element
{
    UDF_Encoding my_enc;
    protected byte[] my_data;
    int my_size;

    public UDF_bytes(UDF_Element elem, String prefix, String name, int sz){
	super(elem, prefix, name == null ? "UDF_bytes" : name);
	my_data = new byte[sz];
	my_size = sz;
    }

    /**
       このコンストラクタを使うことは推奨されない。
     */
    public UDF_bytes(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name);
	my_data = new byte[0];
	my_size = 0;
    }

    /**
       バイトデータを UDF_Bytesにセットする。

       @param b バイトデータ

       このメソッドは引数で渡されたバイトデータを内部的なデータに複写し、また、サイズをバイトデータと同じにする。

       <b>注意</b><br/>
       UDF_bytesが子供を持っていた場合、子供は全て破棄される。
     */
    public void setData(byte[] b){
	removeAllChildren();
	setSize(b.length);
	for(int i=0 ; i<b.length ; ++i){
	    my_data[i] = b[i];
	}
	setNodeVal();
    }

    /**
       文字列をバイトデータとして設定する。

       このメソッドにより、UDF_bytesのサイズは変更されない。
       サイズに足りない部分は0x00で埋められる。

       @param s	文字列
     */
    public void setValue(String s){
	int size = getSize();

	try{
	    byte [] b = getData();
	    if(my_enc == null) {
		b = UDF_Util.qstr2b(s);
	    }
	    else {
		b = my_enc.toBytes(s);
	    }
	    setData(b);
        }
	catch(UDF_Exception e){
	    ;
	}

	setSize(size);
    }

    /**
       バイトデータをバイト配列として取得する。
       
       @return データのバイト配列。

       <b>注意</b><br/>
       getData()で取得したデータの値を変更しても UDF_bytesが保持するバイトデータは書きかわらない。
    */
    final public byte[] getData(){
	if(!hasChild()){
	    byte[] b = new byte[my_data.length];
	    for(int i=0 ; i<b.length ; ++i){
		b[i] = my_data[i];
	    }
	    return b;
	}
	UDF_ElementBase[] child = getChildren();
	long child_size = 0;
	for(int i=0 ; i<child.length ; ++i){
	    child_size += child[i].getSize();
	}
	int size = (int)child_size;

	if(size > my_size)
	    size = my_size;
	
	UDF_RandomAccessBuffer rab;
	if(child_size > my_size)
	    rab = new UDF_RandomAccessBuffer(new byte[(int)child_size]);
	else
	    rab = new UDF_RandomAccessBuffer(new byte[my_size]);
	try{
	    for(int i=0 ; i<child.length ; ++i){
		child[i].writeTo(rab);
	    }
	}
	catch(IOException e){
	    ;
	}
	catch(UDF_Exception e){
	    ;
	}

	if(child_size > my_size)
	    return UDF_Util.subByte(rab.getBuffer(), 0, my_size);
	else
	    return rab.getBuffer();
    }
    
    /**
       バイトデータを文字列として取得する。
       内部エンコーディングが設定されている場合、"Quoted Encoded" 文字列を返す。
       そうでない場合、単にtoString() 文字列を返す。
       
       @return データ文字列。
    */
    public String getStringData(){
	String s;
	if(my_enc == null)
	    s = UDF_Util.b2qstr(getData());
	else
	    s = my_enc.toString(getData());

	return s;
    }

    /**
       文字列データをバイト配列に変換してsetData()する。

       内部エンコーディングが設定されていない場合、"Quoted Encoded" 文字列が与えられたと仮定して処理する。

       @param s 文字列。
    */
    public void setStringData(String s) throws UDF_Exception {

        byte [] b;
        if(my_enc == null) {
            b = UDF_Util.qstr2b(s);
        }
        else {
            b = my_enc.toBytes(s);
        }
        setData(b);
    }

    /**
       バイトデータのサイズを取得する。
       
       @return サイズ。
    */
    public int getSize(){
	return my_size;
    }
    
    /**
       バイトデータのサイズをlong 型で取得する。
       
       @return サイズ。
    */
    public long getLongSize(){
	return (long)my_size;
    }
    

    /**
       バイトデータのサイズを増やす/減らす。
       
       @param sz 増加/減少するサイズ

       サイズが大きくなる場合は余った部分は 0x00で埋められる。
    */
    public void incSize(int sz){
	setSize(getSize() + sz);
    }


    /**
       バイトデータのサイズを設定する。
       
       @param sz 設定するサイズ。

       サイズが大きくなる場合は余った部分は 0x00で埋められる。
    */
    public void setSize(int sz){
	if(sz < 0)
	  sz = 0;
	my_size = sz;
	byte[] new_b = new byte[sz];
	for(int i=0 ; i < my_data.length && i < new_b.length ; ++i){
	    new_b[i] = my_data[i];
	}
	my_data = new_b;

	setAttribute("size", my_data.length);
    }
    
    /**
       バイトデータを文字列と比較する。
       
       @param s  比較対象となる文字列。
       @return  データと文字列が一致する場合、true を返す。
                一致しない場合、false を返す。
    */
    public boolean cmpString(String s){
	return UDF_Util.cmpBytesString(getData(), s);
    }
    
    /**
       バイトデータのXML上の表現のエンコーディング方法を設定する。
       
       @param encoding エンコーディングを指すUDF_Encoding インスタンス。

       <b>注意</b><br/>
       UDF_bytesが子供を持っていた場合、子供は全て破棄される。
    */
    public void setEncoding(UDF_Encoding encoding){
	my_enc = encoding;
	setAttribute("encoding", encoding.encName());

	setNodeVal();
    }
    
    /**
       現在設定されているエンコーディングを取得する。
       
       @return 設定されているUDF_Encoding インスタンス。
    */
    public UDF_Encoding getEncoding( ) {
	return my_enc;
    }
    
    /**
       エレメントに値を設定する。
    */
    protected void setNodeVal(){
	//has_child = false;
	removeAllChildren();
	if(marimite){
	    String v;
	    if(getSize() == 0)
		return;
	    
	    if(my_enc == null){
		v = UDF_Util.b2qstr(getData());
	    }
	    else
		v = my_enc.toString(getData());
	    setNodeVal(v);
	}
    }
    
    /**
       アクセサからバイトデータを読み込む。
       
       @param f  アクセサ。
       @return   実際に読み込んだデータのバイト数。

       <b>注意</b><br/>
       UDF_bytesが子供を持っていた場合、子供は全て破棄される。
    */
    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception{
	preReadHook(f);

	if(getSize() == 0)
	    return 0;

	setAttribute("size", getSize());
	int ret =  f.read(my_data, 0, getSize());

	setNodeVal();

	postReadHook(f);
	return ret;
    }
    
    /**
       アクセサにバイトデータを書き込む。
       
       @param f  アクセサ。
       @return   実際に書き込んだデータのバイト数。
    */
    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{
	if(getSize() == 0)
	    return 0;

	byte[] b = getData();
	long ret =  f.write(b);
	return ret;
    }
    
    /**
       XML からバイトデータを読み込む。
       
       @param n  読み込むノード。
    */
    public void readFromXML(Node n) throws UDF_Exception{
	preReadFromXMLHook(n);
	
	NodeList nl = n.getChildNodes();
	UDF_Encoding enc = null;
	String attr_enc = ((Element)n).getAttribute(getPrefix(true) + "encoding");
	int size = UDF_Util.str2i(((Element)n).getAttribute(getPrefix(true) + "size"));

	int i0 = 0;

	setSize(size);
	try{
	    enc = UDF_Encoding.genEncoding(attr_enc);
	    setEncoding(enc);
	}
	catch(UDF_EncodingException e){
	    ;
	}


	// UDF_bytesは中に子エレメントを入れることができる
	// 1つも子エレメントがなければ子テキストノードが UDF_bytesのデータを意味し
	// 1つでも子エレメントがあれば子テキストノードは全て無視する
	if(nl != null){
	    boolean has_child = false;
	    for(int i=0 ; i<nl.getLength() ; ++i){
		Node child = nl.item(i);
		if(child.getNodeType() == Node.ELEMENT_NODE){
		    has_child = true;
		    break;
		}
	    }
	    /*
	      子エレメントがないときはテキストノードをそのままいれる。
	      encodingが指定されているときは、それで XMLのテキストノードを encodeする
	    */
	    if(!has_child){
		for(int i=0 ; i<nl.getLength() ; ++i){
		    Node child = nl.item(i);
		    if(child.getNodeType() == Node.TEXT_NODE){
			String text_str = ((Text)child).getData();
			//Text tt = getDocument().createTextNode(text_str);
			//getNode().appendChild(tt);

			byte[]data;
			if(enc == null)
			    data = UDF_Util.qstr2b(text_str, size);
			else
			    data = enc.toBytes(text_str, size);

			setData(data);
			setNodeVal();
		    }
		}
	    }
	    /*
	      子エレメントがあるときは、子エレメントを生成する。
	     */
	    else{
		UDF_RandomAccessBuffer rab = new UDF_RandomAccessBuffer(getSize());

		for(int i=0 ; i<nl.getLength() ; ++i){
		    Node child = nl.item(i);
		    if(child.getNodeType() == Node.ELEMENT_NODE){
			Element elem = (Element)child;
			try{
			    //子エレメント生成
			    UDF_Element new_elem = createElement(child.getLocalName(), null, null);
			    appendChild(new_elem);
			    new_elem.setHintSize(getSize());
			    new_elem.readFromXML(child);
			    new_elem.writeTo(rab);//getData()で値が取れるようにdataに格納する。
			}
			catch(Exception e){
			    e.printStackTrace();
			}
		    }
		}
		my_data = rab.getBuffer();
	    }
	}	
    }
    
    /**
       保持しているバイトデータを、指定したエレメントと置き換える。
       
       @param v  置き換えるエレメントのインスタンス。

       <b>注意</b><br/>
       UDF_bytesが子供を持っていた場合、全て破棄されてから追加される。

       UDF_bytesのサイズ以上の子供を追加した場合、 getData(), writeTo()で
       サイズ分しかデータを取得できない。
    */
    public void replaceChildren(UDF_ElementList v) throws UDF_Exception{
	int elem_sz = 0;
	int this_sz = getSize();

	removeAllChildren();
	for(int i=0 ; i<v.size() ; ++i){
	    UDF_Element elem = (UDF_Element)v.elementAt(i);
	    elem_sz += elem.getSize();
	    appendChild(elem);
	}
	//has_child = true;
    }

    /**
       保持しているバイトデータを、指定したエレメントと置き換える。
       
       @param elm  置き換えるエレメントのインスタンス。

       UDF_bytesのサイズ以上の子供を追加した場合、 getData(), writeTo()で
       サイズ分しかデータを取得できない。
    */
    public void replaceChildren(UDF_Element[] elm) throws UDF_Exception{
	int elem_sz = 0;
	int this_sz = getSize();

	removeAllChildren();
	for(int i=0 ; i<elm.length ; ++i){
	    UDF_Element elem = elm[i];
	    elem_sz += elem.getSize();
	    appendChild(elem);
	}
	//has_child = true;
    }
    
    /**
       保持しているバイトデータを、指定したエレメントと置き換える。
       
       @param elem  置き換えるエレメントのインスタンス。

    */
    //↓ウソ？
    //このメソッドはUDF_bytesをバッファと見たてて、そこから、
    //readFromを実行し、UDF_bytesの中の子供として追加する。
    public void replaceChild(UDF_Element elem) throws UDF_Exception{
	int elem_sz = elem.getSize();
	int this_sz = getSize();

	removeAllChildren();
	appendChild(elem);

/*	    if(this_sz > elem_sz){
		byte[] b = UDF_Util.subByte(getData(), elem_sz, this_sz - elem_sz);
		if(my_enc == null)
		    setNodeVal(UDF_Util.b2qstr(b));
		else
		    setNodeVal(my_enc.toString(b));
	    }
	   */
	//has_child = true;
    }
    
    /**
       保持しているバイトデータを、指定したエレメントと置き換える。
       このメソッドは、置き換える前にreadFrom() を実行する。
       
       @param elem  置き換えるエレメントのインスタンス。

       このメソッドはUDF_bytesをバッファの見たてて、そこから、
       readFromを実行し、UDF_bytesの中の子供として追加する。

    */
    public void readFromAndReplaceChild(UDF_Element elem) throws UDF_Exception{
	UDF_RandomAccessBuffer rab = genRandomAccessBytes();
	    
	try{
	    elem.readFrom(rab);
	}
	catch(IOException e){
	    /* ignore */
	}
	
	    replaceChild(elem);
    }
    
    /**
       保持しているバイトデータにアクセスするためのUDF_RandomAccessBytes インスタンスを生成します。
       
       @return 生成したアクセサインスタンス。
    */
    public UDF_RandomAccessBytes genRandomAccessBytes(){
	return new UDF_RandomAccessBytes(this);
    }

    /**
        自身と同じバイト配列データを持つ UDF_bytes のインスタンスを生成する。

        duplicateElement()と違い、子の UDF Element などは複製されない。

        @param name 作成する UDF Element の XML Node 名（内部で UDF_ElementBase#createElement()に渡される）
    */
    public UDF_bytes createElementBytes(String name) {

        UDF_RandomAccessBytes rab = genRandomAccessBytes();
        UDF_bytes b = (UDF_bytes) createElement("UDF_bytes", null, name);
        b.setSize(getSize());
        try {
            b.readFrom(rab);
        }
        catch(UDF_Exception e) {
            //　ここにはこない　//
            debugMsg(0, "readFrom(genRandomAccessBytes()) : error");
        }
        catch(IOException e) {
            //　ここにはこない　//
            debugMsg(0, "readFrom(genRandomAccessBytes()) : error");
        }

        return b;
    }

    /**
      バイト配列の内容と同じものを指定の UDF Element として読み込んだインスタンスを返す。

      @param className クラス名。
      @param prefix    ネームスペース。
      @param tagName   XML Node 名。
      @return 作成された UDF Element のインスタンス。

      <em>
      readFrom()メソッドが実装されていない UDF Element にでは意図通りに動作しない。(UDF_Extent など)
      </em>

    */
    public UDF_ElementBase toElement(String className, String prefix, String tagName) throws UDF_Exception, IOException {

        UDF_RandomAccessBytes rab = genRandomAccessBytes();
        UDF_ElementBase elem = (UDF_ElementBase) createElement(className, prefix, tagName);
        elem.readFrom(rab);
        return elem;
    }

    /**
       エレメントを複製する。
       
       @return 複製されたエレメント。
    */
    public UDF_Element duplicateElement(){
	UDF_bytes v = (UDF_bytes)createElement(getClass().getName(), null, getName());
	v.duplicateHook(this);
	v.setSize(getSize());
	v.setData(getData());
	
	if(getEncoding() != null)
	    v.setEncoding(getEncoding());
	v.setNodeVal();

	UDF_ElementBase[] child = getChildren();
	if(child.length > 0){
	    v.removeAllChildren();
	    for(int i=0 ; i<child.length ; ++i){
		v.appendChild((UDF_Element) child[i].duplicateElement());
	    }
	}

	return v;
    }
    
    public void debug(int indent){
	System.err.print(UDF_Util.repeat(' ', indent));
	String info = getName() + " size=" + getSize();
	if(getEncoding() != null)
	    info +=  " enc=\"" + getEncoding().encName() + "\"";
	else
	    info +=  " enc=default";

	info += " pn=" + getElemPartRefNo();
	info += " sn=" + getPartSubno();

	System.err.print(info + ":\n");
	UDF_ElementBase[] e = getChildren();
	if(hasChild()){
	    for(int i=0 ; i<e.length ; ++i){
		e[i].debug(indent + 1);
	    }
	}
	else
	    System.err.println(UDF_Util.b2qstr(getData()));
    }

    public String getInfo(int indent){
	String ind = UDF_Util.repeat(' ', indent);
	String info = "" + ind;
	info += getName() + "(size=" + getSize();
	if(getEncoding() != null)
	    info +=  " enc=\"" + getEncoding().encName() + "\"";

	info += "):\n";
	UDF_ElementBase[] e = getChildren();
	if(hasChild()){//子供があるとき
	    for(int i=0 ; i<e.length ; ++i){
		info += e[i].getInfo(indent + 1);
	    }
	}
	else
	    info += ind + ind + UDF_Util.b2qstr(getData()) + "\n";

	return info;
    }
    public void setDefaultValue(){
	setNodeVal();
    }
    public void updateNodeVal() {
	setNodeVal();
    }

    public JComponent getJInfo(){
	String info = "";
	info += getName() + " (size=" + getSize();
	if(getEncoding() != null)
	    info +=  " enc=\"" + getEncoding().encName() + "\"";

	info += ")";

	JComponent panel = createJInfoPanel(info);
	if(hasChild()){
	    for(Iterator it=getChildList().iterator() ; it.hasNext() ; ){
		UDF_Element elem = (UDF_Element)it.next();
		panel.add(elem.getJInfo());
	    }
	    return panel;
	}
	else{
	    JTextArea jt = new JTextArea(getStringData());
	    jt.setEditable(false);
	    panel.add(jt);
	    return panel;
	}
    }
};
