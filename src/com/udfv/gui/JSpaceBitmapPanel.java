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
package com.udfv.gui;

import com.udfv.core.UDF_Element;
import com.udfv.core.UDF_Image;
import com.udfv.core.UDF_Extent;
import com.udfv.core.UDF_ExtentElem;
import com.udfv.exception.*;

/**
  UDF_Image が持つビットマップ情報を描画したJPanel。
*/
public class JSpaceBitmapPanel extends JBitmapPanel {

    //　カラー情報　//
    protected java.awt.Color [] cols;

    //　UDF ファイルシステム　//
    protected UDF_Image image;

    /**
      コンストラクタ。

      @param width  コンポーネントの幅。
      @param height コンポーネントの高さ。
      @param cols ビット配色用Color配列。

      <em>
      4096以上の横幅が指定されたときは4096に丸める。
      </em>
    */
    public JSpaceBitmapPanel(com.udfv.core.UDF_Image image, int width, int height, com.udfv.gui.JBitmapColorSet cs) {

        this.cols = cs.getColor();
        this.image = image;
        this.total = (int)(image.getLongSize() / image.env.LBS);

        if (width > 4096) {
            width = 4096;
        }

        this.w = width;
        this.h = height;
        offset = 8;
        wmax = width - offset * 2;

        packSize();

        db = new java.awt.Image[2];
        idb = 0;

        init = true;
    }

    //　指定の領域情報に合わせて矩形をビットマップの使用状況を示す描画する　//
    public void drawSpaceBit(java.awt.Graphics gr, UDF_ExtentElem extelem, java.awt.Color col) {
	/*
	try{
	    UDF_Extent[] part_e = new UDF_Extent[image.env.getPartMapList().size()];
	    for(int i=0 ; i<image.env.getPartMapList().size() ; ++i){
		part_e[i] = image.env.getPartMapExtent(i, 0);
	    }

	    UDF_Extent low_ext = (UDF_Extent) image.createElement("UDF_Extent", "", "UDF_Extent");
	    image.convertAndAddExtent(low_ext, part_e, extelem.partno, extelem.loc, extelem.len);
	    
	    gr.setColor(col);
	    
	    UDF_ExtentElem [] low_elem = low_ext.getExtent();
	    for (int i = 0, max = low_elem.length; i < max; i++) {
		drawSpaceBit(gr, low_elem[i].loc, (int)(low_elem[i].len / image.env.LBS));
	    }
	}
	catch(UDF_PartMapException e){
	    e.printStackTrace();
	}
	*/

	gr.setColor(col);
	drawSpaceBit(gr, extelem.getLoc(), (int)(extelem.getLen() / image.env.LBS));

        return;
    }

    //　UDF_Image が持つ領域情報に準じて矩形を描画する　//
    public void drawSpaceBitmap( ) {

        java.awt.Graphics gr = db[idb].getGraphics();
        gr.setColor(java.awt.Color.white);
        gr.fillRect(0, 0, w, h);
        gr.setColor(cols[JBitmapColorSet.FREE]);
        gr.fillRect(offset, 0, wmax, h);

        UDF_Element [] elem = image.env.root.getChildren();

        for (int idx = 0, max = elem.length; idx < max; idx++) {

            String name = elem[idx].getName();
            if (name.equals("system")) {
                continue;
            }
            if (name.equals("partition")) {
                continue;
            }

            UDF_Extent ext = (UDF_Extent) elem[idx];

            UDF_ExtentElem [] extelem = ext.getExtent();
            for (int i = 0, imax = extelem.length; i < imax; i++) {

                int cidx = extelem[i].getPartRefNo() + 2;
                if (cidx > 1) {
                    cidx++;
                }

		UDF_Extent blessed_ext = (UDF_Extent) ext.createElement("UDF_Extent", null, null);
		blessed_ext.addExtent(extelem[i]);

		try{
		    blessed_ext.blessExtent();
		}
		catch(UDF_PartMapException e){
		    e.printStackTrace();
		}
		catch(UDF_InternalException e){
		    e.printStackTrace();
		}
		UDF_ExtentElem[] ee = blessed_ext.getExtent();

		for(int j=0 ; j<ee.length ; ++j)
		    drawSpaceBit(gr, ee[j], cols[cidx]);
            }
        }

	int npartmap = 0;
	try{
	    npartmap =  image.env.getPartMapList().size();
	}
	catch(UDF_PartMapException e){
	    e.printStackTrace();
	}
        /*
          Metadata File と Metadata Mirror File の領域を使用済みにする。
          ※XML ではこの領域がUDF_Extentとして現れない。
        */
        for(int i = 1; i < npartmap ; i++) {

            if (image.env.part[i] == null) {
                continue;
            }

            int pt = image.env.part[i].getPartMapType().getIntValue();
            if (pt != 2) {
                continue;
            }

            if (!image.env.part[i].getName().equals("UDF_MetadataPartMap")) {
                continue;
            }

            //　Metadata File(or Mirror)を取り出して　//
            com.udfv.ecma167.UDF_desc261  fe = null;
            com.udfv.ecma167.UDF_desc261 mfe = null;
            try {
                fe = image.findMetadataFile(261, com.udfv.udf250.UDF_icbtag.T_METADATA_FILE);
            }
            catch(UDF_InternalException e) {
                try {
                    fe = image.findMetadataFile(266, com.udfv.udf250.UDF_icbtag.T_METADATA_FILE);
                }
                catch(UDF_InternalException ee) {
                }
            }

            try {
                mfe = image.findMetadataFile(261, com.udfv.udf250.UDF_icbtag.T_METADATA_MIRROR);
            }
            catch(UDF_InternalException e) {
                try {
                    mfe = image.findMetadataFile(266, com.udfv.udf250.UDF_icbtag.T_METADATA_MIRROR);
                }
                catch(UDF_InternalException ee) {
                }
            }

            if (fe == null && mfe == null) {
                image.debugMsg(0, "NOT FOUND Metadata File / Metadata Mirror File");
                continue;
            }

            UDF_Extent ext = (UDF_Extent) image.createElement("UDF_Extent", null, null);
            if (fe != null) {
                //※default_partnoの検証を忘れずに。
                fe.setADToExtent(ext, 0);
            }
            if (mfe != null) {
                mfe.setADToExtent(ext, 0);
            }

            UDF_ExtentElem [] extelem = ext.getExtent();
            for (int ii = 0, iimax = extelem.length; ii < iimax; ii++) {
                drawSpaceBit(gr, extelem[ii], cols[i + 3]);
            }
        }
    }
}


