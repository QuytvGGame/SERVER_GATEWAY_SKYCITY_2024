package com.sgc.game.network.websocket;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;

import config.Config;

public class Http {
	// HTTP GET request
	public static String sendGet(String url) throws Exception {

		String USER_AGENT = "Mozilla/5.0";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		// optional default is GET
		con.setRequestMethod("GET");
		// add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		StringBuffer response = new StringBuffer();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response.toString();

	}

	// HTTP POST request
	public static String sendPost(String url, String params) throws Exception {

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", "");
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		con.setRequestProperty("Content-type", "text/html");
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(params);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
//		System.out.println("\nSending 'POST' request to URL : " + url);
//		System.out.println("Post parameters : " + params);
//		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		return response.toString();
	}
	
	
	// HTTP POST request
		public static String sendPostJson(String url, StringEntity postingString) throws Exception {	
			try {
				RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000).build(); // chờ kết nối trong 5s
				HttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
				HttpPost post = new HttpPost(url);	
				post.setEntity(postingString);
				post.setHeader("Content-type", "application/json");		
				HttpResponse res = httpClient.execute(post);		
				String jsonResponse = EntityUtils.toString(res.getEntity());
//				System.out.println("======================== json = " + jsonResponse);
				return jsonResponse;
			} catch (Exception e) {
				return "";
			}
		}
		
//		public static String sendPostJson(String url, StringEntity postingString) throws Exception {	
//			try {
//				HttpClient httpClient = HttpClientBuilder.create().build();		
//				HttpPost post = new HttpPost(url);	
//				post.setEntity(postingString);
//				post.setHeader("Content-type", "application/json");		
//				HttpResponse res = httpClient.execute(post);		
//				String jsonResponse = EntityUtils.toString(res.getEntity());
////				System.out.println("======================== json = " + jsonResponse);
//				return jsonResponse;
//			} catch (Exception e) {
//				return "";
//			}
//		}
		
		
		
}
