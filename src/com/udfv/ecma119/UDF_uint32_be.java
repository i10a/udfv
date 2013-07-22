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
package com.udfv.ecma119;

import java.io.*;
import java.util.*;
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.access.*;
import com.udfv.exception.*;
import com.udfv.ecma167.*;

/**
   ECMA119/7.3.2 で定義されている32ビット値を表すクラス。
*/
public class UDF_uint32_be extends com.udfv.ecma167.UDF_uint32
{

    /**
       コンストラクタ。
       
       @param elem   親エレメント。
       @param prefix ネームスペース。
       @param name   エレメント名。
    */
    public UDF_uint32_be(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? "UDF_uint32_be" : name);	
    }

    /**
       アクセサからデータを読み込む。
       
       @param f アクセサ。
    */
    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_EOFException{
        byte[] b = new byte[4];

        int ret = f.read(b, 0, 4);
        long v = (((long)b[3]) & 0xff) |
            ((((long)b[2]) & 0xff) << 8) |
            ((((long)b[1]) & 0xff) << 16) |
            ((((long)b[0]) & 0xff) << 24);

        setValue(v);
        return ret;
    }
    
    /**
       アクセサへデータを書き込む。
       
       @param f アクセサ。
    */
    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_EOFException{
	byte[] b = new byte[4];

        b[3] = (byte)val;
        b[2] = (byte)(val >> 8);
        b[1] = (byte)(val >> 16);
        b[0] = (byte)(val >> 24);
        int ret = f.write(b, 0, 4);

        return ret;
    }
};
