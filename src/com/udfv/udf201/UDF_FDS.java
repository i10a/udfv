/*

*/
package com.udfv.udf201;

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

public class UDF_FDS extends com.udfv.udf200.UDF_FDS
{
    public UDF_FDS(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? "UDF_FDS" : name);
    }
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	if(env.udf_revision != 0x201)
	    return super.verify();
	
	el.addError(verifyBase(UDF_Error.C_UDF201));
	
	el.setRName("File Set Descriptor Sequence");
	el.setGlobalPoint(getGlobalPoint());
	el.addError(super.verify());
	
        return el;
    }
}
