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
package com.udfv.encoding;

import java.io.*;
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import com.udfv.exception.*;
import org.apache.xerces.dom.*;
import com.udfv.exception.*;
import com.udfv.core.*;
import com.udfv.util.*;

/**
   Encodingを表現するクラス。

   UDFVでは、バイトデータを視覚的に見やすい文字列に整えて出力する機能を持つ。
   この視覚的に見やすい文字列をUDFV文字列と定義する。

   UDFVはバイト列とUDFV文字列の変換を行うメソッドを備えたクラスである。
 */
abstract public class UDF_Encoding
{
    public String encName(){return "default";}

    /**
       バイト列をUDFV文字列に変換する。
     */
    abstract public String toString(byte []b);
    /**
       UDFV文字列をバイト列に変換する。
     */
    abstract public byte[] toBytes(String str, int size) throws UDF_Exception;
    /**
       UDFV文字列をバイト列に変換する。
     */
    abstract public byte[] toBytes(String str) throws UDF_Exception;

    //static UDF_Encoding i_enc[] = new UDF_Encoding[2];
    static UDF_Encoding enc0;
    static  UDF_Encoding enc1;

    /**
       エンコーディングの種類を現わす文字列から Encodingを生成する。

       現在、OSTA Compressed Unicodeおよび CS0_UDFの2種をサポートしている。
     */
    static public UDF_Encoding genEncoding(String encoding) throws UDF_EncodingException{
	UDF_Encoding enc;

        if(encoding.equals("OSTA Compressed Unicode")){
	    if(enc0 == null)
		enc0 = new UDF_EncodingOSTACompressedUnicode();
	    return enc0;
        }
        else if(encoding.equals("CS0_UDF")){
	    if(enc1 == null)
		enc1 = new UDF_EncodingCS0_UDF();
	    return enc1;
        }
	throw new UDF_EncodingException(null, "Bad encoding:" + encoding);
    }
}
