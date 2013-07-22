@java -Xincgc -Xmx128m -classpath ./dist/lib/udfv.jar;./udfv.jar;./lib/xerces.jar;./lib/xercesImpl.jar;./lib/xercesSamples.jar;./lib/xml-apis.jar;./lib/xmlParserAPIs.jar com.udfv.gui.UDFVGui


@REM XMLファイルを読むとき
@rem @java -Xincgc -Xmx128m -classpath ./dist/lib/udfv.jar;./lib/xerces.jar;./lib/xercesImpl.jar;./lib/xercesSamples.jar;./lib/xml-apis.jar;./lib/xmlParserAPIs.jar com.udfv.gui.UDFVGui -x "C:\Documents and Settings\kawa\My Documents\hoge.xml"


@rem ファイルを引数に指定したとき
@rem @java -Xincgc -Xmx128m -classpath ./dist/lib/udfv.jar;./lib/xerces.jar;./lib/xercesImpl.jar;./lib/xercesSamples.jar;./lib/xml-apis.jar;./lib/xmlParserAPIs.jar com.udfv.gui.UDFVGui Z:\export6\verifyimg\DVR7K-VIDEO2.IMG
