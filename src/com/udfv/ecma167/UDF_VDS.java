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
   UDF_VDSは Extentからデータをリードするクラス。
   デスクリプタが複数並び、desc8または desc3 で終端されることが期待されている。
   desc3の場合はその先を読む。
 */
public class UDF_VDS extends UDF_Extent
{
    UDF_ElementList my_vdslist;
    UDF_VDS prev_extent;
    UDF_VDS next_extent;

    public UDF_VDS(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? "UDF_VDS" : name);
	my_vdslist = new UDF_ElementList();
    }

    /**
       VDSがチェーンされている場合、次のVDSを取得する
       されていない場合は nullを返す
    */
    public UDF_VDS getNextVDS(){
	return next_extent;
    }
    /**
       VDSがチェーンされている場合、前のVDSを取得する
       されていない場合は nullを返す
    */
    public UDF_VDS getPrevVDS(){
	return prev_extent;
    }

    /**
       VDSがチェーンされている場合、最初のVDSを取得する。
    */
    protected UDF_VDS getFirstVDS(){
	UDF_VDS ext = this;
	while(ext.getPrevVDS() != null)
	    ext = ext.getPrevVDS();
	return ext;
    }
    /**
       VDSがチェーンされている場合、最後のVDSを取得する
    */
    protected UDF_VDS getLastVDS(){
	UDF_VDS ext = this;
	while(ext.getNextVDS() != null)
	    ext = ext.getNextVDS();
	return ext;
    }

    /**
       VDSをチェーンする場合用のメソッド。
       システムで使用する。
    */
    void setNextVDS(UDF_VDS ext){
	next_extent = ext;
    }
    /**
       VDSをチェーンする場合用のメソッド
       システムで使用する。
    */
    void setPrevVDS(UDF_VDS ext){
	prev_extent = ext;
    }

    /**
       VDSのリストを取得する

       @return リスト

       チェーンされているときはその先も辿る。
     */
    public UDF_ElementList getVDSList(){
	UDF_ElementList list = new UDF_ElementList();
	UDF_VDS tgt_vds = getFirstVDS();

	while(tgt_vds != null){
	    list.add(tgt_vds.my_vdslist);
	    tgt_vds = tgt_vds.getNextVDS();
	}

	return list;
    }

    public boolean hasPrimaryDesc(){
	try{
	    return getPrevailingPrimaryVolDesc().size() >= 1;
	}
	catch(UDF_VolException e){
	    return false;
	}
    }
    public boolean hasPartDesc(){
	try{
	    return getPrevailingPartDesc().size() >= 1;
	}
	catch(UDF_VolException e){
	    return false;
	}
    }
    public boolean hasLogicalVolDesc(){
	try{
	    return getPrevailingLogicalVolDesc().size() >= 1;
	}
	catch(UDF_VolException e){
	    return false;
	}
    }
    public boolean hasUnallocatedSpaceDesc(){
	try{
	    return getPrevailingUnallocatedSpaceDesc().size() >= 1;
	}
	catch(UDF_VolException e){
	    return false;
	}
    }
    public boolean hasImplDesc(){
	UDF_ElementList list = getVDSList();
	for(int i=0 ; i<list.size() ; ++i){
	    UDF_Element elem = list.elementAt(i);
	    if(com.udfv.ecma167.UDF_desc4.class.isAssignableFrom(elem.getClass()))
		return true;
	}
	return false;
    }

    public UDF_desc1 getPrimaryVolDesc(int volno) throws UDF_VolException{
	UDF_ElementList list = getPrevailingPrimaryVolDesc();

	for(int i=0 ; i<list.size() ; ++i){
	    UDF_desc1 desc1 = (UDF_desc1)list.elementAt(i);
	    if(desc1.getVolSeqNumber().getIntValue() == volno)
		return desc1;
	}
	throw new UDF_VolException(null, "No such volume sequence number:" + volno);

    }
    public UDF_desc5 getPartDesc(int partno) throws UDF_VolException{
	UDF_ElementList list = getPrevailingPartDesc();

	for(int i=0 ; i<list.size() ; ++i){
	    UDF_desc5 desc5 = (UDF_desc5)list.elementAt(i);
	    if(desc5.getPartNumber().getIntValue() == partno)
		return desc5;
	}
	throw new UDF_VolException(null, "No such partition number:" + partno);
    }
    
    /**
       Implementation Use Volume Descriptorを取得する。
       (see ECMA167 3/8.4.3)

       複数ある場合を考慮し、戻り値は UDF_ElementListになっている。
     */
    public UDF_ElementList getImplUseVolDesc() throws UDF_VolException{
	UDF_ElementList list = getVDSList();
	TreeMap hash = new TreeMap();

	for(int i=0 ; i<list.size() ; ++i){
	    UDF_Element elem = list.elementAt(i);
	    if(com.udfv.ecma167.UDF_desc4.class.isAssignableFrom(elem.getClass())){
		UDF_desc4 desc4 = (UDF_desc4)elem;
		
		//String  implid = UDF_Util.b2qstr(desc4.getImplId().getId().getBytes());
		//Integer voldescseqnum = new Integer(desc4.getVolDescSeqNumber().getIntValue());
		//if(implid.equals("*UDF LV Info")){
		//
		//key は uniqueな数字で作る。(ようするに全部リストにつっこむ)
		hash.put(new Integer(i), desc4);
		//}
	    }
	}
	
	UDF_ElementList desc4_list = new UDF_ElementList();
	Set set = hash.keySet();
	Iterator it = set.iterator();
	//Enumeration e = hash.keys();
	for( ; it.hasNext() ; )
	    desc4_list.add((UDF_Element)hash.get(it.next()));

	int size = desc4_list.size();
	if(size == 0)
	    throw new UDF_VolException(null, "No impl use volume descriptor");
	
	//UDF_desc4 desc4 = (UDF_desc4)desc4_list.elementAt(size - 1);
	return desc4_list;
    }
    
    /**
       prevaling primary descriptorを取得する。
       (see ECMA167 3/8.4.3)

       複数ある場合を考慮し、戻り値は UDF_ElementListになっている。
     */
    public UDF_ElementList getPrevailingPrimaryVolDesc() throws UDF_VolException{
	UDF_ElementList list = getVDSList();
	TreeMap hash = new TreeMap();

	for(int i=0 ; i<list.size() ; ++i){
	    UDF_Element elem = list.elementAt(i);
	    if(com.udfv.ecma167.UDF_desc1.class.isAssignableFrom(elem.getClass())){
		UDF_desc1 desc1 = (UDF_desc1)elem;
		/*
		  以下の3つの値が同じ desc1は Volume Desc Seq Numberが最大のものが優勢
		  である。

		  等しくない desc1は別の desc1として扱う
		*/
		String volid = UDF_Util.b2qstr(desc1.getVolId().getBytes());
		String volsetid = UDF_Util.b2qstr(desc1.getVolSetId().getBytes());
		String desccharset = UDF_Util.b2qstr(desc1.getDescCharSet().getBytes());

		String key = volid + volsetid + desccharset;
		if(hash.get(key) == null){
		    hash.put(key, desc1);
		}
		else{
		    UDF_desc1 p_desc1 = (UDF_desc1)hash.get(key);
		    if(desc1.getVolDescSeqNumber().getIntValue() > p_desc1.getVolDescSeqNumber().getIntValue())
			hash.put(key, desc1);
		}
	    }
	}
	UDF_ElementList desc1_list = new UDF_ElementList();
	//Enumeration e = hash.keys();
	Set set = hash.keySet();
	Iterator it = set.iterator();
	for( ; it.hasNext() ; )
	    desc1_list.add((UDF_Element)hash.get(it.next()));

	if(desc1_list.size() == 0)
	    throw new UDF_VolException(null, "No primary volume descriptor");
	return desc1_list;
    }

    /**
       prevaling partition descriptorを取得する。
       (see ECMA167 3/8.4.3)

       以下のルールで VDSを検索し、結果を UDF_ELementListに格納し、そのリストを返す。

       Partition Descriptorが VDS内に複数あり、かつ、Partition Numberが等しい場合は、
       Volume Sequence Numberが大きいものを Prevailing Partition Descriptorとして
       UDF_ElementListに追加する。
       
       Partition Numberが等しくない場合は、別の Partitionとして UDF_ElementListに
       追加する。

       Listはパーティション番号の順番に並べられる。
     */
    public UDF_ElementList getPrevailingPartDesc() throws UDF_VolException{

	UDF_ElementList list = getVDSList();
	TreeMap hash = new TreeMap();

	for(int i=0 ; i<list.size() ; ++i){
	    UDF_Element elem = list.elementAt(i);
	    if(com.udfv.ecma167.UDF_desc5.class.isAssignableFrom(elem.getClass())){
		UDF_desc5 desc5 = (UDF_desc5)elem;
		/*
		  partition numbers が同じ desc5は Volume Desc Seq Numberが最大のものが優勢
		  である。

		  等しくない desc5は別の desc5として扱う
		*/
		Integer key = new Integer(desc5.getPartNumber().getIntValue());
		if(hash.get(key) == null){
		    //System.err.println("new desc5:" + i + " " + key);
		    hash.put(key, desc5);
		}
		else{
		    //System.err.println("hoge desc5:" + i + " " + key);
		    UDF_desc5 p_desc5 = (UDF_desc5)hash.get(key);
		    if(desc5.getVolDescSeqNumber().getIntValue() > p_desc5.getVolDescSeqNumber().getIntValue()){
			hash.put(key, desc5);
			//System.err.println("update");
		    }
		}
	    }
	}

	Set set = hash.keySet();
	Iterator it = set.iterator();

	UDF_ElementList desc5_list = new UDF_ElementList();
	for( ; it.hasNext() ; )
	    desc5_list.add((UDF_Element)hash.get(it.next()));


	if(desc5_list.size() == 0)
	    throw new UDF_VolException(null, "No partition descriptor");


	return desc5_list;
    }

    /**
       prevaling logical volume descriptorを取得する。
       (see ECMA167 3/8.4.3)

       以下のルールで VDSを検索し、結果を UDF_ELementListに格納し、そのリストを返す。

       Logical Volume Descriptorが VDS内に複数あり、かつ、

       Logical Volume Identifier および Descriptor Charsetが等しい場合は、
       Volume Sequence Numberが大きいものを Prevailing LVD として
       UDF_ElementListに追加する。
       
       等しくない場合は、別の LVDとして UDF_ElementListに追加する。

       Listは "LogicalVolId + descCharsec"の文字列を辞書順にソートした順番に
       並べられる。
     */
    public UDF_ElementList getPrevailingLogicalVolDesc() throws UDF_VolException{
	UDF_ElementList list = getVDSList();
	TreeMap hash = new TreeMap();

	for(int i=0 ; i<list.size() ; ++i){
	    UDF_Element elem = list.elementAt(i);
	    if(com.udfv.ecma167.UDF_desc6.class.isAssignableFrom(elem.getClass())){
		UDF_desc6 desc6 = (UDF_desc6)elem;
		String logicalvolid = UDF_Util.b2str(desc6.getLogicalVolId().getBytes());
		String desccharset = UDF_Util.b2str(desc6.getDescCharSet().getBytes());
		String key = logicalvolid + desccharset;
		if(hash.get(key) == null){
		    hash.put(key, desc6);
		}
		else{
		    UDF_desc6 p_desc6 = (UDF_desc6)hash.get(key);
		    if(desc6.getVolDescSeqNumber().getIntValue() > p_desc6.getVolDescSeqNumber().getIntValue())
			hash.put(key, desc6);
		}
	    }
	}

	Set set = hash.keySet();
	Iterator it = set.iterator();

	UDF_ElementList desc6_list = new UDF_ElementList();
	for( ; it.hasNext() ; )
	    desc6_list.add((UDF_Element)hash.get(it.next()));

	if(desc6_list.size() == 0)
	    throw new UDF_VolException(null, "No logical volume descriptor");

	return desc6_list;
    }

    /**
       prevaling unallocated space descriptorを取得する。
       (see ECMA167 3/8.4.3)

       複数ある場合を考慮し、戻り値は UDF_ElementListになっている。
     */
    public UDF_ElementList getPrevailingUnallocatedSpaceDesc() throws UDF_VolException{
	UDF_ElementList list = getVDSList();
	TreeMap hash = new TreeMap();

	for(int i=0 ; i<list.size() ; ++i){
	    UDF_Element elem = list.elementAt(i);
	    if(com.udfv.ecma167.UDF_desc7.class.isAssignableFrom(elem.getClass())){
		UDF_desc7 desc7 = (UDF_desc7)elem;
		//desc7には keyがない。VolDescSeqNumberが大きいものが優勢
		String key = "";
		if(hash.get(key) == null){
		    hash.put(key, desc7);
		}
		else{
		    UDF_desc7 p_desc7 = (UDF_desc7)hash.get(key);
		    if(desc7.getVolDescSeqNumber().getIntValue() > p_desc7.getVolDescSeqNumber().getIntValue())
			hash.put(key, desc7);
		}
	    }
	}
	UDF_ElementList desc7_list = new UDF_ElementList();
	//Enumeration e = hash.keys();
	
	Set set = hash.keySet();
	Iterator it = set.iterator();

	for( ; it.hasNext() ; )
	    desc7_list.add((UDF_Element)hash.get(it.next()));

	if(desc7_list.size() == 0)
	    throw new UDF_VolException(null, "No logical volume descriptor");

	return desc7_list;
    }

    /*
      テーマ:シンプル & スマート

      desc5/desc6は readFromの直後に postReadHookで重要な情報を設定しない。
      
      全VDSを読み終わった後に postVolReadHook()を実行して情報を設定する。
    */
    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception{
	int revision  = 0;

	my_vdslist.removeAllElements();// = new UDF_ElementList();
	preReadHook(f);

	UDF_RandomAccessExtent rae = genRandomAccessExtent();
	//UDF_VDS this_vds = this;
	
	while(!rae.eof()){
	    try{
		UDF_CrcDesc desc = UDF_CrcDesc.genCrcDesc(rae, this, null, null);
		UDF_pad pad = new UDF_pad(this, null, null, env.LBS);
		    
		debugMsg(3, "reading..." + desc.getClass().getName());
		desc.readFrom(rae);
		pad.readFrom(rae);

		my_vdslist.add(desc);

		appendChild(desc);
		appendChild(pad);

		// UDF 環境に各情報を記録しておく
		if(com.udfv.ecma167.UDF_desc1.class.isAssignableFrom(desc.getClass())){
		    ;
		}
		else if(com.udfv.ecma167.UDF_desc4.class.isAssignableFrom(desc.getClass())){
		    
		    com.udfv.ecma167.UDF_desc4 desc4 = (com.udfv.ecma167.UDF_desc4)desc;
		    byte[] implid = desc4.getImplId().getIdSuffix().getData();
		    env.recorded_udf_revision = UDF_Util.b2uint16(implid, 0);
		    revision = UDF_Util.b2uint16(implid, 0);
		}
		else if(com.udfv.ecma167.UDF_desc5.class.isAssignableFrom(desc.getClass())){
		    com.udfv.ecma167.UDF_desc5 partDesc = (com.udfv.ecma167.UDF_desc5)desc;
		}
		else if(com.udfv.ecma167.UDF_desc6.class.isAssignableFrom(desc.getClass())){
		    ;
		}
		else if(com.udfv.ecma167.UDF_desc3.class.isAssignableFrom(desc.getClass())){
		    com.udfv.ecma167.UDF_desc3 desc3 = (com.udfv.ecma167.UDF_desc3)desc;

		    UDF_pad pad2 = new UDF_pad(this, null, null, env.LBS);
		    pad2.readFrom(rae);
		    appendChild(pad2);

		    //次のVDSの場所を取得
		    UDF_extent_ad ext = desc3.getNextVolDescSeqExtent();
		    UDF_VDS new_vds = (UDF_VDS)createElement("UDF_VDS", null, null);//ext.genExtent();
		    int nextloc = ext.getExtentLoc().getIntValue();
		    int nextlen = ext.getExtentLen().getIntValue();
		    
		    if(nextlen != 0){
			new_vds.addExtent(nextloc, -1, nextlen, 0);

			env.root.appendChild(new_vds);

			new_vds.setPrevVDS(this);
			setNextVDS(new_vds);

			String label = UDF_Label.genExtentLabel(new_vds);
			new_vds.setAttribute("id", label);
			ext.getExtentLoc().setAttribute("ref", label + ".loc");
			ext.getExtentLen().setAttribute("ref", label + ".len");
			
			new_vds.readFrom(f);
		    }
		    break;
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
		//e.printStackTrace();
		throw new UDF_DataException(this, "VDS Data Exception");
	    }
	}

	try{
	    UDF_pad pad = new UDF_pad(this, null, null, (int)rae.length());
	    pad.readFrom(rae);
	    appendChild(pad);
	}
	catch(UDF_EOFException e){
	    e.printStackTrace();
	}

	UDF_ElementList desc5_list = getPrevailingPartDesc();
	for(int i=0 ; i<desc5_list.size() ; ++i){
	    UDF_VolDesc desc5 = (UDF_VolDesc)desc5_list.elementAt(i);
	    desc5.postVolReadHook(f);
	}

	UDF_ElementList desc6_list = getPrevailingLogicalVolDesc();
	for(int i=0 ; i<desc6_list.size() ; ++i){
	    UDF_VolDesc desc6 = (UDF_VolDesc)desc6_list.elementAt(i);
	    desc6.postVolReadHook(f);
	}

	return (int)rae.length();
    }

    public long writeTo(UDF_RandomAccess f) throws UDF_Exception,IOException{
	return super.writeTo(f);
    }

    public void recalcVDSLIST(){
	my_vdslist.removeAllElements();
	UDF_Element[] child = getChildren();
	for(int i=0 ; i<child.length ; ++i){
	    if(com.udfv.ecma167.UDF_CrcDesc.class.isAssignableFrom(child[i].getClass()))
		my_vdslist.add(child[i]);
	    if(com.udfv.ecma167.UDF_desc3.class.isAssignableFrom(child[i].getClass())){
		UDF_extent_ad ext = ((UDF_desc3)child[i]).getNextVolDescSeqExtent();
		String label = ext.getExtentLoc().getAttribute("ref");
		label = UDF_Util.car(label, '.');
		UDF_VDS nxt_vds = (UDF_VDS)findById(label);
		if(nxt_vds != null){
		    this.setNextVDS(nxt_vds);
		    nxt_vds.setPrevVDS(this);
		    nxt_vds.recalcVDSLIST();
		}
	    }
	}
    }
    public void recalc(short type, UDF_RandomAccess f){
	super.recalc(type, f);
	try{
	    if(type == RECALC_VDSLIST2)
		recalcVDSLIST();
	    if(type == RECALC_VDSLIST){
		recalcVDSLIST();

		//desc5でやっていたものをここでやる
		UDF_ElementList el = getPrevailingPartDesc();
		for(int i=0 ; i<el.size() ; ++i){
		    ((UDF_desc5)el.elementAt(i)).recalcPD();
		}

		//desc6でやっていたものをここでやる
		el = getPrevailingLogicalVolDesc();
		for(int i=0 ; i<el.size() ; ++i){
		    ((UDF_desc6)el.elementAt(i)).recalcLVD();
		}
	    }
	}
	catch(Exception e){
	    ignoreMsg("recalc", e);
	}
    }

    public void readFromXML(Node n) throws UDF_Exception{

	//↓これやらないとLABELがはいらねぇ……
	//2005/5/20修正 by issei
	preReadFromXMLHook(n);

	my_vdslist.removeAllElements();// = new UDF_ElementList();
	readExtentFromXML(n);

	//このVDS(Extent)のGlobalPointを再計算しないとエラーに反映されない。
	//2005/05/24 add by M.Yazaki
	UDF_RandomAccessExtent rae = genRandomAccessExtent();
	setGlobalPoint(rae);

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
	Vector volseq = new Vector();
	boolean terminate = false;
	boolean hasdesc1  = false;
	
	final String ERRMSG = "An extent of a Volume Descriptor Sequence shall be recorded according to the schema shown in figure 3/4.";
	final short category = UDF_Error.C_ECMA167;
	
	
	// desc3 で飛ばされた領域単体では検証できないのでやらない
	if(prev_extent != null)
	    return el;
	
	UDF_Element[] child = getVDSList().toElemArray();
	
	for(int i = 0; i < child.length; i++){

	    String name = child[i].getName();
	    
	    if(name.equals("UDF_desc1")){  // Primary Vol Desc
		
		if(terminate){
		    
		    el.addError(new UDF_Error(category, UDF_Error.L_ERROR, "volume descriptor(Primary Volume Descriptor)",
					      ERRMSG, "3/8.4.2"));
		}
		com.udfv.ecma167.UDF_desc1 desc = (com.udfv.ecma167.UDF_desc1)child[i];
		volseq.add(desc);
		hasdesc1 = true;
	    }
	    else if(name.equals("UDF_desc4")){  // Impl Use Vol Desc
		
		if(terminate){
		    
		    el.addError(new UDF_Error(category, UDF_Error.L_ERROR, "volume descriptor(Implementation Use Volume Descriptor)",
					      ERRMSG, "3/8.4.2"));
		}
		com.udfv.ecma167.UDF_desc4 desc = (com.udfv.ecma167.UDF_desc4)child[i];
		volseq.add(desc);
	    }
	    else if(name.equals("UDF_desc5")){  // Part Desc
		
		if(terminate){
		    
		    el.addError(new UDF_Error(category, UDF_Error.L_ERROR, "volume descriptor(Partition Descriptor)",
					      ERRMSG, "3/8.4.2"));
		}
		com.udfv.ecma167.UDF_desc5 desc = (com.udfv.ecma167.UDF_desc5)child[i];
		volseq.add(desc);
	    }
	    else if(name.equals("UDF_desc6")){  // Logical Vol Desc
		
		if(terminate){
		    
		    el.addError(new UDF_Error(category, UDF_Error.L_ERROR, "volume descriptor(Logical Volume Descriptor)",
					      ERRMSG, "3/8.4.2"));
		}
		com.udfv.ecma167.UDF_desc6 desc = (com.udfv.ecma167.UDF_desc6)child[i];
		volseq.add(desc);
	    }
	    else if(name.equals("UDF_desc7")){  // Unallocated Space Desc
		
		if(terminate){
		    
		    el.addError(new UDF_Error(category, UDF_Error.L_ERROR, "volume descriptor(Unallocated Space Descriptor)",
					      ERRMSG, "3/8.4.2"));
		}
		com.udfv.ecma167.UDF_desc7 desc = (com.udfv.ecma167.UDF_desc7)child[i];
		volseq.add(desc);
	    }
	    else if(name.equals("UDF_desc3")){  // Volume Desc Pointer
		
		com.udfv.ecma167.UDF_desc3 desc = (com.udfv.ecma167.UDF_desc3)child[i];
		volseq.add(desc);
//		if(!terminate)
//		    terminate = true;
		if(terminate)//else  // すでにターミネートされている
		    el.addError(new UDF_Error(category, UDF_Error.L_ERROR, "Terminator(Volume Descriptro Pointer)", ERRMSG, "3/8.4.2"));
	    }
	    else if(name.equals("UDF_desc8")){  // Terminating Desc
	    
		if(!terminate)
		    terminate = true;
		else  // すでにターミネートされている
		    el.addError(new UDF_Error(category, UDF_Error.L_ERROR, "Terminator(Terminating Descriptor)", ERRMSG, "3/8.4.2"));
	    }
	    else if(name.equals("UDF_pad")){
		;
	    }
	    else{  // unrecorded logical sector?
		
		el.addError(new UDF_Error(category, UDF_Error.L_ERROR, "Unknown Data(" + name + ")", ERRMSG, "3/8.4.2"));
	    }
	}
	
	
	// Primary Vol Desc が1 つも存在しない
	if(!hasdesc1){
	    
	    UDF_Error err = new UDF_Error
		(category, UDF_Error.L_ERROR, "",
		 "A Volume Descriptor Sequence shall contain one or more Primary Volume Descriptors.", "3/8.4.1");

	    el.addError(err);
	}
	el.setGlobalPoint(getGlobalPoint());
	
	// 全てのVolume Descriptor Sequece Number は、1から始まって登場順に+1 されていくべきである
	// →訂正 何から始まってもよいが+1されていくべきである
	UDF_Element vd0 = (UDF_Element)volseq.elementAt(0);
	long cur0 = ((UDF_VolDescSeqNum)vd0).getVolDescSeqNumber().getLongValue();
	for(int i = 0; i < volseq.size(); i++){
	    
	    UDF_Element vd = (UDF_Element)volseq.elementAt(i);
	    long cur  = ((UDF_VolDescSeqNum)vd).getVolDescSeqNumber().getLongValue();
	    if(i + cur0 != cur){
		
		UDF_Error err = new UDF_Error
		    (category, UDF_Error.L_WARNING, "Volume descriptors[" + i + "]",
		     "Typically, an originating system will chose a new Volume Descriptor Sequence Number by adding 1 " +
		     "to the largest such number seen when scanning the Volume Descriptor Sequence.",
		     "3/8.4.1", String.valueOf(cur), String.valueOf(i + 1));

		err.setGlobalPoint(((UDF_CrcDesc)vd).getGlobalPoint());
		el.addError(err);
	    }
	}
	
//	el.setGlobalPoint(getGlobalPoint());
	
	// 子の検証は個々のglobal point を設定したいので別途ここで追加する
	for(int i = 0; i < child.length; i++)
	    el.addError(child[i].verify());
	
	el.setRName("Volume Descriptor Sequence");
	return el;
    }
    
    public void debug(int indent){
	super.debug(indent);
    }
}
