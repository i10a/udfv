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
import com.udfv.exception.*;
import com.udfv.access.*;
import org.apache.xerces.dom.*;

/**
   UDF_AD のベクター。

   @see <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/util/Collection.html">Collection</a>
 */
public class UDF_ADList implements Collection{
    Vector vec;

    public UDF_ADList(){
	vec = new Vector();
    }

    public boolean add(Object elem){
	return vec.add(elem);
    }
    public boolean add(UDF_AD elem){
	return vec.add(elem);
    }
    public boolean add(UDF_ADList el){
	Iterator it = el.iterator();
	while(it.hasNext()){
	    vec.add(it.next());
	}
	return true;
    }
    public boolean addAll(Collection c){
	return vec.addAll(c);
    }

    public void clear(){
	vec.clear();
    }

    public boolean contains(Object o){
	return vec.contains(o);
    }

    public boolean containsAll(Collection o){
	return vec.containsAll(o);
    }

    public boolean equals(Object o){
	return vec.equals(o);
    }

    public int hashCode(){
	return vec.hashCode();
    }

    public boolean isEmpty(){
	return vec.isEmpty();
    }

    public Iterator iterator(){
	return vec.iterator();
    }

    public boolean remove(UDF_AD elem){
	return vec.remove(elem);
    }

    public boolean remove(Object elem){
	return vec.remove(elem);
    }

    public boolean removeAll(Collection c){
	return vec.removeAll(c);
    }

    public boolean retainAll(Collection c){
	return vec.retainAll(c);
    }

    public void removeAllADs(){
	vec.removeAllElements();
    }

    public int size(){
	return vec.size();
    }

    public Object[] toArray(){
	return vec.toArray();
    }

    public Object[] toArray(Object[] a){
	return vec.toArray(a);
    }

    public UDF_AD[] toADArray(){
	UDF_AD[] elms = new UDF_AD[vec.size()];

	int i=0;
	Iterator it = vec.iterator();
	while(it.hasNext())
	    elms[i++] = (UDF_AD)it.next();

	return elms;
    }


    public int indexOf(UDF_AD elem) {
	return vec.indexOf(elem);
    }

    public UDF_AD set(int idx, UDF_AD elem) {
	return (UDF_AD) vec.set(idx, elem);
    }

    public UDF_AD elementAt(int i){
	return (UDF_AD)vec.elementAt(i);
    }


    public void setList(Vector l){
	vec.removeAllElements();

	/*
	for(int i=0 ; i<l.size() ;++i){
	    vec.add(l.elementAt(i));
	}
	*/
	for(Iterator it=l.iterator() ; it.hasNext() ; ){
	    vec.add(it.next());
	}
    }
    public Vector getList(){
	return vec;
    }
    public UDF_AD firstElement(){
	return (UDF_AD)vec.firstElement();
    }

    /**
      指定のLBNからsizeバイトの領域を持つADを作成して UDF_ADList に追加する。

      @param base 追加するADのインスタンス。
      @param lbn 開始セクタ番号
      @param size サイズ

      ※追加されるADは base をduplicateElement()したものになる。初期値、パーティション番号は設定しておく必要がある。
    */
    public void addADFromLbn(UDF_AD base, int lbn, long size) {

        long bsize = com.udfv.util.UDF_Env.LBS;
        while(size > 0) {

            long ssize = (size + bsize - 1) / bsize;

            //　Extent Length に書き込める最大サイズに調整　//
            if (ssize > 0x0007ffff) {
                ssize = 0x0007ffff;
            }

            UDF_AD ad = (UDF_AD) base.duplicateElement();
            ad.setLbn(lbn);

            lbn += (int)ssize;
            ssize *= bsize;
            size -= ssize;

            if (size < 0) {
                ssize += size;
                size = 0;
            }

            ad.setLen(ssize);
//ad.debug(4);
            add(ad);
        }
    }

    /**
       複製する
     */
    public UDF_ADList duplicateList(){
	UDF_ADList new_list = new UDF_ADList();
	for(Iterator it = iterator() ; it.hasNext() ; ){
	    UDF_AD ad = (UDF_AD)it.next();
	    new_list.add(ad.duplicateElement());
	}
	return new_list;
    }

}
