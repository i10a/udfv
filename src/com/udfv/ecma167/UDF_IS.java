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
import com.udfv.exception.*;
import com.udfv.access.*;
import com.udfv.ecma167.*;

/**
   UDF_ISは Extentからデータをリードするクラス。
   Logical Vol Integrity Desc を先頭にデスクリプタが複数並び、
   desc8で終端されることが期待されている。

 */
public class UDF_IS extends UDF_Extent{
    UDF_ElementList my_islist;
    UDF_IS prev_extent;
    UDF_IS next_extent;

    public UDF_IS(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? "UDF_IS" : name);
	my_islist = new UDF_ElementList();
    }
    
    /**
       ISがチェーンされている場合、次のISを取得する
       されていない場合は nullを返す
    */
    public UDF_IS getNextIS(){
	return next_extent;
    }
    /**
       ISがチェーンされている場合、前のISを取得する
       されていない場合は nullを返す
    */
    public UDF_IS getPrevIS(){
	return prev_extent;
    }

    /**
       ISがチェーンされている場合、最初のISを取得する。
    */
    protected UDF_IS getFirstIS(){
	UDF_IS ext = this;
	while(ext.getPrevIS() != null)
	    ext = ext.getPrevIS();
	return ext;
    }
    /**
       ISがチェーンされている場合、最後のISを取得する
    */
    protected UDF_IS getLastIS(){
	UDF_IS ext = this;
	while(ext.getNextIS() != null)
	    ext = ext.getNextIS();
	return ext;
    }

    /**
       ISをチェーンする場合用のメソッド。
       システムで使用する。
    */
    void setNextIS(UDF_IS ext){
	next_extent = ext;
    }
    /**
       ISをチェーンする場合用のメソッド
       システムで使用する。
    */
    void setPrevIS(UDF_IS ext){
	prev_extent = ext;
    }
    /**
       ISのリストを取得する

       @return リスト

       チェーンされているときはその先も辿る。
     */
    public UDF_ElementList getISList(){
	UDF_ElementList list = new UDF_ElementList();
	UDF_IS tgt_is = getFirstIS();

	while(tgt_is != null){
	    list.add(tgt_is.my_islist);
	    tgt_is = tgt_is.getNextIS();
	}

	return list;
    }
    public UDF_desc9 getPrevailingLogicalVolIntegrityDesc(){
	//最後の desc9が privalingである。
	UDF_ElementList islist = getISList();
	int i = islist.size() - 1;
	while(i >= 0){
	    UDF_Element desc = islist.elementAt(i);
	    if(com.udfv.ecma167.UDF_desc9.class.isAssignableFrom(desc.getClass())){
		return (UDF_desc9)desc;
	    }
	    --i;
	}
	return null;
    }
    public UDF_LogicalVolHeaderDesc getPrevailingLogicalVolHeaderDesc() throws UDF_VolException{
	UDF_LogicalVolHeaderDesc lvhd = (UDF_LogicalVolHeaderDesc)getPrevailingLogicalVolIntegrityDesc().getLogicalVolContentsUse().getFirstChild();
	if(lvhd == null){
	    debug(0);
	    throw new UDF_VolException(this, "no logical volume header descriptor");
	}
	return lvhd;
    }

    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception{
	my_islist.removeAllElements();// = new UDF_ElementList();
	UDF_RandomAccessExtent rae = genRandomAccessExtent();
	
	while(!rae.eof()){
	    try{
		UDF_CrcDesc desc = UDF_CrcDesc.genCrcDesc(rae, this, null, null);
		debugMsg(3, "reading..." + desc.getClass().getName());
		desc.readFrom(rae);
		
		UDF_pad pad = new UDF_pad(this, null, null, env.LBS);
		pad.readFrom(rae);
		
		appendChild(desc);
		appendChild(pad);
		
		if(com.udfv.ecma167.UDF_desc9.class.isAssignableFrom(desc.getClass())){
		    UDF_desc9 desc9 = (UDF_desc9)desc;
		    my_islist.add(desc9);
		    UDF_extent_ad ad = desc9.getNextIntegrityExtent();
		    //NextExtentがあるか
		    if(ad.getExtentLen().getLongValue() != 0){
			UDF_IS new_is = (UDF_IS)createElement("UDF_IS", "UDF_Image", null);
			new_is.addExtent(ad);
			String label = UDF_Label.genExtentLabel(new_is);
			new_is.setAttribute("id", label);
			ad.setRefAttribute(label);


			long saved_pos = f.getPointer();
			f.seekSec(ad.getExtentLoc().getIntValue());
			new_is.readFrom(f);
			f.seek(saved_pos);

			setNextIS(new_is);
			new_is.setPrevIS(this);
			env.root.appendChild(new_is);
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
	    catch(UDF_DataException e){
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

	//最後の desc9が privalingである。
	UDF_ElementList islist = getISList();
	UDF_desc9 desc9 = (UDF_desc9)islist.elementAt(islist.size() - 1);
	desc9.postVolReadHook(f);

	return (int)rae.length();
    }
    public long writeTo(UDF_RandomAccess f) throws UDF_Exception,IOException{
	return super.writeTo(f);
    }

    public void recalcISLIST(){
	my_islist.removeAllElements();
	UDF_Element[] child = getChildren();
	for(int i=0 ; i<child.length ; ++i){
	    if(com.udfv.ecma167.UDF_CrcDesc.class.isAssignableFrom(child[i].getClass()))
		my_islist.add(child[i]);
	    if(com.udfv.ecma167.UDF_desc9.class.isAssignableFrom(child[i].getClass())){
		UDF_desc9 d9 = (UDF_desc9)child[i];
		if(d9.getNextIntegrityExtent().getExtentLoc().getIntValue() != 0 &&
		   d9.getNextIntegrityExtent().getExtentLen().getLongValue() != 0){//nextがある
		    String label = d9.getNextIntegrityExtent().getExtentLoc().getAttribute("ref");
		    label = UDF_Util.car(label, '.');
		    if(label != null){
			UDF_IS nxt_is = (UDF_IS)findById(label);
			if(nxt_is != null){
			    this.setNextIS(nxt_is);
			    nxt_is.setPrevIS(this);
			    nxt_is.recalcISLIST();
			}
		    }
		}
	    }
	}
    }
    public void readFromXML(Node n) throws UDF_Exception{
	my_islist = new UDF_ElementList();

	readExtentFromXML(n);
	NodeList nl = n.getChildNodes();
	if(nl != null && nl.getLength() > 1){
	    for(int i=1 ; i<nl.getLength() ; ++i){
		Node child = nl.item(i);
		if(child.getNodeType() == Node.ELEMENT_NODE){
		    Element childelem = (Element)child;
		    String nn = childelem.getLocalName();
		    try{
			if(nn.equals("extents"))
			    continue;

			UDF_Element d = UDF_Element.genElement(nn, this, null, null);
			d.readFromXML(child);
			
			if(com.udfv.ecma167.UDF_desc9.class.isAssignableFrom(d.getClass())){
			    UDF_desc9 desc9 = (UDF_desc9)d;
			    my_islist.add(desc9);
			}
			
			appendChild(d);
		    }
		    catch(ClassNotFoundException e){
			return;
		    }
		}
	    }
	}
    }
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	UDF_ElementBase[] child = getChildren();
	
	final short category = UDF_Error.C_ECMA167;
	
	
	for(int i = 0; i < child.length; i++){

	    String name = child[i].getName();
	    
	    
	    // 1つめは必ずLogical Vol Integrity Desc
	    if(i == 0 && !name.equals("UDF_desc9")){  // Logical Volume Integrity Desc
		
		el.addError(new UDF_Error
			    (category, UDF_Error.L_ERROR, "",
			     "The first extent shall be specified by the prevailing Logical Volume Descriptor for the logical volume. " +
			     "Succeeding extents, if any, shall be specified by a Logical Volume Integrity Descriptor.",
			     "3/8.8,2"));
	    }
	    else if(!name.equals("UDF_desc9") && !name.equals("UDF_pad") && !name.equals("UDF_desc8")){
		
		el.addError(new UDF_Error
			    (category, UDF_Error.L_ERROR, name,
			     "Processing of a extent of Logical Volume Integrity Descriptors shall be as if the descriptors " +
			     "were processed in order of ascending order of their addreses and processing was terminated by " +
			     "an unrecorded logica sector, or a Terminating Descriptor(see 3/10.9) or after a descriptor specifying a subsequent extent.",
			     "3/8.8.2"));
	    }
	
	    el.addError(child[i].verify());
	}
	
	el.setRName("Logical Volume Integrity Sequence");
	return el;
    }
    
    public void debug(int indent){
	super.debug(indent);
    }

    public void recalc(short type, UDF_RandomAccess f){
	super.recalc(type, f);
	try{
	    if(type == RECALC_VDSLIST || type == RECALC_VDSLIST2){
		recalcISLIST();
	    }
	}
	catch(Exception e){
	    ignoreMsg("recalc", e);
	}
	
	/*
	if(type == RECALC_VDSLIST){
	    my_islist.removeAllElements();

	    UDF_Element[] child = getChildren();
	    for(int i=0 ; i<child.length ; ++i){
		if(com.udfv.ecma167.UDF_desc9.class.isAssignableFrom(child[i].getClass()))
		    my_islist.add(child[i]);
		if(com.udfv.ecma167.UDF_desc8.class.isAssignableFrom(child[i].getClass())){
		    break;
		}
	    }
	}
	*/
    }

}
