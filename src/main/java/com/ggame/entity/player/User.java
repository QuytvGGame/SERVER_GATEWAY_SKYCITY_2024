package com.ggame.entity.player;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONObject;

import com.galaxy.framework.util.StringUtil;
import com.ggame.accountservice.T;
import com.ggame.database.DatabaseAccount;
import com.ggame.network.ISession;
import com.ggame.network.Message;
import com.sgc.game.server.NetworkServer;

public class User implements Cloneable {
	private long userId; // id của
	private String username; // tên tài khoản
	private String password;
	private String username2 = "";
	private String password2 = "";
	private int platform;
	private int type; // 0: dk bang SMS. 1: facebook. 2: du khach(ime). 3:mail. 4: tk thuong
	private String regPhone;
	private String ime;
	private String ip;
	private int active;
	private int lockStatus;
	private Timestamp timeReg;
	public Timestamp logOutTime;
	private String keyHash = "";
	private String email;
	private String gameId;
	private int providerId;
	private int server_login_close;
	private int game_id_close;

	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		try {
			json.put("userId", userId);
			json.put("username", username);
			json.put("password", password);
			json.put("platform", platform);
			json.put("type", type);
			json.put("regPhone", regPhone);
			json.put("ime", ime);
			json.put("ip", ip);
			json.put("active", active);
			json.put("lockStatus", lockStatus);
			json.put("keyHash", keyHash);
			json.put("email", email);
			json.put("gameId", gameId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	public JSONObject toJsonLog() {
		JSONObject json = new JSONObject();
		try {
			json.put("userId", userId);
			json.put("username", username);
			json.put("password", password);
			json.put("username2", username2);
			json.put("password2", password2);
			json.put("platform", platform);
			json.put("type", type);
			json.put("regPhone", regPhone);
			json.put("ime", ime);
			json.put("ip", ip);
			json.put("active", active);
			json.put("lockStatus", lockStatus);
			json.put("keyHash", keyHash);
			json.put("email", email);
			json.put("gameId", gameId);
			json.put("providerId", providerId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	private ISession session;

	public String getLogOutTime() {
		SimpleDateFormat userTimeFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss a");
		return logOutTime == null ? "" : userTimeFormatter.format(logOutTime);
	}

	public void setLogOutTime(Timestamp logOutTime) {
		this.logOutTime = logOutTime;
	}

	public long getTimeLogout() {
		if (logOutTime != null)
			return logOutTime.getTime();
		else
			return T.ZERO_LONG;
	}

	public void sendMessage(Message msg) {
		if (getSession() != null)
			getSession().sendMessage(msg);
	}

	public String getKeyHash() {
		return keyHash;
	}

	public void setKeyHash(String keyHash) {
		this.keyHash = keyHash;
	}

	public ISession getSession() {
		return session;
	}

	public void setSession(ISession session) {
		this.session = session;
	}

	public void logOut() {
		try {
			SimpleDateFormat datetimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			setLogOutTime(DatabaseAccount.getDateTime());
		} catch (Exception e) {
			DatabaseAccount.logToFile(StringUtil.stackTrace(e), NetworkServer.PATH_EXCEPTION);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;

		if (userId == 0) {
			if (other.userId != 0)
				return false;
		} else if (userId != other.userId)
			return false;
		return true;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public String getTimeNow() {
		SimpleDateFormat datetimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return datetimeFormatter.format(Calendar.getInstance().getTime()) + " ";
	}

	public String getPromotionByDay() {
		String result = "";
		return result;
	}

	public void subPromotionByDay(int num_day) {

	}

	public void disconnect() {

	}

	public boolean isOnline() {
		if (getSession() != null)
			return getSession().isConnected();
		return false;
	}

	public String getPassWord() {
		return password;
	}

	public void setPassWord(String passWord) {
		this.password = passWord;
	}

	public int getPlatform() {
		return platform;
	}

	public void setPlatform(int platform) {
		this.platform = platform;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getRegPhone() {
		return regPhone;
	}

	public void setRegPhone(String regPhone) {
		this.regPhone = regPhone;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getActive() {
		return active;
	}

	public void setActive(int active) {
		this.active = active;
	}

	public int getLockStatus() {
		return lockStatus;
	}

	public void setLockStatus(int lockStatus) {
		this.lockStatus = lockStatus;
	}

	public Timestamp getTimeReg() {
		return timeReg;
	}

	public void setTimeReg(Timestamp timeReg) {
		this.timeReg = timeReg;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return username;
	}

	public void setUserName(String userName) {
		this.username = userName;
	}

	public String getUsername2() {
		return username2;
	}

	public void setUsername2(String username2) {
		this.username2 = username2;
	}

	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String gmail) {
		this.email = gmail;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public int getProviderId() {
		return providerId;
	}

	public void setProviderId(int providerId) {
		this.providerId = providerId;
	}

	public int getServer_login_close() {
		return server_login_close;
	}

	public void setServer_login_close(int server_login_close) {
		this.server_login_close = server_login_close;
	}

	public int getGame_id_close() {
		return game_id_close;
	}

	public void setGame_id_close(int game_id_close) {
		this.game_id_close = game_id_close;
	}

}
