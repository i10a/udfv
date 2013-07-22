/*
 */
package com.udfv.ecma167;

import java.io.*;
import java.util.*;
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
import com.udfv.access.*;
import com.udfv.exception.*;
import com.udfv.core.*;
import com.udfv.util.*;

/**
   Directoryを表現するクラス。

   旧バージョンでは UDF_bytesから継承していたが、
   より軽いSimpleContainerから継承するように変更した。


 */
public class UDF_Directory extends UDF_LightweightContainer//UDF_bytes
{
    public UDF_Directory(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? "UDF_Directory" : name);
    }
    
    public void readFromXML(Node n) throws UDF_Exception{

	preReadFromXMLHook(n);

	NodeList nl = n.getChildNodes();
	if(nl == null || nl.getLength() < 2){
	    return;
	}

	for(int i=1 ; i<nl.getLength() ; ++i){

	    Node child = nl.item(i);
	    if(child.getNodeType() != Node.ELEMENT_NODE){
		continue;
	    }

	    Element childelem = (Element)child;
	    String nn = childelem.getLocalName();
	    try{
		if(nn.equals("extents"))
		    continue;

		UDF_Element d = UDF_Element.genElement(nn, this, null, null);
		d.readFromXML(child);
		appendChild(d);
	    }
	    catch(ClassNotFoundException e){
		return;
	    }
	}
    }
    
    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{

	UDF_ElementBase[] children = getChildren();
	for(int i = 0, max = children.length; i < max; i++){

	    children[i].writeTo(f);
	}

	return (int)(f.length());
    }
    
    public UDF_ErrorList verify() throws UDF_Exception{

	UDF_ErrorList el = new UDF_ErrorList();
	UDF_Error err;
	UDF_ElementBase[] child = getChildren();
	
	
	Vector filevernumvec = new Vector();  // File Version Number を貯める
	Vector fileidvec = new Vector();  // File Identifier を貯める
	final short category = UDF_Error.C_ECMA167;
	int parentnum = 0;
	
	for(int i = 0; i < child.length; i++){
	    
	    UDF_desc257 fid = (UDF_desc257)child[i];
	    UDF_uint16  fvernum = fid.getFileVersionNumber();
	    UDF_bytes   fileid  = fid.getFileId();
	    
	    
	    // 基本チェック
	    el.addError(fid.verify("File Identifier Descriptor[" + i + "]"));
	    
	    // File Version Number とFile Identifier が等しい
	    // FID は２つとあってはならない
	    for(int j = 0; j < fileidvec.size(); j++){
		
		UDF_uint16 fvernum2 = (UDF_uint16)filevernumvec.elementAt(j);
		UDF_bytes  fileid2  = (UDF_bytes)fileidvec.elementAt(j);
		
		
		if(fvernum2.getIntValue() == fvernum.getIntValue() &&
		   UDF_Util.cmpBytesBytes(fileid2.getData(), fileid.getData())){
		    
		    el.addError(new UDF_Error
				(category, UDF_Error.L_ERROR, "File Identifier Descriptor[" + i + "]",
				 "For the descriptors in a directory\n" +
				 "- there shall not be more than one descriptor with the same File Identifier(see 4/14.4.8) and " +
				 "File Version Number(see 4/14.4.2).",
				 "4/8.6"));
		}
	    }
	    
	    // 今回のデータを追加
	    fileidvec.add(fid.getFileId());
	    filevernumvec.add(fid.getFileVersionNumber());
	    
	    
	    // ディレクトリを指すFID の場合はFile Version Number が１でなければならない
	    if((fid.getFileChar().getIntValue() & 0x02) != 0){
		
		err = fid.verifyFileVerNum(1);
		if(err.isError()){
		    
		    err.setRName("File Identifier Descriptor[" + i + "]");
		    err.setMessage("For the descriptors in a directory\n - a descriptor identifying a directory shall have a File Version Number of 1.");
		    err.setRefer("4/8.6");
		    el.addError(err);
		}
	    }
	    
	    if((fid.getFileChar().getIntValue() & 0x08) != 0)  // Parent bit
		parentnum++;
	}
	
	// Parent を指すFID は１つだけ存在しなければならない
	if(parentnum != 1){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "",
			 "For the descriptors in a directory\n" +
			 "- there shall be exactly one File Identifier Descriptor identifying the parent directory(see 4/14.4.3).",
			 "4/8.6"));
	}
	
	el.setRName("Directory");
	el.setGlobalPoint(getGlobalPoint());
	return el;
    }
    /**
       desc257を読む。
     */
    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception{
	preReadHook(f);

	long readsz = 0;
	UDF_ElementList dlist = new UDF_ElementList();
	try{
	    while(!f.eof()){
		UDF_desc257 d7 = (UDF_desc257)createElement("UDF_desc257", null, null);
		readsz += d7.readFrom(f);
		if(d7.getDescTag().getTagId().getIntValue() == 257){
		    dlist.add(d7);
		    debugMsg(3, d7.getFileId().getStringData());
		}
		else
		    break;
	    }
	}
	catch(IOException e){
	    ignoreMsg("UDF_Directory#readFrom", e);
	}
	catch(UDF_DescTagException e){
	    ;//目をつぶる
	}
	catch(UDF_Exception e){
	    ignoreMsg("UDF_Directory#readFrom", e);
	}
	appendChildren(dlist);

	postReadHook(f);
	return readsz;
    }
    //long my_size;

    /**
       互換のため
     */
    public void setSize(int sz){
	//setAttribute("size", sz);
	//my_size = sz;
    }
    /**
       互換のため
     */
    public void setSize(long sz){
	//setAttribute("size", sz);
	//my_size = sz;
    }
    /**
       互換のため
     */
    public void incSize(int sz){
	//setSize(my_size + sz);
    }
    /**
       互換のため
     */
    public void incSize(long sz){
	//setSize(my_size + sz);
    }
}
