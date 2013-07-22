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
/**
 */

package com.udfv.udfapi;

import java.lang.*;
import java.io.*;
import com.udfv.core.*;
import com.udfv.ecma167.*;
import com.udfv.exception.*;

/*
方針
1)
  何かを書きかえるAPIは全て UDF_Policyを第一引数に持つ。
2)
  例え投げる必用がなくてもIOExceptionを投げるようインタフェースを
  定義する
3)
  UDF_APIはできるだけ publicな UDF_APIのインタフェースメソッドを使用しない
  (なぜなら UDF_APIのインタフェースメソッドの作成を新たに作成するとき
   全て実装しないと動かないというのは不便だからである)
4)
  mirrorが考えられうるシチュエーションでは mirrorを先に処理する。
  これは uniqueidのインクリメントの関係から先に mirrorを処理したほうが
  楽だからである。(mainは常に存在するので)

 */

public interface UDF_FileAPI {
    //errorは多岐にわたるので UDF_FileAPIExceptionと1つにしてみたが……
    /**
      ディレクトリを作成する。

      @param path 作成したいディレクトリ名（ルートからのパス含む）。
      @throws UDF_FileAPIException 既に同名のファイル／ディレクトリが存在している。／ディレクトリが作れない。
    */
    public void mkdir(UDF_Policy policy, String path, int adtype) throws UDF_Exception, IOException;

    /**
      ファイルを作成する。

      @param policy ポリシー。
      @param path 作成したいファイル名（rootからのパス含む）。
      @param adtype ADのタイプ。
      @throws UDF_EntryExistException 既に同名のファイル／ディレクトリが存在している。
      @throws UDF_NoEntryException ファイルまでのパスが存在しない。

      //@param lbn ファイルのデータを置く先頭のLBN。
      //引数の順番をCOREのメソッドと合わせました。LBN->partno->size (by issei)
    */
    public void mkfile(UDF_Policy policy, String path, int adtype) throws UDF_Exception, IOException;

    /**
      リンクファイルを作成する。

      @param name1 リンク対象となるファイル名（rootからのパス含む）。
      @param name2 作成したいリンクファイル名（rootからのパス含む）。
      @throws UDF_EntryExistException 既に同名のファイル／ディレクトリが存在している。
      @throws UDF_FileAPIException ファイルまでのパスが存在しない。
    */
    public void link(UDF_Policy policy, String name1, String name2) throws UDF_Exception, IOException;

    /**
      ファイル／ディレクトリを削除する。

      @param path 削除したいファイル／ディレクトリ名（rootからのパス含む）。
      @throws UDF_NoEntryException ファイル／ディレクトリのパスが存在しない。
    */
    public void remove(UDF_Policy policy, String path) throws IOException, UDF_Exception;

    /**
      ファイル／ディレクトリ名を変更する。

      @param dst 希望するファイル／ディレクトリ名。
      @param src 元のファイル／ディレクトリ名。
      @throws UDF_EntryExistException 既に同名のファイル／ディレクトリが存在している。
      @throws UDF_NoEntryException ファイル／ディレクトリのパスが存在しない。
    */
    public void rename(UDF_Policy policy, String dst, String src) throws IOException, UDF_Exception;

    /**
      ファイルのサイズを変更する。

      @param path サイズを変更したいファイルの名称（rootからのパス含む）。
      @param size 希望するサイズ。
      @throws UDF_NoEntryException ファイルのパスが存在しない。
      @throws UDF_IrregularArgumentException サイズが異常。
    */
    public void resize(UDF_Policy policy, String path, long size) throws UDF_Exception, IOException;

    /**
      ファイルのサイズを取得する。

      @param path ファイル名（rootからのパス含む）。
      @throws UDF_NoEntryException ファイルのパスが存在しない。
    */
    public long getFileSize(String path) throws UDF_Exception;

    /*
      ファイル／ディレクトリのタイプを変更する。

      @param path ファイル／ディレクトリ名（rootからのパス含む）。
      @param type 希望するタイプの値。
      @throws UDF_NoEntryException ファイルのパスが存在しない。
    public void chtype(String path, int type) throws UDF_Exception;
    */

    /**
      ファイル／ディレクトリのフラグを変更する。

      @param path ファイル／ディレクトリ名（rootからのパス含む）。
      @param flag 希望するフラグの値。
      @throws UDF_NoEntryException ファイルのパスが存在しない。
    */
    public void chflag(UDF_Policy policy, String path, int flag) throws IOException, UDF_Exception;

    /**
      ファイル／ディレクトリの所有者を変更する。

      @param path ファイル／ディレクトリ名（rootからのパス含む）。
      @param owner 希望するオーナーの値。
      @throws UDF_NoEntryException ファイルのパスが存在しない。
    */
    public void chown(UDF_Policy policy, String path, int owner) throws IOException, UDF_Exception;

    /**
      ファイル／ディレクトリのグループを変更する。

      @param path ファイル／ディレクトリ名（rootからのパス含む）。
      @param group 希望するグループの値。
      @throws UDF_NoEntryException ファイルのパスが存在しない。
    */
    public void chgrp(UDF_Policy policy, String path, int group) throws IOException, UDF_Exception;

    /**
      ファイルに指定の外部ファイルのデータを書き込む。

      @param path ファイル名（rootからのパス含む）。
      @param offset 書き込み始めるファイル上の位置。
      @param size 書き込むバイト数。
      @param src 読み込む外部ファイルのパス
      @param src_offset 外部ファイルの読み込みを始める位置。
      @throws UDF_NoEntryException ファイルのパスが存在しない。
      @throws UDF_IrregularArgumentException 外部ファイルのパスが間違っている／オフセット値が異常／サイズ値が異常。
    */
    public void writeFileFile(String path, long offset, long size, String src, long src_offset) throws UDF_Exception;

    /**
      ファイルの内容を外部ファイルに書き出す。

      @param path ファイル名（rootからのパス含む）。
      @param offset 読み込み始めるファイル上の位置。
      @param size 書き込むバイト数。
      @param src 書き込む外部ファイルのパス
      @param src_offset 外部ファイルの書き込みを始める位置。
      @throws UDF_NoEntryException ファイルのパスが存在しない。
      @throws UDF_IrregularArgumentException 外部ファイルのパスが間違っている／オフセット値が異常／サイズ値が異常。
    */
    public void readFileFile(UDF_Policy policy, String path, long offset, long size, String src, long src_offset) throws UDF_Exception, IOException;

    /**
      ファイルの指定の位置に指定の数値を指定の数だけ書き込む。

      @param path ファイル名（rootからのパス含む）。
      @param b 書き込む数値。
      @param offset 書き込み始めるファイル上の位置。
      @param size 書き込む回数（バイト数）。
      @throws UDF_NoEntryException ファイルのパスが存在しない。
    */
    public void writeFileByte(String path, byte b, long offset, long size) throws UDF_Exception;


    /**
      ファイル／ディレクトリの File Entry を取得する。

      @param path ファイル／ディレクトリ名（rootからのパス含む）。
      @param subno 副番号
      @return File Entry のUDF Element。
      @throws UDF_NoEntryException ファイルのパスが存在しない。
    */
    public UDF_FEDesc getFileEntry(String path, int subno) throws UDF_Exception;


    /**
      ファイル／ディレクトリのADのタイプを取得する。

      @param path ファイル名（rootからのパス含む）。
      @return ADのタイプ (bit0-2/Flags/ICB Tag)。
      @throws UDF_NoEntryException ファイルのパスが存在しない。
    */
    public int getAllocType(String path) throws IOException, UDF_Exception;

    /*
      ファイル／ディレクトリのADのタイプを設定する。

      また、変更後に格納されるADの形式を自動的に変換する。

      @param path ファイル／ディレクトリ名（rootからのパス含む）。
      @param type ADのタイプ。
      @throws UDF_NoEntryException ファイルのパスが存在しない。
      @throws UDF_IrregularArgumentException emmbedな形式のとき１論理ブロックに収まらない。
    public void setAllocDescType(String path, int type) throws UDF_Exception;
    */

    /**
      ファイル／ディレクトリのADを取得する。

      @param path ファイル／ディレクトリ名（rootからのパス含む）。
      @return ADの配列。
      @throws UDF_NoEntryException ファイルのパスが存在しない。
      @throws UDF_IrregularArgumentException AD配列を持たないファイルである。
    */
    public UDF_ADList getAllocDesc(String path) throws UDF_Exception;

    /**
      ファイル／ディレクトリが持つADに指定のADを追加する。

      @param path ファイル／ディレクトリ名（rootからのパス含む）。
      @param ad 追加したいAD配列。
      @throws UDF_NoEntryException ファイルのパスが存在しない。
      @throws UDF_IrregularArgumentException 指定のADは使用できない。
    */
    public void appendAllocDesc(UDF_Policy policy, String path, UDF_ADList ad) throws IOException, UDF_Exception;

    /**
      ファイル／ディレクトリが持つADから指定のADと一致するものを削除する。

      @param path ファイル／ディレクトリ名（rootからのパス含む）。
      @param ad 削除したいAD配列。
      @throws UDF_NoEntryException ファイルのパスが存在しない。
      @throws UDF_IrregularArgumentException 指定のADは存在しない。
    */
    public void removeAllocDesc(UDF_Policy policy, String path, UDF_ADList ad) throws IOException, UDF_Exception;

    /**
      ファイル／ディレクトリのADを置き換える。

      @param path ファイル／ディレクトリ名（rootからのパス含む）。
      @param ad 新しいAD配列。
      @throws UDF_NoEntryException ファイルのパスが存在しない。
      @throws UDF_IrregularArgumentException 指定のADは使用できない。
    */
    public void replaceAllocDesc(UDF_Policy policy, String path, UDF_ADList ad) throws IOException, UDF_Exception;

    /**
     */
    public void attachDataToFile(UDF_Policy policy, String path, String filename) throws UDF_Exception, IOException;
    /**
       ファイルやディレクトリのADに FLAG3のADを追加し、desc258を作成する

       FEのFLAGが3のときは何もしない

       desc258が複数個チェーンされているときは、最後のdescに追加される。

       追加された desc258の len-of-alloc-descは0である。

     */
    public void chainAllocDesc(UDF_Policy policy, String path) throws UDF_Exception, IOException;

    /**
       ファイルのAD の種類を入れ替える。
       現状はディレクトリのAD のみサポートしている。
       
       @param policy  ポリシー
       @param path    AD を入れ替えるファイルのパス。
       @param adtype  入れ替えるAD の種類。下位3ビットのみが使用される。
                      UDF_icbtag.SHORT_AD、UDF_icbtag.LONG_AD、UDF_icbtag.DIRECT の３つのみ指定可。
       
       @throws UDF_FileAPIException path で示されるファイルが存在しない。
                                    またはadtype にDIRECT を指定した場合に、ファイルの内容がFile Entryに収まりきらない。
       @throws IllegalArgumentException adtype がExtentded AD のタイプを指している。
    */
    public void changeADType(UDF_Policy policy, String path, int adtype) throws UDF_Exception, IOException, IllegalArgumentException;

}

