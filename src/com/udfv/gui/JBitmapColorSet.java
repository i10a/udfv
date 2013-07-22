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
/*

*/
package com.udfv.gui;

/**
  ビットマップを描画したパネルの描画に使用する色集合のためのクラス。
*/
public class JBitmapColorSet {

    public static final int COLOR_MAX = 10;

    /** 使用されていない領域を示すID */
    public static final int FREE     = 0;
    /** VRS, AVDP, VDS などの UDF のボリューム情報を表現するための Descriptor が使用する領域を示すID */
    public static final int VOL      = 1;
    /** ０番の Partition Map が参照する領域を示すID */
    public static final int PART_0   = 3;
    /** １番の Partition Map が参照する領域を示すID */
    public static final int PART_1   = 4;
    /** ２番の Partition Map が参照する領域を示すID */
    public static final int PART_2   = 5;
    /** ３番の Partition Map が参照する領域を示すID */
    public static final int PART_3   = 6;
    /** 拡張用のIDその１ */
    public static final int USER_0   = 8;
    /** 拡張用のIDその２ */
    public static final int USER_1   = 9;

    private java.awt.Color [] cols;

    /**
      コンストラクタ。

      色集合は以下のように初期化される。

      <table>
      <tr><td>FREE</td>  <td>java.awt.Color.gray</td></tr>
      <tr><td>VOL</td>   <td>java.awt.Color.green</td></tr>
      <tr><td>PART_0</td><td>java.awt.Color.blue</td></tr>
      <tr><td>PART_1</td><td>java.awt.Color.orange</td></tr>
      <tr><td>PART_2</td><td>java.awt.Color.pink</td></tr>
      <tr><td>PART_3</td><td>java.awt.Color.yellow</td></tr>
      <tr><td>USER_0</td><td>java.awt.Color.white</td></tr>
      <tr><td>USER_1</td><td>java.awt.Color.white</td></tr>
      </table>
    */
    public JBitmapColorSet( ) {
        cols = new java.awt.Color[COLOR_MAX];

        cols[0] = java.awt.Color.gray;
        cols[1] = java.awt.Color.green;
        cols[2] = java.awt.Color.red;
        cols[3] = java.awt.Color.blue;
        cols[4] = java.awt.Color.orange;
        cols[5] = java.awt.Color.pink;
        cols[6] = java.awt.Color.yellow;
        cols[7] = java.awt.Color.cyan;
        cols[8] = java.awt.Color.white;
        cols[9] = java.awt.Color.white;
    }

    /**
      ビット配色を設定する。

      @param id ビット配色番号
      @param col 指定のColor
    */
    public void setColor(int id, java.awt.Color col) {
        this.cols[id] = new java.awt.Color(col.getRGB());
    }

    /**
      ビット配色を設定する。

      @param col 指定のColor配列
    */
    public void setColor(java.awt.Color [] cols) throws java.lang.ArrayIndexOutOfBoundsException {

        if (cols.length > COLOR_MAX) {
            throw new java.lang.ArrayIndexOutOfBoundsException("JSpaceBitmaPanel color index overflow");
        }
        this.cols = cloneColor(cols);
    }

    /**
      ビット配色を取得する。

      割り当てられる色のクローンを返す。

      @param id ビット配色番号
      @return 指定のColor
    */
    public java.awt.Color getColor(int id) {
        return new java.awt.Color(this.cols[id].getRGB());
    }

    /**
      ビット配色を取得する。

      割り当てられる色のクローンを返す。

      @return Colorの配列
    */
    public java.awt.Color [] getColor() {
        return cloneColor(this.cols);
    }

    /**
      java.awt.Colorの配列を複製する。
    */
    public static java.awt.Color [] cloneColor(java.awt.Color [] cols) {
        java.awt.Color [] clone = new java.awt.Color[COLOR_MAX];
        for (int i = 0, max = cols.length; i < max; i++) {
            clone[i] = new java.awt.Color(cols[i].getRGB());
        }
        return clone;
    }

}

