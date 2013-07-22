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
