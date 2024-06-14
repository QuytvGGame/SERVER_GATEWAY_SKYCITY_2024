package com.ggame.services;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.json.JSONArray;

import com.ggame.database.DatabaseAccount;
import com.ggame.entity.player.User;
import com.ggame.pushnotification.PushNotificationFCM;
import com.google.gson.Gson;
import com.sgc.game.server.GameManager;
import com.sgc.game.server.PlayerManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ServicesPushnotification implements HttpHandler {
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
			PushNotificationRequest data = gson.fromJson(str.toString(), PushNotificationRequest.class);
			if (data == null) {
				message.setErrorCode(ErrorServices.FAIL);
				message.setMessage("Lỗi không có dữ liệu");
			} else {
				PushNotificationFCM pushNotificationFCM = new PushNotificationFCM(data.getTitle(), data.getContent());
				System.out.println("--- ServicesPushnotification --- JSON = " + data.toJson());
				JSONArray jsonArray = new JSONArray();
				switch (data.getStatus()) {
				case 1: // 1- gửi cho 1 thiết bị theo server
					User user = null;
//					user = PlayerManager.getInstance().getUsersByUserName(data.getUser_name());
//                    pushNotificationFCM.sendToToken(user.getto);
					break;
				case 2: // 2- gửi cho 1 server
					for (String token : GameManager.getInstance().mapTokenFCM.values()) {
						if (token.length() > 5) {
							jsonArray.put(token);
						}
					}
					pushNotificationFCM.sendToGroup(jsonArray);
					break;
				case 3: // 3- gửi cho all server
					for (String token : GameManager.getInstance().mapTokenFCM.values()) {
						if (token.length() > 5) {
							jsonArray.put(token);
						}
					}
					pushNotificationFCM.sendToGroup(jsonArray);
					break;

				default:
					break;
				}

//				System.out.println("--- ServicesPushnotification --- PUSH DATA SUCCES, DATA = " + jsonArray);
				message.setErrorCode(ErrorServices.SUCCESS);
				message.setMessage("Push dữ liệu thành công");
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
