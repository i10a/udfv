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
package com.udfv.core;

import java.io.*;
import java.util.*;
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.ecma167.*;
import com.udfv.access.*;
import com.udfv.exception.*;
import com.udfv.udf201.*;

/**
  Allocation Descriptor のベースとなる抽象クラス。
*/
abstract public class UDF_AD extends UDF_Element implements Comparable
{
    /**
       @param elem 親
       @param name 名前
    */
    public UDF_AD(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name);
    }

    /**
       パーティション番号を取得する。

       @return パーティション番号
    */
    public abstract int getPartRefNo();
    /**
       LBNを取得する。

       @return LBN
    */
    public abstract int getLbn();
    /**
       長さを取得する。

       @return 長さ
    */
    public abstract long getLen();
    /**
       フラグを取得する。

       @return フラグ
    */
    public abstract int getFlag();

    /**
       パーティション番号を設定する。

       @param partrefno パーティション番号
    */
    public abstract void setPartRefNo(int partrefno);
    /**
       LBNを設定する。

       @return LBN
    */
    public abstract void setLbn(int lbn);
    /**
       長さを設定する。

       @return 長さ
    */
    public abstract void setLen(long len);
    /**
       フラグを設定する。

       @return フラグ
    */
    public abstract void setFlag(int flag);


    /**
       Extentの長さを表す UDF Elementを取得する。

       @return	UDF Element
     */
    public abstract UDF_uint32a getExtentLen();
    /**
       Extentの場所を表す UDF Elementを取得する。

       @return	UDF Element
     */
    public abstract UDF_uint32 getExtentLbn();
    /**
       Extentのパーティション番号を表す UDF Elementを取得する。

       @return	UDF Element

       UDF_short_adのときは該当する UDF Elementがないので、nullを返す。
     */
    public abstract UDF_uint16 getExtentPartRefNo();

    /**
       フラグ0

       ECMA167 4/14.14.1.1 Extent recorded and allocated
     */
    public static final int FLAG0 = 0;
    /**
       フラグ1

       ECMA167 4/14.14.1.1 Extent not recorded but allocated
     */
    public static final int FLAG1 = 1;
    /**
       フラグ2

       ECMA167 4/14.14.1.1 Extent not recorded and not allocated
     */
    public static final int FLAG2 = 2;
    /**
       フラグ3

       ECMA167 4/14.14.1.1 The extent is the next extent of allocation descriptors(see 4/12)
     */
    public static final int FLAG3 = 3;
    
    public static UDF_AD genAD(UDF_Element mother, int type) throws UDF_InternalException{
	try{
	    switch(type){
	    case 0:
		return (UDF_AD)genElement("UDF_short_ad", mother, null, null);
	    case 1:
		return (UDF_AD)genElement("UDF_long_ad", mother, null, null);
	    case 2:
		return (UDF_AD)genElement("UDF_ext_ad", mother, null, null);
	    default:
		throw new UDF_InternalException(mother, "No such AD type: " + type);
	    }
	}
	catch(ClassNotFoundException e){
	    throw new UDF_InternalException(mother, "No such AD type: " + type);
	}
    }
    public int compareTo(Object o){
	UDF_AD ad = (UDF_AD) o;
	if(getPartRefNo() != ad.getPartRefNo())
	    return getPartRefNo() - ad.getPartRefNo();

	if(getLbn() != ad.getLbn())
	    return getLbn() - ad.getLbn();

	if(getLen() != ad.getLen())
	    return (int)(getLen() - ad.getLen());

	return 0;
    }
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	final short category = UDF_Error.C_ECMA167;
	
	
	if(getLen() == 0 && getFlag() != 0){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Extent Length",
			 "If the 30 least significant bits are set to ZERO, " +
			 "the two most significant bits shall also be set to ZERO.",
			 "4/14.14.1.1",
			 String.valueOf(getFlag()), "0"));
	}
	
	
	return el;
    }
    
    /**
       Extent Length のflag の値を検証します。
       エラーにはC_ECMA167 カテゴリ、L_ERROR レベル、原因、記録値、期待値が含まれます。
       
       @param flag 比較検証するフラグの値。
       @return エラーインスタンス。
    */
    public UDF_Error verifyFlag(int flag) throws UDF_Exception{
	
	if(getFlag() != flag){
	    
	    return new UDF_Error(UDF_Error.C_ECMA167, UDF_Error.L_ERROR, "Extent Length(2 most significant bits)",
				 "", "", String.valueOf(getFlag()), String.valueOf(flag));
	}
	else
	    return new UDF_Error();
    }

    /**
       ref + ".lbn", ref + ".len" , ref + ".partno" のラベルをつける。
     */
    public void setRefAttribute(String ref){
	getExtentLbn().setAttribute("ref", ref + ".lbn");
	getExtentLen().setAttribute("ref", ref + ".len");
	getExtentPartRefNo().setAttribute("ref", ref + ".partno");
    }

}
