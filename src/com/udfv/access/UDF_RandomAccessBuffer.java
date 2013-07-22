/*
 */
package com.udfv.access;

import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.exception.*;
import java.io.*;

/**
   バッファにアクセスするためのクラス。
   <p>
   バッファはパーティション情報やミラー情報を持たないので、一部のメソッドを実行するとExceptionをスローする。このため UDF_RandomAccessBytesの利用が強く推奨される。
   </p>
 */
public class UDF_RandomAccessBuffer extends UDF_RandomAccess
{
    private int pos = 0;
    private byte[] buf;
    private int buf_size;

    /**
       コンストラクタ

       @param	size	バッファのサイズ
       
       サイズを指定して生成する。
     */
    public UDF_RandomAccessBuffer(int size){
	buf = new byte[size];
	buf_size = size;
    }
    /**
       コンストラクタ

       初期データを指定して生成する。バッファのサイズは引数のデータのサイズと同じになる。

       @param	b	初期データとして格納するデータ
     */
    public UDF_RandomAccessBuffer(byte[] b){
	buf = new byte[b.length];
	buf_size = b.length;
	for(int i=0 ; i<b.length ; ++i)
	    buf[i] = b[i];
    }

    public void close(){
	;
    }

    public boolean eof(){
	if(buf_size == pos)
	    return true;
	return false;
    }

    public long length(){
	return (long)buf_size;
    }

    public void resize(int size){
	byte[]  new_buf = new byte[size];
	for(int i=0 ; i<size && i<buf_size ; ++i)
	    new_buf[i] = buf[i];
	
	buf = new_buf;
	buf_size = size;
    }
    public int read(byte[] b, int off, int len){
	int i = 0;
	int ii = off;
	for(i=0 ; i<len && pos<buf_size ; ++i, ++ii, ++pos)
	    b[ii] = buf[pos];

	return i;
    }

    /**
       データを書く。バッファのサイズを越えた場合は、バッファのサイズを自動で拡張する。

       @param b	書きこむデータを格納するバッファ
       @param off 書きこむバッファのオフセット
       @param len 書きこむデータの長さ

       @return 実際に書きこまれたバイト数
     */
    public int write(byte[] b, int off, int len){
	int i = 0;
	int ii = off;

	if(pos + len > buf_size)
	    resize(pos + len);

	for(i=0 ; i<len && pos<buf_size ; ++i, ++ii, ++pos)
	    buf[pos] = b[ii];

	return i;
    }

    public long seek(long off, int whence){
	switch(whence){
	case SEEK_SET:
	    pos = (int)off;
	    break;
	case SEEK_CUR:
	    pos += (int)off;
	    break;
	case SEEK_END:
	    pos = (int)(buf_size + off);
	    break;
	}
	return (long)pos;
    }

    public long getPointer(){
	return (long)pos;
    }

    /**
       このメソッドは UDF_RandomAccessBuffer()では使用できない。
       必ずIOExceptionをスローする。
     */
    public long getAbsPointer() throws IOException{
	try{
	    throw new IOException();
	}
	catch(IOException e){
	    System.err.println("PANIC");
	    e.printStackTrace();
	}
	return 0;
    }
    /**
       このメソッドは UDF_RandomAccessBuffer()では使用できない。
       必ずIOExceptionをスローする。
     */
    public long getPartPointer() throws IOException{
	try{
	    throw new IOException();
	}
	catch(IOException e){
	    System.err.println("PANIC");
	    e.printStackTrace();
	    System.exit(0);
	}
	return 0;
    }
    /**
       このメソッドは UDF_RandomAccessBuffer()では使用できない。
       必ずIOExceptionをスローする。
     */
    public int getPartRefNo() throws IOException{
	try{
	    throw new IOException();
	}
	catch(IOException e){
	    System.err.println("PANIC");
	    e.printStackTrace();
	    System.exit(0);
	}
	return 0;
    }
    /*
       このメソッドは UDF_RandomAccessBuffer()では使用できない。
       必ずIOExceptionをスローする。
    public boolean isMirror() throws IOException{
	try{
	    throw new IOException();
	}
	catch(IOException e){
	    System.err.println("PANIC");
	    e.printStackTrace();
	    System.exit(0);
	}
	return false;
    }
     */
    /**
       このメソッドは UDF_RandomAccessBuffer()では使用できない。
       必ずIOExceptionをスローする。
     */
    public int getPartSubno() throws IOException{
	try{
	    throw new IOException();
	}
	catch(IOException e){
	    System.err.println("PANIC");
	    e.printStackTrace();
	    System.exit(0);
	}
	return 0;
    }

    /**
       データバッファを取得する。

       @return バッファ
     */
    public byte[] getBuffer(){
	return buf;
    }
}
