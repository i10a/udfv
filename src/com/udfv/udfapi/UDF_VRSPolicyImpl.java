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
import java.util.*;
import com.udfv.core.*;
import com.udfv.ecma167.*;
import com.udfv.ecma119.*;
import com.udfv.exception.*;
import com.udfv.access.*;
import com.udfv.util.*;
import com.udfv.encoding.*;
import com.udfv.udf102.UDF_LVInformation;


public class UDF_VRSPolicyImpl implements UDF_VRSPolicy {
    int vrs_nsr = 3;
    int ecma119_loc = 257;

    public UDF_VRS createVRS(UDF_Image image, UDF_Policy policy, int sec) throws UDF_VolException{
	long size = 0;
	if(vrs_nsr != 2 && vrs_nsr != 3){
	    throw new UDF_VolException(null, "bad nsr:" + vrs_nsr);
	}

	UDF_VRS vrs = (UDF_VRS)image.createElement("UDF_VRS", null, "vrs");
	vrs.addExtent(sec, -1, Long.MAX_VALUE);

	UDF_ECMA167_BEA01 bea01 = (UDF_ECMA167_BEA01)image.createElement("UDF_ECMA167_BEA01", null, null);
	bea01.setDefaultValue();
	vrs.appendChild(bea01);
	size += bea01.getSize();

	if(vrs_nsr == 3){
	    UDF_ECMA167_NSR03 nsr03 = (UDF_ECMA167_NSR03)image.createElement("UDF_ECMA167_NSR03", null, null);
	    nsr03.setDefaultValue();
	    vrs.appendChild(nsr03);
	    size += nsr03.getSize();
	}
	else{
	    UDF_ECMA167_NSR02 nsr02 = (UDF_ECMA167_NSR02)image.createElement("UDF_ECMA167_NSR02", null, null);
	    nsr02.setDefaultValue();
	    vrs.appendChild(nsr02);
	    size += nsr02.getSize();
	}

	UDF_ECMA167_TEA01 tea01 = (UDF_ECMA167_TEA01)image.createElement("UDF_ECMA167_TEA01", null, null);
	tea01.setDefaultValue();
	vrs.appendChild(tea01);
	size += tea01.getSize();

	try{
	    vrs.truncExtent(size);
	}
	catch(UDF_InternalException e){
	    e.printStackTrace();//おきないはず
	}
	return vrs;
    }

    UDF_ECMA119PathTable lpath;
    UDF_ECMA119PathTable_be mpath;

    private long align(UDF_Image image, long s){
	return UDF_Util.align(s, image.env.LBS);
    }
    public void createECMA119Bridge(UDF_Image image, UDF_Policy policy) throws UDF_Exception,IOException{
	UDF_ElementList lpath_list = new UDF_ElementList();
	UDF_ElementList mpath_list = new UDF_ElementList();
	UDF_FEDesc root = image.env.getRootFE(0);
    
	{
	    UDF_ECMA119PathTable lpath = (UDF_ECMA119PathTable)image.createElement("UDF_ECMA119PathTable", null, null);
	    lpath.setDefaultValue();
	    String s = "\0";
	    lpath.getLenOfDirectoryId().setValue(s.length());
	    lpath.getPaddingField().setSize(s.length()%2);
	    lpath.getDirectoryId().setStringData(s);
	    lpath_list.add(lpath);
	}
	{
	    UDF_ECMA119PathTable_be mpath = (UDF_ECMA119PathTable_be)image.createElement("UDF_ECMA119PathTable_be", null, null);
	    mpath.setDefaultValue();
	    String s = "\0";
	    mpath.getLenOfDirectoryId().setValue(s.length());
	    mpath.getPaddingField().setSize(s.length()%2);
	    mpath.getDirectoryId().setStringData(s);
	    mpath_list.add(mpath);
	}
	createECMA119LPath(image, policy, root, lpath_list);
	createECMA119MPath(image, policy, root, mpath_list);
	//ここで lpath_listをソートする
	//そのあと parentDirectoryNumberを設定する
	//ここで mpath_listをソートする
	//そのあと parentDirectoryNumberを設定する

	UDF_Extent ext_lpath = (UDF_Extent)image.createElement("UDF_Extent", null, null);
	UDF_Extent ext_mpath = (UDF_Extent)image.createElement("UDF_Extent", null, null);

	long size = 0;
	for(Iterator it=lpath_list.iterator() ; it.hasNext() ; )
	    size += ((UDF_Element)it.next()).getSize();

	ext_lpath.appendChildren(lpath_list);
	ext_lpath.addExtent(ecma119_loc, -1, align(image, size));
	image.appendChild(ext_lpath);
	
	ecma119_loc += align(image, size)/image.env.LBS;

	ext_mpath.appendChildren(mpath_list);
	ext_mpath.addExtent(ecma119_loc, -1, align(image, size));
	image.appendChild(ext_mpath);

	ecma119_loc += align(image, size)/image.env.LBS;

	//createECMA119BridgeSub(image, policy, root);
    }

    void createECMA119LPath(UDF_Image image, UDF_Policy policy, UDF_FEDesc fe, UDF_ElementList list) throws UDF_Exception{
	UDF_ElementList el = fe.getDirectoryList();
	for(Iterator it=el.iterator() ; it.hasNext() ; ){
	    UDF_desc257 d = (UDF_desc257)it.next();
	    if((d.getFileChar().getIntValue() & UDF_desc257.PARENT) != 0)
		continue;
	    if((d.getFileChar().getIntValue() & UDF_desc257.DELETED) != 0)
		continue;
	    if((d.getFileChar().getIntValue() & UDF_desc257.DIRECTORY) != 0){
		UDF_ECMA119PathTable lpath = (UDF_ECMA119PathTable)image.createElement("UDF_ECMA119PathTable", null, null);
		lpath.setDefaultValue();
		String fid = d.getFileId().getStringData();
		String s = fid.substring(2);
		lpath.getLenOfDirectoryId().setValue(s.length());
		lpath.getPaddingField().setSize(s.length()%2);
		lpath.getDirectoryId().setStringData(s);
		list.add(lpath);
		createECMA119LPath(image, policy, d.getReferenceTo(), list);
	    }
	}
    }

    void createECMA119MPath(UDF_Image image, UDF_Policy policy, UDF_FEDesc fe, UDF_ElementList list) throws UDF_Exception{
	UDF_ElementList el = fe.getDirectoryList();
	for(Iterator it=el.iterator() ; it.hasNext() ; ){
	    UDF_desc257 d = (UDF_desc257)it.next();
	    if((d.getFileChar().getIntValue() & UDF_desc257.PARENT) != 0)
		continue;
	    if((d.getFileChar().getIntValue() & UDF_desc257.DELETED) != 0)
		continue;
	    if((d.getFileChar().getIntValue() & UDF_desc257.DIRECTORY) != 0){
		UDF_ECMA119PathTable_be mpath = (UDF_ECMA119PathTable_be)image.createElement("UDF_ECMA119PathTable_be", null, null);
		mpath.setDefaultValue();
		String fid = d.getFileId().getStringData();
		String s = fid.substring(2);
		mpath.getLenOfDirectoryId().setValue(s.length());
		mpath.getPaddingField().setSize(s.length()%2);
		mpath.getDirectoryId().setStringData(s);
		list.add(mpath);
		createECMA119MPath(image, policy, d.getReferenceTo(), list);
	    }
	}
    }

    private void createECMA119BridgeSub(UDF_Image image, UDF_Policy policy, UDF_FEDesc fe) throws UDF_Exception,IOException{
	UDF_Extent ext = (UDF_Extent)image.createElement("UDF_Extent", null, null);

	UDF_ElementList el = fe.getDirectoryList();
	long size = 0;

	{
	    UDF_ECMA119DirectoryRecord dr = (UDF_ECMA119DirectoryRecord)image.createElement("UDF_ECMA119DirectoryRecord", null, null);
	    dr.setHintSize(40);
	    dr.setDefaultValue();
	    dr.getLenOfFileId().setValue(1);
	    dr.getFileId().setSize(1);
	    byte[] fn = new byte[]{0};
	    dr.getFileId().setData(fn);
	    dr.getPaddingField().setSize(0);
	    dr.getSystemUse().setSize(6);

	    dr.getLenOfDirectoryRecord().setValue(dr.getSize());
	    ext.appendChild(dr);
	    
	    size += dr.getSize();
	}
	{
	    UDF_ECMA119DirectoryRecord dr = (UDF_ECMA119DirectoryRecord)image.createElement("UDF_ECMA119DirectoryRecord", null, null);
	    dr.setHintSize(40);
	    dr.setDefaultValue();
	    dr.getLenOfFileId().setValue(1);
	    dr.getFileId().setSize(1);
	    byte[] fn = new byte[]{1};
	    dr.getFileId().setData(fn);
	    dr.getPaddingField().setSize(0);
	    dr.getSystemUse().setSize(6);

	    dr.getLenOfDirectoryRecord().setValue(dr.getSize());
	    ext.appendChild(dr);

	    size += dr.getSize();
	}

	for(Iterator it=el.iterator() ; it.hasNext() ; ){
	    UDF_desc257 d = (UDF_desc257)it.next();
	    if((d.getFileChar().getIntValue() & UDF_desc257.PARENT) != 0)
		continue;

	    UDF_ECMA119DirectoryRecord dr = (UDF_ECMA119DirectoryRecord)image.createElement("UDF_ECMA119DirectoryRecord", null, null);
	    dr.setHintSize(40);
	    dr.setDefaultValue();

	    UDF_FEDesc tgt = d.getReferenceTo();
	    int flags = 0;
	    if(tgt.isDirectory())
		flags = 2;

	    String fid = d.getFileId().getStringData();
	    String s = fid.substring(2);
	    s += ";1";
	    dr.getLenOfFileId().setValue(s.length());
	    dr.getFileId().setStringData(s);
	    dr.getPaddingField().setSize((s.length()+1)%2);
	    dr.getSystemUse().setSize(6);
	    dr.getDataLen().setValue(fe.getADSize());

	    dr.getLenOfDirectoryRecord().setValue(dr.getSize());
	    dr.getFileFlags().setValue(flags);
	    ext.appendChild(dr);

	    size += dr.getSize();

	}
	image.appendChild(ext);
	ext.addExtent(ecma119_loc++, -1, size);
    }
}
