package com.yammer;


import java.util.*;

public class SensorDB {


    public static List<SensorInfo> sensors = new ArrayList<>();

    public static void addSensor(SensorInfo sensorInfo) {
    sensors.add(sensorInfo);
    }

    public static List<SensorInfo> getSensors() {
    return sensors;
    }

}