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
package com.udfv.core;

public interface UDF_Numeric extends Comparable
{
    /**
       vを足す

       @param v

       @return 足した結果の値
     */
    long addValue(int v);
    /**
       vを足す

       @param v

       @return 足した結果の値
     */
    long addValue(long v);

    /**
       比較する
     */
    int compareTo(Object obj);
}