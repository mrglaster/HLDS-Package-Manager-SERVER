package com.hldspm.server.connections.requests.get_requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hldspm.server.connections.requests.parental_requests.BasicGetRequest;
import java.util.Objects;

/**Class describing the bundle getting request*/
public class BundleGetRequest extends BasicGetRequest {
    @JsonProperty("type")
    private String type;
    public BundleGetRequest(String engine, String game, String name, String type) {
        super(engine, game, name);
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isValidType(){
        return Objects.equals(type, "plugin") || Objects.equals(type, "map") || Objects.equals(type, "module");
    }

    public boolean isValidRequest() {
        return isValidEngine() && BasicGetRequest.isValidGame(getGame()) && getGame().length() > 0 && isValidType();
    }
}
