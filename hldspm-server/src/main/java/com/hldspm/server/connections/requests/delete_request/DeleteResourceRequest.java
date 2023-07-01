package com.hldspm.server.connections.requests.delete_request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hldspm.server.connections.requests.parental_requests.BasicGetRequest;

import java.util.List;

public class DeleteResourceRequest extends BasicGetRequest {

    @JsonProperty("token")
    String sudoToken;
    @JsonProperty("type")
    String type;

    private final List<String> supportedTypes = List.of(new String[]{"plugin", "module", "map", "bundle", "uploader"});
    public DeleteResourceRequest(String engine, String game, String name, String sudoToken, String type) {
        super(engine, game, name);
        this.type = type;
        this.sudoToken = sudoToken;
    }

    public boolean isValidRequest(){
        return isValidGame(getGame()) && isValidEngine() && supportedTypes.contains(type) && getName().length() > 0;
    }

    public String getSudoToken() {
        return sudoToken;
    }

    public void setSudoToken(String sudoToken) {
        this.sudoToken = sudoToken;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
