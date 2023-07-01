package com.hldspm.server.database.data_processor.users_processor;

import com.hldspm.server.ServerApplication;

import java.util.Objects;

public class UserChecks {
    /**Checks if admin's token corresponds the superusers' token */
    public static boolean isSudoUser(String token){
        return Objects.equals(token, ServerApplication.configure.getRepoAdminToken());
    }

    public static boolean isNameFree(String name){
        String query = "SELECT COUNT(*) FROM uploaders WHERE name='"+name+"';";
        return ServerApplication.jdbcTemplate.queryForObject(query, Integer.class) == 0;
    }


}
