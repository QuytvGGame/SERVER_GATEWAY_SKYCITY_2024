package com.ggame.services;

import org.json.JSONObject;

public class TextRequest {
	public String gameid;
	public int language;

	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		try {
			json.put("gameid", gameid);
			json.put("language", language);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
}
