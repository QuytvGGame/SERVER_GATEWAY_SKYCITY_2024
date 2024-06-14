package com.sgc.game.server;

import org.json.JSONException;
import org.json.JSONObject;

public class GiftItem {
	/** id vật phẩm tương ứng tạo ra cho người chơi */
	private String giftId = "";
	private int typeId;
	private int type;
	private int quantity;
	private int title;

	public GiftItem(int typeId, int type, int quantity) {
		super();
		this.typeId = typeId;
		this.type = type;
		this.quantity = quantity;
	}

	public GiftItem(int typeId, int type, int quantity, String giftId) {
		super();
		this.typeId = typeId;
		this.type = type;
		this.quantity = quantity;
		this.giftId = giftId;
	}

	public GiftItem(GiftItem giftItemClone) {
		this.typeId = giftItemClone.typeId;
		this.type = giftItemClone.type;
		this.quantity = giftItemClone.quantity;
		this.giftId = giftItemClone.giftId;
	}

	public GiftItem() {
		// TODO Auto-generated constructor stub
	}

	public String getGiftId() {
		return giftId;
	}

	public void setGiftId(String giftId) {
		this.giftId = giftId;
	}

	public int getTypeId() {
		return typeId;
	}

	public int getType() {
		return type;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setTypeId(int infoId) {
		this.typeId = infoId;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public JSONObject toJson() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("giftId", getGiftId());
			jsonObject.put("typeId", getTypeId());
			jsonObject.put("type", getType());
			jsonObject.put("quantity", getQuantity());
			if (title > 0)
				jsonObject.put("title", getTitle());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

	public int getTitle() {
		return title;
	}

	public void setTitle(int tile) {
		this.title = tile;
	}
}
