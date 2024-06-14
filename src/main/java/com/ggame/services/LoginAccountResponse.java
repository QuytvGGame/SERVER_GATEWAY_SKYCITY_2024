package com.ggame.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.ggame.entity.ServerInfoBase;
import com.ggame.entity.player.ServerPlayerData;

public class LoginAccountResponse {
	private int errorCode;
	private String message;
	private Boolean updateVersion;
	private String linkUpdate;
	private long userId;
	private String userName;
	private List<ServerInfoBase> serverInfo = new ArrayList<>();
	private List<ServerPlayerData> serverPlayerData = new ArrayList<>();

	public JSONObject toJson() {
		JSONObject object = new JSONObject();
		try {
			object.put("errorCode", errorCode);
			object.put("message", message);
			object.put("updateVersion", updateVersion);
			object.put("linkUpdate", linkUpdate);
			object.put("userId", userId);
			object.put("userName", userName);
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

	public Boolean getUpdateVersion() {
		return updateVersion;
	}

	public void setUpdateVersion(Boolean updateVersion) {
		this.updateVersion = updateVersion;
	}

	public String getLinkUpdate() {
		return linkUpdate;
	}

	public void setLinkUpdate(String linkUpdate) {
		this.linkUpdate = linkUpdate;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<ServerInfoBase> getServerInfo() {
		return serverInfo;
	}

	public void setServerInfo(List<ServerInfoBase> serverInfo) {
		this.serverInfo = serverInfo;
	}

	public List<ServerPlayerData> getServerPlayerData() {
		return serverPlayerData;
	}

	public void setServerPlayerData(List<ServerPlayerData> serverPlayerData) {
		this.serverPlayerData = serverPlayerData;
	}

}
