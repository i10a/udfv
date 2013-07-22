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

import com.udfv.access.*;
import com.udfv.exception.*;
import java.io.*;
import java.util.*;
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.ecma167.*;

/**
<p>
  UDF_Dataは外部ファイルを参照するバイトデータを表わす UDF_Elementである。
</p>
<p>
  UDFの巨大なバイトデータ(例えばファイルのような)をXML中にUDF_bytes等で表現するのは無駄が多い。
そのようなデータは別ファイルとして外部参照とするのが望ましい。
UDF_Dataは、そのような外部ファイル上にあるデータを取りこむことができるクラスである。
</p>
<p>
UDF_Dataは XMLで表現したときに、src-file, src-off, lenの３つの属性を持つ。それぞれの意味は以下の通りである。
<dl>
<dt>src-file</dt><dd>参照するファイル。OSで有効なパス名。</dd>
<dt>src-off</dt><dd>ファイル中での取りこむデータの開始位置</dd>
<dt>src-off</dt><dd>ファイル中での取りこむデータの長さ</dd>
</dl>

</p>
 */
public class UDF_Data extends UDF_Element
{
    String my_src;
    long my_off;
    long my_len;

    public UDF_Data(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? "UDF_Data" : name);
    }
    
    /**
       ソースファイルを設定する
       
       @param src	ソースファイル
     */
    public void setSourceFile(String src){
	my_src = src;
	setAttribute("src-file", src);
    }
    /**
       データのソースファイル中での開始位置を設定する
       
       @param off	オフセット
     */
    public void setSourceOffset(long off){
	my_off = off;
	setAttribute("src-off", off);
    }
    /**
       データのソースファイル中での長さを設定する
       
       @param len	長さ
     */
    public void setLength(long len){
	my_len = len;
	setAttribute("len", len);
    }

    public long readFrom(UDF_RandomAccess f) throws IOException{
	return my_len;
    }

    //
    private void writeBytesTo(UDF_RandomAccess f, byte num, long len) {
    }

    /**
      この UDF Element を書き出す。

      @param f アクセサ
      @return 書き込んだ長さ
      @throws 何らかの理由で書き出しに失敗した。
    */
    public long writeTo(UDF_RandomAccess f) throws IOException{
	String fname;
	long off, len;
	
	
	//　書き込みはExtentに依存しませんので、底辺のアクセサを使用します　//
	//f = env.f;

	try {

	    NamedNodeMap map = getNode().getAttributes();

	    //　元のイメージファイル名の取得　//
	    //fname = map.getNamedItem("src-file").getNodeValue();
	    fname = my_src;

	    if (fname.equals("null")) {
		return my_len;
	    }

	    fname = env.absolutePath(fname);

	    //　データ位置と長さを読み込み　//

	    off = my_off;//Long.parseLong(map.getNamedItem("src-off").getNodeValue());
	    len = my_len;//Long.parseLong(map.getNamedItem("len").getNodeValue());
	    
	    // ファイルコピーはしない
	    if(!env.filecopy)
		return my_len;
	}
	catch (NullPointerException e ) {
	    return 0;
	}

        //　現在のイメージ書き込み位置へシーク　//
        //f.seek(off, UDF_RandomAccessFile.SEEK_SET);

	//　元ファイルが存在しなかったときはダミー書き込みを行う　//
	File check = new File(fname);
	if (!check.exists()) {

	    debugMsg(0, "UDF_Data : NOT FOUND SRC FILE = " + fname);

	    try {
	        f.writeBytes((byte)-1, len);
	    }
	    catch(UDF_EOFException e) {
	        e.printStackTrace();
	        System.exit(1);
	    }
	    return my_len;
	}
	if (check.length() < off) {
	    debugMsg(0, "UDF_Data : NOT ENOUGH SIZE, SRC FILE " + fname);

	    try {
	        f.writeBytes((byte)-1, len);
	    }
	    catch(UDF_EOFException e) {
	        e.printStackTrace();
	        System.exit(1);
	    }
	    return my_len;
	}

	check = null;

	//　元のファイルを読み込み専用でオープン　//
	RandomAccessFile raf = new RandomAccessFile(fname, "r");

	//　元のデータ位置へシーク　//
	raf.seek(off);

	//　読み込み用バッファを生成　//
        byte [] buff = new byte[1024 * 1024];

	long len0 = len;
        //　元ファイルから現在のイメージへデータを複写します　//
        for ( ; len > 0; ) {

	    int slen = (int)((len < buff.length) ? len: buff.length);
	    int rlen = (int)raf.read(buff, 0, slen);
	    if (rlen < 0) {
	        throw new IOException("UDF_Data#writeTo() RandomAccessFile#read() error, off = " + off);
	    }

	    for (int woff = 0; woff < rlen; ) {
		try {
		    debugMsg(1, "writing..." + ((100.0  * (len0-len)) / len0) + "%");
		    woff += f.write(buff, woff, rlen - woff);
		}
		catch (UDF_EOFException e) {
		    //　ここにはこないことが期待される　//
		    throw new IOException(e.toString());
		}
	    }
	    len -= rlen;
	    //	    System.err.println(len);
        }
	debugMsg(3, "done 100%");

	//　バッファ解放　//
	buff = null;

	//　ファイル解放　//
	raf.close();
	raf = null;

	return my_len;
    }

    public void readFromXML(Node n){
	preReadFromXMLHook(n);
	Element e = (Element)n;
	//NamedNodeMap map = n.getAttributes();
	/*
	setAttribute("len", map.getNamedItem("len").getNodeValue());
	setAttribute("src-file", map.getNamedItem("src-file").getNodeValue());
	setAttribute("src-off", map.getNamedItem("src-off").getNodeValue());
	*/
	setLength(Long.parseLong(e.getAttribute("len")));
	setSourceFile(e.getAttribute("src-file"));
	setSourceOffset(Long.parseLong(e.getAttribute("src-off")));
    }

    public void debug(int indent){
	;
    }

    public int getSize(){return (int)my_len;}
    public long getLongSize(){return my_len;}

    /**
       UDF_Data同士を比較をする。

       等しい条件は

       1)サイズが等しい
       2)参照ファイルが同じ
       3)参照オフセット位置が同じ
       4)参照サイズが同じ

       の4つを満すものである。
     */
    public int compareTo(Object o){
	if(!getClass().isAssignableFrom(o.getClass())){
	    throw new ClassCastException(getClass().getName() + "!=" + o.getClass());
	}

	UDF_Data d = (UDF_Data)o;

	if(getLongSize() != d.getLongSize())
	    return (int)(getLongSize() - d.getLongSize());

	if(my_src.equals(d.my_src))
	    return my_src.compareTo(d.my_src);

	if(my_off != d.my_off)
	    return (int)(my_off - d.my_off);

	if(my_len != d.my_len)
	    return (int)(my_len - my_len);

	return 0;
    }
}
