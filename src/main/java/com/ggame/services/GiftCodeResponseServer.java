package com.ggame.services;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GiftCodeResponseServer {
	private int errorCode;
	private int status;
	private int typegiftcode;
	private String namegiftcode;
	private String message; // nội dung
	private int levelmin; // cấp độ tối thiểu dùng code
	private List<GiftItemThanhChien> lstGiftItem = new ArrayList<>();
	private List<GiftItemBabylon> lstGiftItemBabylon = new ArrayList<>();

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
			for (GiftItemThanhChien gift : lstGiftItem) {
				jsonArray.put(gift);
			}
			object.put("lstGiftItem", jsonArray);
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getLevelmin() {
		return levelmin;
	}

	public void setLevelmin(int levelmin) {
		this.levelmin = levelmin;
	}

	public List<GiftItemThanhChien> getLstGiftItem() {
		return lstGiftItem;
	}

	public void setLstGiftItem(List<GiftItemThanhChien> lstGiftItem) {
		this.lstGiftItem = lstGiftItem;
	}

	public List<GiftItemBabylon> getLstGiftItemBabylon() {
		return lstGiftItemBabylon;
	}

	public void setLstGiftItemBabylon(List<GiftItemBabylon> lstGiftItemBabylon) {
		this.lstGiftItemBabylon = lstGiftItemBabylon;
	}

}
