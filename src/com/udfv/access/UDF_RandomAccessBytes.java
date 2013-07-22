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
import com.udfv.ecma167.UDF_bytes;
import java.io.*;

/**
   UDF_bytesにアクセスするためのクラス。

<p>	
   UDF_RandomAccessBufferと違い、生成元の partnoや位置を取得できるので、このアクセサを利用して readFromをした場合、readされた UDF Elementの partnoや位置が正しく設定される。
</p>	
<p>
注意
</p>
UDF_RandomAccessBytesにデータを書きこんでも、参照元の UDF_bytesのデータは書きかわらない。
参照元の UDF_bytesを書きかえるには getBuffer()した結果を setData()すること。
 */
public class UDF_RandomAccessBytes extends UDF_RandomAccessBuffer
{
    //絶対位置
    long my_absoff;
    long my_off;
    int my_partno;
    int my_subno;

    /**
       コンストラクタ

	UDF_bytesを指定して生成する。

	@param b	UDF_bytes
     */
    public UDF_RandomAccessBytes(UDF_bytes b){
	super(b.getData());
	setEnv(b.env);
	my_off = b.getPartMapOffset();
	my_partno = b.getElemPartRefNo();
	my_subno = b.getPartSubno();
	
	init();
    }

    /**
       コンストラクタ

       初期データおよびイメージファイル内でのポジションおよびパーティション番号を指定して生成する。

	@param data	データ
	@param off	イメージファイル内でのオフセット位置
	@param partno	パーティション番号
	@deprecated
     */
    public UDF_RandomAccessBytes(byte[] data, long off, int partno){
	super(data);
	my_off = off;
	my_partno = partno;
	init();
    }

    /**
       コンストラクタ

       初期データおよびイメージファイル内でのポジション、パーティション番号およびミラー情報を指定して生成する。

	@param env	UDF環境
	@param data	データ
	@param off	イメージファイル内でのオフセット位置
	@param partno	パーティション番号
	@param mirror	ミラー情報
     */
    public UDF_RandomAccessBytes(UDF_Env env, byte[] data, long off, int partno, boolean mirror){
	super(data);
	my_off = off;
	my_partno = partno;
	my_subno = mirror ? 1 : 0;
	this.env = env;
	init();
    }

    /**
       コンストラクタ

       初期データおよびイメージファイル内でのポジション、パーティション番号およびミラー情報を指定して生成する。

	@param env	UDF環境
	@param data	データ
	@param off	イメージファイル内でのオフセット位置
	@param partno	パーティション番号
	@param subno	副パーティション番号

	@see <a href="subno.html">副パーティション番号</a>
     */
    public UDF_RandomAccessBytes(UDF_Env env, byte[] data, long off, int partno, int subno){
	super(data);
	my_off = off;
	my_partno = partno;
	my_subno = subno;
	this.env = env;
	init();
    }

    private void init(){
	try{
	    long saved_pos = getPointer();
	    UDF_RandomAccess f = getRandomAccess();
	    long saved_pos2 = f.getPointer();
	    //ここで巻きもどるバグがあった。
	    f.seek(0);
	    my_absoff = f.getAbsPointer();
	    f.seek(saved_pos2);
	    seek(saved_pos);
	}
	catch(IOException e){
	    ;
	}
	catch(NullPointerException e){
	    System.err.println("bad partition number:" + my_partno + " subno=" + my_subno);
	    throw new NullPointerException();
	}
    }

    private UDF_RandomAccess getRandomAccess(){
	UDF_RandomAccess f = null;
	try{
	    if(my_partno == -1)
		f = env.f;
	    else
		f = env.getPartMapRandomAccess(my_partno, my_subno);

	    if(f == null)
		throw new NullPointerException();
	}
	catch(NullPointerException e){
	    e.printStackTrace();
	    System.err.println("bad partition number:" + my_partno + " subno=" + my_subno);
	}
	catch(UDF_PartMapException e){
	    e.printStackTrace();
	    System.err.println("bad partition number:" + my_partno + " subno=" + my_subno);
	}

	return f;
    }
    /**
       絶対位置の取得。
       絶対位置とはイメージファイル(一番大元の)どの位置かということである。

       @return ポジション
     */
    public long getAbsPointer() throws IOException{
	return my_off + getPointer() + my_absoff;
    }
    /**
       パーティション位置の取得。<br/>
       パーティション位置とはパーティション内のどの位置かということである。

       @return ポジション
     */
    public long getPartPointer() throws IOException{
	return my_off + getPointer();
    }
    /**
       このアクセサの現在のファイルポジションのパーティション番号を取得する。

       @return パーティション番号
     */
    public int getPartRefNo(){
	return my_partno;
    }
    /*
       このアクセサがミラーを現わすものか否かを取得する。

       @return <dl><dt>false</dt><dd>ミラーでない</dd><dt>true</dt><dd>ミラー</dd></dl>
    public boolean isMirror(){
	return my_mirror;
    }
     */

    public int getPartSubno(){
	return my_subno;
    }

}
