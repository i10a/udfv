/*
 */
package com.udfv.core;

public interface UDF_Numeric extends Comparable
{
    /**
       vを足す

       @param v

       @return 足した結果の値
     */
    long addValue(int v);
    /**
       vを足す

       @param v

       @return 足した結果の値
     */
    long addValue(long v);

    /**
       比較する
     */
    int compareTo(Object obj);
}