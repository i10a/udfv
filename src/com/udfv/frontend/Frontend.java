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
package com.udfv.frontend;

import java.io.*;
import java.util.*;
import java.util.zip.*;
import java.lang.reflect.*;
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

   1行に1コマンドを書く。

   #で始まる行はコメント。

   クラス名をパッケージから続けて記述することでプラグインを作ることができる。

   プラグインは FrontendPluginをインタフェースとして実装しなくてはならない。
 */
public class Frontend extends com.udfv.test.UDF_TestApp{
    UDF_Image image = null;
    DOMParser parser;
    Document document;
    InputStream isr;

    UDF_Policy policy;
    UDF_API api;

    public int data_partno = 0;
    public int desc_partno = 0;
    public int data_adtype = 0;
    public int desc_adtype = 0;

    String input_file;
    String output_file;
    String input_xml;
    String output_xml;

    UDF_ADList my_ad;
    Eval eval;

    public Frontend(String argv[]) {
	eval = new Eval(this);
	argv = env.parseOpt(argv);
	//readImageで書きかえられるが、一応生成しておく
	policy = new UDF_PolicyImpl(0, 0);
	my_ad = new UDF_ADList();
	UDF_Env.freeze_time = true;
	for(int i=0 ; i<argv.length ; ++i){
	    if(argv[i].equals("-in"))
		input_file = argv[++i];
	    else if(argv[i].equals("-out"))
		output_file = argv[++i];
	    else if(argv[i].equals("-inxml"))
		input_xml = argv[++i];
	    else if(argv[i].equals("-outxml"))
		output_xml = argv[++i];
	}

	try{
	    isr = System.in;//new InputStream(System.in);
	    
	    while(true){
		String l = readline();
		if(l == null)
		    break;
		String cmd[] = split(l);
		try{
		    if(runCmd(cmd, MODE_DEFAULT) == CMD_QUIT)
			break;
		}
		catch(OutOfMemoryError e){
		    e.printStackTrace();
		}
		catch(Exception e){
		    e.printStackTrace();
		}
	    }
	}
	catch(Exception e){
	    e.printStackTrace();
	}
    }
    /**
       APIを取得する

       @return API
     */
    public UDF_API getAPI(){
	return api;
    }
    /**
       Policyを取得する

       @return Policy
     */
    public UDF_Policy getPolicy(){
	return policy;
    }

    /**
       @return Image
     */
    public UDF_Image getImage(){
	return image;
    }

    String[] split(String s){
	s = s.trim();
	Vector v = new Vector();

	return s.split("\\s+");
    }

    long evalConst(String s) throws NoSuchFieldException {
	try{
	    java.lang.reflect.Field f = UDF_ElementBase.class.getField(s);
	    return f.getInt(null);
	}
	catch(Exception e){
	    throw new NoSuchFieldException();
	}
    }

    public int evalInt(String s) throws NumberFormatException{
	try{
	    return (int)eval.eval(s);
	}
	catch(Exception e){
	    throw new NumberFormatException(s);
	}
    }
    public long evalLong(String s) throws NumberFormatException{
	try{
	    return eval.eval(s);
	}
	catch(Exception e){
	    throw new NumberFormatException(s);
	}
    }

    String readline(){
	try{
	    int c;
	    ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    
	    while(true){
		c = isr.read();
		if(c <= 0)
		    return null;
		if(c == '\n')
		    break;
		bos.write((byte)c);
	    }

	    return new String(bos.toByteArray(), "UTF-8");
	}
	catch(IOException e){
	    return "";
	}
    }

    static final int CMD_OK = 0;
    static final int CMD_QUIT = -1;
    static final int MODE_DEFAULT = 0;
    static final int MODE_SILENT = 1;

    StringBuffer _help = new StringBuffer();
    StringBuffer _desc = new StringBuffer();

    //コマンドハンドラ

    public int cmdHELP(String argv[], int mode){
	if(argv == null){
	    _help.append("Help");
	    _desc.append("Show this message");
	    return CMD_OK;
	}
	//printArgs(argv);
	_help = new StringBuffer();
	try{
	    java.lang.reflect.Method[] methods = getClass().getMethods();
	    for(int i=0 ; i<methods.length ; ++i){
		if(methods[i].getName().indexOf("cmd") == 0){
		    _desc = new StringBuffer();
		    try{
			Object []o = new Object[]{null, new Integer(0)};
			methods[i].invoke(this, o);
			_help.append("\n\t");
			_help.append(_desc);
			_help.append("\n");
		    }
		    catch(Exception e){
			e.printStackTrace();
		    }
		}
	    }
	    System.err.println(_help.toString());
	}
	catch(Exception e){
	    ;
	}
	return CMD_OK;
    }
    public int cmdSETDEBUGLEVEL(String argv[], int mode) throws Exception{
	if(argv == null){
	    _help.append("setDebugLevel <debug_level>");
	    _desc.append("set debug level.");
	    return CMD_OK;
	}
	printArgs(argv);
	int level = evalInt(argv[1]);

	env.debug_level = level;
	return CMD_OK;
    }
    public int cmdLOOP(String argv[], int mode) throws Exception{
	if(argv == null){
	    _help.append("Loop");
	    _desc.append("Loop");
	    return CMD_OK;
	}
	printArgs(argv);
	int loop = evalInt(argv[1]);
	String[] new_argv = new String[argv.length - 2];
	for(int i=0 ; i<new_argv.length ; ++i){
	    new_argv[i] = argv[i+2];
	}
	while(loop > 0){
	    if(loop % 100 == 0)
		System.err.println(loop);
	    runCmd(new_argv, MODE_SILENT);
	    --loop;
	}
	return CMD_OK;
    }
    public int cmdCREATEPOLICY(String argv[], int mode) throws Exception{
	if(argv == null){
	    _help.append("CreatePolicy [<class>]");
	    _desc.append("Create default policy");
	    return CMD_OK;
	}

	printArgs(argv);

	policy = new UDF_PolicyImpl(0, 1);
	data_partno = 0;
	desc_partno = 1;
	data_adtype = 1;
	desc_adtype = 0;

	return CMD_OK;
    }
    public int cmdSETVOLUMEPOLICY(String argv[], int mode) throws Exception{
	if(argv == null){
	    _help.append("SetVolumePolicy <class>");
	    _desc.append("Create Volume Policy instance and set it current policy");
	    return CMD_OK;
	}

	Class c = Class.forName(argv[1]);
	Constructor cst = c.getConstructor(new Class[]{});
	UDF_VolPolicy volp = (UDF_VolPolicy)cst.newInstance(new Object[]{});
	policy.setVolPolicy(volp);
	
	return CMD_OK;
    }
    public int cmdCREATEVOLUME(String argv[], int mode) throws Exception{
	if(argv == null){
	    _help.append("CreateVolume");
	    _desc.append("Create Volume");
	    return CMD_OK;
	}
	printArgs(argv);

	policy.getVolPolicy().createVolume(image, policy);

	return CMD_OK;
    }
    public int cmdDISPLAYTREE(String argv[], int mode) throws Exception{
	if(argv == null){
	    _help.append("DisplayTree");
	    _desc.append("DisplayTree");
	    return CMD_OK;
	}
	printArgs(argv);
	displayTree(System.out, image.env.getRootFE(0), F_ALL);
	String nl = UDF_Util.getSystemNewLine();
	if(image.env.getSRootFE(0) != null){
	    System.out.print(   nl
				+ "System Stream Tree:" + nl
				);
	    
	    displayTree(System.out, image.env.getSRootFE(0), F_ALL);
	}

	return CMD_OK;
    }
    public int cmdVERIFY(String argv[], int mode) throws Exception{
	if(argv == null){
	    _help.append("Verify");
	    _desc.append("Verify");
	    return CMD_OK;
	}
	printArgs(argv);
	UDF_ErrorList el = image.verify();
	el.output();
	return CMD_OK;
    }
    public int cmdMKDIR(String argv[], int mode) throws Exception{
	if(argv == null){
	    _help.append("mkdir <path> <ad type>");
	    _desc.append("Create directory");
	    return CMD_OK;
	}
	printArgs(argv);
	api.mkdir(policy, argv[1], evalInt(argv[2]));
	return CMD_OK;
    }
    public int cmdMKFILE(String argv[], int mode) throws Exception{
	if(argv == null){
	    _help.append("mkfile <path> [<ad type>]");
	    _desc.append("Create File");
	    return CMD_OK;
	}

	printArgs(argv);
	if(argv.length >= 3)
	    api.mkfile(policy, argv[1], evalInt(argv[2]));
	else{
	    api.mkfile(policy, argv[1], data_adtype);
	}
	return CMD_OK;
    }
    public int cmdRESIZE(String argv[], int mode) throws Exception{
	if(argv == null){
	    _help.append("resize <path> <new size>");
	    _desc.append("Resize file or directory");
	    return CMD_OK;
	}
	    
	printArgs(argv);
	api.resize(policy, argv[1], evalLong(argv[2]));
	return CMD_OK;
    }
    public int cmdRESIZEPARTMAP(String argv[], int mode) throws Exception{
	if(argv == null){
	    _help.append("resizePartMap <partmapno> <new size>");
	    _desc.append("Resize Partition Map");
	    return CMD_OK;
	}
	    
	printArgs(argv);
	api.resizePartMap(policy, evalInt(argv[1]), evalLong(argv[2]));
	return CMD_OK;
    }

    public int cmdRECALC(String argv[], int mode) throws Exception{
	if(argv == null){
	    _help.append("recalc [<type>] [<type>] ...");
	    _desc.append("call recalc function. If type is not specified, RECALC_GP, RECALC_REF, RECALC_SB, RECALC_PAD, RECALC_TAGLOC, RECALC_LVIS, RECALC_CRC is called");
	    return CMD_OK;
	}
	printArgs(argv);

	if(argv.length <= 1){
	    image.recalc(UDF_ElementBase.RECALC_GP, env.f);
	    image.recalc(UDF_ElementBase.RECALC_REF, env.f);
	    image.recalc(UDF_ElementBase.RECALC_SB, env.f);
	    image.recalc(UDF_ElementBase.RECALC_PAD, env.f);
	    image.recalc(UDF_ElementBase.RECALC_TAGLOC, env.f);
	    image.recalc(UDF_ElementBase.RECALC_LVIS, env.f);
	    image.recalc(UDF_ElementBase.RECALC_CRC, env.f);
	}
	else{
	    for(int i=1 ; i < argv.length ; ++i){
		short typ = (short)evalInt(argv[i]);
		image.recalc(typ, env.f);
	    }
	}

	return CMD_OK;
    }
    public int cmdREADIMAGE(String argv[], int mode) throws Exception{
	if(argv == null){
	    _help.append("readimage [<input file>]");
	    _desc.append("Read image file. If input file is not specified, use default input file which is specified by '-in' option.");
	    return CMD_OK;
	}
	printArgs(argv);

	if(argv.length <= 1){
	    env.image_file = input_file;
	    env.f = new UDF_RandomAccessFile(input_file);
	}
	else{
	    env.image_file = argv[1];
	    env.f = new UDF_RandomAccessFile(argv[1]);
	}
	image = UDF_ImageFactory.genImage(env, env.f);
	image.setUDFDocument(UDF_XMLUtil.genDocument());
	image.readFrom(env.f);
	if(!image.env.hasMetadataPartMap()){
	    policy = new UDF_PolicyImpl(0, 0);
	    data_partno = 0;
	    desc_partno = 0;
	    data_adtype = 0;
	    desc_adtype = 0;
	}
	else{
	    policy = new UDF_PolicyImpl(0, 1);
	    data_partno = 0;
	    desc_partno = 1;
	    data_adtype = 1;
	    desc_adtype = 0;
	}
	api = new UDF_API(image);

	return CMD_OK;
    }
    public int cmdREADXML(String argv[], int mode) throws Exception{
	if(argv == null){
	    _help.append("readXML [<input XML file>]");
	    _desc.append("Read XML file. If input file is not specified, use default input file which is specified by '-inxml' option.");
	    return CMD_OK;
	}
	printArgs(argv);

	parser = new DOMParser();
	if(argv.length <= 1){
	    parser.parse(input_xml);
	}
	else{
	    parser.parse(argv[1]);
	}
	
	Document input_doc = parser.getDocument();
	image = UDF_ImageFactory.genImage(env, 0x260);
	env.f = new UDF_RandomAccessZero(env.image_size);
	
	image.setEnv(env);
	image.readFromXML(input_doc);
	
	image.recalc(UDF_Element.RECALC_GP, env.f);
	image.recalc(UDF_Element.RECALC_ENV, env.f);
	image.recalc(UDF_Element.RECALC_TREE, env.f);
	image.recalc(UDF_Element.RECALC_ADLIST, env.f);
	
	if(!image.env.hasMetadataPartMap()){
	    policy = new UDF_PolicyImpl(0, 0);
	    data_partno = 0;
	    desc_partno = 0;
	    data_adtype = 0;
	    desc_adtype = 0;
	}
	else{
	    policy = new UDF_PolicyImpl(0, 1);
	    data_partno = 0;
	    desc_partno = 1;
	    data_adtype = 1;
	    desc_adtype = 0;
	}
	api = new UDF_API(image);
	return CMD_OK;
    }

    public int cmdREADFILE(String argv[], int mode) throws Exception{
	if(argv == null){
	    _help.append("readFile <file> <output file>");
	    _desc.append("read specified UDF file and store it to output file.");
	    return CMD_OK;
	}
	printArgs(argv);

	api.readFileFile(policy, argv[1], 0, 0, argv[2], 0);

	return CMD_OK;
    }
    public int cmdQUIT(String argv[], int mode) throws Exception{
	if(argv == null){
	    _help.append("Quit");
	    _desc.append("Quit Frontend.");
	    return CMD_OK;
	}
	printArgs(argv);

	return CMD_QUIT;
    }
    public int cmdCHANGEADTYPE(String argv[], int mode) throws Exception{
	if(argv == null){
	    _help.append("ChangeADType <file> <type>");
	    _desc.append("change allocation descriptor type.");
	    return CMD_OK;
	}
	printArgs(argv);

	api.changeADType(policy, argv[1], Integer.parseInt(argv[2]));

	return CMD_OK;
    }
    public int cmdREALLOC(String argv[], int mode) throws Exception{
	if(argv == null){
	    _help.append("Realloc <file>");
	    _desc.append("re-allocation file or directory and make it continuous data");
	    return CMD_OK;
	}
	printArgs(argv);

	api.realloc(policy, argv[1]);

	return CMD_OK;
    }
    public int cmdCHAINALLOCDESC(String argv[], int mode) throws Exception{
	if(argv == null){
	    _help.append("ChainAllocDesc <file>");
	    _desc.append("Chain allocation descriptor");
	    return CMD_OK;
	}

	printArgs(argv, mode);
	api.chainAllocDesc(policy, argv[1]);
	return CMD_OK;
    }
    public int cmdUNCHAINALLOCDESC(String argv[], int mode) throws Exception{
	if(argv == null){
	    _help.append("UnchainAllocDesc <file>");
	    _desc.append("Unchain allocation descriptor");
	    return CMD_OK;
	}

	printArgs(argv);
	api.unchainAllocDesc(policy, argv[1]);
	return CMD_OK;
    }
    public int cmdLINK(String argv[], int mode) throws Exception{
	if(argv[0].equalsIgnoreCase("unchainAllocDesc")){
	    _help.append("UnchainAllocDesc <file>");
	    _desc.append("Unchain allocation descriptor");
	    return CMD_OK;
	}
	printArgs(argv);
	api.link(policy, argv[1], argv[2]);
	return CMD_OK;
    }
    public int cmdREMOVE(String argv[], int mode) throws Exception{
	if(argv == null){
	    _help.append("Remove <file>");
	    _desc.append("Remove file or directory.");
	    return CMD_OK;
	}

	printArgs(argv);
	api.remove(policy, argv[1]);
	return CMD_OK;
    }
    public int cmdRENAME(String argv[], int mode) throws Exception{
	if(argv == null){
	    _help.append("Rename <file1> <file2>");
	    _desc.append("Rename file or directory.");
	    return CMD_OK;
	}

	printArgs(argv);
	api.rename(policy, argv[1], argv[2]);
	return CMD_OK;
    }
    public int cmdCHFLAG(String argv[], int mode) throws Exception{
	if(argv == null){
	    _help.append("Chflag <file> <flag>");
	    _desc.append("Change file's ICB flag.");
	    return CMD_OK;
	}

	printArgs(argv);
	api.chflag(policy, argv[1], Integer.parseInt(argv[2]));
	return CMD_OK;
    }
    public int cmdCHOWN(String argv[], int mode) throws Exception{
	if(argv == null){
	    _help.append("Chown <file> <uid>");
	    _desc.append("Change file's owner id.");
	    return CMD_OK;
	}

	printArgs(argv);
	api.chown(policy, argv[1], Integer.parseInt(argv[2]));
	return CMD_OK;
    }
    public int cmdCHGRP(String argv[], int mode) throws Exception{
	if(argv == null){
	    _help.append("Chgrp <file> <gid>");
	    _desc.append("Change file's group id.");
	    return CMD_OK;
	}

	printArgs(argv);
	api.chgrp(policy, argv[1], Integer.parseInt(argv[2]));
	return CMD_OK;
    }

    public int cmdCREATEIMAGE(String argv[], int mode) throws Exception{
	if(argv == null){
	    _help.append("CreateImage <revision> <image-size>");
	    _desc.append("Create new UDF_Image.");
	    return CMD_OK;
	}

	env.f = new UDF_RandomAccessZero(evalLong(argv[2]));
	image = UDF_ImageFactory.genImage(env, evalInt(argv[1]));
	image.setEnv(env);
	image.setUDFDocument(UDF_XMLUtil.genDocument());
	api = new UDF_API(image);

	return CMD_OK;
    }
    

    public int cmdOUTPUTIMAGE(String argv[], int mode) throws Exception{
	if(argv == null){
	    _help.append("OutputImage [<output image file>]");
	    _desc.append("Output image");
	    return CMD_OK;
	}

	if(argv.length >= 2){
	    System.err.println("output to" + argv[1]);
	    UDF_RandomAccessFile f = new UDF_RandomAccessFile(argv[1]);
	    image.writeTo(f);
	    f.close();
	}
	else{
	    System.err.println("output to" + output_file);
	    UDF_RandomAccessFile f = new UDF_RandomAccessFile(output_file);
	    image.writeTo(f);
	    f.close();
	}
	return CMD_OK;
    }


    public int cmdOUTPUTXML(String argv[], int mode) throws Exception{
	if(argv == null){
	    _help.append("OutputXML [<output XML file>]");
	    _desc.append("Output XML");
	    return CMD_OK;
	}

	if(argv.length >= 2){
	    OutputStream os = new FileOutputStream(argv[1]);
	    image.outputXML(os);
	}
	else if(output_xml == null)
	    image.outputXML(System.out);
	else{
	    OutputStream os = new FileOutputStream(output_xml);
	    image.outputXML(os);
	}

	return CMD_OK;
    }


    public int cmdATTACHDATATOFILE(String argv[], int mode) throws Exception{
	if(argv == null){
	    _help.append("AttachDataToFile <UDF file> <data file>");
	    _desc.append("Attach data file to UDF file.");
	    return CMD_OK;
	}
	
	api.attachDataToFile(policy, argv[1], argv[2]);
	return CMD_OK;
    }
    
    public int cmdAPPENDALLOCDESC(String argv[], int mode) throws Exception{
	if(argv == null){
	    _help.append("AppendAllocDesc <file>");
	    _desc.append("Append allocation descriptor to file. The allocation descriptor should be created by @resetAD, @addAD or @allocAndAddAD");
	    return CMD_OK;
	}

	printArgs(argv);
	/*
	UDF_AD[] ad = new UDF_AD[my_ad.size()];
	
	for(int i=0 ; i<ad.length ; ++i){
	    ad[i] = (UDF_AD)my_ad.elementAt(i);
	}
	*/
	api.appendAllocDesc(policy, argv[1], my_ad);
	return CMD_OK;
    }

    public int cmdREMOVEALLOCDESC(String argv[], int mode) throws Exception{
	if(argv == null){
	    _help.append("RemoveAllocDesc <file>");
	    _desc.append("Remove allocation descriptor from file. The allocation descriptor should be created by @resetAD, @addAD or @allocAndAddAD");
	    return CMD_OK;
	}
	printArgs(argv);
	/*
	UDF_AD[] ad = new UDF_AD[my_ad.size()];
	
	for(int i=0 ; i<ad.length ; ++i){
	    ad[i] = (UDF_AD)my_ad.elementAt(i);
	}
	*/
	api.removeAllocDesc(policy, argv[1], my_ad);
	return CMD_OK;
    }

    public int cmdREPLACEALLOCDESC(String argv[], int mode) throws Exception{
	if(argv == null){
	    _help.append("ReplaceAllocDesc <file>");
	    _desc.append("Remove all allocation descriptor from file and set new allocation descriptor. The allocation descriptor should be created by @resetAD, @addAD or @allocAndAddAD");
	    return CMD_OK;
	}
	printArgs(argv);
	/*
	UDF_AD[] ad = new UDF_AD[my_ad.size()];
	
	for(int i=0 ; i<ad.length ; ++i){
	    ad[i] = (UDF_AD)my_ad.elementAt(i);
	}
	*/
	api.replaceAllocDesc(policy, argv[1], my_ad);

	return CMD_OK;
    }

    public int cmdSHIFTPARTMAPSUBNO(String argv[], int mode) throws Exception{
	if(argv == null){
	    _help.append("IncPartMapSubno <partmapno>");
	    _desc.append("Increment Parttitoin Map Subno. This function is useful for packet write with VAT.");
	    return CMD_OK;
	}
	printArgs(argv);

	api.shiftPartMapSubno(policy, evalInt(argv[1]));

	return CMD_OK;
    }

    public int runCmd(String argv[], int mode) throws Exception{
	if(argv[0].equals("")){
	    //ignore
	}
	else if(argv[0].charAt(0) == '#'){
	    //ignore
	}
	else if(argv[0].indexOf('.') >= 0){//plugin
	    String clsname = argv[0];
	    Class c = Class.forName(clsname);
	    Constructor cst = c.getConstructor(new Class[]{});
	    FrontendPlugin pg = (FrontendPlugin)cst.newInstance(new Object[]{});
	    pg.setFrontend(this);
	    String[] argv2;
	    argv2 = new String[argv.length - 1];
	    for(int i=0 ; i<argv2.length ; ++i)
		argv2[i] = argv[i + 1];
	    
	    pg.runCmd(argv2, mode);
	}
	else if(argv[0].equalsIgnoreCase("debug")){
	    printArgs(argv);
	    UDF_FEDesc fe = api.getFileEntry(argv[1], 0);
	    fe.debug(0);
	}
	else if(argv[0].equalsIgnoreCase("@test1")){
	    UDF_DescPolicy pol = new UDF_DescPolicyImpl();
	    UDF_CrcDesc d = pol.createVolDesc(image, policy, evalInt(argv[1]));
	    d.debug(0);
	}
	else if(argv[0].equalsIgnoreCase("@test2")){
	    UDF_VRSPolicy pol = new UDF_VRSPolicyImpl();
	    UDF_VRS d = pol.createVRS(image, policy, 16);
	    d.debug(0);
	}
	else if(argv[0].equalsIgnoreCase("@test3")){
	    UDF_desc256 d = env.getFileSetDesc(0);
	    d.debug(0);
	    d = env.getFileSetDesc(1);
	    d.debug(0);
	}
	else if(argv[0].equalsIgnoreCase("@test4")){
	    UDF_VolPolicy pol = new UDF_VolPolicyImpl();
	    pol.createVolume(image, policy);
	}
	else if(argv[0].equalsIgnoreCase("@test5")){
	    policy.getVRSPolicy().createECMA119Bridge(image, policy);
	}
	else if(argv[0].equalsIgnoreCase("@test999")){
	    JFrame.setDefaultLookAndFeelDecorated(true);
	    JFrame frame = new JFrame("FrontEnd");
	    JLabel label = new JLabel("Hello World");
	    //JComponent hoge = new JTextArea("hogehoge");
	    JComponent hoge = image.getJInfo();
	    frame.getContentPane().add(hoge);

	    frame.pack();
	    frame.setVisible(true);
	}


	//以下内部コマンド
	//内部で保有する ADLISTをリセットする
	else if(argv[0].equalsIgnoreCase("@resetAD")){
	    printArgs(argv);
	    //my_ad = new UDF_ADList();
	    my_ad.clear();
	}
	// @allocAndAddAD <size> <type> <lbn> <partno>
	//内部で保有する ADにデータを追加する(手動指定)
	else if(argv[0].equalsIgnoreCase("@addAD")){
	    printArgs(argv);

	    int lbn = 0;
	    int partno = data_partno;
	    int type = 0;
	    long size = 0;

	    if(argv.length >= 5)
		partno = evalInt(argv[4]);
	    if(argv.length >= 4)
		lbn = evalInt(argv[3]);
	    if(argv.length >= 3)
		type = evalInt(argv[2]);
	    if(argv.length >= 2)
		size = evalLong(argv[1]);
	    UDF_AD ad = UDF_AD.genAD(image, type);
	    ad.setDefaultValue();

	    ad.setLbn(lbn);
	    ad.setPartRefNo(partno);
	    ad.setLen(size);

	    my_ad.add(ad);
	}
	// @allocAndAddAD <size> <type> <lbn> <partno> <align>
	//内部で保有する ADにデータを追加する(自動で検索)
	else if(argv[0].equalsIgnoreCase("@allocAndAddAD")){
	    printArgs(argv);

	    int lbn = 0;
	    int partno = data_partno;
	    int align = 1;
	    int type = 0;
	    long size = 0;
	    if(argv.length >= 6)
		align = evalInt(argv[5]);
	    if(argv.length >= 5)
		partno = evalInt(argv[4]);
	    if(argv.length >= 4)
		lbn = evalInt(argv[3]);
	    if(argv.length >= 3)
		type = evalInt(argv[2]);
	    if(argv.length >= 2)
		size = evalLong(argv[1]);
	    UDF_AllocPolicyImpl alp = new UDF_AllocPolicyImpl(lbn, partno, align);
	    UDF_ADList ad = alp.alloc(image, size, type);
	    for(Iterator i = ad.iterator() ; i.hasNext() ;)
		my_ad.add((UDF_AD)i.next());
	}
	else{
	    Class[] cls = {String[].class, int.class};
	    try{
		String mth = "cmd" + argv[0].toUpperCase();
		java.lang.reflect.Method method = getClass().getMethod(mth, cls);

		Object[] obj = {argv, new Integer(mode)};
		if(method != null){
		    Object o = method.invoke(this, obj);
		    return ((Integer) o).intValue();
		}
		else
		    System.out.println("Unknown command: " + argv[0]);
	    }
	    catch(java.lang.reflect.InvocationTargetException e){
		e.getCause().printStackTrace();
		System.out.println("Error: " + argv[0]);
	    }
	    catch(NoSuchMethodException e){
		e.printStackTrace();
		System.out.println("Error: " + argv[0]);
	    }
	    catch(SecurityException e){
		e.printStackTrace();
		System.out.println("Error: " + argv[0]);
	    }
	}
	return CMD_OK;
    }
    public void printArgs(String argv[]){
	printArgs(argv, MODE_DEFAULT);
    }
    public void printArgs(String argv[], int mode){
	if((mode & MODE_SILENT) != 0)
	    ;
	else{
	    for(int i=0 ; i<argv.length ; ++i){
		System.err.print(argv[i] + " ");
	    }
	    System.err.print("\n");
	}
    }
}
