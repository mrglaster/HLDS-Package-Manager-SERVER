package com.hldspm.server.connections.requests.parental_requests;

import java.util.Objects;

public class BasicGetRequest {
    private String game;
    private String engine;
    private String name;

    public BasicGetRequest(String engine, String game, String name) {
        this.game = game;
        this.engine = engine;
        this.name = name;
    }

    public BasicGetRequest(){}

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isValidEngine(){
        return Objects.equals(engine, "gold") || Objects.equals(engine, "source");
    }

}
