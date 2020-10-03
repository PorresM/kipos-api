package com.kipos.kiposapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementServerProperties;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class KiposApiApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(KiposApiApplication.class);

	public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(KiposApiApplication.class, args);

        ServerProperties serverConf = applicationContext.getBean(ServerProperties.class);
        ManagementServerProperties managementConf = applicationContext.getBean(ManagementServerProperties.class);

        LOGGER.info("***************************************************");
        LOGGER.info("* KIPOS API application started                   *");
        LOGGER.info("* - Public port: " + serverConf.getPort() + "                             *");
        LOGGER.info("* - Admin port: " + managementConf.getPort() + "                              *");
        LOGGER.info("***************************************************");
	}

}
