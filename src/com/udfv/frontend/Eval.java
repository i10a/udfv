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
package com.udfv.frontend;

import java.io.*;
import java.util.*;
import java.util.zip.*;
import java.lang.reflect.*;
import org.w3c.dom.*;
import com.udfv.core.*;
import com.udfv.util.*;
import com.udfv.access.*;
import com.udfv.udfapi.*;
import com.udfv.ecma167.*;

/**
   簡単な評価をする。評価は longで行う。

   input: <exp>
   exp: <term> '+' <exp> | <term> '-' <exp> | <term>
   term: <atom> '*' <term> | <atom>
   atom: '(' <exp> ')' | <num> | <val>
   num: 0x <digits> | <digits> [GKM]
   val: '${' <alnum> '}'

   valは Frontendの Public field、および、
   UDF_ElementBaseの Public fieldを指定できる。
 */
class Eval {
    int pos;
    Frontend front;

    long evalConst(Class cls, Object o , String s) throws NoSuchFieldException {
	try{
	    //System.err.println(s);
	    java.lang.reflect.Field f = cls.getField(s);
	    return f.getInt(o);
	}
	catch(Exception e){
	    throw new NoSuchFieldException();
	}
    }

    /**
       <val>

       ${name}

       1) Frontend中にある public fieldを検索する
       2) UDF_ElementBase中にある public fieldを検索する
     */
    long val(String s) throws EvalException{
	//System.err.println("val:"+ pos);
	int saved_pos = pos;
	try{
	    int pos0 = pos;
	    int pos1 = pos;
	    if(s.charAt(pos) == '$' && s.charAt(pos+1) == '{'){
		pos += 2;
		pos0 = pos;

		while(s.charAt(pos) != '}' && pos < s.length()){
		    ++pos;
		    pos1 = pos;
		}
		String val = s.substring(pos0, pos1);
		try{
		    return evalConst(Frontend.class, front, val);
		}
		catch(NoSuchFieldException e){
		    ;
		}
		try{
		    return evalConst(UDF_ElementBase.class, null, val);
		}
		catch(NoSuchFieldException e){
		    ;
		}
	    }
	}
	catch(Exception e){
	    ;
	}
	pos = saved_pos;
	throw new EvalException();
    }
    /**
       <num>
     */
    long num(String s) throws EvalException{
	int saved_pos = pos;
	try{
	    int pos0 = pos;
	    int pos1 = pos;
	    int length = s.length();
	    if(length >= 2 && s.charAt(pos) == '0' && s.charAt(pos+1) == 'x'){
		pos += 2;
		pos0 = pos;
		while(pos < s.length() &&
		      ((s.charAt(pos) >= '0' && s.charAt(pos) <= '9')||
		       (s.charAt(pos) >= 'a' && s.charAt(pos) <= 'f')||
		       (s.charAt(pos) >= 'A' && s.charAt(pos) <= 'F')))
		    ++pos;
		pos1 = pos;
		String v = s.substring(pos0, pos1);
		s = s.substring(pos1);
		return Long.parseLong(v, 16);
	    }
	    else{
		pos0 = pos;
		while(pos < s.length() && s.charAt(pos) >= '0' && s.charAt(pos) <= '9')
		    ++pos;
		pos1 = pos;
		long scale = 1;
		try{
		    if(s.charAt(pos) == 'K'){
			scale = 1024;
			++pos;
		    }
		    else if(s.charAt(pos) == 'M'){
			scale = 1024*1024;
			++pos;
		    }
		    else if(s.charAt(pos) == 'G'){
			scale = 1024*1024*1024;
			++pos;
		    }
		}
		catch(Exception e){
		}
		String v = s.substring(pos0, pos1);
		s = s.substring(pos1);
		return Long.parseLong(v) * scale;
	    }
	}
	catch(Exception e){
	    ;
	}
	pos = saved_pos;
	throw new EvalException();
    }
    /**
       <atom>:
       '(' <exp> ')' |
       <num> |
       <val>
    */
    long atom(String s) throws EvalException{
	//System.err.println("atom:" + pos);
	int saved_pos = pos;
	try{
	    if(s.charAt(pos) == '('){
		++pos;
		long v = exp(s);
		if(s.charAt(pos) == ')'){
		    ++pos;
		    return v;
		}
	    }
	}
	catch(EvalException e){
	    ;
	}
	pos = saved_pos;

	try{
	    return num(s);
	}
	catch(EvalException e){
	    ;
	}
	pos = saved_pos;

	try{
	    return val(s);
	}
	catch(EvalException e){
	    ;
	}
	pos = saved_pos;
	throw new EvalException();
    }
    /**
      <term>:
       <atom> * <term> |
       <atom>
     */
    long term(String s) throws EvalException{
	int saved_pos = pos;
	try{
	    long a1 = atom(s);
	    int op = s.charAt(pos);
	    if(op != '*' &&  op != '/')
		throw new EvalException();
	    ++pos;
	    long t1 = term(s);
	    if(op == '*')
		return a1 * t1;
	    else
		return a1 / t1;
	}
	catch(Exception e){
	    ;
	}
	pos = saved_pos;

	try{
	    long a1 = atom(s);
	    return a1;
	}
	catch(Exception e){
	    ;
	}
	pos = saved_pos;

	throw new EvalException();
    }
    /**
       <exp>:
         <term> + <exp> |
         <term> - <exp> |
	 <term>
     */
    long exp(String s) throws EvalException{
	int saved_pos = pos;
	try{
	    long t1 = term(s);
	    if(pos == s.length())
		return t1;
	    int op = s.charAt(pos);
	    if(op != '+' &&  op != '-')
		return t1;

	    ++pos;
	    long e1 = exp(s);
	    if(op == '+')
		return t1 + e1;
	    else
		return t1 - e1;
	}
	catch(Exception e){
	}
	pos = saved_pos;

	throw new EvalException();
    }
    /**
       s = <exp>
     */
    public long eval(String s) throws EvalException{
	try{
	    pos = 0;
	    return exp(s);
	}
	catch(EvalException e){
	    //e.printStackTrace();
	    //System.err.println(pos);
	    throw new EvalException(e);
	}
    }

    Eval(Frontend f){
	pos = 0;
	front = f;
    }

    static public void main(String args[]) throws Exception{
	Eval eval = new Eval(null);
	System.err.println(eval.eval(args[0]));

    }
}
