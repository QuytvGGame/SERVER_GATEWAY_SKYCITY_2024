package com.ggame.services;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateInfoDisplayNameResponse {
	public long user_id_game;
	public String display_name;
	
	public JSONObject toJson() {
		JSONObject object = new JSONObject();
		try {
			object.put("user_id_game", user_id_game);
			object.put("display_name", display_name);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object;
	}
}
