package com.ggame.entity.player;

public class GiftCodeStatus {
	public static int FAILT = 0; // không tồn tại
	public static int SUCCES = 1; // thành công
	public static int HET_HAN = 2; // Code hết hạn
	public static int NOT_TIME_YET = 3; // Chưa tới thời gian sử dụng
	public static int USED = 4; // đã sử dụng bởi bản thân
	public static int NOT_ENOUGH_LEVEL = 5; // chưa đủ cấp độ
	public static int USED_BY_SOMEONE = 6; // đã sử dụng bởi người khác
	public static int USED_EVENT = 7; // đã sử dụng 1 GiftCode trong sự kiện này
}
