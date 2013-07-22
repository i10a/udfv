/*
 */
package com.udfv.udf150;

import java.io.*;
import java.util.*;
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
import com.udfv.access.*;
import com.udfv.exception.*;
import com.udfv.core.*;
import com.udfv.util.*;

/**
   UDF_SDirectory は、Stream Directory を表現するクラスである。
*/
public class UDF_SDirectory extends com.udfv.udf102.UDF_SDirectory
{
    public UDF_SDirectory(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? getUDFClassName() : name);
    }
}


