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
package com.udfv.udf200;

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

public class UDF_long_ad_ADImpUse extends com.udfv.udf102.UDF_long_ad_ADImpUse
{

    /**
       @param elem 
       @param name 
     */
    public UDF_long_ad_ADImpUse(UDF_Element elem, String prefix, String name){
        super(elem, prefix, name == null ? "UDF_long_ad_ADImpUse" : name);	
    }

//begin:add your code here
    public UDF_ErrorList verify() throws UDF_Exception{
	return verifyBase(UDF_Error.C_UDF200);
    }
//end:
};
