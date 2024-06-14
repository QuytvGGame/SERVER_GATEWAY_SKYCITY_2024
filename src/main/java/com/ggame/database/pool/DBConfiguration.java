package com.ggame.database.pool;

public class DBConfiguration {
	// MAIN_DB
	public static String DB_MAIN_DRIVER = "";
	public static String DB_MAIN_CONNECTION_URL = "";
	public static String DB_MAIN_USER_NAME = "";
	public static String DB_MAIN_PASSWORD = "";
	public static final int DB_MAIN_MIN_CONNECTIONS = 5;
	public static final int DB_MAIN_MAX_CONNECTIONS = 20;

	// LOGS_DB
	public static String DB_LOG_DRIVER = "";
	public static String DB_LOG_CONNECTION_URL = "";
	public static String DB_LOG_USER_NAME = "";
	public static String DB_LOG_PASSWORD = "";
	public static final int DB_LOG_MIN_CONNECTIONS = 5;
	public static final int DB_LOG_MAX_CONNECTIONS = 20;
}
