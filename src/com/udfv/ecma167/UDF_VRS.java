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
package com.udfv.ecma167;

import java.io.*;
import java.util.*;
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
import com.udfv.exception.*;
import com.udfv.access.*;
import com.udfv.core.*;
import com.udfv.util.*;

public class UDF_VRS extends UDF_Extent
{
    public UDF_VRS(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? "UDF_VRS" : name);
    }

    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long readsz = 0;
	UDF_RandomAccessExtent rae = genRandomAccessExtent();
	boolean exit_loop = false;

	preReadHook(f);

	// cd9660 VDS があれば読み込んで追加する
	try{
	    com.udfv.ecma119.UDF_ECMA119_VDS cd9660_vrs_ext =
		(com.udfv.ecma119.UDF_ECMA119_VDS)createElement("UDF_ECMA119_VDS", null, UDF_XML.CD9660_VDS);

	    // ECMA119のDTDを設定
	    //	    cd9660_vrs_ext.setAttribute("xmlns:ecma119", UDF_Env.ECMA119_DTD);

	    // 長さ未定につき最大の長さで挑戦
	    int abssec = (int)(rae.getAbsPointer() / UDF_Env.LBS);
	    if(abssec == 16){

		readsz = cd9660_vrs_ext.readFrom(f);
		if(0 < readsz){

		    appendChild(cd9660_vrs_ext);
		}
		rae.seek(readsz);
	    }
	}
	catch(UDF_Exception e){
	    ;
	}

	try{
	    while(!rae.eof() && !exit_loop){
		byte[] b = new byte[6];
		UDF_Element uelem;
		int ret;
		ret = rae.read(b, 0, 6);

		rae.seek(-6, UDF_RandomAccess.SEEK_CUR);

		if(UDF_Util.cmpBytesString(b, 1, "BEA01")){
		    uelem = genElement("UDF_ECMA167_BEA01", this, "UDF_VRS", null);
		    readsz += uelem.readFrom(rae);
		    appendChild(uelem);
		}
		else if(UDF_Util.cmpBytesString(b, 1, "NSR02")){
		    uelem = genElement("UDF_ECMA167_NSR02", this, "UDF_VRS", null);
		    readsz += uelem.readFrom(rae);
		    appendChild(uelem);
		    env.recorded_udf_revision = 0x102;
		}
		else if(UDF_Util.cmpBytesString(b, 1, "NSR03")){
		    uelem = genElement("UDF_ECMA167_NSR03", this, "UDF_VRS", null);
		    readsz += uelem.readFrom(rae);
		    appendChild(uelem);
		}
		else if(UDF_Util.cmpBytesString(b, 1, "BOOT2")){
		    uelem = genElement("UDF_ECMA167_BOOT2", this, "UDF_VRS", null);
		    readsz += uelem.readFrom(rae);
		    appendChild(uelem);
		}
		else if(UDF_Util.cmpBytesString(b, 1, "TEA01")){
		    uelem = genElement("UDF_ECMA167_TEA01", this, "UDF_VRS", null);
		    readsz += uelem.readFrom(rae);
		    appendChild(uelem);

		    //exit_loop = true;
		}
		else
		    exit_loop = true;
	    }
	    //UDF_pad pad = new UDF_pad(this, null, null, env.LBS);
	    UDF_pad pad = (UDF_pad)createElement("UDF_pad", null, null);
	    pad.setAlign(env.LBS);
	    pad.readFrom(rae);
	    appendChild(pad);
	}
	catch(UDF_EOFException e){
	    ;// ignore
	}
	catch(Exception e){
	    e.printStackTrace();
	}

	return readsz;
    }

    /**
      Volume&nbsp;Recognition&nbsp;Sequence&nbsp;を読み込みます。<br>
      cd9660の&nbsp;VDSを読み込むために&nbsp;ExtentのreadFromXML()を拡張しています。

      @param n XML&nbsp;のノード
    */
    public void readFromXML(Node n) throws UDF_Exception{
	readExtentFromXML(n);

	NodeList nl = n.getChildNodes();
	if(nl == null)
	    return;
	if(nl.getLength() < 2)
	    return;

	for(int i = 0; i < nl.getLength(); ++i) {

	    Node child = nl.item(i);
	    if(child.getNodeType() != Node.ELEMENT_NODE){
	        continue;
	    }

	    String tagName = child.getLocalName();
	    String className = child.getLocalName();

	    if(className.equals("extents"))
		continue;

	    if(className.equals(UDF_XML.CD9660_VDS)){
		com.udfv.ecma119.UDF_ECMA119_VDS ext = new com.udfv.ecma119.UDF_ECMA119_VDS(this, "UDF_ECMA119_VDS", tagName);
		ext.readFromXML(child);
		appendChild(ext);
		continue;
	    }

	    try{
		UDF_Element elm = UDF_Element.genElement(className, this, "UDF_VRS", tagName);
		elm.readFromXML(child);
		appendChild(elm);
		continue;
	    }
	    catch(ClassNotFoundException e){
		throw new UDF_XMLException(this, "XML: Unknown Element:" + className);
	    }
	}
    }


    public UDF_ErrorList verify() throws UDF_Exception{

	UDF_ErrorList el = new UDF_ErrorList();
	UDF_ElementBase[] child = getChildren();
	boolean isBEA01 = false;
	boolean isTEA01 = true;
	int i = 0;

	final String ERRMSG = "A volume recognition sequence shall consist of a consecutively recorded sequence of one or more " +
	    "Volume Structure Descriptors(see 2/9.1) recorded according to the schema shown in figure 2/1.";


	for(i = 0; i < child.length; i++){

	    String name = child[i].getName();

	    if(name.equals(UDF_XML.CD9660_VDS)){

		// 一番最初のみ
		if(i != 0){

		    el.addError(new UDF_Error
				(UDF_Error.C_ECMA167, UDF_Error.L_ERROR, "CD-ROM Volume Descriptor Set", ERRMSG, "2/8.3.1"));
		}
	    }
	    else if(name.equals("UDF_ECMA167_BEA01")){

		// すでにBEA が存在していて、TEA がまだきていない
		if(isBEA01){

		    el.addError(new UDF_Error
				(UDF_Error.C_ECMA167, UDF_Error.L_ERROR, "Beginning Extended Area Descriptor", ERRMSG, "2/8.3.1"));
		}

		isBEA01 = true;
		isTEA01 = false;
	    }
	    else if(name.equals("UDF_ECMA167_TEA01")){

		// 閉じられていないBEA が存在しない
		if(!isBEA01){

		    el.addError(new UDF_Error
				(UDF_Error.C_ECMA167, UDF_Error.L_ERROR, "Terminating Extended Area Descriptor",
				 "TEAD is recorded without BEAD is not recorded before it.\n" + ERRMSG, "2/8.3.1"));
		}

		isBEA01 = false;
		isTEA01 = true;
	    }
	    else if(name.equals("UDF_ECMA167_NSR02")){

		if(!isBEA01 || isTEA01){

		    el.addError(new UDF_Error
				(UDF_Error.C_ECMA167, UDF_Error.L_ERROR, "Volume Structure Descriptor(NSR02)", ERRMSG, "2/8.3.1"));
		}
	    }
	    else if(name.equals("UDF_ECMA167_NSR03")){

		if(!isBEA01 || isTEA01){

		    el.addError(new UDF_Error
				(UDF_Error.C_ECMA167, UDF_Error.L_ERROR, "Volume Structure Descriptor(NSR03)",
				 "Probably BEAD is not recorded before NSRD.\n" + ERRMSG, "2/8.3.1"));
		}
	    }
	    else if(name.equals("UDF_ECMA167_BOOT2")){

		if(!isBEA01 || isTEA01){

		    el.addError(new UDF_Error
				(UDF_Error.C_ECMA167, UDF_Error.L_ERROR, "Boot Descriptor(BOOT2)", ERRMSG, "2/8.3.1"));
		}
	    }
	    else if(name.equals("UDF_pad")){  // pad のときは何もしない
		;
	    }
	    else{
		el.addError(new UDF_Error
			    (UDF_Error.C_ECMA167, UDF_Error.L_ERROR, "Volume Structure Descriptor",
			     "Unknown data:(" + name + ")\n" + ERRMSG, "2/8.3.1"));
	    }
	}

	// TEA で閉じられていない
	if(!isTEA01){

	    el.addError(new UDF_Error
			(UDF_Error.C_ECMA167, UDF_Error.L_ERROR, "Extended Area", ERRMSG, "2/8.3.1"));
	}


	el.setGlobalPoint(getGlobalPoint());

	for(i = 0; i < child.length; i++)
	    el.addError(child[i].verify());

	el.setRName("Volume recognition sequence");
	return el;
    }
}


