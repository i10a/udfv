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
   UDF_Elementのベクター。

   @see <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/util/Collection.html">Collection</a>
 */
public class UDF_ElementList implements Collection{
    Vector vec;

    public UDF_ElementList(){
	vec = new Vector(0, 16);
    }

    public boolean add(Object elem){
	return vec.add(elem);
    }
    public boolean add(UDF_Element elem){
	return vec.add(elem);
    }
    public boolean add(UDF_ElementList el){
	/*
	for(int i=0 ; i<el.size() ; ++i)
	    vec.add(el.elementAt(i));
	*/
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

    public boolean remove(UDF_Element elem){
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

    public void removeAllElements(){
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

    public UDF_Element[] toElemArray(){
	UDF_Element[] elms = new UDF_Element[vec.size()];

	/*
	for(int i=0 ; i<vec.size() ; ++i){
	    elms[i] = (UDF_Element)vec.elementAt(i);
	}
	*/
	int i=0;
	Iterator it = vec.iterator();
	while(it.hasNext())
	    elms[i++] = (UDF_Element)it.next();

	return elms;
    }


    public int indexOf(UDF_Element elem) {
	return vec.indexOf(elem);
    }

    public UDF_Element set(int idx, UDF_Element elem) {
	return (UDF_Element) vec.set(idx, elem);
    }

    public UDF_Element elementAt(int i){
	return (UDF_Element)vec.elementAt(i);
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

    public UDF_Element firstElement(){
	return (UDF_Element)vec.firstElement();
    }
    public UDF_Element lastElement(){
	return (UDF_Element)vec.lastElement();
    }
}
