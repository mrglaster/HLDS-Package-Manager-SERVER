package ru.hldspm.db.repository;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import ru.hldspm.Main;
import ru.hldspm.db.models.Plugin;

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

    public static String getGameContent(String game, String platform) {
        try {
            Properties properties = loadProperties();
            try (Connection connection = getConnection(properties)) {
                String query = "SELECT * FROM content WHERE (game = (SELECT id FROM games WHERE name = ?) OR game = (SELECT id FROM games WHERE name = 'gold_universal'))" +
                        " AND (platform = (SELECT id FROM platforms WHERE name = ?) OR platform = (SELECT id FROM platforms WHERE name = 'all'))";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, game);
                    preparedStatement.setString(2, platform);
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        return constructJsonResponse(resultSet, connection);
                    }
                }
            } catch (SQLException e) {
                handleSQLException(e);
                return "Error retrieving content.";
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Properties loadProperties() throws IOException {
        Properties properties = new Properties();
        properties.load(Main.class.getClassLoader().getResourceAsStream("liquibase.properties"));
        return properties;
    }

    private static Connection getConnection(Properties properties) throws SQLException {
        String url = properties.getProperty("url");
        String username = properties.getProperty("username");
        String password = properties.getProperty("password");
        return DriverManager.getConnection(url, username, password);
    }

    private static String constructJsonResponse(ResultSet resultSet, Connection connection) throws SQLException {
        // Construct JSON response
        JsonObject jsonResponse = new JsonObject();
        JsonArray amxPluginList = new JsonArray();
        JsonArray maps = new JsonArray();

        while (resultSet.next()) {
            int contentType = resultSet.getInt("content_type");
            switch (contentType) {
                case AMX_PLUGIN_TYPE -> {
                    Plugin currentPlugin = new Plugin(connection, resultSet.getInt("id"), resultSet.getString("name"));
                    amxPluginList.add(currentPlugin.getPluginJson());
                }
                case MAP_TYPE -> {
                    maps.add(resultSet.getString("name"));
                }
            }
        }
        jsonResponse.add("amx-plugins", amxPluginList);
        jsonResponse.add("maps", maps);
        return new Gson().toJson(jsonResponse);
    }

    private static void handleSQLException(SQLException e) {
        e.printStackTrace();
    }
}
