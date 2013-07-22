/*

*/
package com.udfv.core;

import com.udfv.exception.*;
import com.udfv.access.*;
import com.udfv.util.*;
import com.udfv.ecma167.*;
import java.io.*;
import java.util.*;
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;

/**
<dl>
<dt>概要</dt>
<dd>
領域を表現するクラス。
</dd>
<dt>詳細</dt>
<dd>
<p>
 UDF_Extentクラスは、断片化したデータ領域をあたかも1つの領域として扱うクラスである。
</p>
<p>
&lt;UDF_Extent&gt;の第1のXML子エレメントは &lt;extents&gt;で、第2以降に実際のデータの実体を表わすXMLエレメントが続かなければならない。ただし、内部的には &lt;extents&gt;は子UDF Elementとして扱っておらず、UDF_Extentは単なるXMLのデータとして扱う。
<p>
&lt;extent&gt;タグは領域の断片の1つを表わすタグで、パーティション外にある場合は以下の3つの XML Attributeを持つ
<pre>
len
off
sec
</pre>
パーティション内にある場合は以下の4つのXML Attributeを持つ
<pre>
len
off
lbn
partno
</pre>
なお、内部的にはパーティション外にある場合は
<pre>
partno=-1 
</pre>
として処理している。詳しくは UDF_ElemBaseを参照のこと。

<p>
例)
</p>
<p>
パーティション外に置く場合、XMLは以下のような形式になる
</p>
<pre>
 &lt;UDF_Extent&gt;
        &lt;extents&gt;
            &lt;extent len="32768" off="0" sec="16"/&gt;
            &lt;extent len="32768" off="32768" sec="32"/&gt;
        &lt;/extents&gt;
	&lt;要素1&gt;
	&lt;要素2&gt;
	...
	...
	&lt;要素n&gt;
  &lt;/UDF_Extent&gt;
</pre>
<p>
パーティション内に置く場合、XMLは以下のような形式になる
</p>
<pre>
 &lt;UDF_Extent&gt;
        &lt;extents&gt;
            &lt;extent len="32768" partno="0" off="0" lbn="16"/&gt;
            &lt;extent len="32768" partno="0" off="32768" lbn="32"/&gt;
        &lt;/extents&gt;
	&lt;要素1&gt;
	&lt;要素2&gt;
	...
	...
	&lt;要素n&gt;
  &lt;/UDF_Extent&gt;
</pre>
<p>
UDF250におけるMetadataPartitionMapは1つのパーティションマップに対し、
MainとMirrorがある。MainとMirrorの区別は mirrorアトリビュートで行う。
</p>
<pre>
 &lt;UDF_Extent mirror="yes"&gt;
        &lt;extents&gt;
            &lt;extent lbn="16" off="0" len="32768" partno="1"/&gt;
            &lt;extent lbn="32" off="32768" len="32768" partno="1"/&gt;
        &lt;/extents&gt;
	&lt;要素1&gt;
	&lt;要素2&gt;
	...
	...
	&lt;要素n&gt;
  &lt;/UDF_Extent&gt;
</pre>
</dl>


多数のExtentを表わす識別子として、&lt;multiple-extent&gt;を追加した。
&lt;multiple-extent&gt;に対してはいくつかのメソッドが機能しないので注意が必用である。

 */
public class UDF_Extent extends UDF_Element
{
    private UDF_ExtentElemList extents_elem;
    private Element extents_node;

    public UDF_Extent(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? "UDF_Extent" : name);
	extents_elem = new UDF_ExtentElemList();
    }


    public UDF_RandomAccessExtent genRandomAccessExtent(){
	return new UDF_RandomAccessExtent(this);
    }

    public boolean hasAttrPartSubno(){
	return true;
    }
    /**
       @deprecated replaced by {@link setPartSubno(int)}
     */
    public void setMirror(boolean flag) {
	setPartSubno(flag ? 1 : 0);
    }

    public void setPartSubno(int subno){
	super.setPartSubno(subno);

	createExtentsNode();

	/*
	  mirrorフラグは <extents>ではなく <UDF_Extent>につけるのが正しいが、
	  <extent>のフラグは現在ソートで使用しているようなので、
	  とりあえずつける。

	  subno=0は属性としてつけないのが正しい。
	*/
	if(marimite && getNode() != null){
	    if (subno != 0) {
		extents_node.setAttribute("subno", "" + subno);
	    }
	    else {
		extents_node.removeAttribute("subno");
	    }
	}
	return;
    }

    /**
       ExtentをExtentElemの配列の形で取得する。

       MutipleExtentElemがあった場合は、ExtentElemを繰り返し数だけ生成して割付けし配列に格納する。

       @return Extent Elemm

       このメソッドは遅い。また MultipleExtentElemがある場合大量のメモリを消費する。
       getExtentElem()を使うのが望ましい。
       
       @deprecated replcated by {@link getExtentElem()}
    */
    public UDF_ExtentElem[] getExtent(){
	int size = 0;
	int i=0;
	for(Iterator it=extents_elem.iterator() ; it.hasNext() ; ++i){
	    UDF_ExtentElem ee = (UDF_ExtentElem)it.next();
	    size += ee.getTimes();
	}

	UDF_ExtentElem [] e = new UDF_ExtentElem[size];

	i=0;
	for(Iterator it=extents_elem.iterator() ; it.hasNext() ; ){
	    UDF_ExtentElem ee = (UDF_ExtentElem)it.next();
	    if(UDF_MultipleExtentElem.class.isAssignableFrom(ee.getClass())){
		UDF_MultipleExtentElem mee = (UDF_MultipleExtentElem)ee;
		for(int j=0 ; j<mee.getTimes() ; ++j)
		    e[i++] = mee.getExtentElem(j);
	    }
	    else
		e[i++] = (UDF_ExtentElem)ee;
	}
	return e;
    }

    /**
       オフセット位置 posが含まれる ExtentElemを取得する。

       MutipleExtentElem内にある場合は ExtentElemeを生成してそれを返す。

       @return Extent Elemm
     */
    protected UDF_ExtentElem getExtent(long pos){
	long off = 0;
	for(Iterator it=extents_elem.iterator() ; it.hasNext() ; ){
	    UDF_ExtentElem ee = (UDF_ExtentElem)it.next();
	    if(UDF_MultipleExtentElem.class.isAssignableFrom(ee.getClass())){
		UDF_MultipleExtentElem mee = (UDF_MultipleExtentElem)it.next();
		for(int j=0 ; j<mee.getTimes() ; ++j){
		    if(pos >= off && pos < off + mee.len)
			return mee.getExtentElem(j);
		    off += ee.len;
		}
	    }
	    else{
		if(pos >= off && pos < off + ee.len)
		    return ee;
		off += ee.len;
	    }
	}
	return null;
    }

    /**
       ExtentをUDF_ADの配列の形で取得する。

       @return UDF_AD
       @deprecated replcated by {@link getExtentElem()}
    */
    public UDF_AD[] getExtentAD(){
	int i=0;
	int size = 0;
	for(Iterator it=extents_elem.iterator() ; it.hasNext() ; ++i){
	    UDF_ExtentElem ee = (UDF_ExtentElem)it.next();
	    size += ee.getTimes();
	}
	
	UDF_AD [] ad = new UDF_AD[size];
	i=0;
	for(Iterator it=extents_elem.iterator() ; it.hasNext() ; ++i){
	    UDF_ExtentElem ee = (UDF_ExtentElem)it.next();
	    if(UDF_MultipleExtentElem.class.isAssignableFrom(ee.getClass())){
		UDF_MultipleExtentElem mee = (UDF_MultipleExtentElem)ee;
		for(int j=0 ; j<mee.getTimes() ; ++j){
		    ad[j] = (UDF_long_ad)createElement("UDF_long_ad", null, null);
		    ad[j].setDefaultValue();
		    ad[j].setLen(mee.len);
		    ad[j].setLbn(mee.loc + mee.getStep() * j);
		    ad[j].setPartRefNo(mee.partno);
		}
	    }
	    else{
		ad[i] = (UDF_long_ad)createElement("UDF_long_ad", null, null);
		ad[i].setDefaultValue();
		ad[i].setLen(ee.len);
		ad[i].setLbn(ee.loc);
		ad[i].setPartRefNo(ee.partno);
	    }
	}
	return ad;
    }

    /**
       Extentを ExtentElemのVectorの形で取得する。

       @return UDF_AD
    */
    public UDF_ExtentElemList getExtentElem(){
	return extents_elem;
    }

    /**
      指定のUDF_Extentが持つ要素と同じ内容を加える。
    */
    public void copyExtent(UDF_Extent src){

	UDF_ExtentElem [] elem = src.getExtent();

	for(int i = 0, max = elem.length ; i <max; i++) {
	    addExtent(elem[i].loc, elem[i].partno, elem[i].len, elem[i].getExtentFlag());
	}
	return;
    }


    /**
       動作:
       <extents>ノードを作成する。既にあるときは何もしない。
       <extents>ノードは常にXML要素の一番最初に作成する
     */
    private void createExtentsNode(){
	if(!marimite || getNode() == null)
	    return;
	NodeList nl = getNode().getChildNodes();

	for(int i=0 ; i<nl.getLength() ; ++i){
	    Node child = nl.item(i);
	    if(child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("extents")){
		extents_node = (Element)child;
		return;
	    }
	}
	extents_node = getDocument().createElement("extents");

	Node first = getNode().getFirstChild();
	getNode().insertBefore(extents_node, first);

	return;
    }

    /**
       UDF_Extentを切り詰め短くしたり、長くしたりする。

       @param len	指定の長さ

       
短くするとき:

以下の状態になっているextentを 5000に切り詰めると
=======================================================
       i	LOC	SIZE	OFF
       0        0	4096	0
       1	4	4096	4096
       2	8	4096	8192
=======================================================
以下のようになる。
=======================================================
       i	LOC	SIZE	OFF
       0        0	4096	0
       1	4	904	4096
=======================================================

長くするとき:

LBSに満たない extentがあれば、それを必要な分だけ増やす。
新たな領域は割り当てない。

     */
    public void truncExtent(long len) throws UDF_InternalException{
	if(len > getLongSize()){
	    long avail_size = 0;	//長くすることができるサイズ
	    UDF_ExtentElem[] ee = getExtent();
	    for(int i=0 ; i<ee.length ; ++i){
		if((ee[i].len % env.LBS) != 0)
		    avail_size += (env.LBS - (ee[i].len % env.LBS));
	    }
	    if(avail_size < len - getLongSize())
		throw new UDF_InternalException(this, "truncExtent: Cannot truncate: avail_size=" + avail_size + " request=" + len);

	    len -= getLongSize();
	    for(int i=0 ; i<ee.length && len > 0 ; ++i){
		if((ee[i].len % env.LBS) != 0){
		    avail_size = (env.LBS - (ee[i].len % env.LBS));
		    if(avail_size > len)
			avail_size = len;
		    ee[i].len += avail_size;
		    len -= avail_size;
		}
	    }
	    rebuildExtentNode();
	    return;
	}

	long off = 0;
	UDF_ExtentElem[] ee = getExtent();
	for(int i=0 ; i<ee.length ; ++i){
	    off += ee[i].len;
	    if(len < off){
		ee[i].len -= (off - len);
		if(ee[i].len == 0)
		    extents_elem.setSize(0);
		else
		    extents_elem.setSize(i + 1);
		rebuildExtentNode();
		return;
	    }
	}
    }

    /**
       <extents>以下を作りなおす
     */
    private void rebuildExtentNode(){

	long off = 0;

	if(marimite){
	    getNode().removeChild(extents_node);
	    createExtentsNode();
	}

	//UDF_ExtentElem[] ee = getExtent();
	UDF_ExtentElemList eel = getExtentElem();
	for(Iterator it=eel.iterator(); it.hasNext() ; ){
	    UDF_ExtentElem ee = (UDF_ExtentElem)it.next();
	    int t = ee.getTimes();
	    ee.setOffset(off);
	    off += ee.getLen() * t;//繰り返し数を考慮
	    if(marimite){
		if(UDF_MultipleExtentElem.class.isAssignableFrom(ee.getClass())){
		    Element ex2 = getDocument().createElement("multiple-extent");
		    if(ee.partno >= 0){
			ex2.setAttribute("lbn", String.valueOf(ee.loc));
			ex2.setAttribute("partno", String.valueOf(ee.partno));
			ex2.setAttribute("len", String.valueOf(ee.len));
			ex2.setAttribute("off", String.valueOf(ee.off));
			ex2.setAttribute("flag", String.valueOf(ee.getExtentFlag()));
		    }
		    else{
			ex2.setAttribute("sec", String.valueOf(ee.loc));
			ex2.setAttribute("len", String.valueOf(ee.len));
			ex2.setAttribute("off", String.valueOf(ee.off));
		    }
		    ex2.setAttribute("times", String.valueOf(ee.getTimes()));
		    ex2.setAttribute("step", String.valueOf(ee.getStep()));
		    extents_node.appendChild(ex2);
		}
		else{
		    Element ex2 = getDocument().createElement("extent");
		    if(ee.partno >= 0){
			ex2.setAttribute("lbn", String.valueOf(ee.loc));
			ex2.setAttribute("partno", String.valueOf(ee.partno));
			ex2.setAttribute("len", String.valueOf(ee.len));
			ex2.setAttribute("off", String.valueOf(ee.off));
			ex2.setAttribute("flag", String.valueOf(ee.getExtentFlag()));
		    }
		    else{
			ex2.setAttribute("sec", String.valueOf(ee.loc));
			ex2.setAttribute("len", String.valueOf(ee.len));
			ex2.setAttribute("off", String.valueOf(ee.off));
		    }
		    extents_node.appendChild(ex2);
		}
	    }
	}
    }

    /**
       extentを移動する。

       @param srcloc	移動元
       @param srcpartno	移動元パーティション参照番号
       @param dstloc	移動先
       @param dstpartno	移動先パーティション参照番号
       @param len	移動する長さ
       @param partnum   Sparing TableまたはVATのパーティション番号

       @return Spareがあったかどうか

       このメソッドは Sparing Tableおよび VAT を実装するためだけに使用される。

       したがって、移動元位置、移動先、移動する長さには制限があり、
       矛盾のある移動をしようとした際の処理は一切考慮されていない。
     */
    public boolean spareExtent(int srcloc, int srcpartno, int dstloc, int dstpartno, long len, int partnum){
	if(len <= 0)
	    return false;

	debugMsg(1, "spare: srcloc=" + srcloc + " dstloc="+dstloc + " len=" + len + " srcpartno=" + srcpartno + " dstpartno="+dstpartno);
	int part_starting_loc = 0;
	try{
	    part_starting_loc = env.getPartStartingLoc(partnum);
	}
	catch(UDF_VolException e){
	    e.printStackTrace();
	}
	srcloc += part_starting_loc;

	UDF_ExtentElem new_post = null;
	UDF_ExtentElem new_pre = null;
	//ここは内部で追加をしているので iteratorにできない?
	//removeじゃないから大丈夫かも
	for(int i=0 ; i<extents_elem.size() ; ++i){
	    UDF_ExtentElem ee = (UDF_ExtentElem)extents_elem.elementAt(i);
	    long ee_loc = ee.off / UDF_Env.LBS + part_starting_loc;

	    if(ee.partno == srcpartno &&
	       ee_loc  <= srcloc &&
	       ee_loc * UDF_Env.LBS + ee.len >= srcloc * UDF_Env.LBS + len){
		//debug(0);

		/**
                                  len
                               ---------->
                             srcloc    
	        ee.loc	   
		  |            |         |        |
                ---------------------------------------------------
   		  |pre         |         |  post  |
                

		 */
		//移動するextentの前部分
		new_pre = new UDF_ExtentElem();
		new_pre.loc = ee.loc;
		new_pre.partno = ee.partno;
		new_pre.len = (srcloc - ee.loc) * UDF_Env.LBS;
		//移動するextentの後部分
		new_post = new UDF_ExtentElem();
		new_post.loc = srcloc + (int)(len / UDF_Env.LBS);
		new_post.partno = ee.partno;
		new_post.len = ee.len - new_pre.len - len;
		//移動した部分
		ee.loc = dstloc;
		//ee.partno = dstpartno;
		ee.setPartRefNo(dstpartno);
		ee.len = len;

		if(new_post.len != 0){
		    extents_elem.add(i+1, new_post);
		}
		if(new_pre.len != 0){
		    extents_elem.add(i, new_pre);
		}
		
		rebuildExtentNode();

		normalize();

		return true;
	    }
	}
	return false;
    }

    /**
     */
    public void setAttribute(int idx, String attr, String val)
    {
	NodeList nl = extents_node.getChildNodes();

	for(int i=0 ; i<nl.getLength()  ; ++i){
	    if(nl.item(i).getNodeName().equals("extent") && i == idx){
		((Element)(nl.item(i))).setAttribute(attr, val);
		break;
	    }
	}
    }

    /**
       extentを削除する
     */
    public void removeExtent(int loc, int partno, long len, int flag){
	UDF_ExtentElem tgt = new UDF_ExtentElem(loc, partno, len, flag);
	for(int i=0 ; i<extents_elem.size() ; ++i){
	    UDF_ExtentElem ee = (UDF_ExtentElem)extents_elem.elementAt(i);
	    if(ee.equals(tgt)){
		extents_elem.removeElementAt(i);
		rebuildExtentNode();
	    }
	}
    }
    /**
       eeに等しい extentを削除する
     */
    public void removeExtent(UDF_ExtentElem ee){
	removeExtent(ee.loc, ee.partno, ee.len, ee.getExtentFlag());
    }
    /**
       extentを削除する
    */
    public void removeExtent(UDF_AD[] ad){
	for(int i=0 ; i<ad.length ; ++i){
	    removeExtent(ad[i].getLbn(), ad[i].getPartRefNo(), ad[i].getLen(), ad[i].getFlag());
	}
    }

    /**
       extentを削除する
    */
    public void removeExtent(UDF_AD ad){
	removeExtent(ad.getLbn(), ad.getPartRefNo(), ad.getLen(), ad.getFlag());
    }
    /**
       extentを追加する。
     */
    public void addExtentElem(UDF_AD ad){
	UDF_ExtentElem ee = null;
	if(UDF_MultipleAD_long_ad.class.isAssignableFrom(ad.getClass()))
	    ee = new UDF_MultipleExtentElem((UDF_MultipleAD_long_ad)ad);
	else if(UDF_MultipleAD_short_ad.class.isAssignableFrom(ad.getClass()))
	    ee = new UDF_MultipleExtentElem((UDF_MultipleAD_short_ad)ad);
	else
	    ee = new UDF_ExtentElem(ad);

	addExtentElem(ee);
    }
    /**
       extentを追加する。
     */
    public void addExtentElem(UDF_ExtentElem ee){
	ee.off = getLongSize();
	extents_elem.add(ee);

	if(marimite && getNode() != null){
	    createExtentsNode();
	    if(UDF_MultipleExtentElem.class.isAssignableFrom(ee.getClass())){
		Element ex2 = getDocument().createElement("multiple-extent");
		if(ee.partno >= 0){
		    ex2.setAttribute("lbn", String.valueOf(ee.loc));
		    ex2.setAttribute("partno", String.valueOf(ee.partno));
		    ex2.setAttribute("len", String.valueOf(ee.len));
		    ex2.setAttribute("off", String.valueOf(ee.off));
		    ex2.setAttribute("flag", String.valueOf(ee.getExtentFlag()));
		}
		else{
		    ex2.setAttribute("sec", String.valueOf(ee.loc));
		    ex2.setAttribute("len", String.valueOf(ee.len));
		    ex2.setAttribute("off", String.valueOf(ee.off));
		}
		ex2.setAttribute("times", String.valueOf(ee.getTimes()));
		ex2.setAttribute("step", String.valueOf(ee.getStep()));
		extents_node.appendChild(ex2);
	    }
	    else{
		Element ex2 = getDocument().createElement("extent");
		if(ee.partno >= 0){
		    ex2.setAttribute("lbn", String.valueOf(ee.loc));
		    ex2.setAttribute("partno", String.valueOf(ee.partno));
		    ex2.setAttribute("len", String.valueOf(ee.len));
		    ex2.setAttribute("off", String.valueOf(ee.off));
		    ex2.setAttribute("flag", String.valueOf(ee.getExtentFlag()));
		}
		else{
		    ex2.setAttribute("sec", String.valueOf(ee.loc));
		    ex2.setAttribute("len", String.valueOf(ee.len));
		    ex2.setAttribute("off", String.valueOf(ee.off));
		}
		extents_node.appendChild(ex2);
	    }
	}

	return;

    }

    /**
       extentを追加する。
     */
    public void addExtent(UDF_ExtentElem ee){
	addExtent(ee.loc, ee.partno, ee.len, ee.getExtentFlag());
    }

    /**
       extentを追加する
     */
    public void addExtent(int loc, int partno, long len){
	addExtent(loc, partno, len, 0, /*null,*/ false);
    }
    /**
       extentを追加する
     */
    public void addExtent(int loc, int partno, long len, int flag){
	addExtent(loc, partno, len, flag, /*null,*/ false);
    }
    /**
       extentを追加する
     */
    public void addExtent(int loc, int partno, long len, int flag, /*String id,*/ boolean normalize){
	//debugMsg("ADD: "+loc+":"+partno+":"+len + ":" + flag);

	UDF_ExtentElem ee = new UDF_ExtentElem(loc, partno, len, flag);
	ee.off = getLongSize();
	//ee.id = id;
	extents_elem.add(ee);

	//System.err.println("zz0");
	debugMsg(3, "loc="+loc+" partno="+partno+" len="+len+" flag="+flag);
	if(marimite && getNode() != null){
	    createExtentsNode();
	    Element ex2 = getDocument().createElement("extent");
	    if(partno >= 0){
		ex2.setAttribute("lbn", String.valueOf(loc));
		ex2.setAttribute("partno", String.valueOf(partno));
		ex2.setAttribute("len", String.valueOf(len));
		ex2.setAttribute("off", String.valueOf(ee.off));
		ex2.setAttribute("flag", String.valueOf(flag));
	    }
	    else{
		ex2.setAttribute("sec", String.valueOf(loc));
		ex2.setAttribute("len", String.valueOf(len));
		ex2.setAttribute("off", String.valueOf(ee.off));
	    }
	    /*
	    if(id != null && id.length() > 0){
		ex2.setAttribute("id", id);
	    }
	    */
	    extents_node.appendChild(ex2);
	}
	//System.err.println("zz1");

	return;
    }

    /**
       extentを追加する
     */
    public void addExtent(UDF_AD[] ad){
	for(int i=0 ; i<ad.length ; ++i){
	    addExtent(ad[i].getLbn(), ad[i].getPartRefNo(), ad[i].getLen(), ad[i].getFlag());
	}
    }

    /**
       extentを追加する
     */
    public void addExtent(UDF_AD ad){
	addExtent(ad.getLbn(), ad.getPartRefNo(), ad.getLen(), ad.getFlag());
    }

    /**
       extentを追加する
     */
    public void addExtent(UDF_extent_ad ad){
	addExtent(ad.getExtentLoc().getIntValue(), -1, ad.getExtentLen().getLongValue(), 0);
    }

    /**
       extentを追加する
     */
    public void addExtent(UDF_ADList list){
	for(Iterator it = list.iterator() ; it.hasNext() ; ){
	    UDF_AD ad = (UDF_AD)it.next();
	    addExtent(ad);
	}
    }

    public long getLongSize(){
	long sz = 0;
	for(Iterator i=extents_elem.iterator() ; i.hasNext() ; ){
	    UDF_ExtentElem ee = (UDF_ExtentElem)i.next();
	    sz += ee.getLen() * ee.getTimes();
	}

	return sz;
    }
    public int getSize(){
	return (int)getLongSize();
    }

    /**
       断片化しているかどうか？

       @return false断片化していない、true断片化している

       複数 Extentがあっても互いに連続していれば断片化と判断しない。
     */
    public boolean isFragment(){
	boolean cont;
	UDF_ExtentElem ee = (UDF_ExtentElem)extents_elem.elementAt(0);
	for(int i=1 ; i<extents_elem.size(); ++i){
	    UDF_ExtentElem ee2 = (UDF_ExtentElem)extents_elem.elementAt(i);
	    if(ee.partno != ee2.partno)
		return true;
	    if(ee.loc * env.LBS + ee.len != ee2.loc * env.LBS)
		return true;
	    ee = ee2;
	}
	return false;
    }

    /**
       offの地点で extentを分割する。
       
       <extent>の数が1つ増える。ただし、分割地点がちょうどextentの境界の場合は何もしない

       @param off	分割する地点。LBSの倍数であること
     */
    public void splitExtent(long off){
	UDF_ExtentElem ee = getExtent(off);
	if(ee == null)
	    return;
	if(off == ee.off)
	    return;
	int idx = extents_elem.indexOf(ee);
	UDF_ExtentElem new_pre = new UDF_ExtentElem(ee);
	UDF_ExtentElem new_post = new UDF_ExtentElem(ee);
	//前部分
	new_pre.loc = ee.loc;
	new_pre.len = off - ee.off;
	new_pre.off = ee.off;
	//後部分
	new_post.loc = ee.loc + (int)(new_pre.len / env.LBS);
	new_post.len = ee.len - new_pre.len;
	new_post.off = ee.off + new_pre.len;

	extents_elem.remove(idx);
	extents_elem.insertElementAt(new_post, idx);
	extents_elem.insertElementAt(new_pre, idx);
    }
    /**
       extentsの基底化をする。

       <extent>を全て基底のパーティション参照のもの(-1)に変換する。

       また、長さ0の extentsを削除し、連続するExtentを結合する。
       これは Sparing Tableや VATで partitionを表わす extentsに長さ0のものが
       あらわれることがあり、それを削除するために udf150.UDF_Imageおよび
       udf250.UDF_Imageから呼ばれるためだけに存在する。

       その他の用途ではこのメソッドは使うべきでない。
     */
    public void blessExtent() throws UDF_PartMapException, UDF_InternalException{
	Vector v = new Vector();
	final boolean verbose = false;

	//try{
	retry:
	    for(int i=0 ; i<extents_elem.size() ; ++i){
		if(verbose){
		    System.err.println("IN:"+i);
		    debug(0);
		}
		UDF_ExtentElem ee = (UDF_ExtentElem)extents_elem.elementAt(i);
		if(ee.partno < 0){//partnoが負のものはそのまま
		    UDF_ExtentElem nee = new UDF_ExtentElem(ee);
		    //System.err.println("ADD1");
		    v.add(nee);
		}
		else if(ee.partno >= 0){
		    //参照先
		    if(verbose)
			System.err.println("reference subno = " + getPartSubno());
		    UDF_Extent ref = env.getPartMapExtent(ee.partno, getPartSubno());

		    //(このeeの開始位置が含まれる、参照先の eeを探す)		    
		    UDF_ExtentElem ref_ee = ref.getExtent(ee.loc * env.LBS);
		    //(このeeの終了位置が含まれる、参照先の eeを探す)		    
		    UDF_ExtentElem ref2_ee;

		    if(ee.len > 0)
			ref2_ee = ref.getExtent(ee.loc * env.LBS + ee.len - 1);
		    else
			ref2_ee = ref_ee;

		    if(ref2_ee == null){
			throw new UDF_InternalException(this, "blessExtent: extent position of size is invalid: start_pos=" + ee.loc * env.LBS + "(lbn=" + ee.loc + ") end_pos=" + (ee.loc * env.LBS + ee.len - 1) + "(lbn=" + ((ee.loc * env.LBS + ee.len - 1) / env.LBS) + ")");
		    }

		    //1つのextentにおさまっていない場合はおさまるように分割をして
		    //retryする。次はちゃんと分割できるはず
		    if(ref_ee != ref2_ee){
			/*

					  eeこの長さを求める(=x)
                                         |-------->
                                                  v
			           ee.loc*env.LBS            ee.loc*LBS+ee.len
			ee	         |-------------------------|
			ref_ee  |-----------------|-----------------------|
                                off0               0ff1
			*/ 

			long off1 = ref_ee.off + ref_ee.len;
			long x = off1 - ee.loc * env.LBS;
			
			splitExtent(ee.off + x);
			i--;//ループで自動で1足されるので引いておく。
			continue retry;//このインデックスのeeをやりなおす。
		    }
		    else{
			/*

			           ee.loc*env.LBS            ee.loc*LBS+ee.len
			ee	         |-------------------------|
			ref_ee  |-----------------------------------------|
                                off0     
				|-------->この長さを求める(=x)
			*/ 

			long x = ee.loc * env.LBS - ref_ee.off;
			UDF_ExtentElem nee = new UDF_ExtentElem(ee);
			nee.loc = ref_ee.loc + (int)(x / env.LBS);
			nee.partno = ref_ee.partno;

			if(verbose){
			    System.err.println("PUT:"+i);
			    nee.debug(0);
			}
			v.add(nee);
		    }
		}
	    }
	    truncExtent(0);
	    boolean ng = false;
	    for(int i=0 ; i<v.size() ; ++i){
		UDF_ExtentElem ee = (UDF_ExtentElem)v.elementAt(i);
		if(ee.partno != -1){
		    ng = true;
		}
		addExtent(ee);
	    }
	    if(ng){
		//System.err.println("try bless again");
		//System.err.println(getPartSubno());
		//debug(0);
		setPartSubno(0);
		blessExtent();
		//throw new UDF_InternalException(this, "cannot bless extents");
	    }
	    //System.err.println("E"+hashCode());
	    //debug(0);
	/*
	catch(UDF_NotImplException e){
	    e.printStackTrace();
	}
	*/
	normalize();
    }

    /**
       extentsの正規化をする。

       長さ0の extentsを削除し、連続するExtentを結合する。
       これは Sparing Tableや VATで partitionを表わす extentsに長さ0のものが
       あらわれることがあり、それを削除するために udf150.UDF_Imageおよび
       udf250.UDF_Imageから呼ばれるためだけに存在する。

       その他の用途ではこのメソッドは使うべきでない。

     */
    public void normalize(){
	boolean update_flg = true;

	if(extents_node == null)
	    return;

	while(update_flg){
	    update_flg = false;
	    for(Iterator i=extents_elem.iterator() ; i.hasNext() ; ){
		UDF_ExtentElem e = (UDF_ExtentElem)i.next();
		if(e.getLen() == 0){
		    i.remove();
		    update_flg = true;
		    break;
		}
	    }
	}
	rebuildExtentNode();
    }

    protected void readExtentFromXML(Node n) throws UDF_Exception{
	preReadFromXMLHook(n);

	NodeList nl = n.getChildNodes();

	if(nl == null || nl.getLength() < 2){
	    return;
	}

	
	for(int i = 0 ; i < nl.getLength() ; ++i){

	    Node child = nl.item(i);
	    if(child.getNodeType() != Node.ELEMENT_NODE){
		continue;
	    }

            if(!child.getLocalName().equals("extents")){
		continue;
	    }

	    NodeList nl2 = child.getChildNodes();

	    for(int ii = 0 ; ii < nl2.getLength(); ++ii){

		Node extent = nl2.item(ii);

		if(extent.getNodeType() != Node.ELEMENT_NODE){
		    continue;
		}

		if(extent.getLocalName().equals("extent")){
		    Element ext = (Element)extent;

		    long len = UDF_Util.str2l(ext.getAttribute("len"));
		    String str = ext.getAttribute("sec");
		    
		    if (str.equals("")) {
			int lbn = UDF_Util.str2i(ext.getAttribute("lbn"));
			int partno = UDF_Util.str2i(ext.getAttribute("partno"));
			int flag = UDF_Util.str2i(ext.getAttribute("flag"));
			addExtent(lbn, partno, len, flag, false);
		    }
		    else {
			int sec = UDF_Util.str2i(str);
			//String id = ext.getAttribute("id");
			addExtent(sec, -1, len, 0, false);
		    }
		}
		else if(extent.getLocalName().equals("multiple-extent")){
		    Element ext = (Element)extent;

		    long len = UDF_Util.str2l(ext.getAttribute("len"));
		    String str = ext.getAttribute("sec");
		    int times = UDF_Util.str2i(ext.getAttribute("times"));
		    int step = UDF_Util.str2i(ext.getAttribute("step"));
		    
		    if (str.equals("")) {
			int lbn = UDF_Util.str2i(ext.getAttribute("lbn"));
			int partno = UDF_Util.str2i(ext.getAttribute("partno"));
			int flag = UDF_Util.str2i(ext.getAttribute("flag"));
			addExtentElem(new UDF_MultipleExtentElem(lbn, partno, len, flag, times, step));
		    }
		    else {
			int sec = UDF_Util.str2i(str);
			//String id = ext.getAttribute("id");
			addExtentElem(new UDF_MultipleExtentElem(sec, -1, len, 0, times, step));
		    }
		}
	    }
	    return;
	}
    }

    public void readFromXML(Node n) throws UDF_Exception{
	preReadFromXMLHook(n);//これをして属性をコピーしよう

	try{
	    int subno = Integer.parseInt(((Element)n).getAttribute("subno"));
	    setPartSubno(subno);
	    removeAttribute("subno");
	}
	catch(NumberFormatException e){
	    setPartSubno(0);
	}

	if(((Element)n).getAttribute("mirror").equals("yes")){
	    setMirror(true);
	    setPartSubno(1);
	}


	readExtentFromXML(n);

	NodeList nl = n.getChildNodes();
	if(nl == null || nl.getLength() < 2){
	    return;
	}

	for(int i=0 ; i<nl.getLength() ; ++i){

	    Node child = nl.item(i);
	    if(child.getNodeType() != Node.ELEMENT_NODE){
		continue;
	    }

	    String tagName = child.getLocalName();
	    String prefix = child.getPrefix();
	    String className = child.getLocalName();

	    if(className.equals("extents"))
		continue;

	    try{
		UDF_Element elm = UDF_Element.genElement(className, this, prefix, tagName);
		elm.readFromXML(child);
		appendChild(elm);
	    }
	    catch(ClassNotFoundException e){
		throw new UDF_XMLException(this, "XML: Unknown Element:" + className);
	    }
	}
	//読んだら消す(もういらないので)
	n.getParentNode().removeChild(n);
    }

    public long readFrom(UDF_RandomAccess f) throws IOException,UDF_Exception {
	return 0;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException,UDF_Exception{

	UDF_RandomAccessExtent rae = genRandomAccessExtent();

	UDF_ElementList children = getChildList();
	Iterator it = children.iterator(); 
	while(it.hasNext()){
	    UDF_Element child = (UDF_Element)it.next();
	    debugMsg(3, "====> " + child.getName() +".writeTo(f) : " + rae.getAbsPointer());
	    child.writeTo(rae);
	    debugMsg(3, "<==== " + child.getName() +".writeTo(f) done: " + rae.getAbsPointer());
	}
	

	return (int)(rae.length());
    }

    /**
       UDF_Extentが等しいかどうか。

       等しい UDF_Extentとは

       1) extentが完全に同じ
       2) subnoが同じ

       ものである。

       注)このメソッドは子供が同じかどうかの確認をしていない。

    public boolean equals(UDF_Extent extent){
    *
	if(getMirror() != extent.getMirror())
	    return false;
	*
	if(getPartSubno() != extent.getPartSubno())
	    return false;

	UDF_ExtentElem[] this_ext = getExtent();
	UDF_ExtentElem[] op_ext = extent.getExtent();
	if(this_ext.length != op_ext.length)
	    return false;
	for(int i=0 ; i<this_ext.length ; ++i)
	    if(!this_ext[i].equals(op_ext[i]))
		return false;
	return true;
    }
     */

    public UDF_ErrorList verify() throws UDF_Exception{
	
	UDF_ErrorList el = new UDF_ErrorList();
	UDF_ElementBase[] children = getChildren();
	
	
	for(int i = 0; i < children.length; i++)
	    el.addError(children[i].verify());
	
	return el;
    }

    public void recalc(short type, UDF_RandomAccess f){
	try{
	    UDF_ElementBase[] children = getChildren();
	    
	    if(type == RECALC_GP){
		long ofst = 0;
		UDF_RandomAccessExtent rae = genRandomAccessExtent();
		
		for(Iterator i = getChildList().iterator() ; i.hasNext() ; ){
		    UDF_Element child = (UDF_Element)i.next();
		    child.setOffset(ofst);
		    child.recalc(type, rae);
		    ofst += child.getLongSize();
		}
	    }
	    /*
	      ここでは子を追加するだけにして、
	      細かい位置計算は子供(UDF_pad)でするようにしてみた。
	    */
	    else if(type == RECALC_PAD) {
		if (children.length < 1) {
		    return;
		}
		
		int last = children.length - 1;
		int rsize = getSize();
		
		//　最後の Element が UDF_pad でなければ追加しておく　//
		//↓これだと <UDF_pad>というXMLでないとヒットしないような気がする
		if (!UDF_pad.class.isAssignableFrom(children[last].getClass())) {
		    
		    UDF_pad pad = (UDF_pad) createElement("UDF_pad", null, "UDF_pad");
		    pad.setData(new byte[0]);
		    pad.setAlign(getLongSize());
		    appendChild(pad);
		    
		    children = getChildren();
		    last++;
		}
		
		//最後にある padを強引に Extentのサイズに合うようにする
		UDF_pad lastpad = (UDF_pad)children[last];
		lastpad.setAlign(getLongSize());

		//子供を recalcする
		super.recalc(type, f);
	    }
	    else
		super.recalc(type, f);
	}
	catch(Exception e){
	    ignoreMsg("UDF_Extent.recalc", e);
	}
    }
    
    public void debug(int indent){
	System.err.println("subno=" + getPartSubno() + " num of extent=" + getExtentElem().size());
	for(Iterator it=getExtentElem().iterator() ; it.hasNext() ; ){
	    ((UDF_ExtentElem)it.next()).debug(0);
	}
    }

    public void preReadHook(UDF_RandomAccess f) throws IOException{
	//UDF_Extentのgpは 最初のExtentの位置
	super.preReadHook(f);
	UDF_RandomAccessExtent rae = genRandomAccessExtent();
	setGlobalPoint(rae);
    }

    /**
       エレメントを複製する。
       
       @return 複製されたエレメント。
    */
    public UDF_Element duplicateElement(){
	UDF_Extent dup = (UDF_Extent)createElement(getClass().getName(), getPrefix(), getName());
	dup.duplicateHook(this);
	dup.copyExtent(this);

	UDF_ElementBase[] child = getChildren();
	if(child.length > 0){
	    for(int i=0 ; i<child.length ; ++i){
		dup.appendChild((UDF_Element) child[i].duplicateElement());
	    }
	}
	
	dup.setPartSubno(getPartSubno());
	return dup;
    }

    /**
       この UDF_Elementの子供を全て削除する
       
       このメソッドは同時にXMLの子Node も削除する
     */
    public void removeAllChildren(){
	UDF_Element[] e = getChildren();
	for(int i=0 ; i<e.length ; ++i)
	    removeChild(e[i]);
	e = getChildren();
    }

    /*
    public static void main(String argv[]){
	UDF_Env env = new UDF_Env();
	UDF_Image img = UDF_ImageFactory.genImage(env, 0x250);
	UDF_Extent ext = new UDF_Extent(img, null, null);

	ext.addExtent(100, -1, 1024*1024);
	ext.debug(0);
	ext.splitExtent(40*1024);
	ext.debug(0);
    }
    */
}
