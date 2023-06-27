package com.hldspm.server.database.dumper;

import com.hldspm.server.io.io;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ArchiveProcessing {

    public static void archiveRepoFolder(String folderPath, String archiveName) {
        io.customPrint("Archiving the repo's files");
        try {
            FileOutputStream fos = new FileOutputStream(archiveName);
            TarArchiveOutputStream tarOut = new TarArchiveOutputStream(fos);
            addFilesToTar(tarOut, folderPath, "");
            tarOut.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addFilesToTar(TarArchiveOutputStream tarOut, String sourceFolder, String parentFolder) throws IOException {
        File folder = new File(sourceFolder);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    addFilesToTar(tarOut, file.getAbsolutePath(), parentFolder + file.getName() + "/");
                } else {
                    TarArchiveEntry entry = new TarArchiveEntry(parentFolder + file.getName());
                    entry.setSize(file.length()); // Устанавливаем правильный размер файла
                    tarOut.putArchiveEntry(entry);

                    FileInputStream fis = new FileInputStream(file);
                    IOUtils.copy(fis, tarOut);
                    fis.close();

                    tarOut.closeArchiveEntry();
                }
            }
        }
    }

    public static void generateDumpFile(String sqlPath, String repoArchivePath, String outputPath) {
        io.customPrint("Generating the dump file...");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(outputPath);
            GzipCompressorOutputStream gzipOutputStream = new GzipCompressorOutputStream(fileOutputStream);
            TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(gzipOutputStream);
            addToTar(sqlPath, tarOutputStream);
            addToTar(repoArchivePath, tarOutputStream);
            tarOutputStream.close();
            gzipOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void addToTar(String filePath, TarArchiveOutputStream tarOutputStream) throws IOException {
        File file = new File(filePath);
        TarArchiveEntry tarEntry = new TarArchiveEntry(file);
        tarEntry.setSize(file.length());
        tarOutputStream.putArchiveEntry(tarEntry);
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            tarOutputStream.write(buffer, 0, bytesRead);
        }
        fileInputStream.close();
        tarOutputStream.closeArchiveEntry();
    }


}
