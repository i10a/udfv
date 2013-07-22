/*
  $Id: $
 */
package com.udfv.util;

import java.io.*;
import java.util.*;
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
import org.apache.xml.serialize.*;
import com.udfv.core.UDF_Image;

/**
  Document の実装環境に依存する部分をまとめたクラス。

*/
public class UDF_XMLUtil {

    public static final boolean MARIMITE = true;
    
    public static final byte SAYA_DEFAULT = 0;
    public static final byte SAYA_UME = 1;
    
    public static final byte SAYA = SAYA_DEFAULT;//UME;
    
    
    private UDF_XMLUtil( ) {
    }

    /**
       XMLの Document Nodeを生成する。

       @return XML Document Node。現在は org.apache.Xerces.dom.DocumentImplで実装している。
     */
    public static Document genDocument(){
        return new DocumentImpl();
    }

    /**
       XMLにDTD情報を加える。

       @param doc XMLの Document Node。
    */
    public static void addDocumentType(Document doc, String udf, String dtd2, String dtd) {
        DocumentType doc_type = ((DocumentImpl)doc).createDocumentType("udf", UDF_XML.UDF_DTD2, UDF_XML.UDF_DTD);
        doc.appendChild(doc_type);
    }

    /**
       XMLを標準出力する。

       @param my_doc XMLの Document Node 。
       @param sortflag ソートするか否か。
    */
    public static void output(Document my_doc, boolean sortflag, UDF_Env env) {
        try{
            if(sortflag)
		com.udfv.util.UDF_XML.sortDocumentbyEnv(my_doc, env);

            OutputFormat format = new OutputFormat(my_doc, "UTF-8", true);
            format.setLineWidth(0);
            OutputStreamWriter osw = new OutputStreamWriter(System.out, "UTF-8");
            XMLSerializer serial = new XMLSerializer(osw, format);
            serial.serialize(my_doc.getDocumentElement());
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    /**
       XMLをファイルに出力する。

       @param my_doc XMLの Document Node 。
       @param os 出力先のストリーム。
       @param sortflag ソートするか否か。
    */
    public static void output(Document my_doc, OutputStream os, boolean sortflag, UDF_Env env) {
        if(sortflag){
            try{
		com.udfv.util.UDF_XML.sortDocumentbyEnv(my_doc, env);
            }
            catch(Exception e1){
                e1.printStackTrace();
            }
        }

        try {
            OutputFormat format = new OutputFormat(my_doc, "UTF-8", true);
            format.setLineWidth(0);
	    //            Writer out = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            Writer out = new OutputStreamWriter(os, "UTF-8");
            XMLSerializer serial = new XMLSerializer(out, format);
            serial.serialize(my_doc.getDocumentElement());
            out.close();
        }
        catch(Exception e2){
            e2.printStackTrace();
        }
    }

    /**
      エラーリストを XMLとして出力する。

      @param el エラーリスト
    */
    public static void outputError(UDF_ErrorList el) {

        Document doc = UDF_Util.genDocument();
        Element root = doc.createElement("udf-error");

        for(int i = 0; i < el.getSize(); i++){
            el.getError(i).toXML(doc, root);
        }

        doc.appendChild(root);

        try{
            OutputFormat format = new OutputFormat(doc, "UTF-8", true);
            format.setLineWidth(0);
            OutputStreamWriter osw = new OutputStreamWriter(System.out, "UTF-8");
            XMLSerializer serial = new XMLSerializer(osw, format);
            serial.serialize(doc.getDocumentElement());
       }
       catch(Exception e){
           e.printStackTrace();
       }
    }
}
