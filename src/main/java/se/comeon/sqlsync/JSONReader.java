package se.comeon.sqlsync;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.List;

import com.google.gson.Gson;

public class JSONReader {

	private final Gson gson = new Gson();
	private Reader jsonReader;

	public JSONReader(String jsonTextString) throws FileNotFoundException {
		FileReader fileReader = new FileReader(jsonTextString);
		this.jsonReader = new BufferedReader(fileReader);
	}

	protected List<SqlSyncDatabaseItem> getSqlSyncDatabaseItemList() {
		return gson.fromJson(jsonReader, SqlSyncDatabaseItemList.class).getDbList();
	}
}
