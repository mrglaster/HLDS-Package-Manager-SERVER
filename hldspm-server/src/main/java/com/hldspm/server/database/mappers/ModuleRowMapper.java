package com.hldspm.server.database.mappers;

import com.hldspm.server.models.ModuleModel;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ModuleRowMapper implements RowMapper<ModuleModel> {
    @Override
    public ModuleModel mapRow(ResultSet rs, int rowNum) throws SQLException {
        ModuleModel mm = new ModuleModel();
        mm.setId(rs.getLong("id"));
        mm.setEngine(rs.getInt("engine"));
        mm.setGame(rs.getString("game"));
        mm.setPlatform(rs.getString("platform"));
        mm.setName(rs.getString("name"));
        return mm;
    }
}
