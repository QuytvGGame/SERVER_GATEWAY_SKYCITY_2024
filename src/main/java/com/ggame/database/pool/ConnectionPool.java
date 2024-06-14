package com.ggame.database.pool;

import java.sql.Connection;
import java.sql.SQLException;

import com.ggame.accountservice.AgentJava;
import com.ggame.accountservice.T;
import com.ggame.database.MysqlConnect;
import com.sgc.game.server.NetworkServer;

public class ConnectionPool {

	private static final long MAX_TIME_PROCESS_PER_CONNECT = 500L * 1000L;
	private static int _countDBMain = 0;
	private static long _lastTimeUsingMainConnect = 0L;
	private static Connection _connectDBDefault;

	public static Connection getConnection(String task) {
		try {
			if (_connectDBDefault == null || _connectDBDefault.isClosed()) {
				_connectDBDefault = new MysqlConnect().connect(T.Empty);
				_lastTimeUsingMainConnect = System.currentTimeMillis();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			_lastTimeUsingMainConnect = System.currentTimeMillis();
			return _connectDBDefault = new MysqlConnect().connect(T.Empty);
		}
		_countDBMain++;
		if ((System.currentTimeMillis() - _lastTimeUsingMainConnect) > MAX_TIME_PROCESS_PER_CONNECT) {
			try {
				_connectDBDefault.close();
				AgentJava.log("ConnectionPool.getConnection() - Close() - " + _countDBMain);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			_countDBMain = T.ZERO;
			_connectDBDefault = new MysqlConnect().connect(T.Empty);
			_lastTimeUsingMainConnect = System.currentTimeMillis();
		}
		return _connectDBDefault;
	}
}
