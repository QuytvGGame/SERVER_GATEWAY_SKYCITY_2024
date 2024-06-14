package com.ggame.services;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateEmailRequest {
	public String username;
	public String emailNew;
	
	public JSONObject toJson() {
		JSONObject object = new JSONObject();
		try {
			object.put("username", username);
			object.put("emailNew", emailNew);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object;
	}
}
