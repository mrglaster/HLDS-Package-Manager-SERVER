package com.hldspm.server.connections.responses;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class StatusResponses {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private static String generateResponse(int status, String description) {
        JsonObject response = new JsonObject();
        response.addProperty("status", status);
        response.addProperty("description", description);
        return gson.toJson(response);
    }

    public static String generateError(int status, String description) {
        return generateResponse(status, description);
    }

    public static String generateBadTokenError() {
        return generateResponse(403, "Access denied! Bad token!");
    }

    public static String generateBadGameError(String game) {
        return generateResponse(400, "Unknown game: " + game);
    }

    public static String generateBadResourceNameError() {
        return generateResponse(433, "There is already a resource with such name!");
    }

    public static String generateUnknownEngineError(String engine) {
        return generateResponse(400, "Unknown engine: " + engine);
    }

    public static String generateInvalidResourceDataErr() {
        return generateResponse(400, "Invalid resource data!");
    }

    public static String generateSuccessfulUpload() {
        return generateResponse(200, "Your resource was successfully added to the repository");
    }

    public static String generateBadRequestErr() {
        return generateResponse(400, "Bad request");
    }

    public static String generateBadManifestData() {
        return generateError(400, "Bad manifest data");
    }
}
