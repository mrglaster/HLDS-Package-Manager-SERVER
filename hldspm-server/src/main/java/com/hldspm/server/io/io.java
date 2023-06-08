package com.hldspm.server.io;

public class io {
    private final static String UTIL_PREFIX = "[HLDS:PM] ";

    /**Prints something into the console with some prefix*/
    public static void customPrint(Object message){
        System.out.println(UTIL_PREFIX + message.toString());
    }
}
