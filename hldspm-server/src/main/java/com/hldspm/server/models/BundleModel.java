package com.hldspm.server.models;

import java.util.Arrays;
import java.util.List;

public class BundleModel {
    private long id;
    private String name;
    private List<Integer> pluginIds;

    public BundleModel(){}

    public BundleModel(long id, String name, List<Integer> pluginIds) {
        this.id = id;
        this.name = name;
        this.pluginIds = pluginIds;
    }

    @Override
    public String toString() {
        return "BundleModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pluginIds=" + Arrays.toString(pluginIds.toArray()) +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getPluginIds() {
        return pluginIds;
    }

    public void setPluginIds(List<Integer> pluginIds) {
        this.pluginIds = pluginIds;
    }
}
