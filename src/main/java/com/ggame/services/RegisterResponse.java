package com.ggame.services;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterResponse {
	public long user_id;
	public String username;
	public String password;
	public String email;
	public String gameId;
	public String ime;
	public String ip;	
	public int platform; // 1:Android, 2:iOS, 3:Web, 0: mặc định
	public int provider_id;
	public int logintype; // 
	
	public JSONObject toJson() {
		JSONObject object = new JSONObject();
		try {
			object.put("user_id", user_id);
			object.put("username", username);
			object.put("password", password);
			object.put("email", email);
			object.put("gameId", gameId);
			object.put("ime", ime);
			object.put("ip", ip);
			object.put("platform", platform);
			object.put("logintype", logintype);
			object.put("provider_id", provider_id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object;
	}
}
