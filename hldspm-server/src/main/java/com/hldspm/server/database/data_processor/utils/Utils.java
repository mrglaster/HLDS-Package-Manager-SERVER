package com.hldspm.server.database.data_processor.utils;

public class Utils {
    public static String wrapWithQuotes(Object obj){
        return "'" + obj.toString() + "'";
    }
}
