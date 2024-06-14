package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
	public static int VERSION_GAME_THANH_CHIEN = 989;
	public static int VERSION_GAME_BABYLON = 199;
	public static int VERSION_GAME_CO_TUONG = 1;
	
	public static Boolean BAO_TRI = true;
	
	public static int LENGTH_GIFTCODE = 8;
	
	public static int LENGTH_MIN_USERNAME = 6;
	public static int LENGTH_MAX_USERNAME = 20;
	public static int LENGTH_MIN_PASSWORD = 8;
	public static int LENGTH_MAX_PASSWORD = 20;
	
	public static String gameId;
	public static int portService;
	public static String urlPostRegisterUserToWeb;
	public static String urlPostSessionLog;
	public static String urlUpadteInfoDisplayerNameToWeb;
	public static String urlRegisterAccount;
	public static String urlLoginAccount;	
	public static String urlRegisterAccountIos;
	public static String urlLoginAccountIos;	
	public static String urlGetCountry;	
	public static String urlGetText;	
	public static String urlGetInfoAccount;	
	public static String urlUpdateAccount;	
	
//	public static String urlRegisterUserToThanhChien;
	
	private static Properties properties = new Properties();
	public static void loadProperties(String fileName) throws IOException {
		try {
			FileInputStream propsFile = new FileInputStream(fileName);
			properties.load(propsFile);
			propsFile.close();
			gameId = properties.getProperty("gameId", gameId);
			urlPostRegisterUserToWeb = properties.getProperty("urlPostRegisterUserToWeb", urlPostRegisterUserToWeb);
			urlPostSessionLog = properties.getProperty("urlPostSessionLog", urlPostSessionLog);
//			urlRegisterUserToThanhChien = properties.getProperty("urlRegisterUserToThanhChien", urlRegisterUserToThanhChien);
			urlUpadteInfoDisplayerNameToWeb = properties.getProperty("urlUpadteInfoDisplayerNameToWeb", urlUpadteInfoDisplayerNameToWeb);
			urlRegisterAccount = properties.getProperty("urlRegisterAccount", urlRegisterAccount);
			urlLoginAccount = properties.getProperty("urlLoginAccount", urlLoginAccount);
			urlGetCountry = properties.getProperty("urlGetCountry", urlGetCountry);
			urlGetText = properties.getProperty("urlGetText", urlGetText);
			urlGetInfoAccount = properties.getProperty("urlGetInfoAccount", urlGetInfoAccount);
			urlUpdateAccount = properties.getProperty("urlUpdateAccount", urlUpdateAccount);
			
			urlRegisterAccountIos = properties.getProperty("urlRegisterAccountIos", urlRegisterAccountIos);
			urlLoginAccountIos = properties.getProperty("urlLoginAccountIos", urlLoginAccountIos);
			portService = getIntProperty("portService", portService);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static int getIntProperty(String propName, int defaultValue) {
		return Integer.parseInt(properties.getProperty(propName, Integer.toString(defaultValue)));
	}

	static boolean getBoolProperty(String propName, boolean defaultValue) {
		if (properties.getProperty(propName).equalsIgnoreCase("true"))
			return true;
		else if (properties.getProperty(propName).equalsIgnoreCase("false"))
			return false;
		else
			return defaultValue;
	}

}
