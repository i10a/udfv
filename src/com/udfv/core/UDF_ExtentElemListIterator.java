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
   UDF_ExtentElemListの Iterator

   @see <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/util/Collection.html">Collection</a>
 */
public class UDF_ExtentElemListIterator implements Iterator{
    UDF_ExtentElemList my_el;
    Iterator it;
    public UDF_ExtentElemListIterator(UDF_ExtentElemList el){
	my_el = el;
	it = el.vec.iterator();
    }
    public boolean hasNext(){
	return it.hasNext();
    }
    public Object next(){
	return it.next();
    }
    public void remove(){
	it.remove();
    }
}
