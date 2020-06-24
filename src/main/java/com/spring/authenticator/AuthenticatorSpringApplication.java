package com.spring.authenticator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AuthenticatorSpringApplication {
	
	private static Logger logger = LoggerFactory.getLogger(AuthenticatorSpringApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(AuthenticatorSpringApplication.class, args);
		
		logger.info("Authenticator Application Started");
	}

}
