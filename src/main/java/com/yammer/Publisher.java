package com.yammer;

import org.fusesource.mqtt.client.FutureConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;

import java.net.URISyntaxException;

public class Publisher {

    private static Publisher instance;

    private FutureConnection connection;
    private Topic topic;


    public Publisher() throws URISyntaxException {
        MQTT mqtt = new MQTT();
        mqtt.setHost("localhost", 1883);
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
