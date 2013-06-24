package se.comeon.sqlsync;

public class SqlSyncDatabaseItem {

	private final String dbHost;
	private final String dbName;
	private final String dbUser;
	private final String dbPassword;
	private final String scriptFilePath;
	private final String executeScript;
	private final String verbose;
	private final String environment;

	public SqlSyncDatabaseItem(String dbHost, String dbName, String dbUser, String dbPassword, String scriptFilePath, String executeScript, String verbose,
			String environment) {
		this.dbHost = dbHost;
		this.dbName = dbName;
		this.dbUser = dbUser;
		this.dbPassword = dbPassword;
		this.scriptFilePath = scriptFilePath;
		this.executeScript = executeScript;
		this.verbose = verbose;
		this.environment = environment;
	}

	public String getDbHost() {
		return dbHost;
	}

	public String getDbName() {
		return dbName;
	}

	public String getDbUser() {
		return dbUser;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public String getScriptFilePath() {
		return scriptFilePath;
	}

	public String getExecuteScript() {
		return executeScript;
	}

	public String getVerbose() {
		return verbose;
	}

	public String getEnvironment() {
		return environment;
	}
}