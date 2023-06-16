package com.hldspm.server.database.data_processor.maps_processor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.hldspm.server.ServerApplication;
import com.hldspm.server.database.mappers.CountRowMapper;
import com.hldspm.server.ftp_server.cfg.FtpConstants;
import com.hldspm.server.models.CountModel;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

public class MapsGetter {


    /**Generates SQL query to get count of the map*/
    protected String getIdentificationQuery(String game, String name){
        return "SELECT COUNT(*) FROM maps WHERE game= '" + game + "' AND name='"+name+"';";
    }

    /**Checks if there is the specified map in the database*/
    protected boolean availabilityCheck(String game, String name){
        String sql = this.getIdentificationQuery(game, name);
        RowMapper<CountModel> rowMapper = new CountRowMapper();
        List<CountModel> count = ServerApplication.jdbcTemplate.query(sql, rowMapper);
        return count.get(0).getCount() > 0;
    }

    /**Generates FTP-link for the map*/
    protected String generateFtpLink(String engine, String game, String name){
        return FtpConstants.FTP_LINK_INIT + engine + '/' + "maps/"+game+'/'+name+".tar.gz";
    }

    /**Processes map finding and returns the response JSON*/
    public String processGetting(String game, String name, String engine){
        Gson curGson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject response = new JsonObject();
        if (this.availabilityCheck(game, name)){
            response.addProperty("status", 200);
            response.addProperty("link", generateFtpLink(engine, game, name));
            return curGson.toJson(response);
        }
        response.addProperty("status", 404);
        response.addProperty("link", "none");
        return curGson.toJson(response);

    }
}
