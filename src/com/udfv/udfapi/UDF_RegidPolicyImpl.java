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
import com.udfv.util.*;
import com.udfv.access.*;

/**
regid を作成する。
*/
public class UDF_RegidPolicyImpl implements UDF_RegidPolicy{
    int os_class;
    int os_id;
    int domain_flags;

    int getUDFRevision(UDF_Image image){
	return image.env.recorded_udf_revision;
    }
    /**
       "*Application ID"を生成する。Suffix type は ImplIdSuffixとなる

       @param image	イメージ
       @return regid
     */
    public UDF_regid createApplicationId(UDF_Image image, String name) throws UDF_Exception,IOException{
	
	return createApplicationId(image, "*UDFV");
    }
    
    /**
       "*Developer ID"を生成する。Suffix type は ImplIDSuffixとなる。

       @param image	イメージ
       @return regid
     */
    public UDF_regid createDeveloperId(UDF_Image image, String name) throws UDF_Exception,IOException{
	
	return createImplId(image, "*Heart Solutions.Inc", name);
    }
    
    /**
       IDValueを指定して regidを生成する。Suffix Typeは ImplIdSuffixとなる。

       @param image	イメージ
       @return regid
     */
    public UDF_regid createImplId(UDF_Image image, String idval, String name) throws UDF_Exception,IOException{
	// *Heart Solutions.Inc
	UDF_regid regid = (UDF_regid)image.createElement("UDF_regid", null, name);
	regid.setDefaultValue();
	
	// flags
	regid.getFlags().setValue(0);
	
	// id
	regid.getId().setData(UDF_Util.qstr2b(idval, 23));
	
	// suffix
	com.udfv.udf102.UDF_regid_UDFImplIdSuffix suffix = (com.udfv.udf102.UDF_regid_UDFImplIdSuffix)image.createElement
	    ("UDF_regid_UDFImplIdSuffix", null, null);
	suffix.setDefaultValue();
	
	suffix.getOSClass().setValue(0);
	suffix.getOSId().setValue(0);
	suffix.getImplUseArea().setData(new byte[6]);
	
	regid.getIdSuffix().replaceChild(suffix);
	
	return regid;
    }
    
    /**
       IDValueを指定して regidを生成する。Suffix Typeは UDFIdSuffixとなる。

       @param image	イメージ
       @return regid
     */
    public UDF_regid createUDFId(UDF_Image image, String idval, String name) throws UDF_Exception,IOException{
	
	UDF_regid regid = (UDF_regid)image.createElement("UDF_regid", null, name);
	regid.setDefaultValue();
	
	// flags
	regid.getFlags().setValue(0);
	
	// id
	regid.getId().setData(UDF_Util.qstr2b(idval, 23));
	
	// suffix
	com.udfv.udf102.UDF_regid_UDFIdSuffix suffix = (com.udfv.udf102.UDF_regid_UDFIdSuffix)image.createElement
	    ("UDF_regid_UDFIdSuffix", null, null);
	suffix.setDefaultValue();
	
	suffix.getUDFRevision().setValue(getUDFRevision(image));
	suffix.getOSClass().setValue(os_class);
	suffix.getOSId().setValue(os_id);
	suffix.getReserved().setData(new byte[4]);
	
	
	regid.getIdSuffix().replaceChild(suffix);	

	return regid;
    }
    
    /**
       "*OSTA UDF Compliant"を生成する。Suffix Typeは DomainIdSuffixとなる。

       @param image	イメージ
       @return regid
     */
    public UDF_regid createDomainId(UDF_Image image, String name) throws UDF_Exception,IOException{
	
	UDF_regid regid = (UDF_regid)image.createElement("UDF_regid", null, name);
	regid.setDefaultValue();
	
	// flags
	regid.getFlags().setValue(0);
	
	// id
	regid.getId().setData(UDF_Util.qstr2b("*OSTA UDF Compliant", 23));
	
	// suffix
	com.udfv.udf102.UDF_regid_UDFDomainIdSuffix suffix = (com.udfv.udf102.UDF_regid_UDFDomainIdSuffix)image.createElement
	    ("UDF_regid_UDFDomainIdSuffix", null, null);
	suffix.setDefaultValue();
	
	suffix.getUDFRevision().setValue(getUDFRevision(image));
	suffix.getDomainFlags().setValue(domain_flags);
	suffix.getReserved().setData(new byte[5]);
	
	//regid.replaceChild(suffix, regid.getIdSuffix());
	regid.getIdSuffix().replaceChild(suffix);
	
	return regid;
    }
    
    /**
       IDValueを指定して regidを生成する。suffix Typeは ApplicationIdSuffixとなる。

       @param image	イメージ
       @return regid
     */
    public UDF_regid createApplicationId(UDF_Image image, String idval, String name) throws UDF_Exception,IOException{
	
	UDF_regid regid = (UDF_regid)image.createElement("UDF_regid", null, name);
	regid.setDefaultValue();
	
	// flags
	regid.getFlags().setValue(0);
	
	// id
	regid.getId().setData(UDF_Util.qstr2b(idval, 23));
	
	// suffix
	regid.getIdSuffix().setData(new byte[8]);
	
	return regid;
    }
}
