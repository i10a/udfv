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
import com.udfv.exception.*;
import com.udfv.access.*;
import com.udfv.ecma167.*;

/**
   ECMA119 7.2.3 で定義されている16ビット値を表すクラス。
*/
public class UDF_uint16_lebe extends com.udfv.ecma167.UDF_uint16
{
    /**
       コンストラクタ。
       
       @param elem   親エレメント。
       @param prefix ネームスペース。
       @param name   エレメント名。
    */
    public UDF_uint16_lebe(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? "UDF_uint16_be" : name);	
    }

    public int getSize(){return (super.getSize() * 2);}
    public long getLongSize(){return (super.getLongSize());}

    /**
       アクセサからデータを読み込む。
       
       @param f アクセサ。
    */
    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_EOFException, UDF_DataException{
	byte[] b = new byte[4];

	int ret = f.read(b, 0, 4);
	int v1 = (((int)b[0]) & 0xff) |((((int)b[1]) & 0xff) << 8);
	int v2 = (((int)b[3]) & 0xff) |((((int)b[2]) & 0xff) << 8);

	if(v1 != v2)
	    throw new UDF_DataException(this, "ECMA119 uint16 le("+v1+")!=("+v2+")be");

	setValue(v1);

	return ret;
    }

    /**
       アクセサへデータを書き込む。
       
       @param f アクセサ。
    */
    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_EOFException, UDF_DataException{
	byte[] b = new byte[4];

	b[0] = (byte)val;
	b[1] = (byte)(val >> 8);
	b[2] = (byte)(val >> 8);
	b[3] = (byte)val;
	int ret = f.write(b, 0, 4);

	return ret;
    }
};
