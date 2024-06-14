package com.ggame.services;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateTokenFCMRequest {
	public long userid;
	public String tokenFCM;

	public JSONObject toJson() {
		JSONObject object = new JSONObject();
		try {
			object.put("userid", userid);
			object.put("tokenFCM", tokenFCM);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object;
	}
}
