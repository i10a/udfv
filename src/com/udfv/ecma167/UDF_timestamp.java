/*
*/
package com.udfv.ecma167;

import java.io.*;
import java.util.*;
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.access.*;
import com.udfv.exception.*;
import com.udfv.ecma167.*;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextArea;

/**
  ECMA-167 1/7.3 Timestamp を表現するクラス。

*/
public class UDF_timestamp extends UDF_Element
{
    String my_timestr;
    /**
	（ローカル）クラス名を取得します
    */
    public static String getUDFClassName( ) {
        return "UDF_timestamp";
    }

    /**
       コンストラクタ

       @param elem 親
       @param name 名前
    */
    public UDF_timestamp(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? "UDF_timestamp" : name);	
	init();
    }

    /**
       エレメントのサイズを返す

       @return サイズ
    */
    public int getSize(){
	return 0+2+2+1+1+1+1+1+1+1+1;
    }

    /**
       エレメントのサイズを返す

       @return サイズ
    */
    public long getLongSize(){
	return (long)0+2+2+1+1+1+1+1+1+1+1;
    }
    private int typeAndTimeZone;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private int second;
    private int centiseconds;
    private int hundredsOfMicroseconds;
    private int microseconds;

    public int getCentiseconds() { return centiseconds; }
    public int getHundredsofMicroseconds() { return hundredsOfMicroseconds; }
    public int getMicroseconds() { return microseconds; }

    private void setNodeVal(){
	if(!marimite)
	    return;

	removeAllChildren();
	int tz = getTimeZone();
	int typ = getTypeOfTimeZone();
	
	String typtz = "" + typ;
	if((tz & 0x0800) == 0){

	    typtz += "+";
	}
	else{

	    typtz += "-";
	    tz = -tz;
	} 
	typtz += UDF_Util.i2d4(tz);

	String date = UDF_Util.i2d4(year) + "/" + UDF_Util.i2d2(month) + "/" + UDF_Util.i2d2(day);
	String time = UDF_Util.i2d2(hour) + ":" + UDF_Util.i2d2(minute) + ":" + UDF_Util.i2d2(second);
	String msec = UDF_Util.i2d2(centiseconds) + "." + UDF_Util.i2d2(hundredsOfMicroseconds) + "." + UDF_Util.i2d2(microseconds);
	my_timestr = typtz + " " + date + " " + time + " " + msec;

	if(marimite && getNode() != null){
	    Text text = getDocument().createTextNode(my_timestr); 
	    getNode().appendChild(text);
	}
    }

    public long readFrom(UDF_RandomAccess f) throws IOException, UDF_Exception{
	
	preReadHook(f);

	long pos0 = f.getPointer();
	typeAndTimeZone = f.readUint16();
	year = f.readUint16();
	month = f.readUint8();
	day = f.readUint8();
	hour = f.readUint8();
	minute = f.readUint8();
	second = f.readUint8();
	centiseconds = f.readUint8();
	hundredsOfMicroseconds = f.readUint8();
	microseconds = f.readUint8();
	long pos1 = f.getPointer();
	postReadHook(f);

	setNodeVal();
	return pos1 - pos0;
    }

    public long writeTo(UDF_RandomAccess f) throws IOException, UDF_Exception{
	long wsize = 0;
	wsize += f.writeUint16(typeAndTimeZone);
	wsize += f.writeUint16(year);
	wsize += f.writeUint8(month);
	wsize += f.writeUint8(day);
	wsize += f.writeUint8(hour);
	wsize += f.writeUint8(minute);
	wsize += f.writeUint8(second);
	wsize += f.writeUint8(centiseconds);
	wsize += f.writeUint8(hundredsOfMicroseconds);
	wsize += f.writeUint8(microseconds);
	return wsize;
    }

    /**
       XMLのノードを指定して読み込む

       @param n	読み込む先ノード
    */
    public void readFromXML(Node n) throws UDF_Exception{
	preReadFromXMLHook(n);
	NodeList nl = n.getChildNodes();
	if(nl == null){
	    return;
	}

	//           1         2         3
	// 01234567890123456789012345678901234
	// 1+0540 2004/01/20 15:42:05 00.00.00
	//
	String v = ((Text)nl.item(0)).getData();

	int type = UDF_Util.str2i(v.substring(0,1));
	int sign = v.charAt(1) == '+' ? 1 : -1;
	String substr = v.substring(1,6);
	if(substr.charAt(0) == '+')
	    substr = substr.substring(1, 5);
	int tz = UDF_Util.str2i(substr);
	int tmp = tz;
	if(tz < 0)
	    tmp = (Math.abs(tz) ^ 0x0fff) + 1;

	typeAndTimeZone = (type << 12) | tmp;
	year = UDF_Util.str2i(v.substring(7,11));
	month = UDF_Util.str2i(v.substring(12,14));
	day = UDF_Util.str2i(v.substring(15,17));
	hour = UDF_Util.str2i(v.substring(18,20));
	minute = UDF_Util.str2i(v.substring(21,23));
	second = UDF_Util.str2i(v.substring(24,26));
	centiseconds = UDF_Util.str2i(v.substring(27,29));
	hundredsOfMicroseconds = UDF_Util.str2i(v.substring(30,32));
	microseconds = UDF_Util.str2i(v.substring(33,35));

	setNodeVal();
    }

    public String getInfo(int indent){
	String a = "";
	a += UDF_Util.repeat(' ', indent);
	a += getName() + ":";
	a += my_timestr;
	a += "\n";
	return a;
    }    

    public void debug(int indent){
	System.err.println(getInfo(indent));
    }

//begin:add your code here
    
    public UDF_Element duplicateElement(){

	UDF_timestamp v = (UDF_timestamp)createElement(getClass().getName(), null, getName());
	v.duplicateHook(this);
	v.typeAndTimeZone = typeAndTimeZone;
	v.year = year;
	v.month = month;
	v.day = day;
	v.hour = hour;
	v.minute = minute;
	v.second = second;
	v.centiseconds = centiseconds;
	v.hundredsOfMicroseconds = hundredsOfMicroseconds;
	v.microseconds = microseconds;

	v.setNodeVal();
	
	return v;
    }

    /**
      UDF_timestamp の内容の検証を行います。
      間違った値が格納されていたとしても、読み込み自体には影響を与えないため、
      UDF_Exception() を発行することはない。
    */
    public UDF_ErrorList verify() throws UDF_Exception {

	UDF_ErrorList el = new UDF_ErrorList();

	short category = UDF_Error.C_ECMA167;

	UDF_Error err;

	int type = getTypeOfTimeZone();
	if (2 < type) {
	    err = new UDF_Error(category, UDF_Error.L_ERROR, "Type and Time Zone.Type");
	    err.setRefer("1/7.3.1");
	    err.setMessage("3 - 15 | Reserved for future standardisation.");
	    err.setRecordedValue(String.valueOf(type));
	    el.addError(err);
	}

	int tz = getTimeZone();
	if (tz == -2047) {
	    err = new UDF_Error(category, UDF_Error.L_WARNING, "Type and Time Zone.TimeZone");
	    err.setRefer("1/7.3.1");
	    err.setMessage("If the value is -2047, then no such value is specified.");
	    el.addError(err);
	}
	else
	if (tz < -1440 || 1440 < tz) {
	    err = new UDF_Error(category, UDF_Error.L_CAUTION, "Type and Time Zone.TimeZone");
	    err.setRefer("1/7.3.1");
	    err.setMessage("If the value is in the range -1440 to 1440 inclusive, then the value specifies the offset, in minutes, of the date and time of the day from Coordinated Universal Time. If the value is -2047, then no such value is specified.");
	    err.setRecordedValue(String.valueOf(tz));
	    el.addError(err);
	}

	el.setGlobalPoint(getGlobalPoint());
	return el;
    }

    /**
      Timestamp 構造のType And Time ZoneフィールドからTypeを取り出す。

      @return タイムゾーンの種類値
    */
    protected int getTypeOfTimeZone() {
       return ((typeAndTimeZone & 0xf000) >> 12);
    }

    /**
      Timestamp 構造のType And Time ZoneフィールドからTime Zone を取り出す。

      @return タイムゾーンの値
    */
    protected int getTimeZone() {

	int tz = typeAndTimeZone & 0x07ff;
	if ((typeAndTimeZone & 0x0800) != 0) {
	    tz = tz - 0x0800;
	}
	return tz;
    }

    /**
      UDF_timestamp の情報をjava.sql.Timestamp形式に変換する。

    */
    private java.sql.Timestamp getTimestamp( ) {

        String str;

        str  =       UDF_Util.i2d4(year);
        str += "-" + UDF_Util.i2d2(month);
        str += "-" + UDF_Util.i2d2(day);
        str += " " + UDF_Util.i2d2(hour);
        str += ":" + UDF_Util.i2d2(minute);
        str += ":" + UDF_Util.i2d2(second);
        str += "." + UDF_Util.i2d2(centiseconds);
        str +=       UDF_Util.i2d2(hundredsOfMicroseconds);
        str +=       UDF_Util.i2d2(microseconds);
        str +=       "000";

        return java.sql.Timestamp.valueOf(str);

    }

    /**
      UDF_timestamp の情報をjava.sql.Timestamp 形式に変換したUTC に変換する。

    */
    private java.sql.Timestamp getUTC( ) {

        long time = getTimestamp().getTime();

        int tz = getTimeZone();
        if (tz != -2047) {
	    time -= tz * 60 * 100;
        }

        return new java.sql.Timestamp(time);
    }

    /**
      UDF_timestamp オブジェクトで表される内容を、
      1970年 1月 1日、0時 0分 0秒 GMT (グリニッジ標準時) からのミリ秒数に変換した値を返す。

    */
    public long getTime() {
        return getUTC().getTime();
    }

    /**
      この UDF_timestamp オブジェクトが、指定された UDF_timestamp オブジェクトより遅い時刻かどうか判定する。

      @param ts 比較対象のUDF_timestamp
    */
    public boolean after(UDF_timestamp ts) {
        return getUTC().after(ts.getUTC());
    }

    /**
      この UDF_timestamp オブジェクトが、指定された UDF_timestamp オブジェクトより早い時刻かどうか判定する。

      @param ts 比較対象のUDF_timestamp
    */
    public boolean before(UDF_timestamp ts) {
        return getUTC().before(ts.getUTC());
    }

    /**
      この UDF_timestamp オブジェクトと指定された UDF_timestamp オブジェクトが等しいかどうかを判定する。

      @param ts 比較対象のUDF_timestamp
    */
    public boolean equals(UDF_timestamp ts) {
        return getUTC().equals(ts.getUTC());
    }

    /**
      順序付けのために 2 つの UDF_timestamp を比較する。

      <em>
      ※jdk1.3.1ではjava.util.Dateから継承されたメソッドが呼ばれ、
        jdk1.4.2ではjava.sql.Timestampで実装されたメソッドが呼ばれます。
      </em>

      @param ts 比較対象のUDF_timestamp
    */
    public int compareTo(UDF_timestamp ts) {
        return getUTC().compareTo(ts.getUTC());
    }

    /**
       現在の時間を設定する。

     */
    public void setDefaultValue(){
	Calendar now = Calendar.getInstance();
	int tz = now.get(Calendar.ZONE_OFFSET) / (60*1000);
	int typ = 1;
	String typtz = "" + typ;
	if(tz >= 0){

	    typtz += "+";
	}
	else{
	    typtz += "-";
	    tz = -tz;
	} 
	typtz += UDF_Util.i2d4(tz);

	year = now.get(Calendar.YEAR);
	month = now.get(Calendar.MONTH);
	day = now.get(Calendar.DAY_OF_MONTH);
	hour = now.get(Calendar.HOUR_OF_DAY);
	minute = now.get(Calendar.MINUTE);
	second = now.get(Calendar.SECOND);
	if(env.freeze_time){
	    year = 2002;
	    month = 3;
	    day = 4;
	    hour = 5;
	    minute = 6;
	    second = 7;
	}

	String date = UDF_Util.i2d4(year) + "/" + UDF_Util.i2d2(month) + "/" + UDF_Util.i2d2(day);
	String time = UDF_Util.i2d2(hour) + ":" + UDF_Util.i2d2(minute) + ":" + UDF_Util.i2d2(second);
	String msec = UDF_Util.i2d2(centiseconds) + "." + UDF_Util.i2d2(hundredsOfMicroseconds) + "." + UDF_Util.i2d2(microseconds);
	my_timestr = typtz + " " + date + " " + time + " " + msec;

	typeAndTimeZone = (1<<12) + tz;

	setNodeVal();
    }
    public String getStringData(){
	return my_timestr;
    }

    public JComponent getJInfo(){
	JComponent panel = createJInfoPanel(getName());
	JTextArea jt = new JTextArea(my_timestr);
	jt.setEditable(false);
	panel.add(jt);
	return panel;
    }

    //end:
};
