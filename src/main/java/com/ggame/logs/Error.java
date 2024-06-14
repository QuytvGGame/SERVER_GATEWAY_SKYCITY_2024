package com.ggame.logs;

public class Error {
	
	public static int action_pending = 99;
	public static int transaction_pending = 99;
	public static int transaction_duplicate = 4;	
	public static int transaction_not_exist = 4;
	
	public static int connect_db_error = -13;
	public static int login_success = 0;
	public static int invalid_ip = -13;
	public static int invalid_mpin = -13;
	public static int logout_success = 0;
	public static int logout_fail = -1;
	public static int session_success = 0;
	public static int invalid_session = 3;
	public static int invalid_transactionid = 3;
	public static int session_time_out = 4;
	public static int wrong_password = -1;	
	
	//-------------------------------------------//
	//decrypte exception
	public static int InvalidKeyException = -13;
	public static int InvalidKeySpecException = -13;
	public static int NoSuchAlgorithmException = -13;
	public static int NoSuchPaddingException = -13;
	public static int IllegalBlockSizeException = -13;
	public static int BadPaddingException = -13;
	public static int NoSuchProviderException = -13;	
	public static int UnsupportedEncodingException = -13; 
	//--------------------------------------------//
	public static int RemoteException = -11;	
	public static int AxisFault = -11;
	public static int BadCard_Data = -11;
	
	public static int SQLException = -13;
	
	public static int MalformedURLException = -14;
	
//	public static int Invalid_Param = -13;
//	private static rootConfig root = new rootConfig();
//	private static Properties properties = new Properties();
//	public static void loadProperties() throws IOException {
//
//		String fileName = root.getRoot() + "/config/error.cfg";
//		try
//		{
//			FileInputStream propsFile = new FileInputStream(fileName);
//			properties.load(propsFile);
//			propsFile.close();
//
//			connect_db_error = getIntProperty("db_driver", connect_db_error);
//			login_success = getIntProperty("login_success", login_success);	
//			invalid_ip = getIntProperty("invalid_ip", invalid_ip);
//			invalid_mpin = getIntProperty("invalid_mpin", invalid_mpin);
//			
//			logout_success = getIntProperty("logout_success", logout_success);			
//			logout_fail = getIntProperty("logout_fail", logout_fail);
//			
//			invalid_session = getIntProperty("invalid_session", invalid_session);
//			session_time_out = getIntProperty("session_time_out", session_time_out);
//			wrong_password = getIntProperty("wrong_password", wrong_password);
//			invalid_transactionid = getIntProperty("invalid_transactionid", invalid_transactionid);
//			InvalidKeyException = getIntProperty("InvalidKeyException", InvalidKeyException);
//			InvalidKeySpecException = getIntProperty("InvalidKeySpecException", InvalidKeySpecException);
//			NoSuchAlgorithmException = getIntProperty("NoSuchAlgorithmException", NoSuchAlgorithmException);
//			NoSuchPaddingException = getIntProperty("NoSuchPaddingException", NoSuchPaddingException);
//			IllegalBlockSizeException = getIntProperty("IllegalBlockSizeException", IllegalBlockSizeException);
//			BadPaddingException = getIntProperty("BadPaddingException", BadPaddingException);
//			NoSuchProviderException = getIntProperty("NoSuchProviderException", NoSuchProviderException);
//			UnsupportedEncodingException = getIntProperty("UnsupportedEncodingException", UnsupportedEncodingException);
//			SQLException = getIntProperty("SQLException", SQLException);
//			RemoteException = getIntProperty("RemoteException", RemoteException);
//			AxisFault = getIntProperty("AxisFault", AxisFault);
//			action_pending = getIntProperty("pending_action", action_pending);
//			transaction_pending = getIntProperty("pending_transaction", transaction_pending);
//			transaction_duplicate = getIntProperty("transaction_duplicate", transaction_duplicate);
//			transaction_not_exist = getIntProperty("transaction_not_exist", transaction_not_exist);
//			BadCard_Data = getIntProperty("BadCard_Data", BadCard_Data);
//			
//			Invalid_Param = getIntProperty("Invalid_Param", Invalid_Param);
//			MalformedURLException = getIntProperty("MalformedURLException", MalformedURLException);
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
//
//	static int getIntProperty(String propName, int defaultValue) {
//		return Integer.parseInt(properties.getProperty(propName, Integer
//				.toString(defaultValue)));
//	}
//	
//
//	static boolean getBoolProperty(String propName, boolean defaultValue) {
//		if (properties.getProperty(propName).equalsIgnoreCase("true")) return true;
//		else if (properties.getProperty(propName).equalsIgnoreCase("false")) return false;
//		else return defaultValue;
//	}
//
//	static {
//		try {
//			loadProperties();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
}
