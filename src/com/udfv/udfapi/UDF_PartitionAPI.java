package com.udfv.udfapi;

import java.lang.*;
import java.io.*;
import com.udfv.core.*;
import com.udfv.ecma167.*;
import com.udfv.exception.*;

/**
   パーティションおよびパーティションマップに関するAPI
 */
public interface UDF_PartitionAPI {
    /*
      パーティションのサイズを変更する。

      @param policy 領域割当てポリシー
      @param map_no パーティションマップ番号。
      @param size 希望するパーティションの長さ（単位：バイト）。
      @throws UDF_IrregularArgumentException 存在しないマップ番号を指定した／サイズが異常。
    public void resizePartition(UDF_Policy policy, int partno, long size) throws UDF_Exception;
    */

    /**
       パーティションマップのサイズを変更する。

       @param policy 領域割当てポリシー
       @param partmapno パーティションマップ番号。
       @param size 希望するパーティションの長さ（単位：バイト）。
       @throws UDF_PartMapException 存在しないマップ番号を指定した／サイズが異常。
    */
    public void resizePartMap(UDF_Policy policy, int partmapno, long size) throws UDF_Exception,IOException;

    /**
       Virtual Partition Mapの subnoを1つ繰り上げる。

       その後 subnoが1のものを複製し、subnoを0とする。
       その際 id Attributeが被らないように、idをつけかえる。

       @param policy 領域割当てポリシー
       @param partmapno パーティションマップ番号。
       @throws UDF_PartMapException 存在しないマップ番号を指定した／サイズが異常。
    */
    public void shiftPartMapSubno(UDF_Policy policy, int partmapno) throws UDF_Exception,IOException;

}

