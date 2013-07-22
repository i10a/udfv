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
/**
 */
package com.udfv.ecma119;

import java.io.*;
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
import com.udfv.exception.*;
import com.udfv.access.*;
import com.udfv.core.*;
import com.udfv.util.*;

/**
   ECMA119で規定されている VDSを表わすクラス。

 */
public class UDF_ECMA119_VDS extends UDF_Element//UDF_Extent
{
    public UDF_ECMA119_VDS(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? "UDF_VRS" : name);
    }

    /*
      getSize(), getLongSize() ともに readFrom()/readFromXML() が
      設定するvdssizeを返す。
      ※readFromXML()には2005/06/01に追加。
    */
    public int getSize(){
	
	return (int)getLongSize();
    }
    
    public long getLongSize(){
	
	return vdssize;
    }
    
    /**
       Boot Recordを取得する。
       Boot Recordがなければ nullを返す
       
       @return Boot Record
     */
    public UDF_ECMA119_CD001_0 getBootRecord() { return bootRecord; }
    /**
       Primary Volume Descriptorを取得する。
       Primary Volume Descriptorがなければ nullを返す
       
       @return Primary Volume Descriptor
     */
    public UDF_ECMA119_CD001_1 getPrimaryVolDesc() { return primaryVolDesc; }
    /**
       Supplementary Volume Descriptorを取得する。
       Supplementary Volume Descriptorがなければ nullを返す
       
       @return Supplementary Volume Descriptor
     */
    public UDF_ECMA119_CD001_2 getSupplementaryVolDesc() { return supplementaryVolDesc; }
    /**
       Volume Partition Descriptorを取得する。
       Volume Partition Descriptorがなければ nullを返す
       
       @return Volume Partiton Descriptor
     */
    public UDF_ECMA119_CD001_3 getVolPartDesc() { return volPartDesc; }
    /**
       Descriptor Terminatorを取得する。
       Descriptor Terminatorがなければ nullを返す
       
       @return Descriptor Terminator
     */
    public UDF_ECMA119_CD001_255 getDescTerminator() { return volDescTerminator; }
    
    /**
       Boot Recordを設定する。

       @param v Boot Record
     */
    public void setBootRecord(UDF_ECMA119_CD001_0 v) { bootRecord = v; }
    /**
       Praimary Volume Descriptorを設定する。

       @param v Primary Volume Descriptor
     */
    public void setPrimaryVolDesc(UDF_ECMA119_CD001_1 v) { primaryVolDesc = v; }
    /**
       Supplementary Volume Descriptorを設定する。

       @param v Supplementary Volume Descriptor
     */
    public void setSupplementaryVolDesc(UDF_ECMA119_CD001_2 v) { supplementaryVolDesc = v; }
    /**
       Volume Partition Descriptorを設定する。

       @param v Volume Partition Descriptor
     */
    public void setVolPartDesc(UDF_ECMA119_CD001_3 v) { volPartDesc = v; }
    /**
       Descriptor Terminatorを設定する。

       @param v Descriptor Terminator
     */
    public void setDescTerminator(UDF_ECMA119_CD001_255 v) { volDescTerminator = v; }
    
    /**
       現在のポジションからECMA119のVDSを読む。もしVDSがなければ何もせず戻り値0を返し終了する。

	<p>       
       VDSがある場合は XMLのノードに追加し、UDF_Env.cd9660_root_loc に rootディレクトリの位置を格納し、読みこんだサイズを戻り値として返す。
	</p>       
       <p>
       次の場合、直ちに読むのを中断して終了する。
<ul>
<li>UDF_ECMA119_CD001_0, UDF_ECMA119_CD001_1, UDF_ECMA119_CD001_2, UDF_ECMA119_CD001_3, UDF_ECMA119_CD001_255以外のデスクリプタが存在する。</li>
</ul>
     */
    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception{

	long readsz = 0;
	boolean exit_loop = false;
	
	env.ecma119_PrimaryVolumeDesc = null;
	env.ecma119_SupplementaryVolumeDesc = new UDF_ElementList();

	removeAllChildren();

	try{
	    while(!exit_loop){
		byte[] b = new byte[6];
		UDF_Element uelem = null;
		int ret;
		ret = f.read(b, 0, 6);
		f.seek(-6, UDF_RandomAccess.SEEK_CUR);

		if(UDF_Util.cmpBytesString(b, "\0CD001")){
		    uelem = genElement("UDF_ECMA119_CD001_0", this, "ecma119", null);
		    readsz += uelem.readFrom(f);
		    appendChild(uelem);
		}
		else if(UDF_Util.cmpBytesString(b, "\1CD001")){
		    uelem = genElement("UDF_ECMA119_CD001_1", this, "ecma119", null);
		    readsz += uelem.readFrom(f);
		    appendChild(uelem);
		    UDF_ECMA119_CD001_1 cd001_1 = (UDF_ECMA119_CD001_1)uelem;

		    env.ecma119_PrimaryVolumeDesc = cd001_1;
		}
		else if(UDF_Util.cmpBytesString(b, "\2CD001")){

		    uelem = genElement("UDF_ECMA119_CD001_2", this, "ecma119", null);
		    readsz += uelem.readFrom(f);
		    appendChild(uelem);

		    env.ecma119_SupplementaryVolumeDesc.add(uelem);
		}
		else if(UDF_Util.cmpBytesString(b, "\3CD001")){
		    uelem = genElement("UDF_ECMA119_CD001_3", this, "ecma119", null);
		    readsz += uelem.readFrom(f);
		    appendChild(uelem);
		}
		else if(UDF_Util.cmpBytesString(b, "\377CD001")){
		    uelem = genElement("UDF_ECMA119_CD001_255", this, "ecma119", null);
		    readsz += uelem.readFrom(f);
		    appendChild(uelem);
		    exit_loop = true;
		}
		else{
		    return readsz;
		}
	    }
	}
	catch(Exception e){
	    e.printStackTrace();
	}
	
	vdssize = readsz;
	return readsz;
    }
    
    public void debug(int indent){
	if(bootRecord != null)
	    bootRecord.debug(indent + 1);
	if(primaryVolDesc != null)
	    primaryVolDesc.debug(indent + 1);
	if(supplementaryVolDesc != null)
	    supplementaryVolDesc.debug(indent + 1);
	if(volPartDesc != null)
	    volPartDesc.debug(indent + 1);
	if(volDescTerminator != null)
	    volDescTerminator.debug(indent + 1);
    }
    
    public void readFromXML(Node n) throws UDF_Exception{

	removeAllChildren();

	preReadFromXMLHook(n);
	NodeList nl = n.getChildNodes();

	for(int i=0 ; i<nl.getLength() ; ++i){
	    Node child = nl.item(i);
	    if(child.getNodeType() != Node.ELEMENT_NODE)
		continue;

	    String prefix = child.getPrefix();
	    String name = child.getLocalName();

	    if(false)
		;
	    else if(name.equals("UDF_ECMA119_CD001_0")){
		bootRecord = (UDF_ECMA119_CD001_0)createElement("UDF_ECMA119_CD001_0", prefix, "UDF_ECMA119_CD001_0");
		bootRecord.readFromXML(child);
		appendChild(bootRecord);
	    }
	    else if(name.equals("UDF_ECMA119_CD001_1")){
		primaryVolDesc = (UDF_ECMA119_CD001_1)createElement("UDF_ECMA119_CD001_1", prefix, "UDF_ECMA119_CD001_1");
		primaryVolDesc.readFromXML(child);
		appendChild(primaryVolDesc);
	    }
	    else if(name.equals("UDF_ECMA119_CD001_2")){
		supplementaryVolDesc = (UDF_ECMA119_CD001_2)createElement("UDF_ECMA119_CD001_2", prefix, "UDF_ECMA119_CD001_2");
		supplementaryVolDesc.readFromXML(child);
		appendChild(supplementaryVolDesc);
	    }
	    else if(name.equals("UDF_ECMA119_CD001_3")){
		volPartDesc = (UDF_ECMA119_CD001_3)createElement("UDF_ECMA119_CD001_3", prefix, "UDF_ECMA119_CD001_3");
		volPartDesc.readFromXML(child);
		appendChild(volPartDesc);
	    }
	    else if(name.equals("UDF_ECMA119_CD001_255")){
		volDescTerminator = (UDF_ECMA119_CD001_255)createElement("UDF_ECMA119_CD001_255", prefix, "UDF_ECMA119_CD001_255");
		volDescTerminator.readFromXML(child);
		appendChild(volDescTerminator);
	    }
	}

	/*　サイズ計算※readFrom()で行っていることと同等の処理が必要　*/
	long readsz = 0;

	UDF_Element [] el = getChildren();

	for(int i = 0; i < el.length; i++){
	    readsz += el[i].getSize();
	}

	vdssize = readsz;
    }
    
    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long wsize = 0;
	UDF_ElementBase[] eb = getChildren();
	
	for(int i = 0; i < eb.length; i++){
	    wsize += eb[i].writeTo(f);
	}

	return wsize;
    }

    private UDF_ECMA119_CD001_0 bootRecord = null;
    private UDF_ECMA119_CD001_1 primaryVolDesc = null;
    private UDF_ECMA119_CD001_2 supplementaryVolDesc = null;
    private UDF_ECMA119_CD001_3 volPartDesc = null;
    private UDF_ECMA119_CD001_255 volDescTerminator = null;
    
    private long vdssize = 0;

/*
    protected boolean hasGlobalPoint(){
	return true;
    }
*/
}

