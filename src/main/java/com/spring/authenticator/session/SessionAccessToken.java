package com.spring.authenticator.session;

import java.io.IOException;

import org.json.JSONException;
import org.springframework.stereotype.Component;

import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.User;

@Component
public class SessionAccessToken {

	private KiteConnect kiteSDK;
	private String APIKey;
	private String APISecret;
	private String AccessToken=null;
	private String UserId;
	
	public SessionAccessToken(){
		
		//TODO : Initiate kiteSDK with API_Key from app context
		//
		
	}
	
	public void generateSession(String request_token ) throws JSONException, IOException, KiteException {
		
		if(kiteSDK != null && AccessToken != null)
			return;
		
		//TODO : Get kiteConnect object, using API Key from Application Context		
		kiteSDK = new KiteConnect(APIKey);		
		kiteSDK.setUserId(UserId);	
		
		System.out.println("Request Token : " + request_token + "APISecret : " + APISecret);
		User user = kiteSDK.generateSession(request_token, APISecret);		
		this.AccessToken = user.accessToken;
		kiteSDK.setAccessToken(AccessToken);		
		System.out.println("Access Token: " + AccessToken);	
	}

	public void setAPIKey(String APIKey) {
		// TODO Auto-generated method stub
		this.APIKey = APIKey;
		
	}

	public void setAPISecret(String APISecret) {
		// TODO Auto-generated method stub
		this.APISecret=APISecret;
		
	}

	public void setUser(String UserId) {
		// TODO Auto-generated method stub
		this.UserId = UserId;
		
	}

	public String getAccessToken() {
		// TODO Auto-generated method stub
		return AccessToken;
	}

	public String getApiKey() {
		// TODO Auto-generated method stub
		return APIKey;
	}

	public void resetAccessToken() {
		// TODO Auto-generated method stub
		this.AccessToken=null;	
		
	}
	
	
	
	
	
	
	
	
	
}
