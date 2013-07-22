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
package com.udfv.frontend;

/**
   Frontendのプラグインが実装しなければならないインタフェース。

 */
public interface FrontendPlugin{
    public static final int MODE_DEFAULT = 0;
    public static final int MODE_SILENT = 1;
    public static final int CMD_OK = 0;
    public static final int CMD_QUIT = -1;

    /**
       コマンドを実行する。

       Frontendに対し、

       <パッケージ名> <メソッド名> <arg1> <arg2> ... <argN>

       とコマンドを渡すと、プラグイン側の runCmdには最初のパッケージ名が除去され

       <メソッド名> <arg1> <arg2> ... <argN>

       として渡される。

       runCmd()は戻り値として CMD_OKまたは、 CMD_QUITを返さなければならない。
     */
    public int runCmd(String argv[], int mode) throws Exception;
    /**
       プラグインに対して、Frontend(呼出側)を設定する。

       これにより、プラグインは Frontendのフィールド変数を参照したり、メソッドを利用することができる。
     */
    public void setFrontend(Frontend frontend);
}