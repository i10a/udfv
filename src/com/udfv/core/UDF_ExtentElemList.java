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
   UDF_ExtentElemのベクター。

   @see <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/util/Collection.html">Collection</a>
 */
public class UDF_ExtentElemList implements Collection{
    /**
       保有する ExtentElemの個数
     */
    Vector vec;

    public UDF_ExtentElemList(){
	vec = new Vector(0, 16);
    }

    public boolean add(Object elem){
	return vec.add(elem);
    }
    public boolean add(UDF_ExtentElem elem){
	return vec.add(elem);
    }
    public boolean add(UDF_ExtentElemList el){
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
	return new UDF_ExtentElemListIterator(this);
    }

    public boolean remove(UDF_ExtentElem elem){
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

    public UDF_ExtentElem[] toElemArray(){
	UDF_ExtentElem[] elms = new UDF_ExtentElem[vec.size()];

	int i=0;
	Iterator it = vec.iterator();
	while(it.hasNext())
	    elms[i++] = (UDF_ExtentElem)it.next();

	return elms;
    }


    public int indexOf(UDF_ExtentElem elem) {
	return vec.indexOf(elem);
    }

    public UDF_ExtentElem set(int idx, UDF_ExtentElem elem) {
	return (UDF_ExtentElem) vec.set(idx, elem);
    }

    public UDF_ExtentElem elementAt(int i){
	return (UDF_ExtentElem)vec.elementAt(i);
    }

    /*
    public void setList(Vector l){
	vec.removeAllElements();

	for(Iterator it=l.iterator() ; it.hasNext() ; ){
	    vec.add(it.next());
	}
    }
    public Vector getList(){
	return vec;
    }
    */
    // for vector
    public UDF_ExtentElem firstElement(){
	return (UDF_ExtentElem)vec.firstElement();
    }
    public UDF_ExtentElem lastElement(){
	return (UDF_ExtentElem)vec.lastElement();
    }

    public void setSize(int newsize){
	vec.setSize(newsize);
    }
    
    public void add(int index, Object element){
	vec.add(index, element);
    }

    public void removeElementAt(int index){
	vec.removeElementAt(index);
    }

    public void insertElementAt(Object obj, int index){
	vec.insertElementAt(obj, index);
    }

    public Object remove(int index){
	return vec.remove(index);
    }

}


