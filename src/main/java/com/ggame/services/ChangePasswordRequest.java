package com.ggame.services;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangePasswordRequest {
	public String username;
	public String password_new;
	
	public JSONObject toJson() {
		JSONObject object = new JSONObject();
		try {
			object.put("username", username);
			object.put("password_new", password_new);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object;
	}
}
