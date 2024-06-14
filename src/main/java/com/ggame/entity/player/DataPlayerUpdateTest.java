package com.ggame.entity.player;

public class DataPlayerUpdateTest {
	private long userId;
	private String ip;
	private String imei;

	public DataPlayerUpdateTest(long userId, String ip, String imei) {
		super();
		this.userId = userId;
		this.ip = ip;
		this.imei = imei;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
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

}
