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
