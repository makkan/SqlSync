package se.comeon.sqlsync;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.codehaus.groovy.control.CompilationFailedException;

public class SqlSync {

	private static final String GROOVY_RUN_FILE_ECLIPSE = "src/main/java/se/comeon/sqlsync/groovy/SqlSync.groovy";
	private static final String GROOVY_RUN_FILE = "se/comeon/sqlsync/groovy/SqlSync.groovy";

	public static void main(String[] args) {
		if (args.length != 1) {
			printUsage();
			System.exit(-1);
		}
		GroovyRunner groovyRunner = new GroovyRunner();
		File sqlSync = new File(GROOVY_RUN_FILE);
		if (!sqlSync.exists()) {
			sqlSync = new File(GROOVY_RUN_FILE_ECLIPSE);
		}
		try {
			JSONReader jsonReader = new JSONReader(args[0]);
			List<SqlSyncDatabaseItem> syncList = jsonReader.getSqlSyncDatabaseItemList();
			for (SqlSyncDatabaseItem sqlSyncDatabaseItem : syncList) {
				groovyRunner.runGroovy(sqlSync, sqlSyncDatabaseItem);
			}
		} catch (CompilationFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public static void printUsage() {
		System.out.println("Usage: java -jar SqlSync.jar <sqlsync.json>");
	}
}
