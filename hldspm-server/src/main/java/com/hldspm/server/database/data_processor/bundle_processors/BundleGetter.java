package com.hldspm.server.database.data_processor.bundle_processors;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hldspm.server.ServerApplication;
import com.hldspm.server.connections.requests.get_requests.BundleGetRequest;
import com.hldspm.server.database.mappers.BundleRowMapper;
import com.hldspm.server.ftp_server.cfg.FtpConstants;
import com.hldspm.server.models.BundleModel;
import org.springframework.jdbc.core.RowMapper;
import java.util.List;

// TODO Оптимизировать запросы
// TODO Сделать загрузку последних версий плагинов
public class BundleGetter {


    private static int getNumberByType(String type){
        switch(type){
            case "plugin" -> {
                return 1;
            }
            case "map" -> {
                return 2;
            }
            case "module" -> {
                return 3;
            }
        }
        return 1;
    }

    public static String processBundleGetting(BundleGetRequest request){
        JsonObject response = new JsonObject();
        Gson curGson = new GsonBuilder().setPrettyPrinting().create();
        if(!request.isValidRequest()) {
            response.addProperty("status", 400);
            response.add("elements", new JsonArray());
            return curGson.toJson(response);
        }
        String query = "SELECT * FROM bundles WHERE engine="+request.engineToId() + " AND content_type=" + getNumberByType(request.getType()) + " AND name='" + request.getName() + "';";
        RowMapper<BundleModel> rowMapper = new BundleRowMapper();
        List<BundleModel> bundle = ServerApplication.jdbcTemplate.query(query, rowMapper);
        if (bundle.size() == 0){
            response.addProperty("status", 404);
            response.add("elements", new JsonArray());
            return curGson.toJson(response);
        }
        String resources = bundle.get(0).getPluginIds().replace('{', '(').replace('}', ')').replace(",", ", ");
        String searching = request.getType() + 's';
        String newQuery = "SELECT name FROM " + searching + " WHERE id IN " + resources;

        List<String> names = ServerApplication.jdbcTemplate.queryForList(newQuery, String.class);
        JsonArray elements = new JsonArray();
        for (String model : names){
            elements.add(FtpConstants.FTP_LINK_INIT  + request.getEngine() + '/' + searching + '/' + request.getGame() + '/' + model + ".tar.gz" );
        }
        response.addProperty("status", 200);
        response.add("elements", elements);
        return curGson.toJson(response);
    }
}
