package com.hldspm.server;
import ch.qos.logback.classic.Logger;
import com.hldspm.server.io.banner.CustomBanner;
import com.hldspm.server.cfg_reader.CfgReader;
import com.hldspm.server.database.dumper.DumpCreator;
import com.hldspm.server.database.dumper.DumpReader;
import com.hldspm.server.database.initializer.DatabaseInitializer;
import com.hldspm.server.ftp_server.file_structure.StructureOrganizer;
import com.hldspm.server.io.custom_print.io;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;


@SpringBootApplication
public class ServerApplication {

	public static JdbcTemplate jdbcTemplate;
	public static CfgReader configure;
	private static volatile boolean isShuttingDown = false;


	/**Initializing JdbcTemplate to work with the database*/
	@Autowired
	public ServerApplication(JdbcTemplate jdbcTemplate) {
		ServerApplication.jdbcTemplate = jdbcTemplate;
	}


	/**Create a backup on server shutdown*/
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

	/**Disable logging (for jar run)*/
	private static void disableLogging(){
		Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		root.setLevel(ch.qos.logback.classic.Level.convertAnSLF4JLevel(Level.ERROR));
	}

	/**Read the data from the config file*/
	private static void readConfig(){
		configure = new CfgReader();
		configure.processCfgRead();
	}

	/**Initialize tables in the database*/
	private static void processDbInit(){
		if (Integer.parseInt(configure.getCreateTableStructureFlag()) == 1){
			io.customPrint("Initializing database...");
			DatabaseInitializer.processDatabaseInit();
		}
	}

	/**Configures the port change*/
	@Bean
	public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer() {
		return factory -> factory.setPort(configure.getPort());
	}

	public static void main(String[] args) {
		readConfig();
		disableLogging();
		SpringApplication app = new SpringApplication(ServerApplication.class);
		app.setBanner(CustomBanner.customBanner);
		app.run(args);
		StructureOrganizer.initFileSystem();
		processDbInit();
		DumpReader.processDumps();
		io.customPrint("The server is running on port " + configure.getPort());
		processShutdownHook();
	}
}
