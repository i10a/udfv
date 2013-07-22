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

import java.io.*;
import java.util.*;
import com.udfv.exception.*;
import com.udfv.core.*;
import com.udfv.util.*;

//////
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;

/**
   UDF_RandomAccessExtentは分断された extentをさも1つの連続したデータ
   のようにアクセスをするアクセサである。
<p>
   extentは内部的に UDF_ExtentElemで保存している。
</p>
<p>
   UDF_ExtentElemは基本的に
</p>
<ul>
<li>   loc</li>
<li>   partno</li>
<li>   len</li>
</ul>
<p>
の3パラメータからなる。 partが -1の場合はパーティションの外(すなわちVolume領域を差す)、それ以外の場合は各パーティションを差す。
</p>
<p>
UDF_ExtentElemのその他のパラメータはシステム管理用のパラメータである。
外部から修正してはならない。
</p>
<p>
extentが複数ある場合は複数のパーティションにまたがる可能性があるので、UDF_RandomAccessExtentはイメージにアクセスする際、UDF_Envのパラメータ、UDF_Env.part_eおよび UDF_Env.fを参考にする。<br/>
したがって、UDF_Envにはその時点でアクセスしうるすべてのパーティションが存在していなければならない。
</p>
*/
public class UDF_RandomAccessExtent extends UDF_RandomAccess
{
    UDF_Extent my_refext;
    long my_pos;

    public UDF_RandomAccessExtent(UDF_Extent ext){
	my_refext = ext;
	my_pos = 0;
	setEnv(ext.env);
    }

    public long length(){
	/*
	long size = 0;
	UDF_ExtentElem[] my_extents = my_refext.getExtent();
	for(int i=0 ; i<my_extents.length ; ++i){
	    size += my_extents[i].len;
	}
	return size;
	*/
	return my_refext.getLongSize();
    }
    public boolean eof(){
	return getPointer() == (length());
    }
    public void close(){
	return;
    }
    public long getPointer(){
	return my_pos;
    }
    public long getPartPointer() throws IOException{
	long saved_pos = getPointer();
	long retpos = 0;

	seek(my_pos);
	getAbsPointer();//本当にseekさせるため readを呼ぶ

	if(getPartRefNo() < 0){
	    retpos = env.f.getAbsPointer();
	}
	else{
	    retpos = getRandomAccessExtent(getPartRefNo(), getPartSubno()).getPointer();
	}
	seek(saved_pos);

	return retpos;
    }
    public int getPartRefNo() throws IOException{
	long start_off = 0;
	long end_off = 0;
	UDF_ExtentElemList my_ee = my_refext.getExtentElem();

	UDF_ExtentElem extents = null;
	for(Iterator i=my_ee.iterator() ; i.hasNext() ; ){
	    extents = (UDF_ExtentElem)i.next();
	    start_off = extents.getOffset();
	    end_off = start_off + extents.getLen() * extents.getTimes();

	    if(my_pos >= start_off && my_pos < end_off){
		return extents.getPartRefNo();
	    }
	}
	//最後の位置のときは直前の extentsに属していることにする。
	if(my_pos == end_off){
	    return ((UDF_ExtentElem)my_ee.lastElement()).getPartRefNo();
	}
	System.err.println("PANIC: pos is out of extent(=" + my_pos + ")");
	throw new IOException("PANIC: pos is out of extent(=" + my_pos + ")");
    }

    public int getPartSubno(){
	return my_refext.getPartSubno();
    }

    public long getAbsPointer() throws IOException{
	UDF_RandomAccess ref;
	long saved_pos = my_pos;
	long abs_pos = 0;
	UDF_ExtentElemList my_ee = my_refext.getExtentElem();

	long end_off = 0;

	for(Iterator i=my_ee.iterator() ; i.hasNext() ; ){
	    UDF_ExtentElem extents = (UDF_ExtentElem)i.next();
	    
	    for(int j=0 ; j<extents.getTimes() ; ++j){
		long start_pos = extents.getLoc() * UDF_Env.LBS;
		long end_pos = start_pos + extents.getLen();
		long start_off = extents.getOffset();
		end_off = start_off + extents.getLen();

		long more_off = env.LBS * extents.getStep() * j;
		start_pos += more_off;
		end_pos += more_off;

		long more_off2 = extents.getLen() * j;
		start_off += more_off2;
		end_off += more_off2;

		if(my_pos >= start_off && my_pos < end_off ||
		   (!i.hasNext() && j >= extents.getTimes()-1 && my_pos == end_off)){

		    //なぜ最後の extentを見ているのだろう？？？？？ by issei
		    //不安はのこるが修正 2006/12/25
		    //
		    //ref = getRandomAccessExtent(((UDF_ExtentElem)my_ee.lastElement()).getPartRefNo(), getPartSubno());
		    ref = getRandomAccessExtent(extents.getPartRefNo(), getPartSubno());

		    long extent_off = my_pos - start_off;
		    ref.seek(start_pos + extent_off);
		    abs_pos = ref.getAbsPointer();

		    seek(saved_pos);
		    return abs_pos;
		}
	    }
	}

	System.err.println("PANIC: pos is out of extent(=" + my_pos + ")");
	throw new IOException("PANIC: pos is out of extent(=" + my_pos + ")");
    }

    public long seek(long off, int whence) throws IOException{
	long tmp = off;
	long old_my_pos = my_pos;
	switch(whence){
	case SEEK_SET:
	    break;
	case SEEK_CUR:
	    off = my_pos + off;
	    break;
	case SEEK_END:
	    off = length() + off;
	    break;
	}
	if(off >= length()){
	    my_pos = off;
	    return length();
	}
	my_pos = off;

	return my_pos;
    }

    ////
    private UDF_RandomAccess getRandomAccessExtent(int partno, int subno) {
	if (partno == -1) {
	    return env.f;
	}

	//debug();
	try{
	    return env.getPartMapRandomAccess(partno, subno);
	}
	catch(UDF_PartMapException e){
	    e.printStackTrace();
	}
	return null;
    }

    ////
    public int write(byte[] b, int off, int len) throws IOException, UDF_EOFException{
	UDF_RandomAccess ref;
	long ref_size;

	ref_size = my_refext.getExtentElem().size();
	if(ref_size == 0)
	    return 0;

	if(len == 0)
	    return 0;

	int actual_writesz = 0;

	int prev_len = -1;
	while(len > 0){
	    //無限ループ防止
	    if(prev_len == len){
		if(actual_writesz == 0){
		    debug();
		    throw new UDF_EOFException(null, "illegal data: pos=" + my_pos);
		}
		else
		    return actual_writesz;
	    }
	    prev_len = len;
	    int i = 0;
	    Iterator it = my_refext.getExtentElem().iterator();
	    for(i=0 ; len > 0 && it.hasNext() ; ++i){
		Object obj = it.next();
		UDF_ExtentElem extents = (UDF_ExtentElem)obj;

		for(int j=0 ; len > 0 && j<extents.getTimes() ; ++j){
		    if(false && j>0){
			System.err.println(j);
			extents.debug(0);
		    }
		    long start_pos = extents.getLoc() * UDF_Env.LBS;
		    long end_pos = start_pos + extents.getLen();
		    long start_off = extents.getOffset();
		    long end_off = start_off + extents.getLen();

		    long more_off = env.LBS * extents.getStep() * j;
		    start_pos += more_off;
		    end_pos += more_off;

		    more_off = extents.getLen() * j;
		    start_off += more_off;
		    end_off += more_off;
		    if(false && j>0){
			System.err.println("my_pos=" + my_pos);
			System.err.println("start_pos=" + start_pos);
			System.err.println("end_pos=" + end_pos);
			System.err.println("start_off=" + start_off);
			System.err.println("end_off=" + end_off);
		    }
		    if(my_pos >= start_off && my_pos < end_off){
			ref = getRandomAccessExtent(extents.getPartRefNo(), getPartSubno());
			
			long extent_off = my_pos - start_off;
			if(false && j>0)
			    System.err.println("extent_off=" + extent_off);

			ref.seek(start_pos + extent_off);
			int writesz = len;
			if(start_off + extent_off + len > end_off){
			    writesz = (int)(end_off - my_pos);
			}
			if(false && j>0){
			    System.err.println("writesz=" + writesz);
			}
			
			int ret = 0;
			if(extents.getExtentFlag() == 2)
			    ret = writesz;
			else
			    ret = ref.write(b, off, writesz);
			
			if(ret == 0)
			    return actual_writesz;
			if(false && ret < 0){
			    System.err.println(ref.getClass().getName());
			    System.err.println("PANIC" + off);
			    System.err.println("PANIC" + writesz);
			    System.exit(0);
			}

			len -= ret;
			off += ret;
			my_pos += ret;
			actual_writesz += ret;
			continue;
		    }
		    else if(!it.hasNext() && j >= extents.getTimes()){

			debug();
			throw new UDF_EOFException(null, "illegal data: pos=" + my_pos + " start_off=" + start_off + " end_off=" + end_off);
		    }
		}
	    }
	}
	if(actual_writesz == 0)
	    throw new UDF_EOFException(null, "EOF");

	return actual_writesz;
    }

    public int read(byte[] b, int off, int len) throws IOException, UDF_EOFException{
	//UDF_ExtentElem[] my_extents = my_refext.getExtent();
	UDF_RandomAccess ref;

	if(my_refext.getExtentElem().size() == 0)
	    return 0;

	if(len == 0)
	    return 0;

	int actual_readsz = 0;

	while(len > 0){
	    int i = 0;
	    Iterator it = my_refext.getExtentElem().iterator();
	    for(i=0 ; it.hasNext() && len > 0 ; ++i){
		UDF_ExtentElem my_extents = (UDF_ExtentElem)it.next();
		long start_pos = my_extents.getLoc() * UDF_Env.LBS;
		long end_pos = start_pos + my_extents.getLen();
		long start_off = my_extents.getOffset();
		long end_off = start_off + my_extents.getLen();

		if(env.debug_level >= 3){
		    //UDF_ElementBase.debugMsg(3, hashCode()+ "\t:pos:" +i+":" + my_pos);
		    //UDF_ElementBase.debugMsg(3, hashCode()+ "\t:start_pos:" +i+":" + start_pos);
		    //UDF_ElementBase.debugMsg(3, hashCode()+ "\t:end_pos:"  +i+":"+ end_pos);
		    //UDF_ElementBase.debugMsg(3, hashCode()+ "\t:start_off:"  +i+":"+ start_off);
		    //UDF_ElementBase.debugMsg(3, hashCode()+ "\t:end_off:"  +i+":"+ end_off);
		}
		if(my_pos >= start_off && my_pos < end_off){
		    ref = getRandomAccessExtent(my_extents.getPartRefNo(), getPartSubno());

		    long extent_off = my_pos - start_off;
		    ref.seek(start_pos + extent_off);
		    int readsz = len;
		    if(start_off + extent_off + len > end_off)
			//計算ミス発覚。複数extentにわたって readができないね。これじゃ
			//readsz = (int)(start_off + extent_off + len - end_off);
//			readsz =(int)(end_off - extent_off);
			readsz = (int)(end_off - my_pos);
		    int ret = ref.read(b, off, readsz);
		    if(env.debug_level >= 3){
			//UDF_ElementBase.debugMsg(3, hashCode()+ "\t:readsz:" +i+":"+ readsz);
			//UDF_ElementBase.debugMsg(3, hashCode()+ "\t:ret:" +i+":"+ ret);
		    }

		    if(ret == 0)
			return actual_readsz;
		    if(ret < 0){
			System.err.println(ref.getClass().getName());
			System.err.println("PANIC" + off);
			System.err.println("PANIC" + readsz);
			System.exit(0);
		    }

		    len -= ret;
		    off += ret;
		    my_pos += ret;
		    actual_readsz += ret;
		    continue;
		}
		else if(!it.hasNext()){

		    debug();
		    throw new UDF_EOFException(null, "illegal data: pos=" + my_pos + " start_off=" + start_off + " end_off=" + end_off);
		}
	    }
	}
	if(actual_readsz == 0)
	    throw new UDF_EOFException(null, "EOF");

	return actual_readsz;
    }
    /**
       デバッグ情報を出力する
     */
    public void debug(){
	//UDF_ExtentElem[] my_extents = my_refext.getExtent();
	UDF_ExtentElemList vector = my_refext.getExtentElem();
	for(Iterator i=vector.iterator() ; i.hasNext() ; ){
	    UDF_ExtentElem ee = (UDF_ExtentElem)i.next();
	    System.err.println(hashCode() +
			       "" + i + " loc=" + ee.getLoc() +
			       " partno=" + ee.getPartRefNo() +
			       " subno=" + getPartSubno() +
			       " len=" + ee.getLen() +
			       " off=" + ee.getOffset());
	}
    }
}
