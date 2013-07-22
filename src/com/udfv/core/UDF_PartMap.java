/*
*/
package com.udfv.core;

import java.io.*;
import java.util.*;
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
import com.udfv.exception.*;
import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.ecma167.*;
import com.udfv.udf201.*;
import com.udfv.access.*;

/**
   UDF_Partは、全てのPartition Mapの基底となるクラスです。
 */
abstract public class UDF_PartMap extends UDF_Element
{
    //このパーティションマップのパーティション番号

    int partpartno;

    /**
       このパーティションマップのパーティションリファレンス番号を設定する。
       このAPIはシステムで使用する。ユーザが特に気にする必要はない。
       
     */
    public void setPartPartno(int n){
	partpartno = n;
    }
    /**
       このパーティションマップのパーティションリファレンス番号を取得する。

       Logical Volume Descriptorの何番目にあるかを取得する関数。
     */
    public int getPartPartno(){
	return partpartno;
    }

    /**
       コンストラクタ

       @param elem 親
       @param name 名前
    */
    public UDF_PartMap(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name);
    }

    /*
       パーティションの開始位置と長さを指定する。

       @param	loc	開始位置(LBS単位)
       @param	len	サイズ

       このメソッドは type1 partition map, sparable partition map
       で有効である。

       それ以外で意味を持たない
    public void setLocAndLen(int loc, long len) throws UDF_PartMapException{
	;
    }
     */

    /**
       mirrorを持つ可能性のあるパーティションか？

       ※ MetadataPartMapだけが trueを返します。
    public boolean hasMirror(){
	return false;
    }
     */

    abstract public UDF_uint8 getPartMapType();
    abstract public UDF_uint16 getPartNumber();

    /**
      パーティションマップの再計算を行う。

      @param partnum パーティションマップのパーティション番号。
     */
    public void recalcPM(int partnum) throws UDF_PartMapException, UDF_InternalException, IOException{
	UDF_Extent ext = env.part[partnum].genExtent(0);
	UDF_RandomAccessExtent rae = ext.genRandomAccessExtent();

	env.setPartMapExtent(partnum, 0, ext);
	env.setPartMapRandomAccess(partnum, 0, rae);
    }

    /**
       UDF_Extentを作る
    */
    public UDF_Extent genExtent(int subno) throws UDF_PartMapException{
	try{
	    UDF_Extent extent = (UDF_Extent)createElement("UDF_Extent", null, null);
	    com.udfv.ecma167.UDF_desc5 desc5 = env.getPartDesc(UDF_Env.VDS_AUTO, getPartNumber().getIntValue());
	    
	    extent.addExtent(desc5.getPartStartingLoc().getIntValue(), -1,
			     desc5.getPartLen().getIntValue() * env.LBS);

	    extent.setPartSubno(subno);
	    return extent;
	}
	catch(Exception e){
	    ignoreMsg("UDF_PartMap#genExtent", e);
	}
	return null;
    }
}
