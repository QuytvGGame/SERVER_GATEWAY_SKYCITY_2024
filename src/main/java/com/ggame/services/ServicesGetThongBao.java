package com.ggame.services;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.ggame.database.DatabaseAccount;
import com.google.gson.Gson;
import com.sgc.game.server.GameManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ServicesGetThongBao implements HttpHandler {
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
		GetThongBaoResponse message = new GetThongBaoResponse();

		try {
			Gson gson = new Gson();
			GetThongBaoRequest data = gson.fromJson(str.toString(), GetThongBaoRequest.class);
			if (data == null) {
				message.setErrorCode(ErrorServices.FAIL);
				message.setMessage("Lỗi không có dữ liệu");
				message.setThongBao("");
			} else {
				if (GameManager.getInstance().mapLinkConfig.containsKey(data.gameId)) {
					message.setErrorCode(ErrorServices.SUCCESS);
					message.setMessage("Lấy thông báo thành công");
					message.setThongBao(GameManager.getInstance().mapLinkConfig.get(data.gameId).getThongBao());
					message.setThongBaoIOS(GameManager.getInstance().mapLinkConfig.get(data.gameId).getThongBaoIOS());
				} else {
					message.setErrorCode(ErrorServices.FAIL);
					message.setMessage("Không tìm thấy Game ID");
					message.setThongBao("");
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
