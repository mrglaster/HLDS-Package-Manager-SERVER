package com.hldspm.server.ftp_server.file_structure;
import com.hldspm.server.ftp_server.cfg.FtpConstants;
import com.hldspm.server.io.custom_print.io;

import java.io.File;

public class StructureOrganizer {
    private static final String[] mods = {"cs", "cscz", "dod", "hl", "op4", "universal"};


    /**Checks if the file structure is valid*/
    public static boolean checkFilesStructure() {
        String rootPath = FtpConstants.getFtpPath();
        String[] rootFolders = {
                "source",
                "gold"
        };

        String[] goldSourceSubfolders = {
                "maps",
                "plugins",
                "modules"
        };

        String[] moduleSubfolders = {
                "lin",
                "win"
        };


        File rootFolder = new File(rootPath);
        if (!rootFolder.isDirectory()) {
            return false;
        }

        if (!checkSubfolders(rootFolder, rootFolders)) {
            return false;
        }

        File goldsourceFolder = new File(rootFolder, "gold");
        if (!checkSubfolders(goldsourceFolder, goldSourceSubfolders)) {
            return false;
        }

        File modulesFolder = new File(goldsourceFolder, "modules");
        if (!checkSubfolders(modulesFolder, moduleSubfolders)) {
            return false;
        }

        File linFolder = new File(modulesFolder, moduleSubfolders[0]);
        File winFolder = new File(modulesFolder, moduleSubfolders[1]);

        return checkSubfolders(linFolder, mods)
                && checkSubfolders(winFolder, mods);
    }

    /**Checks subfolders in the root folders */
    private static boolean checkSubfolders(File folder, String[] expectedFolders) {
        File[] subfolders = folder.listFiles();
        if (subfolders == null) {
            return false;
        }

        for (File subfolder : subfolders) {
            if (!subfolder.isDirectory()) {
                return false;
            }

            String subfolderName = subfolder.getName();
            if (!containsExpectedFolder(subfolderName, expectedFolders)) {
                return false;
            }
        }

        return true;
    }

    /**Checks if the folder contains the required list of  folders*/
    private static boolean containsExpectedFolder(String folderName, String[] expectedFolders) {
        for (String expectedFolder : expectedFolders) {
            if (expectedFolder.equals(folderName)) {
                return true;
            }
        }
        return false;
    }

    public static void createFtpFilesystem(String rootPath) {
        if (!checkFilesStructure()) {
            File rootFolder = new File(rootPath);
            File sourceFolder = new File(rootFolder, "source");
            sourceFolder.mkdirs();
            File goldSourceFolder = new File(rootFolder, "gold");
            goldSourceFolder.mkdirs();
            File mapsFolder = new File(goldSourceFolder, "maps");
            mapsFolder.mkdirs();
            File pluginsFolder = new File(goldSourceFolder, "plugins");
            pluginsFolder.mkdirs();
            File modulesFolder = new File(goldSourceFolder, "modules");
            modulesFolder.mkdirs();
            createSubfolders(mapsFolder);
            createSubfolders(pluginsFolder);
            createSubfolders(modulesFolder);
        }
    }

    public static void createSubfolders(File parentFolder) {
        if (parentFolder.getName().equals("modules")){
            File subfolderFile = new File(parentFolder, "win");
            subfolderFile.mkdirs();
            File newer = new File(parentFolder, "lin");
            newer.mkdirs();
            createSubfolders(subfolderFile);
            createSubfolders(newer);
        } else {
            for (String subfolder : mods) {
                File subfolderFile = new File(parentFolder, subfolder);
                subfolderFile.mkdirs();
            }
        }
    }

    public static void initFileSystem(){
        io.customPrint("Checking the FTP-server file tree");
        StructureOrganizer.createFtpFilesystem("files");
    }


}
