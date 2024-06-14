package com.ggame.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.json.JSONArray;
import com.galaxy.framework.server.util.AppServer;
import com.galaxy.framework.sql.Database;
import com.galaxy.framework.util.StringUtil;
import com.ggame.accountservice.AgentGame;
import com.ggame.accountservice.T;
import com.ggame.entity.GiftCodeData;
import com.ggame.entity.GiftCodeInfoBase;
import com.ggame.entity.ServerInfoBase;
import com.ggame.entity.player.CountryInfo;
import com.ggame.entity.player.DataPlayerUpdateTest;
import com.ggame.entity.player.InfoAccount;
import com.ggame.entity.player.LinkConfigInfo;
import com.ggame.entity.player.ServerPlayerData;
import com.ggame.entity.player.TextInfo;
import com.ggame.entity.player.User;
import com.ggame.network.ISession;
import com.ggame.services.AddMoneyRequest;
import com.ggame.services.CheckLoginRequest;
import com.ggame.services.GiftItemThanhChien;
import com.ggame.services.ImeiAddressData;
import com.ggame.services.IpAddressData;
import com.ggame.services.LockAccountRequest;
import com.ggame.services.TypeGiftCode;
import com.ggame.services.UpdateInfoAccountRequest;
import com.ggame.services.UsersInformationSettingData;
import com.sgc.game.server.GameManager;
import com.sgc.game.server.LogsManager;
import com.sgc.game.server.NetworkServer;
import com.sgc.game.server.PlayerManager;

import config.GameId;

public class DatabaseAccount {

	public static void loadAllPlayers(Map<Long, User> mapUser) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = AppServer.getConnection(NetworkServer.DB_CONGGAME);
			String sSQL = "SELECT * FROM users";
			pstmt = conn.prepareStatement(sSQL);
			rs = pstmt.executeQuery();
			long maxUserID = 1;
			while (rs.next()) {
				User user = getUser(rs);
				mapUser.put(user.getUserId(), user);
				if (user.getUserId() > maxUserID) {
					maxUserID = user.getUserId();
				}
			}

			PlayerManager.getInstance().setMaxUserID(maxUserID);
			System.out.println("=============load xong player================ " + mapUser.size());
		} catch (Exception e) {
			logToFile(e);
		} finally {
			Database.closeObject(rs);
			Database.closeObject(pstmt);
			Database.closeObject(conn);
		}
	}

	private static User getUser(ResultSet rs) {
		User user = new User();
		try {
			user.setUserId(rs.getLong("user_id"));
			user.setUserName(rs.getString("username"));
			user.setPassWord(rs.getString("password"));
			user.setPlatform(rs.getInt("platform"));
			user.setProviderId(rs.getInt("providerId"));
			user.setType(rs.getInt("type"));
			user.setRegPhone(rs.getString("reg_phone"));
			user.setIme(rs.getString("ime"));
			user.setIp(rs.getString("ip"));
			user.setGameId(rs.getString("gameid"));
			user.setActive(rs.getInt("active"));
			user.setEmail(rs.getString("email"));
			user.setLockStatus(rs.getInt("lock_status"));
			user.setKeyHash(rs.getString("keyHash"));
			user.setTimeReg(rs.getTimestamp("time_created"));
			try {
//				user.setKey(rs.getString("token_firebase"));
				user.setUsername2(rs.getString("username2"));
				user.setPassword2(rs.getString("password2"));
			} catch (Exception e) {

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return user;
	}

	public static User insertUser(ISession session, User user) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = AppServer.getConnection(NetworkServer.DB_CONGGAME);
			String sSQL = "INSERT INTO users (user_id, username, password, platform, type, reg_phone, ime, ip, active, lock_status, keyHash, email, gameid) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sSQL);
			pstmt.setLong(1, user.getUserId());
			pstmt.setString(2, user.getUserName());
			pstmt.setString(3, user.getPassWord());
			pstmt.setInt(4, user.getPlatform());
			pstmt.setInt(5, user.getType());
			pstmt.setString(6, user.getRegPhone());
			pstmt.setString(7, user.getIme());
			pstmt.setString(8, user.getIp());
			pstmt.setInt(9, user.getActive());
			pstmt.setInt(10, user.getLockStatus());
			pstmt.setString(11, user.getKeyHash());
			pstmt.setString(12, user.getEmail());
			pstmt.setString(13, user.getGameId());
			int result = pstmt.executeUpdate();
			insertEmail(user.getEmail());
			System.out.println("-------finish insertUser DatabaseAccount User new Cache----- result = " + result
					+ " , JSON = " + user.toJson());
		} catch (Exception e) {
			logToFile(e);
			Database.closeObject(rs);
			Database.closeObject(pstmt);
			Database.closeObject(conn);
			return null;
		} finally {
			Database.closeObject(rs);
			Database.closeObject(pstmt);
			Database.closeObject(conn);
		}
		return user;
	}

	public static User insertUser(User user) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		User player = null;
		try {
			conn = AppServer.getConnection(NetworkServer.DB_CONGGAME);
			String sSQL = "INSERT INTO users (user_id, username, password, platform, type, reg_phone, ime, ip, active, lock_status, keyHash, email, gameid, providerId) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sSQL);
			pstmt.setLong(1, user.getUserId());
			pstmt.setString(2, user.getUserName());
			pstmt.setString(3, user.getPassWord());
			pstmt.setInt(4, user.getPlatform());
			pstmt.setInt(5, user.getType());
			pstmt.setString(6, user.getRegPhone());
			pstmt.setString(7, user.getIme());
			pstmt.setString(8, user.getIp());
			pstmt.setInt(9, user.getActive());
			pstmt.setInt(10, user.getLockStatus());
			pstmt.setString(11, user.getKeyHash());
			pstmt.setString(12, user.getEmail());
			pstmt.setString(13, user.getGameId());
			pstmt.setInt(14, user.getProviderId());
			pstmt.executeUpdate();
			insertEmail(user.getEmail());
			System.out.println("-------finish insertUser DatabaseAccount User new Cache-----" + user.toJson());
		} catch (Exception e) {
			logToFile(e);
			return null;
		} finally {
			Database.closeObject(rs);
			Database.closeObject(pstmt);
			Database.closeObject(conn);
		}
		return player;
	}

	public static void updateIpUser(String username, String ip, String gameIdStr) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = AppServer.getConnection(NetworkServer.DB_CONGGAME);
			String sSQL = "UPDATE users SET ip = ? where username = ?";
			pstmt = conn.prepareStatement(sSQL);
			pstmt.setString(1, ip);
			pstmt.setString(2, username);
			pstmt.executeUpdate();
			System.out
					.println("-------finish updateIpUser DatabaseAccount ---- username = " + username + ", ip = " + ip);

			try {
				int gameId = Integer.parseInt(gameIdStr);
			} catch (Exception e) {
				logToFile(e);
			}
		} catch (Exception e) {
			logToFile(e);
		} finally {
			Database.closeObject(rs);
			Database.closeObject(pstmt);
			Database.closeObject(conn);
		}
	}

	public static void updateAccountPlayNow(String username, String username2, String password2) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = AppServer.getConnection(NetworkServer.DB_CONGGAME);
			String sSQL = "UPDATE users SET username2 = ?, password2=? where username=?";
			pstmt = conn.prepareStatement(sSQL);
			pstmt.setString(1, username2);
			pstmt.setString(2, password2);
			pstmt.setString(3, username);
			pstmt.executeUpdate();
			System.out.println("-------finish updateAccountPlayNow DatabaseAccount ---- username = " + username
					+ ", username2 = " + username2 + ", password2 = " + password2);
		} catch (Exception e) {
			logToFile(e);
		} finally {
			Database.closeObject(rs);
			Database.closeObject(pstmt);
			Database.closeObject(conn);
		}
	}

	public static Timestamp getDateTime() {
		Timestamp datetime = new Timestamp(new java.util.Date().getTime());
		return datetime;
	}

	public static Map<String, HashMap<Integer, Integer[]>> getScratch_cards(
			Map<String, HashMap<Integer, Integer[]>> map) {
		return map;
	}

	public static int versionToInt(String v) {
		String str = "";
		for (int i = 0; i < v.length(); i++) {
			if (v.charAt(i) != '.') {
				str += v.charAt(i);
			}
		}
		int value = Integer.parseInt(str);
		if (value < 100)
			value *= 10;
		return value;
	}

	public static String versionToString(int t) {
		String end = "";
		String str = Integer.toString(t);
		for (int i = 0; i < str.length(); i++) {
			if (i == (str.length() - 1))
				end = end + str.charAt(i);
			else
				end = end + str.charAt(i) + ".";
		}
		return end;
	}

	/**
	 * 
	 * @param t1
	 * @param t2
	 * @return 1: t1 lon hon t2; 0: bang nhau; -1: t1 nho hon t2
	 * 
	 */
	public static int compareDate(Timestamp t1, Timestamp t2) {
		SimpleDateFormat userTimeFormatter = new SimpleDateFormat("dd-MM-yyyy");
		Date d1 = new Date(t1.getTime());
		Date d2 = new Date(t2.getTime());
		String[] strDate1 = userTimeFormatter.format(d1).split("-");
		String[] strDate2 = userTimeFormatter.format(d2).split("-");
		if (Integer.parseInt(strDate1[2]) > Integer.parseInt(strDate2[2])) {
			return 1;
		} else if (Integer.parseInt(strDate1[2]) == Integer.parseInt(strDate2[2])) {
			if (Integer.parseInt(strDate1[1]) > Integer.parseInt(strDate2[1])) {
				return 1;
			} else if (Integer.parseInt(strDate1[1]) == Integer.parseInt(strDate2[1])) {
				if (Integer.parseInt(strDate1[0]) > Integer.parseInt(strDate2[0])) {
					return 1;
				} else if (Integer.parseInt(strDate1[0]) == Integer.parseInt(strDate2[0])) {
					return 0;
				} else {
					return -1;
				}
			} else {
				return -1;
			}
		} else {
			return -1;
		}
	}

	public static void loadServerInfo() {
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		GameManager.getInstance().mapServerInfo.clear();
		GameManager.getInstance().mapServerInfoInGameId.clear();
		GameManager.getInstance().jsonArrayMapServerInfo = new JSONArray();
		try {
			conn = AppServer.getConnection(NetworkServer.DB_CONGGAME);
			String sSQL = "SELECT * FROM server_info";
			pstmt = conn.prepareStatement(sSQL);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ServerInfoBase item = new ServerInfoBase();
				item.setId(rs.getInt("id"));
				item.setGameId(rs.getString("game_id"));
				item.setServerId(rs.getString("server_id"));
				item.setLoginIp(rs.getInt("login_ip"));
				item.setServerName(rs.getString("server_name"));
				item.setServerIp(rs.getString("server_ip"));
				item.setServerPort(rs.getInt("server_port"));
				item.setStatus(rs.getInt("status"));
				item.setDateOpenServer(rs.getString("date_open_server"));
				GameManager.getInstance().mapServerInfo.put(item.getId(), item);
				GameManager.getInstance().putServerInfoBase(item);
				GameManager.getInstance().jsonArrayMapServerInfo.put(item.toJson());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logToFile(e);
		} finally {
			Database.closeObject(rs);
			Database.closeObject(pstmt);
			Database.closeObject(conn);
		}
	}

	public static void loadCountryInfo() {
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		GameManager.getInstance().mapCountryInfo.clear();
		GameManager.getInstance().mapCountryInfoInGameId.clear();
		try {
			conn = AppServer.getConnection(NetworkServer.DB_CONGGAME);
			String sSQL = "SELECT * FROM country_info";
			pstmt = conn.prepareStatement(sSQL);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				CountryInfo item = new CountryInfo();
				item.setGameId(rs.getString("game_id"));
				item.setIdLanguage(rs.getInt("idLanguage"));
				item.setIndex(rs.getInt("index"));
				item.setCode(rs.getString("code"));
				item.setName(rs.getString("name"));
				item.setNameCode(rs.getString("nameCode"));
				GameManager.getInstance().mapCountryInfo.put(item.getIdLanguage(), item);
				GameManager.getInstance().putCountryInfoBase(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logToFile(e);
		} finally {
			Database.closeObject(rs);
			Database.closeObject(pstmt);
			Database.closeObject(conn);
		}
	}

	public static void loadInfoAccount() {
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		GameManager.getInstance().mapInfoAccount.clear();
		try {
			conn = AppServer.getConnection(NetworkServer.DB_CONGGAME);
			String sSQL = "SELECT * FROM info_account";
			pstmt = conn.prepareStatement(sSQL);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String username = rs.getString("username");
				String hoten = rs.getString("hoten");
				int gioitinh = rs.getInt("gioitinh");
				String ngaysinh = rs.getString("ngaysinh");
				String diachi = rs.getString("diachi");
				String cccd = rs.getString("cccd");
				String ngaycap = rs.getString("ngaycap");
				String noicap = rs.getString("noicap");
				String sdt = rs.getString("sdt");
				InfoAccount info = new InfoAccount(username, hoten, gioitinh, ngaysinh, diachi, cccd, ngaycap, noicap,
						sdt);
				GameManager.getInstance().mapInfoAccount.put(username, info);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logToFile(e);
		} finally {
			Database.closeObject(rs);
			Database.closeObject(pstmt);
			Database.closeObject(conn);
		}
	}

	public static void insertInfoAccount(UpdateInfoAccountRequest info) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = AppServer.getConnection(NetworkServer.DB_CONGGAME);
			String sSQL = "INSERT INTO info_account (username, gameId, hoten, gioitinh, ngaysinh, diachi, cccd, ngaycap, noicap, sdt) values(?,?,?,?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sSQL);
			pstmt.setString(1, info.username);
			pstmt.setString(2, info.gameid);
			pstmt.setNString(3, info.hoten);
			pstmt.setInt(4, info.gioitinh);
			pstmt.setString(5, info.ngaysinh);
			pstmt.setNString(6, info.diachi);
			pstmt.setString(7, info.cccd);
			pstmt.setString(8, info.ngaycap);
			pstmt.setNString(9, info.noicap);
			pstmt.setString(10, info.sdt);
			pstmt.executeUpdate();
			System.out
					.println("-------finish insertInfoAccount DatabaseAccount INFO Account JSON -----" + info.toJson());
		} catch (Exception e) {
			logToFile(e);
		} finally {
			Database.closeObject(rs);
			Database.closeObject(pstmt);
			Database.closeObject(conn);
		}
	}

	public static int updateInfoAccount(UpdateInfoAccountRequest info) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			conn = AppServer.getConnection(NetworkServer.DB_CONGGAME);
			String sSQL = "update info_account SET gameId=?, hoten=?, gioitinh=?, ngaysinh=?, diachi=?, cccd=?, ngaycap=?, noicap=?, sdt=? WHERE username = ?";
			pstmt = conn.prepareStatement(sSQL);
			pstmt.setString(1, info.gameid);
			pstmt.setNString(2, info.hoten);
			pstmt.setInt(3, info.gioitinh);
			pstmt.setString(4, info.ngaysinh);
			pstmt.setNString(5, info.diachi);
			pstmt.setString(6, info.cccd);
			pstmt.setString(7, info.ngaycap);
			pstmt.setNString(8, info.noicap);
			pstmt.setString(9, info.sdt);
			pstmt.setString(10, info.username);
			return pstmt.executeUpdate();
		} catch (Exception e) {
			logToFile(e);
			return 0;
		} finally {
			Database.closeObject(pstmt);
			Database.closeObject(conn);
		}
	}

	public static void loadServerPlayerData() {
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		GameManager.getInstance().mapServerPlayerData.clear();
		try {
			conn = AppServer.getConnection(NetworkServer.DB_CONGGAME);
			String sSQL = "SELECT * FROM server_player_data";
			pstmt = conn.prepareStatement(sSQL);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ServerPlayerData data = new ServerPlayerData();
				data.setUser_id_game(rs.getLong("user_id_game"));
				data.setUsername(rs.getString("username"));
				data.setUser_id(rs.getLong("user_id"));
				data.setDisplay_name(rs.getNString("display_name"));
				data.setGame_id(rs.getString("game_id"));
				data.setServer_id(rs.getString("server_id"));
				data.setServer_id_current(rs.getString("server_id_current"));
				data.setServer_ip(rs.getString("server_ip"));
				data.setServer_port(rs.getInt("server_port"));
				data.setKeyhash(rs.getString("keyhash"));
				GameManager.getInstance().putServerPlayerData(data, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logToFile(e);
		} finally {
			Database.closeObject(rs);
			Database.closeObject(pstmt);
			Database.closeObject(conn);
		}
	}

	public static int updatePassword(long user_id, String passWord) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			conn = AppServer.getConnection(NetworkServer.DB_CONGGAME);
			String sSQL = "update users set password = ? where user_id = ?";
			pstmt = conn.prepareStatement(sSQL);
			pstmt.setNString(1, passWord);
			pstmt.setLong(2, user_id);
			return pstmt.executeUpdate();
		} catch (Exception e) {
			logToFile(e);
			return 0;
		} finally {
			Database.closeObject(pstmt);
			Database.closeObject(conn);
		}
	}

	public static int updateUserLogin(CheckLoginRequest login) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			conn = AppServer.getConnection(NetworkServer.DB_CONGGAME);
			String sSQL = "UPDATE users SET server_login_close = ?, game_id_close = ?, login_time = ? where username = ?";
			pstmt = conn.prepareStatement(sSQL);
			pstmt.setInt(1, login.getServerIdInt());
			pstmt.setInt(2, login.getGameIdInt());
			SimpleDateFormat datetimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String timeLogin = datetimeFormatter.format(Calendar.getInstance().getTime());
			pstmt.setString(3, timeLogin);
			pstmt.setString(4, login.username);
			return pstmt.executeUpdate();
		} catch (Exception e) {
			logToFile(e);
			return 0;
		} finally {
			Database.closeObject(pstmt);
			Database.closeObject(conn);
		}
	}

	public static int updateEmail(long user_id, String email) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			conn = AppServer.getConnection(NetworkServer.DB_CONGGAME);
			String sSQL = "update users set email = ? where user_id = ?";
			pstmt = conn.prepareStatement(sSQL);
			pstmt.setNString(1, email);
			pstmt.setLong(2, user_id);
			return pstmt.executeUpdate();
		} catch (Exception e) {
			logToFile(e);
			return 0;
		} finally {
			Database.closeObject(pstmt);
			Database.closeObject(conn);
		}
	}

	public static int updateDisPlayerNameInPlayerData(String display_name, long user_id_game) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			conn = AppServer.getConnection(NetworkServer.DB_CONGGAME);
			String sSQL = "update server_player_data set display_name = ? where user_id_game = ?";
			pstmt = conn.prepareStatement(sSQL);
			pstmt.setNString(1, display_name);
			pstmt.setLong(2, user_id_game);
			return pstmt.executeUpdate();
		} catch (Exception e) {
			logToFile(e);
			return 0;
		} finally {
			Database.closeObject(pstmt);
			Database.closeObject(conn);
		}
	}

	public static void insertServerPlayerData(ServerPlayerData player) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = AppServer.getConnection(NetworkServer.DB_CONGGAME);
			String sSQL = "INSERT INTO server_player_data (user_id_game, username, user_id, display_name, game_id, server_id, server_id_current, server_ip,"
					+ " server_port, keyhash) values(?,?,?,?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sSQL);
			pstmt.setLong(1, player.getUser_id_game());
			pstmt.setString(2, player.getUsername());
			pstmt.setLong(3, player.getUser_id());
			pstmt.setNString(4, player.getDisplay_name());
			pstmt.setString(5, player.getGame_id());
			pstmt.setString(6, player.getServer_id());
			pstmt.setString(7, player.getServer_id_current());
			pstmt.setString(8, player.getServer_ip());
			pstmt.setInt(9, player.getServer_port());
			pstmt.setString(10, player.getKeyhash());
			pstmt.executeUpdate();
			System.out.println(
					"-------finish insertServerPlayerData DatabaseAccount Player new Cache-----" + player.toJson());
		} catch (Exception e) {
			logToFile(e);
		} finally {
			Database.closeObject(rs);
			Database.closeObject(pstmt);
			Database.closeObject(conn);
		}
	}

	public static void updateServerIpPlayerData(long userIdGame, String ip) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = AppServer.getConnection(NetworkServer.DB_CONGGAME);
			String sSQL = "UPDATE server_player_data SET server_ip=? where user_id_game=?";
			pstmt = conn.prepareStatement(sSQL);
			pstmt.setString(1, ip);
			pstmt.setLong(2, userIdGame);
			pstmt.executeUpdate();
			System.out.println("-------finish updateServerIpPlayerData DatabaseAccount userIdGame =" + userIdGame
					+ ", ip = " + ip);
		} catch (Exception e) {
			logToFile(e);
		} finally {
			Database.closeObject(rs);
			Database.closeObject(pstmt);
			Database.closeObject(conn);
		}
	}

	public static void updateServerPlayerData(ServerPlayerData player) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = AppServer.getConnection(NetworkServer.DB_CONGGAME);
			String sSQL = "UPDATE server_player_data SET username=?, user_id=?, display_name=?, game_id=?, server_id=?, server_id_current=?, server_ip=?,"
					+ " server_port=?, keyhash=? where user_id_game=?";
			pstmt = conn.prepareStatement(sSQL);
			pstmt.setString(1, player.getUsername());
			pstmt.setLong(2, player.getUser_id());
			pstmt.setNString(3, player.getDisplay_name());
			pstmt.setString(4, player.getGame_id());
			pstmt.setString(5, player.getServer_id());
			pstmt.setString(6, player.getServer_id_current());
			pstmt.setString(7, player.getServer_ip());
			pstmt.setInt(8, player.getServer_port());
			pstmt.setString(9, player.getKeyhash());
			pstmt.setLong(10, player.getUser_id_game());
			pstmt.executeUpdate();
			System.out.println(
					"-------finish updateServerPlayerData DatabaseAccount Player new Cache-----" + player.toJson());
		} catch (Exception e) {
			logToFile(e);
		} finally {
			Database.closeObject(rs);
			Database.closeObject(pstmt);
			Database.closeObject(conn);
		}
	}

	public static void loadGiftCode() {
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		GameManager.getInstance().mapGiftCode.clear();
		try {
			conn = AppServer.getConnection(NetworkServer.DB_CONGGAME);
			String sSQL = "SELECT * FROM giftcode";
			pstmt = conn.prepareStatement(sSQL);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String data = rs.getString("data");
				GiftCodeInfoBase tbData = new GiftCodeInfoBase(data);
				int isDelete = rs.getInt("isDelete");
				if (isDelete != T.TRUE) {
					GameManager.getInstance().mapGiftCode.put(tbData.getNamegiftcode(), tbData);
				}
//				System.out.println("-------DEBUG = " + tbData.getNamegiftcode() + ", JSON = " + tbData.toJson());
			}

		} catch (Exception e) {
			e.printStackTrace();
			logToFile(e);
		} finally {
			Database.closeObject(rs);
			Database.closeObject(pstmt);
			Database.closeObject(conn);
		}
	}

	public static void insertGiftCode(GiftCodeInfoBase giftcode) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = AppServer.getConnection(NetworkServer.DB_CONGGAME);
			String sSQL = "INSERT INTO giftcode (namegiftcode, data, timeopen, timeclose) values(?,?,?,?)";
			pstmt = conn.prepareStatement(sSQL);
			pstmt.setString(1, giftcode.getNamegiftcode());
			pstmt.setString(2, giftcode.toJsonSave().toString());
			pstmt.setString(3, giftcode.getTimeopen());
			pstmt.setString(4, giftcode.getTimeclose());
			pstmt.executeUpdate();
			System.out.println(
					"-------finish insertGiftCode DatabaseAccount ---- namegiftcode = " + giftcode.getNamegiftcode());
		} catch (Exception e) {
			logToFile(e);
		} finally {
			Database.closeObject(rs);
			Database.closeObject(pstmt);
			Database.closeObject(conn);
		}
	}

	public static void updateGiftCode(GiftCodeInfoBase giftcode) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = AppServer.getConnection(NetworkServer.DB_CONGGAME);
			String sSQL = "update giftcode set data = ?, timeopen = ?, timeclose = ? where namegiftcode = ?";
			pstmt = conn.prepareStatement(sSQL);
			pstmt.setString(1, giftcode.toJsonSave().toString());
			pstmt.setString(2, giftcode.getTimeopen());
			pstmt.setString(3, giftcode.getTimeclose());
			pstmt.setString(4, giftcode.getNamegiftcode());
			pstmt.executeUpdate();
			System.out.println(
					"-------finish updateGiftCode DatabaseAccount ---- namegiftcode = " + giftcode.getNamegiftcode());
		} catch (Exception e) {
			logToFile(e);
		} finally {
			Database.closeObject(rs);
			Database.closeObject(pstmt);
			Database.closeObject(conn);
		}
	}

	public static void deleteGiftCode(String namegiftcode) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = AppServer.getConnection(NetworkServer.DB_CONGGAME);
			String sSQL = "update giftcode set isDelete = ? where namegiftcode = ?";
			pstmt = conn.prepareStatement(sSQL);
			pstmt.setInt(1, 1);
			pstmt.setString(2, namegiftcode);
			pstmt.executeUpdate();
			System.out.println("-------finish deleteGiftCode DatabaseAccount ---- namegiftcode = " + namegiftcode);
		} catch (Exception e) {
			logToFile(e);
		} finally {
			Database.closeObject(rs);
			Database.closeObject(pstmt);
			Database.closeObject(conn);
		}
	}


	public static void updateThongBaoConfig(int gameId, String thongBao) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = AppServer.getConnection(NetworkServer.DB_CONGGAME);
			String sSQL = "update links_config set thongBao = ? where gameId =?";
			pstmt = conn.prepareStatement(sSQL);
			pstmt.setNString(1, thongBao);
			pstmt.setInt(2, gameId);
			pstmt.executeUpdate();
			System.out.println("-------finish updateThongBaoConfig DatabaseAccount ---- thongBaoNew = " + thongBao
					+ ", gameId = " + gameId);
		} catch (Exception e) {
			logToFile(e);
		} finally {
			Database.closeObject(rs);
			Database.closeObject(pstmt);
			Database.closeObject(conn);
		}
	}

	public static void loadLinksConfig() {
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		GameManager.getInstance().mapLinkConfig.clear();
		try {
			conn = AppServer.getConnection(NetworkServer.DB_CONGGAME);
			String sSQL = "SELECT * FROM links_config";
			pstmt = conn.prepareStatement(sSQL);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				LinkConfigInfo info = new LinkConfigInfo();
				info.setGameId(rs.getInt("gameId"));
				info.setLinkImgBanner(rs.getString("linkImgBanner"));
				info.setLinkBanner(rs.getString("linkBanner"));
				info.setLinkFanpage(rs.getString("linkFanpage"));
				info.setLinkGroup(rs.getString("linkGroup"));
				info.setHotline(rs.getString("hotline"));
				info.setThongBao(rs.getNString("thongBao"));
				info.setThongBaoIOS(rs.getNString("thongBaoIOS"));
				info.setLinkWebGame(rs.getString("linkWebGame"));
				info.setEmail(rs.getString("email"));
				info.setLinkEvent(rs.getString("linkEvent"));
				info.setLinkUpdateThongTinCaNhan(rs.getString("linkUpdateThongTinCaNhan"));
				info.setLinkDoiMatKhau(rs.getString("linkDoiMatKhau"));
				info.setLoginIp(rs.getInt("loginIp"));
				info.setLoginIpGame(rs.getInt("loginIpGame"));
				info.setThongBaoUpdate(rs.getString("thongBaoUpdate"));
				info.setLinkVote(rs.getString("linkVote"));
				info.setShowIcon(rs.getInt("showIcon"));
				info.setIsOnUpdateThongTin(rs.getInt("isOnUpdateThongTin"));
				info.setIsRegisterGmail(rs.getInt("isRegisterGmail"));
				info.setLinkDksd(rs.getString("linkDksd"));
				info.setThoitiet(rs.getInt("thoitiet"));
				info.setIsApple(rs.getInt("isApple"));
				info.setLinksCheckInternet(rs.getString("linksCheckInternet"));
				GameManager.getInstance().mapLinkConfig.put(info.getGameId(), info);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logToFile(e);
		} finally {
			Database.closeObject(rs);
			Database.closeObject(pstmt);
			Database.closeObject(conn);
		}
	}

	public static void loadIpLogin() {
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		GameManager.getInstance().listIpLogin.clear();
		try {
			conn = AppServer.getConnection(NetworkServer.DB_CONGGAME);
			String sSQL = "SELECT * FROM ip_login";
			pstmt = conn.prepareStatement(sSQL);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String ip = rs.getString("ip");
				GameManager.getInstance().listIpLogin.add(ip);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logToFile(e);
		} finally {
			Database.closeObject(rs);
			Database.closeObject(pstmt);
			Database.closeObject(conn);
		}
	}
	
	public static void loadEmail() {
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		try {
			GameManager.getInstance().mapEmail.clear();
			conn = AppServer.getConnection(NetworkServer.DB_CONGGAME);
			String sSQL = "SELECT * FROM email";
			pstmt = conn.prepareStatement(sSQL);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				String email = rs.getNString("email");
				GameManager.getInstance().mapEmail.put(email, email);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logToFile(e);
		} finally {
			Database.closeObject(rs);
			Database.closeObject(pstmt);
			Database.closeObject(conn);
		}
	}

	public static void insertEmail(String email) {
		if (email.length() > 3) {
			PreparedStatement pstmt = null;
			Connection conn = null;
			ResultSet rs = null;
			try {
				conn = AppServer.getConnection(NetworkServer.DB_CONGGAME);
				String sSQL = "INSERT INTO email (email) values(?)";
				pstmt = conn.prepareStatement(sSQL);
				pstmt.setNString(1, email);
				pstmt.executeUpdate();
				GameManager.getInstance().mapEmail.put(email, email);
			} catch (Exception e) {
				logToFile(e);
			} finally {
				Database.closeObject(rs);
				Database.closeObject(pstmt);
				Database.closeObject(conn);
			}
		}
	}

	public static void LoadAdminCommand(Vector<AdminCommand> vtAdminCmd) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		vtAdminCmd.clear();
		try {
			conn = AppServer.getConnection(NetworkServer.DB_CONGGAME);
			String sSql = "Select * from sg_admin where status='s'";
			pstmt = conn.prepareStatement(sSql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				AdminCommand adminCommand = new AdminCommand();
				adminCommand.command = rs.getString("command");
				adminCommand.status = rs.getString("status");
				vtAdminCmd.add(adminCommand);
			}
		} catch (Exception e) {
			logToFile(e);
		} finally {
			Database.closeObject(rs);
			Database.closeObject(pstmt);
			Database.closeObject(conn);
		}
	}

	public static void SetAdminCommand(AdminCommand AdminCmd) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = AppServer.getConnection(NetworkServer.DB_CONGGAME);
			String sSql = "update sg_admin set status = '' where command = '" + AdminCmd.command + "'";
			pstmt = conn.prepareStatement(sSql);
			pstmt.executeUpdate();
		} catch (Exception e) {
			logToFile(e);
		} finally {
			Database.closeObject(rs);
			Database.closeObject(pstmt);
			Database.closeObject(conn);
		}
	}

	public static void loadUsersLockData() {
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		GameManager.getInstance().mapLockAccount.clear();
		try {
			conn = AppServer.getConnection(NetworkServer.DB_CONGGAME);
			String sSQL = "SELECT * FROM users_lock_data";
			pstmt = conn.prepareStatement(sSQL);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				LockAccountRequest data = new LockAccountRequest();
				data.setUser_name(rs.getString("user_name"));
				data.setGame_id(rs.getString("game_id"));
				data.setNote(rs.getString("note"));
				data.setLocker(rs.getString("locker"));
				data.setStatus(rs.getInt("status"));
				GameManager.getInstance().mapLockAccount.put(data.getUser_name(), data);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logToFile(e);
		} finally {
			Database.closeObject(rs);
			Database.closeObject(pstmt);
			Database.closeObject(conn);
		}
	}

	public static void insertUsersLockData(LockAccountRequest data, String user_name) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = AppServer.getConnection(NetworkServer.DB_CONGGAME);
			String sSQL = "INSERT INTO users_lock_data (user_name, game_id, note, locker, status) values(?,?,?,?,?)";
			pstmt = conn.prepareStatement(sSQL);
			pstmt.setString(1, user_name);
			pstmt.setString(2, data.getGame_id());
			pstmt.setString(3, data.getNote());
			pstmt.setString(4, data.getLocker());
			pstmt.setInt(5, data.getStatus());
			pstmt.executeUpdate();
			System.out.println("-------finish insertUsersLockData DatabaseAccount ---- user_name = "
					+ data.getUser_name() + ", note = " + data.getNote());
		} catch (Exception e) {
			logToFile(e);
		} finally {
			Database.closeObject(rs);
			Database.closeObject(pstmt);
			Database.closeObject(conn);
		}
	}

	public static void updateUsersLockData(LockAccountRequest data, String user_name) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = AppServer.getConnection(NetworkServer.DB_CONGGAME);
			String sSQL = "update users_lock_data set locker = ?, note =? where user_name = ?";
			pstmt = conn.prepareStatement(sSQL);
			pstmt.setInt(1, data.getStatus());
			pstmt.setString(2, data.getNote());
			pstmt.setString(3, user_name);
			pstmt.executeUpdate();
			System.out.println("-------finish updateUsersLockData DatabaseAccount ---- user_name = "
					+ data.getUser_name() + ", note = " + data.getNote());
		} catch (Exception e) {
			logToFile(e);
		} finally {
			Database.closeObject(rs);
			Database.closeObject(pstmt);
			Database.closeObject(conn);
		}
	}

	public static void loadGiftCodeData() {
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		GameManager.getInstance().mapGiftCodeData.clear();
		GameManager.getInstance().listGiftCodeRiengData.clear();
		try {
			conn = AppServer.getConnection(NetworkServer.DB_CONGGAME);
			String sSQL = "SELECT * FROM giftcode_data";
			pstmt = conn.prepareStatement(sSQL);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				long user_id = rs.getLong("user_id");
				int server_id = rs.getInt("server_id");
				String game_id = rs.getString("game_id");
				int typegiftcode = rs.getInt("typegiftcode");
				String namegiftcode = rs.getNString("namegiftcode");
				String giftcode = rs.getString("giftcode");
				GiftCodeData data = new GiftCodeData(user_id, server_id, game_id, typegiftcode, namegiftcode, giftcode);
				GameManager.getInstance().putGiftCodeData(data);
				if (typegiftcode != TypeGiftCode.GIFT_CHUNG) {
					GameManager.getInstance().listGiftCodeRiengData.add(giftcode);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logToFile(e);
		} finally {
			Database.closeObject(rs);
			Database.closeObject(pstmt);
			Database.closeObject(conn);
		}
	}

	public static void insertGiftCodeData(GiftCodeData data) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = AppServer.getConnection(NetworkServer.DB_CONGGAME);
			String sSQL = "INSERT INTO giftcode_data (user_id, server_id, game_id, typegiftcode, namegiftcode, giftcode) values(?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sSQL);
			pstmt.setLong(1, data.getUser_id());
			pstmt.setInt(2, data.getServer_id());
			pstmt.setString(3, data.getGame_id());
			pstmt.setInt(4, data.getTypegiftcode());
			pstmt.setNString(5, data.getNamegiftcode());
			pstmt.setString(6, data.getGiftcode());
			pstmt.executeUpdate();
			System.out.println("-------finish insertGiftCodeData DatabaseAccount ---- user_id = " + data.getUser_id()
					+ ", giftCode = " + data.getGiftcode());
		} catch (Exception e) {
			logToFile(e);
		} finally {
			Database.closeObject(rs);
			Database.closeObject(pstmt);
			Database.closeObject(conn);
		}
	}

	public static void loadTokenFCMData() {
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		GameManager.getInstance().mapTokenFCM.clear();
		try {
			conn = AppServer.getConnection(NetworkServer.DB_CONGGAME);
			String sSQL = "SELECT * FROM token_fcm_data";
			pstmt = conn.prepareStatement(sSQL);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				long userId = rs.getLong("userId");
				String tokenFCM = rs.getString("tokenFCM");
				GameManager.getInstance().mapTokenFCM.put(userId, tokenFCM);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logToFile(e);
		} finally {
			Database.closeObject(rs);
			Database.closeObject(pstmt);
			Database.closeObject(conn);
		}
	}

	public static void insertTokenFCMData(long userId, String token) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = AppServer.getConnection(NetworkServer.DB_CONGGAME);
			String sSQL = "INSERT INTO token_fcm_data (userId,tokenFCM) values(?,?)";
			pstmt = conn.prepareStatement(sSQL);
			pstmt.setLong(1, userId);
			pstmt.setString(2, token);
			pstmt.executeUpdate();
//			System.out.println("-------finish insertTokenFCMData DatabaseAccount ---- tokenFCM = " + token);
		} catch (Exception e) {
			logToFile(e);
		} finally {
			Database.closeObject(rs);
			Database.closeObject(pstmt);
			Database.closeObject(conn);
		}
	}

	public static void updateTokenFCMData(long userId, String token) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = AppServer.getConnection(NetworkServer.DB_CONGGAME);
			String sSQL = "update token_fcm_data set tokenFCM = ? where userId = ?";
			pstmt = conn.prepareStatement(sSQL);
			pstmt.setString(1, token);
			pstmt.setLong(2, userId);
			pstmt.executeUpdate();
			System.out.println(
					"-------finish updateTokenFCMData DatabaseAccount ---- userId = " + userId + ", token = " + token);
		} catch (Exception e) {
			logToFile(e);
		} finally {
			Database.closeObject(rs);
			Database.closeObject(pstmt);
			Database.closeObject(conn);
		}
	}

	private static int maxId = 0;

	private static int nextMaxId() {
		maxId++;
		return maxId;
	}

	public static void loadUsersInformationSettingMain() {
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		GameManager.getInstance().mapUsersInformationSettingDataMain.clear();
		try {
			conn = AppServer.getConnection(NetworkServer.DB_CONGGAME);
			String sSQL = "SELECT * FROM users_information_setting_main";
			pstmt = conn.prepareStatement(sSQL);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				UsersInformationSettingData data = new UsersInformationSettingData();
				data.setId(rs.getInt("id"));
				data.setGameId(rs.getString("gameId"));
				data.setPlatform(rs.getInt("platform"));
				data.setProviderId(rs.getInt("providerId"));
				data.setImei(rs.getString("imei"));
				data.setInstall(rs.getInt("install"));
				data.setIp(rs.getString("ip"));
				data.setDeviceName(rs.getString("deviceName"));
				GameManager.getInstance().mapUsersInformationSettingDataMain.put(data.getImei(), data);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logToFile(e);
		} finally {
			Database.closeObject(rs);
			Database.closeObject(pstmt);
			Database.closeObject(conn);
		}
	}

	public static void loadUsersInformationSetting() {
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		GameManager.getInstance().mapUsersInformationSettingData.clear();
		try {
			conn = AppServer.getConnection(NetworkServer.DB_CONGGAME);
			String sSQL = "SELECT * FROM users_information_setting";
			pstmt = conn.prepareStatement(sSQL);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				UsersInformationSettingData data = new UsersInformationSettingData();
				data.setId(rs.getInt("id"));
				data.setGameId(rs.getString("gameId"));
				data.setPlatform(rs.getInt("platform"));
				data.setProviderId(rs.getInt("providerId"));
				data.setImei(rs.getString("imei"));
				data.setInstall(rs.getInt("install"));
				data.setIp(rs.getString("ip"));
				data.setDeviceName(rs.getString("deviceName"));
				GameManager.getInstance().mapUsersInformationSettingData.put(data.getId(), data);
				if (data.getId() > maxId) {
					maxId = data.getId();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logToFile(e);
		} finally {
			Database.closeObject(rs);
			Database.closeObject(pstmt);
			Database.closeObject(conn);
		}
	}

	public static void loadTextClientInfo() {
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		GameManager.getInstance().mapTextClientVn.clear();
		GameManager.getInstance().mapTextClientEn.clear();
		try {
			conn = AppServer.getConnection(NetworkServer.DB_CONGGAME);
			String sSQL = "SELECT * FROM text_game_client";
			pstmt = conn.prepareStatement(sSQL);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				TextInfo dataVn = new TextInfo();
				int id = rs.getInt("id");
				dataVn.setId(id);
				dataVn.setText(rs.getString("nameVn"));
				GameManager.getInstance().mapTextClientVn.put(dataVn.getId(), dataVn);
				TextInfo dataEn = new TextInfo();
				dataEn.setId(id);
				dataEn.setText(rs.getString("nameEn"));
				GameManager.getInstance().mapTextClientEn.put(dataEn.getId(), dataEn);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logToFile(e);
		} finally {
			Database.closeObject(rs);
			Database.closeObject(pstmt);
			Database.closeObject(conn);
		}
	}

	public static void loadTextServerInfo() {
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		GameManager.getInstance().mapTextServerVn.clear();
		GameManager.getInstance().mapTextServerEn.clear();
		try {
			conn = AppServer.getConnection(NetworkServer.DB_CONGGAME);
			String sSQL = "SELECT * FROM text_game_server";
			pstmt = conn.prepareStatement(sSQL);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				TextInfo dataVn = new TextInfo();
				int id = rs.getInt("id");
				dataVn.setId(id);
				dataVn.setText(rs.getString("nameVn"));
				GameManager.getInstance().mapTextServerVn.put(dataVn.getId(), dataVn);
				TextInfo dataEn = new TextInfo();
				dataEn.setId(id);
				dataEn.setText(rs.getString("nameEn"));
				GameManager.getInstance().mapTextServerEn.put(dataEn.getId(), dataEn);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logToFile(e);
		} finally {
			Database.closeObject(rs);
			Database.closeObject(pstmt);
			Database.closeObject(conn);
		}
	}

	public static void insertUsersInformationSetting(UsersInformationSettingData data, String ip) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		try {
			int id = nextMaxId();
			data.setId(id);
			conn = AppServer.getConnection(NetworkServer.DB_CONGGAME);
			String sSQL = "INSERT INTO users_information_setting (id,gameId,platform,providerId,imei,install,ip, deviceName) values(?,?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sSQL);
			pstmt.setInt(1, data.getId());
			pstmt.setString(2, data.getGameId());
			pstmt.setInt(3, data.getPlatform());
			pstmt.setInt(4, data.getProviderId());
			pstmt.setString(5, data.getImei());
			pstmt.setInt(6, data.getInstall());
			pstmt.setString(7, ip);
			pstmt.setString(8, data.getDeviceName());
			pstmt.executeUpdate();
//			try {
//				int gameId = Integer.parseInt(data.getGameId());
//				if (gameId == GameId.THANH_CHIEN) {
//					insertUsersInformationSettingThanhChien(data, ip);
//				}
//			} catch (Exception e) {
//				logToFile(e);
//			}
			GameManager.getInstance().mapUsersInformationSettingData.put(data.getId(), data);
			if (!GameManager.getInstance().mapUsersInformationSettingDataMain.containsKey(data.getImei())) {
				insertUsersInformationSettingMain(data, ip);
				GameManager.getInstance().mapUsersInformationSettingDataMain.put(data.getImei(), data);
			}

			GameManager.getInstance().putIpAddressData(data.getIp(), data.getPlatform(), data.getProviderId(),
					data.getDeviceName());
			GameManager.getInstance().putImeiAddressData(data.getImei(), data.getPlatform(), data.getProviderId(),
					data.getDeviceName());
		} catch (Exception e) {
			logToFile(e);
		} finally {
			Database.closeObject(rs);
			Database.closeObject(pstmt);
			Database.closeObject(conn);
		}
	}

	public static void insertUsersInformationSettingMain(UsersInformationSettingData data, String ip) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = AppServer.getConnection(NetworkServer.DB_CONGGAME);
			String sSQL = "INSERT INTO users_information_setting_main (gameId,platform,providerId,imei,install,ip,deviceName) values(?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sSQL);
			pstmt.setString(1, data.getGameId());
			pstmt.setInt(2, data.getPlatform());
			pstmt.setInt(3, data.getProviderId());
			pstmt.setString(4, data.getImei());
			pstmt.setInt(5, data.getInstall());
			pstmt.setString(6, ip);
			pstmt.setString(7, data.getDeviceName());
			pstmt.executeUpdate();
		} catch (Exception e) {
			logToFile(e);
		} finally {
			Database.closeObject(rs);
			Database.closeObject(pstmt);
			Database.closeObject(conn);
		}
	}

//	public static void insertUsersInformationSettingThanhChien(UsersInformationSettingData data, String ip) {
//		PreparedStatement pstmt = null;
//		Connection conn = null;
//		ResultSet rs = null;
//		try {
//			conn = AppServer.getConnection(NetworkServer.DB_THANH_CHIEN_ACCOUNT);
//			String sSQL = "INSERT INTO users_information_setting (gameId,platform,providerId,imei,install,ip) values(?,?,?,?,?,?)";
//			pstmt = conn.prepareStatement(sSQL);
//			pstmt.setString(1, data.getGameId());
//			pstmt.setInt(2, data.getPlatform());
//			pstmt.setInt(3, data.getProviderId());
//			pstmt.setString(4, data.getImei());
//			pstmt.setInt(5, data.getInstall());
//			pstmt.setString(6, ip);
//			pstmt.executeUpdate();
//		} catch (Exception e) {
//			logToFile(e);
//		} finally {
//			Database.closeObject(rs);
//			Database.closeObject(pstmt);
//			Database.closeObject(conn);
//		}
//	}

	public static void loadUsersIp() {
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		GameManager.getInstance().mapIpAddressData.clear();
		try {
			conn = AppServer.getConnection(NetworkServer.DB_CONGGAME);
			String sSQL = "SELECT * FROM users_ip";
			pstmt = conn.prepareStatement(sSQL);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				IpAddressData data = new IpAddressData();
				data.setIp(rs.getString("ip"));
				data.setQuantity(rs.getInt("quantity"));
				GameManager.getInstance().mapIpAddressData.put(data.getIp(), data);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logToFile(e);
		} finally {
			Database.closeObject(rs);
			Database.closeObject(pstmt);
			Database.closeObject(conn);
		}
	}

	public static void insertUsersIp(String ip, int platform, int providerId, String deviceName) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = AppServer.getConnection(NetworkServer.DB_CONGGAME);
			String sSQL = "INSERT INTO users_ip (ip,quantity,platform,providerId,deviceName) values(?,?,?,?,?)";
			pstmt = conn.prepareStatement(sSQL);
			pstmt.setString(1, ip);
			pstmt.setInt(2, 1);
			pstmt.setInt(3, platform);
			pstmt.setInt(4, providerId);
			pstmt.setString(5, deviceName);
			pstmt.executeUpdate();
		} catch (Exception e) {
			logToFile(e);
		} finally {
			Database.closeObject(rs);
			Database.closeObject(pstmt);
			Database.closeObject(conn);
		}
	}

	public static void updateUsersIp(String ip, int quantity) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = AppServer.getConnection(NetworkServer.DB_CONGGAME);
			String sSQL = "UPDATE users_ip SET quantity = ? where ip = ?";
			pstmt = conn.prepareStatement(sSQL);
			pstmt.setInt(1, quantity);
			pstmt.setString(2, ip);
			pstmt.executeUpdate();
		} catch (Exception e) {
			logToFile(e);
		} finally {
			Database.closeObject(rs);
			Database.closeObject(pstmt);
			Database.closeObject(conn);
		}
	}

//	public static void updateUsersIp(Map<String, IpAddressData> map) {
//		PreparedStatement pstmt = null;
//		Connection conn = null;
//		ResultSet rs = null;
//		try {
//
//			conn = AppServer.getConnection(NetworkServer.DB_CONGGAME);
//			String sSQL = "UPDATE users_ip set quantity = ? where ip = ?";
//			pstmt = conn.prepareStatement(sSQL);
//			for (IpAddressData data : map.values()) {
//				pstmt.setInt(1, data.getQuantity());
//				pstmt.setString(2, data.getIp());
//				pstmt.addBatch();
//			}
//
//			int result[] = pstmt.executeBatch();
//			if (result.length == map.size()) {
//				AgentGame.logError("--------------------finish update UsersIp ---" + result.length
//						+ " player-----------------" + AgentJava.getTimeStamp());
//			}
//			pstmt.executeUpdate();
//		} catch (Exception e) {
//			logToFile(e);
//		} finally {
//			Database.closeObject(rs);
//			Database.closeObject(pstmt);
//			Database.closeObject(conn);
//			GameManager.getInstance().mapIpAddressData.clear();
//		}
//	}

	public static void loadUsersImei() {
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		GameManager.getInstance().mapImeiAddressData.clear();
		try {
			conn = AppServer.getConnection(NetworkServer.DB_CONGGAME);
			String sSQL = "SELECT * FROM users_imei";
			pstmt = conn.prepareStatement(sSQL);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				ImeiAddressData data = new ImeiAddressData();
				data.setImei(rs.getString("imei"));
				data.setQuantity(rs.getInt("quantity"));
				GameManager.getInstance().mapImeiAddressData.put(data.getImei(), data);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logToFile(e);
		} finally {
			Database.closeObject(rs);
			Database.closeObject(pstmt);
			Database.closeObject(conn);
		}
	}

	public static void insertUsersImei(String imei, int platform, int providerId, String deviceName) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = AppServer.getConnection(NetworkServer.DB_CONGGAME);
			String sSQL = "INSERT INTO users_imei (imei,quantity,platform,providerId,deviceName) values(?,?,?,?,?)";
			pstmt = conn.prepareStatement(sSQL);
			pstmt.setString(1, imei);
			pstmt.setInt(2, 1);
			pstmt.setInt(3, platform);
			pstmt.setInt(4, providerId);
			pstmt.setNString(5, deviceName);
			pstmt.executeUpdate();
		} catch (Exception e) {
			logToFile(e);
		} finally {
			Database.closeObject(rs);
			Database.closeObject(pstmt);
			Database.closeObject(conn);
		}
	}

	public static void updateUsersImei(String imei, int quantity) {
		PreparedStatement pstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = AppServer.getConnection(NetworkServer.DB_CONGGAME);
			String sSQL = "UPDATE users_imei SET quantity = ? where imei = ?";
			pstmt = conn.prepareStatement(sSQL);
			pstmt.setInt(1, quantity);
			pstmt.setString(2, imei);
			pstmt.executeUpdate();
		} catch (Exception e) {
			logToFile(e);
		} finally {
			Database.closeObject(rs);
			Database.closeObject(pstmt);
			Database.closeObject(conn);
		}
	}

	public static void logToFile(String str, String PATH) {
		LogsManager.logToFile(str, PATH);
	}

	public static void logToFile(Throwable args0) {
		LogsManager.logToFile(args0);
	}

}
