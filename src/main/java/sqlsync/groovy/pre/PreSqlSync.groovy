package sqlsync.groovy.pre

import sqlsync.groovy.ISql;
import sqlsync.groovy.Log;

class PreSqlSync {
	def log = Log.getInstance(this);
	def static VERSION_TABLE = "version_history";
	def static CREATE_TABLE_VERSION_TABLE = "CREATE TABLE version_history(id INT, sync_date DATETIME, PRIMARY KEY (id))";

	public void execute() {
		def createTable = true;
		ISql.newInstance() { con ->
			con.eachRow("SHOW TABLES") { row ->
				if(row.getAt(0).equals(VERSION_TABLE)) {
					createTable = false;
				}
			}
		}
		if(createTable) {
			log.info("Version_history doesn't exist, will create it.");
			def sqlOk = ISql.newInstance() { con ->
				def commit;
				con.withTransaction {
					con.execute(CREATE_TABLE_VERSION_TABLE);
					try {
						con.commit();
						commit = true;
					} catch (Exception e) {
						con.rollback;
						commit = false;
					}
				}
				return commit;
			}
			if(sqlOk) {
				log.info("Version_history created.");
			} else {
				log.error("Version_history could not be created.");
			}
		}
	}
}
