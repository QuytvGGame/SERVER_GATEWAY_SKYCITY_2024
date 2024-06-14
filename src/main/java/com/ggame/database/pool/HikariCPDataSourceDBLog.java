package com.ggame.database.pool;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class HikariCPDataSourceDBLog {
	private static HikariConfig config = new HikariConfig();

	private static HikariDataSource ds;

	static {
		config.setDriverClassName(DBConfiguration.DB_LOG_DRIVER);
		config.setJdbcUrl(DBConfiguration.DB_LOG_CONNECTION_URL);
		config.setUsername(DBConfiguration.DB_LOG_USER_NAME);
		config.setPassword(DBConfiguration.DB_LOG_PASSWORD);
		config.setMinimumIdle(DBConfiguration.DB_LOG_MIN_CONNECTIONS);
		config.setMaximumPoolSize(DBConfiguration.DB_LOG_MAX_CONNECTIONS);
		// Some additional properties
		config.addDataSourceProperty("cachePrepStmts", "true");
		config.addDataSourceProperty("prepStmtCacheSize", "250");
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		ds = new HikariDataSource(config);
	}

	private HikariCPDataSourceDBLog() {
		super();
	}

	public static Connection getConnection() throws SQLException {
		logPoolStatus();
		return ds.getConnection();
	}

	public synchronized static void logPoolStatus() throws SQLException {
		System.out.println("DB_LOG + NumberConnections: " + ds.getMaximumPoolSize() + " + NumberIdleConnections: "
				+ ds.getMinimumIdle());
	}
}
