package com.ggame.services;

import org.json.JSONException;
import org.json.JSONObject;

import com.ggame.accountservice.T;

public class InfoAccountResponse {
	private int errorCode;
	private String message;
	private int isUpdate; // 1- đã cập nhật, 0-chưa cập nhật

	private String hoten;
	private int gioitinh; // 1- name, 0- nữ
	private String ngaysinh;
	private String diachi;
	private String cccd;
	private String ngaycap;
	private String noicap;
	private String sdt;

	public JSONObject toJson() {
		JSONObject object = new JSONObject();
		try {
			object.put("errorCode", errorCode);
			object.put("message", message);
			object.put("isUpdate", isUpdate);

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

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getIsUpdate() {
		return isUpdate;
	}

	public void setIsUpdate(int isUpdate) {
		this.isUpdate = isUpdate;
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

	public void setDataEmtpy() {
		this.hoten = T.Empty;
		this.gioitinh = 0;
		this.ngaysinh = T.Empty;
		this.diachi = T.Empty;
		this.cccd = T.Empty;
		this.ngaycap = T.Empty;
		this.noicap = T.Empty;
		this.sdt = T.Empty;
	}

}
