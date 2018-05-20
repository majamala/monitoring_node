package com.yammer;

import javax.validation.constraints.NotNull;
import java.net.InetAddress;

public class NodeRegister {

    @NotNull
    private String name;
    private String location;
    @NotNull
    private String IPAdress;
    private String description;
    private String connectors;
    private String meta;

    public NodeRegister(){

    }

    public NodeRegister(String IPAddress) {
        this.IPAdress = IPAddress;
    }

    public NodeRegister(String name, String location, String IPAdress, String description, String connectors, String meta) {
        this.name=name;
        this.location=location;
        this.IPAdress = IPAdress;
        this.description=description;
        this.connectors=connectors;
        this.meta=meta;
    }

    @Override
    public String toString() {
        return "NodeRegister{" +
                "name=" + name +
                ", location='" + location + '\'' +
                ", IPAdress='" + IPAdress + '\'' +
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

    public String getIPAdress() {
        return IPAdress;
    }

    public void setIPAdress(String IPAdress) {
        this.IPAdress = IPAdress;
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