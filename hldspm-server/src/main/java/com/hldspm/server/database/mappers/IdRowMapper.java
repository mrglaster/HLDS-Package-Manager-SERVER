package com.hldspm.server.database.mappers;

import com.hldspm.server.models.CountModel;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class IdRowMapper implements RowMapper<CountModel> {
    @Override
    public CountModel mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new CountModel(rs.getLong(1));
    }
}
