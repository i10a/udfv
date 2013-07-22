/*
*/
package com.udfv.ecma167;

import java.io.*;
import java.util.*;
import org.w3c.dom.*;
import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.encoding.*;
import com.udfv.access.*;
import com.udfv.exception.*;
import com.udfv.ecma167.*;

/**
Partition&nbsp;Descriptor&nbsp;を表現するクラス。<br/>


<dl>

<dt>フィールド一覧</dt>
<dd>
<table border="1">
<tr><td>メンバ</td><td>型</td><td>サイズ</td></tr>
<tr><td><b>DescTag</b></td><td><b>UDF_tag</b></td><td><i>descTag.getSize()</i></td></tr>
<tr><td><b>VolDescSeqNumber</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>PartFlags</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td></tr>
<tr><td><b>PartNumber</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td></tr>
<tr><td><b>PartContents</b></td><td><b>UDF_regid</b></td><td><i>partContents.getSize()</i></td></tr>
<tr><td><b>PartContentsUse</b></td><td><b>UDF_bytes</b></td><td><i>128</i></td></tr>
<tr><td><b>AccessType</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>PartStartingLoc</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>PartLen</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>ImplId</b></td><td><b>UDF_regid</b></td><td><i>implId.getSize()</i></td></tr>
<tr><td><b>ImplUse</b></td><td><b>UDF_bytes</b></td><td><i>128</i></td></tr>
<tr><td><b>Reserved</b></td><td><b>UDF_bytes</b></td><td><i>156</i></td></tr>
</table>
</dd>
</dl>
*/
public class UDF_desc5 extends UDF_CrcDesc implements UDF_VolDesc, UDF_VolDescSeqNum, Comparable
{

    /**
	このオブジェクトのパッケージ名を除いたクラス名を取得する。

	@return クラス名
    */
    public static String getUDFClassName( ) {
        return "UDF_desc5";
    }

    /**
	コンストラクタ。

	@param elem   親エレメント。
	@param prefix ネームスペース。
	@param name   エレメント名。
    */
    public UDF_desc5(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }
    /**
	このUDFエレメントのサイズを返す。

	@return UDFエレメントのサイズ。
    */
    public int getSize(){
	return 0+descTag.getSize()+4+2+2+partContents.getSize()+partContentsUse.getSize()+4+4+4+implId.getSize()+implUse.getSize()+reserved.getSize();
    }

    /**
	このUDFエレメントのサイズをlong 型で返す。

	@return UDFエレメントのサイズ。
    */
    public long getLongSize(){
	return (long)0+descTag.getSize()+4+2+2+partContents.getSize()+partContentsUse.getSize()+4+4+4+implId.getSize()+implUse.getSize()+reserved.getSize();
    }
    private UDF_tag descTag;
    private UDF_uint32 volDescSeqNumber;
    private UDF_uint16 partFlags;
    private UDF_uint16 partNumber;
    private UDF_regid partContents;
    private UDF_bytes partContentsUse;
    private UDF_uint32 accessType;
    private UDF_uint32 partStartingLoc;
    private UDF_uint32 partLen;
    private UDF_regid implId;
    private UDF_bytes implUse;
    private UDF_bytes reserved;

    /**
	descTagを取得する。

	@return 取得したdescTag を返す。
    */
    public UDF_tag getDescTag(){return descTag;}
    /**
	volDescSeqNumberを取得する。

	@return 取得したvolDescSeqNumber を返す。
    */
    public UDF_uint32 getVolDescSeqNumber(){return volDescSeqNumber;}
    /**
	partFlagsを取得する。

	@return 取得したpartFlags を返す。
    */
    public UDF_uint16 getPartFlags(){return partFlags;}
    /**
	partNumberを取得する。

	@return 取得したpartNumber を返す。
    */
    public UDF_uint16 getPartNumber(){return partNumber;}
    /**
	partContentsを取得する。

	@return 取得したpartContents を返す。
    */
    public UDF_regid getPartContents(){return partContents;}
    /**
	partContentsUseを取得する。

	@return 取得したpartContentsUse を返す。
    */
    public UDF_bytes getPartContentsUse(){return partContentsUse;}
    /**
	accessTypeを取得する。

	@return 取得したaccessType を返す。
    */
    public UDF_uint32 getAccessType(){return accessType;}
    /**
	partStartingLocを取得する。

	@return 取得したpartStartingLoc を返す。
    */
    public UDF_uint32 getPartStartingLoc(){return partStartingLoc;}
    /**
	partLenを取得する。

	@return 取得したpartLen を返す。
    */
    public UDF_uint32 getPartLen(){return partLen;}
    /**
	implIdを取得する。

	@return 取得したimplId を返す。
    */
    public UDF_regid getImplId(){return implId;}
    /**
	implUseを取得する。

	@return 取得したimplUse を返す。
    */
    public UDF_bytes getImplUse(){return implUse;}
    /**
	reservedを取得する。

	@return 取得したreserved を返す。
    */
    public UDF_bytes getReserved(){return reserved;}

    /**
	descTagを設定する。

	@param	v 設定する値。
    */
    public void setDescTag(UDF_tag v){replaceChild(v, descTag); descTag = v;}
    /**
	volDescSeqNumberを設定する。

	@param	v 設定する値。
    */
    public void setVolDescSeqNumber(UDF_uint32 v){replaceChild(v, volDescSeqNumber); volDescSeqNumber = v;}
    /**
	partFlagsを設定する。

	@param	v 設定する値。
    */
    public void setPartFlags(UDF_uint16 v){replaceChild(v, partFlags); partFlags = v;}
    /**
	partNumberを設定する。

	@param	v 設定する値。
    */
    public void setPartNumber(UDF_uint16 v){replaceChild(v, partNumber); partNumber = v;}
    /**
	partContentsを設定する。

	@param	v 設定する値。
    */
    public void setPartContents(UDF_regid v){replaceChild(v, partContents); partContents = v;}
    /**
	partContentsUseを設定する。

	@param	v 設定する値。
    */
    public void setPartContentsUse(UDF_bytes v){replaceChild(v, partContentsUse); partContentsUse = v;}
    /**
	accessTypeを設定する。

	@param	v 設定する値。
    */
    public void setAccessType(UDF_uint32 v){replaceChild(v, accessType); accessType = v;}
    /**
	partStartingLocを設定する。

	@param	v 設定する値。
    */
    public void setPartStartingLoc(UDF_uint32 v){replaceChild(v, partStartingLoc); partStartingLoc = v;}
    /**
	partLenを設定する。

	@param	v 設定する値。
    */
    public void setPartLen(UDF_uint32 v){replaceChild(v, partLen); partLen = v;}
    /**
	implIdを設定する。

	@param	v 設定する値。
    */
    public void setImplId(UDF_regid v){replaceChild(v, implId); implId = v;}
    /**
	implUseを設定する。

	@param	v 設定する値。
    */
    public void setImplUse(UDF_bytes v){replaceChild(v, implUse); implUse = v;}
    /**
	reservedを設定する。

	@param	v 設定する値。
    */
    public void setReserved(UDF_bytes v){replaceChild(v, reserved); reserved = v;}

    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long rsize = 0;
	preReadHook(f);
	descTag = (UDF_tag)createElement("UDF_tag", "", "desc-tag");
	rsize += descTag.readFrom(f);
	volDescSeqNumber = (UDF_uint32)createElement("UDF_uint32", "", "vol-desc-seq-number");
	rsize += volDescSeqNumber.readFrom(f);
	partFlags = (UDF_uint16)createElement("UDF_uint16", "", "part-flags");
	rsize += partFlags.readFrom(f);
	partNumber = (UDF_uint16)createElement("UDF_uint16", "", "part-number");
	rsize += partNumber.readFrom(f);
	partContents = (UDF_regid)createElement("UDF_regid", "", "part-contents");
	rsize += partContents.readFrom(f);
	partContentsUse = (UDF_bytes)createElement("UDF_bytes", "", "part-contents-use");
	partContentsUse.setSize(128);
	rsize += partContentsUse.readFrom(f);
	accessType = (UDF_uint32)createElement("UDF_uint32", "", "access-type");
	rsize += accessType.readFrom(f);
	partStartingLoc = (UDF_uint32)createElement("UDF_uint32", "", "part-starting-loc");
	rsize += partStartingLoc.readFrom(f);
	partLen = (UDF_uint32)createElement("UDF_uint32", "", "part-len");
	rsize += partLen.readFrom(f);
	implId = (UDF_regid)createElement("UDF_regid", "", "impl-id");
	rsize += implId.readFrom(f);
	implUse = (UDF_bytes)createElement("UDF_bytes", "", "impl-use");
	implUse.setSize(128);
	rsize += implUse.readFrom(f);
	reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
	reserved.setSize(156);
	rsize += reserved.readFrom(f);
	apply( );
	postReadHook(f);
	return rsize;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long wsize = 0;
	wsize += descTag.writeTo(f);
	wsize += volDescSeqNumber.writeTo(f);
	wsize += partFlags.writeTo(f);
	wsize += partNumber.writeTo(f);
	wsize += partContents.writeTo(f);
	wsize += partContentsUse.writeTo(f);
	wsize += accessType.writeTo(f);
	wsize += partStartingLoc.writeTo(f);
	wsize += partLen.writeTo(f);
	wsize += implId.writeTo(f);
	wsize += implUse.writeTo(f);
	wsize += reserved.writeTo(f);
	return wsize;
    }

    /**
	XMLのノードを指定して読み込む。

	@param n 読み込み先ノード。
    */
    public void readFromXML(Node n) throws UDF_Exception{
	preReadFromXMLHook(n);
	NodeList nl = n.getChildNodes();

	for(int i=0 ; i<nl.getLength() ; ++i){
	    Node child = nl.item(i);
	    if(child.getNodeType() != Node.ELEMENT_NODE)
		continue;
	    String name = child.getLocalName();
	    if(false)
		;
	    else if(name.equals("desc-tag")){
		descTag = (UDF_tag)createElement("UDF_tag", "", "desc-tag");
		descTag.readFromXML(child);
	    }
	    else if(name.equals("vol-desc-seq-number")){
		volDescSeqNumber = (UDF_uint32)createElement("UDF_uint32", "", "vol-desc-seq-number");
		volDescSeqNumber.readFromXML(child);
	    }
	    else if(name.equals("part-flags")){
		partFlags = (UDF_uint16)createElement("UDF_uint16", "", "part-flags");
		partFlags.readFromXML(child);
	    }
	    else if(name.equals("part-number")){
		partNumber = (UDF_uint16)createElement("UDF_uint16", "", "part-number");
		partNumber.readFromXML(child);
	    }
	    else if(name.equals("part-contents")){
		partContents = (UDF_regid)createElement("UDF_regid", "", "part-contents");
		partContents.readFromXML(child);
	    }
	    else if(name.equals("part-contents-use")){
		partContentsUse = (UDF_bytes)createElement("UDF_bytes", "", "part-contents-use");
		partContentsUse.setSize(128);
		partContentsUse.readFromXML(child);
	    }
	    else if(name.equals("access-type")){
		accessType = (UDF_uint32)createElement("UDF_uint32", "", "access-type");
		accessType.readFromXML(child);
	    }
	    else if(name.equals("part-starting-loc")){
		partStartingLoc = (UDF_uint32)createElement("UDF_uint32", "", "part-starting-loc");
		partStartingLoc.readFromXML(child);
	    }
	    else if(name.equals("part-len")){
		partLen = (UDF_uint32)createElement("UDF_uint32", "", "part-len");
		partLen.readFromXML(child);
	    }
	    else if(name.equals("impl-id")){
		implId = (UDF_regid)createElement("UDF_regid", "", "impl-id");
		implId.readFromXML(child);
	    }
	    else if(name.equals("impl-use")){
		implUse = (UDF_bytes)createElement("UDF_bytes", "", "impl-use");
		implUse.setSize(128);
		implUse.readFromXML(child);
	    }
	    else if(name.equals("reserved")){
		reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
		reserved.setSize(156);
		reserved.readFromXML(child);
	    }
	    else{
		System.err.println("Unknown XML tag:" + name);
	    }
	}
	apply( );
	postReadFromXMLHook(n);

	//読んだら消す(もういらないので)
	n.getParentNode().removeChild(n);
    }

    /**
	初期値を設定する
    */
    public void setDefaultValue(){
	super.setDefaultValue();
	descTag = (UDF_tag)createElement("UDF_tag", "", "desc-tag");
	descTag.setDefaultValue();
	volDescSeqNumber = (UDF_uint32)createElement("UDF_uint32", "", "vol-desc-seq-number");
	volDescSeqNumber.setDefaultValue();
	partFlags = (UDF_uint16)createElement("UDF_uint16", "", "part-flags");
	partFlags.setDefaultValue();
	partNumber = (UDF_uint16)createElement("UDF_uint16", "", "part-number");
	partNumber.setDefaultValue();
	partContents = (UDF_regid)createElement("UDF_regid", "", "part-contents");
	partContents.setDefaultValue();
	partContentsUse = (UDF_bytes)createElement("UDF_bytes", "", "part-contents-use");
	partContentsUse.setSize(128);
	partContentsUse.setDefaultValue();
	accessType = (UDF_uint32)createElement("UDF_uint32", "", "access-type");
	accessType.setDefaultValue();
	partStartingLoc = (UDF_uint32)createElement("UDF_uint32", "", "part-starting-loc");
	partStartingLoc.setDefaultValue();
	partLen = (UDF_uint32)createElement("UDF_uint32", "", "part-len");
	partLen.setDefaultValue();
	implId = (UDF_regid)createElement("UDF_regid", "", "impl-id");
	implId.setDefaultValue();
	implUse = (UDF_bytes)createElement("UDF_bytes", "", "impl-use");
	implUse.setSize(128);
	implUse.setDefaultValue();
	reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
	reserved.setSize(156);
	reserved.setDefaultValue();

	apply( );
	postSetDefaultValueHook();
    }


    /**
	複製する
    */
    public UDF_Element duplicateElement(){
	UDF_desc5 dup = new UDF_desc5(this, null, getName());
	dup.setDefaultValue();
	dup.duplicateHook(this);
	dup.setDescTag((UDF_tag)descTag.duplicateElement());
	dup.setVolDescSeqNumber((UDF_uint32)volDescSeqNumber.duplicateElement());
	dup.setPartFlags((UDF_uint16)partFlags.duplicateElement());
	dup.setPartNumber((UDF_uint16)partNumber.duplicateElement());
	dup.setPartContents((UDF_regid)partContents.duplicateElement());
	dup.setPartContentsUse((UDF_bytes)partContentsUse.duplicateElement());
	dup.setAccessType((UDF_uint32)accessType.duplicateElement());
	dup.setPartStartingLoc((UDF_uint32)partStartingLoc.duplicateElement());
	dup.setPartLen((UDF_uint32)partLen.duplicateElement());
	dup.setImplId((UDF_regid)implId.duplicateElement());
	dup.setImplUse((UDF_bytes)implUse.duplicateElement());
	dup.setReserved((UDF_bytes)reserved.duplicateElement());

	apply();

	return dup;
    }

    /**
        子の UDF Element の Node を append しなおす。

    */
    private void apply( ) {

	removeAllChildren();
	appendChild(descTag);
	appendChild(volDescSeqNumber);
	appendChild(partFlags);
	appendChild(partNumber);
	appendChild(partContents);
	appendChild(partContentsUse);
	appendChild(accessType);
	appendChild(partStartingLoc);
	appendChild(partLen);
	appendChild(implId);
	appendChild(implUse);
	appendChild(reserved);
    }

    public String getInfo(int indent){
	String a = "";
	a += UDF_Util.repeat(' ', indent);
	a += descTag.getInfo(indent + 1);
	a += volDescSeqNumber.getInfo(indent + 1);
	a += partFlags.getInfo(indent + 1);
	a += partNumber.getInfo(indent + 1);
	a += partContents.getInfo(indent + 1);
	a += partContentsUse.getInfo(indent + 1);
	a += accessType.getInfo(indent + 1);
	a += partStartingLoc.getInfo(indent + 1);
	a += partLen.getInfo(indent + 1);
	a += implId.getInfo(indent + 1);
	a += implUse.getInfo(indent + 1);
	a += reserved.getInfo(indent + 1);
      return a;
    }
    public void debug(int indent){
	System.err.print(UDF_Util.repeat(' ', indent));

	String info = " gp=" + getGlobalPoint();
	info += " pn=" + getElemPartRefNo();
	info += " sn=" + getPartSubno();
	System.err.println(getName() + ": (" + hashCode() + ")" + info);	descTag.debug(indent + 1);
	volDescSeqNumber.debug(indent + 1);
	partFlags.debug(indent + 1);
	partNumber.debug(indent + 1);
	partContents.debug(indent + 1);
	partContentsUse.debug(indent + 1);
	accessType.debug(indent + 1);
	partStartingLoc.debug(indent + 1);
	partLen.debug(indent + 1);
	implId.debug(indent + 1);
	implUse.debug(indent + 1);
	reserved.debug(indent + 1);
    }
//begin:add your code here

    public void postVolReadHook(UDF_RandomAccess f) throws UDF_Exception{
	/*
	  ECMA167 4/3.1
	  
	  If the volume is recorded according to Part3, the Partition
	  Contents Use field of the Partition Descriptor describing
	  the partition shall be recorded as Partition Header Desc.

	  Such a Partition Descriptor shall have "+NSR03" recorded in
	  the Partition Contents Field
	 */
	if(UDF_Util.cmpBytesString(getPartContents().getId().getData(), "+NSR02") ||
	   UDF_Util.cmpBytesString(getPartContents().getId().getData(), "+NSR03")){
	    com.udfv.ecma167.UDF_PartHeaderDesc d = 
		(com.udfv.ecma167.UDF_PartHeaderDesc)createElement("UDF_PartHeaderDesc", null, "UDF_PartHeaderDesc");
	    getPartContentsUse().readFromAndReplaceChild(d);

	    if(d.getUnallocatedSpaceBitmap().getExtentLen().getIntValue() >= 0){
		int partno = getPartNumber().getIntValue();
		String partsfx = "" + partno;
		d.getUnallocatedSpaceBitmap().getExtentLen().setAttribute("ref", "SB" + partsfx + ".len");
		d.getUnallocatedSpaceBitmap().getExtentPos().setAttribute("ref", "SB" + partsfx + ".pos");
	    }
	}
	//env.part_len = getPartLen().getLongValue() * env.LBS;
	//env.part_loc = getPartStartingLoc().getIntValue();

    }

    public int getFixedTagId() { return 5; }

    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	UDF_Error ret;
	final short category = UDF_Error.C_ECMA167;
	
	
	el.addError(super.verify());  // Descriptor Tag
	
	ret = verifyId();
	if(ret.isError()){
	    
	    ret.setRefer("3/10.5.1");
	    el.addError(ret);
	}
	
	if((partFlags.getIntValue() & 0xfffe) != 0){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Partition Flags",
			 "Shall be reserved for future standardisation and all bits shall be set to ZERO.",
			 "3/10.5.3", String.valueOf(partFlags.getIntValue()), ""));
	}
	
	if(partNumber.getIntValue() != 0){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_WARNING, "Partition Number",
			 "The Partition Number may be 0.",
			 "3/10.5.4", String.valueOf(partNumber.getIntValue()), ""));
	}
	
	el.addError(partContents.verify("Partition Contents"));
	el.addError(partContentsUse.verify("Partition Contents Use"));
	
	// regid のIdentifier の値が以下の値のいずれとも一致しない
	final String PCINTERPRET[] = { "+NSR03", "+FDC01", "+CD001", "+CDW02", };
	int  i = 0;
	
	
	for(; i < PCINTERPRET.length; i++){
	    
	    ret = partContents.verifyId(PCINTERPRET[i]);
	    if(!ret.isError())
		break;
	}
	
	if(i == PCINTERPRET.length){
	    
	    ret.setMessage("This field shall specify an indentification of how to interpret the contents of the partition. " + 
			   "The identification specified by Part 3 are given in figure 3/13.\n" +
			   "Other identifications shall be specified according to 1/7.4.");
	    ret.setRefer("3/10.5.5");
	    ret.setRName("Partition Contents");
	    ret.setExpectedValue("");
	    ret.setLevel(UDF_Error.L_CAUTION);
	    
	    el.addError(ret);
	}
	
	if(4 < accessType.getLongValue()){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Access Type",
			 "The access types are given in figure 3/14.\n" +
			 "5 and above: Rserved for future standardisation.",
			 "3/10.5.7", String.valueOf(accessType.getLongValue()), ""));
	}
	
	el.addError(implId.verify("Implementation Identifier"));
	
	if(!UDF_Util.isAllZero(reserved.getData())){
	    
	    el.addError(new UDF_Error
			(UDF_Error.C_ECMA167, UDF_Error.L_ERROR, "Reserved",
			 "This field shall be reserved for future standardisation and all bytes shall be se to #00.",
			 "3/10.3.4"));
	}

	int at = getAccessType().getIntValue();
	
	if(at == 0){
	    el.addError(new UDF_Error
			(UDF_Error.C_ECMA167, UDF_Error.L_CAUTION, "Access Type",
			 "The type of access is not specified by this field.",
			 "3/10.5.7"));
	}
	else if(at != 1 && env.isMediaReadonly()){
		el.addError(new UDF_Error
			    (UDF_Error.C_ECMA167, UDF_Error.L_ERROR, "Access Type",
			     "The access types are given in figure 3/14.\n" +
			     "1: Read only",
			     "3/10.5.7", String.valueOf(at), "1"));
	    }
	else if(at != 2 && env.isMediaWriteonce()){
	    el.addError(new UDF_Error
			(UDF_Error.C_ECMA167, UDF_Error.L_ERROR, "Access Type",
			 "The access types are given in figure 3/14.\n" +
			 "2: Write once",
			 "3/10.5.7", String.valueOf(at), "2"));
	}
	else if(at != 3 && env.isMediaRewritable()){
	    el.addError(new UDF_Error
			(UDF_Error.C_ECMA167, UDF_Error.L_ERROR, "Access Type",
			 "The access types are given in figure 3/14.\n" +
			 "3: Rewritable",
			 "3/10.5.7", String.valueOf(at), "3"));
	}
	else if(at !=4 && env.isMediaOverwritable()){
	    el.addError(new UDF_Error
			(UDF_Error.C_ECMA167, UDF_Error.L_ERROR, "Access Type",
			 "The access types are given in figure 3/14.\n" +
			 "4: Overwritable",
			 "3/10.5.7", String.valueOf(at), "4"));
	}
	
	el.setGlobalPoint(getGlobalPoint());
	el.setRName("Partition Descriptor");
	return el;
    }
    public void recalcPD(){
	try{
	    UDF_ElementBase eb = getPartContentsUse();
	    UDF_PartHeaderDesc phd = (UDF_PartHeaderDesc)eb.getFirstChild();
	    /*
	    if(eb != null){//自分が MVDSにいるのか RVDSにいるのか
		UDF_VDS parent = (UDF_VDS)getParent();
		if(parent.getFirstVDS().getName().equals("mvds"))
		    env.mainPartHeaderDesc = phd;
		else
		    env.reservePartHeaderDesc = phd;
	    }
	    */
	    /*
	    String label = UDF_Util.car(phd.getUnallocatedSpaceBitmap().getExtentLen().getAttribute("ref"), '.');
	    eb = findById(label);
	    if(eb != null){
		env.spaceBitmapDesc = (UDF_desc264)eb.getFirstChild();
	    }
	    */
	}
	catch(Exception e){
	    ignoreMsg("recalc", e);
	}
    }
    public UDF_PartHeaderDesc getPartHeaderDesc() throws UDF_VolException{
	try{
	    return (UDF_PartHeaderDesc)getPartContentsUse().getFirstChild();
	}
	catch(ClassCastException e){
	    throw new UDF_VolException(this, "no part header desc");
	}
    }
    public void recalc(short type, UDF_RandomAccess f){
	super.recalc(type, f);
	/*
	  desc5は複数ありうる。そのうちの prevailingだけが信用できるの
	  だが、desc5内だけでは自身が prevailingかどうかわからない。
	  よって、ここでは recalcしない
	if(type == RECALC_ENV){
	    try{
		{
		    UDF_ElementBase eb = getPartContentsUse();
		    if(eb != null){//自分が MVDSにいるのか RVDSにいるのか
			if(getParent().getName().equals("mvds"))
			    env.mainPartHeaderDesc = (UDF_PartHeaderDesc)eb.getFirstChild();
			else if(getParent().getName().equals("rvds"))
			    env.reservePartHeaderDesc = (UDF_PartHeaderDesc)eb.getFirstChild();
		    }
		}
		if(env.spaceBitmapDesc == null){
		    UDF_ElementBase eb = findById("SB");
		    if(eb != null)
			env.spaceBitmapDesc = (UDF_desc264)eb.getFirstChild();
		}
	    }
	    catch(Exception e){
		ignoreMsg("recalc", e);
	    }
	}
	*/
    }
//end:
};
