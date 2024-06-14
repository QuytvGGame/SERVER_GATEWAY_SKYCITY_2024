package com.ggame.services;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import com.ggame.database.DatabaseAccount;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ServicesInstallCongGame implements HttpHandler{
	@Override
	public void handle(HttpExchange he) throws IOException {
		// TODO Auto-generated method stub
		StringBuilder str = new StringBuilder();
		InputStreamReader isr = new InputStreamReader(he.getRequestBody(), "utf-8");
		BufferedReader br = new BufferedReader(isr);
		String line;
		while ((line = br.readLine()) != null) {
			str.append(line);
		}

		String response = "";
		Gson callback = new Gson();
		InstallGameResponse message = new InstallGameResponse();
		System.out.println("========= 0- ServicesInstallCongGame ======== str = " + str.toString());		
		
		try {
			Gson gson = new Gson();
			InstallGameRequest data = gson.fromJson(str.toString(), InstallGameRequest.class);
			if (data == null) {
				message.setErrorCode(ErrorServices.SUCCESS);
				message.setMessage("Lỗi không có dữ liệu");
			} else {				
				System.out.println("========= 1- ServicesInstallCongGame ======== data = " + data.toJson());				
				message.setErrorCode(ErrorServices.FAIL);				
				message.setMessage("Lấy dữ liệu Install Game");
				message.setLinkWebGame("http://napggame.gamehay24h.vip/");
				message.setLinkFanpage("https://www.facebook.com/");
				message.setLinkGroup("https://www.facebook.com/");
				message.setHotline("HotLine hỗ trợ: 19001008");
				message.setEmail("https://translate.google.com/?hl=vi");
				message.setLinkImgBanner("\"https://ggame.com.vn/");
				message.setThongBao("Chào mừng bạn đã đến với game Thành Chiến Mobile. Chúc các bạn có những giờ chơi game vui vẻ");
				message.setLinkBanner("https://ggame.com.vn/");
			}

			response = callback.toJson(message);
			System.out.println("========= 2- ServicesInstallCongGame ======== response = " + response);

		} catch (Exception e) {
			DatabaseAccount.logToFile(e);
			message.setErrorCode(ErrorServices.NULL);
			message.setMessage("NULL");
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
