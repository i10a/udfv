/*
*/
package com.udfv.udf250;

import java.io.*;
import java.util.*;
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.access.*;
import com.udfv.exception.*;
import com.udfv.ecma167.*;

public class UDF_FDS extends com.udfv.udf201.UDF_FDS
{
    /**
       コンストラクタ

       @param elem 親
       @param prefix ネームスコープ
       @param name 名前
    */
    public UDF_FDS(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? "UDF_FDS" : name);
    }

    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	if(env.udf_revision != 0x200)
	    return super.verify();
	
	el.addError(verifyBase(UDF_Error.C_UDF200));
	
	el.setRName("File Set Descriptor Sequence");
	el.setGlobalPoint(getGlobalPoint());
	el.addError(super.verify());
	
        return el;
    }
}
