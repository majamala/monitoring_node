package com.yammer;

import java.util.ArrayList;

import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Produces(MediaType.TEXT_PLAIN)
@Path("/client/")
public class RESTClientController
{
    private Client client;

    public RESTClientController(Client client) {
        this.client = client;
        Config cfg = new Config();

        NodeRegister nodeRegister = new NodeRegister(cfg.getProperty("name"),cfg.getProperty("location"),App.ip.getHostAddress(), cfg.getProperty("description"), cfg.getProperty("connectors"), cfg.getProperty("meta"));
     /**   WebTarget webTarget = client.target ("http://10.19.128.213:8080/nodeRegister");
        Response response = webTarget.request().post(Entity.json(nodeRegister));
**/
    }

    @GET
    @Path("/employees/")
    public String getEmployees()
    {
        //Do not hard code in your application
        WebTarget webTarget = client.target("http://10.19.128.213:8080/employees");
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();
        @SuppressWarnings("rawtypes")
        ArrayList employees = response.readEntity(ArrayList.class);
        return employees.toString();
    }

    @GET
    @Path("/employees/{id}")
    public String getEmployeeById(@PathParam("id") int id)
    {
        //Do not hard code in your application
        WebTarget webTarget = client.target("http://10.19.128.213/employees/"+id);
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();
        /**Sensor sensor = response.readEntity(Sensor.class);
        return sensor.toString();**/
        return response.toString();
    }

    @POST
    @Path("/nodeRegister")
    public Response postNodeRegister(NodeRegister nodeRegister)
    {
        WebTarget webTarget = client.target ("http://10.19.128.213:8080/nodeRegister");
        Response response = webTarget.request().post(Entity.json(nodeRegister));
        return response;
    }
}
