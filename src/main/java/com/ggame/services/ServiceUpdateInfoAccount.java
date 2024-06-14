package com.ggame.services;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.ggame.accountservice.AgentGame;
import com.ggame.accountservice.AgentJava;
import com.google.gson.Gson;
import com.sgc.game.server.GameManager;
import com.sgc.game.server.LogsManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ServiceUpdateInfoAccount implements HttpHandler {
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
		UpdateInfoAccountResponse message = new UpdateInfoAccountResponse();
		try {
			Gson gson = new Gson();
			UpdateInfoAccountRequest data = gson.fromJson(str.toString(), UpdateInfoAccountRequest.class);
			if (data == null) {
				message.setErrorCode(ErrorServices.FAIL);
				message.setMessage("Lỗi không có dữ liệu");
			} else {
				if (!AgentJava.isValidTime(data.ngaysinh)) {
					message.setErrorCode(ErrorServices.FAIL);
					message.setMessage("Ngày sinh không hợp lệ.");
					response = callback.toJson(message);
				} else if (!AgentJava.isValidTime(data.ngaycap)) {
					message.setErrorCode(ErrorServices.FAIL);
					message.setMessage("Ngày cấp CCCD/CMT không hợp lệ.");
					response = callback.toJson(message);
				} else if (data.sdt.length() < 9) {
					message.setErrorCode(ErrorServices.FAIL);
					message.setMessage("Số điện thoại không hợp lệ, vui lòng kiểm tra lại");
					response = callback.toJson(message);
				} else {
					long dayBetween = AgentJava.getTimeBetWeen2Day(data.ngaysinh, data.ngaycap);
					if (dayBetween <= 0) {
						message.setErrorCode(ErrorServices.FAIL);
						message.setMessage("Ngày cấp CCCD/CMT không thể nhỏ hơn ngày sinh");
						response = callback.toJson(message);
					} else {
						if (dayBetween < 5110) {
							message.setErrorCode(ErrorServices.FAIL);
							message.setMessage("Tối thiểu 14 tuổi mới đăng ký được CCCD/CMT, vui lòng kiểm tra lại");
							response = callback.toJson(message);
						} else {
							message.setErrorCode(ErrorServices.SUCCESS);
							message.setMessage("Cập nhật thông tin thành công.");
							response = callback.toJson(message);
							GameManager.getInstance().UpdateInfoAccount(data);
						}
					}
				}
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
