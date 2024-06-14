package com.ggame.services;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import com.ggame.database.DatabaseAccount;
import com.ggame.entity.GiftCodeInfoBase;
import com.ggame.entity.player.GiftCodeStatus;
import com.google.gson.Gson;
import com.sgc.game.server.GameManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import config.GameId;

public class ServicesGetGiftCode implements HttpHandler {
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
		GiftCodeResponseServer message = new GiftCodeResponseServer();
		try {
			Gson gson = new Gson();
			GiftCodeRequestServer request = gson.fromJson(str.toString(), GiftCodeRequestServer.class);
			if (request == null) {
				message.setErrorCode(ErrorServices.FAIL);
				message.setMessage("Get GiftCode thất bại. Lỗi không có dữ liệu");
			} else {
				int status = GameManager.getInstance().isExitsGiftCode(request);
				message.setStatus(status);
				message.setErrorCode(ErrorServices.SUCCESS);
				if (status == GiftCodeStatus.SUCCES) {
					GiftCodeInfoBase info = GameManager.getInstance().getInfoGiftCode(request.code);
					message.setNamegiftcode(info.getNamegiftcode());
					message.setTypegiftcode(info.getTypegiftcode());
					message.setMessage("Get GiftCode thành công");
					message.setLevelmin(info.getLevelMin());
					if (request.gameId.equals(GameId.THANH_CHIEN_STR)) {
						message.setLstGiftItem(info.getLstGiftItem());
					} else {
						message.setLstGiftItemBabylon(info.getLstGiftItemBabylon());
					}
				}
				response = callback.toJson(message);
			}
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
