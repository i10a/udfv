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
package com.udfv.ecma167;

import java.io.*;
import java.util.*;
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
import java.lang.reflect.*;
import com.udfv.exception.*;
import com.udfv.access.*;
import com.udfv.core.*;
import com.udfv.util.*;

/**
  パディングとなる領域を表現するクラス。

  alignになるよう親 Elementを揃える。
  

*/
public class UDF_pad extends UDF_bytes
{
    private long my_align;

    /**

       @param align	揃える位置。0の場合は常に長さ 0となる。
     */
    public UDF_pad(UDF_Element elem, String prefix, String name, long align){
	super(elem, prefix, name == null ? "UDF_pad" : name, 0);
	setAlign(align);
    }

    public UDF_pad(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? "UDF_pad" : name);
    }

    public void setAlign(long align){
	setAttribute("align", align);
	my_align = align;
    }

    public long getAlign(){
	return my_align;
    }

    public void setSize(int size){
	super.setSize(size);
	setNodeVal();
    }

    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception{
	
	preReadHook(f);
	
	if(my_align == 0){
	    setSize(0);
	    return 0;
	}

	long pos0 = f.getPointer();
	long pos1 = UDF_Util.align(pos0, my_align);
	setSize((int)(pos1 - pos0));
	if(getSize() == 0)
	    return 0;

	return super.readFrom(f);
    }

    public void readFromXML(Node n) throws UDF_Exception{
	super.readFromXML(n);
	Element elem = (Element)n;
	String align = elem.getAttribute(getPrefix(true) + "align");
	setAlign(UDF_Util.str2l(align));
    }

    /**
      空き領域の検証をします。<br>
      空き領域は０であるべきです。<br>
      ※必須でない箇所もありえます。<br>
      <br>
      ex)<br>
      10.6.13 Partition Maps<br>
      14.9.21 Extended Attributes<br>
      14.10.3.7 Escape Sequences
    */
    public UDF_ErrorList verify() throws UDF_Exception {

        UDF_ErrorList el = new UDF_ErrorList();

        //　論理ブロックの未使用領域は０クリアされているべきである　//
        if(!UDF_Util.isAllZero(getData())){

            el.addError(
                new UDF_Error(
                    UDF_Error.C_ECMA167, UDF_Error.L_ERROR, "",
                    "Unused bytes following descriptor till the end of the logical block shall have a value of #00."
                )
            );
	    el.setGlobalPoint(getGlobalPoint());
            el.setRName("Unused bytes");
        }

        return el;
    }

    /**
       エレメントを複製する。
       
       @return 複製されたエレメント。
    */
    public UDF_Element duplicateElement(){
	UDF_pad v = (UDF_pad)createElement(getClass().getName(), null, getName());
	v.duplicateHook(this);
	v.setSize(getSize());
	v.setData(getData());
	v.setAlign(getAlign());
	
	if(getEncoding() != null)
	    v.setEncoding(getEncoding());
	v.setNodeVal();

	UDF_ElementBase[] child = getChildren();
	if(child.length > 0){
	    v.removeAllChildren();
	    for(int i=0 ; i<child.length ; ++i){
		v.appendChild((UDF_Element) child[i].duplicateElement());
	    }
	}
	
	return v;
    }
    /*
    protected boolean hasOffset(){
	return true;
    }
    
    protected boolean hasGlobalPoint(){
	return true;
    }
    */

    public void recalc(short type, UDF_RandomAccess f){
	super.recalc(type, f);
	if(type == RECALC_PAD) {
	    try{
		long cur = getOffset();

		if(my_align == 0)
		    my_align = 1;
		if(cur % my_align != 0){
		    int new_size = (int)(my_align - (cur % my_align));
		    byte[] b = getData();
		    if(new_size == getSize())
			return;

		    if(new_size < getSize()){
			int psize = getSize() - new_size;
			byte[] bb = new byte[new_size];
			//　サイズが縮む場合は先頭から消失していくべきだと考える　//
			for (int i = 0; i < new_size ; i++) {
			    bb[i] = b[psize + i];
			}
			setSize(new_size);
			setData(bb);
		    }
		    else
			setSize(new_size);

		    //sizeがかわったということは親クラスのオフセットも
		    //かわるということである。
		    getParent().recalc(RECALC_GP, f);
		}
		//alignに揃っているのにサイズが0以上の場合
		else if(getSize() > 0){
		    setSize(0);
		}
	    }
	    catch(Exception e){
		ignoreMsg("UDF_pad:recalc", e);
		getParent().debug(0);
		//System.exit(0);
	    }
	}
    }

    public void debug(int indent){
	System.err.print(UDF_Util.repeat(' ', indent));
	String info = getName() + " size=" + getSize();
	info +=  " align=" + my_align;

	System.err.print(info + ":");
	UDF_ElementBase[] e = getChildren();
	System.err.println(UDF_Util.b2qstr(getData()));
    }

}
