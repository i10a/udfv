/*
*/
package com.udfv.udf260;

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
//import com.udfv.udf201.*;

public class UDF_dstring extends com.udfv.udf250.UDF_dstring
{
    /**
  	@param elem 親
	@param prefix ネームスペース
	@param name 名前
    */
    public UDF_dstring(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name);
    }
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = super.verify();
	
	
	if(env.udf_revision != 0x260)
	    return el;
	
	el.addError(verifyBase(UDF_Error.C_UDF260));
	el.setGlobalPoint(getGlobalPoint());
	el.addError(super.verify());
	
	return el;
    }

//end:
};
