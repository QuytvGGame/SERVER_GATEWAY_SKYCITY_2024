package com.ggame.services;

public class IpAddressData {
	private String ip;
	private int platform;
	private int providerId;
	private String deviceName;
	private int quantity;

	public IpAddressData(String ip, int quantity, int platform, int providerId, String deviceName) {
		super();
		this.ip = ip;
		this.quantity = quantity;
		this.platform = platform;
		this.providerId = providerId;
		this.deviceName = deviceName;
	}

	public IpAddressData() {
		// TODO Auto-generated constructor stub
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int addQuantity() {
		quantity++;
		return quantity;
	}

	public int getProviderId() {
		return providerId;
	}

	public void setProviderId(int providerId) {
		this.providerId = providerId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getPlatform() {
		return platform;
	}

	public void setPlatform(int platform) {
		this.platform = platform;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

}
