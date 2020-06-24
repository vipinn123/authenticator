package com.spring.authenticator.ticker;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;

import com.google.gson.Gson;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.Tick;
import com.zerodhatech.ticker.KiteTicker;
import com.zerodhatech.ticker.OnConnect;
import com.zerodhatech.ticker.OnDisconnect;
import com.zerodhatech.ticker.OnTicks;

public class Ticker {
	
	public Ticker() {
		
	}
	
	public void startTicker(String accessToken, String apiKey) throws KiteException {
		
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
            	//CustomKafkaProducer producer = new CustomKafkaProducer();
                NumberFormat formatter = new DecimalFormat();
                System.out.println("ticks size "+ticks.size());
                if(ticks.size() > 0) {
                	
                    
                    
                    Iterator<Tick> iterator = ticks.iterator();
                    
                    
                    String tickJson = new Gson().toJson(ticks.get(1));
                    
                    System.out.println("********************************************************");
                    System.out.println(tickJson);
                    System.out.println("********************************************************");
                    
                    while(iterator.hasNext()) {
                    	
                    	
                    	//producer.sendTick(tick);
                    	
                    }
                    
                    //producer.close();
                    	
                    		
                }
            }

			
        });


      

        
        tickerProvider.setTryReconnection(true);
        
        //maximum retries and should be greater than 0
        tickerProvider.setMaximumRetries(10);
        
        //set maximum retry interval in seconds
        tickerProvider.setMaximumRetryInterval(30);

        /** connects to com.zerodhatech.com.zerodhatech.ticker server for getting live quotes*/
        tickerProvider.connect();

        /** You can check, if websocket connection is open or not using the following method.*/
        boolean isConnected = tickerProvider.isConnectionOpen();
        System.out.println(isConnected);

        
        /**
         * 
         */
        //System.out.println(tickerProvider.)

        /** set mode is used to set mode in which you need tick for list of tokens.
         * Ticker allows three modes, modeFull, modeQuote, modeLTP.
         * For getting only last traded price, use modeLTP
         * For getting last traded price, last traded quantity, average price, volume traded today, total sell quantity and total buy quantity, open, high, low, close, change, use modeQuote
         * For getting all data with depth, use modeFull*/
        //tickerProvider.setMode(tokens, KiteTicker.modeLTP);
        
        try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println("Got Quote 5");
        // Unsubscribe for a token.
        tickerProvider.unsubscribe(tokens);

        // After using com.zerodhatech.com.zerodhatech.ticker, close websocket connection.
        tickerProvider.disconnect();        

		
	}

}
