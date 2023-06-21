package com.hldspm.server.ftp_server.cfg;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;

import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.hldspm.server.ftp_server.cfg.FtpConstants.getFtpPath;

@Configuration
public class FtpServerConfig {
    private static final String REPO_DATA_ROOT = getFtpPath();
    @Bean
    public FtpServerFactory ftpServerFactory() throws FtpException {
        FtpServerFactory factory = new FtpServerFactory();

        // Creating the ftp-user
        BaseUser anonymousUser = new BaseUser();
        anonymousUser.setName(FtpConstants.ANON_USERNAME);
        anonymousUser.setHomeDirectory(REPO_DATA_ROOT);

        // Adding the anon user
        UserManager userManager = factory.getUserManager();
        userManager.save(anonymousUser);

        // Creating the listeners factory & user adding
        ListenerFactory listenerFactory = new ListenerFactory();
        listenerFactory.setPort(FtpConstants.FTP_PORT);

        factory.addListener(FtpConstants.LISTENER_TYPE, listenerFactory.createListener());

        return factory;
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public FtpServer ftpServer(FtpServerFactory ftpServerFactory) {
        return ftpServerFactory.createServer();
    }
}
