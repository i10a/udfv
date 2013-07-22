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
import com.udfv.access.*;
import com.udfv.exception.*;
import com.udfv.ecma167.*;

/**
   UDF_FDSは Extentからデータをリードするクラス。
   デスクリプタが複数並び、desc8で終端されることが期待されている。
*/
public class UDF_FDS extends com.udfv.ecma167.UDF_FDS
{
    public UDF_FDS(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? "UDF_FDS" : name);
    }
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	if(env.udf_revision != 0x102)
	    return super.verify();
	
	el.addError(verifyBase(UDF_Error.C_UDF102));
	
	el.setRName("File Set Descriptor Sequence");
	el.setGlobalPoint(getGlobalPoint());
	el.addError(super.verify());
	
        return el;
    }
    /**
      File Set Descriptor Sequenceの検証を行います。

      ※Multi File Setへの対応は保留。
    */
    public UDF_ErrorList verifyBase(short category) throws UDF_Exception {

        UDF_ErrorList el = new UDF_ErrorList();
	
        int num_of_fsd = 0;

        UDF_ElementBase [] child = getChildren();
        for (int i = 0, max = child.length; i < max; i++) {

            String tag_name = child[i].getName();

            if (tag_name == null) {
                //　起こりえない？　//
                continue;
            }

            if (tag_name.equals("UDF_pad") || tag_name.equals("extents")) {
                continue;
            }

            if (tag_name.equals("UDF_desc256")) {

                //　File Set Descriptor を検証　//
		// 基底クラスで実行
///                UDF_desc256 desc256 = (UDF_desc256) child[i];
//                el.addError(desc256.verify());

                num_of_fsd++;
                continue;
            }

            if (tag_name.equals("UDF_desc8")) {

                //　Terminate Descriptorを検証　//
		// 基底クラスで実行
//                UDF_desc8 desc8 = (UDF_desc8) child[i];
//                el.addError(desc8.verify());
                continue;
            }
        }

        //　File Setは基本的に一つしか保持しません。しかし、WORMだけは複数持つことが可能です　//
        if (1 < num_of_fsd) {

            el.addError(
                new UDF_Error(
                    category, UDF_Error.L_CAUTION, "",
                    "Only one FileSet descriptor shall be recorded. On WORM media, multiple FileSets may be recorded.",
                    "2.3.2"
                )
            );
        }
	
        return el;
    }
}
