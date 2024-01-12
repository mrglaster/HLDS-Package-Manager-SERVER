package ru.hldspm.file_access_server.misc;


import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**Creates folders structure for the repo*/
public class RepoStructureOrganizer {
    public static void createRepoFileStructure(String serverFilesPath){
        String[] rootDirectories = {"gold_universal", "valve", "cstrike", "czero", "dod", "tfc", "ts"};
        try {
            for (String rootDir : rootDirectories) {
                Path rootPath = Paths.get(serverFilesPath, rootDir);
                Files.createDirectories(rootPath);


                // Metamod modules
                Files.createDirectories(rootPath.resolve("mmmodules/linux"));
                Files.createDirectories(rootPath.resolve("mmmodules/windows"));
                Files.createDirectories(rootPath.resolve("mmmodules/mac"));

                //Amx mod x modules
                Files.createDirectories(rootPath.resolve("amxmodules/linux"));
                Files.createDirectories(rootPath.resolve("amxmodules/windows"));
                Files.createDirectories(rootPath.resolve("amxmodules/mac"));

                // Maps
                Files.createDirectories(rootPath.resolve("maps"));

                // Amx Mod X Plugins
                Files.createDirectories(rootPath.resolve("amxplugins"));

                // Angel Script plugins
                Files.createDirectories(rootPath.resolve("asplugins"));

                // Lambda Mod Plugins
                Files.createDirectories(rootPath.resolve("lmplugins"));

                // Source Mod Plugins
                Files.createDirectories(rootPath.resolve("smplugins"));



            }

            System.out.println("[HLDS:PM] Folders structure initialized successfully.");
        } catch (FileAlreadyExistsException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
