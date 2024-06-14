 package com.ggame.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.galaxy.framework.server.util.AppServer;
import com.sgc.game.server.NetworkServer;

public class MysqlConnect {
	// init database constants
    private static String DATABASE_DRIVER = "com.mysql.jdbc.Driver";
    private static String DATABASE_URL = "jdbc:mysql://localhost:3306/database_name";
    private static String USERNAME = "root";
    private static String PASSWORD = "";
    private static String MAX_POOL = "250";
    
 // init connection object
    private Connection connection;
    // init properties object
    private Properties properties;

    private void init(String db){
		String DefaultDatabase = AppServer.getParam("DefaultDatabase");
		String rootDB = "Connection." + DefaultDatabase;
		DATABASE_DRIVER = AppServer.getParam(rootDB +".DriverClass");
		DATABASE_URL = AppServer.getParam(rootDB +".Url");
		USERNAME = AppServer.getParam(rootDB +".UserName");
		PASSWORD = AppServer.getParam(rootDB +".Password");
		MAX_POOL = AppServer.getParam(rootDB +".MaxActive");
    }
    // create properties
    private Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
            properties.setProperty("user", USERNAME);
            properties.setProperty("password", PASSWORD);
            properties.setProperty("MaxPooledStatements", MAX_POOL);
        }
        return properties;
    }

    // connect database
    public Connection connect(String db) {
        if (connection == null) {
            try {
            	init(db);
                Class.forName(DATABASE_DRIVER);
                connection = DriverManager.getConnection(DATABASE_URL, getProperties());
                System.out.println("DATABASE_URL="+DATABASE_URL);
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    // disconnect database
    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void main(String[] a){
    	System.out.println(AppServer.getParam("Connection.DB_ALOCHOI.UserName"));
    }
}
