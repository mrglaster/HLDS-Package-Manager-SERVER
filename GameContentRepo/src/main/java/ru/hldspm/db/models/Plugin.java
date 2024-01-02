package ru.hldspm.db.models;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Plugin {
    private static final String GET_VERSIONS_QUERY = "SELECT version FROM content_versions WHERE content_id=? ORDER BY uploaded_at DESC";

    private JsonArray versions = new JsonArray();
    private String name;

    public Plugin(Connection connection, int pluginId, String pluginName) {
        this.name = pluginName;
        try (PreparedStatement pluginsVersionsStatement = connection.prepareStatement(GET_VERSIONS_QUERY)) {
            pluginsVersionsStatement.setInt(1, pluginId);
            try (ResultSet pluginVersions = pluginsVersionsStatement.executeQuery()) {
                while (pluginVersions.next()) {
                    versions.add(pluginVersions.getString("version"));
                }
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public JsonObject getPluginJson() {
        JsonObject plugin = new JsonObject();
        plugin.addProperty("name", this.name);
        plugin.add("versions", versions);
        return plugin;
    }

    private static void handleSQLException(SQLException e) {
        // Handle SQLException (log, throw a custom exception, etc.)
        e.printStackTrace();
    }
}