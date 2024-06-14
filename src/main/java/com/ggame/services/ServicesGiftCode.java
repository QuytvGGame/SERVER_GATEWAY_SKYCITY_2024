package com.ggame.services;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ggame.accountservice.AgentGame;
import com.ggame.database.DatabaseAccount;
import com.ggame.entity.GiftCodeInfoBase;
import com.google.gson.Gson;
import com.sgc.game.server.GameManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import config.Config;
import config.GameId;

public class ServicesGiftCode implements HttpHandler {
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
		GiftCodeResponseWeb message = new GiftCodeResponseWeb();
		try {
			System.out.println("========= 0- ServicesCreateGiftCode ======== str = " + str.toString());
			Gson gson = new Gson();
			GiftCodeRequestWeb request = gson.fromJson(str.toString(), GiftCodeRequestWeb.class);

			if (request == null) {
				message.setErrorCode(ErrorServices.FAIL);
				message.setMessage("Tạo GiftCode thất bại. Lỗi không có dữ liệu");
			} else {
				Map<String, GiftCodeInfoBase> map = new HashMap<>();
				map = GameManager.getInstance().mapGiftCode;
				List<ServerItem> lstServerItem = new ArrayList<>();
				List<GiftItemThanhChien> lstGiftItem = new ArrayList<>();
				List<GiftItemBabylon> lstGiftItemBabylon = new ArrayList<>();

				List<String> lstGiftCode = new ArrayList<>();
				List<GiftCodeItem> lstGiftCodeItem = new ArrayList<>();
				System.out.println("========= 1- ServicesCreateGiftCode ======== data = " + request.toJson());
				switch (request.typeAction) {
				case 1:// Tạo GiftCode
					if (map.containsKey(request.namegiftcode)) {
						message.setErrorCode(ErrorServices.FAIL);
						message.setMessage("Tạo GiftCode thất bại. GiftCode đã tồn tại trên hệ thống");
					} else {

						if (request.lstServer.length() > 0) {
							lstServerItem = AgentGame.getListServerItemFromString(request.lstServer);
						}

						if (request.gameid == GameId.THANH_CHIEN) {
							if (request.lstGiftItem.length() > 0) {
								lstGiftItem = AgentGame.getListGiftItemFromString(request.lstGiftItem);
							}
						} else {
							if (request.lstGiftItem.length() > 0) {
								lstGiftItemBabylon = AgentGame.getListGiftItemFromStringBabylon(request.lstGiftItem);
							}
						}

						Boolean success = false;
						String content = "";
						switch (request.typegiftcode) {
						case 1:
							if (request.quantity == 1) {
								GiftCodeItem item = new GiftCodeItem(request.namegiftcode);
								lstGiftCodeItem.add(item);
								lstGiftCode.add(request.namegiftcode);
								message.setLstGiftCode(lstGiftCode);
								success = true;
							} else {
								content = "Tạo GiftCode thất bại.Số lượng gửi không hợp lệ. Gift Code chung chỉ được tạo được 1 GiftCode";
							}
							break;
						case 2:
						case 3:
							if (request.quantity > 0) {
								for (int i = 0; i < request.quantity; i++) {
									String giftCode = AgentGame.randomGiftCode(Config.LENGTH_GIFTCODE);
									lstGiftCode.add(giftCode);
									GiftCodeItem item = new GiftCodeItem(giftCode);
									lstGiftCodeItem.add(item);
								}
								message.setLstGiftCode(lstGiftCode);
								success = true;
							} else {
								content = "Tạo GiftCode thất bại.Số lượng gửi không hợp lệ. Số lượng phải lớn hơn 0";
							}
							break;
						}

						if (success) {
							message.setErrorCode(ErrorServices.SUCCESS);
							message.setMessage("Tạo GiftCode thành công");
							GiftCodeInfoBase data = null;
							if (request.gameid == GameId.THANH_CHIEN) {
								data = new GiftCodeInfoBase(1, request.gameid, request.isAllServer, lstServerItem,
										request.typegiftcode, request.namegiftcode, request.timeopen, request.timeclose,
										request.quantity, lstGiftItem, request.levelMin, lstGiftCodeItem);
							} else {
								data = new GiftCodeInfoBase(1, request.isAllServer, lstServerItem, request.typegiftcode,
										request.namegiftcode, request.timeopen, request.timeclose, request.quantity,
										lstGiftItemBabylon, request.levelMin, lstGiftCodeItem);
							}

							map.put(data.getNamegiftcode(), data);
							DatabaseAccount.insertGiftCode(data);
						} else {
							message.setErrorCode(ErrorServices.FAIL);
							message.setMessage(content);
						}

					}

					break;
				case 2:// Sửa GiftCode
					if (!map.containsKey(request.namegiftcode)) {
						message.setErrorCode(ErrorServices.FAIL);
						message.setMessage("Sửa GiftCode thất bại, GiftCode không có trên hệ thống");
					} else {
						if (request.lstServer.length() > 0) {
							lstServerItem = AgentGame.getListServerItemFromString(request.lstServer);
						}

						if (request.lstGiftItem.length() > 0) {
							lstGiftItem = AgentGame.getListGiftItemFromString(request.lstGiftItem);
						}

						GiftCodeInfoBase dataNew = new GiftCodeInfoBase(1, request.gameid, request.isAllServer,
								lstServerItem, request.typegiftcode, request.namegiftcode, request.timeopen,
								request.timeclose, request.quantity, lstGiftItem, request.levelMin, lstGiftCodeItem);
						map.replace(request.namegiftcode, dataNew);
						message.setErrorCode(ErrorServices.SUCCESS);
						message.setMessage("Sửa GiftCode thành công");
						DatabaseAccount.updateGiftCode(dataNew);
					}

					break;
				case 3:// Xóa GiftCode
					if (!map.containsKey(request.namegiftcode)) {
						message.setErrorCode(ErrorServices.FAIL);
						message.setMessage("Xóa GiftCode thất bại, GiftCode không có trên hệ thống");
					} else {
						message.setErrorCode(ErrorServices.SUCCESS);
						message.setMessage("Xóa GiftCode thành công");
//						map.get(request.namegiftcode).setIsDelete(1);
						DatabaseAccount.deleteGiftCode(request.namegiftcode);
						map.remove(request.namegiftcode);
					}
					break;
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
