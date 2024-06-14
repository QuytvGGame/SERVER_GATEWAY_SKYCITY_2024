package com.ggame.services;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;

import org.apache.http.entity.StringEntity;

import com.ggame.accountservice.AgentJava;
import com.ggame.accountservice.IDText;
import com.ggame.accountservice.T;
import com.ggame.database.DatabaseAccount;
import com.ggame.entity.LoginType;
import com.ggame.entity.TypePlatform;
import com.ggame.entity.player.LinkConfigInfo;
import com.ggame.entity.player.User;
import com.google.gson.Gson;
import com.sgc.game.network.websocket.Http;
import com.sgc.game.server.GameManager;
import com.sgc.game.server.LogsManager;
import com.sgc.game.server.PlayerManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import config.Config;
import config.GameId;

public class ServicesRegisterAccountCongGame implements HttpHandler {
	@Override
	public void handle(HttpExchange he) throws IOException {
		// TODO Auto-generated method stub
		StringBuilder str = new StringBuilder();
		InputStreamReader isr = new InputStreamReader(he.getRequestBody(), "UTF-8");
		BufferedReader br = new BufferedReader(isr);
		String line;
		while ((line = br.readLine()) != null) {
			str.append(line);
		}

		String response = "";
		Gson callback = new Gson();
		MessageReponse message = new MessageReponse();

		try {
			Gson gson = new Gson();
			RegisterAccountRequest data = gson.fromJson(str.toString(), RegisterAccountRequest.class);
			if (data == null) {
				message.setErrorCode(ErrorServices.FAIL);
				message.setMessage("Lỗi không có dữ liệu");
			} else {
				int gameId = Integer.parseInt(data.gameid);
				int iResult = AgentJava.isValidUserName(data.getUser());
				if (iResult == -1) {
					message.setErrorCode(ErrorServices.FAIL);
					if (data.getGameidInt() != GameId.BABYLON) {
						message.setMessage("Tên tài khoản không được chứa ký tự đặc biệt");
					} else {
						message.setMessage(GameManager.getInstance().getText(data.language, IDText.DANG_KY_ERROR_TK_1));
					}
				} else if (iResult == -3) {
					message.setErrorCode(ErrorServices.FAIL);
					if (data.getGameidInt() != GameId.BABYLON) {
						message.setMessage("Tài khoản chỉ được phép chứa từ 6 đến 20 ký tự");
					} else {
						message.setMessage(GameManager.getInstance().getText(data.language, IDText.DANG_KY_ERROR_TK_2));
					}
				} else if (!data.getPassword().equals(data.getRePassword())) {
					message.setErrorCode(ErrorServices.FAIL);
					if (data.getGameidInt() != GameId.BABYLON) {
						message.setMessage("Mật khẩu không trùng, vui lòng nhập lại");
					} else {
						message.setMessage(GameManager.getInstance().getText(data.language, IDText.DANG_KY_ERROR_MK));
					}
				} else if (PlayerManager.getInstance().checkTonTaiUserName(data.getUser())) {
					message.setErrorCode(ErrorServices.FAIL);
					if (data.getGameidInt() != GameId.BABYLON) {
						message.setMessage("Tài khoản đã tồn tại");
					} else {
						message.setMessage(GameManager.getInstance().getText(data.language, IDText.DANG_KY_ERROR_TK_3));
					}
				} else if (GameManager.getInstance().mapEmail.containsKey(data.email)) {
					message.setErrorCode(ErrorServices.FAIL);
					if (data.getGameidInt() != GameId.BABYLON) {
						message.setMessage("Email đã tồn tại");
					} else {
						message.setMessage(
								GameManager.getInstance().getText(data.language, IDText.DANG_KY_ERROR_EMAIL_2));
					}
				} else {
					boolean registerSs = true;
					if (GameManager.getInstance().mapLinkConfig.containsKey(gameId)) {
						LinkConfigInfo info = GameManager.getInstance().mapLinkConfig.get(gameId);
						if (info.getIsRegisterGmail() == T.TRUE) {
							if (GameManager.getInstance().mapEmail.containsKey(data.email)) {
								registerSs = false;
								message.setErrorCode(ErrorServices.FAIL);
								if (data.getGameidInt() != GameId.BABYLON) {
									message.setMessage("Email đã tồn tại");
								} else {
									message.setMessage(GameManager.getInstance().getText(data.language,
											IDText.DANG_KY_ERROR_EMAIL_2));
								}
							}
						}
					}

					if (registerSs) {
						User userAdd = new User();
						long userIdGen = PlayerManager.getInstance().getMaxUserID() + 1;
						userAdd.setUserId(userIdGen);
						userAdd.setUserName(data.getUser());
						userAdd.setPassWord(data.getPassword());
						userAdd.setPlatform(data.getPlatform());
						userAdd.setIme(data.getIme());
						userAdd.setGameId(data.getGameid());
						userAdd.setType(LoginType.NORMAL);
						userAdd.setKeyHash(userIdGen + AgentJava.generateKeyhash());
						userAdd.setEmail(data.getEmail());

						InetAddress inet = he.getRemoteAddress().getAddress();
						String ip = "";
						// Android -1, Ios-2
						if (data.platform == TypePlatform.ANDROID) {
							ip = inet.getHostAddress();
						} else {
							ip = data.getServerIp();
						}

						userAdd.setIp(ip);
						userAdd.setProviderId(data.getProviderId());
						DatabaseAccount.insertUser(userAdd);
						PlayerManager.getInstance().putUserCached(userAdd, true);
						message.setErrorCode(ErrorServices.SUCCESS);
						if (data.getGameidInt() != GameId.BABYLON) {
							message.setMessage("Đăng ký tài khoản thành công");
						} else {
							message.setMessage(
									GameManager.getInstance().getText(data.language, IDText.DANG_KY_SUCCESS));
						}

						try {
							// gửi dữ liệu qua cổng Web
							RegisterResponse registerResponse = new RegisterResponse();
							registerResponse.username = data.getUser();
							registerResponse.password = data.getPassword();
							registerResponse.email = data.getEmail();
							registerResponse.gameId = data.getGameid();
							registerResponse.ime = data.getIme();
							registerResponse.ip = ip;
							registerResponse.platform = data.getPlatform();
							registerResponse.logintype = LoginType.NORMAL;
							registerResponse.user_id = userIdGen;
							registerResponse.provider_id = data.getProviderId();
							Gson gsonResponse = new Gson();
							StringEntity postingString = new StringEntity(gsonResponse.toJson(registerResponse),
									"UTF-8");
							Http.sendPostJson(Config.urlPostRegisterUserToWeb, postingString);
						} catch (Exception e) {
							LogsManager.logToFile(e);
						}
					} else {

					}
				}
			}

			response = callback.toJson(message);
		} catch (Exception e) {
			message.setErrorCode(ErrorServices.FAIL);
			message.setMessage("NULL");
			response = callback.toJson(message);
			e.printStackTrace();
			DatabaseAccount.logToFile(e);
		}

//		AgentGame.logTest("3 --- ServicesRegisterAccountCongGame --- response = " + response);
		OutputStream os = he.getResponseBody();
		byte[] bs = response.getBytes("UTF-8");
		he.sendResponseHeaders(200, 0);
		try (BufferedOutputStream out = new BufferedOutputStream(he.getResponseBody())) {
			try (ByteArrayInputStream bis = new ByteArrayInputStream(bs)) {
				byte[] buffer = new byte[2];
				int count;
				while ((count = bis.read(buffer)) != -1) {
					os.write(buffer, 0, count);
				}
				os.close();
			}
		}
	}
}
