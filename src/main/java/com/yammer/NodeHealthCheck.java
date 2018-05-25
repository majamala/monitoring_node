package com.yammer;

import com.codahale.metrics.health.HealthCheck;

import java.util.List;

public class NodeHealthCheck extends HealthCheck {

    private static final String HEALTHY = "The Dropwizard node Service is healthy for read and write";
    private static final String UNHEALTHY = "The Dropwizard node Service is not healthy. ";
    private static final String MESSAGE_PLACEHOLDER = "{}";

    private final SensorReadingsService sensorReadingsService;

    public NodeHealthCheck(SensorReadingsService sensorReadingsService) {
        this.sensorReadingsService=sensorReadingsService;
    }

    @Override
    public Result check() throws Exception {

        String mySqlHealthStatus = sensorReadingsService.performHealthCheck();

        if (mySqlHealthStatus == null) {
            return Result.healthy(HEALTHY);
        } else {
            return Result.unhealthy(UNHEALTHY + MESSAGE_PLACEHOLDER, mySqlHealthStatus);
        }
    }
}



