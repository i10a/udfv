/*
 */
package com.udfv.util;

import java.io.*;
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
import com.udfv.exception.*;
import com.udfv.core.*;

/**
<p>
   UDF_Utilは UDFV用のユーティリティを集めたクラスである。
</p>
<p>   
   UDF_Utilのメソッドは全て staticでなければならない。
</p>   
 */
abstract public class UDF_Util extends UDF_Base
{
    /**
       整数をn桁の文字列にする。負数のときの動作は保証しない
       足りない桁は chで埋め、右によせる

       @param  i	整数
       @return	文字列
     */
    static public String i2d(int i, int n, char ch){
	char[] c = new char[n];
	for(int j=0 ; j<c.length ; ++j)
	    c[j] = ch;

	String s = new String(c) + String.valueOf(i);
	return s.substring(s.length() - n, s.length());
    }

    /**
       整数をn桁の文字列にする。負数のときの動作は保証しない
       足りない桁は chで埋め、右によせる

       @param  i	整数
       @return	文字列
     */
    static public String i2d(long i, int n, char ch){
	char[] c = new char[n];
	for(int j=0 ; j<c.length ; ++j)
	    c[j] = ch;

	String s = new String(c) + String.valueOf(i);
	return s.substring(s.length() - n, s.length());
    }
    
    /**
       整数を10桁の文字列にする。負数のときの動作は保証しない

       @param  i	整数
       @return	文字列
     */
    static public String i2d10(int i){
	String s = "000000000" + String.valueOf(i);
	return s.substring(s.length() - 10, s.length());
    }
    /**
       整数を4桁の文字列にする。負数のときの動作は保証しない

       @param  i	整数
       @return	文字列
     */
    static public String i2d4(int i){
	String s = "000" + String.valueOf(i);
	return s.substring(s.length() - 4, s.length());
    }
    /**
       整数を2桁の文字列にする。負数のときの動作は保証しない

       @param  i	整数
       @return	文字列
     */
    static public String i2d2(int i){
	String s = "0" + String.valueOf(i);
	return s.substring(s.length() - 2, s.length());
    }
    /**
       文字列をintにする。
    */
    static public int str2i(String s){
	return Integer.parseInt(s);
    }

    /**
       文字列をlongにする。
    */
    static public long str2l(String s){
	return Long.parseLong(s);
    }
    /**
      byteに格納された値を「無符号の１バイト値」として intにする

      @param b バイト値
      @return 変換後の数値 ( 0 - 255 )
    */
    static public int b2i(byte b){
	int c = (int)b;
	c &= 0xff;
	return c;
    }
    /**
       intをbyteにする。

       与えられた intが256以上の場合は下位8bitが適用される。

       @param i	整数
       @return バイト
     */
    static public byte i2b(int i){
	return (byte)(i & 0xff);
    }
    /**
       long をbyteにする。

       与えられた longが256以上の場合は下位8bitが適用される。
       @param i	整数
       @return バイト
     */
    static public byte i2b(long i){
	return (byte)(i & 0xff);
    }

    /**
       バイト列を 0終端された UTF-8文字列とみなして Stringに変換する。
     */
    static public String bz2str(byte[] b){
	int len = 0;
	while(b[len] != 0 && len < b.length)
	    ++len;

	try{
	    return new String(b, 0, len, "UTF-8");
	}
	catch(UnsupportedEncodingException e){
	    ;
	}
	return null;
    }

    /**
       バイトを "Quoted Encoded" 文字列に変換する。

       @param b	入力バイト
       @return 文字列
     */
    static public String b2qstr(byte b){
	String hex;
	
	if(b < 0x20 || b == 0x27 || b >= 0x7f || b == 0x3d ||
	   b == 0x26 || b == 0x3c || b == 0x3e ||
	   b == 0x22 || b == 0x7b || b == 0x7d){
	    hex = "00" + Integer.toHexString(b);
	    hex = hex.substring(hex.length() - 2, hex.length());
	    hex = "=" + hex;
	}
	else
	    hex = String.valueOf((char) b);

	return hex;
    }
    /**
       バイト列を文字列にする
    */
    static public String b2str(byte[] b){
	try{
	    return new String(b, "ISO8859-1");
	}
	catch(Exception e){
	    ;
	}
	return null;//ここには到達しない
    }

    /**
       バイト列を "Quoted Encoded" 文字列にする

       @param b	入力バイト列
       @return 文字列
     */
    static public String b2qstr(byte[] b){
	return b2qstr(b, 0, true);
    }

    /**
       バイト列を "Quoted Encoded" 文字列にする

       @param b	入力バイト列
       @param ofst オフセット
       @return 文字列
     */
    static public String b2qstr(byte[] b, int ofst){
	return b2qstr(b, ofst, true);
    }


    /**
      byte値を "Quoted Encoded" 文字列としてchar配列に書き込む。

      @param cc char配列
      @param offset ccのオフセット（書き込み位置）
      @param b 書き込みたいバイト値
      @return ccのオフセット（次の書き込み位置）
    */
    static public int bWrite(char [] cc, int offset, byte b) {

	final char [] int2hexstr = {
	    0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37,
	    0x38, 0x39, 0x61, 0x62, 0x63, 0x64, 0x65, 0x66,
	};

	if(b < 0x20 || b == 0x27 || b >= 0x7f || b == 0x3d ||
	   b == 0x26 || b == 0x3c || b == 0x3e ||
	   b == 0x22 || b == 0x7b || b == 0x7d){

	    cc[offset++] = '=';
	    cc[offset++] = int2hexstr[0x0f & (b >> 4)];
	    cc[offset++] = int2hexstr[0x0f & b];
	}
	else {
	    cc[offset++] = (char)b;
	}

	return offset;
    }

    /**
      int 値を文字列に変換してchar配列に格納する。

      @param cc char配列
      @param offset ccのオフセット（書き込み位置）
      @param number 書き込みたい数値
      @return ccのオフセット（次の書き込み位置）
    */
    static public int nWrite(char [] cc, int offset, int number) {

        if (number == 0) {
           cc[offset++] = (char)0x30;
           return offset;
        }
        int max = 1000000000;
        for ( ; max >= 10; max /= 10) {

            int num = number / max;
            if (num > 0) {
                break;
            }
        }
        for ( ; max >= 1; max /= 10) {
            int num = number / max;
            number -= num * max;
            cc[offset++] = (char)(0x30 + num);
        }

        return offset;
    }

    /**
       バイト列を "Quoted Encoded" 文字列にする

       @param b	入力バイト列
       @param ofst オフセット
       @param abbrev <dl><dt>false</dt><dd>省略形式を使用しない</dd><dt>true</dt><dd>省略形式を使用する</dd></dl>
       @return 文字列
     */
    static public String b2qstr(byte[] b, int ofst, boolean abbrev){
//long before = 0;
//if (b.length > 2048) {
//   System.err.println(">>> UDF_Util.b2qstr() start : len = " + b.length);
//   before = System.currentTimeMillis();
//}
	String hex = "";
/*
	int i;
	int j;
	int cont;

	for(i = ofst ; i < b.length ; ++i){
	    cont = 0;
	    for(j = i ; j < b.length ; ++j){
		if(b[i] == b[j])
		    ++cont;
		else
		    break;
	    }
	    if(cont > 3 && abbrev){
		hex += (b2qstr(b[i]) + "{" + cont + "}");
		i = j - 1;
	    }
	    else
		hex += b2qstr(b[i]);
	}
*/
/**/
	int i;
	int j;
	int cont;
	int ccidx;

	char [] cc = new char[b.length * 3];
	ccidx = 0;

	for(i = ofst ; i < b.length ; ++i){
	    cont = 0;
	    for(j = i ; j < b.length ; ++j){
		if(b[i] == b[j])
		    ++cont;
		else
		    break;
	    }
	    if(cont > 3 && abbrev){
		ccidx = bWrite(cc, ccidx, b[i]);
		cc[ccidx++] = '{';
		ccidx = nWrite(cc, ccidx, cont);
		cc[ccidx++] = '}';
		i = j - 1;
	    }
	    else {
		ccidx = bWrite(cc, ccidx, b[i]);
	    }
	}
	hex = new String(cc, 0, ccidx);
/**/
//if (b.length > 2048) {
//   long after = System.currentTimeMillis();
//   double sec = (double)(after - before) / 1000.0;
//   System.err.println(">>> UDF_Util.b2qstr() time => " + sec + " second");
//}
	return hex;
    }

    /**
       文字列をバイト列にする
     */
    static public byte[] str2b(String str, int size)
    {
	try{
	    byte[] b = str.getBytes("ISO-8859-1");
	    byte[] bb = new byte[size];
	    if(b.length == size)
		return b;
	    else if(b.length > size){
		for(int i=0 ; i<size ; ++i)
		    bb[i] = b[i];
		return bb;
	    }
	    else if(b.length < size){
		for(int i=0 ; i<b.length ; ++i)
		    bb[i] = b[i];
		return bb;
	    }
	}
	catch(UnsupportedEncodingException e){
	    ;
	}
	return null;
    }
    /**
       "Quoted Encoded" 文字列をバイト列にする

       @param str 入力文字列
       @param size 出力バイトのサイズ
       @return バイト列
     */
    static public byte[] qstr2b(String str, int size){

	byte[]b1 = qstr2b(str);
	byte[]b2 = new byte[size];
	for(int i=0 ; i<b1.length && i<b2.length ; ++i)
	    b2[i] = b1[i];

	return b2;
    }

    /**
       "Quoted Encoded" 文字列をバイト列にする

       @param str 入力文字列
       @return バイト列
     */
    static public byte[] qstr2b(String str){
	char[] in = str.toCharArray();
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	int ii = 0;
	byte b = 0;

	for(int i=0 ; i<in.length ; ){
	    if(in[i] == '{'){
		int iii = i+1;
		while(in[iii] != '}'){
		    ++iii;
		}
		String s = new String(in, i+1, iii-i-1);
		int times = Integer.parseInt(s);
		while(times > 1){
		    out.write(b);
		    --times;
		}
		i = iii + 1;
	    }
	    else if(in[i] == '='){
		String s = new String(in, i+1, 2);
		b = (byte)Integer.parseInt(s, 16);
		i += 3;
		out.write(b);
	    }
	    else{
		b = (byte)in[i++];
		out.write(b);
	    }
	}
	return out.toByteArray();
    }

    /*
       "OSTA Compressed Unicode"を Stringにする

    static public String OSTACompressedUnicodeToString(byte[] b){
	int enc = b2i(b[0]);
	int i;
	int count = 0;
	char[] chars = new char[b.length];

	if(enc == 8 || enc == 254){
	    for(i = 1 ; i<b.length ; ++i, ++count)
		chars[count] = (char)b2i(b[i]);
	}
	else if(enc == 16 || enc == 255){
	    for(i = 1 ; i<b.length ; i += 2, ++count)
		chars[count] = (char)((b2i(b[i])<<8) + b2i(b[i+1]));
	}
	return new String(chars, 0, count);
    }
     */
    /**
       文字列を バイト配列に変換する。

       文字列は ISO-8859-1 でエンコードされているとみなされる。

       @param	s	文字列
       @return	バイト配列
     */
    static public byte[] stringToBytes(String s){
	try{
	    byte []b = s.getBytes("ISO-8859-1");
	    return b;
	}
	catch(UnsupportedEncodingException e){
	    e.printStackTrace();
	}
	return null;
    }
    /**
       バイト配列と文字列を比較する
       
       文字列を ISO-8859-1とみなしバイト配列に変換し、バイト比較する。
       文字列がバイト配列より短い場合は、バイト配列の残りが 0で埋められているときに限り一致するとみなす。

       バイト配列が文字列より短かい場合は不一致とみなす。

       @param b	バイト配列
       @param s	文字列
       @return	<dl><dt>false</dt><dd>一致しない</dd><dt>true</dt><dd>一致した</dd></dl>
     */
    static public boolean cmpBytesString(byte[] b, String s){
	byte chars[] = stringToBytes(s);

	if(b.length < chars.length)
	    return false;
	int i;
	for(i=0 ; i<chars.length ; ++i)
	    if(b[i] != chars[i])
		return false;

	for(; i<b.length ; ++i)
	    if(b[i] != 0)
		return false;

	return true;
    }

    /**
       オフセット位置からのバイト配列と文字列を比較する
       
       文字列を ISO-8859-1とみなしバイト配列に変換し、バイト比較する。
       文字列がバイト配列より短い場合は、バイト配列の残りが 0で埋められているときに限り一致するとみなす。

       バイト配列が文字列より短かい場合は不一致とみなす。

       @param b	バイト配列
       @param off オフセット位置
       @param s	文字列
       @return	<dl><dt>false</dt><dd>一致しない</dd><dt>true</dt><dd>一致した</dd></dl>
     */
    static public boolean cmpBytesString(byte[] b, int off, String s){
	byte chars[] = stringToBytes(s);

	if(b.length - off < chars.length)
	    return false;
	int i;
	for(i=0 ; i<chars.length ; ++i)
	    if(b[i + off] != chars[i])
		return false;

	for(; i<b.length-off ; ++i)
	    if(b[i + off] != 0)
		return false;

	return true;
    }
    
    /**
       バイト配列とバイト配列を比較する。
       
       @param b1 比較バイト配列
       @param b2 比較バイト配列
       @return	<dl><dt>false</dt><dd>一致しない</dd><dt>true</dt><dd>一致した</dd></dl>
    */
    static public boolean cmpBytesBytes(byte[] b1, byte[] b2){
	
	if(b1.length != b2.length)
	    return false;
	for(int i = 0; i < b1.length; i++){
	    
	    if(b1[i] != b2[i])
		return false;
	}
	return true;
    }


    /**
       バイト配列の一部分を新たなバイト配列として返す。

       @param b	入力バイト配列
       @param off 開始位置
       @param len 長さ
       @return サブバイト列
     */
    static public byte[] subByte(byte[] b, int off, int len){
	byte[] new_b = new byte[len];
	for(int i=0 ; i<len ; ++i, ++off){
	    new_b[i] = b[off];
	}
	return new_b;
    }
    /**
       posより後にあるalign境界位置を求める

       @param pos	位置
       @param align	境界位置

       境界位置とは

       <pre>
       pos + (align - (pos % align));
       </pre>

       で求められる値である。
    */
    static public int align(int pos, int align){
	if((pos % align) != 0)
	    return pos + (align - (pos % align));
	return pos;
    }
    /**
       posより後にあるalign境界位置を求める

       @param pos	位置
       @param align	境界位置

       境界位置とは

       <pre>
       pos + (align - (pos % align));
       </pre>

       で求められる値である。
    */
    static public long align(long pos, int align){
	if((pos % align) != 0)
	    return pos + (align - (pos % align));
	return pos;
    }
    /**
       posより後にあるalign境界位置を求める

       @param pos	位置
       @param align	境界位置

       境界位置とは

       <pre>
       pos + (align - (pos % align));
       </pre>

       で求められる値である。
    */
    static public long align(long pos, long align){
	if((pos % align) != 0)
	    return pos + (align - (pos % align));
	return pos;
    }

    /**
       バイト配列を表示するデバッグ用メソッド

       @param	b	バイト列
     */
    static public void dumpBytes(byte[] b){
	for(int i=0 ; i<b.length ; ++i){

	    if(i % 16 == 0)
		System.err.println("");

	    String hex = "0" + Integer.toHexString(b[i]);
	    hex = hex.substring(hex.length() - 2);
	    System.err.print(hex +" ");
	}
	System.err.println("\n");
    }

    /**
       バイト配列を表示するデバッグ用メソッド

       @param	b	バイト列
     */
    static public String dumpBytesToString(byte[] b){
	StringBuffer sb = new StringBuffer();
	for(int i=0 ; i<b.length ; ++i){

	    if(i % 16 == 0)
		sb.append('\n');

	    String hex = "0" + Integer.toHexString(b[i]);
	    hex = hex.substring(hex.length() - 2);
	    sb.append(hex +" ");
	}
	return sb.toString();
    }
    
    /**
       バイト配列の内容が全て0であるかどうかをチェックする。
       
       @param b  バイト配列。
       @return	<dl><dt>false</dt><dd>全て0でない</dd><dt>true</dt><dd>全て0</dd></dl>
    */
    static public boolean isAllZero(byte b[]){
        for(int i=0 ; i<b.length ; ++i)
            if(b[i] != 0)
                return false;

        return true;
    }
    
    /**
       バイト配列の指定された領域の内容が全て0であるかどうかをチェックする。
       
       @param b  バイト配列。
       @param offset  チェックを開始するオフセット。
       @param len     チェックする長さ。
       @return	<dl><dt>false</dt><dd>全て0でない</dd><dt>true</dt><dd>全て0</dd></dl>
    */
    static public boolean isAllZero(byte b[], int offset, int len){
	if(b.length < offset + len)
	    len = b.length - offset;
        for(int i=offset ; i<offset+len ; ++i)
            if(b[i] != 0)
                return false;

        return true;
    }

    /**
      バイト配列の指定の位置から４バイトをUDFで規定された 32ビット値として読み取る。

      @param b  バイト配列。
      @param offset バイト配列上のインデックス。
      @return 読み取った結果。
    */
    static public long b2uint32(byte b[], int offset){

	long tmp;
	//if(getEndian() == LITTLE_ENDIAN)
	tmp = (b2i(b[offset + 3]) << 24) + (b2i(b[offset + 2]) << 16) + (b2i(b[offset + 1]) << 8) + b2i(b[offset + 0]);
	//else
	//tmp = (b2i(b[offset + 0]) << 24) + (b2i(b[offset + 1]) << 16) + (b2i(b[offset + 2]) << 8) + b2i(b[offset + 3]);
	return tmp;
    }
    
    /**
      バイト配列の指定の位置から２バイトをUDFで規定された 16ビット値として読み取る。

      @param b  バイト配列。
      @param offset バイト配列上のインデックス。
      @return 読み取った結果 ( 0 - 65535 )。
    */
    static public int b2uint16(byte b[], int offset){

	int tmp;
	//if(getEndian() == LITTLE_ENDIAN)
	tmp = (b2i(b[offset + 1]) << 8) + b2i(b[offset + 0]);
	//else
	//tmp = (b2i(b[offset + 0]) << 8) + b2i(b[offset + 1]);
	return tmp;
    }

    //======================================================================
    // XML Section
    //======================================================================

    /**
       指定されたノードの子供に
       &lt;a&gt;123&lt;/a&gt;
       	のようにTextノードを追加する。
       既にノードの中に子ノードがある場合は全て削除してから追加する。

       @param	node	ノード
       @param	str	文字列
     */
    static public void setNodeVal(Node node, String str){
	while(node.hasChildNodes())
	    node.removeChild(node.getFirstChild());

	Text t = node.getOwnerDocument().createTextNode(str);
	node.appendChild(t);
    }
    /**
       指定されたノードの子供に
       &lt;a&gt;123&lt;/a&gt;
       	のようにTextノードを追加する。
       既にノードの中に子ノードがある場合は全て削除してから追加する。

       @param	node	ノード
       @param	val	値
     */
    static public void setNodeVal(Node node, short val){
	setNodeVal(node, String.valueOf(val));
    }

    /**
       指定されたノードの子供に
       &lt;a&gt;123&lt;/a&gt;
       のようにTextノードを追加する。
       既にノードの中に子ノードがある場合は全て削除してから追加する。

       @param	node	ノード
       @param	val	値
     */
    static public void setNodeVal(Node node, int val){
	setNodeVal(node, String.valueOf(val));
    }

    /**
       指定したノードの子供に
       &lt;a&gt;123&lt;/a&gt;
       のように記述されたTextノードが存在すればそれを文字列として返す。
       無い場合は nullを返す

       @param node
       @return 文字列
     */
    static public String getNodeVal(Node node){
	NodeList nl = node.getChildNodes();
	for(int i=0 ; i<nl.getLength() ; ++i){
	    if(nl.item(i).getNodeType() == Node.TEXT_NODE){
		if(nl != null){
		    String v = ((Text)(nl.item(0))).getData();
		    return v;
		}
	    }
	}
	return null;
    }

    /**
       XMLの Document Nodeを生成する。

       @return XML Document Node。
     */
    public static Document genDocument(){
        return UDF_XMLUtil.genDocument();
    }

    /**
      Systemの改行コードを取得する。

      @return 改行
    */
    public static String getSystemNewLine( ) {
        return System.getProperties().getProperty("line.separator");
    }

    /**
      改行コードをシステムのものに置き換えます。

      @param base 元の文字列
      @return 修正後の文字列
    */
    public static String toSystemNewLine(String base) {

        String nl = getSystemNewLine();

        if (base == null) {
            return null;
        }

        int length = base.length();

        int p = base.indexOf("\r");
        if (p < 0) {

            p = base.indexOf("\n");
            if (p < 0) {
                return base;
            }
            String head = (p == 0) ? nl: (base.substring(0, p) + nl);

            p++;
            if (length == p) {
                return head;
            }
            return (head + toSystemNewLine(base.substring(p)));
        }

        String head = (p == 0) ? nl: (base.substring(0, p) + nl);

        p++;
        if (length == p) {
            return head;
        }

        if (base.charAt(p) == 0x000a) {
            p++;
        }
        return (head + toSystemNewLine(base.substring(p)));
    }

    /**
       文字 cを n個繰り返した文字列を生成する

       @param c	文字
       @param n 個数
       @return 文字列
     */
    static public String repeat(char c, int n){
	char[] tmp = new char[n];
	for(int i=0 ; i<n ; ++i){
	    tmp[i] = c;
	}
	return new String(tmp);
    }

    /**
       pathnameから directory nameを取得する。

       @param pathname
       @return directory name
例)
<pre>/a/b/c => /a/b
/a/b/  =>  /a/b
a/b	=> a
a/	=> a
a	=> ""</pre>

     */
    static public String dirname(String pathname){
	int pos = pathname.lastIndexOf('/');
	if(pos >= 0)
	    return pathname.substring(0, pos);
	return "";
    }
    /**
       pathnameから base nameを取得する。

       @param pathname
       @return directory name
例)
<pre>/a/b/c => c
/a/b/  =>  ""
a/b	=> b
a/	=> ""
a	=> a</pre>
     */
    static public String fidname(String pathname){
	int pos = pathname.lastIndexOf('/');
	if(pos >= 0)
	    return pathname.substring(pos + 1);
	return "";
    }

    /**
       文字cより前の部分の文字列を取得する

       文字cが無い場合は文字列そのもを返す
     */
    static public String car(String str, char c){
	int pos = str.indexOf(c);
	if(pos < 0)
	    return str;

	return str.substring(0, pos);
    }

    /**
       文字cより後の部分の文字列を取得する

       文字cが無い場合は空文字列を返す
     */
    static public String cdr(String str, char c){
	int pos = str.indexOf(c);
	if(pos < 0)
	    return "";

	return str.substring(pos + 1);
    }
}
