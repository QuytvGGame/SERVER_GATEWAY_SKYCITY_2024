package com.ggame.services;

import org.json.JSONException;
import org.json.JSONObject;

public class GetCountryRequest {
	private String gameid;
	private int version;
	private int platform;
	private int providerId;

	public JSONObject toJson() {
		JSONObject object = new JSONObject();
		try {
			object.put("gameId", gameid);
			object.put("version", version);
			object.put("platform", platform);
			object.put("providerId", providerId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object;
	}

	public String getGameid() {
		return gameid;
	}

	public void setGameid(String gameid) {
		this.gameid = gameid;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getPlatform() {
		return platform;
	}

	public void setPlatform(int platform) {
		this.platform = platform;
	}

	public int getProviderId() {
		return providerId;
	}

	public void setProviderId(int providerId) {
		this.providerId = providerId;
	}

}
