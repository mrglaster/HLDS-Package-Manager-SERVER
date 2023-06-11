package com.hldspm.server.database.mappers;

import com.hldspm.server.models.BundleModel;
import org.springframework.jdbc.core.RowMapper;

import java.lang.reflect.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class BundleRowMapper implements RowMapper<BundleModel> {
    @Override
    public BundleModel mapRow(ResultSet rs, int rowNum) throws SQLException {
        BundleModel bm = new BundleModel();
        bm.setId(rs.getLong("id"));
        bm.setName(rs.getString("name"));
        bm.setPluginIds((Array) rs.getArray("plugins").getArray());
        return bm;
    }
}
