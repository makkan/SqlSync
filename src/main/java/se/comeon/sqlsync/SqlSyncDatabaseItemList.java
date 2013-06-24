package se.comeon.sqlsync;

import java.util.List;

public class SqlSyncDatabaseItemList {

	private final List<SqlSyncDatabaseItem> dbList;

	public SqlSyncDatabaseItemList(List<SqlSyncDatabaseItem> dbList) {
		this.dbList = dbList;
	}

	public List<SqlSyncDatabaseItem> getDbList() {
		return dbList;
	}
}