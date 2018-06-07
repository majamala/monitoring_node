package com.yammer;

import org.fusesource.mqtt.client.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

public class Receiver {

    private FutureConnection connection;
    private static Receiver instance;

    public Receiver() throws URISyntaxException {

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


    public static Receiver getInstance() throws URISyntaxException {
        if(instance == null) {
            instance = new Receiver();
        }
        return instance;
    }

    public void receive() {
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    Message message = null;
                    Future<Message> receive;
                    try {
                        receive = connection.receive();
                        message = receive.await();
                        message.ack();
                        System.out.println(new String(message.getPayload(), "UTF-8"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t1.start();

    }
}
