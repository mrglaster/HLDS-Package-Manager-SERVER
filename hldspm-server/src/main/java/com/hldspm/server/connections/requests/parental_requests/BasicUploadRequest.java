package com.hldspm.server.connections.requests.parental_requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BasicUploadRequest extends BasicGetRequest{

    @JsonProperty("token")
    private String token;

    @JsonProperty("data")
    private String data;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public BasicUploadRequest(String engine, String game, String name, String token, String data) {
        super(engine, game, name);
        this.token = token;
        this.data = data;
    }


}
