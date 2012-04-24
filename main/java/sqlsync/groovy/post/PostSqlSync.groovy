package se.comeon.sqlsync.groovy.post

import se.comeon.sqlsync.groovy.ISql;
import se.comeon.sqlsync.groovy.Log;
import se.comeon.sqlsync.groovy.transaction.SqlSyncTransaction;

import java.sql.Date;

class PostSqlSync {
	def log = Log.getInstance(this);
	def INSERT_VERSION_HISTORY = "INSERT INTO version_history (id, sync_date) VALUES(";
	def static DATE_FORMAT_BIG = "yyyy-MM-dd HH:mm:ss"
	def static DATE_FORMAT_SMALL = "yyyy-MM-dd HH:mm"
	def summaryFile = new File(ISql.dbHost + "-" + ISql.dbName + ".html");

	def logResult = { sqlSyncItem, executeScript ->
		if(executeScript) {
			if(sqlSyncItem.error) {
				log.info("Error syncing script " + sqlSyncItem.versionNumber + ISql.printString());
			} else {
				log.info("Script synced " + sqlSyncItem.versionNumber + ISql.printString());
			}
		} else {
			log.info("Script not executed " + sqlSyncItem.versionNumber + ", will just write to version_history" + ISql.printString());
		}
	};

	def insertVersionHistory = { sqlSyncItem ->
		if(!sqlSyncItem.error) {
			def syncDate =  new Date(System.currentTimeMillis());
			ISql.newInstance() { con ->
				con.execute(INSERT_VERSION_HISTORY + sqlSyncItem.versionNumber + ", '" + syncDate.format(DATE_FORMAT_BIG) + "')");
			};
		}
	};

	def initSummary = {
		def syncDate =  new Date(System.currentTimeMillis());
		def openHtml = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\"><html><head><title>SQL Sync Summary " +
				ISql.dbHost + ", " + ISql.dbName +
				"</title></head><style> body { font-family: \"Lucida Grande\", Verdana, Arial, Helvetica, sans-serif;" +
				"font-size: 12px; text-align: center;} h1 {font-size: 18px; font-weight: normal;} h2 {font-size: 16px; font-weight: normal;}" +
				"th { font-size: 13px;font-weight: bold;color: #000;margin: 0px 15px; border-bottom: 2px solid #000; padding: 4px;} " +
				"td { border-bottom: 1px solid #ccc; font-size: 12px; line-height: 14px; color: #000; padding: 6px 8px; }" +
				"tbody tr:hover td { background: #A9A9A9;} .error { color: red; } .ok { color: green; }" +
				"</style><body><h1>Sync result for dbHost: " + ISql.dbHost + ", dbName: " + ISql.dbName +
				"</h1><h2>Sync date: " + syncDate.format(DATE_FORMAT_SMALL) + "</h2>"+
				"<table align=\"center\"><tr><th>Script number</th><th>Sync result</th><th>Error reason</th></tr>";
		if(summaryFile.exists()) {
			summaryFile.delete();
		}
		summaryFile << openHtml;
	};

	def printLineSummary = { sqlSyncItem, executeScript ->
		def summaryLine;
		if(executeScript) {
			if(!sqlSyncItem.error) {
				summaryLine = "<tr><td class=\"ok\">" + sqlSyncItem.versionNumber +
						"</td><td class=\"ok\">Executed OK</td><td class=\"ok\">N/A</td></tr>";
			} else {
				summaryLine = "<tr><td class=\"error\">" + sqlSyncItem.versionNumber +
						"</td><td class=\"error\">Executed with error</td><td class=\"error\">" +
						sqlSyncItem.exception.getMessage() +"</td></tr>";
			}
		} else {
			summaryLine = "<tr><td>" + sqlSyncItem.versionNumber + "</td><td>Nothing executed</td><td>N/A</td></tr>";
		}
		summaryFile << summaryLine;
	};

	def closeSummary =  {
		def closeHtml = "</table></body>";
		summaryFile << closeHtml;
	};

	public void execute(sqlSyncList, executeTransaction) {
		initSummary();
		sqlSyncList.each {
			insertVersionHistory(it);
			logResult(it, executeTransaction);
			printLineSummary(it, executeTransaction);
		}
		closeSummary();
	}
}
