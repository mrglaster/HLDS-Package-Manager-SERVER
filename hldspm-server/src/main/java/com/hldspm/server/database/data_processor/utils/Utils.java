package com.hldspm.server.database.data_processor.utils;

public class Utils {
    /**Wraps string with single quotes*/
    public static String wrapWithQuotes(Object obj){
        return "'" + obj.toString() + "'";
    }
}
