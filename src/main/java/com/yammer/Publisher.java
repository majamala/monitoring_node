package com.yammer;

import org.fusesource.mqtt.client.FutureConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

public class Publisher {

    private static Publisher instance;
    private FutureConnection connection;


    public Publisher() throws URISyntaxException {

        Properties configFile = new java.util.Properties();
        try {
            configFile.load(this.getClass().getClassLoader().
                    getResourceAsStream("mosquitto_config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        MQTT mqtt = new MQTT();
        mqtt.setHost(configFile.getProperty("ip_addr"), Integer.parseInt(configFile.getProperty("port")));
        this.connection = mqtt.futureConnection();
        this.connection.connect();
        this.connection.subscribe(new Topic[]{new Topic("Alarm", QoS.AT_LEAST_ONCE)});
    }

    public static Publisher getInstance() throws URISyntaxException {
        if(instance == null) {
            instance = new Publisher();
        }
        return instance;
    }

    public void publish(String message) {

        connection.publish("Alarm", message.getBytes(), QoS.AT_LEAST_ONCE, false);

    }

}
