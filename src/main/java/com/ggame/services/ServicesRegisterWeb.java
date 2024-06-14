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

import config.GameId;

public class ServicesRegisterWeb implements HttpHandler {
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
			RegisterRequest registerRequest = gson.fromJson(str.toString(), RegisterRequest.class);
			if (registerRequest == null) {
				message.setErrorCode(ErrorServices.FAIL);
				message.setMessage("Đăng kí thất bại. Lỗi không có dữ liệu");
			} else {
				int iResult = AgentJava.isValidUserName(registerRequest.username);
				if (iResult == -1) {
					message.setErrorCode(ErrorServices.FAIL);
					message.setMessage("Tên tài khoản không được chứa ký tự đặc biệt");
				} else if (iResult == -3) {
					message.setErrorCode(ErrorServices.FAIL);
					message.setMessage("Tài khoản chỉ được phép chứa từ 6 đến 20 ký tự");
				} else if (!AgentJava.isValidEmail(registerRequest.email)) {
					message.setErrorCode(ErrorServices.FAIL);
					message.setMessage("Email sai định dạng. Vui lòng nhập lại");
				} else if (PlayerManager.getInstance().getUsersByUserName(registerRequest.username) != null) {
					message.setErrorCode(ErrorServices.FAIL);
					message.setMessage("Tài khoản đã tồn tại");
				} else if (GameManager.getInstance().mapEmail.containsKey(registerRequest.email)) {
					message.setErrorCode(ErrorServices.FAIL);
					message.setMessage("Email đã tồn tại");
				} else {
					User user = PlayerManager.getInstance().getUser(registerRequest.username);
					if (user == null) {
						User userAdd = new User();
						userAdd.setUserId(PlayerManager.getInstance().getMaxUserID() + 1);
						userAdd.setUserName(registerRequest.username);
						userAdd.setPassWord(registerRequest.password);
						userAdd.setIp(registerRequest.ip);
						userAdd.setEmail(registerRequest.email);
						userAdd.setType(registerRequest.type);
						userAdd.setKeyHash(AgentJava.generateKeyhash());
						userAdd.setGameId(GameId.THANH_CHIEN_STR);
						userAdd.setProviderId(1);
						DatabaseAccount.insertUser(userAdd);
						PlayerManager.getInstance().putUserCached(userAdd, true);
						message.setErrorCode(ErrorServices.SUCCESS);
						message.setMessage("Đăng ký tài khoản từ cổng Web thành công");
					} else {
						message.setErrorCode(ErrorServices.FAIL);
						message.setMessage("Đăng kí thất bại. Đã tồn tại tài khoản trên Cổng");
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
