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
import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.access.*;
import com.udfv.exception.*;
import com.udfv.ecma167.*;

/**
   UDF_FDSは Extentからデータをリードするクラス。
   デスクリプタが複数並び、desc8で終端されることが期待されている。
 */
public class UDF_FDS extends UDF_Extent
{
    UDF_ElementList my_fdslist;
    UDF_FDS prev_extent;
    UDF_FDS next_extent;

    /**
	（ローカル）クラス名を取得します
    */
    public static String getUDFClassName( ) {
        return "UDF_FDS";
    }

    public UDF_FDS(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? "UDF_FDS" : name);
	my_fdslist = new UDF_ElementList();
    }

    /**
       FDSがチェーンされている場合、次のFDSを取得する
       されていない場合は nullを返す
    */
    public UDF_FDS getNextFDS(){
	return next_extent;
    }
    /**
       FDSがチェーンされている場合、前のFDSを取得する
       されていない場合は nullを返す
    */
    public UDF_FDS getPrevFDS(){
	return prev_extent;
    }

    /**
       FDSがチェーンされている場合、最初のFDSを取得する。
    */
    protected UDF_FDS getFirstFDS(){
	UDF_FDS ext = this;
	while(ext.getPrevFDS() != null)
	    ext = ext.getPrevFDS();
	return ext;
    }
    /**
       FDSがチェーンされている場合、最後のFDSを取得する
    */
    protected UDF_FDS getLastFDS(){
	UDF_FDS ext = this;
	while(ext.getNextFDS() != null)
	    ext = ext.getNextFDS();
	return ext;
    }

    /**
       FDSをチェーンする場合用のメソッド。
       システムで使用する。
    */
    void setNextFDS(UDF_FDS ext){
	next_extent = ext;
    }
    /**
       FDSをチェーンする場合用のメソッド
       システムで使用する。
    */
    void setPrevFDS(UDF_FDS ext){
	prev_extent = ext;
    }
    /**
       VDSのリストを取得する

       @return リスト

       チェーンされているときはその先も辿る。
     */
    public UDF_ElementList getFDSList(){
	UDF_ElementList list = new UDF_ElementList();
	UDF_FDS tgt_fds = getFirstFDS();

	while(tgt_fds != null){
	    list.add(tgt_fds.my_fdslist);
	    tgt_fds = tgt_fds.getNextFDS();
	}

	return list;
    }

    /**
       prevaling unallocated space descriptorを取得する。
       (see ECMA167 3/8.4.3)

       複数ある場合を考慮し、戻り値は UDF_ElementListになっている。
     */
    public UDF_ElementList getPrevailingFileSetDesc() throws UDF_VolException{
	UDF_ElementList list = getFDSList();
	TreeMap hash = new TreeMap();

	for(Iterator it = list.iterator() ; it.hasNext() ; ){
	    UDF_Element elem = (UDF_Element)it.next();

	    if(com.udfv.ecma167.UDF_desc256.class.isAssignableFrom(elem.getClass())){
		UDF_desc256 desc256 = (UDF_desc256)elem;
		UDF_uint32 key = desc256.getFileSetNumber();
		if(hash.get(key) == null){
		    hash.put(key, desc256);
		}
		else{//fileSetDecNumberが大きいものが prevailing
		    UDF_desc256 p_desc256 = (UDF_desc256)hash.get(key);
		    if(desc256.getFileSetDescNumber().getIntValue() > p_desc256.getFileSetDescNumber().getIntValue())
			hash.put(key, desc256);
		}
	    }
	}
	UDF_ElementList desc256_list = new UDF_ElementList();
	Set set = hash.keySet();
	Iterator it = set.iterator();

	for( ; it.hasNext() ; )
	    desc256_list.add((UDF_Element)hash.get(it.next()));

	if(desc256_list.size() == 0)
	    throw new UDF_VolException(this, "No Fileset descriptor");

	return desc256_list;
    }

    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception{
	UDF_RandomAccessExtent rae = genRandomAccessExtent();
	while(!rae.eof()){
	    try{
		UDF_CrcDesc desc = UDF_CrcDesc.genCrcDesc(rae, this, null, null);
		debugMsg(3, "reading..." + desc.getClass().getName());
		desc.readFrom(rae);
		UDF_pad pad = new UDF_pad(this, null, null, env.LBS);
		pad.readFrom(rae);
		
		appendChild(desc);
		my_fdslist.add(desc);

		appendChild(pad);

		if(com.udfv.ecma167.UDF_desc256.class.isAssignableFrom(desc.getClass())){
		    UDF_desc256 desc256 = (UDF_desc256)desc;
		    UDF_long_ad ad = desc256.getNextExtent();

		    //NextExtentがあるか
		    if(ad.getLen() != 0){
			//boolean mirror = getMirror();
			int subno = getPartSubno();

			UDF_FDS new_fsds = (UDF_FDS) createElement("UDF_FDS", "UDF_Image", null);
			//new_fsds.setMirror(mirror);
			new_fsds.setPartSubno(subno);
			new_fsds.addExtent(ad);

			String label = UDF_Label.genExtentLabel(new_fsds);
			new_fsds.setAttribute("id", label);
			ad.setRefAttribute(label);

			//if(mirror)
			new_fsds.readFrom(env.getPartMapRandomAccess(ad.getPartRefNo(), subno));
			//else
			//new_fsds.readFrom(env.getPartMapRandomAccess(ad.getPartRefNo(), 0));
			
			
			setNextFDS(new_fsds);
			new_fsds.setPrevFDS(this);

			env.root.appendChild(new_fsds);

			break;
		    }
		}
		else if(com.udfv.ecma167.UDF_desc8.class.isAssignableFrom(desc.getClass()))
		    break;
	    }
	    catch(UDF_DescTagException e){
		break;
	    }
	    catch(UDF_EOFException e){
		break;
	    }
	    catch(Exception e){
		e.printStackTrace();
		break;
	    }
	}
	try{
	    UDF_pad pad = new UDF_pad(this, null, null, (int)rae.length());
	    pad.readFrom(rae);
	    appendChild(pad);
	}
	catch(UDF_EOFException e){
	    ;//ignore
	}
	return (int)rae.length();
    }
    
    public long writeTo(UDF_RandomAccess f) throws UDF_Exception,IOException{
	return super.writeTo(f);
    }
    
    public void recalcFDSLIST(){
	my_fdslist.removeAllElements();
	UDF_Element[] child = getChildren();
	for(int i=0 ; i<child.length ; ++i){
	    if(com.udfv.ecma167.UDF_CrcDesc.class.isAssignableFrom(child[i].getClass()))
		my_fdslist.add(child[i]);
	    if(com.udfv.ecma167.UDF_desc256.class.isAssignableFrom(child[i].getClass())){
		UDF_desc256 d256 = (UDF_desc256)child[i];
		if(d256.getNextExtent().getLbn() != 0 &&
		   d256.getNextExtent().getLen() != 0){//nextがある
		    String label = d256.getNextExtent().getExtentLbn().getAttribute("ref");
		    label = UDF_Util.car(label, '.');
		    if(label != null){
			UDF_FDS nxt_fds = (UDF_FDS)findById(label);
			if(nxt_fds != null){
			    this.setNextFDS(nxt_fds);
			    nxt_fds.setPrevFDS(this);
			    nxt_fds.recalcFDSLIST();
			}
		    }
		}
	    }
	}
    }
    public void recalc(short type, UDF_RandomAccess f){
	super.recalc(type, f);
	try{
	    if(type == RECALC_VDSLIST || type == RECALC_VDSLIST2){
		recalcFDSLIST();
		env.setFDS(null, getPartSubno(), this);
	    }
	}
	catch(Exception e){
	    ignoreMsg("recalc", e);
	}
    }
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	UDF_ElementBase[] child = getChildren();
	int zerofilesetnum = 0;
	
	
	for(int i = 0; i < child.length; i++){
	    
	    String name = child[i].getName();
	    
	    
	    if(name.equals("UDF_desc256")){  // File Set Descriptor
		
		com.udfv.ecma167.UDF_desc256 desc = (com.udfv.ecma167.UDF_desc256)child[i];
		if(desc.getFileSetNumber().getIntValue() == 0){
		    
		    if(zerofilesetnum == 0 || zerofilesetnum == 2)
			zerofilesetnum = 1;
		}
		else{
		    
		    if(zerofilesetnum == 0)
			zerofilesetnum = 2;
		}
	    }
	    else if(name.equals("UDF_desc8")){
	    }
	    else if(name.equals("UDF_pad")){
	    }
	    else{
		// 想定されていない構造
		UDF_Error err = new UDF_Error
		    (UDF_Error.C_ECMA167, UDF_Error.L_ERROR, "Unknown Data(" + name + ")",
		     "A File Set Descriptor Sequence shall be recorded as a sequence of extents within a logical volume. " +
		     "An extent of the File Set Descriptor Sequence shall be recorded according to the schema shown in figure 4/4."
		     , "4/8.3.1");
		UDF_Element tmp = (UDF_Element)child[i];
		err.setGlobalPoint(tmp.getGlobalPoint());
		el.addError(err);
	    }
	}
	
	// File Set Descriptor は存在しているが、File Set Number が0 であるFSD が存在しない
	if(zerofilesetnum == 2){
	    
	    UDF_Error err = new UDF_Error
		(UDF_Error.C_ECMA167, UDF_Error.L_ERROR, "",
		 "One of the File Set Descriptors of a File Set Descriptor Sequence shall have a file set number of 0.",
		 "4/8.3.1");
	    err.setGlobalPoint(getGlobalPoint());
	    el.addError(err);
	}
	
	el.addError(super.verify());
	el.setRName("File Set Descriptor Sequence");
	return el;
    }
}
