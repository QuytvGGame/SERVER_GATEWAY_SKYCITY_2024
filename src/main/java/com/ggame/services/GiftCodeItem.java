package com.ggame.services;

import org.json.JSONException;
import org.json.JSONObject;

public class GiftCodeItem {
	private String gift;

	public GiftCodeItem(String gift) {
		super();
		this.gift = gift;
	}

	public JSONObject toJson() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("gift", gift);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

	public String getGift() {
		return gift;
	}

	public void setGift(String gift) {
		this.gift = gift;
	}

}
