package com.yammer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Properties;


@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
public class NodeRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    private Validator validator;
    private Client client;
    private SensorReadingsService sensorReadingService;
    private RuleEngine ruleEngine;


    public NodeRestController(Validator validator, Client client, SensorReadingsService sensorReadingsService, RuleEngine ruleEngine) throws Exception {
        this.validator = validator;
        this.client = client;
        Config cfg = new Config();

        Properties configFile = new java.util.Properties();
        try {
            configFile.load(this.getClass().getClassLoader().
                    getResourceAsStream("managing_node_config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }


        NodeInfo nodeInfo = new NodeInfo(cfg.getProperty("name"),cfg.getProperty("location"),App.ip.getHostAddress(), cfg.getProperty("description"), cfg.getProperty("connectors"), cfg.getProperty("meta"));
        //WebTarget webTarget = client.target ("http://"+configFile.getProperty("ip_addr_port")+"/nodeRegister");
        //Response response = webTarget.request().post(Entity.json(nodeInfo));

        this.sensorReadingService=sensorReadingsService;
        this.ruleEngine=ruleEngine;

    }


    @GET
    @Path("/sensorReadings/{sensorName}")
    public Representation <List<SensorReading>> getSensorReadings(@PathParam("sensorName") String sensorName,
                                                                  @DefaultValue("") @QueryParam("startDate") String startDate,
                                                                  @DefaultValue("") @QueryParam("endDate") String endDate) throws ParseException {

        if (startDate.isEmpty()) {
            return new Representation<List<SensorReading>>(HttpStatus.OK_200, sensorReadingService.getSensorReadings(sensorName));
        } else if (endDate.isEmpty()) {
            List<SensorReading> sensorReadings = sensorReadingService.getSensorReadingsByStartDate(sensorName, startDate);
            if (sensorReadings.size() != 0)
                return new Representation<List<SensorReading>>(HttpStatus.OK_200, sensorReadings);
            else
                return new Representation<List<SensorReading>>(HttpStatus.NOT_FOUND_404, null);
        } else {
            List<SensorReading> sensorReadings = sensorReadingService.getSensorReadingsByDate(sensorName, startDate, endDate);
            if (sensorReadings.size() != 0)
                return new Representation<List<SensorReading>>(HttpStatus.OK_200, sensorReadings);
            else
                return new Representation<List<SensorReading>>(HttpStatus.NOT_FOUND_404, null);
        }
    }

    @POST
    @Path("/sensors")
    public Response getSensors(String body) {

        Gson gson = new Gson();
        SensorInfo sensorInfo = gson.fromJson(body, SensorInfo.class);

        if (SensorDB.getSensors().contains(sensorInfo)) {
            return Response.status(Status.CONFLICT).build();
        } else {
            SensorDB.addSensor(sensorInfo);
            return Response.ok(sensorInfo).build();
        }
    }

    @GET
    @Path("/sensors")
    public Response getSensors() {
        return Response.ok(SensorDB.getSensors()).build();
    }

    @POST
    @Path("/sensorReadings/{sensorName}")
    public Response postSensorReading(@PathParam("sensorName") String sensorName, String body) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        SensorReading sensorReading = gson.fromJson(body, SensorReading.class);
        sensorReadingService.insert(sensorReading.getName(), sensorReading.getDate(), sensorReading.getValue(), sensorReading.getUnit());
        this.ruleEngine.ruleTest(sensorReading);
        return Response.ok(sensorReading).build();
    }

}