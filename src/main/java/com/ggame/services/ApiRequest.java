package com.ggame.services;

import org.json.JSONException;
import org.json.JSONObject;

public class ApiRequest {
	public String gameId;
	public int platform;
	public int providerId;
	public String imei;
	public int install;
	public String serverIp; // ip của người chơi
	public String deviceName;
	public int language;

	public JSONObject toJson() {
		JSONObject object = new JSONObject();
		try {
			object.put("gameId", gameId);
			object.put("platform", platform);
			object.put("providerId", providerId);
			object.put("imei", imei);
			object.put("install", install);
			object.put("serverIp", serverIp);
			object.put("deviceName", deviceName);
			object.put("language", language);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object;
	}
}
