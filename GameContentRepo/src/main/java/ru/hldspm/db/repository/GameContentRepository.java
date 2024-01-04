package ru.hldspm.db.repository;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import ru.hldspm.Main;
import ru.hldspm.db.models.VersionedContent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
public class GameContentRepository {
    private static final int AMX_PLUGIN_TYPE = 1;
    private static final int MAP_TYPE = 2;
    private static final int AS_PLUGIN_TYPE = 3;
    private static final int LM_PLUGIN_TYPE=4;
    private static final int SM_PLUGIN_TYPE = 5;
    private static final int MM_MODULE_TYPE = 6;
    private static final int AMX_MODULE_TYPE = 7;

    private static final String[] operationSystems = {"linux", "windows", "mac"};

    private static final int PLATFORM_ALL = 1;
    private static final int PLATFORM_LINUX = 2;
    private static final int PLATFORM_WINDOWS = 3;
    private static final int PLATFORM_MAC = 4;

    private static final Gson currentGson = new Gson();

    /**Creates cache in JSON files for current game*/
    public static void createGameCache(String game) {
        try {
            Properties properties = loadProperties();
            try (Connection connection = getConnection(properties)) {
                String query = "SELECT * FROM content WHERE (game = (SELECT id FROM games WHERE name = ?) OR game = (SELECT id FROM games WHERE name = 'gold_universal'));";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, game);
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        JsonObject[] gameResults = getRepoGameInfo(resultSet, connection);
                        writeGameCache(gameResults, game);
                    }
                }
            } catch (SQLException e) {
                throw new SQLException("Something went wrong during the database connection.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**Loads database connection properties from liquibase.properties*/
    private static Properties loadProperties() throws IOException {
        Properties properties = new Properties();
        properties.load(Main.class.getClassLoader().getResourceAsStream("liquibase.properties"));
        return properties;
    }

    /**Creates Connection to the database*/
    private static Connection getConnection(Properties properties) throws SQLException {
        String url = properties.getProperty("url");
        String username = properties.getProperty("username");
        String password = properties.getProperty("password");
        return DriverManager.getConnection(url, username, password);
    }

    /**Sorts content for current game and builds result JSONs  */
    private static JsonObject[] getRepoGameInfo(ResultSet resultSet, Connection connection) throws SQLException {
        // Construct JSON response
        JsonObject gameContentLinux = new JsonObject();
        JsonObject gameContentWindows = new JsonObject();
        JsonObject gameContentMac = new JsonObject();

        JsonArray amxPluginList = new JsonArray();
        JsonArray maps = new JsonArray();

        JsonArray windowsMModules = new JsonArray();
        JsonArray linuxMModules = new JsonArray();
        JsonArray macMMModules = new JsonArray();

        while (resultSet.next()) {
            int contentType = resultSet.getInt("content_type");
            switch (contentType) {
                case AMX_PLUGIN_TYPE -> {
                    VersionedContent currentPlugin = new VersionedContent(connection, resultSet.getInt("id"), resultSet.getString("name"));
                    amxPluginList.add(currentPlugin.getVersionedContentData());
                }
                case MAP_TYPE -> {
                    maps.add(resultSet.getString("name"));
                }
                case MM_MODULE_TYPE -> {
                    int platform = resultSet.getInt("platform");
                    VersionedContent currentMMModule = new VersionedContent(connection, resultSet.getInt("id"), resultSet.getString("name"));
                    switch (platform){
                        case PLATFORM_LINUX -> linuxMModules.add(currentMMModule.getVersionedContentData());
                        case PLATFORM_WINDOWS -> windowsMModules.add(currentMMModule.getVersionedContentData());
                        case PLATFORM_MAC -> macMMModules.add(currentMMModule.getVersionedContentData());
                    }
                }
            }
        }
        gameContentLinux.add("amx-plugins", amxPluginList);
        gameContentWindows.add("amx-plugins", amxPluginList);
        gameContentMac.add("amx-plugins", amxPluginList);

        gameContentLinux.add("maps", maps);
        gameContentWindows.add("maps", maps);
        gameContentMac.add("maps", maps);

        gameContentLinux.add("mmmodule", linuxMModules);
        gameContentWindows.add("mmmodule", windowsMModules);
        gameContentMac.add("mmmodule", macMMModules);

        return new JsonObject[]{gameContentLinux, gameContentWindows, gameContentMac};
    }

    /**Writes information about content for current game for each platform*/
    private static void writeGameCache(JsonObject[] gameData, String game) throws IOException {
        File theDir = new File("cashed/");
        if (!theDir.exists()){
            theDir.mkdirs();
        }
        for (int i = 0; i < 3; i++) {
            File currentConfigurationInfo = new File("cashed/"+game + '_' + operationSystems[i] + ".json");
            FileWriter writer = new FileWriter(currentConfigurationInfo);
            writer.write(currentGson.toJson(gameData[i]));
        }
    }
}
