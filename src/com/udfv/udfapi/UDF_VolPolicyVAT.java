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
package com.udfv.udfapi;

import java.lang.*;
import java.io.*;
import com.udfv.core.*;
import com.udfv.ecma167.*;
import com.udfv.exception.*;
import com.udfv.access.*;
import com.udfv.encoding.*;
import com.udfv.util.*;

/**
   VATつきでフォーマットするクラス。
 */
public class UDF_VolPolicyVAT extends UDF_VolPolicyImpl implements UDF_VolPolicy {
    public UDF_VolPolicyVAT(){
	;
    }
    //Volmue作成パラメータ
    public int getPartLen(){return 2297888 - 544;};
    public int getUSBLen(){return 0;};
    public int getUSBPos(){return 0;};
    public int getRootLbn(){return 2;};
    public int getRootPartno(){return 1;};
    public int getSRootLbn(){return 4;};
    public int getSRootPartno(){return 1;};
    public String[] getPartMapId(){return new String[]{"", "*UDF Virtual Partition"};};

    //以下 interfaceにない項目
    public boolean has2ndAnchor(){
	return false;
    }
    public boolean has3rdAnchor(){
	return false;
    }
    public boolean has2ndVRS(){
	return false;
    }
    
}
