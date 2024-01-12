package ru.hldspm;

import ru.hldspm.file_access_server.misc.RepoStructureOrganizer;
import ru.hldspm.file_access_server.server.FileAccessServer;

public class Main {
    public static void main(String[] args){
        String baseDir =  System.getProperty("user.dir") +  "/files/";
        int port = 5610;
        RepoStructureOrganizer.createRepoFileStructure(baseDir);
        FileAccessServer.startFileServer(baseDir, port);
    }
}