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
import com.udfv.exception.*;
import com.udfv.access.*;
import com.udfv.ecma167.*;
import com.udfv.encoding.*;

public class UDF_desc257 extends com.udfv.udf201.UDF_desc257
{
    /**
      コンストラクタ

      @param elem 親
      @param prefix ネームスコープ
      @param name 名前
    */
    public UDF_desc257(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
    }
    public UDF_ErrorList verify() throws UDF_Exception {
	
        UDF_ErrorList el = new UDF_ErrorList();
	
	
	if(env.udf_revision != 0x250)
	    return super.verify();
	
	el.addError(verifyBase(UDF_Error.C_UDF250));
	el.setRName("File Identifier Descriptor");
	
        el.addError(super.verify());
        el.setGlobalPoint(getGlobalPoint());
	return el;
    }

    /**
      UDF 2.50規格のFile Identifier Descriptorを検証します。<br>
      <em>
      ※注意事項<br>
      ライトワンスメディアに書き込むときには、File Identifier Descriptorの
      Descriptor Tagが論理ブロック境界をまたがないように、Implementation Useを
      使ってサイズ調整を行います。<br>
      しかし、File Identifier Descriptorの単体テストでは検証ができませんので
      ここでは行わないことにします。<br>
      File Entryにて検証することを検討します。(2005/03/09)
      </em>
    */
    public UDF_ErrorList verifyBase(short category) throws UDF_Exception {

        UDF_ErrorList el = new UDF_ErrorList();
        boolean error_flag = false;
	
	
        el.addError(super.verifyBase(category));


        UDF_bytes file_identifier = getFileId();
        int file_length = getLenOfFileId().getIntValue();

        //　File Identifier のエンコーディングを読み取ります　//
        UDF_Encoding enc = file_identifier.getEncoding();

        int file_characteristics = getFileChar().getIntValue();
        //　FileIdentifierの長さは親ディレクトリを指すとき以外は１以上でなければならない　//
        if (0 == (file_characteristics & PARENT)) {

            if (file_length < 1) {

                el.addError(
                    new UDF_Error(
                        UDF_Error.C_UDF250, UDF_Error.L_ERROR, "LengthOfFileIdentifier",
                        "The byte length of this field shall be greater than 1 with the sole exception of 0 for a parent FID.",
                        "2.3.4.6"
                    )
                );
                error_flag = true;
            }
        }

        //　エンコーディングが内部的に必要です　//
        if (enc == null) {

            el.addError(
                new UDF_Error(
                    UDF_Error.C_UDF250, UDF_Error.L_CAUTION, "FileIdentifier",
                    "Contains a File Identifier stored in the OSTA Compressed Unicode format, see 2.1.1.",
                    "2.3.4.6"
                )
            );
            error_flag = true;
        }

        return el;
    }
}
