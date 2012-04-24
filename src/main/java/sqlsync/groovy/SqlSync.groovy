package se.comeon.sqlsync.groovy

import se.comeon.sqlsync.groovy.api.SqlSyncExecute;
import se.comeon.sqlsync.groovy.filter.SqlSyncFilter;
import se.comeon.sqlsync.groovy.post.PostSqlSync;
import se.comeon.sqlsync.groovy.pre.PreSqlSync;
import se.comeon.sqlsync.groovy.transaction.SqlSyncTransaction;

class SqlSync implements SqlSyncExecute {
	def log = Log.getInstance(this);

	public void execute(String[] args) {
		SqlSyncTransaction sqlSyncTransaction;
		def executeScript;
		if(args[5] != "") {
			executeScript = true;
			sqlSyncTransaction = new SqlSyncTransaction(executeScript:executeScript);
		} else {
			executeScript = false;
			sqlSyncTransaction = new SqlSyncTransaction(executeScript:executeScript);
		}
		if(args[6] != "") {
			log.verbose = true;
		}
		def iSql = new ISql(dbHost:args[0], dbName:args[1], dbUser:args[2], dbPassword:args[3]);

		log.info("SQLSync Start" + ISql.printString());
		PreSqlSync preSqlSync = new PreSqlSync();
		preSqlSync.execute();

		SqlSyncFilter sqlSyncFilter = new SqlSyncFilter(scriptDir:args[4]);
		sqlSyncFilter.execute();
		def syncList = sqlSyncFilter.getSyncList();
		if(!syncList.empty) {
			syncList.each {
				sqlSyncTransaction.executeScript(it);
			};

			PostSqlSync postSqlSync = new PostSqlSync();
			postSqlSync.execute(syncList, executeScript);
		} else {
			log.info("Nothing to sync" + ISql.printString());
		}
		log.info("SQLSync Completed" + ISql.printString());
	}
}
