package com.ggame.services;

public class GetThongBaoResponse {
	public int errorCode;
	public String message;
	public String thongBao;
	public String thongBaoIOS;

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

	public String getThongBao() {
		return thongBao;
	}

	public void setThongBao(String thongBao) {
		this.thongBao = thongBao;
	}

	public String getThongBaoIOS() {
		return thongBaoIOS;
	}

	public void setThongBaoIOS(String thongBaoIOS) {
		this.thongBaoIOS = thongBaoIOS;
	}

}
