package com.ggame.entity.player;

import org.json.JSONException;
import org.json.JSONObject;

public class ServerPlayerData {

	private long user_id_game;
	private String username;
	private long user_id;
	private String display_name;
	private String game_id;
	private String server_id;
	private String server_id_current;
	private String server_ip = "";
	private int server_port;
	private String keyhash;

	public ServerPlayerData() {
		// TODO Auto-generated constructor stub
	}

	public JSONObject toJson() {
		JSONObject object = new JSONObject();
		try {
			object.put("userIdGame", user_id_game);
			object.put("displayName", display_name);
			object.put("serverId", server_id);
			object.put("serverIp", server_ip);
			object.put("serverPort", server_port);
			object.put("keyhash", keyhash);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object;
	}
	
	public JSONObject toJsonLog() {
		JSONObject object = new JSONObject();
		try {
			object.put("user_id_game", user_id_game);
			object.put("username", username);
			object.put("user_id", user_id);
			object.put("display_name", display_name);
			object.put("game_id", game_id);
			object.put("server_id", server_id);
			object.put("server_id_current", server_id_current);
			object.put("server_ip", server_ip);
			object.put("server_port", server_port);
			object.put("keyhash", keyhash);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object;
	}

	public long getUser_id_game() {
		return user_id_game;
	}

	public void setUser_id_game(long user_id_game) {
		this.user_id_game = user_id_game;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	public String getDisplay_name() {
		return display_name;
	}

	public void setDisplay_name(String display_name) {
		this.display_name = display_name;
	}

	public String getGame_id() {
		return game_id;
	}

	public void setGame_id(String game_id) {
		this.game_id = game_id;
	}

	public String getServer_id() {
		return server_id;
	}

	public void setServer_id(String server_id) {
		this.server_id = server_id;
	}

	public String getServer_ip() {
		return server_ip;
	}

	public boolean isIpSucces() {
		if (this.server_ip.length() < 3)
			return false;
		return true;
	}

	public void setServer_ip(String server_ip) {
		this.server_ip = server_ip;
	}

	public int getServer_port() {
		return server_port;
	}

	public void setServer_port(int server_port) {
		this.server_port = server_port;
	}

	public String getKeyhash() {
		return keyhash;
	}

	public void setKeyhash(String keyhash) {
		this.keyhash = keyhash;
	}

	public String getServer_id_current() {
		return server_id_current;
	}

	public void setServer_id_current(String server_id_current) {
		this.server_id_current = server_id_current;
	}

}
