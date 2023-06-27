package com.hldspm.server.database.data_processor.maps_processor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.hldspm.server.ServerApplication;
import com.hldspm.server.ftp_server.cfg.FtpConstants;

public class MapsGetter {


    /**Generates SQL query to get count of the map*/
    protected String getIdentificationQuery(String game, String name){
        return "SELECT COUNT(*) FROM maps WHERE game= '" + game + "' AND name='"+name+"';";
    }

    /**Checks if there is the specified map in the database*/
    protected boolean hasMap(String game, String name){
        String sql = this.getIdentificationQuery(game, name);
        return ServerApplication.jdbcTemplate.queryForObject(sql, Integer.class) != 0;
    }

    /**Generates FTP-link for the map*/
    protected String generateFtpLink(String engine, String game, String name){
        return FtpConstants.FTP_LINK_INIT + engine + '/' + "maps/"+game+'/'+name+".tar.gz";
    }

    /**Processes map finding and returns the response JSON*/
    public String processGetting(String game, String name, String engine){
        Gson curGson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject response = new JsonObject();
        if (this.hasMap(game, name)){
            response.addProperty("status", 200);
            response.addProperty("link", generateFtpLink(engine, game, name));
            return curGson.toJson(response);
        }
        response.addProperty("status", 404);
        response.addProperty("link", "none");
        return curGson.toJson(response);

    }
}
