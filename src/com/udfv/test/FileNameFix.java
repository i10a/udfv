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
   第１引数に指定されたファイルの内容から、第２引数で指定された値を、
   第３引数で指定された文字列へ変換し、標準出力へ出力します。
*/
package com.udfv.test;

import java.io.*;
import java.util.*;

public class FileNameFix extends UDF_TestApp{

    public static void main(String argv[]){
	new FileNameFix(argv);
    }

    FileNameFix(String argv[]){

	try{
	    File file = new File(argv[0]);
	    FileInputStream fis = new FileInputStream(file);
	    byte[] buf = new byte[(int)file.length()];
	    fis.read(buf);
	    String str = new String(buf, "UTF-8");
	    String str2 = str.replaceAll(argv[1], argv[2]);

	    PrintStream  ps = new PrintStream(System.out, true, "UTF-8");
	    System.setOut(ps);
	    System.out.print(str2);
	}
	catch(Exception e){
	    e.printStackTrace();
	}
    }
}






