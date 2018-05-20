package com.yammer;

import com.google.gson.Gson;

import java.text.ParseException;
import java.util.*;

import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;


@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
public class NodeRestController {

    private final Validator validator;
    private Client client;


    public NodeRestController(Validator validator, Client client) {
        this.validator = validator;
        this.client = client;
        Config cfg = new Config();

        NodeInfo nodeInfo = new NodeInfo(cfg.getProperty("name"),cfg.getProperty("location"),App.ip.getHostAddress(), cfg.getProperty("description"), cfg.getProperty("connectors"), cfg.getProperty("meta"));
        /*   WebTarget webTarget = client.target ("http://10.19.128.213:8080/nodeRegister");
         Response response = webTarget.request().post(Entity.json(nodeInfo));
        */
    }

    @GET
    @Path("/co2")
    public Response getSensorsCo2() {
        return Response.ok(SensorDB.getSensorCO2()).build();
        //return Response.ok(App.sensorReadings_co2).build();

    }

    @GET
    @Path("/temp")
    public Response getSensorById(@DefaultValue("") @QueryParam("startDate") String startDate,
                                  @DefaultValue("") @QueryParam("endDate") String endDate) throws ParseException {

        if (startDate.isEmpty()) {
            return Response.ok(SensorDB.getSensorTemp()).build();
        } else {
            List<SensorReading> sensorReadings = SensorDB.getSensorTempByDate(startDate,endDate);
            if (sensorReadings.size() != 0)
                return Response.ok(sensorReadings).build();
            else
                return Response.status(Status.NOT_FOUND).build();
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
            return Response.ok(sensorInfo).build();
        }
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