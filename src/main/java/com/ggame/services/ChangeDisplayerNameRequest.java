package com.ggame.services;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangeDisplayerNameRequest {
	public String userName;
	public long userId;
	public String displayerName;
	
	public JSONObject toJson() {
		JSONObject object = new JSONObject();
		try {
			object.put("userId", userId);
			object.put("userName", userName);
			object.put("displayerName", displayerName);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object;
	}	
}
