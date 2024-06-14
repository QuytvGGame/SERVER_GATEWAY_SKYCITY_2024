package com.ggame.services;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateAccountPlayNowRequest {
	public String username;
	public String password;
	public String username2;
	public String password2;
	public String email;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername2() {
		return username2;
	}

	public void setUsername2(String username2) {
		this.username2 = username2;
	}

	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public JSONObject toJson() {
		JSONObject object = new JSONObject();
		try {
			object.put("username", username);
			object.put("password", password);
			object.put("username2", username2);
			object.put("password2", password2);
			object.put("email", email);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object;
	}
}
