package com.ggame.services;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginAccountRequest {
	private String gameid;
	private int version;
	private String user;
	private String password;
	private int platform;
	private int providerId;
	private String imei;
	private int loginType;
	public String serverIp; // ip của người chơi
	public int language;
//	public int isEmulator; // 1- là giả lập, 0- thiết bị di động

	public JSONObject toJson() {
		JSONObject object = new JSONObject();
		try {
			object.put("gameId", gameid);
			object.put("version", version);
			object.put("user", user);
			object.put("password", password);
			object.put("platform", platform);
			object.put("providerId", providerId);
			object.put("imei", imei);
			object.put("loginType", loginType);
			object.put("serverIp", serverIp);
//			object.put("isEmulator", isEmulator);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object;
	}

	public String getGameid() {
		return gameid;
	}

	public void setGameid(String gameid) {
		this.gameid = gameid;
	}

	public int getGameidInt() {
		return Integer.parseInt(this.gameid);
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
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

	public int getPlatform() {
		return platform;
	}

	public void setPlatform(int platform) {
		this.platform = platform;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public int getLoginType() {
		return loginType;
	}

	public void setLoginType(int loginType) {
		this.loginType = loginType;
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
