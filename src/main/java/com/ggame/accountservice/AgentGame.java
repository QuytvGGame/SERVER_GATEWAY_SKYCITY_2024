package com.ggame.accountservice;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.ggame.services.GiftCodeItem;
import com.ggame.services.GiftItemBabylon;
import com.ggame.services.GiftItemThanhChien;
import com.ggame.services.ServerItem;
import com.google.gson.Gson;

public class AgentGame {

//	private static Log logger = LogFactory.getLog(AgentGame.class);

	public static String getDisplayNameChat(String displayName) {
		if (displayName.length() >= 8) {
			displayName = displayName.substring(0, displayName.length() - 3);
			displayName += T.BaCham;
		}
		return displayName;
	}

	public static boolean isDebug = false;

	private static Gson _gson = new Gson();

	public static void log(Object obj) {
		if (isDebug)
			System.out.println("\n" + obj + "\n");
	}

	public static void logError(Object obj) {
		System.err.println("\n" + obj + "\n");
	}
	
	public static void logTest(Object obj) {
		System.err.println("\n" + obj + "\n");
	}

	public static boolean checkJsonValid(String json) {
		if (json != null && json.trim().length() > T.JSON_VALID)
			return true;
		return false;
	}

	private static Random _random = new Random();

	public static int ranDom(int maxRange) {
		return _random.nextInt(maxRange + 1);
	}

	public static int ranDom(int minRange, int maxRange) {
		return _random.nextInt(maxRange - minRange + 1) + minRange;
	}

	// chuyển đổi từ chuỗi 3,4,5,5,5 sang list<Integer>s
	public static List<Integer[]> getListTextIdFromString(String strTextId) {
		List<Integer[]> listTextId = new ArrayList<>();
		if (strTextId != null) {
			String[] arrayTextId = strTextId.split(T.PhanTachPhay);
			if (arrayTextId != null) {
				for (int i = 0; i < arrayTextId.length; i++) {
					String[] arr = arrayTextId[i].split(T.Tach);
					listTextId.add(new Integer[] { Integer.parseInt(arr[0]), Integer.parseInt(arr[1]) });
				}
			}
		}
		return listTextId;
	}

	public static List<Integer> getListFromString(String str) {
		List<Integer> list = new ArrayList<>();
		if (str != null) {
			String[] array = str.split(T.PhanTachPhay);
			if (array != null) {
				for (int i = 0; i < array.length; i++) {
					list.add(Integer.parseInt(array[i]));
				}
			}
		}
		return list;
	}

	public static List<ServerItem> getListServerItemFromString(String str) {
		List<ServerItem> list = new ArrayList<>();
		if (str != null) {
			String[] arrayGiftItem = str.split(T.PhanTachPhay);
			if (arrayGiftItem != null) {
				for (int i = 0; i < arrayGiftItem.length; i++) {
					String gift = arrayGiftItem[i];
					ServerItem item = new ServerItem(Integer.parseInt(gift));
					list.add(item);
				}
			}
		}
		return list;
	}

	public static List<GiftCodeItem> getListGiftCodeItemFromString(String str) {
		List<GiftCodeItem> list = new ArrayList<>();
		if (str != null) {
			String[] arrayGiftItem = str.split(T.PhanTachPhay);
			if (arrayGiftItem != null) {
				for (int i = 0; i < arrayGiftItem.length; i++) {
					String gift = arrayGiftItem[i];
					GiftCodeItem item = new GiftCodeItem(gift);
					list.add(item);
				}
			}
		}
		return list;
	}

	public static List<GiftItemThanhChien> getListGiftItemFromString(String strGiftItem) {
		List<GiftItemThanhChien> listGift = new ArrayList<>();
		if (strGiftItem != null) {
			String[] arrayGiftItem = strGiftItem.split(T.PhanTachPhay);
			if (arrayGiftItem != null) {
				for (int i = 0; i < arrayGiftItem.length; i++) {
					String[] arrayElement = arrayGiftItem[i].split(T.Tach);
					if (arrayElement != null && arrayElement.length == 2) {
						int typeId = Integer.parseInt(arrayElement[0]);
						int type = Integer.parseInt(arrayElement[1]);
						GiftItemThanhChien giftItem = new GiftItemThanhChien(typeId, type, 1);
						listGift.add(giftItem);

					} else if (arrayElement != null && arrayElement.length == 3) {
						int typeId = Integer.parseInt(arrayElement[0]);
						int type = Integer.parseInt(arrayElement[1]);
						int quantity = Integer.parseInt(arrayElement[2]);
						GiftItemThanhChien giftItem = new GiftItemThanhChien(typeId, type, quantity);
						listGift.add(giftItem);

					} else if (arrayElement != null && arrayElement.length == 4) {
						int typeId = Integer.parseInt(arrayElement[0]);
						int type = Integer.parseInt(arrayElement[1]);
						int quantity = Integer.parseInt(arrayElement[2]);
						String giftId = arrayElement[3];
						GiftItemThanhChien giftItem = new GiftItemThanhChien(typeId, type, quantity, giftId);
						listGift.add(giftItem);
					}
				}
			}
		}
		return listGift;
	}

	public static List<GiftItemBabylon> getListGiftItemFromStringBabylon(String strGiftItem) {
		List<GiftItemBabylon> listGift = new ArrayList<>();
		if (strGiftItem != null) {
			String[] arrayGiftItem = strGiftItem.split(T.PhanTachPhay);
			if (arrayGiftItem != null) {
				for (int i = 0; i < arrayGiftItem.length; i++) {
					String[] arrayElement = arrayGiftItem[i].split(T.Tach);
					if (arrayElement != null && arrayElement.length == 2) {
						int idIt = Integer.parseInt(arrayElement[0]);
						int quantity = Integer.parseInt(arrayElement[1]);
						GiftItemBabylon giftItem = new GiftItemBabylon(idIt, 0, quantity);
						listGift.add(giftItem);
					} else if (arrayElement != null && arrayElement.length == 3) {
						int idIt = Integer.parseInt(arrayElement[0]);
						int type = Integer.parseInt(arrayElement[1]);
						int quantity = Integer.parseInt(arrayElement[2]);
						GiftItemBabylon giftItem = new GiftItemBabylon(idIt, type, quantity);
						listGift.add(giftItem);
					}
				}
			}
		}
		return listGift;
	}

	static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	static SecureRandom rnd = new SecureRandom();

	public static String randomGiftCode(int len) {
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++)
			sb.append(AB.charAt(rnd.nextInt(AB.length())));
		return sb.toString();
	}
}
