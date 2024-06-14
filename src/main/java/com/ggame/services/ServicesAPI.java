package com.ggame.services;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import com.ggame.accountservice.PortGame;
import com.ggame.accountservice.T;
import com.ggame.database.DatabaseAccount;
import com.ggame.entity.player.LinkConfigInfo;
import com.google.gson.Gson;
import com.sgc.game.server.GameManager;
import com.sgc.game.server.LogsManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import config.Config;
import config.GameId;

public class ServicesAPI implements HttpHandler {
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
		ApiResponse message = new ApiResponse();

		try {
			Gson gson = new Gson();
			ApiRequest request = null;
			try {
				request = gson.fromJson(str.toString(), ApiRequest.class);
			} catch (Exception e) {
				message.setErrorCode(ErrorServices.FAIL);
				message.setMessage("NULL");
				response = callback.toJson(message);
				e.printStackTrace();
				logToFile(e);
			}

			if (request == null) {
				message.setErrorCode(ErrorServices.FAIL);
				message.setMessage("Lỗi dữ liệu trả về");
			} else {
				try {
					if (request.imei != null) {
						if (request.imei.length() > 3) {
							if (request.install == T.ZERO) {
								// Cài đặt mới
								UsersInformationSettingData data = new UsersInformationSettingData(request.gameId,
										request.platform, request.providerId, request.imei, request.install,
										request.deviceName);
								DatabaseAccount.insertUsersInformationSetting(data, request.serverIp);
							}
						}
					}
				} catch (Exception e) {
					message.setErrorCode(ErrorServices.FAIL);
					message.setMessage("NULL");
					response = callback.toJson(message);
					e.printStackTrace();
					logToFile(e);
				}

				String requestedURL = "http://" + he.getRequestHeaders().getFirst("Host") + he.getRequestURI();
				URL u = new URL(requestedURL);
				int port = u.getPort();
				if (port == PortGame.ANDROID) {
					message.setRegisterAccount(Config.urlRegisterAccount);
					message.setLoginAccount(Config.urlLoginAccount);
				} else {
					message.setRegisterAccount(Config.urlRegisterAccountIos);
					message.setLoginAccount(Config.urlLoginAccountIos);
				}

				message.setErrorCode(ErrorServices.SUCCESS);
				message.setMessage("Thành công");

				int gameId = Integer.parseInt(request.gameId);
				if (gameId == GameId.BABYLON || gameId == GameId.CO_TUONG) {
					message.setCountryInfo(Config.urlGetCountry);
					message.setTextInfo(Config.urlGetText);
					message.setInfoAccount(Config.urlGetInfoAccount);
					message.setUpdateAccount(Config.urlUpdateAccount);
				}

				if (GameManager.getInstance().mapLinkConfig.containsKey(gameId)) {
					LinkConfigInfo info = GameManager.getInstance().mapLinkConfig.get(gameId);
					message.setLinkWebGame(info.getLinkWebGame());
					message.setLinkFanpage(info.getLinkFanpage());
					message.setLinkGroup(info.getLinkGroup());
					message.setHotline(info.getHotline());
					message.setThongBao(info.getThongBao());
					message.setIsThongBaoPlayNow(0); // tắt

					message.setEmail(info.getEmail());
					message.setLinkImgBanner(info.getLinkImgBanner());
					message.setLinkBanner(info.getLinkBanner());
					message.setLinkUpdateThongTinCaNhan(info.getLinkUpdateThongTinCaNhan());
					message.setLinkDoiMatKhau(info.getLinkDoiMatKhau());
					message.setLinkVote(info.getLinkVote());
					message.setShowIcon(info.getShowIcon());
					message.setShowUpdateInfo(info.getIsOnUpdateThongTin());
					message.setThoitiet(info.getThoitiet());
					message.setIsApple(info.getIsApple());
					message.setLinkDksd(info.getLinkDksd());
					message.setIsRegisterGmail(info.getIsRegisterGmail());
					message.setLinksCheckInternet(info.getLinksCheckInternet());
				}
			}

			response = callback.toJson(message);
		} catch (Exception e) {
			message.setErrorCode(ErrorServices.FAIL);
			message.setMessage("NULL");
			response = callback.toJson(message);
			e.printStackTrace();
			logToFile(e);
		}

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

	public static void logToFile(String str, String PATH) {
		LogsManager.logToFile(str, PATH);
	}

	public static void logToFile(Throwable args0) {
		LogsManager.logToFile(args0);
	}
}
