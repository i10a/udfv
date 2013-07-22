
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
