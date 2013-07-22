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
package com.udfv.util;

import com.udfv.exception.*;
import com.udfv.access.*;
import com.udfv.util.*;
import com.udfv.ecma167.*;
import com.udfv.core.*;

/**
   ラベルを生成するクラス。

   UDFVでは特定の UDF_Elementに setAttribute()でラベルをつけることにより、recalc()等のメソッドを実装している。

   このクラスは各Extentにつけるべきラベルを生成するためのクラスである。
 */
abstract public class UDF_Label{
    /**
       Extentのラベルを生成する

       @param extent
       @return ラベル

現在ラベルは &lt;extents&gt; の最初の要素の値を見て生成する。

ラベル命名規則(partnoが -1の場合)

"EXTENT" + loc

ラベル命名規則(partnoが 0以上の場合)

"EXTENT" + extent_flag + "_" + loc + "_" + partno + ["_" + subno]

領域がオーバーラップしている場合、データ領域としては異なるものであっても同じ Extentとして判断されてしまう問題がある。
     */
    static public String genExtentLabel(UDF_Extent extent){
	//UDF_ExtentElem[] ext = extent.getExtent();
	//for(int i=0 ; i<ext.length ; ++i){
	UDF_ExtentElem ee = extent.getExtentElem().firstElement();
	if(ee != null){
	    if(ee.getLen() > 0 && ee.getPartRefNo() == -1)
		return "EXTENT" + ee.getLoc();
	    else if(ee.getLen() > 0)
		return "EXTENT" + ee.getExtentFlag() + "_" + ee.getLoc() + "_" + ee.getPartRefNo() + (extent.getPartSubno()!=0 ? "_"+extent.getPartSubno() : "");
	    else
		return "EXTENT" + ee.getLoc();
	}
	return "EXTENT";
    }

    /**
       Extentのラベルを生成する

       @param extent_flag	extentフラグ
       @param loc		LBN
       @param partno		パーティション番号
       @param mirror		ミラーかどうか
       @return ラベル

現在ラベルは &lt;extents&gt; の最初の要素の値を見て生成する。

ラベル命名規則(partnoが -1の場合)

"EXTENT" + loc

ラベル命名規則(partnoが 0以上の場合)

"EXTENT" + extent_flag + "_" + loc + "_" + partno + ["_" + mirror]

領域がオーバーラップしている場合、データ領域としては異なるものであっても同じ Extentとして判断されてしまう問題がある。

@deprecated replaced by {@link genExtentLabel(int, int , int, int)}
     */
    static public String genExtentLabel(int extent_flag, int loc, int partno, boolean mirror){
	if(partno > 0)
	    return "EXTENT" + extent_flag + "_" + loc + "_" + partno + (mirror ? "_1" : "");
	else
	    return "EXTENT" + loc;
    }

    /**
       Extentのラベルを生成する

       @param extent_flag	extentフラグ
       @param loc		LBN
       @param partno		パーティション番号
       @param mirror		ミラーかどうか
       @return ラベル

現在ラベルは &lt;extents&gt; の最初の要素の値を見て生成する。

ラベル命名規則(partnoが -1の場合)

"EXTENT" + loc

ラベル命名規則(partnoが 0以上の場合)

"EXTENT" + extent_flag + "_" + loc + "_" + partno + ["_" + subno]

領域がオーバーラップしている場合、データ領域としては異なるものであっても同じ Extentとして判断されてしまう問題がある。

     */
    static public String genExtentLabel(int extent_flag, int loc, int partno, int subno){
	if(partno > 0)
	    return "EXTENT" + extent_flag + "_" + loc + "_" + partno + (subno != 0 ? ("_" + subno) : "");
	else
	    return "EXTENT" + loc;
    }

    /**
       FENTのラベルを作成する。

       root FEのときは ROOTになる。
       root system streamのときは SROOTになる。
       それ以外は、FENT + lbn +"_" + partnoになる。

       その文字列にさらに副番号が "_" + subno の形で結合される。
       ただしsubnoが 0の場合は何もつかない。

       @param lbn	LBN
       @param partno	partno
       @param subno	副パーティション番号

       @return ラベル

       @see <a href="subno.html">副パーティション番号</a>
     */
    static public String genFELabel(UDF_Env env, int lbn, int partno, int subno){
	String subnostr = subno != 0 ? ("_" + subno) : "";
	try{
	    int root_lbn = env.getFileSetDesc(subno).getRootDirectoryICB().getLbn();
	    int root_partno = env.getFileSetDesc(subno).getRootDirectoryICB().getPartRefNo();
	    int stream_lbn = env.getFileSetDesc(subno).getSystemStreamDirectoryICB().getLbn();
	    int stream_partno = env.getFileSetDesc(subno).getSystemStreamDirectoryICB().getPartRefNo();
	    if(lbn == root_lbn && partno == root_partno)
		return "ROOT" + subnostr;
	    if(lbn == stream_lbn && partno == stream_partno)
		return "SROOT" + subnostr;
	    
	}
	catch(UDF_VolException e){
	    //MetadataFile等のラベルを作るときにここにくる可能性がある。
	    //(FSDSがまだ登録されていないから)
	    //return "FENT" + lbn + "_" + partno + subnostr;
	}
	return "FENT" + lbn + "_" + partno + subnostr;
    }

    /**
       FENTのラベルを作成する。

       root FEのときは ROOTになる。
       root system streamのときは SROOTになる。
       mainのときは FENT + lbn +"_" + partnoになる。

       @param lbn	LBN
       @param partno	partno
       @param mirror	mirror
       @return ラベル

       @deprecated replaced by {@link genFELabel(UDF_Env, int, int, int)}
     */
    static public String genFELabel(UDF_Env env, int lbn, int partno, boolean mirror){
	return genFELabel(env, lbn, partno, mirror ? 1 : 0);
    }

    /**
       このDirectoryに付けるユニークな"id"ラベルを生成する。

       @param dir	ディレクトリ

       @return ラベル
    */
    static public String genDirectoryLabel(UDF_Directory dir){
	String subnostr = dir.getPartSubno() == 0 ? "" : ("_" + dir.getPartSubno());
	//最初の位置を見て決める。
	//Directoryは内部的にoverlapしないはずなので、これで十分と思われるが
	//overlapも想定したイメージを作るとき用に再考を要するかもしれぬ

	if(com.udfv.ecma167.UDF_SDirectory.class.isAssignableFrom(dir.getClass()))
	    return "SDIR" + (dir.getPartMapOffset()/dir.env.LBS) + "_" + dir.getElemPartRefNo() + subnostr;
	else
	    return "DIR" + (dir.getPartMapOffset()/dir.env.LBS) + "_" + dir.getElemPartRefNo() + subnostr;
    }

    /**
       このDirectoryに付けるユニークな"id"ラベルを生成する。

       @param lbn	LBN
       @param partno	パーティション番号
       @param mirror	ミラーかどうか
       @param stream_dir	STREAM DIRかどうか

       @return ラベル

       @deprecated replacetd by {@link genUDFDirectoryLabel(UDF_Env, int, int, int boolean);}
    */
    static public String genDirectoryLabel(UDF_Env env, int lbn, int partno, boolean mirror, boolean stream_dir){
	//最初の位置を見て決める。
	//Directoryは内部的にoverlapしないはずなので、これで十分と思われるが
	//overlapも想定したイメージを作るとき用に再考を要するかもしれぬ

	if(stream_dir)
	    return "SDIR" + lbn + "_" + partno + (mirror ? "_1" : "");
	else
	    return "DIR" + lbn + "_" + partno + (mirror ? "_1" : "");
    }

    /**
       このDirectoryに付けるユニークな"id"ラベルを生成する。

       @param lbn	LBN
       @param partno	パーティション番号
       @param subno	subno
       @param stream_dir	STREAM DIRかどうか

       @return ラベル
    */
    static public String genDirectoryLabel(UDF_Env env, int lbn, int partno, int subno, boolean stream_dir){
	//最初の位置を見て決める。
	//Directoryは内部的にoverlapしないはずなので、これで十分と思われるが
	//overlapも想定したイメージを作るとき用に再考を要するかもしれぬ

	if(stream_dir)
	    return "SDIR" + lbn + "_" + partno + (subno != 0 ? "_" + subno : "");
	else
	    return "DIR" + lbn + "_" + partno + (subno != 0 ? "_" + subno : "");
    }
}
