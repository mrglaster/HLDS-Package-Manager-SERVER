package com.hldspm.server.database.dumper;

import com.hldspm.server.ServerApplication;
import com.hldspm.server.io.io;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.springframework.dao.DataAccessException;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DumpReader {
    private static final File ftpFiles = new File("files");

    private static String calculateMd5Hash(String filePath) throws IOException {
        byte[] data = Files.readAllBytes(Paths.get(filePath));
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(data);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return new BigInteger(1, hash).toString(16);
    }

    private static void unzipStorage(File archive) {
        io.customPrint("Unpacking the storage backup from:  " + archive.getName());
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(archive));
             GzipCompressorInputStream gzis = new GzipCompressorInputStream(bis);
             TarArchiveInputStream taris = new TarArchiveInputStream(gzis)) {
            TarArchiveEntry entry;
            while ((entry = taris.getNextTarEntry()) != null) {
                File outputFile = new File("files", entry.getName());
                if (entry.isDirectory()) {
                    outputFile.mkdirs();
                } else {
                    outputFile.getParentFile().mkdirs();
                    try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outputFile))) {
                        byte[] buffer = new byte[8192];
                        int bytesRead;
                        while ((bytesRead = taris.read(buffer)) != -1) {
                            bos.write(buffer, 0, bytesRead);
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void restoreDb(File sqlFile) {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(sqlFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String restoreQuery = stringBuilder.toString();
        String[] substrings = restoreQuery.split(";");
        for (String substring : substrings) {
            try {
                ServerApplication.jdbcTemplate.update(substring);
            } catch (DataAccessException ignored) {
            }
        }
    }

    private static void processDumpFile(File archive) {
        File outArchive = null;
        File outSql = null;
        try (FileInputStream fis = new FileInputStream(archive);
             BufferedInputStream bis = new BufferedInputStream(fis);
             GzipCompressorInputStream gzis = new GzipCompressorInputStream(bis);
             TarArchiveInputStream tais = new TarArchiveInputStream(gzis)) {
            TarArchiveEntry entry;
            while ((entry = tais.getNextTarEntry()) != null) {
                if (entry.getName().contains("tar.gz")) {
                    outArchive = new File("dumps" + '/' + entry.getName());
                    try (OutputStream fos = new FileOutputStream(outArchive);
                         BufferedOutputStream bos = new BufferedOutputStream(fos)) {
                        byte[] buffer = new byte[8192];
                        int bytesRead;
                        while ((bytesRead = tais.read(buffer)) != -1) {
                            bos.write(buffer, 0, bytesRead);
                        }
                    }
                } else if (entry.getName().contains(".sql")) {
                    outSql = new File("dumps" + '/' + entry.getName());
                    try (OutputStream fos = new FileOutputStream(outSql);
                         BufferedOutputStream bos = new BufferedOutputStream(fos)) {
                        byte[] buffer = new byte[8192];
                        int bytesRead;
                        while ((bytesRead = tais.read(buffer)) != -1) {
                            bos.write(buffer, 0, bytesRead);
                        }
                    }
                }
            }

            tais.close();
            gzis.close();
            bis.close();
            fis.close();
            unzipStorage(outArchive);
            restoreDb(outSql);
            outArchive.delete();
            outSql.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void processDumps() {
        String dumpsFolder = "dumps";
        File folder = new File(dumpsFolder);
        File[] files = folder.listFiles();
        if (files == null || files.length == 0) {
            io.customPrint("Dumps weren't found!");
            return;
        }

        ArrayList<String> mdHashes = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (File file : files) {
            executorService.execute(() -> {
                try {
                    //TODO использовать другой способ определения дубликатов
                    String hash = calculateMd5Hash(file.getPath());
                    if (mdHashes.contains(hash) || !file.getName().contains("tar.gz")) {
                        return;
                    }
                    mdHashes.add(hash);
                    processDumpFile(file);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
