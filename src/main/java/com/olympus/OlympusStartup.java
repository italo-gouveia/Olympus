package com.olympus;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan
public class OlympusStartup {

	public static void main(String[] args) {
		SpringApplication.run(OlympusStartup.class, args);
	    //Logger logger = LoggerFactory.getLogger(OlympusStartup.class);
	    //logger.info("[INFO] Method={}, msg={}", "OlympusStartup.Main", "Executed this function");
	    //logger.error("Timestamp={}, Class={},  msg={}", new Date(), OlympusStartup.class, "Error at execution");
	}

}
