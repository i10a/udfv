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
package com.udfv.udf250;

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
<tr><td><b>PartMapType</b></td><td><b>UDF_uint8</b></td><td><i>1</i></td></tr>
<tr><td><b>PartMapLen</b></td><td><b>UDF_uint8</b></td><td><i>1</i></td></tr>
<tr><td><b>Reserved</b></td><td><b>UDF_bytes</b></td><td><i>2</i></td></tr>
<tr><td><b>PartTypeId</b></td><td><b>UDF_regid</b></td><td><i>partTypeId.getSize()</i></td></tr>
<tr><td><b>VolSeqNumber</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td></tr>
<tr><td><b>PartNumber</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td></tr>
<tr><td><b>MetadataFileLoc</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>MetadataMirrorFileLoc</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>MetadataBitmapFileLoc</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>AllocUnitSize</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>AlignmentUnitSize</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td></tr>
<tr><td><b>Flags</b></td><td><b>UDF_uint8</b></td><td><i>1</i></td></tr>
<tr><td><b>Reserved2</b></td><td><b>UDF_bytes</b></td><td><i>5</i></td></tr>
</table>
</dd>
</dl>
*/
public class UDF_MetadataPartMap extends UDF_PartMap 
{

    /**
	このオブジェクトのパッケージ名を除いたクラス名を取得する。

	@return クラス名
    */
    public static String getUDFClassName( ) {
        return "UDF_MetadataPartMap";
    }

    /**
	コンストラクタ。

	@param elem   親エレメント。
	@param prefix ネームスペース。
	@param name   エレメント名。
    */
    public UDF_MetadataPartMap(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }
    /**
	このUDFエレメントのサイズを返す。

	@return UDFエレメントのサイズ。
    */
    public int getSize(){
	return 0+1+1+reserved.getSize()+partTypeId.getSize()+2+2+4+4+4+4+2+1+reserved2.getSize();
    }

    /**
	このUDFエレメントのサイズをlong 型で返す。

	@return UDFエレメントのサイズ。
    */
    public long getLongSize(){
	return (long)0+1+1+reserved.getSize()+partTypeId.getSize()+2+2+4+4+4+4+2+1+reserved2.getSize();
    }
    private UDF_uint8 partMapType;
    private UDF_uint8 partMapLen;
    private UDF_bytes reserved;
    private com.udfv.ecma167.UDF_regid partTypeId;
    private UDF_uint16 volSeqNumber;
    private UDF_uint16 partNumber;
    private UDF_uint32 metadataFileLoc;
    private UDF_uint32 metadataMirrorFileLoc;
    private UDF_uint32 metadataBitmapFileLoc;
    private UDF_uint32 allocUnitSize;
    private UDF_uint16 alignmentUnitSize;
    private UDF_uint8 flags;
    private UDF_bytes reserved2;

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
	metadataFileLocを取得する。

	@return 取得したmetadataFileLoc を返す。
    */
    public UDF_uint32 getMetadataFileLoc(){return metadataFileLoc;}
    /**
	metadataMirrorFileLocを取得する。

	@return 取得したmetadataMirrorFileLoc を返す。
    */
    public UDF_uint32 getMetadataMirrorFileLoc(){return metadataMirrorFileLoc;}
    /**
	metadataBitmapFileLocを取得する。

	@return 取得したmetadataBitmapFileLoc を返す。
    */
    public UDF_uint32 getMetadataBitmapFileLoc(){return metadataBitmapFileLoc;}
    /**
	allocUnitSizeを取得する。

	@return 取得したallocUnitSize を返す。
    */
    public UDF_uint32 getAllocUnitSize(){return allocUnitSize;}
    /**
	alignmentUnitSizeを取得する。

	@return 取得したalignmentUnitSize を返す。
    */
    public UDF_uint16 getAlignmentUnitSize(){return alignmentUnitSize;}
    /**
	flagsを取得する。

	@return 取得したflags を返す。
    */
    public UDF_uint8 getFlags(){return flags;}
    /**
	reserved2を取得する。

	@return 取得したreserved2 を返す。
    */
    public UDF_bytes getReserved2(){return reserved2;}

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
	metadataFileLocを設定する。

	@param	v 設定する値。
    */
    public void setMetadataFileLoc(UDF_uint32 v){replaceChild(v, metadataFileLoc); metadataFileLoc = v;}
    /**
	metadataMirrorFileLocを設定する。

	@param	v 設定する値。
    */
    public void setMetadataMirrorFileLoc(UDF_uint32 v){replaceChild(v, metadataMirrorFileLoc); metadataMirrorFileLoc = v;}
    /**
	metadataBitmapFileLocを設定する。

	@param	v 設定する値。
    */
    public void setMetadataBitmapFileLoc(UDF_uint32 v){replaceChild(v, metadataBitmapFileLoc); metadataBitmapFileLoc = v;}
    /**
	allocUnitSizeを設定する。

	@param	v 設定する値。
    */
    public void setAllocUnitSize(UDF_uint32 v){replaceChild(v, allocUnitSize); allocUnitSize = v;}
    /**
	alignmentUnitSizeを設定する。

	@param	v 設定する値。
    */
    public void setAlignmentUnitSize(UDF_uint16 v){replaceChild(v, alignmentUnitSize); alignmentUnitSize = v;}
    /**
	flagsを設定する。

	@param	v 設定する値。
    */
    public void setFlags(UDF_uint8 v){replaceChild(v, flags); flags = v;}
    /**
	reserved2を設定する。

	@param	v 設定する値。
    */
    public void setReserved2(UDF_bytes v){replaceChild(v, reserved2); reserved2 = v;}

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
	metadataFileLoc = (UDF_uint32)createElement("UDF_uint32", "", "metadata-file-loc");
	rsize += metadataFileLoc.readFrom(f);
	metadataMirrorFileLoc = (UDF_uint32)createElement("UDF_uint32", "", "metadata-mirror-file-loc");
	rsize += metadataMirrorFileLoc.readFrom(f);
	metadataBitmapFileLoc = (UDF_uint32)createElement("UDF_uint32", "", "metadata-bitmap-file-loc");
	rsize += metadataBitmapFileLoc.readFrom(f);
	allocUnitSize = (UDF_uint32)createElement("UDF_uint32", "", "alloc-unit-size");
	rsize += allocUnitSize.readFrom(f);
	alignmentUnitSize = (UDF_uint16)createElement("UDF_uint16", "", "alignment-unit-size");
	rsize += alignmentUnitSize.readFrom(f);
	flags = (UDF_uint8)createElement("UDF_uint8", "", "flags");
	rsize += flags.readFrom(f);
	reserved2 = (UDF_bytes)createElement("UDF_bytes", "", "reserved2");
	reserved2.setSize(5);
	rsize += reserved2.readFrom(f);
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
	wsize += metadataFileLoc.writeTo(f);
	wsize += metadataMirrorFileLoc.writeTo(f);
	wsize += metadataBitmapFileLoc.writeTo(f);
	wsize += allocUnitSize.writeTo(f);
	wsize += alignmentUnitSize.writeTo(f);
	wsize += flags.writeTo(f);
	wsize += reserved2.writeTo(f);
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
	    else if(name.equals("metadata-file-loc")){
		metadataFileLoc = (UDF_uint32)createElement("UDF_uint32", "", "metadata-file-loc");
		metadataFileLoc.readFromXML(child);
	    }
	    else if(name.equals("metadata-mirror-file-loc")){
		metadataMirrorFileLoc = (UDF_uint32)createElement("UDF_uint32", "", "metadata-mirror-file-loc");
		metadataMirrorFileLoc.readFromXML(child);
	    }
	    else if(name.equals("metadata-bitmap-file-loc")){
		metadataBitmapFileLoc = (UDF_uint32)createElement("UDF_uint32", "", "metadata-bitmap-file-loc");
		metadataBitmapFileLoc.readFromXML(child);
	    }
	    else if(name.equals("alloc-unit-size")){
		allocUnitSize = (UDF_uint32)createElement("UDF_uint32", "", "alloc-unit-size");
		allocUnitSize.readFromXML(child);
	    }
	    else if(name.equals("alignment-unit-size")){
		alignmentUnitSize = (UDF_uint16)createElement("UDF_uint16", "", "alignment-unit-size");
		alignmentUnitSize.readFromXML(child);
	    }
	    else if(name.equals("flags")){
		flags = (UDF_uint8)createElement("UDF_uint8", "", "flags");
		flags.readFromXML(child);
	    }
	    else if(name.equals("reserved2")){
		reserved2 = (UDF_bytes)createElement("UDF_bytes", "", "reserved2");
		reserved2.setSize(5);
		reserved2.readFromXML(child);
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
	metadataFileLoc = (UDF_uint32)createElement("UDF_uint32", "", "metadata-file-loc");
	metadataFileLoc.setDefaultValue();
	metadataMirrorFileLoc = (UDF_uint32)createElement("UDF_uint32", "", "metadata-mirror-file-loc");
	metadataMirrorFileLoc.setDefaultValue();
	metadataBitmapFileLoc = (UDF_uint32)createElement("UDF_uint32", "", "metadata-bitmap-file-loc");
	metadataBitmapFileLoc.setDefaultValue();
	allocUnitSize = (UDF_uint32)createElement("UDF_uint32", "", "alloc-unit-size");
	allocUnitSize.setDefaultValue();
	alignmentUnitSize = (UDF_uint16)createElement("UDF_uint16", "", "alignment-unit-size");
	alignmentUnitSize.setDefaultValue();
	flags = (UDF_uint8)createElement("UDF_uint8", "", "flags");
	flags.setDefaultValue();
	reserved2 = (UDF_bytes)createElement("UDF_bytes", "", "reserved2");
	reserved2.setSize(5);
	reserved2.setDefaultValue();

	apply( );
	postSetDefaultValueHook();
    }


    /**
	複製する
    */
    public UDF_Element duplicateElement(){
	UDF_MetadataPartMap dup = new UDF_MetadataPartMap(this, null, getName());
	dup.setDefaultValue();
	dup.duplicateHook(this);
	dup.setPartMapType((UDF_uint8)partMapType.duplicateElement());
	dup.setPartMapLen((UDF_uint8)partMapLen.duplicateElement());
	dup.setReserved((UDF_bytes)reserved.duplicateElement());
	dup.setPartTypeId((com.udfv.ecma167.UDF_regid)partTypeId.duplicateElement());
	dup.setVolSeqNumber((UDF_uint16)volSeqNumber.duplicateElement());
	dup.setPartNumber((UDF_uint16)partNumber.duplicateElement());
	dup.setMetadataFileLoc((UDF_uint32)metadataFileLoc.duplicateElement());
	dup.setMetadataMirrorFileLoc((UDF_uint32)metadataMirrorFileLoc.duplicateElement());
	dup.setMetadataBitmapFileLoc((UDF_uint32)metadataBitmapFileLoc.duplicateElement());
	dup.setAllocUnitSize((UDF_uint32)allocUnitSize.duplicateElement());
	dup.setAlignmentUnitSize((UDF_uint16)alignmentUnitSize.duplicateElement());
	dup.setFlags((UDF_uint8)flags.duplicateElement());
	dup.setReserved2((UDF_bytes)reserved2.duplicateElement());

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
	appendChild(metadataFileLoc);
	appendChild(metadataMirrorFileLoc);
	appendChild(metadataBitmapFileLoc);
	appendChild(allocUnitSize);
	appendChild(alignmentUnitSize);
	appendChild(flags);
	appendChild(reserved2);
    }

    public void debug(int indent){
	System.err.print(UDF_Util.repeat(' ', indent));
	System.err.println(getName() + ": (" + hashCode() + ")");	partMapType.debug(indent + 1);
	partMapLen.debug(indent + 1);
	reserved.debug(indent + 1);
	partTypeId.debug(indent + 1);
	volSeqNumber.debug(indent + 1);
	partNumber.debug(indent + 1);
	metadataFileLoc.debug(indent + 1);
	metadataMirrorFileLoc.debug(indent + 1);
	metadataBitmapFileLoc.debug(indent + 1);
	allocUnitSize.debug(indent + 1);
	alignmentUnitSize.debug(indent + 1);
	flags.debug(indent + 1);
	reserved2.debug(indent + 1);
    }
//begin:add your code here

    /**
        Metadata Part. Map の情報を表示。

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
	str = indent + "Metadata File Loc.        # " + getMetadataFileLoc().getIntValue();
	vec.add(str);
	str = indent + "Metadata Mirror File Loc. # " + getMetadataMirrorFileLoc().getIntValue();
	vec.add(str);
	str = indent + "Metadata Bitmap File Loc. # " + getMetadataBitmapFileLoc().getIntValue();
	vec.add(str);
	str = indent + "Alloc Unit Size           # " + getAllocUnitSize().getIntValue();
	vec.add(str);
	str = indent + "Alignment Unit Size       # " + getAlignmentUnitSize().getIntValue();
	vec.add(str);
	str = indent + "Flags                     # " + getFlags().getIntValue();
	vec.add(str);

	String [] sl = new String[vec.size()];
	for (int i = 0, max = vec.size(); i < max; i++) {
	    sl[i] = (String)vec.elementAt(i);
	}

	return sl;
    }

    /*
     * MetadatPartmap -> main/mirror/bitampのリンク
     * リンクが貼られるのは
     * 
     * 1)recalc(RECALC_PARTMAP)をしたとき
     * 2)readMetadataPartMaps()をしたとき。 (※中で recalcが呼ばれている)
     *
     */
    UDF_FEDesc my_metadata_file;
    UDF_FEDesc my_metadata_mirror_file;
    UDF_FEDesc my_metadata_bitmap_file;

    public UDF_FEDesc getMetadataFile(){
	return my_metadata_file;
    }
    public UDF_FEDesc getMetadataMirrorFile(){
	return my_metadata_mirror_file;
    }
    public UDF_FEDesc getMetadataBitmapFile(){
	return my_metadata_bitmap_file;
    }

    public void setMetadataFile(UDF_FEDesc fe){
	my_metadata_file = fe;
    }
    public void setMetadataMirrorFile(UDF_FEDesc fe){
	my_metadata_mirror_file = fe;
    }
    public void setMetadataBitmapFile(UDF_FEDesc fe){
	my_metadata_bitmap_file = fe;
    }

    public UDF_desc264 getMetadataBitmap(){
	UDF_FEDesc fe = my_metadata_bitmap_file;
	if(fe == null)
	    return null;

	int file_flags = fe.getICBFlags() & 0x07;

	if(file_flags == 3) {
	    UDF_desc264 desc264 = (UDF_desc264) fe.getAllocDesc().getFirstChild();
	    return desc264;
	}
	else {
	    String label = UDF_Util.car(fe.getAllocDesc().getAttribute("ref"), '.');
	    UDF_desc264 desc264 = (UDF_desc264) findById(label).getFirstChild();

	    return desc264;
	}
    }

    public void recalc(short type, UDF_RandomAccess f){
	super.recalc(type, f);
	try{
	    if(type == RECALC_PARTMAP){
		String ref = UDF_Util.car(metadataFileLoc.getAttribute("ref"), '.');
		String ref2 = UDF_Util.car(metadataMirrorFileLoc.getAttribute("ref"), '.');
		String ref3 = UDF_Util.car(metadataBitmapFileLoc.getAttribute("ref"), '.');

		UDF_Extent ext = (UDF_Extent)findById(ref);
		if(ext != null)
		    my_metadata_file = (UDF_FEDesc)ext.getFirstChild();

		UDF_Extent ext2 = (UDF_Extent)findById(ref2);
		if(ext2 != null)
		    my_metadata_mirror_file = (UDF_FEDesc)ext2.getFirstChild();

		UDF_Extent ext3 = (UDF_Extent)findById(ref3);
		if(ext3 != null)
		    my_metadata_bitmap_file = (UDF_FEDesc)ext3.getFirstChild();
	    }
	}
	catch(Exception e){
	    ignoreMsg("UDF_MetadataPartMap", e);
	}
    }

    public void recalcPM(int partrefno) throws UDF_PartMapException,UDF_InternalException{
	String ref = UDF_Util.car(metadataFileLoc.getAttribute("ref"), '.');
	String ref2 = UDF_Util.car(metadataMirrorFileLoc.getAttribute("ref"), '.');
	String ref3 = UDF_Util.car(metadataBitmapFileLoc.getAttribute("ref"), '.');
	
	UDF_Extent ext = (UDF_Extent)findById(ref);
	if(ext != null){
	    my_metadata_file = (UDF_FEDesc)ext.getFirstChild();
	    my_metadata_file.recalcADLIST();
	}
	
	UDF_Extent ext2 = (UDF_Extent)findById(ref2);
	if(ext2 != null){
	    my_metadata_mirror_file = (UDF_FEDesc)ext2.getFirstChild();
	    my_metadata_mirror_file.recalcADLIST();
	}
	
	UDF_Extent ext3 = (UDF_Extent)findById(ref3);
	if(ext3 != null){
	    my_metadata_bitmap_file = (UDF_FEDesc)ext3.getFirstChild();
	    my_metadata_bitmap_file.recalcADLIST();
	}
	
	UDF_Extent main_ext = genExtent(0);
	main_ext.blessExtent();

	env.setPartMapExtent(partrefno, 0, main_ext);
	env.setPartMapRandomAccess(partrefno, 0, main_ext.genRandomAccessExtent());

	UDF_Extent mirr_ext = genExtent(1);
	mirr_ext.blessExtent();

	env.setPartMapExtent(partrefno, 1, mirr_ext);
	env.setPartMapRandomAccess(partrefno, 1, mirr_ext.genRandomAccessExtent());
	
    }

    public UDF_Extent genExtent(int subno) throws UDF_PartMapException {
	UDF_ADList el = null;
	try{
	    if(subno == 1)
		el = my_metadata_mirror_file.getADList();
	    else
		el = my_metadata_file.getADList();
	}
	//
	catch(NullPointerException e){
	    throw new UDF_PartMapException(this, "no metarata file or metadata mirror file");
	}
	    
	UDF_Extent ext = (UDF_Extent)createElement("UDF_Extent", null, null);
	
	// Type1かSparable PartMapのPartRefNoを取ってくる(2005/07/27 by seta)
	int metafepartrefno = -1;
	for(int i = 0 ; i<env.part.length ; ++i){
	    if(env.part[i].getPartMapType().getIntValue() == 1 ||
	       com.udfv.udf150.UDF_SparablePartMap.class.isAssignableFrom(env.part[i].getClass())){
		metafepartrefno = i;
		break;
	    }
	}
	
	if(metafepartrefno == -1)
	    throw new UDF_PartMapException(this, "no Type 1 Partition Map");

	for(int i=0 ; i<el.size() ; ++i){
	    UDF_AD ad = el.elementAt(i);
	    ext.addExtent(ad.getLbn(), metafepartrefno, ad.getLen(), ad.getFlag());
	}

	return ext;
    }


    public void postReadHook(UDF_RandomAccess f){
	//int partno = partNumber.getIntValue();
	int partno = 0;
	int metadata_file_loc = metadataFileLoc.getIntValue();
	int metadata_mirror_loc = metadataMirrorFileLoc.getIntValue();
	int metadata_bitmap_loc = metadataBitmapFileLoc.getIntValue();

	metadataFileLoc.setAttribute("ref", 
				     UDF_Label.genFELabel(env, metadata_file_loc, partno, 0) + ".lbn");
	metadataMirrorFileLoc.setAttribute("ref",
					   UDF_Label.genFELabel(env, metadata_mirror_loc, partno, 0) + ".lbn");
	metadataBitmapFileLoc.setAttribute("ref",
					   UDF_Label.genFELabel(env, metadata_bitmap_loc, partno, 0) + ".lbn");
    }

    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	final short category = UDF_Error.C_UDF250;
	
	
	if(env.udf_revision != 0x250)
	    return el;
	
	// Access Type 1 か4 のパーティションに存在しなければならない
	// （2.6では、これに0(pseud-overwritable)が加わる）
	//int at = env.partDesc.getAccessType().getIntValue();
	int at = env.getPartDescByPartRefNo(UDF_Env.VDS_AUTO, getPartPartno()).getAccessType().getIntValue();
	if(at != env.MM_READONLY && at != env.MM_OVERWRITABLE){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "",
			 "This partition map shall be recorded for volumes which contain a single partition having an " +
			 "access type of 1(read only) or 4(overwritable).",
			 "2.2.10"));
	}
	
	el.addError(verifyBase(category, "2.2.10"));
	
	el.setRName("UDF Metadata Partition Map");
	el.setGlobalPoint(getGlobalPoint());
	return el;
    }
    
    /**
       Metadata Partition Map のUDF 2.50 以降共通の検証を行う。
       
       @param category  エラーカテゴリ。
       @param refer     リファレンス文字列。
       @return エラーリストインスタンス。
    */
    public UDF_ErrorList verifyBase(short category, String refer) throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	UDF_Error ret;
	final String basemsg = "Shall be recorded in the format shown in 'Layout of Type 2 partition map for sparable partition'.";
	
	
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
	
	ret = partTypeId.verifyId("*UDF Metadata Partition");
	if(ret.isError()){
	    
	    ret.setCategory(category);
	    ret.setMessage(basemsg);
	    ret.setRefer(refer);
	    ret.setRName(ptid);
	    el.addError(ret);
	}
    
	// reserved2 も0
	if(!UDF_Util.isAllZero(reserved2.getData())){
	    
	    el.addError(new UDF_Error(category, UDF_Error.L_ERROR, "Reserved(2nd)", basemsg + "(#00 bytes)", refer));
	}
	
	long pktlen = -1;
	// Sparable PartMap が存在するか？
	for(int i = 0; i < env.getPartMapList().size() ; i++){
	    
	    if(com.udfv.udf200.UDF_SparablePartMap.class.isAssignableFrom(env.part[i].getClass())){
		
		com.udfv.udf200.UDF_SparablePartMap spm = (com.udfv.udf200.UDF_SparablePartMap)env.part[i];
		pktlen = spm.getPacketLen().getLongValue();
		break;
	    }
	}
	
	int allocunitsize = allocUnitSize.getIntValue();
	long ecc = env.ecc_blocksize / env.LBS;
	String errstr = "This value shall be an integer multiple of the larger of the following three values: " +
	    "(media ECC block size (divided by) logical block size(=" + ecc + ")); ";
	if(pktlen != -1)
	    errstr += "Packet Length(=" + pktlen + ") (if a type 2 sparable partition map is recorded);";
	else
	    errstr += "Packet Length (if a type 2 sparable partition map is recorded);";
	
	
	// Alignment Unit Size は、ECC、Packet Len のうち、大きい値の整数倍である
	long max = (pktlen < ecc) ? ecc : pktlen;
	if(alignmentUnitSize.getIntValue() < max){
	    
	    el.addError(new UDF_Error(category, UDF_Error.L_ERROR, "Alignment Unit Size", errstr,
				      refer, String.valueOf(alignmentUnitSize.getLongValue()), ""));
	}
	
	// Allocation Unit Size は、ECC、Packet Len、32 のうち、最大値の整数倍である
	max = (32 < pktlen) ? pktlen : 32;
	max = (max < ecc) ? ecc : max;
	
	if(allocunitsize < max){
	    
	    errstr += " 32.";
	    el.addError(new UDF_Error(category, UDF_Error.L_ERROR, "Allocation Unit Size", errstr,
				      refer, String.valueOf(allocUnitSize.getLongValue()), ""));
	}
	
	// Flags の1〜7ビットは0
	if((flags.getIntValue() & 0xfe) != 0){
	    
	    el.addError(new UDF_Error
			(category, UDF_Error.L_ERROR, "Flags",
			 "Bits 1-7: Reserved. Shall be set to zero on write, and ignored on read.",
			 refer, String.valueOf(flags.getIntValue()), ""));
	}
	
	// Metadata Bitmap Loc がReadOnly パーティション以外で0xFFFFFFF であってはならない
	try{
	    com.udfv.ecma167.UDF_desc5 pd = env.getPartDesc(env.VDS_AUTO, 0);
	    if(pd.getAccessType().getIntValue() != 1){ // read only
		
		int mbloc = getMetadataBitmapFileLoc().getIntValue();
		if(mbloc == -1){
		    
		    el.addError(new UDF_Error
				(category, UDF_Error.L_ERROR, "Metadata Bitmap File Location",
				 "Metadata Bitmap is undefined with access type in Partition Descriptor is "
				 + pd.getAccessType().getIntValue() + ". " +
				 "When a Type2 Metadata Partition map is recorded, the Metadata File, Metadata Mirror File " +
				 "and Metadata Bitmap File shall also be recorded and maintained. The sole exception is " +
				 "that a Metadata Bitmap File shall not be recorded for a read-only partition.",
				 refer, "#FFFFFFFF", ""));
		}
	    }
	}
	catch(UDF_VolException e){
	    ;
	}

	return el;
    }
    /*
    public boolean hasMirror(){
	return true;
    }
    */
        
//end:
};
