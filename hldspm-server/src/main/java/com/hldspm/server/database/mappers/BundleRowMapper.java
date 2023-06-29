package com.hldspm.server.database.mappers;

import com.hldspm.server.models.BundleModel;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

/**Class for bundle getting results from the database*/
public class BundleRowMapper implements RowMapper<BundleModel> {
    @Override
    public BundleModel mapRow(ResultSet rs, int rowNum) throws SQLException {
        BundleModel bm = new BundleModel();
        bm.setId(rs.getLong("id"));
        bm.setName(rs.getString("name"));
        bm.setPluginIds(rs.getString("elements"));
        return bm;
    }
}
