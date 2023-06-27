package com.hldspm.server.database.data_processor.uploads_checks;
import com.hldspm.server.ServerApplication;


/**Class implementing uploader's token verification methods*/
public class UploaderVerification {

    /**Generates SQL query for getting of amount of records with specified token*/
    private static String generateUploaderCheckQuery(String token){
        return "SELECT COUNT(*) FROM uploaders where token='"+token+"'";
    }

    /**Checks if the uploader is valid*/
    public static boolean isValidUploader(String token){
        String query = generateUploaderCheckQuery(token);
        return ServerApplication.jdbcTemplate.queryForObject(query, Integer.class) != 0;
    }
}

