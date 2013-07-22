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

package com.udfv.udfapi;

import java.lang.*;
import java.io.*;
import com.udfv.core.*;
import com.udfv.ecma167.*;
import com.udfv.exception.*;

public class UDF_DescTagPolicyImpl implements UDF_DescTagPolicy{
    protected int getDescVersionValue(){
	return 3;
    }
    protected int getTagSerialNumberValue(){
	return 1;
    }
    /**
       @param image	UDF_Image
       @param policy	ポリシー
    */
    public UDF_tag create(UDF_Image image, UDF_Policy policy) throws UDF_Exception,IOException{
        UDF_tag tag = (UDF_tag) image.createElement("UDF_tag", null, "desc-tag");
	tag.setDefaultValue();
        tag.getDescVersion().setValue(getDescVersionValue());
        tag.getTagSerialNumber().setValue(getTagSerialNumberValue());
	
	return tag;
    }
}
