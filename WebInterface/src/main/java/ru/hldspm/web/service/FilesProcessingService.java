package ru.hldspm.web.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hldspm.web.entities.ContentType;
import ru.hldspm.web.entities.Game;
import ru.hldspm.web.entities.Platform;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@Service
public class FilesProcessingService {
    public static void saveUploadedContent(MultipartFile contentArchive, Game game, Platform platform, ContentType contentType, String contentName, String contentVersion){
        if (contentArchive != null && !contentArchive.isEmpty()) {
            String fileName =  contentName + ":" + contentVersion + ".zip";
            String uploadDir = "";
            if (Objects.equals(platform.getName(), "all")){
                //TODO Replace to value from cfg file
                uploadDir =  "/home/mrglaster/Desktop/hlds-pm-s/HLDS-Package-Manager-SERVER/files/" + game.getName() + '/' + contentType.getName() + "/";
                try {
                    Files.createDirectories(Path.of(uploadDir));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            String filePath = uploadDir + fileName;
            try {
                contentArchive.transferTo(new File(filePath));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("File saved at: " + filePath);
        }
    }
}
