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
package com.udfv.udf150;

import java.io.*;
import java.util.*;
import org.w3c.dom.*;
import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.encoding.*;
import com.udfv.access.*;
import com.udfv.exception.*;
import com.udfv.ecma167.*;

/**
*/
public interface UDF_VirtualAllocTable
{
    /**
	vATEntryを取得する。

	@return 取得したvATEntry を返す。
    */
    public UDF_bytes getVATEntry();
    /**
	previousVATICBLocationを取得する。

	@return 取得したpreviousVATICBLocation を返す。
    */
    public UDF_uint32 getPreviousVATICBLocation();

    /**
	vATEntryを設定する。

	@param	v 設定する値。
    */
    public void setVATEntry(UDF_bytes v);
    /**
	previousVATICBLocationを設定する。

	@param	v 設定する値。
    */
    public void setPreviousVATICBLocation(UDF_uint32 v);

};
