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
package com.udfv.exception;
import com.udfv.core.*;
import com.udfv.util.*;

/**
   ICBに関する例外
 */
public class UDF_ICBException extends UDF_Exception
{
    /**
       ICBのファイルタイプが期待したものでない。infoに問題のファイルタイプを入れること
     */
    public static final short C_BADFILETYPE = 1;
    /**
       ICBのフラグが期待したものでない。infoに問題のファイルタイプを入れること
     */
    public static final short C_BADFLAGS = 2;

    public UDF_ICBException(UDF_Element elem, String msg, short typ, short category, int info){
	super(elem, msg, typ, category, info);
    }
    public UDF_ICBException(UDF_Element elem, String msg){
	super(elem, msg);
    }
}
