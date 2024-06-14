package com.ggame.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GiftCodeData {
	public long user_id;
	public int server_id;
	public String game_id;
	public int typegiftcode;
	public String namegiftcode;
	public String giftcode;

	public JSONObject toJson() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("user_id", user_id);
			jsonObject.put("server_id", server_id);
			jsonObject.put("game_id", game_id);
			jsonObject.put("typegiftcode", typegiftcode);
			jsonObject.put("namegiftcode", namegiftcode);
			jsonObject.put("giftcode", giftcode);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

	public GiftCodeData(long user_id, int server_id, String game_id, int typegiftcode, String namegiftcode,
			String giftcode) {
		super();
		this.user_id = user_id;
		this.server_id = server_id;
		this.game_id = game_id;
		this.typegiftcode = typegiftcode;
		this.namegiftcode = namegiftcode;
		this.giftcode = giftcode;
	}

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	public int getServer_id() {
		return server_id;
	}

	public void setServer_id(int server_id) {
		this.server_id = server_id;
	}

	public String getGame_id() {
		return game_id;
	}

	public void setGame_id(String game_id) {
		this.game_id = game_id;
	}

	public int getTypegiftcode() {
		return typegiftcode;
	}

	public void setTypegiftcode(int typegiftcode) {
		this.typegiftcode = typegiftcode;
	}

	public String getNamegiftcode() {
		return namegiftcode;
	}

	public void setNamegiftcode(String namegiftcode) {
		this.namegiftcode = namegiftcode;
	}

	public String getGiftcode() {
		return giftcode;
	}

	public void setGiftcode(String giftcode) {
		this.giftcode = giftcode;
	}

}
