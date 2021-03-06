/*
   Copyright 2013 Heart Solutions Inc.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
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
import com.udfv.ecma167.*;
import com.udfv.exception.*;
import com.udfv.access.*;
import com.udfv.udf201.*;

/**
Alternate Permissions を表現するクラス。

*/
public class UDF_attr3 extends com.udfv.ecma167.UDF_attr3
{
    /**
	コンストラクタ

	@param elem 親
	@param prefix ネームスペース
	@param name 名前
    */
    public UDF_attr3(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }

    /**
      UDF 1.20規格の検証をする。
    */
    public UDF_ErrorList verify() throws UDF_Exception{

	/*　ECMA-167の検証へ　*/
	UDF_ErrorList el = super.verify();

	/*　UDF Revisionが一致しなければ検証を行わない　*/
	if(env.udf_revision != 0x102)
	    return el;

	// この構造は存在してはいけない
	el.addError(new UDF_Error
		    (UDF_Error.C_UDF102, UDF_Error.L_ERROR, "", "This structure shall not be recorded.", "3.3.4.2"));

	el.setRName("Alternate Permissions Extended Attribute");
	return el;
    }

};
