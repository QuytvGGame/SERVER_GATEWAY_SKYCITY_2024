package com.ggame.database.pool;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class C3P0DataSourceDBLog {
	private static ComboPooledDataSource cpds = new ComboPooledDataSource();

	static {
		try {
			cpds.setDriverClass(DBConfiguration.DB_LOG_DRIVER);
			cpds.setJdbcUrl(DBConfiguration.DB_LOG_CONNECTION_URL);
			cpds.setUser(DBConfiguration.DB_LOG_USER_NAME);
			cpds.setPassword(DBConfiguration.DB_LOG_PASSWORD);
			cpds.setMinPoolSize(DBConfiguration.DB_LOG_MIN_CONNECTIONS);
			cpds.setInitialPoolSize(DBConfiguration.DB_LOG_MIN_CONNECTIONS);
//			cpds.setAcquireIncrement(5);
			cpds.setMaxPoolSize(DBConfiguration.DB_LOG_MAX_CONNECTIONS);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
	}

	private C3P0DataSourceDBLog() {
		super();
	}

	public static Connection getConnection() throws SQLException {
		logPoolStatus();
		return cpds.getConnection();
	}

	public synchronized static void logPoolStatus() throws SQLException {
		System.out.println("DB_LOG + NumberConnections: " + cpds.getNumConnections() + " NumberConnectionsIdle: "
				+ cpds.getNumIdleConnections() + " NumberConnectionsBusy: " + cpds.getNumBusyConnections());
	}
}
