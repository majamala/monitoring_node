package com.yammer;

import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

@RegisterMapper(SensorReadingsMapper.class)
public interface SensorReadingDao {

    @SqlQuery("select * from sensorreadings;")
    public List<SensorReading> getSensorReadings();

    @SqlQuery ("")

}
