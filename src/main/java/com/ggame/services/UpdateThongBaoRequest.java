package com.ggame.services;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateThongBaoRequest {
	public String thongBao;
	public int gameId;
	public int platform; // 1:Android, 2:IOS
	
	public JSONObject toJson() {
		JSONObject object = new JSONObject();
		try {
			object.put("thongBao", thongBao);
			object.put("gameId", gameId);
			object.put("platform", platform);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object;
	}
}
