package com.yammer;

import com.codahale.metrics.health.HealthCheck;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.jdbi.DBIHealthCheck;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import javax.sql.DataSource;
import javax.ws.rs.client.Client;

import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class App extends Application<NodeConfiguration> {
    private static final String SQL = "mysql";
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
    public static InetAddress ip;
    private static final String NODE_SERVICE = "NodeHealthCheck";


    @Override
    public void run(NodeConfiguration c, Environment e) {

        // Datasource configuration
        final DataSource dataSource =
                c.getDataSourceFactory().build(e.metrics(), SQL);
        DBI dbi = new DBI(dataSource);

        LOGGER.info("Registering REST resources");
        final Client client = new JerseyClientBuilder(e).build("NodeRestClient");
        e.jersey().register(new NodeRestController(e.getValidator(), client, dbi.onDemand(SensorReadingsService.class)));


        //Register healthcheck
        HealthCheck appHealthCheck = new DBIHealthCheck(dbi, "SELECT 1");
        e.healthChecks().register("db", appHealthCheck);

        // Register resources
        //e.jersey().register(new NodeRestController(dbi.onDemand(SensorReadingsService.class)));
    }

    public static void main(String[] args) throws Exception {
        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        new App().run(args);



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