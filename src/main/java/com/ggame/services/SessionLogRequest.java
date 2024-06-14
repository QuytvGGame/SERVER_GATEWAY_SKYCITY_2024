package com.ggame.services;

public class SessionLogRequest {
	public String username;
	public String time_login;
	public int type_device; // 0 là android, 1 là ios, 2 là khác
	public int type_login; // 0: Đn trên web, 1: Đn vào game Thành Chiến
}
