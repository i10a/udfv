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
import com.udfv.udf201.*;

public class UDF_SparablePartMap extends com.udfv.udf250.UDF_SparablePartMap
{
    /**
        @param elem 親
        @param prefix ネームスコープ
        @param name 名前
    */
    public UDF_SparablePartMap(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? "UDF_SparablePartMap" : name);
    }
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	if(env.udf_revision != 0x260)
	    return super.verify();
	
	
	el.addError(verifyBase(UDF_Error.C_UDF260, "2.2.9"));
	
	el.setRName("UDF Sparable Partition Map");
	el.addError(super.verify());
	el.setGlobalPoint(getGlobalPoint());
	return el;
    }
}
