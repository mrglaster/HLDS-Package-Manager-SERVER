package com.hldspm.server.database.dumper;

import com.hldspm.server.ServerApplication;
import com.hldspm.server.io.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class DumpCreator {
    public static boolean hasChanges = false;
    private static void dumpAllTables(String dumpFilePath) {
        io.customPrint("Saving the database information");
        List<String> tableNames = getAllTableNames();
        try (PrintWriter writer = new PrintWriter(new FileWriter(dumpFilePath))) {
            for (String tableName : tableNames) {
                String query = "SELECT * FROM " + tableName;
                List<String> columnNames = getColumnNames(tableName);
                List<List<Object>> rows = ServerApplication.jdbcTemplate.query(query, (rs, rowNum) -> {
                    List<Object> row = new ArrayList<>();
                    for (String columnName : columnNames) {
                        row.add(rs.getObject(columnName));
                    }
                    return row;
                });
                writeTableDump(writer, tableName, rows);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> getAllTableNames() {
        String query = "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public'";
        return ServerApplication.jdbcTemplate.queryForList(query, String.class);
    }

    private static List<String> getColumnNames(String tableName) {
        String query = "SELECT column_name FROM information_schema.columns WHERE table_name = ?";
        return ServerApplication.jdbcTemplate.queryForList(query, new Object[]{tableName}, String.class);
    }

    /**Writes dump for one table*/
    private static void writeTableDump(PrintWriter writer, String tableName, List<List<Object>> rows) {
        for (List<Object> row : rows) {
            if (!Objects.equals(tableName, "engines") && !Objects.equals(tableName, "content_types")){
                String currentTableName = "";
                switch (tableName){
                    case "maps"-> currentTableName = "maps(engine, game, name, gamemode)";
                    case "plugins" -> currentTableName = "plugins(engine, time, game, name)";
                    case "bundles" -> currentTableName = "bundles(content_type, engine, elements, game, name)";
                    case "uploaders" -> currentTableName = "uploaders(name, token)";
                }
                writer.print("INSERT INTO " + currentTableName + " VALUES (");
                for (int i = 1; i < row.size(); i++) {
                    Object value = row.get(i);
                    if (value == null) {
                        writer.print("NULL");
                    } else if (value instanceof Number) {
                        writer.print(value);
                    } else {
                        writer.print("'" + value.toString().replace("'", "''") + "'");
                    }
                    if (i < row.size() - 1) {
                        writer.print(",");
                    }
                }
                writer.println(");");
            }
        }
    }



    public static void makeDump(){
        String dumpFolderName = "dumps";
        String sqlName = UUID.randomUUID() + ".sql";
        String archiveName = UUID.randomUUID() + ".tar.gz";
        String dumpName =  "dump%" + UUID.randomUUID() + ".tar.gz";
        if (!Files.exists(Paths.get(dumpFolderName))){
            try {
                Files.createDirectory(Path.of(dumpFolderName));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        dumpAllTables(sqlName);
        ArchiveProcessing.archiveRepoFolder("files", archiveName);
        ArchiveProcessing.generateDumpFile(sqlName, archiveName, dumpName);
        new File(sqlName).delete();
        new File(archiveName).delete();
        try {
            Files.move(Path.of(dumpName), Path.of(dumpFolderName + '/' + dumpName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }





}
