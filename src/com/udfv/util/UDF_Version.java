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
package com.udfv.util;

import java.io.*;
import java.lang.*;

/**
Class to express version.

*/
public class UDF_Version {

    private UDF_Version( ) {
    }

    public static final String building_date = "2012-03-07 15:54:24.834";

    public static String getBuildingDate( ) {
      return building_date;
    }

    public static String getVerifierVersion( ) {

        return "0.90(Beta)";
    }
}
