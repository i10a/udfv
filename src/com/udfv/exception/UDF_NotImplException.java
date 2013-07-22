/*
 */
package com.udfv.exception;
import com.udfv.core.*;
import com.udfv.util.*;

/**
   まだ実装していないことを示し、早く実装するよう促すための例外。

   早く実装しよう！！
 */
public class UDF_NotImplException extends UDF_Exception
{
    public UDF_NotImplException(UDF_Element elem, String msg, short typ, short category, int info){
	super(elem, msg, typ, category, info);
    }
    public UDF_NotImplException(UDF_Element elem, String msg){
	super(elem, msg);

    }
}
