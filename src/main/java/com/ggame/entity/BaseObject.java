package com.ggame.entity;

import org.json.JSONObject;

public class BaseObject {

	/**
	 * Đánh dấu xem dữ liệu có thay đổi ko để update vào db
	 */
	public boolean isUpdate;

	public JSONObject toJson(){
		return new JSONObject();
	}

}
