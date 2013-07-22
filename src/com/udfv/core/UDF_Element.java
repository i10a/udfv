/*
 */
package com.udfv.core;

import java.io.*;
import java.util.*;
import java.lang.reflect.*;
import org.w3c.dom.*;
import com.udfv.exception.*;
import com.udfv.access.*;

/**
   UDF_Elementは全ての UDF 要素の基底となるクラスである。

   このクラスは interfaceになる予定
*/
abstract public class UDF_Element extends UDF_ElementBase {
    /**
       コンストラクタ
    */
    public UDF_Element(){
	super();
    }
    /**
       コンストラクタ

       @param elem	親
       @param prefix	namespace
       @param name	名前
     */
    public UDF_Element(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name);
    }

    /**
       UDFにおけるバイトサイズを取得する。ただしサイズが 2^31を越える場合は桁落ちがある可能性がある。
     */
    abstract public int getSize();
    /**
       UDFにおけるバイトサイズを取得する
     */
    abstract public long getLongSize();
}
