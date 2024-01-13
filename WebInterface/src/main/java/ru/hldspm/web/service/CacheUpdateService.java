package ru.hldspm.web.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;
import ru.hldspm.web.entities.Content;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

@Service
public class CacheUpdateService {

    private static final int AMX_PLUGIN_TYPE = 1;
    private static final int MAP_TYPE = 2;
    private static final int MM_MODULE_TYPE = 6;

    private static final String cacheFolder = System.getProperty("user.dir") + "/cached/";

    private static final String[] supportedPlatforms = new String[]{"all", "linux", "windows", "mac"};

    private static final Gson gson = new Gson();

    public static void updateJsonCache(Content savedContent, String contentVersion){
        long contentTypeId = savedContent.getContentType().getId();
        String currentGame = savedContent.getGame().getName();
        if (contentTypeId == MAP_TYPE) {
            updateMapJson(currentGame, savedContent.getName());
        } else if (contentTypeId == AMX_PLUGIN_TYPE) {
            updatePluginJsom(currentGame, savedContent.getName(), contentVersion);
        }
    }

    private static void updateMapJson(String game, String contentName){
        for (int i = 1; i < supportedPlatforms.length; i++ ){
            String currentPlatform = supportedPlatforms[i];
            String currentJsonFile = cacheFolder + "/" + game + "_" + currentPlatform + ".json";
            try {
                JsonObject jsonData = gson.fromJson(new FileReader(currentJsonFile), JsonObject.class);
                JsonArray mapsArray = jsonData.get("maps").getAsJsonArray();
                mapsArray.add(gson.toJsonTree(contentName));
                FileWriter writer = new FileWriter(currentJsonFile);
                gson.toJson(jsonData, writer);
                writer.flush();
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    private static void updatePluginJsom(String game, String pluginName, String pluginVersion){
        for (int i = 1; i < supportedPlatforms.length; i++ ){
            try {
                String currentPlatform = supportedPlatforms[i];
                String currentJsonFile = cacheFolder + "/" + game + "_" + currentPlatform + ".json";
                JsonObject jsonData = gson.fromJson(new FileReader(currentJsonFile), JsonObject.class);
                JsonArray pluginsArray = jsonData.getAsJsonArray("amx-plugins");
                JsonObject updatingPlugin = null;
                for (int j = 0; j < pluginsArray.size(); j++) {
                    JsonObject pluginObject = pluginsArray.get(j).getAsJsonObject();
                    if (pluginObject.get("name").getAsString().equals(pluginName)) {
                       updatingPlugin = pluginObject;
                       break;
                    }
                }
                if (updatingPlugin == null){
                    updatingPlugin = new JsonObject();
                    updatingPlugin.addProperty("name", pluginName);
                    updatingPlugin.add("versions", new JsonArray());
                    pluginsArray.add(updatingPlugin);
                }
                JsonArray versionsArray = updatingPlugin.getAsJsonArray("versions");
                versionsArray.add(pluginVersion);
                FileWriter writer = new FileWriter(currentJsonFile);
                gson.toJson(jsonData, writer);
                writer.flush();
                writer.close();
            } catch (IOException e){
                throw new RuntimeException(e);
            }
        }
    }
}
