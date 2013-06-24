package se.comeon.sqlsync;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;

import java.io.File;
import java.io.IOException;

import org.codehaus.groovy.control.CompilationFailedException;

public class GroovyRunner {

	private static final String DEFAULT_RUN_METHOD = "execute";
	private static final String ENVIRONMENT_DEPENDENT_RUN_METHOD = "executeEnvironmentDependentScripts";

	/**
	 * Default constructor
	 */
	public GroovyRunner() {
	}

	/**
	 * Will run the groovy script within this package
	 * 
	 * @param groovyFile
	 * @param sqlSyncDatabaseItem
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws CompilationFailedException
	 * @throws IOException
	 */
	public void runGroovy(final File groovyFile, SqlSyncDatabaseItem sqlSyncDatabaseItem) throws InstantiationException, IllegalAccessException,
			CompilationFailedException, IOException {
		ClassLoader parent = getClass().getClassLoader();
		GroovyClassLoader loader = new GroovyClassLoader(parent);
		Class<?> groovyClass = loader.parseClass(groovyFile);
		GroovyObject groovyObject = (GroovyObject) groovyClass.newInstance();
		String[] args = new String[8];
		args[0] = sqlSyncDatabaseItem.getDbHost();
		args[1] = sqlSyncDatabaseItem.getDbName();
		args[2] = sqlSyncDatabaseItem.getDbUser();
		args[3] = sqlSyncDatabaseItem.getDbPassword();
		args[4] = sqlSyncDatabaseItem.getScriptFilePath();
		args[5] = sqlSyncDatabaseItem.getExecuteScript();
		args[6] = sqlSyncDatabaseItem.getVerbose();
		args[7] = sqlSyncDatabaseItem.getEnvironment();
		groovyObject.invokeMethod(DEFAULT_RUN_METHOD, args);
		groovyObject.invokeMethod(ENVIRONMENT_DEPENDENT_RUN_METHOD, args);
		loader.close();
	}
}