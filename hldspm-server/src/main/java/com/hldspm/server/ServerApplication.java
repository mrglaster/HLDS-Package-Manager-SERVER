package com.hldspm.server;

import com.hldspm.server.database.mappers.EngineRowMapper;
import com.hldspm.server.ftp_server.file_structure.StructureOrganizer;
import com.hldspm.server.io.io;
import com.hldspm.server.models.EngineModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import java.util.List;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, SecurityAutoConfiguration.class})
public class ServerApplication {

	private static JdbcTemplate jdbcTemplate;


	@Autowired
	public ServerApplication(JdbcTemplate jdbcTemplate) {
		ServerApplication.jdbcTemplate = jdbcTemplate;
	}

	public static void demoConnection(){
		String sql = "SELECT * FROM engines";
		RowMapper<EngineModel> rowMapper = new EngineRowMapper();
		List<EngineModel> engines = jdbcTemplate.query(sql, rowMapper);
		System.out.println("GOT DATA : " + engines.get(0));
		System.out.println("GOT DATA : " + engines.get(1));
	}


	public static void main(String[] args) {
		StructureOrganizer.initFileSystem();
		io.customPrint("Starting the server...");
		SpringApplication.run(ServerApplication.class, args);

	}
}
