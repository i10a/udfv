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
