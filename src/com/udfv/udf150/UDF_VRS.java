/*
 */
package com.udfv.udf150;

import java.io.*;
import java.util.*;
import org.w3c.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xerces.dom.*;
import com.udfv.exception.*;
import com.udfv.access.*;
import com.udfv.core.*;
import com.udfv.util.*;

public class UDF_VRS extends com.udfv.udf102.UDF_VRS
{
    public UDF_VRS(UDF_Element elem, String prefix, String name){
	super(elem, prefix, name == null ? "UDF_VRS" : name);
    }
}


