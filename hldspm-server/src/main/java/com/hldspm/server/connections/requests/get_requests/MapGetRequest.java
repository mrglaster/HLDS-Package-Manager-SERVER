package com.hldspm.server.connections.requests.get_requests;

import com.hldspm.server.connections.requests.parental_requests.BasicGetRequest;

/**Class describing the map get request*/
public class MapGetRequest extends BasicGetRequest {
    public MapGetRequest(String engine, String game, String name) {
        super(engine, game, name);
    }
}
