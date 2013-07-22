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

public interface UDF_VolumeAPI {
    /**
      ボリュームのoffset位置からsizeバイトbを書き込む。

      @param b 書き込みたい数値。
      @param offset 書き込み始める位置。
      @param size 書き込む回数（バイト数）。
      @throws UDF_IOException 何らかの理由で書き込みに失敗した。
      
    */
    public void writeByte(byte b, long offset, long size) throws UDF_Exception;

    /**
      ボリュームサイズを変更する。

      @param size 希望するボリュームサイズ（単位：バイト）。
      @throws UDF_IrregularArgumentException 値が異常。
    */
    public void resizeVolume(long size) throws UDF_Exception;

    /**
      ボリュームサイズを取得する。

      @return ボリュームサイズ（単位：セクタ）。
    */
    public int getVolumeSize();

    /**
      ボリュームを書き出す。

      @throws UDF_IOException 何らかの理由で書き出しに失敗した。
      @throws UDF_Exception ボリュームが構築されていない。
    */
    public void writeVolume() throws UDF_Exception;

    /**
      UDF 環境変数に合わせて空のファイルシステムを作成する。

      ルートディレクトリのみのファイルシステムを構築する。

      @throws UDF_IrregularArgumentException UDF 環境に何らかの異常値が存在する。
    */
    public void createVolume() throws UDF_Exception;

    /**
      ボリュームからファイルシステムを読み込む。

      （内部でUDF_Env を生成します）

      @throws UDF_IOException 何らかの理由で読み込みに失敗した。
      @throws UDF_DescTagException 読み込みの過程で descriptor のエラーが検出された。
    */
    /* == UDF_CppInterface#udfReadVolume() */
    public void readVolume( ) throws UDF_Exception;

    /**
      UDF イメージを取得する。

      @throws UDF_Exception ボリュームが生成／読み込みされていない。
    */
    public UDF_Image getImage( ) throws UDF_Exception;

    /**
      タイムスタンプを特定の値に固定する。

      タイムスタンプを取得するとき、必ず指定の値になるようにする。

      デバッグ用途。

      @param time Epochからのミリ秒。
    */
    public void freezeTime(long time);

    /**
      UDFVライブラリのシステムバージョンを指定の値に書き換える。

      デバッグ用途。

      @param system_version バージョン番号。
    */
    public void setVersion(int system_version);
}

