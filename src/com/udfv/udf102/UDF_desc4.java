/*
*/
package com.udfv.udf102;

import java.io.*;
import java.util.*;
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.exception.*;
import com.udfv.access.*;
import com.udfv.udf102.*;

public class UDF_desc4 extends com.udfv.ecma167.UDF_desc4
{

  /**
	@param elem 親
	@param name 名前
  */
  public UDF_desc4(UDF_Element elem, String prefix, String name){
     super(elem, prefix, name == null ? "UDF_desc4" : name);	
     init();
  }

//begin:add your code here
    public void postReadHook(UDF_RandomAccess f) throws UDF_Exception{
	UDF_LVInformation d = (UDF_LVInformation)createElement("UDF_LVInformation", "UDF_desc4", "UDF_LVInformation");
	getImplUse().readFromAndReplaceChild(d);
    }

    /**
        Impl. Use Vol. Desc. の情報を表示。

    */
    public String [] toInformation(String indent, boolean detail) {

	String nl = UDF_Util.getSystemNewLine();

	Vector vec = new Vector();
	String str;

	com.udfv.ecma167.UDF_regid autoImplId = getImplId();
	UDF_regid_UDFIdSuffix suffix = (UDF_regid_UDFIdSuffix) autoImplId.getIdSuffix().getFirstChild();
	str = indent + "Impl.Id                   # Id              # " + autoImplId.getId().getStringData() + nl;
	if (detail) {
	    str = str
	    + indent + "                          # UDF Revision    # " + suffix.getUDFRevision().getIntValue() + nl
	    + indent + "                          # OS Class        # " + suffix.getOSClass().getIntValue() + nl
	    + indent + "                          # OS Id           # " + suffix.getOSId().getIntValue();
	}
	vec.add(str);

	com.udfv.udf102.UDF_LVInformation autoLVInfo = (UDF_LVInformation) getImplUse().getFirstChild();
	str = indent + "Impl.Use                  # Logical Vol. Id # " + autoLVInfo.getLogicalVolId().getStringData() + nl;
	if (detail) {
	    str = str
	    + indent + "                          # Lv-info1        # " + autoLVInfo.getLVInfo1().getStringData() + nl
	    + indent + "                          # Lv-info2        # " + autoLVInfo.getLVInfo2().getStringData() + nl
	    + indent + "                          # Lv-info2        # " + autoLVInfo.getLVInfo3().getStringData();
	}
	vec.add(str);

	String [] sl = new String[vec.size()];
	for (int i = 0, max = vec.size(); i < max; i++) {
	    sl[i] = (String)vec.elementAt(i);
	}

	return sl;
    }

    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	if(env.udf_revision != 0x102)
	    return super.verify();
	
	el.addError(verifyBase(UDF_Error.C_UDF102, "2.2.7.1"));
	
	el.setRName("Implementation Use Volume Descriptor");
	el.addError(super.verify());
	
	el.setGlobalPoint(getGlobalPoint());
	return el;
    }
    
    /**
       UDF 1.02 以上のリビジョン共通の検証を行う。
       
       @param category エラーカテゴリ。
       @param refer  参照番号。
       @return エラーリストインスタンス。
    */
    public UDF_ErrorList verifyBase(short category, String refer) throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	UDF_Error ret;
	
	
	// Id は"*UDF LV Info"
	ret = getImplId().verifyId("*UDF LV Info");
	if(ret.isError()){
	    
	    ret.setCategory(category);
	    ret.setMessage("This field shall specify \"*UDF LV Info\".");
	    ret.setRefer(refer);
	    ret.setRName("ImplementationIdentifier");
	    el.addError(ret);
	}
	
	if(512 < getSize()){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "",
			 "The following table summarizes the UDF limitations on the lengths of the Descriptors described in ECMA 167.\n" +
			 "Implementation Use Volume Descriptor: 512", "5.1"));
	}
	
	el.addError(getImplUse().verify("ImplementationUse"));
	
	return el;
    }
//end:
};
