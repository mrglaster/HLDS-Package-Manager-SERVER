package com.hldspm.server.connections.requests.get_requests;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class MapListRequest {
    @JsonProperty("engine")
    private String engine;

    @JsonProperty("game")
    private String game;

    @JsonProperty("gamemodes")
    private List<String> gameModes;

    public MapListRequest(String engine, String game, List<String> gameModes) {
        this.engine = engine;
        this.game = game;
        this.gameModes = gameModes;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public List<String> getGameModes() {
        return gameModes;
    }

    public void setGameModes(List<String> gameModes) {
        this.gameModes = gameModes;
    }

    public int engineToId(){
        switch(engine){
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
        return 0;
    }


    public  boolean isValidRequest(){
        return gameModes.size() > 0 && engineToId() != 0;
    }


}
