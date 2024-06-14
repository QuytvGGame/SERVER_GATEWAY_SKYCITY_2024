package com.ggame.database.pool;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class C3P0DataSourceDBDefault {

	private static ComboPooledDataSource cpds = new ComboPooledDataSource();

	static {
		try {
			cpds.setDriverClass(DBConfiguration.DB_MAIN_DRIVER);
			cpds.setJdbcUrl(DBConfiguration.DB_MAIN_CONNECTION_URL);
			cpds.setUser(DBConfiguration.DB_MAIN_USER_NAME);
			cpds.setPassword(DBConfiguration.DB_MAIN_PASSWORD);
			cpds.setMinPoolSize(DBConfiguration.DB_MAIN_MIN_CONNECTIONS);
			cpds.setInitialPoolSize(DBConfiguration.DB_MAIN_MIN_CONNECTIONS);
//			cpds.setAcquireIncrement(5);
			cpds.setMaxPoolSize(DBConfiguration.DB_MAIN_MAX_CONNECTIONS);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
	}

	private C3P0DataSourceDBDefault() {
		super();
	}

	public static Connection getConnection(String task) throws SQLException {
		System.out.println("-- Getting connection for task " + task);
		logPoolStatus(task);
		return cpds.getConnection();
	}

	public synchronized static void logPoolStatus(String task) throws SQLException {
		System.out.println("-Received connection for task " + task);
		System.out.println(" + NumberConnections: " + cpds.getNumConnections() + " NumberConnectionsIdle: "
				+ cpds.getNumIdleConnections() + " NumberConnectionsBusy: " + cpds.getNumBusyConnections());
	}
}
