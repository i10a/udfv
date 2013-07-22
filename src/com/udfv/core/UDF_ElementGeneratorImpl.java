/*
 */
package com.udfv.core;

import java.io.*;
import java.util.*;
import java.lang.reflect.*;
import com.udfv.ecma167.UDF_uint8;
import com.udfv.ecma167.UDF_uint16;
import com.udfv.ecma167.UDF_uint32;
import com.udfv.ecma167.UDF_uint32a;
import com.udfv.ecma167.UDF_uint64;
import com.udfv.ecma167.UDF_short_ad;
import com.udfv.ecma167.UDF_lb_addr;
import com.udfv.ecma167.UDF_bytes;
import com.udfv.ecma167.UDF_pad;

/*
 */
public class UDF_ElementGeneratorImpl implements UDF_ElementGenerator{
    int dlvl;
    /**
       ２回目以降高速にクラスを作るために使用するhashtable
     */
    Hashtable classhash;

    public UDF_ElementGeneratorImpl(int debug_lvl){
	dlvl = debug_lvl;
	pkg_list = new String[]{PKG_UDFV, PKG_UDF250, PKG_UDF200, PKG_UDF150, PKG_UDF102, PKG_ECMA167, PKG_ECMA119};
	classhash = new Hashtable();

	//System.err.println("GENERATOR\n");
    }

    static final String PKG_UDFV    = "com.udfv.core.";
    static final String PKG_ECMA119 = "com.udfv.ecma119.";
    static final String PKG_ECMA167 = "com.udfv.ecma167.";    
    static final String PKG_UDF102  = "com.udfv.udf102.";
    static final String PKG_UDF150  = "com.udfv.udf150.";
    static final String PKG_UDF200  = "com.udfv.udf200.";
    static final String PKG_UDF201  = "com.udfv.udf201.";
    static final String PKG_UDF250  = "com.udfv.udf250.";
    static final String PKG_UDF260  = "com.udfv.udf260.";

    String[] pkg_list;

    public void setPkgPriority(int revision){
	classhash.clear();
	switch(revision){
	case 0x102:
	    if(dlvl >= 1)
		System.err.println("udf102:setPkgPriority");
	    pkg_list = new String[]{
		PKG_UDFV,
		PKG_UDF102,
		PKG_ECMA167,
		PKG_ECMA119
	    };
	    break;
	case 0x150:
	    if(dlvl >= 1)
		System.err.println("udf150:setPkgPriority");
	    pkg_list = new String[]{
		PKG_UDFV,
		PKG_UDF150,
		PKG_UDF102,
		PKG_ECMA167,
		PKG_ECMA119
	    };
	    break;
	case 0x200:
	    if(dlvl >= 1)
		System.err.println("udf200:setPkgPriority");
	    pkg_list = new String[]{
		PKG_UDFV,
		PKG_UDF200,
		PKG_UDF150,
		PKG_UDF102,
		PKG_ECMA167,
		PKG_ECMA119
	    };
	    break;
	case 0x201:
	    if(dlvl >= 1)
		System.err.println("udf201:setPkgPriority");
	    pkg_list = new String[]{
		PKG_UDFV,
		PKG_UDF201,
		PKG_UDF200,
		PKG_UDF150,
		PKG_UDF102,
		PKG_ECMA167,
		PKG_ECMA119
	    };
	    break;
	case 0x250:
	    if(dlvl >= 1)
		System.err.println("udf250:setPkgPriority");
	    pkg_list = new String[]{
		PKG_UDFV,
		PKG_UDF250,
		PKG_UDF201,
		PKG_UDF200,
		PKG_UDF150,
		PKG_UDF102,
		PKG_ECMA167,
		PKG_ECMA119
	    };
	    break;
	case 0x260:
	    if(dlvl >= 1)
		System.err.println("udf260:setPkgPriority");
	    pkg_list = new String[]{
		PKG_UDFV,
		PKG_UDF260,
		PKG_UDF250,
		PKG_UDF201,
		PKG_UDF200,
		PKG_UDF150,
		PKG_UDF102,
		PKG_ECMA167,
		PKG_ECMA119
	    };
	    break;
	}
    }

    /**
       現在の UDFのリビジョンレベルに合った適切なパッケージを持つ UDF_Elementを生成する。

       Hashtableを使用し、一度生成した UDF_Elementは高速に生成する。
       
       @param className	クラス名
       @param mother	親
       @param prefix	ネームスペース
       @param tagName	XML Node名
     */
    public UDF_Element genElement(String className, UDF_Element mother, String prefix, String tagName) throws ClassNotFoundException{
	UDF_Element elem = null;
	/*
	  よく使用されるこれらの UDF Elementは高速に生成する
	*/
	if(className.equals("UDF_uint8"))
	    elem = new UDF_uint8(mother, prefix, tagName);
	else if(className.equals("UDF_uint16"))
	    elem = new UDF_uint16(mother, prefix, tagName);
	else if(className.equals("UDF_uint32"))
	    elem = new UDF_uint32(mother, prefix, tagName);
	else if(className.equals("UDF_uint32a"))
	    elem = new UDF_uint32a(mother, prefix, tagName);
	else if(className.equals("UDF_uint64"))
	    elem = new UDF_uint64(mother, prefix, tagName);
	else if(className.equals("UDF_bytes"))
	    elem = new UDF_bytes(mother, prefix, tagName);
	else if(className.equals("UDF_short_ad"))
	    elem = new UDF_short_ad(mother, prefix, tagName);
	else if(className.equals("UDF_lb_addr"))
	    elem = new UDF_lb_addr(mother, prefix, tagName);
	else if(className.equals("UDF_pad"))
	    elem = new UDF_pad(mother, prefix, tagName);
	else if(className.equals("UDF_Data"))
	    elem = new UDF_Data(mother, prefix, tagName);
	else if(className.equals("UDF_Extent"))
	    elem = new UDF_Extent(mother, prefix, tagName);
	if(elem != null){
	    elem.setEnv(mother.env);
	    return elem;
	}

	String cls;
	//packageの優先順位
	String pkg[] = pkg_list;
	Class c = null;

	Object o;
	if((o = classhash.get(className)) != null){
	    c = (Class)o;
	}
	if(c == null && className.indexOf("com.udfv.") == 0){
	    try{
		c = Class.forName(className);
	    }
	    catch(ClassNotFoundException e){
		;
	    }
	    //
	    //Classhashに登録し、次回から高速に生成
	    //
	    if(c != null)
		classhash.put(className, c);
	}
	
	if(c == null){

	    for(int i=0 ; i<pkg.length ; ++i){
		try{
		    cls = pkg[i] + className;
		    c = Class.forName(cls);
		    break;
		}
		catch(ClassNotFoundException e){
		    ;//next;
		}
	    }
	    if(c == null){
		System.err.println("NOT FOUND: "+ className);
		throw new ClassNotFoundException();
	    }
	    //
	    //Classhashに登録し、次回から高速に生成
	    //
	    if(c != null)
		classhash.put(className, c);
	}

	try{
	    Object []oarg = new Object[]{mother, prefix, tagName};
	    Constructor cst = c.getConstructor(new Class[]{
						   com.udfv.core.UDF_Element.class,
						   String.class,
						   String.class});
	    UDF_Element new_elem = (UDF_Element)cst.newInstance(oarg);
	    new_elem.setEnv(mother.env);
	    return new_elem;
	}
	catch(NoSuchMethodException e){
	    //ここにはこないはず
	    mother.debugMsg(1, "NoSuchMethodException");
	    e.printStackTrace();
	    throw new ClassNotFoundException();
	}
	catch(InstantiationException e){
	    //ここにはこないはず
	    mother.debugMsg(1, "InstantitationException");
	    e.printStackTrace();
	    throw new ClassNotFoundException();
	}
	catch(IllegalAccessException e){
	    //ここにはこないはず
	    mother.debugMsg(1, "IllegalAccessException");
	    e.printStackTrace();
	    throw new ClassNotFoundException();
	}
	catch(InvocationTargetException e){
	    //コンストラクタでExceptionが投げられた?
	    mother.debugMsg(1, "InvocationTargetException");
	    e.getCause().printStackTrace();
	    e.printStackTrace();
	    throw new ClassNotFoundException();
	}

    }
}
