/*
 */
package com.udfv.exception;
import com.udfv.core.*;
import com.udfv.util.*;

/**
   ICBに関する例外
 */
public class UDF_ICBException extends UDF_Exception
{
    /**
       ICBのファイルタイプが期待したものでない。infoに問題のファイルタイプを入れること
     */
    public static final short C_BADFILETYPE = 1;
    /**
       ICBのフラグが期待したものでない。infoに問題のファイルタイプを入れること
     */
    public static final short C_BADFLAGS = 2;

    public UDF_ICBException(UDF_Element elem, String msg, short typ, short category, int info){
	super(elem, msg, typ, category, info);
    }
    public UDF_ICBException(UDF_Element elem, String msg){
	super(elem, msg);
    }
}
