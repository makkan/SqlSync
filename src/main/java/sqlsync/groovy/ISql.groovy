package sqlsync.groovy

import java.sql.SQLException;

import groovy.sql.Sql

class ISql {
	def static log = Log.getInstance(this);

	private static String dbHost;
	private static String dbName;
	private static String dbUser;
	private static String dbPassword;
	private static int dbPort = 3306;
	private static String dbDriver = "com.mysql.jdbc.Driver";

	public static newInstance(closure) {
		def con;
		try {
			con = Sql.newInstance("jdbc:mysql://" + dbHost + ":" + dbPort +"/" + dbName + "?autoReconnect=true", dbUser,
					dbPassword, dbDriver);
			if(closure) {
				closure.call(con);
			}
		} catch(Exception e) {
			log.error("Database Error" + printString(), e);
		} finally {
			if(con != null) {
				con.close();
			}
		}
	}

	public static printString() {
		return " dbName: " + dbName + ", dbHost: " + dbHost;
	}
}