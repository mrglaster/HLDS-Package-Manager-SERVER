package com.hldspm.server.connections.requests.get_requests;

import com.hldspm.server.connections.requests.parental_requests.BasicGetRequest;

/**Class describing the plugin getting request*/
public class PluginGetRequest extends BasicGetRequest {
    public PluginGetRequest(String game, String name, String engine) {
        super(engine, game, name);
    }

}
