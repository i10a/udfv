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
package com.udfv.udf102;

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
<tr><td><b>ImplId</b></td><td><b>UDF_regid</b></td><td><i>implId.getSize()</i></td></tr>
<tr><td><b>NumberOfFiles</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>NumberOfDirectories</b></td><td><b>UDF_uint32</b></td><td><i>4</i></td></tr>
<tr><td><b>MinUDFReadRevision</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td></tr>
<tr><td><b>MinUDFWriteRevision</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td></tr>
<tr><td><b>MaxUDFWriteRevision</b></td><td><b>UDF_uint16</b></td><td><i>2</i></td></tr>
<tr><td><b>ImplUse</b></td><td><b>UDF_bytes</b></td><td><i>getHintSize() - 46</i></td></tr>
</table>
</dd>
</dl>
*/
public class UDF_desc9_ImplUse extends UDF_Element 
{

    /**
	このオブジェクトのパッケージ名を除いたクラス名を取得する。

	@return クラス名
    */
    public static String getUDFClassName( ) {
        return "UDF_desc9_ImplUse";
    }

    /**
	コンストラクタ。

	@param elem   親エレメント。
	@param prefix ネームスペース。
	@param name   エレメント名。
    */
    public UDF_desc9_ImplUse(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
	init();
    }
    /**
	このUDFエレメントのサイズを返す。

	@return UDFエレメントのサイズ。
    */
    public int getSize(){
	return 0+implId.getSize()+4+4+2+2+2+implUse.getSize();
    }

    /**
	このUDFエレメントのサイズをlong 型で返す。

	@return UDFエレメントのサイズ。
    */
    public long getLongSize(){
	return (long)0+implId.getSize()+4+4+2+2+2+implUse.getSize();
    }
    private com.udfv.ecma167.UDF_regid implId;
    private UDF_uint32 numberOfFiles;
    private UDF_uint32 numberOfDirectories;
    private UDF_uint16 minUDFReadRevision;
    private UDF_uint16 minUDFWriteRevision;
    private UDF_uint16 maxUDFWriteRevision;
    private UDF_bytes implUse;

    /**
	implIdを取得する。

	@return 取得したimplId を返す。
    */
    public com.udfv.ecma167.UDF_regid getImplId(){return implId;}
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
	minUDFReadRevisionを取得する。

	@return 取得したminUDFReadRevision を返す。
    */
    public UDF_uint16 getMinUDFReadRevision(){return minUDFReadRevision;}
    /**
	minUDFWriteRevisionを取得する。

	@return 取得したminUDFWriteRevision を返す。
    */
    public UDF_uint16 getMinUDFWriteRevision(){return minUDFWriteRevision;}
    /**
	maxUDFWriteRevisionを取得する。

	@return 取得したmaxUDFWriteRevision を返す。
    */
    public UDF_uint16 getMaxUDFWriteRevision(){return maxUDFWriteRevision;}
    /**
	implUseを取得する。

	@return 取得したimplUse を返す。
    */
    public UDF_bytes getImplUse(){return implUse;}

    /**
	implIdを設定する。

	@param	v 設定する値。
    */
    public void setImplId(com.udfv.ecma167.UDF_regid v){replaceChild(v, implId); implId = v;}
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
	minUDFReadRevisionを設定する。

	@param	v 設定する値。
    */
    public void setMinUDFReadRevision(UDF_uint16 v){replaceChild(v, minUDFReadRevision); minUDFReadRevision = v;}
    /**
	minUDFWriteRevisionを設定する。

	@param	v 設定する値。
    */
    public void setMinUDFWriteRevision(UDF_uint16 v){replaceChild(v, minUDFWriteRevision); minUDFWriteRevision = v;}
    /**
	maxUDFWriteRevisionを設定する。

	@param	v 設定する値。
    */
    public void setMaxUDFWriteRevision(UDF_uint16 v){replaceChild(v, maxUDFWriteRevision); maxUDFWriteRevision = v;}
    /**
	implUseを設定する。

	@param	v 設定する値。
    */
    public void setImplUse(UDF_bytes v){replaceChild(v, implUse); implUse = v;}

    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long rsize = 0;
	preReadHook(f);
	implId = (UDF_regid)createElement("UDF_regid", "", "impl-id");
	rsize += implId.readFrom(f);
	numberOfFiles = (UDF_uint32)createElement("UDF_uint32", "", "number-of-files");
	rsize += numberOfFiles.readFrom(f);
	numberOfDirectories = (UDF_uint32)createElement("UDF_uint32", "", "number-of-directories");
	rsize += numberOfDirectories.readFrom(f);
	minUDFReadRevision = (UDF_uint16)createElement("UDF_uint16", "", "min-udf-read-revision");
	rsize += minUDFReadRevision.readFrom(f);
	minUDFWriteRevision = (UDF_uint16)createElement("UDF_uint16", "", "min-udf-write-revision");
	rsize += minUDFWriteRevision.readFrom(f);
	maxUDFWriteRevision = (UDF_uint16)createElement("UDF_uint16", "", "max-udf-write-revision");
	rsize += maxUDFWriteRevision.readFrom(f);
	implUse = (UDF_bytes)createElement("UDF_bytes", "", "impl-use");
	implUse.setSize(getHintSize() - 46);
	rsize += implUse.readFrom(f);
	apply( );
	postReadHook(f);
	return rsize;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long wsize = 0;
	wsize += implId.writeTo(f);
	wsize += numberOfFiles.writeTo(f);
	wsize += numberOfDirectories.writeTo(f);
	wsize += minUDFReadRevision.writeTo(f);
	wsize += minUDFWriteRevision.writeTo(f);
	wsize += maxUDFWriteRevision.writeTo(f);
	wsize += implUse.writeTo(f);
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
	    else if(name.equals("impl-id")){
		implId = (UDF_regid)createElement("UDF_regid", "", "impl-id");
		implId.readFromXML(child);
	    }
	    else if(name.equals("number-of-files")){
		numberOfFiles = (UDF_uint32)createElement("UDF_uint32", "", "number-of-files");
		numberOfFiles.readFromXML(child);
	    }
	    else if(name.equals("number-of-directories")){
		numberOfDirectories = (UDF_uint32)createElement("UDF_uint32", "", "number-of-directories");
		numberOfDirectories.readFromXML(child);
	    }
	    else if(name.equals("min-udf-read-revision")){
		minUDFReadRevision = (UDF_uint16)createElement("UDF_uint16", "", "min-udf-read-revision");
		minUDFReadRevision.readFromXML(child);
	    }
	    else if(name.equals("min-udf-write-revision")){
		minUDFWriteRevision = (UDF_uint16)createElement("UDF_uint16", "", "min-udf-write-revision");
		minUDFWriteRevision.readFromXML(child);
	    }
	    else if(name.equals("max-udf-write-revision")){
		maxUDFWriteRevision = (UDF_uint16)createElement("UDF_uint16", "", "max-udf-write-revision");
		maxUDFWriteRevision.readFromXML(child);
	    }
	    else if(name.equals("impl-use")){
		implUse = (UDF_bytes)createElement("UDF_bytes", "", "impl-use");
		implUse.setSize(getHintSize() - 46);
		implUse.readFromXML(child);
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
	implId = (UDF_regid)createElement("UDF_regid", "", "impl-id");
	implId.setDefaultValue();
	numberOfFiles = (UDF_uint32)createElement("UDF_uint32", "", "number-of-files");
	numberOfFiles.setDefaultValue();
	numberOfDirectories = (UDF_uint32)createElement("UDF_uint32", "", "number-of-directories");
	numberOfDirectories.setDefaultValue();
	minUDFReadRevision = (UDF_uint16)createElement("UDF_uint16", "", "min-udf-read-revision");
	minUDFReadRevision.setDefaultValue();
	minUDFWriteRevision = (UDF_uint16)createElement("UDF_uint16", "", "min-udf-write-revision");
	minUDFWriteRevision.setDefaultValue();
	maxUDFWriteRevision = (UDF_uint16)createElement("UDF_uint16", "", "max-udf-write-revision");
	maxUDFWriteRevision.setDefaultValue();
	implUse = (UDF_bytes)createElement("UDF_bytes", "", "impl-use");
	implUse.setSize(getHintSize() - 46);
	implUse.setDefaultValue();

	apply( );
	postSetDefaultValueHook();
    }


    /**
	複製する
    */
    public UDF_Element duplicateElement(){
	UDF_desc9_ImplUse dup = new UDF_desc9_ImplUse(this, null, getName());
	dup.setDefaultValue();
	dup.duplicateHook(this);
	dup.setImplId((com.udfv.ecma167.UDF_regid)implId.duplicateElement());
	dup.setNumberOfFiles((UDF_uint32)numberOfFiles.duplicateElement());
	dup.setNumberOfDirectories((UDF_uint32)numberOfDirectories.duplicateElement());
	dup.setMinUDFReadRevision((UDF_uint16)minUDFReadRevision.duplicateElement());
	dup.setMinUDFWriteRevision((UDF_uint16)minUDFWriteRevision.duplicateElement());
	dup.setMaxUDFWriteRevision((UDF_uint16)maxUDFWriteRevision.duplicateElement());
	dup.setImplUse((UDF_bytes)implUse.duplicateElement());

	apply();

	return dup;
    }

    /**
        子の UDF Element の Node を append しなおす。

    */
    private void apply( ) {

	removeAllChildren();
	appendChild(implId);
	appendChild(numberOfFiles);
	appendChild(numberOfDirectories);
	appendChild(minUDFReadRevision);
	appendChild(minUDFWriteRevision);
	appendChild(maxUDFWriteRevision);
	appendChild(implUse);
    }

    public void debug(int indent){
	System.err.print(UDF_Util.repeat(' ', indent));
	System.err.println(getName() + ": (" + hashCode() + ")");	implId.debug(indent + 1);
	numberOfFiles.debug(indent + 1);
	numberOfDirectories.debug(indent + 1);
	minUDFReadRevision.debug(indent + 1);
	minUDFWriteRevision.debug(indent + 1);
	maxUDFWriteRevision.debug(indent + 1);
	implUse.debug(indent + 1);
    }
//begin:add your code here
    public long addDirectories(long n){
	return numberOfDirectories.addValue(n);
    }
    public long addFiles(long n){
	return numberOfFiles.addValue(n);
    }
    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	
	
	if(env.udf_revision != 0x102)
	    return el;
	
	el.addError(verifyBase(UDF_Error.C_UDF102, "2.2.6.4"));
	el.setGlobalPoint(getGlobalPoint());
	
	return el;
    }
    
    protected UDF_ErrorList verifyBase(short category, String refer) throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	//親が prevailing ISじゃないときはチェックしない。

	if(env.getLogicalVolIntegrityDesc() != getParentCrcDesc())
	    return el;

	boolean virtualpart = false;

	
	// UDF2.00 以降でVirtual Partition が存在するとき、
	// 以下のフィールドはVirtual Alloc Table の同じフィールドをメンテしなければならない。
	// 但しこの構造のフィールドについては無視してもよいとまでは書いてなさそうなので、
	// その場合はWARNING とする。
	int i = 0;
	for(; i < env.getPartMapList().size(); i++){
	    
	    if(com.udfv.udf201.UDF_VirtualPartMap.class.isAssignableFrom(env.part[i].getClass())){
		
		virtualpart = true;
		break;
	    }
	}
	
	// ImplementationID
	el.addError(getImplId().verify("ImplementationID"));
	
	short level = (virtualpart) ? UDF_Error.L_WARNING : UDF_Error.L_ERROR;
	// ファイル数が一致しない
	if(numberOfFiles.getLongValue() != env.recorded_num_files){
	    
	    el.addError(new UDF_Error
			(category, level, "Number of Files",
			 "The current number of files in the associated Logical Volume. This information is needed by the Macintosh OS. " +
			 "All implementation shall maintain this information.", refer,
			 numberOfFiles.getLongValue(), env.recorded_num_files));
	}
	
	// ディレクトリ数が一致しない
	if(numberOfDirectories.getLongValue() != env.recorded_num_directories){
	    el.addError(new UDF_Error
			(category, level, "Number of Directories",
			 "The current number of directories in the associated Logical Volume. This information is needed by the Macintosh OS. " +
			 "All implementation shall maintain this information.", refer,
			 numberOfDirectories.getLongValue(), env.recorded_num_directories));
	}
	
	int minudfreadrev = getMinUDFReadRevision().getIntValue();
	int minudfwriterev = getMinUDFWriteRevision().getIntValue();
	int maxudfwriterev = getMaxUDFWriteRevision().getIntValue();

	// 多分102 よりは大きい
	if(minudfreadrev < 0x102){
        
	    el.addError(new UDF_Error
			(category, UDF_Error.L_WARNING, "Minimum UDF Read Revision",
			 "Shall indicate the minimum recommended revision of the UDF specification that an implementation is " +
			 "required to support to successfully be able to read all potential structures on the media.",
			 refer, String.valueOf(minudfreadrev), ""));
	}

	if(minudfwriterev < 0x102){
        
	    el.addError(new UDF_Error
			(category, UDF_Error.L_WARNING, "Minimum UDF Write Revision",
			 "Shall indicate the minimum revision of the UDF specification that an implementation is " +
			 "required to support to successfully be able to modify all potential structures on the media.",
			 refer, String.valueOf(minudfwriterev), ""));
	}

	if(minudfreadrev < 0x102){
        
	        el.addError(new UDF_Error
			(category, UDF_Error.L_WARNING, "Maximum UDF Write Revision",
			 "Shall indicate the maximum revision of the UDF specification that an implementation that " +
			 "has modified the media has supported.",
			 refer, String.valueOf(maxudfwriterev), ""));
	}
	
	return el;
    }
    public void postReadHook(UDF_RandomAccess f) throws IOException, UDF_Exception{
	//UDF 2.1.5.2
	getImplId().extractSuffixAsUDFImplIdSuffix();
    }

//end:
};
