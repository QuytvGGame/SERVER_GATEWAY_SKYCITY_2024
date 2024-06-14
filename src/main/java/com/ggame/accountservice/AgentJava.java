package com.ggame.accountservice;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sgc.game.server.LogsManager;

import config.Config;

public class AgentJava {
	public static String getCurrentTime() {
		return new Timestamp(System.currentTimeMillis()).toString();
	}

	public static long currentTimeMillis() {
		return System.currentTimeMillis();
	}

	public static Timestamp getTimeStamp() {
		return new Timestamp(System.currentTimeMillis());
	}

	public static void log(Object obj) {
		System.out.println("\n" + obj + "\n");
	}

	public static void logError(Object obj) {
		System.err.println("\n" + obj + "\n");
	}

	// 3 dấu ### bên phải là nhóm 3 số thì phân tách
	private static NumberFormat formatter = new DecimalFormat("###,###");

	/**
	 * Định dạng tiền có dấu chấm
	 */
	public static String formatMoney(long money) {
		return formatter.format(money);
	}

	public static boolean checkTimestampNhoHonTimeCurrentDay(Timestamp timeCheck) {
		Timestamp timeCurrent = new Timestamp(System.currentTimeMillis());
		long timeCk = getYearMonthDayGhepChuoiToLong(timeCheck);
		long timeC = getYearMonthDayGhepChuoiToLong(timeCurrent);
		return timeCk < timeC;
	}

	public static boolean isValidTime(String dateStr) {
		DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		sdf.setLenient(false);
		try {
			sdf.parse(dateStr);
		} catch (ParseException e) {
			return false;
		}
		return true;
	}

	// khoảng cách giữa 2 ngày tính bằng ngày
	public static long getTimeBetWeen2Day(String dayStart, String dayEnd) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
			Date firstDate = sdf.parse(dayStart);
			Date secondDate = sdf.parse(dayEnd);
			long timeDifferenceInMillis = secondDate.getTime() - firstDate.getTime();
			TimeUnit time = TimeUnit.DAYS;
			long differenceInDays = time.convert(timeDifferenceInMillis, TimeUnit.MILLISECONDS);
			return differenceInDays;
		} catch (Exception e) {
			LogsManager.logToFile(e);
			return 0;
		}
	}

	public static long getYearMonthDayGhepChuoiToLong(Timestamp timeConvert) {
		return Long.parseLong(timeConvert.toString().split(" ")[0].replaceAll("-", ""));
	}

	public static Timestamp getTimestamp(long miliseconds) {
		return new Timestamp(miliseconds);
	}

	public static long getTotalMiliSecondsOneDay() {
		return getTotalSecondsOneDay() * 1000L;
	}

	public static long getTotalSecondsOneDay() {
		return 24L * 60L * 60L;
	}

	public static long getTimestampTruocNgay(long day) {
		return currentTimeMillis() - (day * getTotalMiliSecondsOneDay());
	}

	public static long getTimestampSauNgay(long day) {
		return currentTimeMillis() + (day * getTotalMiliSecondsOneDay());
	}

	public static long convertTimeCurrentMiliSecondsToTimeCurrentSeconds(long currentMiliSeconds) {
		return currentMiliSeconds / 1000L;
	}

	public static long convertTimeCurrentSecondsToTimeCurrentMiliSeconds(long currentSeconds) {
		return currentSeconds * 1000L;
	}

	public static String getTextTimeFromMiliseconds(long miliSeconds) {
		return (getTextTimeFromSeconds(miliSeconds / 1000L));
	}

	private static String getTextTimeFromSeconds(long seconds) {
		long giay = seconds;
		long phut;
		long gio;
		long onePhutToGiay = 60;
		long oneGioToGiay = 3600;
		if (giay < onePhutToGiay) {
			String _giay = giay > 9 ? "" + giay : "0" + giay;
			return ("00:" + _giay);
		} else if (giay >= onePhutToGiay && giay < oneGioToGiay) {
			phut = (giay - giay % onePhutToGiay) / onePhutToGiay;
			String _minus = phut > 9 ? "" + phut : "0" + phut;
			giay %= onePhutToGiay;
			String _giay = giay > 9 ? "" + giay : "0" + giay;
			return (_minus + ":" + _giay);
		} else {
			gio = (giay - giay % oneGioToGiay) / oneGioToGiay;
			phut = ((giay % oneGioToGiay) - (giay % oneGioToGiay) % onePhutToGiay) / onePhutToGiay;
			String _minus = phut > 9 ? "" + phut : "0" + phut;
			giay = giay - phut * onePhutToGiay - gio * oneGioToGiay;
			return (gio + ":" + _minus + ":" + (giay > 9 ? "" + giay : "0" + giay));
		}
	}

	public static long getTimeMiniSecondsCachTimePlay(int _timeHenGio) {
		int timeHenGio = Math.abs(_timeHenGio);
		long timeWait = 0L;
		Calendar c = Calendar.getInstance();
		int hour24 = c.get(Calendar.HOUR_OF_DAY);
		int minutes = c.get(Calendar.MINUTE);
		int seconds = c.get(Calendar.SECOND);
		int hourWait = 0;
		if (timeHenGio > 11) {
			if (timeHenGio < hour24) {
				hourWait = 24 - hour24 - timeHenGio;
			} else {
				hourWait = timeHenGio - hour24;
			}
		} else {
			hourWait = 24 - hour24 + timeHenGio;
		}
		// System.err.println(hourWait);
		timeWait = hourWait * 60L * 60L * 1000L - (60 - minutes) * 60L * 1000L - (60 - seconds) * 1000L;
		// System.out.println(((float) timeWait /
		// 3600000F));System.out.println("-----");
		return timeWait;
	}

	public static JSONArray getJsonArray(List<Integer> list) {
		JSONArray array = new JSONArray();
		for (Integer data : list)
			array.put(data);
		return array;
	}

	public static JSONObject getJsonObject(String key, Object data) {
		try {
			return new JSONObject().put(key, data);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static Gson _gson = new Gson();

	/** Error */
	// public static <T> List<T> getClassList(String json) {
	// return _gson.fromJson(json, new TypeToken<ArrayList<T>>() {
	// }.getType());
	// }

	public static int round(float value) {
		return Math.round(value);
	}

	private static final String SPLIT_FLOAT = "\\.";

	/** Làm tròn lên số nguyên lớn hơn. VD: 3.1 -> 4 */
	public static int roundUp(float value) {
		String[] raws = String.valueOf(value).split(SPLIT_FLOAT);
		if (raws.length == 2) {
			if (Integer.valueOf(raws[1].trim()) > 0)
				return Integer.parseInt(raws[0]) + 1;
			else
				return Integer.parseInt(raws[0]);
		}
		return (int) value;
	}

	/** Làm tròn xuống. VD: 3.9 -> 3 */
	public static int roundDown(float value) {
		String[] raws = String.valueOf(value).split(SPLIT_FLOAT);
		if (raws.length == 2) {
			return Integer.parseInt(raws[0]);
		}
		return (int) value;
	}

	/** Lấy mấy số sau phần thập phân không làm tròn */
	public static float getFloatOrigin(float value, int length) {
		String result = "0.0F";
		String[] raws = String.valueOf(value).split(SPLIT_FLOAT);
		if (raws.length == 2) {
			result = raws[0] + ".";
			if (raws[1].length() >= length)
				result += raws[1].substring(0, length);
			else
				result += raws[1];
		}
		return Float.parseFloat(result);
	}

	private final static int CheckJson = 2;

	public static List<Integer> getListInteger(String json) {
		List<Integer> listResult = new ArrayList<>();
		if (json != null && json.length() > CheckJson) {
			try {
				JSONArray array = new JSONArray(json);
				for (int i = 0; i < array.length(); i++)
					listResult.add(array.getInt(i));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return listResult;
	}

	public static List<Long> getListLong(String json) {
		List<Long> listResult = new ArrayList<>();
		if (json != null && json.length() > CheckJson) {
			try {
				JSONArray array = new JSONArray(json);
				for (int i = 0; i < array.length(); i++)
					listResult.add(array.getLong(i));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return listResult;
	}

	public static String[] getArrayString(String json) {
		String[] arrayString = null;
		if (json != null && json.length() > CheckJson) {
			try {
				JSONArray array = new JSONArray(json);
				arrayString = new String[array.length()];
				for (int i = 0; i < array.length(); i++)
					arrayString[i] = array.getString(i);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return arrayString;
	}

	public static List<String> getListString(String json) {
		List<String> listResult = new ArrayList<>();
		if (json != null && json.length() > CheckJson) {
			try {
				JSONArray array = new JSONArray(json);
				for (int i = 0; i < array.length(); i++)
					listResult.add(array.getString(i));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return listResult;
	}

	public static String getStringArrayJson(List<String> listString) {
		JSONArray array = new JSONArray();
		if (listString != null) {
			for (String val : listString)
				array.put(val);
		}
		return array.toString();
	}

	public static String getIntArrayJson(List<Integer> listString) {
		JSONArray array = new JSONArray();
		if (listString != null) {
			for (Integer val : listString)
				array.put(val);
		}
		return array.toString();
	}

	public static String[][] getArray2String(String json) {
		if (json.trim().length() > 2) {
			try {
				return _gson.fromJson(json, new TypeToken<String[][]>() {
				}.getType());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static String getJson(Object val) {
		JSONObject object = new JSONObject();
		try {
			object.put("data", val);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return object.toString();
	}

	public static String[][] copyString2ArrayMatrix(String[][] origin) {
		if (origin != null) {
			String[][] result = new String[origin.length][origin.length];
			for (int i = 0; i < origin.length; i++) {
				String[] tmp = new String[origin[i].length];
				for (int j = 0; j < origin[i].length; j++)
					tmp[j] = origin[i][j];
				result[i] = tmp;
			}
			return result;
		}
		return null;
	}

	private static Random random = new Random();

	public static int getRandomBound(int bound) {
		return random.nextInt(bound);
	}

	public static String getDateCurrent() {
		DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

		Date currentDate = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		String endDate = T.Empty;
		try {
			endDate = simpleDateFormat.format(currentDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return endDate;
	}

	public static long getNumberDayDiffernce(Date dateOld, Date dateCurrent) {
		long numberDay = 0;
		try {
			long getDiff = dateCurrent.getTime() - dateOld.getTime();
			// using TimeUnit class from java.util.concurrent package
			numberDay = TimeUnit.MILLISECONDS.toDays(getDiff);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return numberDay;
	}

	public static long getNumberDayDifference(String date1Str, String date2Str) {
		DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

		long numberDay = -999;
		try {
			Date date2 = simpleDateFormat.parse(date2Str);
			Date date1 = simpleDateFormat.parse(date1Str);
			long getDiff = date2.getTime() - date1.getTime();
			// using TimeUnit class from java.util.concurrent package
			numberDay = TimeUnit.MILLISECONDS.toDays(getDiff);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return numberDay;
	}

	public static Boolean CheckPasswordValidate(String password) {
		for (int i = 0; i < password.length(); i++) {
			if (!Character.isDigit(password.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public static int isValidUserName(String userName) {
		for (int i = 0; i < userName.length(); i++) {
			char c = userName.charAt(i);
			if ((('0' > c) || (c > '9')) && (('A' > c) || (c > 'Z')) && (('a' > c) || (c > 'z'))) {
				return -1;
			}
		}
//        if (CacheManage.getInstance().mapUsers.get(userName) != null)
//            return -2;
		if (userName.length() > Config.LENGTH_MAX_USERNAME || userName.length() < Config.LENGTH_MIN_USERNAME)
			return -3;
		return 1;
	}

	public static Boolean isValidPassword(String password) {
		if (password.length() > Config.LENGTH_MAX_USERNAME || password.length() < Config.LENGTH_MIN_USERNAME)
			return false;
		return true;
	}

	public static Boolean isValidEmail(String email) {
		String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
		java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
		java.util.regex.Matcher m = p.matcher(email);
		return m.matches();
	}

//	public static String generateToken()
//	{
//		SecureRandom random = new SecureRandom();
//		byte bytes[] = new byte[20];
//		random.nextBytes(bytes);
//		String token = bytes.toString();
//		return token;
//	}

	// time kết thúc < time hiện tại -> hết thời gian sử dụng code, long <= 0,
	// --------- time mở < time hiện tại -> chưa tới thời gian mở, long <= 0
	public static long getCompareTime(String date) {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String timeCurrent = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
			Date date1 = format.parse(timeCurrent);
			Date date2 = new Date();
			date2 = format.parse(date);
			long time = (date2.getTime() - date1.getTime()) / 1000;
			return time;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return T.ZERO;
		}
	}

	private static final SecureRandom secureRandom = new SecureRandom(); // threadsafe
	private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); // threadsafe

	public static String generateKeyhash() {
		byte[] randomBytes = new byte[24];
		secureRandom.nextBytes(randomBytes);
		return base64Encoder.encodeToString(randomBytes);
	}

	public static String getClientIpAddress(HttpServletRequest request) {
		String xForwardedForHeader = request.getHeader("X-Forwarded-For");
		if (xForwardedForHeader == null) {
			return request.getRemoteAddr();
		} else {
			// As of https://en.wikipedia.org/wiki/X-Forwarded-For
			// The general format of the field is: X-Forwarded-For: client, proxy1, proxy2
			// ...
			// we only want the client
			return new StringTokenizer(xForwardedForHeader, ",").nextToken().trim();
		}
	}

	// Random chuỗi String
	public static String RandomString(int n) {
		// choose a Character random from this String
		String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";
		// create StringBuffer size of AlphaNumericString
		StringBuilder sb = new StringBuilder(n);
		for (int i = 0; i < n; i++) {
			// generate a random number between
			// 0 to AlphaNumericString variable length
			int index = (int) (AlphaNumericString.length() * Math.random());
			// add Character one by one in end of sb
			sb.append(AlphaNumericString.charAt(index));
		}
		return sb.toString();
	}

	public static void Log(String log) {
		// System.out.println(log);
	}
}
