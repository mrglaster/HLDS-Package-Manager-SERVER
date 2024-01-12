package ru.hldspm;

import ru.hldspm.db.migration.LiquibaseMigrationProcessor;
import ru.hldspm.server.RepoServer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            LiquibaseMigrationProcessor.processDatabaseMigration();
            RepoServer.createRepoInformation();
            RepoServer.startServer(5611);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}