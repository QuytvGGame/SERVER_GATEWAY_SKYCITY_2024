package com.ggame.services;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GiftCodeResponseWeb {
	private int errorCode;
	private int status;
	private int typegiftcode;
	private String namegiftcode;
	private String message; // nội dung
	private int levelmin; // cấp độ tối thiểu dùng code
//	private List<GiftItem> lstGiftItem = new ArrayList<>();
	private List<String> lstGiftCode = new ArrayList<>();

	public JSONObject toJson() {
		JSONObject object = new JSONObject();
		try {
			object.put("errorCode", errorCode);
			object.put("status", status);
			object.put("typegiftcode", typegiftcode);
			object.put("namegiftcode", namegiftcode);
			object.put("message", message);
			object.put("levelmin", levelmin);

			JSONArray jsonArray = new JSONArray();
			for (String gift : lstGiftCode) {
				jsonArray.put(gift);
			}
			object.put("lstGiftCode", jsonArray);
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

	public List<String> getLstGiftCode() {
		return lstGiftCode;
	}

	public void setLstGiftCode(List<String> lstGiftCode) {
		this.lstGiftCode = lstGiftCode;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getTypegiftcode() {
		return typegiftcode;
	}

	public void setTypegiftcode(int typegiftcode) {
		this.typegiftcode = typegiftcode;
	}

	public String getNamegiftcode() {
		return namegiftcode;
	}

	public void setNamegiftcode(String namegiftcode) {
		this.namegiftcode = namegiftcode;
	}

	public int getLevelmin() {
		return levelmin;
	}

	public void setLevelmin(int levelmin) {
		this.levelmin = levelmin;
	}

}
