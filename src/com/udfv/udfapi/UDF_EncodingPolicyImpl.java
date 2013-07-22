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
import com.udfv.encoding.*;

public class UDF_EncodingPolicyImpl implements UDF_EncodingPolicy {
    /**
       @param image	UDF_Image
       @param policy	ポリシー
       @param str	エンコードしようとする文字列
    */
    public UDF_Encoding create(UDF_Image image, UDF_Policy policy, String str) throws UDF_Exception,IOException{
	return new UDF_EncodingOSTACompressedUnicode();
    }
}
