package com.hldspm.server.database.data_processor.bundle_processors;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.hldspm.server.ServerApplication;
import com.hldspm.server.connections.requests.upload_requests.BundleUploadRequest;
import com.hldspm.server.connections.responses.StatusResponses;
import com.hldspm.server.database.data_processor.uploads_checks.UploaderVerification;
import com.hldspm.server.database.dumper.DumpCreator;
import com.hldspm.server.models.SimpleRecordModel;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.hldspm.server.database.data_processor.utils.Utils.wrapWithQuotes;

//TODO огпределить что использовать лучше, имена или id

/**Class processing the bundle upload request*/
public class BundleUploader {
    private static final Gson curGson = new GsonBuilder().setPrettyPrinting().create();

    private static  String generateMapsGettingQuery(BundleUploadRequest request){
        return "SELECT COUNT(*) FROM maps WHERE name IN " + request.getMapsAsDataList();
    }

    private static String generateLatestPluginGetQuery(String game, String name){
        if (!name.contains("%"))  return "SELECT id, name FROM plugins WHERE game= '" + game + "' AND name LIKE '"+name+"%' ORDER BY time DESC LIMIT 1";
        return "SELECT id, name FROM plugins WHERE game= '" + game + "' AND name = '"+name+"' ORDER BY time DESC LIMIT 1";
    }


    private static boolean isBundleNameAvailable(BundleUploadRequest request){
        String query = "SELECT COUNT(*) FROM bundles WHERE name='" + request.getName() + "' AND content_type=" + request.contentTypeToId() + ';';;
        return ServerApplication.jdbcTemplate.queryForObject(query, Integer.class) == 0;
    }


    private static String processMapBundle(BundleUploadRequest request){
       String query = generateMapsGettingQuery(request);
       List<Integer> ids = ServerApplication.jdbcTemplate.queryForList(query, Integer.class);
       JsonObject response = new JsonObject();
       if (ids.size() != request.getElements().size()){
           response.addProperty("status", 400);
           response.addProperty("description", "Request contains unknown resources");
           return curGson.toJson(response);
       }
       StringBuilder arrayLine = new StringBuilder("'{");
       for (int i : ids){
           arrayLine.append(i).append(',');
       }
        arrayLine.deleteCharAt(arrayLine.length() - 1);
        arrayLine.append("}'");
        String insertQuery = "INSERT INTO bundles(content_type, engine, game, name, elements) VALUES (" +  request.contentTypeToId() + ", " + request.engineToId() + ", " + wrapWithQuotes(request.getGame()) + ", " + wrapWithQuotes(request.getName()) + ", " + arrayLine + ");";
        ServerApplication.jdbcTemplate.update(insertQuery);
        DumpCreator.hasChanges = true;
        return StatusResponses.generateSuccessfulUpload();
    }

    private static String processPluginBundle(BundleUploadRequest request){
        StringBuilder pluginsIds = new StringBuilder("'{");
        String game = request.getGame();
        List<String> processedNames = new ArrayList<>();

        for (String name : request.getElements()){
            if (!processedNames.contains(name)){
                processedNames.add(name);
                String getQuery = generateLatestPluginGetQuery(game, name);
                SimpleRecordModel  elem;
                try {
                    elem = ServerApplication.jdbcTemplate.queryForObject(
                            getQuery,
                            new Object[]{},
                            new BeanPropertyRowMapper<>(SimpleRecordModel.class));

                } catch (Exception e){
                    return StatusResponses.generateBadRequestErr();
                }
                if (elem != null) {
                    pluginsIds.append(elem.getId()).append(',');
                }
            }

        }
        pluginsIds.deleteCharAt(pluginsIds.length() - 1);
        pluginsIds.append("}'");
        String insertQuery = "INSERT INTO bundles(content_type, engine, game, name, elements) VALUES (" +  request.contentTypeToId() + ", " + request.engineToId() + ", " + wrapWithQuotes(request.getGame()) + ", " + wrapWithQuotes(request.getName()) + ", " + pluginsIds + ");";
        ServerApplication.jdbcTemplate.update(insertQuery);
        return StatusResponses.generateSuccessfulUpload();
    }

    public static String processBundleUpload(BundleUploadRequest request){
        if (!UploaderVerification.isValidUploader(request.getToken())) {
            return StatusResponses.generateBadTokenError();
        }
        if (!request.isValidRequest()){
            return StatusResponses.generateBadRequestErr();
        }
        if (!isBundleNameAvailable(request)){
            return StatusResponses.generateBadResourceNameError();
        }
        if (Objects.equals(request.getType(), "plugin")){
            return processPluginBundle(request);
        }
        return processMapBundle(request);
    }
}
