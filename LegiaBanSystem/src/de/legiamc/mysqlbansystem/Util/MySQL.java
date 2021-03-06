package de.legiamc.mysqlbansystem.Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MySQL {

	public static String username;
	public static String passwort;
	public static String host;
	public static String port;
	public static String database;
	
	public static String TABEL_BANNEDPLAYERS = "BannedPlayers";
	public static String TABEL_WARNEDPLAYERS = "WarnedPlayers";
	
	public static Connection con;
	
	private static ExecutorService executor;
	
	static {
		executor = Executors.newCachedThreadPool();
	}
	
	// Connect to MySQL
	public static void connect() {
		try {
			con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, passwort);
			if(isConnected()) {
				setup();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// Disconnect from MySQL
	public static void diconnect() {
		try {
			if(isConnected()) {
				con.close();
				con = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// Test connection
	public static boolean isConnected() {
		try {
			return con !=null && con.isValid(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	// Setup Tabels
	private static void setup() {
		if(isConnected()) {
			executor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						{
							String qry = "CREATE TABLE IF NOT EXISTS "+ MySQL.TABEL_BANNEDPLAYERS +" (id INT auto_increment, playername TEXT, uuid TEXT, end TEXT, reason TEXT, PRIMARY KEY(id))";
							PreparedStatement stmt;
							stmt = con.prepareStatement(qry);
							stmt.executeUpdate();
							stmt.close();
						}
						{
							String qry = "CREATE TABLE IF NOT EXISTS "+ MySQL.TABEL_WARNEDPLAYERS +" (id INT auto_increment, playername TEXT, uuid TEXT, warns INT, PRIMARY KEY(id))";
							PreparedStatement stmt;
							stmt = con.prepareStatement(qry);
							stmt.executeUpdate();
							stmt.close();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			});
		}
	}
	
//	// MySQL update
//	public static void update(String query) {
//		if(isConnected()) {
//			executor.execute(new Runnable() {
//				@Override
//				public void run() {
//					try {
//						PreparedStatement stmt;
//						stmt = con.prepareStatement(query);
//						stmt.executeUpdate();
//						stmt.close();
//					} catch (SQLException e) {
//						e.printStackTrace();
//					}
//				}
//			});
//		}
//	}
//	
//	// MySQL result
//	public static ResultSet getResult(String query) {
//		if(isConnected()) {
//			try {
//				return con.prepareStatement(query).executeQuery();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//		return null;
//	}
	
}
