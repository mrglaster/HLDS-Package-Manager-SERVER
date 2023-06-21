package com.hldspm.server.models;

/**Model for the Bundle object*/
public class BundleModel {
    private long id;
    private String name;
    private String pluginIds;

    public BundleModel(){}

    public BundleModel(long id, String name, String pluginIds) {
        this.id = id;
        this.name = name;
        this.pluginIds = pluginIds;
    }

    @Override
    public String toString() {
        return "BundleModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pluginIds=" + pluginIds +
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

    public String getPluginIds() {
        return pluginIds;
    }

    public void setPluginIds(String pluginIds) {
        this.pluginIds = pluginIds;
    }
}
