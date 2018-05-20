package com.yammer;

import com.fasterxml.jackson.core.JsonParser;
import com.google.gson.Gson;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;


@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
public class SensorRESTController {

    private final Validator validator;
    private Client client;


    public SensorRESTController(Validator validator, Client client) {
        this.validator = validator;
        this.client = client;
        Config cfg = new Config();

        NodeRegister nodeRegister = new NodeRegister(cfg.getProperty("name"),cfg.getProperty("location"),App.ip.getHostAddress(), cfg.getProperty("description"), cfg.getProperty("connectors"), cfg.getProperty("meta"));
        /*   WebTarget webTarget = client.target ("http://10.19.128.213:8080/nodeRegister");
         Response response = webTarget.request().post(Entity.json(nodeRegister));
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
            List<Sensor> sensors = SensorDB.getSensorTempByDate(startDate,endDate);
            if (sensors.size() != 0)
                return Response.ok(sensors).build();
            else
                return Response.status(Status.NOT_FOUND).build();
        }
    }

    @POST
    @Path("/sensors")
    public Response getSensors(String body) {

        Gson gson = new Gson();
        Sensor sensor = gson.fromJson(body, Sensor.class);

        if (SensorDB.getSensors().contains(sensor)) {
            return Response.status(Status.CONFLICT).build();
        } else {
            return Response.ok(sensor).build();
        }
    }

/**
    @GET
    @Path("/{id}")
    public Response getSensorById(@PathParam("id") Integer id) {
        Sensor sensor = SensorDB.getSensorById(id);
        if (sensor != null)
            return Response.ok(sensor).build();
        else
            return Response.status(Status.NOT_FOUND).build();
    }


    @GET
    @Path("/{name}")
    public Response getSensorByName(@PathParam("name") String name) {
        Sensor sensor = SensorDB.getSensorByName(name);
        if (sensor != null)
            return Response.ok(sensor).build();
        else
            return Response.status(Status.NOT_FOUND).build();
    }


    @POST
    public Response createSensor(Sensor sensor) throws URISyntaxException {
        // validation
        Set<ConstraintViolation<Sensor>> violations = validator.validate(sensor);
        Sensor e = SensorDB.getSensorById(sensor.getId());
        if (violations.size() > 0) {
            ArrayList<String> validationMessages = new ArrayList<String>();
            for (ConstraintViolation<Sensor> violation : violations) {
                validationMessages.add(violation.getPropertyPath().toString() + ": " + violation.getMessage());
            }
            return Response.status(Status.BAD_REQUEST).entity(validationMessages).build();
        }
        if (e != null) {
            SensorDB.updateSensor(sensor.getId(), sensor);
            return Response.created(new URI("/sensors/" + sensor.getId()))
                    .build();
        } else
            return Response.status(Status.NOT_FOUND).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateSensorById(@PathParam("id") Integer id, Sensor sensor) {
        // validation
        Set<ConstraintViolation<Sensor>> violations = validator.validate(sensor);
        Sensor e = SensorDB.getSensorById(sensor.getId());
        if (violations.size() > 0) {
            ArrayList<String> validationMessages = new ArrayList<String>();
            for (ConstraintViolation<Sensor> violation : violations) {
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
        Sensor sensor = SensorDB.getSensorById(id);
        if (sensor != null) {
            SensorDB.removeSensor(id);
            return Response.ok().build();
        } else
            return Response.status(Status.NOT_FOUND).build();
    }**/
}