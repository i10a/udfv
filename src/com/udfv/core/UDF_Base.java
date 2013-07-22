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
package com.udfv.core;

import java.io.*;
import java.util.*;
import com.udfv.util.UDF_Env;

/**
<p>
UDF_Baseは殆ど全ての UDFVクラスの 基底になるクラスである。
</p>
<p>
UDF_Baseは UDF_Envをフィールドとして持ち、
デバッグメッセージを表示するためのメソッドを持つだけのクラスである。
</p>
 */
abstract public class UDF_Base implements Serializable{
    final static int DEFAULT_DEBUG_LEVEL = 3;

    /**
       UDF環境
     */
    public UDF_Env env;
    /**
       UDF環境を設定する

       @param env	UDF環境
     */
    public void setEnv(UDF_Env env){
	this.env = env;
    }
    /**
       UDF環境を取得する

       @return	UDF環境
     */
    public UDF_Env getEnv(){
	return env;
    }
    /**
       デバッグメッセージを出力する。

       @param o 出力対象
       @deprecated replaced by {@link #debugMsg(int, Object)}
     */
    public void debugMsg(Object o){
	debugMsg(DEFAULT_DEBUG_LEVEL, o);
    }
    /**
       デバッグメッセージを出力する。

       @param o 出力値
       @deprecated replaced by {@link #debugMsg(int, int)}
     */
    public void debugMsg(int o){
	debugMsg(DEFAULT_DEBUG_LEVEL, o);
    }
    /**
       デバッグメッセージを出力する。

       @param o 出力値
       @deprecated replaced by {@link #debugMsg(int, long)}
     */
    public void debugMsg(long o){
	debugMsg(DEFAULT_DEBUG_LEVEL, o);
    }

    /**
       env.debug_levelの値が level以上ならばデバッグメッセージを出力する

       @param level デバッグレベル
       @param o 出力値
     */
    public void debugMsg(int level, Object o){
	UDF_Env.debugMsg(level, o);
    }
    /**
       env.debug_levelの値が level以上ならばデバッグメッセージを出力する

       @param level デバッグレベル
       @param o 出力値
     */
    public void debugMsg(int level, int o){
	UDF_Env.debugMsg(level, o);
    }
    /**
       env.debug_levelの値が level以上ならばデバッグメッセージを出力する

       @param level デバッグレベル
       @param o 出力値
     */
    public void debugMsg(int level, long o){
	UDF_Env.debugMsg(level, o);
    }

    /**
       e.debug_levelの値が level以上ならばデバッグメッセージを出力する

       @param e UDF環境
       @param o 出力オブジェクト
     */
    static public void debugMsg(UDF_Env e, Object o){
	e.debugMsg(DEFAULT_DEBUG_LEVEL, o);
    }
    /**
       e.debug_levelの値が level以上ならばデバッグメッセージを出力する

       @param e UDF環境
       @param level デバッグレベル
       @param o 出力オブジェクト
     */
    static public void debugMsg(UDF_Env e, int level, Object o){
	e.debugMsg(level, o);
    }
}
