package com.ggame.services;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.ggame.accountservice.T;

public class LockAccountRequest {
	private String user_name; // gửi nhiều user ngăn cách dùng bằng
	private String game_id; // gameId
	private String note; // lý do khóa tài khoản
	private String locker; // tên người khóa
	private int status; // 1: lock, 2: unlock

	public JSONObject toJson() {
		JSONObject object = new JSONObject();
		try {
			object.put("user_name", user_name);
			object.put("game_id", game_id);
			object.put("note", note);
			object.put("locker", locker);
			object.put("status", status);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object;
	}

	public String getUser_name() {
		return user_name;
	}

	public List<String> getLstUser_name() {
		List<String> lst = new ArrayList<String>();
		if (user_name != null) {
			String[] array = user_name.split(T.PhanTachPhay);
			if (array != null) {
				for (int i = 0; i < array.length; i++) {
					lst.add(array[i]);
				}
			}
		}
		return lst;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getGame_id() {
		return game_id;
	}

	public void setGame_id(String game_id) {
		this.game_id = game_id;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getLocker() {
		return locker;
	}

	public void setLocker(String locker) {
		this.locker = locker;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
