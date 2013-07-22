/*
 */
package com.udfv.access;

import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.exception.*;
import java.io.*;

/**
   何もしないアクセサ。

   readをするとIOExceptionをスローする
   writeをしてもデータは書き変わらない。
   seekはできる。

   UDF_RandomAccessFileがないと動かないメソッドにダミーで渡してやることで、ファイルを作らずに動作をさせることができる。
 */
public class UDF_RandomAccessZero extends UDF_RandomAccess
{
    long my_off;
    long my_pos;
    long my_length;
    /**
       コンストラクタ
    */
    public UDF_RandomAccessZero(long length){
	my_length = length;
    }
    public void close() throws IOException{
	;
    }

    public int read(byte[] b, int off, int len) throws IOException{
	throw new IOException("cannot read");
    }

    public int write(byte[] b, int off, int len){
	my_pos += len;
	if(my_pos > my_length)
	    my_length = my_pos;

	return len;
    }

    public long seek(long off, int whence) throws IOException{
	switch(whence){
	case SEEK_SET:
	    my_pos = off;
	    break;
	case SEEK_CUR:
	    my_pos += off;
	    break;
	case SEEK_END:
	    my_pos = my_length + off;
	    break;
	}
	if(my_pos > my_length)
	    my_length = my_pos;

	return my_pos;
    }

    public long getPointer() throws IOException{
	return my_pos;
    }

    public long getAbsPointer() throws IOException{
	return my_pos;
    }

    public long getPartPointer() throws IOException{
	return my_pos;
    }

    public int getPartRefNo(){
	return -1;
    }
    /*
    public boolean isMirror(){
	return false;
    }
    */
    public int getPartSubno(){
	return 0;
    }

    public long length() throws IOException{
	return my_length;
    }

    public boolean eof() throws IOException{
	return my_length >= length();
    }

}
