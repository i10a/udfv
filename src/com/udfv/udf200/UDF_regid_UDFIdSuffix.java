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
import com.udfv.ecma167.*;
import com.udfv.exception.*;
import com.udfv.access.*;
import com.udfv.udf200.*;

public class UDF_regid_UDFIdSuffix extends com.udfv.udf150.UDF_regid_UDFIdSuffix
{
  /**
	@param elem 親
	@param name 名前
  */
  public UDF_regid_UDFIdSuffix(UDF_Element elem, String prefix, String name){
     super(elem, prefix, name == null ? "UDF_regid_UDFIdSuffix" : name);	
  }

//begin:add your code here
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = super.verify();
	
	
	if(env.udf_revision != 0x200)
	    return el;
	
	final short category = UDF_Error.C_UDF200;
	
	// リビジョンチェック
	if(getUDFRevision().getIntValue() != 0x200){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "UDF Revision",
			 "The UDFRevision field shall contain #0200 to indicate revision 2.00.",
			 "2.1.5.3", String.valueOf(getUDFRevision().getIntValue()), "512"));
	}
	
	// Reserved は0
	if(!UDF_Util.isAllZero(getReserved().getData())){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Reserved",
			 "Implementation use Entity Identifiers defined by UDF(appendix 6.1) " +
			 "the IdentifierSuffix field shall be constructed as follows.", "2.1.5.3"));
	}
	
	el.setGlobalPoint(getGlobalPoint());
	return el;
    }
//end:
};
