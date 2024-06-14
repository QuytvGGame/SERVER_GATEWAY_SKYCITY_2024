package com.ggame.services;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.ggame.accountservice.T;
import com.ggame.database.DatabaseAccount;
import com.ggame.entity.player.User;
import com.google.gson.Gson;
import com.sgc.game.server.GameManager;
import com.sgc.game.server.PlayerManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ServicesLockAccount implements HttpHandler {
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
		MessageReponse message = new MessageReponse();

		try {
			Gson gson = new Gson();
			LockAccountRequest data = gson.fromJson(str.toString(), LockAccountRequest.class);
			if (data == null) {
				message.setErrorCode(ErrorServices.FAIL);
				message.setMessage("Lỗi không có dữ liệu");
			} else {
				if (data.getLstUser_name().size() > T.ZERO) {
					String content = "Thông tin : ";
					for (int i = 0; i < data.getLstUser_name().size(); i++) {
						String userName = data.getLstUser_name().get(i);
						User user = PlayerManager.getInstance().getUsersByUserName(userName);
						if (user != null) {
							if (!GameManager.getInstance().mapLockAccount.containsKey(userName)) {
								GameManager.getInstance().mapLockAccount.put(userName, data);
								DatabaseAccount.insertUsersLockData(data, userName);
							} else {
								GameManager.getInstance().mapLockAccount.replace(userName, data);
								DatabaseAccount.updateUsersLockData(data, userName);
							}

							if (data.getStatus() == T.ONE) {
								content += " Khóa người chơi " + userName + " thành công.";
							} else {
								content += " Mở khóa người chơi " + userName + " thành công.";
							}

						} else {
							content += ".Không tìm thấy người chơi " + userName + " trên hệ thống.";
						}
					}

					message.setErrorCode(ErrorServices.SUCCESS);
					message.setMessage(content);
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
