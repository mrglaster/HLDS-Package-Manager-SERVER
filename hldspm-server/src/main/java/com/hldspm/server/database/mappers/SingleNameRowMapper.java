package com.hldspm.server.database.mappers;
import com.hldspm.server.models.SingleNameModel;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SingleNameRowMapper implements RowMapper<SingleNameModel> {
    @Override
    public SingleNameModel mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new SingleNameModel(rs.getString("name"));
    }
}
