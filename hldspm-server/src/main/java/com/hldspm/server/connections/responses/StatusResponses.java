package com.hldspm.server.connections.responses;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class StatusResponses {
    private static final Gson curGson = new GsonBuilder().setPrettyPrinting().create();

    public static String generateBadTokenError(){
        JsonObject response = new JsonObject();
        response.addProperty("status", 403);
        response.addProperty("description", "Access denied! Bad token!");
        return curGson.toJson(response);
    }


    public static String generateBadGameError(String game){
        JsonObject response = new JsonObject();
        response.addProperty("status", 400);
        response.addProperty("description", "Unknown game: " + game);
        return curGson.toJson(response);
    }

    public static String generateBadResourceNameError(){
        JsonObject response = new JsonObject();
        response.addProperty("status", 433);
        response.addProperty("description", "There is already a resource with such name!");
        return curGson.toJson(response);
    }

    public static String generateUnknownEngineError(String engine){
        JsonObject response = new JsonObject();
        response.addProperty("status", 400);
        response.addProperty("description", "Unknown engine: " + engine);
        return curGson.toJson(response);
    }


    public static String generateInvalidResourceDataErr(){
        JsonObject response = new JsonObject();
        response.addProperty("status", 400);
        response.addProperty("description", "Invalid resource data!");
        return curGson.toJson(response);
    }

    public static String generateSuccessfulUpload(){
        JsonObject response = new JsonObject();
        response.addProperty("status", 200);
        response.addProperty("description", "Your resource was successfully added to the repository");
        return curGson.toJson(response);
    }


    public static String generateBadRequestErr(){
        JsonObject response = new JsonObject();
        response.addProperty("status", 400);
        response.addProperty("description", "Bad request");
        return curGson.toJson(response);
    }


}
