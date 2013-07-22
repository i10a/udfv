/*
 */
package com.udfv.access;

import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.exception.*;
import java.io.*;

/**
   OS上のファイル(イメージファイル)にアクセスするためのクラス
 */
public class UDF_RandomAccessFile extends UDF_RandomAccess
{
    private RandomAccessFile raf;

    /**
       コンストラクタ

       @param name イメージファイル名
    */
    public UDF_RandomAccessFile(String name) throws FileNotFoundException, IOException{
	try{
	    raf = new RandomAccessFile(name, "rw");
	}
	catch(FileNotFoundException e){
	    raf = new RandomAccessFile(name, "r");
	}
    }

    /**
       コンストラクタ

       @param name イメージファイル名
    */
    public UDF_RandomAccessFile(String name, String mode) throws FileNotFoundException, IOException{
	raf = new RandomAccessFile(name, mode);
    }

    /**
       コンストラクタ

       @param file イメージファイル
    */
    public UDF_RandomAccessFile(File file, String mode) throws FileNotFoundException, IOException{
	raf = new RandomAccessFile(file.getPath(), mode);
    }

    public void close() throws IOException{
	raf.close();
    }

    public int read(byte[] b, int off, int len) throws IOException, UDF_EOFException{
	int ret = raf.read(b, off, len);
	if(ret < 0)
	    throw new UDF_EOFException(null, "EOF:" + getPointer());
	return ret;
    }

    public int write(byte[] b, int off, int len) throws IOException{
	long cur_pos = raf.getFilePointer();
	raf.write(b, off, len);
	long new_pos = raf.getFilePointer();
	return (int)(new_pos - cur_pos);
    }

    public long seek(long off, int whence) throws IOException{
	switch(whence){
	case SEEK_SET:
	    raf.seek(off);
	    break;
	case SEEK_CUR:
	    raf.seek(raf.getFilePointer() + off);
	    break;
	case SEEK_END:
	    raf.seek(raf.length() + off);
	    break;
	}
	return raf.getFilePointer();
    }

    public long getPointer() throws IOException{
	return raf.getFilePointer();
    }

    public long getAbsPointer() throws IOException{
	return raf.getFilePointer();
    }

    public long getPartPointer() throws IOException{
	return raf.getFilePointer();
    }

    /**
       このアクセサの現在のファイルポジションのパーティション番号を取得する。

       UDF_RandomAccessFileのパーティション番号は -1である。

       @return パーティション番号(常に-1)
     */
    public int getPartRefNo(){
	return -1;
    }

    /*
       このアクセサの現在のミラーフラグを取得する。

       UDF_RandomAccessFileのミラーフラグは常に falseである。

       @return 常にfalse
    public boolean isMirror(){
	return false;
    }
     */
    public int getPartSubno(){
	return 0;
    }

    public long length() throws IOException{
	return raf.length();
    }

    public boolean eof() throws IOException{
	return raf.getFilePointer() >= length();
    }

}
