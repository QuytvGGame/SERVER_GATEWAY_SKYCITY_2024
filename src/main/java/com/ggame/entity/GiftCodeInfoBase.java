package com.ggame.entity;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import com.ggame.services.GiftCodeItem;
import com.ggame.services.GiftItemBabylon;
import com.ggame.services.GiftItemThanhChien;
import com.ggame.services.ServerItem;

import config.GameId;

public class GiftCodeInfoBase {
	private int id;
	private int gameId;
	private int isAllServer;
	private List<ServerItem> lstServer = new ArrayList<>();
	private int typegiftcode;
	private String namegiftcode;
	private String timeopen;
	private String timeclose;
	private int quantity;
	private List<GiftItemThanhChien> lstGiftItem = new ArrayList<>();
	private List<GiftItemBabylon> lstGiftItemBabylon = new ArrayList<>();

	private List<GiftCodeItem> lstGiftCode = new ArrayList<>();
	private int levelMin;
	private int isDelete;

	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		try {
			json.put("id", id);
			json.put("gameId", gameId);
			json.put("isAllServer", isAllServer);
			json.put("lstServer", lstServer);
			JSONArray jsonArray = new JSONArray();
			for (ServerItem item : lstServer) {
				jsonArray.put(item.toJson());
			}
			json.put("lstServer", jsonArray);
			json.put("typegiftcode", typegiftcode);
			json.put("namegiftcode", namegiftcode);
			json.put("timeopen", timeopen);
			json.put("timeclose", timeclose);
			json.put("quantity", quantity);
			json.put("levelMin", levelMin);

			JSONArray jsonArray2 = new JSONArray();
			for (GiftItemThanhChien item : lstGiftItem) {
				jsonArray2.put(item.toJson());
			}
			json.put("lstGiftItem", jsonArray2);

			JSONArray jsonArray3 = new JSONArray();
			for (GiftCodeItem item : lstGiftCode) {
				jsonArray3.put(item.toJson());
			}
			json.put("lstGiftCode", jsonArray3);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	public JSONObject toJsonSave() {
		JSONObject json = new JSONObject();
		try {
			json.put("id", id);
			json.put("gameId", gameId);
			json.put("isAllServer", isAllServer);
			json.put("lstServer", lstServer);
			json.put("lstGiftItem", lstGiftItem);
			JSONArray jsonArray = new JSONArray();
			for (ServerItem item : lstServer) {
				jsonArray.put(item.toJson());
			}

			json.put("lstServer", jsonArray);
			JSONArray jsonArray2 = new JSONArray();
			if (gameId == GameId.THANH_CHIEN) {
				for (GiftItemThanhChien item : lstGiftItem) {
					jsonArray2.put(item.toJson());
				}
			} else {
				for (GiftItemBabylon item : lstGiftItemBabylon) {
					jsonArray2.put(item.toJson());
				}
			}

			json.put("lstGiftItem", jsonArray2);

			JSONArray jsonArray3 = new JSONArray();
			for (GiftCodeItem item : lstGiftCode) {
				jsonArray3.put(item.toJson());
			}
			json.put("lstGiftCode", jsonArray3);

			json.put("typegiftcode", typegiftcode);
			json.put("namegiftcode", namegiftcode);
			json.put("timeopen", timeopen);
			json.put("timeclose", timeclose);
			json.put("quantity", quantity);
			json.put("levelMin", levelMin);
			json.put("isDelete", isDelete);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	public GiftCodeInfoBase(String json) {
		try {
			JSONObject object = new JSONObject(json);
			this.id = object.getInt("id");
			this.gameId = object.getInt("gameId");
			this.isAllServer = object.getInt("gameId");
			this.typegiftcode = object.getInt("typegiftcode");
			this.namegiftcode = object.getString("namegiftcode");
			this.timeopen = object.getString("timeopen");
			this.timeclose = object.getString("timeclose");
			this.quantity = object.getInt("quantity");
			this.levelMin = object.getInt("levelMin");
			this.isDelete = object.getInt("isDelete");

			List<ServerItem> lstServer = new ArrayList<>();
			JSONArray arrayServer = object.getJSONArray("lstServer");
			if (arrayServer != null) {
				JSONObject object2 = null;
				for (int j = 0; j < arrayServer.length(); j++) {
					object2 = arrayServer.getJSONObject(j);
					int server = object2.getInt("server");
					ServerItem item = new ServerItem(server);
					lstServer.add(item);
				}
			}
			this.lstServer = lstServer;

			if (this.gameId == GameId.THANH_CHIEN) {
				List<GiftItemThanhChien> lstGiftItem = new ArrayList<>();
				JSONArray arrayGiftItem = object.getJSONArray("lstGiftItem");
				if (arrayGiftItem != null) {
					JSONObject object2 = null;
					for (int j = 0; j < arrayGiftItem.length(); j++) {
						object2 = arrayGiftItem.getJSONObject(j);
						int typeId = object2.getInt("typeId");
						int type = object2.getInt("type");
						int quantity = object2.getInt("quantity");
						GiftItemThanhChien item = new GiftItemThanhChien(typeId, type, quantity);
						lstGiftItem.add(item);
					}
				}
				this.lstGiftItem = lstGiftItem;
			} else {
				List<GiftItemBabylon> lstGiftItemBabylon = new ArrayList<>();
				JSONArray arrayGiftItem = object.getJSONArray("lstGiftItem");
				if (arrayGiftItem != null) {
					JSONObject object2 = null;
					for (int j = 0; j < arrayGiftItem.length(); j++) {
						object2 = arrayGiftItem.getJSONObject(j);
						int idIt = object2.getInt("idIt");
						int type = object2.getInt("type");
						int quantity = object2.getInt("quantity");
						GiftItemBabylon item = new GiftItemBabylon(idIt, type, quantity);
						lstGiftItemBabylon.add(item);
					}
				}
				this.lstGiftItemBabylon = lstGiftItemBabylon;
			}

			List<GiftCodeItem> lstGiftCode = new ArrayList<>();
			JSONArray arrayGiftCode = object.getJSONArray("lstGiftCode");
			if (arrayGiftCode != null) {
				JSONObject object2 = null;
				for (int j = 0; j < arrayGiftCode.length(); j++) {
					object2 = arrayGiftCode.getJSONObject(j);
					String gift = object2.getString("gift");
					GiftCodeItem item = new GiftCodeItem(gift);
					lstGiftCode.add(item);
				}
			}
			this.lstGiftCode = lstGiftCode;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public GiftCodeInfoBase(int id, int gameId, int isAllServer, List<ServerItem> lstServer, int typegiftcode,
			String namegiftcode, String timeopen, String timeclose, int quantity, List<GiftItemThanhChien> lstGiftItem,
			int levelMin, List<GiftCodeItem> lstGiftCode) {
		super();
		this.id = id;
		this.gameId = gameId;
		this.isAllServer = isAllServer;
		this.lstServer = lstServer;
		this.typegiftcode = typegiftcode;
		this.namegiftcode = namegiftcode;
		this.timeopen = timeopen;
		this.timeclose = timeclose;
		this.quantity = quantity;
		this.lstGiftItem = lstGiftItem;
		this.lstGiftCode = lstGiftCode;
		this.levelMin = levelMin;
	}

	public GiftCodeInfoBase(int id, int isAllServer, List<ServerItem> lstServer, int typegiftcode, String namegiftcode,
			String timeopen, String timeclose, int quantity, List<GiftItemBabylon> lstGiftItem, int levelMin,
			List<GiftCodeItem> lstGiftCode) {
		super();
		this.id = id;
		this.gameId = GameId.BABYLON;
		this.isAllServer = isAllServer;
		this.lstServer = lstServer;
		this.typegiftcode = typegiftcode;
		this.namegiftcode = namegiftcode;
		this.timeopen = timeopen;
		this.timeclose = timeclose;
		this.quantity = quantity;
		this.lstGiftItemBabylon = lstGiftItem;
		this.lstGiftCode = lstGiftCode;
		this.levelMin = levelMin;
	}

	public GiftCodeInfoBase() {
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public int getIsAllServer() {
		return isAllServer;
	}

	public void setIsAllServer(int isAllServer) {
		this.isAllServer = isAllServer;
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

	public String getTimeopen() {
		return timeopen;
	}

	public void setTimeopen(String timeopen) {
		this.timeopen = timeopen;
	}

	public String getTimeclose() {
		return timeclose;
	}

	public void setTimeclose(String timeclose) {
		this.timeclose = timeclose;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getLevelMin() {
		return levelMin;
	}

	public void setLevelMin(int levelMin) {
		this.levelMin = levelMin;
	}

	public boolean checkServer(int serverId) {
		for (ServerItem server : lstServer) {
			if (serverId == server.getServer())
				return true;

		}
		return false;
	}

	public List<ServerItem> getLstServer() {
		return lstServer;
	}

	public void setLstServer(List<ServerItem> lstServer) {
		this.lstServer = lstServer;
	}

	public List<GiftItemThanhChien> getLstGiftItem() {
		return lstGiftItem;
	}

	public void setLstGiftItem(List<GiftItemThanhChien> lstGiftItem) {
		this.lstGiftItem = lstGiftItem;
	}

	public List<GiftCodeItem> getLstGiftCode() {
		return lstGiftCode;
	}

	public void setLstGiftCode(List<GiftCodeItem> lstGiftCode) {
		this.lstGiftCode = lstGiftCode;
	}

	public int getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}

	public List<GiftItemBabylon> getLstGiftItemBabylon() {
		return lstGiftItemBabylon;
	}

	public void setLstGiftItemBabylon(List<GiftItemBabylon> lstGiftItemBabylon) {
		this.lstGiftItemBabylon = lstGiftItemBabylon;
	}

}
