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

public class UDF_VDS extends com.udfv.udf250.UDF_VDS
{
    public UDF_VDS(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? "UDF_VDS" : name);
    }
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	if(env.udf_revision != 0x260)
	    return super.verify();
	
	el.addError(verifyBase(UDF_Error.C_UDF260));
	
	el.setRName("Volume Descriptor Sequence");
	el.addError(super.verify());
	
	return el;
    }
}
