/*
 */
package com.udfv.access;

import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.exception.*;
import java.io.*;

/**
   UDF_RandomAccessはファイルやデータにアクセスするクラスの基底となるクラスである。
   UDF_RandomAccessは抽象クラスなのでインスタンス生成できない。
 */
abstract public class UDF_RandomAccess extends com.udfv.core.UDF_Base
{
    private int b2i(byte b){
	int c = (int)b;
	c &= 0xff;
	return c;
    }
    private long b2l(byte b){
	long c = (int)b;
	c &= 0xff;
	return c;
    }

    /**
       データを読む

       @param b	読みこまれたデータを格納するバッファ
       @param off 格納するバッファのオフセット
       @param len 読みこむデータの長さ

       @return 実際に読みこまれたバイト数
     */
    abstract public int read(byte[] b, int off, int len) throws IOException, UDF_EOFException;
    /**
       データを書く

       @param b	書きこむデータを格納するバッファ
       @param off 書きこむバッファのオフセット
       @param len 書きこむデータの長さ

       @return 実際に書きこまれたバイト数
     */
    abstract public int write(byte[] b, int off, int len) throws IOException, UDF_EOFException;
    /**
       read/writeポジションを変更する

       @param off	オフセット
       @param whence	位置(SEEK_SET: 先頭から, SEEK_CUR: 現在位置から, SEEK_END: 最終位置から)

       @return 実際にシークされた位置
     */
    abstract public long seek(long off, int whence) throws IOException;
    /**
       現在のポジションの取得

       @return ポジション
     */
    abstract public long getPointer() throws IOException;

    /**
       絶対位置の取得。
       絶対位置とはイメージファイル(一番大元の)どの位置かということである。

       @return ポジション
     */
    abstract public long getAbsPointer() throws IOException;

    /**
       パーティション位置の取得。
       パーティション位置とはパーティション内のどの位置かということである。

       @return ポジション
     */
    abstract public long getPartPointer() throws IOException;

    /**
       close処理をする。
     */
    abstract public void close() throws IOException;

    /**
       ポジションが最後にあるかどうかをチェックする。

       @return <dl>
		<dt>false</dt><dd>最後でない</dd>
		<dt>true</dt><dd>最後である</dd>
		</dl>
     */
    abstract public boolean eof() throws IOException;


    /**
       現在位置から最終位置までの長さ。0なら最後である。

       @return 長さ
     */
    final public long rest() throws IOException{
	return length() - getPointer();
    }

    /**
       このアクセサがオープンしている対象のサイズを取得する
     */
    abstract public long length() throws IOException;

    /**
       シークする際のポジション位置指定
     */
    final static public int SEEK_SET = 0;
    /**
       シークする際のポジション位置指定
     */
    final static public int SEEK_CUR = 1;
    /**
       シークする際のポジション位置指定
     */
    final static public int SEEK_END = 2;

    /**
       指定したポジション位置にシークする。このメソッドは
	<pre>
	seek(off, SEEK_SET);
	</pre>
	と同等の動作をする。

       @param off ポジション
       @return 実際にシークしたポジション
     */
    final public long seek(long off) throws IOException{
	return seek(off, SEEK_SET);
    }

    /**
       secのセクタにシークする。
       このメソッドは
	<pre>
	seek(sec * env.LBS, SEEK_SET);
	</pre>
	と同等の動作をする。

       @param sec セクタ
       @return 実際にシークしたポジション
     */
    final public long seekSec(int sec) throws IOException{
	return seek((long)sec * UDF_Env.LBS, SEEK_SET);
    }

    /**
       現在のファイルポジション位置からデータを読み指定したバッファに格納する。
       読み出したバイト数分ポジションが進む。
       このメソッドは
	<pre>
	read(b, 0, b.length)
	</pre>
	と同等の動作をする。

       @param b		バッファ
       @return		読み出したバイト数
       @throws UDF_EOFException	もうこれ以上データが読めない
     */
    final public int read(byte[] b) throws IOException, UDF_EOFException{
	return read(b, 0, b.length);
    }

    /**
       現在のファイルポジション位置にバッファのデータを書く。
       書き出したバイト数分ポジションが進む。
       このメソッドは
	<pre>
	write(b, 0, b.length)
	</pre>
	と同等の動作をする。

       @param b		バッファ
       @return		実際に書き出したバイト数
       @throws UDF_EOFException	もうこれ以上データが書けない。
     */
    final public int write(byte[] b) throws IOException, UDF_EOFException{
	return write(b, 0, b.length);
    }

    /**
       現在のファイルポジション位置に指定のバイト値を指定の長さだけ書き込む。
       書き出したバイト数分ポジションが進む。

       @param b 書き込む値
       @return 実際に書き出したバイト数

       @throws UDF_EOFException	もうこれ以上データが書けない。
    */
    final public long writeBytes(byte b, long len) throws IOException, UDF_EOFException {

        int plen = 16 * 2048;

        byte [] bb = new byte[plen];

        for (int i = 0; i < plen; i++) {
            bb[i] = b;
        }

        long rest = len;
        while(rest > 0) {
            int blen = plen > rest ? (int)rest: plen;

            int wlen = write(bb, 0, blen);
            rest -= wlen;
        }

        return len;
    }

    /**
       現在のファイルポジション位置から UDFで規定された uint16型のデータを
       読む。1バイトだけファイルポジションが進む。

       @return		データ
       @throws UDF_EOFException	もうこれ以上データが読めない
     */
    final public int readUint8() throws IOException, UDF_EOFException{
	byte[] b;

	b = new byte[1];
	if(read(b, 0, 1) != 1)
	    throw new UDF_EOFException(null, "EOF");

	return (int)b[0];
    }
    /**
       現在のファイルポジション位置から UDFで規定された uint16型のデータを
       読む。2バイトだけファイルポジションが進む。

       @return		データ
       @throws UDF_EOFException	もうこれ以上データが読めない
     */
    final public int readUint16() throws IOException, UDF_EOFException{
	byte[] b;

	b = new byte[2];
	if(read(b, 0, 2) != 2)
	    throw new UDF_EOFException(null, "EOF");

	return (int)(b2i(b[0]) + (b2i(b[1])<<8));
    }
    /**
       現在のファイルポジション位置から UDFで規定された uint32型のデータを
       読む。4バイトだけファイルポジションが進む。

       @return		データ
       @throws UDF_EOFException	もうこれ以上データが読めない
     */
    final public long readUint32() throws IOException, UDF_EOFException{
	byte[] b;

	b = new byte[4];
	if(read(b, 0, 4) != 4)
	    throw new UDF_EOFException(null, "EOF");

	return (long)(b2i(b[0]) + (b2i(b[1])<<8) + (b2i(b[2])<<16) + (b2l(b[3])<<24));
    }
    /**
       現在のファイルポジション位置から UDFで規定された uint64型のデータを
       読む。8バイトだけファイルポジションが進む。

       @return		データ
       @throws UDF_EOFException	もうこれ以上データが読めない
     */
    final public long readUint64() throws IOException, UDF_EOFException{
	byte[] b;

	b = new byte[8];
	if(read(b, 0, 8) != 8)
	    throw new UDF_EOFException(null, "EOF");
	long x = (long)(b2i(b[0]) + (b2i(b[1])<<8) + (b2i(b[2])<<16) + (b2l(b[3])<<24));
	long y = (long)(b2i(b[4]) + (b2i(b[5])<<8) + (b2i(b[6])<<16) + (b2l(b[7])<<24));

	return x + (y<<32);
    }

    /**
       現在のファイルポジション位置から UDFで規定された uint8型のデータとして
       データを書く。1バイトだけファイルポジションが進む。

       @param x	データ
       @return		実際に書き出したバイト数(通常1)
       @throws UDF_EOFException	もうこれ以上データが書けない
     */
    final public int writeUint8(int x) throws IOException, UDF_EOFException{
	byte[] b = new byte[1];
	b[0] = UDF_Util.i2b(x);

	if(write(b, 0, 1) != 1)
	    throw new UDF_EOFException(null, "EOF");

	return 1;
    }
    /**
       現在のファイルポジション位置から UDFで規定された uint16型のデータとして
       データを書く。2バイトだけファイルポジションが進む。

       @param x	データ
       @return		実際に書き出したバイト数(通常2)
       @throws UDF_EOFException	もうこれ以上データが書けない
     */
    final public int writeUint16(int x) throws IOException, UDF_EOFException{
	byte[] b;

	b = new byte[2];
	b[0] = UDF_Util.i2b(x);
	b[1] = UDF_Util.i2b(x>>8);
	if(write(b, 0, 2) != 2)
	    throw new UDF_EOFException(null, "EOF");

	return 2;
    }
    /**
       現在のファイルポジション位置から UDFで規定された uint32型のデータとして
       データを書く。4バイトだけファイルポジションが進む。

       @param x	データ
       @return		実際に書き出したバイト数(通常4)
       @throws UDF_EOFException	もうこれ以上データが書けない
     */
    final public long writeUint32(long x) throws IOException, UDF_EOFException{
	byte[] b;

	b = new byte[4];
	b[0] = UDF_Util.i2b(x);
	b[1] = UDF_Util.i2b(x>>8);
	b[2] = UDF_Util.i2b(x>>16);
	b[3] = UDF_Util.i2b(x>>24);

	if(write(b, 0, 4) != 4)
	    throw new UDF_EOFException(null, "EOF");

	return 4;
    }
    /**
       現在のファイルポジション位置から UDFで規定された uint64型のデータとして
       データを書く。8バイトだけファイルポジションが進む。

       @param x	データ
       @return		実際に書き出したバイト数(通常8)
       @throws UDF_EOFException	もうこれ以上データが書けない
     */
    final public long writeUint64(long x) throws IOException, UDF_EOFException{
	byte[] b;

	b = new byte[8];
	b[0] = UDF_Util.i2b(x);
	b[1] = UDF_Util.i2b(x>>8);
	b[2] = UDF_Util.i2b(x>>16);
	b[3] = UDF_Util.i2b(x>>24);
	b[4] = UDF_Util.i2b(x>>32);
	b[5] = UDF_Util.i2b(x>>40);
	b[6] = UDF_Util.i2b(x>>48);
	b[7] = UDF_Util.i2b(x>>56);

	if(write(b, 0, 8) != 8)
	    throw new UDF_EOFException(null, "EOF");
	return 8;
    }

    /**
       このアクセサの現在のファイルポジションのパーティションリファレンス番号を取得する。

       @return	パーティション番号
     */
    abstract public int getPartRefNo() throws IOException;

    /**
       このアクセサがミラーを現わすものか否かを取得する。

       @return	<dl>
		<dt>false</dt><dd>mirrorではない</dd>
		<dt>true</dt><dd>mirrorである</dd>
		</dl>

       @deprecated replaced by {@link #getPartSubno()}
		
     */
    public boolean isMirror() throws IOException{
	return getPartSubno() == 0 ? false : true;
    }

    /**
       このアクセサの副番号を取得する。

       @return 副番号
     */
    abstract public int getPartSubno() throws IOException;
}
