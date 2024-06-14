package com.ggame.services;

import org.json.JSONObject;

public class RegisterRequest {
	public String username;
	public String password;
	public String email;
	public int type;
	public String regPhone;
	public String ip;	
	
	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		try {
			json.put("userName", username);
			json.put("passWord", password);
			json.put("email", email);
			json.put("type", type);
			json.put("regPhone", regPhone);
			json.put("ip", ip);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
}
