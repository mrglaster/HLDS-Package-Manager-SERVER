package com.hldspm.server.database.data_processor.maps_processor;

import com.google.gson.*;
import com.hldspm.server.ServerApplication;
import com.hldspm.server.connections.requests.get_requests.MapListRequest;
import com.hldspm.server.connections.responses.StatusResponses;

import java.util.List;
import java.util.stream.Collectors;

public class MapListGetter {
    private static final Gson curGson = new GsonBuilder().setPrettyPrinting().create();

    /**Provides getting of maps list for requests the request contains*/
    public static String processMapListGetting(MapListRequest request){
        if (!request.isValidRequest()){
            return StatusResponses.generateBadRequestErr();
        }
        String query = "SELECT name FROM maps WHERE game='" + request.getGame() + "'";
        if (!request.getGameModes().contains("all")){
            String subPart = request.getGameModes().stream()
                    .map(s -> "'" + s + "'")
                    .collect(Collectors.joining(", ", "ARRAY[", "]"));
            query += " AND gamemode = ANY(" + subPart + ");";
        }
        List<String> gotNames = ServerApplication.jdbcTemplate.queryForList(query, String.class);
        JsonObject result = new JsonObject();
        result.addProperty("status", 200);
        result.add("maps", curGson.toJsonTree(gotNames).getAsJsonArray());
        return curGson.toJson(result);
    }

}
