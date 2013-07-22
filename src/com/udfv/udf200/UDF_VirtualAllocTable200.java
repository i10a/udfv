/*
*/
package com.udfv.udf200;

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
no documents.(AUTOMATICALY GENERATED)

<dl>

<dt>フィールド一覧</dt>
<dd>
<table border="1">
<tr><td>メンバ</td><td>型</td><td>サイズ</td></tr>
<tr><td><b>LenOfHeader</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td></tr>
<tr><td><b>LenOfImplUse</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td></tr>
<tr><td><b>LogicalVolId</b></td><td><b>UDF_dstring</b></td><td><i>128</i></td></tr>
<tr><td><b>PreviousVATICBLocation</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>NumberOfFiles</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>NumberOfDirectories</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>MinUDFReadVersion</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td></tr>
<tr><td><b>MinUDFWriteVersion</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td></tr>
<tr><td><b>MaxUDFWriteVersion</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td></tr>
<tr><td><b>Reserved</b></td><td><b>UDF_bytes</b></td><td><i>2</i></td></tr>
<tr><td><b>ImplUse</b></td><td><b>UDF_bytes</b></td><td><i>getLenOfImplUse().getIntValue()</i></td></tr>
<tr><td><b>VATEntry</b></td><td><b>UDF_bytes</b></td><td><i>getHintSize() - 152</i></td></tr>
</table>
</dd>
</dl>
*/
public class UDF_VirtualAllocTable200 extends UDF_Element implements com.udfv.udf150.UDF_VirtualAllocTable
{

    /**
	このオブジェクトのパッケージ名を除いたクラス名を取得する。

	@return クラス名
    */
    public static String getUDFClassName( ) {
        return "UDF_VirtualAllocTable200";
    }

    /**
	コンストラクタ。

	@param elem   親エレメント。
	@param prefix ネームスペース。
	@param name   エレメント名。
    */
    public UDF_VirtualAllocTable200(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }
    /**
	このUDFエレメントのサイズを返す。

	@return UDFエレメントのサイズ。
    */
    public int getSize(){
	return 0+2+2+logicalVolId.getSize()+4+4+4+2+2+2+reserved.getSize()+implUse.getSize()+vATEntry.getSize();
    }

    /**
	このUDFエレメントのサイズをlong 型で返す。

	@return UDFエレメントのサイズ。
    */
    public long getLongSize(){
	return (long)0+2+2+logicalVolId.getSize()+4+4+4+2+2+2+reserved.getSize()+implUse.getSize()+vATEntry.getSize();
    }
    private UDF_uint16 lenOfHeader;
    private UDF_uint16 lenOfImplUse;
    private UDF_dstring logicalVolId;
    private UDF_uint32 previousVATICBLocation;
    private UDF_uint32 numberOfFiles;
    private UDF_uint32 numberOfDirectories;
    private UDF_uint16 minUDFReadVersion;
    private UDF_uint16 minUDFWriteVersion;
    private UDF_uint16 maxUDFWriteVersion;
    private UDF_bytes reserved;
    private UDF_bytes implUse;
    private UDF_bytes vATEntry;

    /**
	lenOfHeaderを取得する。

	@return 取得したlenOfHeader を返す。
    */
    public UDF_uint16 getLenOfHeader(){return lenOfHeader;}
    /**
	lenOfImplUseを取得する。

	@return 取得したlenOfImplUse を返す。
    */
    public UDF_uint16 getLenOfImplUse(){return lenOfImplUse;}
    /**
	logicalVolIdを取得する。

	@return 取得したlogicalVolId を返す。
    */
    public UDF_dstring getLogicalVolId(){return logicalVolId;}
    /**
	previousVATICBLocationを取得する。

	@return 取得したpreviousVATICBLocation を返す。
    */
    public UDF_uint32 getPreviousVATICBLocation(){return previousVATICBLocation;}
    /**
	numberOfFilesを取得する。

	@return 取得したnumberOfFiles を返す。
    */
    public UDF_uint32 getNumberOfFiles(){return numberOfFiles;}
    /**
	numberOfDirectoriesを取得する。

	@return 取得したnumberOfDirectories を返す。
    */
    public UDF_uint32 getNumberOfDirectories(){return numberOfDirectories;}
    /**
	minUDFReadVersionを取得する。

	@return 取得したminUDFReadVersion を返す。
    */
    public UDF_uint16 getMinUDFReadVersion(){return minUDFReadVersion;}
    /**
	minUDFWriteVersionを取得する。

	@return 取得したminUDFWriteVersion を返す。
    */
    public UDF_uint16 getMinUDFWriteVersion(){return minUDFWriteVersion;}
    /**
	maxUDFWriteVersionを取得する。

	@return 取得したmaxUDFWriteVersion を返す。
    */
    public UDF_uint16 getMaxUDFWriteVersion(){return maxUDFWriteVersion;}
    /**
	reservedを取得する。

	@return 取得したreserved を返す。
    */
    public UDF_bytes getReserved(){return reserved;}
    /**
	implUseを取得する。

	@return 取得したimplUse を返す。
    */
    public UDF_bytes getImplUse(){return implUse;}
    /**
	vATEntryを取得する。

	@return 取得したvATEntry を返す。
    */
    public UDF_bytes getVATEntry(){return vATEntry;}

    /**
	lenOfHeaderを設定する。

	@param	v 設定する値。
    */
    public void setLenOfHeader(UDF_uint16 v){replaceChild(v, lenOfHeader); lenOfHeader = v;}
    /**
	lenOfImplUseを設定する。

	@param	v 設定する値。
    */
    public void setLenOfImplUse(UDF_uint16 v){replaceChild(v, lenOfImplUse); lenOfImplUse = v;}
    /**
	logicalVolIdを設定する。

	@param	v 設定する値。
    */
    public void setLogicalVolId(UDF_dstring v){replaceChild(v, logicalVolId); logicalVolId = v;}
    /**
	previousVATICBLocationを設定する。

	@param	v 設定する値。
    */
    public void setPreviousVATICBLocation(UDF_uint32 v){replaceChild(v, previousVATICBLocation); previousVATICBLocation = v;}
    /**
	numberOfFilesを設定する。

	@param	v 設定する値。
    */
    public void setNumberOfFiles(UDF_uint32 v){replaceChild(v, numberOfFiles); numberOfFiles = v;}
    /**
	numberOfDirectoriesを設定する。

	@param	v 設定する値。
    */
    public void setNumberOfDirectories(UDF_uint32 v){replaceChild(v, numberOfDirectories); numberOfDirectories = v;}
    /**
	minUDFReadVersionを設定する。

	@param	v 設定する値。
    */
    public void setMinUDFReadVersion(UDF_uint16 v){replaceChild(v, minUDFReadVersion); minUDFReadVersion = v;}
    /**
	minUDFWriteVersionを設定する。

	@param	v 設定する値。
    */
    public void setMinUDFWriteVersion(UDF_uint16 v){replaceChild(v, minUDFWriteVersion); minUDFWriteVersion = v;}
    /**
	maxUDFWriteVersionを設定する。

	@param	v 設定する値。
    */
    public void setMaxUDFWriteVersion(UDF_uint16 v){replaceChild(v, maxUDFWriteVersion); maxUDFWriteVersion = v;}
    /**
	reservedを設定する。

	@param	v 設定する値。
    */
    public void setReserved(UDF_bytes v){replaceChild(v, reserved); reserved = v;}
    /**
	implUseを設定する。

	@param	v 設定する値。
    */
    public void setImplUse(UDF_bytes v){replaceChild(v, implUse); implUse = v;}
    /**
	vATEntryを設定する。

	@param	v 設定する値。
    */
    public void setVATEntry(UDF_bytes v){replaceChild(v, vATEntry); vATEntry = v;}

    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long rsize = 0;
	preReadHook(f);
	lenOfHeader = (UDF_uint16)createElement("UDF_uint16", "", "len-of-header");
	rsize += lenOfHeader.readFrom(f);
	lenOfImplUse = (UDF_uint16)createElement("UDF_uint16", "", "len-of-impl-use");
	rsize += lenOfImplUse.readFrom(f);
	logicalVolId = (UDF_dstring)createElement("UDF_dstring", "", "logical-vol-id");
	logicalVolId.setSize(128);
	rsize += logicalVolId.readFrom(f);
	previousVATICBLocation = (UDF_uint32)createElement("UDF_uint32", "", "previous-vat-icb-location");
	rsize += previousVATICBLocation.readFrom(f);
	numberOfFiles = (UDF_uint32)createElement("UDF_uint32", "", "number-of-files");
	rsize += numberOfFiles.readFrom(f);
	numberOfDirectories = (UDF_uint32)createElement("UDF_uint32", "", "number-of-directories");
	rsize += numberOfDirectories.readFrom(f);
	minUDFReadVersion = (UDF_uint16)createElement("UDF_uint16", "", "min-udf-read-version");
	rsize += minUDFReadVersion.readFrom(f);
	minUDFWriteVersion = (UDF_uint16)createElement("UDF_uint16", "", "min-udf-write-version");
	rsize += minUDFWriteVersion.readFrom(f);
	maxUDFWriteVersion = (UDF_uint16)createElement("UDF_uint16", "", "max-udf-write-version");
	rsize += maxUDFWriteVersion.readFrom(f);
	reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
	reserved.setSize(2);
	rsize += reserved.readFrom(f);
	implUse = (UDF_bytes)createElement("UDF_bytes", "", "impl-use");
	implUse.setSize(getLenOfImplUse().getIntValue());
	rsize += implUse.readFrom(f);
	vATEntry = (UDF_bytes)createElement("UDF_bytes", "", "vat-entry");
	vATEntry.setSize(getHintSize() - 152);
	rsize += vATEntry.readFrom(f);
	apply( );
	postReadHook(f);
	return rsize;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long wsize = 0;
	wsize += lenOfHeader.writeTo(f);
	wsize += lenOfImplUse.writeTo(f);
	wsize += logicalVolId.writeTo(f);
	wsize += previousVATICBLocation.writeTo(f);
	wsize += numberOfFiles.writeTo(f);
	wsize += numberOfDirectories.writeTo(f);
	wsize += minUDFReadVersion.writeTo(f);
	wsize += minUDFWriteVersion.writeTo(f);
	wsize += maxUDFWriteVersion.writeTo(f);
	wsize += reserved.writeTo(f);
	wsize += implUse.writeTo(f);
	wsize += vATEntry.writeTo(f);
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
	    else if(name.equals("len-of-header")){
		lenOfHeader = (UDF_uint16)createElement("UDF_uint16", "", "len-of-header");
		lenOfHeader.readFromXML(child);
	    }
	    else if(name.equals("len-of-impl-use")){
		lenOfImplUse = (UDF_uint16)createElement("UDF_uint16", "", "len-of-impl-use");
		lenOfImplUse.readFromXML(child);
	    }
	    else if(name.equals("logical-vol-id")){
		logicalVolId = (UDF_dstring)createElement("UDF_dstring", "", "logical-vol-id");
		logicalVolId.setSize(128);
		logicalVolId.readFromXML(child);
	    }
	    else if(name.equals("previous-vat-icb-location")){
		previousVATICBLocation = (UDF_uint32)createElement("UDF_uint32", "", "previous-vat-icb-location");
		previousVATICBLocation.readFromXML(child);
	    }
	    else if(name.equals("number-of-files")){
		numberOfFiles = (UDF_uint32)createElement("UDF_uint32", "", "number-of-files");
		numberOfFiles.readFromXML(child);
	    }
	    else if(name.equals("number-of-directories")){
		numberOfDirectories = (UDF_uint32)createElement("UDF_uint32", "", "number-of-directories");
		numberOfDirectories.readFromXML(child);
	    }
	    else if(name.equals("min-udf-read-version")){
		minUDFReadVersion = (UDF_uint16)createElement("UDF_uint16", "", "min-udf-read-version");
		minUDFReadVersion.readFromXML(child);
	    }
	    else if(name.equals("min-udf-write-version")){
		minUDFWriteVersion = (UDF_uint16)createElement("UDF_uint16", "", "min-udf-write-version");
		minUDFWriteVersion.readFromXML(child);
	    }
	    else if(name.equals("max-udf-write-version")){
		maxUDFWriteVersion = (UDF_uint16)createElement("UDF_uint16", "", "max-udf-write-version");
		maxUDFWriteVersion.readFromXML(child);
	    }
	    else if(name.equals("reserved")){
		reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
		reserved.setSize(2);
		reserved.readFromXML(child);
	    }
	    else if(name.equals("impl-use")){
		implUse = (UDF_bytes)createElement("UDF_bytes", "", "impl-use");
		implUse.setSize(getLenOfImplUse().getIntValue());
		implUse.readFromXML(child);
	    }
	    else if(name.equals("vat-entry")){
		vATEntry = (UDF_bytes)createElement("UDF_bytes", "", "vat-entry");
		vATEntry.setSize(getHintSize() - 152);
		vATEntry.readFromXML(child);
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
	lenOfHeader = (UDF_uint16)createElement("UDF_uint16", "", "len-of-header");
	lenOfHeader.setDefaultValue();
	lenOfHeader.setValue(152);
	lenOfImplUse = (UDF_uint16)createElement("UDF_uint16", "", "len-of-impl-use");
	lenOfImplUse.setDefaultValue();
	logicalVolId = (UDF_dstring)createElement("UDF_dstring", "", "logical-vol-id");
	logicalVolId.setSize(128);
	logicalVolId.setDefaultValue();
	previousVATICBLocation = (UDF_uint32)createElement("UDF_uint32", "", "previous-vat-icb-location");
	previousVATICBLocation.setDefaultValue();
	previousVATICBLocation.setValue(4294967295L);
	numberOfFiles = (UDF_uint32)createElement("UDF_uint32", "", "number-of-files");
	numberOfFiles.setDefaultValue();
	numberOfDirectories = (UDF_uint32)createElement("UDF_uint32", "", "number-of-directories");
	numberOfDirectories.setDefaultValue();
	minUDFReadVersion = (UDF_uint16)createElement("UDF_uint16", "", "min-udf-read-version");
	minUDFReadVersion.setDefaultValue();
	minUDFWriteVersion = (UDF_uint16)createElement("UDF_uint16", "", "min-udf-write-version");
	minUDFWriteVersion.setDefaultValue();
	maxUDFWriteVersion = (UDF_uint16)createElement("UDF_uint16", "", "max-udf-write-version");
	maxUDFWriteVersion.setDefaultValue();
	reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
	reserved.setSize(2);
	reserved.setDefaultValue();
	implUse = (UDF_bytes)createElement("UDF_bytes", "", "impl-use");
	implUse.setSize(getLenOfImplUse().getIntValue());
	implUse.setDefaultValue();
	vATEntry = (UDF_bytes)createElement("UDF_bytes", "", "vat-entry");
	vATEntry.setSize(getHintSize() - 152);
	vATEntry.setDefaultValue();

	apply( );
	postSetDefaultValueHook();
    }


    /**
	複製する
    */
    public UDF_Element duplicateElement(){
	UDF_VirtualAllocTable200 dup = new UDF_VirtualAllocTable200(this, null, getName());
	dup.setDefaultValue();
	dup.duplicateHook(this);
	dup.setLenOfHeader((UDF_uint16)lenOfHeader.duplicateElement());
	dup.setLenOfImplUse((UDF_uint16)lenOfImplUse.duplicateElement());
	dup.setLogicalVolId((UDF_dstring)logicalVolId.duplicateElement());
	dup.setPreviousVATICBLocation((UDF_uint32)previousVATICBLocation.duplicateElement());
	dup.setNumberOfFiles((UDF_uint32)numberOfFiles.duplicateElement());
	dup.setNumberOfDirectories((UDF_uint32)numberOfDirectories.duplicateElement());
	dup.setMinUDFReadVersion((UDF_uint16)minUDFReadVersion.duplicateElement());
	dup.setMinUDFWriteVersion((UDF_uint16)minUDFWriteVersion.duplicateElement());
	dup.setMaxUDFWriteVersion((UDF_uint16)maxUDFWriteVersion.duplicateElement());
	dup.setReserved((UDF_bytes)reserved.duplicateElement());
	dup.setImplUse((UDF_bytes)implUse.duplicateElement());
	dup.setVATEntry((UDF_bytes)vATEntry.duplicateElement());

	apply();

	return dup;
    }

    /**
        子の UDF Element の Node を append しなおす。

    */
    private void apply( ) {

	removeAllChildren();
	appendChild(lenOfHeader);
	appendChild(lenOfImplUse);
	appendChild(logicalVolId);
	appendChild(previousVATICBLocation);
	appendChild(numberOfFiles);
	appendChild(numberOfDirectories);
	appendChild(minUDFReadVersion);
	appendChild(minUDFWriteVersion);
	appendChild(maxUDFWriteVersion);
	appendChild(reserved);
	appendChild(implUse);
	appendChild(vATEntry);
    }

    public String getInfo(int indent){
	String a = "";
	a += UDF_Util.repeat(' ', indent);
	a += lenOfHeader.getInfo(indent + 1);
	a += lenOfImplUse.getInfo(indent + 1);
	a += logicalVolId.getInfo(indent + 1);
	a += previousVATICBLocation.getInfo(indent + 1);
	a += numberOfFiles.getInfo(indent + 1);
	a += numberOfDirectories.getInfo(indent + 1);
	a += minUDFReadVersion.getInfo(indent + 1);
	a += minUDFWriteVersion.getInfo(indent + 1);
	a += maxUDFWriteVersion.getInfo(indent + 1);
	a += reserved.getInfo(indent + 1);
	a += implUse.getInfo(indent + 1);
	a += vATEntry.getInfo(indent + 1);
      return a;
    }
    public void debug(int indent){
	System.err.print(UDF_Util.repeat(' ', indent));

	String info = " gp=" + getGlobalPoint();
	info += " pn=" + getElemPartRefNo();
	info += " sn=" + getPartSubno();
	System.err.println(getName() + ": (" + hashCode() + ")" + info);	lenOfHeader.debug(indent + 1);
	lenOfImplUse.debug(indent + 1);
	logicalVolId.debug(indent + 1);
	previousVATICBLocation.debug(indent + 1);
	numberOfFiles.debug(indent + 1);
	numberOfDirectories.debug(indent + 1);
	minUDFReadVersion.debug(indent + 1);
	minUDFWriteVersion.debug(indent + 1);
	maxUDFWriteVersion.debug(indent + 1);
	reserved.debug(indent + 1);
	implUse.debug(indent + 1);
	vATEntry.debug(indent + 1);
    }
//begin:add your code here

    public void postReadHook(UDF_RandomAccess f) throws IOException, UDF_Exception{
	UDF_Encoding enc = UDF_Encoding.genEncoding("OSTA Compressed Unicode");
	getLogicalVolId().setEncoding(enc);
	
	UDF_ElementList v = new UDF_ElementList();
	UDF_RandomAccessBuffer rab = getVATEntry().genRandomAccessBytes();
	
	while(!rab.eof()){
	    UDF_uint32 u = new UDF_uint32(this, null, null);
	    u.readFrom(rab);
	    v.add(u);
	}
	getVATEntry().replaceChildren(v);
    }

    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	// Logical Volume Identifier
	el.addError(logicalVolId.verify("Logical Volume Identifier"));
	
	if(env.udf_revision != 0x200)
	    return el;
	
	el.addError(verifyBase(UDF_Error.C_UDF200, "2.2.10"));
	
	el.setRName("Virtual Allocation Table");
	el.setGlobalPoint(getGlobalPoint());
	
	return el;
    }
    
    /**
       @param category  エラーカテゴリ。
       @param refer     ドキュメントの参照番号。
       
       @return エラーリストインスタンス。
    */
    protected UDF_ErrorList verifyBase(short category, String refer) throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	

	// ユーザによって変更されない限り、Logical Vol Id は
	// LVD のLogical Vol Id と等しくなければならない
//	byte[] desc6logicalvolid = env.logicalVolDesc.getLogicalVolId().getData();
	byte[] desc6logicalvolid = env.getLogicalVolDesc(UDF_Env.VDS_AUTO).getLogicalVolId().getData();
	byte[] logicalvolid = getLogicalVolId().getData();
	
	if(!UDF_Util.cmpBytesBytes(desc6logicalvolid, logicalvolid)){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_WARNING, "Logical Volume Identifier",
			 "The value of this field should be the same as the field in the LVD until changed by the user.",
			 refer));
	}
	
	// L_HD の長さが152 + L_IUと一致しない
	int loh = getLenOfHeader().getIntValue();
	if(loh != 152 + getLenOfImplUse().getIntValue()){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Length of Header",
			 "Indicates the amount of data preceding the VAT entries.", refer,
			 loh, (152 + getLenOfImplUse().getIntValue())));
	}
	
	// この構造の位置より必ず前に存在する
	long prevloc = previousVATICBLocation.getLongValue();
	if(prevloc != 0xffffffffL && getGlobalPoint() <= previousVATICBLocation.getLongValue()){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Previous VAT ICB Location",
			 "Previous VAT ICB Location - Shalll specify the logical block number of an earlier VAT ICB " +
			 "in the partition identified by the partition map entry.",
			 refer, String.valueOf(previousVATICBLocation.getLongValue()), ""));
	}
	
	// L_IU が0でないとき、その値は少なくとも32以上、且つ4の倍数でなければならない
	int loiu = getLenOfImplUse().getIntValue();
	if(loiu != 0){
	    
	    if(loiu < 32 || (loiu % 4) != 0){
		
		el.addError(new UDF_Error
			    (category, UDF_Error.L_ERROR, "Length of Implementation Use",
			     "If this field is non-zero, the value shall be at least 32 and be an integral multiple of 4.",
			     refer,  String.valueOf(loiu), ""));
	    }
	}
	
	// ファイル数が一致しない
	if(numberOfFiles.getLongValue() != env.getNumberOfFiles()){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Number of Files",
			 "The current number of files in the associated Logical Volume. This information is needed by the Macintosh OS. " +
			 "All implementation shall maintain this information.", refer,
			 numberOfFiles.getLongValue(), env.getNumberOfFiles()));
	}
	
	// ディレクトリ数が一致しない
	if(numberOfDirectories.getLongValue() != env.getNumberOfDirectories()){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Number of Directories",
			 "The current number of directories in the associated Logical Volume. This information is needed by the Macintosh OS. " +
			 "All implementation shall maintain this information.", refer,
			 numberOfDirectories.getLongValue(), env.getNumberOfDirectories()));
	}
	
	int minudfreadver = getMinUDFReadVersion().getIntValue();
	int minudfwritever = getMinUDFWriteVersion().getIntValue();
	int maxudfwritever = getMaxUDFWriteVersion().getIntValue();
	
	// 多分102 よりは大きい
	if(minudfreadver < 0x102){
        
	    el.addError(new UDF_Error
			(category, UDF_Error.L_WARNING, "Minimum UDF Read Version",
			 "Shall indicate the minimum recommended verision of the UDF specification that an implementation is " +
			 "required to support to successfully be able to read all potential structures on the media.",
			 refer, String.valueOf(minudfreadver), ""));
	}

	if(minudfwritever < 0x102){
        
	    el.addError(new UDF_Error
			(category, UDF_Error.L_WARNING, "Minimum UDF Write Version",
			 "Shall indicate the minimum verision of the UDF specification that an implementation is " +
			 "required to support to successfully be able to modify all potential structures on the media.",
			 refer, String.valueOf(minudfwritever), ""));
	}

	if(minudfreadver < 0x102){
        
	        el.addError(new UDF_Error
			(category, UDF_Error.L_WARNING, "Maximum UDF Write Version",
			 "Shall indicate the maximum verision of the UDF specification that an implementation that " +
			 "has modified the media has supported.",
			 refer, String.valueOf(maxudfwritever), ""));
	}
	
	return el;
    }
    
//end:
};
