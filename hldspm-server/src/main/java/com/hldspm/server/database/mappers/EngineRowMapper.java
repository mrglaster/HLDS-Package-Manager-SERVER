package com.hldspm.server.database.mappers;
import com.hldspm.server.models.EngineModel;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EngineRowMapper implements RowMapper<EngineModel> {
    @Override
    public EngineModel mapRow(ResultSet rs, int rowNum) throws SQLException {

        EngineModel engine = new EngineModel();
        engine.setId(rs.getInt("id"));
        engine.setName(rs.getString("name"));
        return engine;

    }
}
