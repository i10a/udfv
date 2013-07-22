/*

*/
package com.udfv.core;

import java.io.*;
import java.util.*;
import java.util.zip.*;
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
import org.apache.xml.serialize.*;
import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.access.*;
import com.udfv.exception.*;
import com.udfv.ecma167.*;

/**
   全てのバージョンのイメージを統轄するクラス。
*/
abstract public class UDF_Image extends UDF_Element
{
    // Document of XML
    private Document my_doc = null;
    // XML root Element
    private Element  root_node;
    // Parition 情報を表すエレメント
    private Element  my_partition;
    // Parition 情報を表すエレメント
    private Element  my_partmap;

    private int  desc2_mvds_loc[] = { 0, 0, 0, };
    private long desc2_mvds_len[] = { 0, 0, 0, };
    private int  desc2_rvds_loc[] = { 0, 0, 0, };
    private long desc2_rvds_len[] = { 0, 0, 0, };

    private Hashtable fe_hash = new Hashtable();
    private Hashtable fid_hash = new Hashtable();

    public UDF_Image(){
	super();
	setPkgPriority();
    }

    public UDF_Image(Document document){
	super();
	my_doc = document;
	setupNode(document);
	setPkgPriority();
    }

    /**
       UDF_Imageに Envを設定する。
     */
    public void setEnv(UDF_Env e){
	env = e;
	env.root = this;
    }
    /**
       UDF イメージを表現するXML のDocument インスタンスを設定する。
     */
    public void setUDFDocument(Document document){
	my_doc = document;
	setupNode(document);
    }

    /**
       UDF イメージを表現するXML のDocument インスタンスを取得する。
       
       @return Document インスタンス。
    */
    public Document getUDFDocument() { 
	return my_doc;
    }

    /**
       ファイルシステムを読み込む。
       
       @param f         イメージアクセサ。
       @param subno	副番号。

       @return 常に0を返す。
     */
    abstract public long readFileSystem(UDF_RandomAccess f, int subno) throws IOException, ClassNotFoundException, UDF_VolException, UDF_Exception;
    /**
       ファイルシステムを読み込む。
       
       @param f         イメージアクセサ。
       @param mirror	ミラーかどうか。

       @return 常に0を返す。

       @deprecated replaced by {@link #readFileSystem(UDF_RandomAccess, int)}

     */
    abstract public long readFileSystem(UDF_RandomAccess f, boolean mirror) throws IOException, ClassNotFoundException, UDF_VolException, UDF_Exception;
    abstract public long readVolume(UDF_RandomAccess f);

    private int progress_status;
    /**
       操作の進行度を取得する。

       @return 進行度 0-100の値
     */
    public synchronized int imageProgressStatus(){
	return progress_status;
    }
    /**
       操作の進行度の値を設定する。
     */
    protected synchronized void setProgressStatus(int st){
	progress_status = st;
    }
    /**
       FEの位置を指定して読む。FEがディレクトリならその先を辿る。

       @param mother	親
       @param lbn	lbn
       @param partno	partno
       @param subno	副パーティション番号
       @param sstream	sstream フラグ

       @see <a href="subno.html">副パーティション番号</a>
    */
    abstract protected UDF_Extent parseTree(UDF_Element mother, int lbn, int partno, int subno, boolean sstream) throws ClassNotFoundException, UDF_Exception, IOException;

    /**
       FEの位置を指定して読む。FEがディレクトリならその先を辿る。

       @param mother	親
       @param lbn	lbn
       @param partno	partno
       @param mirror	mirrorフラグ
       @param sstream	sstream フラグ
     */
    abstract protected UDF_Extent parseTree(UDF_Element mother, int lbn, int partno, boolean mirror, boolean sstream) throws ClassNotFoundException, UDF_Exception, IOException;

    /**
       parseTreeから呼ばれる関数。
    */
    protected void parseTreeDirectory(UDF_Element mother, UDF_FEDesc fe, int partno, int subno, boolean sstream) throws IOException, UDF_Exception, ClassNotFoundException{
	int file_flags = fe.getICBTag().getFlags().getIntValue() & 0x7;
	int file_type = fe.getICBTag().getFileType().getIntValue();
	/*
	  ADによるextentの場合は あらたな extentを追加する。
	  DIRECTの場合はすでに XML要素として <alloc-desc>に格納されているから
	  あたらしいXML要素は追加しない
	*/
	if(file_flags != 3){
	    com.udfv.core.UDF_Extent ext = (com.udfv.core.UDF_Extent)createElement("UDF_Extent", null, "UDF_Extent");
	    com.udfv.ecma167.UDF_Directory dir = (com.udfv.ecma167.UDF_Directory)(file_type == UDF_icbtag.T_SDIRECTORY ?
							 createElement("UDF_SDirectory", null, UDF_XML.SDIRECTORY) :
							 createElement("UDF_Directory", null, UDF_XML.DIRECTORY));

	    ext.setPartSubno(subno);

	    fe.setADToExtent(ext, partno);
	    dir.setSize(ext.getSize());//ここで設定したサイズは readFrom()後に変る
	    
	    //extentのラベルは廃止
	    mother.appendChild(ext);
	    ext.appendChild(dir);
	    UDF_RandomAccessExtent rae = ext.genRandomAccessExtent();
	    dir.readFrom(rae);

	    //このあたりの UDF_Extent, UDF_bytes, UDF_padの使用方法は
	    //通常の使用方法ではない。のちのち問題になる可能性があるかも。
	    rae.seek(dir.getSize());
	    UDF_pad pad = new UDF_pad(this, null, null, ext.getSize());
	    pad.readFrom(rae);
	    ext.appendChild(pad);

	    //かわりに UDF_Directoryにラベルをつけてみた
	    String dir_label = UDF_Label.genDirectoryLabel(dir);
	    fe.getAllocDesc().setAttribute("ref", dir_label);
	    dir.setAttribute("id", dir_label);
	    
	    parseDir(mother, fe, dir, subno, sstream);
	}
	else{
	    //かわりにDirectoryにラベルをつけてみた
	    //これなら3でもつけれる。
	    UDF_Directory dir = (UDF_Directory)fe.getAllocDesc().getFirstChild();
	    String dir_label = UDF_Label.genDirectoryLabel(dir);
	    fe.getAllocDesc().setAttribute("ref", dir_label);
	    dir.setAttribute("id", dir_label);
	    parseDir(mother, fe, fe.getAllocDesc(), subno, sstream);
	}
    }

    /**
       parseTreeから呼ばれる関数。

       @deprecated replaced by {@link #parseTreeDirectory(UDF_Element, UDF_FEDesc, int, int, boolean)}
     */
    protected void parseTreeDirectory(UDF_Element mother, UDF_FEDesc fe, int partno, boolean mirror, boolean sstream) throws IOException, UDF_Exception, ClassNotFoundException{
	parseTreeDirectory(mother, fe, partno, mirror ? 1 : 0, sstream);
    }
    
    /**
       parseTreeから呼ばれる関数。

     */
    protected void parseTreeFile(UDF_Element mother, UDF_FEDesc fe, int partno, int subno) throws IOException{
	boolean mirror = subno != 0 ? true : false;
	int file_flags = fe.getICBTag().getFlags().getIntValue() & 0x7;
	/*
	  ADによるextentの場合は あらたな extentを追加する。
	  DIRECTの場合はすでに XML要素として <allock-desc>に格納されているから
	  あたらしいXML要素は追加しない
	*/
	if(file_flags != 3){
	    UDF_Extent file_ext = (UDF_Extent) createElement("UDF_Extent", null, null);
	    int data_refno = 0;
	    int data_subno = 0;
	    try{
		UDF_AD[] ad = fe.getAD();
		if(ad != null && ad.length > 0) {
		    data_refno = fe.getAD()[0].getPartRefNo();
		}
		if(mirror && ad != null && ad.length > 0 && env.hasMirrorPartmap(data_refno)) {
		    file_ext.setPartSubno(1);
		    data_subno = 1;
		}

		if(ad != null && ad.length > 0 && env.isVirtualPartMap(fe.getAD()[0].getPartRefNo()))
		    file_ext.setPartSubno(subno);
	    }
	    catch(UDF_PartMapException e){
		e.printStackTrace();
	    }

	    fe.setADToExtent(file_ext, partno);	

	    if(file_ext.getLongSize() > 0){
		String ext_label = UDF_Label.genExtentLabel(file_ext);
		fe.getAllocDesc().setAttribute("ref", ext_label + ".extents");
		
		if(findById(ext_label) == null){
		    file_ext.setAttribute("id", ext_label);
		    try{
			UDF_Extent data_ext = (UDF_Extent)file_ext.duplicateElement();

			data_ext.blessExtent();
			//UDF_ExtentElem[] ee = data_ext.getExtent();
			UDF_ExtentElemList el = data_ext.getExtentElem();
			for(Iterator it=el.iterator(); it.hasNext() ; ){
			    //dataを表現するXML要素
			    UDF_Data file = (UDF_Data) createElement("UDF_Data", null, null);
			    //dataを入れるXML要素
			    //UDF_Extent file_ext2 = file_ext;
			    UDF_ExtentElem ee = (UDF_ExtentElem)it.next();
			    file.setSourceFile(env.image_file);
			    file.setSourceOffset(ee.loc * env.LBS);
			    file.setLength(ee.len);
			    file.setElemPartRefNo(data_refno);
			    file.setPartSubno(data_subno);
			    file_ext.appendChild(file);
			}
			
			this.appendChild(file_ext);
		    }
		    catch(UDF_InternalException e){
			e.printStackTrace();
		    }
		    catch(UDF_PartMapException e){
			e.printStackTrace();
		    }
		}
	    }
	}
	
    }
    /**
       parseTreeから呼ばれる関数。

       @deprecated replaced by {@link #parseTreeFile(UDF_Element, UDF_FEDesc, int, int)}
     */
    protected void parseTreeFile(UDF_Element mother, UDF_FEDesc fe, int partno, boolean mirror) throws IOException{
	parseTreeFile(mother, fe, partno, mirror ? 1 : 0);
    }
    private void setPkgPriority(){
	if(getClass().getName().equals("com.udfv.udf102.UDF_Image"))
	    setPkgPriority(0x102);
	else if(getClass().getName().equals("com.udfv.udf150.UDF_Image"))
	    setPkgPriority(0x150);
	else if(getClass().getName().equals("com.udfv.udf200.UDF_Image"))
	    setPkgPriority(0x200);
	else if(getClass().getName().equals("com.udfv.udf201.UDF_Image"))
	    setPkgPriority(0x201);
	else if(getClass().getName().equals("com.udfv.udf250.UDF_Image"))
	    setPkgPriority(0x250);
	else if(getClass().getName().equals("com.udfv.udf260.UDF_Image"))
	    setPkgPriority(0x260);
	else
	    setPkgPriority(0x260);
    }

    /**
       FEを入れる UDF_Extentを生成する

       @param lbn	LBN
       @param partno	partition reference number
       @param subno	副パーティション番号

       @see <a href="subno.html">副パーティション番号</a>
     */
    protected UDF_Extent createFEExtent(int lbn, int partno, int subno){
	UDF_Extent fe_ext = (UDF_Extent) createElement("UDF_Extent", null, null);
	//fe_ext.setMirror(mirror);
	fe_ext.setPartSubno(subno);
	fe_ext.addExtent(lbn, partno, env.LBS);
	fe_ext.setAttribute("id", UDF_Label.genFELabel(env, lbn, partno, subno));

	return fe_ext;
    }

    /**
       FEを入れる UDF_Extentを生成する

       @param lbn	LBN
       @param partno	partition reference number
       @param mirror	ミラーフラグ

       @deprecated replaced by {@link #createFEExtent(int, int, int)}

     */
    protected UDF_Extent createFEExtent(int lbn, int partno, boolean mirror){
	return createFEExtent(lbn, partno, mirror ? 1 : 0);
    }

    /**
       XML Documentを人間が可読な形で出力する。

       @deprecated replaced by {@link outputXML OutputStream, boolean)}
    */ 
    public void outputXML(){
	if(sa_ya == UDF_XMLUtil.SAYA_UME)
	    return;
        outputXML(env.sortxml);
    }

    /**
       XML Documentを人間が可読な形で出力する。
       @deprecated replaced by {@link outputXML(OutputStream, boolean)}
    */ 
    public void outputXML(boolean sortflag){
	if(sa_ya == UDF_XMLUtil.SAYA_UME)
	    return;

	createSystemXML();
	UDF_XMLUtil.output(my_doc, sortflag, env);
    }

    /**
       XML Documentを人間が可読な形で出力する。

       @param file	出力ファイル。
       @deprecated replaced by {@link outputXML OutputStream)}
    */ 
    public void outputXML(String file) {
	if(sa_ya == UDF_XMLUtil.SAYA_UME)
	    return;
	
	outputXML(file, env.sortxml);
    }

    /**
       XML Documentを人間が可読な形で出力する。

       @param file	出力ファイル。
       @deprecated replaced by {@link outputXML(OutputStream, boolean)}
    */ 
    public void outputXML(String file, boolean sortflag) {
	if(sa_ya == UDF_XMLUtil.SAYA_UME)
	    return;
	
	createSystemXML();
	try{
	    OutputStream os = new FileOutputStream(file);
	    UDF_XMLUtil.output(my_doc, os, sortflag, env);
	}
	catch(IOException e){
	    System.err.println("cannot open file:" + file);
	}
    }

    /**
       XML Documentを人間が可読な形で出力する。

       @param os	出力ストリーム
    */ 
    public void outputXML(OutputStream os) {
	if(sa_ya == UDF_XMLUtil.SAYA_UME)
	    return;
	
        outputXML(os, env.sortxml);
    }

    /**
       XML Documentを人間が可読な形で出力する。

       @param os	出力ストリーム
    */ 
    public void outputXML(OutputStream os, boolean sortflag) {
	if(sa_ya == UDF_XMLUtil.SAYA_UME)
	    return;
	createSystemXML();
	setAttribute("revision", env.recorded_udf_revision);
	UDF_XMLUtil.output(my_doc, os, sortflag, env);
    }

    /**
       XMLドキュメントから UDF Imageを構築します。
       
       @param docnode XMLドキュメント
    */
    final public void readFromXML(Node docnode) throws UDF_Exception{
	Document input_doc = (Document)docnode;
	Node input_root_node = input_doc.getElementsByTagName("udf").item(0);

	env.clearPartMap();
	env.clearPartBitmap();
	env.clearPartMapBitmap();

	//input_docを参考に新規 XMLツリーを作る必要があるので、
	//ここで 新規 Documentを newする。

	readSystemXML(input_doc);
	env.f = new UDF_RandomAccessZero(env.image_size);


	setupNode(UDF_Util.genDocument());

	setProgressStatus(10);

	NodeList nl = input_root_node.getChildNodes();

	if(nl == null){
	    throw new UDF_InternalException(this, "Not Found UDF_Element.");
	}

	for(int i = 0; i < nl.getLength(); ++i){

	    Node child = nl.item(i);

	    if(child.getNodeType() != Node.ELEMENT_NODE) {
		continue;
	    }

	    String tag_name = child.getLocalName();
	    
	    if(tag_name.equals(UDF_XML.VRS)){
		UDF_VRS ext = (UDF_VRS) createElement("UDF_VRS", "UDF_Image", tag_name);
		ext.readFromXML(child);
		appendChild(ext);
		if(env.vrs1 == null)
		    env.vrs1 = ext;
	    }
	    else if(tag_name.equals(UDF_XML.AVDP)){
		UDF_Extent ext = (UDF_Extent) createElement("UDF_Extent", "UDF_Image", tag_name);
		ext.readFromXML(child);
		appendChild(ext);
	    }
	    else if(tag_name.equals(UDF_XML.IS)){
		UDF_Extent ext = (UDF_Extent) createElement("UDF_IS", "UDF_Image", tag_name);
		ext.readFromXML(child);
		appendChild(ext);
	    }
	    else if(tag_name.equals(UDF_XML.MVDS)){
		UDF_VDS ext = (UDF_VDS) createElement("UDF_VDS", "UDF_Image", tag_name);
		ext.readFromXML(child);
		appendChild(ext);
	    }
	    else if(tag_name.equals(UDF_XML.RVDS)){
		UDF_VDS ext = (UDF_VDS) createElement("UDF_VDS", "UDF_Image", tag_name);
		ext.readFromXML(child);
		appendChild(ext);
	    }
	    else if(tag_name.equals(UDF_XML.FSDS)) {
		UDF_Extent ext = (UDF_Extent) createElement("UDF_FDS", "UDF_Image", tag_name);
		ext.readFromXML(child);
		appendChild(ext);
	    }
	    else if(tag_name.equals(UDF_XML.UDF_EXTENT)) {
		UDF_Extent ext = (UDF_Extent) createElement("UDF_Extent", "UDF_Image", tag_name);
		ext.readFromXML(child);
		appendChild(ext);
	    }
	    else if(tag_name.equals(UDF_XML.DIRECTORY)) {
		UDF_Extent ext = (UDF_Extent) createElement("UDF_Directory", "UDF_Image", tag_name);
		ext.readFromXML(child);
		appendChild(ext);
	    }
	    else if(tag_name.equals(UDF_XML.SDIRECTORY)) {
		UDF_Extent ext = (UDF_Extent) createElement("UDF_SDirectory", "UDF_Image", tag_name);
		ext.readFromXML(child);
		appendChild(ext);
	    }
	    else if(tag_name.equals("UDF_VDS")) {
		UDF_VDS vds = (UDF_VDS) createElement("UDF_VDS", null, null);
		vds.readFromXML(child);
		appendChild(vds);
	    }
	    else if(tag_name.equals("UDF_FDS")) {
		UDF_FDS fds = (UDF_FDS) createElement("UDF_FDS", null, null);
		fds.readFromXML(child);
		appendChild(fds);
	    }
	    else if(tag_name.equals("UDF_IS")) {
		UDF_IS is = (UDF_IS) createElement("UDF_IS", null, null);
		is.readFromXML(child);
		appendChild(is);
	    }
	    else if(tag_name.equals("system")) {
	    }
	    else if(tag_name.equals("partition-info")) {
	    }
	    else if(tag_name.equals("partition-map-info")) {
	    }
	    else if(tag_name.equals("partition")) {//for old-version compatible
	    }
	    else{
		System.err.println("Error: Unknown XML tag " + tag_name);
		throw new UDF_InternalException(null, "Error: Unknown XML tag " + tag_name);
	    }
	}

	setProgressStatus(50);

	try{
	    UDF_VDS mvds = (UDF_VDS)findByXPATH("/udf/mvds");
	    env.mvds = mvds;
	    UDF_VDS rvds = (UDF_VDS)findByXPATH("/udf/rvds");
	    env.rvds = rvds;

	    //desc3がある場合を考慮し、VDSのリストを生成する
	    recalc_core(RECALC_VDSLIST, env.f);
	    // XML のヘッダ情報である<partition> ノードを作成する
	    recalc_core(RECALC_ENV, env.f);
	    postReadFromXMLHook(docnode);

	    setProgressStatus(80);
	    recalc_core(RECALC_PARTMAP, env.f);
	    recalcSB(false);

	    setProgressStatus(90);

	    createPartElement();
	}
	catch(Exception e){
	    e.printStackTrace();
	}

	setProgressStatus(100);
    }

    /**
       イメージファイルのリビジョンをチェックする。
       
       @param f アクセサ
     */
    public int checkUDFRevision(UDF_RandomAccess f)  throws IOException, UDF_Exception, ClassNotFoundException{
	// 256 => N-256 => N-1 の順にAVDP を読む。1つでも読めたらおしまい
	if(!readAVDPinVR(f)){
	    System.err.println("no AVDP");
	    return 0x0;
	}
	    
	if(!readMVDS(f)){
	    
	    if(!readRVDS(f)){
		System.err.println("no MVDS nor RVDS");
		return 0x0;
	    }
	}
	//System.err.println("UDF revision: " + env.recorded_udf_revision);

	debugMsg(3, "UDF revision: " + env.recorded_udf_revision);
	
	if(env.getISLoc() == 0){
	    System.err.println("no IS");
	    return 0x0;	// UNKNWON;
	}

	return readUDFRevision();//env.recorded_udf_revision;
    }

    /**
        XMLからリビジョンを読み取る。

       UDF Element から Implementation Use Volume Descriptor を探し、
       ImplmentationIdentifier からリビジョンを取り出す。
    */
    public int readUDFRevision( ) throws UDF_Exception {
	
	UDF_VDS vds = (env.mvds != null) ? env.mvds : env.rvds;

	try{
	    UDF_ElementList desc4_list = vds.getImplUseVolDesc();

	    UDF_desc4 desc4 = (UDF_desc4)desc4_list.elementAt(0);
	    return UDF_Util.b2uint16(desc4.getImplId().getIdSuffix().getData(), 0);
	}
	catch(UDF_VolException e){
	    e.printStackTrace();
	    try{
		UDF_ElementList desc6_list = vds.getPrevailingLogicalVolDesc();
		UDF_desc6 desc6 = (UDF_desc6)desc6_list.elementAt(0); // 1つ目決めうち
		
		return UDF_Util.b2uint16(desc6.getDomainId().getIdSuffix().getData(), 0);
	    }
	    catch(UDF_VolException e2){
		
		throw new UDF_InternalException(this, "CAN'T READ REVISION : can't find Implementation Use Volume Descriptor");
	    }
        }
    }

    /**
       そこにあるデスクリプタを単純に1つ読みこみ、子供として追加する。
     */
    public void readFromDesc(UDF_RandomAccess f, int n) throws IOException, UDF_Exception, ClassNotFoundException{
	UDF_Extent ext = createExtent(null, null);
	ext.addExtent((int)(f.getPointer()/env.LBS), -1, env.LBS);
	while(n > 0){
	    UDF_CrcDesc dsc = UDF_CrcDesc.genCrcDesc(f, this, null, null);
	    dsc.readFrom(f);
	    ext.appendChild(dsc);
	    --n;
	}
	appendChild(ext);
    }

    /**
       イメージファイルから UDF Imageを構築する。
       
       @param f イメージファイル
    */
    public long readFrom(UDF_RandomAccess f) throws UDF_Exception, IOException{
	
	if(sa_ya == UDF_XMLUtil.SAYA_UME)
	    throw new UDF_NotImplException(null, "\nSorry, but this version of udfv is not supported to read image file.\n");
	
	debugMsg(3, "readFrom: " + getClass().getName());

	env.f = f;

	env.anchorVolDescPointer = new UDF_desc2[3];
	env.mvds = null;
	env.rvds = null;
	env.is = null;
	env.clearFDS();
	env.image_size = f.length();
	env.image_total_sec = f.length() / env.LBS;

	// PartitionMap関連のアクセサをスッキリ
	env.clearPartMap();
	// Partition関連のビットマップをスッキリ
	env.clearPartBitmap();
	// PartitiomMap依存のビットマップをスッキリ
	env.clearPartMapBitmap();
	
	setProgressStatus(0);

	readVolume(f);
	setProgressStatus(10);

	try{
	    if(env.hasMetadataPartMap()){
		if(env.has_metadata_partmap_main)
		    readFileSystem(f, false);
	    }
	    else{
		try{
		    readFileSystem(f, false);
		}
		catch(UDF_VolException e){
		    //mainがぶっこわれている場合は無視
		}
	    }
	    
	    setProgressStatus(40);
	    try{
		if(env.has_metadata_partmap_mirror)
		    readFileSystem(f, true);
	    }
	    catch(UDF_VolException e){
		//mirrorがぶっこわれている場合は無視
	    }

	}
	catch(ClassNotFoundException e){
	    e.printStackTrace();
	}

	if(env.hasVirtualPartMap()){
	    try{
		for(int i=1 ; ; ++i){
		    readFileSystem(f, i);
		}
	    }
	    catch(IOException e){
		e.printStackTrace();
	    }
	    catch(UDF_PartMapException e){
		;//深く辿りすぎた
	    }
	    catch(ClassNotFoundException e){
		e.printStackTrace();
	    }
	    catch(UDF_VolException e){
		e.printStackTrace();
	    }
	    catch(UDF_Exception e){
		e.printStackTrace();
	    }
	}

	setProgressStatus(70);
	
	recalc(RECALC_PARTMAP, f);
	recalc(RECALC_TREE, f);

	setProgressStatus(80);

	//prepareSpaceBitmap(f);

	recalcSB(false);

	setProgressStatus(90);

	if (env.udf_revision < 1) {
	    
	    try{
		env.udf_revision = readUDFRevision();
	    }
	    catch(Exception e){
		
		e.printStackTrace();
	    }
	}
	
	createPartElement();
	setProgressStatus(100);
	
	return 0;
    }

    public long writeTo(UDF_RandomAccess f) throws UDF_Exception, IOException {

	env.f = f;

	UDF_ElementBase[] children = getChildren();

	setProgressStatus(0);
	for(int i=0 ; i<children.length ; ++i){
	    setProgressStatus((int)(i * 100.0 / children.length));

	    debugMsg(3, "++++> " + children[i].getName() +".writeTo(f) : " + f.getAbsPointer());
	    children[i].writeTo(f);
	}
	while(f.length() < env.image_size){
//	    f.seek(0, UDF_RandomAccess.SEEK_END);
//	    f.writeUint8(0);
	    f.seek(env.image_size - 1, UDF_RandomAccess.SEEK_SET);
	    f.writeUint8(0);
	}
	setProgressStatus(100);
	return 0;
    }
    
    public long getLongSize(){
	try{
	    if(env == null)
		throw new UDF_InternalException(this, "UDF_Image.getLongSize(): No Environment");
	    else if(env.f == null)
		throw new UDF_InternalException(this, "UDF_Image.getLongSize(): No RandomAccessFile");
	    return env.f.length();
	}
	catch(Exception e){
	    e.printStackTrace();
	}
	return 0;
    }
    public int getSize(){
	try{
	    throw new UDF_NotImplException(this, "UDF_Image.getSize(): Use getLongSize() instead");
	}
	catch(Exception e){
	    e.printStackTrace();
	}
	return 0;
    }
        
    public void debug(int indent){
	//uzaiので削除
	/*
	  UDF_ElementBase[] children = getChildren();
	for(int i=0 ; i<children.length ; ++i){
	    children[i].debug(indent+1);
	}
	*/
	
    }


    //_/_/ Protected /_/_/

    
    /**
       createElement を用いて、UDF_Extent インスタンスを生成する。
       
       @param prefix  現状では使用されない。null を指定する。
       @param name    現状では使用されない。null を指定する。
       
       @return 生成されたUDF_Extent インスタンスを返す。
    */
    protected UDF_Extent createExtent(String prefix, String name){
	return (UDF_Extent) createElement("UDF_Extent", null, null);
    }
    
    /**
       UDF イメージを表現するXML のルートノードエレメントを取得する。
       
       @return ルートノードエレメントのインスタンス。
    */
    protected Element  getRootNode() { return root_node; }
    
    /**
      XML Nodeから指定のUDF_Extentに XML Element "partition"の内容を読み取る。
      @param part 書き込み先
      @param n XML Node
    */
    private static void readNodeOfExtentOfPartitionFromXML(UDF_Extent part, Node n) throws UDF_Exception {
	part.readExtentFromXML(n);

	NodeList nl = n.getChildNodes();
	if(nl == null)
	    return;
	if(nl.getLength() < 2)
	    return;

	for(int i = 0; i < nl.getLength(); ++i) {

	    Node child = nl.item(i);
	    if(child.getNodeType() != Node.ELEMENT_NODE){
	        continue;
	    }

	    String tagName = child.getLocalName();
	    String className = child.getLocalName();

	    if(className.equals("extents"))
		continue;

	    if (className.equals(UDF_XML.PARTITION)) {

		String partno = child.getAttributes().getNamedItem("partno").getNodeValue();

		UDF_Extent ext = (UDF_Extent) part.createElement("UDF_Extent", "UDF_Image", tagName);
		ext.readFromXML(child);
		ext.setAttribute("partno", partno);
		part.appendChild(ext);
		continue;
	    }
	    else {
		throw new UDF_XMLException(part, "XML: Unknown Element:" + className);
	    }
	}
    }
    public void readPartMap() throws IOException, UDF_Exception, ClassNotFoundException{
    }

    /**
       <a>123</a>のようになっている場合の子供の(最初の)Textノードを取得する。
     */
    private String getNodeVal(Node node){
	if(marimite && node != null){
	    NodeList list = node.getChildNodes();
	    if(list.item(0).getNodeType() == Node.TEXT_NODE)
		return list.item(0).getNodeValue();
	}
	return "";
    }
    /**
       <a><hoge></hoge></a>のようになっている場合の(最初の)ノードを取
       得する。
     */
    private Element getFirstElem(Node node){
	if(marimite && node != null){
	    NodeList list = node.getChildNodes();
	    for(int i=0 ; i<list.getLength() ; ++i){
		if(list.item(i).getNodeType() == Node.ELEMENT_NODE)
		    return (Element)list.item(i);
	    }
	}
	return null;
    }

    private void readPartMapInfo2(Element partmap) throws UDF_Exception{
	int partno = Integer.parseInt(partmap.getAttribute("partno"));
	NodeList partmaplist = partmap.getChildNodes();

	String type = "";
	int k=0;
	for( ; k< partmaplist.getLength(); ++k){
	    if(partmaplist.item(k).getNodeType() != Node.ELEMENT_NODE)
		continue;
	    Element elem = (Element)(partmaplist.item(k));
	    if(elem.getLocalName().equals("pm-type")){
		type = getNodeVal(elem);
		break;
	    }
	}
	if(type.equals("Type1 Partition Map")){
	    for( ; k< partmaplist.getLength(); ++k){
		if(partmaplist.item(k).getNodeType() != Node.ELEMENT_NODE)
		    continue;
		Element elem = (Element)(partmaplist.item(k));
		if(elem.getLocalName().equals("UDF_Extent")){
		    UDF_Extent extent = (UDF_Extent)createElement("UDF_Extent", null, null);
		    extent.readFromXML(elem);
		    //extent.debug(0);

		    env.setPartMapExtent(partno, 0, extent);
		    env.setPartMapRandomAccess(partno, 0, extent.genRandomAccessExtent());
		    debugMsg(1, "TP1: setPartMapExtent: " + partno);
		}
	    }
	}
	else if(type.equals("Sparable Partition Map")){
	    for( ; k< partmaplist.getLength(); ++k){
		if(partmaplist.item(k).getNodeType() != Node.ELEMENT_NODE)
		    continue;
		Element elem = (Element)(partmaplist.item(k));
		if(elem.getLocalName().equals("UDF_Extent")){
		    UDF_Extent extent = (UDF_Extent)createElement("UDF_Extent", null, null);
		    extent.readFromXML(elem);

		    env.setPartMapExtent(partno, 0, extent);
		    env.setPartMapRandomAccess(partno, 0, extent.genRandomAccessExtent());
		    debugMsg(1, "SPM: setPartMapExtent: " + partno);
		}
	    }
	}
	else if(type.equals("Virtual Partition Map")){
	    for( ; k< partmaplist.getLength(); ++k){
		if(partmaplist.item(k).getNodeType() != Node.ELEMENT_NODE)
		    continue;
		Element elem = (Element)(partmaplist.item(k));

		if(elem.getLocalName().equals("vat")){
		    int vatno = Integer.parseInt(elem.getAttribute("vatno"));
		    UDF_Extent extent = (UDF_Extent)createElement("UDF_Extent", null, null);
		    extent.readFromXML(getFirstElem(elem));

		    env.setPartMapExtent(partno, vatno, extent);
		    env.setPartMapRandomAccess(partno, vatno, extent.genRandomAccessExtent());
		    debugMsg(1, "VPM: setPartMapExtent: " + partno + ":" + vatno);
		}
	    }
	}
	else if(type.equals("Metadata Partition Map")){
	    for( ; k< partmaplist.getLength(); ++k){
		if(partmaplist.item(k).getNodeType() != Node.ELEMENT_NODE)
		    continue;
		Element elem = (Element)(partmaplist.item(k));
		if(elem.getLocalName().equals("metadata")){
		    int subno;
		    String mirror_s = elem.getAttribute("mirror");
		    if(mirror_s.equals("yes"))
			subno = 1;
		    else
			subno = 0;
		    
		    UDF_Extent extent = (UDF_Extent)createElement("UDF_Extent", null, null);
		    //extent.debug(0);
		    extent.readFromXML(getFirstElem(elem));

		    //extent.debug(0);
		    env.setPartMapExtent(partno, subno, extent);
		    env.setPartMapRandomAccess(partno, subno, extent.genRandomAccessExtent());
		    debugMsg(1, "MPM: setPartMapExtent: " + partno + ":" + subno);
		}
	    }
	}
    }
    /*
       XML の先頭で定義されているパーティションマップ情報を取得する。
       
       @param partition <partition> エレメントを指すノードインスタンス。
       @param parttype    true を指定すると、ミラーの情報のみ取得する。
    */
    private void readPartInfo2(Node partition) throws UDF_Exception{
	NodeList partlist = partition.getChildNodes();
	for(int j = 0; j < partlist.getLength(); j++){
	    if(partlist.item(j).getNodeType() != Node.ELEMENT_NODE)
		continue;
	    Element partmap = (Element)(partlist.item(j));

	    readPartMapInfo2(partmap);
	}
    }

    /*
       XML の先頭で定義されているパーティションマップ情報を取得する。
       
       @param partition <partition> エレメントを指すノードインスタンス。
       @param parttype    true を指定すると、ミラーの情報のみ取得する。

    private UDF_Extent[] readPartitionInfo(Node partition, int parttype) throws UDF_XMLException{
	
	NodeList partmaplist = partition.getChildNodes();
	Vector vec = new Vector();
	
	
	for(int j = 0; j < partmaplist.getLength(); j++){  // パーティションマップのリスト(<partmap>)
	    
	    if(partmaplist.item(j).getNodeType() != Node.ELEMENT_NODE)
		continue;
	    
	    Element  elem = (Element)(partmaplist.item(j));
	    Integer  partno = Integer.decode(elem.getAttribute("partno"));
	    
	    UDF_Extent[] tmpext = readPartMapInfo(elem, partno, parttype);
	    if(tmpext.length != 0)
		vec.add(tmpext);
	}
	

	UDF_Extent[] part_e = null;
	
	if(parttype == 0){
	    
	    part_e = new UDF_Extent[vec.size()];

	    for(int i = 0; i < vec.size(); i++){
		
		UDF_Extent[] array = (UDF_Extent[])vec.elementAt(i);
		part_e[i] = array[0];
	    }
	}
	else if(vec.size() != 0){
	    
	    UDF_Extent[] tmp = (UDF_Extent[])vec.elementAt(0);
	    part_e = new UDF_Extent[tmp.length];
	    
	    for(int i = 0; i < tmp.length; i++){
	    
		part_e[i] = tmp[i];
	    }
	}
	
	return part_e;
    }
    */
    /**
       XML の先頭で定義されているパーティションマップのエクステントを取得する。
       
       @param partmap パーティションマップを表すエレメント（<partmap>)。
       @param partno  パーティションマップ番号。
       @param partype
       1 を指定すると、Metadata Partition のMirror 情報のみを取得する。
       Metadata Partition 以外のパーティションマップのエクステント情報は、null が格納される。
       2 を指定すると、Virtual Partition のエクステントのみを取得する。
       0 を指定すると、上記以外のエクステントを取得する。
    */
    private UDF_Extent[] readPartMapInfo(Element partmap, Integer partno, int parttype){
	
	NodeList pmchild = partmap.getChildNodes();
	Vector vec = new Vector();
	int partmaptype = -1;
	
	
	for(int i = 0; i < pmchild.getLength(); i++){
	    
	    if(pmchild.item(i).getNodeType() != Node.ELEMENT_NODE)
		continue;
	    
	    Element elem = (Element)(pmchild.item(i));
	    String  nodename = elem.getNodeName();
	    
	    
	    if(parttype == 0){
		
		UDF_Extent ext = readUDFExtent(elem);
		if(ext != null)
		    vec.add(ext);
	    }
	    
	    if(nodename.equals("vat")){
		
		NodeList vatchild = elem.getChildNodes();
		for(int j = 0; j < vatchild.getLength(); j++){
		    
		    if(pmchild.item(j).getNodeType() != Node.ELEMENT_NODE)
			continue;
		    
		    if(parttype == 0){
			
			// 0 のときは、最新のVAT のみ取得する
			if(elem.getAttribute("vatno").equals("0")){
			    
			    UDF_Extent ext = readUDFExtent((Element)vatchild.item(j));
			    if(ext != null)
				vec.add(ext);
			}
		    }
		    else if(parttype == 2){
			
			UDF_Extent ext = readUDFExtent((Element)vatchild.item(j));
			if(ext != null)
			    vec.add(ext);
		    }
		}
	    }
	    else if(nodename.equals("metadata")){
		
		NodeList metachild = elem.getChildNodes();
		for(int j = 0; j < metachild.getLength(); j++){
		    
		    if(metachild.item(j).getNodeType() != Node.ELEMENT_NODE)
			continue;
		    
		    if(parttype == 0){
			
			// 0 のときは、MetadataMain のみ取得する
			if(!elem.getAttribute("mirror").equals("yes")){
			    
			    UDF_Extent ext = readUDFExtent((Element)metachild.item(j));
			    if(ext != null)
			    vec.add(ext);
			}
		    }
		    else if(parttype == 1){
			
			if(elem.getAttribute("mirror").equals("yes")){
			    
			    // Mirror のときは、Metadata Partition 以外の
			    // パーティションマップは、null として保持しておかなければならない
			    for(int k = 0; k < partno.intValue(); k++)
				vec.add(null);
			    
			    UDF_Extent ext = readUDFExtent((Element)metachild.item(j));
			    if(ext != null)
				vec.add(ext);
			}
		    }
		}
	    }
	}
	
	UDF_Extent[] part_e = new UDF_Extent[vec.size()];
	for(int i = 0; i < vec.size(); i++){
	    
	    part_e[i] = (UDF_Extent)vec.elementAt(i);
	}
	
	return part_e;
    }
    
    /**
       Element からUDF_Extent 情報を生成し、取得する。
       このメソッドは、readPartMapInfo() からのみコールされる。
       
       @param UDF_Extent エレメント。
       @return UDF_Extent インスタンス。
    */
    private UDF_Extent readUDFExtent(Element udfextent){
	
	String  nodename = udfextent.getNodeName();
	if(nodename.equals("UDF_Extent")){
	    
	    UDF_Extent ext = (UDF_Extent) createElement("UDF_Extent", "UDF_Image", "UDF_Extent");
	    
	    try{
		ext.readFromXML(udfextent);
	    }
	    catch(Exception e){
		
		e.printStackTrace();
	    }
	    
	    return ext;
	}
	
	return null;
    }
    
    /**
       NodeからUDF&nbsp;パーティションの領域情報を読み取り、UDF&nbsp;環境&nbsp;(UDF_Env)に情報を複写する。<br>
       <em>
       ※注意事項<br>
       &nbsp;Metadata&nbsp;Mirror&nbsp;File&nbsp;が指し示す領域を読み取るが、
       パーティションマップ番号０以外に存在しないパーティションマップ番号が
       間に存在したときは&nbsp;Exception&nbsp;が発行される。<br>
       例）パーティションマップのミラーが１と３に存在していた場合。現状ではありえない。
       </em>

       @param child パーティションの領域情報。
       @param child_tag_name パーティションの領域情報を表すタグ名。
    */
    protected UDF_Extent [] readExtentOfPartitionFromXML(Node child, String child_tag_name) throws UDF_Exception {

	//　パーティションの領域情報を読み込みます　//

	UDF_Extent part = (UDF_Extent) createElement("UDF_Extent", "UDF_Image", child_tag_name);

	readNodeOfExtentOfPartitionFromXML(part, child);
	appendChild(part);

	//　パーティションの領域情報をアクセス用に複写します　//

	UDF_ElementBase [] elem = part.getChildren();

	//　配列リストを作成　//
	ArrayList partinfo = new ArrayList(2);
	partinfo.add(0, null);

	for (int i = 0, max = elem.length; i < max; i++) {

	    if(!elem[i].getName().equals(UDF_XML.PARTITION)) {
		continue;
	    }

	    //　属性：パーティション番号を読み取ります　//
	    NamedNodeMap map = elem[i].getNode().getAttributes();
	    String v = map.getNamedItem("partno").getNodeValue();
	    int partno = 0;
	    if (!v.equals("")) {
	        partno = Integer.parseInt(v);
	    }

	    //　パーティションの情報要素を複写します　//
	    UDF_Extent ext = (UDF_Extent) createElement("UDF_Extent", "UDF_Image", "UDF_Extent");
	    ext.copyExtent((UDF_Extent)elem[i]);

	    //　配列に追加しておきます　//
	    try {
		partinfo.set(partno, ext);
	    }
	    catch(IndexOutOfBoundsException e) {
		partinfo.add(partno, ext);
	    }
	}

	//　UDF_Extentの配列として出力します　//
	UDF_Extent [] part_e = new UDF_Extent[partinfo.size()];
	part_e = (UDF_Extent [])partinfo.toArray(part_e);

	return part_e;
    }

    /**
       <UDF_desc257>+ を読む。

       @param mother	親
       @param parent_fe	親FE
       @param elem	FIDを内部にもつ UDF_Extent
       @param subno	subno
       @param sstream	sstreamかどうか

       PARENTもしくは DELETEDフラグがついていない場合はその先を読む。
       ただし、link等で既に構築している FEは辿らない。
    */
    protected void parseDir(UDF_Element mother, UDF_FEDesc parent_fe, UDF_Element elem, int subno, boolean sstream) throws ClassNotFoundException, UDF_Exception, IOException{
	UDF_ElementBase[] el = elem.getChildren();
	for(int i=0 ; i<el.length ; ++i){
	    
	    if(com.udfv.ecma167.UDF_Directory.class.isAssignableFrom(el[i].getClass())){
		parseDir(mother, parent_fe, (UDF_Element)el[i], subno, sstream);
		continue;
	    }
	    else if(!com.udfv.ecma167.UDF_desc257.class.isAssignableFrom(el[i].getClass())){
		continue;
	    }


	    UDF_desc257 d = (UDF_desc257)el[i];
	    int filechar = d.getFileChar().getIntValue();
	    long len = d.getICB().getLen();
	    int lbn = d.getICB().getLbn();
	    int partno = d.getICB().getPartRefNo();

	    if((filechar & UDF_desc257.DELETED) != 0)
		continue;

	    String label = UDF_Label.genFELabel(env, lbn, partno, subno);
	    d.getICB().getExtentLen().setAttribute("ref", label + ".len");
	    d.getICB().getExtentLoc().getLogicalBlockNumber().setAttribute("ref", label + ".lbn");
	    d.getICB().getExtentLoc().getPartReferenceNumber().setAttribute("ref", label + ".partno");

	    if((filechar & UDF_desc257.PARENT) != 0){
		continue;
	    }
	    else{
		UDF_Extent ref_ext = (UDF_Extent)findById(label);
		UDF_FEDesc ref_fe = null;
		//System.err.println("lbn="+lbn);
		//System.err.println("partno="+partno);
		if(ref_ext == null){
		    UDF_Extent ext = parseTree(mother, lbn, partno, subno == 1 ? true : false, sstream);
		    //extはFEが1つしか入っていないので最初の子供がこのFIDが差す子供である。
		    ref_fe = (UDF_FEDesc)(ext.getFirstChild());
		}
		else{
		    //extはFEが1つしか入っていないので最初の子供がこのFIDが差す子供である。
		    ref_fe = (UDF_FEDesc)(ref_ext.getFirstChild());
		}
		if((ref_fe.getICBTag().getFlags().getIntValue() & 0x2000) == 0){
		    /*
		      UDF2.60 2.2.6.4
		      
		      Number of files:
		      The current number of files in the Logical Volume, including hard links.
		      The count includes all FIDs in the directory hierarchy for which the
		      Directory Bit, Parent Bit and Deleted Bit are all ZERO....

		      Number of directory:
		      The count includes the root directory and ALL FIDs in the directory
		      hierarchy for which the Directory bit is ONE and the Parent bit and
		      Deleted bit are both ZERO....

		      とあるので、filecharだけを見て、どちらをカウントするか決める。
		     */
		    
		    // Metadata Main が正しく読み込めていれば、mirror でないエレメントの
		    // ディレクトリ数とファイル数をカウントする。
		    // そうでない場合、mirror なエレメントのディレクトリ数とファイル数をカウントする。
		    boolean mirror = (subno == 0 ? false : true);
		    if(!env.hasMetadataPartMap() || env.has_metadata_partmap_main){
			if(!mirror){
			    if((filechar & UDF_desc257.DIRECTORY) != 0){
				++env.recorded_num_directories;
			    }
			    else
				++env.recorded_num_files;
			}
		    }
		    else if(env.has_metadata_partmap_mirror && mirror){
			if((filechar & UDF_desc257.DIRECTORY) != 0){
			    ++env.recorded_num_directories;
			}
			else
			    ++env.recorded_num_files;
		    }
		}
	    }
	}
    }

    /**
       <UDF_desc257>+ を読む。

       @param mother	親
       @param parent_fe	親FE
       @param elem	FIDを内部にもつ UDF_Extent
       @param mirror	mirrorかどうか
       @param sstream	sstreamかどうか

       PARENTもしくは DELETEDフラグがついていない場合はその先を読む。
       ただし、link等で既に構築している FEは辿らない。

       @deprecated replaced by {@link #parseDir(UDF_Element, UDF_FEDesc, UDF_Element, int, boolean)}
    */
    protected void parseDir(UDF_Element mother, UDF_FEDesc parent_fe, UDF_Element elem, boolean mirror, boolean sstream) throws ClassNotFoundException, UDF_Exception, IOException{
	parseDir(mother, parent_fe, elem, mirror ? 1 : 0, sstream);

    }
    
    /**
       <UDF_Extent> の子供である<extents> エレメントを作成する。
       
       @param part  エクステント情報が格納されたUDF_Extent。
       @return Element インスタンス。
    */
    protected Element createExtentsElement(UDF_Extent part){
	
	Element extents = my_doc.createElement("extents");
	
	//書くときだけでなく、readFromでも convertしたいので
	//別メソッドをつくってみた。
	try{
	    part.blessExtent();
	}
	catch(UDF_Exception e){
	    e.printStackTrace();
	}

	//UDF_ExtentElem[] partmapinfo_sec = part.getExtent();
	UDF_ExtentElemList eel = part.getExtentElem();
	    
	// patmapinfo_sec.length は１であるはず
	//for(int j = 0; j < partmapinfo_sec.length; j++){
	for(Iterator it = eel.iterator() ; it.hasNext() ; ){
	    UDF_ExtentElem ee = (UDF_ExtentElem)it.next();
	    Element extent = my_doc.createElement("extent");
	    
	    extent.setAttribute("len", String.valueOf(ee.len));
	    extent.setAttribute("off", String.valueOf(ee.off));
	    extent.setAttribute("sec", String.valueOf(ee.loc));
	    
	    // <extents> <- <extent>
	    extents.appendChild(extent);
	}

	return extents;
    }

    /**
       XML のヘッダ部、<system> に続く<partition> エレメントを作成する。
    */
    public void createPartElement2() throws UDF_VolException, UDF_PartMapException{
	if(!marimite)
	    return;

	final String PARTMAP = "partmap";
	//final String PMTYPE  = "pm-type";
	final String UDF_EXTENT = "UDF_Extent";
	final String METADATA = "metadata";
	final String VAT = "vat";
	
	while(my_partmap.hasChildNodes())
	    my_partmap.removeChild(my_partmap.getFirstChild());


	for(int i = 0; i < env.getPartMapList().size() ; i++){
	    
	    Element partmap = my_doc.createElement(PARTMAP);
	    
	    
	    partmap.setAttribute("partno", String.valueOf(i));
	    my_partmap.appendChild(partmap);
	    
	    
	    if(com.udfv.ecma167.UDF_part_map1.class.isAssignableFrom(env.part[i].getClass())){
		debugMsg(1, i + ":Type1 Partition Map");
		//tpmtype = my_doc.createTextNode("Type1 Partition Map");
		//Element udf_extent = my_doc.createElement(UDF_EXTENT);
		
		partmap.setAttribute("type", "UDF_type1");
		//partmap.appendChild(udf_extent);
		//udf_extent.appendChild(createExtentsElement(env.part_e[i]));
		partmap.appendChild(createExtentsElement(env.getPartMapExtent(i, 0)));
		
	    }
	    else if(com.udfv.udf150.UDF_SparablePartMap.class.isAssignableFrom(env.part[i].getClass())){
		debugMsg(1, i + ":Sparable Partition Map");

		//tpmtype = my_doc.createTextNode("Sparable Partition Map");
		//Element udf_extent = my_doc.createElement(UDF_EXTENT);
		
		partmap.setAttribute("type", "UDF_SparablePartMap");
		//partmap.appendChild(udf_extent);
		partmap.appendChild(createExtentsElement(env.getPartMapExtent(i, 0)));
	    }
	    else if(com.udfv.udf250.UDF_MetadataPartMap.class.isAssignableFrom(env.part[i].getClass())){
		debugMsg(1, i + ":Metadata Partition Map");

		//Element udf_extent = my_doc.createElement(UDF_EXTENT);

		partmap.setAttribute("type", "UDF_MetadataPartMap");
		partmap.appendChild(createExtentsElement(env.getPartMapExtent(i, 0)));
		// has mirror?
		if(env.getPartMapExtent(i, 1) != null)
		    partmap.appendChild(createExtentsElement(env.getPartMapExtent(i, 1)));
	    }
	    else if(com.udfv.udf150.UDF_VirtualPartMap.class.isAssignableFrom(env.part[i].getClass())){
		debugMsg(1, i + ":Virtual Partition Map");
		partmap.setAttribute("type", "UDF_VirtualPartMap");
		try{
		    for(int j = 0; ; j++){
			UDF_Extent vat_ext = env.getPartMapExtent(i, j);
			//Element vat = my_doc.createElement(VAT);
			//Element udf_extent = my_doc.createElement(UDF_EXTENT);
		    
			//vat.setAttribute("vatno", String.valueOf(j));
			//partmap.appendChild(vat);
			//partmap.appendChild(udf_extent);
			partmap.appendChild(createExtentsElement(vat_ext));
		    }
		}
		catch(UDF_PartMapException e){
		    ;//errorになったら抜ける
		}
	    }
	    else if(com.udfv.ecma167.UDF_part_map2.class.isAssignableFrom(env.part[i].getClass())){
		
		//tpmtype = my_doc.createTextNode("Type2 Partition Map");
		//Element udf_extent = my_doc.createElement(UDF_EXTENT);
		
		//partmap.appendChild(udf_extent);
		partmap.appendChild(createExtentsElement(env.getPartMapExtent(i, 0)));
	    }
	    else{
		
		;// pmtype.setNodeValue("Unknown Partition Map");
	    }
	    
	    // <pm-type> に値を設定
	    //epmtype.appendChild(tpmtype);
	}
    }
    
    /**
       XML のヘッダ部、<system> に続く<partition> エレメントを作成する。
    */
    public void createPartElement() throws UDF_VolException, UDF_PartMapException{
	createPartElement2();
	if(true)
	    return;

	if(!marimite)
	    return;
	final String PARTMAP = "partmap";
	final String PMTYPE  = "pm-type";
	final String UDF_EXTENT = "UDF_Extent";
	final String METADATA = "metadata";
	final String VAT = "vat";
	

	while(my_partition.hasChildNodes())
	    my_partition.removeChild(my_partition.getFirstChild());


	for(int i = 0; i < env.getPartMapList().size() ; i++){
	    
	    Element partmap = my_doc.createElement(PARTMAP);
	    Element epmtype = my_doc.createElement(PMTYPE);
	    Text tpmtype = null;
	    
	    
	    partmap.setAttribute("partno", String.valueOf(i));
	    my_partition.appendChild(partmap);
	    
	    
	    // <partmap> <- <pm-type>
	    partmap.appendChild(epmtype);
	    
	    
	    if(com.udfv.ecma167.UDF_part_map1.class.isAssignableFrom(env.part[i].getClass())){
		debugMsg(1, i + ":Type1 Partition Map");
		tpmtype = my_doc.createTextNode("Type1 Partition Map");
		Element udf_extent = my_doc.createElement(UDF_EXTENT);
		
		partmap.appendChild(udf_extent);
		//udf_extent.appendChild(createExtentsElement(env.part_e[i]));
		udf_extent.appendChild(createExtentsElement(env.getPartMapExtent(i, 0)));
	    }
	    else if(com.udfv.udf150.UDF_SparablePartMap.class.isAssignableFrom(env.part[i].getClass())){
		debugMsg(1, i + ":Sparable Partition Map");

		tpmtype = my_doc.createTextNode("Sparable Partition Map");
		Element udf_extent = my_doc.createElement(UDF_EXTENT);
		
		partmap.appendChild(udf_extent);
		udf_extent.appendChild(createExtentsElement(env.getPartMapExtent(i, 0)));
	    }
	    else if(com.udfv.udf250.UDF_MetadataPartMap.class.isAssignableFrom(env.part[i].getClass())){
		debugMsg(1, i + ":Metadata Partition Map");
		/*
		   <pm-type>Metadata Partition Map</pm-type>
		   <metadata mirror="no">
		       <UDF_Extent...
		   </metadata>
		   <metadata mirror="yes">
		       <UDF_Extent...
		   </metadata>
		*/
		// Main
		tpmtype = my_doc.createTextNode("Metadata Partition Map");
		Element udf_extent = my_doc.createElement(UDF_EXTENT);
		Element metadata   = my_doc.createElement(METADATA);
		
		metadata.setAttribute("mirror", "no");
		partmap.appendChild(metadata);
		metadata.appendChild(udf_extent);
		udf_extent.appendChild(createExtentsElement(env.getPartMapExtent(i, 0)));
		
		// Mirror
		if(env.getPartMapExtent(i, 1) != null){ 
		    
		    Element udf_extent_m = my_doc.createElement(UDF_EXTENT);
		    Element metadata_m   = my_doc.createElement(METADATA);
		    
		    metadata_m.setAttribute("mirror", "yes");
		    partmap.appendChild(metadata_m);
		    metadata_m.appendChild(udf_extent_m);
		    udf_extent_m.appendChild(createExtentsElement(env.getPartMapExtent(i, 1)));
		}
	    }
	    else if(com.udfv.udf150.UDF_VirtualPartMap.class.isAssignableFrom(env.part[i].getClass())){
		debugMsg(1, i + ":Virtual Partition Map");
		/*
		   <pm-type>Virtual Partition Map</pm-type>
		   <vat vatno="0" icb="yyyyyyy">
		       <UDF_Extent...
		   </vat>
		   <vat vatno="1" icb="xxxxxxx">
		       <UDF_Extent...
		   </vat>
		   </partmap>
		*/
		tpmtype = my_doc.createTextNode("Virtual Partition Map");

		/*
		for(int j = 0; j < env.vat_part_e.length; j++){
		    
		    Element vat = my_doc.createElement(VAT);
		    Element udf_extent = my_doc.createElement(UDF_EXTENT);
		    
		    vat.setAttribute("vatno", String.valueOf(j));
		    partmap.appendChild(vat);
		    vat.appendChild(udf_extent);
		    udf_extent.appendChild(createExtentsElement(env.getPartMapExtent(i, j)));
		}
		*/
		try{
		    for(int j = 0; ; j++){
			UDF_Extent vat_ext = env.getPartMapExtent(i, j);
			Element vat = my_doc.createElement(VAT);
			Element udf_extent = my_doc.createElement(UDF_EXTENT);
		    
			vat.setAttribute("vatno", String.valueOf(j));
			partmap.appendChild(vat);
			vat.appendChild(udf_extent);
			udf_extent.appendChild(createExtentsElement(vat_ext));
		    }
		}
		catch(UDF_PartMapException e){
		    ;//errorになったら抜ける
		}
	    }
	    else if(com.udfv.ecma167.UDF_part_map2.class.isAssignableFrom(env.part[i].getClass())){
		
		tpmtype = my_doc.createTextNode("Type2 Partition Map");
		Element udf_extent = my_doc.createElement(UDF_EXTENT);
		
		partmap.appendChild(udf_extent);
		udf_extent.appendChild(createExtentsElement(env.getPartMapExtent(i, 0)));
	    }
	    else{
		
		;// pmtype.setNodeValue("Unknown Partition Map");
	    }
	    
	    // <pm-type> に値を設定
	    epmtype.appendChild(tpmtype);
	}
    }
    
    /**
       Anchor Volume Descriptor Pointer を読み込む。
       正常に読み込めた場合、UDF_Env にMain/Reserve VDS の位置を格納する。
       
       @param f    イメージアクセサ。
       @param sec  読み込むセクタ。通常、256/N-256/N-1 のいずれか
       @param nth  何番目のanchorか
	
       @return     正常に読み込んだ場合、true を返す。
                   AVDP が存在しない、または不正なデータであった場合はfalse を返す。
    */
    final protected boolean readAVDP(UDF_RandomAccess f, int sec, int nth) throws IOException, UDF_Exception{
	debugMsg(3, "readAVDP:" + sec);
	f.seekSec(sec);

	// XML に <avdp>を追加する
	//UDF_Extent avdp = createExtent("UDF_Image", "avdp");
	UDF_Extent avdp = (UDF_Extent)createElement("UDF_Extent", null, UDF_XML.AVDP);
	avdp.addExtent(sec, -1, env.LBS);
	UDF_RandomAccessExtent rae = avdp.genRandomAccessExtent();
	
	//UDF_desc2 desc2 = new UDF_desc2(this, null, "UDF_desc2");
	com.udfv.ecma167.UDF_desc2 desc2 = (UDF_desc2)createElement("UDF_desc2", null, null);
	desc2.readFrom(rae);
	
	com.udfv.ecma167.UDF_desc2 prevdesc2 = env.anchorVolDescPointer[nth];
	env.anchorVolDescPointer[nth] = desc2;
	
	UDF_ErrorList el = desc2.verify();
	if(el.hasMoreErrorLevel(UDF_Error.L_ERROR)){
	    //env.anchorDesc = prevdesc2;
	    env.anchorVolDescPointer[nth] = prevdesc2;
	    return false;
	}
	else{
	    appendChild(avdp);
	    avdp.appendChild(desc2);
	}
	
	UDF_pad pad = new UDF_pad(this, null, "UDF_pad", env.LBS);
	avdp.appendChild(pad);
	pad.readFrom(f);

	desc2.getMainVolDescSeqExtent().getExtentLoc().setAttribute("ref", "MVDS.loc");
	desc2.getMainVolDescSeqExtent().getExtentLen().setAttribute("ref", "MVDS.len");
	desc2.getReserveVolDescSeqExtent().getExtentLoc().setAttribute("ref", "RVDS.loc");
	desc2.getReserveVolDescSeqExtent().getExtentLen().setAttribute("ref", "RVDS.len");


	return true;
    }
    
    /**
       ボリューム認識時用に、256/N-256/N-1 セクタのAVDP の読み込みを試みる。
       
       @param f  イメージアクセサ。
       @return   3箇所のAVDP のうち、いずれか1つでも認識できた場合、true を返す。
                 全て認識できなかった場合、false を返す。
    */
    protected boolean readAVDPinVR(UDF_RandomAccess f) throws IOException, UDF_Exception{
	
	//env.exist_avdp[0] = env.exist_avdp[1] = env.exist_avdp[2] = false;
	
	// 256 sec
	//env.exist_avdp[0] =
	readAVDP(f, 256, 0);
	
	if(env.anchorVolDescPointer[0] != null){
	    desc2_mvds_loc[0] = env.anchorVolDescPointer[0].getMVDSLoc();//mvds_loc;
	    desc2_mvds_len[0] = env.anchorVolDescPointer[0].getMVDSLen();//mvds_len;
	    desc2_rvds_loc[0] = env.anchorVolDescPointer[0].getRVDSLoc();//rvds_loc;
	    desc2_rvds_len[0] = env.anchorVolDescPointer[0].getRVDSLen();//rvds_len;
	}
	
	// N - 257 AVDP
	long loc = f.seek(-((256 + 1) * env.LBS), UDF_RandomAccess.SEEK_END);
	//env.exist_avdp[1] = 
	readAVDP(f, (int)(loc / env.LBS), 1);
	if(env.anchorVolDescPointer[1] != null){
	    desc2_mvds_loc[1] = env.anchorVolDescPointer[1].getMVDSLoc();//mvds_loc;
	    desc2_mvds_len[1] = env.anchorVolDescPointer[1].getMVDSLen();//mvds_len;
	    desc2_rvds_loc[1] = env.anchorVolDescPointer[1].getRVDSLoc();//rvds_loc;
	    desc2_rvds_len[1] = env.anchorVolDescPointer[1].getRVDSLen();//rvds_len;
	}
	
	// N - 1 AVDP
	loc = f.seek(-env.LBS, UDF_RandomAccess.SEEK_END);
	//env.exist_avdp[2] = 
	readAVDP(f, (int)(loc / env.LBS), 2);
	if(env.anchorVolDescPointer[2] != null){
	    desc2_mvds_loc[2] = env.anchorVolDescPointer[2].getMVDSLoc();//mvds_loc;
	    desc2_mvds_len[2] = env.anchorVolDescPointer[2].getMVDSLen();//mvds_len;
	    desc2_rvds_loc[2] = env.anchorVolDescPointer[2].getRVDSLoc();//rvds_loc;
	    desc2_rvds_len[2] = env.anchorVolDescPointer[2].getRVDSLen();//rvds_len;
	}

	try{
	    env.getAnchorVolDescPointer();
	}
	catch(UDF_VolException e){
	    return false;
	}
	
	return true;
    }
    
    /**
       ISO9660 のディレクトリレコード群を読み込みます。
       @param sec        ディレクトリレコードの開始セクタ。
       @param datalen    読み込む１ディレクトリレコード群のサイズ。
       @param parentsec  sec にある先頭のディレクトリレコードの親ディレクトリレコードが記録されているセクタ。
                         Primary Vol Desc のroot ディレクトリレコードの場合は-1を指定します。
       
       @return 読み込んだディレクトリレコードの総サイズを返します。
    */
    protected long readCD9660DirRec(long sec, long datalen, long parentsec) throws IOException, UDF_DataException, UDF_IOException, UDF_Exception{
	
	long total = 0;
	long ret = 0;
	
	
	if(env.ecma119_PrimaryVolumeDesc == null)
	    return 0;
	
//	try{
	    long  readsz = 0;
	    
	    UDF_Extent ext = (UDF_Extent)createElement("UDF_Extent", null, null);//"UDF_ECMA119DirectoryRecords");
	    appendChild(ext);
	    
	    ext.addExtent((int)sec, -1, Long.MAX_VALUE);
	    UDF_RandomAccessExtent rae = ext.genRandomAccessExtent();
	    
	    while(total < datalen){
		
		com.udfv.ecma119.UDF_ECMA119DirectoryRecord dirrec = 
		    new com.udfv.ecma119.UDF_ECMA119DirectoryRecord(this, "ecma119", "UDF_ECMA119DirectoryRecord");
		
		// Length of Record は０はありえないが
		// datalen が2048 で丸められているケースがある(DIGA とか? のでチェックする
		if(rae.readUint8() == 0){
		    
		    // ECMA 119 6.8.1.1
		    // Directory Record は、開始位置と同じ論理セクタで終了しなければならない
		    // 終了できない場合は、論理セクタ終了位置まで0で埋め、次のセクタから開始する
		    // パディング領域を作成
		    UDF_pad pad = new UDF_pad(this, null, null, UDF_Env.LBS);
		    rae.seek(-1, UDF_RandomAccess.SEEK_CUR);
		    readsz = pad.readFrom(rae);
		    if(readsz == 0)
			throw new UDF_DataException(this, "invalid 9660 Directory Record");
			
		    total += readsz;
		    
		    ext.appendChild(pad);
		    
		    continue;
		}else
		    rae.seek(-1, UDF_RandomAccess.SEEK_CUR);
		
		readsz = dirrec.readFrom(rae);
		ext.appendChild(dirrec);
		total += readsz;
		
		// ディレクトリならその先も読みにいく
		if((dirrec.getFileFlags().getLongValue() & 0x02) != 0){
		    
		    long childsec = dirrec.getLocOfExtent().getLongValue();
		    
		    if(childsec != sec && childsec != parentsec){
			
			ret += readCD9660DirRec(childsec, dirrec.getDataLen().getLongValue(), sec);
		    }
		}
	    }

	    ext.truncExtent(UDF_Util.align(datalen, UDF_Env.LBS));
/*	}
	catch(Exception e){
	    
	    e.printStackTrace();
	}
*/	
	return ret + total;
    }
    
    /**
       ISO9660 のパステーブルを読み込みます。
       
       @param sec  パステーブルの開始セクタ。
       @param lorm
       true を指定すると、Ｌパステーブルとして読み込みます。
       false を指定すると、Ｍパステーブルとして読み込みます。
       
       @return 読み込んだディレクトリレコードの総サイズを返します。
    */
    protected long readCD9660PathTable(long sec, long ptsize, boolean lorm){
	
	long  total = 0;
	//long  ptsize = env.cd9660_pathtable_size;
	
	
	if(ptsize == -1)
	    return 0;
	
	try{
	    long readsz = 0;
	    UDF_Extent ext = (UDF_Extent)createElement("UDF_Extent", null, null);
	    /*
(lorm) ?
		(UDF_Extent)createElement("UDF_Extent", null, "UDF_ECMA119LPathTable") :
		(UDF_Extent)createElement("UDF_Extent", null, "UDF_ECMA119MPathTable"); 
	    */
	    ext.addExtent((int)sec, -1, Long.MAX_VALUE);
	    UDF_RandomAccessExtent rae = ext.genRandomAccessExtent();
	    
	    while(total < ptsize){
		
		if(lorm){  // LPath
		    
		    com.udfv.ecma119.UDF_ECMA119PathTable pathtable = 
			new com.udfv.ecma119.UDF_ECMA119PathTable(this, "ecma119", "UDF_ECMA119PathTable");
		    
		    readsz = pathtable.readFrom(rae);
		    ext.appendChild(pathtable);
		    total += readsz;
		}
		else{  // MPath
		    
		    com.udfv.ecma119.UDF_ECMA119PathTable_be pathtable = 
			new com.udfv.ecma119.UDF_ECMA119PathTable_be(this, "ecma119", "UDF_ECMA119PathTable_be");
		    
		    readsz = pathtable.readFrom(rae);
		    ext.appendChild(pathtable);
		    total += readsz;
		}
	    }
	    
	    UDF_pad pad = new UDF_pad(this, null, null, ptsize);
	    pad.readFrom(rae);
	    ext.appendChild(pad);
	    
	    appendChild(ext);
	    ext.truncExtent(ptsize);
	}
	catch(Exception e){
	    
	    e.printStackTrace();
	}
	
	return total;
    }
    
    /**
       <UDF_desc257>+をイメージから読み、指定された UDF_Extentに格納していく。
       このメソッドは type=DIRECTのときは使用しない。
    public final void readDir(UDF_Extent ext) throws IOException, UDF_Exception{
	UDF_RandomAccessExtent rae = null;
	try{
	    rae = ext.genRandomAccessExtent();
	    ext.setGlobalPoint(rae);
	    while(!rae.eof()){
		UDF_desc257 desc257 = (UDF_desc257)createElement("UDF_desc257", null, "UDF_desc257");
		desc257.readFrom(rae);
		ext.appendChild(desc257);
	    }
	}
	catch(UDF_DescTagException e){
	    //tagidがちがっていても0x00ならばエラーとしない
	    if(e.getTagId() != 0)
		throw e;
	}
	catch(UDF_EOFException e){
	    ;// ignore
	}
    }
    */
    
    /**
       Logical Volume Integrity Sequence を読む。
       
       @param  f  イメージアクセサ。
       @return true を返す。
    */
    protected boolean readLVIS(UDF_RandomAccess f) throws IOException, UDF_Exception{
	debugMsg(3, "readIS:" + env.getISLoc());
	
	// <is>を追加する
	UDF_IS is_ext = (UDF_IS)createElement("UDF_IS", "UDF_Image", UDF_XML.IS);
	appendChild(is_ext);
	// extentの位置を指定
	is_ext.addExtent(env.getISLoc(), -1, env.getISLen());
	is_ext.setAttribute("id", "LVIS");
	is_ext.readFrom(f);

	env.is = is_ext;

	return true;
    }
    
    /**
       Main Volume Descriptor Sequence を読む。
       このメソッドを正常に終了させるためには、前もってreadAVDPinVR()メソッドをコールしておく必要がある。
       
       @param  f  イメージアクセサ。
       @return MVDS を正常に読み込めた場合、true を返す。そうでない場合、false を返す。
    */
    protected boolean readMVDS(UDF_RandomAccess f) throws IOException, UDF_Exception{
	for(int i = 0; i < 3; i++){
	
	    debugMsg(3, "readMVDS:" + desc2_mvds_loc[i]);
	    
	    if(desc2_mvds_loc[i] == 0)
		continue;
		
	    // <mvds>を追加する
	    UDF_VDS mvds_ext = (UDF_VDS)createElement("UDF_VDS", "UDF_Image", UDF_XML.MVDS);
	    // extentの位置を指定
	    mvds_ext.addExtent(desc2_mvds_loc[i], -1, desc2_mvds_len[i]);
	    mvds_ext.setAttribute("id", "MVDS");
	    env.mvds = mvds_ext;
	
	    try{
		long readsz = mvds_ext.readFrom(f);
		if(readsz == 0){
		    env.mvds = null;
		    continue;
		}
	    }
	    catch(UDF_Exception ue){
		env.mvds = null;
		continue;
	    }
	    appendChild(mvds_ext);

	    return true;
	}
	
	return false;
    }
    
    /**
       Reserve Volume Descriptor Sequence を読む。
       このメソッドを正常に終了させるためには、前もってreadAVDPinVR()メソッドをコールしておく必要がある。
       
       @param  f  イメージアクセサ。
       @return RVDS を正常に読み込めた場合、true を返す。そうでない場合、false を返す。
    */
    protected boolean readRVDS(UDF_RandomAccess f) throws IOException, UDF_Exception{
	
	for(int i = 0; i < 3; i++){
	
	    if(desc2_rvds_loc[i] == 0)
		continue;
	    
	    debugMsg(3, "readRVDS:" + desc2_rvds_loc[i]);
	    // <rvds>を追加する
	    UDF_VDS rvds_ext = (UDF_VDS)createElement("UDF_VDS", "UDF_Image", UDF_XML.RVDS);
	    // extentの位置を指定
	    rvds_ext.addExtent(desc2_rvds_loc[i], -1, desc2_rvds_len[i]);
	    rvds_ext.setAttribute("id", "RVDS");
	    env.rvds = rvds_ext;
	
	    try{
		long readsz = rvds_ext.readFrom(f);
		if(readsz == 0){
		    env.rvds = null;
		    continue;
		}
	    }
	    catch(UDF_Exception ue){
		//ue.printStackTrace();
		env.rvds = null;
		continue;
	    }
	    appendChild(rvds_ext);

	    return true;
	}
	
	return false;
    }

    protected void postVDSReadHook(UDF_RandomAccess f) throws IOException, UDF_Exception{
	if(env.mvds == null && env.rvds == null)
	    throw new UDF_VolException(this, "no VDS");

	UDF_VDS vds = env.mvds;
	if(vds == null)
	    vds = env.rvds;

	UDF_ElementList desc5_list = vds.getPrevailingPartDesc();
	for(int i=0 ; i<desc5_list.size() ; ++i){
	    UDF_VolDesc desc5 = (UDF_VolDesc)desc5_list.elementAt(i);
	    desc5.postVolReadHook(f);
	}

	UDF_ElementList desc6_list = vds.getPrevailingLogicalVolDesc();
	for(int i=0 ; i<desc6_list.size() ; ++i){
	    UDF_VolDesc desc6 = (UDF_VolDesc)desc6_list.elementAt(i);
	    desc6.postVolReadHook(f);
	}

    }
    
    /**
       Volume Recobnition Sequence を読み込みます。
       
       @param f  イメージアクセサ。読み込み開始位置までシークしておく必要があります。
       @return 実際に読み込んだサイズを返します。
    */
    protected long readVRS(UDF_RandomAccess f) throws IOException, UDF_Exception{

	long readsz = 0;
	UDF_VRS vrs_ext = (UDF_VRS)createElement("UDF_VRS", "UDF_Image", UDF_XML.VRS);
	
	
	long loc = f.getPointer() / env.LBS;
	
	// 長さ未定につき最大の長さで挑戦
	vrs_ext.addExtent((int)loc, -1, 16 * env.LBS);
	readsz = vrs_ext.readFrom(f);
	vrs_ext.truncExtent(readsz);
	
	
	if(0 < readsz){

	    appendChild(vrs_ext);
	    vrs_ext.truncExtent(readsz);
	}
	
	env.vrs1 = vrs_ext;
	return readsz;
    }

    /**
       FSDSを読む

       @param f	アクセサ
       @param subno 副番号
     */
    protected void readFSDS(UDF_RandomAccess f, int subno) throws IOException, UDF_Exception{
	UDF_AD loc = env.getLogicalVolDesc().getFSDSLoc();
	debugMsg(1, "readFSDS:" + loc.getLbn()
		 + ":" + loc.getPartRefNo());

	// <fsds>追加
	UDF_FDS fsds_ext = (UDF_FDS) createElement("UDF_FDS", "UDF_Image", UDF_XML.FSDS);
	fsds_ext.setPartSubno(subno);
	fsds_ext.addExtent(loc.getLbn(), loc.getPartRefNo(), loc.getLen());
	fsds_ext.setAttribute("id", "FSDS" + (subno > 0 ? ("_"+subno) : ""));
	//fsds_ext.readFrom(env.part_e[env.fsds_partno].genRandomAccessExtent());
	fsds_ext.readFrom(env.getPartMapRandomAccess(loc.getPartRefNo(), subno));
	appendChild(fsds_ext);

	/*
	if(subno == 1)
	    env.mirr_fsds = fsds_ext;
	else
	    env.fsds = fsds_ext;
	*/
	env.setFDS(null, subno, fsds_ext);
    }

    /**
       FSDSを読む

       @param f	アクセサ
       @param mirror ミラーフラグ
       @deprecated replaced by {@link #readFSDS(UDF_RandomAccess, int)}
     */
    protected void readFSDS(UDF_RandomAccess f, boolean mirror) throws IOException, UDF_Exception{
	readFSDS(f, mirror ? 1 : 0);
    }

    protected void readSystemXML(Document inputdoc){
	Node system_node = inputdoc.getElementsByTagName("system").item(0);
	
	NodeList nl = system_node.getChildNodes();
	for(int i=0 ; i<nl.getLength() ; ++i){
	    if(nl.item(i).getNodeType() == Node.ELEMENT_NODE){
		Element e = (Element)nl.item(i);
		if(e.getNodeName().equals("version")){
		    //env.system_version = UDF_Util.getNodeVal(e);
		}
		else if(e.getNodeName().equals("image-file")){
		    //env.image_file = UDF_Util.getNodeVal(e);
		}
		else if(e.getNodeName().equals("image-comment")){
		    env.image_comments = UDF_Util.getNodeVal(e);
		}
		else if(e.getNodeName().equals("image-size")){
		    //ファイルを合わせる
		    if(env.f != null){
			long file_size = Long.parseLong(UDF_Util.getNodeVal(e));
			try{
			    debugMsg(3, "set image file size: " + file_size);
			    env.f.seek(file_size - 1);
			    env.f.write(new byte[1]);
			    debugMsg(3, "set image file size: done");
			}
			catch(IOException exception){
			    ;
			}
			catch(UDF_Exception exception){
			    ;
			}
		    }
		    env.image_size = Long.parseLong(UDF_Util.getNodeVal(e));
		}
		else if(e.getNodeName().equals("image-total-sec")){
		    env.image_total_sec = Long.parseLong(UDF_Util.getNodeVal(e));
		}
		else if(e.getNodeName().equals("media-type")){
		    env.media_type = Short.parseShort(UDF_Util.getNodeVal(e));
		}
	    }
	}
    }
    /**
     */
    private void createSystemXML(){
	Document doc = my_doc;
	Element system = (Element)doc.getElementsByTagName("system").item(0);
	if(system == null){
	    system = my_doc.createElement("system");
	    root_node.insertBefore(system, my_doc.getFirstChild().getFirstChild());
	}
	while(system.hasChildNodes()){
	    system.removeChild(system.getFirstChild());
	}

	Element version = my_doc.createElement("version");
	Element file = my_doc.createElement("image-file");
	Element comment = my_doc.createElement("image-comment");
	Element file_size = my_doc.createElement("image-size");
	Element total_sec = my_doc.createElement("image-total-sec");
	Element media_type = my_doc.createElement("media-type");
	//Element media_mode = my_doc.createElement("media-mode");

	system.appendChild(version);
	system.appendChild(file);
	system.appendChild(comment);
	system.appendChild(file_size);
	system.appendChild(total_sec);
	system.appendChild(media_type);
	//system.appendChild(media_mode);
	

	UDF_Util.setNodeVal(version, env.system_version);
	UDF_Util.setNodeVal(file, env.image_file == null ? "null" : env.image_file);
	UDF_Util.setNodeVal(comment, env.image_comments);
	UDF_Util.setNodeVal(media_type, env.media_type);

	try{
	    if(env.f != null){
		UDF_Util.setNodeVal(file_size, "" + env.f.length());
		UDF_Util.setNodeVal(total_sec, "" + env.f.length() / env.LBS);
	    }
	    else{
		UDF_Util.setNodeVal(file_size, "" + env.image_size);
		UDF_Util.setNodeVal(total_sec, "" + env.image_total_sec);
	    }
	}
	catch(IOException e){
	    UDF_Util.setNodeVal(file_size, "" + env.image_size);
	    UDF_Util.setNodeVal(total_sec, "" + env.image_total_sec);
	    ;//ignore
	}
	
    }
    /**
    */
    protected void setupNode(Document doc){
	
	my_doc = doc;
	if (my_doc == null) {
	    return;
	}

	if(env.xmlns){
	    try{
		UDF_XMLUtil.addDocumentType(my_doc, "udf", UDF_XML.UDF_DTD2, UDF_XML.UDF_DTD);
	    }
	    catch(org.w3c.dom.DOMException e){
		// ignore
		;
	    }
	}

	root_node = my_doc.createElement("udf");
	if(true || env.xmlns){
	    root_node.setAttribute("xmlns", UDF_XML.NS_UDF);
	    root_node.setAttribute("xmlns:ecma119", UDF_XML.NS_ECMA119);
	}

	setNode(root_node);
	my_doc.appendChild(root_node);
	createSystemXML();

	// <partition>:ボリュームを読み取った後、パーティション情報を追加するためのエレメント
	my_partition = my_doc.createElement("partition-info");
	root_node.appendChild(my_partition);
	my_partmap = my_doc.createElement("partition-map-info");
	root_node.appendChild(my_partmap);

    }

    /*
      Space Bitmap Descriptor のビットマップ情報のビット数を取得する。
      @return Space Bitmap Descriptor のビットマップ情報のビット数
    */
    /*
    public int getLenOfSpaceBitmap() {
        if (env.spaceBitmapDesc == null) {
            return 0;
        }

        return env.spaceBitmapDesc.getNumberOfBits().getIntValue();
    }
    */

    /*
      パーティション０の長さをセクタ単位で取得する。
      @return パーティション０の長さ（セクタ単位）
    public int getLenOfCreatedSpaceBitmap() {
        return (int)((env.getPartLen(0) + env.LBS -1) / env.LBS);
    }
    */

    /**
       Space Bitmap Descriptor のビットマップ情報をbyte配列で取得する。
       @return Space Bitmap Descriptor のビットマップ情報のバイト配列

       このメソッドは TreeDemo用である。
    */
    public byte[] getSpaceBitmap() {
	/*
	if (env.spaceBitmapDesc == null) {
		return new byte[0];
	}
	return env.spaceBitmapDesc.getBitmap().getData();
	*/
	try{
	    return env.getPartBitmap(0).getData();
	}
	catch(UDF_VolException e){
	    return new byte[0];
	}
    }

    /**
      env.rootからの情報を元にビットマップデータを作成する。
      @return 作成したビットマップ情報のバイト配列

       このメソッドは TreeDemo用である。
    */
    /*
    public byte[] getCreatedSpaceBitmap() {
	UDF_desc264 desc264 = (UDF_desc264) createElement("UDF_desc264", null, null);
	return desc264.createBitmap(env.root.getChildren(), (getLenOfCreatedSpaceBitmap() + 7) / 8, 0);
    }
    */
    /*
      廃棄予定。
    */
    public byte[] createBytemap(int sec) {
        return new byte[sec];
    }

    /**
      この UDF_Image が持つ UDF_Extent の extents 情報を線として描いた JPanel を返す。

      <em>
      4096以上の横幅が指定されたときは4096に丸める。
      </em>

      @param w 戻すJPanelの横幅
      @param h 戻すJPanelの縦幅
    */
    public javax.swing.JComponent createJBitmapPanel(int w, int h) {

        com.udfv.gui.JBitmapColorSet cs = new com.udfv.gui.JBitmapColorSet();
        com.udfv.gui.JSpaceBitmapPanel panel = new com.udfv.gui.JSpaceBitmapPanel(this, w, h, cs);

        return panel;
    }


    /**
      この UDF_Image が持つ UDF_Extent の extents 情報を線として描いた JPanel を返す。

      <em>
      4096以上の横幅が指定されたときは4096に丸める。
      </em>

      @param w 戻すJPanelの横幅
      @param h 戻すJPanelの縦幅
      @param cs 配色情報
    */
    public javax.swing.JComponent createJBitmapPanel(int w, int h, com.udfv.gui.JBitmapColorSet cs) {

        com.udfv.gui.JSpaceBitmapPanel panel = new com.udfv.gui.JSpaceBitmapPanel(this, w, h, cs);

        return panel;
    }


    /**
      Metadata File / Metadata Mirror File / Metadata Bitmap File を検索する。
    */
    public com.udfv.ecma167.UDF_desc261 findMetadataFile(int desc_number, int type) throws UDF_InternalException {

	com.udfv.core.UDF_Extent extent = null;
	com.udfv.ecma167.UDF_desc261 fe = null;

	for (int x1 = 0; ; x1++) {
	    extent = (com.udfv.core.UDF_Extent) findByXPATH("/udf/UDF_Extent[" + x1 + "]");
	    if (extent == null) {
		break;
	    }

	    for (int x2 = 0; ; x2++) {
		String path = "/udf/UDF_Extent[" + x1 +"]/UDF_desc" + desc_number + "[" + x2 + "]";
		fe = (com.udfv.ecma167.UDF_desc261) findByXPATH(path);
		if (fe == null) {
		    break;
		}

		debugMsg(4, "#### findMetadataFile(" + desc_number + ") : " + path);

		int ft = fe.getICBFileType();

		if (ft == type) {
		    return fe;
		}
	    }
	}

	throw new UDF_InternalException(this, "NOT FOUND : File Type = " + type);
    }
    
    /**
       Part Header Descに付随するデータを読む

     */
    public int readPartHeaderDesc() throws IOException, UDF_Exception{
	UDF_ElementList d5l = env.getPartDescList(UDF_Env.VDS_AUTO);

	for(int i=0 ; i<d5l.size() ; ++i){
	    UDF_desc5 d5 = (UDF_desc5) d5l.elementAt(i);
	    UDF_PartHeaderDesc phd = env.getPartHeaderDesc(UDF_Env.VDS_AUTO, d5.getPartNumber().getIntValue());

	    UDF_short_ad ad = phd.getUnallocatedSpaceBitmap();

	    if(ad.getLen() > 0){
		debugMsg(1, "readSpaceBitmap:" + ad.getLbn() + ":" + ad.getLen());
		
		UDF_Extent sb_ext = createExtent(null, null);
		sb_ext.setAttribute("id", "SB" + d5.getPartNumber().getIntValue());
		UDF_desc264 desc264 = new UDF_desc264(this, null, null);
		UDF_pad pad = new UDF_pad(this, null, null, ad.getLen());
		
		sb_ext.addExtent(ad.getLbn(), 0, ad.getLen());
		UDF_RandomAccessExtent ra = sb_ext.genRandomAccessExtent();
		desc264.readFrom(ra);
		pad.readFrom(ra);
		
		//env.spaceBitmapDesc = desc264;
		this.appendChild(sb_ext);
		sb_ext.appendChild(desc264);
		sb_ext.appendChild(pad);
	    }
	
	    ad = phd.getFreedSpaceBitmap();
	    if(ad.getLen() > 0){
		UDF_Extent sb_ext = createExtent(null, null);
		UDF_desc264 desc264 = new UDF_desc264(this, null, null);
		UDF_pad pad = new UDF_pad(this, null, null, ad.getLen());
	
		sb_ext.addExtent(ad.getLbn(), 0, ad.getLen());
		UDF_RandomAccessExtent ra = sb_ext.genRandomAccessExtent();
		desc264.readFrom(ra);
		pad.readFrom(ra);
	
		this.appendChild(sb_ext);
		sb_ext.appendChild(desc264);
		sb_ext.appendChild(pad);
	    }

	    ad = phd.getPartIntegrityTable();
	    if(ad.getLen() > 0){
		UDF_Extent pi_ext = createExtent(null, null);
		com.udfv.ecma167.UDF_desc265 desc265 = (com.udfv.ecma167.UDF_desc265)createElement("UDF_desc265", null, "UDF_desc265");
		UDF_pad pad = new UDF_pad(this, null, null, ad.getLen());
	    
		pi_ext.addExtent(ad.getLbn(), 0, ad.getLen());
		UDF_RandomAccessExtent ra = pi_ext.genRandomAccessExtent();
		desc265.readFrom(ra);
		pad.readFrom(ra);
	    
		this.appendChild(pi_ext);
		pi_ext.appendChild(desc265);
		pi_ext.appendChild(pad);
	    }
	
	    ad = phd.getFreedSpaceTable();
	    if(ad.getLen() > 0){
		UDF_Extent pi_ext = createExtent(null, null);
		com.udfv.ecma167.UDF_desc263 desc263 = (com.udfv.ecma167.UDF_desc263)createElement("UDF_desc263", null, "UDF_desc263");
		UDF_pad pad = new UDF_pad(this, null, null, ad.getLen());
	    
		pi_ext.addExtent(ad.getLbn(), 0, ad.getLen());
		UDF_RandomAccessExtent ra = pi_ext.genRandomAccessExtent();
		desc263.readFrom(ra);
		pad.readFrom(ra);
	    
		this.appendChild(pi_ext);
		pi_ext.appendChild(desc263);
		pi_ext.appendChild(pad);
	    }
	
	    ad = phd.getUnallocatedSpaceTable();
	    if(ad.getLen() > 0){
		UDF_Extent pi_ext = createExtent(null, null);
		com.udfv.ecma167.UDF_desc263 desc263 = (com.udfv.ecma167.UDF_desc263)createElement("UDF_desc263", null, "UDF_desc263");
		UDF_pad pad = new UDF_pad(this, null, null, ad.getLen());
	    
		pi_ext.addExtent(ad.getLbn(), 0, ad.getLen());
		UDF_RandomAccessExtent ra = pi_ext.genRandomAccessExtent();
		desc263.readFrom(ra);
		pad.readFrom(ra);
	   
		this.appendChild(pi_ext);
		pi_ext.appendChild(desc263);
		pi_ext.appendChild(pad);
	    }
	}

	return 0;
    }

    /**
       Bitmapを構築する。

       @param overwrite	SBがある場合、上書きするか否か

       このメソッドは、以下のルールで SpaceBitmapを構築する。
       <pre>

       1) Partitionに SBがあるときはその bitmapを Envに格納する
       2) Partitionに SBがないときはFSの中身を検索してBitmapを構築し、
          そのBitmapをEnvに格納する。
       3) OverWriteフラグが trueならば、FSの中身を検索してBitmapを構築し、
          そのBitmapをEnvに格納すると同時に、SB(もしあれば)のbitmapに
	  複写する。

       4) PartitionMapが MetadataPartitionMapのときは
	4-1) SBがあれば、その bitmapをEnvに格納する。
	4-2) SBがなければ、bitmapを構築し、そのBitmapをEnvに格納する。
	4-3) OverWriteフラグが trueならば 作成したBitmapを
	     SB(もしあれば)のbitmapに複写する

       5) PartitionMapが MetadataPartitionMap以外のときは、対応する
          PartitionのbitmapをEnvに格納する。対応するBitmapは 1)-3)で既に
	  もとまっているはずである。
       </pre>
    */
    public void recalcSB(boolean overwrite) throws UDF_VolException, UDF_PartMapException, UDF_InternalException{
	//System.err.println("recalcSB: overwrite=" + overwrite);
	UDF_ElementList el = env.getPartDescList(UDF_Env.VDS_AUTO);
	for(int i=0 ; i<el.size() ; ++i){
	    UDF_desc5 d5 = (UDF_desc5)el.elementAt(i);
	    UDF_PartHeaderDesc phd = d5.getPartHeaderDesc();
	    int partno = d5.getPartNumber().getIntValue();
	    UDF_bitmap bm = buildPartBitmap(partno);
	    //SBがある場合
	    if(phd.getSpaceBitmap() != null){
		//System.err.println("partition " + i + " has spaceBitmap");
		//phd.getSpaceBitmap().getBitmap().debug(0);
		if(overwrite)
		    env.setPartBitmap(partno, bm);
		else
		    env.setPartBitmap(partno, phd.getSpaceBitmap().getBitmap());
	    }
	    //SBがない
	    else{
		//System.err.println("partition " + i + " has no spaceBitmap");
		env.setPartBitmap(partno, bm);
	    }
	    if(overwrite){
		//System.err.println("YYY" + i);
		//bm.debug(9);
		if(d5.getPartHeaderDesc().getSpaceBitmap() != null)
		    d5.getPartHeaderDesc().getSpaceBitmap().setBitmap(bm);
	    }
	}

	el = env.getPartMapList();
	for(int i=0 ; i<el.size() ; ++i){
	    UDF_PartMap pm = (UDF_PartMap)el.elementAt(i);
	    if(env.isMetadataPartMap(i)){
		com.udfv.udf250.UDF_MetadataPartMap mpm = (com.udfv.udf250.UDF_MetadataPartMap) pm;
		UDF_bitmap bm = buildPartMapBitmap(i);
		//SBがある場合
		if(mpm.getMetadataBitmap() != null){
		    //mpm.getMetadataBitmap().getBitmap().debug(9);
		    env.setPartMapBitmap(i, mpm.getMetadataBitmap().getBitmap());
		}
		//SBがない場合
		else{
		    env.setPartMapBitmap(i, bm);
		}
		if(overwrite){
		    env.getPartMapBitmap(i).setData(bm.getData());
		    // bitsizeが半端な値(8の倍数でない)である可能性もあるので、ここで改めて設定する
		    env.getPartMapBitmap(i).setBitSize(bm.getBitSize());
		}
	    }
	    else{//対応する Partition Mapの Bitmapをかわりにセットする
		//System.err.println("QQQ" + i);
		UDF_desc5 d5 = env.getPartDescByPartRefNo(UDF_Env.VDS_AUTO, i);
		int partno = d5.getPartNumber().getIntValue();
		//env.getPartBitmap(partno).debug(9);
		
		env.setPartMapBitmap(i, env.getPartBitmap(partno));
	    }
	}
	
	if(overwrite)
	    recalcSBD();
    }
    
    /**
       Space Bitmap Descriptorにおいて、現在保持しているBitmapを元に各値を再計算する。

       全てのSBDについて、NumberOfBits、NumberOfBytesの値をrecalcする。
       但し、Metadata Bitmapの場合、以下の2通りに分けられる。
       
       SBDがMetadata Bitmap FE外に記録されている場合、上記2つの値のみをrecalcする。
       
       SBDがMetadata Bitmap FE内に記録されている場合、
       上記の2つに加え、Metadata Bitmap FEの以下の値のrecalcも行う。
       <ul>
       <li> CRC Length </li>
       <li> Information Length </li>
       <li> (Extended FEであった場合)Object Size </li>
       <li> Logical Blocks Recorded </li>
       <li> LengthOfAllocDesc </li>
       <li> AllocDesc.size </li>
       </ul>
       この状態で、Bitmapの増大によりSBDがFEに入りきらないサイズになったとしても、
       外に出すようなことは行わない（つまりそのままではエラーが発生する）。
    */
    public void recalcSBD() throws UDF_VolException, UDF_PartMapException, UDF_InternalException{
    
	UDF_ElementList el = env.getPartDescList(UDF_Env.VDS_AUTO);
	el = env.getPartMapList();
	
	for(int i=0 ; i<el.size() ; ++i){
	    UDF_PartMap pm = (UDF_PartMap)el.elementAt(i);
	    if(env.isMetadataPartMap(i)){
		
		com.udfv.udf250.UDF_MetadataPartMap mpm = (com.udfv.udf250.UDF_MetadataPartMap) pm;
		if(mpm.getMetadataBitmapFileLoc().getIntValue() == -1)
		    continue;
		
		UDF_FEDesc mbfe = mpm.getMetadataBitmapFile();
		UDF_desc264 desc264 = mpm.getMetadataBitmap();
		UDF_bitmap bitmap = env.getPartMapBitmap(i);
		
		int beforeNumOfBits = desc264.getNumberOfBits().getIntValue();
		int afterNumOfBits = bitmap.getBitSize();
		int sbsize = 0;
		
		desc264.getNumberOfBits().setValue(afterNumOfBits);
		desc264.getNumberOfBytes().setValue((afterNumOfBits + 7) / 8);
		
		
		if(mbfe.getAllocType() != 3){
		    
		    sbsize = 0;
		    for(Iterator it = mbfe.getADList().iterator(); it.hasNext();){
			UDF_AD ad = (UDF_AD)it.next();
			sbsize += ad.getLen();
		    }
		    
		    long lbr = sbsize / env.LBS;
		    if(0 < (sbsize % env.LBS))
			lbr++;
		
		    mbfe.getLogicalBlocksRecorded().setValue(lbr);
		}
		else{
		    sbsize = desc264.getSize();
		    int crclen = mbfe.getDescTag().getDescCRCLen().getIntValue();
		    mbfe.getDescTag().getDescCRCLen().setValue(crclen + (afterNumOfBits - beforeNumOfBits) / 8);
		    mbfe.getAllocDesc().setSize(sbsize);
		    mbfe.getLenOfAllocDesc().setValue(sbsize);
		}
		
		// InfolenとObject Size
		mbfe.getInfoLen().setValue(sbsize);
		if(com.udfv.ecma167.UDF_desc266.class.isAssignableFrom(mbfe.getClass())){
		
		    UDF_desc266 mbefe = (UDF_desc266)mbfe;
		    mbefe.getObjectSize().setValue(sbsize);
		}
	    }
	}
	
	el = env.getPartDescList(UDF_Env.VDS_AUTO);
	for(int i=0 ; i<el.size() ; ++i){
	
	    UDF_desc264 desc264 = null;
	    UDF_desc5 d5 = (UDF_desc5)el.elementAt(i);
	    UDF_PartHeaderDesc phd = d5.getPartHeaderDesc();
	    
	    //SBがある場合
	    desc264 = phd.getSpaceBitmap();
	    if(desc264 == null)
		continue;
	
	    /* desc5 に対応する bitmap を取得する -yazaki- */
	    UDF_bitmap bitmap = env.getPartBitmap(d5.getPartNumber().getIntValue());
	    int afterNumOfBits = bitmap.getBitSize();
	    
	    desc264.getNumberOfBits().setValue(afterNumOfBits);
	    desc264.getNumberOfBytes().setValue((afterNumOfBits + 7) / 8);
	}
    }
    
    /**
       recalcを行う。
       
       簡易バージョンの際は何も行わない。
    */
    public void recalc(short type, UDF_RandomAccess f){
	
	if(sa_ya == UDF_XMLUtil.SAYA_UME){
	
	    System.err.println("Sorry, but this version of udfv is not supported a reculc function.");
	    return;
	}
	else
	    recalc_core(type, f);
    }
    
    // このメソッドは2005/10/20までrecalcであったが、
    // 梅(簡易バージョン、XML => IMGの機能のみ持つ)を作る際にrecalc_coreへと変更した。
    /**
       recalcを行う。
       
       このメソッドは、readFromXMLもしくはrecalcからのみコールされる。
    */
    public void recalc_core(short type, UDF_RandomAccess f){
    
	UDF_ElementBase[] children = getChildren();

	debugMsg(1, "recalc:"+type);
	try{
	    if(type == RECALC_ENV) {
		//　有効なAVDPを見つけて値を環境にMVDS, RVDSの位置を設定　//
		com.udfv.ecma167.UDF_desc2 avdp = findAVDP();
		if (avdp != null) {
		    UDF_extent_ad ad = avdp.getMainVolDescSeqExtent();
		    
		    ad = avdp.getReserveVolDescSeqExtent();
		}
		
		UDF_VDS mvds = (UDF_VDS)findByXPATH("/udf/mvds");
		env.mvds = mvds;
		UDF_VDS rvds = (UDF_VDS)findByXPATH("/udf/rvds");
		env.rvds = rvds;
		UDF_IS is = (UDF_IS)findByXPATH("/udf/is");
		env.is = is;
		
		
		//　記録されているリビジョンを再登録　//
		try {
		    env.recorded_udf_revision = readUDFRevision();
		}
		catch(UDF_Exception e) {
		    ignoreMsg("recalc", e);
		}
		
		//　Partition Descriptorからパーティションの情報を取得する　//
		UDF_VDS validvds = (mvds != null) ? mvds : rvds;
		com.udfv.ecma167.UDF_desc5 desc5;
		try{
		    desc5 = (UDF_desc5)validvds.getPrevailingPartDesc().elementAt(0);
		}
		catch(UDF_Exception e){
		    ignoreMsg("recalc", e);
		}
		
		try{
		    com.udfv.ecma167.UDF_desc6 lvd = env.getLogicalVolDesc(UDF_Env.VDS_AUTO); //findLogicalVolDesc();
		    
		    //　FSDSの位置を　//
		    UDF_ElementBase [] elem = lvd.getLogicalVolContentsUse().getChildren();
		    for (int i = 0; i < elem.length; i++) {
			
			if (elem[i].getName().equals("UDF_long_ad")) {
			    
			    UDF_long_ad ad2 = (UDF_long_ad) elem[i];
			    break;
			}
		    }
		    
		    //　Part Maps を読み込み　//
		    elem = lvd.getPartMaps().getChildren();
		    env.part = new UDF_PartMap[elem.length];
		    for (int i = 0; i < elem.length; i++) {
			
			env.part[i] = (UDF_PartMap) elem[i];
			
			if (!com.udfv.udf250.UDF_MetadataPartMap.class.isAssignableFrom(elem[i].getClass())) {
			    continue;
			}
			
			com.udfv.udf250.UDF_MetadataPartMap mpm = (com.udfv.udf250.UDF_MetadataPartMap) env.part[i];
			//env.getMetadataPartMap() = mpm;
		    }
		}
		catch(UDF_VolException e){
		    ignoreMsg("core.UDF_Image", e);
		}
	    }
	}
	catch(Exception e){
	    ignoreMsg("core.UDF_Image", e);
	}


	try{
	    if(type == RECALC_TREE || type == RECALC_ENV){
		try{
		    // ROOT を見つけられないとき、Metadata Part があればMirror をかわりに設定する
		    //↑この機能は削除 by issei (2005/6/10)
		    //あくまでも mirrorは mirrorのほうに登録すべきで、mirrorをたどるのが
		    //正しい。mirrorはどこまで行っても mirrorである。

		    UDF_Element root = findById("ROOT");
		    /*
		    if(root != null){
		    
			env.root_fe = (UDF_FEDesc)root.getFirstChild();
			if(env.root_fe == null)
			    env.root_fe = (UDF_FEDesc)findById("ROOT_1").getFirstChild();

		    }else{
			env.root_fe = (UDF_FEDesc)findById("ROOT_1").getFirstChild();
		    }
		    */
		    env.setRootFE(0, (UDF_FEDesc)root.getFirstChild());

		    if(type == RECALC_ENV){
			env.recorded_num_directories = 1;
			env.recorded_num_files = 0;
		    }
		}
		catch(NullPointerException e){
		    ;
		}
		try{
		    UDF_Element sroot = findById("SROOT");
		    /*
		    if(sroot != null){
		    
			env.sroot_fe = (UDF_FEDesc)sroot.getFirstChild();
			if(env.sroot_fe == null)
			    env.sroot_fe = (UDF_FEDesc)findById("SROOT_1").getFirstChild();
		    }else
			env.sroot_fe = (UDF_FEDesc)findById("SROOT_1").getFirstChild();
		    */
		    env.setSRootFE(0, (UDF_FEDesc)sroot.getFirstChild());
		}
		catch(NullPointerException e){
		    ;
		}
	    }
	    else if(type == RECALC_SB){
		recalcSB(true);
	    }
	}
	catch(Exception e){
	    ignoreMsg("core.UDF_Image", e);
	}

	try{
	    // パーティションを表わすタグは再計算してはいけない。
	    for(int i=0 ; i < children.length; i++){
		if(children[i].getName().equals(UDF_XML.EXTENT_OF_PARTITION))
		    continue;
		else if(children[i].getName().equals(UDF_XML.MIRROR_EXTENT_OF_PARTITION))
		    continue;
		
		children[i].recalc(type, f);
	    }
	    if (type == RECALC_LVIS) {

		UDF_desc9 desc9 = env.is.getPrevailingLogicalVolIntegrityDesc();
		if(desc9 != null){
	
		    UDF_ElementBase[] eb = desc9.getFreeSpaceTable().getChildren();
		    for(int i = 0; i < eb.length; i++){
			UDF_uint32 size = (UDF_uint32)eb[i];
			int count = 0;
			{
			    //access typeが readonlyのときは freetableの値は0
			    if(env.getPartDescByPartRefNo(UDF_Env.VDS_AUTO, i).getAccessType().getIntValue() == 1)
				;
			    else{
				UDF_bitmap bm = env.getPartMapBitmap(i);
				count = bm.countBitOn();
			    }
			}

			size.setValue(count);
		    }
		}
	    }
	}
	catch(Exception e){
	    ignoreMsg("core.UDF_Image", e);
	}
    }

    /**
       XMLのラベルを参考に directory ツリーを作りなおす

       まだ機能してません。

       @param id	FEのラベル	
     */
    public void rebuildTreeByAttr(String id){
	try{
	    UDF_ElementBase fe_ext = findById(id);
	    if(fe_ext != null){
		UDF_FEDesc fe = (UDF_FEDesc)fe_ext.getFirstChild();
		int file_type = fe.getICBTag().getFileType().getIntValue();
		if(file_type == UDF_icbtag.T_DIRECTORY || 
		   file_type == UDF_icbtag.T_SDIRECTORY){
		    String ref = fe.getAllocDesc().getAttribute("ref");
		    ref = ref.substring(0, ref.indexOf("."));
		    System.err.println(ref);
		    UDF_ElementBase dir_ext = findById(ref);
		    if(dir_ext == null)
			return;
		    UDF_ElementBase dirs[] = dir_ext.getChildren();
		    for(int i=0 ; i<dirs.length ; ++i){
			UDF_desc257 fid = (UDF_desc257)dirs[i];
			fid.setReferencedBy(fe);
			fe.getDirectoryList().add(fid);
			int filechar = fid.getFileChar().getIntValue();
			if((filechar & UDF_desc257.PARENT) != 0)
			    continue;
			else if((filechar & UDF_desc257.DELETED) != 0)
			    continue;
			else if((filechar & UDF_desc257.DIRECTORY) != 0){
			    String ref2 = fid.getICB().getExtentLen().getAttribute("ref");
			    ref2 = ref2.substring(0, ref2.indexOf("."));
			    UDF_ElementBase reffe_ext = findById(ref2);
			    if(reffe_ext != null){
				UDF_FEDesc reffe = (UDF_FEDesc)reffe_ext.getFirstChild();
				reffe.getReferencedFID().add(fid);
				fid.setReferenceTo(reffe);
				rebuildTreeByAttr(ref2);
			    }
			}
		    }
		}
	    }
	}
	catch(Exception e){
	    ignoreMsg("rebuildTreeByAttr", e);
	}
    }

    /**
      Anchor Volume Descriptor を検索し、取得する。

      @return UDF_desc2。nullのときは発見できていない。
    */
    public UDF_desc2 findAVDP() {

        com.udfv.ecma167.UDF_desc2 desc2 = null;

        for (int i = 0; i < 3; i++) {

            desc2 = (UDF_desc2) findByXPATH("/udf/avdp[" + i + "]/UDF_desc2");
            if (desc2 != null) {

                if (!desc2.isCRC() || !desc2.getDescTag().isChecksum()) {
                    desc2 = null;
                    continue;
                }
                break;
            }
        }

        return desc2;
    }

    /**
      Logical Volume Descriptor を検索し、取得する。

      @return UDF_desc6。nullのときは発見できていない。
    */
    public UDF_desc6 findLogicalVolDesc() {

        com.udfv.ecma167.UDF_desc6 desc6 = null;

        desc6 = (UDF_desc6) findByXPATH("/udf/mvds/UDF_desc6");
        if (desc6 != null) {
            if (desc6.isCRC() && desc6.getDescTag().isChecksum()) {
                return desc6;
            }
        }
        desc6 = (UDF_desc6) findByXPATH("/udf/rvds/UDF_desc6");
        if (desc6 != null) {
            if (desc6.isCRC() && desc6.getDescTag().isChecksum()) {
                return desc6;
            }
        }
        return null;
    }

    /**
     */
    public void resetFEHash(){
	fe_hash.clear();
    }
    /**
       (ディレクトリ構造で見た場合の)Pathnameを指定し、それに対応した FEを取得する。

       @param pathname	pathname
       @return FE

       Pathnameは /8:DVD_RTAV/8:VR_MOVIE.VRO のようにディレクトリを /で区切り指定すること。
       また OSTA Compressed Unicodeのエンコードスキームを 8: または 16:で指定しなければならない。
     */
    public UDF_FEDesc findFEByPathname(String pathname, int subno){
	Object obj = fe_hash.get(subno + ";" + pathname);
	if(obj != null)
	    return (UDF_FEDesc)obj;

	UDF_FEDesc ret = findFEByPathname(null, pathname, subno);

	if(ret != null)
	    fe_hash.put(subno + ";" + pathname, ret);

	return ret;
    }
    /**
       (ディレクトリ構造で見た場合の)Pathnameを指定し、それに対応した FID を取得する。

       @param pathname	pathname
       @return FID

       Pathnameは /8:DVD_RTAV/8:VR_MOVIE.VRO のようにディレクトリを /で区切り指定すること。
       また OSTA Compressed Unicodeのエンコードスキームを 8: または 16:で指定しなければならない。
     */
    public UDF_desc257 findFIDByPathname(String pathname, int subno){
	return findFIDByPathname(null, pathname, subno);
    }

    /**
       (ディレクトリ構造で見た場合の)Pathnameを指定し、それに対応した FEを取得する。

       @param fe	検索を開始するツリーのFE。nullを指定すると root。
       @param pathname	pathname
       @return FE

       Pathnameは /8:DVD_RTAV/8:VR_MOVIE.VRO のようにディレクトリを /で区切り指定すること。
       また OSTA Compressed Unicodeのエンコードスキームを 8: または 16:で指定しなければならない。
     */
    UDF_FEDesc findFEByPathname(UDF_FEDesc fe, String pathname){
	return findFEByPathname(fe, pathname, false);
    }

    /**
       @deprecated replace by {@link findFEByPathname(UDF_FEDesc, String, int)}
     */
    UDF_FEDesc findFEByPathname(UDF_FEDesc fe, String pathname, boolean mirror){
	return findFEByPathname(fe, pathname, mirror ? 1 :  0);
    }


    /**
       (ディレクトリ構造で見た場合の)Pathnameを指定し、それに対応した FEを取得する。

       @param fe	検索を開始するツリーのFE。nullを指定すると root。
       @param pathname	pathname
       @return FE

       Pathnameは /8:DVD_RTAV/8:VR_MOVIE.VRO のようにディレクトリを /で区切り指定すること。
       また OSTA Compressed Unicodeのエンコードスキームを 8: または 16:で指定しなければならない。
       
       1バイト目には以下の記号が使用できる。
       
       <ul>
       <li>rootを表わす記号は /である。</li>
       <li>system stream rootを表わす記号は #である。</li>
       <li>metadata fileを表わす記号は $である。</li>
       <li>metadata mirror fileを表わす記号は %である。</li>
       </ul>
    */
    UDF_FEDesc findFEByPathname(UDF_FEDesc fe, String pathname, int subno){
	/*
	  FID を検索するメソッドを追加するときのコードの変更により
	  FID を持たない root が検索できなくなっていた。
	*/
	if(pathname == null) {
	    return null;
	}
	if (fe == null) {
	    fe = env.getRootFE(subno);
	    if(fe == null)
		return null;
	}
	if(pathname.length() == 0) {
	    return fe;
	}
	if(pathname.equals("/")) {
	    return fe;
	}
	try{
	    if(pathname.equals("$")) {
		UDF_PartMap pm = env.getMetadataPartMap();
		if(pm != null)
		    return env.getMetadataPartMap().getMetadataFile();
		return null;
	    }
	    if(pathname.equals("%")) {
		UDF_PartMap pm = env.getMetadataPartMap();
		if(pm != null)
		    return env.getMetadataPartMap().getMetadataMirrorFile();
		return null;
	    }
	}
	catch(UDF_PartMapException e){
	    e.printStackTrace();
	    return null;
	}

	UDF_desc257 fid = findFIDByPathname(fe, pathname, subno);
	if (fid == null) {
	    return null;
	}
	return fid.getReferenceTo();
    }


    /**
       (ディレクトリ構造で見た場合の)Pathnameを指定し、それに対応した FID を取得する。

       @param fe	検索を開始するツリーのFE。nullを指定すると root。
       @param pathname	pathname
       @return FID

       Pathnameは /8:DVD_RTAV/8:VR_MOVIE.VRO のようにディレクトリを /で区切り指定すること。
       また OSTA Compressed Unicodeのエンコードスキームを 8: または 16:で指定しなければならない。


     */
    UDF_desc257 findFIDByPathname(UDF_FEDesc fe, String pathname){
	return findFIDByPathname(fe, pathname, false);
    }

    UDF_desc257 findFIDByPathname(UDF_FEDesc fe, String pathname, boolean mirror){
	return findFIDByPathname(fe, pathname, mirror ? 1 : 0);
    }
    /**
       (ディレクトリ構造で見た場合の)Pathnameを指定し、それに対応した FID を取得する。

       @param fe	検索を開始するツリーのFE。nullを指定すると root。
       @param pathname	pathname
       @return FID

       Pathnameは /8:DVD_RTAV/8:VR_MOVIE.VRO のようにディレクトリを /で区切り指定すること。
       また OSTA Compressed Unicodeのエンコードスキームを 8: または 16:で指定しなければならない。

       rootを表わす記号は /である。
       srootを表わす記号は #である。

     */
    UDF_desc257 findFIDByPathname(UDF_FEDesc fe, String pathname, int subno){

	String car = pathname;
	String cdr = "";

	if(pathname == null) {
	    return null;
	}
	if(fe == null){
	    fe = env.getRootFE(subno);
	    //fe = mirror ? env.mirr_root_fe : env.root_fe;
	}
	if(car.length() == 0) {
	    return null;
	}

	if(car.charAt(0) == '/') {
	    //return findFIDByPathname(mirror ? env.mirr_root_fe : env.root_fe, car.substring(1), mirror);
	    return findFIDByPathname(env.getRootFE(subno), car.substring(1), subno);
	}
	if(car.charAt(0) == '#') {
	    //return findFIDByPathname(mirror ? env.mirr_sroot_fe : env.sroot_fe, car.substring(1), mirror);
	    return findFIDByPathname(env.getRootFE(subno), car.substring(1), subno);
	}

	if(car.indexOf("/") >= 0){
	    cdr = car.substring(car.indexOf("/") + 1);
	    car = car.substring(0, car.indexOf("/"));
	}

	if(fe == null)
	    return null;

	UDF_ElementList fids = fe.getDirectoryList();
	for(int i = 0, max = fids.size(); i < max ; ++i){

	    UDF_desc257 fid = (UDF_desc257) fids.elementAt(i);
	    //System.err.println(fid.getFileId().getStringData());
	    if(fid.getFileId().getStringData().equals(car)){
		if (cdr.length() == 0) {
		    return fid;
		}
		return findFIDByPathname(fid.getReferenceTo(), cdr, subno);
	    }
	}

	return null;
    }

    /**
       パーティション番号に対応した、Space Bitmapを作る。

       @param partno	パーティション番号
       @return 生成された UDF_bitmap

       パーティションマップに依存する Space Bitmapを作るには、
       buildPartMapBitmap()を使用すること。

     */
    public UDF_bitmap buildPartBitmap(int partno) throws UDF_PartMapException, UDF_InternalException, UDF_VolException{
	UDF_desc5 d5 = env.getPartDesc(UDF_Env.VDS_AUTO, partno);
	UDF_bitmap bitmap = (UDF_bitmap)createElement("UDF_bitmap", null, "bitmap");

	int sloc = d5.getPartStartingLoc().getIntValue();
	int nsec = d5.getPartLen().getIntValue();
	bitmap.setBitSize(nsec);
	bitmap.set(0, bitmap.getBitSize());

	/*
	  ここでアルゴリズムはシンプル。
	  
	  1) Sparable および type1のextentを予約する。
	  2) Metadata FE, Metadata Mirror FE で示される領域を予約する

	*/
	UDF_Element[] child = getChildren();
	for(int i=0 ; i<child.length ; ++i){
	    UDF_Extent ext = (UDF_Extent)child[i];
	    if(ext.getPartSubno() == 0){
		//UDF_ExtentElem[] ee = ext.getExtent();
		UDF_ExtentElemList eel = ext.getExtentElem();
		for(Iterator it = eel.iterator() ; it.hasNext() ; ){
		    UDF_ExtentElem ee = (UDF_ExtentElem)it.next();
		    if(ee.partno < 0)
			continue;
/* 指定のパーティション番号と、パーティションリファレンス番号から参照されるパーティション番号が一致しなければ処理しない -yazaki- */
/* ※毎回計算するのも馬鹿らしいので、簡単に変換できるテーブルを作っておきたいところだ */
		    if(partno != env.getPartDescByPartRefNo(UDF_Env.VDS_AUTO, ee.partno).getPartNumber().getIntValue()) {
			continue;
		    }
///
		    if(ee.getExtentFlag() == UDF_AD.FLAG2)
			continue;
		    if(!env.isVirtualPartMap(ee.partno) &&
		       !env.isMetadataPartMap(ee.partno)){
		        // multiple-extentに対応する(05/09/07 by seta)
			if(com.udfv.core.UDF_MultipleExtentElem.class.isAssignableFrom(ee.getClass())){
			
			    UDF_MultipleExtentElem mee = (UDF_MultipleExtentElem)ee;
			    int lbn  = mee.getLoc();
			    int len  = (int)(UDF_Util.align(mee.getLen(), env.LBS) / env.LBS);
			    int step = len * mee.getStep();
			    
			    for(int j = 0; j < mee.getTimes(); j++){
			    
//				System.err.println("j:" + j + " lbn = " + lbn + " len = " + len);
				bitmap.unset(lbn, len);
				lbn += step;
			    }
			}
			else
			    bitmap.unset(ee);
		    }
		}
	    }
	}
	UDF_ElementList el = env.getPartMapList();

	for(int i=0 ; i<el.size() ; ++i){
	    if(com.udfv.udf250.UDF_MetadataPartMap.class.isAssignableFrom(el.elementAt(i).getClass())){
		int starting_loc = env.getPartDescByPartRefNo(UDF_Env.VDS_AUTO, i).getPartStartingLoc().getIntValue();
		
		{
		    UDF_Extent ext = env.getPartMapExtent(i, 0);
		    //UDF_ExtentElem[] ee = ext.getExtent();
		    UDF_ExtentElemList eel = ext.getExtentElem();
		    for(Iterator it = eel.iterator() ; it.hasNext() ; ){
			UDF_ExtentElem ee = (UDF_ExtentElem)it.next();
			// XMLから読むときと Imageから読むときで
			// PartExtentの値が内部的に違う。
			// こまった……
			if(ee.getExtentFlag() == UDF_AD.FLAG2)
			    continue;
			if(ee.partno == -1) {

			    /*  bitmapに含まれる領域内に収まるという保証が無いため、長さを調整する -yazaki- */
			    int loc = ee.loc - starting_loc;
			    int len = (int)(ee.len / env.LBS);
			    if ((loc + len) > bitmap.getBitSize()) {
			         len = bitmap.getBitSize() - loc;
			    }
			    bitmap.unset(loc, len);
			    continue;
			}
/* 指定のパーティション番号と、パーティションリファレンス番号から参照されるパーティション番号が一致しなければ処理しない -yazaki- */
			if(partno != env.getPartDescByPartRefNo(UDF_Env.VDS_AUTO, ee.partno).getPartNumber().getIntValue()) {
			    continue;
			}
///
			bitmap.unset(ee.loc, ee.len / env.LBS);
		    }
		}
		{
		    UDF_Extent ext = env.getPartMapExtent(i, 1);
		    
		    //UDF_ExtentElem[] ee = ext.getExtent();
		    //for(int j=0 ; j<ee.length ; ++j){
		    UDF_ExtentElemList eel = ext.getExtentElem();
		    for(Iterator it = eel.iterator() ; it.hasNext() ; ){
			UDF_ExtentElem ee = (UDF_ExtentElem)it.next();
			if(ee.getExtentFlag() == UDF_AD.FLAG2)
			    continue;
			if(ee.partno == -1) {

			    /*  bitmapに含まれる領域内に収まるという保証が無いため、長さを調整する -yazaki- */
			    int loc = ee.loc - starting_loc;
			    int len = (int)(ee.len / env.LBS);
			    if ((loc + len) > bitmap.getBitSize()) {
			         len = bitmap.getBitSize() - loc;
			    }
			    bitmap.unset(loc, len);
			    continue;
			}

/* 指定のパーティション番号と、パーティションリファレンス番号から参照されるパーティション番号が一致しなければ処理しない -yazaki- */
			if(partno != env.getPartDescByPartRefNo(UDF_Env.VDS_AUTO, ee.partno).getPartNumber().getIntValue()) {
			    continue;
			}
///
			bitmap.unset(ee.loc, ee.len / env.LBS);
		    }
		}
	    }
	}
	
	bitmap.updateNodeVal();
	return bitmap;
    }
    /**
       パーティション参照番号に対応した、Space Bitmapを作る。

       @param partrefno	パーティション番号
       @return 生成された UDF_bitmap

       副番号が0のものだけで作る。
     */
    public UDF_bitmap buildPartMapBitmap(int partrefno) throws UDF_PartMapException, UDF_InternalException{
	UDF_bitmap bitmap = (UDF_bitmap)createElement("UDF_bitmap", null, null);
	int nsec = (int)((env.getPartMapExtent(partrefno, 0).getLongSize() / env.LBS));
	bitmap.setBitSize(nsec);
			  
	bitmap.set(0, bitmap.getBitSize());

	UDF_Element[] child = getChildren();
	for(int i=0 ; i<child.length ; ++i){
	    UDF_Extent ext = (UDF_Extent)child[i];
	    if(ext.getPartSubno() == 0){
		UDF_ExtentElemList eel = ext.getExtentElem();
		//UDF_ExtentElem[] ee = ext.getExtent();
		for(Iterator it=eel.iterator() ; it.hasNext() ; ){
		    UDF_ExtentElem ee = (UDF_ExtentElem)it.next();
		    if(ee.partno == partrefno){
		    
		        // multiple-extentに対応する(05/09/07 by seta)
			if(com.udfv.core.UDF_MultipleExtentElem.class.isAssignableFrom(ee.getClass())){
			
			    UDF_MultipleExtentElem mee = (UDF_MultipleExtentElem)ee;
			    int lbn  = mee.getLoc();
			    int len  = (int)(UDF_Util.align(mee.getLen(), env.LBS) / env.LBS);
			    int step = len * mee.getStep();
			    
			    for(int j = 0; j < mee.getTimes(); j++){
			    
				bitmap.unset(lbn, len);
				lbn += step;
			    }
			}
			else
			    bitmap.unset(ee);
		    }
		}
	    }
	}

	bitmap.updateNodeVal();
	return bitmap;
    }


    /*
      指定のタイプの AD を作成する。

      @param alloc_type 作成するADの種類 (0:short_ad, 1:long_ad, 2:ext_ad)
      @return 作成したADのインスタンス。
      @throws UDF_InternalException 未知のADの種類である。

      ※UDF_AD#setDefaultValue()を実行済みのインスタンスが生成される。
    */
    public UDF_AD createAllocDesc(int alloc_type) throws UDF_InternalException {

        String ad_type;
        if (alloc_type == 2) {
            ad_type = "UDF_ext_ad";
        }
        else
        if (alloc_type == 1) {
            ad_type = "UDF_long_ad";
        }
        else
        if (alloc_type == 0) {
            ad_type = "UDF_short_ad";
        }
        else {
            throw new UDF_InternalException(this, "can not create allocation descriptor : type = " + alloc_type);
        }

        UDF_AD ad = (UDF_AD) createElement(ad_type, null, null);
        ad.setDefaultValue();
        return ad;
    }
}



