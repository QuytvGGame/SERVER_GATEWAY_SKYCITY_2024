package com.ggame.services;

import org.json.JSONException;
import org.json.JSONObject;

public class ServerItem {
	private int server;

	public ServerItem(int server) {
		super();
		this.server = server;
	}

	public JSONObject toJson() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("server", server);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

	public int getServer() {
		return server;
	}

	public void setServer(int server) {
		this.server = server;
	}

}
