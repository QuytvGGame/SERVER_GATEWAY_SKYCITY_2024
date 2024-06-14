package com.ggame.database.pool;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class HikariCPDataSourceDBDefault {
	private static HikariConfig config = new HikariConfig();

	private static HikariDataSource ds;

	static {
		config.setDriverClassName(DBConfiguration.DB_MAIN_DRIVER);
		config.setJdbcUrl(DBConfiguration.DB_MAIN_CONNECTION_URL);
		config.setUsername(DBConfiguration.DB_MAIN_USER_NAME);
		config.setPassword(DBConfiguration.DB_MAIN_PASSWORD);
		config.setMinimumIdle(DBConfiguration.DB_MAIN_MIN_CONNECTIONS);
		config.setMaximumPoolSize(DBConfiguration.DB_MAIN_MAX_CONNECTIONS);
		// Some additional properties
		config.addDataSourceProperty("cachePrepStmts", "true");
		config.addDataSourceProperty("prepStmtCacheSize", "250");
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		ds = new HikariDataSource(config);
	}

	private HikariCPDataSourceDBDefault() {
		super();
	}

	public static Connection getConnection(String task) throws SQLException {
		System.out.println("-- Getting connection for task " + task);
		logPoolStatus(task);
		return ds.getConnection();
	}

	public synchronized static void logPoolStatus(String task) throws SQLException {
		System.out.println("-Received connection for task " + task + " + NumberConnections: " + ds.getMaximumPoolSize()
				+ " + NumberIdleConnections: " + ds.getMinimumIdle());
	}
}
