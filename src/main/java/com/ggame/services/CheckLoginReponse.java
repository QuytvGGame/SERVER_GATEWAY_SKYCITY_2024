package com.ggame.services;

import org.json.JSONObject;

public class CheckLoginReponse {
	public String username;
	public String passWord;
	public long userIdGame;
	public String email;
	public int errorCode;
	public String message;
	public int isUpdate;
	public String ip;
	public String imei;
	public int isReload;

	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		try {
			json.put("username", username);
			json.put("userIdGame", userIdGame);
			json.put("email", email);
			json.put("errorCode", errorCode);
			json.put("message", message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public long getUserIdGame() {
		return userIdGame;
	}

	public void setUserIdGame(long userIdGame) {
		this.userIdGame = userIdGame;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public int getIsUpdate() {
		return isUpdate;
	}

	public void setIsUpdate(int isUpdate) {
		this.isUpdate = isUpdate;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public int getIsReload() {
		return isReload;
	}

	public void setIsReload(int isReload) {
		this.isReload = isReload;
	}

}
