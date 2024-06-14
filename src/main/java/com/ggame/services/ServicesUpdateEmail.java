package com.ggame.services;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.http.entity.StringEntity;

import com.ggame.accountservice.AgentJava;
import com.ggame.database.DatabaseAccount;
import com.ggame.entity.player.User;
import com.google.gson.Gson;
import com.sgc.game.network.websocket.Http;
import com.sgc.game.server.GameManager;
import com.sgc.game.server.LogsManager;
import com.sgc.game.server.PlayerManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import config.Config;

public class ServicesUpdateEmail implements HttpHandler {
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
			UpdateEmailRequest data = gson.fromJson(str.toString(), UpdateEmailRequest.class);
			if (data == null) {
				message.setErrorCode(ErrorServices.FAIL);
				message.setMessage("Lỗi không có dữ liệu");
			} else {
				if (!AgentJava.isValidEmail(data.emailNew)) {
					message.setErrorCode(ErrorServices.FAIL);
					message.setMessage("Email không đúng định dạng");
				} else {
					if (GameManager.getInstance().mapEmail.containsKey(data.emailNew)) {
						message.setErrorCode(ErrorServices.FAIL);
						message.setMessage("Email đã tồn tại");
					} else {
						message.setErrorCode(ErrorServices.SUCCESS);
						message.setMessage("Cập nhật Email thành công");
						User user = PlayerManager.getInstance().getUsersByUserName(data.username);
						if (user != null) {
							user.setEmail(data.emailNew);
							DatabaseAccount.updateEmail(user.getUserId(), data.emailNew);
						}
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
