package com.ggame.services;

public class ImeiAddressData {
	private String imei;
	private int platform;
	private int providerId;
	private String deviceName;
	private int quantity;

	public ImeiAddressData(String imei, int quantity, int platform, int providerId, String deviceName) {
		super();
		this.imei = imei;
		this.quantity = quantity;
		this.platform = platform;
		this.providerId = providerId;
		this.deviceName = deviceName;
	}

	public ImeiAddressData() {
		// TODO Auto-generated constructor stub
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public int addQuantity() {
		quantity++;
		return quantity;
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

	public int getProviderId() {
		return providerId;
	}

	public void setProviderId(int providerId) {
		this.providerId = providerId;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

}
