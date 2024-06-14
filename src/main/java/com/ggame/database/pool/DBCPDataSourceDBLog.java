package com.ggame.database.pool;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;

public class DBCPDataSourceDBLog {
	private static BasicDataSource ds = new BasicDataSource();

	static {
		ds.setDriverClassName(DBConfiguration.DB_LOG_DRIVER);
		ds.setUrl(DBConfiguration.DB_LOG_CONNECTION_URL);
		ds.setUsername(DBConfiguration.DB_LOG_USER_NAME);
		ds.setPassword(DBConfiguration.DB_LOG_PASSWORD);
		ds.setMinIdle(DBConfiguration.DB_LOG_MIN_CONNECTIONS);
		ds.setInitialSize(DBConfiguration.DB_LOG_MIN_CONNECTIONS);
		ds.setMaxIdle(DBConfiguration.DB_LOG_MAX_CONNECTIONS);
		ds.setMaxOpenPreparedStatements(100);
	}

	private DBCPDataSourceDBLog() {
		super();
	}

	public static Connection getConnection() throws SQLException {
		logPoolStatus();
		return ds.getConnection();
	}

	public synchronized static void logPoolStatus() throws SQLException {
		System.out.println("DB_LOG + NumberConnections: " + ds.getMaxTotal() + " + Num of Idle Connections: "
				+ ds.getNumIdle() + " + NumberConnectionsBusy: " + ds.getNumActive());
	}
}
