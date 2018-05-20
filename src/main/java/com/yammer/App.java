package com.yammer;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import javax.ws.rs.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.prefs.Preferences;


public class App extends Application<Configuration> {
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
    public static InetAddress ip;

    @Override
    public void initialize(Bootstrap<Configuration> b) {
    }

    @Override
    public void run(Configuration c, Environment e) throws Exception {
        LOGGER.info("Registering REST resources");
        final Client client = new JerseyClientBuilder(e).build("DemoRESTClient");
        e.jersey().register(new SensorRESTController(e.getValidator(), client));

        e.jersey().register(new RESTClientController(client));
    }

    public static void main(String[] args) throws Exception {
        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        new App().run(args);


        Callable<Void> callable1 = new Callable<Void>()
        {
            @Override
            public Void call() throws Exception
            {
                SensorDB.runTempSensor();
                return null;
            }
        };

        Callable<Void> callable2 = new Callable<Void>()
        {
            @Override
            public Void call() throws Exception
            {
                SensorDB.runCO2Sensor();
                return null;
            }
        };

        List<Callable<Void>> taskList = new ArrayList<Callable<Void>>();
        taskList.add(callable1);
        taskList.add(callable2);

        ExecutorService executor = Executors.newFixedThreadPool(2);

        try
        {
            executor.invokeAll(taskList);
        }
        catch (InterruptedException ie)
        {

        }

    }

}