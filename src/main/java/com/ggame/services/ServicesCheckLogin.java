package com.ggame.services;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.ggame.accountservice.T;
import com.ggame.database.DatabaseAccount;
import com.ggame.entity.LoginType;
import com.ggame.entity.player.ServerPlayerData;
import com.ggame.entity.player.User;
import com.google.gson.Gson;
import com.sgc.game.server.GameManager;
import com.sgc.game.server.PlayerManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ServicesCheckLogin implements HttpHandler {
	@Override
	public void handle(HttpExchange he) throws IOException {
		StringBuilder str = new StringBuilder();
		InputStreamReader isr = new InputStreamReader(he.getRequestBody(), "UTF-8");
		BufferedReader br = new BufferedReader(isr);
		String line;
		while ((line = br.readLine()) != null) {
			str.append(line);
		}

		String response = "";
		Gson callback = new Gson();
		CheckLoginReponse message = new CheckLoginReponse();

		try {
			Gson gson = new Gson();
			CheckLoginRequest data = gson.fromJson(str.toString(), CheckLoginRequest.class);
			if (data == null) {
				message.setErrorCode(ErrorServices.FAIL);
				message.setMessage("Lỗi không có dữ liệu");
			} else {
				ServerPlayerData player = null;
				if (GameManager.getInstance().mapServerPlayerData.containsKey(data.username)) {
					player = GameManager.getInstance().GetDataPlayer(data.username, data.keyhash);
					if (player == null) {
						message.setErrorCode(ErrorServices.FAIL);
						message.setUsername(data.username);
						message.setMessage("Phản hồi từ máy chủ. Lỗi không tìm thấy người chơi trên hệ thống");
					} else {
						if (player.getKeyhash().length() < 3) {
							message.setErrorCode(ErrorServices.FAIL);
							message.setUsername(player.getUsername());
							message.setMessage("Phản hồi từ máy chủ. Lỗi không tìm thấy người chơi trên hệ thống");
						} else {
							message.setErrorCode(ErrorServices.SUCCESS);
							message.setMessage("Phản hồi từ máy chủ. Tìm thấy người chơi trên hệ thống");
							message.setUsername(player.getUsername());
							message.setUserIdGame(player.getUser_id_game());
							message.setEmail("");
							User user = PlayerManager.getInstance().getUsersByUserName(data.username);
							if (user != null) {
								message.setEmail(user.getEmail());
								message.setPassWord(user.getPassWord());
								if (user.getType() == LoginType.PLAYNOW) {
									if (user.getUsername2() != null) {
										if (user.getUsername2().length() >= 6) {
											message.setIsUpdate(T.FALSE);
										} else {
											message.setIsUpdate(T.TRUE);
										}
									} else {
										message.setIsUpdate(T.TRUE);
									}
								} else {
									message.setIsUpdate(T.FALSE);
								}
							}

							message.setIsReload(T.FALSE);
							DatabaseAccount.updateUserLogin(data);
							if (user.getGame_id_close() != 0) {
								if (user.getServer_login_close() != 0) {
									if (user.getServer_login_close() != data.getServerIdInt()) {
										message.setIsReload(T.TRUE);
									}
								} else {
									message.setIsReload(T.TRUE);
								}
							} else {
								message.setIsReload(T.TRUE);
							}

							user.setServer_login_close(data.getServerIdInt());
							user.setGame_id_close(data.getGameIdInt());
						}
					}
				} else {
					User user = PlayerManager.getInstance().getUsersByUserName(data.username);
					if (user != null) {
						message.setImei(user.getIme());
						message.setIp(user.getIp());
						message.setEmail(user.getEmail());
					}
					message.setErrorCode(ErrorServices.FAIL);
					message.setMessage("Phản hồi từ máy chủ. Lỗi không tìm thấy người chơi trên hệ thống");
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
