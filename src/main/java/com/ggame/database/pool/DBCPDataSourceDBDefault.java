package com.ggame.database.pool;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;

public class DBCPDataSourceDBDefault {

	private static BasicDataSource ds = new BasicDataSource();

	static {
		ds.setDriverClassName(DBConfiguration.DB_MAIN_DRIVER);
		ds.setUrl(DBConfiguration.DB_MAIN_CONNECTION_URL);
		ds.setUsername(DBConfiguration.DB_MAIN_USER_NAME);
		ds.setPassword(DBConfiguration.DB_MAIN_PASSWORD);
		ds.setMinIdle(DBConfiguration.DB_MAIN_MIN_CONNECTIONS);
		ds.setInitialSize(DBConfiguration.DB_MAIN_MIN_CONNECTIONS);
		ds.setMaxIdle(DBConfiguration.DB_MAIN_MAX_CONNECTIONS);
		ds.setMaxOpenPreparedStatements(100);
	}
	private DBCPDataSourceDBDefault() {
		super();
	}

	public static Connection getConnection(String task) throws SQLException {
		System.out.println("-- Getting connection for task " + task);
		logPoolStatus(task);
		return ds.getConnection();
	}

	public synchronized static void logPoolStatus(String task) throws SQLException {
		System.out.println("-Received connection for task " + task + " + NumberConnections: " + ds.getMaxTotal()
				+ " + Num of Idle Connections: " + ds.getNumIdle() + " + NumberConnectionsBusy: " + ds.getNumActive());
	}
}
