package com.hldspm.server.connections.requests.delete_request;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DeleteUploaderRequest {
    @JsonProperty("token")
    private  String sudoToken;

    @JsonProperty("name")
    private String uploaderName;

    public DeleteUploaderRequest(String sudoToken, String uploaderName) {
        this.sudoToken = sudoToken;
        this.uploaderName = uploaderName;
    }

    public String getSudoToken() {
        return sudoToken;
    }

    public void setSudoToken(String sudoToken) {
        this.sudoToken = sudoToken;
    }

    public String getUploaderName() {
        return uploaderName;
    }

    public void setUploaderName(String uploaderName) {
        this.uploaderName = uploaderName;
    }
}
