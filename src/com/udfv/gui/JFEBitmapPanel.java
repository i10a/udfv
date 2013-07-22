/*

*/
package com.udfv.gui;

import com.udfv.core.UDF_Extent;
import com.udfv.core.UDF_ExtentElem;
import com.udfv.core.UDF_Image;
import com.udfv.ecma167.UDF_FEDesc;
import com.udfv.exception.*;

/**
  FEが持つADが示す領域を線描画し、ビット情報を描いたJPanel。
*/
public class JFEBitmapPanel extends com.udfv.gui.JSpaceBitmapPanel {

    UDF_FEDesc fe;

    /**
      コンストラクタ。

      @param width  コンポーネントの幅。
      @param height コンポーネントの高さ。
      @param cs ビット配色。※FEの領域の描画にはJBitmapColorSet.USER_0に対応する色が使用される。
    */
    public JFEBitmapPanel(UDF_FEDesc fe, int width, int height, com.udfv.gui.JBitmapColorSet cs) {
        super((UDF_Image)fe.env.root, width, height, cs);
        this.fe = fe;
    }

    //　UDF_FEDescが持つ領域情報に準じて矩形を描画する　//
    public void drawSpaceBitmap( ) {

        super.drawSpaceBitmap( );

        java.awt.Graphics gr = db[idb].getGraphics();
        gr.setColor(cols[JBitmapColorSet.USER_0]);

        int flags = fe.getICBFlags() & 0x07;
        if (flags == com.udfv.ecma167.UDF_icbtag.DIRECT){
            int loc = (int)(fe.getGlobalPoint() / image.env.LBS);
            drawSpaceBit(gr, loc, 1);
            return;
        }

        UDF_Extent ext = (UDF_Extent) image.createElement("UDF_Extent", "", "UDF_Extent");
        fe.setADToExtent(ext, 0);
        UDF_ExtentElem [] elem = ext.getExtent();

	try{
	    /*
	    if(false){
		UDF_Extent[] part_e = new UDF_Extent[image.env.getPartMapList().size()];
		for(int i=0 ; i<image.env.getPartMapList().size() ; ++i){
		    part_e[i] = image.env.getPartMapExtent(i, 0);
		}
		
		for (int i = 0, max = elem.length; i < max; i++) {
		    UDF_Extent low_ext = (UDF_Extent) image.createElement("UDF_Extent", "", "UDF_Extent");
		    image.convertAndAddExtent(low_ext, part_e, elem[i].partno, elem[i].loc, elem[i].len);
		    UDF_ExtentElem [] low_elem = low_ext.getExtent();
		    for (int j = 0, jmax = low_elem.length; j < jmax; j++) {
			drawSpaceBit(gr, low_elem[j].loc, (int)(low_elem[j].len / image.env.LBS));
		    }
		}
	    }
	    else*/{
		//System.err.println("BASE");
		//image.env.getPartMapExtent(0, 0).debug(0);
		//System.err.println("PRE");
		//ext.debug(0);
		UDF_Extent low_ext = ext;
		low_ext.blessExtent();//祝福
		//System.err.println("POST");
		//low_ext.debug(0);
		UDF_ExtentElem [] low_elem = low_ext.getExtent();
		for (int j = 0, jmax = low_elem.length; j < jmax; j++) {
		    drawSpaceBit(gr, low_elem[j].getLoc(), (int)(low_elem[j].getLen() / image.env.LBS));
		}
	    }
	}
	catch(UDF_PartMapException e){
	    e.printStackTrace();
	}
	catch(UDF_InternalException e){
	    e.printStackTrace();
	}

        return;
    }
}

