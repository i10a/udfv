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

public interface UDF_Policy{
    public UDF_DescTagPolicy getDescTagPolicy();
    public UDF_FIDPolicy getFIDPolicy();
    public UDF_FEPolicy getFEPolicy();
    /**
       データをAllocationする Policy
     */
    public UDF_AllocPolicy getAllocDataPolicy();
    /**
       デスクリプタをAllocationする Policy
     */
    public UDF_AllocPolicy getAllocDescPolicy();
    public UDF_RegidPolicy getRegidPolicy();
    public UDF_EncodingPolicy getEncodingPolicy();
    public UDF_PartMapPolicy getPartMapPolicy();
    public UDF_DescPolicy getDescPolicy();
    public UDF_VRSPolicy  getVRSPolicy();
    public UDF_VolPolicy getVolPolicy();

    public void setDescTagPolicy(UDF_DescTagPolicy policy);
    public void setFIDPolicy(UDF_FIDPolicy policy);
    public void setFEPolicy(UDF_FEPolicy policy);
    public void setAllocDataPolicy(UDF_AllocPolicy policy);
    public void setAllocDescPolicy(UDF_AllocPolicy policy);
    public void setRegidPolicy(UDF_RegidPolicy policy);
    public void setEncodingPolicy(UDF_EncodingPolicy policy);
    public void setPartMapPolicy(UDF_PartMapPolicy policy);
    public void setDescPolicy(UDF_DescPolicy policy);
    public void setVRSPolicy(UDF_VRSPolicy policy);
    public void setVolPolicy(UDF_VolPolicy policy);

}
