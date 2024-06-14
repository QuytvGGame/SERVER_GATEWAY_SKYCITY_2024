package com.ggame.services;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.ggame.accountservice.AgentGame;
import com.ggame.accountservice.AgentJava;
import com.ggame.database.DatabaseAccount;
import com.ggame.entity.player.User;
import com.google.gson.Gson;
import com.sgc.game.server.PlayerManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ServicesChangePassword implements HttpHandler {
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
			ChangePasswordRequest data = gson.fromJson(str.toString(), ChangePasswordRequest.class);
			if (data == null) {
				message.setErrorCode(ErrorServices.FAIL);
				message.setMessage("Lỗi không có dữ liệu");
			} else {
				if (data.username != null) {
					int iResult = AgentJava.isValidUserName(data.username);
					if (iResult == -1) {
						message.setErrorCode(ErrorServices.FAIL);
						message.setMessage("Tên tài khoản không được chứa ký tự đặc biệt");
					} else if (iResult == -3) {
						message.setErrorCode(ErrorServices.FAIL);
						message.setMessage("Tài khoản chỉ được phép chứa từ 6 đến 20 ký tự");
					} else {
						if (!AgentJava.isValidPassword(data.password_new)) {
							message.setErrorCode(ErrorServices.FAIL);
							message.setMessage("Mật khẩu không đúng định dạng");
						} else {
							message.setErrorCode(ErrorServices.SUCCESS);
							message.setMessage("Đổi mật khẩu thành công");
							User user = PlayerManager.getInstance().getUsersByUserName(data.username);
							if (user != null) {
								user.setPassWord(data.password_new);
								DatabaseAccount.updatePassword(user.getUserId(), data.password_new);
							}
						}
					}
				} else {
					message.setErrorCode(ErrorServices.FAIL);
					message.setMessage("Null UserName");
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
