package com.spring.authenticator.ticker;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.google.gson.Gson;
import com.spring.authenticator.AuthenticatorSpringApplication;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.Tick;
import com.zerodhatech.ticker.KiteTicker;
import com.zerodhatech.ticker.OnConnect;
import com.zerodhatech.ticker.OnDisconnect;
import com.zerodhatech.ticker.OnTicks;


public class Ticker {
	
	private static Logger logger = LoggerFactory.getLogger(Ticker.class);
	
	public Ticker() {
		
	}
	
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	 
	public void sendMessage(String message) {
		logger.info("In Ticker.senMessage()");
		
		ListenableFuture<SendResult<String, String>> future = 
			      this.kafkaTemplate.send("luckyv", message);
			     
			    future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
			 
			        @Override
			        public void onSuccess(SendResult<String, String> result) {
			            System.out.println("Sent message=[" + message + 
			              "] with offset=[" + result.getRecordMetadata().offset() + "]");
			        }
			        @Override
			        public void onFailure(Throwable ex) {
			            System.out.println("Unable to send message=["
			              + message + "] due to : " + ex.getMessage());
			        }
			    });
			    
		//kafkaTemplate.send("tickdata", msg);
		logger.info("Exiting Ticker.senMessage()");
	}
	
	public void startTicker(String accessToken, String apiKey) throws KiteException {
		
		logger.info("Entering Ticker.startTicker()");
		
		ArrayList<Long> tokens = Tokens.getDefaultTokens();
		
		/** To get live price use websocket connection.
         * It is recommended to use only one websocket connection at any point of time and make sure you stop connection, once user goes out of app.
         * custom url points to new endpoint which can be used till complete Kite Connect 3 migration is done. */
        KiteTicker tickerProvider = new KiteTicker(accessToken, apiKey);

        tickerProvider.setOnConnectedListener(new OnConnect() {
            @Override
            public void onConnected() {
                /** Subscribe ticks for token.
                 * By default, all tokens are subscribed for modeQuote.
                 * */
            	
            	
                tickerProvider.subscribe(tokens);
                tickerProvider.setMode(tokens, KiteTicker.modeFull);
            }
        });

        tickerProvider.setOnDisconnectedListener(new OnDisconnect() {
            @Override
            public void onDisconnected() {
                // your code goes here
            }
        });

        tickerProvider.setOnTickerArrivalListener(new OnTicks() {
            
        	@Override
            public void onTicks(ArrayList<Tick> ticks) {
                
                logger.info("Ticks recieved : "+ticks.size());

                if(ticks.size() > 0) {
 
                	Iterator<Tick> iterator = ticks.iterator();
                    String tickJson = new Gson().toJson(ticks.get(1));
                    logger.info(tickJson);

                    while(iterator.hasNext()) {
                    	
                    	//Ticker.sendMessage(new Gson().toJson(iterator.next()));
                    	kafkaTemplate.send("tickdata",new Gson().toJson(iterator.next()));

                    }
                    		
                }
            }
        });
        
        tickerProvider.setTryReconnection(true);
        tickerProvider.setMaximumRetries(10);
        tickerProvider.setMaximumRetryInterval(30);

        /** connects to com.zerodhatech.com.zerodhatech.ticker server for getting live quotes*/
        tickerProvider.connect();

        
        try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        

        logger.info("Exiting Ticker.startTicker()");
		
	}

}
