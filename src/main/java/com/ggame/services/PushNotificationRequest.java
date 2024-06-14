package com.ggame.services;

import org.json.JSONException;
import org.json.JSONObject;

public class PushNotificationRequest {
	public int status; // 1- gửi cho 1 thiết bị theo server, 2- gửi cho 1 server, 3- gửi cho all server
	public String user_name; // username (gửi nhiều phân cách bằng dấu "," . VD:
							// username1,username2,username3,...)
	public String title; // tiêu đề
	public String content; // nội dung
	public int game_id; // game id
	public int server_id; // server game

	public JSONObject toJson() {
		JSONObject object = new JSONObject();
		try {
			object.put("status", status);
			object.put("user_name", user_name);
			object.put("title", title);
			object.put("content", content);
			object.put("game_id", game_id);
			object.put("server_id", server_id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object;
	}	
	
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getGame_id() {
		return game_id;
	}

	public void setGame_id(int game_id) {
		this.game_id = game_id;
	}

	public int getServer_id() {
		return server_id;
	}

	public void setServer_id(int server_id) {
		this.server_id = server_id;
	}

}
