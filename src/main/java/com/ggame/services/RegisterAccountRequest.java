package com.ggame.services;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterAccountRequest {
	public String gameid;
	public String user;
	public String password;
	public String rePassword;
	public String email;
	public int platform;
	public int providerId;
	public String ime;
	public String serverIp; // ip của người chơi
	public String deviceName;
	public int language;
//	public int isEmulator; // 1- là giả lập, 0- thiết bị di động

	public JSONObject toJson() {
		JSONObject object = new JSONObject();
		try {
			object.put("gameId", gameid);
			object.put("user", user);
			object.put("password", password);
			object.put("rePassword", rePassword);
			object.put("email", email);
			object.put("platform", platform);
			object.put("ime", ime);
			object.put("providerId", providerId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object;
	}

	public String getGameid() {
		return gameid;
	}
	
	public int getGameidInt() {
		return Integer.parseInt(this.gameid);
	}

	public void setGameid(String gameid) {
		this.gameid = gameid;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRePassword() {
		return rePassword;
	}

	public void setRePassword(String rePassword) {
		this.rePassword = rePassword;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getPlatform() {
		return platform;
	}

	public void setPlatform(int platform) {
		this.platform = platform;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public int getProviderId() {
		return providerId;
	}

	public void setProviderId(int providerId) {
		this.providerId = providerId;
	}

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

//	public int getIsEmulator() {
//		return isEmulator;
//	}
//
//	public void setIsEmulator(int isEmulator) {
//		this.isEmulator = isEmulator;
//	}

}
