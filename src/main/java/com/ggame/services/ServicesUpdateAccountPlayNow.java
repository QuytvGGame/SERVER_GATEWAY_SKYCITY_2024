package com.ggame.services;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import com.ggame.accountservice.AgentJava;
import com.ggame.database.DatabaseAccount;
import com.ggame.entity.player.User;
import com.google.gson.Gson;
import com.sgc.game.server.GameManager;
import com.sgc.game.server.PlayerManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ServicesUpdateAccountPlayNow implements HttpHandler {
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
			UpdateAccountPlayNowRequest data = gson.fromJson(str.toString(), UpdateAccountPlayNowRequest.class);
			if (data == null) {
				message.setErrorCode(ErrorServices.FAIL);
				message.setMessage("Lỗi không có dữ liệu");
			} else {
				int iResult = AgentJava.isValidUserName(data.getUsername2());
				if (iResult == -1) {
					message.setErrorCode(ErrorServices.FAIL);
					message.setMessage("Tên tài khoản không được chứa ký tự đặc biệt");
				} else if (iResult == -3) {
					message.setErrorCode(ErrorServices.FAIL);
					message.setMessage("Tài khoản chỉ được phép chứa từ 6 đến 20 ký tự");
				} else if (!AgentJava.isValidEmail(data.getEmail())) {
					message.setErrorCode(ErrorServices.FAIL);
					message.setMessage("Email sai định dạng. Vui lòng nhập lại");
				} else if (PlayerManager.getInstance().getUsersByUserName(data.getUsername2()) != null) {
					message.setErrorCode(ErrorServices.FAIL);
					message.setMessage("Tài khoản đã tồn tại");
				} else if (GameManager.getInstance().mapEmail.containsKey(data.email)) {
					message.setErrorCode(ErrorServices.FAIL);
					message.setMessage("Email đã tồn tại");
				} else {
					if (!PlayerManager.getInstance().checkInfoUsers(data.getUsername(), data.getPassword())) {
						message.setErrorCode(ErrorServices.FAIL);
						message.setMessage("Không tìm thấy tài khoản chơi ngay cần cập nhật");
					} else {
						message.setErrorCode(ErrorServices.SUCCESS);
						message.setMessage("Cập nhật tài khoản chơi ngay thành công");
						User user = PlayerManager.getInstance().getUsersByUserName(data.getUsername());
						user.setUsername2(data.getUsername2());
						user.setPassword2(data.getPassword2());
						DatabaseAccount.updateAccountPlayNow(data.getUsername(), data.getUsername2(),
								data.getPassword2());
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
