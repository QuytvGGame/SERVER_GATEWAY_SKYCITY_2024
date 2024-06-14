package com.ggame.pushnotification;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PushNotificationFCM {
	private static final String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";
//	private static final String keyFCMAndroid = "AAAAPPuEf4Q:APA91bHPUIz7UHETDpg0I1-pNfRv3DOTIP_8v8lGngia-neDHENeRcokqpALyleZID6Baw3hbNBrMfGGUZJQwKxcvf3b6VpoHPDGnbTT4b0ZWRlfcKyowwaTdmksUPF1KaPST1zA_t05";
//	private static final String keyFCMIos = "AAAAPPuEf4Q:APA91bHPUIz7UHETDpg0I1-pNfRv3DOTIP_8v8lGngia-neDHENeRcokqpALyleZID6Baw3hbNBrMfGGUZJQwKxcvf3b6VpoHPDGnbTT4b0ZWRlfcKyowwaTdmksUPF1KaPST1zA_t05";
	private static final String keyFCMAndroid = "AAAAaSzCAaU:APA91bFRGYIzCAd6jnNyqPtDDIOb4gRPcSz1O4MYpcU_r8w-O0p5_mtnqacThkXIrbY-U197UOSUhmQ2oEeuWZcAkG2j1PI3YnVfWa2KiJCgkyN0o466IAWZT9Xw0gZbz-OuTazhPBBB";
	private static final String keyFCMIos = "AAAAaSzCAaU:APA91bHIZDcRWEAUDVLCLZklFNxWPwDXsuyQowSfwHzkKeW9-6b0U_Nh8Vc7Wi6ybmgBNeaabdcqF5kZsTXY7BJg8BSp0cWVdvbCdiaKpdgOLQdyQz7XfMIMbX-S7D2sCGnFx6gSMT5X";
	private JSONObject root;

	public PushNotificationFCM(String title, String message) throws JSONException {
		root = new JSONObject();
		JSONObject data = new JSONObject();
		data.put("title", title);
		data.put("body", message);
		root.put("notification", data);
	}

	public String sendToTopic(String topic) throws Exception { // SEND TO TOPIC
		System.out.println("Send to Topic");
		root.put("condition", "'" + topic + "' in topics");
		sendPushNotification(true);
		return sendPushNotificationAndroid(true);
	}

	public String sendToGroup(JSONArray mobileTokens) throws Exception { // SEND TO GROUP OF PHONES - ARRAY OF TOKENS
		root.put("registration_ids", mobileTokens);
		return sendPushNotificationAndroid(false);
	}

	public String sendToToken(String token) throws Exception {// SEND MESSAGE TO SINGLE MOBILE - TO TOKEN
		root.put("to", token);
		return sendPushNotificationAndroid(false);
	}

	private String sendPushNotification(boolean toTopic) throws Exception {
		URL url = new URL(API_URL_FCM);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setUseCaches(false);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");

		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Accept", "application/json");
		conn.setRequestProperty("Authorization", "key=" + keyFCMIos);

		System.out.println(root.toString());

		try {
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(root.toString());
			wr.flush();

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output;
			StringBuilder builder = new StringBuilder();
			while ((output = br.readLine()) != null) {
				builder.append(output);
			}
			System.out.println(builder);
			String result = builder.toString();

			JSONObject obj = new JSONObject(result);

			if (toTopic) {
				if (obj.has("message_id")) {
					return "SUCCESS";
				}
			} else {
				int success = Integer.parseInt(obj.getString("success"));
				if (success > 0) {
					return "SUCCESS";
				}
			}

			return builder.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}

	}

	private String sendPushNotificationAndroid(boolean toTopic) throws Exception {
		URL url = new URL(API_URL_FCM);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setUseCaches(false);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");

		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Accept", "application/json");
		conn.setRequestProperty("Authorization", "key=" + keyFCMAndroid);

		System.out.println(root.toString());

		try {
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(root.toString());
			wr.flush();

			try {
				int status = 0;
				if (conn != null) {
					status = conn.getResponseCode();
				}
				
				System.out.println("--- PushNotificationFCM sendPushNotificationAndroid --- status = " + status);
				
			} catch (Exception e) {
				e.printStackTrace();
				return e.getMessage();
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output;
			StringBuilder builder = new StringBuilder();
			while ((output = br.readLine()) != null) {
				builder.append(output);
			}
			System.out.println(builder);
			String result = builder.toString();

			JSONObject obj = new JSONObject(result);

			if (toTopic) {
				if (obj.has("message_id")) {
					return "SUCCESS";
				}
			} else {
				int success = Integer.parseInt(obj.getString("success"));
				if (success > 0) {
					return "SUCCESS";
				}
			}

			return builder.toString();
		} catch (

		Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}

	}
}
