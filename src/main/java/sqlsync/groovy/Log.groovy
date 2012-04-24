package sqlsync.groovy

class Log {
	def static logFile = new File("sqlsync.log");
	def static logTable = new Hashtable<String, Log>();
	def static verbose;
	def date = new Date();
	def static DATE_FORMAT = "yyyy-MM-dd-HH:mm:ss";
	def className;

	private Log() {
		if(!logFile.exists()) {
			logFile.createNewFile();
		}
	}

	public static getInstance(inputClass) {
		def logger;
		if(logTable.get(inputClass.getClass().getName()) == null) {
			logger = new Log(className:inputClass.getClass().getName());
		}
		return logger;
	}

	public void info(info) {
		def logString = "[" + date.format(DATE_FORMAT) + "][" + className + "]: " + info + "\n";
		logFile << logString;
		if(verbose) {
			println info;
		}
	}

	public void error(error) {
		this.error(error, null);
	}

	public void error(error, Exception e) {
		def errorString = "[" + date.format(DATE_FORMAT) + "][" + className + "]: " + error + "\n";
		logFile << errorString;
		if(e != null) {
			def stackTrace = "[" + date.format(DATE_FORMAT) + "][" + className + "]: " + e.getMessage() + "\n";
			logFile << stackTrace;
		}
		if(verbose) {
			println error;
		}
	}
}
