package com.hldspm.server.database.data_processor.delete_processor;
import com.hldspm.server.ServerApplication;
import com.hldspm.server.connections.requests.delete_request.DeleteResourceRequest;
import com.hldspm.server.connections.responses.StatusResponses;
import com.hldspm.server.database.mappers.SimpleRecordRowMapper;
import com.hldspm.server.ftp_server.cfg.FtpConstants;
import com.hldspm.server.models.SimpleRecordModel;
import org.springframework.jdbc.core.RowMapper;
import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static com.hldspm.server.database.data_processor.users_processor.UserChecks.isSudoUser;

public class DataDeleter {
    private static final List<String> supportedTypes = List.of(new String[]{"wtf", "plugin", "map", "module"});

    private static String generateFileName(DeleteResourceRequest request) {
        StringBuilder fileName = new StringBuilder();
        fileName.append(FtpConstants.getFtpPath())
                .append("/")
                .append(request.getEngine())
                .append("/")
                .append(request.getType())
                .append("s/")
                .append(request.getGame())
                .append("/")
                .append(request.getName())
                .append(".tar.gz");

        return fileName.toString();
    }

    private static int getTypeNumeric(String type) {
        return supportedTypes.indexOf(type);
    }

    private static boolean isSupportedType(String type) {
        return supportedTypes.contains(type);
    }

    private static void deleteEmptyBundles() {
        String bundleCleaner = "DELETE FROM bundles WHERE elements = '{}';";
        ServerApplication.jdbcTemplate.update(bundleCleaner);
    }

    private static String generateSelectorQuery(DeleteResourceRequest request) {
        if (!request.getName().contains("%") && Objects.equals(request.getType(), "plugin")) {
            return "SELECT id, name FROM " + request.getType() + "s WHERE name LIKE '" + request.getName() + "%'";
        }
        return "SELECT id, name FROM " + request.getType() + "s WHERE name='" + request.getName() + "'";
    }

    private static List<SimpleRecordModel> getSimpleRows(String query) {
        RowMapper<SimpleRecordModel> rowMapper = new SimpleRecordRowMapper();
        return ServerApplication.jdbcTemplate.query(query, rowMapper);
    }

    private static void processDeletion(DeleteResourceRequest request, SimpleRecordModel elem) {
        String pluginDeleteQuery = "DElETE FROM " + request.getType() + "s  WHERE name='" + elem.getName() + "'";
        ServerApplication.jdbcTemplate.update(pluginDeleteQuery);
        File toDelete = new File(generateFileName(request));
        toDelete.delete();
    }


    private static void updateBundleArrays(long i, int contentType) {
        String pluginClearQuery = "UPDATE bundles SET elements = array_remove(elements, " + i + ") WHERE " + i + " = ANY(elements) AND content_type=" + contentType;
        ServerApplication.jdbcTemplate.update(pluginClearQuery);
    }

    private static String processUniversalDelete(DeleteResourceRequest request) {
        String query = generateSelectorQuery(request);
        List<SimpleRecordModel> result = getSimpleRows(query);
        if (result.size() == 0) {
            return StatusResponses.generateError(404, "Unknown resource family: " + request.getName());
        }
        int type = getTypeNumeric(request.getType());
        for (SimpleRecordModel elem : result) {
            updateBundleArrays(elem.getId(), type);
            processDeletion(request, elem);
        }
        deleteEmptyBundles();
        return StatusResponses.generateError(200, request.getType() + "(s) have been deleted and corresponding bundles have been redacted!");
    }


    public static CompletableFuture<String> processDataDelete(DeleteResourceRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            if (!isSudoUser(request.getSudoToken())) {
                return StatusResponses.generateError(400, "Not sudo user!");
            }
            if (!request.isValidRequest()) {
                return StatusResponses.generateBadRequestErr();
            }
            if (!isSupportedType(request.getType())) {
                return StatusResponses.generateBadRequestErr();
            }
            return processUniversalDelete(request);
        });
    }
}
