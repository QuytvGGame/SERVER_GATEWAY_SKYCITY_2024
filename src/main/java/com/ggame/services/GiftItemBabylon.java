package com.ggame.services;

import org.json.JSONException;
import org.json.JSONObject;


public class GiftItemBabylon {
	private int idIt;
	private int type;
	private long quantity;
	private int ratio = 100; // tỷ lệ

	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		try {
			json.put("idIt", idIt);
			json.put("type", type);
			json.put("quantity", quantity);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}

	public GiftItemBabylon(int idIt, int type, long quantity, int ratio) {
		super();
		this.idIt = idIt;
		this.type = type;
		this.quantity = quantity;
		this.ratio = ratio;
	}

	public GiftItemBabylon(JSONObject jsonObject) {
		try {
			this.idIt = jsonObject.getInt("idIt");
			this.quantity = jsonObject.getInt("quantity");
			this.type = jsonObject.getInt("type");
			this.ratio = 100;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public GiftItemBabylon(int idIt, int type, long quantity) {
		super();
		this.idIt = idIt;
		this.type = type;
		this.quantity = quantity;
		this.ratio = 100;
	}

	public GiftItemBabylon(GiftItemBabylon giftClone) {
		this.idIt = giftClone.idIt;
		this.type = giftClone.type;
		this.quantity = giftClone.quantity;
		this.ratio = giftClone.ratio;
	}

	public GiftItemBabylon() {
		// TODO Auto-generated constructor stub
	}

	public int getIdIt() {
		return idIt;
	}

	public void setIdIt(int id) {
		this.idIt = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getQuantity() {
		return quantity;
	}

	public int getQuantityInt() {
		return (int) quantity;
	}

	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}

	public int getRatio() {
		return ratio;
	}

	public void setRatio(int ratio) {
		this.ratio = ratio;
	}
}
