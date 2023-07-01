package com.hldspm.server.connections.requests.upload_requests;
import com.fasterxml.jackson.annotation.JsonProperty;


public class CreateAdminRequest {

    @JsonProperty("token")
    private String sudoToken;
    @JsonProperty("name")
    private String name;

    @JsonProperty("uploader_token")
    private String uploaderToken;

    public CreateAdminRequest(String sudoToken, String name, String uploaderToken) {
        this.sudoToken = sudoToken;
        this.name = name;
        this.uploaderToken = uploaderToken;
    }


    public boolean isValidRequest(){
        return sudoToken.length() != 0 && name.length() != 0 && uploaderToken.length() != 0;
    }

    public String getSudoToken() {
        return sudoToken;
    }

    public void setSudoToken(String sudoToken) {
        this.sudoToken = sudoToken;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUploaderToken() {
        return uploaderToken;
    }

    public void setUploaderToken(String uploaderToken) {
        this.uploaderToken = uploaderToken;
    }
}
