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


package com.udfv.util;

import java.io.*;
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import com.udfv.exception.*;
import org.apache.xerces.dom.*;


public class UDF_Error implements Comparable
{
    // エラーカテゴリ
    static public final short C_NOERR   = 0x0000;
    static public final short C_ECMA167 = 0x0001;
    static public final short C_UDF102  = 0x0002;
    static public final short C_UDF150  = 0x0004;
    static public final short C_UDF200  = 0x0008;
    static public final short C_UDF201  = 0x0010;
    static public final short C_UDF250  = 0x0020;
    static public final short C_UDF260  = 0x0040;

    static public final short C_DVD_VIDEO  = 0x1000;
    
    static public final short C_UDFALL  = (C_UDF102 | C_UDF150 | C_UDF200 | C_UDF201 | C_UDF250 | C_UDF260);
    static public final short C_UDF20X  = (C_UDF200 | C_UDF201);
    
    // エラーレベル
    static public final short L_NOERR   = 0x0000;
    static public final short L_CAUTION = 0x0001;  //!< 規格では特に触れられていないが、注意が必要
    static public final short L_WARNING = 0x0002;  //!< 規格に違反はしていないが、その使用は勧められていない
    static public final short L_ERROR   = 0x0004;  //!< 規格に違反しているが、値そのものは独立しており、他には影響が出ない
    static public final short L_ALERT   = 0x0008;  //!< 規格に違反しており、領域を認識することができない
    
    
    /**
       デフォルトコンストラクタ。
       エラーではないインスタンスを生成します。
    */
    public UDF_Error()
    {
	setAllMember(C_NOERR, L_NOERR, -1, "", "", "");
    }
    
    /**
       コンストラクタ。
       
       @param category  エラーカテゴリ。
       @param level     エラーレベル。
    */
    public UDF_Error(short category, short level)
    {
	setAllMember(category, level, -1, "", "", "");
    }
    
    /**
       コンストラクタ。
       
       @param category  エラーカテゴリ。
       @param level     エラーレベル。
       @param rname     エラーの直接の原因となる位置、個所などを特定するための文字列。
    */
    public UDF_Error(short category, short level, String rname)
    {
	setAllMember(category, level, -1, rname, "", "");
    }
    
    /**
       コンストラクタ。
       
       @param category  エラーカテゴリ。
       @param level     エラーレベル。
       @param rname     エラーの直接の原因となる位置、個所などを特定するための文字列。
       @param msg       エラーの内容を解説するための文字列。
    */
    public UDF_Error(short category, short level, String rname, String msg)
    {
	setAllMember(category, level, -1, rname, msg, "");
    }
    
    /**
       コンストラクタ。
       
       @param category  エラーカテゴリ。
       @param level     エラーレベル。
       @param rname     エラーの直接の原因となる位置、個所などを特定するための文字列。
       @param msg       エラーの内容を解説するための文字列。
       @param refer     エラーの根拠となる規格のセクション。数値のみの文字列を指定します。
    */
    public UDF_Error(short category, short level, String rname, String msg, String refer)
    {
	setAllMember(category, level, -1, rname, msg, refer);
    }
  
    /**
       コンストラクタ。
       
       @param category  エラーカテゴリ。
       @param level     エラーレベル。
       @param rname     エラーの直接の原因となる位置、個所などを特定するための文字列。
       @param msg       エラーの内容を解説するための文字列。
       @param refer     エラーの根拠となる規格のセクション。数値のみの文字列を指定します。
       @param expect    本来記録されていることが期待される値
       @param record    実際に記録されている値
    */
    public UDF_Error(
		     short category,
		     short level,
		     String rname,
		     String msg,
		     String refer,
		     long record,
		     long expect
		     )
    {
        this(category, level, rname, msg, refer, Long.toString(record), Long.toString(expect));
    }

    /**
       コンストラクタ。
       
       @param category  エラーカテゴリ。
       @param level     エラーレベル。
       @param rname     エラーの直接の原因となる位置、個所などを特定するための文字列。
       @param msg       エラーの内容を解説するための文字列。
       @param refer     エラーの根拠となる規格のセクション。数値のみの文字列を指定します。
       @param expect    本来記録されていることが期待される値の文字列。
       @param record    実際に記録されている値の文字列。
    */
    public UDF_Error(
		     short category,
		     short level,
		     String rname,
		     String msg,
		     String refer,
		     String record,
		     String expect
		    )
    {
	setAllMember(category, level, -1, rname, msg, refer);
	recorded_ = record;
	expected_ = expect;
    }
    
    /**
       エラーの原因となった個所の、親レベルの情報を追加します。
       親レベルのエラーとその子供との間はピリオドで連結して表現されます。
       
       @param rname  エラーの原因となる位置、個所などを特定するための文字列。
    */
    public void setRName(String rname)
    {
	rname_ = (rname_.equals("")) ? rname : rname + "." + rname_;
    }
    
    /**
       エラーのカテゴリを取得します。
       
       @return エラーカテゴリ。
    */
    public short getCategory()
    {
	return category_;
    }
    
    /**
       エラーのレベルを取得します。
       
       @return エラーレベル。
    */
    public short getLevel()
    {
	return level_;
    }
    
    /**
       そのレベルより深刻なレベルのエラーであるかどうかを判定します。
       
       @return より深刻なエラーである場合、true を返します。そうでない場合は、false を返します。
               指定したレベルと同じレベルのエラーである場合はfalse を返します。
    */
    public boolean hasMoreErrorLevel(short level)
    {
	if(level < level_)
	    return true;
	else
	    return false;
    }
    
    /**
       インスタンスがエラーであるかどうかを判定します。
       カテゴリがC_NOERR ではなく、L_ERROR 以上のレベルであればエラーとみなします。
       
       @return エラーである場合、true を返します。エラーでない場合は、false を返します。
    */
    public boolean isError()
    {
	if(category_ == C_NOERR || level_ < L_ERROR)
	    return false;
	else
	    return true;
    }
    
    /**
       エラーがXML 構築の障害となりうるかどうかを判定します。
       
       @return 特に問題がない場合、true を返します。そうでない場合は、false を返します。
    */
    public boolean isXMLSafe()
    {
	if(level_ == L_ALERT)
	    return false;
	else
	    return true;
    }
    
    /**
       エラーカテゴリを設定します。
       
       @param category 設定するカテゴリの値。
    */
    public void setCategory(short category)
    {
	category_ = category;
    }
    
    /**
       エラーの直接の原因となる位置、個所などを特定するための文字列を設定します。
       
       @param rname 適切な文字列。
    */
    public void setCause(String rname)
    {
	rname_ = rname;
    }
    
    /**
       本来記録されるべきことが期待される値の文字列を設定します。
       
       @param val 適切な文字列。
    */
    public void setExpectedValue(String val)
    {
	expected_ = val;
    }
    
    /**
       エラーが発生した位置（バイト）を設定します。
       
       @param gp 絶対位置（バイト）。
    */
    public void setGlobalPoint(long gp)
    {
	gp_ = gp;
    }
    
    /**
       エラーレベルを設定します。
       
       @param level 設定するレベルの値。
    */
    public void setLevel(short level)
    {
	level_ = level;
    }
    
    /**
       エラーの内容を解説するための文字列を設定します。
       
       @param msg 適切な文字列。
    */
    public void setMessage(String msg)
    {
	msg_ = msg;
    }
    
    /**
       実際に記録されている値の文字列を設定します。
       
       @param val 適切な文字列。
    */
    public void setRecordedValue(String val)
    {
	recorded_ = val;
    }
    
    /**
       エラーの根拠となる規格のセクションを設定します。
       文字列は、基本的に数値のみの文字列を指定します。
       
       @param refer 適切な文字列。
    */
    public void setRefer(String refer)
    {
	refer_ = refer;
    }
    
    /**
       エラーを文字列として出力する。
       
       @return エラー文字列。
    */
    public String toString()
    {
	String category = new String();
	String level = new String();
	String ret = new String();
	
	
	category = getCategoryString();
	level = "[" + getLevelString() + "]";
	

	ret = "[" + category + "] " + level;
	if(gp_ != -1)
	    ret += " [LSN " + String.valueOf(gp_ / UDF_Env.LBS) + "]";
	
	if(!refer_.equals(""))
	    ret += " [" + category + " " + refer_ + "]";
	
	if(!rname_.equals(""))
	    ret += "\n[" + rname_ + "]\n";
	else
	    ret += "\n";
	
	if(!recorded_.equals(""))
	    ret += "Recorded: '" + recorded_ + "'\n";
	if(!expected_.equals(""))
	    ret += "Expected: '" + expected_ + "'\n";
	
	ret += msg_ + "\n";
	
	
	return UDF_Util.toSystemNewLine(ret);
    }
    
    /**
       エラーをXML として出力する。
       
       @param doc XML ドキュメントインスタンス。
       @param parent エラーノードを追加するエレメント。 
    */
    public void toXML(Document doc, Element parent)
    {
	
	Element e_error = doc.createElement(_NODE_ERROR);
	String  tmp = null;
	
	
	Element e_category = doc.createElement(_NODE_CATEGORY);
	e_category.appendChild(doc.createTextNode(getCategoryString()));
	e_error.appendChild(e_category);
	
	Element e_level = doc.createElement(_NODE_LEVEL);
	e_level.appendChild(doc.createTextNode(getLevelString()));
	e_error.appendChild(e_level);
	
	
	if(gp_ != -1){
	    
	    Element e_lsn = doc.createElement(_NODE_LSN);
	    e_lsn.appendChild(doc.createTextNode(String.valueOf(gp_ / UDF_Env.LBS)));
	    e_error.appendChild(e_lsn);
	}
	
	if(!refer_.equals("")){
	    
	    Element e_refer = doc.createElement(_NODE_REFER);
	    e_refer.appendChild(doc.createTextNode(refer_));
	    e_error.appendChild(e_refer);
	}
	
	if(!rname_.equals("")){
	    
	    Element e_rname = doc.createElement(_NODE_RNAME);
	    e_rname.appendChild(doc.createTextNode(rname_));
	    e_error.appendChild(e_rname);
	}
	
	if(!recorded_.equals("")){
	    
	    Element e_recorded = doc.createElement(_NODE_RECORDED);
	    e_recorded.appendChild(doc.createTextNode(recorded_));
	    e_error.appendChild(e_recorded);
	}
	
	if(!expected_.equals("")){
	    
	    Element e_expected = doc.createElement(_NODE_EXPECTED);
	    e_expected.appendChild(doc.createTextNode(expected_));
	    e_error.appendChild(e_expected);
	}
	
	Element e_msg = doc.createElement(_NODE_MESSAGE);
	e_msg.appendChild(doc.createTextNode(msg_));
	e_error.appendChild(e_msg);
	
	
	parent.appendChild(e_error);
    }
    
    /**
       このオブジェクトと指定されたオブジェクトの順序を比較する。
       
       @param o 比較対象のObject 。
       @return  このオブジェクトが指定されたオブジェクトより小さい場合は負の整数、等しい場合はゼロ、大きい場合は正の整数を返す。
    */
    public int compareTo(Object o){
	
	UDF_Error err = (UDF_Error)o;
	return (int)(gp_ - err.gp_);
    }
    
    /**
       エラーカテゴリ文字列を取得する。
       
       @return エラーカテゴリ文字列。
    */
    private String getCategoryString(){
	String ret = "";

	if((category_ & C_DVD_VIDEO) != 0)
	    ret += "DVD_VIDEO ";

	switch(category_){
	case C_ECMA167:
	    return ret + "ECMA167";
	case C_UDF102:
	    return ret + "UDF1.02";
	case C_UDF150:
	    return ret + "UDF1.50";
	case C_UDF200:
	    return ret + "UDF2.00";
	case C_UDF201:
	    return ret + "UDF2.01";
	case C_UDF250:
	    return ret + "UDF2.50";
	case C_UDF260:
	    return ret + "UDF2.60";
	}
	return  ret + "UNKNOWN";
    }
    
    /**
       エラーレベル文字列を取得する。
       
       @return エラーレベル文字列。
    */
    private String getLevelString(){
	
	switch(level_){
	case L_ALERT:
	    return "Alert";
	case L_ERROR:
	    return "Error";
	case L_WARNING:
	    return "Warning";
	case L_CAUTION:
	    return "Caution";
	}
	
	return "Unknown";
    }
    
    
    private void setAllMember(short category, short level, long gp, String rname, String msg, String refer)
    {
	rname_    = rname;
	category_ = category;
	level_    = level;
	msg_      = msg;
	refer_    = refer;
	gp_       = gp;
	recorded_ = new String();
	expected_ = new String();
    }
    
    
    final private String _NODE_ERROR    = "error";
    final private String _NODE_CATEGORY = "category";
    final private String _NODE_LEVEL    = "level";
    final private String _NODE_LSN      = "lsn";
    final private String _NODE_REFER    = "reference";
    final private String _NODE_RNAME    = "rname";
    final private String _NODE_RECORDED = "recorded";
    final private String _NODE_EXPECTED = "expected";
    final private String _NODE_MESSAGE  = "message";
    
    private String  rname_;
    private short   category_;
    private short   level_;
    private long    gp_;
    private String  msg_;
    private String  refer_;
    private String  recorded_;
    private String  expected_;
}


