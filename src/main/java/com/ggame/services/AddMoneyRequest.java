package com.ggame.services;

import org.json.JSONObject;

public class AddMoneyRequest {
	public String username;
	public int addmoney;
	public String sender;
	public String title;
	public String content;
	public int serverId;
	public long userId;
	public int gameId;
	public int typeSpecial; // Type gói đặc biệt : 0- Normal, 2-THE_THANG, 3-THE_QUY, 4-QUY_TRUONG_THANH_VIP
	public String productId;
	public int status;
	public int idGoi;
	public int reChange; // 1-cổng, 2- inapp

	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		try {
			json.put("username", username);
			json.put("addmoney", addmoney);
			json.put("sender", sender);
			json.put("title", title);
			json.put("content", content);
			json.put("serverId", serverId);
			json.put("userId", userId);
			json.put("gameId", gameId);
			json.put("typeSpecial", typeSpecial);
			json.put("productId", productId);
			json.put("status", status);
			json.put("idGoi", idGoi);
			json.put("reChange", reChange);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
}
