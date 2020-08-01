package com.spring.authenticator.session;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
	
	@Autowired
	private Environment env;
	
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
		
		//write access token to trader
		
		setTraderToken();
	}

	private void setTraderToken() throws IOException {
		
		// TODO Auto-generated method stub
		URL url = new URL(env.getProperty("trader.api.url")); //Get Trader URL from Properties
 		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("POST");
		
		Map<String, String> parameters = new HashMap<>();
		parameters.put("access_Token", this.AccessToken);
		
		con.setDoOutput(true);
		DataOutputStream out = new DataOutputStream(con.getOutputStream());
		out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
		out.flush();
		out.close();
		
		
		
		/*BufferedReader in = new BufferedReader(
				  new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer content = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
		    content.append(inputLine);
		}
		in.close();*/
		
		System.out.println("Response from Trader :" + con.getContent());
		
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
