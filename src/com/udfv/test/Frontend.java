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
package com.udfv.test;

import java.io.*;
import java.util.*;
import java.util.zip.*;
import org.w3c.dom.*;
import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.access.*;
import com.udfv.udfapi.*;
import com.udfv.ecma167.*;
import org.apache.xml.serialize.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
   Frontend [-in <input image file>] [-out <output image file>] [-inxml <input XML file] [-outxml <output XML file>]


 */
public class Frontend extends UDF_TestApp{
    public static void main(String argv[]){
	new com.udfv.frontend.Frontend(argv);
    }
}
