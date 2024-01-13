package ru.hldspm.web.service;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hldspm.web.entities.Content;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@Service
public class FilesProcessingService {
    private static final String fileServerFolder = System.getProperty("user.dir") +  "/files/";
    public static void saveUploadedContent(MultipartFile contentArchive, Content savedContent, String contentVersion){
        if (contentArchive != null && !contentArchive.isEmpty()) {
            String fileName = "";

            String contentName = savedContent.getName();
            String platformName = savedContent.getPlatform().getName();
            String typeName = savedContent.getContentType().getName();

            if (savedContent.getContentType().getName().equals("maps")){
                fileName =  contentName + ".zip";
            } else {
                fileName = savedContent.getName() + ":" + contentVersion + ".zip";
            }
                String uploadDir = "";
            if (Objects.equals(savedContent.getPlatform().getName(), "all")){
                uploadDir =  fileServerFolder + contentName + '/' +typeName + "/";
                if ((Objects.equals(typeName, "amxmodules") || Objects.equals(typeName, "mmmodules")) && !Objects.equals(platformName, "all")){
                    uploadDir +=  platformName + "/";
                }
                try {
                    Files.createDirectories(Path.of(uploadDir));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            String filePath = uploadDir + fileName;
            try {
                contentArchive.transferTo(new File(filePath));
                System.out.println("[HLDS PM] Content " + contentName +  " saved at: " + filePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }
    }
}
