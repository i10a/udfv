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
  $Id: UDF_SortXML.java,v 1.1.2.11 2003/07/25 02:04:29 seta Exp $
 */
package com.udfv.util;

import java.io.*;
import java.util.*;
import java.lang.*;
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
import org.apache.xml.serialize.*;
import com.udfv.exception.*;
import com.udfv.udf250.*;
import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.access.*;
import com.udfv.exception.*;
import com.udfv.ecma167.*;

/**
<DL>
<DT>概要</DT>
<DD>
XMLドキュメントをlbn またはsec 属性値でソートするためのクラス。
</DD>
<DT>詳細</DT>
<DD>
UDF_XMLクラスは、指定されたXMLドキュメント内のudfノード直下のノードリストを、
sec 属性値に従って昇順で配置し直す。パーティション内のデータについては
lbn 属性値が付加されているので、XML ヘッダ部のパーティション情報を元に
sec 値へと変換してソートを行う。
</DD>
</DL>
*/
public class UDF_XML
{
    //DTD関連
    static public final String UDF_DTD2 = "-//Heart Solutions, Inc.//DTD UDF TEST TOOL//EN";
    static public final String UDF_DTD = "http://udfv.com/udf.dtd";
    //static public final String NS_UDF = "http://udfv.com/udf/";
    //static public final String NS_ECMA119 = "http://udfv.com/ecma119/";
    static public final String NS_UDF = "file:/udfv.com/udf/";
    static public final String NS_ECMA119 = "file:/udfv.com/ecma119/";

    public static final String SYSTEM                     = "system";
    public static final String EXTENT_OF_PARTITION        = "extent-of-partitions";
    public static final String MIRROR_EXTENT_OF_PARTITION = "mirror-extent-of-partitions";
    public static final String VRS                        = "vrs";
    public static final String CD9660_VDS                 = "cd9660-vds";
    public static final String AVDP                       = "avdp";
    public static final String IS                         = "is";
    public static final String MVDS                       = "mvds";
    public static final String RVDS                       = "rvds";
    public static final String FSDS                       = "fsds";
    public static final String PARTITION                  = "partition";
    public static final String UDF_EXTENT                 = "UDF_Extent";
    //public static final String UDF_METADATA_MAIN          = "metadata-main";
    //public static final String UDF_METADATA_MIRROR        = "metadata-mirror";
    public static final String DIRECTORY                  = "UDF_Directory";
    public static final String SDIRECTORY                 = "UDF_SDirectory";
    
    
    static private class UDFElementInfo{
	
	Element elem;
	long    partno;
    }
    
    static private class UDFPartInfo{
	
	long  sec;
	long  length;
    }
    
    static private class UDFPartInfo2{
	
	long    sec;
	long    length;
	String  parttype;
    }
    
    /**
        コンストラクタ。
    */
    public UDF_XML() {}
    
    /**
        コンストラクタ。

        指定したXMLファイルをソートし、出力する。
        @param args 入力ファイルと出力ファイル名。
    */
    public UDF_XML(String args[]){
	
	try{
	    String input_xml = args[0];
	    String output_xml = args[1];
	    // インプットXMLドキュメントを取得する
	    DOMParser parser = new DOMParser();
	    parser.parse(input_xml);
	    Document inDoc = parser.getDocument();
	    
	    
	    // ドキュメントをソートする
	    sortDocument(inDoc, -1);
	    
	    
	    // ドキュメントをXMLファイルに整形する
	    OutputFormat format    = new OutputFormat(inDoc, "UTF-8", true);
	    format.setLineWidth(0);
	    
	    StringWriter stringOut = new StringWriter();
	    XMLSerializer serial   = new XMLSerializer(stringOut, format);
	    serial.serialize(inDoc.getDocumentElement());
	    
	    
	    // ファイルに出力する
	    FileOutputStream fos   = new FileOutputStream(output_xml);
	    byte b[] = stringOut.toString().getBytes("UTF-8");
	    fos.write(b, 0, b.length);
	}
	catch(Exception e){
	
	    e.printStackTrace();
	}
    }
    
    /**
       ドキュメントをpos属性値でソートする。

       @param inputdoc ソート対象となるXMLドキュメント。
       @param mirrorloc  Metadata Mirror File の位置。Mirror が存在しない場合は-1 を指定する。
    */
    static public void sortDocument(Document inputdoc, int mirrorloc){
	try{
	    NodeList nlist   = inputdoc.getElementsByTagName("udf");
	    Node     udfnode = nlist.item(0);
	    NodeList childlist  = udfnode.getChildNodes();
	    TreeMap  partinfo   = getPartInfo2(inputdoc, UDF_XML.PARTITION, false);
	    TreeMap  mirrorinfo = getPartInfo2(inputdoc, UDF_XML.PARTITION, true);
	    TreeMap  extentinfo = getUDFElementInfo(udfnode, partinfo, mirrorinfo, mirrorloc);
	    
	    
	    // ソート順にエレメントを追加していく
	    Object[] sec = (extentinfo.keySet().toArray());
	    
	    for(int i = 0; i < sec.length; i++){
		
		UDFElementInfo uei = (UDFElementInfo)(extentinfo.get((String)sec[i]));
		if(uei == null)
		    continue;
		
		udfnode.appendChild(uei.elem);
	    }
	       
	}
	catch(Exception e){
	
	    e.printStackTrace();
	}
    }
    
    /**
       ドキュメントをソートする。
       
       各エレメントは、以下のグループ単位でソートされる。
       <ol>
       <li>パーティション外のデータ
       <li>各パーティションマップ内のデータ
       </ol>
       
       各グループ内のソートは、UDF_Extentの

       <ol>
       <li> パーティション番号</li>
       <li> 副番号</li>
       <li> secまたはlbn</li>
       </ol>

       でソートされる。

       また、パーティション外のデータについては、secアトリビュート値が
       Partition 終了位置より後にある場合は最後尾に配置される。

       (ちなみに稀に Partition開始位置後に Volume情報がある場合がある
        ex: YUMERIA-VIDEO.IMG)

       （パーティションが2つある場合の処理は考慮されていない。）
       
       @param doc ソート対象となるXMLドキュメント。
       @param env UDF 環境。
    */
    static public void sortDocumentbyEnv(Document doc, UDF_Env env) throws UDF_XMLException{
	
	NodeList nlist   = doc.getElementsByTagName("udf");
	Node     udfnode = nlist.item(0);
	
	try{
	    // Prevailing Descriptor の1つ目を用いる。2つあった場合は考慮してない
	    UDF_ElementList desc5l = env.getPartDescList(env.VDS_AUTO);
	    com.udfv.ecma167.UDF_desc5 desc5 = (com.udfv.ecma167.UDF_desc5)desc5l.elementAt(0);
	    int partno = desc5.getPartNumber().getIntValue();
	    
	    int partstart = env.getPartStartingLoc(partno);
	    int partend = partstart + (int)(env.getPartLen(partno) / env.LBS);
	    int partnum = env.getPartMapList().size();
	    TreeMap[] tm = null;
	    int i = 0;
	    
	    /*
	    // 各グループのUDF_Extent情報を取得する
	    // tm[0]:パーティション外(-1)
	    // tm[1]〜tm[n-1]:各パーティションマップ(但しMirrorが無い場合は〜tm[n])
	    // tm[n]:(存在すれば)Mirror Partition
	    if(env.hasMetadataPartMap()){
		
		tm = new TreeMap[partnum + 1 + 1];  // -1とMirror の2つを追加
		for(i = 0; i < partnum + 1; i++)
		    tm[i] = getExtents(doc, i - 1, false);
		tm[i] = getExtents(doc, i - 2, true);
	    }
	    else{
		
		tm = new TreeMap[partnum + 1];  // -1を追加
		for(i = 0; i < tm.length; i++)
		    tm[i] = getExtents(doc, i - 1, false);
	    }
	    */
	    tm = new TreeMap[1];

	    tm[0] = getExtents(doc, partend/*, 0, false*/);
	    
	    for(i = 0; i < tm.length; i++){
		
		Object[] sec = tm[i].keySet().toArray();
		for(int j = 0; j < sec.length; j++){
		    
		    UDFElementInfo uei = (UDFElementInfo)(tm[i].get((String)sec[j]));
		    if(uei == null)
			continue;
		    
		    /*
		    // Partition Lengthより後ろにあったらやらない
		    if(i == 0){
			
			Integer strsec = (Integer)sec[j];
			int intsec = strsec.intValue();
			
			if(partend <= intsec)
			    continue;
		    }
		    */
		    udfnode.appendChild(uei.elem);
		}
	    }
	    
	    /*
	    // Partition Lengthより後ろのものを追加
	    Object[] sec = tm[0].keySet().toArray();
	    for(i = 0; i < sec.length; i++){
		
		UDFElementInfo uei = (UDFElementInfo)(tm[0].get((String)sec[i]));
		if(uei == null)
		    continue;
		
		Integer strsec = (Integer)sec[i];
		int intsec = strsec.intValue();
		
		if(intsec < partend)
		    continue;
		
		udfnode.appendChild(uei.elem);
	    }
	    */
	}
	catch(Exception e){
	    
	    e.printStackTrace();
	}
    }
    
    /**
       ドキュメントからUDF_ExtentのTreeMapを取得する。
       
       @param doc XMLドキュメント。
       @param partstart partitionの開始位置

       TreeMapのキーは以下の文字列で構成される。

       "位置" + "." + "パーティション番号" + "." + "副番号" + "." + "LOC"

       位置はパーティションの前にあれば 0、
       位置はパーティションの中にあれば 1、
       位置はパーティションの後にあれば 2

       パーティション番号は4桁の数字。パーティション外にある場合は "----"となる

       副番号は4桁の数字。パーティション外にある場合は "0000"となる

       LBNは10桁の数字。

       以上をキーとして辞書順にソートする。
    */
    static private TreeMap getExtents(Document doc, int partstart/*, int partmapno, boolean mirror*/){
	
	TreeMap tm = new TreeMap();
	NodeList udflist = doc.getElementsByTagName("udf");
	NodeList nlist = udflist.item(0).getChildNodes();
	
	
	for(int i = 0; i < nlist.getLength(); i++){
	    
	    if(nlist.item(i).getNodeType() != Node.ELEMENT_NODE)
		continue;
	    
	    Element udfelem = (Element)nlist.item(i);
	    if(udfelem.getTagName().equals(UDF_XML.EXTENT_OF_PARTITION) ||
	       udfelem.getTagName().equals(UDF_XML.MIRROR_EXTENT_OF_PARTITION) ||
	       udfelem.getTagName().equals(UDF_XML.SYSTEM) ||
	       udfelem.getTagName().equals(UDF_XML.PARTITION) ||
	       udfelem.getTagName().equals("partition-info") ||
	       udfelem.getTagName().equals("partition-map-info"))
		continue;
	    
	    // UDF_Extent 内にextents エレメントは1つしか存在しないので
	    // １つ目のみのエクステントのみをソートの対象とする
	    NodeList extentlist = udfelem.getElementsByTagName(_ELEM_EXTENTS);
	    Element  firstextents = (Element)extentlist.item(0);
	    Element  extent = (Element)firstextents.getElementsByTagName(_ELEM_EXTENT).item(0);
	    
	    if(extent == null){
		
		System.err.println("UDF_SortXML - Warning: This Element(" + udfelem + ") doesn't have " + _ELEM_EXTENT + " element.");
		continue;
	    }

	    String key;
	    String strsec = extent.getAttribute(_ATTR_SECTOR);
	    if(strsec.length() > 0){
		int sec_ = Integer.parseInt(strsec);
		String sec = UDF_Util.i2d10(sec_);
		if(sec_ < partstart)
		    key = "0" + "." + "----" + "." + "0000" + "." + sec;
		else
		    key = "2" + "." + "----" + "." + "0000" + "." + sec;
	    }
	    else{
		String strpartno = extent.getAttribute(_ATTR_PARTNO);
		String partno = UDF_Util.i2d4(Integer.parseInt(strpartno));
		
		String subno = "0000";
		if(udfelem.getAttribute(_ATTR_MIRROR).equals("yes")){
		    subno = "0001";
		}

		if(udfelem.getAttribute("subno").length() >= 1){
		    subno = UDF_Util.i2d4(Integer.parseInt(udfelem.getAttribute("subno")));
		}

		
		String strlbn = extent.getAttribute(_ATTR_LBN);
		String lbn = UDF_Util.i2d10(Integer.parseInt(strlbn));

		key = "1" + "." + partno + "." + subno + "." + lbn;
	    }
	    
	    // TreeMapに追加
	    UDFElementInfo uei = new UDFElementInfo();
	    uei.elem = udfelem;
	    //uei.partno = partmapno;
	    tm.put(key, uei);
	}
	
	return tm;
    }
    
    /**
       XML の先頭で定義されているパーティション情報を取得する。
       @param doc       XML のドキュメントインスタンス。
       @param partelem  パーティション情報が格納されているエレメント名。
       
       @return
       正常に終了すると、パーティション番号をキーとした UDFPartInfo クラスのTreeMap を返す。
       partelem で指定されるエレメントが存在しないときは、null を返す。
    */
    static private TreeMap getPartInfo(Document doc, String partelem) throws UDF_XMLException{
	
	NodeList partlist = doc.getElementsByTagName(partelem);
	TreeMap  tm = new TreeMap();  // パーティション番号をキーとしたハッシュ
	
	
	if(partlist.getLength() == 0){
	    
//	    System.err.println("UDF_SortXML - Warning: " + partelem + " element that indicates Partition infomation is not exist.");
	    return null;
	}
	
	// パーティションのリストを取得する
	for(int i = 0; i < partlist.getLength(); i++){
	    
	    Element  partmaps   = (Element)partlist.item(i);
	    NodeList partitions = partmaps.getElementsByTagName(UDF_XML.PARTITION);
	    
	    
	    if(partitions.getLength() == 0)
		throw new UDF_XMLException(null, "Couldn't sort. This XML file doesn't have " + UDF_XML.EXTENT_OF_PARTITION + " element.");
	    
	    // １つのパーティションの "extents" のリストを取得する
	    for(int j = 0; j < partitions.getLength(); j++){
		
		Element  partition   = (Element)partitions.item(j);
		NodeList extentslist = partition.getElementsByTagName(_ELEM_EXTENTS);
		Element  extents    = (Element)extentslist.item(0);
		NodeList extentlist = partition.getElementsByTagName(_ELEM_EXTENT);
		Vector   extentinfo = new Vector();
		Integer  partno     = Integer.decode(partition.getAttribute(_ATTR_PARTNO));
		
		// １つのパーティションのエクステントのデータを取得する
		for(int k = 0; k < extentlist.getLength(); k++){
		    
		    Element elem_ext = (Element)extentlist.item(k);
		    long  sec = Long.parseLong(elem_ext.getAttribute(_ATTR_SECTOR));
		    long  len = Long.parseLong(elem_ext.getAttribute(_ATTR_LEN));
		    UDFPartInfo ei = new UDFPartInfo();
		    
		    
		    ei.sec    = sec;
		    ei.length = len;
		    extentinfo.add(ei);
		}
		
		tm.put(partno, extentinfo);
	    }
	}
	
	return tm;
    }
    /**
       XML の先頭で定義されているパーティション情報を取得する。
       @param doc       XML のドキュメントインスタンス。
       @param partelem  パーティション情報が格納されているエレメント名。
       
       @return
       正常に終了すると、パーティション番号をキーとした UDFPartInfo クラスのTreeMap を返す。
       partelem で指定されるエレメントが存在しないときは、null を返す。
    */
    static private TreeMap getPartInfo2(Document doc, String partelem, boolean mirror) throws UDF_XMLException{
	
	NodeList partlist = doc.getElementsByTagName(partelem);
	TreeMap  tm = new TreeMap();  // パーティション番号をキーとしたハッシュ
	
	
	if(partlist.getLength() == 0){
	    
	    System.err.println("UDF_SortXML - Warning: " + partelem + " element that indicates Partition infomation is not exist.");
	    return null;
	}
	
	
	for(int i = 0; i < partlist.getLength(); i++){  // パーティションのリスト(<partition>)
	    
	    if(partlist.item(i).getNodeType() != Node.ELEMENT_NODE)
		continue;
	    
	    Element  partition = (Element)partlist.item(i);
	    NodeList partmaplist = partition.getElementsByTagName(_ELEM_PARTMAP);
	    
	    
	    for(int j = 0; j < partmaplist.getLength(); j++){  // パーティションマップのリスト(<partmap>)
		
		if(partmaplist.item(j).getNodeType() != Node.ELEMENT_NODE)
		    continue;
		
		Element  elem = (Element)(partmaplist.item(j));
		Integer  partno = Integer.decode(elem.getAttribute(_ATTR_PARTNO));
		
		getPartMapInfo(tm, elem, partno, mirror, "");
	    }
	}

	return tm;
    }
    
    /**
       XML の先頭で定義されているパーティションマップ情報を取得する。
       
       @param tm      UDFPartInfo クラスを格納するためのTreeMap。
       @param partmap パーティションマップを表すエレメント（<partmap>)。
       @param partno  パーティションマップ番号。
    */
    static private void getPartMapInfo(TreeMap tm, Element partmap, Integer partno, boolean mirror, String type){
	
	NodeList pmchild = partmap.getChildNodes();
	int partmaptype = -1;
	
	
	for(int i = 0; i < pmchild.getLength(); i++){
	    
	    if(pmchild.item(i).getNodeType() != Node.ELEMENT_NODE)
		continue;
	    
	    Element elem = (Element)(pmchild.item(i));
	    String nodename = elem.getNodeName();
	    
	    
	    if(nodename.equals("UDF_Extent") && !mirror){
		
		NodeList extentslist = elem.getChildNodes();
		Element  extents = (Element)extentslist.item(0);
		NodeList extentlist = extents.getChildNodes();
		Vector   extentinfo  = new Vector();
		
		
		// 既に追加されていればそれは最新のVAT なので、上書きしない
		if(type.equals("vat") && tm.containsKey(partno))
		    return;
		
		for(int j = 0; j < extentlist.getLength(); j++){
		    
		    Element elem_ext = (Element)extentlist.item(j);
		    long  sec = Long.parseLong(elem_ext.getAttribute(_ATTR_SECTOR));
		    long  len = Long.parseLong(elem_ext.getAttribute(_ATTR_LEN));
		    UDFPartInfo2 ei = new UDFPartInfo2();
		    
		    ei.sec      = sec;
		    ei.length   = len;
		    ei.parttype = type;
		    extentinfo.add(ei);
		}
		
		tm.put(partno, extentinfo);
	    }
	    else if(nodename.equals("vat")){
		
		getPartMapInfo(tm, elem, partno, mirror, "vat");
	    }
	    else if(nodename.equals("metadata")){
		
		// MetadataMain のみ
		if(!elem.getAttribute(_ATTR_MIRROR).equals("yes"))
		    getPartMapInfo(tm, elem, partno, mirror, "meta");
		else if(mirror)
		    getPartMapInfo(tm, elem, partno, false, "meta");
	    }
	}
    }
    
    /**
       各UDF Element の所属するパーティション番号と、開始セクタを取得する。
       extents Element 内に複数のextent Element が存在していた場合、先頭のextent Element のみを取得する。
       もしudfnode 内に同じ位置(sec)を指すUDF Element が複数あった場合は、そのUDF Element を削除する。
       また、partinfo またはmirrorinfo がnull の場合で、且つUDF Element がそれらの情報を必要とした場合
       （UDF Element がpartno XML Attribute を持っている場合）、単にsec/lbn XML Attribute の値でソートする。
       
       @param udfnode    <udf> Element を示すインスタンス。
       @param partinfo   パーティション情報が格納されたTreeMap インスタンス。この情報はgetPartInfo2() で取得できる。
       @param mirrorinfo ミラーパーティション情報が格納されたTreeMap インスタンス。この情報はgetPartInfo() で取得できる。 
       @param mirrorloc  Metadata Mirror File の位置。Mirror が存在しない場合は-1 を指定する。
       @return 正常に終了すると、セクタをキーとしたUDFElementInfo クラスのTreeMap を返す。
    */
    static private TreeMap getUDFElementInfo(Node udfnode, TreeMap partinfo, TreeMap mirrorinfo, int mirrorloc)
	throws UDF_XMLException{
	
	TreeMap  tm = new TreeMap();  // sec 値をキーとしたハッシュ
	NodeList nl = udfnode.getChildNodes();
	
	
	for(int i = 0; i < nl.getLength(); i++){
	    
	    if(nl.item(i).getNodeType() != Node.ELEMENT_NODE)
		continue;
	    
	    Element  udfelem = (Element)nl.item(i);
	    
	    
	    if(udfelem.getTagName().equals(UDF_XML.EXTENT_OF_PARTITION) ||
	       udfelem.getTagName().equals(UDF_XML.MIRROR_EXTENT_OF_PARTITION) ||
	       udfelem.getTagName().equals(UDF_XML.SYSTEM) ||
	       udfelem.getTagName().equals(UDF_XML.PARTITION))
		continue;
	    
	    // UDF_Extent 内にextents エレメントは1つしか存在しないので
	    // １つ目のみのエクステントのみをソートの対象とする
	    NodeList extentlist   = udfelem.getElementsByTagName(_ELEM_EXTENTS);
	    
	    if(extentlist == null){
		
		System.err.println("UDF_SortXML - Warning: This Element(" + udfelem + ") doesn't have " + _ELEM_EXTENTS + " element.");
		continue;
	    }
	    
	    Element  firstextents = (Element)extentlist.item(0);
	    Element  extent = (Element)firstextents.getElementsByTagName(_ELEM_EXTENT).item(0);
	    
	    if(extent == null){
		
		System.err.println("UDF_SortXML - Warning: This Element(" + udfelem + ") doesn't have " + _ELEM_EXTENT + " element.");
		continue;
	    }
	    
	    String   strsec = extent.getAttribute(_ATTR_SECTOR);
	    Integer  partno = new Integer(-1);
	    Long     sec    = null;
	    String   keystr = new String();
	    
	    
	    if(strsec.equals("")){  // sec アトリビュートが存在しない
		
		// かわりにlbn アトリビュートを探す
		strsec = extent.getAttribute(_ATTR_LBN);
		if(strsec.equals("")){
		    
		    System.err.println("elem = " + udfelem.getTagName());
		    throw new UDF_XMLException(null,
			 "Couldn't sort. This element of udf has neither " + _ATTR_SECTOR + " nor " + _ATTR_LBN + " attribute.");
		}
		
		// lbn のときは、partno アトリビュートも必須
		String strpartno = extent.getAttribute(_ATTR_PARTNO);
		partno = Integer.decode(strpartno);
		
		
		if(strpartno.equals(""))
		    throw new UDF_XMLException(null,
			"Couldn't sort. This element of udf has " + _ATTR_LBN + " attribute, but doesn't have " + _ATTR_PARTNO + " attribute.");
		
		boolean ismirror = extent.getAttribute(_ATTR_MIRROR).equals("yes");
		
		if(partinfo == null || (ismirror && mirrorinfo == null)){
		    
		    sec = Long.decode(strsec);  // ソート効果はほとんどない
		}
		else{
		    // パーティションの情報をモトに、lbn をsec 値へと変換する
		    Vector partextents = (Vector)(ismirror ? mirrorinfo.get(partno) : partinfo.get(partno));
		    
		    
		    if(partextents == null){
			
			throw new UDF_XMLException(null,
			    "ismirror = " + ismirror + " Couldn't sort. Partition Number " + partno + " is not defined in "
			     + (ismirror ? UDF_XML.MIRROR_EXTENT_OF_PARTITION : UDF_XML.EXTENT_OF_PARTITION) + ".");
		    }
		    
		    UDFPartInfo2 firstinfo  = (UDFPartInfo2)partextents.elementAt(0);
		    long  firstsec = firstinfo.sec;
		    long  lbn = (Long.parseLong(strsec));
		    long  offset = 0;
		    
		    
		    for(int k = 0; k < partextents.size(); k++){
			
			UDFPartInfo2 upi = (UDFPartInfo2)partextents.elementAt(k);
			
			
			if(upi.sec - firstsec < lbn && k != (partextents.size() - 1)){
			    
			    offset += upi.length / 2048;
			    continue;
			}
			
			sec = new Long((lbn - offset) + upi.sec);
			
			if(udfelem.getAttribute(_ATTR_MIRROR).equals("yes"))
			    sec = new Long((lbn - offset) + upi.sec + mirrorloc);
			
			String tmpstr = new String();
			for(int l = String.valueOf(sec).length(); l < 25; l++)
			    tmpstr += '0';
			
			keystr += ("sec=" + tmpstr + sec + ":partno=" + partno);
			
			break;
		    }
		}
	    }
	    else{
		
		sec = Long.decode(strsec);  // ナマのセクタ値
		
		
		if(udfelem.getAttribute(_ATTR_MIRROR).equals("yes")){
		    
		    long tmp = sec.longValue();
		    sec = new Long(tmp + mirrorloc);
		}
		
		String tmpstr = new String();
		for(int l = String.valueOf(sec).length(); l < 25; l++)
		    tmpstr += '0';
		keystr += ("sec=" + tmpstr + sec + ":partno=" + partno);
	    }
	    
//	    System.err.println(keystr + " elem = " + udfelem);
	    
	    // マップに追加する
	    UDFElementInfo uei = new UDFElementInfo();
	    uei.elem   = udfelem;
	    uei.partno = partno.intValue();
	    
	}

	return tm;
    }
    
    
    public static void main(String args[]){

	if(args.length < 2){
	
	    System.err.println("Usage: java UDF_SortXML <Input_XML> <Output_XML>");
	    return;
	}
	
	new UDF_XML(args);
    }
    
    private int mirrorloc_;
    
    private static final String _ELEM_EXTENTS     = "extents";
    private static final String _ELEM_EXTENT      = "extent";
    private static final String _ELEM_PARTITION   = "partition";
    private static final String _ELEM_PMTYPE      = "pm-type";
    private static final String _ELEM_PARTMAP     = "partmap";
    private static final String _ELEM_VAT         = "vat";
    private static final String _ELEM_METADATA    = "metadata";
    
    private static final String _ATTR_PARTNO      = "partno";
    private static final String _ATTR_SECTOR      = "sec";
    private static final String _ATTR_LEN         = "len";
    private static final String _ATTR_LBN         = "lbn";
    private static final String _ATTR_MIRROR      = "mirror";

}


