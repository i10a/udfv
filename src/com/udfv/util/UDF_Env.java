package com.udfv.util;

import java.io.*;
import java.util.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
import com.udfv.exception.*;
import com.udfv.access.*;
import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.encoding.*;
import com.udfv.ecma167.*;

/**
   UDF_Envは UDFVの環境を保存しておくクラスである。
   
   環境は
   1) ユーザが意図的に設定しなければならないもの
   2) Image/XMLから読むときに自動で設定されるもの

   の二種類がある。

   自動で設定される変数について

   A)
   自動で設定される変数は Image中のUDFノードそのものである。
   したがって、その変数の内容を書きかえれば、
   UDF Elementそのものが内部的に書きかえられ、
   その UDF Elementを XMLやファイルに保存すれば、
   その値も書き変わる。

   B)
   いくつかのElementはUDFでは、二重、三重に記録されている。
   そのようなElementは複数環境に記録すること。

   そのなかのどれか1つを利用する場合、それをアプリ側で指定する
   のではなく、アクセスメソッドを使って適切に選ぶメソッドを
   用意すること。

 */
public class UDF_Env
{
    /**
       LBSのサイズ。2048バイト固定
     */
    public static final long LBS = 2048;
    /**
       システムのバージョン
     */
    public static String system_version = "$Rev: 1439 $";
    public static final short MT_DVD_ROM = 0;	//Appendix 6.9(UDF250)
    public static final short MT_DVD_RAM = 1;	//Appendix 6.12.1(UDF250)
    public static final short MT_DVD_RW = 2;	//Appendix 6.12.2(UDF250)
    public static final short MT_DVD_R = 3;	//Appendix 6.12.3(UDF250)

    public static final short MT_DVD_PLUS_R = 4;  //Appendix 6.13(UDF250)
    public static final short MT_DVD_PLUS_RW = 5; //Appendix 6.13(UDF250)

    public static final short MT_CD_R = 8;	//Appendix 6.10.1(UDF250)
    public static final short MT_CD_RW = 9;	//Appendix 6.10.2(UDF250)
    public static final short MT_CD_ROM = 10;	//Appendix 6.10.2(UDF250)

    public static final short MT_BR_ROM = 16;
    public static final short MT_BR_R = 17;
    public static final short MT_BR_RE = 18;

    public static final short MT_LEGACY_MO = 32;

    public static final short MM_PSEUDOOVERWRITABLE = 0;
    public static final short MM_READONLY = 1;
    public static final short MM_WRITEONCE = 2;
    public static final short MM_REWRITABLE = 3;
    public static final short MM_OVERWRITABLE = 4;

    /**
       debug level
     */
    public static int debug_level;
    /**
       debug level
     */
    public static boolean freeze_time;
    /** 
	メディアタイプ
    */
    public short media_type;
    
    /** 
	メディアモード
    */
    public short media_mode;

    /**
       イメージファイル名
    */
    public String image_file;
    /**
       イメージへのコメント
    */
    public String image_comments = "no comments";

    /**
       イメージのサイズ
    */
    public long image_size;
    /**
       イメージのセクタ数

       image_total_sec = image_size / LBS
    */
    public long image_total_sec;

    /**
       ECCのブロックサイズ

       メディアタイプが DVDなら32768。BRなら65536。
    */
    public long ecc_blocksize;
    
    /**
       この値をtrue にすると、XML → Image 変換の際に、
       オリジナルイメージが持つファイルの内容を、作成するイメージにコピーしない。
    */
    public boolean filecopy = true;

    /**
       この値をtrue にすると、Image → XMLの変換の再に
       xmlns属性および &lt;!DOCTYPE&gt;属性を生成しない。
    */
    public boolean xmlns = false;

    /**
       この値をtrue にすると、Image → XMLの変換の再に
       XMLをソートする
    */
    public boolean sortxml = true;

    /**
       この値をtrue にすると、FIDを視覚的に分りやすいXMLデータとして表記する。
       そのような形式になっているかは、 encoding属性で指定する。
     */
    public boolean encodefid = true;

    /**
       XML 上のファイル名を相対パスとして扱うときに使用されるカレントパス。

       nullが設定されているときは絶対パスになる。
    */
    public String path_base = null;
    
    
    /**
       Space Bitmap においてFSDSの領域を、FSDSのサイズ分ではなく、16セクタ分消費していると見せかけるか否か。

       16セクタ分消費していると見せかけるなら true 、実際のサイズに合わせるなら false 。
    */
    public boolean flag_compat_pioneer_fsds_bitmap = false;//true;

    /**
       Rootの UDF_Element。通常は UDF_Image
    */
    public UDF_Element root;

    /**
       コンストラクタ。

       メディアタイプを DVD-REとして作成
    */
    public UDF_Env(){
	init(MT_DVD_RW, MM_OVERWRITABLE);
    }
    /**
       コンストラクタ。

       メディアタイプを指定して作成
    */
    public UDF_Env(short type, short mode){
	init(type, mode);
    }

    public boolean isMediaTypeBR(){
	if(media_type >= MT_BR_ROM  && media_type <= MT_BR_RE)
	    return true;
	return false;
    }

    public boolean isMediaTypeCD(){
	if(media_type >= MT_CD_R  && media_type <= MT_CD_ROM)
	    return true;
	return false;
    }

    public boolean isMediaTypeDVD(){
	if(media_type >= MT_DVD_ROM  && media_type <= MT_DVD_R)
	    return true;
	return false;
    }

    public boolean isMediaOverwritable(){
	return (media_mode == MM_OVERWRITABLE);
    }

    public boolean isMediaWriteonce(){
	return (media_mode == MM_WRITEONCE);
    }
    public boolean isMediaRewritable(){
	return (media_mode == MM_REWRITABLE);
    }

    public boolean isMediaWritable(){
	return isMediaOverwritable() ||
	    isMediaWriteonce() ||
	    isMediaRewritable() ||
	    isMediaPseudoOverwritable();
    }
    public boolean isMediaReadonly(){
	return (media_mode == MM_READONLY);
    }
    public boolean isMediaPseudoOverwritable(){
	return (media_mode == MM_PSEUDOOVERWRITABLE);
    }

    private void init(short type, short mode){
	media_type = type;
	media_mode = mode;

	if(isMediaTypeBR())
	    ecc_blocksize = 65536;
	else
	    ecc_blocksize = 32768;

	image_file= "null";

	reset();
    }  

    public void reset(){
	id_hash = new Hashtable();
	mvds = null;
	rvds = null;
	is = null;
	//fsds = null;

	has_metadata_partmap_main   = false;
	has_metadata_partmap_mirror = false;

	ecma119_SupplementaryVolumeDesc = new UDF_ElementList();
	errorlist = new UDF_ErrorList();

	recorded_num_files = 0;
	recorded_num_directories = 1;

	//part2 = new Hashtable();
	part_e2 = new Hashtable();
	part_ra2 = new Hashtable();

	vat_hash = new Hashtable();

	pm_sb = new Hashtable();
	p_sb = new Hashtable();

	fsds = new Hashtable();
	root_dir = new Hashtable();
	sroot_dir = new Hashtable();
    }

    /**
       XML Elementの id Attributeと UDF Elementの対応をつけるための
       Hash Table
     */
    public Hashtable id_hash;

    /**
     */
    //private Hashtable part2;

    /**
       パーティションマップを格納する

       [logicalvol]+"."+[partref]+"."+[subno]をキーとする Hashtable

       <ul>
       <li>Metadata Partition Mapのときは 2つ作られる。</li>
       <li>Virtual Partition Mapのときは historyの分だけ作られる。</li>
       </ul>
    */
    private Hashtable part_e2;

    /**
       パーティションマップへのアクセサ

       part_ra2[logicalvol][partref][subno]

       <ul>
       <li>Metadata Partition Mapのときは 2つ作られる。</li>
       <li>Virtual Partition Mapのときは historyの分だけ作られる。</li>
       </ul>
    */
    private Hashtable part_ra2;

    /**
       パーティションマップを現わす Extentやアクセサを初期化し、何もない状態にする。
     */
    public void clearPartMap(){
	//part2.clear();
	part_e2.clear();
	part_ra2.clear();
    }

    /**
       パーティションマップを表わす Extentを設定する。
     */
    public void setPartMapExtent(int partrefno, int subno, UDF_Extent ext){
	part_e2.put("." + partrefno + "." + subno, ext);
    }

    /**
       パーティションマップを表わす アクセサを設定する。
     */
    public void setPartMapRandomAccess(int partrefno, int subno, UDF_RandomAccess f){
	part_ra2.put("." + partrefno + "." + subno, f);
    }

    /**
       パーティションマップを表わす Extentを取得する。
     */
    public UDF_Extent getPartMapExtent(int partrefno, int subno) throws UDF_PartMapException{
	UDF_Extent ext = (UDF_Extent)part_e2.get("." + partrefno + "." + subno);
	if(ext == null)
	    throw new UDF_PartMapException(null, "getPartMapExtent: no such partition: partref=" +  partrefno + " subno:" + subno);

	return ext;
    }
    /**
       パーティションマップへのアクセサを取得する。
     */
    public UDF_RandomAccess getPartMapRandomAccess(int partrefno, int subno) throws UDF_PartMapException{
	UDF_RandomAccess f = (UDF_RandomAccess)part_ra2.get("." + partrefno + "." + subno);
	if(f == null && !isMetadataPartMap(partrefno) && !isVirtualPartMap(partrefno))
	    f = (UDF_RandomAccess)part_ra2.get("." + partrefno + "." + 0);
	if(f == null)
	    throw new UDF_PartMapException(null, "getPartMapRandomAccess: no such partition: partref=" +  partrefno + " subno:" + subno);

	return f;
    }
    /**
       パーティションマップの全ての副番号に対応した Extentを取得する。

       
     */
    public UDF_ElementList getPartMapSubList(int partrefno){
	UDF_ElementList list = new UDF_ElementList();
	for(int i=0 ; ; ++i){
	    UDF_Extent ext = (UDF_Extent)part_e2.get("." + partrefno + "." + i);
	    if(ext == null)
		break;
	    list.add(ext);
	}
	return list;
    }

    /**
       イメージへのアクセサ
    */
    public UDF_RandomAccess f;

    /** 
	パーティションマップ
    */
    public UDF_PartMap part[];

    /**
       パーティションを表す Extent
    */
    //public UDF_Extent part_e[];

    /*
       パーティションマップへのアクセサ

       getPartMapRandomAccess()を使用してください。
    */
    //public UDF_RandomAccessExtent part_ra[];

    /*
      part_sb[]のかわりに

      getPartBitmap() または、
      getPartMapBitmap() を御使用ください。

      2005/6/4 issei

     */
    //public UDF_bitmap part_sb[];


    /**
       Partition Map のBitmap
     */
    private Hashtable pm_sb;
    /**
       Partition のBitmap
     */
    private Hashtable p_sb;

    /**
       Partitionに対応するBitmap情報を初期化する。
     */
    public void clearPartBitmap(){
	p_sb.clear();
    }
    /**
       Partition番号に対応する Bitmapを UDF環境に格納する。
       
       @param partno	パーティション番号
       @param bitmap	ビットマップ

       Bitmapは 文字列 "partno"をキーとして格納されている。

       このメソッドは readFrom()や readFromXML()から自動で実行される。
       特にユーザが気にする必用はない。
     */
    public void setPartBitmap(int partno, UDF_bitmap bitmap) throws UDF_VolException{
	p_sb.put("" + partno, bitmap);
    }
    /**
       Partition番号に対応する Bitmapを UDF環境より取得する。
       
       @param partno	パーティション番号

       Partitionに対し、SBがあるならば、そのBitmapを、ない場合は、
       SBを再構成をし、それを返す。
     */
    public UDF_bitmap getPartBitmap(int partno) throws UDF_VolException{
	return (UDF_bitmap)p_sb.get("" + partno);
    }

    /**
       PartitionMapに対応する Bitmap状況を初期化する。
    */
    public void clearPartMapBitmap(){
	pm_sb.clear();
    }
    /**
       PartitionMap の Bitmapを設定する。

       @param partrefno	パーティション参照番号
       @param bitmap	ビットマップ

       ・ readFrom() -> udf250.UDF_Image.readMetadataPartMap()で設定される。
    */
    public void setPartMapBitmap(int partrefno, UDF_bitmap bitmap) throws UDF_VolException{
	pm_sb.put("." + partrefno, bitmap);
    }
    /**
       PartitionMap に対応する Bitmapを取得する。

       もし partrefnoの Partition Mapが Metadata Partition Mapでかつ、
       Metadata Bitmapがある場合は その Bitmapを返す。

       無い場合は、Bitmapを再構成をし、それを返す。

       Metadata Partition Map以外の場合は、PartitionMapに対応する Partitionの
       Bitmapを返す。

       そのBitmapも無い場合は、Bitmapを再構成し、それを返す。

       注)このメソッドがそう実装されているわけではなく、そうなるように setPartMapBitmapが呼ばれているだけである。

       @param partrefno	パーティション参照番号
       @return ビットマップ
       @see UDF_Image#recalcSB()
    */
    public UDF_bitmap getPartMapBitmap(int partrefno) throws UDF_VolException{
	return (UDF_bitmap) pm_sb.get("." + partrefno);
    }

    private Hashtable root_dir;
    private Hashtable sroot_dir;

    /**
       Root Directoryを取得する。

       @param subno	副番号
       @return Root Directory FE
     */
    public UDF_FEDesc getRootFE(int subno){
	return (UDF_FEDesc)root_dir.get("." + subno);
    }

    /**
       System Stream Root Directoryを取得する。

       @param subno	副番号
       @return System Stream Root Directory FE
     */
    public UDF_FEDesc getSRootFE(int subno){
	return (UDF_FEDesc)sroot_dir.get("." + subno);
    }

    public void setRootFE(int subno, UDF_FEDesc desc){
	root_dir.put("." + subno, desc);
    }
    public void setSRootFE(int subno, UDF_FEDesc desc){
	sroot_dir.put("." + subno, desc);
    }

    public void clearRootFE(){
	root_dir.clear();
    }
    public void clearSRootFE(){
	sroot_dir.clear();
    }


    //public UDF_FEDesc root_fe;
    //public UDF_FEDesc mirr_root_fe;

    //public UDF_FEDesc sroot_fe;
    //public UDF_FEDesc mirr_sroot_fe;

    //public UDF_Encoding descCharacterSet;
    //public UDF_Encoding explantoryCharacterSet;
    //public UDF_Encoding logicalVolIdCharSet;
    //public UDF_Encoding fileSetCharSet;

    //　ベリファイの基準とするUDF リビジョン　//
    public int udf_revision = -1;

    //　UDF から読み取ったリビジョン　//
    public int recorded_udf_revision = 0;//0x200;

    // UDFに実際に記録されているファイルの数
    public int recorded_num_files = 0;

    // UDFに実際に記録されているディレクトリの数
    public int recorded_num_directories = 0;

    /**
       リビジョンを取得する。

    */
    public int getUDFRevision( ) throws UDF_VolException {
	UDF_VDS vds = (mvds != null) ? mvds : rvds;

	try{
	    UDF_ElementList desc4_list = vds.getImplUseVolDesc();

	    UDF_desc4 desc4 = (UDF_desc4)desc4_list.elementAt(0);
	    return UDF_Util.b2uint16(desc4.getImplId().getIdSuffix().getData(), 0);
	}
	catch(UDF_VolException e){
	    e.printStackTrace();

	    UDF_ElementList desc6_list = vds.getPrevailingLogicalVolDesc();
	    UDF_desc6 desc6 = (UDF_desc6)desc6_list.elementAt(0); // 1つ目決めうち
		
	    return UDF_Util.b2uint16(desc6.getDomainId().getIdSuffix().getData(), 0);
        }
    }

    /**
       イメージのサイズを求める

       @return サイズ(バイト)
     */
    public long getImageSize() throws UDF_InternalException, IOException{
	if(f == null)
	    throw new UDF_InternalException(null, "UDF_Image.getLongSize(): No RandomAccessFile");
	return f.length();
    }

    /**
       AVDPに記録されている値からMVDSの位置を求める。
       そのため実際にボリュームを読み込むのに使用している有効なMVDSの位置と異なる可能性がある。

       @return 位置(単位セクタ)
    */
    public int getMVDSLoc() throws UDF_VolException {
	return getAnchorVolDescPointer().getMVDSLoc();
    }
    /**
       AVDPに記録されている値からRVDSの位置を求める。
       そのため実際にボリュームを読み込むのに使用している有効なRVDSの位置と異なる可能性がある。

       @return 位置(単位セクタ)
    */
    public int getRVDSLoc() throws UDF_VolException{
	return getAnchorVolDescPointer().getRVDSLoc();
    }
    /**
       AVDPに記録されている値からMVDSの長さを求める。
       そのため実際にボリュームを読み込むのに使用している有効なMVDSの長さと異なる可能性がある。

       @return 長さ(単位バイト)
    */
    public long getMVDSLen() throws UDF_VolException{
	return getAnchorVolDescPointer().getMVDSLen();
    }
    /**
       AVDPに記録されている値からRVDSの長さを求める。
       そのため実際にボリュームを読み込むのに使用している有効なRVDSの長さと異なる可能性がある。

       @return 長さ(単位バイト)
    */
    public long getRVDSLen() throws UDF_VolException{
	return getAnchorVolDescPointer().getRVDSLen();
    }
    
    /**
       実際にvolumeの読み込みに使用しているMVDSの位置を求める。

       @return 位置(単位セクタ)
    */
    public int getMVDSLoc2() throws UDF_VolException {
	UDF_VDS udfvds = getVDS(VDS_MAIN);
	return (int)(udfvds.getGlobalPoint() / LBS);
    }
    /**
       実際にvolumeの読み込みに使用しているRVDSの位置を求める。

       @return 位置(単位セクタ)
    */
    public int getRVDSLoc2() throws UDF_VolException{
	UDF_VDS udfvds = getVDS(VDS_RESERVE);
	return (int)(udfvds.getGlobalPoint() / LBS);
    }
    /**
       実際にvolumeの読み込みに使用しているMVDSの長さを求める。

       @return 長さ(単位バイト)
    */
    public long getMVDSLen2() throws UDF_VolException{
	UDF_VDS udfvds = getVDS(VDS_MAIN);
	return udfvds.getLongSize();
    }
    /**
       実際にvolumeの読み込みに使用しているRVDSの長さを求める。

       @return 長さ(単位バイト)
    */
    public long getRVDSLen2() throws UDF_VolException{
	UDF_VDS udfvds = getVDS(VDS_RESERVE);
	return udfvds.getLongSize();
    }

    /**
       LVISの位置を求める。

       @return 位置(単位セクタ)
    */
    public int getISLoc() throws UDF_VolException{
	return getLogicalVolDesc(VDS_AUTO).getIntegritySeqExtent().getExtentLoc().getIntValue();
    }
    /**
       LVISの長さを求める。

       @return 長さ(単位バイト)
    */
    public long getISLen() throws UDF_VolException{
	return getLogicalVolDesc(VDS_AUTO).getIntegritySeqExtent().getExtentLen().getLongValue();
    }

    /**
       Partitionの開始位置を求める。

       @return 位置(単位セクタ)
    */
    public int getPartStartingLoc(int partno) throws UDF_VolException{
	return getPartDesc(VDS_AUTO, partno).getPartStartingLoc().getIntValue();
    }

    /**
       Partitionの長さを求める。

       @return 長さ(単位バイト)
    */
    public long getPartLen(int partno) throws UDF_VolException{
	return getPartDesc(VDS_AUTO, partno).getPartLen().getLongValue() * LBS;
    }

    /**
       PartitionMapの属する Partitionの開始位置を求める。

       @param	partrefno	パーティション参照番号
       @return 位置(単位セクタ)
    */
    public int getPartStartingLocByPartRefNo(int partrefno) throws UDF_PartMapException{
	try{
	    int partno = getPartDescByPartRefNo(UDF_Env.VDS_AUTO, partrefno).getPartNumber().getIntValue();
	    return getPartStartingLoc(partno);
	}
	catch(UDF_VolException e){
	    panic(e);
	    throw new UDF_PartMapException(e);
	}
    }

    /**
       PartitionMapの属する Partitionの長さを求める。

       @param	partrefno	パーティション参照番号
       @return 長さ(単位バイト)
    */
    public long getPartLenByPartRefNo(int partrefno) throws UDF_PartMapException{
	try{
	    int partno = getPartDescByPartRefNo(UDF_Env.VDS_AUTO, partrefno).getPartNumber().getIntValue();
	    return getPartLen(partno);
	}
	catch(UDF_VolException e){
	    panic(e);
	    throw new UDF_PartMapException(e);
	}
    }

    /*
      getPartStartingLoc()を使おう
     */
    //public int part_loc;
    /*
      getPartLen()を使おう
     */
    //public long part_len;

    /*
    public int fsds_lbn;
    public int fsds_partno;
    public long fsds_len;
    */
    /*
    public int root_lbn;
    public int root_partno;
    public long root_len;

    public int stream_lbn;
    public int stream_partno;
    public long stream_len;
    */

    /**
       このボリュームの Metadata Partition Map を取得する。

       複数ある場合は、どれか一つを返す。

       ない場合は nullを返す。

       このメソッド互換のために用意しているものである。

       LVDや MPMは複数ありうるので、このメソッドを使用しない実装が望ましい。
       
    */
    public com.udfv.udf250.UDF_MetadataPartMap getMetadataPartMap() throws UDF_PartMapException{
	try{
	    UDF_ElementList d6 = getVDS(VDS_AUTO).getPrevailingLogicalVolDesc();
	    
	    //for(int i=0 ; i<d6.size() ; ++i){
	    for(Iterator i=d6.iterator() ; i.hasNext() ; ){
		UDF_desc6 desc6 = (UDF_desc6)i.next();//elementAt(i);
		UDF_ElementList child = desc6.getPartMaps().getChildList();
		//for(int j=0 ; j<child.length ; ++j){
		for(Iterator j=child.iterator(); j.hasNext() ; ){
		    Object o = j.next();
		    if(com.udfv.udf250.UDF_MetadataPartMap.class.isAssignableFrom(o.getClass())){
			return (com.udfv.udf250.UDF_MetadataPartMap)o;
		    }
		}
	    }
	}
	catch(UDF_VolException e){
	    throw new UDF_PartMapException(e);
	}
	return null;
    }

    /**
       このボリュームが Metadata Partition Map を持っているかを求める。

       持っているとは、

       Metadata Partition Mapが LVDに記録されている

       ことである。

       LVDが複数ある場合は、どれか1つにあればよい。
    */
    public boolean hasMetadataPartMap() throws UDF_VolException{
	UDF_ElementList d6 = getVDS(VDS_AUTO).getPrevailingLogicalVolDesc();
	
	for(Iterator i=d6.iterator() ; i.hasNext(); ){
	    UDF_desc6 desc6 = (UDF_desc6)i.next();
	    //UDF_Element[] child = desc6.getPartMaps().getChildren();
	    UDF_ElementList child = desc6.getPartMaps().getChildList();
	    for(Iterator j=child.iterator() ; j.hasNext() ; ){
		Object o = j.next();
		if(com.udfv.udf250.UDF_MetadataPartMap.class.isAssignableFrom(o.getClass())){
		    return true;
		}
	    }
	}
	return false;
    }
    /**
       このボリュームが Virtual Partition Map を持っているかを求める。

       持っているとは、

       Virtual Partition Mapが LVDに記録されている

       ことである。

       LVDが複数ある場合は、どれか1つにあればよい。
    */
    public boolean hasVirtualPartMap() throws UDF_VolException{
	UDF_ElementList d6 = getVDS(VDS_AUTO).getPrevailingLogicalVolDesc();
	
	//for(int i=0 ; i<d6.size() ; ++i){
	for(Iterator i=d6.iterator() ; i.hasNext() ; ){
	    UDF_desc6 desc6 = (UDF_desc6)i.next();
	    UDF_ElementList child = desc6.getPartMaps().getChildList();
	    for(Iterator j=child.iterator() ; j.hasNext(); ){
		Object o = j.next();
		if(com.udfv.udf150.UDF_VirtualPartMap.class.isAssignableFrom(o.getClass())){
		    return true;
		}
	    }
	}
	return false;
    }

    /**
       Metadata Main 領域が存在しているかどうかを示すフラグ。
       
       hasMetadataPartmap()がtrue のとき、基本的にこの値もtrue になる。
       hasMetadataPartmap()がtrue でこの値がfalse のときは、
       Metadata Main 領域を正しく読み込めていない状態を表す。
       hasMetadataPartmap()がfalseのときは、この値も必ずfalseである。
    */
    public boolean has_metadata_partmap_main;
    /**
       Metadata Mirror 領域が存在しているかどうかを示すフラグ。
       
       hasMetadataPartmap()がtrue でこの値がfalse のときは、
       Metadata Partition Map のDuplicate Flag がオフになっているか、
       Metadata Mirror 領域を正しく読み込めていない状態を表す。
       hasMetadataPartmap()がfalseのときは、この値も必ずfalseである。
    */
    public boolean has_metadata_partmap_mirror;

    /**
       指定したパーティション番号は mirrorを持つかどうかを調べる。
       
       @param partno	パーティション番号。
       @return false: mirrorを持たない true: mirrorを持つ

       @throws UDFPartMapException	有効なパーティションマップがない
    */
    public boolean hasMirrorPartmap(int partno) throws UDF_PartMapException{
	/*
	if(mirr_part_e == null)
	    return false;
	return mirr_part_e[partno] != null;
	*/
	if(isMetadataPartMap(partno) && getPartMapExtent(partno, 1) != null)
	    return true;
	return false;
    }
    /**
       指定したパーティション番号が Virtual Partition Mapかどうかを調べる。
       
       @param partno	パーティション番号。
    */
    public boolean isVirtualPartMap(int partno) throws UDF_PartMapException{
	try{
	    UDF_PartMap pm = getPartMap(VDS_AUTO, partno);
	    if(com.udfv.udf150.UDF_VirtualPartMap.class.isAssignableFrom(pm.getClass()))
		return true;
	    return false;
	}
	catch(UDF_VolException e){
	    panic(e);
	    throw new UDF_PartMapException(e);
	}
    }

    /**
       指定したパーティション番号が Metadata Partition Mapかどうかを調べる。
       
       @param partrefno	パーティション番号。
    */
    public boolean isMetadataPartMap(int partrefno) throws UDF_PartMapException{
	try{
	    UDF_PartMap pm = getPartMap(VDS_AUTO, partrefno);
	    if(com.udfv.udf250.UDF_MetadataPartMap.class.isAssignableFrom(pm.getClass()))
		return true;
	    return false;
	}
	catch(UDF_VolException e){
	    panic(e);
	    throw new UDF_PartMapException(e);
	}
    }
    //
    //自動で設定される変数およびアクセスメソッドのセクション
    //

    /**
       AVDP

       <ul>
       <li>0: 1st anchor(256)</li>
       <li>1: 3rd anchor(N-257)</li>
       <li>2: 2nd anchor(N-1)</li>
       </ol>
     */
    public UDF_desc2 anchorVolDescPointer[] = new UDF_desc2[3];
    /**
       Main Volume Descriptor Sequence
     */
    public UDF_VDS mvds;
    /**
       Reserve Volume Descriptor Sequence
     */
    public UDF_VDS rvds;
    /**
       Logical Volume Integrity Sequence
     */
    public UDF_IS is;

    /**
       FSDS
     */
    //public UDF_FDS fsds;
    //public UDF_FDS mirr_fsds;

    private Hashtable fsds;

    public void clearFDS(){
	fsds.clear();
    }
    /**
       FDSを設定する。

       @param lv	Logical Volume
       @param subno	副番号
       @param fds	FileSet Desc Seq.

       Logical Volumeは現在無視される。
     */
    public void setFDS(String lv, int subno, UDF_FDS fds){
	fsds.put("." + subno, fds);
    }

    /**
       FDSを取得する。

       @param lv	Logical Volume
       @param subno	副番号
       @param fds	FileSet Desc Seq.

       Logical Volumeは現在無視される。
     */
    public UDF_FDS getFDS(String lv, int subno) throws UDF_VolException{
	UDF_FDS fds = (UDF_FDS) fsds.get("." + subno);
	if(fds == null)
	    throw new UDF_VolException(null, "no FSDS: subno=" + subno);

	return fds;
    }

    public static final short VDS_AUTO = 0;
    public static final short VDS_MAIN = 1;
    public static final short VDS_RESERVE = 2;

    /**
       VDSを取得する

       @param vdstype	どのVDSから取得するか(VDS_AUTO, VDS_MAIN, VDS_RESERVE)

     */
    private UDF_VDS getVDS(short vdstype) throws UDF_VolException{
	if(vdstype == VDS_MAIN && mvds != null)
	    return mvds;
	else if(vdstype == VDS_RESERVE && rvds != null)
	    return rvds;
	else if(vdstype == VDS_AUTO){
	    if(mvds != null)
		return mvds;
	    if(rvds != null)
		return rvds;
	}
	throw new UDF_VolException(null, "No volume");
    }
    /**
       AVDPを取得する。

       @throws UDF_VolException 有効なAVDPが一つもない。
     */
    public UDF_desc2 getAnchorVolDescPointer() throws UDF_VolException{
	if(anchorVolDescPointer[0] != null)
	    return anchorVolDescPointer[0];
	else if(anchorVolDescPointer[1] != null)
	    return anchorVolDescPointer[1];
	else if(anchorVolDescPointer[2] != null)
	    return anchorVolDescPointer[2];
	throw new UDF_VolException(null, "No anchor volume descriptor:");
    }

    /**
       このボリュームにある Volume Sequence Numberに相当する
       primary volume descriptorを取得する

       @param vdstype	どのVDSから取得するか(VDS_AUTO, VDS_MAIN, VDS_RESERVE)
       @param volno	Volume Sequence Number

       @throws UDF_VolException volno ボリューム存在しない。
    */
    public UDF_desc1 getPrimaryVolDesc(short vdstype, int volno) throws UDF_VolException{
	return (UDF_desc1)getVDS(vdstype).getPrimaryVolDesc(volno);
    }

    /**
       Implementation Use Volume Descriptor を取得する。

       @param vdstype	どのVDSから取得するか(VDS_AUTO, VDS_MAIN, VDS_RESERVE)
       @return Implementation&nbsp;Use&nbsp;Volume&nbsp;Descriptor&nbsp;の配列

       @throws UDF_VolException volno ボリューム存在しない。
    */
    public UDF_desc4 [] getImplUseVolDesc(short vdstype) throws UDF_VolException{
	UDF_ElementList el = getVDS(vdstype).getImplUseVolDesc();
	UDF_desc4 [] desc4 = new UDF_desc4[el.size()];
	for (int i = 0, max = el.size(); i < max; i++) {
	    desc4[i] = (UDF_desc4) el.elementAt(i);
	}
	return desc4;
    }

    /**
       このボリュームにある Partition Map番号に対応した Partition Descriptorを取得する

       @param partrefno パーティション参照番号
    */
    public UDF_desc5 getPartDescByPartRefNo(short vdstype, int partrefno) throws UDF_VolException{
	int partno = part[partrefno].getPartNumber().getIntValue();
	return (UDF_desc5)getPartDesc(vdstype, partno);
    }

    /**
       このボリュームにある Partition Map番号に対応した Partition Descriptorを取得する

       @param partrefno パーティション参照番号
    */
    public UDF_desc5 getPartDescByPartRefNo(int partrefno) throws UDF_VolException{
	int partno = part[partrefno].getPartNumber().getIntValue();
	return (UDF_desc5)getPartDesc(VDS_AUTO, partno);
    }

    /**
       このボリュームにある Partition Descriptorを取得する
       対応するものがなければ VolExceptionをスローする。

       @param pn パーティション番号
    */
    public UDF_desc5 getPartDesc(short vdstype, int pn) throws UDF_VolException{
	return (UDF_desc5)getVDS(vdstype).getPartDesc(pn);
    }

    /**
       このボリュームにある Prevailing Partition Descriptorを取得する。
    */
    public UDF_ElementList getPartDescList(short vdstype) throws UDF_VolException{
	return getVDS(vdstype).getPrevailingPartDesc();
    }

    /**
       このボリュームにある Prevailing Logical Volume Descriptorで一番
       LVIDが小さいものを返す
    */
    public UDF_desc6 getLogicalVolDesc(short vdstype) throws UDF_VolException{
	return (UDF_desc6) getVDS(vdstype).getPrevailingLogicalVolDesc().elementAt(0);
    }

    /**
       このボリュームにある Prevailing Logical Volume Descriptorで一番
       LVIDが小さいものを返す
    */
    public UDF_desc6 getLogicalVolDesc() throws UDF_VolException{
	return (UDF_desc6) getVDS(VDS_AUTO).getPrevailingLogicalVolDesc().elementAt(0);
    }

    /**
       このボリュームにある Partition Mapを返す。

       @param vdstype	どのVDSから取得するか(VDS_AUTO, VDS_MAIN, VDS_RESERVE)
     */
    public UDF_PartMap getPartMap(short vdstype, int partrefno) throws UDF_VolException, UDF_PartMapException{
	int saved_partrefno = partrefno;
	UDF_ElementList d6 = getVDS(vdstype).getPrevailingLogicalVolDesc();

	for(Iterator i=d6.iterator() ; i.hasNext() && partrefno >= 0; ){
	    UDF_desc6 desc6 = (UDF_desc6)i.next();
	    UDF_Element[] child = desc6.getPartMaps().getChildren();
	    if(child.length >= partrefno)
		return (UDF_PartMap)child[partrefno];
	    partrefno -= child.length;
	}

	throw new UDF_PartMapException(null, "No sucn partitionmap (partition reference no=" + saved_partrefno + ":" + d6.size() + ")");
    }

    /**
       このボリュームにある Partition Mapを返す。
     */
    public UDF_PartMap getPartMap(int partrefno) throws UDF_PartMapException{
	int saved_partrefno = partrefno;
	try{
	    UDF_ElementList d6 = getVDS(VDS_AUTO).getPrevailingLogicalVolDesc();
	    
	    for(Iterator i=d6.iterator(); i.hasNext(); ){//int i=0 ; i<d6.size() && partrefno >= 0; ++i){
		UDF_desc6 desc6 = (UDF_desc6)i.next();
		UDF_Element[] child = desc6.getPartMaps().getChildren();
		if(child.length >= partrefno)
		    return (UDF_PartMap)child[partrefno];
		partrefno -= child.length;
	    }
	    throw new UDF_PartMapException(null, "No sucn partitionmap (partition reference no=" + saved_partrefno + ":" + d6.size() + ")");
	}
	catch(UDF_VolException e){
	    panic(e);
	    throw new UDF_PartMapException(e);
	}
    }

    public UDF_ElementList getPartMapList() throws UDF_PartMapException{
	try{
	    UDF_ElementList d6 = getVDS(VDS_AUTO).getPrevailingLogicalVolDesc();
	    UDF_desc6 desc6 = (UDF_desc6)d6.firstElement();
	    
	    return desc6.getPartMaps().getChildList();
	}
	catch(UDF_VolException e){
	    panic(e);
	    throw new UDF_PartMapException(e);
	}
    }
    /*
      VATのあるPMを探す
     */
    public int getVATPartMap() throws UDF_PartMapException{
        //UDF1.50 p.6
        UDF_ElementList pmlist = getPartMapList();
        int ref_partmapno = 0;

        for(Iterator i=pmlist.iterator(); i.hasNext(); ){
            UDF_Element itm = (UDF_Element)i.next();
            if(itm.getName().equals("UDF_part_map1"))
                break;
            ++ref_partmapno;
        }
        return ref_partmapno;
    }

    /**
       このボリュームにある Prevailing Partition Descriptor内に
       あるPHDを取得する。
    */
    public UDF_ElementList getPartHeaderDescList(short voltype) throws UDF_VolException{
	
	UDF_ElementList el = new UDF_ElementList();
	UDF_ElementList d5l = getPartDescList(voltype);

	for(Iterator i=d5l.iterator() ; i.hasNext() ; ){
	    UDF_desc5 d5 = (UDF_desc5)i.next();
	    el.add(d5.getPartContentsUse().getFirstChild());
	}

	return el;
    }

    public UDF_PartHeaderDesc getPartHeaderDesc(short voltype, int partno) throws UDF_VolException{
	UDF_desc5 d5 = getPartDesc(voltype, partno);
	return d5.getPartHeaderDesc();
    }

    public UDF_desc9 getLogicalVolIntegrityDesc() throws UDF_VolException{
	if(is != null)
	    return is.getPrevailingLogicalVolIntegrityDesc();
	throw new UDF_VolException(null, "No logical volume integiry descriptor");
    }
    public UDF_LogicalVolHeaderDesc getLogicalVolHeaderDesc() throws UDF_VolException{
	if(is != null)
	    return is.getPrevailingLogicalVolHeaderDesc();
	throw new UDF_VolException(null, "No logical volume integiry descriptor");
    }

    public com.udfv.udf102.UDF_desc9_ImplUse getLVIDImplUse() throws UDF_VolException{
	UDF_desc9 d9 =  getLogicalVolIntegrityDesc();
	if(d9 != null){
	    UDF_Element el = d9.getImplUse().getFirstChild();
	    if(el != null)
		return (com.udfv.udf102.UDF_desc9_ImplUse)el;
	}
	throw new UDF_VolException(null, "No Implmentation Use in logical volume integiry descriptor");
    }
    public long getNumberOfFiles() throws UDF_VolException{
	return getLVIDImplUse().getNumberOfFiles().getLongValue();
    }
    public long getNumberOfDirectories() throws UDF_VolException{
	return getLVIDImplUse().getNumberOfDirectories().getLongValue();
    }

    public long setNumberOfFiles(long n) throws UDF_VolException{
	getLVIDImplUse().getNumberOfFiles().setValue(n);
	return n;
    }
    public long setNumberOfDirectories(long n) throws UDF_VolException{
	getLVIDImplUse().getNumberOfDirectories().setValue(n);
	return n;
    }

    public long addNumberOfFiles(long n) throws UDF_VolException{
	return getLVIDImplUse().addFiles(n);
							 
    }
    public long addNumberOfDirectories(long n) throws UDF_VolException{
	return getLVIDImplUse().addDirectories(n);
	//return setNumberOfDirectories(getNumberOfDirectories() + n);
    }

    /**
       File Set Descriptor を取得する

       @param subno	副番号
       @return FSD
     */
    public UDF_desc256 getFileSetDesc(int subno) throws UDF_VolException{
	return (UDF_desc256)getFDS(null, subno).getPrevailingFileSetDesc().lastElement();
    }

    private Hashtable vat_hash;
    /**
       VATを取得する。
    */
    public com.udfv.udf150.UDF_VirtualAllocTable getVAT(int vatno){
	return (com.udfv.udf150.UDF_VirtualAllocTable)vat_hash.get("" + vatno);
    }
    /**
       VATを設定する。
    */
    public void setVAT(int vatno, com.udfv.udf150.UDF_VirtualAllocTable vat){
	vat_hash.put("" + vatno, (com.udfv.udf150.UDF_VirtualAllocTable)vat);
    }
    /**
       VATの数(何代あるか)を取得する。

       VATが記録されていない場合は -1を返す。
     */
    public int getNumOfVAT(){
	int vatno = -1;
	while(getVAT(vatno) != null)
	    ++vatno;

	return vatno;
    }

    public com.udfv.ecma167.UDF_VRS vrs1;


    /**
       Priary Volume Descriptorを格納する
    */
    public com.udfv.ecma119.UDF_ECMA119_CD001_1 ecma119_PrimaryVolumeDesc;
    /**
       Supplementary Volume Descriptorを格納する
    */
    public UDF_ElementList ecma119_SupplementaryVolumeDesc = new UDF_ElementList();

    public UDF_ErrorList errorlist = new UDF_ErrorList();
    
    public void debug(){
	Field[] f = UDF_Env.class.getFields();
	try{
	    for(int i=0 ; i<f.length ; ++i){
		int mod = f[i].getModifiers();
		Class type = f[i].getType();
		if((mod & Modifier.FINAL) == 0){//finalは表示しない
		    if(type == short.class || // short int boolean long Stringのみ表示する
		       type == int.class ||
		       type == boolean.class ||
		       type == long.class ||
		       type == String.class)
			UDF_Base.debugMsg(this, 2, f[i].getName() + ":" + f[i].get(this));
		}
	    }
	}
	catch(IllegalAccessException e){
	    ;
	}
    }

    /**
       相対パスを受けて、基底となるパスを加えて絶対パスを返す。

       @param relative 相対パス
       @return 絶対パス

       UDF_Env#path_base を基底のパスとする。
    */
    public String absolutePath(String relative) {

        if (path_base == null) {
            return relative;
        }
        if (relative.length() < 1) {
            return relative;
        }
        if (relative.charAt(0) == '/') {
            return relative;
        }
        if (relative.equals("null")) {
            return relative;
        }
        return (path_base + "/" + relative);
    }

    /**
       コマンドライン引数を解析し、UDF の環境変数としてその値を設定する。
       
       @param argv
       コマンドライン引数を指定する。
       引数には、UDF_Env.java で定義されている変数に'-'を付加したものを指定できる。
       引数名を指定し、空白文字の後にその値を指定する。
       例：
       <blockquote><pre>
       ./Xml2Img -ecc_blocksize 32768 -filecopy false hoge.XML hoge.IMG
       </pre></blockquote>
       
       @return  解析に使用された部分以外のコマンドライン引数が返る。
                上記の例の場合、"hoge.XML hoge.IMG" が返される。
    */
    public String[] parseOpt(String argv[]){
	Vector v = new Vector();
	for(int i=0 ; i<argv.length ; ++i){

	    int len = argv[i].length();

	    //　０文字はありえない　//
	    if (len < 1) {
		continue;
	    }
	    //　一文字のオプションはない　//
	    if (len < 2) {
		v.add(argv[i]);
		continue;
	    }
	    //　オプション設定でないときはargvとして残す　//
	    if(argv[i].charAt(0) != '-'){
		v.add(argv[i]);
		continue;
	    }
	    //　オプションの引数に相当するものが無いときはUDF_Env のフィールド変更であることはない　//
	    if ((i + 1) >= argv.length) {
		v.add(argv[i]);
		continue;
	    }

	    String optname = argv[i].substring(1);
	    String arg = argv[i + 1];

	    // UDF_Envのフィールドを設定する
	    try{
		Field f = UDF_Env.class.getField(optname);
		//System.err.println(f);
		if(f.getType() == short.class)
		    f.setShort(this, Short.parseShort(arg));
		else if(f.getType() == int.class)
		    f.setInt(this, Integer.parseInt(arg));
		else if(f.getType() == long.class)
		    f.setLong(this, Long.parseLong(arg));
		else if(f.getType() == boolean.class){
		    if(arg.equals("true"))
			f.setBoolean(this, true);
		    else
			f.setBoolean(this, false);
		}
		else if(f.getType() == String.class)
		    f.set(this, arg);

		i++;
	    }
	    catch(NoSuchFieldException e) {
	    //System.err.println("No such option: -" + optname);
	    //扱えなかったオプションを戻す
		v.add(argv[i]);
	    }
	    catch(IllegalArgumentException e) {
		System.err.println("Illegal Argument: -" + optname + " " + arg);
	    }
	    catch(IllegalAccessException e) {
		System.err.println("Illegal Access: -" + optname + " " + arg);
	    }
	}

	String ret[] = new String[v.size()];
	for(int i=0 ; i<ret.length ; ++i){
	    ret[i] = (String)v.elementAt(i);
	}
	
	if(isMediaTypeBR())
	    ecc_blocksize = 65536;
	else
	    ecc_blocksize = 32768;
	    
	return ret;
    }

    /**
       env.debug_levelの値が level以上ならばデバッグメッセージを出力する

       @param level デバッグレベル
       @param o 出力値
     */
    public static void debugMsg(int level, Object o){
	if(debug_level >= level)
	    System.err.println(o);
    }
    /**
       env.debug_levelの値が level以上ならばデバッグメッセージを出力する

       @param level デバッグレベル
       @param o 出力値
     */
    public static void debugMsg(int level, int o){
	if(debug_level >= level)
	    System.err.println(o);
    }
    /**
       env.debug_levelの値が level以上ならばデバッグメッセージを出力する

       @param level デバッグレベル
       @param o 出力値
     */
    public static void debugMsg(int level, long o){
	if(debug_level >= level)
	    System.err.println(o);
    }

    void panic(Exception e){
	System.err.println("PANIC");
	e.printStackTrace();
	System.exit(0);
    }

}
