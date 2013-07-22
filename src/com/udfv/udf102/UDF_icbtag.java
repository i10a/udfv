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
import com.udfv.exception.*;
import com.udfv.access.*;

public class UDF_icbtag extends com.udfv.ecma167.UDF_icbtag
{

  /**
	@param elem 親
	@param name 名前
  */
  public UDF_icbtag(UDF_Element elem, String prefix, String name){
     super(elem, prefix, name == null ? "UDF_icbtag" : name);	
  }

//begin:add your code here
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	if(env.udf_revision != 0x102)
	    return super.verify();
	
	el.addError(verifyBase(UDF_Error.C_UDF102));
	
        int flags_value = getFlags().getIntValue();
        //　Non-relocatable は０であることが推奨　//
        if (0 != (flags_value & (1 << 4))) {

            el.addError(
                new UDF_Error(
                    UDF_Error.C_UDF102, UDF_Error.L_WARNING, "Flags(Non-relocatable)",
                    "Should be set to ZERO.",
                    "2.3.5.4"
               )
            );
        }
	
	el.setRName("ICB Tag");
	el.setGlobalPoint(getGlobalPoint());
	el.addError(super.verify());
	
	return el;
    }
    
    public UDF_ErrorList verifyBase(short category) throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
        //　StrategyTypeは 4か4096のどちらかを使用します。4096はWORMに対してのみ使用します　//
        int strategy = getStrategyType().getIntValue();
        if (4096 == strategy) {

            el.addError(
                new UDF_Error(
                    category, UDF_Error.L_CAUTION, "StrategyType",
                    "NOTE: Strategy type 4096, defined in section 6.6, is intended for use on WORM media. Strategy type 4096 is allowed only for ICBs in a partition with Access Type write-once recorded on non-sequential write once media.",
                    "2.3.5.1"
                )
            );
        }
        else
        if (4 != strategy) {

            el.addError(
                new UDF_Error(
                    category, UDF_Error.L_ERROR, "StrategyType",
                    "Shall be set to 4 or 4096, see NOTE .",
                    "2.3.5.1"
                )
            );
        }
	
        //　UDF 2.50規格で使用されるADはshort_adとlong_ad です　//
        //　※emmbedなファイルはADを持たないので３はありえます　//
        int flags_value = getFlags().getIntValue();
        int ad_type = flags_value & 0x0003;
        if (ad_type == 2) {

            el.addError(
                new UDF_Error(
                    category, UDF_Error.L_ERROR, "Flags(Allocation Descriptor Type)",
                    "2. Basic Restrictions & Requirements : Allocation Descriptors | Only Short and Long Allocation Descriptors shall be recorded.",
                    "2.3.5.4."
               )
            );
        }
	
	//　Sortedは０であることが必須　//
        if (0 != (flags_value & (1 << 3))) {

            el.addError(
                new UDF_Error(
                    category, UDF_Error.L_ERROR, "Flags(Sorted)",
                    "Shall be set to ZERO.",
                    "2.3.5.4"
               )
            );
        }
	
	//　Non-relocatable はUDF2.01 から微妙に解釈が異なる

        //　Contiguousは０であることが推奨　//
        if (0 != (flags_value & (1 << 9))) {

            el.addError(
                new UDF_Error(
                    category, UDF_Error.L_WARNING, "Flags(Contiguous)",
                    "Should be set to ZERO.",
                    "2.3.5.4"
               )
            );
        }

        //　Transformed は０であることが必須　//
        if (0 != (flags_value & (1 << 11))) {

            el.addError(
                new UDF_Error(
                    category, UDF_Error.L_ERROR, "Flags(Transformed)",
                    "Shall be set to ZERO.",
                    "2.3.5.4"
               )
            );
        }

        //　Multi-versionsは０であることが必須　//
        if (0 != (flags_value & (1 << 12))) {

            el.addError(
                new UDF_Error(
                    category, UDF_Error.L_ERROR, "Flags(Multi-versions)",
                    "Shall be set to ZERO.",
                    "2.3.5.4"
               )
            );
        }
	
	return el;
    }
    
//end:
};
