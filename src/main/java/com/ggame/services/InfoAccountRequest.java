package com.ggame.services;

import org.json.JSONObject;

public class InfoAccountRequest {
	public String gameid;
	public String username;

	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		try {
			json.put("gameid", gameid);
			json.put("username", username);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
}
