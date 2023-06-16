package com.hldspm.server.database.mappers;

import com.hldspm.server.models.UploaderModel;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UploaderRowMapper implements RowMapper<UploaderModel> {
    @Override
    public UploaderModel mapRow(ResultSet rs, int rowNum) throws SQLException {
        UploaderModel um = new UploaderModel();
        um.setId(rs.getLong("id"));
        um.setName(rs.getString("name"));
        um.setToken(rs.getString("token"));
        return um;
    }
}
