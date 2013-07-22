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
package com.udfv.udfapi;

import java.lang.*;
import java.io.*;
import com.udfv.core.*;
import com.udfv.ecma167.*;
import com.udfv.exception.*;
import com.udfv.access.*;
import com.udfv.encoding.*;
import com.udfv.util.*;

public class UDF_VolPolicyImpl implements UDF_VolPolicy {
    public UDF_VolPolicyImpl(){
	;
    }

    UDF_Image my_image;
    int total_sec;

    public int getRevision(){return 0x250;}

    //Volmue作成パラメータ
    public int getPartLen(){return total_sec - 544;};
    public int getUSBLen(){return total_sec - 544;};
    public int getUSBPos(){return 2;};
    public int getFSDSLbn(){return 0;};
    public int getFSDSPartno(){return 1;};
    public long getFSDSLen(){return 4096;};
    public int getRootLbn(){return 16;};
    public int getRootPartno(){return 1;};
    public int getSRootLbn(){return 18;};
    public int getSRootPartno(){return 1;};
    public String[] getPartMapId(){return new String[]{"", "*UDF Metadata Partition"};};


    public int getVolSeqNumber(){return 1;}
    public int getMaxVolSeqNumber(){return 1;}
    public int getInterchangeLevel(){return 2;}
    public String getVolId(){return "8:HEART UDF VOLUME";}
    public String getVolSetId(){return "8:2C6428C300000000TEST_IMAGE";}

    public int getMVDSLoc(){return 32;}
    public int getMVDSLen(){return 32768;}
    public int getRVDSLoc(){return 64;}
    public int getRVDSLen(){return 32768;}

    public String getLVInfo1(){return "8:OWNER NAME";}
    public String getLVInfo2(){return "8:Heart Solutions, Inc.";}
    public String getLVInfo3(){return "8:CONTACT INFORMATION";}

    public int getAccessType(){return 4;}
    public int getPartNumber(){return 0;}
    public int getPartStartingLoc(){return 272;}
    public String getNSR(){return "+NSR03";}
    public String getLogicalVolId(){return "8:PSX Volume";}
    public int getLogicalBlockSize(){return 2048;}

    public int getLVISLoc(){return 96;}
    public int getLVISLen(){return 8192;}
    public int getFSDSLoc(){return 0;}

    public String getFileSetId(){return "8:$ Dvd Rewritable Volume $";}

    public int getSparablePacketLen(){return 16;}
    public int getSparableSizeOfEachTable(){return 6144;}
    public int getSparableReallocationTableLen(){return 512;}
    public int[] getSparableLocOfSparingTables(){return new int[]{288, 8544};}
    public int[] getSparableMapEntry(){
	int[] sparable_map_entry = new int[getSparableReallocationTableLen() * 2];
	int n = 320;
	for(int i=0 ; i<sparable_map_entry.length ; ){
	    sparable_map_entry[i] = -1;
	    sparable_map_entry[i+1] = n;
	    i += 2;
	    n += 16;
	}

	return sparable_map_entry;
    }
    public int getMetadataFileLoc(){return 0;}
    public int getMetadataMirrorFileLoc(){return 2296319;}
    public int getMetadataBitmapFileLoc(){return 1;}
    public int getMetadataAllocUnitSize(){return 32;}
    public int getMetadataAlignUnitSize(){return 16;}
    public int getMetadataFlags(){return 1;}
    public UDF_ExtentElem[] getMetadataFileExtent(){
	UDF_ExtentElem[] ee = new UDF_ExtentElem[1];
	ee[0] = new UDF_ExtentElem(144, 0, 2097152L, 0);
	return ee;
    }
    public UDF_ExtentElem[] getMetadataMirrorFileExtent(){
	UDF_ExtentElem[] ee = new UDF_ExtentElem[1];
	ee[0] = new UDF_ExtentElem(2296320, 0, 2097152L, 0);
	return ee;
    }

    //以下 interfaceにない項目
    public boolean has2ndAnchor(){
	return true;
    }
    public boolean has3rdAnchor(){
	return true;
    }
    public boolean has2ndVRS(){
	return true;
    }

    /**
       Volumeを作る

       createVRS() -> 
       createAnchor() ->  createVDS() -> createLVIS() ->
       createFSDS() -> を順番に呼び出す
    */
    public void createVolume(UDF_Image image, UDF_Policy policy) throws IOException,UDF_Exception{
	my_image = image;
	total_sec = (int)(my_image.env.getImageSize() / image.env.LBS);
	image.env.recorded_udf_revision = getRevision();

	UDF_VRSPolicy vrspol = policy.getVRSPolicy();
	UDF_Extent vrs = vrspol.createVRS(image, policy, 16);
	image.appendChild(vrs);
	if(has2ndVRS())
	    image.appendChild(vrspol.createVRS(image, policy, total_sec - 17));

	UDF_Extent avdp = createAnchor(image, policy, 256);
	image.appendChild(avdp);
	UDF_desc2 anchor = (UDF_desc2)avdp.getFirstChild();
	image.env.anchorVolDescPointer[0] = anchor;

	if(has2ndAnchor()){
	   UDF_Extent avdp2 = createAnchor(image, policy, total_sec - 1);
	   UDF_desc2 anchor2 = (UDF_desc2)avdp2.getFirstChild();
	   image.env.anchorVolDescPointer[2] = anchor2;
	   image.appendChild(avdp2);
	}

	if(has3rdAnchor()){
	    UDF_Extent avdp3 = createAnchor(image, policy, total_sec - 257);
	    UDF_desc2 anchor3 = (UDF_desc2)avdp3.getFirstChild();
	    image.env.anchorVolDescPointer[1] = anchor3;
	    image.appendChild(avdp3);
	}

	UDF_VDS mvds = createVDS(image, policy, "mvds", anchor.getMVDSLoc(), anchor.getMVDSLen(), false);
	UDF_VDS rvds = createVDS(image, policy, "rvds", anchor.getRVDSLoc(), anchor.getRVDSLen(), true);

	image.env.mvds = mvds;
	image.env.rvds = rvds;

	mvds.recalc(UDF_ElementBase.RECALC_VDSLIST2, null);
	rvds.recalc(UDF_ElementBase.RECALC_VDSLIST2, null);


	image.appendChild(mvds);
	image.appendChild(rvds);

	mvds.setAttribute("id", "MVDS");
	rvds.setAttribute("id", "RVDS");

	{
	    //0番目のパーティション決めうちとなっている。
	    {
		UDF_desc5 desc5 = (UDF_desc5)image.env.mvds.getPrevailingPartDesc().elementAt(0);
		
		UDF_PartHeaderDesc phd = (UDF_PartHeaderDesc)image.createElement
		    ("UDF_PartHeaderDesc", null, null);
		phd.setDefaultValue();
		phd.getUnallocatedSpaceBitmap().getExtentLen().setValue(getUSBLen());
		phd.getUnallocatedSpaceBitmap().getExtentLen().setAttribute("ref", "SB0.len");
		phd.getUnallocatedSpaceBitmap().getExtentPos().setValue(getUSBPos());
		phd.getUnallocatedSpaceBitmap().getExtentPos().setAttribute("ref", "SB0.pos");
		desc5.getPartContentsUse().replaceChild(phd);
		
		//image.env.mainPartHeaderDesc = phd;
	    }
	    {
		UDF_desc5 desc5 = (UDF_desc5)image.env.rvds.getPrevailingPartDesc().elementAt(0);
		
		UDF_PartHeaderDesc phd = (UDF_PartHeaderDesc)image.createElement
		    ("UDF_PartHeaderDesc", null, null);
		phd.setDefaultValue();
		if(getUSBLen() > 0){
		    phd.getUnallocatedSpaceBitmap().getExtentLen().setValue(getUSBLen());
		    phd.getUnallocatedSpaceBitmap().getExtentLen().setAttribute("ref", "SB0.len");
		    phd.getUnallocatedSpaceBitmap().getExtentPos().setValue(getUSBPos());
		    phd.getUnallocatedSpaceBitmap().getExtentPos().setAttribute("ref", "SB0.pos");
		}
		desc5.getPartContentsUse().replaceChild(phd);

		//image.env.reservePartHeaderDesc = phd;
	    }

	    if(getUSBLen() != 0 && getUSBPos() != 0){
		UDF_Extent extent = (UDF_Extent)image.createElement("UDF_Extent", null, null);
		extent.setAttribute("id", "SB0");
		extent.addExtent(getUSBPos(), 0, getUSBLen());
		
		UDF_desc264 desc264 = (UDF_desc264)policy.getDescPolicy().createFSDesc(image, policy, 264);
		desc264.getNumberOfBits().setValue(getPartLen());
		desc264.getNumberOfBytes().setValue((getPartLen()+7)/8);
		desc264.getBitmap().setSize((getPartLen()+7)/8);
		
		extent.appendChild(desc264);
		image.appendChild(extent);
	    }
	}

	UDF_desc6 desc6 = image.env.getLogicalVolDesc(UDF_Env.VDS_AUTO);
	UDF_Element[] pm = desc6.getPartMaps().getChildren();
	//テンポラリで設定しておく
	for(int i=0 ; i<pm.length ; ++i){
	    UDF_Extent ext = (UDF_Extent)image.createElement("UDF_Extent", null, null);
	    ext.addExtent(getPartStartingLoc()-1, -1, getPartLen());
	    image.env.setPartMapExtent(i, 0, ext);
	    image.env.setPartMapRandomAccess(i, 0, ext.genRandomAccessExtent());
	}
	for(int i=0 ; i<pm.length ; ++i){
	    policy.getPartMapPolicy().createPartMapSub(image, policy, (UDF_PartMap)pm[i]);
	}

	mvds.recalc(UDF_ElementBase.RECALC_VDSLIST, null);
	rvds.recalc(UDF_ElementBase.RECALC_VDSLIST, null);

	int is_len = desc6.getIntegritySeqExtent().getExtentLen().getIntValue();
	int is_loc = desc6.getIntegritySeqExtent().getExtentLoc().getIntValue();

	int pn = desc6.getNumberOfPartMaps().getIntValue();
	image.env.part = new UDF_PartMap[pn];
		
	mvds.recalc(UDF_ElementBase.RECALC_ENV, null);
	rvds.recalc(UDF_ElementBase.RECALC_ENV, null);

	for(int i=0 ; i<image.env.getPartMapList().size() ; ++i){
	    image.env.part[i] = image.env.getPartMap(i);
	    image.env.part[i].recalc(UDF_ElementBase.RECALC_PARTMAP, image.env.f);
	}

	UDF_IS is = createLVIS(image, policy, is_loc);
	image.appendChild(is);
	image.createPartElement();

	is.recalc(UDF_ElementBase.RECALC_VDSLIST, null);

	createFSDS(image, policy, "fsds", getFSDSLbn(), getFSDSPartno(), getFSDSLen());

	createRootDir(image, policy, getRootLbn(), getRootPartno());
	createRootSDir(image, policy, getSRootLbn(), getSRootPartno());

	image.recalc(UDF_ElementBase.RECALC_ENV, image.env.f);
	image.recalc(UDF_ElementBase.RECALC_SB, image.env.f);

	//image.env.numberOfDirs = 1;
	//image.env.numberOfFiles = 0;
	image.env.setNumberOfDirectories(1);
	image.env.setNumberOfFiles(0);
	
    }

    /**
       ROOT Directory を作る
     */
    public void createRootDir(UDF_Image image, UDF_Policy policy, int lbn, int partno) throws UDF_Exception, IOException{
	createRootDir(image, policy, lbn, partno, 0, false);
	if(image.env.isMetadataPartMap(partno))
	    createRootDir(image, policy, lbn, partno, 1, false);
    }
    /**
       ROOT Directory を作る
     */
    public void createRootSDir(UDF_Image image, UDF_Policy policy, int lbn, int partno) throws UDF_Exception, IOException{
	createRootDir(image, policy, lbn, partno, 0, true);
	if(image.env.isMetadataPartMap(partno))
	    createRootDir(image, policy, lbn, partno, 1, true);
    }

    /**
       Directory を作る
     */
    public UDF_FEDesc createRootDir(UDF_Image image, UDF_Policy policy, int lbn, int partno, int subno, boolean stream) throws UDF_Exception, IOException{
	UDF_Extent ext = (UDF_Extent)image.createElement("UDF_Extent", null, null);
	ext.setPartSubno(subno);
	ext.addExtent(lbn, partno, image.env.LBS);
	UDF_FEDesc fe = null;
	String subnosfx = subno !=0 ? ("_" + subno) : "";
	if(stream){
	    fe = policy.getFEPolicy().create(image, policy, UDF_icbtag.T_SDIRECTORY);
	    ext.setAttribute("id", "SROOT" + subnosfx);

	    fe.getLenOfExtendedAttr().setValue(0);
	    fe.getExtendedAttr().setData(new byte[0]);
	}
	else{
	    fe = policy.getFEPolicy().create(image, policy, UDF_icbtag.T_DIRECTORY);
	    ext.setAttribute("id", "ROOT" + subnosfx);
	}

	ext.appendChild(fe);
	image.appendChild(ext);

	UDF_Extent ext_dir = (UDF_Extent)image.createElement("UDF_Extent", null, null);
	ext_dir.setPartSubno(subno);
	ext_dir.addExtent(lbn+1, partno, 40);
	
	UDF_Directory dir = null;

	if(!stream)
	    dir =(UDF_Directory)image.createElement("UDF_Directory", null, null);
	else
	    dir =(UDF_Directory)image.createElement("UDF_SDirectory", null, null);

	dir.setSize(40);
    
	UDF_desc257 fid = policy.getFIDPolicy().create(image, policy, "");
	fid.getFileChar().setValue(UDF_desc257.DIRECTORY | UDF_desc257.PARENT);
	String prefix = stream ? "S" : "";
	String postfix = subnosfx;
	fid.getICB().getExtentLen().setAttribute("ref", prefix + "ROOT" + postfix + ".len");
	fid.getICB().getExtentLoc().getLogicalBlockNumber().setAttribute("ref", prefix + "ROOT" + postfix + ".lbn");
	fid.getICB().getExtentLoc().getPartReferenceNumber().setAttribute("ref", prefix + "ROOT" + postfix +".partno");

	//LINK貼り
	fid.setReferenceTo(fe);
	fe.getDirectoryList().add(fid);

	UDF_ExtentElem[] ee = new UDF_ExtentElem[1];
	ee[0] = new UDF_ExtentElem();
	ee[0].setLoc(lbn + 1);
	ee[0].setLen(fid.getSize());
	ee[0].setPartRefNo(partno);
	fe.appendAD(ee);

	//LABEL貼り
	String label = UDF_Label.genDirectoryLabel(image.env, lbn+1, partno, subno, stream);
	fe.getAllocDesc().setAttribute("ref", label);
	dir.setAttribute("id", label);
	
	dir.appendChild(fid);
	ext_dir.appendChild(dir);
	image.appendChild(ext_dir);

	return fe;
    }


    public void createFSDS(UDF_Image image, UDF_Policy policy, String tagname, int lbn, int partno, long len) throws IOException,UDF_Exception{
	createFSDS(image, policy, tagname, lbn, partno, len, 0);
	if(image.env.isMetadataPartMap(partno))
	    createFSDS(image, policy, tagname, lbn, partno, len, 1);
    }
    public void createFSDS(UDF_Image image, UDF_Policy policy, String tagname, int lbn, int partno, long len, int subno) throws IOException,UDF_Exception{
	UDF_FDS fds = (UDF_FDS)image.createElement("UDF_FDS", null, tagname);
	image.env.setFDS(null, subno, fds);

	fds.setPartSubno(subno);
	fds.addExtent(lbn, partno, len, 0);
	UDF_DescPolicy desc_pol = policy.getDescPolicy();

	UDF_desc256 desc256 = (UDF_desc256)desc_pol.createFSDesc(image, policy, 256);
	String subnosfx = subno!=0 ? ("_"+subno) : "";
	desc256.getRootDirectoryICB().getExtentLbn().setAttribute("ref", "ROOT"+subnosfx+".lbn");
	desc256.getRootDirectoryICB().getExtentLen().setAttribute("ref", "ROOT"+subnosfx+".len");
	desc256.getRootDirectoryICB().getExtentPartRefNo().setAttribute("ref", "ROOT"+subnosfx+".partno");

	desc256.getSystemStreamDirectoryICB().getExtentLbn().setAttribute("ref", "SROOT"+subnosfx+".lbn");
	desc256.getSystemStreamDirectoryICB().getExtentLen().setAttribute("ref", "SROOT"+subnosfx+".len");
	desc256.getSystemStreamDirectoryICB().getExtentPartRefNo().setAttribute("ref", "SROOT"+subnosfx+".partno");

	UDF_desc8 desc8 = (UDF_desc8)desc_pol.createVolDesc(image, policy, 8);
	fds.appendChild(desc256);

	UDF_pad pad = (UDF_pad)image.createElement("UDF_pad", null, null);
	pad.setAlign(image.env.LBS);
	fds.appendChild(pad);

	fds.appendChild(desc8);
	pad = (UDF_pad)image.createElement("UDF_pad", null, null);
	pad.setAlign(image.env.LBS);
	
	image.appendChild(fds);
	fds.recalcFDSLIST();
    }

    public UDF_VDS createVDS(UDF_Image image, UDF_Policy policy, String tagname, int sec, long len, boolean reserved) throws IOException,UDF_Exception{
	UDF_VDS vds = (UDF_VDS)image.createElement("UDF_VDS", null, tagname);
	vds.addExtent(sec, -1, len, 0);
	int volseq = 1;

	UDF_DescPolicy desc_pol = policy.getDescPolicy();

	UDF_desc1 desc1 = (UDF_desc1)desc_pol.createVolDesc(image, policy, 1);
	UDF_desc4 desc4 = (UDF_desc4)desc_pol.createVolDesc(image, policy, 4);
	UDF_desc5 desc5 = (UDF_desc5)desc_pol.createVolDesc(image, policy, 5);
	UDF_desc6 desc6 = (UDF_desc6)desc_pol.createVolDesc(image, policy, 6);
	UDF_desc7 desc7 = (UDF_desc7)desc_pol.createVolDesc(image, policy, 7);
	UDF_desc8 desc8 = (UDF_desc8)desc_pol.createVolDesc(image, policy, 8);
	String[] partmap = getPartMapId();

	{
	    UDF_PartMap[] pm = new UDF_PartMap[partmap.length];
	    int size = 0;
	    for(int i=0 ; i<partmap.length ; ++i){
		pm[i] = policy.getPartMapPolicy().createPartMap(image, policy, partmap[i]);
		size += pm[i].getSize();
	    }
	    desc6.getNumberOfPartMaps().setValue(pm.length);
	    desc6.getMapTableLen().setValue(size);
	    desc6.getPartMaps().setSize(size);
	    desc6.getPartMaps().removeAllChildren();
	    for(int i=0 ; i<partmap.length ; ++i){
		desc6.getPartMaps().appendChild(pm[i]);
	    }
	    desc6.getDescTag().getDescCRCLen().setValue(desc6.getSize() - 16);
	}

	vds.appendChild(desc1);
	desc1.getVolDescSeqNumber().setValue(volseq++);
	UDF_pad pad = (UDF_pad)image.createElement("UDF_pad", null, null);
	pad.setAlign(image.env.LBS);
	pad.setSize(0);
	vds.appendChild(pad);

	vds.appendChild(desc4);
	desc4.getVolDescSeqNumber().setValue(volseq++);
	pad = (UDF_pad)image.createElement("UDF_pad", null, null);
	pad.setAlign(image.env.LBS);
	pad.setSize(0);
	vds.appendChild(pad);

	vds.appendChild(desc5);
	desc5.getVolDescSeqNumber().setValue(volseq++);
	pad = (UDF_pad)image.createElement("UDF_pad", null, null);
	pad.setAlign(image.env.LBS);
	pad.setSize(0);
	vds.appendChild(pad);

	vds.appendChild(desc6);
	desc6.getVolDescSeqNumber().setValue(volseq++);
	pad = (UDF_pad)image.createElement("UDF_pad", null, null);
	pad.setAlign(image.env.LBS);
	pad.setSize(0);
	vds.appendChild(pad);

	vds.appendChild(desc7);
	desc7.getVolDescSeqNumber().setValue(volseq++);
	pad = (UDF_pad)image.createElement("UDF_pad", null, null);
	pad.setAlign(image.env.LBS);
	pad.setSize(0);
	vds.appendChild(pad);

	vds.appendChild(desc8);

	return vds;
    }

    public UDF_IS createLVIS(UDF_Image image, UDF_Policy policy, int sec) throws IOException,UDF_Exception{
	UDF_IS is = (UDF_IS)image.createElement("UDF_IS", null, "is");
	is.setAttribute("id", "LVIS");
	is.addExtent(sec, -1, image.env.LBS * 2, 0);
	UDF_DescPolicy desc_pol = policy.getDescPolicy();
	String[] partmap = getPartMapId();

	UDF_desc9 desc9 = (UDF_desc9)desc_pol.createVolDesc(image, policy, 9);

	desc9.getNumberOfPart().setValue(partmap.length);
	desc9.getFreeSpaceTable().setSize(4 * partmap.length);
	desc9.getSizeTable().setSize(4 * partmap.length);

	for(int i=0 ; i<partmap.length ; ++i){
	    UDF_uint32 u = (UDF_uint32)image.createElement("UDF_uint32", null, null);
	    desc9.getFreeSpaceTable().appendChild(u);
	    UDF_uint32 uu = (UDF_uint32)image.createElement("UDF_uint32", null, null);
	    uu.setValue(image.env.getPartMapExtent(i,0).getLongSize() / image.env.LBS);
	    desc9.getSizeTable().appendChild(uu);
	}
	desc9.getDescTag().getDescCRCLen().setValue(desc9.getSize() - 16);


	is.appendChild(desc9);
	UDF_pad pad = (UDF_pad)image.createElement("UDF_pad", null, null);
	pad.setAlign(image.env.LBS);
	pad.setSize(0);
	is.appendChild(pad);

	UDF_desc8 desc8 = (UDF_desc8)desc_pol.createVolDesc(image, policy, 8);
	is.appendChild(desc8);
	pad = (UDF_pad)image.createElement("UDF_pad", null, null);
	pad.setAlign(image.env.LBS);
	pad.setSize(0);
	is.appendChild(pad);

	return is;
    }

    public UDF_Extent createAnchor(UDF_Image image, UDF_Policy policy, int sec) throws IOException,UDF_Exception{
	UDF_Extent extent = (UDF_Extent)image.createElement("UDF_Extent", null, "avdp");
	extent.addExtent(sec, -1, image.env.LBS, 0);

	UDF_DescPolicy desc_pol = policy.getDescPolicy();

	UDF_desc2 desc2 = (UDF_desc2)desc_pol.createVolDesc(image, policy, 2);
	extent.appendChild(desc2);
	UDF_pad pad = (UDF_pad)image.createElement("UDF_pad", null, null);
	pad.setAlign(image.env.LBS);
	pad.setSize(0);
	extent.appendChild(pad);

	return extent;
    }
}
