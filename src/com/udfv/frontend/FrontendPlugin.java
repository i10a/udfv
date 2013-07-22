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