package com.yammer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SensorDB {

    public static HashMap<Integer, SensorReading> sensorTemp = new HashMap<>();
    public static HashMap<Integer, SensorReading> sensorCO2 = new HashMap<>();

    public static List<SensorInfo> sensors = new ArrayList<>();
/**
    static {
        sensorReadings.put(1, new SensorReading(1, "sensor_temp", "4/18/18 8:14 PM", 22, "°C"));
        sensorReadings.put(2, new SensorReading(2, "sensor_temp", "4/19/18 3:14 PM", 18, "°C"));
        sensorReadings.put(3, new SensorReading(3, "sensor_temp", "4/20/18 10:14 AM", 15, "°C"));
    }
**/

public static void runTempSensor () throws InterruptedException{
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    Random r = new Random();
    int rangeMin = 10;
    int rangeMax = 30;
    int id = 0;

    while (true) {

        id++;
        LocalDateTime now = LocalDateTime.now();
        int randomValue = r.nextInt((rangeMax - rangeMin) + 1) + rangeMin;

        sensorTemp.put(id, new SensorReading(id, "temp_sensor", dtf.format(now).toString(), randomValue, "°C"));
        //Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        //System.out.println(timestamp);

        Thread.sleep(5000);
    }
}

    public static void runCO2Sensor () throws InterruptedException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        Random r = new Random();
        int rangeMin = 0;
        int rangeMax = 9;
        int id = 0;

        while(true) {

            id++;
            LocalDateTime now = LocalDateTime.now();
            int randomValue = r.nextInt((rangeMax - rangeMin) + 1) + rangeMin;

            sensorCO2.put(id, new SensorReading(id, "co2_sensor", dtf.format(now).toString(), randomValue, "°C"));

            Thread.sleep(7000);
        }

    }

    public static List<SensorReading> getSensorTemp() {
        return new ArrayList<SensorReading>(sensorTemp.values());
    }

    public static List<SensorReading> getSensorCO2() {
        return new ArrayList<SensorReading>(sensorCO2.values());
    }

    public static List<SensorReading> getSensorTempByDate(String startDate, String endDate) throws ParseException {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
        Date dateDate = format.parse(startDate);
        long miliStartDate=dateDate.getTime();
        long miliEndDate;
        if (endDate.isEmpty()) {
            miliEndDate = System.currentTimeMillis();
        } else {
            dateDate = format.parse(endDate);
            miliEndDate = dateDate.getTime();
        }
        List<SensorReading> sensorReadingList = new ArrayList<>();

        for(SensorReading s: getSensorTemp()) {
            format = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
            Date readingDate = format.parse(s.getDate());
            long miliReadingDate = readingDate.getTime();

            if(miliReadingDate >= miliStartDate && miliReadingDate<= miliEndDate) {
                sensorReadingList.add(s);
            }
        }
    return sensorReadingList;
    }

    public static List<SensorInfo> getSensors() {
    return sensors;
    }
/**
    public static SensorReading getSensorByName(String name) {
        return sensorReadings.get(name);
    }

    public static void updateSensor(Integer id, SensorReading sensor) {
        sensorReadings.put(id, sensor);
    }

    public static void removeSensor(Integer id) {
        sensorReadings.remove(id);
    }**/
}