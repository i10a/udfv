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

