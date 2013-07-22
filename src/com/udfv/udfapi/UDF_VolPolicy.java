package com.udfv.udfapi;

import java.lang.*;
import java.io.*;
import com.udfv.ecma167.*;
import com.udfv.exception.*;
import com.udfv.core.*;


/**
   Volume(主としてVDS)に関するポリシー

*/
public interface UDF_VolPolicy {
    /**
       Volumeを作る

       createAnchor() ->  createVDS() -> createLVIS() ->
       createFSDS() -> を順番に呼び出す
    */
    public void createVolume(UDF_Image image, UDF_Policy policy) throws IOException,UDF_Exception;

    /**
       ROOT Directory を作る
     */
    public void createRootDir(UDF_Image image, UDF_Policy policy, int lbn, int partno) throws UDF_Exception, IOException;

    /**
       ROOT Stream Directory を作る
     */
    public void createRootSDir(UDF_Image image, UDF_Policy policy, int lbn, int partno) throws UDF_Exception, IOException;

    /**
       FSDSを作る
     */
    public void createFSDS(UDF_Image image, UDF_Policy policy, String tagname, int lbn, int partno, long len) throws UDF_Exception, IOException;

    /**
       VDS を作る

       @param image	イメージ
       @param policy	ポリシー
       @param tagname
       @param sec
       @param len
    */
    public UDF_VDS createVDS(UDF_Image image, UDF_Policy policy, String tagname, int sec, long len, boolean reserved) throws UDF_Exception, IOException;

    /**
       IS を作る

       @param image	イメージ
       @param policy	ポリシー
       @param sec
    */
    public UDF_IS createLVIS(UDF_Image image, UDF_Policy policy, int sec) throws IOException,UDF_Exception;

    /**
       Anchor を作る

       @param image	イメージ
       @param policy	ポリシー
       @param sec
    */
    public UDF_Extent createAnchor(UDF_Image image, UDF_Policy policy, int sec) throws UDF_Exception, IOException;

    public int getRevision();

    //
    //Volmue作成パラメータ
    //
    public int getPartLen();//{return 2297344;};
    public int getUSBLen();//{return 288768;};
    public int getUSBPos();//{return 2;};
    public int getFSDSLbn();//{return 0;};
    public int getFSDSPartno();//{return 1;};
    public long getFSDSLen();//{return 4096;};
    public int getRootLbn();//{return 16;};
    public int getRootPartno();//{return 1;};
    public int getSRootLbn();//{return 18;};
    public int getSRootPartno();//{return 1;};
    public String[] getPartMapId();//{return new String[];//{"", "*UDF Metadata Partition"};};


    public int getVolSeqNumber();//{return 1;}
    public int getMaxVolSeqNumber();//{return 1;}
    public int getInterchangeLevel();//{return 2;}
    public String getVolId();//{return "8:HEART UDF VOLUME";}
    public String getVolSetId();//{return "8:2C6428C300000000TEST_IMAGE";}

    public int getMVDSLoc();//{return 32;}
    public int getMVDSLen();//{return 32768;}
    public int getRVDSLoc();//{return 64;}
    public int getRVDSLen();//{return 32768;}

    public String getLVInfo1();//{return "8:OWNER NAME";}
    public String getLVInfo2();//{return "8:Heart Solutions, Inc.";}
    public String getLVInfo3();//{return "8:CONTACT INFORMATION";}

    public int getAccessType();//{return 4;};
    public int getPartNumber();//{return 0;};
    public int getPartStartingLoc();//{return 272;};
    public String getNSR();//{return "+NSR03";};
    public String getLogicalVolId();//{return "8:PSX Volume";};
    public int getLogicalBlockSize();//{return 2048;};

    public int getLVISLoc();//{return 96;};
    public int getLVISLen();//{return 8192;};
    public int getFSDSLoc();//{return 0;};
    public String getFileSetId();

    //
    //PartMap 作成パラメータ
    //

    public int getSparablePacketLen();//{return 16;};
    public int getSparableSizeOfEachTable();//{return 6144;};
    public int getSparableReallocationTableLen();//{return 512;};
    public int[] getSparableLocOfSparingTables();//{return ;//{288, 8544};};
    public int[] getSparableMapEntry();//{return };
    public int getMetadataFileLoc();//{return 0;};
    public int getMetadataMirrorFileLoc();//{return 2296319;};
    public int getMetadataBitmapFileLoc();//{return 1;};
    public int getMetadataAllocUnitSize();//{return 32;};
    public int getMetadataAlignUnitSize();//{return 16;};
    public int getMetadataFlags();//{return 1;};
    public UDF_ExtentElem[] getMetadataFileExtent();
    public UDF_ExtentElem[] getMetadataMirrorFileExtent();
}
