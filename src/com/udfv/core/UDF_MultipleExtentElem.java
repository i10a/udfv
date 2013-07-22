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

   複数の要素を現わすことが出来る。
 */
public class UDF_MultipleExtentElem extends UDF_ExtentElem
{
    /**
       繰り返し数
     */
    private int times;
    /**
       繰り返しの際のLBNのスキップ数
     */
    private int step;

    public UDF_MultipleExtentElem(){
	;
    }
    public UDF_MultipleExtentElem(int loc_, int partno_, long len_, int extent_flag_){
	super(loc_, partno_, len_, extent_flag_);
    }
    public UDF_MultipleExtentElem(int loc_, int partno_, long len_, int extent_flag_, int times_, int step_){
	super(loc_, partno_, len_, extent_flag_);
	times = times_;
	step = step_;
    }
    public UDF_MultipleExtentElem(UDF_ExtentElem ee){
	super(ee);
    }
    public UDF_MultipleExtentElem(UDF_MultipleExtentElem ee){
	super(ee.getLoc(), ee.getPartRefNo(), ee.getLen(), ee.getExtentFlag());
	times = ee.times;
	step = ee.step;
    }
    public UDF_MultipleExtentElem(UDF_MultipleAD_long_ad ad){
	super(ad);

	len = ad.getLen() / ad.getTimes();
	times = ad.getTimes();
	step = ad.getStep();
    }
    public UDF_MultipleExtentElem(UDF_MultipleAD_short_ad ad){
	super(ad);

	len = ad.getLen() / ad.getTimes();
	times = ad.getTimes();
	step = ad.getStep();
    }

    /**
       繰り返し数。
     */
    public int getTimes(){
	return times;
    }
    /**
       繰り返す際の次の loc位置。
    */
    public int getStep(){
	return step;
    }
    /**
       トータルでどれくらいのサイズを占有しているか。
    */
    public long getTotalLen(){
	return len * times;
    }

    public UDF_ExtentElem getExtentElem(int idx){
	return new UDF_ExtentElem(loc + step * idx, partno, len, extent_flag);
    }

    public void debug(int indent){
	System.err.println("off:" + off + " loc:" + loc + " partno:" + partno + " len:" + len + " extent_flag:" + extent_flag + " times:" + times + " step:" + step);
    }
}
