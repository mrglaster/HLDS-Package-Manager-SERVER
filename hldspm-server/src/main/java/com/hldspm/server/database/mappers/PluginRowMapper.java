package com.hldspm.server.database.mappers;

import com.hldspm.server.models.PluginModel;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**Class for plguin getting database response parsing*/
public class PluginRowMapper implements RowMapper<PluginModel> {
    @Override
    public PluginModel mapRow(ResultSet rs, int rowNum) throws SQLException {
        PluginModel pm = new PluginModel();
        pm.setId(rs.getLong("id"));
        pm.setEngine(rs.getInt("engine"));
        pm.setGame(rs.getString("game"));
        pm.setName(rs.getString("name"));
        pm.setTime(rs.getTimestamp("time"));
        return pm;

    }
}
