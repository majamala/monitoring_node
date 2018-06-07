package com.yammer;

import javax.validation.constraints.NotNull;

public class NodeInfo {

    @NotNull
    private String name;
    private String location;
    @NotNull
    private String IPAddress;
    private String description;
    private String connectors;
    private String meta;

    public NodeInfo(){

    }

    public NodeInfo(String IPAddress) {
        this.IPAddress = IPAddress;
    }

    public NodeInfo(String name, String location, String IPAddress, String description, String connectors, String meta) {
        this.name=name;
        this.location=location;
        this.IPAddress = IPAddress;
        this.description=description;
        this.connectors=connectors;
        this.meta=meta;
    }

    @Override
    public String toString() {
        return "NodeInfo{" +
                "name=" + name +
                ", location='" + location + '\'' +
                ", IPAddress='" + IPAddress + '\'' +
                ", description='" + description + '\'' +
                ", connectors='" + connectors + '\'' +
                ", meta='" + meta + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIPAddress() {
        return IPAddress;
    }

    public void setIPAddress(String IPAddress) {
        this.IPAddress = IPAddress;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getConnectors() {
        return connectors;
    }

    public void setConnectors(String connectors) {
        this.connectors = connectors;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }
}