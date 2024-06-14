package com.ggame.services;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import org.apache.http.entity.StringEntity;

import com.ggame.database.DatabaseAccount;
import com.google.gson.Gson;
import com.sgc.game.network.websocket.Http;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import config.Config;

public class ServicesChangeDisplayerName implements HttpHandler {
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
			ChangeDisplayerNameRequest data = gson.fromJson(str.toString(), ChangeDisplayerNameRequest.class);
			if (data == null) {
				message.setErrorCode(ErrorServices.FAIL);
				message.setMessage("Lỗi không có dữ liệu");
			} else {
				message.setErrorCode(ErrorServices.SUCCESS);
				message.setMessage("Success. Thay đổi tên người chơi thành công");
				// gửi dữ liệu qua cổng
				UpdateInfoDisplayNameResponse dataSend = new UpdateInfoDisplayNameResponse();
				dataSend.user_id_game = data.userId;
				dataSend.display_name = data.displayerName;
				Gson gsonResponse = new Gson();
				StringEntity postingString = new StringEntity(gsonResponse.toJson(dataSend), "UTF-8");
				Http.sendPostJson(Config.urlUpadteInfoDisplayerNameToWeb, postingString);
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
