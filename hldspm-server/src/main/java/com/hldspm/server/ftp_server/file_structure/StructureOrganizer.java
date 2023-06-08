package com.hldspm.server.ftp_server.file_structure;
import com.hldspm.server.ftp_server.cfg.FtpConstants;
import com.hldspm.server.io.io;

import java.io.File;



// TODO Переписать модуль к чертовой матери

public class StructureOrganizer {
    private static String[] mods = {"cs", "cscz", "dod", "hl", "op4", "universal"};


    /**Checks if the file structure is valid*/
    public static boolean checkFilesStructure() {
        String rootPath = FtpConstants.getFtpPath();
        String[] rootFolders = {
                "source",
                "goldsource"
        };

        String[] goldsourceSubfolders = {
                "maps",
                "plugins",
                "modules"
        };

        String[] moduleSubfolders = {
                "lin",
                "win"
        };

        String[] gameFolders = {
                "cs",
                "cscz",
                "dod",
                "hl",
                "op4",
                "universal"
        };

        File rootFolder = new File(rootPath);
        if (!rootFolder.isDirectory()) {
            return false;
        }

        if (!checkSubfolders(rootFolder, rootFolders)) {
            return false;
        }

        File goldsourceFolder = new File(rootFolder, "goldsource");
        if (!checkSubfolders(goldsourceFolder, goldsourceSubfolders)) {
            return false;
        }

        File modulesFolder = new File(goldsourceFolder, "modules");
        if (!checkSubfolders(modulesFolder, moduleSubfolders)) {
            return false;
        }

        File linFolder = new File(modulesFolder, "lin");
        File winFolder = new File(modulesFolder, "win");
        return checkSubfolders(linFolder, gameFolders)
                && checkSubfolders(winFolder, gameFolders);
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
        File rootFolder = new File(rootPath);

        // Создание корневых папок
        File sourceFolder = new File(rootFolder, "source");
        sourceFolder.mkdirs();

        File goldSourceFolder = new File(rootFolder, "goldsource");
        goldSourceFolder.mkdirs();

        // Создание папки maps в папке goldsource
        File mapsFolder = new File(goldSourceFolder, "maps");
        mapsFolder.mkdirs();

        // Создание папки plugins в папке goldsource
        File pluginsFolder = new File(goldSourceFolder, "plugins");
        pluginsFolder.mkdirs();

        // Создание папки modules в папке goldsource
        File modulesFolder = new File(goldSourceFolder, "modules");
        modulesFolder.mkdirs();

        // Создание папок внутри goldsource/maps
        createSubfolders(mapsFolder);

        // Создание папок внутри goldsource/plugins
        createSubfolders(pluginsFolder);

        // Создание папок внутри goldsource/modules
        createSubfolders(modulesFolder);
    }

    public static void createSubfolders(File parentFolder) {
        for (String subfolder : mods) {
            File subfolderFile = new File(parentFolder, subfolder);
            subfolderFile.mkdirs();
        }
    }

    public static void initFileSystem(){
        io.customPrint("Checking the FTP-server file structure");
        if (!StructureOrganizer.checkFilesStructure()){
            io.customPrint("File structure is invalid!");
            io.customPrint("Creating the file system");
            StructureOrganizer.createFtpFilesystem("files");
            io.customPrint("File system creation is done!");
        }
        io.customPrint("File system is valid");
    }


}
