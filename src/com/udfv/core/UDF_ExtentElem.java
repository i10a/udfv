/*

*/
package com.udfv.core;

import java.io.*;
import java.util.*;
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import com.udfv.exception.*;
import org.apache.xerces.dom.*;

/**
   extentの要素を表わすクラス
 */
public class UDF_ExtentElem implements Comparable
{
    /**
       LBS単位の位置
     */
    int loc;
    /**
       パーティション番号。-1ならばパーティションの外を表わす
     */
    short partno;
    /**
       長さ。バイト単位
     */
    long len;
    /**
       extentが複数ある場合、そのextentが先頭から通しで何バイト目にあるかを
       格納しておく変数。
 
      この値は UDF_Extentクラスから使用する場合、正しく管理されているので、
      ユーザが特に気にする必要はない。
     */
    long off;

    /**
       このextentに付けられた id
    public String id;
     */

    /**
       extentのフラグ。0, 1, 2の3種類
    */
    byte extent_flag;

    public UDF_ExtentElem(){
	;
    }
    public UDF_ExtentElem(int loc_, int partno_, long len_, int extent_flag_){
	loc = loc_;
	partno = (short)partno_;
	len = len_;
	extent_flag = (byte)extent_flag_;
	off = 0;
    }
    public UDF_ExtentElem(UDF_ExtentElem ee){
	loc = ee.loc;
	partno = ee.partno;
	len = ee.len;
	extent_flag = ee.extent_flag;
	off = ee.off;
    }

    public UDF_ExtentElem(UDF_AD ad){
	loc = ad.getLbn();
	partno = (short)ad.getPartRefNo();
	len = ad.getLen();
	extent_flag = (byte)ad.getFlag();
    }

    public int getLoc(){
	return loc;
    }
    public int getPartRefNo(){
	return partno;
    }
    public long getLen(){
	return len;
    }
    public long getOffset(){
	return off;
    }
    public int getExtentFlag(){
	return extent_flag;
    }

    public void setLoc(int x){
	loc = x;
    }
    public void setPartRefNo(int x){
	partno = (short)x;
    }
    public void setLen(long x){
	len = x;
    }
    public void setOffset(long x){
	off = x;
    }
    public void setExtentFlag(int x){
	extent_flag = (byte)x;
    }

    public void set(int loc_, int partno_, long len_){
	loc = loc_;
	partno = (short)partno_;
	len = len_;
    }

    /**
       等しいかどうか調べ、等しければ trueを返す

       「loc, partno, len」

       が等しいものである。

       @param o	比較対象
       @return	結果
     */
    public boolean equals(Object o){
	/*
	if(loc == extent.loc &&
	   partno == extent.partno &&
	   len == extent.len)
	    return true;
	return false;
	*/
	if(compareTo(o) == 0)
	    return true;
	return false;
    }

    public int compareTo(Object o){
	UDF_ExtentElem ee = (UDF_ExtentElem)o;

	if(partno != ee.partno)
	    return partno - ee.partno;
	if(loc != ee.loc)
	    return loc - ee.loc;
	if(len != ee.len)
	    return (int)(len - ee.len);
	if(extent_flag != ee.extent_flag)
	    return (extent_flag - ee.extent_flag);

	return 0;
    }

    /**
       繰り返し数。
     */
    public int getTimes(){
	return 1;
    }
    /**
       繰り返す際の次の loc位置。
    */
    public int getStep(){
	return 0;
    }
    /**
       トータルでどれくらいのサイズを占有しているか。
    */
    public long getTotalLen(){
	return len;
    }

    public void debug(int indent){
	System.err.println("off:" + off + " loc:" + loc + " partno:" + partno + " len:" + len + " extent_flag:" + extent_flag);
    }
}
