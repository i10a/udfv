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


package com.udfv.util;

import java.io.*;
import java.util.*;
import com.udfv.exception.*;



public class UDF_ErrorList
{
    /**
       デフォルトコンストラクタ。
       
       特に何もしません。
    */
    public UDF_ErrorList() {}
    
    /**
       エラーをリストに追加する。
       
       @return 正常に終了すると、0を返す。そうでない場合は、負の値を返す。
    */
    public int addError(UDF_Error err)
    {
	if(err.getCategory() == UDF_Error.C_NOERR)
	    return -1;
	
	if(err.getLevel() == UDF_Error.L_NOERR)
	    return -2;
	
	errorList_.add(err);
	return 0;
    }
    
    /**
       エラーリストをリストに追加する。
       
       @return 正常に終了すると、0を返す。そうでない場合は、負の値を返す。
    */
    public void addError(UDF_ErrorList elist)
    {
	for(int i = 0; i < elist.getSize(); i++){
	    
	    UDF_Error err = elist.getError(i);
	    int ret = addError(err);
	    if(ret < 0)
		;  // 内部的なエラーを定義しておいてそれを登録する。かもしれない
	}
    }
    
    /**
       エラーリスト内のエラー全てに対し、エラーの原因となった個所の親レベルの情報を追加する。
       親レベルのエラーとその子供との間はピリオドで連結して表現される。
       
       @param cause  エラーの原因となる位置、個所などを特定するための文字列。
    */
    public void setRName(String cause)
    {
	for(int i = 0; i < getSize(); i++){
	    
	    UDF_Error err = getError(i);
	    err.setRName(cause);
	}
    }
    
    /**
       エラーを取得する。
       
       @param index  エラーのインデックス。
       @return エラークラスを返す。
    */
    public UDF_Error getError(int index)
    {
	return (UDF_Error)errorList_.elementAt(index);
    }
    
    /**
       指定されたエラーカテゴリのリストを取得する。
       指定されたカテゴリのエラーが存在しない場合は、空のエラーリストを返す。
       
       @param category  取得するエラーリストのエラーカテゴリ。
       @return エラーリスト。
    */
    public UDF_ErrorList getErrorInCategory(short category)
    {
	UDF_ErrorList ret = new UDF_ErrorList();
	
	
	for(int i = 0; i < getSize(); i++){
	    
	    UDF_Error err = getError(i);
	    if(err.getCategory() == category)
		ret.addError(err);
	}
	
	return ret;
    }
    
    /**
       指定されたエラーレベルのリストを取得する。
       指定されたレベルのエラーが存在しない場合は、空のエラーリストを返す。
       
       @param level  取得するエラーリストのエラーカテゴリ。
       @return エラーリスト。
    */
    public UDF_ErrorList getErrorInLevel(short level)
    {
	UDF_ErrorList ret = new UDF_ErrorList();
	
	
	for(int i = 0; i < getSize(); i++){
	    
	    UDF_Error err = getError(i);
	    if(err.getLevel() == level)
		ret.addError(err);
	}
	
	return ret;
    }
    
    /**
       エラーの数を取得する。
       
       @return エラーの数を返す。
    */
    public int getSize()
    {
	return errorList_.size();
    }
    
    /**
       リストにエラーが含まれているかを判定する。
       
       @return エラーが含まれている場合、true を返す。そうでない場合は、false を返す。
    */
    public boolean isError()
    {
	for(int i = 0; i < getSize(); i++){
	    
	    UDF_Error err = getError(i);
	    if(err.isError())
		return true;
	}
	
	return false;
    }
    
    /**
       エラーがXML 構築の障害となりうるかどうかを判定する。
       
       @return 安全にXML 化できる場合、true を返す。そうでない場合、false を返す。
    */
    public boolean isXMLSafe()
    {
	for(int i = 0; i < getSize(); i++){
	    
	    UDF_Error err = getError(i);
	    if(!err.isXMLSafe())
		return false;
	}
	
	return true;
    }
    
    /**
       そのレベルより深刻なレベルのエラーが含まれているかどうかを判定する。
       
       @return より深刻なエラーである場合、true を返す。そうでない場合は、false を返す。
               指定したレベルと同じレベルのエラーである場合はfalse を返す。
    */
    public boolean hasMoreErrorLevel(short level)
    {
	for(int i = 0; i < getSize(); i++){
	    
	    UDF_Error err = getError(i);
	    if(err.hasMoreErrorLevel(level))
		return true;
	}
	
	return false;
    }
    
    /**
       エラーを標準出力に出力する。
    */
    public void output()
    {
	int alert = 0;
	int error = 0;
	int warning = 0;
	int caution = 0;

	System.out.println(UDF_Util.getSystemNewLine());
	Collections.sort(errorList_);
	for(int i = 0; i < getSize(); i++){
	    
	    if(getError(i).getLevel() == UDF_Error.L_ERROR)
		error++;
	    else if(getError(i).getLevel() == UDF_Error.L_WARNING)
		warning++;
	    else if(getError(i).getLevel() == UDF_Error.L_CAUTION)
		caution++;
	    else if(getError(i).getLevel() == UDF_Error.L_ALERT)
		alert++;
	    
	    System.out.println(getError(i).toString());
	}

	System.out.println(UDF_Util.toSystemNewLine("\n/// VERIFY COMPLETE ///\n"));
	System.out.println("ALERT:   " + alert);
	System.out.println("ERROR:   " + error);
	System.out.println("WARNING: " + warning);
	System.out.println("CAUTION: " + caution);
	System.out.println(UDF_Util.getSystemNewLine());
    }
    
    /**
       エラーをXML 化して標準出力に出力する。
    */
    public void outputXML()
    {
        UDF_XMLUtil.outputError(this);
    }
    
    /**
       
    */
    public void setGlobalPoint(long gp)
    {
	for(int i = 0; i < getSize(); i++){
	    
	    UDF_Error err = getError(i);
	    err.setGlobalPoint(gp);
	}
    }
    
    public void setRefer(String refer)
    {
	for(int i = 0; i < getSize(); i++){
	    
	    UDF_Error err = getError(i);
	    err.setRefer(refer);
	}
    }
    
    private Vector errorList_ = new Vector();
}


