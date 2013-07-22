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
import com.udfv.udf150.UDF_SparablePartMap;
import com.udfv.udf150.UDF_VirtualPartMap;
import com.udfv.udf250.UDF_MetadataPartMap;

public class UDF_PartMapPolicyImpl implements UDF_PartMapPolicy{
    //現在のバージョンでは作れる Partitionの総数は1固定
    int vol_seq_number = 1;
    int part_number = 0;

    public UDF_PartMapPolicyImpl(){
    }

    /**
       パーティションマップに付随する情報の作成
     */
    public void createPartMapSub(UDF_Image image, UDF_Policy policy, UDF_PartMap pm) throws UDF_Exception, IOException{
	UDF_VolPolicy vp = policy.getVolPolicy();
	if(UDF_VirtualPartMap.class.isAssignableFrom(pm.getClass())){
	    UDF_Extent ext_vat = (UDF_Extent)image.createElement("UDF_Extent", null, null);
	    ext_vat.addExtent((int)(image.getLongSize()/image.env.LBS - policy.getVolPolicy().getPartStartingLoc()), 0, image.env.LBS);
	    UDF_FEDesc fe_vat = policy.getFEPolicy().create(image, policy, 248);
	    com.udfv.udf200.UDF_VirtualAllocTable200 vat200 =
		(com.udfv.udf200.UDF_VirtualAllocTable200)image.createElement("UDF_VirtualAllocTable200", null, null);
	    vat200.setHintSize(152);
	    vat200.setDefaultValue();
	    vat200.getLogicalVolId().setValue(policy.getVolPolicy().getLogicalVolId());
	    vat200.getNumberOfDirectories().setValue(1);
	    vat200.getMinUDFReadVersion().setValue(policy.getVolPolicy().getRevision());
	    vat200.getMinUDFWriteVersion().setValue(policy.getVolPolicy().getRevision());
	    vat200.getMaxUDFWriteVersion().setValue(policy.getVolPolicy().getRevision());
	    fe_vat.getICBTag().getFlags().setValue(3+32);
	    fe_vat.getAllocDesc().setSize(vat200.getSize());
	    fe_vat.getAllocDesc().replaceChild(vat200);
	    fe_vat.getLenOfAllocDesc().setValue(vat200.getSize());
	    ext_vat.appendChild(fe_vat);
	    image.appendChild(ext_vat);
	}
	if(UDF_MetadataPartMap.class.isAssignableFrom(pm.getClass())){
	    UDF_MetadataPartMap mpm = (UDF_MetadataPartMap)pm;
	    
	    UDF_Extent ext_main = (UDF_Extent)image.createElement("UDF_Extent", null, null);
	    UDF_Extent ext_mirror = (UDF_Extent)image.createElement("UDF_Extent", null, null);
	    UDF_Extent ext_bitmap = (UDF_Extent)image.createElement("UDF_Extent", null, null);

	    ext_main.addExtent(vp.getMetadataFileLoc(), 0, image.env.LBS);
	    ext_mirror.addExtent(vp.getMetadataMirrorFileLoc(), 0, image.env.LBS);
	    ext_bitmap.addExtent(vp.getMetadataBitmapFileLoc(), 0, image.env.LBS);

	    UDF_FEDesc fe_main = policy.getFEPolicy().create(image, policy, 250);
	    UDF_FEDesc fe_mirror = policy.getFEPolicy().create(image, policy, 251);
	    UDF_FEDesc fe_bitmap = policy.getFEPolicy().create(image, policy, 252);
	    mpm.setMetadataFile(fe_main);
	    mpm.setMetadataMirrorFile(fe_mirror);
	    mpm.setMetadataBitmapFile(fe_bitmap);

	    ext_main.appendChild(fe_main);
	    ext_mirror.appendChild(fe_mirror);
	    ext_bitmap.appendChild(fe_bitmap);

	    image.appendChild(ext_main);
	    image.appendChild(ext_mirror);
	    image.appendChild(ext_bitmap);

	    fe_main.appendAD(vp.getMetadataFileExtent());
	    fe_mirror.appendAD(vp.getMetadataMirrorFileExtent());

	    String l1 = UDF_Label.genExtentLabel(ext_main);
	    String l2 = UDF_Label.genExtentLabel(ext_mirror);
	    String l3 = UDF_Label.genExtentLabel(ext_bitmap);
	    /*
	    mpm.getMetadataFileLoc().setAttribute("ref", l1 + ".lbn");
	    mpm.getMetadataMirrorFileLoc().setAttribute("ref", l2 + ".lbn");
	    mpm.getMetadataBitmapFileLoc().setAttribute("ref", l3 + ".lbn");
	    */
	    ext_main.setAttribute("id", l1);
	    ext_mirror.setAttribute("id", l2);
	    ext_bitmap.setAttribute("id", l3);

	    fe_bitmap.getICBTag().getFlags().setValue(35);
	    int ad_len = (int)(fe_main.calcInfoLenFromAD() / image.env.LBS);
	    fe_bitmap.getLenOfAllocDesc().setValue((ad_len+7)/8 + 16 + 8);
	    fe_bitmap.getAllocDesc().setSize((ad_len+7)/8 + 16 + 8);

	    UDF_desc264 desc264 = (UDF_desc264)policy.getDescPolicy().createFSDesc(image, policy, 264);
	    desc264.getNumberOfBits().setValue(ad_len);
	    desc264.getNumberOfBytes().setValue((ad_len+7)/8);
	    desc264.getBitmap().setSize((ad_len+7)/8);
	
	    fe_bitmap.getAllocDesc().appendChild(desc264);

	    //image.env.metaPartSpaceBitmapDesc = desc264;
	}
    }

    public UDF_PartMap createPartMap(UDF_Image image, UDF_Policy policy, String id) throws UDF_Exception, IOException{
	UDF_PartMap pm = null;
	UDF_VolPolicy vp = policy.getVolPolicy();

	if(id == null || id.equals("")){
	    pm = (UDF_PartMap)image.createElement("UDF_part_map1", null, null);
	    pm.setDefaultValue();
	    pm.getPartMapType().setValue(1);
	}
	else if(id.equals("*UDF Sparable Partition")){
	    pm = (UDF_PartMap)image.createElement("UDF_SparablePartMap", null, null);
	    pm.setDefaultValue();
	    UDF_SparablePartMap spm = (UDF_SparablePartMap)pm;
	    spm.getPartMapType().setValue(2);
	    spm.getPartMapLen().setValue(64);
	    spm.setPartTypeId(policy.getRegidPolicy().createUDFId(image, "*UDF Sparable Partition", "part-type-id"));
	    spm.getVolSeqNumber().setValue(vol_seq_number);
	    spm.getPartNumber().setValue(part_number);
	    spm.getPacketLen().setValue(vp.getSparablePacketLen());
	    spm.getNumberOfSparingTables().setValue(vp.getSparableLocOfSparingTables().length);
	    spm.getSizeOfEachSparingTable().setValue(vp.getSparableSizeOfEachTable());
	    UDF_ElementList el = new UDF_ElementList();
	    for(int i=0 ; i<vp.getSparableLocOfSparingTables().length ; ++i){
		UDF_uint32 loc = (UDF_uint32)image.createElement("UDF_uint32", null, null);
		loc.setValue(vp.getSparableLocOfSparingTables()[0]);
		el.add(loc);
	    }
	    spm.getLocsOfSparingTables().setSize(vp.getSparableLocOfSparingTables().length * 4);
	    spm.getLocsOfSparingTables().replaceChildren(el);
	}
	else if(id.equals("*UDF Virtual Partition")){
	    pm = (UDF_PartMap)image.createElement("UDF_VirtualPartMap", null, null);
	    pm.setDefaultValue();
	    UDF_VirtualPartMap vpm = (UDF_VirtualPartMap)pm;
	    vpm.getPartMapType().setValue(2);
	    vpm.getPartMapLen().setValue(64);
	    vpm.setPartTypeId(policy.getRegidPolicy().createUDFId(image, "*UDF Virtual Partition", "part-type-id"));
	}
	else if(id.equals("*UDF Metadata Partition")){
	    pm = (UDF_PartMap)image.createElement("UDF_MetadataPartMap", null, null);
	    pm.setDefaultValue();
	    UDF_MetadataPartMap mpm = (UDF_MetadataPartMap)pm;
	    mpm.getPartMapType().setValue(2);
	    mpm.getPartMapLen().setValue(64);
	    mpm.setPartTypeId(policy.getRegidPolicy().createUDFId(image, "*UDF Metadata Partition", "part-type-id"));
	    mpm.getVolSeqNumber().setValue(vol_seq_number);
	    mpm.getMetadataFileLoc().setValue(vp.getMetadataFileLoc());
	    mpm.getMetadataMirrorFileLoc().setValue(vp.getMetadataMirrorFileLoc());
	    mpm.getMetadataBitmapFileLoc().setValue(vp.getMetadataBitmapFileLoc());
	    mpm.getAllocUnitSize().setValue(vp.getMetadataAllocUnitSize());
	    mpm.getAlignmentUnitSize().setValue(vp.getMetadataAlignUnitSize());
	    mpm.getFlags().setValue(vp.getMetadataFlags());

	    //以下の3つのextentはラベルを作るためだけに生成する
	    //実体は createPartMapSubで作る
	    UDF_Extent ext_main = (UDF_Extent)image.createElement("UDF_Extent", null, null);
	    UDF_Extent ext_mirror = (UDF_Extent)image.createElement("UDF_Extent", null, null);
	    UDF_Extent ext_bitmap = (UDF_Extent)image.createElement("UDF_Extent", null, null);

	    ext_main.addExtent(vp.getMetadataFileLoc(), 0, image.env.LBS);
	    ext_mirror.addExtent(vp.getMetadataMirrorFileLoc(), 0, image.env.LBS);
	    ext_bitmap.addExtent(vp.getMetadataBitmapFileLoc(), 0, image.env.LBS);

	    String l1 = UDF_Label.genExtentLabel(ext_main);
	    String l2 = UDF_Label.genExtentLabel(ext_mirror);
	    String l3 = UDF_Label.genExtentLabel(ext_bitmap);
	
	    mpm.getMetadataFileLoc().setAttribute("ref", l1 + ".lbn");
	    mpm.getMetadataMirrorFileLoc().setAttribute("ref", l2 + ".lbn");
	    mpm.getMetadataBitmapFileLoc().setAttribute("ref", l3 + ".lbn");

	    //image.env.metaDataPartMap = mpm;
	}
	else
	    throw new UDF_PartMapException(null, "Unkown partition map:" + id);

	return pm;
    }
}
