package com.hldspm.server.database.initializer;

import com.hldspm.server.ServerApplication;
import com.hldspm.server.io.io;
import org.springframework.dao.DataAccessException;

public class DatabaseInitializer {
    private static final String sqlDatabaseBase = """
            CREATE TABLE IF NOT EXISTS engines(
            	id BIGSERIAL  PRIMARY KEY,
            	name VARCHAR(6) NOT NULL
            );
                                  
            CREATE TABLE IF NOT EXISTS content_types(
            	id BIGSERIAL  PRIMARY KEY,
            	name VARCHAR(6) NOT NULL UNIQUE
            );
                        
            CREATE TABLE IF NOT EXISTS maps(
            	id BIGSERIAL PRIMARY KEY ,
            	engine INT NOT NULL,
            	game VARCHAR(10) NOT NULL,
            	name VARCHAR(255) NOT NULL UNIQUE,
            	gamemode VARCHAR(5),
            	FOREIGN KEY (engine) REFERENCES engines(id)
            );
                        
            CREATE TABLE IF NOT EXISTS plugins(
            	id BIGSERIAL PRIMARY KEY,
            	engine INT NOT NULL,
            	game VARCHAR(10),
            	uploader VARCHAR(255),
            	name VARCHAR(255) NOT NULL UNIQUE,
            	FOREIGN KEY (engine) REFERENCES engines(id)
            );
                        
                        
            CREATE TABLE IF NOT EXISTS modules(
            	id BIGSERIAL PRIMARY KEY,
            	engine INT NOT NULL,
            	game VARCHAR(10) NOT NULL,
            	platform VARCHAR(3) NOT NULL,
            	name VARCHAR(255) NOT NULL UNIQUE,
            	FOREIGN KEY (engine) REFERENCES engines(id)
            );
                        
                        
            CREATE TABLE IF NOT EXISTS uploaders(
            	id BIGSERIAL PRIMARY KEY,
            	name VARCHAR(255) NOT NULL UNIQUE,
            	token VARCHAR(255) NOT NULL UNIQUE
            );
                        
            CREATE TABLE IF NOT EXISTS bundles(
            	id BIGSERIAL PRIMARY KEY,
            	content_type INT NOT NULL,
            	engine INT NOT NULL,
            	game VARCHAR(255) NOT NULL,
            	name VARCHAR(255) NOT NULL UNIQUE,
            	elements bigint[] NOT NULL,
            	FOREIGN KEY (content_type) REFERENCES content_types(id),
            	FOREIGN KEY (engine) REFERENCES engines(id)
            );
                        
            """;

    private static final String initEngines = "INSERT INTO engines (name) VALUES ('gold'), ('source');";
    private static final String initContentTypes = "INSERT INTO content_types(name) VALUES ('plugin'), ('map'), ('module');";


    public static void processDatabaseInit(){
        try {
            io.customPrint("Creating table structure");
            ServerApplication.jdbcTemplate.update(sqlDatabaseBase);
        } catch (DataAccessException e) {
            io.customPrint("Server already has such tables structure");
        }

        try {
            io.customPrint("Engines initialization");
            ServerApplication.jdbcTemplate.update(initEngines);
        } catch (DataAccessException e) {
            io.customPrint("Engines have been initialized");
        }

        try {
            io.customPrint("Content types initialization");
            ServerApplication.jdbcTemplate.update(initContentTypes);
        } catch (DataAccessException e) {
            io.customPrint("Content types have been initialized");
        }

        try {
            io.customPrint("Adding admins with uploader rights");
            ServerApplication.jdbcTemplate.update(ServerApplication.configure.generateAdminInsertionQuery());
        } catch (DataAccessException e) {
            io.customPrint("Admins have been initialized");
        }

    }


}
