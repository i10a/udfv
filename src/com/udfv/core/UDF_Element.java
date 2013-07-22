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
