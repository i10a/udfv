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
import java.util.zip.*;
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
//import org.apache.xml.serialize.*;
import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.access.*;
import com.udfv.exception.*;
import com.udfv.ecma167.*;

/**
   全てのUDFのバージョンの UDF_Imageを作ることができるクラス。

<dl><dt>概要</dt>
<dd>
UDF_ImageFactoryは UDF_Imageを作るためのファクトリクラスである。
バージョンを指定して作成する他、イメージのボリュームを読みとって、自動で生成することもできる。
</dd>
</dl>
 */

public class UDF_ImageFactory// extends com.udfv.udf260.UDF_Image
{
    /*
    private UDF_ImageFactory(){
	super();
    }

    private UDF_ImageFactory(Document doc){
	super(doc);
    }
    */
    
    /**
       リビジョンを指定して UDF_Imageを生成する

       @param revision	UDFのリビジョン

       @return UDF_Image。リビジョンが不明のときは nullを返す。

     */
    public static UDF_Image genImage(UDF_Env e, int revision){
	UDF_Image img = null;

	UDF_Base.debugMsg(e, "genImage:" + revision);
	switch(revision){
	case 0x102:
	    img = new com.udfv.udf102.UDF_Image();
	    break;
	    
	case 0x150:
	    img = new com.udfv.udf150.UDF_Image();
	    break;
    
	case 0x200:
	    img = new com.udfv.udf200.UDF_Image();
	    break;
	    
	case 0x201:
	    img = new com.udfv.udf201.UDF_Image();
	    break;
	    
	case 0x250:
	    img =  new com.udfv.udf250.UDF_Image();
	    break;
	    
	case 0x260:
	    img =  new com.udfv.udf260.UDF_Image();
	    break;
	    
	default:
	    break;
	}
	if(img != null)
	    img.setEnv(e);

	return img;
    }
    /**
       イメージのボリューム情報をを読みとり、イメージに沿ったリビジョンのUDF_Imageを生成する

       @param f アクセサ

       @return UDF_Image
     */
    public static UDF_Image genImage(UDF_Env e, UDF_RandomAccess f) throws IOException, UDF_Exception, ClassNotFoundException{ 
	//考えられうる最大のリビジョンで仮生成
	com.udfv.udf260.UDF_Image u = new com.udfv.udf260.UDF_Image();

	//仮のenv(UDF_Baseのenv)で実行
	//env.fだけ設定する必要がある。
	UDF_Env tmp_env = new UDF_Env();
	tmp_env.f = f;
	u.setEnv(tmp_env);
	//↓いれるかどうかは微妙。いれないと UDFRevisionをチェックするときに
	//生成されるノードの名前が全部 null。たまたまそのおかげで上手くうごいていると怖い……
	u.setUDFDocument(UDF_Util.genDocument());

	int revision = u.checkUDFRevision(f);
	UDF_Image img = genImage(e, revision);
	
	/**
	   ここで確実に UDF_Envを初期化する。
	 */
	if(img != null){
	    img.setEnv(e);
	}
	else
	    throw new UDF_DataException(null, "Unknwon revision :" + revision);

	return img;
    }
}
