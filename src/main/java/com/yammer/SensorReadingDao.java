package com.yammer;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

@RegisterMapper(SensorReadingsMapper.class)
public interface SensorReadingDao {

    @SqlQuery("select * from sensorreadings where name = :sensorName")
    public List<SensorReading> getSensorReadings(@Bind("sensorName") String sensorName);

    @SqlUpdate("insert into sensorreadings (id, name, date, value, unit) values (:id, :name, :date, :value, :unit)")
    void insert(@Bind("id") int id, @Bind("name") String name, @Bind("date") String date, @Bind("value") int value, @Bind("unit") String unit);

    @SqlQuery("select * from sensorreadings where name = :sensorName and date >= :startDate")
    public List <SensorReading> getSensorReadingsByStartDate (@Bind("sensorName") String sensorName, @Bind ("startDate") String startDate);

    @SqlQuery("select * from sensorreadings where name = :sensorName and date between :startDate and :endDate")
    public List <SensorReading> getSensorReadingsByDate (@Bind("sensorName") String sensorName, @Bind ("startDate") String startDate, @Bind ("endDate") String endDate);
}
