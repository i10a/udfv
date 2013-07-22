/*
*/
package com.udfv.udf102;

import java.io.*;
import java.util.*;
import org.w3c.dom.*;
import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.encoding.*;
import com.udfv.access.*;
import com.udfv.exception.*;
import com.udfv.ecma167.*;

/**
Type&nbsp;1&nbsp;Partition&nbsp;Map&nbsp;を表現するクラス。


<dl>

<dt>フィールド一覧</dt>
<dd>
<table border="1">
<tr><td>メンバ</td><td>型</td><td>サイズ</td></tr>
<tr><td><b>PartMapType</b></td><td><b>UDF_uint8</b></td><td><i>1</i></td></tr>
<tr><td><b>PartMapLen</b></td><td><b>UDF_uint8</b></td><td><i>1</i></td></tr>
<tr><td><b>VolSeqNumber</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td></tr>
<tr><td><b>PartNumber</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td></tr>
</table>
</dd>
</dl>
*/
public class UDF_part_map1 extends com.udfv.ecma167.UDF_part_map1
{

    /**
	コンストラクタ。

	@param elem   親エレメント。
	@param prefix ネームスペース。
	@param name   エレメント名。
    */
    public UDF_part_map1(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
    }


    /**
        Part. Map TYPE 1  の情報を表示。

    */
    public String [] toInformation(String indent, boolean detail) {

	String nl = UDF_Util.getSystemNewLine();
	Vector vec = new Vector();
	String str;

	str = indent + "Part Map Type             # " + getPartMapType().getIntValue();
	vec.add(str);
	str = indent + "Vol. Seq. Number          # " + getVolSeqNumber().getIntValue();
	vec.add(str);
	str = indent + "Part. Number              # " + getPartNumber().getIntValue();
	vec.add(str);

	String [] sl = new String[vec.size()];
	for (int i = 0, max = vec.size(); i < max; i++) {
	    sl[i] = (String)vec.elementAt(i);
	}

	return sl;
    }

};
