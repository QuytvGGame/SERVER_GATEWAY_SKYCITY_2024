package com.ggame.services;

import org.json.JSONException;
import org.json.JSONObject;

public class InstallGameResponse {
	public int errorCode;
	public String message;
	public String linkImgBanner;
	public String linkBanner;
	public String linkFanpage;
	public String linkGroup;
	public String hotline;
	public String thongBao;
	public String linkWebGame;
	public String email;
	public String linkEvent;

	public JSONObject toJson() {
		JSONObject object = new JSONObject();
		try {
			object.put("errorCode", errorCode);
			object.put("message", message);
			object.put("linkImgBanner", linkImgBanner);
			object.put("linkBanner", linkBanner);
			object.put("linkFanpage", linkFanpage);
			object.put("linkGroup", linkGroup);
			object.put("hotline", hotline);
			object.put("thongBao", thongBao);
			object.put("linkWebGame", linkWebGame);
			object.put("email", email);
			object.put("linkEvent", linkEvent);
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

	public String getLinkImgBanner() {
		return linkImgBanner;
	}

	public void setLinkImgBanner(String linkImgBanner) {
		this.linkImgBanner = linkImgBanner;
	}

	public String getLinkBanner() {
		return linkBanner;
	}

	public void setLinkBanner(String linkBanner) {
		this.linkBanner = linkBanner;
	}

	public String getLinkFanpage() {
		return linkFanpage;
	}

	public void setLinkFanpage(String linkFanpage) {
		this.linkFanpage = linkFanpage;
	}

	public String getLinkGroup() {
		return linkGroup;
	}

	public void setLinkGroup(String linkGroup) {
		this.linkGroup = linkGroup;
	}

	public String getHotline() {
		return hotline;
	}

	public void setHotline(String hotline) {
		this.hotline = hotline;
	}

	public String getThongBao() {
		return thongBao;
	}

	public void setThongBao(String thongBao) {
		this.thongBao = thongBao;
	}

	public String getLinkWebGame() {
		return linkWebGame;
	}

	public void setLinkWebGame(String linkWebGame) {
		this.linkWebGame = linkWebGame;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLinkEvent() {
		return linkEvent;
	}

	public void setLinkEvent(String linkEvent) {
		this.linkEvent = linkEvent;
	}

}
