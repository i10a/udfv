/*
*/
package com.udfv.udf150;

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
Sparable&nbsp;Partition&nbsp;Map&nbsp;を表現するクラス。<br/>


<dl>

<dt>フィールド一覧</dt>
<dd>
<table border="1">
<tr><td>メンバ</td><td>型</td><td>サイズ</td></tr>
<tr><td><b>PartMapType</b></td><td><b>UDF_uint8</b></td><td><i>1</i></td></tr>
<tr><td><b>PartMapLen</b></td><td><b>UDF_uint8</b></td><td><i>1</i></td></tr>
<tr><td><b>Reserved</b></td><td><b>UDF_bytes</b></td><td><i>2</i></td></tr>
<tr><td><b>PartTypeId</b></td><td><b>UDF_regid</b></td><td><i>partTypeId.getSize()</i></td></tr>
<tr><td><b>VolSeqNumber</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td></tr>
<tr><td><b>PartNumber</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td></tr>
<tr><td><b>PacketLen</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td></tr>
<tr><td><b>NumberOfSparingTables</b></td><td><b>UDF_uint8</b></td><td><i>1</i></td></tr>
<tr><td><b>Reserved2</b></td><td><b>UDF_bytes</b></td><td><i>1</i></td></tr>
<tr><td><b>SizeOfEachSparingTable</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>LocsOfSparingTables</b></td><td><b>UDF_bytes</b></td><td><i>getNumberOfSparingTables().getIntValue()*4</i></td></tr>
<tr><td><b>Pad</b></td><td><b>UDF_bytes</b></td><td><i>16-4*getNumberOfSparingTables().getIntValue()</i></td></tr>
</table>
</dd>
</dl>
*/
public class UDF_SparablePartMap extends UDF_PartMap 
{

    /**
	このオブジェクトのパッケージ名を除いたクラス名を取得する。

	@return クラス名
    */
    public static String getUDFClassName( ) {
        return "UDF_SparablePartMap";
    }

    /**
	コンストラクタ。

	@param elem   親エレメント。
	@param prefix ネームスペース。
	@param name   エレメント名。
    */
    public UDF_SparablePartMap(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }
    /**
	このUDFエレメントのサイズを返す。

	@return UDFエレメントのサイズ。
    */
    public int getSize(){
	return 0+1+1+reserved.getSize()+partTypeId.getSize()+2+2+2+1+reserved2.getSize()+4+locsOfSparingTables.getSize()+pad.getSize();
    }

    /**
	このUDFエレメントのサイズをlong 型で返す。

	@return UDFエレメントのサイズ。
    */
    public long getLongSize(){
	return (long)0+1+1+reserved.getSize()+partTypeId.getSize()+2+2+2+1+reserved2.getSize()+4+locsOfSparingTables.getSize()+pad.getSize();
    }
    private UDF_uint8 partMapType;
    private UDF_uint8 partMapLen;
    private UDF_bytes reserved;
    private com.udfv.ecma167.UDF_regid partTypeId;
    private UDF_uint16 volSeqNumber;
    private UDF_uint16 partNumber;
    private UDF_uint16 packetLen;
    private UDF_uint8 numberOfSparingTables;
    private UDF_bytes reserved2;
    private UDF_uint32 sizeOfEachSparingTable;
    private UDF_bytes locsOfSparingTables;
    private UDF_bytes pad;

    /**
	partMapTypeを取得する。

	@return 取得したpartMapType を返す。
    */
    public UDF_uint8 getPartMapType(){return partMapType;}
    /**
	partMapLenを取得する。

	@return 取得したpartMapLen を返す。
    */
    public UDF_uint8 getPartMapLen(){return partMapLen;}
    /**
	reservedを取得する。

	@return 取得したreserved を返す。
    */
    public UDF_bytes getReserved(){return reserved;}
    /**
	partTypeIdを取得する。

	@return 取得したpartTypeId を返す。
    */
    public com.udfv.ecma167.UDF_regid getPartTypeId(){return partTypeId;}
    /**
	volSeqNumberを取得する。

	@return 取得したvolSeqNumber を返す。
    */
    public UDF_uint16 getVolSeqNumber(){return volSeqNumber;}
    /**
	partNumberを取得する。

	@return 取得したpartNumber を返す。
    */
    public UDF_uint16 getPartNumber(){return partNumber;}
    /**
	packetLenを取得する。

	@return 取得したpacketLen を返す。
    */
    public UDF_uint16 getPacketLen(){return packetLen;}
    /**
	numberOfSparingTablesを取得する。

	@return 取得したnumberOfSparingTables を返す。
    */
    public UDF_uint8 getNumberOfSparingTables(){return numberOfSparingTables;}
    /**
	reserved2を取得する。

	@return 取得したreserved2 を返す。
    */
    public UDF_bytes getReserved2(){return reserved2;}
    /**
	sizeOfEachSparingTableを取得する。

	@return 取得したsizeOfEachSparingTable を返す。
    */
    public UDF_uint32 getSizeOfEachSparingTable(){return sizeOfEachSparingTable;}
    /**
	locsOfSparingTablesを取得する。

	@return 取得したlocsOfSparingTables を返す。
    */
    public UDF_bytes getLocsOfSparingTables(){return locsOfSparingTables;}
    /**
	padを取得する。

	@return 取得したpad を返す。
    */
    public UDF_bytes getPad(){return pad;}

    /**
	partMapTypeを設定する。

	@param	v 設定する値。
    */
    public void setPartMapType(UDF_uint8 v){replaceChild(v, partMapType); partMapType = v;}
    /**
	partMapLenを設定する。

	@param	v 設定する値。
    */
    public void setPartMapLen(UDF_uint8 v){replaceChild(v, partMapLen); partMapLen = v;}
    /**
	reservedを設定する。

	@param	v 設定する値。
    */
    public void setReserved(UDF_bytes v){replaceChild(v, reserved); reserved = v;}
    /**
	partTypeIdを設定する。

	@param	v 設定する値。
    */
    public void setPartTypeId(com.udfv.ecma167.UDF_regid v){replaceChild(v, partTypeId); partTypeId = v;}
    /**
	volSeqNumberを設定する。

	@param	v 設定する値。
    */
    public void setVolSeqNumber(UDF_uint16 v){replaceChild(v, volSeqNumber); volSeqNumber = v;}
    /**
	partNumberを設定する。

	@param	v 設定する値。
    */
    public void setPartNumber(UDF_uint16 v){replaceChild(v, partNumber); partNumber = v;}
    /**
	packetLenを設定する。

	@param	v 設定する値。
    */
    public void setPacketLen(UDF_uint16 v){replaceChild(v, packetLen); packetLen = v;}
    /**
	numberOfSparingTablesを設定する。

	@param	v 設定する値。
    */
    public void setNumberOfSparingTables(UDF_uint8 v){replaceChild(v, numberOfSparingTables); numberOfSparingTables = v;}
    /**
	reserved2を設定する。

	@param	v 設定する値。
    */
    public void setReserved2(UDF_bytes v){replaceChild(v, reserved2); reserved2 = v;}
    /**
	sizeOfEachSparingTableを設定する。

	@param	v 設定する値。
    */
    public void setSizeOfEachSparingTable(UDF_uint32 v){replaceChild(v, sizeOfEachSparingTable); sizeOfEachSparingTable = v;}
    /**
	locsOfSparingTablesを設定する。

	@param	v 設定する値。
    */
    public void setLocsOfSparingTables(UDF_bytes v){replaceChild(v, locsOfSparingTables); locsOfSparingTables = v;}
    /**
	padを設定する。

	@param	v 設定する値。
    */
    public void setPad(UDF_bytes v){replaceChild(v, pad); pad = v;}

    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long rsize = 0;
	preReadHook(f);
	partMapType = (UDF_uint8)createElement("UDF_uint8", "", "part-map-type");
	rsize += partMapType.readFrom(f);
	partMapLen = (UDF_uint8)createElement("UDF_uint8", "", "part-map-len");
	rsize += partMapLen.readFrom(f);
	reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
	reserved.setSize(2);
	rsize += reserved.readFrom(f);
	partTypeId = (UDF_regid)createElement("UDF_regid", "", "part-type-id");
	rsize += partTypeId.readFrom(f);
	volSeqNumber = (UDF_uint16)createElement("UDF_uint16", "", "vol-seq-number");
	rsize += volSeqNumber.readFrom(f);
	partNumber = (UDF_uint16)createElement("UDF_uint16", "", "part-number");
	rsize += partNumber.readFrom(f);
	packetLen = (UDF_uint16)createElement("UDF_uint16", "", "packet-len");
	rsize += packetLen.readFrom(f);
	numberOfSparingTables = (UDF_uint8)createElement("UDF_uint8", "", "number-of-sparing-tables");
	rsize += numberOfSparingTables.readFrom(f);
	reserved2 = (UDF_bytes)createElement("UDF_bytes", "", "reserved2");
	reserved2.setSize(1);
	rsize += reserved2.readFrom(f);
	sizeOfEachSparingTable = (UDF_uint32)createElement("UDF_uint32", "", "size-of-each-sparing-table");
	rsize += sizeOfEachSparingTable.readFrom(f);
	locsOfSparingTables = (UDF_bytes)createElement("UDF_bytes", "", "locs-of-sparing-tables");
	locsOfSparingTables.setSize(getNumberOfSparingTables().getIntValue()*4);
	rsize += locsOfSparingTables.readFrom(f);
	pad = (UDF_bytes)createElement("UDF_bytes", "", "pad");
	pad.setSize(16-4*getNumberOfSparingTables().getIntValue());
	rsize += pad.readFrom(f);
	apply( );
	postReadHook(f);
	return rsize;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long wsize = 0;
	wsize += partMapType.writeTo(f);
	wsize += partMapLen.writeTo(f);
	wsize += reserved.writeTo(f);
	wsize += partTypeId.writeTo(f);
	wsize += volSeqNumber.writeTo(f);
	wsize += partNumber.writeTo(f);
	wsize += packetLen.writeTo(f);
	wsize += numberOfSparingTables.writeTo(f);
	wsize += reserved2.writeTo(f);
	wsize += sizeOfEachSparingTable.writeTo(f);
	wsize += locsOfSparingTables.writeTo(f);
	wsize += pad.writeTo(f);
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
	    else if(name.equals("part-map-type")){
		partMapType = (UDF_uint8)createElement("UDF_uint8", "", "part-map-type");
		partMapType.readFromXML(child);
	    }
	    else if(name.equals("part-map-len")){
		partMapLen = (UDF_uint8)createElement("UDF_uint8", "", "part-map-len");
		partMapLen.readFromXML(child);
	    }
	    else if(name.equals("reserved")){
		reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
		reserved.setSize(2);
		reserved.readFromXML(child);
	    }
	    else if(name.equals("part-type-id")){
		partTypeId = (UDF_regid)createElement("UDF_regid", "", "part-type-id");
		partTypeId.readFromXML(child);
	    }
	    else if(name.equals("vol-seq-number")){
		volSeqNumber = (UDF_uint16)createElement("UDF_uint16", "", "vol-seq-number");
		volSeqNumber.readFromXML(child);
	    }
	    else if(name.equals("part-number")){
		partNumber = (UDF_uint16)createElement("UDF_uint16", "", "part-number");
		partNumber.readFromXML(child);
	    }
	    else if(name.equals("packet-len")){
		packetLen = (UDF_uint16)createElement("UDF_uint16", "", "packet-len");
		packetLen.readFromXML(child);
	    }
	    else if(name.equals("number-of-sparing-tables")){
		numberOfSparingTables = (UDF_uint8)createElement("UDF_uint8", "", "number-of-sparing-tables");
		numberOfSparingTables.readFromXML(child);
	    }
	    else if(name.equals("reserved2")){
		reserved2 = (UDF_bytes)createElement("UDF_bytes", "", "reserved2");
		reserved2.setSize(1);
		reserved2.readFromXML(child);
	    }
	    else if(name.equals("size-of-each-sparing-table")){
		sizeOfEachSparingTable = (UDF_uint32)createElement("UDF_uint32", "", "size-of-each-sparing-table");
		sizeOfEachSparingTable.readFromXML(child);
	    }
	    else if(name.equals("locs-of-sparing-tables")){
		locsOfSparingTables = (UDF_bytes)createElement("UDF_bytes", "", "locs-of-sparing-tables");
		locsOfSparingTables.setSize(getNumberOfSparingTables().getIntValue()*4);
		locsOfSparingTables.readFromXML(child);
	    }
	    else if(name.equals("pad")){
		pad = (UDF_bytes)createElement("UDF_bytes", "", "pad");
		pad.setSize((getNumberOfSparingTables().getIntValue() < 5)?16-4*getNumberOfSparingTables().getIntValue():0);
		pad.readFromXML(child);
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
	partMapType = (UDF_uint8)createElement("UDF_uint8", "", "part-map-type");
	partMapType.setDefaultValue();
	partMapLen = (UDF_uint8)createElement("UDF_uint8", "", "part-map-len");
	partMapLen.setDefaultValue();
	reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
	reserved.setSize(2);
	reserved.setDefaultValue();
	partTypeId = (UDF_regid)createElement("UDF_regid", "", "part-type-id");
	partTypeId.setDefaultValue();
	volSeqNumber = (UDF_uint16)createElement("UDF_uint16", "", "vol-seq-number");
	volSeqNumber.setDefaultValue();
	partNumber = (UDF_uint16)createElement("UDF_uint16", "", "part-number");
	partNumber.setDefaultValue();
	packetLen = (UDF_uint16)createElement("UDF_uint16", "", "packet-len");
	packetLen.setDefaultValue();
	numberOfSparingTables = (UDF_uint8)createElement("UDF_uint8", "", "number-of-sparing-tables");
	numberOfSparingTables.setDefaultValue();
	reserved2 = (UDF_bytes)createElement("UDF_bytes", "", "reserved2");
	reserved2.setSize(1);
	reserved2.setDefaultValue();
	sizeOfEachSparingTable = (UDF_uint32)createElement("UDF_uint32", "", "size-of-each-sparing-table");
	sizeOfEachSparingTable.setDefaultValue();
	locsOfSparingTables = (UDF_bytes)createElement("UDF_bytes", "", "locs-of-sparing-tables");
	locsOfSparingTables.setSize(getNumberOfSparingTables().getIntValue()*4);
	locsOfSparingTables.setDefaultValue();
	pad = (UDF_bytes)createElement("UDF_bytes", "", "pad");
	pad.setSize(16-4*getNumberOfSparingTables().getIntValue());
	pad.setDefaultValue();

	apply( );
	postSetDefaultValueHook();
    }


    /**
	複製する
    */
    public UDF_Element duplicateElement(){
	UDF_SparablePartMap dup = new UDF_SparablePartMap(this, null, getName());
	dup.setDefaultValue();
	dup.duplicateHook(this);
	dup.setPartMapType((UDF_uint8)partMapType.duplicateElement());
	dup.setPartMapLen((UDF_uint8)partMapLen.duplicateElement());
	dup.setReserved((UDF_bytes)reserved.duplicateElement());
	dup.setPartTypeId((com.udfv.ecma167.UDF_regid)partTypeId.duplicateElement());
	dup.setVolSeqNumber((UDF_uint16)volSeqNumber.duplicateElement());
	dup.setPartNumber((UDF_uint16)partNumber.duplicateElement());
	dup.setPacketLen((UDF_uint16)packetLen.duplicateElement());
	dup.setNumberOfSparingTables((UDF_uint8)numberOfSparingTables.duplicateElement());
	dup.setReserved2((UDF_bytes)reserved2.duplicateElement());
	dup.setSizeOfEachSparingTable((UDF_uint32)sizeOfEachSparingTable.duplicateElement());
	dup.setLocsOfSparingTables((UDF_bytes)locsOfSparingTables.duplicateElement());
	dup.setPad((UDF_bytes)pad.duplicateElement());

	apply();

	return dup;
    }

    /**
        子の UDF Element の Node を append しなおす。

    */
    private void apply( ) {

	removeAllChildren();
	appendChild(partMapType);
	appendChild(partMapLen);
	appendChild(reserved);
	appendChild(partTypeId);
	appendChild(volSeqNumber);
	appendChild(partNumber);
	appendChild(packetLen);
	appendChild(numberOfSparingTables);
	appendChild(reserved2);
	appendChild(sizeOfEachSparingTable);
	appendChild(locsOfSparingTables);
	appendChild(pad);
    }

    public String getInfo(int indent){
	String a = "";
	a += UDF_Util.repeat(' ', indent);
	a += partMapType.getInfo(indent + 1);
	a += partMapLen.getInfo(indent + 1);
	a += reserved.getInfo(indent + 1);
	a += partTypeId.getInfo(indent + 1);
	a += volSeqNumber.getInfo(indent + 1);
	a += partNumber.getInfo(indent + 1);
	a += packetLen.getInfo(indent + 1);
	a += numberOfSparingTables.getInfo(indent + 1);
	a += reserved2.getInfo(indent + 1);
	a += sizeOfEachSparingTable.getInfo(indent + 1);
	a += locsOfSparingTables.getInfo(indent + 1);
	a += pad.getInfo(indent + 1);
      return a;
    }
    public void debug(int indent){
	System.err.print(UDF_Util.repeat(' ', indent));

	String info = " gp=" + getGlobalPoint();
	info += " pn=" + getElemPartRefNo();
	info += " sn=" + getPartSubno();
	System.err.println(getName() + ": (" + hashCode() + ")" + info);	partMapType.debug(indent + 1);
	partMapLen.debug(indent + 1);
	reserved.debug(indent + 1);
	partTypeId.debug(indent + 1);
	volSeqNumber.debug(indent + 1);
	partNumber.debug(indent + 1);
	packetLen.debug(indent + 1);
	numberOfSparingTables.debug(indent + 1);
	reserved2.debug(indent + 1);
	sizeOfEachSparingTable.debug(indent + 1);
	locsOfSparingTables.debug(indent + 1);
	pad.debug(indent + 1);
    }
//begin:add your code here
    //注意)この変数値はまだ使用していない
    UDF_ElementList sparingTable;

    public void postReadHook(UDF_RandomAccess f) throws IOException,UDF_Exception{
	//locsOfSpartingTablesの要素を uint32で置きかえる
	UDF_ElementList v = new UDF_ElementList();
	UDF_RandomAccessBuffer rab = locsOfSparingTables.genRandomAccessBytes();
	while(!rab.eof()){
	    UDF_uint32 u = new UDF_uint32(this, null, null);
	    u.readFrom(rab);
	    u.setAttribute("ref", UDF_Label.genExtentLabel(0, u.getIntValue(), -1, 0));
	    v.add(u);
	}
	locsOfSparingTables.replaceChildren(v);


    }

    /**
        Sparable Part. Map の情報を表示。

    */
    public String [] toInformation(String indent, boolean detail) {

	String nl = UDF_Util.getSystemNewLine();
	Vector vec = new Vector();
	String str;

	str = indent + "Part Map Type             # " + getPartMapType().getIntValue();
	vec.add(str);
	str = indent + "Vol. Seq. Number          # " + getVolSeqNumber().getIntValue();
	vec.add(str);
	str = indent + "Part. Number              # " + getPartNumber().getIntValue();
	vec.add(str);
	str = indent + "Packet Len.               # " + getPacketLen().getIntValue();
	vec.add(str);
	str = indent + "Number Of Sparing Table   # " + getNumberOfSparingTables().getIntValue();
	vec.add(str);
	str = indent + "Loc.s Of Sparing Table    # ";
	UDF_Element [] el = getLocsOfSparingTables().getChildren();
	for (int i = 0, max = el.length; i < max; i++) {
	    str += nl + indent + "                          # " + ((UDF_uint32)el[i]).getIntValue();
	}
	vec.add(str);

	String [] sl = new String[vec.size()];
	for (int i = 0, max = vec.size(); i < max; i++) {
	    sl[i] = (String)vec.elementAt(i);
	}

	return sl;
    }

    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	if(env.udf_revision != 0x150)
	    return el;
	
	// Packet Length は32 でなければならない
	if(getPacketLen().getIntValue() != 32){
	    
	    el.addError(new UDF_Error
			(UDF_Error.C_UDF150, UDF_Error.L_ERROR, "Packet Length",
			 "Packet Length = the number of user data blocks per fixed packet. Shall be set to 32.",
			 "2.2.9", String.valueOf(getPacketLen().getIntValue()), "32"));
	}
	
	el.addError(verifyBase(UDF_Error.C_UDF150, "2.2.9"));
	
	el.setRName("UDF Sparable Partition Map");
	el.setGlobalPoint(getGlobalPoint());
	return el;
    }
    
    /**
       UDF 1.50 以降全リビジョン共通のエラー検証を行う。
       
       @param category  エラーカテゴリ。
       @param refer     エラー参照番号。
       
       @return エラーリストインスタンス。
    */
    protected UDF_ErrorList verifyBase(short category, String refer) throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	UDF_Error ret;
	final String basemsg = "Shall be recorded in the format shown in 'Layout of Type 2 partition map for sparable partition\n'.";
	
	
	// Partition Map Type は2
	if(partMapType.getIntValue() != 2){
	    
	    el.addError(new UDF_Error(category, UDF_Error.L_ERROR, "Partition Map Type",
				      basemsg, refer, String.valueOf(partMapType.getIntValue()), "2"));
	}
	
	// Partition Map Length は64
	if(partMapLen.getIntValue() != 64){
	    
	    el.addError(new UDF_Error(category, UDF_Error.L_ERROR, "Partition Map Length",
				      basemsg, refer, String.valueOf(partMapLen.getIntValue()), "64"));
	}
	
	// reserved は0
	if(!UDF_Util.isAllZero(reserved.getData())){
	    
	    el.addError(new UDF_Error(category, UDF_Error.L_ERROR, "Reserved", basemsg + "(#00 bytes)", refer));
	}
	
	// Partition Type Identifier の検証
	final String ptid = "Partition Type Identifier";
	el.addError(partTypeId.verify(ptid));
	ret = partTypeId.verifyFlags(0);
	if(ret.isError()){
	    
	    ret.setCategory(category);
	    ret.setMessage(basemsg);
	    ret.setRefer(refer);
	    ret.setRName(ptid);
	    el.addError(ret);
	}
	
	ret = partTypeId.verifyId("*UDF Sparable Partition");
	if(ret.isError()){
	    
	    ret.setCategory(category);
	    ret.setMessage(basemsg);
	    ret.setRefer(refer);
	    ret.setRName(ptid);
	    el.addError(ret);
	}
	
	// N_ST は１〜４でなければならない
	int n_st = getNumberOfSparingTables().getIntValue();
	if(n_st < 1 || 4 < n_st){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Number of Sparing Tables", basemsg +
			 "the number of redundant tables recorded. This shall be a value in the range of 1 to 4.",
			 "2.2.9", String.valueOf(n_st), ""));
	}
	
	// reserved2 も0
	if(!UDF_Util.isAllZero(reserved2.getData())){
	    
	    el.addError(new UDF_Error(category, UDF_Error.L_ERROR, "Reserved(2nd)", basemsg + "(#00 bytes)", refer));
	}
	
	// Pad も0
	if(!UDF_Util.isAllZero(pad.getData())){
	    
	    el.addError(new UDF_Error(category, UDF_Error.L_ERROR, "Pad", basemsg + "(#00 bytes)", refer));
	}
	
	return el;
    }
    /*
       パーティションの開始位置と長さを指定する。

       @param	loc	開始位置(LBS単位)
       @param	len	サイズ

       このメソッドは type1 partition map, Sparable Partition Map
       で有効である。

       それ以外では何もしない。
    public void setLocAndLen(int loc, long len) throws UDF_PartMapException{
	env.getPartMapExtent(getPartPartno(), 0).addExtent(loc, -1, len);
	//env.part_e[getPartPartno()].addExtent(loc, -1, len);
	//env.part_ra[getPartPartno()] = env.part_e[getPartPartno()].genRandomAccessExtent();
    }
     */

    public void recalcPM(int partrefno) throws UDF_PartMapException,UDF_InternalException,IOException {
	super.recalcPM(partrefno);

	UDF_SparablePartMap sp = this;
	UDF_ElementBase[] eb = sp.getLocsOfSparingTables().getChildren();
	for(int j=0 ; j<eb.length ; ++j){
	    UDF_uint32 loc = (UDF_uint32)eb[j];

	    String label = UDF_Util.car(loc.getAttribute("ref"), '.');

	    UDF_SparingTable st = null;
	    if(label != null){
		UDF_Element x = findById(label);
		if(x != null)
		    st = (UDF_SparingTable) x.getFirstChild();
	    }
	    if(st == null){
		UDF_Element x = findExtentByLoc(loc.getIntValue(), -1, 0);//findById(label);
		if(x != null)
		    st = (UDF_SparingTable) x.getFirstChild();
	    }
	    if(st == null)
		throw new UDF_InternalException(this, "no sparing table");

	    UDF_Extent main_ext = env.getPartMapExtent(partrefno, 0);

	    if(j == 0){//最初の1つにのみ対処
		UDF_Element[] ee = st.getMapEntry().getChildren();
		boolean has_spare = false;
		for(int k=0 ; k<ee.length ; k += 2){
		    UDF_uint32 src = (UDF_uint32)ee[k];
		    UDF_uint32 dst = (UDF_uint32)ee[k + 1];
		    if(src.getLongValue() != 0xffffffffL){//spareがあった
			//もしspareできたなら part_raも作りなおす。
			if(main_ext.spareExtent(src.getIntValue(), -1, dst.getIntValue(), -1, (long)sp.getPacketLen().getIntValue() * env.LBS, getPartNumber().getIntValue())){
			    has_spare = true;

			}
		    }
		}
		//env.part_e[partrefno].debug(0);
		if(has_spare){
		    //env.part_e[partrefno].blessExtent();
		    //env.part_ra[partrefno] = env.part_e[partrefno].genRandomAccessExtent();
		    main_ext.blessExtent();
		}
		env.setPartMapExtent(partrefno, 0, main_ext);
		env.setPartMapRandomAccess(partrefno, 0, main_ext.genRandomAccessExtent());
	    }
	}
    }

//end:
};
