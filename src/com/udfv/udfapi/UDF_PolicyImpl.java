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

public class UDF_PolicyImpl implements UDF_Policy{
    UDF_DescTagPolicy desctag_policy;
    UDF_FIDPolicy fid_policy;
    UDF_FEPolicy fe_policy;
    UDF_AllocPolicy alloc_rt_policy;
    UDF_AllocPolicy alloc_data_policy;
    UDF_AllocPolicy alloc_desc_policy;
    UDF_RegidPolicy regid_policy;
    UDF_EncodingPolicy encoding_policy;
    UDF_PartMapPolicy partmap_policy;
    UDF_DescPolicy desc_policy;
    UDF_VRSPolicy vrs_policy;
    UDF_VolPolicy vol_policy;

    /**

     */
    public UDF_PolicyImpl(){
	/*　データを置くパーティション番号、ディスクリプタを置くパーティション番号ともに０で初期化　*/
	this(0, 0);
    }
    /**
       @param desc_partno	データを置くパーティション番号
       @param data_partno	デスクリプタを置くパーティション番号
     */
    public UDF_PolicyImpl(int desc_partno, int data_partno){
	desctag_policy = new UDF_DescTagPolicyImpl();
	fid_policy = new UDF_FIDPolicyImpl();
	//fe_policy = new UDF_FEPolicyImpl();
	fe_policy = new UDF_FEPolicyNoExtendedAttr();
	alloc_data_policy = new UDF_AllocPolicyImpl(1024, desc_partno, 16);
	alloc_desc_policy = new UDF_AllocPolicyImpl(0, data_partno);
	regid_policy = new UDF_RegidPolicyImpl();
	encoding_policy = new UDF_EncodingPolicyImpl();
	partmap_policy = new UDF_PartMapPolicyImpl();
	desc_policy = new UDF_DescPolicyImpl();
	vrs_policy = new UDF_VRSPolicyImpl();
	vol_policy = new UDF_VolPolicyImpl();
    }

    //
    public UDF_DescTagPolicy getDescTagPolicy(){
	return desctag_policy;
    }
    public UDF_FIDPolicy getFIDPolicy(){
	return fid_policy;
    }
    public UDF_FEPolicy getFEPolicy(){
	return fe_policy;
    }
    public UDF_AllocPolicy getAllocDescPolicy(){
	return alloc_desc_policy;
    }
    public UDF_AllocPolicy getAllocDataPolicy(){
	return alloc_data_policy;
    }
    public UDF_RegidPolicy getRegidPolicy(){
	return regid_policy;
    }
    public UDF_EncodingPolicy getEncodingPolicy(){
	return encoding_policy;
    }
    public UDF_PartMapPolicy getPartMapPolicy(){
	return partmap_policy;
    }
    public UDF_DescPolicy getDescPolicy(){
	return desc_policy;
    }
    public UDF_VRSPolicy getVRSPolicy(){
	return vrs_policy;
    }
    public UDF_VolPolicy getVolPolicy(){
	return vol_policy;
    }

    //
    public void setDescTagPolicy(UDF_DescTagPolicy policy){
	desctag_policy = policy;
    }
    public void setFIDPolicy(UDF_FIDPolicy policy){
	fid_policy = policy;
    }
    public void setFEPolicy(UDF_FEPolicy policy){
	fe_policy = policy;
    }
    public void setAllocDescPolicy(UDF_AllocPolicy policy){
	alloc_desc_policy = policy;
    }
    public void setAllocDataPolicy(UDF_AllocPolicy policy){
	alloc_data_policy = policy;
    }
    public void setRegidPolicy(UDF_RegidPolicy policy){
	regid_policy = policy;
    }
    public void setEncodingPolicy(UDF_EncodingPolicy policy){
	encoding_policy = policy;
    }
    public void setPartMapPolicy(UDF_PartMapPolicy policy){
	partmap_policy = policy;
    }
    public void setDescPolicy(UDF_DescPolicy policy){
	desc_policy = policy;
    }
    public void setVRSPolicy(UDF_VRSPolicy policy){
	vrs_policy = policy;
    }
    public void setVolPolicy(UDF_VolPolicy policy){
	vol_policy = policy;
    }

}
