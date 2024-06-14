package com.sgc.game.server;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import com.ggame.accountservice.AgentJava;
import com.ggame.accountservice.T;
import com.ggame.database.DatabaseAccount;
import com.ggame.entity.GiftCodeData;
import com.ggame.entity.GiftCodeInfoBase;
import com.ggame.entity.ServerInfoBase;
import com.ggame.entity.player.CountryInfo;
import com.ggame.entity.player.GiftCodeStatus;
import com.ggame.entity.player.InfoAccount;
import com.ggame.entity.player.LinkConfigInfo;
import com.ggame.entity.player.ServerPlayerData;
import com.ggame.entity.player.TextInfo;
import com.ggame.entity.player.User;
import com.ggame.services.GiftCodeItem;
import com.ggame.services.GiftCodeRequestServer;
import com.ggame.services.ImeiAddressData;
import com.ggame.services.IpAddressData;
import com.ggame.services.LockAccountRequest;
import com.ggame.services.TypeGiftCode;
import com.ggame.services.UpdateInfoAccountRequest;
import com.ggame.services.UsersInformationSettingData;

public class GameManager {
	private static GameManager instance = new GameManager();
	public static Map<Long, Integer> mapCountPushNotification = new HashMap<Long, Integer>();

	private GameManager() {
	}

	public Map<Integer, ServerInfoBase> mapServerInfo = new HashMap<>();
	// Key : gameId -> ServerId
	public Map<String, Map<String, ServerInfoBase>> mapServerInfoInGameId = new HashMap<>();
	public JSONArray jsonArrayMapServerInfo = new JSONArray();

	public void putServerInfoBase(ServerInfoBase info) {
		if (mapServerInfoInGameId.containsKey(info.getGameId())) {
			// đã có gameId
			Map<String, ServerInfoBase> map = mapServerInfoInGameId.get(info.getGameId());
			if (!map.containsKey(info.getServerId())) {
				// Chưa có ServerId
				map.put(info.getServerId(), info);
			}
		} else {
			// Chưa có GameId
			Map<String, ServerInfoBase> map = new HashMap<>();
			map.put(info.getServerId(), info);
			mapServerInfoInGameId.put(info.getGameId(), map);
		}
	}

	public ServerInfoBase getServerInfoBase(String gameId, String serverId) {
		if (mapServerInfoInGameId.containsKey(gameId)) {
			Map<String, ServerInfoBase> map = mapServerInfoInGameId.get(gameId);
			if (map.containsKey(serverId)) {
				return map.get(serverId);
			}
		}
		return null;
	}

	public Map<Integer, ServerInfoBase> getMapServerInfo(String gameId) {
		Map<Integer, ServerInfoBase> mapServer = new HashMap<>();
		for (Integer key : mapServerInfo.keySet()) {
			ServerInfoBase item = mapServerInfo.get(key);
			if (item != null) {
				if (item.getGameId().equals(gameId)) {
					mapServer.put(key, item);
				}
			}
		}

		return mapServer;
	}

	public Map<Integer, ServerInfoBase> getMapServerInfoCheckIp(String gameId) {
		Map<Integer, ServerInfoBase> mapServer = new HashMap<>();
		for (Integer key : mapServerInfo.keySet()) {
			ServerInfoBase item = mapServerInfo.get(key);
			if (item != null) {
				if (item.getGameId().equals(gameId)) {
					if (item.getLoginIp() == T.FALSE) {
						mapServer.put(key, item);
					}
				}
			}
		}

		return mapServer;
	}

	/*
	 * Thông tin của người chơi Key: username
	 */
	public Map<String, InfoAccount> mapInfoAccount = new HashMap<>();

	public InfoAccount getInfoAccount(String username) {
		if (mapInfoAccount.containsKey(username)) {
			return mapInfoAccount.get(username);
		}

		return null;
	}

	public void UpdateInfoAccount(UpdateInfoAccountRequest data) {
		InfoAccount infoAccount = new InfoAccount(data.username, data.hoten, data.gioitinh, data.ngaysinh, data.diachi,
				data.cccd, data.ngaycap, data.noicap, data.sdt);
		if (mapInfoAccount.containsKey(data.username)) {
			// đã có thông tin người chơi
			mapInfoAccount.replace(data.username, infoAccount);
			DatabaseAccount.updateInfoAccount(data);
		} else {
			// chưa có thông tin người chơi
			mapInfoAccount.put(data.username, infoAccount);
			DatabaseAccount.insertInfoAccount(data);
		}
	}

	// Key : gameId
	public Map<Integer, CountryInfo> mapCountryInfo = new HashMap<>();
	// Key : gameId -> Index
	public Map<String, Map<Integer, CountryInfo>> mapCountryInfoInGameId = new HashMap<>();

	public void putCountryInfoBase(CountryInfo info) {
		if (mapCountryInfoInGameId.containsKey(info.getGameId())) {
			// đã có gameId
			Map<Integer, CountryInfo> map = mapCountryInfoInGameId.get(info.getGameId());
			if (!map.containsKey(info.getIdLanguage())) {
				// Chưa có ServerId
				map.put(info.getIdLanguage(), info);
			}
		} else {
			// Chưa có GameId
			Map<Integer, CountryInfo> map = new HashMap<>();
			map.put(info.getIdLanguage(), info);
			mapCountryInfoInGameId.put(info.getGameId(), map);
		}
	}

	public CountryInfo getCountryInfoBase(String gameId, int id) {
		if (mapServerInfoInGameId.containsKey(gameId)) {
			Map<Integer, CountryInfo> map = mapCountryInfoInGameId.get(gameId);
			if (map.containsKey(id)) {
				return map.get(id);
			}
		}
		return null;
	}

	public Map<Integer, CountryInfo> getMapCountryInfo(String gameId) {
//		return mapCountryInfo;
		if (mapCountryInfoInGameId.containsKey(gameId)) {
			return mapCountryInfoInGameId.get(gameId);
		}

		return new HashMap<>();
	}

	public Map<String, GiftCodeInfoBase> mapGiftCode = new HashMap<>();
	/*
	 * Key: userName -> user_id_game
	 */
	public Map<String, Map<Long, ServerPlayerData>> mapServerPlayerData = new HashMap<>();

	// userId -> giftCode
	public Map<Long, Map<String, GiftCodeData>> mapGiftCodeData = new HashMap<>();
	public List<String> listGiftCodeRiengData = new ArrayList<String>();

	public boolean isCheckGiftCodeRieng(String giftCode) {
		for (int i = 0; i < listGiftCodeRiengData.size(); i++) {
			if (listGiftCodeRiengData.get(i).equals(giftCode)) {
				return true;
			}
		}

		return false;
	}

	public ServerPlayerData getPlayerServer(String userName, long userId_id_game) {
		if (mapServerPlayerData.containsKey(userName)) {
			Map<Long, ServerPlayerData> map = mapServerPlayerData.get(userName);
			if (map.containsKey(userId_id_game)) {
				return map.get(userId_id_game);
			}
		}

		return null;
	}

	public void putGiftCodeData(GiftCodeData data) {
		if (!mapGiftCodeData.containsKey(data.user_id)) {
			// chưa có userId
			Map<String, GiftCodeData> map = new HashMap<>();
			map.put(data.giftcode, data);
			mapGiftCodeData.put(data.user_id, map);

		} else {
			Map<String, GiftCodeData> map = mapGiftCodeData.get(data.user_id);
			if (!map.containsKey(data.giftcode)) {
				// chưa có GiftCode
				map.put(data.giftcode, data);

			} else {
				System.out.println(
						"--- GameManager --- putGiftCodeData GiftCode đã tồn tại có --- DATA = " + data.toJson());
			}
		}

		if (!isCheckGiftCodeRieng(data.giftcode)) {
			listGiftCodeRiengData.add(data.giftcode);
		}
	}

	// status:0- không tồn tại, 1- sử dụng được, 2-Code hết hạn, 3-Code chưa tới
	// thời gian sử dụng
	public int isExitsGiftCode(GiftCodeRequestServer request) {
		int status = GiftCodeStatus.FAILT;
		for (String key : GameManager.getInstance().mapGiftCode.keySet()) {
			GiftCodeInfoBase item = GameManager.getInstance().mapGiftCode.get(key);
			if (item != null) {
				for (int i = 0; i < item.getLstGiftCode().size(); i++) {
					GiftCodeItem item2 = item.getLstGiftCode().get(i);
					if (item2.getGift().equals(request.code)) {
//						GiftCodeInfoBase info = GameManager.getInstance().getInfoGiftCode(request.code);
						// Code chưa đủ cấp độ để sử dụng
						if (request.level < item.getLevelMin()) {
							return GiftCodeStatus.NOT_ENOUGH_LEVEL;
						}

						int serverId = Integer.parseInt(request.serverId);
						if (item.getIsAllServer() == T.FALSE) {
							// Không phải Code All Server
							if (!item.checkServer(serverId)) {
								// Không phải Server này
								return GiftCodeStatus.FAILT;
							}
						}

						// Code chưa tới thời gian sử dụng
						if (AgentJava.getCompareTime(item.getTimeopen()) > 0) {
							return GiftCodeStatus.NOT_TIME_YET;
						}

						// Code hết hạn
						if (AgentJava.getCompareTime(item.getTimeclose()) <= 0) {
							return GiftCodeStatus.HET_HAN;
						}

						if (item.getTypegiftcode() != TypeGiftCode.GIFT_CHUNG) {
							if (isCheckGiftCodeRieng(request.code)) {
								return GiftCodeStatus.USED_BY_SOMEONE;
							}
						}

						// tìm thấy, chưa có dữ liệu Data
						if (!mapGiftCodeData.containsKey(request.userId)) {
							GiftCodeData data = new GiftCodeData(request.userId, serverId, request.gameId,
									item.getTypegiftcode(), item.getNamegiftcode(), request.code);
							putGiftCodeData(data);
							DatabaseAccount.insertGiftCodeData(data);
							return GiftCodeStatus.SUCCES;
						}

						Map<String, GiftCodeData> mapData = mapGiftCodeData.get(request.userId);
						switch (item.getTypegiftcode()) {
						case 1: // GiftCode chung
							if (!mapData.containsKey(request.code)) {
								GiftCodeData data = new GiftCodeData(request.userId, serverId, request.gameId,
										item.getTypegiftcode(), item.getNamegiftcode(), request.code);
								putGiftCodeData(data);
								DatabaseAccount.insertGiftCodeData(data);
								return GiftCodeStatus.SUCCES;
							} else {
								return GiftCodeStatus.USED;
							}
						case 2: // GiftCode riêng, k giới hạn
							if (!mapData.containsKey(request.code)) {
								GiftCodeData data = new GiftCodeData(request.userId, serverId, request.gameId,
										item.getTypegiftcode(), item.getNamegiftcode(), request.code);
								putGiftCodeData(data);
								DatabaseAccount.insertGiftCodeData(data);
								return GiftCodeStatus.SUCCES;
							} else {
								return GiftCodeStatus.USED;
							}
						case 3: // GiftCode riêng, giới hạn
							if (isExitNameGiftCode(mapData, item.getNamegiftcode())) {
								return GiftCodeStatus.USED_EVENT;
							}

							GiftCodeData data = new GiftCodeData(request.userId, serverId, request.gameId,
									item.getTypegiftcode(), item.getNamegiftcode(), request.code);
							putGiftCodeData(data);
							DatabaseAccount.insertGiftCodeData(data);
							return GiftCodeStatus.SUCCES;

						default:
							break;
						}

						status = GiftCodeStatus.SUCCES;
						return status;

					}
				}
			}
		}

		return status;
	}

	public boolean isExitNameGiftCode(Map<String, GiftCodeData> map, String nameGiftCode) {
		for (String key : map.keySet()) {
			GiftCodeData item = map.get(key);
			if (item != null) {
				if (item.namegiftcode.equals(nameGiftCode))
					return true;
			}
		}

		return false;
	}

	public void Test() {

	}

	public GiftCodeInfoBase getInfoGiftCode(String code) {
		GiftCodeInfoBase info = null;
		for (String key : GameManager.getInstance().mapGiftCode.keySet()) {
			GiftCodeInfoBase item = GameManager.getInstance().mapGiftCode.get(key);
			if (item != null) {
				for (int i = 0; i < item.getLstGiftCode().size(); i++) {
					GiftCodeItem item2 = item.getLstGiftCode().get(i);
					if (item2.getGift().equals(code)) {
						info = item;
					}
				}
			}
		}

		return info;
	}

	public Map<Long, ServerPlayerData> getMapServerPlayer(String username) {
		return this.mapServerPlayerData.get(username);
	}

	public Map<Long, ServerPlayerData> getMapServerPlayer2(String gameId, String username) {
		Map<Long, ServerPlayerData> mapServerPlayer = new HashMap<>();
		Map<Long, ServerPlayerData> mapServerPlayerFinish = new HashMap<>();
		if (mapServerPlayerData.containsKey(username)) {
			mapServerPlayer = this.mapServerPlayerData.get(username);
			if (mapServerPlayer != null) {
				for (Long key : mapServerPlayer.keySet()) {
					ServerPlayerData item = mapServerPlayer.get(key);
					if (item != null) {
						if (item.getGame_id().equals(gameId)) {
							mapServerPlayerFinish.put(key, item);
						}
					}
				}
			}
		}

		return mapServerPlayerFinish;
	}

	public Map<Long, ServerPlayerData> getMapServerPlayer2CheckIP(String gameId, String username) {
		Map<Long, ServerPlayerData> mapServerPlayer = new HashMap<>();
		Map<Long, ServerPlayerData> mapServerPlayerFinish = new HashMap<>();
		if (mapServerPlayerData.containsKey(username)) {
			mapServerPlayer = this.mapServerPlayerData.get(username);
			if (mapServerPlayer != null) {
				for (Long key : mapServerPlayer.keySet()) {
					ServerPlayerData item = mapServerPlayer.get(key);
					if (item != null) {
						if (item.getGame_id().equals(gameId)) {
							ServerInfoBase itemBase = getServerInfoBase(item.getGame_id(), item.getServer_id());
							if (itemBase != null) {
								if (itemBase.getLoginIp() == T.ZERO) {
									mapServerPlayerFinish.put(key, item);
								}
							} else {
								mapServerPlayerFinish.put(key, item);
							}
						}
					}
				}
			}
		}

		return mapServerPlayerFinish;
	}

	public void putServerPlayerData(ServerPlayerData data, boolean insertDb) {
		if (this.mapServerPlayerData.containsKey(data.getUsername())) {
			// Đã có userName này rồi
			Map<Long, ServerPlayerData> map = mapServerPlayerData.get(data.getUsername());
			if (!map.containsKey(data.getUser_id_game())) {
				map.put(data.getUser_id_game(), data);
				if (insertDb) {
					DatabaseAccount.insertServerPlayerData(data);
				}
			} else {
				if (insertDb) {
					DatabaseAccount.updateServerPlayerData(data);
				}
			}
		} else {
			// Chưa tồn tại userName trên hệ thống
			Map<Long, ServerPlayerData> map = new HashMap<>();
			map.put(data.getUser_id_game(), data);
			this.mapServerPlayerData.put(data.getUsername(), map);
			if (insertDb) {
				DatabaseAccount.insertServerPlayerData(data);
			}
		}
	}

	public ServerPlayerData GetDataPlayer(String userName, String keyhash) {
		Map<Long, ServerPlayerData> dataPlayer = getMapServerPlayer(userName);
		for (ServerPlayerData player : dataPlayer.values()) {
			if (player.getKeyhash().equals(keyhash)) {
				return player;
			}

		}
		return null;
	}

	public ServerPlayerData GetDataPlayerInServerId(String userName, String serverId) {
		Map<Long, ServerPlayerData> dataPlayer = getMapServerPlayer(userName);
		for (ServerPlayerData player : dataPlayer.values()) {
			if (player.getServer_id().equals(serverId)) {
				return player;
			}

		}
		return null;
	}

	public Map<String, LockAccountRequest> mapLockAccount = new HashMap<>();
	public Map<String, String> mapEmail = new HashMap<>();
	public Map<Integer, LinkConfigInfo> mapLinkConfig = new HashMap<>();
	public List<String> listIpLogin = new ArrayList<String>();

	public boolean checkIpLogin(String ip) {
		for (int i = 0; i < listIpLogin.size(); i++) {
			if (listIpLogin.get(i).equals(ip)) {
				return true;
			}
		}

		return false;
	}

	public LinkConfigInfo getConfigInfo(int gameId) {
		if (mapLinkConfig.containsKey(gameId)) {
			return mapLinkConfig.get(gameId);
		}

		return null;
	}

	public static GameManager getInstance() {
		return instance;
	}

	public List<User> getPlayerWaitIntoGame(int gameId) {
		List<User> list = new ArrayList<User>();
		return list;
	}

	public static DecimalFormat formatmoney = new DecimalFormat("###,###");
	public static DecimalFormat formatmoneyK = new DecimalFormat("###,###K");
	public static DecimalFormat formatmoneyM = new DecimalFormat("###,###M");

	public static String formatMoney(long money) {
		if (money < 1000000000 && money >= 0) {
			return formatmoney.format(money);
		}
		// else if (money >= 1000000 && money < 100000000) {
		// return formatmoneyK.format(money / 1000);
		// }
		else if (money >= 1000000000) {
			return formatmoneyM.format(money / 1000000);
		}
		return T.Empty;
	}

	public List<Long> sortDateKey(List<String> dateKeys, String format) {
		List<Long> dateKeysSorted = new ArrayList<Long>();
		SimpleDateFormat dateFormatter = new SimpleDateFormat(format);
		for (int i = 0; i < dateKeys.size(); i++) {
			Date date;
			try {
				date = dateFormatter.parse(dateKeys.get(i));
				dateKeysSorted.add(date.getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}

		}
		dateKeysSorted.sort(null);
		return dateKeysSorted;
	}

	public String convertToStr(long money) {
		StringBuilder strMoney = new StringBuilder();
		String oldstr = money + "";
		int count = 1;
		for (int i = oldstr.length() - 1; i >= 0; i--) {
			strMoney.insert(0, oldstr.charAt(i));
			if (count % 3 == 0 && i != 0) {
				strMoney.insert(0, ".");
			}
			count++;
		}
		return strMoney.toString();
	}

	public String convertListToString(List<Integer> list) {
		StringBuffer listIds = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			if (listIds.length() == 0) {
				listIds.append(list.get(i));
			} else {
				listIds.append(",");
				listIds.append(list.get(i));
			}
		}
		return listIds.toString();
	}

	public List<Integer> convertStringToList(String list) {
		List<Integer> vector = new ArrayList<>();
		if (list != null && list.length() > 0) {
			String[] arrIds = list.split(T.PhanTachPhay);
			if (arrIds != null && arrIds.length > 0) {
				for (int i = 0; i < arrIds.length; i++) {
					vector.add(Integer.parseInt(arrIds[i]));
				}
			}
		}
		return vector;
	}

	public List<String> convertStringToListString(String list) {
		List<String> lstString = new ArrayList<>();
		if (list != null && list.length() > 0) {
			String[] arrIds = list.split(T.PhanTachPhay);
			if (arrIds != null && arrIds.length > 0) {
				for (int i = 0; i < arrIds.length; i++) {
					lstString.add(arrIds[i]);
				}
			}
		}
		return lstString;
	}

	/**
	 * 
	 * @param array
	 * @return 0,1;1,1;2,1;....
	 */
	public String convertArrayToString(int[] array) {
		StringBuffer listPosition = new StringBuffer();
		for (int i = 0; i < array.length; i++) {
			if (listPosition.length() == 0) {
				listPosition.append(i);
				listPosition.append(T.PhanTachPhay);
				listPosition.append(array[i]);
			} else {
				listPosition.append(";");
				listPosition.append(i);
				listPosition.append(T.PhanTachPhay);
				listPosition.append(array[i]);
			}
		}
		return listPosition.toString();
	}

	/**
	 * 
	 * @param list  0,1;1,1;2,1;....
	 * @param array
	 */
	public void convertStringToArray(String list, int[] array) {
		if (list != null && list.length() > 0) {
			String[] arrStr = list.split(";");
			if (arrStr != null) {
				for (int i = 0; i < arrStr.length; i++) {
					String[] arrIdPostion = arrStr[i].split(T.PhanTachPhay);
					if (arrIdPostion != null && arrIdPostion.length > 1) {
						try {
							int index = Integer.parseInt(arrIdPostion[0]);
							int id = Integer.parseInt(arrIdPostion[1]);
							array[index] = id;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	public String convertArray2StringToString(String[][] array) {
		StringBuffer list = new StringBuffer();
		if (array != null)
			for (int i = 0; i < array.length; i++) {
				for (int j = 0; j < array[i].length; j++) {
					if (j == 0) {
						list.append(array[i][j] == null ? " " : array[i][j]);
					} else {
						list.append(",");
						list.append(array[i][j] == null ? " " : array[i][j]);
					}
				}
				if (i + 1 < array.length)
					list.append(";");
			}
		return list.toString();
	}

	public void convertStringToArray2String(String list, String[][] array) {
		if (list != null && list.length() > 0) {
			String[] arrStr = list.split(";");
			if (arrStr != null) {
				for (int i = 0; i < arrStr.length; i++) {
					String[] arrId = arrStr[i].split(T.PhanTachPhay);
					if (arrId != null) {
						for (int j = 0; j < arrId.length; j++) {
							array[i][j] = arrId[j];
						}
					}
				}
			}
		}
	}

	public String[][] cloneArray2String(String[][] srcArray) {
		String[][] destArray = new String[srcArray.length][srcArray[0].length];
		for (int i = 0; i < srcArray.length; i++) {
			for (int j = 0; j < srcArray[i].length; j++) {
				destArray[i][j] = srcArray[i][j] == null ? "" : srcArray[i][j];
			}
		}
		return destArray;
	}

	public Map<Long, String> mapTokenFCM = new HashMap<>();
	// Key: Id
	public Map<Integer, UsersInformationSettingData> mapUsersInformationSettingData = new HashMap<>();
	public Map<String, UsersInformationSettingData> mapUsersInformationSettingDataMain = new HashMap<>();

	/*
	 * Key: imei
	 */
	public Map<String, ImeiAddressData> mapImeiAddressData = new HashMap<>();

	public Boolean checkImei(String imei) {
		for (ImeiAddressData data : mapImeiAddressData.values()) {
			if (data.getImei().equals(imei)) {
				return true;
			}
		}

		return false;
	}

	public ImeiAddressData getImeiAddressData(String imei) {
		if (mapImeiAddressData.containsKey(imei)) {
			return mapImeiAddressData.get(imei);
		}

		return null;
	}

	public void putImeiAddressData(String imei, int platform, int providerId, String deviceName) {
		if (mapImeiAddressData.containsKey(imei)) {
			ImeiAddressData data = mapImeiAddressData.get(imei);
			int quantityNext = data.addQuantity();
			DatabaseAccount.updateUsersImei(imei, quantityNext);
		} else {
			ImeiAddressData imeiNew = new ImeiAddressData(imei, 1, platform, providerId, deviceName);
			mapImeiAddressData.put(imei, imeiNew);
			DatabaseAccount.insertUsersImei(imei, platform, providerId, deviceName);
		}
	}

	/*
	 * Key: ip
	 */
	public Map<String, IpAddressData> mapIpAddressData = new HashMap<>();

	public IpAddressData getIpAddressData(String ip) {
		if (mapIpAddressData.containsKey(ip)) {
			return mapIpAddressData.get(ip);
		}

		return null;
	}

	public void putIpAddressData(String ip, int platform, int providerId, String deviceName) {
		if (mapIpAddressData.containsKey(ip)) {
			IpAddressData data = mapIpAddressData.get(ip);
			int quantityNext = data.addQuantity();
			DatabaseAccount.updateUsersIp(ip, quantityNext);
		} else {
			IpAddressData imeiNew = new IpAddressData(ip, 1, platform, providerId, deviceName);
			mapIpAddressData.put(ip, imeiNew);
			DatabaseAccount.insertUsersIp(ip, platform, providerId, deviceName);
		}
	}

	/*
	 * Dữ liệu text trong game
	 */
	public Map<Integer, TextInfo> mapTextClientVn = new HashMap<>();
	public Map<Integer, TextInfo> mapTextClientEn = new HashMap<>();

	public Map<Integer, TextInfo> mapTextServerVn = new HashMap<>();
	public Map<Integer, TextInfo> mapTextServerEn = new HashMap<>();

	public String getText(int language, int key) {
		if (language == IDLanguage.VIETNAMESE) {
			if (mapTextServerVn.containsKey(key)) {
				return mapTextServerVn.get(key).getText();
			}
		} else {
			if (mapTextServerEn.containsKey(key)) {
				return mapTextServerEn.get(key).getText();
			}
		}

		return "";
	}

	public static void load() {
		DatabaseAccount.loadServerInfo();
		DatabaseAccount.loadCountryInfo();
		DatabaseAccount.loadServerPlayerData();
		DatabaseAccount.loadGiftCode();
		DatabaseAccount.loadLinksConfig();
		DatabaseAccount.loadIpLogin();
		DatabaseAccount.loadEmail();
		DatabaseAccount.loadUsersLockData();
		DatabaseAccount.loadGiftCodeData();
		DatabaseAccount.loadTokenFCMData();
		DatabaseAccount.loadUsersInformationSetting();
		DatabaseAccount.loadUsersInformationSettingMain();
		DatabaseAccount.loadUsersIp();
		DatabaseAccount.loadUsersImei();
		DatabaseAccount.loadTextClientInfo();
		DatabaseAccount.loadTextServerInfo();
		DatabaseAccount.loadInfoAccount();
	}

	public static void reLoad() {
		DatabaseAccount.loadServerInfo();
		DatabaseAccount.loadCountryInfo();
		DatabaseAccount.loadServerPlayerData();
		DatabaseAccount.loadGiftCode();
		DatabaseAccount.loadLinksConfig();
		DatabaseAccount.loadIpLogin();
		DatabaseAccount.loadEmail();
		DatabaseAccount.loadUsersLockData();
		DatabaseAccount.loadGiftCodeData();
		DatabaseAccount.loadTokenFCMData();
		DatabaseAccount.loadUsersInformationSetting();
		DatabaseAccount.loadTextClientInfo();
		DatabaseAccount.loadTextServerInfo();
	}

}