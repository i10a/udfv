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
