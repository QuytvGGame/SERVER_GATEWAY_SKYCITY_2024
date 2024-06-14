package com.ggame.entity;

import org.json.JSONObject;

public class ServerInfoBase {
	private int id;
	private String gameId;
	private String serverId;
	private String serverName;
	private String serverIp;
	private int serverPort;
	private int status;
	private int loginIp;
	private String dateOpenServer;

	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		try {
			json.put("id", id);
			json.put("gameId", gameId);
			json.put("serverId", serverId);
			json.put("serverName", serverName);
			json.put("serverIp", serverIp);
			json.put("serverPort", serverPort);
			json.put("status", status);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(int loginIp) {
		this.loginIp = loginIp;
	}

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public Integer getServerPort() {
		return serverPort;
	}

	public void setServerPort(Integer serverPort) {
		this.serverPort = serverPort;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public String getDateOpenServer() {
		return dateOpenServer;
	}

	public void setDateOpenServer(String dateOpenServer) {
		this.dateOpenServer = dateOpenServer;
	}
}
