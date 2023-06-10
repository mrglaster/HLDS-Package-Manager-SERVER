package com.hldspm.server.database.mappers;

import com.hldspm.server.models.EngineModel;
import com.hldspm.server.models.MapModel;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MapRowMapper implements RowMapper<MapModel> {
    @Override
    public MapModel mapRow(ResultSet rs, int rowNum) throws SQLException {
        MapModel mm = new MapModel();
        mm.setId(rs.getLong("id"));
        mm.setEngine(rs.getInt("engine"));
        mm.setGame(rs.getString("game"));
        mm.setGameMode(rs.getString("mode"));
        mm.setName(rs.getString("name"));
        return mm;
    }
}
