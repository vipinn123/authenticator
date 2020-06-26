package com.spring.authenticator;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AuthenticatorSpringApplication {
	
	private static Logger logger = LoggerFactory.getLogger(AuthenticatorSpringApplication.class);

	public static void main(String[] args) {
		//SpringApplication.run(AuthenticatorSpringApplication.class, args);
		
		// spring boot 2.x
		SpringApplication application = new SpringApplication(AuthenticatorSpringApplication.class);
        application.setAdditionalProfiles("ssl");
        application.run(args);
		
        
		logger.info("Authenticator Application Started");
	}
	
	//@KafkaListener(topics = "tickdata", groupId = "full-read")
	public void listen(String message) {
		
		logger.info("Received Messasge in group - full-read: " + message);
		System.out.println(message);
	
	}
	
	


}
