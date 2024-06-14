package com.ggame.services;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import com.ggame.entity.player.CountryInfo;

public class GetCountryResponse {
	private int errorCode;
	private String message;
	private List<CountryInfo> countryInfo = new ArrayList<>();

	public JSONObject toJson() {
		JSONObject object = new JSONObject();
		try {
			object.put("errorCode", errorCode);
			object.put("message", message);
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

	public List<CountryInfo> getCountryInfo() {
		return countryInfo;
	}

	public void setCountryInfo(List<CountryInfo> countryInfo) {
		this.countryInfo = countryInfo;
	}

}
