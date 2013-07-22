/*
*/
package com.udfv.udf102;

import java.io.*;
import java.util.*;
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.ecma167.*;
import com.udfv.access.*;
import com.udfv.exception.*;

/**
Implementation Use Extended Attributes を表現するクラス。

*/
public class UDF_attr2048 extends com.udfv.ecma167.UDF_attr2048
{
    /**
      コンストラクタ

      @param elem 親
      @param prefix ネームスコープ
      @param name 名前
    */
    public UDF_attr2048(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
    }

    /**
      XMLの整合性を取る。

      @param type 整合性を取る内容
      @param f アクセサ

    */
    public void recalc(short type, UDF_RandomAccess f) {

        if(type == RECALC_CRC){

	    //getParent().getParent().debug(0);
            //　チェックサム計算を行います　//
            com.udfv.ecma167.UDF_uint16 ui16 = (UDF_uint16) createElement("UDF_uint16", null, null);
	    ui16.setValue(headerChecksum(getBytes()));


            UDF_RandomAccessBytes rab = getImplUse().genRandomAccessBytes();
            try {
                ui16.writeTo(rab);
            }
            catch(UDF_Exception e) {
                ignoreMsg("udf102.UDF_attr2048#recalc", e);
            }
            catch(IOException e) {
                ignoreMsg("udf102.UDF_attr2048#recalc", e);
            }
            com.udfv.ecma167.UDF_bytes b = (UDF_bytes) createElement("UDF_bytes", "", "impl-use");
            b.setData(rab.getBuffer());
            setImplUse(b);
        }

        super.recalc(type, f);

	int pn = getImplUse().getElemPartRefNo();
	int sn = getImplUse().getPartSubno();
	/*
	System.err.println("type"+type);
	System.err.println("a1 pn"+pn);
	System.err.println("a1 sn"+sn);
	if(pn == 0 && sn == 1){
	    getParent().getParent().debug(0);
	    int z = 1/0;
	}
	*/

    }

    /**
      Implementation Use Exteneded Attributes の検証。
    */
    public UDF_ErrorList verify() throws UDF_Exception{

        if(env.udf_revision != 0x102) {
            return super.verify();
        }

        UDF_ErrorList el = new UDF_ErrorList();

        int size = getSize();
        if (size > 49 && getImplUseLen().getIntValue() > 1) {

            //　チェックサム計算を行います　//
            byte [] bin = getBytes();

            int checksum = headerChecksum(bin);
            int recorded = UDF_Util.b2uint16(bin, 48);
            if (checksum != recorded) {

                el.addError(
                    new UDF_Error(
                        UDF_Error.C_UDF102, UDF_Error.L_ERROR, "Header Checksum",
                        "The structures defined in the following sections contain a header checksum field."
                      + "This field represents a 16-bit checksum of the Implementation Use Extended Attribute header."
                      + "The fields AttributeType through ImplementationIdentifier inclusively represent the data covered by the checksum.",
                        "3.3.4.5",
                        recorded,
                        checksum
                    )
                );
            }

            el.setRName("Implementation Use Extended Attribute");
        }

        el.addError(super.verify());

        return el;
    }
}
