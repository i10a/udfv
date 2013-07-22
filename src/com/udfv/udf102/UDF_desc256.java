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
import com.udfv.ecma167.*;

/**
  File Set Descriptor を表現するクラス。

*/
public class UDF_desc256 extends com.udfv.ecma167.UDF_desc256
{
    /**
      コンストラクタ。

      @param elem 親
      @param prefix ネームスコープ
      @param name 名前
    */
    public UDF_desc256(UDF_Element elem, String prefix, String name){
        super(elem, prefix, name == null ? getUDFClassName() : name);
    }

    /**
        File Set Desc. の情報を表示。

    */
    public String [] toInformation(String indent, boolean detail) {

	String nl = UDF_Util.getSystemNewLine();

	Vector vec = new Vector();
	String str;

	int idx = 0;
	str = indent + "Interchange Level         # " + getInterchangeLevel().getIntValue();
	vec.add(str);
	str = indent + "File Set Number           # " + getFileSetNumber().getIntValue();
	vec.add(str);
	str = indent + "File Set Desc. Number     # " + getFileSetDescNumber().getIntValue();
	vec.add(str);

	com.udfv.ecma167.UDF_charspec cs = getLogicalVolIdCharSet();
	str = indent + "Logical Vol. Id. Char. Set# Char. Set Type  # " + cs.getCharSetType().getIntValue() + nl
	    + indent + "                          # Char. Set Info. # " + UDF_Util.b2qstr(cs.getCharSetInfo().getData());
	vec.add(str);
	str = indent + "Logical Vol. Id.          # " + getLogicalVolId().getStringData();
	vec.add(str);

	cs = getFileSetCharSet();
	str = indent + "File Set Char. Set        # Char. Set Type  # " + cs.getCharSetType().getIntValue() + nl
	    + indent + "                          # Char. Set Info. # " + UDF_Util.b2qstr(cs.getCharSetInfo().getData());
	vec.add(str);
	str = indent + "File Set Id.              # " + getFileSetId().getStringData();
	vec.add(str);

	if (detail) {
	    str = indent + "Copyright File Id.        # " + getCopyrightFileId().getStringData();
	    vec.add(str);
	    str = indent + "Abstract File Id.         # " + getAbstractFileId().getStringData();
	    vec.add(str);
	}

	com.udfv.ecma167.UDF_long_ad ad = getRootDirectoryICB();
	str = indent + "Root Dir. ICB             # long_ad refno   # " + ad.getPartRefNo() + nl
	    + indent + "                          # long_ad lbn     # " + ad.getLbn() + nl
	    + indent + "                          # long_ad len     # " + ad.getLen();
	vec.add(str);

	com.udfv.ecma167.UDF_regid autoDomainId = getDomainId();
	com.udfv.udf102.UDF_regid_UDFDomainIdSuffix suffix = (UDF_regid_UDFDomainIdSuffix) autoDomainId.getIdSuffix().getFirstChild();
	str = indent + "Domain Id                 # Id              # " + autoDomainId.getId().getStringData() + nl
	    + indent + "                          # UDF Revision    # " + suffix.getUDFRevision().getIntValue() + nl
	    + indent + "                          # Domain Flags    # " + suffix.getDomainFlags().getIntValue();
	vec.add(str);

	ad = getSystemStreamDirectoryICB();
	str = indent + "System Stream Dir. ICB    # long_ad refno   # " + ad.getPartRefNo() + nl
	    + indent + "                          # long_ad lbn     # " + ad.getLbn() + nl
	    + indent + "                          # long_ad len     # " + ad.getLen();
	vec.add(str);

	String [] sl = new String[vec.size()];
	for (int i = 0, max = sl.length; i < max; i++) {
	    sl[i] = (String)vec.elementAt(i);
	}
	vec = null;

	return sl;
    }

    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	if(env.udf_revision != 0x102)
	    return super.verify();
	
	el.addError(verifyBase(UDF_Error.C_UDF102));
	
	el.setRName("File Set Descriptor");
	el.setGlobalPoint(getGlobalPoint());
	el.addError(super.verify());
	
	return el;
    }
    
    /**
       UDF 全リビジョン共通のFile Set Descriptor の検証を行う。
       
       @param category  エラーカテゴリ。
       @return エラーリストインスタンス。
    */
    public UDF_ErrorList verifyBase(short category) throws UDF_Exception {

        UDF_ErrorList el = new UDF_ErrorList();
        UDF_Error err;
	
	
	// 全体のサイズが512バイトを超えてはならない
	if(512 < getSize()){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "",
			 "The total length of a File Set Descriptor shall not exceed the 512 byte.",
			 "5", String.valueOf(getSize()), ""));
	}
	
        //　InterchangeLevel, MaximumInterchangeLevel は３に設定するべきです　//
        int interchange_level = getInterchangeLevel().getIntValue();
        if (3 != interchange_level) {

            el.addError(
                new UDF_Error(
                    category, UDF_Error.L_ERROR, "InterchangeLevel",
                    "Shall be set to a level of 3.",
                    "2.3.2.1",
                    interchange_level,
                    3
                )
            );
        }

        int maximum_interchange_level = getMaxInterchangeLevel().getIntValue();
        if (3 != maximum_interchange_level) {

            el.addError(
                new UDF_Error(
                    category, UDF_Error.L_ERROR, "MaximumInterchangeLevel",
                    "Shall be set to a level of 3.",
                    "2.3.2.2",
                    maximum_interchange_level,
                    3
                )
            );
        }

        //　CharacterSetList, MaximumCharacterSetList は CS0のみをサポートするようにしなければならない　//
        int character_set_list = getCharSetList().getIntValue();
        if (1 != character_set_list) {

            el.addError(
                new UDF_Error(
                    category, UDF_Error.L_ERROR, "CharacterSetList",
                    "Shall be set to indicate support for CS0 only as defined in 2.1.2.",
                    "2.3.2.3",
                    character_set_list,
                    1
                )
            );
        }

        int maximum_character_set_list = getMaxCharSetList().getIntValue();
        if (1 != maximum_character_set_list) {

            el.addError(
                new UDF_Error(
                    category, UDF_Error.L_ERROR, "MaximumCharacterSetList",
                    "Shall be set to indicate support for CS0 only as defined in 2.1.2.",
                    "2.3.2.4",
                    maximum_character_set_list,
                    1
                )
            );
        }

        //　LogicalVolumeIdentifierCharacterSet, FileSetCharacterSetは CS0で記載されているべきです　//
        UDF_charspec logical_volume_identifier_character_set = (UDF_charspec)getLogicalVolIdCharSet();

        err = logical_volume_identifier_character_set.verifyCharSetType(0);
        if (err.isError()) {

            err.setCategory(category);
            err.setLevel(UDF_Error.L_ERROR);
            err.setRName("LogicalVolumeIdentifierCharacterSet");
            err.setMessage("Shall be set to indicate support for CS0 as defined in 2.1.2.");
            err.setRefer("2.3.2.5");
            el.addError(err);
        }

        UDF_charspec file_set_character_set = (UDF_charspec)getFileSetCharSet();

        err = file_set_character_set.verifyCharSetType(0);
        if (err.isError()) {

            err.setCategory(category);
            err.setLevel(UDF_Error.L_ERROR);
            err.setRName("FileSetCharacterSet");
            err.setMessage("Shall be set to indicate support for CS0 as defined in 2.1.2.");
            err.setRefer("2.3.2.6");
            el.addError(err);
        }

        //　DomainIdentifierには文字列 "*OSTA UDF Compliant"が保存されている必要があります　//
        UDF_regid entity_identifier = (UDF_regid) getDomainId();
        el.addError(entity_identifier.verify());
        err = entity_identifier.verifyId("*OSTA UDF Compliant");
        if (err.isError()) {

            err.setCategory(category);
            err.setRName("DomainIdentifier");
            err.setMessage("This field shall indicate that the scope of this File Set Descriptor conforms to the domain defined in this document, therefore the ImplementationIdentifier shall be set to: \"*OSTA UDF Compliant\"");
            err.setRefer("2.3.2.7");
        }

        return el;
    }
}
