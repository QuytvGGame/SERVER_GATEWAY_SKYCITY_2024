package com.ggame.services;

import org.json.JSONObject;

public class CheckLoginRequest {
	public String username;
	public long userId;
	public String serverId;
	public String keyhash;
	public String gameId;

	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		try {
			json.put("username", username);
			json.put("userId", userId);
			json.put("serverId", serverId);
			json.put("keyhash", keyhash);
			json.put("gameId", gameId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	public int getServerIdInt() {
		return Integer.parseInt(serverId);
	}

	public int getGameIdInt() {
		return Integer.parseInt(gameId);
	}
}
