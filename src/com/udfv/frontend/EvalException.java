/*

*/
package com.udfv.frontend;

import java.io.*;
import java.util.*;
import java.util.zip.*;

class EvalException extends Exception {
    public EvalException(Exception e){
	super(e);
    }
    public EvalException(){
	;
    }
};
