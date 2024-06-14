package com.sgc.game.server;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.http.entity.StringEntity;

import com.ggame.accountservice.AgentGame;
import com.ggame.database.DatabaseAccount;
import com.ggame.entity.LoginType;
import com.ggame.entity.player.User;
import com.ggame.network.ISession;
import com.ggame.services.RegisterResponse;
import com.google.gson.Gson;
import com.sgc.game.network.websocket.Http;

import config.Config;

public class PlayerManager {
	private Map<String, User> mapPlayerSession = new HashMap<>();
	private static PlayerManager instance = new PlayerManager();
	private Map<String, User> hashByName = new HashMap<>();
	private Map<Long, User> hashByUserID = new HashMap<>();
	public Map<Long, User> usersOutCached = new HashMap<>();
	public Map<Long, User> usersCachedAll = new HashMap<>();
	public List<User> vtUsers = Collections.synchronizedList(new ArrayList<User>());
	public List<User> vtUsersOnlines = Collections.synchronizedList(new ArrayList<User>());
	public Map<String, Integer> phoneCheckActive = new HashMap<>();
	public int providerDefault = 1;
	public int timeDelayCacheUser = 300000;
	public int timeDelayCacheUserOut = 1800000;
	private long maxUserID = 0;
	public List<String> mapImeiAddMoneyActice = Collections.synchronizedList(new ArrayList<String>());
	public Map<String, Vector<String>> ipCheckDaily = new HashMap<>();

	public Map<String, Timestamp> mapGetPromotionByDay = new HashMap<>();

	public PlayerManager() {
	}

	public Map<String, User> getMapUsersSession() {
		return mapPlayerSession;
	}

	public User getPlayerByWSSession(String sessionWS) {
		return mapPlayerSession.get(sessionWS);
	}

	/*
	 * public void setMapPlayerSession(Map<String, Player> mapPlayerSession) {
	 * this.mapPlayerSession = mapPlayerSession; }
	 */

	public static PlayerManager getInstance() {
		return instance;
	}

	public long getMaxUserID() {
		return maxUserID;
	}

	public void setMaxUserID(long maxUserID) {
		this.maxUserID = maxUserID;
	}

	public void addUserID() {
		maxUserID++;
	}

	public User checkLogin(ISession conn, User users) {
		try {
			User user = getUser(users.getUserName());
			if (user != null) {
				doLoginExits(conn, user, users.getUserName());
			} else {
				user = usersOutCached.remove(users.getUserId());
				if (user == null) {
					user = usersCachedAll.get(users.getUserId());
				}
				if (user == null) {
					user = DatabaseAccount.insertUser(conn, users);
					if (user != null)
						putUserCached(user, true);
				}
			}

			if (user == null)
				return null;
			user = putUser(user);
			conn.setUsers(user);
			user.setSession(conn);
			conn.setSessionId(user.getUserId());
			return user;

		} catch (Exception e) {
			LogsManager.logToFile(e);
			return null;
		}
	}

	public void CreateAccount(User userAdd) {
		DatabaseAccount.insertUser(userAdd);
		PlayerManager.getInstance().putUserCached(userAdd, true);

		try {
			// gửi dữ liệu qua cổng Web
			RegisterResponse registerResponse = new RegisterResponse();
			registerResponse.username = userAdd.getUserName();
			registerResponse.password = userAdd.getPassWord();
			registerResponse.email = userAdd.getEmail();
			registerResponse.gameId = userAdd.getGameId();
			registerResponse.ime = userAdd.getIme();
			registerResponse.ip = userAdd.getIp();
			registerResponse.platform = userAdd.getPlatform();
			registerResponse.logintype = userAdd.getType();
			Gson gsonResponse = new Gson();
			StringEntity postingString = new StringEntity(gsonResponse.toJson(registerResponse), "UTF-8");
			Http.sendPostJson(Config.urlPostRegisterUserToWeb, postingString);

		} catch (Exception e) {
			LogsManager.logToFile(e);
		}
	}

	public void doLoginExits(ISession conn, User player, String nick) {
		try {
			player.getSession().shutDownNow();
			player.logOut();
			AgentGame.log("=shutDownNow===doLoginExits===" + player.getUserName());
		} catch (Exception e) {
			logToFile(e);
		}
	}

	private void logToFile(Exception e) {
		LogsManager.logToFile(e);
	}

	public void putUsersRe(User player) {
		User p = hashByName.get(player.getUserName());
		if (p != null) {
			hashByName.remove(p.getUserName());
			vtUsers.remove(p);
			hashByUserID.remove(p.getUserId());
		}
		hashByName.put(player.getUserName(), player);
		vtUsers.add(player);
		hashByUserID.put(player.getUserId(), player);
	}

	public User putUser(User user) {
		User p = hashByName.get(user.getUserName());
		if (p != null) {
			return p;
		} else {
			hashByName.put(user.getUserName(), user);
			vtUsers.add(user);
			hashByUserID.put(user.getUserId(), user);
			return user;
		}
	}

	public void putUserOutCached(User user) {
		usersOutCached.put(user.getUserId(), user);
	}

	public void putUserCached(User user, Boolean add) {
		usersCachedAll.put(user.getUserId(), user);
		if (add) {
			addUserID();
		}
	}

	public User getUser(String username) {
		return hashByName.get(username);
	}

	public User getUser(long user_id) {
		return hashByUserID.get(user_id);
	}

	public User getUsersFromCache(long userId) {
		return usersCachedAll.get(userId);
	}

	public User getUsersOnline(long userId) {
		for (User player : vtUsers) {
			if (player.getUserId() == userId)
				return player;
		}
		return null;
	}

	public boolean checkTonTaiUserName(String userName) {
		try {
			String convertInThuong = convertChuInThuong(userName);
			for (User user : usersCachedAll.values()) {
				String convertCheck = convertChuInThuong(user.getUserName());
				if (convertInThuong.equals(convertCheck))
					return true;
			}
			return false;
		} catch (Exception e) {
			LogsManager.logToFile(e);
			return true;
		}
	}

	public static String convertChuInThuong(String text) {
		if (text.length() > 0)
			return text.toLowerCase();

		return text;
	}

	public User getUsersByUserName(String userName) {
		String convertInThuong = convertChuInThuong(userName);
		for (User user : usersCachedAll.values()) {
			String convertInThuong2 = convertChuInThuong(user.getUserName());
			if (convertInThuong.equals(convertInThuong2))
				return user;
		}
		return null;
	}

//	public User getUsersByUserName(String userName) {
//		for (User user : usersCachedAll.values()) {
//			if (userName.equals(user.getUserName()))
//				return user;
//		}
//		return null;
//	}

	public User getUsersByIMEI(String imei) {
		for (User user : usersCachedAll.values()) {
			if (imei.equals(user.getIme()) && user.getType() == LoginType.PLAYNOW)
				return user;
		}
		return null;
	}

	public Boolean isExitPlayNow(String imei) {
		for (User user : usersCachedAll.values()) {
			if (imei.equals(user.getIme()) && user.getType() == LoginType.PLAYNOW)
				return true;
		}
		return false;
	}

	public Boolean exitsEmail(String email) {
		for (User user : usersCachedAll.values()) {
			if (email.equals(user.getEmail()))
				return true;
		}
		return false;
	}

	public Boolean checkInfoUsers(String username, String password) {
		for (User user : usersCachedAll.values()) {
			if (user.getUserName().equals(username)) {
				if (user.getPassWord().equals(password)) {
					return true;
				}
			}
		}

		return false;
	}

	public void removeUsers(String name) {
		User p = hashByName.remove(name);
		if (p != null) {
			vtUsers.remove(p);
			hashByUserID.remove(p.getUserId());
		}
	}

	public List<User> getUsersOnline() {
		return vtUsersOnlines;
	}

	public List<User> getListUsers() {
		return vtUsers;
	}

	public int checkPhoneActive(String phone) {
		return phoneCheckActive.get(phone) == null ? 0 : phoneCheckActive.get(phone);
	}

	public void putPhoneActive(String phone) {
		Integer numNick = phoneCheckActive.get(phone);
		if (numNick != null) {
			numNick += 1;
			phoneCheckActive.put(phone, numNick);
		} else {
			phoneCheckActive.put(phone, 1);
		}
	}

	public void putCheckIP(String ip, String userName) {
		if (ipCheckDaily.get(ip) != null) {
			ipCheckDaily.get(ip).add(userName);
		} else {
			Vector<String> userNames = new Vector<String>();
			userNames.add(userName);
			ipCheckDaily.put(ip, userNames);
		}
	}

	public void load() {
		DatabaseAccount.loadAllPlayers(usersCachedAll);
	}
}
