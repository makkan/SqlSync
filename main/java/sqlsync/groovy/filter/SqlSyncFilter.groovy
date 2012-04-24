package se.comeon.sqlsync.groovy.filter

import se.comeon.sqlsync.groovy.ISql;
import se.comeon.sqlsync.groovy.transaction.SqlSyncItem

class SqlSyncFilter {
	def scriptDir;
	def static SCRIPT_SUFFIX = ".sql";
	def static SCRIPT_SUFFIX_REGEXP = "\\.sql";
	def checkSql = "SELECT COUNT(*) FROM version_history WHERE id = "
	def syncList;

	def filterSqlFile = { file ->
		if(file.getName().endsWith(SCRIPT_SUFFIX) && !scriptInDb(file.getName().replaceAll(SCRIPT_SUFFIX_REGEXP, ""))) {
			return true;
		}
	};

	def scriptInDb = {scriptNr ->
		def count = 0;
		ISql.newInstance() { con ->
			con.eachRow(checkSql + scriptNr) { row ->
				count = row.getAt(0);
			};
		}
		return count > 0 ? true : false;
	};

	public void execute() {
		def scriptDir = new File(scriptDir);
		syncList = [];
		if(scriptDir.exists() && scriptDir.isDirectory()) {
			scriptDir.traverse(maxDepth:0) {
				if(filterSqlFile(it)) {
					syncList.add(new SqlSyncItem(scriptFile:it,versionNumber:Long.valueOf(it.getName().replaceAll(SCRIPT_SUFFIX_REGEXP, "")).longValue()));
				}
			};
		}
	}

	public List<SqlSyncItem> getSyncList() {
		return syncList.sort{ it.versionNumber };
	}
}
