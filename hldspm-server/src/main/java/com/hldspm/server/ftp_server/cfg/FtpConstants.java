package com.hldspm.server.ftp_server.cfg;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FtpConstants {

    public static final String FTP_LINK_INIT = "ftp://anonymous@localhost/";
    public static final int FTP_PORT = 21;
    public static final String ANON_USERNAME = "anonymous";
    public static final String LISTENER_TYPE = "default";

    /**Returns the absolute path of the app*/
    private static String getServerPath(){
        Path currentPath = Paths.get("");
        return currentPath.toAbsolutePath().toString();
    }

    /**Returns the path to the ftp-accessible directory*/
    public static String getFtpPath(){
        return getServerPath() + "\\" + "files";
    }
}
