package com.hldspm.server.cfg_reader;

import com.hldspm.server.io.io;
import org.apache.commons.configuration2.INIConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.FileReader;
import java.io.IOException;

public class CfgReader {

    private static final String cfgFilePath = "cfg/configuration.ini";
    private static String serverUrl;
    private static String serverPort;

    private static String dbName;
    private static String dbUsername;
    private static String dbPassword;

    private static String repoAdmin;
    private static String repoAdminPassword;

    private static String ftpAddress;
    private static String ftpPort;

    private static String createTableStructure;


    private void readDatabaseParameters(INIConfiguration iniConfiguration){
        dbName = iniConfiguration.getSection("database").getProperty("database_name").toString();
        dbUsername = iniConfiguration.getSection("database").getProperty("username").toString();
        dbPassword = iniConfiguration.getSection("database").getProperty("password").toString();
        serverUrl = iniConfiguration.getSection("database").getProperty("server_address").toString();
        serverPort = iniConfiguration.getSection("database").getProperty("server_port").toString();
        createTableStructure = iniConfiguration.getSection("database").getProperty("create_table_structure").toString();
        repoAdmin = iniConfiguration.getSection("database").getProperty("repo_admin").toString();
        repoAdminPassword = iniConfiguration.getSection("database").getProperty("repo_admin_token").toString();
    }

    private void readFtpServerParameters(INIConfiguration iniConfiguration){
        ftpAddress = iniConfiguration.getSection("ftp").getProperty("address").toString();
        ftpPort = iniConfiguration.getSection("ftp").getProperty("port").toString();
    }

    public void processCfgRead() {
        INIConfiguration iniConfiguration = new INIConfiguration();
        try (FileReader fileReader = new FileReader(cfgFilePath)) {
            iniConfiguration.read(fileReader);
        } catch (ConfigurationException | IOException e) {
            throw new RuntimeException(e);
        }
        try {
            io.customPrint("Reading the configuration file");
            readDatabaseParameters(iniConfiguration);
            readFtpServerParameters(iniConfiguration);
            io.customPrint("Data from the configuration file was successfully read");
        } catch (Exception e) {
            throw new RuntimeException("Bad cfg file!");
        }
    }

    public static String getDatabaseUrl(){
        return "jdbc:postgresql://" + serverUrl + ':' + serverPort + '/' + dbName;
    }

    public static String getFtpUrlTemplate(){
        return "ftp://anonymous@" + ftpAddress + '/';
    }

    public static String getServerUrl() {
        return serverUrl;
    }

    public static String getServerPort() {
        return serverPort;
    }

    public static String getDbName() {
        return dbName;
    }

    public static String getDbUsername() {
        return dbUsername;
    }

    public static String getDbPassword() {
        return dbPassword;
    }

    public static String getFtpAddress() {
        return ftpAddress;
    }

    public static String getFtpPort() {
        return ftpPort;
    }

    public static String getCreateTableStructureFlag() {
        return createTableStructure;
    }

    public static String getRepoAdmin() {
        return repoAdmin;
    }

    public static String getRepoAdminPassword() {
        return repoAdminPassword;
    }

    public static String generateAdminInsertionQuery(){
        return "INSERT INTO uploaders(name, token) VALUES('" + getRepoAdmin() + "', '" + getRepoAdminPassword() + "');";
    }

}
