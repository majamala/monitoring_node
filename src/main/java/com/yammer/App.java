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
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Enumeration;


public class App extends Application<NodeConfiguration> {
    private static final String SQL = "mysql";
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
    public static InetAddress ip;


    @Override
    public void run(NodeConfiguration c, Environment e) throws Exception {

        // Datasource configuration
        final DataSource dataSource =
                c.getDataSourceFactory().build(e.metrics(), SQL);
        DBI dbi = new DBI(dataSource);

        LOGGER.info("Registering REST resources");
        final Client client = new JerseyClientBuilder(e).build("NodeRestClient");
        e.jersey().register(new NodeRestController(e.getValidator(), client, dbi.onDemand(SensorReadingsService.class), new RuleEngine()));


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

        ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
        final URL[] localKieConfUrls = new URL[]{
                ClassLoader.getSystemResource("kie_core.conf"),
                ClassLoader.getSystemResource("kie_compiler.conf")
        };
        ClassLoader newClassLoader = new ClassLoader(oldClassLoader) {

            private final URL[] kieConfUrls = localKieConfUrls;

            @Override
            public Enumeration<URL> getResources(String name) throws IOException {
                if ("META-INF/kie.conf".equals(name)) {
                    return new Enumeration<URL>() {
                        int index;

                        public boolean hasMoreElements() {
                            return index < kieConfUrls.length;
                        }

                        public URL nextElement() {
                            return kieConfUrls[index++];
                        }
                    };
                }
                return super.getResources(name);
            }

        };
        Thread.currentThread().setContextClassLoader(newClassLoader);

        new App().run(args);
        Receiver.getInstance().receive();


    }

}