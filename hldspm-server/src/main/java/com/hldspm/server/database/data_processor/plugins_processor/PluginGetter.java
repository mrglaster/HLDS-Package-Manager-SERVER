package com.hldspm.server.database.data_processor.plugins_processor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.hldspm.server.ServerApplication;
import com.hldspm.server.database.data_processor.maps_processor.MapsGetter;
import com.hldspm.server.database.mappers.PluginRowMapper;
import com.hldspm.server.ftp_server.cfg.FtpConstants;
import com.hldspm.server.models.PluginModel;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

//TODO Избавиться от RowMapper

/**Provides functions for plugins getting*/
public class PluginGetter extends MapsGetter{

    /**Generates SQL query for plugin getting*/
    @Override
    protected String getIdentificationQuery(String game, String name) {
        if (name.contains("%")){
            return "SELECT * FROM plugins WHERE game= '" + game + "' AND name='"+name+"';";
        }
        return "SELECT * FROM plugins WHERE game= '" + game + "' AND name LIKE '"+name+"%' ORDER BY time DESC LIMIT 1;";
    }

    /**Generates FTP-link for plugin*/
    @Override
    protected String generateFtpLink(String engine, String game, String name) {
        return FtpConstants.FTP_LINK_INIT + engine + '/' + "plugins/"+game+'/'+name+".tar.gz";
    }

    /**Processes plugin getting*/
    @Override
    public String processGetting(String game, String name, String engine){
        Gson curGson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject response = new JsonObject();
        String sql = getIdentificationQuery(game, name);
        RowMapper<PluginModel> rowMapper = new PluginRowMapper();
        List<PluginModel> plugins = ServerApplication.jdbcTemplate.query(sql, rowMapper);
        if (plugins.size() == 0){
            response.addProperty("status", 404);
            response.addProperty("link", "none");
            return curGson.toJson(response);
        }
        response.addProperty("status", 200);
        response.addProperty("link", generateFtpLink(engine, game, plugins.get(0).getName()));
        return curGson.toJson(response);
    }

}
