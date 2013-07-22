#!/bin/sh
#
#   Copyright 2013 Heart Solutions Inc.
#
#   Licensed under the Apache License, Version 2.0 (the "License");
#   you may not use this file except in compliance with the License.
#   You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
#   Unless required by applicable law or agreed to in writing, software
#   distributed under the License is distributed on an "AS IS" BASIS,
#   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#   See the License for the specific language governing permissions and
#   limitations under the License.
#
@java -Xincgc -Xmx128m -classpath ./dist/lib/udfv.jar;./udfv.jar;./lib/xerces.jar;./lib/xercesImpl.jar;./lib/xercesSamples.jar;./lib/xml-apis.jar;./lib/xmlParserAPIs.jar com.udfv.gui.UDFVGui


@REM XMLファイルを読むとき
@rem @java -Xincgc -Xmx128m -classpath ./dist/lib/udfv.jar;./lib/xerces.jar;./lib/xercesImpl.jar;./lib/xercesSamples.jar;./lib/xml-apis.jar;./lib/xmlParserAPIs.jar com.udfv.gui.UDFVGui -x "C:\Documents and Settings\kawa\My Documents\hoge.xml"


@rem ファイルを引数に指定したとき
@rem @java -Xincgc -Xmx128m -classpath ./dist/lib/udfv.jar;./lib/xerces.jar;./lib/xercesImpl.jar;./lib/xercesSamples.jar;./lib/xml-apis.jar;./lib/xmlParserAPIs.jar com.udfv.gui.UDFVGui Z:\export6\verifyimg\DVR7K-VIDEO2.IMG
