package com.hldspm.server.database.data_processor.uploads_checks;

import com.hldspm.server.ServerApplication;
import com.hldspm.server.database.mappers.CountRowMapper;
import com.hldspm.server.models.CountModel;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

/**Class implementing uploader's token verification methods*/
public class UploaderVerification {

    /**Generates SQL query for getting of amount of records with specified token*/
    private static String generateUploaderCheckQuery(String token){
        return "SELECT COUNT(*) FROM uploaders where token='"+token+"'";
    }

    /**Checks if the uploader is valid*/
    public static boolean isValidUploader(String token){
        String query = generateUploaderCheckQuery(token);
        RowMapper<CountModel> rowMapper = new CountRowMapper();
        List<CountModel> count = ServerApplication.jdbcTemplate.query(query, rowMapper);
        return count.get(0).getCount() > 0;
    }
}

