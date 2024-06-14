package com.ggame.services;

import org.json.JSONException;
import org.json.JSONObject;

public class InstallGameRequest {
	public String gameId;
	public String ime;
	public int platform;
	
	public JSONObject toJson() {
		JSONObject object = new JSONObject();
		try {
			object.put("gameId", gameId);
			object.put("ime", ime);
			object.put("platform", platform);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object;
	}
}
