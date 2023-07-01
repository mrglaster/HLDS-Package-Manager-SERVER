package com.hldspm.server.database.mappers;
import com.hldspm.server.models.SimpleRecordModel;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SimpleRecordRowMapper implements RowMapper<SimpleRecordModel> {
    @Override
    public SimpleRecordModel mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new SimpleRecordModel(rs.getLong("id"), rs.getString("name"));
    }
}
