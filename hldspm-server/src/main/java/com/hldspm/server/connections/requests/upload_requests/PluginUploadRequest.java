package com.hldspm.server.connections.requests.upload_requests;
import com.hldspm.server.connections.requests.parental_requests.BasicUploadRequest;


/**Class describing the plugin upload request*/
public class PluginUploadRequest extends BasicUploadRequest {
    private String version;


    public PluginUploadRequest(String engine, String game, String token, String name, String version, String data) {
        super(engine, game, name, token, data);
        this.version = version;

    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String generateFileName(){
        return this.getName() + '%' + this.version + ".tar.gz";
    }

    public boolean isValidRequest(){
        return this.getData().length() !=0 && this.getName().length() != 0 && this.version.length() != 0 && BasicUploadRequest.isValidGame(this.getGame()) && this.isValidEngine();
    }


}
