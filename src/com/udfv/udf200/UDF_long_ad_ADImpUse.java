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
