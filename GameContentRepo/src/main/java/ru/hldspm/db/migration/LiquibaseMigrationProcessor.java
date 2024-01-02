package ru.hldspm.db.migration;

import ru.hldspm.Main;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;


public class LiquibaseMigrationProcessor {
    public static void processDatabaseMigration() {
        if (!new File(".migrated").exists()) {
            try {
                Properties properties = new Properties();
                properties.load(Main.class.getClassLoader().getResourceAsStream("liquibase.properties"));
                String url = properties.getProperty("url");
                String username = properties.getProperty("username");
                String password = properties.getProperty("password");
                try (Connection connection = DriverManager.getConnection(url, username, password)) {
                    Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
                    Liquibase liquibase = new Liquibase(properties.getProperty("changeLogFile"), new ClassLoaderResourceAccessor(), database);
                    liquibase.update("");
                    File file = new File(".migrated");
                    file.createNewFile();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        else {
            System.out.println("[HLDS PM] Ignoring migration: already up to date.");
        }
    }
}
