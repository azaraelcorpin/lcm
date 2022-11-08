package com.ems.lcm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SpringBootApplication
@EnableSwagger2
public class LcmApplication {

	public static final Logger LOGGER = LogManager.getLogger(LcmApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(LcmApplication.class, args);

		LOGGER.info("Info level log message");
        LOGGER.debug("Debug level log message");
        LOGGER.error("Error level log message");
	}

	

}
