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
ICB&nbsp;Tag&nbsp;を表現するクラス。<br/>


<dl>

<dt>フィールド一覧</dt>
<dd>
<table border="1">
<tr><td>メンバ</td><td>型</td><td>サイズ</td></tr>
<tr><td><b>PriorRecordedNumberOfDirectEntries</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>StrategyType</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td></tr>
<tr><td><b>StrategyParameter</b></td><td><b>UDF_bytes</b></td><td><i>2</i></td></tr>
<tr><td><b>MaxNumberOfEntries</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td></tr>
<tr><td><b>Reserved</b></td><td><b>UDF_bytes</b></td><td><i>1</i></td></tr>
<tr><td><b>FileType</b></td><td><b>UDF_uint8</b></td><td><i>1</i></td></tr>
<tr><td><b>ParentICBLoc</b></td><td><b>UDF_lb_addr</b></td><td><i>parentICBLoc.getSize()</i></td></tr>
<tr><td><b>Flags</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td></tr>
</table>
</dd>
</dl>
*/
public class UDF_icbtag extends UDF_Element 
{

    /**
	このオブジェクトのパッケージ名を除いたクラス名を取得する。

	@return クラス名
    */
    public static String getUDFClassName( ) {
        return "UDF_icbtag";
    }

    /**
	コンストラクタ。

	@param elem   親エレメント。
	@param prefix ネームスペース。
	@param name   エレメント名。
    */
    public UDF_icbtag(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }
    /**
	このUDFエレメントのサイズを返す。

	@return UDFエレメントのサイズ。
    */
    public int getSize(){
	return 0+4+2+strategyParameter.getSize()+2+reserved.getSize()+1+parentICBLoc.getSize()+2;
    }

    /**
	このUDFエレメントのサイズをlong 型で返す。

	@return UDFエレメントのサイズ。
    */
    public long getLongSize(){
	return (long)0+4+2+strategyParameter.getSize()+2+reserved.getSize()+1+parentICBLoc.getSize()+2;
    }
    private UDF_uint32 priorRecordedNumberOfDirectEntries;
    private UDF_uint16 strategyType;
    private UDF_bytes strategyParameter;
    private UDF_uint16 maxNumberOfEntries;
    private UDF_bytes reserved;
    private UDF_uint8 fileType;
    private UDF_lb_addr parentICBLoc;
    private UDF_uint16 flags;

    /**
	priorRecordedNumberOfDirectEntriesを取得する。

	@return 取得したpriorRecordedNumberOfDirectEntries を返す。
    */
    public UDF_uint32 getPriorRecordedNumberOfDirectEntries(){return priorRecordedNumberOfDirectEntries;}
    /**
	strategyTypeを取得する。

	@return 取得したstrategyType を返す。
    */
    public UDF_uint16 getStrategyType(){return strategyType;}
    /**
	strategyParameterを取得する。

	@return 取得したstrategyParameter を返す。
    */
    public UDF_bytes getStrategyParameter(){return strategyParameter;}
    /**
	maxNumberOfEntriesを取得する。

	@return 取得したmaxNumberOfEntries を返す。
    */
    public UDF_uint16 getMaxNumberOfEntries(){return maxNumberOfEntries;}
    /**
	reservedを取得する。

	@return 取得したreserved を返す。
    */
    public UDF_bytes getReserved(){return reserved;}
    /**
	fileTypeを取得する。

	@return 取得したfileType を返す。
    */
    public UDF_uint8 getFileType(){return fileType;}
    /**
	parentICBLocを取得する。

	@return 取得したparentICBLoc を返す。
    */
    public UDF_lb_addr getParentICBLoc(){return parentICBLoc;}
    /**
	flagsを取得する。

	@return 取得したflags を返す。
    */
    public UDF_uint16 getFlags(){return flags;}

    /**
	priorRecordedNumberOfDirectEntriesを設定する。

	@param	v 設定する値。
    */
    public void setPriorRecordedNumberOfDirectEntries(UDF_uint32 v){replaceChild(v, priorRecordedNumberOfDirectEntries); priorRecordedNumberOfDirectEntries = v;}
    /**
	strategyTypeを設定する。

	@param	v 設定する値。
    */
    public void setStrategyType(UDF_uint16 v){replaceChild(v, strategyType); strategyType = v;}
    /**
	strategyParameterを設定する。

	@param	v 設定する値。
    */
    public void setStrategyParameter(UDF_bytes v){replaceChild(v, strategyParameter); strategyParameter = v;}
    /**
	maxNumberOfEntriesを設定する。

	@param	v 設定する値。
    */
    public void setMaxNumberOfEntries(UDF_uint16 v){replaceChild(v, maxNumberOfEntries); maxNumberOfEntries = v;}
    /**
	reservedを設定する。

	@param	v 設定する値。
    */
    public void setReserved(UDF_bytes v){replaceChild(v, reserved); reserved = v;}
    /**
	fileTypeを設定する。

	@param	v 設定する値。
    */
    public void setFileType(UDF_uint8 v){replaceChild(v, fileType); fileType = v;}
    /**
	parentICBLocを設定する。

	@param	v 設定する値。
    */
    public void setParentICBLoc(UDF_lb_addr v){replaceChild(v, parentICBLoc); parentICBLoc = v;}
    /**
	flagsを設定する。

	@param	v 設定する値。
    */
    public void setFlags(UDF_uint16 v){replaceChild(v, flags); flags = v;}

    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long rsize = 0;
	preReadHook(f);
	priorRecordedNumberOfDirectEntries = (UDF_uint32)createElement("UDF_uint32", "", "prior-recorded-number-of-direct-entries");
	rsize += priorRecordedNumberOfDirectEntries.readFrom(f);
	strategyType = (UDF_uint16)createElement("UDF_uint16", "", "strategy-type");
	rsize += strategyType.readFrom(f);
	strategyParameter = (UDF_bytes)createElement("UDF_bytes", "", "strategy-parameter");
	strategyParameter.setSize(2);
	rsize += strategyParameter.readFrom(f);
	maxNumberOfEntries = (UDF_uint16)createElement("UDF_uint16", "", "max-number-of-entries");
	rsize += maxNumberOfEntries.readFrom(f);
	reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
	reserved.setSize(1);
	rsize += reserved.readFrom(f);
	fileType = (UDF_uint8)createElement("UDF_uint8", "", "file-type");
	rsize += fileType.readFrom(f);
	parentICBLoc = (UDF_lb_addr)createElement("UDF_lb_addr", "", "parent-icb-loc");
	rsize += parentICBLoc.readFrom(f);
	flags = (UDF_uint16)createElement("UDF_uint16", "", "flags");
	rsize += flags.readFrom(f);
	apply( );
	postReadHook(f);
	return rsize;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long wsize = 0;
	wsize += priorRecordedNumberOfDirectEntries.writeTo(f);
	wsize += strategyType.writeTo(f);
	wsize += strategyParameter.writeTo(f);
	wsize += maxNumberOfEntries.writeTo(f);
	wsize += reserved.writeTo(f);
	wsize += fileType.writeTo(f);
	wsize += parentICBLoc.writeTo(f);
	wsize += flags.writeTo(f);
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
	    else if(name.equals("prior-recorded-number-of-direct-entries")){
		priorRecordedNumberOfDirectEntries = (UDF_uint32)createElement("UDF_uint32", "", "prior-recorded-number-of-direct-entries");
		priorRecordedNumberOfDirectEntries.readFromXML(child);
	    }
	    else if(name.equals("strategy-type")){
		strategyType = (UDF_uint16)createElement("UDF_uint16", "", "strategy-type");
		strategyType.readFromXML(child);
	    }
	    else if(name.equals("strategy-parameter")){
		strategyParameter = (UDF_bytes)createElement("UDF_bytes", "", "strategy-parameter");
		strategyParameter.setSize(2);
		strategyParameter.readFromXML(child);
	    }
	    else if(name.equals("max-number-of-entries")){
		maxNumberOfEntries = (UDF_uint16)createElement("UDF_uint16", "", "max-number-of-entries");
		maxNumberOfEntries.readFromXML(child);
	    }
	    else if(name.equals("reserved")){
		reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
		reserved.setSize(1);
		reserved.readFromXML(child);
	    }
	    else if(name.equals("file-type")){
		fileType = (UDF_uint8)createElement("UDF_uint8", "", "file-type");
		fileType.readFromXML(child);
	    }
	    else if(name.equals("parent-icb-loc")){
		parentICBLoc = (UDF_lb_addr)createElement("UDF_lb_addr", "", "parent-icb-loc");
		parentICBLoc.readFromXML(child);
	    }
	    else if(name.equals("flags")){
		flags = (UDF_uint16)createElement("UDF_uint16", "", "flags");
		flags.readFromXML(child);
	    }
	    else{
		System.err.println("Unknown XML tag:" + name);
	    }
	}
	apply( );
	postReadFromXMLHook(n);
    }

    /**
	初期値を設定する
    */
    public void setDefaultValue(){
	super.setDefaultValue();
	priorRecordedNumberOfDirectEntries = (UDF_uint32)createElement("UDF_uint32", "", "prior-recorded-number-of-direct-entries");
	priorRecordedNumberOfDirectEntries.setDefaultValue();
	strategyType = (UDF_uint16)createElement("UDF_uint16", "", "strategy-type");
	strategyType.setDefaultValue();
	strategyType.setValue(4);
	strategyParameter = (UDF_bytes)createElement("UDF_bytes", "", "strategy-parameter");
	strategyParameter.setSize(2);
	strategyParameter.setDefaultValue();
	maxNumberOfEntries = (UDF_uint16)createElement("UDF_uint16", "", "max-number-of-entries");
	maxNumberOfEntries.setDefaultValue();
	maxNumberOfEntries.setValue(1);
	reserved = (UDF_bytes)createElement("UDF_bytes", "", "reserved");
	reserved.setSize(1);
	reserved.setDefaultValue();
	fileType = (UDF_uint8)createElement("UDF_uint8", "", "file-type");
	fileType.setDefaultValue();
	parentICBLoc = (UDF_lb_addr)createElement("UDF_lb_addr", "", "parent-icb-loc");
	parentICBLoc.setDefaultValue();
	flags = (UDF_uint16)createElement("UDF_uint16", "", "flags");
	flags.setDefaultValue();

	apply( );
	postSetDefaultValueHook();
    }


    /**
	複製する
    */
    public UDF_Element duplicateElement(){
	UDF_icbtag dup = new UDF_icbtag(this, null, getName());
	dup.setDefaultValue();
	dup.duplicateHook(this);
	dup.setPriorRecordedNumberOfDirectEntries((UDF_uint32)priorRecordedNumberOfDirectEntries.duplicateElement());
	dup.setStrategyType((UDF_uint16)strategyType.duplicateElement());
	dup.setStrategyParameter((UDF_bytes)strategyParameter.duplicateElement());
	dup.setMaxNumberOfEntries((UDF_uint16)maxNumberOfEntries.duplicateElement());
	dup.setReserved((UDF_bytes)reserved.duplicateElement());
	dup.setFileType((UDF_uint8)fileType.duplicateElement());
	dup.setParentICBLoc((UDF_lb_addr)parentICBLoc.duplicateElement());
	dup.setFlags((UDF_uint16)flags.duplicateElement());

	apply();

	return dup;
    }

    /**
        子の UDF Element の Node を append しなおす。

    */
    private void apply( ) {

	removeAllChildren();
	appendChild(priorRecordedNumberOfDirectEntries);
	appendChild(strategyType);
	appendChild(strategyParameter);
	appendChild(maxNumberOfEntries);
	appendChild(reserved);
	appendChild(fileType);
	appendChild(parentICBLoc);
	appendChild(flags);
    }

    public void debug(int indent){
	System.err.print(UDF_Util.repeat(' ', indent));
	System.err.println(getName() + ": (" + hashCode() + ")");	priorRecordedNumberOfDirectEntries.debug(indent + 1);
	strategyType.debug(indent + 1);
	strategyParameter.debug(indent + 1);
	maxNumberOfEntries.debug(indent + 1);
	reserved.debug(indent + 1);
	fileType.debug(indent + 1);
	parentICBLoc.debug(indent + 1);
	flags.debug(indent + 1);
    }
//begin:add your code here
    public static final int SHORT_AD = 0;
    public static final int LONG_AD = 1;
    public static final int EXT_AD = 2;
    public static final int DIRECT = 3;
    public static final int DIRECTORY = (1<<3);
    public static final int NON_RELOCATABLE = (1<<4);
    public static final int ARCHIVE = (1<<5);
    public static final int SETUID = (1<<6);
    public static final int SETGID = (1<<7);
    public static final int STICKY = (1<<8);
    public static final int CONTIGUOUS = (1<<9);
    public static final int SYSTEM = (1<<10);
    public static final int TRANSFORMED = (1<<11);
    public static final int MULTI_VERSIONS = (1<<12);
    public static final int STREAM = (1<<13);

    public static final int T_UNALLOCATED_SPACE_ENTRY = 1;
    public static final int T_PARTITION_INTEGRITY_ENTRY = 2;
    public static final int T_INDIRECT_ENTRY = 3;
    public static final int T_DIRECTORY = 4;
    public static final int T_FILE = 5;
    public static final int T_BLOCK_DEVICE_FILE = 6;
    public static final int T_CHARACTER_DEVICE_FILE = 7;
    public static final int T_EXTENDED_ATTRIBUTE = 8;
    public static final int T_FIFO = 9;
    public static final int T_SOCKET = 11;
    public static final int T_SYMLINK = 12;
    public static final int T_SDIRECTORY = 13;
    public static final int T_VAT = 248;
    public static final int T_RT = 249;
    public static final int T_METADATA_FILE = 250;
    public static final int T_METADATA_MIRROR_FILE = 251;
    public static final int T_METADATA_BITMAP_FILE = 252;

    public static String charToString(int filechar){
	StringBuffer sb = new StringBuffer();
	int alloc_type = filechar & 0x7;

	if(alloc_type == SHORT_AD)
	    sb.append("short_ad");
	else if(alloc_type == LONG_AD)
	    sb.append("long_ad");
	else if(alloc_type == EXT_AD)
	    sb.append("ext_ad");
	else if(alloc_type == DIRECT)
	    sb.append("direct");
	else 
	    sb.append("unknwon");

	if((filechar & DIRECTORY) != 0)
	    sb.append("|directory");
	if((filechar & NON_RELOCATABLE) != 0)
	    sb.append("|non-relocatable");
	if((filechar & ARCHIVE) != 0)
	    sb.append("|archive");
	if((filechar & SETUID) != 0)
	    sb.append("|setuid");
	if((filechar & SETGID) != 0)
	    sb.append("|setgid");
	if((filechar & STICKY) != 0)
	    sb.append("|sticky");
	if((filechar & CONTIGUOUS) != 0)
	    sb.append("|contiguous");
	if((filechar & SYSTEM) != 0)
	    sb.append("|system");
	if((filechar & TRANSFORMED) != 0)
	    sb.append("|transformed");
	if((filechar & MULTI_VERSIONS) != 0)
	    sb.append("|transformed");
	if((filechar & STREAM) != 0)
	    sb.append("|stream");

	return sb.toString();
    }
    public static String typeToString(int typ){
	switch(typ){
	default:
	    return "Unknown";
	case T_UNALLOCATED_SPACE_ENTRY:
	    return "Unallocated Space Entry";
	case T_PARTITION_INTEGRITY_ENTRY:
	    return "Partition Integrity Entry";
	case T_INDIRECT_ENTRY:
	    return "Indirect Entry";
	case T_DIRECTORY:
	    return "Directory";
	case T_FILE:
	    return "File";
	case T_BLOCK_DEVICE_FILE:
	    return "Block Device File";
	case T_CHARACTER_DEVICE_FILE:
	    return "Character Device File";
	case T_EXTENDED_ATTRIBUTE:
	    return "Extended Attribute";
	case T_FIFO:
	    return "FIFO";
	case T_SOCKET:
	    return "Socket";
	case T_SYMLINK:
	    return "Symlink";
	case T_SDIRECTORY:
	    return "Stream Directory";
	case T_VAT:
	    return "Virtual Allocation Table";
	case T_RT:
	    return "Real Time File";
	case T_METADATA_FILE:
	    return "Metadata File";
	case T_METADATA_MIRROR_FILE:
	    return "Metadata Mirror File";
	case T_METADATA_BITMAP_FILE:
	    return "Metadata Bitmap File";
	}
    }

    /**
      ファイルタイプがディレクトリ／ストリームディレクトリかどうか判定する。

      @return ディレクトリかストリームディレクトリであったときに"真"を返す。
    */
    public boolean isTypeDirectory() {

        return isTypeDirectory(getFileType().getIntValue());
    }

    public static boolean isTypeDirectory(int type) {

        if (type == UDF_icbtag.T_DIRECTORY || type == UDF_icbtag.T_SDIRECTORY){
            return true;
        }
        return false;
    }
    
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	final short category = UDF_Error.C_ECMA167;
	
	
	// StrategyType: 5-4095 は予約、4096-65535 は要使用者間の取り決め
	int strategy = strategyType.getIntValue();
	if(4 < strategy && strategy < 4096){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_WARNING, "Strategy Type",
			 "The strategies are specified by a number as shown in figure 4/16.\n" +
			 "5-4095: Reserved for future standardisation.",
			 "4/14.6.2", String.valueOf(strategy), ""));
	}
	else if(4096 <= strategy){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_CAUTION, "Strategy Type",
			 "The strategies are specified by a number as shown in figure 4/16.\n" +
			 "4096-65535: The interpretation of the strategy shall be subject to agreement " +
			 "between the originator and recipient of the medium.",
			 "4/14.6.2", String.valueOf(strategy), ""));
	}
	
	// maxnumOfEntries は0より大きい
	if(maxNumberOfEntries.getIntValue() == 0){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Maximum Number of Entries",
			 "This field specifies the maximum number of entries, including both direct and indirect, " +
			 "that may recorded in this ICB. This field shall be grater than 0.",
			 "4/14.6.4", String.valueOf(maxNumberOfEntries.getIntValue()), ""));
	}
	
	if(!UDF_Util.isAllZero(reserved.getData())){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Reserved",
			 "This field shall be reserved for future standardisation and shall be set to 0.",
			 "4/14.6.5"));
	}
	
	
	// File Type: 0 は規定されていない、14-247 は予約、248-255 は要使用者間の取り決め
	int filetype = fileType.getIntValue();
	if(filetype == 0){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_CAUTION, "File Type",
			 "This field shall specify the type of the file as shown in figure 4/17.\n" +
			 "0: Shall mean that the interpretation of the file is not specified by this field",
			 "4/14.6.6", "0", ""));
	}
	else if(13 < filetype && filetype < 248){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "File Type",
			 "This field shall specify the type of the file as shown in figure 4/17.\n" +
			 "14-247: Reserved for future standardisation",
			 "4/14.6.6", String.valueOf(filetype), ""));
	}
	else if(248 <= filetype){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_CAUTION, "File Type",
			 "This field shall specify the type of the file as shown in figure 4/17.\n" +
			 "248-255: Shall be subject to agreement between the originator and recipient of the medium",
			 "4/14.6.6", String.valueOf(filetype), ""));
	}

	//　Flags の検証　//
	int flagsValue = flags.getIntValue();

	//　Flags Bit 0-2 に示されるAllocation Descriptor の種類を表す数値のうち、4 - 7 は予約分である　//
	int ad_type = flagsValue & 0x0003;
	if (ad_type < 0 || 3 < ad_type) {

	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Flags",
			 "The values of 4-7 are reserved for future standardisation.",
			 "4/14.6.8", String.valueOf(flagsValue), ""));
	}

	// Flags の14-15 ビットは0
	if((flagsValue & 0xc000) != 0){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Flags",
			 "This field shall specify recording information about the file as shown in figure 4/18.\n" +
			 "14-15: Shall be reserved for future standardisation and all bits shall be set to ZERO.",
			 "4/14.6.8", String.valueOf(flagsValue), ""));
	}
	
	
	el.setRName("ICB Tag");
	el.setGlobalPoint(getGlobalPoint());
	return el;
    }
    
    /**
       Flags のAD を指しているビットの値を検証する。
       エラーにはC_ECMA167 カテゴリ、L_ERROR レベル、原因、記録値、期待値が含まれる。
       @param  flag AD の種類を示す値。0,1,2,3 のいずれかを指定可能。
       @return エラーインスタンス。
    */
    public UDF_Error verifyAdFlag(int flag) throws UDF_Exception{
	
	int adflag = getFlags().getIntValue() & 0x03;
	if(adflag != flag){
	    
	    return new UDF_Error(UDF_Error.C_ECMA167, UDF_Error.L_ERROR, "Flags(bit 0-2)",
				 "", "", String.valueOf(adflag), String.valueOf(flag));
	}
	else
	    return new UDF_Error();
    }
    
    /**
       File Type の値を検証する。
       エラーには、L_ERROR レベル、原因、記録値、期待値が設定される。
       
       @param type  比較検証する値。エラーの期待値にはこの値が設定される。
       @return エラーインスタンス。
    */
    public UDF_Error verifyFileType(int type){
	
	if(fileType.getIntValue() != type){
	    
	    return new UDF_Error(UDF_Error.C_ECMA167, UDF_Error.L_ERROR, "File Type",
				 "", "", String.valueOf(fileType.getIntValue()), String.valueOf(type));
	}
	else
	    return new UDF_Error();
    }
    
//end:
};
