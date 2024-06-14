package com.ggame.services;

import org.json.JSONObject;

public class UpdateInfoAccountRequest {
	public String gameid;
	public String username;
	public String hoten;
	public int gioitinh;
	public String ngaysinh;
	public String diachi;
	public String cccd;
	public String ngaycap;
	public String noicap;
	public String sdt;

	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		try {
			json.put("gameid", gameid);
			json.put("username", username);
			json.put("hoten", hoten);
			json.put("gioitinh", gioitinh);
			json.put("diachi", diachi);
			json.put("cccd", cccd);
			json.put("ngaycap", ngaycap);
			json.put("noicap", noicap);
			json.put("sdt", sdt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
}
