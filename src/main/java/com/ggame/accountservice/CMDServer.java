package com.ggame.accountservice;

public interface CMDServer {
	int CONG_INSTALL_GAME = -99;
	int CONG_DISCONNECT = -98;
	int CONG_REGISTER = -97;
	int CONG_LOGIN_NORMAL = -96;
	int CONG_LOGIN_PLAY_NOW = -95;
	int CONG_LOGIN_FACEBOOK = -94;
	int CONG_LOGIN_GMAIL = -93;
	int CONG_LOGIN_APPLEID = -92;
	int CONG_SERVER_INFO = -91;
	int CONG_THONGBAO = -90;
	int CONG_VALIDATE_TOKEN = -89;
	int CONG_UPDATE_PROFILE = -88;
	int CONG_CHANGE_PASSWORD = -87;

	int CONG_VALIDATE_TOKEN_TC = -200;
	int CONG_INSTALL_GAME_TC = -199; // Khởi tạo kết nối tới cổng
	int CONG_REGISTER_TC = -197; // Đăng ký tài khoản trên cổng
	int CONG_LOGIN_NORMAL_TC = -196; // Đăng nhập tài khoản trên cổng
}
