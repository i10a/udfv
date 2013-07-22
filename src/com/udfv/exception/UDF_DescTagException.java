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
   デスクリプタタグに関する例外
 */
public class UDF_DescTagException extends UDF_Exception
{
    /**
     TAGのIDが期待したものでない。infoに問題のIDを入れること
     */
    public static final short C_BADTAGID = 1;
    /**
       TAGのCRCが期待したものでない。infoに問題のCRCを入れること
    */
    public static final short C_BADCRC = 2;
    /**
       TAGのchksumが期待したものでない。infoに問題のCHKSUMを入れること   
    */
    public static final short C_BADCHKSUM = 3;
    /**
       TAGのCRCLengthが不正な値である。
    */
    public static final short C_BADCRCLEN = 4;
    

    public int getTagId(){return errinfo;}

    public UDF_DescTagException(UDF_Element elem, String msg, short typ, short category, int info){
	super(elem, msg, typ, category, info);
    }
    public UDF_DescTagException(UDF_Element elem, String msg){
	super(elem, msg);
    }
}
