package ru.hldspm;

import ru.hldspm.db.migration.LiquibaseMigrationProcessor;
import ru.hldspm.db.repository.GameContentRepository;
import ru.hldspm.server.RepoServer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            LiquibaseMigrationProcessor.processDatabaseMigration();
            RepoServer.updateRepoInformation();
            RepoServer.startServer(666);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}