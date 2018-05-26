package com.yammer;

import com.codahale.metrics.health.HealthCheck;
import io.dropwizard.Application;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.jdbi.DBIHealthCheck;
import io.dropwizard.setup.Environment;
import javax.sql.DataSource;
import javax.ws.rs.client.Client;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;


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

    }

    public static void main(String[] args) throws Exception {
        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        new App().run(args);


    }

}