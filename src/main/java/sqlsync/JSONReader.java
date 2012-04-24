package sqlsync;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import argo.jdom.JdomParser;
import argo.jdom.JsonNode;
import argo.jdom.JsonRootNode;
import argo.saj.InvalidSyntaxException;

public class JSONReader {

	private static final JdomParser JDOM_PARSER = new JdomParser();
	private Reader jsonReader;

	/**
	 * Default constructor
	 * 
	 * @throws FileNotFoundException
	 */
	public JSONReader(String jsonTextString) throws FileNotFoundException {
		FileReader fileReader = new FileReader(jsonTextString);
		this.jsonReader = new BufferedReader(fileReader);
	}

	protected List<SqlSyncDatabaseItem> getSqlSyncDatabaseItemList() throws IOException, InvalidSyntaxException {
		ArrayList<SqlSyncDatabaseItem> returnList = new ArrayList<SqlSyncDatabaseItem>();
		JsonRootNode json = JDOM_PARSER.parse(jsonReader);
		String verbose = json.getStringValue("verbose");
		for (JsonNode arrayNode : json.getArrayNode("syncList")) {
			String dbHost = arrayNode.getStringValue("dbHost");
			String dbName = arrayNode.getStringValue("dbName");
			String dbUser = arrayNode.getStringValue("dbUser");
			String dbPassword = arrayNode.getStringValue("dbPassword");
			String scriptFilePath = arrayNode.getStringValue("scriptFilePath");
			String executeScript = arrayNode.getStringValue("executeScript");
			returnList.add(new SqlSyncDatabaseItem(dbHost, dbName, dbUser, dbPassword, scriptFilePath, executeScript,
					verbose));
		}
		return returnList;
	}
}
