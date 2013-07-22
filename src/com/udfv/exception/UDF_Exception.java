/*
 */
package com.udfv.exception;
import com.udfv.core.*;
import com.udfv.util.*;

/**
   UDFVライブラリ全ての例外の基底となるクラス。
 */
abstract public class UDF_Exception extends Exception
{
    protected UDF_Element errelem;
    protected String errmsg;
    protected short errtyp;
    protected short errcategory;
    protected int errinfo;

    public static final short T_BASIC     = 0x0001;
    public static final short T_UNKNWON   = 0x0002;
    public static final short T_ECMA119   = 0x0004;
    public static final short T_ECMA167_2 = 0x0008;
    public static final short T_ECMA167_3 = 0x0010;
    public static final short T_UDF102    = 0x0100;
    public static final short T_UDF150    = 0x0200;
    public static final short T_UDF200    = 0x0400;
    public static final short T_UDF201    = 0x0800;
    public static final short T_UDF250    = 0x1000;

    /**
       @param elem	例外が発生したクラス
       @param msg	例外メッセージ
       @param typ	例外タイプ(UDFのバージョン)
       @param category	各例外に用意されたサブカテゴリ(例外詳細)
       @param info	例外情報(オプション)

コーディングの注意

elemには例外の個所が別りやすくなるため、例外が発生したクラスを入れなけ
ればならない。ただし、UDF_RandomAccess等 UDF_Elementから派生したク
ラス以外を使用している場合はnullを指定する。

msgは例外が発生したときの状況を説明するためのメッセージで人間が可読な
メッセージを英語で入れなければらなない。

typはは例外が発生したイメージの UDFのリビジョンを入れなければならない。

categoryは例外の詳細を数値で分類したもので、指定の仕方は各例外によって
異なる。

infoはさらなる例外の詳細である。必用ならば指定すること。

     */
    public UDF_Exception(UDF_Element elem, String msg, short typ, short category, int info){
	errelem = elem;
	errmsg = msg;
	errtyp = typ;
	errcategory = category;
	errinfo = info;
    }
    public UDF_Exception(Throwable cause, UDF_Element elem, String msg, short typ, short category, int info){
	super(cause);
	errelem = elem;
	errmsg = msg;
	errtyp = typ;
	errcategory = category;
	errinfo = info;
    }

    public UDF_Exception(UDF_Element elem, String msg){
	errelem = elem;
	errmsg = msg;
    }
    public UDF_Exception(Throwable cause, UDF_Element elem, String msg){
	super(cause);
	errelem = elem;
	errmsg = msg;
    }

    public UDF_Exception(UDF_Exception cause){
	super(cause);
	errelem = cause.getUDFElement();
	errmsg = cause.getMessage();
    }

    public UDF_Exception(Exception cause){
	super(cause);
    }

    public UDF_Element getUDFElement(){
	return errelem;
    }
    public String getMessage(){
	return errmsg;
    }
    public void printmsg(){
	if(errmsg != null)
	    System.err.println(errmsg);
	if(errelem != null)
	    errelem.debug(0);
    }

    public void printStackTrace(){
//	printmsg();
	if(errelem != null)
	    errelem.debug(0);

	super.printStackTrace();
//	System.exit(0);
    }
}
