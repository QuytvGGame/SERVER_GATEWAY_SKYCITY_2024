package com.ggame.services;
import org.json.JSONObject;

public class GiftCodeRequestWeb {
	public int typeAction; // 1-Thêm, 2-Sửa, 3-Xoá
	public int gameid; // 1-Game phủi, 2-Thành chiến, 3-Babylon
	public int isAllServer; // 1-All Server, 0-không phải All Server
	public String lstServer; // List Server nếu không phải All Server
	public int typegiftcode; // 1-Chung, 2-Riêng,không giới hạn, 3-Riêng, giới hạn
	public String namegiftcode; // Tên của Gift Code. Lưu ý: là duy nhất(không lặp lại), không chứ ký tự đặc biệt và dấu.
	public int levelMin; // cấp độ nhỏ nhất của người chơi để sử dụng được GiftCode
	public String timeopen; // Thời gian bắt đầu hiệu lực GiftCode. Định dạng: 2022-06-21 00:00:56
	public String timeclose; // Thời gian hết hiệu lực GiftCode. Định dạng: 2022-06-21 00:00:56
	public int quantity; // Số lượng GiftCode
	public String lstGiftItem; // List Item quà

	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		try {
			json.put("typeAction", typeAction);
			json.put("gameid", gameid);
			json.put("isAllServer", isAllServer);
			json.put("lstServer", lstServer);
			json.put("typegiftcode", typegiftcode);
			json.put("namegiftcode", namegiftcode);
			json.put("levelMin", levelMin);
			json.put("timeopen", timeopen);
			json.put("timeclose", timeclose);
			json.put("quantity", quantity);
			json.put("lstGiftItem", lstGiftItem);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
}
