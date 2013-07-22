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
import com.udfv.ecma167.*;
import com.udfv.exception.*;
import com.udfv.core.*;

/**
   VRSに関するポリシー
 */
public interface UDF_VRSPolicy {
    /**
       VRS を作る

       @param image	イメージ
       @param policy	ポリシー
    */
    public UDF_VRS createVRS(UDF_Image image, UDF_Policy policy, int sec) throws UDF_Exception, IOException;
    public void createECMA119Bridge(UDF_Image image, UDF_Policy policy) throws UDF_Exception,IOException;
}
