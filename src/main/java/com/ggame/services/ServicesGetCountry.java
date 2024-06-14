package com.ggame.services;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ggame.entity.player.CountryInfo;
import com.google.gson.Gson;
import com.sgc.game.server.GameManager;
import com.sgc.game.server.LogsManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ServicesGetCountry implements HttpHandler {
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
		GetCountryResponse message = new GetCountryResponse();
		try {
			Gson gson = new Gson();
			GetCountryRequest data = gson.fromJson(str.toString(), GetCountryRequest.class);
			if (data == null) {
				message.setErrorCode(ErrorServices.FAIL);
				message.setMessage("Lỗi không có dữ liệu");
			} else {
				message.setErrorCode(ErrorServices.SUCCESS);
				message.setMessage("Thành công");
				Map<Integer, CountryInfo> mapCountryInfo = new HashMap<>();
				mapCountryInfo = GameManager.getInstance().getMapCountryInfo(data.getGameid());
				Collection<CountryInfo> values = mapCountryInfo.values();
				List<CountryInfo> lst = new ArrayList<CountryInfo>(values);
				message.setCountryInfo(lst);
				response = callback.toJson(message);
			}
		} catch (Exception e) {
			message.setErrorCode(ErrorServices.FAIL);
			message.setMessage("NULL");
			response = callback.toJson(message);
			e.printStackTrace();
			LogsManager.logToFile(e);
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
