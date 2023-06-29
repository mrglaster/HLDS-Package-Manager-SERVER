package com.hldspm.server.database.data_processor.manifest_processors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hldspm.server.ServerApplication;
import com.hldspm.server.connections.requests.get_requests.ManifestGetRequest;
import com.hldspm.server.connections.responses.StatusResponses;
import com.hldspm.server.ftp_server.cfg.FtpConstants;
import com.hldspm.server.models.ManifestElementModel;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//TODO ОПТИМИЗИРОВАТЬ ЗАПРОСЫ

/**Class processing the manifest get request*/
public class ManifestGetter {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**Generates SQL query to get a manifest element*/
    private static String getManifestElementQuery(String game, ManifestElementModel element) {
        StringBuilder queryBuilder = new StringBuilder();

        switch (element.getType()) {
            case "map" ->
                    queryBuilder.append("SELECT name FROM maps WHERE name='").append(element.getName()).append("';");
            case "plugin" -> {
                if (element.getName().contains("%")) {
                    queryBuilder.append("SELECT name FROM plugins WHERE game='").append(game).append("' AND name='").append(element.getName()).append("';");
                } else {
                    queryBuilder.append("SELECT name FROM plugins WHERE game='").append(game).append("' AND name LIKE '").append(element.getName()).append("%' ORDER BY time DESC LIMIT 1;");
                }
            }
        }
        return queryBuilder.toString();
    }

    /**Generates FTP-server link by some data*/
    private static String getFtpServerLink(String game, String engine, ManifestElementModel element) {
        String query = getManifestElementQuery(game, element);
        List<String> count = ServerApplication.jdbcTemplate.queryForList(query, String.class);
        String currentName;
        try {
            currentName = count.get(0);
        } catch (Exception e) {
            return "None";
        }
        return FtpConstants.FTP_LINK_INIT + engine + '/' + element.getType() + "s/" + game + '/' + currentName + ".tar.gz";
    }

    /**The manifest getting function*/
    public static String processManifestGetting(ManifestGetRequest request) {
        if (request.getManifestList().isEmpty())
            return StatusResponses.generateBadManifestData();
        if (!request.isValidRequest())
            return StatusResponses.generateBadRequestErr();

        String game = request.getGame();
        String engine = request.getEngine();
        JsonArray plugins = new JsonArray();
        JsonArray maps = new JsonArray();
        JsonArray errors = new JsonArray();
        List<String> processedElements = new ArrayList<>();
        for (ManifestElementModel element : request.getManifestList()) {
            if (!processedElements.contains(element.getName())) {
                String currentLink = getFtpServerLink(game, engine, element);
                if (currentLink.equals("None")) {
                    errors.add(element.getName());
                } else {
                    if (Objects.equals(element.getType(), "map"))
                        maps.add(currentLink);
                    else
                        plugins.add(currentLink);
                }
                processedElements.add(element.getName());
            }
        }
        int status = 200;
        if (errors.size() > 0)
            status = 211;

        JsonObject response = new JsonObject();
        response.addProperty("status", status);
        response.add("plugins", plugins);
        response.add("maps", maps);
        response.add("errors", errors);

        return gson.toJson(response);
    }
}
