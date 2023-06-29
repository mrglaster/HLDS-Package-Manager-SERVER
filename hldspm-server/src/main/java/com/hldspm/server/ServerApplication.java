package com.hldspm.server;
import com.hldspm.server.banner.CustomBanner;
import com.hldspm.server.cfg_reader.CfgReader;
import com.hldspm.server.database.dumper.DumpCreator;
import com.hldspm.server.database.dumper.DumpReader;
import com.hldspm.server.database.initializer.DatabaseInitializer;
import com.hldspm.server.ftp_server.file_structure.StructureOrganizer;
import com.hldspm.server.io.io;
import org.apache.juli.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.PrintStream;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, SecurityAutoConfiguration.class})
public class ServerApplication {

	public static JdbcTemplate jdbcTemplate;
	public static CfgReader configure;

	private static volatile boolean isShuttingDown = false;

	/**Initializing JdbcTemplate to work with the database*/
	@Autowired
	public ServerApplication(JdbcTemplate jdbcTemplate) {
		ServerApplication.jdbcTemplate = jdbcTemplate;
	}


	private static void processShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			if (!isShuttingDown) {
				isShuttingDown = true;
				io.customPrint("Shutting down the server...");
				if(DumpCreator.hasChanges) {
					io.customPrint("Generating the repo dump");
					DumpCreator.makeDump();
				} else {
					io.customPrint("There weren't changes in the database. Dump won't be created");
				}
			}
		}));
	}

	public static void main(String[] args) {
		configure = new CfgReader();
		configure.processCfgRead();
		StructureOrganizer.initFileSystem();
		SpringApplication app = new SpringApplication(ServerApplication.class);
		app.setBanner(CustomBanner.customBanner);
		io.customPrint("Starting the server...");
		app.run(args);
		if (Integer.parseInt(configure.getCreateTableStructureFlag()) == 1){
			io.customPrint("Initializing database...");
			DatabaseInitializer.processDatabaseInit();
		}
		DumpReader.processDumps();
		io.customPrint("The server is running");
		processShutdownHook();
	}
}
