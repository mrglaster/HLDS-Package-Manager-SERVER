package com.hldspm.server.io.custom_print;

/**Class providing methods for custom output/logging*/
public class io {
    private final static String UTIL_PREFIX = "[HLDS:PM] ";

    /**Prints something into the console with some prefix*/
    public static void customPrint(Object message){
        System.out.println(UTIL_PREFIX + message.toString());
    }
}
