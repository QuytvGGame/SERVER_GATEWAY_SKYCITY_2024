package com.ggame.services;

import org.json.JSONException;
import org.json.JSONObject;

public class GetThongBaoRequest {
	public int gameId;
	
	public JSONObject toJson() {
		JSONObject object = new JSONObject();
		try {
			object.put("gameId", gameId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object;
	}
}
