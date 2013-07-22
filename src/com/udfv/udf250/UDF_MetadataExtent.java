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
package com.udfv.udf250;

import java.io.*;
import java.util.*;
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
import com.udfv.exception.*;
import com.udfv.core.*;
import com.udfv.util.*;

/**
   このクラスは使用しなくなった。

  このクラスは&nbsp;Metadata&nbsp;File,&nbsp;Metadata&nbsp;Mirror&nbsp;File&nbsp;の
  &nbsp;XML書式から情報を読み取るために拡張したのものです。<br>
*/
public class UDF_MetadataExtent extends UDF_Extent
{
    public UDF_MetadataExtent(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? "UDF_MetadataExtent" : name);
    }

    public void readFromXML(Node n) throws UDF_Exception{
	readExtentFromXML(n);

	NodeList nl = n.getChildNodes();
	if(nl == null)
	    return;
	if(nl.getLength() < 2)
	    return;

	for(int i = 0; i < nl.getLength(); ++i) {

	    Node child = nl.item(i);
	    if(child.getNodeType() != Node.ELEMENT_NODE){
	        continue;
	    }

	    String tagName = child.getLocalName();
	    String className = child.getLocalName();

	    if(className.equals("extents"))
		continue;

	    if(className.equals(UDF_XML.FSDS)) {
		UDF_Extent ext = (UDF_Extent) createElement("UDF_Extent", "UDF_Image", tagName);
		ext.readFromXML(child);
		appendChild(ext);
		continue;
	    }
	    if (className.equals(UDF_XML.DIRECTORY)) {
		UDF_Extent ext = (UDF_Extent) createElement("UDF_Directory", "UDF_Image", tagName);
		ext.readFromXML(child);
		appendChild(ext);
		continue;
	    }
	    if (className.equals(UDF_XML.SDIRECTORY)) {
		UDF_Extent ext = (UDF_Extent) createElement("UDF_SDirectory", "UDF_Image", tagName);
		ext.readFromXML(child);
		appendChild(ext);
		continue;
	    }

	    try{
		UDF_Element elm = UDF_Element.genElement(className, this, "UDF_Extent", tagName);
		elm.readFromXML(child);
		appendChild(elm);
		continue;
	    }
	    catch(ClassNotFoundException e){
		throw new UDF_XMLException(this, "XML: Unknown Element:" + className);
	    }
	}
    }
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	if(env.udf_revision != 0x250)
	    return super.verify();
	
	UDF_ElementBase[] eb = getChildren();
	final String ERRSTR = 
	    "The Allocation Descriptors for Metadata File shall describe only logical blocks which contain " +
	    "one of the below data types. No user data or other metadata may be referenced.\n" +
	    "* FSD\n* ICB\n* Extent of Allocation Descriptors(see 2.3.11)\n" +
	    "* Directory or stream directory data(i.e. FIDs)\n* An unused block marked free in the Metadata Bitmap File.";
	
	// 指定されている構造以外の構造が格納されている
	for(int i = 0; i < eb.length; i++){
	    
	    UDF_Element  udfelem = (UDF_Element)eb[i];
	    
	    if(com.udfv.core.UDF_Extent.class.isAssignableFrom(udfelem.getClass())){
		
		// irectory data
		if(com.udfv.ecma167.UDF_Directory.class.isAssignableFrom(udfelem.getClass()))
		    continue;
		// stream directory data
		else if(com.udfv.ecma167.UDF_SDirectory.class.isAssignableFrom(udfelem.getClass()))
		    continue;
		
		UDF_ElementBase[] child = udfelem.getChildren();
		for(int j = 0; j < child.length; j++){
		    
		    UDF_Element udfelem2 = (UDF_Element)child[j];
		    
		    
		    // File Entry/Extended File Entry
		    // （ecma167 ではdesc266 はdesc261 を継承しているので、desc261 だけをチェックすればよい）
		    if(com.udfv.ecma167.UDF_desc261.class.isAssignableFrom(udfelem2.getClass()))
			continue;
		    // Extent of Allocation Descriptors(see 2.3.11)
		    else if(com.udfv.core.UDF_Data.class.isAssignableFrom(udfelem2.getClass()))
			continue;
		    // FSD（FSDS ではない）
		    else if(com.udfv.ecma167.UDF_desc256.class.isAssignableFrom(udfelem2.getClass()))
			continue;
		    // 以下の2つはたぶん来ない
		    // Indirect Entry
		    else if(com.udfv.ecma167.UDF_desc259.class.isAssignableFrom(udfelem2.getClass()))
			continue;
		    // Termina Entry
		    else if(com.udfv.ecma167.UDF_desc260.class.isAssignableFrom(udfelem2.getClass()))
			continue;
		    // An unused block marked free in the Metadata Bitmap File
		    else if(com.udfv.ecma167.UDF_pad.class.isAssignableFrom(udfelem2.getClass()))
			continue;
		    
		    UDF_Error err = new UDF_Error(UDF_Error.C_UDF250, UDF_Error.L_ERROR,
					      "Metadata Partition(" + udfelem2.getName() + ")", ERRSTR, "2.2.13.1");
		    err.setGlobalPoint(udfelem2.getGlobalPoint());
		    el.addError(err);
		}
	    }
	    // An unused block marked free in the Metadata Bitmap File
	    else if(com.udfv.ecma167.UDF_pad.class.isAssignableFrom(udfelem.getClass()))
		continue;
	    else{
		
		el.addError(new UDF_Error(UDF_Error.C_UDF250, UDF_Error.L_ERROR,
					  "Metadata Partition(" + udfelem.getName() + ")", ERRSTR, "2.2.13.1"));
	    }
	}
	
	el.setGlobalPoint(getGlobalPoint());
	return el;
    }
}


