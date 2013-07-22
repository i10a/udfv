/*
*/
package com.udfv.udf150;

import java.io.*;
import java.util.*;
import org.w3c.dom.*;
import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.encoding.*;
import com.udfv.access.*;
import com.udfv.exception.*;
import com.udfv.ecma167.*;

/**
*/
public interface UDF_VirtualAllocTable
{
    /**
	vATEntryを取得する。

	@return 取得したvATEntry を返す。
    */
    public UDF_bytes getVATEntry();
    /**
	previousVATICBLocationを取得する。

	@return 取得したpreviousVATICBLocation を返す。
    */
    public UDF_uint32 getPreviousVATICBLocation();

    /**
	vATEntryを設定する。

	@param	v 設定する値。
    */
    public void setVATEntry(UDF_bytes v);
    /**
	previousVATICBLocationを設定する。

	@param	v 設定する値。
    */
    public void setPreviousVATICBLocation(UDF_uint32 v);

};
