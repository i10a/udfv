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
package com.udfv.udf201;

import java.io.*;
import java.util.*;
import java.util.zip.*;
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
import org.apache.xml.serialize.*;
import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.exception.*;
import com.udfv.access.*;
import com.udfv.ecma167.*;

/**
   UDF_Imageは UDF2.01に対応したイメージを操作するクラスである。
 */
public class UDF_Image extends com.udfv.udf200.UDF_Image
{
    public UDF_Image(){
	super();
    }
    
    public UDF_Image(Document document){
	super( document);
    }
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	final short category = UDF_Error.C_UDF201;
	
	
	if(env.udf_revision != 0x201)
	    return super.verify();
	
	el.addError(verifyBase(category));
	el.addError(super.verify());
	
	return el;
    }
    
    
    //_/_/ Protected /_/_/
    

    /*
       使用するパッケージの優先順位を設定する。
    private void setPkgPriority(){
	setPkgPriority(0x201);
    }
    */
}
