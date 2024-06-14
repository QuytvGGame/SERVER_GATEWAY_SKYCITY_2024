package com.sgc.game.server;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ServerManager {

	public static String hotline;
	public static String game_name = "ChienThuat";
	public static int serverId = 1;
	public static String[] eventConfig = new String[] { "", "" };
	public static Map<String, List<String>> smsMH;// ma hoa tin nhan
	public static Map<String, Integer> keyWordMap = new HashMap<String, Integer>();
	public static HashMap<Long, String> mLockGifts = new LinkedHashMap<Long, String>(1000);
	public static long minBetMoney = 10;
	
	public static String getHotline() {
		return hotline;
	}

	public static String getGameName() {
		return game_name;
	}
}
