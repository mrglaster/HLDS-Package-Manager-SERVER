package ru.hldspm;

import ru.hldspm.file_access_server.server.FileAccessServer;

public class Main {
    public static void main(String[] args){
        String baseDir = "files/";
        int port = 2456;
        FileAccessServer.startFileServer(baseDir, port);
    }
}