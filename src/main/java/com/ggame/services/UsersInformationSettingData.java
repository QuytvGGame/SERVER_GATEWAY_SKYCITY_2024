package com.ggame.services;

public class UsersInformationSettingData {
	private int id;
	private String gameId;
	private int platform;
	private int providerId;
	private String imei;
	private int install;
	private String ip;
	private String deviceName;

	public UsersInformationSettingData(String gameId, int platform, int providerId, String imei, int install,
			String deviceName) {
		super();
		this.gameId = gameId;
		this.platform = platform;
		this.providerId = providerId;
		this.imei = imei;
		this.install = install;
		this.deviceName = deviceName;
	}

	public UsersInformationSettingData() {
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public int getPlatform() {
		return platform;
	}

	public void setPlatform(int platform) {
		this.platform = platform;
	}

	public int getProviderId() {
		return providerId;
	}

	public void setProviderId(int providerId) {
		this.providerId = providerId;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public int getInstall() {
		return install;
	}

	public void setInstall(int install) {
		this.install = install;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

}
