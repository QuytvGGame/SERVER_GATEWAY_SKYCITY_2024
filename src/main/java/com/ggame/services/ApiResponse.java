package com.ggame.services;

import org.json.JSONException;
import org.json.JSONObject;

public class ApiResponse {
	private int errorCode;
	private String message;
	private String registerAccount;
	private String loginAccount;
	private String countryInfo;
	private String textInfo;
	private String infoAccount;
	private String updateAccount;

	private String linkImgBanner;
	private String linkBanner;
	private String linkFanpage;
	private String linkGroup;
	private String hotline;
	private String thongBao;
	private String linkWebGame;
	private String email;
	private String linkEvent;
	private String linkUpdateThongTinCaNhan;
	private String linkDoiMatKhau;
	private String linkVote;
	private String linkDksd;
	private int showIcon;
	private int showUpdateInfo;

	private int isThongBaoPlayNow;
	private int isRegisterGmail;
	private int thoitiet;
	private int isApple;
	private String linksCheckInternet;

	public JSONObject toJson() {
		JSONObject object = new JSONObject();
		try {
			object.put("registerAccount", registerAccount);
			object.put("loginAccount", loginAccount);
			object.put("linkImgBanner", linkImgBanner);
			object.put("linkBanner", linkBanner);
			object.put("linkFanpage", linkFanpage);
			object.put("linkGroup", linkGroup);
			object.put("hotline", hotline);
			object.put("thongBao", thongBao);
			object.put("linkWebGame", linkWebGame);
			object.put("email", email);
			object.put("linkEvent", linkEvent);
			object.put("linkUpdateThongTinCaNhan", linkUpdateThongTinCaNhan);
			object.put("linkDoiMatKhau", linkDoiMatKhau);
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

	public String getCountryInfo() {
		return countryInfo;
	}

	public void setCountryInfo(String countryInfo) {
		this.countryInfo = countryInfo;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getRegisterAccount() {
		return registerAccount;
	}

	public void setRegisterAccount(String registerAccount) {
		this.registerAccount = registerAccount;
	}

	public String getLoginAccount() {
		return loginAccount;
	}

	public void setLoginAccount(String loginAccount) {
		this.loginAccount = loginAccount;
	}

	public String getTextInfo() {
		return textInfo;
	}

	public void setTextInfo(String textInfo) {
		this.textInfo = textInfo;
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

	public String getLinkUpdateThongTinCaNhan() {
		return linkUpdateThongTinCaNhan;
	}

	public void setLinkUpdateThongTinCaNhan(String linkUpdateThongTinCaNhan) {
		this.linkUpdateThongTinCaNhan = linkUpdateThongTinCaNhan;
	}

	public String getLinkDoiMatKhau() {
		return linkDoiMatKhau;
	}

	public void setLinkDoiMatKhau(String linkDoiMatKhau) {
		this.linkDoiMatKhau = linkDoiMatKhau;
	}

	public String getLinkVote() {
		return linkVote;
	}

	public void setLinkVote(String linkVote) {
		this.linkVote = linkVote;
	}

	public int getShowIcon() {
		return showIcon;
	}

	public void setShowIcon(int showIcon) {
		this.showIcon = showIcon;
	}

	public int getIsThongBaoPlayNow() {
		return isThongBaoPlayNow;
	}

	public void setIsThongBaoPlayNow(int isThongBaoPlayNow) {
		this.isThongBaoPlayNow = isThongBaoPlayNow;
	}

	public int getIsRegisterGmail() {
		return isRegisterGmail;
	}

	public void setIsRegisterGmail(int isRegisterGmail) {
		this.isRegisterGmail = isRegisterGmail;
	}

	public String getInfoAccount() {
		return infoAccount;
	}

	public void setInfoAccount(String infoAccount) {
		this.infoAccount = infoAccount;
	}

	public int getShowUpdateInfo() {
		return showUpdateInfo;
	}

	public void setShowUpdateInfo(int showUpdateInfo) {
		this.showUpdateInfo = showUpdateInfo;
	}

	public String getUpdateAccount() {
		return updateAccount;
	}

	public void setUpdateAccount(String updateAccount) {
		this.updateAccount = updateAccount;
	}

	public String getLinkDksd() {
		return linkDksd;
	}

	public void setLinkDksd(String linkDksd) {
		this.linkDksd = linkDksd;
	}

	public int getThoitiet() {
		return thoitiet;
	}

	public void setThoitiet(int thoitiet) {
		this.thoitiet = thoitiet;
	}

	public int getIsApple() {
		return isApple;
	}

	public void setIsApple(int isApple) {
		this.isApple = isApple;
	}

	public String getLinksCheckInternet() {
		return linksCheckInternet;
	}

	public void setLinksCheckInternet(String linksCheckInternet) {
		this.linksCheckInternet = linksCheckInternet;
	}

}
