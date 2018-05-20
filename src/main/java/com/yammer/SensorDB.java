package com.yammer;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SensorDB {

    public static HashMap<Integer, Sensor> sensorTemp = new HashMap<>();
    public static HashMap<Integer, Sensor> sensorCO2 = new HashMap<>();

    public static List<Sensor> sensors = new ArrayList<>();
/**
    static {
        sensors.put(1, new Sensor(1, "sensor_temp", "4/18/18 8:14 PM", 22, "°C"));
        sensors.put(2, new Sensor(2, "sensor_temp", "4/19/18 3:14 PM", 18, "°C"));
        sensors.put(3, new Sensor(3, "sensor_temp", "4/20/18 10:14 AM", 15, "°C"));
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

        sensorTemp.put(id, new Sensor (id, "temp_sensor", dtf.format(now).toString(), randomValue, "°C"));
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

            sensorCO2.put(id, new Sensor (id, "co2_sensor", dtf.format(now).toString(), randomValue, "°C"));

            Thread.sleep(7000);
        }

    }

    public static List<Sensor> getSensorTemp() {
        return new ArrayList<Sensor>(sensorTemp.values());
    }

    public static List<Sensor> getSensorCO2() {
        return new ArrayList<Sensor>(sensorCO2.values());
    }

    public static List<Sensor> getSensorTempByDate(String startDate, String endDate) throws ParseException {

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
        List<Sensor> sensorList = new ArrayList<>();

        for(Sensor s: getSensorTemp()) {
            format = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
            Date readingDate = format.parse(s.getDate());
            long miliReadingDate = readingDate.getTime();

            if(miliReadingDate >= miliStartDate && miliReadingDate<= miliEndDate) {
                sensorList.add(s);
            }
        }
    return sensorList;
    }

    public static List<Sensor> getSensors() {
    return sensors;
    }
/**
    public static Sensor getSensorByName(String name) {
        return sensors.get(name);
    }

    public static void updateSensor(Integer id, Sensor sensor) {
        sensors.put(id, sensor);
    }

    public static void removeSensor(Integer id) {
        sensors.remove(id);
    }**/
}