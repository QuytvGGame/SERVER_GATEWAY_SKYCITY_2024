package com.sgc.game.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.omg.CORBA.SystemException;

import com.ggame.accountservice.AgentGame;

public class VerifyOpenID {

	private final String USER_AGENT = "Mozilla/5.0";
	private final static String _sandboxUriStr = "https://sandbox.itunes.apple.com/verifyReceipt";// dung
																									// khi
																									// apple
																									// duyet
	private final static String _productionUriStr = "https://buy.itunes.apple.com/verifyReceipt";

	public static List<String[]> processPayment(final String receipt, boolean sandbox) throws SystemException {
		final String jsonData = "{\"receipt-data\" : \"" + receipt + "\"}";

		try {

			String url_payment = _sandboxUriStr;
			if (!sandbox)
				url_payment = _productionUriStr;
			final URL url = new URL(url_payment);
			final HttpURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");

			final OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(jsonData);
			wr.flush();

			// Get the response
			final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			StringBuffer response = new StringBuffer();
			while ((line = rd.readLine()) != null) {
				response.append(line);
			}
			wr.close();
			rd.close();
			String strResponse = response.toString();
			List<String[]> result = new ArrayList<String[]>();
			AgentGame.logError("result == " + strResponse);
			JSONObject jss = new JSONObject(strResponse);
			String status = jss.getString("status");
			if (status.equals("0")) {
				String receipt_response = jss.getString("receipt");
				JSONObject jsons = new JSONObject(receipt_response);
				String utf = jsons.getString("in_app");
				JSONArray jsonArray = new JSONArray(utf);
				for(int i = 0 ;i < jsonArray.length(); i++) {
					String[] obj = new String[2];
					obj[0] = jsonArray.getJSONObject(i).getString("transaction_id");
					obj[1] = jsonArray.getJSONObject(i).getString("product_id");
					result.add(obj);
				}
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<String[]>();
		}
	}

	// HTTP GET request
	public String verifyAccessToken(String link, String access_token) throws Exception {

		// access_token =
		// "CAAEY8kad9zABAKmU6ZALGre1FWdNuZBpK6nHT0YYZBYyqjEHyvxGYmbTddVghizl5JCBslaMGIULFl24RMvASlpHj48yz3xbn9PVQy7KqAZAPpGxNjQoQAZBRMBbt7qeAYAE16PUKBM6U4mXZCUlPe8fK6ofKT7JbAPMMAuvxauM9HHcpZAj4KvoetFPZAJkI9HJHcHAVJQ4ZCPVRETCU7vZAP";

		String url = link + access_token;
		// System.out.println("\nSending 'GET' request to URL : " + url);
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		// add request header
		con.setRequestProperty("User-Agent", USER_AGENT);
		int responseCode = con.getResponseCode();
		System.out.println("Response Code : " + responseCode);
		con.setConnectTimeout(60000);
		con.setReadTimeout(60000);

		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			// JSONObject jsonObject = new JSONObject(response.toString());
			return response.toString();
		} catch (Exception e) {
			return null;
		}

	}

	public static void main(String[] a) {
		// String ass =
		// "CAAKLco03Ne0BAOdtbfZAgdhLOP3xmukEEut6pytAhTmvZAvSWWwyiEWCTQXZB3p3hI2CyNruXHM7ZBbznkOjCBdZCspBAZAODQoWAca4BLAi2JsQpqYEnOWXzxLrbZC3Pug2vlZAwlfpebNOvQU2YZB5kRQdytnX6JjTESCGbflkHKtrIZBg0uxZCy1s9fV4EuKLuSycgqZAKFXZAO52bNp7KdISKtJWG2ZARuZA9ZBUHGf0Pqvs2wZDZD";

		// String ass =
		// "CAAEY8kad9zABAAxEnJ4RkSwpvlyHoA5PzzO9DbfOqmau8JZCo9drgxHkEOMZAAuFZCpzbQG70cA1VdxpfeZA2ZAi1RLX8oLXJGeBMr3zFiTqWmpeFzoxnMbGyGYCw8RDspeCv8mzZAZCADGbe65FMPkJahJWZAWVUIA8o2ioZAhXw2ZCAHZC0JKaZCZA4TwxaflTn1PggSbdVDo1Pq1VLYJXIpKRZCGsvptBfoi6GV8lrzmgo81QZDZD";
		// VerifyOpenID verifyOpenID = new VerifyOpenID();
		// try {
		//// System.out.println(verifyOpenID.verifyAccessToken(ass));
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}
}