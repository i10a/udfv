
package com.udfv.udfapi;

import java.lang.*;
import java.io.*;
import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.ecma167.*;
import com.udfv.exception.*;
import com.udfv.encoding.UDF_Encoding;

/**
   File Identifer Descriptor を作成する。
*/
public class UDF_FIDPolicyImpl implements UDF_FIDPolicy {

    /**
       File Identifer Descriptor を作成する。

       descriptor tagは policyを参考に生成される。
       filenameは policyを参考にエンコードされる。
       

       @param image	UDF_Image
       @param policy	ポリシー
       @param filename	ファイル名
    */
    public UDF_desc257 create(UDF_Image image, UDF_Policy policy, String filename) throws UDF_Exception, IOException{

        com.udfv.ecma167.UDF_desc257 fid = (UDF_desc257) image.createElement("UDF_desc257", null, null);
        fid.setDefaultValue();

        fid.getFileVersionNumber().setValue(1);

        fid.getFileChar().setValue(0);

        UDF_Encoding enc = policy.getEncodingPolicy().create(image, policy, filename);
        fid.getFileId().setEncoding(enc);
        fid.getFileId().setData(enc.toBytes(filename));
        int len = fid.getFileId().getSize();
        fid.getLenOfFileId().setValue(len);

        com.udfv.ecma167.UDF_long_ad icb = fid.getICB();
        icb.setPartRefNo(0);
        icb.setLbn(0);
        icb.setLen(0);
	//追加 (by issei)
	UDF_Element adimpuse = image.createElement("UDF_long_ad_ADImpUse", null, null);
	adimpuse.setDefaultValue();
	icb.getImplUse().replaceChild(adimpuse);


        fid.getLenOfImplUse().setValue(0);

        UDF_pad pad = fid.getPadding();
        pad.setSize(0);
        len = fid.getSize();
        int align = (int)pad.getAlign();

        //4でちょうど割きれるときに4バイトpadが入ってしまう。
        //pad.setSize(align - (len % align));
        pad.setSize(UDF_Util.align(len, align) - len);

        UDF_tag descTag = policy.getDescTagPolicy().create(image, policy);
        descTag.getTagId().setValue(257);
        //descTag.getTagLoc().setValue(lbn);
        descTag.getDescCRCLen().setValue(fid.getSize() - descTag.getSize());
        fid.setDescTag(descTag);

//        fid.recalcCRC();

        return fid;
    }
}
