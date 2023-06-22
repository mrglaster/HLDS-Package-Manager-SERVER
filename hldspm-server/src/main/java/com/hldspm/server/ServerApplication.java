package com.hldspm.server;
import com.hldspm.server.ftp_server.file_structure.StructureOrganizer;
import com.hldspm.server.io.io;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, SecurityAutoConfiguration.class})
public class ServerApplication {

	public static JdbcTemplate jdbcTemplate;

	/**Initializing JdbcTemplate to work with the database*/
	@Autowired
	public ServerApplication(JdbcTemplate jdbcTemplate) {
		ServerApplication.jdbcTemplate = jdbcTemplate;
	}

	public static void main(String[] args) {

		StructureOrganizer.initFileSystem();
		io.customPrint("Starting the server...");
		SpringApplication.run(ServerApplication.class, args);
	}
}
