package com.spring.authenticator;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.spring.authenticator.session.SessionAccessToken;
import com.spring.authenticator.ticker.Ticker;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.ticker.KiteTicker;

@Controller // This means that this class is a Controller
@RequestMapping(path="/trade") // This means URL's start with /demo (after Application path)
public class AuthenticatorController {
	
	private static Logger logger = LoggerFactory.getLogger(AuthenticatorController.class);
	
	@Autowired	
	private SessionAccessToken accessToken;
	
	@Autowired
	private Environment env;
	
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	@Autowired
	Ticker ticker;
	 
	
	//@PostMapping(path="/login") // Map ONLY POST Requests
	@GetMapping(path="/login")
	public @ResponseBody String login (@RequestParam String request_token
	      , @RequestParam String action
	      ,@RequestParam String status ) throws JSONException, IOException, KiteException {
	    // @ResponseBody means the returned String is the response, not a view name
	    // @RequestParam means it is a parameter from the GET or POST request
		
		
		logger.info("Entering /login");
		
		if(status.equalsIgnoreCase("success") && accessToken.getAccessToken() == null) {
			
			//TODO : Get request token from request parameter to generate session
			accessToken.setAPIKey(env.getProperty("zerodha.api.key"));
			accessToken.setUser(env.getProperty("zerodha.user.id"));
			accessToken.setAPISecret(env.getProperty("zerodha.api.secret"));
			accessToken.generateSession(request_token);
			
			logger.info("request_token : "+ request_token);
		}

		logger.info("Exiting /login");
		return status;
	  }
	
	@Scheduled(cron="0 12 13 * * MON-SUN",zone="IST")
	//@Scheduled(fixedRate=5000)
	public void ticker() throws KiteException {
		
		logger.info("Entering ticker() cron");
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		System.out.println("The time is now :" + dateFormat.format(new Date()));
		
		/** To get live price use websocket connection.
         * It is recommended to use only one websocket connection at any point of time and make sure you stop connection, once user goes out of app.
         * custom url points to new endpoint which can be used till complete Kite Connect 3 migration is done. */
		
		//Ticker ticker = new Ticker();
		
		
		ticker.startTicker(accessToken.getAccessToken(), accessToken.getApiKey());
		
		logger.info("Exiting ticker() cron");
        
	}
	
	@Scheduled(cron="0 13 13 * * MON-SUN",zone="IST")
	//@Scheduled(fixedRate=5000)
	public void tickerStop() throws KiteException {
		
		logger.info("Entering tickerStop() cron");
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		System.out.println("The time is now :" + dateFormat.format(new Date()));
		
		/** To get live price use websocket connection.
         * It is recommended to use only one websocket connection at any point of time and make sure you stop connection, once user goes out of app.
         * custom url points to new endpoint which can be used till complete Kite Connect 3 migration is done. */
		
		//Ticker ticker = new Ticker();
		
		
		ticker.stopTicker();
		
		logger.info("Exiting tickerStop() cron");
        
	}


	
}
