/*
 */
package com.udfv.core;

import java.io.*;
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.access.*;
import com.udfv.exception.*;
import com.udfv.ecma167.*;

/**
   ICBを持つクラスのインターフェース
 */
public interface UDF_ICBDesc{
    public UDF_icbtag getICBTag();
    public void setICBTag(UDF_icbtag icb);
    public int getICBFileType();
    public int getICBFlags();
}
