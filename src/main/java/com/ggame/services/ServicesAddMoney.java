package com.ggame.services;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Map;

import com.ggame.accountservice.AgentGame;
import com.ggame.database.DatabaseAccount;
import com.ggame.entity.player.ServerPlayerData;
import com.google.gson.Gson;
import com.sgc.game.server.GameManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ServicesAddMoney implements HttpHandler {
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
			AgentGame.logTest("--- ServicesAddMoney --- JSON =  " + str.toString());
			AddMoneyRequest addMoney = gson.fromJson(str.toString(), AddMoneyRequest.class);
			if (addMoney == null) {
				message.setErrorCode(ErrorServices.FAIL);
				message.setMessage("Lỗi dữ liệu trả về");
			} else {
				if (GameManager.getInstance().mapServerPlayerData.containsKey(addMoney.username)) {
					Map<Long, ServerPlayerData> data = GameManager.getInstance().mapServerPlayerData
							.get(addMoney.username);
					if (data.containsKey(addMoney.userId)) {
				//		DatabaseAccount.insertAddMoney(addMoney);
						message.setErrorCode(ErrorServices.SUCCESS);
						message.setMessage("Thành công");
					} else {
						message.setErrorCode(ErrorServices.FAIL);
						message.setMessage("Không tìm thấy userID = " + addMoney.userId + " tương ứng với username = "
								+ addMoney.username);
					}
				} else {
					message.setErrorCode(ErrorServices.FAIL);
					message.setMessage("Không tìm thấy userName = " + addMoney.username);
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
