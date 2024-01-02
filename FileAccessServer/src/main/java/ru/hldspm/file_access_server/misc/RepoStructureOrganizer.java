package ru.hldspm.file_access_server.misc;


import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**Creates folders structure for the repo*/
public class RepoStructureOrganizer {
    public static void createRepoFileStructure(String serverFilesPath){
        String[] rootDirectories = {"valve", "cstrike", "czero", "dod"};
        try {
            for (String rootDir : rootDirectories) {
                Path rootPath = Paths.get(serverFilesPath, rootDir);
                Files.createDirectories(rootPath);

                // Create subdirectories
                Files.createDirectories(rootPath.resolve("modules/linux"));
                Files.createDirectories(rootPath.resolve("modules/windows"));
                Files.createDirectories(rootPath.resolve("maps"));
                Files.createDirectories(rootPath.resolve("plugins"));
                Files.createDirectories(rootPath.resolve("amxmodules"));
            }

            System.out.println("[HLDS:PM] Folders structure initialized successfully.");
        } catch (FileAlreadyExistsException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
