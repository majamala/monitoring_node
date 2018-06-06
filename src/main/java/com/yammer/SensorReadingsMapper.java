package com.yammer;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SensorReadingsMapper implements ResultSetMapper <SensorReading> {
    private static final String name = "name";
    private static final String date = "date";
    private static final String value = "value";
    private static final String unit = "unit";

    public SensorReading map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new SensorReading(resultSet.getString(name), resultSet.getString(date), resultSet.getInt(value), resultSet.getString(unit));
    }
}
