package se.comeon.sqlsync.groovy.transaction

import se.comeon.sqlsync.groovy.ISql;
import se.comeon.sqlsync.groovy.Log;

class SqlSyncTransaction {
	def log = Log.getInstance(this);

	def executeScript;

	def formatLine = { executeLine, line ->
		return executeLine = executeLine + "\r\n" + line;
	};

	def getExecuteList = { file ->
		def executeLine = "";
		def executeList = new ArrayList<String>();
		def delimiter = ";";
		file.eachLine { line->
			line = line.trim();
			if(line =~ /(?i)^.*delimiter/) {
				delimiter = line.replaceAll(/^.* /, "").trim();
			} else {
				if(line.endsWith(delimiter)) {
					executeLine = formatLine(executeLine, line);
					executeLine = executeLine.replace(delimiter, "");
					executeList.add(executeLine);
					executeLine = "";
				} else {
					executeLine = formatLine(executeLine, line);
				}
			}
		};
		return executeList;
	};

	def executeTransaction = { executeList, sqlSyncItem ->
		boolean returnValue = true;
		ISql.newInstance() { con ->
			con.withTransaction {
				try {
					executeList.each { con.execute(it); }
					con.commit();
				} catch (Exception e) {
					log.error("Sql error running script " + sqlSyncItem.versionNumber , e);
					returnValue = false;
					sqlSyncItem.error = true;
					sqlSyncItem.exception = e;
					con.rollback();
				}
			};
		};
		return returnValue;
	};

	public boolean executeScript(sqlSyncItem) {
		boolean returnValue = true;
		if(executeScript) {
			log.info("Syncing script " + sqlSyncItem.versionNumber + ISql.printString());
			returnValue = executeTransaction(getExecuteList(sqlSyncItem.scriptFile), sqlSyncItem);
		}
		return returnValue;
	}
}
