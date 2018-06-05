package com.yammer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.jetty.http.HttpStatus;
import org.fusesource.mqtt.client.*;

import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.time.Clock;
import java.util.List;


@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
public class NodeRestController {

    private Validator validator;
    private Client client;
    private SensorReadingsService sensorReadingService;


    public NodeRestController(Validator validator, Client client, SensorReadingsService sensorReadingsService) throws Exception {
        this.validator = validator;
        this.client = client;
        Config cfg = new Config();
        MQTT mqtt = new MQTT();
        mqtt.setHost("localhost", 1883);


        NodeInfo nodeInfo = new NodeInfo(cfg.getProperty("name"),cfg.getProperty("location"),App.ip.getHostAddress(), cfg.getProperty("description"), cfg.getProperty("connectors"), cfg.getProperty("meta"));
        WebTarget webTarget = client.target ("http://10.19.128.213:8080/nodeRegister");
        Response response = webTarget.request().post(Entity.json(nodeInfo));

        FutureConnection connection = mqtt.futureConnection();
        connection.connect();
        connection.subscribe(new Topic[]{new Topic("Alarm", QoS.AT_LEAST_ONCE)});
        Future<Message> receive = connection.receive();
        connection.publish("Alarm", "Alaaaaaarm!".getBytes(), QoS.AT_LEAST_ONCE, false);

        this.sensorReadingService=sensorReadingsService;
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
        sensorReadingService.insert(sensorReading.getId(), sensorReading.getName(), sensorReading.getDate(), sensorReading.getValue(), sensorReading.getUnit());
        return Response.ok(sensorReading).build();
    }


/**
    @GET
    @Path("/{id}")
    public Response getSensorById(@PathParam("id") Integer id) {
        SensorReading sensor = SensorDB.getSensorById(id);
        if (sensor != null)
            return Response.ok(sensor).build();
        else
            return Response.status(Status.NOT_FOUND).build();
    }


    @GET
    @Path("/{name}")
    public Response getSensorByName(@PathParam("name") String name) {
        SensorReading sensor = SensorDB.getSensorByName(name);
        if (sensor != null)
            return Response.ok(sensor).build();
        else
            return Response.status(Status.NOT_FOUND).build();
    }


    @POST
    public Response createSensor(SensorReading sensor) throws URISyntaxException {
        // validation
        Set<ConstraintViolation<SensorReading>> violations = validator.validate(sensor);
        SensorReading e = SensorDB.getSensorById(sensor.getId());
        if (violations.size() > 0) {
            ArrayList<String> validationMessages = new ArrayList<String>();
            for (ConstraintViolation<SensorReading> violation : violations) {
                validationMessages.add(violation.getPropertyPath().toString() + ": " + violation.getMessage());
            }
            return Response.status(Status.BAD_REQUEST).entity(validationMessages).build();
        }
        if (e != null) {
            SensorDB.updateSensor(sensor.getId(), sensor);
            return Response.created(new URI("/sensorReadings/" + sensor.getId()))
                    .build();
        } else
            return Response.status(Status.NOT_FOUND).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateSensorById(@PathParam("id") Integer id, SensorReading sensor) {
        // validation
        Set<ConstraintViolation<SensorReading>> violations = validator.validate(sensor);
        SensorReading e = SensorDB.getSensorById(sensor.getId());
        if (violations.size() > 0) {
            ArrayList<String> validationMessages = new ArrayList<String>();
            for (ConstraintViolation<SensorReading> violation : violations) {
                validationMessages.add(violation.getPropertyPath().toString() + ": " + violation.getMessage());
            }
            return Response.status(Status.BAD_REQUEST).entity(validationMessages).build();
        }
        if (e != null) {
            sensor.setId(id);
            SensorDB.updateSensor(id, sensor);
            return Response.ok(sensor).build();
        } else
            return Response.status(Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{id}")
    public Response removeSensorById(@PathParam("id") Integer id) {
        SensorReading sensor = SensorDB.getSensorById(id);
        if (sensor != null) {
            SensorDB.removeSensor(id);
            return Response.ok().build();
        } else
            return Response.status(Status.NOT_FOUND).build();
    }**/
}