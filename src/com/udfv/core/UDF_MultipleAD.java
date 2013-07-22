/*
*/
package com.udfv.core;

import java.io.*;
import java.util.*;
import org.w3c.dom.*;
import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.ecma167.*;
import com.udfv.access.*;
import com.udfv.exception.*;
import com.udfv.udf201.*;

/**
   
*/
public interface UDF_MultipleAD
{
    /**
       繰り返し数を求める。
     */
    abstract public int getTimes();
    /**
       N番目のADのLBN値を求める。
     */
    abstract public int getLoc(int index);
}
