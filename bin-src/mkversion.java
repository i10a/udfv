import java.io.*;
import java.lang.*;

public class mkversion {

    public static void main(String [] argv) {

        String version = "0.90(Beta)";
        String time = new java.sql.Timestamp(System.currentTimeMillis()).toString();

        System.out.print(

   "package com.udfv.util;\n"
 + "\n"
 + "import java.io.*;\n"
 + "import java.lang.*;\n"
 + "\n"
 + "/**\n"
 + "Class to express version.\n"
 + "\n"
 + "*/\n"
 + "public class UDF_Version {\n"
 + "\n"
 + "    private UDF_Version( ) {\n"
 + "    }\n"
 + "\n"
 + "    public static final String building_date = \"" + time + "\";\n"
 + "\n"
 + "    public static String getBuildingDate( ) {\n"
 + "      return building_date;\n"
 + "    }\n"
 + "\n"
 + "    public static String getVerifierVersion( ) {\n"
 + "\n"
 + "        return \"" + version + "\";\n"
 + "    }\n"
 + "}\n"

        );
    }
}

