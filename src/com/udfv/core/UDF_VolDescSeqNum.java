/*
 */
package com.udfv.core;

import java.io.*;
import com.udfv.core.*;
import com.udfv.ecma167.*;

/**
   Volume Descriptor Sequence Numberを持つクラスのインターフェース
 */
public interface UDF_VolDescSeqNum{

    /**
      VolDescSeqNumberを取得する。

      @return 取得した VolDescSeqNumber を返す。
    */
    public UDF_uint32 getVolDescSeqNumber();

    /**
      VolDescSeqNumberを設定する。

      @param	v 設定する値。
    */
    public void setVolDescSeqNumber(UDF_uint32 v);
}
