package com.hldspm.server.connections.requests.upload_requests;
import com.hldspm.server.connections.requests.parental_requests.BasicGetRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class BundleUploadRequest extends BasicGetRequest {
    private List<String> elements;
    private String token;
    private String type;
    public BundleUploadRequest(String engine, String game, String name, String token, String type,  List<String> elements) {
        super(engine, game, name);
        this.token = token;
        this.type = type;
        this.elements = elements;
    }

    public List<String> getElements() {
        return elements;
    }

    public void setElements(List<String> elements) {
        this.elements = elements;
    }

    public String getMapsAsDataList(){
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        List<String> usedNames = new ArrayList<>();
        for (int i = 0; i <elements.size(); i++) {
            String currentElem = elements.get(i);
            if (!usedNames.contains(currentElem)){
                sb.append("'").append(currentElem).append("'");
                usedNames.add(currentElem);
                if (i < elements.size() - 1) {
                    sb.append(", ");
                }
            }
        }
        sb.append(")");
        return sb.toString();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isValidRequest(){
        return isValidEngine() && isValidGame(getGame()) && getName().length() > 0 && (Objects.equals(getType(), "map") || Objects.equals(getType(), "plugin")) && token.length() > 0;
    }

    public int engineToId(){
        switch(getEngine()){
            case "gold" -> {
                return 1;
            }
            case "source" -> {
                return 2;
            }
            case "source2" -> {
                return 3;
            }
        }
        return 1;
    }

    public int contentTypeToId(){
        switch (getType()){
            case "map" -> {
                return 1;
            }
            case "plugin" -> {
                return 2;
            }
        }
        return 1;
    }

}
