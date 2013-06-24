package se.comeon.sqlsync.groovy

import se.comeon.sqlsync.groovy.api.SqlSyncExecute;
import se.comeon.sqlsync.groovy.transaction.SqlSyncItem
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

		log.info("SQLSync Start Normal " + ISql.printString());
		PreSqlSync preSqlSync = new PreSqlSync();
		preSqlSync.execute();

		SqlSyncFilter sqlSyncFilter = new SqlSyncFilter(scriptDir:args[4]);
		sqlSyncFilter.execute();
		def syncList = sqlSyncFilter.getSyncList();
		if(!syncList.empty) {
			PostSqlSync postSqlSync = new PostSqlSync();
			def index = 0;
			def error = false;
			def tempList = [];
			while(index < syncList.size && !error) {
				if(!sqlSyncTransaction.executeScript(syncList[index])) {
					log.error("Error while executing, breaking loop");
					error = true;
				}
				postSqlSync.insertVersionHistory(syncList[index]);
				tempList.add(syncList[index]);
				index++;
			}
			postSqlSync.execute(tempList, executeScript);
			if(error) {
				sendMail(tempList);
			}
		} else {
			log.info("Nothing to sync" + ISql.printString());
		}
		log.info("SQLSync Completed Normal sync" + ISql.printString());
	}
	
	def sendMail = { syncList ->
		def port = 25;
		def host = "susevs14.cleverdolphin.se";
		log.info("Scripts are failing, sending an email to the dba's");
		def ant = new AntBuilder();
		def subject = "Scripts are failing, please check the sqlsync status!";
		ant.mail(mailhost:"$host", mailport:"$port", subject:"$subject", messagemimetype:"text/html") {
			from(address:"jenkins@cleverdolphin.se")
			to(address:"dba@comeon.com")
			message(formatMessage(syncList))
		};
	};
	
	def formatMessage = { syncList ->
		def message = new StringBuilder();
	
		message.append("<table>")
		message.append("<tr>")
		message.append("<th>Scriptnumber</th><th>Error Reason</th>");
		message.append("</tr>");
		syncList.each {
			message.append("<tr>");
			if(it.error) {
				message.append("<td>" + it.versionNumber + "</td><td>" + it.exception.getMessage() + "</td>");
			}
			message.append("</tr>");
		};
		message.append("</table>")
		return message.toString();
	};

	public void executeEnvironmentDependentScripts(String[] args) {
		if (args[7] == "") {
			log.info("SQLSync Environment dependent sync is NOT enabled. Please specify an environment and create a folder for it.");
			return;
		}
		if (!args[4].endsWith("/")) {
			args[4] += "/";
		}
		def scriptDir = new File(args[4] + args[7]);
		log.info("SQLSync Start Environment dependent sync from: "  + scriptDir);
		def syncList = [];
		if (scriptDir.exists() && scriptDir.isDirectory()) {
			scriptDir.traverse(maxDepth:0) {
				syncList.add(new SqlSyncItem(scriptFile:it,versionNumber:it.getName()));
			};
		} 
		if (syncList.empty) {
			log.info("SQLSync Completed, NO environment dependent script found in : "  + scriptDir);
			return;
		}
		def sqlSyncTransaction = new SqlSyncTransaction(executeScript:true);
		syncList.each{ e ->
 		 	if (!sqlSyncTransaction.executeScript(e)) {
				log.error("Error while executing " + e);
			}
		}
		log.info("SQLSync Completed Environment dependent synced "  + scriptDir);
	}
}