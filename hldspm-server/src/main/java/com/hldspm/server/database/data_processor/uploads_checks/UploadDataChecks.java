package com.hldspm.server.database.data_processor.uploads_checks;
import com.hldspm.server.ServerApplication;
import com.hldspm.server.constants.MainConstants;

public class UploadDataChecks {

    //TODO Add modules support

    private static String generateBasicCheckQuery(String table, String name, String game){
        return "SELECT COUNT(*) FROM " + table + " WHERE name='"+name +"' AND game='" + game + "';";
    }

    private static boolean isValidGame(String game){
        return MainConstants.games.contains(game);
    }

    private static boolean isValidUploadType(String upload){
        return MainConstants.uploadTypes.contains(upload);
    }

    private static String generateMapCheckQuery(String name, String game){
        return generateBasicCheckQuery("maps", name, game);
    }

    private static String generatePluginCheckQuery(String name, String game){
        return generateBasicCheckQuery("plugins", name, game);
    }


    public static boolean isNameAvailable(String uploadType, String game, String name){
        if (!isValidGame(game) || !isValidUploadType(uploadType)) return false;
        String query = switch (uploadType) {
            case "plugin" -> generatePluginCheckQuery(name, game);
            case "map" -> generateMapCheckQuery(name, game);
            default -> "";
        };
        return ServerApplication.jdbcTemplate.queryForObject(query, Integer.class) == 0;
    }
}
