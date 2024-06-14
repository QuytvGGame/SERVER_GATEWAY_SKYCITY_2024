package com.ggame.entity.player;

import org.json.JSONException;
import org.json.JSONObject;

public class InfoAccount {
	private String username;
	private String hoten;
	private int gioitinh;
	private String ngaysinh;
	private String diachi;
	private String cccd;
	private String ngaycap;
	private String noicap;
	private String sdt;

	public JSONObject toJson() {
		JSONObject object = new JSONObject();
		try {
			object.put("username", username);
			object.put("hoten", hoten);
			object.put("gioitinh", gioitinh);
			object.put("ngaysinh", ngaysinh);
			object.put("diachi", diachi);
			object.put("cccd", cccd);
			object.put("ngaycap", ngaycap);
			object.put("noicap", noicap);
			object.put("sdt", sdt);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object;
	}

	public InfoAccount(String username, String hoten, int gioitinh, String ngaysinh, String diachi, String cccd,
			String ngaycap, String noicap, String sdt) {
		super();
		this.username = username;
		this.hoten = hoten;
		this.gioitinh = gioitinh;
		this.ngaysinh = ngaysinh;
		this.diachi = diachi;
		this.cccd = cccd;
		this.ngaycap = ngaycap;
		this.noicap = noicap;
		this.sdt = sdt;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getHoten() {
		return hoten;
	}

	public void setHoten(String hoten) {
		this.hoten = hoten;
	}

	public int getGioitinh() {
		return gioitinh;
	}

	public void setGioitinh(int gioitinh) {
		this.gioitinh = gioitinh;
	}

	public String getNgaysinh() {
		return ngaysinh;
	}

	public void setNgaysinh(String ngaysinh) {
		this.ngaysinh = ngaysinh;
	}

	public String getDiachi() {
		return diachi;
	}

	public void setDiachi(String diachi) {
		this.diachi = diachi;
	}

	public String getCccd() {
		return cccd;
	}

	public void setCccd(String cccd) {
		this.cccd = cccd;
	}

	public String getNgaycap() {
		return ngaycap;
	}

	public void setNgaycap(String ngaycap) {
		this.ngaycap = ngaycap;
	}

	public String getNoicap() {
		return noicap;
	}

	public void setNoicap(String noicap) {
		this.noicap = noicap;
	}

	public String getSdt() {
		return sdt;
	}

	public void setSdt(String sdt) {
		this.sdt = sdt;
	}

}
