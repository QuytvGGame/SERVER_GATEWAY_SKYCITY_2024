package com.ggame.entity.player;

import org.json.JSONException;
import org.json.JSONObject;

public class TextInfo {
	private int id;
	private String text;

	public TextInfo() {
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public JSONObject toJson() {
		JSONObject object = new JSONObject();
		try {
			object.put("id", id);
			object.put("text", text);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object;
	}
}
