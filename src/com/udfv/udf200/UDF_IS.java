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

/**
   UDF_ISは Extentからデータをリードするクラス。
   Logical Vol Integrity Desc を先頭にデスクリプタが複数並び、
   desc8で終端されることが期待されている。
 */
public class UDF_IS extends com.udfv.udf150.UDF_IS
{
    public UDF_IS(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? "UDF_IS" : name);
    }
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	if(env.udf_revision != 0x200)
	    return super.verify();
	
	el.addError(verifyBase(UDF_Error.C_UDF200));
	el.setRName("Logical Volume Integrity Sequence");
	el.addError(super.verify());
	
	return el;
    }
}
