package com.ggame.util;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import com.ggame.accountservice.CMDServer;
import com.ggame.accountservice.Key;
import com.ggame.accountservice.T;
import com.ggame.database.DatabaseAccount;
import com.ggame.entity.ServerInfoBase;
import com.ggame.entity.player.LinkConfigInfo;
import com.ggame.entity.player.ServerPlayerData;
import com.ggame.entity.player.User;
import com.ggame.network.ISession;
import com.ggame.network.Message;
import com.ggame.services.ErrorServices;
import com.sgc.game.server.GameManager;

import config.Config;

public class MessageUtils {
	public static void onInstallGame(ISession conn) {
		try {
			String content = " Tạo kết nối thành công";
			Message msg = new Message(CMDServer.CONG_INSTALL_GAME);
			msg.putString("content", content);
			conn.sendMessage(msg);
		} catch (Exception e) {
			e.printStackTrace();
			logToFile(e);
		}
	}
	
	public static void onCongValidateTokenTC(ISession conn) {
		try {
			String content = " Tạo kết nối thành công";
			Message msg = new Message(CMDServer.CONG_VALIDATE_TOKEN_TC);
			msg.putString("content", content);
			conn.sendMessage(msg);
		} catch (Exception e) {
			e.printStackTrace();
			logToFile(e);
		}
	}

	public static void onDisconnect(User player, Boolean succes) {
		Message msg = new Message(CMDServer.CONG_DISCONNECT);
		msg.putBoolean("succes", succes);
		player.sendMessage(msg);
	}

	public static void onThongBao(ISession conn, String content) {
		Message msg = new Message(CMDServer.CONG_THONGBAO);
		msg.putString("content", content);
		conn.sendMessage(msg);
	}

	public static void onValidateToken(User player) {
		Message msg = new Message(CMDServer.CONG_VALIDATE_TOKEN);
		msg.putLong("userId", player.getUserId());
		msg.putString("token", player.getKeyHash());
		msg.putString("userName", player.getUserName());

		Boolean updateVersion = false;
		msg.putBoolean("updateVersion", updateVersion);
		msg.putString("linkImgBanner",
				"https://scontent.fhan17-1.fna.fbcdn.net/v/t39.30808-6/268082166_401241441780519_1808010706458584087_n.jpg?_nc_cat=102&ccb=1-7&_nc_sid=730e14&_nc_ohc=EosdpzjNgO8AX_BZnBQ&_nc_ht=scontent.fhan17-1.fna&oh=00_AfAJhIay6BHWAq1jyqC0c3EyIXGVhluq4Pu9HkmJdD3PBA&oe=63A636F5");
		msg.putString("linkBanner", "https://ggame.com.vn/");
		msg.putString("linkFanpage", "https://www.facebook.com/");
		msg.putString("linkGroup", "https://www.facebook.com/");
		msg.putString("linkWebGame", "http://napggame.gamehay24h.vip/");
		msg.putString("email", "https://translate.google.com/?hl=vi");
		msg.putString("linkEvent",
				"https://www.facebook.com/thanhchienvietnam/photos/a.102962564941743/406197991284864/");
		msg.putString("hotline", "HotLine hỗ trợ: 19001008");
		msg.putString("thongBao", "Chào mừng bạn đã đến với game Phui88. Chúc các bạn có những giờ chơi game vui vẻ");

		if (updateVersion) {
			msg.putString("linkUpdate", "");
			msg.putString("content", "");
		}

		player.sendMessage(msg);
	}

	public static void onServerInfo(User player) {
		try {
			Message msg = new Message(CMDServer.CONG_SERVER_INFO);
			msg.putArrayJson("serverInfo", GameManager.getInstance().jsonArrayMapServerInfo);
			JSONArray list = new JSONArray();
			Map<Long, ServerPlayerData> map = GameManager.getInstance().getMapServerPlayer(player.getUserName());
			if (map != null) {
				for (ServerPlayerData serverPlayer : map.values())
					list.put(serverPlayer.toJson());
			}
			msg.putArrayJson("serverPlayerData", list);
			player.sendMessage(msg);
		} catch (Exception e) {
			e.printStackTrace();
			logToFile(e);
			// TODO: handle exception
		}
	}

	public static void onChagePassword(User player, Boolean status, String content) {
		Message msg = new Message(CMDServer.CONG_CHANGE_PASSWORD);
		msg.putBoolean("status", status);
		msg.putString("content", content);
		if (status) {
			msg.putString("passwordNew", player.getPassWord());
		}
		player.sendMessage(msg);
	}

	public static void onUpdateProfile(User player, Boolean status, String content) {
		Message msg = new Message(CMDServer.CONG_UPDATE_PROFILE);
		msg.putBoolean("status", status);
		msg.putString("content", content);
		if (status) {
			msg.putString("email", player.getEmail());
			msg.putString("phoneNumber", player.getRegPhone());
		}
		player.sendMessage(msg);
	}

	public static void onRegister(ISession session, String user, String password, Boolean succes, String content) {
		Message msg = new Message(CMDServer.CONG_REGISTER);
		msg.putBoolean("succes", succes);
		msg.putString("content", content);
		if (succes) {
			msg.putString("user", user);
			msg.putString("password", password);
		}
		session.sendMessage(msg);
	}

	public static void onLoginNormal(User player, Boolean succes, String content) {
		Message msg = new Message(CMDServer.CONG_LOGIN_NORMAL);
		msg.putBoolean("succes", succes);
		msg.putString("content", content);
		player.sendMessage(msg);
	}

	private static void logToFile(Throwable e) {
		DatabaseAccount.logToFile(e);
	}

	public static void onCongInstallGameTC(ISession conn, LinkConfigInfo info) {
		try {
			Message msg = new Message(CMDServer.CONG_INSTALL_GAME_TC);
			msg.putString("registerAccount", Config.urlRegisterAccount);
			msg.putString("loginAccount", Config.urlLoginAccount);
			msg.putString("linkWebGame", info.getLinkWebGame());
			msg.putString("linkFanpage", info.getLinkFanpage());
			msg.putString("linkGroup", info.getLinkGroup());
			msg.putString("hotline", info.getHotline());
			msg.putString("email", info.getEmail());
			msg.putString("linkImgBanner", info.getLinkImgBanner());
			msg.putString("thongBao", info.getThongBao());
			msg.putString("linkBanner", info.getLinkBanner());
			msg.putString("linkUpdateThongTinCaNhan", info.getLinkUpdateThongTinCaNhan());
			msg.putString("linkDoiMatKhau", info.getLinkDoiMatKhau());
			conn.sendMessage(msg);
		} catch (Exception e) {
			e.printStackTrace();
			logToFile(e);
		}
	}

	public static void onCongRegisterTC(ISession conn, boolean isSuccess, String message) {
		try {
			Message msg = setResultAndMessage(CMDServer.CONG_REGISTER_TC, isSuccess, message);
			conn.sendMessage(msg);
		} catch (Exception e) {
			e.printStackTrace();
			logToFile(e);
		}
	}

	public static void onCongRegisterTC(ISession conn, boolean isSuccess, String message, String user,
			String password) {
		try {
			Message msg = setResultAndMessage(CMDServer.CONG_REGISTER_TC, isSuccess, message);
			msg.putString("user", user);
			msg.putString("password", password);
			conn.sendMessage(msg);
		} catch (Exception e) {
			e.printStackTrace();
			logToFile(e);
		}
	}

	public static void onCongLoginNormalTC(ISession conn, boolean isSuccess, String message) {
		try {
			Message msg = setResultAndMessage(CMDServer.CONG_LOGIN_NORMAL_TC, isSuccess, message);
			conn.sendMessage(msg);
		} catch (Exception e) {
			e.printStackTrace();
			logToFile(e);
		}
	}

	public static void onCongLoginNormalTC(ISession conn, boolean isSuccess, String message, long userId,
			String userName, String password, int loginType, JSONArray lstServerData) {
		try {
			Message msg = setResultAndMessage(CMDServer.CONG_LOGIN_NORMAL_TC, isSuccess, message);
			msg.putBoolean("updateVersion", false);
			msg.putString("linkUpdate", "");
			msg.putLong("userId", userId);
			msg.putString("userName", userName);
			msg.putString("password", password);
			msg.putInt("loginType", loginType);
			msg.putArrayJson("serverInfo", GameManager.getInstance().jsonArrayMapServerInfo);
			msg.putArrayJson("serverPlayerData", lstServerData);
			conn.sendMessage(msg);
		} catch (Exception e) {
			e.printStackTrace();
			logToFile(e);
		}
	}

	public static Message setResultAndMessage(int cmd, boolean isSuccess, String message) {
		Message msg = new Message(cmd);
		msg.putInt(Key.Result, isSuccess ? T.TRUE : T.FALSE);
		msg.putString(Key.Message, message);
		return msg;
	}

}
