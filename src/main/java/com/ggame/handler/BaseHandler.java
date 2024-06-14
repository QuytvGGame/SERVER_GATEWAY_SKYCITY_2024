package com.ggame.handler;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ggame.accountservice.AgentJava;
import com.ggame.database.DatabaseAccount;
import com.ggame.entity.LoginType;
import com.ggame.entity.player.LinkConfigInfo;
import com.ggame.entity.player.ServerPlayerData;
import com.ggame.entity.player.User;
import com.ggame.network.ISession;
import com.ggame.network.Message;
import com.ggame.services.RegisterResponse;
import com.ggame.util.MessageUtils;
import com.google.gson.Gson;
import com.sgc.game.network.websocket.Http;
import com.sgc.game.server.GameManager;
import com.sgc.game.server.LogsManager;
import com.sgc.game.server.PlayerManager;

import config.Config;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;

public class BaseHandler {
	Log log = LogFactory.getLog(CommonHandler.class);
	private static BaseHandler instance;

	public static BaseHandler getInstance() {
		if (instance == null)
			instance = new BaseHandler();
		return instance;
	}

	public void logToFile(Exception e) {
		LogsManager.logToFile(e);
	}

	public void logToFile2(String log, String PATH) {
		LogsManager.logToFile(log, PATH);
	}

	public int versionToInt(String v) {
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

	private boolean checkYesPlayer(ISession session) {
		if (session.getUser() == null)
			return false;
		return true;
	}

	public void onInstallGame(ISession session, Message message) {
		try {
			MessageUtils.onInstallGame(session);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logToFile(e);
		}
	}
	
	public void onCongValidateTokenTC(ISession session, Message message) {
		try {
			MessageUtils.onCongValidateTokenTC(session);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logToFile(e);
		}
	}

	public void onDisconnectServer(ISession session) {
		if (session != null) {
			if (checkYesPlayer(session)) {
				PlayerManager.getInstance().doLoginExits(session, session.getUser(), session.getUser().getUserName());
				MessageUtils.onDisconnect(session.getUser(), true);
			}
		}
	}

	public void onServerInfo(ISession session, Message message) {
		try {
			MessageUtils.onServerInfo(session.getUser());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logToFile(e);
		}
	}

	public void onChangePassword(ISession session, Message message) {
		try {

			User player = session.getUser();
			if (player == null)
				return;

			String passwordCurrent = message.getString("passwordCurrent");
			String passwordNew = message.getString("passwordNew");
			String repasswordNew = message.getString("repasswordNew");
			Boolean status = false;
			String content = "";

			if (AgentJava.CheckPasswordValidate(passwordCurrent)) {
				content = "Mật khẩu sai định dạng. Mật khẩu phải từ 8 kí tự trở lên, có cả chữ và số";
				MessageUtils.onChagePassword(session.getUser(), status, content);
				return;
			}

			if (AgentJava.CheckPasswordValidate(passwordNew)) {
				content = "Mật khẩu mới sai định dạng. Mật khẩu phải từ 8 kí tự trở lên, có cả chữ và số";
				MessageUtils.onChagePassword(session.getUser(), status, content);
				return;
			}

			if (!passwordNew.equals(repasswordNew)) {
				content = "Mật khẩu mới không trùng nhau, vui lòng kiểm tra lại";
				MessageUtils.onChagePassword(session.getUser(), status, content);
				return;
			}

			status = true;
			content = "Thay đổi mật khẩu thành công";
			player.setPassWord(passwordNew);
			MessageUtils.onChagePassword(session.getUser(), status, content);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logToFile(e);
		}
	}

	public void onUpdateProfile(ISession session, Message message) {
		try {

			User player = session.getUser();
			if (player == null)
				return;

			String userName = message.getString("userName");
			String password = message.getString("password");
			String phoneNumber = message.getString("phoneNumber");
			String email = message.getString("email");
			Boolean status = false;
			String content = "";

			if (phoneNumber.length() < 10) {
				content = "Số điện thoại sai định dạng. Vui lòng nhập lại";
				MessageUtils.onUpdateProfile(session.getUser(), status, content);
				return;
			}

			if (!AgentJava.isValidEmail(email)) {
				content = "Email sai định dạng. Vui lòng nhập lại";
				MessageUtils.onUpdateProfile(session.getUser(), status, content);
				return;
			}

			if (!PlayerManager.getInstance().checkInfoUsers(userName, password)) {
				content = "Mật khẩu không đúng xin vui lòng kiểm trai lại";
				MessageUtils.onUpdateProfile(session.getUser(), status, content);
				return;
			}

			status = true;
			content = "Thay đổi thông tin thành công";
			player.setEmail(email);
			player.setRegPhone(phoneNumber);
			MessageUtils.onUpdateProfile(session.getUser(), status, content);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logToFile(e);
		}
	}

	public void onRegister(ISession session, Message message) {
		try {
			String user = message.getString("user");
			String password = message.getString("password");
			String rePassword = message.getString("rePassword");
			String email = message.getString("email");
			int platform = message.getInt("platform");
			String imei = message.getString("imei");

			Boolean succes = false;
			String content = "";

			int iResult = AgentJava.isValidUserName(user);
			if (iResult == -1) {
				content = "Tên tài khoản không được chứa ký tự đặc biệt";
				MessageUtils.onThongBao(session, content);
				return;
			}

			if (iResult == -3) {
				content = "Tài khoản chỉ được phép chứa từ 6 đến 20 ký tự";
				MessageUtils.onThongBao(session, content);
				return;
			}

			if (!password.equals(rePassword)) {
				content = "Mật khẩu không trùng, vui lòng nhập lại";
				MessageUtils.onThongBao(session, content);
				return;
			}

			if (!AgentJava.isValidEmail(email)) {
				content = "Email sai định dạng. Vui lòng nhập lại";
				MessageUtils.onThongBao(session, content);
				return;
			}

			if (PlayerManager.getInstance().getUsersByUserName(user) != null) {
				content = "Tài khoản đã tồn tại";
				MessageUtils.onThongBao(session, content);
				return;
			}

			succes = true;
			content = "Đăng ký tài khoản thành công";
			User userAdd = new User();
			long userIdGen = PlayerManager.getInstance().getMaxUserID() + 1;
			userAdd.setUserId(userIdGen);
			userAdd.setUserName(user);
			userAdd.setPassWord(password);
			userAdd.setPlatform(platform);
			userAdd.setIme(imei);
			userAdd.setGameId(Config.gameId);
			userAdd.setType(LoginType.NORMAL);
			userAdd.setKeyHash(userIdGen + AgentJava.generateKeyhash());
			userAdd.setEmail(email);
			userAdd.setIp(session.getIP());

			User userCheck = DatabaseAccount.insertUser(session, userAdd);
			if (user != null)
				PlayerManager.getInstance().putUserCached(userCheck, true);

			MessageUtils.onRegister(session, user, password, succes, content);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logToFile(e);
		}
	}

	public void onLoginNormal(ISession session, Message message) {
		try {
			String user = message.getString("user");
			String password = message.getString("password");
//			String gameId =  message.getString("gameId");
//			int platform = message.getInt("platform");
//			int version = message.getInt("version");
//			String imei = message.getString("imei");
//			String keyHash = message.getString("keyHash");
			Boolean succes = false;
			String content = "";
//			System.out.println("LOGIN_NEW_GGAME = -99,  succes = " + succes + ", content = " + content);
			User user1 = PlayerManager.getInstance().getUsersByUserName(user);

			if (user1 == null) {
				content = "Tài khoản không tồn tại, vui lòng kiểm tra lại";
				MessageUtils.onThongBao(session, content);
				return;
			}

			if (!PlayerManager.getInstance().checkInfoUsers(user, password)) {
				content = "Tài khoản hoặc mật khẩu không đúng xin vui lòng nhập lại";
				MessageUtils.onThongBao(session, content);
				return;
			}

			User userCheck = PlayerManager.getInstance().checkLogin(session, user1);
			if (userCheck != null) {
				try {
					// Đưa vào danh sách online để quản lý
					if (session.getSessionWS() != null)
						PlayerManager.getInstance().getMapUsersSession()
								.put(session.getSessionWS().getSessionWebSocket().getId(), userCheck);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}

			succes = true;
			content = "Đăng nhập thành công";
			doLoginSuccess(session, userCheck);

			if (checkYesPlayer(session))
				MessageUtils.onLoginNormal(session.getUser(), succes, content);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logToFile(e);
		}
	}

	private void doLoginSuccess(final ISession conn, final User player) throws IOException {
		try {
			if (player == null)
				return;

			MessageUtils.onServerInfo(player);
			MessageUtils.onValidateToken(player);
		} catch (Exception e) {
			logToFile(e);
		}
	}

	public void onCongInstallGameTC(ISession session, Message message) {
		try {
			
			System.out.println("==========1 BaseHandler ======= onCongInstallGameTC");
			
			String gameId = message.getString("gameId");
			int platform = message.getInt("platform");
			int providerId = message.getInt("providerId");

			System.out.println("==========2 BaseHandler ======= onCongInstallGameTC, gameId = " + gameId + ", platform = " + platform + ", providerId = " + providerId);
			
			int gameIdInt = Integer.parseInt(gameId);
			if (GameManager.getInstance().mapLinkConfig.containsKey(gameIdInt)) {
				LinkConfigInfo info = GameManager.getInstance().mapLinkConfig.get(gameId);
				MessageUtils.onCongInstallGameTC(session, info);
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logToFile(e);
		}
	}

	public void onCongRegisterTC(ISession session, Message message) {
		try {
			String gameid = message.getString("gameid");
			String user = message.getString("user");
			String password = message.getString("password");
			String rePassword = message.getString("rePassword");
			String email = message.getString("email");
			int providerId = message.getInt("providerId");
			int platform = message.getInt("platform");
			String imei = message.getString("imei");

			int iResult = AgentJava.isValidUserName(user);
			if (iResult == -1) {
				MessageUtils.onCongRegisterTC(session, false, "Tên tài khoản không được chứa ký tự đặc biệt");
				return;
			}

			if (iResult == -3) {
				MessageUtils.onCongRegisterTC(session, false, "Tài khoản chỉ được phép chứa từ 6 đến 20 ký tự");
				return;
			}

			if (!password.equals(rePassword)) {
				MessageUtils.onCongRegisterTC(session, false, "Mật khẩu không trùng, vui lòng nhập lại");
				return;
			}

			if (!AgentJava.isValidEmail(email)) {
				MessageUtils.onCongRegisterTC(session, false, "Email sai định dạng. Vui lòng nhập lại");
				return;
			}

			if (PlayerManager.getInstance().getUsersByUserName(user) != null) {
				MessageUtils.onCongRegisterTC(session, false, "Tài khoản đã tồn tại");
				return;
			}

			if (GameManager.getInstance().mapEmail.containsKey(email)) {
				MessageUtils.onCongRegisterTC(session, false, "Email đã tồn tại");
				return;
			}

			User userAdd = new User();
			long userIdGen = PlayerManager.getInstance().getMaxUserID() + 1;
			userAdd.setUserId(userIdGen);
			userAdd.setUserName(user);
			userAdd.setPassWord(password);
			userAdd.setPlatform(platform);
			userAdd.setIme(imei);
			userAdd.setGameId(gameid);
			userAdd.setType(LoginType.NORMAL);
			userAdd.setKeyHash(userIdGen + AgentJava.generateKeyhash());
			userAdd.setEmail(email);
			userAdd.setIp(session.getIP());
			userAdd.setProviderId(providerId);
			DatabaseAccount.insertUser(userAdd);
			PlayerManager.getInstance().putUserCached(userAdd, true);
			MessageUtils.onCongRegisterTC(session, true, "Đăng ký tài khoản thành công", user, password);

			try {
				// gửi dữ liệu qua cổng Web
				RegisterResponse registerResponse = new RegisterResponse();
				registerResponse.username = user;
				registerResponse.password = password;
				registerResponse.email = email;
				registerResponse.gameId = gameid;
				registerResponse.ime = imei;
				registerResponse.ip = session.getIP();
				registerResponse.platform = platform;
				registerResponse.logintype = LoginType.NORMAL;
				registerResponse.user_id = userIdGen;
				registerResponse.provider_id = providerId;
				Gson gsonResponse = new Gson();
				StringEntity postingString = new StringEntity(gsonResponse.toJson(registerResponse), "UTF-8");
				Http.sendPostJson(Config.urlPostRegisterUserToWeb, postingString);
//				Http.sendPostJson(Config.urlRegisterUserToThanhChien, postingString);
			} catch (Exception e) {
				// TODO: handle exception
				LogsManager.logToFile(e);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logToFile(e);
		}
	}

	public void onCongLoginNormalTC(ISession session, Message message) {
		try {
//			User player = session.getUser();
//			if (player == null)
//				return;
			String gameid = message.getString("gameid");
			int version = message.getInt("version");
			String username = message.getString("user");
			String password = message.getString("password");
			int platform = message.getInt("platform");
			int providerId = message.getInt("providerId");
			String imei = message.getString("imei");
			int loginType = message.getInt("loginType");

			User user = null;
			if (loginType == LoginType.NORMAL) {
				user = PlayerManager.getInstance().getUsersByUserName(username);
			} else if (loginType == LoginType.PLAYNOW) {
				if (PlayerManager.getInstance().isExitPlayNow(imei)) {
					// đã có tài khoản chơi ngay
					user = PlayerManager.getInstance().getUsersByIMEI(imei);
				} else {
					// chưa có tài khoản chơi ngay, tạo tài khoản ngẫu nhiên cho người chơi
					user = new User();
					long userIdGen = PlayerManager.getInstance().getMaxUserID() + 1;
					user.setUserId(userIdGen);
					String userName = AgentJava.RandomString(Config.LENGTH_MAX_USERNAME);
					user.setUserName(userName);
					String password2 = AgentJava.RandomString(Config.LENGTH_MAX_PASSWORD);
					user.setPassWord(password2);
					user.setPlatform(platform);
					user.setIme(imei);
					user.setGameId(gameid);
					user.setType(LoginType.PLAYNOW);
					user.setKeyHash(userIdGen + AgentJava.generateKeyhash());
					user.setEmail("");
					user.setIp(session.getIP());
					PlayerManager.getInstance().CreateAccount(user);
				}
			}

			if (user == null) {
				MessageUtils.onCongLoginNormalTC(session, false, "Tài khoản không tồn tại, vui lòng kiểm tra lại");
				return;
			}

			if (!PlayerManager.getInstance().checkInfoUsers(username, password)) {
				MessageUtils.onCongLoginNormalTC(session, false,
						"Tài khoản hoặc mật khẩu không đúng xin vui lòng nhập lại");
				return;
			}

			JSONArray jsonArray = new JSONArray();
			Map<Long, ServerPlayerData> map = GameManager.getInstance().getMapServerPlayer2(gameid, username);
			if (map != null) {
				for (ServerPlayerData serverPlayer : map.values())
					jsonArray.put(serverPlayer.toJson());
			}

			MessageUtils.onCongLoginNormalTC(session, true, "Đăng nhập thành công", user.getUserId(),
					user.getUserName(), password, loginType, jsonArray);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logToFile(e);
		}
	}
}
