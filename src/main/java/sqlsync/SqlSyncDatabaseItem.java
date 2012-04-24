package sqlsync;

public class SqlSyncDatabaseItem {

	private String dbHost;
	private String dbName;
	private String dbUser;
	private String dbPassword;
	private String scriptFilePath;
	private String executeScript;
	private String verbose;

	public SqlSyncDatabaseItem(String dbHost, String dbName, String dbUser, String dbPassword, String scriptFilePath,
			String executeScript, String verbose) {
		this.dbHost = dbHost;
		this.dbName = dbName;
		this.dbUser = dbUser;
		this.dbPassword = dbPassword;
		this.scriptFilePath = scriptFilePath;
		this.executeScript = executeScript;
		this.verbose = verbose;
	}

	public String getDbHost() {
		return dbHost;
	}

	public void setDbHost(String dbHost) {
		this.dbHost = dbHost;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getDbUser() {
		return dbUser;
	}

	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}

	public String getScriptFilePath() {
		return scriptFilePath;
	}

	public void setScriptFilePath(String scriptFilePath) {
		this.scriptFilePath = scriptFilePath;
	}

	public String getExecuteScript() {
		return executeScript;
	}

	public void setExecuteScript(String executeScript) {
		this.executeScript = executeScript;
	}

	public String getVerbose() {
		return verbose;
	}

	public void setVerbose(String verbose) {
		this.verbose = verbose;
	}
}
